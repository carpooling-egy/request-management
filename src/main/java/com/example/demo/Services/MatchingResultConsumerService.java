package com.example.demo.Services;

import com.example.demo.DTOs.MatchingResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.*;
import io.nats.client.api.ConsumerInfo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class MatchingResultConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private JetStreamSubscription subscription;
    private Connection connection;
    private JetStream js;
    private final String consumerType = System.getenv().getOrDefault("CONSUMER_TYPE", "SHIPPING");

    @PostConstruct
    public void init() throws Exception {
        String natsUrl = System.getenv().getOrDefault("NATS_URL", Options.DEFAULT_URL);

        Options options = new Options.Builder()
                .server(natsUrl)
                .userInfo("subscriber", "subscriberpass")
                .connectionName("matching-results-consumer")
                .maxReconnects(-1)
                .reconnectWait(Duration.ofSeconds(1))
                .connectionListener((conn, type) -> {
                    switch (type) {
                        case CONNECTED:
                            log.info("Connected to NATS at {}", conn.getConnectedUrl());
                            break;
                        case RECONNECTED:
                            log.info("Reconnected to NATS at {}", conn.getConnectedUrl());
                            break;
                        case DISCONNECTED:
                            log.warn("Disconnected from NATS. Attempting reconnect...");
                            break;
                        case CLOSED:
                            log.warn("Connection to NATS closed.");
                            break;
                        case RESUBSCRIBED:
                            log.info("Resubscribed to NATS.");
                            break;
                        default:
                            log.info("NATS connection event: {}", type);
                    }
                })
                .errorListener(new ErrorListener() {
                    @Override
                    public void errorOccurred(Connection conn, String msg) {
                        log.error("NATS error occurred: {}", msg);
                    }

                    @Override
                    public void exceptionOccurred(Connection conn, Exception exp) {
                        log.error("NATS exception occurred", exp);
                    }

                    @Override
                    public void slowConsumerDetected(Connection conn, Consumer consumer) {
                        log.warn("Slow consumer detected on connection to NATS at: {}", conn.getConnectedUrl());
                    }

                })
                .build();


        connection = Nats.connect(options);
        js = connection.jetStream();
        JetStreamManagement jsm = connection.jetStreamManagement();

        ConsumerInfo info = jsm.getConsumerInfo("MATCHED_REQUESTS", "MATCHED_REQUESTS_PROCESSOR");
        log.info("Connected to NATS. Consumer: {}", info.getName());

        PullSubscribeOptions pullOpts = PullSubscribeOptions.builder()
                .stream("MATCHED_REQUESTS")
                .durable("MATCHED_REQUESTS_PROCESSOR")
                .build();

        subscription = js.subscribe(null, pullOpts);
        log.info("Started {} consumer for matching results...", consumerType);
    }

    // Run every 3 seconds
    @Scheduled(fixedDelay = 3000)
    public void pollMessages() {
        if (subscription == null) return;

        try {
            List<Message> messages = subscription.fetch(10, Duration.ofSeconds(2));
            for (Message msg : messages) {
                try {
                    MatchingResultDTO dto = objectMapper.readValue(msg.getData(), MatchingResultDTO.class);
                    log.info("[{}] OfferID: {}, Requests: {}, Points: {}",
                            consumerType,
                            dto.getOfferId(),
                            dto.getAssignedMatchedRequests() != null ? dto.getAssignedMatchedRequests().size() : 0,
                            dto.getPath() != null ? dto.getPath().size() : 0
                    );
                } catch (Exception e) {
                    log.error("Failed to parse message: {}", e.getMessage());
                    log.debug("Raw: {}", new String(msg.getData()));
                } finally {
                    msg.ack();
                }
            }
        } catch (Exception e) {
            log.warn("Error while polling messages: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down NATS consumer...");
        try {
            if (subscription != null) subscription.unsubscribe();
            if (connection != null) connection.close();
        } catch (Exception e) {
            log.warn("Error during shutdown: {}", e.getMessage());
        }
    }
}
