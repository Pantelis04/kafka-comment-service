# Comment Service Application

## Local Setup

### Step 1: Run PostgreSQL Database Locally

1. Open a terminal or command prompt.
2. Pull the PostgreSQL Docker image and run a container: docker pull postgres
3. docker run --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
4. docker exec -it my-postgres psql -U postgres
5. CREATE DATABASE commentdatabase;
6. CREATE DATABASE commentdatabaseTest;


### Step 2: Run Kafka and ZooKeeper Locally

1. Open a terminal or command prompt.
2. Pull the ZooKeeper Docker image and run a container:
3. docker pull confluentinc/cp-zookeeper
4. docker pull confluentinc/cp-kafka
5. docker run -d --name zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper
6. docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 confluentinc/cp-kafka
7. docker exec -it kafka kafka-topics --create --topic __consumer_offsets --bootstrap-server localhost:9092 --partitions 50 --replication-factor 1 --config cleanup.policy=compact --config compression.type=producer --config segment.bytes=104


### Step 3:Run the Ui
1. Open a terminal or command prompt.
2. Install the serve package globally by running the following command: npm install -g serve
3. Navigate inside the Ui directory and start the server and serve the UI files by running the following command: serve -s
4. After running the command, you should see a message indicating that the server is running.
