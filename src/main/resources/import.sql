INSERT INTO category (id_category,name,super_category,picture) VALUES (1,'Computers', null, 'pc-asus-vivobook-s14-s430fa-intel-core-i5-8265u-4-6-ghz-4gb-ddr4-ssd-256gb-14-verde-intel-1.jpg');
INSERT INTO category (id_category,name,super_category,picture) VALUES (2,'Home', null, 'animated-house.png');
INSERT INTO category (id_category,name,super_category,picture) VALUES (3,'Furnitures', 2, null);
INSERT INTO category (id_category,name,super_category,picture) VALUES (4,'Tools', 2, 'tools.jpg');
INSERT INTO category (id_category,name,super_category,picture) VALUES (5,'Cellphones', null, null);
INSERT INTO product (id_product,id_category,name,description,weight,price,picture1,picture2,picture3) VALUES (1, 4,'Drill','Mulitfunctional drill',8,7,'drill1.PNG','drill2.PNG','drill3.PNG');
INSERT INTO product (id_product,id_category,name,description,weight,price,picture1,picture2,picture3) VALUES (2, 4,'Hammer','Basic hammer',3,4, null, null, null);