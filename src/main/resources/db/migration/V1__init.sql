CREATE TABLE addresses
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    state          VARCHAR(255)          NULL,
    city           VARCHAR(100)          NOT NULL,
    area           VARCHAR(100)          NOT NULL,
    street         VARCHAR(100)          NOT NULL,
    details        VARCHAR(100)          NOT NULL,
    full_address   VARCHAR(255)          NULL,
    latitude       FLOAT                 NULL,
    longitude      FLOAT                 NULL,
    post_code      VARCHAR(255)          NULL,
    intercome_code VARCHAR(100)          NOT NULL,
    type_address   INT                   NULL,
    created_at     datetime              NULL,
    updated_at     datetime              NULL,
    client_id      BIGINT                NULL,
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE carts
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    delivery_price DECIMAL(10, 2)        NULL,
    sum_order      DECIMAL(7, 2)         NULL,
    total_dish     INT                   NULL,
    total_sum      DECIMAL(15, 2)        NULL,
    created_at     datetime              NULL,
    updated_at     datetime              NULL,
    CONSTRAINT pk_carts PRIMARY KEY (id)
);

CREATE TABLE delivery
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    district      VARCHAR(50)           NULL,
    area          VARCHAR(50)           NULL,
    price         DECIMAL(10, 2)        NULL,
    is_active     BIT(1)                NULL,
    is_deleted    BIT(1)                NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    restaurant_id BIGINT                NULL,
    CONSTRAINT pk_delivery PRIMARY KEY (id)
);

CREATE TABLE dish_category
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100)          NOT NULL,
    sort          INT                   NULL,
    is_active     BIT(1)                NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    restaurant_id BIGINT                NULL,
    file_id       BIGINT                NULL,
    CONSTRAINT pk_dish_category PRIMARY KEY (id)
);

CREATE TABLE dish_to_order_elements
(
    dish_to_order_id BIGINT NOT NULL,
    element_id       BIGINT NOT NULL
);

CREATE TABLE dishes
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    `description`    VARCHAR(700)          NULL,
    is_active        BIT(1)                NULL,
    is_deleted       BIT(1)                NULL,
    price            DECIMAL(10, 2)        NULL,
    sort             INT                   NULL,
    title            VARCHAR(170)          NOT NULL,
    created_at       datetime              NULL,
    updated_at       datetime              NULL,
    file_id          BIGINT                NULL,
    dish_category_id BIGINT                NULL,
    restaurant_id    BIGINT                NULL,
    CONSTRAINT pk_dishes PRIMARY KEY (id)
);

CREATE TABLE dishes_to_order
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    count            INT                   NULL,
    sum              DECIMAL(12, 2)        NULL,
    created_at       datetime              NULL,
    updated_at       datetime              NULL,
    cart_id          BIGINT                NULL,
    dish_id          BIGINT                NULL,
    order_id_in_dish BIGINT                NULL,
    CONSTRAINT pk_dishes_to_order PRIMARY KEY (id)
);

CREATE TABLE elements
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(170)          NOT NULL,
    `description` VARCHAR(500)          NULL,
    price         DECIMAL(10, 2)        NOT NULL,
    is_active     BIT(1)                NULL,
    is_deleted    BIT(1)                NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    option_id     BIGINT                NULL,
    CONSTRAINT pk_elements PRIMARY KEY (id)
);

CREATE TABLE favorites
(
    client_id     BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT pk_favorites PRIMARY KEY (client_id, restaurant_id)
);

CREATE TABLE files
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    original_title VARCHAR(255)          NULL,
    `path`         VARCHAR(255)          NULL,
    type           VARCHAR(255)          NULL,
    created_at     datetime              NULL,
    updated_at     datetime              NULL,
    CONSTRAINT pk_files PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    date              VARCHAR(255)          NULL,
    text              VARCHAR(255)          NULL,
    time_notification VARCHAR(255)          NULL,
    type_order_title  VARCHAR(255)          NULL,
    error_message     VARCHAR(255)          NULL,
    is_successful     BIT(1)                NULL,
    created_at        datetime              NULL,
    updated_at        datetime              NULL,
    user_id           BIGINT                NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE operating_mode
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    day_of_week   INT                   NULL,
    start_time    time                  NULL,
    end_time      time                  NULL,
    day_off       BIT(1)                NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    restaurant_id BIGINT                NULL,
    CONSTRAINT pk_operating_mode PRIMARY KEY (id)
);

