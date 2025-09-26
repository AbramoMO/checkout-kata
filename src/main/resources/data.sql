INSERT INTO items(name, price_in_minor) VALUES
  ('APPLE', 30),
  ('BANANA', 50),
  ('PEACH', 60),
  ('KIWI', 20);

INSERT INTO offers(type, item, metadata, usage_limit) VALUES
  ('N_FOR_FIXED', 'APPLE',  '{"n":2,"bundlePriceInMinor":45}',  NULL),
  ('N_FOR_FIXED', 'BANANA', '{"n":3,"bundlePriceInMinor":130}', 1),
  ('N_FOR_FIXED', 'BANANA', '{"n":2,"bundlePriceInMinor":90}', 1);
