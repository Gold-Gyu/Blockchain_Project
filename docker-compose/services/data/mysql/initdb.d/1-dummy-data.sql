USE `eticket`;

INSERT INTO
    `user` (
        `id`,
        `username`,
        `password`,
        `nickname`,
        `email`,
        `role`
    )
VALUES (
        1,
        "SSAFYC203",
        "SSAFYC203",
        "SSAFYC203",
        "ssafy@ssafy.com",
        "GUEST"
    );

INSERT INTO
    `concert_hall` (
        `concert_hall_id`,
        `name`,
        `seat_count`
    )
VALUES (1, "올림픽 공원", 80);

INSERT INTO
    `section` (
        `section_id`,
        `name`,
        `concert_hall_id`,
        `section_seat_count`
    )
VALUES (1, "A", 1, 20), (2, "B", 1, 20), (3, "C", 1, 20), (4, "D", 1, 20);

INSERT INTO
    `performance` (
        `performance_id`,
        `title`,
        `cast`,
        `description`,
        `genre`,
        `poster_image_path`,
        `running_time`,
        `ticketing_open_date_time`,
        `concert_hall_id`,
        `user_id`,
        `detail_image_path`
    )
VALUES (
        1,
        "2023 라포엠 단독콘서트",
        "라포엠",
        "라포엠 콘서트",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013495_p.gif&w=750&q=75",
        120,
        '2023-10-1 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23013495-02.jpg"
    ), (
        2,
        "2023 이승환 콘서트",
        "이승환",
        "이승환 콘서트",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013154_p.gif&w=750&q=75",
        120,
        '2023-10-8 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23013590-01.jpg"
    ), (
        3,
        "그랜드 민트 페스티벌 2023",
        "민트",
        "페스티벌",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011055_p.gif&w=750&q=75",
        120,
        '2023-10-8 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23011055-06.jpg"
    ), (
        4,
        "2023서귀포글로컬페스타-제주",
        "제주",
        "페스티벌",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23013039_p.gif&w=750&q=75",
        120,
        '2023-10-8 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23013039-09.jpg"
    ), (
        5,
        "2023 폴킴 단독 콘서트",
        "폴킴",
        "폴킴 콘서트",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23011967_p.gif&w=750&q=75",
        120,
        '2023-10-8 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23011967-05.jpg"
    ), (
        6,
        "LOVE IN SEOUL 2023",
        "권진아&샘김",
        "권진아 샘김 콘서트",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2FP0%2FP0003604_p.gif&w=750&q=75",
        120,
        '2023-10-8 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/P0003604-02.jpg"
    ), (
        7,
        "임영웅 콘서트 IM HERO TOUR 2023 - 서울",
        "임영웅",
        "임영웅 콘서트",
        "CONCERT",
        "https://tickets.interpark.com/contents/_next/image?url=https%3A%2F%2Fticketimage.interpark.com%2FPlay%2Fimage%2Flarge%2F23%2F23012698_p.gif&w=1200&q=75",
        120,
        '2023-10-1 00:00:00',
        1,
        1,
        "https://ticketimage.interpark.com/Play/image/etc/23/23012698-06.jpg"
    );

INSERT INTO
    `performance_schedule` (
        `start_date_time`,
        `performance_id`
    )
VALUES ('2023-10-10 18:00:00', 1), ('2023-10-12 18:00:00', 1), ('2023-10-10 18:00:00', 2), ('2023-10-12 18:00:00', 2), ('2023-10-10 18:00:00', 3), ('2023-10-12 18:00:00', 3), ('2023-10-10 18:00:00', 4), ('2023-10-12 18:00:00', 4), ('2023-10-10 18:00:00', 5), ('2023-10-12 18:00:00', 5), ('2023-10-10 18:00:00', 6), ('2023-10-12 18:00:00', 6), ('2023-10-10 18:00:00', 7), ('2023-10-12 18:00:00', 7);

INSERT INTO
    `seat_class` (
        `seat_class_id`,
        `class_name`,
        `price`,
        `performance_id`
    )
VALUES (1, 'S', 150000, 1), (2, 'A', 80000, 1);

INSERT INTO
    `section_and_seat_class_relation` (`section_id`, `seat_class_id`)
VALUES (1, 1), (2, 1), (3, 2), (4, 2);

INSERT INTO
    `seat` (
        `seat_id`,
        `section_id`,
        `number`
    )
VALUES (1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 4), (5, 1, 5), (6, 1, 6), (7, 1, 7), (8, 1, 8), (9, 1, 9), (10, 1, 10), (11, 1, 11), (12, 1, 12), (13, 1, 13), (14, 1, 14), (15, 1, 15), (16, 1, 16), (17, 1, 17), (18, 1, 18), (19, 1, 19), (20, 1, 20), (21, 2, 21), (22, 2, 22), (23, 2, 23), (24, 2, 24), (25, 2, 25), (26, 2, 26), (27, 2, 27), (28, 2, 28), (29, 2, 29), (30, 2, 30), (31, 2, 31), (32, 2, 32), (33, 2, 33), (34, 2, 34), (35, 2, 35), (36, 2, 36), (37, 2, 37), (38, 2, 38), (39, 2, 39), (40, 2, 40), (41, 3, 41), (42, 3, 42), (43, 3, 43), (44, 3, 44), (45, 3, 45), (46, 3, 46), (47, 3, 47), (48, 3, 48), (49, 3, 49), (50, 3, 50), (51, 3, 51), (52, 3, 52), (53, 3, 53), (54, 3, 54), (55, 3, 55), (56, 3, 56), (57, 3, 57), (58, 3, 58), (59, 3, 59), (60, 3, 60), (61, 4, 61), (62, 4, 62), (63, 4, 63), (64, 4, 64), (65, 4, 65), (66, 4, 66), (67, 4, 67), (68, 4, 68), (69, 4, 69), (70, 4, 70), (71, 4, 71), (72, 4, 72), (73, 4, 73), (74, 4, 74), (75, 4, 75), (76, 4, 76), (77, 4, 77), (78, 4, 78), (79, 4, 79), (80, 4, 80);