CREATE TABLE Users
(
    id         INT PRIMARY KEY auto_increment,
    name       VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(50),
    registered TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Security
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(50) UNIQUE NOT NULL,
    registered TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Orders
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    security_id INT          NOT NULL,
    quantity    INTEGER      NOT NULL,
    price       DOUBLE       NOT NULL,
    side        VARCHAR(255) NOT NULL,
    user_id     INT          NOT NULL,
    created     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_user_fk FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT orders_security_fk FOREIGN KEY (security_id) REFERENCES Security (id)
);

CREATE TABLE OrderHistory
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    order_id    INT          NOT NULL,
    security_id INT          NOT NULL,
    quantity    INTEGER      NOT NULL,
    price       DOUBLE       NOT NULL,
    side        VARCHAR(255) NOT NULL,
    user_id     INT          NOT NULL,
    created     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT order_history_user_fk FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT order_history_order_fk FOREIGN KEY (order_id) REFERENCES Orders (id),
    CONSTRAINT order_history_security_fk FOREIGN KEY (security_id) REFERENCES Security (id)
);

CREATE TABLE Matches
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    buying_order_id  INT,
    selling_order_id INT,
    price            DOUBLE NOT NULL,
    quantity         INT    NOT NULL,
    security_id      INT    NOT NULL,
    date_buyer       TIMESTAMP,
    date_seller      TIMESTAMP,
    deleted          BIT DEFAULT false,
    CONSTRAINT match_buying_fk FOREIGN KEY (buying_order_id) REFERENCES Users (id),
    CONSTRAINT match_selling_fk FOREIGN KEY (selling_order_id) REFERENCES Users (id),
    CONSTRAINT match_security_fk FOREIGN KEY (security_id) REFERENCES Security (id)
);


