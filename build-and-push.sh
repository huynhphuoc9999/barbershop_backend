#!/bin/bash

# Build và push Docker image lên Docker Hub
# Render sẽ pull image này thay vì build từ source → nhanh hơn nhiều

set -e

echo "🔨 Building Docker image..."
docker build -t barbershop-backend:latest .

echo "🏷️  Tagging image..."
# Thay YOUR_DOCKER_USERNAME bằng username Docker Hub của bạn
DOCKER_USERNAME=${DOCKER_USERNAME:-"yourusername"}
docker tag barbershop-backend:latest $DOCKER_USERNAME/barbershop-backend:latest

echo "📤 Pushing to Docker Hub..."
docker push $DOCKER_USERNAME/barbershop-backend:latest

echo "✅ Done! Image pushed to: $DOCKER_USERNAME/barbershop-backend:latest"
echo ""
echo "📝 Next steps:"
echo "1. Vào Render Dashboard → Service Settings"
echo "2. Chọn 'Docker' làm Environment"
echo "3. Set Docker Image URL: $DOCKER_USERNAME/barbershop-backend:latest"
echo "4. Deploy → Render sẽ pull image (< 30 giây) thay vì build (5-10 phút)"
