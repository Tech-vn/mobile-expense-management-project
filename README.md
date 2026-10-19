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

## Tài liệu

| File | Nội dung |
|---|---|
| [docs/DATABASE.md](docs/DATABASE.md) | Sơ đồ quan hệ, mô tả bảng, Room Entity, truy vấn mẫu |
| [docs/SEED_DATA.md](docs/SEED_DATA.md) | Dữ liệu cứng: danh mục mặc định, dữ liệu mẫu để demo |

## Cấu trúc dự án

Package gốc: `com.example.chitieu`. Chú thích: `(đã có)` là file hiện có trong repo, còn lại là kế hoạch.

```
mobile-expense-management-project/
├── app/
│   ├── schemas/                                  # Room tự xuất schema JSON (commit lên Git)
│   └── src/main/java/com/example/chitieu/
│       ├── MainActivity.kt                       # (đã có) Điểm vào, gọi AppNavGraph
│       ├── ChiTieuApp.kt                         # Application class
│       │
│       ├── data/                                 # Tầng dữ liệu
│       │   ├── local/
│       │   │   ├── AppDatabase.kt                # Khai báo database
│       │   │   ├── DatabaseSeeder.kt             # Nạp danh mục mặc định (xem SEED_DATA.md)
│       │   │   ├── dao/
│       │   │   │   ├── CategoryDao.kt
│       │   │   │   ├── TransactionDao.kt
│       │   │   │   └── BudgetDao.kt
│       │   │   └── entity/
│       │   │       ├── TransactionType.kt        # enum EXPENSE / INCOME
│       │   │       ├── CategoryEntity.kt
│       │   │       ├── TransactionEntity.kt
│       │   │       ├── BudgetEntity.kt
│       │   │       └── QueryModels.kt            # Model kết quả join / tổng hợp
│       │   └── repository/
│       │       ├── CategoryRepository.kt
│       │       ├── TransactionRepository.kt
│       │       └── BudgetRepository.kt
│       │
│       ├── ui/                                   # Tầng giao diện, chia theo tính năng
│       │   ├── navigation/
│       │   │   └── AppNavGraph.kt
│       │   ├── theme/                            # (đã có) Color.kt, Theme.kt, Type.kt
│       │   ├── components/                       # Composable dùng chung (AmountText, CategoryIcon...)
│       │   ├── auth/
│       │   │   └── LoginScreen.kt                # (đã có, đang ở ui/screens/auth/)
│       │   ├── home/
│       │   │   ├── HomeScreen.kt
│       │   │   └── HomeViewModel.kt
│       │   ├── transaction/
│       │   │   ├── TransactionListScreen.kt
│       │   │   ├── AddEditTransactionScreen.kt
│       │   │   └── TransactionViewModel.kt
│       │   ├── category/
│       │   │   ├── CategoryScreen.kt
│       │   │   └── CategoryViewModel.kt
│       │   ├── budget/
│       │   │   ├── BudgetScreen.kt
│       │   │   └── BudgetViewModel.kt
│       │   └── report/
│       │       ├── ReportScreen.kt
│       │       └── ReportViewModel.kt
│       │
│       ├── di/
│       │   └── AppModule.kt                      # Cung cấp Database, DAO, Repository
│       └── util/
│           ├── CurrencyFormatter.kt              # Định dạng tiền VND
│           └── DateUtils.kt                      # Đổi epoch millis <-> ngày/tháng
│
├── docs/
│   ├── DATABASE.md
│   └── SEED_DATA.md
├── gradle/
│   └── libs.versions.toml                        # Khai báo phiên bản thư viện
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

Dự án chưa dùng tầng `domain/`: ViewModel đọc thẳng Entity và `QueryModels` từ Repository. Nếu sau này logic nghiệp vụ phức tạp hơn, có thể thêm `domain/model/` và `domain/usecase/`.

### Quy ước đặt tên

| Loại | Mẫu tên | Ví dụ |
|---|---|---|
| Màn hình | `XxxScreen.kt` | `LoginScreen`, `HomeScreen` |
| ViewModel | `XxxViewModel.kt` | `TransactionViewModel` |
| Entity | `XxxEntity.kt` | `CategoryEntity` |
| DAO | `XxxDao.kt` | `TransactionDao` |
| Repository | `XxxRepository.kt` | `BudgetRepository` |
| Composable dùng chung | `PascalCase`, đặt trong `ui/components/` | `AmountText` |
| Bảng / cột trong DB | `snake_case`, tên bảng số nhiều | `transactions`, `category_id` |
| Package | chữ thường, không gạch | `ui.transaction` |

### Luồng dữ liệu

```
Screen  →  ViewModel  →  Repository  →  DAO  →  SQLite (Room)
  ▲                                                  │
  └──────────── Flow / StateFlow ◄───────────────────┘
```
