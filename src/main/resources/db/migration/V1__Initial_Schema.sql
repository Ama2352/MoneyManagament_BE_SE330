CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    jwt_id VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    invalidated BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX ix_category_name ON categories(name);
CREATE INDEX ix_category_userid ON categories(user_id);

CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    wallet_name VARCHAR(255) NOT NULL,
    balance NUMERIC(20,4) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX ix_wallet_walletname ON wallets(wallet_name);
CREATE INDEX ix_wallet_userid ON wallets(user_id);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(20,4) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP NOT NULL,
    type VARCHAR(20) NOT NULL,
    wallet_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX ix_transaction_walletid ON transactions(wallet_id);
CREATE INDEX ix_transaction_categoryid ON transactions(category_id);
CREATE INDEX ix_transaction_transactiondate ON transactions(transaction_date);
CREATE INDEX ix_transaction_type ON transactions(type);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users(id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users(id)
);