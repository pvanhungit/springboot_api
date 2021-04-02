INSERT INTO category
(category_id, "name", created_by, created_at, updated_by, updated_at)
VALUES(1, 'sport', 1, '2021-01-04 23:01:50.000', 1, '2021-01-04 23:01:57.000');

INSERT INTO account
(user_id, phone_number, email, first_name, last_name, user_keycloak_id, profile_img_url, is_enabled, is_subscribing, reward, reward_name_id, created_by, created_at, updated_by, updated_at)
VALUES(1, '0123456789', 'test@test.test', 'michael', 'jordan', '7f9af5e9-0bda-4feb-ae76-ccbe140ce35f', NULL, false, false, NULL, NULL, 1, '2021-01-04 23:15:03.240', 1, '2021-01-04 23:15:03.240');

INSERT INTO post
(post_id, author_id, title, img, "content", tags, category_id, up_vote, down_vote, created_at, updated_by, updated_at)
VALUES(2, 1, 'Kodak: Ông vua một thời của ngành nhiếp ảnh chật vật mưu sinh vì chậm đổi mới',
'{https://genk.mediacdn.vn/139269124445442048/2021/1/3/photo-1-1609613915965611278902.jpg,https://genk.mediacdn.vn/139269124445442048/2021/1/3/photo-3-1609613919408784165250.jpg}',
'Những chiếc máy ảnh cùng những cuộn phim của Kodak được nhiều người đánh giá cao cả về chất lượng lẫn giá cả, đem lại cho công ty vị thế ông hoàng trong suốt một thời gian dài. Tuy nhiên, việc chậm chạp thay đổi so với thị hiếu khách hàng cùng sự lên ngôi của những chiếc điện thoại thông minh đã đặt dấu chấm hết cho Kodak.\nĐược thành lập từ cách đây hơn một trăm năm bởi nhà kinh doanh George Eastman, mục tiêu của Kodak là thay đổi cách mọi người chụp ảnh. Cái tên Kodak và biểu tượng của công ty xuất phát từ niềm đam mê đặc biệt của ông chủ với chữ K, vốn được George Eastman mô tả là chữ cái mạnh mẽ và sắc sảo. Cái tên Kodak tuy đơn giản, ngắn gọn nhưng gây được hứng thú và ghi dấu ấn đậm nét trong lòng công chúng.',
'{"filmphotography", "analog", "kodak"}', NULL, 0, 0, '2021-01-04 23:40:38.445', NULL, '2021-01-04 23:40:38.445');
