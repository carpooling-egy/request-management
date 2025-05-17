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
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class MatchingResultConsumerService {

    private final MatchingResultService matchingResultService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile JetStreamSubscription subscription;
    private volatile Connection connection;
    private volatile JetStream js;
    private final ExecutorService connectExecutor = Executors.newSingleThreadExecutor();
    private final String consumerType = System.getenv().getOrDefault("CONSUMER_TYPE", "SHIPPING");

    public MatchingResultConsumerService(MatchingResultService matchingResultService) {
        this.matchingResultService = matchingResultService;
    }

    @PostConstruct
    public void init() {
        // Kick off the connect-retry loop in the background
        connectExecutor.submit(this::connectWithRetry);
    }

    private void connectWithRetry() {
        String natsUrl = System.getenv().getOrDefault("NATS_URL", Options.DEFAULT_URL);

        Options options = new Options.Builder()
                .server(natsUrl)
                .userInfo("subscriber", "subscriberpass")
                .connectionName("matching-results-consumer")
                .maxReconnects(-1)                          // infinite reconnects after first success
                .reconnectWait(Duration.ofSeconds(2))       // 2s between reconnect attempts
                .connectionListener(this::handleEvent)
                .errorListener(new ErrorListener() {
                    @Override
                    public void errorOccurred(Connection conn, String msg) {
                        log.error("NATS error: {}", msg);
                    }
                    @Override
                    public void exceptionOccurred(Connection conn, Exception exp) {
                        log.error("NATS exception:", exp);
                    }
                    @Override
                    public void slowConsumerDetected(Connection conn, Consumer consumer) {
                        log.warn("Slow consumer on NATS at {}", conn.getConnectedUrl());
                    }
                })
                .build();

        while (true) {
            try {
                log.info("Attempting initial NATS connect to {}...", natsUrl);
                connection = Nats.connectReconnectOnConnect(options);
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
                break;  // once connected and subscribed, exit retry loop

            } catch (Exception e) {
                log.warn("NATS connect failed: {}. Retrying in 5s…", e.getMessage());
                try { Thread.sleep(5000); }
                catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void handleEvent(Connection conn, ConnectionListener.Events type) {
        switch (type) {
            case CONNECTED:
                log.info("Connected to NATS at {}", conn.getConnectedUrl());
                break;
            case RECONNECTED:
                log.info("Reconnected to NATS at {}", conn.getConnectedUrl());
                break;
            case DISCONNECTED:
                log.warn("Disconnected from NATS; auto-reconnect will retry");
                break;
            case CLOSED:
                log.warn("NATS connection closed");
                break;
            case RESUBSCRIBED:
                log.info("Resubscribed to NATS");
                break;
            default:
                log.debug("NATS event: {}", type);
        }
    }

    // Run every 3 seconds
    @Scheduled(fixedDelay = 3000)
    public void pollMessages() {
        JetStreamSubscription sub = this.subscription;
        if (sub == null) {
            // not ready yet
            return;
        }
        try {
            List<Message> messages = sub.fetch(10, Duration.ofSeconds(2));
            for (Message msg : messages) {
                try {
                    MatchingResultDTO dto = objectMapper.readValue(msg.getData(), MatchingResultDTO.class);
                    // print out the message
                    log.debug("Received message: {}", dto);

                    //matchingResultService.processMatchingResult(dto);
                    log.info("[{}] OfferID: {}, Requests: {}, Points: {}",
                            consumerType,
                            dto.getOfferId(),
                            dto.getAssignedMatchedRequests() != null ? dto.getAssignedMatchedRequests().size() : 0,
                            dto.getPath() != null ? dto.getPath().size() : 0
                    );
                } catch (Exception e) {
                    log.error("Failed to parse message: {}", e.getMessage());
                    log.debug("Raw payload: {}", new String(msg.getData()));
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
        connectExecutor.shutdownNow();
        try {
            if (subscription != null) subscription.unsubscribe();
            if (connection != null) connection.close();
        } catch (Exception e) {
            log.warn("Error during shutdown: {}", e.getMessage());
        }
    }
}
