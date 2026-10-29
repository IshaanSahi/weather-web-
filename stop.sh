#!/bin/bash

echo "🛑 Stopping Weather App..."
echo ""

# Stop backend
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    PID=$(lsof -ti:8080)
    kill -9 $PID 2>/dev/null
    echo "✅ Backend stopped (port 8080)"
else
    echo "⚠️  Backend not running"
fi

# Stop frontend
if lsof -Pi :3000 -sTCP:LISTEN -t >/dev/null 2>&1; then
    PID=$(lsof -ti:3000)
    kill -9 $PID 2>/dev/null
    echo "✅ Frontend stopped (port 3000)"
else
    echo "⚠️  Frontend not running"
fi

echo ""
echo "✅ All servers stopped"
