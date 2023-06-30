create table catalogs
(id int primary key auto_increment,
 catalog_name varchar(50));

create table categories
(id int primary key auto_increment,
 category_name varchar(50),
 catalog_id int,
 constraint fk_categories_catalog foreign key (catalog_id) references catalogs(id)
);

create table products
(id int primary key auto_increment,
 product_name varchar(50),
 price double,
 stock int,
 category_id int,
 constraint fk_products_catagory foreign key (category_id) references categories(id)
);

insert into catalogs (catalog_name)
values
    ('MyShop');

insert into categories (category_name, catalog_id)
values
    ('groceries', 1),
    ('electronics',1),
    ('toys',1),
    ('cosmetics',1),
    ('beverages',1);

insert into products (product_name, price, stock, category_id)
values
    ('carrot', 15, 33,1),
    ('bananas', 33, 50,1),
    ('ananas', 25, 30,1),
    ('apple', 25, 30,1),
    ('onion', 12, 40,1),
    ('oranges', 30, 21,1),
    ('potatoes', 18, 50,1),
    ('train', 80, 20,3),
    ('car', 80, 15,3),
    ('lego', 50, 30,3),
    ('dinosaur', 80, 30,3),
    ('teddyBear', 50,30,3),
    ('barbi', 20,90,3),
    ('Coca Cola', 5, 105,5),
    ('Pepsi', 4.5, 99,5),
    ('fanta', 5, 80,5),
    ('stella artois', 9, 50,5);

create table Orders
(
    id int primary key auto_increment,
    product_id int,
    order_Status varchar(15),
    issued_Date varchar(50),
    confirmed_Date varchar(50),
    completed_Date varchar(50),
    constraint fk_orders_product foreign key (product_id) references products(id)
);
