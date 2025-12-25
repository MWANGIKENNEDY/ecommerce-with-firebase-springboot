# Ecommerce Application

A full-stack ecommerce application built with **Spring Boot** (backend) and **Next.js** (frontend), featuring Firebase authentication, shopping cart functionality, and order management.

## 🚀 Features

### Core Functionality
- **Product Catalog**: Browse and view product details
- **Shopping Cart**: Add/remove items, view cart summary
- **Order Management**: Checkout process and order history
- **User Authentication**: Google Sign-In via Firebase
- **Responsive Design**: Mobile-friendly interface

### Technical Features
- **RESTful API**: Spring Boot backend with JPA/Hibernate
- **Modern Frontend**: Next.js 16 with React 19 and TypeScript
- **Authentication**: Firebase token-based authentication
- **Database**: H2 (development) / MySQL (production)
- **Containerization**: Docker support with multi-stage builds
- **Cloud Ready**: AWS deployment configuration

## 🏗️ Architecture

### Backend (Spring Boot)
```
src/main/java/com/ecommerce/ecommerce/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── model/          # JPA entities
├── repo/           # Data repositories
├── dtos/           # Data transfer objects
├── security/       # Authentication & authorization
├── config/         # Configuration beans
└── exceptions/     # Exception handling
```

### Frontend (Next.js)
```
frontend/ui/
├── app/            # Next.js App Router pages
├── components/     # React components
├── lib/            # Utilities, API client, hooks
└── public/         # Static assets
```

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Backend** | Spring Boot | 3.5.7 |
| | Java | 17 |
| | Spring Security | 6.x |
| | Firebase Admin SDK | 9.7.0 |
| | MySQL/H2 | 8.0/Latest |
| **Frontend** | Next.js | 16.0.3 |
| | React | 19.2.0 |
| | TypeScript | 5.x |
| | Tailwind CSS | 4.x |
| | Firebase SDK | 12.6.0 |
| **DevOps** | Docker | Latest |
| | Maven | 3.9 |

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Node.js 18** or higher
- **npm** or **yarn**
- **Maven 3.6+**
- **Docker** (optional, for containerization)
- **Firebase Project** (for authentication)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ecommerce-maven
```

### 2. Backend Setup

#### Configure Firebase
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Authentication → Sign-in method → Google
3. Generate a service account key:
   - Go to Project Settings → Service Accounts
   - Click "Generate new private key"
   - Save as `src/main/resources/serviceAccountKey.json`

#### Start Backend
```bash
# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

The backend will start at `http://localhost:8080`

#### Verify Backend
```bash
# Test products endpoint
curl http://localhost:8080/api/products

# Access H2 console (development)
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:file:./data/testdb
# Username: sa
# Password: (leave empty)
```

### 3. Frontend Setup

#### Configure Environment
```bash
cd frontend/ui
cp .env .env.local  # Copy environment template
```

Edit `.env.local` with your Firebase configuration:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your-sender-id
NEXT_PUBLIC_FIREBASE_APP_ID=your-app-id
```

#### Start Frontend
```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will start at `http://localhost:3000`

### 4. Access the Application

1. **Browse Products**: Visit `http://localhost:3000`
2. **Sign In**: Click "Login" → "Continue with Google"
3. **Shop**: Add products to cart, checkout, view orders

## 📖 Detailed Setup Guide

### Backend Configuration

#### Development Database (H2)
The application uses H2 database by default for development:
- **Database File**: `./data/testdb.mv.db`
- **Console**: `http://localhost:8080/h2-console`
- **Auto-DDL**: Enabled (creates tables automatically)

#### Production Database (MySQL)
For production, configure MySQL via environment variables:
```bash
export DB_HOST=your-mysql-host
export DB_PORT=3306
export DB_NAME=ecommerce
export DB_USERNAME=your-username
export DB_PASSWORD=your-password
export SPRING_PROFILES_ACTIVE=prod
```

#### Firebase Configuration
Two options for Firebase service account:

**Option 1: File (Development)**
```bash
# Place your service account JSON file at:
src/main/resources/serviceAccountKey.json
```

**Option 2: Environment Variable (Production)**
```bash
export FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
```

### Frontend Configuration

#### Environment Variables
Create `frontend/ui/.env.local`:
```env
# API Configuration
NEXT_PUBLIC_API_URL=http://localhost:8080

# Firebase Configuration (get from Firebase Console)
NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your-sender-id
NEXT_PUBLIC_FIREBASE_APP_ID=your-app-id
```

