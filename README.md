# Expense Tracker

Ứng dụng Android quản lý chi tiêu cá nhân, viết bằng Kotlin, lưu dữ liệu cục bộ bằng SQLite thông qua Room.

## Công nghệ

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Kotlin |
| UI | Jetpack Compose |
| Kiến trúc | MVVM (Screen → ViewModel → Repository → DAO) |
| Database | SQLite qua Room |
| Bất đồng bộ | Kotlin Coroutines + Flow |
| Điều hướng | Navigation Compose |
| Dependency Injection | Hilt (hoặc khởi tạo thủ công) |
| Biểu đồ | Vico / MPAndroidChart |
| Build | Gradle (Kotlin DSL), KSP |

## Cấu trúc dự án

```
mobile-expense-management-project/
├── app/
│   ├── schemas/                              # Room tự xuất schema JSON (commit lên Git)
│   └── src/main/java/com/techvn/expensetracker/
│       ├── data/
│       │   ├── local/
│       │   │   ├── AppDatabase.kt            # Khai báo database, seed danh mục mặc định
│       │   │   ├── dao/
│       │   │   │   ├── CategoryDao.kt
│       │   │   │   ├── TransactionDao.kt
│       │   │   │   └── BudgetDao.kt
│       │   │   └── entity/
│       │   │       ├── TransactionType.kt
│       │   │       ├── CategoryEntity.kt
│       │   │       ├── TransactionEntity.kt
│       │   │       ├── BudgetEntity.kt
│       │   │       └── QueryModels.kt        # Model kết quả join / tổng hợp
│       │   └── repository/
│       │       ├── CategoryRepository.kt
│       │       ├── TransactionRepository.kt
│       │       └── BudgetRepository.kt
│       ├── domain/
│       │   └── model/                        # Model dùng cho UI / logic
│       ├── ui/
│       │   ├── navigation/
│       │   │   └── AppNavGraph.kt
│       │   ├── theme/
│       │   ├── home/                         # Màn tổng quan
│       │   ├── transaction/                  # Thêm / sửa / danh sách giao dịch
│       │   ├── category/                     # Quản lý danh mục
│       │   ├── budget/                       # Ngân sách
│       │   └── report/                       # Báo cáo, biểu đồ
│       ├── di/
│       │   └── AppModule.kt
│       ├── util/
│       │   ├── CurrencyFormatter.kt
│       │   └── DateUtils.kt
│       ├── ExpenseApp.kt                     # Application class
│       └── MainActivity.kt
├── docs/                                     # Tài liệu, ảnh minh họa
├── gradle/
│   └── libs.versions.toml                    # Khai báo phiên bản thư viện
├── build.gradle.kts
├── settings.gradle.kts
├── .editorconfig
├── .gitattributes
├── .gitignore
└── README.md
```

### Vai trò từng tầng

| Tầng | Thư mục | Trách nhiệm |
|---|---|---|
| UI | `ui/` | Hiển thị và nhận sự kiện người dùng (Compose Screen + ViewModel) |
| Repository | `data/repository/` | Điểm truy cập dữ liệu duy nhất, UI không biết dữ liệu đến từ đâu |
| DAO | `data/local/dao/` | Các câu query SQLite, trả về `Flow` để UI tự cập nhật |
| Entity | `data/local/entity/` | Định nghĩa bảng trong database |
| Domain | `domain/model/` | Model thuần dùng chung giữa các tầng |