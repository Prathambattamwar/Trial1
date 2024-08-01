insert into public.category (id, description, name)
values
    (1, 'Electronics and gadgets', 'Electronics'),
    (2, 'Books of various genres', 'Books'),
    (3, 'Household and kitchen items', 'Home & Kitchen'),
    (4, 'Clothing and accessories', 'Fashion'),
    (5, 'Health and personal care products', 'Health & Beauty'),
    (6, 'Sporting goods and outdoor equipment', 'Sports & Outdoors');

-- Insert data into the product table with corresponding category_id values
insert into public.product (id, description, name, available_quantity, price, category_id)
values
    (nextval('product_seq'), 'Smartphone with 128GB storage', 'Smartphone', 50, 699.99, 1),
    (nextval('product_seq'), 'Thriller novel', 'Book', 200, 15.99, 2),
    (nextval('product_seq'), 'Blender with multiple settings', 'Blender', 30, 89.99, 3),
    (nextval('product_seq'), 'Men casual shirt', 'Casual Shirt', 150, 29.99, 4),
    (nextval('product_seq'), 'Women running shoes', 'Running Shoes', 80, 59.99, 4),
    (nextval('product_seq'), 'Organic shampoo', 'Shampoo', 120, 12.99, 5),
    (nextval('product_seq'), 'Electric toothbrush', 'Toothbrush', 60, 49.99, 5),
    (nextval('product_seq'), 'Yoga mat', 'Yoga Mat', 200, 19.99, 6),
    (nextval('product_seq'), 'Mountain bike', 'Bike', 10, 599.99, 6);

-- Select data to verify inserts
select * from category;
select * from product;