#!/bin/bash

echo "🌤️  Weather App Starter"
echo "======================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if already running
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "⚠️  Backend already running on port 8080"
else
    echo "${BLUE}📦 Starting backend...${NC}"
    cd backend
    ./gradlew bootRun > /dev/null 2>&1 &
    BACKEND_PID=$!
    echo "${GREEN}✅ Backend started (PID: $BACKEND_PID)${NC}"
    cd ..
    sleep 3
fi

if lsof -Pi :3000 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "⚠️  Frontend already running on port 3000"
else
    echo "${BLUE}🌐 Starting frontend...${NC}"
    cd Frontend/html
    python3 -m http.server 3000 > /dev/null 2>&1 &
    FRONTEND_PID=$!
    echo "${GREEN}✅ Frontend started (PID: $FRONTEND_PID)${NC}"
    cd ../..
fi

echo ""
echo "🎉 Weather App is ready!"
echo "🌐 Open: ${BLUE}http://127.0.0.1:3000${NC}"
echo ""
echo "To stop: Run ${BLUE}./stop.sh${NC}"
