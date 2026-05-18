Dựa vào file PaymentsController.java và VNPayServiceImpl.java của bạn, mình xin phân tích luồng thanh toán VNPay của hệ thống như sau:

💸 Luồng Thanh Toán VNPay (VNPay Payment Flow)
Giai đoạn 1: Khởi tạo thanh toán (Frontend gọi Backend)

Request từ FE: Khi người dùng bấm "Thanh toán VNPay", FE sẽ gọi API POST /api/customer/payments/create kèm theo Body (PaymentDTO) chứa các thông tin: userId, amount, và orderId hoặc appointmentId.
BE tạo Transaction lưu Database:
Hàm createPayment sẽ kiểm tra thông tin User, Order/Appointment.
Tạo một bản ghi mới trong bảng Payments với PaymentStatus = PENDING.
BE Build URL VNPay:
BE lấy ID của Payment vừa tạo gán vào vnp_TxnRef.
Lấy các tham số cấu hình VNPay (vnp_TmnCode, vnp_HashSecret, vnpUrl, vnpReturnUrl).
Dùng thuật toán HmacSHA512 để tạo chữ ký số (vnp_SecureHash) giúp bảo mật dữ liệu.
Cuối cùng, BE ghép tất cả thành một đường link thanh toán chuẩn của VNPay và trả về cho FE (apiResponse.setData("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...")).
Giai đoạn 2: User thanh toán trên VNPay 4. FE Chuyển hướng: Nhận được link từ BE, FE dùng lệnh (ví dụ: window.location.href = link) để đẩy trình duyệt của người dùng sang cổng thanh toán của VNPay. 5. Thanh toán: Người dùng nhập thông tin thẻ / quét mã QR Code trên trang của VNPay và xác nhận thanh toán.

Giai đoạn 3: Nhận kết quả trả về từ VNPay 6. VNPay Redirect: Sau khi User thanh toán xong (thành công hoặc thất bại/hủy bỏ), VNPay sẽ chuyển hướng (redirect) trình duyệt của User về lại địa chỉ vnpReturnUrl kèm theo rất nhiều query parameters trên URL (như vnp_ResponseCode, vnp_Amount, vnp_TxnRef,...).

BE xử lý kết quả thanh toán:
Dữ liệu này cuối cùng sẽ đi vào API GET /api/customer/payments/execute/vnpay.
Hàm executePayment(HttpServletRequest request) sẽ đọc các tham số:
vnp_ResponseCode: Code trả về từ VNPay.
vnp_TxnRef: ID của Payment đã lưu ở Bước 2.
vnp_Amount: Số tiền giao dịch.
Nếu vnp_ResponseCode == "00" (Thành công):
BE chia amount / 100 để quy đổi lại tiền thật và so sánh với số tiền trong Database xem có khớp không (chống gian lận).
Đổi PaymentStatus thành COMPLETED.
Nếu là mua hàng (Orders) -> Cập nhật Order status thành PAID.
Nếu là đặt lịch (Appointments) -> Cập nhật Appointment isPaid = true.
Trả về JSON HTTP 200: "Thanh toán thành công!".
Ngược lại: Trả về JSON HTTP 400 hoặc 500: "Thanh toán thất bại!".
⚠️ Phân tích rủi ro / Điểm cần lưu ý trong luồng này:
Trong thực tế, ở Giai đoạn 3 (Bước 6, 7), cách ứng dụng hoạt động sẽ phụ thuộc vào việc biến VNP_RETURN_URL trong file application.properties của bạn đang trỏ đi đâu:

Trường hợp A (Trỏ thẳng vào BE - https://barbershop-backend-yn3c.onrender.com/api/customer/payments/execute/vnpay): Sau khi thanh toán xong, người dùng sẽ bị redirect thẳng vào API backend. Trình duyệt của họ sẽ chỉ hiển thị một cục text JSON khô khan (như kiểu {"statusCode": 200, "message": "Thanh toán thành công!"}). Trải nghiệm người dùng sẽ rất tệ và họ không biết cách để quay lại trang web Frontend.

Trường hợp B (Trỏ về FE - http://localhost:5173/payment-result): Đây là cách chuẩn! Sau khi VNPay redirect về FE, trang FE của bạn sẽ hiển thị giao diện "Đang xử lý thanh toán...", đồng thời FE sẽ lấy toàn bộ cục query params trên thanh địa chỉ URL, nối nó lại và gửi một request ngầm tới API GET /api/customer/payments/execute/vnpay của BE. Sau khi BE xử lý và trả JSON báo thành công/thất bại, FE mới đổi giao diện thành "Thanh toán thành công! Bấm vào đây để về trang chủ."

Ngoài ra, hệ thống của bạn hiện tại chưa có IPN (Instant Payment Notification) - một cơ chế chuẩn của VNPay (gọi API server-to-server) để cập nhật trạng thái đơn hàng ngầm trong trường hợp trình duyệt của người dùng tắt ngang trước khi redirect về. Bạn chỉ đang xử lý ở đầu Return URL. Tạm thời luồng hiện tại vẫn chạy được nhưng tỷ lệ hụt đơn có thể xảy ra.