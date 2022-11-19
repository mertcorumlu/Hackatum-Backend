CREATE TABLE Users
(
    id         INT PRIMARY KEY auto_increment,
    name       VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(50)         NOT NULL,
    registered TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance    DOUBLE              NOT NULL DEFAULT 0
);

CREATE TABLE Cards
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    card_id   VARCHAR(255) UNIQUE NOT NULL,
    name      VARCHAR(255)        NOT NULL,
    rarity    VARCHAR(30)         NOT NULL,
    image_url VARCHAR(255)        NOT NULL
);

CREATE TABLE UserCards
(
    user_id INT          NOT NULL,
    card_id VARCHAR(255) NOT NULL,
    count   INT          NOT NULL,
    PRIMARY KEY (user_id, card_id),
    CONSTRAINT usercards_user_fk FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT usercards_card_fk FOREIGN KEY (card_id) REFERENCES Cards (card_id)
);

CREATE TABLE MasterOrders
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    card_id   VARCHAR(255) NOT NULL,
    quantity  INTEGER      NOT NULL,
    price     DOUBLE       NOT NULL,
    side      BIT          NOT NULL,
    user_id   INT          NOT NULL,
    created   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed BIT          NOT NULL DEFAULT false,
    CONSTRAINT orders_user_fk FOREIGN KEY (user_id) REFERENCES Users (id),
    CONSTRAINT orders_card_fk FOREIGN KEY (card_id) REFERENCES Cards (card_id)
);

CREATE TABLE SnapshotOrders
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    master_order_id INT UNIQUE NOT NULL,
    quantity        INTEGER    NOT NULL,
    CONSTRAINT master_order_fk FOREIGN KEY (master_order_id) REFERENCES MasterOrders (id)
);

CREATE TABLE ChildOrders
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT       NOT NULL,
    quantity INTEGER   NOT NULL,
    price    DOUBLE    NOT NULL,
    created  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT order_history_order_fk FOREIGN KEY (order_id) REFERENCES MasterOrders (id)
);

CREATE TABLE Matches
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    buying_order_id  INT NOT NULL,
    selling_order_id INT NOT NULL,
    CONSTRAINT match_buying_fk FOREIGN KEY (buying_order_id) REFERENCES ChildOrders (id),
    CONSTRAINT match_selling_fk FOREIGN KEY (selling_order_id) REFERENCES ChildOrders (id)
);
