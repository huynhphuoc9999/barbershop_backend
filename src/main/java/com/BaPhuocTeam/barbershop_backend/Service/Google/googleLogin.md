Dựa trên mã nguồn cấu hình Spring Security và Service của hệ thống (trong file `SecurityConfig.java`, `UserServiceImpl.java`, và `OAuth2LoginSuccessHandler.java`), đây là toàn bộ luồng đăng nhập bằng Google (Google OAuth2 Flow) của Project bạn đang làm:

### 🔄 Luồng đăng nhập đầy đủ (Full Flow)

**Giai đoạn 1: Frontend khởi xướng yêu cầu**
1. **Click Login:** Người dùng nhấn nút "Đăng nhập với Google" trên giao diện Frontend (FE).
2. **Gọi tới Backend:** Nút này không gọi API thông thường mà sẽ chuyển hướng (redirect) hoặc gọi thẳng vào endpoint của Backend (BE) do Spring Security cung cấp mặc định:
   👉 `GET https://barbershop-backend-yn3c.onrender.com/oauth2/authorization/google`

**Giai đoạn 2: Xác thực qua Google (Xử lý ngầm bởi BE và Google)**
3. **Chuyển hướng đến Google:** BE nhận được yêu cầu trên, tự động build một URL xác thực (chứa `client_id`, `redirect_uri` đã cấu hình trong `application.properties`) và chuyển hướng trình duyệt của User sang trang đăng nhập của Google.
4. **Cấp quyền (Consent):** User đăng nhập tài khoản Google và đồng ý cấp quyền (cấp email, profile) cho ứng dụng Barbershop.
5. **Google trả về Code:** Sau khi User đồng ý, Google chuyển hướng (redirect) ngược lại về BE theo đường dẫn callback:
   👉 `GET https://barbershop-backend-yn3c.onrender.com/login/oauth2/code/google?code=abcxyz...`
6. **Lấy Thông tin User:** Spring Security tại BE tự động lấy cái `code` này gửi lại cho Google để đổi lấy **Access Token** của Google. Sau đó, BE dùng Access Token này gọi lên API của Google để lấy thông tin User (Tên, Email, Ảnh đại diện).

**Giai đoạn 3: Xử lý logic nghiệp vụ tại Backend (Barbershop)**
7. **Bọc Data (CustomOAuth2User):** Thông tin lấy từ Google được đưa qua hàm `oAuth2UserService()` trong `SecurityConfig` để đóng gói thành object `CustomOAuth2User`.
8. **Success Handler Kích hoạt:** Luồng xử lý chuyển vào `OAuth2LoginSuccessHandler.java`. Tại đây, BE gọi hàm `userService.processOAuthPostLogin(...)`.
9. **Kiểm tra/Lưu Database (`UserServiceImpl.java`):**
   - BE tìm trong Database xem bảng `users` đã có email này chưa.
   - **Nếu CHƯA có:** BE tạo một User mới với `provider = "Google"`, role mặc định là `ROLE_CUSTOMER`, tạo mật khẩu rỗng hoặc random (hiện tại trong code bạn đang set mật khẩu cứng là `123456`) và lưu vào DB.
   - **Nếu ĐÃ có:** BE bỏ qua bước tạo mới và lấy luôn User cũ trong DB ra.
10. **Tạo JWT Token:** BE load thông tin User vừa xử lý để tạo ra một chuỗi **JWT (JSON Web Token)** (`jwtUtils.generateToken(userDetails)`).

**Giai đoạn 4: Trả Token về cho Frontend**
11. **Redirect về FE:** Tại dòng code cuối cùng của `OAuth2LoginSuccessHandler`, BE thực hiện chuyển hướng trình duyệt một lần nữa, đưa User **quay trở về Frontend** kèm theo JWT vừa tạo trên URL:
    👉 `Redirect tới: http://localhost:5173/oauth2/redirect?token=<chuỗi_jwt_token>`

**Giai đoạn 5: Frontend tiếp nhận và Verify**
12. **Lưu Token:** Trang FE (tại route `/oauth2/redirect`) khởi động, đọc lấy token trên URL (thanh địa chỉ) và lưu vào `localStorage` hoặc Cookie.
13. **Xác thực (Verify):** FE dùng Token vừa lưu, kẹp vào header `Authorization: Bearer <token>` và gọi API lấy thông tin người dùng (vd: gọi vào hàm `getUserInfo()` mà bạn đã viết ở BE).
14. **Hoàn tất:** BE kiểm tra Token hợp lệ, trả về chi tiết thông tin User. FE hiển thị trạng thái "Đã đăng nhập" và vào màn hình chính.

---

### ⚠️ Một số điểm cần lưu ý với luồng hiện tại của bạn:
- **Redirect URL ở bước 11 đang bị fix cứng:** Code BE đang hardcode trả về `http://localhost:5173`. Nếu bạn đã deploy BE lên Render và FE lên một host khác (như Vercel/Netlify), FE sẽ không thể nhận được token vì BE đang đẩy về localhost. 
- **Thiếu FailureHandler (Luồng thất bại):** Như phân tích ở câu trước, bạn hiện chỉ có Success Handler. Nếu User hủy cấp quyền ở bước 4, hoặc có lỗi ở bước 6, 9... Spring Security sẽ văng lỗi 500 như ảnh bạn chụp thay vì báo cho FE biết.