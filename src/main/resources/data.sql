SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM dish_to_order_elements;

-- 1. Роли
INSERT INTO roles (id, name, created_at, updated_at) VALUES
                                                         (1, 'ROLE_ADMIN', NOW(), NOW()),
                                                         (2, 'ROLE_CLIENT', NOW(), NOW()),
                                                         (3, 'ROLE_RESTAURANT_ADMIN', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 2. Файлы (mock)
INSERT INTO files (id, original_title, path, type, created_at, updated_at) VALUES
                                                                               (1, 'mock-image-1.jpg', '/mock/path/mock-image-1.jpg', 'image/jpeg', NOW(), NOW()),
                                                                               (2, 'mock-image-2.jpg', '/mock/path/mock-image-2.jpg', 'image/jpeg', NOW(), NOW()),
                                                                               (3, 'mock-document.pdf', '/mock/path/mock-document.pdf', 'application/pdf', NOW(), NOW())
ON DUPLICATE KEY UPDATE original_title = VALUES(original_title);

-- 3. Адреса
INSERT INTO addresses (
    id, area, city, details, full_address, latitude, longitude, post_code, state, street,
    intercome_code, type_address, created_at, updated_at, client_id
) VALUES (
             1, 'Центральный район', 'Москва', 'ул. Ленина, д. 10, кв. 15', 'Россия, Москва, ул. Ленина, д. 10, кв. 15',
             55.7558, 37.6173, '101000', 'Москва', 'Ленина', '123', 1, NOW(), NOW(), 1
         )
ON DUPLICATE KEY UPDATE full_address = VALUES(full_address), client_id = VALUES(client_id);

-- 4. Корзина
INSERT INTO carts (id, delivery_price, sum_order, total_dish, total_sum, created_at, updated_at) VALUES
    (1, 5.00, 45.50, 3, 50.50, NOW(), NOW())
ON DUPLICATE KEY UPDATE delivery_price = VALUES(delivery_price);

-- 5. Ресторан
INSERT INTO restaurant (
    id, delivery_time, description, facilities, is_recommended, min_order_amount, phone, rating, status, title,
    total_ratings, status_forced_changed, is_active, created_at, updated_at, address_id, file_id, restaurant_admin_id
) VALUES (
             1, '30 мин', 'Очень вкусная еда', 'WiFi, Парковка', TRUE, 15000.00, '+821012345678', 4.8,
             'OPEN', 'K-BBQ House', 127, FALSE, TRUE, NOW(), NOW(), 1, 2, 101
         )
ON DUPLICATE KEY UPDATE title = VALUES(title), restaurant_admin_id = VALUES(restaurant_admin_id);

-- 6. Клиент
INSERT INTO users (
    id, is_active, password, phone_number, created_at, updated_at, dtype, full_name, default_address, file_id, cart_id
) VALUES (
             1, TRUE, '$2a$10$haz8tbPPooj63WjWdL.mEup/VLBHop6ZA9L8B1e2zHAvBpBnq14Xe', '+79990001127', NOW(), NOW(),
             'client', 'Иван Иванов', 1, 1, 1
         )
ON DUPLICATE KEY UPDATE phone_number = VALUES(phone_number), default_address = VALUES(default_address), file_id = VALUES(file_id), cart_id = VALUES(cart_id);

-- 7. Админ
INSERT INTO users (
    id, is_active, phone_number, password, dtype, created_at, updated_at
) VALUES (
             100, TRUE, '01960124852', '$2a$10$haz8tbPPooj63WjWdL.mEup/VLBHop6ZA9L8B1e2zHAvBpBnq14Xe', 'User', NOW(), NOW()
         )
ON DUPLICATE KEY UPDATE phone_number = VALUES(phone_number);

-- 8. RestaurantAdmin
INSERT INTO users (
    id, is_active, phone_number, password, dtype, created_at, updated_at, full_name
) VALUES (
             101, TRUE, '01960124853', '$2a$10$haz8tbPPooj63WjWdL.mEup/VLBHop6ZA9L8B1e2zHAvBpBnq14Xe', 'RestaurantAdmin', NOW(), NOW(), 'Иван Петров'
         )
ON DUPLICATE KEY UPDATE phone_number = VALUES(phone_number), full_name = VALUES(full_name);

-- 9. user_roles для админа
INSERT INTO user_roles (user_id, role_id) VALUES (100, 1)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 10. user_roles для ресторанного админа
INSERT INTO user_roles (user_id, role_id) VALUES (101, 3)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 11. Категории ресторанов
INSERT INTO restaurant_category (id, name, sort, is_active, created_at, updated_at, file_id) VALUES
    (1, 'Корейская кухня', 1, TRUE, NOW(), NOW(), 3)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 12. restaurant - category связь
INSERT INTO restaurant_categories_link (restaurant_id, category_id) VALUES
    (1, 1)
ON DUPLICATE KEY UPDATE restaurant_id = VALUES(restaurant_id);

-- 13. Категории блюд
INSERT INTO dish_category (id, name, sort, is_active, created_at, updated_at, restaurant_id, file_id) VALUES
    (1, 'Горячие блюда', 1, TRUE, NOW(), NOW(), 1, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 14. Блюда
INSERT INTO dishes (
    id, description, is_active, is_deleted, price, sort, title, created_at, updated_at, file_id, dish_category_id, restaurant_id
) VALUES (
             1, 'Вкусное куриное филе с гарниром', TRUE, FALSE, 9900.00, 1, 'Куриное филе с рисом', NOW(), NOW(), 1, 1, 1
         )
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- 15. Опции
INSERT INTO options (
    id, name, is_required, min_value, max_value, is_active, created_at, updated_at, dish_id
) VALUES (
             1, 'Соусы', TRUE, 1, 2, TRUE, NOW(), NOW(), 1
         )
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 16. Элементы
INSERT INTO elements (
    id, name, description, price, is_active, is_deleted, created_at, updated_at
) VALUES
      (1, 'Кетчуп', 'Томатный соус', 0.50, TRUE, FALSE, NOW(), NOW()),
      (2, 'Чесночный соус', 'Сливочно-чесночный', 0.70, TRUE, FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 17. dishes_to_order
INSERT INTO dishes_to_order (
    id, count, sum, created_at, updated_at, cart_id, dish_id, order_id_in_dish
) VALUES (
             1, 2, 15000.00, NOW(), NOW(), 1, 1, NULL
         )
ON DUPLICATE KEY UPDATE count = VALUES(count);

-- 18. dish_to_order_elements
INSERT INTO dish_to_order_elements (dish_to_order_id, element_id) VALUES (1, 1), (1, 2);

-- 19. Доставка
INSERT INTO delivery (
    id, area, price, district, is_active, is_deleted, created_at, updated_at, restaurant_id
) VALUES (
             1, 'Центральный район', 150.00, 'Московский', TRUE, FALSE, NOW(), NOW(), 1
         )
ON DUPLICATE KEY UPDATE area = VALUES(area);

-- 20. Уведомления
INSERT INTO notifications (
    id, date, type_notification, type_order, type_restaurant, type_user,
    text, time_notification, time_order, time_restaurant, time_user, time_order_status,
    type_order_title, error_message, is_successful, created_at, updated_at, user_id
) VALUES (
             1, '2025-07-10', 1, 2, 1, 1,
             'Ваш заказ был успешно оформлен!', '12:00', '12:01', '12:02', '12:03', '12:04',
             'Оформление заказа', NULL, TRUE, NOW(), NOW(), 1
         )
ON DUPLICATE KEY UPDATE text = VALUES(text);

-- 21. Режим работы ресторана
INSERT INTO operating_mode (
    id, day_of_week, start, end, day_off, created_at, updated_at, restaurant_id
) VALUES
      (1, 1, '2025-07-14 09:00:00', '2025-07-14 21:00:00', FALSE, NOW(), NOW(), 1),
      (2, 2, '2025-07-15 09:00:00', '2025-07-15 21:00:00', FALSE, NOW(), NOW(), 1),
      (3, 3, '2025-07-16 09:00:00', '2025-07-16 21:00:00', FALSE, NOW(), NOW(), 1),
      (4, 4, '2025-07-17 09:00:00', '2025-07-17 21:00:00', FALSE, NOW(), NOW(), 1),
      (5, 5, '2025-07-18 09:00:00', '2025-07-18 21:00:00', FALSE, NOW(), NOW(), 1),
      (6, 6, NULL, NULL, TRUE, NOW(), NOW(), 1),
      (7, 7, NULL, NULL, TRUE, NOW(), NOW(), 1)
ON DUPLICATE KEY UPDATE day_of_week = VALUES(day_of_week);

-- 22. Заказ (исправлен time_of_accepted)
INSERT INTO orders (
    id, area, city, client_phone, date, delivery_price, delivery_time,
    details, full_address, is_paid, latitude, longitude, note_for_courier,
    number, order_price, payment, postcode, restaurant_phone, state,
    status, street, time, time_of_accepted, time_of_delivery, time_of_sending,
    total_sum, type_address, cooking_time, delivery_status, end_time_of_cooking,
    intercom_code, created_at, updated_at, restaurant_id, client_id
) VALUES (
             1, 'Gangnam', 'Seoul', '010-1234-5678', '2025-07-10', 3000.00, '30 min',
             'Leave at door', '123 Gangnam-daero', TRUE, 37.4979, 127.0276, 'Ring once',
             'ORD-001', 15000.00, 'CARD', '12345', '02-9876-5432', 'Seoul',
             'COMPLETED', 'Gangnam-daero', '12:00:00', '2025-07-10 11:55:00', '2025-07-10 12:30:00', '2025-07-10 12:05:00',
             18000.00, 1, 15, 'Preparing', '2025-07-10 12:20:00',
             '1234', NOW(), NOW(), 1, 1
         )
ON DUPLICATE KEY UPDATE number = VALUES(number);

-- 23. Рейтинг
INSERT INTO ratings (
    id, restaurant_id, client_id, created_at, updated_at
) VALUES (
             1, 1, 1, NOW(), NOW()
         )
ON DUPLICATE KEY UPDATE id = VALUES(id);

SET FOREIGN_KEY_CHECKS = 1;
