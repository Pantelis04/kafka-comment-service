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

1. Open a terminal or command prompt
2. Navigate to the comment-service folder where the docker-compose.yml file is.
3. Run the command docker-compose up


### Step 3:Run the Ui
1. Open a terminal or command prompt.
2. Install the serve package globally by running the following command: npm install -g serve
3. Navigate inside the dist directory and start the server and serve the UI files by running the following command: serve -s
4. After running the command, you should see a message indicating that the server is running.
