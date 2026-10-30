# Demo notes – one or two sentences per requirement

Not required by the paper. Read these, open the matching file, and make sure you can say them in your own words.

**A1** – Every entity has a UUID id from `@GeneratedValue(strategy = GenerationType.UUID)`. BookingAgent owns the many-to-many with `@JoinTable(name = "booking_agent_route")`; Route has `mappedBy = "routes"` and `@JsonIgnore` so the JSON does not loop.

**A2** – Each controller calls its service; the service returns a String message, a List or an Optional; the controller turns the message into an HttpStatus with if/else (201 for save, 404 for "does not exist", 200 otherwise).

**B1** – `findByLastNameIgnoreCaseOrderByFirstNameAsc`: Spring builds the SQL from the method name; IgnoreCase makes "ingabire" match "Ingabire".
**B2** – `findByStatusOrderByTravelDateAsc(BookingStatus status)`: Spring turns the text RESERVED into the enum automatically.
**B3** – The controller does `LocalDate.parse(start)`; `findByTravelDateBetween...` uses SQL BETWEEN, which includes both ends.

**C1** – JPQL `JOIN a.routes r` joins through the join table; `LOWER(...) LIKE` matches "nairobi" in "Kigali - Nairobi" in any case.
**C2** – `NOT EXISTS` subquery: keep a desk only when no BookingAgent has `salesDesk` equal to it.
**C3** – `WHERE r.agents IS EMPTY` checks that the collection has no elements, without counting.
**C4** – `SELECT DISTINCT b.passenger FROM Booking b WHERE b.agent.id = :id`: dot navigation, DISTINCT shows each passenger once. The controller first checks the agent exists (404).

**D1** – Three checks in order: `isBefore(LocalDate.now())` → 400; `existsByAgentAndTravelDateAndStatusNot(agent, date, CANCELLED)` → 409; then `setStatus(RESERVED)` whatever the JSON said.
**D2** – Compare enums with `==`: RESERVED can go to PAID/CANCELLED, PAID to TICKETED/CANCELLED, nothing else; the error message uses the real names. The PUT update does not touch the status.
**D3** – Load both, `contains` → 409, otherwise `getRoutes().add(route)` and save the BookingAgent because it owns the join table.
**D4** – `findByDeskNumber` → 404 if empty; `existsBySalesDesk` → 409 if another agent sits there; otherwise set the desk and save, and the old desk shows up in C2.

**E1** – Find the booking (404), check `status != TICKETED` (400), set `purchaseDate = LocalDate.now()` and the booking, save (201).
**E2** – JPQL `d.booking.passenger.passportNumber = :p ORDER BY d.purchaseDate DESC`: three dots of navigation, no JOIN.
**E3** – `existsByBooking(booking)` in the booking delete → 409, so a booking with purchases is protected.

**F1** – `SELECT b.status, COUNT(b) ... GROUP BY b.status` returns `List<Object[]>`, one row per status that exists.
**F2** – One JPQL UPDATE with `@Modifying` (repository) and `@Transactional` (service); it returns the number of changed rows. Bookings already TICKETED (or already CANCELLED) are left out, so the number is the real one.

**Bonus dashboard** – `count()`, the derived `countByTravelDate` and `countByStatus`, and the size of the C2 list, put in a LinkedHashMap to keep the order.
**Bonus page** – Split `sort` on the comma, build `PageRequest.of(page, size, Sort.by(direction, field))`; `findAll(pageable)` returns a Page with totalPages and totalElements.

## Seed data note

D1 refuses dates before today, but the paper wants bookings on 2026-10-20. So the seed folder saves each booking with a future date, then uses the PUT update (which has no date rule) to set the real date, then PATCH status to move it along the status flow. Everything still goes through your own endpoints.
