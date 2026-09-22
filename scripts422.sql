-- ============================================================
-- scripts422.sql
-- Домашнее задание 4.2, Шаг 2
-- Создание таблиц person и car со связью ManyToOne
-- ============================================================

-- ------------------------------------------------------------
-- Сначала создаём таблицу car (родительскую),
-- потому что на неё будет ссылаться person
-- ------------------------------------------------------------
CREATE TABLE car (
                     id         BIGSERIAL PRIMARY KEY,            -- первичный ключ
                     brand      VARCHAR(100) NOT NULL,            -- марка (строка)
                     model      VARCHAR(100) NOT NULL,            -- модель (строка)
                     price      DECIMAL(12, 2) NOT NULL           -- стоимость (число с копейками)
);

-- ------------------------------------------------------------
-- Затем таблицу person с внешним ключом на car
-- ------------------------------------------------------------
CREATE TABLE person (
                        id          BIGSERIAL PRIMARY KEY,           -- первичный ключ
                        name        VARCHAR(100) NOT NULL,           -- имя (строка)
                        age         INT NOT NULL,                    -- возраст (число)
                        has_license BOOLEAN NOT NULL DEFAULT FALSE,  -- есть ли права (логический)
                        car_id      BIGINT,                          -- внешний ключ на car

                        CONSTRAINT fk_person_car
                            FOREIGN KEY (car_id)
                                REFERENCES car (id)
                                ON DELETE SET NULL
);