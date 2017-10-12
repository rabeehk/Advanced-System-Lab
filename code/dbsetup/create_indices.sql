CREATE INDEX idx_message_queue_id_arrival ON message(queue_id, arrival_time);
CREATE INDEX idx_message_receiver_queue_id ON message(receiver_id, queue_id);
CREATE INDEX idx_message_sender_arrival ON message(sender_id, arrival_time);