#### Firebase Setup Steps
1. **Create Project**: Go to [Firebase Console](https://console.firebase.google.com/)
2. **Enable Authentication**:
   - Authentication → Sign-in method
   - Enable "Google" provider
   - Add your domain to authorized domains
3. **Get Configuration**:
   - Project Settings → General → Your apps
   - Copy the config object values to `.env.local`

## 🔗 API Endpoints

### Public Endpoints
```
GET  /api/products           # List all products
GET  /api/products/{id}      # Get product details
```

### Authenticated Endpoints (Requires Firebase Token)
```
POST /api/auth/login         # Login/register user
GET  /api/cart              # Get user's cart
POST /api/cart/items        # Add item to cart
DELETE /api/cart/items/{id} # Remove item from cart
POST /api/orders/checkout   # Create order from cart
GET  /api/orders            # Get user's orders
GET  /api/orders/{id}       # Get order details
```

### Authentication
Include Firebase ID token in Authorization header:
```
Authorization: Bearer <firebase-id-token>
```

## 🎯 User Flow

### Shopping Flow
1. **Browse Products** → Home page displays product catalog
2. **View Product** → Click product for detailed view
3. **Add to Cart** → Requires login if not authenticated
4. **View Cart** → Review items and quantities
5. **Checkout** → Create order from cart items
6. **Order History** → View past orders and details

### Authentication Flow
1. **Login Page** → Click "Continue with Google"
2. **Google OAuth** → Select Google account
3. **Firebase Token** → Stored in browser cookie
4. **Backend Registration** → User created/updated in database
5. **Authenticated Access** → Can now use cart and orders

## 🐳 Docker Deployment

### Build and Run with Docker
```bash
# Build Docker image
docker build -t ecommerce-app .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e DB_USERNAME=your-username \
  -e DB_PASSWORD=your-password \
  -e FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}' \
  ecommerce-app
```

### Docker Compose (Optional)
Create `docker-compose.yml` for local development:
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    volumes:
      - ./data:/app/data
```

## ☁️ AWS Deployment

For detailed AWS deployment instructions, see [AWS_DEPLOYMENT.md](AWS_DEPLOYMENT.md).

### Quick AWS Setup
1. **Create RDS MySQL Database**
2. **Deploy to Elastic Beanstalk**:
   ```bash
   eb init -p "Docker" ecommerce-app
   eb create ecommerce-env
   eb setenv DB_HOST=your-rds-endpoint DB_USERNAME=admin DB_PASSWORD=your-password
   eb deploy
   ```

## 🧪 Testing

### Backend Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductControllerTest
```

### Frontend Testing
```bash
cd frontend/ui

# Run tests
npm test

# Run tests in watch mode
npm run test:watch
```

### Manual Testing
1. **Products API**: `curl http://localhost:8080/api/products`
2. **H2 Console**: `http://localhost:8080/h2-console`
3. **Frontend**: `http://localhost:3000`

## 🔧 Development

### Adding New Features

#### Backend
1. **Create Entity** in `model/` package
2. **Create Repository** in `repo/` package
3. **Create Service** in `service/` package
4. **Create Controller** in `controller/` package
5. **Create DTOs** in `dtos/` package

#### Frontend
1. **Add Types** in `lib/types.ts`
2. **Add API Methods** in `lib/api.ts`
3. **Create Components** in `components/`
4. **Add Pages** in `app/` directory

### Database Schema
The application uses JPA auto-DDL to create tables. Key entities:
- **User**: Firebase UID as primary key
- **Product**: Name, price, inventory, brand, category
- **Cart**: User's shopping cart with items
- **Order**: Completed purchases with items
- **Category**: Product categories

## 🐛 Troubleshooting

### Common Issues

#### Backend Won't Start
- **Check Java Version**: Ensure Java 17+ is installed
- **Firebase Config**: Verify `serviceAccountKey.json` exists
- **Port Conflict**: Ensure port 8080 is available

#### Frontend Won't Start
- **Node Version**: Ensure Node.js 18+ is installed
- **Dependencies**: Run `npm install` in `frontend/ui/`
- **Environment**: Check `.env.local` file exists with correct values

#### Authentication Issues
- **Firebase Config**: Verify all Firebase environment variables
- **Google Sign-In**: Ensure Google provider is enabled in Firebase
- **CORS**: Check backend allows requests from frontend origin

#### Database Issues
- **H2 Console**: Access at `http://localhost:8080/h2-console`
- **File Permissions**: Ensure `./data/` directory is writable
- **MySQL Connection**: Verify connection string and credentials

### Logs and Debugging
```bash
# Backend logs
mvn spring-boot:run

# Frontend logs
npm run dev

# Docker logs
docker logs <container-id>
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Next.js Documentation](https://nextjs.org/docs)
- [Firebase Documentation](https://firebase.google.com/docs)
- [AWS Deployment Guide](AWS_DEPLOYMENT.md)
- [Frontend Complete Guide](FRONTEND_COMPLETE.md)
- [User Login Guide](USER_LOGIN_GUIDE.md)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -am 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions or issues:
1. Check the troubleshooting section above
2. Review existing documentation files
3. Create an issue in the repository
4. Contact the development team

---

**Happy Shopping! 🛒**