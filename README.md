# Crobs_Grp16
1. Complete Project Structure
crobs
│
├── Application.java
│
├── model
│   ├── CROBS.java
│   │
│   ├── user
│   │   ├── User.java
│   │   ├── RegularUser.java
│   │   ├── AdminUser.java
│   │   └── SuperAdmin.java
│   │
│   ├── room
│   │   ├── Room.java
│   │   └── RoomType.java
│   │
│   ├── booking
│   │   └── Booking.java
│   │
│   └── enums
│       └── UserRole.java
│
├── command
│   ├── CommandProcessor.java
│   └── CommandParser.java
│
├── service
│   ├── UserService.java
│   ├── RoomService.java
│   ├── BookingService.java
│   └── SearchService.java
│
├── manager
│   ├── UserManager.java
│   ├── RoomManager.java
│   └── BookingManager.java
│
├── log
│   ├── HTMLLogger.java
│   └── TXTLogger.java
│
├── exception
│   ├── PermissionDeniedException.java
│   ├── InvalidCommandException.java
│   ├── UserNotFoundException.java
│   ├── RoomNotFoundException.java
│   └── BookingNotFoundException.java
│
└── util
    ├── IDGenerator.java
    ├── DateUtil.java
    └── ValidationUtil.java
   
2. Requirement Mapping
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

3. High Mark Design Tips
• Follow MVC strictly.
• Keep business logic in model package.
• Use HashMap for users, rooms and bookings.
• Use LocalDate and LocalTime.
• Use enums instead of raw strings.
• Generate IDs U001/R001/B001.
• Centralize permission checking.
• Validate before modifying data.
• Handle all errors gracefully.
• Test success paths and failure paths.

4. Critical Marking Considerations
• Unit testing is performed on hk.edu.polyu.comp.comp2021.crobs.model package.
• Aim for 90%+ line coverage.
• Coverage marks are multiplied by requirement completion percentage.
• Error handling directly affects design marks.
• Object-Oriented design, inheritance and extensibility are assessed.
