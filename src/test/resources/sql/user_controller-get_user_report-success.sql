DELETE FROM payment;

INSERT INTO `payment` (`id`, `user_id`, `amount`, `status`) VALUES
(1, 'user-id-1', 10, 'PROCESSED'),
(2, 'user-id-2', 10, 'PROCESSED'),
(9, 'user-id-14', 10, 'NEW'),
(10, 'user-id-15', 20, 'NEW'),
(11, 'user-id-15', 10, 'PROCESSED');
