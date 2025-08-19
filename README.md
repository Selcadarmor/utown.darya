# UTOWN — Backend

Backend for **UTOWN**, a food delivery web service for ethnic cuisine in Korea.  
The platform connects multicultural residents and foreigners with unique restaurants, providing an easy way to order food, manage favorites, and receive real-time notifications.

---

## 📖 Table of Contents

- [Description](#description)
- [Main Users](#main-users)
- [Models](#models)
- [Tech Stack](#tech-stack)
- [Deployment](#deployment)

---

## 📝 Project Description

**UTOWN** is a web application for food delivery with a focus on ethnic cuisine in Korea.  
It allows users to discover unique restaurants, place food orders, manage favorites, and receive real-time updates on order status.

---

## 👤 Main Users

- **Client** — Mobile web application for customers (ordering food, managing cart, receiving notifications etc.)
- **Restaurant Admin** — Web application for restaurants (managing menu, orders, and restaurant profile)
- **Admin** — Web application for system administrators (managing users, restaurants, and categories)

---
---

## 📂 Models

The backend is built around several core entities that represent users, restaurants, orders, and related data.

### 👤 UserType
- **User** — Base user model (extended by Client and RestaurantAdmin).
- **Client** — End-user who places orders.
- **RestaurantAdmin** — Administrator of a specific restaurant.

### 📍 Address
Represents the delivery address of the client. Linked to `User` `Restaurant` and `Order`.

### 🛒 Cart
A temporary container that holds selected dishes before an order is placed.

### 🚚 Delivery
Contains delivery details, including delivery status, courier info, and estimated time.

### 🍽️ Dish
Represents a menu item offered by a restaurant.

### 🗂️ DishCategory
Categories for organizing dishes (e.g., "Drinks", "Main Course", "Desserts").

### 🔗 DishToOrder
Intermediate entity linking dishes to orders (many-to-many relationship).

### ⚙️ Element
Configurable elements (e.g., ingredients, additional components for dishes).

### 📂 FileInfo
Stores metadata about uploaded files (e.g., dish photos, restaurant logos).

### 🔔 Notification
Represents a real-time notification for order updates and system messages (via Socket.io).

### 🕒 OperatingMode
Defines restaurant opening/closing hours.

### 📝 Option
Additional options for dishes (e.g., "Extra Spicy", "Large Size").

### 📦 Order
Represents a placed order by the client, containing order items, delivery details, and status.

### ⭐ Rating
Stores client ratings and reviews for dishes and restaurants.

### 🍴 Restaurant
Represents a restaurant, linked to dishes, admins, and categories.

### 🏷️ RestaurantCategory
Categories for restaurants (e.g., "Korean", "Japanese", "Fast Food").

### 🔑 Role
Defines user roles and permissions (Client, RestaurantAdmin, Admin).


---

## ✨ Order Cycle
- Add dishes to cart
- Place an order
- Track orders by role (client / restaurant admin)
- Update order status in real-time

---

## 🛠 Tech Stack

| Layer            | Technology                 |
|------------------|----------------------------|
| Language         | Java 21                    |
| Framework        | Spring Boot                |
| Database         | MySQL                      |
| Migrations       | Flyway                     |
| API              | REST + Swagger (OpenAPI)   |
| Auth             | JWT + Spring Security      |
| Realtime         | WebSocket                  |
| File Storage     | AWS S3 or local filesystem |
| Hosting          | Railway                    |


---

## 🚀 How to Run the Project

### ✅ Run Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/Habsida-Projects/utown-backand-jun9/tree/main
   cd your-project

2. **Install MySQL**

Make sure you have MySQL 8+ installed and running.
   Create a database.
3. **Switch Profile to Local**

To run the application locally, simply set the active profile to `local` in `application-local.properties`:

```properties
spring.profiles.active=local
```

4. **Run Flyway migrations**

Flyway migrations run automatically when the application starts.  
If you want to run them manually, execute:

```bash
./mvnw flyway:migrate
```


4.**Configure environment variables**

The application uses environment variables for **Database**, **JWT** and **AWS** configuration.

Create a `.env` file in the project root (⚠️ this file should be added to `.gitignore`):

```env
# Database
URL=your_database_url
USERNAME=your_username
PASSWORD=your_password

# JWT
JWT_ACCESS_SECRET=REMOVED
JWT_REFRESH_SECRET=REMOVED
JWT_ACCESS_EXPIRATION_MS=900000        # 15 minutes
JWT_REFRESH_EXPIRATION_MS=604800000    # 7 days

# AWS S3 (optional for local development)
AWS_REGION=ap-southeast-2
AWS_S3_BUCKET=my-bucket
AWS_ACCESS_KEY=AKIAXXXXXXX
AWS_SECRET_KEY=xxxxxxxx
```
---


## 🚀 Deployment

1. **Switch Profile to Production**

To run the application in production, simply set the active profile to `production` in `application-production.properties`:

```properties
spring.profiles.active=production
```

- Free hosting option:
    - [Railway](https://railway.app)

- Swagger documentation available at:
  - https://<your-domain>/swagger-ui/index.html

