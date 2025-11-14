#!/bin/bash

# MongoDB Startup Script for Development

echo "🚀 Starting MongoDB for hackathon project..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop first."
    echo ""
    echo "Options to start MongoDB:"
    echo "1. Start Docker Desktop and run this script again"
    echo "2. Install MongoDB locally with Homebrew:"
    echo "   brew tap mongodb/brew"
    echo "   brew install mongodb-community"
    echo "   brew services start mongodb-community"
    exit 1
fi

# Check if MongoDB container already exists
if docker ps -a --format '{{.Names}}' | grep -q '^mongodb$'; then
    echo "MongoDB container already exists."

    # Check if it's running
    if docker ps --format '{{.Names}}' | grep -q '^mongodb$'; then
        echo "✅ MongoDB is already running!"
    else
        echo "Starting existing MongoDB container..."
        docker start mongodb
        echo "✅ MongoDB started successfully!"
    fi
else
    echo "Creating and starting new MongoDB container..."
    docker run -d -p 27017:27017 --name mongodb mongo:latest
    echo "✅ MongoDB started successfully!"
fi

echo ""
echo "MongoDB is available at: mongodb://localhost:27017"
echo "Database name: hackathon_db"
echo ""
echo "To stop MongoDB, run: docker stop mongodb"
echo "To remove MongoDB container, run: docker rm mongodb"

