# 📥 Request Management Service

The Request Management Service manages the complete lifecycle of ride requests and driver offers. It ensures data validation, handles persistence in the central database, and listens to asynchronous status updates through a shared messaging system.

---

## 📌 Responsibilities

- 📝 Submit a new **ride request**
- 🚗 Submit a new **driver offer**
- 🔄 Update the **status** of a ride request or offer (via message queue events)

---

## 🔁 Interactions

- 📬 **Message Queue** — Subscribes to match-related updates to modify request/offer statuses in real time
- 👤 **Profile Service** — Retrieves user **gender** to validate compatibility during request submission
- 🧭 **Route Engine** — Fetches estimated trip **time and distance** to validate trip data before storing it

---

## 📣 Maintainers

This service is part of the **3alsekka Carpooling System** (Graduation Project - Alexandria University, 2025).
