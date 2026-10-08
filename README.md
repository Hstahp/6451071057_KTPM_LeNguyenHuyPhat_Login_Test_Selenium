# 6451071057_KTPM_LeNguyenHuyPhat_LoginTest

Dự án kiểm thử tự động (Automation Testing) tính năng **Đăng nhập (Login)** cho hệ thống **Văn phòng điện tử Trường ĐH Giao Thông Vận Tải (UTC)** sử dụng **Java**, **Selenium WebDriver**, **TestNG**, xuất báo cáo **Excel (.xlsx)** bằng **Apache POI** và báo cáo phân tích số liệu trực quan với **Allure Report**.

---

## Trang web kiểm thử

- **Hệ thống:** Văn phòng điện tử UTC
- **URL:** `https://vanphongdientu.utc.edu.vn/Login`

---

## Công nghệ & Thư viện sử dụng

- **Ngôn ngữ:** Java 21 (LTS)
- **Công cụ build:** Apache Maven (đã tích hợp sẵn Maven Wrapper `mvnw`)
- **Kiểm thử tự động:** Selenium WebDriver (`v4.27.0`)
- **Testing Framework:** TestNG (`v7.10.2`)
- **Báo cáo phân tích:** Allure Report (`v2.29.0`)
- **Xuất file Excel:** Apache POI (`v5.3.0`)
- **Design Pattern:** Page Object Model (POM)

---

## 📂 Cấu trúc thư mục dự án (Chuẩn E2E Test Architecture)

```text
d:\KTPM\login-test-selenium\
├── src/test/java/com/ktpm/
│   ├── base/
│   │   └── BaseTest.java        // Khởi tạo WebDriver, timeout, tearDown & xuất Excel
│   ├── pages/
│   │   ├── BasePage.java        // Thao tác chung: wait, click, type, clear, get text,...
│   │   └── LoginPage.java       // Form đăng nhập UTC (kế thừa BasePage)
│   ├── tests/
│   │   └── LoginE2ETest.java    // Toàn bộ 16 Test Cases E2E (kế thừa BaseTest)
│   ├── model/
│   │   └── TestCaseResult.java  // Model kết quả test case
│   └── utils/
│       └── ExcelExporter.java   // Tiện ích xuất báo cáo Excel (.xlsx)
│
├── src/test/resources/
│   └── testng.xml               // Cấu hình TestNG Suite cho LoginE2ETest
├── testcase.txt                 // Tài liệu mô tả chi tiết 16 Test Cases
├── pom.xml                      // Cấu hình dependencies & build Maven
└── KetQuaKiemThu_Login.xlsx     // Báo cáo kết quả Excel tự động
```

---

## Danh sách 16 Test Cases Mới (Đã cập nhật)

