INSERT INTO users (
    id, is_active, password, phoneNumber, created_at, updated_at, dtype, full_name, default_address, file_id, cart_id
) VALUES (
             DEFAULT, -- если у тебя ID с AUTO_INCREMENT
             true,
             '$2a$10$abcdef...', -- захешированный пароль bcrypt
             '+79990001122', -- username (номер телефона)
             NOW(),
             NOW(),
             'client', -- discriminator для Client
             'Иван Иванов',
             NULL,
             NULL,
             NULL
         );
INSERT INTO users (
    is_active,
    password,
    phoneNumber,
    created_at,
    updated_at,
    dtype,
    full_name,
    restaurant_id
) VALUES (
             true,
             '$2a$10$abcdef...', -- хеш пароля
             '+79991234567',
             NOW(),
             NOW(),
             'restaurant_admin', -- discriminator для RestaurantAdmin
             'Админ Иванов',
             5 -- id ресторана, который он админит
         );
INSERT INTO addresses (
    area,
    city,
    details,
    full_address,
    latitude,
    longitude,
    post_code,
    state,
    street,
    intercome_code,
    type_address,
    created_at,
    updated_at
) VALUES (
             'Центральный район',
             'Москва',
             'ул. Ленина, д. 10, кв. 15',
             'Россия, Москва, ул. Ленина, д. 10, кв. 15',
             55.7558,
             37.6173,
             '101000',
             'Москва',
             'Ленина',
             '123',
             1,
             NOW(),
             NOW()
         );
INSERT INTO carts (
    delivery_price,
    sum_order,
    total_dish,
    total_sum,
    created_at,
    updated_at
) VALUES (
             5.00,
             45.50,
             3,
             50.50,
             NOW(),
             NOW()
         );
INSERT INTO delivery (
    area,
    price,
    district,
    is_active,
    is_deleted,
    created_at,
    updated_at,
    restaurant_id
) VALUES (
             'Центральный район',
             150.00,
             'Московский',
             true,
             false,
             NOW(),
             NOW(),
             1 -- здесь ID ресторана, к которому привязываем доставку
         );
INSERT INTO dishes (
    description,
    is_active,
    is_deleted,
    price,
    sort,
    title,
    created_at,
    updated_at,
    file_id,
    dish_category_id,
    restaurant_id
) VALUES (
             'Вкусное куриное филе с гарниром',
             true,
             false,
             9900.00,  -- 99.00 в копейках
             1,
             'Куриное филе с рисом',
             NOW(),
             NOW(),
             2,    -- ID из таблицы file_info
             3,    -- ID из dish_category
             1     -- ID ресторана
         );
INSERT INTO dish_category (
    name,
    sort,
    is_active,
    created_at,
    updated_at,
    restaurant_id,
    file_id
) VALUES (
             'Горячие блюда',
             1,
             true,
             NOW(),
             NOW(),
             1,     -- id ресторана
             2      -- id картинки (file_info), если не нужно — поставь NULL
         );
-- Пример: блюдо в корзине
INSERT INTO dishes_to_order (
    count,
    sum,
    created_at,
    updated_at,
    cart_id,
    dish_id
) VALUES (
             2,
             15000.00,
             NOW(),
             NOW(),
             1,      -- cart_id
             3       -- dish_id
         );

-- После вставки можно вручную связать с элементами:
-- Предположим, ID вставленной записи = 10 (проверь через SELECT LAST_INSERT_ID() или аналог)
INSERT INTO dish_to_order_elements (dish_to_order_id, element_id) VALUES
                                                                      (10, 1),
                                                                      (10, 4);
INSERT INTO elements (
    name,
    description,
    price,
    is_active,
    is_deleted,
    created_at,
    updated_at
) VALUES
      ('Сыр Чеддер', 'Дополнительный сыр чеддер', 1500.00, true, false, NOW(), NOW()),
      ('Без лука', 'Исключить лук из блюда', 0.00, true, false, NOW(), NOW()),
      ('Больше мяса', 'Увеличенная порция мяса', 3000.00, true, false, NOW(), NOW());
