# BelAirLine Backend

- **Student:** Nizeyimana Kevine
- **Student ID:** 26859
- **Project:** BelAirLine (INSY 8322 Web Technology and Internet, mid-term)
- **Stack:** Spring Boot 3, Spring Web, Spring Data JPA, PostgreSQL

BelAirLine is an airline booking API. A passenger makes a booking for a travel date, a booking agent at a sales desk handles it, and once the booking is TICKETED the agent can add ancillary purchases (extra baggage, lounge access).

## Steps to run the API

1. Install Java 17 or newer, Maven, PostgreSQL and Postman.
2. Create the database:
   ```sql
   CREATE DATABASE belairline;
   ```
3. Open `src/main/resources/application.properties` and set your PostgreSQL username and password.
4. Start the API from the project folder:
   ```bash
   mvn spring-boot:run
   ```
   The API runs on `http://localhost:8080`. Hibernate creates the tables (`ddl-auto=update`) and prints every SQL query (`show-sql=true`).
5. In Postman, import `bel-air-line-midterm.postman_collection.json`.
6. Run the folder **Step 0 - Seed data** once on an empty database. It enters the seed data through the API's own endpoints and stores the ids in collection variables.
7. Run the requirement folders (A2, B, C, D, E, F, Bonus). Each request is named by its requirement ID.

## Entities

| Entity | Table | Relationships |
|---|---|---|
| Route | route | `@ManyToMany(mappedBy = "routes")` agents, `@JsonIgnore` |
| SalesDesk | sales_desk | none |
| BookingAgent | booking_agent | `@ManyToOne` salesDesk (`sales_desk_id`), `@ManyToMany` routes (join table `booking_agent_route`) |
| Passenger | passenger | none, `passportNumber` is unique |
| Booking | booking | `@ManyToOne` passenger (`passenger_id`), `@ManyToOne` agent (`agent_id`), status stored as STRING |
| AncillaryPurchase | ancillary_purchase | `@ManyToOne` booking (`booking_id`) |

## Endpoints

| ID | Method and path |
|---|---|
| A2 | `POST /api/{path}/save`, `GET /api/{path}/all`, `GET /api/{path}/{id}`, `PUT /api/{path}/update/{id}`, `DELETE /api/{path}/delete/{id}` for routes, sales-desks, booking-agents, passengers, bookings |
| B1 | `GET /api/passengers/by-last-name?lastName=ingabire` |
| B2 | `GET /api/bookings/by-status?status=RESERVED` |
| B3 | `GET /api/bookings/between?start=2026-10-01&end=2026-10-31` |
| C1 | `GET /api/booking-agents/by-route?name=nairobi` |
| C2 | `GET /api/sales-desks/free` |
| C3 | `GET /api/routes/unused` |
| C4 | `GET /api/passengers/of-booking-agent/{bookingAgentId}` |
| D1 | `POST /api/bookings/save` |
| D2 | `PATCH /api/bookings/{id}/status?status=PAID` |
| D3 | `PUT /api/booking-agents/{bookingAgentId}/routes/{routeId}` |
| D4 | `PATCH /api/booking-agents/{bookingAgentId}/sales-desk?deskNumber=4` |
| E1 | `POST /api/ancillary-purchases/save?bookingId=...` |
| E2 | `GET /api/ancillary-purchases/passenger/{passportNumber}` |
| E3 | `DELETE /api/ancillary-purchases/delete/{id}` and `DELETE /api/bookings/delete/{id}` |
| F1 | `GET /api/bookings/stats/by-status` |
| F2 | `PATCH /api/bookings/cancel-day?bookingAgentId=...&date=2026-10-20` |
| Bonus | `GET /api/dashboard/summary`, `GET /api/bookings/page?page=0&size=5&sort=travelDate,desc` |
