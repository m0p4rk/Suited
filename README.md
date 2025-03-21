# Suited

A real-time multiplayer Texas Hold'em poker web application.

## Overview

Suited is a web-based Texas Hold'em poker platform that provides real-time multiplayer gaming experience. The project is structured as a full-stack application with a robust backend and a modern frontend (coming soon).

## Features

- Real-time multiplayer poker games
- User authentication and profile management
- Game room creation and management
- Real-time chat system
- Ranking system
- Point/Money system

## Tech Stack

### Backend (Current)
- Spring Boot 3.x
- Java 17
- Maven
- Spring Security
- Spring WebSocket
- Spring Data JPA
- H2 Database

### Frontend (Coming Soon)
- React
- TypeScript
- Vite
- WebSocket Client
- State Management
- UI Framework

### Development Tools
- IntelliJ IDEA
- VS Code
- Maven
- Git

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.8 or higher
- Git 2.30 or higher
- Node.js 18 or higher (for frontend, coming soon)

### System Requirements
- CPU: 2 cores or more
- RAM: 4GB or more
- Storage: 20GB or more

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/suited.git
```

2. Navigate to the project directory
```bash
cd suited
```

3. Build the backend
```bash
cd backend
mvn clean install
```

4. Run the backend application
```bash
mvn spring-boot:run
```

5. Frontend setup (coming soon)
```bash
cd frontend
npm install
npm run dev
```

## Project Structure

```
suited/
├── backend/         # Spring Boot backend
│   └── src/
│       ├── main/
│       │   └── java/com/suited/
│       │       ├── config/          # Configuration classes
│       │       ├── controller/      # REST API controllers
│       │       ├── service/         # Business logic
│       │       ├── repository/      # Data access layer
│       │       ├── model/          # Domain models
│       │       │   ├── entity/     # JPA entities
│       │       │   ├── dto/        # Data transfer objects
│       │       │   └── enums/      # Enumerations
│       │       ├── security/       # Security related classes
│       │       └── websocket/      # WebSocket related classes
│       └── test/                   # Backend tests
└── frontend/       # React frontend (coming soon)
    └── src/
        ├── components/    # React components
        ├── pages/        # Page components
        ├── services/     # API services
        ├── store/        # State management
        └── utils/        # Utility functions
```

## API Documentation

### REST API Endpoints
```
/api/v1
├── /auth
│   ├── POST /login
│   ├── POST /register
│   └── POST /logout
├── /users
│   ├── GET /profile
│   ├── GET /stats
│   └── GET /ranking
├── /rooms
│   ├── POST /create
│   ├── POST /join/{roomId}
│   └── GET /list
└── /game
    ├── POST /start
    ├── POST /bet
    └── POST /fold
```

### WebSocket Events
- PLAYER_JOIN: Player joins the game
- PLAYER_LEAVE: Player leaves the game
- GAME_START: Game starts
- DEAL_CARDS: Cards are dealt
- BETTING_ROUND: Betting round
- SHOWDOWN: Winner determination
- GAME_END: Game ends

## Development Status

### Backend (Current)
- ✅ Project setup
- ✅ Basic architecture
- ⏳ Core features implementation
- ⏳ Testing
- ⏳ Documentation

### Frontend (Coming Soon)
- ⏳ Project setup
- ⏳ UI/UX design
- ⏳ Component development
- ⏳ State management
- ⏳ Testing
- ⏳ Documentation

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- React team for the frontend framework
- All contributors who have helped with the project