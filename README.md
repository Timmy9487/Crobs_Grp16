# Crobs_Grp16

## Work division + Checklist

### COMPLETED
[25089289D] Candice: Room Module (R7-9, R15-16)
[25081767D] Tim:REQ1,REQ18
>> **check [CHANGELOG.md](CHANGELOG.md)**

### TO-DO
User & Permission Module (R2-6)

Booking & Search Module (R10-14,17)

Infrastructure, Logging, Testing & Integration ( testing, B1-2)

---
### 1. Complete Project Structure
```
[x]: completed
[o]: incomplete
[?]: To be discussed

note for CROBS.java: i might have missed an import so should double check later
```
```
crobs
│
├── [?] Application.java
│
├──[x]logging
│   [x]├── Logger.java
│   [x]├── TxtLogger.java
│   [x]├── HtmlLogger.java
│   [x]└── LogManager.java
│
├── model
│   ├── [o] CROBS.java
│   │
│   ├── user
│   │   ├── User.java
│   │   ├── RegularUser.java
│   │   ├── AdminUser.java
│   │   └── SuperAdmin.java
│   │
│   ├── [x] room
│   │   ├── [x] Room.java
│   │   └── [x] RoomType.java
│   │
│   ├── booking
│   │   └── Booking.java
│   │
│   └── enums
│       └── UserRole.java
│
├── [o] command
│   ├──[x]ParsedCommand.java
│   ├──[o] CommandProcessor.java
│   └──[x] CommandParser.java
│   │
│   ├── [o]Command.java          (BON2)
│   │
│   ├── AddUserCommand.java
│   ├── RemoveUserCommand.java│
│   ├── ChangeRoleCommand.java
│   │
│   ├── AddRoomCommand.java
│   ├── ModifyRoomCommand.java
│   └── DeleteRoomCommand.java
│   │
│   ├── BookCommand.java
│   ├── ModifyBookingCommand.java
│   └── DeleteBookingCommand.java
│
├── [o]undo
│   └──[o] UndoManager.java
│
├── service
│   ├── UserService.java
│   ├── RoomService.java
│   ├── BookingService.java
│   └── SearchService.java
│
├── [o] manager
│   ├── UserManager.java
│   ├── [x] RoomManager.java
│   └── BookingManager.java
│
├── [o] exception
│   ├── [x] PermissionDeniedException.java
│   ├── [x] InvalidCommandException.java
│   ├── UserNotFoundException.java 
│   ├── [x] RoomNotFoundException.java
│   └── BookingNotFoundException.java
│
└── [o] util
    ├── [x] IDGenerator.java
    ├── DateUtil.java
    └── ValidationUtil.java
```
   
### 2. Requirement Mapping
```
REQ1 -> HtmlLogger, TxtLogger
REQ2 -> UserManager.addUser()
REQ3 -> UserManager.removeUser()
REQ4 -> UserManager.changeRole()
REQ5 -> UserManager.login()
REQ6 -> UserManager.logout()
REQ7 -> RoomManager.addRoom()
REQ8 -> RoomManager.modifyRoom()
REQ9 -> RoomManager.deleteRoom()
REQ10 -> BookingManager.book()
REQ11 -> BookingManager.modifyBooking()
REQ12 -> BookingManager.deleteBooking()
REQ13 -> SearchService.search()
REQ14 -> BookingManager.getSchedule()
REQ15 -> RoomManager.listRoom()
REQ16 -> RoomManager.listAllRooms()
REQ17 -> BookingManager.listBookings()
REQ18 -> Application.quit()
```

### 3. High Mark Design Tips
* Follow MVC strictly.
* Keep business logic in model package.
* Use HashMap for users, rooms and bookings.
* Use HashMap for users, rooms and bookings.
* Use LocalDate and LocalTime.
* Use enums instead of raw strings.
* Generate IDs U001/R001/B001.
* Centralize permission checking.
* Validate before modifying data.
* Handle all errors gracefully.
* Test success paths and failure paths.

### 4. Critical Marking Considerations
* Unit testing is performed on hk.edu.polyu.comp.comp2021.crobs.model package.
* Aim for 90%+ line coverage.
* Coverage marks are multiplied by requirement completion percentage.
* Error handling directly affects design marks.
* Object-Oriented design, inheritance and extensibility are assessed.
