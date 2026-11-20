# Crobs_Grp16

## Work division + Checklist

### COMPLETED
[25089289D] Candice: Room Module (R7-9, R15-16)
[25081767D] Tim:REQ1,REQ18
>> **check [CHANGELOG.md](CHANGELOG.md)**

### TO-DO

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
│   ├── [x] user
│   │   [x] ├── User.java
│   │   [x] ├── RegularUser.java
│   │   [x] ├── AdminUser.java
│   │   [x] └── SuperAdmin.java
│   │
│   ├── [x] room
│   │   ├── [x] Room.java
│   │   └── [x] RoomType.java
│   │
│   ├── [x] booking
│   │   └── [x] Booking.java (REQ10-14,17)
│   │   └── [x] BookingRegistry.java
│   │   └── [x] RoomSearch.java
│   │
│   └── [x] enums
│       [x] └── UserRole.java
│
├── [o] command
│   ├──[x]ParsedCommand.java
│   ├──[o] CommandProcessor.java
│   └──[x] CommandParser.java
│   │
│   ├── [o]Command.java          (BON2)
│   │
│   ├── AddUserCommand.java (B2,2)
│   ├── RemoveUserCommand.java│ (B2,3)
│   ├── ChangeRoleCommand.java (B2,4)
│   │
│   ├── AddRoomCommand.java (B2,7)
│   ├── ModifyRoomCommand.java (B2,8)
│   └── DeleteRoomCommand.java (B2,9)
│   │
│   ├── BookCommand.java (B2,10)
│   ├── ModifyBookingCommand.java (B2,11)
│   └── DeleteBookingCommand.java (B2,12)
│
├── [o]undo
│   └──[o] UndoManager.java
│
├── [x]service
│   └── [x]SearchService.java
│
├── [o] manager
│   ├── [x] UserManager.java
│   ├── [x] RoomManager.java
│   └── [x] BookingManager.java
│
├── [o] exception
│   ├── [x] PermissionDeniedException.java
│   ├── [x] InvalidCommandException.java
│   ├── [x] UserNotFoundException.java (REQ 2-6)
│   ├── [x] RoomNotFoundException.java
│   ├── BookingNotFoundException.java (10-17)
│   └── BookingConflictException.java (REQ10,11)
│
└── [o] util
    ├── [x] IDGenerator.java
    ├── DateUtil.java (REQ10-14,17)
    └── ValidationUtil.java (REQ2-18)
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