| STT | Mã TC | Tên Kịch Bản Kiểm Thử | Dữ Liệu Đầu Vào | Kết Quả Mong Đợi | Trạng Thái |
| :-: | :------: | :-------------------------------------------- | :--------------------------------------------------------- | :---------------------------------------------------- | :--------: |
| 1 | **TC1** | Nhập Tên đăng nhập là Mã sinh viên (chữ số) | `user: '6451071057'`, `pass: 'pass123'` | Báo lỗi: _"Tài khoản hoặc mật khẩu không đúng."_ | **PASS** |
| 2 | **TC2** | Tấn công SQL Injection ở ô Mật khẩu | `user: 'testuser'`, `pass: '\' OR \'1\'=\'1'` | Hệ thống chặn truy cập SQLi an toàn | **PASS** |
| 3 | **TC3** | Tấn công XSS dạng SVG Payload ở ô Username | `user: '<svg onload=alert("XSS_UTC")>'`, `pass: '123456'` | Không bật alert script, xử lý an toàn | **PASS** |
| 4 | **TC4** | Dán (Paste `Ctrl+V`) vào ô Tên đăng nhập | Dán chuỗi `sv6451071057` | Dán thành công chuỗi vào ô Username | **PASS** |
| 5 | **TC5** | Liên kết "Bạn quên mật khẩu đăng nhập ?" | Click link _"Bạn quên mật khẩu đăng nhập ?"_ | Chuyển hướng tới trang khôi phục mật khẩu (`/Login/GetPass`) | **PASS** |
| 6 | **TC6** | Nút "Đăng nhập bằng e-mail UTC" | Click nút _"Đăng nhập bằng e-mail UTC"_ | Chuyển hướng tới cổng xác thực Email SSO của trường | **PASS** |
| 7 | **TC7** | Bật/tắt (Toggle) 2 lần checkbox "Giữ tôi luôn đăng nhập" | Click 2 lần vào checkbox | Checkbox quay về trạng thái bỏ chọn (Unchecked) | **PASS** |
| 8 | **TC8** | Gửi form đăng nhập bằng phím ENTER từ ô Mật khẩu | `user: 'huongnt'`, `pass: '123456'` + Press ENTER | Form tự động submit và hiển thị phản hồi từ máy chủ | **PASS** |
| 9 | **TC9** | Nhập Mật khẩu chứa khoảng trắng ở giữa | `user: 'huongnt'`, `pass: '123 456@utc'` | Báo lỗi: _"Tài khoản hoặc mật khẩu không đúng."_ | **PASS** |
| 10 | **TC10** | Xóa (Clear) dữ liệu ô Username trước khi Đăng nhập | `user: ''` (xóa sau khi nhập), `pass: '123456'` | Báo lỗi: _"Bạn chưa nhập tên đăng nhập"_ | **PASS** |
| 11 | **TC11** | Nhập Mật khẩu chứa ký tự Tiếng Việt có dấu (UTF-8) | `user: 'huongnt'`, `pass: 'MậtKhẩuViệt2026!'` | Xử lý UTF-8 an toàn, báo lỗi tài khoản không đúng | **PASS** |
| 12 | **TC12** | Nhập Tên đăng nhập là địa chỉ Email UTC hợp lệ | `user: 'sinhvien@st.utc.edu.vn'`, `pass: '123456'` | Báo lỗi: _"Tài khoản hoặc mật khẩu không đúng."_ | **PASS** |
| 13 | **TC13** | Tên đăng nhập và Mật khẩu độ dài cực ngắn (1 ký tự) | `user: 'a'`, `pass: '1'` | Báo lỗi: _"Tài khoản hoặc mật khẩu không đúng."_ | **PASS** |
| 14 | **TC14** | Mật khẩu chứa tập hợp đầy đủ các ký tự đặc biệt phức tạp | `user: 'huongnt'`, `pass: '!@#$%^&*()_+-=[]{}|;:',.<>?/'` | Xử lý an toàn, báo lỗi tài khoản không đúng | **PASS** |
| 15 | **TC15** | Tấn công XSS ở ô Mật khẩu | `user: 'huongnt'`, `pass: '<img src=x onerror=alert("XSS")>'` | Chặn XSS ô mật khẩu, không bật alert | **PASS** |
| 16 | **TC16** | Kiểm thử ẩn ký tự ô Mật khẩu | `pass: 'SecurePass2026#'` | Ô pass có thuộc tính `type="password"`, ký tự bị ẩn | **PASS** |

---

## 1. Báo cáo số liệu với Allure Report

Dự án đã tích hợp **Allure Report** giúp xem biểu đồ thống kê, tỷ lệ Pass/Fail, thời gian thực thi của từng test case:

### Bước 1: Dọn dẹp cache cũ và thực thi kiểm thử 16 Test Cases

```powershell
.\mvnw.cmd clean test
```

_(Dữ liệu kết quả sẽ tự động lưu vào thư mục `target/allure-results`)_

### Bước 2: Bật trang báo cáo Allure Report trên trình duyệt

```powershell
.\mvnw.cmd allure:serve
```

> Lệnh này sẽ tự động khởi động một web server nội bộ và mở trang Dashboard của Allure trên trình duyệt (hiển thị đúng chuẩn 16 Test Cases mới).

---

## 2. Báo cáo kết quả ra file Excel (`KetQuaKiemThu_Login.xlsx`)

File Excel được tự động tạo/cập nhật ngay tại thư mục gốc với các thông tin:

- Tiêu đề báo cáo và thông tin Mã sinh viên: **6451071057**.
- Chi tiết từng kịch bản, dữ liệu đầu vào, kết quả mong đợi, kết quả thực tế, thời gian chạy.
- Các ô trạng thái được tô màu trực quan (**PASS** màu xanh lá, **FAIL** màu đỏ).
- Dòng tổng kết thống kê số lượng và tỷ lệ đạt (%).

---

## Hướng dẫn chạy nhanh dự án từ Terminal

```powershell
# Dọn dẹp kết quả cũ và chạy đúng 16 Test Cases E2E
.\mvnw.cmd clean test

# Xem Allure Report chi tiết
.\mvnw.cmd allure:serve
```
