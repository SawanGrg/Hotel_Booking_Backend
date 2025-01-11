Ensure Redis Server is Running:

First, make sure that your Redis server is running on 127.0.0.1:6379.
You can check this by going to your terminal or PowerShell and running:
cmd:
redis-server


Start Redis CLI:
After ensuring the Redis server is running, you can start the Redis CLI in a new terminal window:
redis-cli


Check Connection:
Once you are in the Redis CLI, check if you can connect to Redis by running:
PING
It should return PONG if the connection is successful.

SET key value
Sets the value of a key.

bash
Copy code
SET mykey "Hello, Redis!"
GET key
Retrieves the value of a key.
GET mykey
DEL key
Deletes a key.

bash
Copy code
DEL mykey
EXPIRE key seconds
Sets an expiration time for a key in seconds.

bash
Copy code
EXPIRE mykey 3600
TTL key
Retrieves the time-to-live (TTL) for a key.

bash
Copy code
TTL mykey
INCR key
Increments the value of a key by one (if the value is an integer).

bash
Copy code
INCR counter
DECR key
Decrements the value of a key by one (if the value is an integer).

bash
Copy code
DECR counter