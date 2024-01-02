# Restaurant Table Reservation Application

## Key Features

### API Features
- **Manage Restaurants**: Get all restaurants, add, edit, or delete restaurant listings from the platform.
- **Manage Tables**: Get tables by restaurant id, add, edit, or delete tables in a restaurant.
- **Manage Reservations**: Get reservations by restaurant id, add, edit or delete reservation for table.



## Technology Stack
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Database**: PostgreSQL with Hibernate for data storage and management.
- **Testing**: 
  - Unit tests using Mockito for mocking dependencies.
  - Integration tests using an in-memory database.
  - AssertJ for assertions.
- **Error Handling**: Global exception handling where errors are thrown at the service layer and caught by exception handlers to return appropriate HTTP statuses with messages.
