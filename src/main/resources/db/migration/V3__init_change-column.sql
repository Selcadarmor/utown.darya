-- Переименовать колонку только если она существует
SET @column_exists := (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_name = 'users'
      AND column_name = 'phone_number'
      AND table_schema = DATABASE()
);

SET @rename_sql := IF(@column_exists > 0,
                      'ALTER TABLE users CHANGE phone_number username VARCHAR(20) NOT NULL;',
                      'SELECT "Column phone_number does not exist";'
                   );

PREPARE stmt FROM @rename_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Добавить уникальность только если её нет
SET @index_exists := (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_name = 'users'
      AND index_name = 'uc_users_username'
      AND table_schema = DATABASE()
);

SET @add_index_sql := IF(@index_exists = 0,
                         'ALTER TABLE users ADD CONSTRAINT uc_users_username UNIQUE (username);',
                         'SELECT "Index already exists";'
                      );

PREPARE stmt2 FROM @add_index_sql;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;