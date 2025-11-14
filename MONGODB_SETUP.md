# MongoDB Setup Instructions

## Quick Start

### Step 1: Start MongoDB

**Option A: Using Docker (Recommended for development)**

Make sure Docker Desktop is running, then:

```bash
# Use the provided script
./start-mongodb.sh
```

Or manually:
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

**Option B: Using Homebrew (Install MongoDB locally)**

```bash
# Install MongoDB Community Edition
brew tap mongodb/brew
brew install mongodb-community

# Start MongoDB service
brew services start mongodb-community
```

### Step 2: Start the Application

```bash
# Build and run the application
./gradlew bootRun
```

The application will automatically:
- Connect to MongoDB at `localhost:27017`
- Create a database called `hackathon_db`
- Initialize it with 8 sample products

## What Was Added

### 1. Dependencies (build.gradle.kts)
- Added `spring-boot-starter-data-mongodb` dependency

### 2. Configuration (application.properties)
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=hackathon_db
```

### 3. Project Structure

New files created:

```
src/main/kotlin/com/e1s/hackathon/
├── model/
│   └── Product.kt                 # MongoDB document entity
├── repository/
│   └── ProductRepository.kt       # MongoDB repository interface
├── config/
│   └── DataInitializer.kt        # Database initialization with sample data
└── controller/
    └── ProductController.kt       # REST API endpoints
```

### 4. Sample Data

The application initializes with 8 products:
- **Electronics**: Laptop, Wireless Mouse, Mechanical Keyboard, USB-C Hub, Monitor
- **Furniture**: Standing Desk, Office Chair, Desk Lamp

## API Endpoints

Base URL: `http://localhost:8080`

### Get all products
```bash
curl http://localhost:8080/api/products
```

### Get product by ID
```bash
curl http://localhost:8080/api/products/{id}
```

### Get products by category
```bash
# Get all electronics
curl http://localhost:8080/api/products/category/Electronics

# Get all furniture
curl http://localhost:8080/api/products/category/Furniture
```

### Search products by name
```bash
curl http://localhost:8080/api/products/search?name=laptop
```

### Create a new product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Webcam",
    "description": "HD webcam for video calls",
    "price": 79.99,
    "category": "Electronics",
    "inStock": true
  }'
```

### Update a product
```bash
curl -X PUT http://localhost:8080/api/products/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "description": "Updated description",
    "price": 149.99,
    "category": "Electronics",
    "inStock": true
  }'
```

### Delete a product
```bash
curl -X DELETE http://localhost:8080/api/products/{id}
```

## Accessing MongoDB Directly

### Using MongoDB Shell (mongosh)

If you have MongoDB installed locally:
```bash
# Connect to MongoDB
mongosh

# Switch to the hackathon database
use hackathon_db

# View all products
db.products.find().pretty()

# Count products
db.products.countDocuments()

# Find products by category
db.products.find({category: "Electronics"}).pretty()

# Find products in stock
db.products.find({inStock: true}).pretty()
```

### Using Docker

```bash
# Access MongoDB shell inside Docker container
docker exec -it mongodb mongosh

# Then run MongoDB commands as above
use hackathon_db
db.products.find().pretty()
```

## MongoDB Management Commands

### Docker Commands

```bash
# Check if MongoDB is running
docker ps | grep mongodb

# Stop MongoDB
docker stop mongodb

# Start MongoDB (if already created)
docker start mongodb

# View MongoDB logs
docker logs mongodb

# Remove MongoDB container (will delete data)
docker stop mongodb
docker rm mongodb
```

### Homebrew Commands (if installed locally)

```bash
# Start MongoDB
brew services start mongodb-community

# Stop MongoDB
brew services stop mongodb-community

# Restart MongoDB
brew services restart mongodb-community

# Check status
brew services list | grep mongodb
```

## Troubleshooting

### MongoDB Connection Issues

If you see errors like "Connection refused" or "Unable to connect to MongoDB":

1. **Check if MongoDB is running:**
   ```bash
   # For Docker
   docker ps | grep mongodb
   
   # For local installation
   brew services list | grep mongodb
   ```

2. **Verify the port is available:**
   ```bash
   lsof -i :27017
   ```

3. **Check application logs** for specific error messages

### Port Already in Use

If port 27017 is already in use:
```bash
# Find what's using the port
lsof -i :27017

# Kill the process (replace PID with actual process ID)
kill -9 PID
```

## Data Persistence

### Docker
Data is stored inside the Docker container by default. To persist data even after removing the container:

```bash
docker run -d -p 27017:27017 \
  --name mongodb \
  -v mongodb_data:/data/db \
  mongo:latest
```

### Local Installation
Data is automatically persisted in MongoDB's data directory (usually `/usr/local/var/mongodb`)

## Next Steps

1. Start MongoDB using one of the methods above
2. Run the application: `./gradlew bootRun`
3. Test the API endpoints with curl or a tool like Postman
4. View the data in MongoDB using mongosh or a GUI tool like MongoDB Compass

## Additional Resources

- [Spring Data MongoDB Documentation](https://spring.io/projects/spring-data-mongodb)
- [MongoDB Documentation](https://www.mongodb.com/docs/)
- [MongoDB Compass](https://www.mongodb.com/products/compass) - GUI for MongoDB