CREATE TABLE options
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(200)          NOT NULL,
    is_required BIT(1)                NULL,
    min_value   INT                   NULL,
    max_value   INT                   NULL,
    is_active   BIT(1)                NULL,
    created_at  datetime              NULL,
    updated_at  datetime              NULL,
    dish_id     BIGINT                NULL,
    CONSTRAINT pk_options PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    area                VARCHAR(255)          NULL,
    city                VARCHAR(255)          NULL,
    client_phone        VARCHAR(255)          NULL,
    date                date                  NULL,
    delivery_price      DECIMAL               NULL,
    details             VARCHAR(255)          NULL,
    full_address        VARCHAR(255)          NULL,
    is_paid             BIT(1)                NULL,
    latitude            FLOAT                 NULL,
    longitude           FLOAT                 NULL,
    note_for_courier    VARCHAR(255)          NULL,
    number              VARCHAR(255)          NULL,
    order_price         DECIMAL               NULL,
    payment             VARCHAR(255)          NULL,
    postcode            VARCHAR(255)          NULL,
    restaurant_phone    VARCHAR(255)          NULL,
    state               VARCHAR(255)          NULL,
    order_status        VARCHAR(30)           NULL,
    street              VARCHAR(255)          NULL,
    time_of_accepted    time                  NULL,
    time_of_delivery    VARCHAR(255)          NULL,
    time_of_sending     VARCHAR(255)          NULL,
    total_sum           DECIMAL               NULL,
    type_address        INT                   NULL,
    cooking_time        INT                   NULL,
    delivery_status     SMALLINT              NULL,
    end_time_of_cooking VARCHAR(255)          NULL,
    intercom_code       VARCHAR(255)          NULL,
    created_at          datetime              NULL,
    updated_at          datetime              NULL,
    restaurant_id       BIGINT                NULL,
    client_id           BIGINT                NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE ratings
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    grade         DOUBLE                NULL,
    restaurant_id BIGINT                NULL,
    client_id     BIGINT                NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    CONSTRAINT pk_ratings PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    token           VARCHAR(600)          NOT NULL,
    expiration_time datetime              NOT NULL,
    created_at      datetime              NULL,
    updated_at      datetime              NULL,
    user_id         BIGINT                NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

CREATE TABLE restaurant
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    delivery_time         VARCHAR(10)           NULL,
    `description`         LONGTEXT              NULL,
    facilities            VARCHAR(255)          NULL,
    is_recommended        BIT(1)                NULL,
    min_order_amount      DECIMAL(10, 2)        NULL,
    phone                 VARCHAR(20)           NULL,
    rating                DECIMAL(15, 2)        NULL,
    restaurant_status     VARCHAR(255)          NULL,
    title                 VARCHAR(170)          NULL,
    total_ratings         INT                   NULL,
    status_forced_changed BIT(1)                NULL,
    is_active             BIT(1)                NULL,
    created_at            datetime              NULL,
    updated_at            datetime              NULL,
    address_id            BIGINT                NULL,
    file_id               BIGINT                NULL,
    CONSTRAINT pk_restaurant PRIMARY KEY (id)
);

CREATE TABLE restaurant_categories_link
(
    category_id   BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT pk_restaurant_categories_link PRIMARY KEY (category_id, restaurant_id)
);

CREATE TABLE restaurant_category
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NULL,
    sort       INT                   NULL,
    is_active  BIT(1)                NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    file_id    BIGINT                NULL,
    CONSTRAINT pk_restaurant_category PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    dtype           VARCHAR(31)           NULL,
    is_active       BIT(1)                NULL,
    password        VARCHAR(255)          NOT NULL,
    phone_number    VARCHAR(20)           NOT NULL,
    created_at      datetime              NULL,
    updated_at      datetime              NULL,
    full_name       VARCHAR(170)          NULL,
    default_address BIGINT                NULL,
    file_id         BIGINT                NULL,
    cart_id         BIGINT                NULL,
    restaurant_id   BIGINT                NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE dish_category
    ADD CONSTRAINT uc_dish_category_file UNIQUE (file_id);

