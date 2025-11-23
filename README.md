📘 ModuleGuard – Module Access System (Jetpack Compose + MVVM + Kotlin)

A clean, modern Android application demonstrating:

✅ Role-based module access
✅ Cooling period logic (time-based restrictions)
✅ Real-time countdown updates
✅ Clean MVVM architecture
✅ Jetpack Compose UI
✅ Kotlin Coroutines + StateFlow
✅ Robust date-time handling using Instant

🌟 Features
🔐 Module Access Control

Each module may or may not be accessible depending on:

User's allowed module list

Whether cooling period is active

⏳ Cooling Timer (Real-Time)

Shows “Cooling ends in mm:ss”

Updated every second

Disables module cards during cooldown

🧠 Clean Architecture

Layers:

UI Layer → Compose screens

ViewModel Layer → State management

Domain Layer → Business logic (AccessManager)

Data Layer → Repository reading mock JSON

📂 Mock JSON Data

Loaded from assets (mock_data.json) for easy testing.

🏗 Architecture Overview
presentation/
ModuleAccessScreen.kt
components/
CoolingBanner.kt
ModuleCard.kt

viewmodel/
ModuleViewModel.kt

domain/
AccessManager.kt

data/
repository/
MockRepository.kt
models/
UserDto.kt
ModuleDto.kt
MockResponse.kt


Architecture Pattern:

MVVM

Unidirectional Data Flow (UDF)

Separation of Concerns

Testable Business Logic

🧩 How It Works
1️⃣ AccessManager (Domain Logic)

AccessManager is responsible for:

✓ Cooling Period Logic
isCoolingActive(): Boolean


Checks if:

start < now < end

✓ Countdown
coolingCountdown(): String?


Returns strings like:

Cooling ends in 04:12

✓ Permission Checks
hasPermissionFor(module)

✓ Robust Date Parsing

Handles:

Timezones

BOM characters

Invalid date formats

2️⃣ ModuleViewModel (Presentation Logic)

The ViewModel:

Loads JSON from repository

Instantiates AccessManager

Updates UI state every second

Checks module access on click

Uses:
✔ viewModelScope.launch
✔ StateFlow
✔ delay(1000) ticker

UI observes changes automatically via collectAsState().

3️⃣ Compose UI
ModuleAccessScreen

Reads state from ViewModel

Shows cooling banner

Displays module list

On click → shows Toast

Applies disable state during cooling

CoolingBanner

Clean Material3 card with warning icon.

ModuleCard

Reusable card showing:

Module title

Allowed / Denied indicator

Disabled state visuals

🚀 Running the Project
1. Clone the repo
   git clone https://github.com/yourname/ModuleGuard.git
   cd ModuleGuard

2. Open in Android Studio
3. Run on emulator / device.
   📦 Sample JSON (mock_data.json)
   {
   "user": {
   "userType": "active",
   "coolingStartTime": "2025-11-22T23:37:00Z",
   "coolingEndTime": "2025-11-22T23:42:00Z",
   "accessibleModules": ["payments", "account_info"]
   },
   "modules": [
   { "id": "payments", "title": "Payments", "requiresConsent": true },
   { "id": "account_info", "title": "Account Info", "requiresConsent": false },
   { "id": "consent_center", "title": "Consent Center", "requiresConsent": true }
   ]
   }



📚 Tech Stack
Layer	Technology
UI	Jetpack Compose, Material3
State	Kotlin StateFlow
Async	Coroutines
DI (optional)	Hilt
Date-Time	java.time.Instant
JSON	Kotlinx Serialization
Architecture	MVVM + UDF