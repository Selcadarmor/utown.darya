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

## 🚀 Deployment

- Free hosting option:
    - [Railway](https://railway.app)

- Swagger documentation available at:
  - https://<your-domain>/swagger-ui/index.html

