----------- write message to the database with sender ------------------------
CREATE OR REPLACE FUNCTION send_message(
    _sender_id INT, _receiver_id INT, _queue_id INT, _message TEXT) RETURNS void AS $$
    INSERT INTO message(sender_id, receiver_id, queue_id, message)
    VALUES(_sender_id, _receiver_id, _queue_id, _message);
$$ LANGUAGE sql VOLATILE;

----------- write message to the database(send request) ----------------------
CREATE OR REPLACE FUNCTION send_message(
    _sender_id INT, _queue_id INT, _message TEXT) RETURNS void AS $$
    INSERT INTO message(sender_id, receiver_id, queue_id, message)
    VALUES(_sender_id, null, _queue_id, _message);
$$ LANGUAGE sql VOLATILE;

------------ peek message from database ----------------------------------------
CREATE OR REPLACE FUNCTION peek_message(_queue_id INT, _receiver_id INT) RETURNS TABLE(id INT, sender_id INT, text TEXT) AS $$
    SELECT id, sender_id, message
    FROM message
    WHERE queue_id = _queue_id AND (receiver_id IS NULL OR receiver_id = _receiver_id)
    ORDER BY queue_id, arrival_time
    LIMIT 1;
$$ LANGUAGE sql IMMUTABLE;


----------- query queues --------------------------------------------------------
CREATE OR REPLACE FUNCTION query_queues(_receiver_id INT) RETURNS TABLE(id INT) AS $$
    SELECT DISTINCT ON (queue_id) queue_id
    FROM message
    WHERE receiver_id = _receiver_id
    ORDER BY queue_id;
$$ LANGUAGE sql IMMUTABLE;

------------ pop a message from database --------------------------------------
CREATE OR REPLACE FUNCTION pop_message(_queue_id INT, _receiver_id INT) RETURNS TABLE(id INT, sender_id INT, text TEXT) AS $$
    DELETE FROM message AS mnew
    WHERE mnew.id = (
        SELECT mold.id
        FROM message AS mold
        WHERE queue_id = _queue_id AND (receiver_id IS NULL OR receiver_id = _receiver_id)
        ORDER BY queue_id, arrival_time
        LIMIT 1 FOR UPDATE
    ) RETURNING mnew.id, mnew.sender_id, mnew.message;
$$ LANGUAGE sql VOLATILE;


------------ peek a message from database from special sender ----------------
CREATE OR REPLACE FUNCTION peek_message_from_sender(_sender_id INT, _receiver_id INT) RETURNS TABLE(id INT, sender_id INT, text TEXT) AS $$
    SELECT id, sender_id, message
    FROM message
    WHERE sender_id = _sender_id AND (receiver_id IS NULL OR receiver_id = _receiver_id)
    ORDER BY sender_id, arrival_time
    LIMIT 1;
$$ LANGUAGE sql IMMUTABLE;

