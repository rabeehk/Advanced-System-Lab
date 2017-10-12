DROP FUNCTION send_message(_sender INT, _queue_id INT, _message TEXT);
DROP FUNCTION send_message(_sender INT, _receiver_id INT, _queue_id INT, _message TEXT);
DROP FUNCTION peek_message_from_sender(integer,integer);
DROP FUNCTION query_queues(_receiver_id INT);
DROP FUNCTION pop_message(_id INT, _sender_id INT);
DROP FUNCTION peek_message(_queue_id INT, _receiver_id INT);

