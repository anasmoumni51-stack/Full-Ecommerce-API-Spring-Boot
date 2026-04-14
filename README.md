# E-Commerce Store API

A full-featured e-commerce REST API built with Spring Boot. Includes product management, shopping cart, checkout with Stripe payments, JWT authentication, role-based access control, and database migrations with Flyway.

## Features

- **Product Catalog** — CRUD operations with category management
- **Shopping Cart** — Create carts, add/update/remove items
- **User Authentication** — JWT-based auth with refresh tokens
- **Role-Based Access Control** — Admin and user roles with modular security rules
- **Stripe Payments** — Checkout sessions and webhook handling
- **Order Management** — Order history and status tracking
- **Global Error Handling** — Custom validation and exception handling
- **API Documentation** — Swagger/OpenAPI UI

## Tech Stack

- Java 17, Spring Boot 3.4
- Spring Security, Spring Data JPA (Hibernate)
- MySQL, Flyway Migrations
- Stripe Payment Integration
- JWT (jjwt), MapStruct, Lombok
- SpringDoc OpenAPI (Swagger)

## Getting Started

### Prerequisites

- Java 17+
- MySQL running on localhost:3306
- Maven (or use the included wrapper)

### Setup

1. Clone the repository:
```bash
git clone https://github.com/anas-moumni/ecommerce-store-api.git
cd ecommerce-store-api
```

2. Configure environment variables:
```bash
cp .env.example .env
```

Edit `.env` and set:
- `JWT_SECRET` — Generate with `openssl rand -base64 32`
- `STRIPE_SECRET_KEY` — From your [Stripe dashboard](https://stripe.com)
- `STRIPE_WEBHOOK_SECRET_KEY` — From Stripe CLI (see below)

3. Start the Stripe webhook listener (for local development):
```bash
stripe login
stripe listen --forward-to http://localhost:8080/checkout/webhook
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/products` | List all products | No |
| POST | `/products` | Create a product | Admin |
| PUT | `/products/{id}` | Update a product | Admin |
| DELETE | `/products/{id}` | Delete a product | Admin |
| POST | `/carts` | Create a cart | No |
| POST | `/carts/{cartId}/items` | Add item to cart | No |
| GET | `/carts/{cartId}` | Get cart details | No |
| POST | `/users` | Register a new user | No |
| POST | `/auth/login` | Login and get JWT | No |
| POST | `/auth/refresh` | Refresh access token | No |
| GET | `/auth/me` | Get current user | Yes |
| POST | `/checkout` | Start Stripe checkout | Yes |
| GET | `/orders` | List user orders | Yes |
| GET | `/orders/{orderId}` | Get order details | Yes |

## Example Flow

1. Browse products: `GET /products`
2. Create a cart: `POST /carts`
3. Add items: `POST /carts/{cartId}/items`
4. Register: `POST /users`
5. Login: `POST /auth/login`
6. Checkout: `POST /checkout` (returns Stripe payment URL)
7. Complete payment with test card: `4242 4242 4242 4242`
8. View orders: `GET /orders`