ALTER TABLE dish_category
    ADD CONSTRAINT uc_dish_category_name UNIQUE (name);

ALTER TABLE dish_to_order_elements
    ADD CONSTRAINT uc_dish_to_order_elements_element UNIQUE (element_id);

ALTER TABLE dishes
    ADD CONSTRAINT uc_dishes_file UNIQUE (file_id);

ALTER TABLE ratings
    ADD CONSTRAINT uc_ratings_client UNIQUE (client_id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);

ALTER TABLE restaurant
    ADD CONSTRAINT uc_restaurant_address UNIQUE (address_id);

ALTER TABLE restaurant_category
    ADD CONSTRAINT uc_restaurant_category_file UNIQUE (file_id);

ALTER TABLE restaurant_category
    ADD CONSTRAINT uc_restaurant_category_name UNIQUE (name);

ALTER TABLE restaurant
    ADD CONSTRAINT uc_restaurant_file UNIQUE (file_id);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_cart UNIQUE (cart_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phonenumber UNIQUE (phone_number);

ALTER TABLE users
    ADD CONSTRAINT uc_users_restaurant UNIQUE (restaurant_id);

ALTER TABLE addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_CLIENT FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE delivery
    ADD CONSTRAINT FK_DELIVERY_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE dishes
    ADD CONSTRAINT FK_DISHES_ON_DISH_CATEGORY FOREIGN KEY (dish_category_id) REFERENCES dish_category (id);

ALTER TABLE dishes
    ADD CONSTRAINT FK_DISHES_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE dishes
    ADD CONSTRAINT FK_DISHES_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE dishes_to_order
    ADD CONSTRAINT FK_DISHES_TO_ORDER_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE dishes_to_order
    ADD CONSTRAINT FK_DISHES_TO_ORDER_ON_DISH FOREIGN KEY (dish_id) REFERENCES dishes (id);

ALTER TABLE dishes_to_order
    ADD CONSTRAINT FK_DISHES_TO_ORDER_ON_ORDER_ID_IN_DISH FOREIGN KEY (order_id_in_dish) REFERENCES orders (id);

ALTER TABLE dish_category
    ADD CONSTRAINT FK_DISH_CATEGORY_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE dish_category
    ADD CONSTRAINT FK_DISH_CATEGORY_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE elements
    ADD CONSTRAINT FK_ELEMENTS_ON_OPTION FOREIGN KEY (option_id) REFERENCES options (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE operating_mode
    ADD CONSTRAINT FK_OPERATING_MODE_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE options
    ADD CONSTRAINT FK_OPTIONS_ON_DISH FOREIGN KEY (dish_id) REFERENCES dishes (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE restaurant_category
    ADD CONSTRAINT FK_RESTAURANT_CATEGORY_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE restaurant
    ADD CONSTRAINT FK_RESTAURANT_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES addresses (id);

ALTER TABLE restaurant
    ADD CONSTRAINT FK_RESTAURANT_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_RESTAURANT FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE dish_to_order_elements
    ADD CONSTRAINT fk_distoordele_on_dish_to_order FOREIGN KEY (dish_to_order_id) REFERENCES dishes_to_order (id);

ALTER TABLE dish_to_order_elements
    ADD CONSTRAINT fk_distoordele_on_element FOREIGN KEY (element_id) REFERENCES elements (id);

ALTER TABLE favorites
    ADD CONSTRAINT fk_favorites_on_client FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE favorites
    ADD CONSTRAINT fk_favorites_on_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE restaurant_categories_link
    ADD CONSTRAINT fk_rescat_on_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE restaurant_categories_link
    ADD CONSTRAINT fk_rescat_on_restaurant_category FOREIGN KEY (category_id) REFERENCES restaurant_category (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);