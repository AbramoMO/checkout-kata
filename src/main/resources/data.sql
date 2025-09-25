INSERT INTO items(name, price_in_minor) VALUES
  ('APPLE', 30),
  ('BANANA', 50),
  ('PEACH', 60),
  ('KIWI', 20);

INSERT INTO offers(id, item, metadata, usage_limit) VALUES
  ('2_APPLE_FOR_45',  'APPLE',  '{"type":"N_FOR_FIXED","n":2,"bundlePrice":45}',  NULL),
  ('3_BANANA_FOR_130','BANANA', '{"type":"N_FOR_FIXED","n":3,"bundlePrice":130}', 1);
