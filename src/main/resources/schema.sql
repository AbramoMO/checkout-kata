CREATE TABLE items (
  name VARCHAR(255) PRIMARY KEY,
  price_in_minor INT NOT NULL
);

CREATE TABLE offers (
  id VARCHAR(255) PRIMARY KEY,
  item VARCHAR(255) NOT NULL,
  metadata CLOB,
  usage_limit INT,
  CONSTRAINT fk_offers_item
    FOREIGN KEY (item) REFERENCES items(name)
);
