# Full E-Commerce API — Spring Boot & Spring Security

This repo is a Full E-Commerce REST API built with Spring Boot & Spring Security, covering the complete shopping flow from product catalog browsing to Stripe checkout with JWT authentication, role-based access control, database migrations and Swagger documentation.

## Project Structure

```text
.
├── src/main/java/com/ecommercestore/store/
│   ├── StoreApplication.java                      # Spring Boot entry point
│   ├── products/
│   │   ├── ProductController.java                 # Product CRUD endpoints
│   │   ├── ProductMapper.java                     # MapStruct entity-DTO mapper
│   │   ├── ProductDto.java                        # Product request/response DTO
│   │   ├── Product.java                           # Product JPA entity
│   │   ├── Category.java                          # Category entity
│   │   ├── ProductRepository.java                 # JPA repository
│   │   └── ProductNotFoundException.java          # Custom exception
│   ├── carts/
│   │   ├── CartController.java                    # Cart CRUD endpoints
│   │   ├── CartService.java                       # Cart business logic
│   │   ├── CartRepository.java                    # JPA repository with EntityGraph
│   │   ├── CartMapper.java                        # MapStruct mapper
│   │   ├── Cart.java                              # Cart entity (UUID PK)
│   │   ├── CartItem.java                          # Cart item entity
│   │   ├── CartDto.java, CartItemDto.java         # DTOs
│   │   └── AddItemToCartRequest.java              # Request DTO
│   ├── orders/
│   │   ├── OrderController.java                   # Order history endpoints
│   │   ├── OrderService.java                      # Order business logic
│   │   ├── OrderRepository.java                   # JPA repository
│   │   ├── OrderMapper.java                       # MapStruct mapper
│   │   ├── Order.java                             # Order entity
│   │   ├── OrderItem.java                         # Order item entity
│   │   └── PaymentStatus.java                     # PENDING / PAID / FAILED / CANCELED
│   ├── payments/
│   │   ├── CheckoutController.java                # Checkout & webhook endpoints
│   │   ├── CheckoutService.java                   # Checkout flow (cart → order → Stripe)
│   │   ├── PaymentGateway.java                    # Payment gateway interface
│   │   ├── StripePaymentGateway.java              # Stripe implementation
│   │   ├── StripeConfig.java                      # Stripe API key init
│   │   └── PaymentSecurityRules.java              # Webhook endpoint access rules
│   ├── auth/
│   │   ├── AuthController.java                    # Login, refresh, /me endpoints
│   │   ├── AuthService.java                       # Authentication logic
│   │   ├── JwtService.java                        # JWT generation & parsing
│   │   ├── Jwt.java                               # JWT model (claims, expiry, role)
│   │   ├── JwtAuthenticationFilter.java           # Bearer token filter
│   │   ├── JwtConfig.java                         # JWT properties binding
│   │   └── SecurityConfig.java                    # Spring Security filter chain
│   ├── users/
│   │   ├── UserController.java                    # User CRUD + register + change password
│   │   ├── UserService.java                       # User business logic
│   │   ├── UserRepository.java                    # JPA repository
│   │   ├── UserMapper.java                        # MapStruct mapper
│   │   ├── User.java                              # User JPA entity
│   │   ├── Profile.java, Address.java             # Related entities
│   │   ├── Role.java                              # USER / ADMIN enum
│   │   ├── RegisterUserRequest.java               # Validated registration DTO
│   │   ├── Lowercase.java                         # Custom @Lowercase annotation
│   │   └── LowercaseValidator.java                # ConstraintValidator implementation
│   ├── admin/
│   │   ├── AdminController.java                   # Admin-only endpoints
│   │   └── AdminSecurityRules.java                # ROLE_ADMIN restriction
│   └── common/
│       ├── GlobalExceptionHandler.java             # @ControllerAdvice error handling
│       ├── ErrorDto.java                           # Standard error response
│       ├── SecurityRules.java                      # Modular security interface
│       ├── SwaggerSecurityRules.java               # Swagger UI public access
│       └── LoggingFilter.java                      # Request/response logging
├── src/main/resources/
│   ├── application.yaml                            # Base config
│   ├── application-dev.yaml                        # Dev profile (MySQL local)
│   ├── application-prod.yaml                       # Prod profile (env vars)
│   ├── db/migration/
│   │   ├── V1__initial_migration.sql               # Core tables (users, products, categories, etc.)
│   │   ├── V2__create_cart_tables.sql              # Cart tables
│   │   ├── V3__add_role_to_users.sql               # Role column
│   │   ├── V4__add_order_tables.sql                # Order tables
│   │   └── V5__populate_database.sql               # Seed data (6 categories, 10 products)
│   └── templates/index.html                        # Thymeleaf home view
└── pom.xml                                          # Maven config
```

## Stack & Service Overview

