/*test data used for JUnit tests*/

Use hsbcbooking;

INSERT INTO location VALUES
('Richmond Westminster Red', '5991 Jacombs Rd, Richmond, BC V6V 2C2');

INSERT INTO desk VALUES
('2.A.201', 'Richmond Westminster Red', 2, 'A', '1.A.A');

INSERT INTO admin VALUES ('43868488'), ('12345678'), ('11112222');

INSERT INTO employee VALUES
('11112222','Jane Doe','Development Team','117781234567', 'JANEDOE@GMAIL.COM'),
('12345678','Peter Patterson','Development Team', '116041234567','PPATTERSON@GMAIL.COM');


INSERT INTO deskreservation (desk_id, location_name, employee, status, create_datetime, start_datetime, end_datetime) VALUES
('1.A.104', 'Vancouver Broadway Green', '11112222', 'reserved', '2017-01-01 00:00:00','2017-06-02 03:00:00','2017-06-05 09:00:00'),
('1.A.110', 'Vancouver Broadway Green', '12345678', 'reserved', '2017-01-01 00:00:00', '2017-05-22 12:34:56', '2017-05-24 21:43:55'),
('1.A.102', 'Vancouver Broadway Green', '12345678', 'reserved', '2017-01-01 00:00:00', '2017-04-21 12:00:00', '2017-04-23 22:00:00'),
('1.B.101', 'Vancouver Broadway Green', '11112222', 'reserved', '2017-01-01 00:00:00', '2017-05-01 11:00:00', '2017-05-05 14:00:00'),
('1.A.101', 'Vancouver Broadway Green', '12345678', 'reserved', '2017-01-01 00:00:00', '2017-02-02 12:00:00', '2017-02-03 23:00:00'),
('1.A.104', 'Vancouver Broadway Green', '11112222', 'reserved', '2017-01-01 00:00:00', '2017-02-02 00:00:00', '2017-02-05 12:00:00'),
('1.A.105', 'Vancouver Broadway Green', '12345678', 'reserved', '2017-01-01 00:00:00', '2017-02-03 09:00:00', '2017-02-03 16:00:00'),
('3.A.105', 'Vancouver Broadway Green', '12345678', 'reserved', '2017-01-01 00:00:00', '2017-07-01 09:00:00', '2017-07-01 16:00:00'),
('1.A.101', 'Vancouver Broadway Green', '11112222', 'reserved', NOW(), '2017-04-01 08:00:00','2017-04-01 10:00:00'),
('1.A.101', 'Vancouver Broadway Green', '11112222', 'reserved', NOW(), '2017-04-02 08:00:00','2017-04-02 10:00:00'),
('1.A.102', 'Vancouver Broadway Green', '12345678', 'reserved', NOW(), '2017-04-01 08:00:00','2017-04-02 14:00:00'),
('3.B.110', 'Vancouver Broadway Green', '12345678', 'reserved', NOW(), '2017-04-01 08:00:00','2017-04-04 08:00:00');