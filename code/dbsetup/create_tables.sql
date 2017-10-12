CREATE TABLE queue (
  id SERIAL PRIMARY KEY,
  creation_time TIMESTAMP DEFAULT NOW(),
  creator_id INTEGER NOT NULL
 );
 
 CREATE TABLE message (
  id SERIAL PRIMARY KEY,
  sender_id INTEGER NOT NULL,
  receiver_id INTEGER,
  queue_id INTEGER NOT NULL,
  arrival_time TIMESTAMP DEFAULT NOW(),
  message TEXT NOT NULL,
  CONSTRAINT queue_id FOREIGN KEY (queue_id)
    REFERENCES queue (id)
);
