# CHANGELOG

## 2026-11-10 : Candice

### 1. added `model.room`
* **`RoomType.java`**: Enum containing the 4 required room types (`lectureTheatre`, `computerLab`, `seminarRoom`, `meetingRoom`) with case-insensitive input parsing and validation.
* **`Room.java`**: Holds room data (`roomId`, `roomType`, `capacity`, and a list of `features`). Implemented `Comparable` so sorting by `roomId` ascending happens automatically.

### 2. added `manager.RoomManager`
* **REQ7 (`addRoom`)**: Admin only. Validates capacity (> 0) and room type, parses comma-separated features, and auto-generates IDs like `R001`, `R002`.
* **REQ8 (`modifyRoom`)**: Admin only. Allows editing `roomType`, `capacity`, or `features` of an existing room.
* **REQ9 (`deleteRoom`)**: Admin only. Deletes a room and has a hook to call `BookingManager.cancelFutureBookingsForRoom()` so future bookings get cleaned up.
* **REQ15 (`listRoom`)**: Displays details for a single room ID (must be logged in).
* **REQ16 (`listAllRooms`)**: Prints all rooms sorted by room ID ascending.

### 3. added helpers and `exceptions/`
* **`IDGenerator.java`**: Simple auto-increment generator for room IDs (`R001`, `R002`...).
* Custom exception classes: `RoomNotFoundException`, `PermissionDeniedException`, and `InvalidCommandException`

### 4. edited `CROBS` and added `CommandProcessor`
* **`CROBS.java`**: Connected `RoomManager` to `BookingManager` and added getters so other modules can access room data.
* **`CommandProcessor.java`**: All room commands are kept inside an isolated `handleRoomCommand()` helper method. There are placeholder methods ready for `handleUserCommand()` and `handleBookingCommand()` so nobody has to deal with merge conflicts when adding new commands

### 5. added unit test for line coverage (`RoomManagerTest.java`)

### 6. fixed the readme formatting and added the work division checklist stuff + renamed SampleProject to project

## !! NOTE PLEASE READ !!
* **for User Module**: in `handleUserCommand()`, you can just check `crobs.getUserManager()` and update `crobs.setCurrentUser()` upon login/logout
* **for Booking Module**: make sure `BookingManager` implements `cancelFutureBookingsForRoom(String roomId)` so `deleteRoom` works with your schedule table

