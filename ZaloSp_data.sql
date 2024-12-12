
/*Table company */
SELECT * FROM companys;
INSERT INTO companys(id,name) VALUE (1,N'Việt Vang ');
/*Table Event */
SELECT * FROM EVENTS;

UPDATE EVENTS
SET time_end_event = DATE_FORMAT(time_end_event, '%Y/%m/%d'),
    time_start_event = DATE_FORMAT(time_start_event, '%Y/%m/%d')
WHERE time_end_event = '0000-00-00' OR time_start_event = '0000-00-00';

INSERT INTO EVENTS(id,image,description,status,name,event_end,event_start,company_id) VALUE (3,'https://i.pinimg.com/736x/7a/98/f5/7a98f571e7b345a8576e0b23b9d15aaa.jpg',N'Đây sự kiện của công ty mẫu, mỗi sự kiện dành cho 1 app !',1 ,N'Lavoe va','2024/1/11','2022/1/13',1);

1. Đối tượng tham gia:
Tất cả các khách hàng của Nhà hàng Việt Vang và các địa điểm cộng tác có thể được áp dụng.

2. Thời gian tham gia:
Chương trình tích điểm đổi quà bắt đầu từ ngày 1/1/2021 đến ngày 30/6/2021.

3. Cách thức tham gia:
- Khách hàng có thể tham gia bằng cách quét hóa đơn của Nhà hàng Việt Vang và các địa điểm cộng tác.
- Mỗi hóa đơn sẽ được tích lũy điểm tương ứng với mức trị giá.
- Khách hàng có thể đổi các phần thưởng dựa trên số điểm tích lũy.

4. Phần thưởng:
- Các phần thưởng đổi sẽ nhận thưởng ngay khi đổi.
- Các phần thưởng đăng kí, hệ thống sẽ phản hồi ngay khi bạn trúng giải.

5. Cơ hội:
Chục tỷ đồng cho người trúng giải chính và hàng ngàn giải phụ.',1,N'Việt Vang tích điểm đổi thưởng','2024/1/11','2022/1/13',1);


/*Table*/
SELECT * FROM bills;
DROP TABLE bills;

/*Customers */
SELECT * FROM customers ;
INSERT INTO customers VALUE (1,'admin zalo','https://i.pinimg.com/564x/5f/40/6a/5f406ab25e8942cbe0da6485afd26b71.jpg','123123123123',N'admin zalo','0898308827');
INSERT INTO customers VALUE (2,'admin review  zalo','https://i.pinimg.com/564x/5f/40/6a/5f406ab25e8942cbe0da6485afd26b71.jpg','123123123123',N'zalo reviewer' ,'0831111111');

/*Table Product */
SELECT * FROM products;
INSERT INTO products(id,name,image,point,status) VALUE (10,N'Viet Vang Pure','https://vietvang.net/wp-content/uploads/2023/08/logo-preview.png',1010,1);
INSERT INTO products VALUE (2,N'Golden Viet Vang','https://vietvang.net/wp-content/uploads/2020/12/thiet-ke-web-wordpress.png',1000,TRUE);
INSERT INTO products VALUE (3,N'Viet Coffee','https://vietvang.net/wp-content/uploads/2023/07/thiet_ke_web_2-1-1.webp',1120,TRUE);
INSERT INTO products VALUE (4,N'Basic Dinner','https://vietvang.net/wp-content/uploads/2023/07/dich-vu-mobile-app-3-1.png',1340,TRUE);
INSERT INTO products VALUE (5,N'Combo Lux','https://i.pinimg.com/564x/51/2e/25/512e2506b08133070bce551ab755e6f1.jpg',1500,TRUE);
INSERT INTO products VALUE (6,N'Combo Premium','https://i.pinimg.com/564x/82/95/76/829576eda28ad571cfea14a6ea84044b.jpg',900,TRUE);
INSERT INTO products VALUE (7,N'Soul','https://i.pinimg.com/564x/9e/fa/90/9efa901c6148ff94454379b9e4c63b72.jpg',1100,TRUE);
INSERT INTO products VALUE (8,N'BeafSteak dat vang','https://i.pinimg.com/564x/9e/fa/90/9efa901c6148ff94454379b9e4c63b72.jpg',1200,TRUE);
INSERT INTO products VALUE (9,N'Admin Event','https://i.pinimg.com/564x/8a/98/71/8a9871d60ce812a86851891787e04af5.jpg',1000,TRUE);
INSERT INTO products VALUE (10,N'Ẹnoy Viet Vang','https://i.pinimg.com/736x/50/9a/dd/509add6d7c18084cda68b3b880f2c3a4.jpg',1230,TRUE);
INSERT INTO products VALUE (11,N'Demo App','https://i.pinimg.com/736x/e8/f2/d8/e8f2d8141fb1d9e8472e3d93b8e86954.jpg',1230,TRUE);
INSERT INTO products VALUE (12,N'Ẹnoy Viet Vang','https://i.pinimg.com/736x/50/9a/dd/509add6d7c18084cda68b3b880f2c3a4.jpg',1230,TRUE);

/*Table Rewards */
SELECT * FROM rewards;
INSERT INTO rewards VALUE (1,'https://i.pinimg.com/564x/7c/4b/8f/7c4b8ffe45e32c1435a6fd252f2b3bea.jpg',N'Máy sấy tóc Dyson ',500,20,2,3);
INSERT INTO rewards VALUE (2,'https://i.pinimg.com/564x/2a/6d/c8/2a6dc897d6e976284f5ade238e39d036.jpg',N'Máy lọc nước Kanguroo',200,10,1,3);
INSERT INTO rewards VALUE (3,'https://i.pinimg.com/564x/22/38/f9/2238f9e6e3ad4a97cede0b24557f59c7.jpg',N'Xe Winner X ',300,20,2,3);
INSERT INTO rewards VALUE (4,'https://i.pinimg.com/564x/4c/11/58/4c11583749b29dbc07ab1fdb9319fa32.jpg',N'Tour Du lịch trị gía 200.000.000',700,20,1,3);
INSERT INTO rewards VALUE (5,'https://i.pinimg.com/564x/89/f2/a4/89f2a487d0d01a5e17f9a47963d72e92.jpg',N'Tour Du lịch châu Âu bí ẩn ',1000,20,1,3);
INSERT INTO rewards VALUE (6,'https://i.pinimg.com/564x/84/cb/39/84cb39ac00e884524a1a1ec882ce2eec.jpg',N'Căn nhà khu private VinHomes',1200,20,2,3);
INSERT INTO rewards VALUE (7,'https://i.pinimg.com/564x/84/cb/39/84cb39ac00e884524a1a1ec882ce2eec.jpg',N'Căn nhà khu private VinHomes fake ',1200,20,1,3);

/*Customer Point */
SELECT* FROM customer_point;
INSERT INTO customer_point VALUE (1,1000000,1,3);
INSERT INTO customer_point VALUE (1,1000000,2,3);


insert into display_css(id,background1,background2,background3,background4,background5,button1,button2,button3,message_success,message_fail) value(1,'#006AF5','#ffffff','#A1E5EE','#D6D9DC','#86E3DD','#8F9499','#006AF5','#DC1F18','#34B764','#DC1F18');
