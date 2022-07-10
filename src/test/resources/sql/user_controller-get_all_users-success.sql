DELETE FROM payment;

INSERT INTO `payment` (`id`, `user_id`, `amount`) VALUES
(1, 'user-id-1', 10, 'PROCESSED'),
(2, 'user-id-2', 11, 'PROCESSED'),
(3, 'user-id-3', 12, 'PROCESSED'),
(4, 'user-id-4', 13, 'PROCESSED'),
(5, 'user-id-5', 14, 'PROCESSED'),
(6, 'user-id-6', 15, 'PROCESSED'),
(7, 'user-id-7', 16, 'NEW'),
(8, 'user-id-8', 17.11, 'NEW'),
(9, 'user-id-8', 20.56, 'NEW'),
(10, 'user-id-10', 9999999.11, 'NEW'),
(11, 'user-id-10', 34, 'NEW'),
(12, 'user-id-10', 40, 'NEW'),
(13, 'user-id-13', 50, 'NEW'),
(14, 'user-id-14', 60, 'NEW'),
(15, 'user-id-15', 70, 'NEW');