- Framework: Spring Boot 3.4.1 + Java 17
- Web: Spring MVC (REST controllers)
- Security: Spring Security + JWT (jjwt 0.12) + Role-based access (USER / ADMIN)
- Persistence: Spring Data JPA (Hibernate) + MySQL
- Migrations: Flyway 10.15
- Payments: Stripe Java SDK 29.0 (Checkout Sessions + Webhooks)
- Mapping: MapStruct 1.6 + Lombok
- Validation: Jakarta Bean Validation + custom constraints
- API docs: SpringDoc OpenAPI 2.8 (Swagger UI)
- Build: Maven

## Features

- Product catalog with CRUD and categories
- Shopping cart with add, update, remove, clear
- User registration with custom @Lowercase email validation
- JWT authentication with access + refresh tokens (HttpOnly cookie)
- Role-based access control (USER / ADMIN) with modular security rules
- Stripe Checkout Session creation and webhook handling
- Order management with cart-to-order conversion
- Global error handling with @ControllerAdvice
- Flyway database migrations with seed data
- Swagger/OpenAPI documentation

## Quick Start

### 1) Prerequisites

- Java 17+
- MySQL running on localhost:3306
- Maven (or use the included wrapper)

### 2) Configure environment

```bash
cp .env.example .env
```

Edit `.env` and set:

- `JWT_SECRET` — Generate with `openssl rand -base64 32`
- `STRIPE_SECRET_KEY` — From your Stripe dashboard
- `STRIPE_WEBHOOK_SECRET_KEY` — From Stripe CLI (see below)

### 3) Start Stripe webhook listener (for local dev)

```bash
stripe login
stripe listen --forward-to http://localhost:8080/checkout/webhook
```

### 4) Run the application

```bash
./mvnw spring-boot:run
```

Default local API URL:

- http://localhost:8080

Swagger docs locally:

- http://localhost:8080/swagger-ui.html

## REST API Endpoints

### Products

- GET /products — List all products
- GET /products/:id — Get one product
- POST /products — Create a product (Admin)
- PUT /products/:id — Update a product (Admin)
- DELETE /products/:id — Delete a product (Admin)

### Shopping Cart

- POST /carts — Create a cart
- GET /carts/:id — Get cart details
- POST /carts/:id/items — Add item to cart
- PUT /carts/:id/items/:productId — Update item quantity
- DELETE /carts/:id/items/:productId — Remove item
- DELETE /carts/:id/items — Clear cart

### Authentication

- POST /users — Register a new user
- POST /auth/login — Login (returns JWT)
- POST /auth/refresh — Refresh access token (from HttpOnly cookie)
- GET /auth/me — Get current user (Auth required)

### Checkout & Orders

- POST /checkout — Start Stripe checkout (Auth required)
- POST /checkout/webhook — Stripe webhook callback (public)
- GET /orders — List user orders (Auth required)
- GET /orders/:id — Get order details (Auth required)

## Data Model

```text
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   users      │       │   orders     │       │  order_items  │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │──┐    │ id (PK)      │──┐    │ id (PK)      │
│ name         │  │    │ customer_id  │  │    │ order_id (FK)│
│ email        │  ├──→ │ status       │  ├──→ │ product_id   │
│ password     │  │    │ total_price  │  │    │ unit_price   │
│ role         │  │    │ created_at   │  │    │ quantity     │
└──────────────┘  │    └──────────────┘  │    └──────────────┘
                  │                      │
┌──────────────┐  │    ┌──────────────┐  │    ┌──────────────┐
│   carts      │  │    │  cart_items  │  │    │   products   │
├──────────────┤  │    ├──────────────┤  │    ├──────────────┤
│ id (UUID PK) │──┼──→ │ cart_id (FK) │  │    │ id (PK)      │
│ created_at   │  │    │ product_id   │←─┘    │ name         │
└──────────────┘  │    │ quantity     │       │ price        │
                  │    └──────────────┘       │ description  │
┌──────────────┐  │                           │ category_id  │
│  profiles    │  │    ┌──────────────┐       └──────────────┘
├──────────────┤  │    │  categories  │
│ user_id (FK) │←─┘    ├──────────────┤
│ bio          │       │ id (PK)      │←─── product.category_id
│ phone        │       │ name         │
└──────────────┘       └──────────────┘
```

## Environment Variables

Use `.env.example` as a baseline.

- `JWT_SECRET` — Secret key for JWT signing
- `STRIPE_SECRET_KEY` — Stripe API secret key
- `STRIPE_WEBHOOK_SECRET_KEY` — Stripe webhook signing secret
- `SPRING_DATASOURCE_URL` — MySQL URL (prod only)
- `SPRING_DATASOURCE_USERNAME` — MySQL username (prod only)
- `SPRING_DATASOURCE_PASSWORD` — MySQL password (prod only)

## Development Notes

- The project uses a modular security rules pattern where each feature module defines its own access rules via the `SecurityRules` interface
- Refresh tokens are stored in HttpOnly secure cookies, not in the response body
- Cart endpoints are public (no auth required) to support guest checkout flows
- The Stripe webhook endpoint bypasses authentication to allow Stripe callback verification
- The `@Lowercase` custom annotation enforces lowercase email addresses at the DTO validation layer
- Database seed data (6 categories, 10 products) is applied automatically via Flyway migration V5
