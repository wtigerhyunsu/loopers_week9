CREATE INDEX idx_ps_like_count_product  ON product_status (like_count DESC, product_id);
CREATE INDEX idx_product_brand_id ON product (brand_id);
CREATE INDEX idx_stock_product_id ON stock (product_id);
