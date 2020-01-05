INSERT INTO category (id_category,name,super_category,picture) VALUES (1,'Computers', null, 'pc-asus-vivobook-s14-s430fa-intel-core-i5-8265u-4-6-ghz-4gb-ddr4-ssd-256gb-14-verde-intel-1.jpg');
INSERT INTO category (id_category,name,super_category,picture) VALUES (2,'Home', null, 'gettyimages-183881669-612x612.jpg');
INSERT INTO category (id_category,name,super_category,picture) VALUES (3,'Furnitures', 2, null);
INSERT INTO category (id_category,name,super_category,picture) VALUES (4,'Tools', 2, 'Herramienta.jpg');
INSERT INTO category (id_category,name,super_category,picture) VALUES (5,'Cellphones', null, null);
INSERT INTO product (id_product,id_category,name,description,weight,price,picture1,picture2,picture3) VALUES (1, 4,'Drill','et',8,7,'drill1.PNG','drill2.PNG','drill3.PNG');