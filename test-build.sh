#!/bin/bash

# Script để test build local trước khi push lên Render
# Sử dụng Docker để build → không cần cài Maven

set -e

echo "🔨 Building Docker image locally..."
echo "⏳ Đợi ~20-30 giây để build..."
echo ""

if docker build -t barbershop-backend:test .; then
    echo ""
    echo "✅ BUILD THÀNH CÔNG!"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Code không có lỗi compile."
    echo "An toàn để push lên Render."
    echo ""
    echo "📤 Chạy lệnh sau để deploy:"
    echo "   git push origin khoa-deploy"
    echo ""
else
    echo ""
    echo "❌ BUILD FAILED!"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Có lỗi compile. FIX LỖI trước khi push."
    echo ""
    exit 1
fi
