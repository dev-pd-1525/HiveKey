# 🐝 HiveKey — Secure Access Control Management System

<p align="center">
  <img src="https://img.shields.io/badge/Android-26%2B-brightgreen?style=flat-square&logo=android" />
  <img src="https://img.shields.io/badge/Kotlin-2.2.0-blue?style=flat-square&logo=kotlin" />
  <img src="https://img.shields.io/badge/Storage-Cloudinary-purple?style=flat-square" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" />
</p>

HiveKey is a premium Android application for secure access control management. It features role-based admin/user panels, biometric selfie capture during signup, cloud-based photo storage, GPS location tracking, and a sleek dark-mode glassmorphic UI.

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🛡️ **Admin & User Panels** | Separate dashboards with role-specific controls |
| 📸 **Selfie Capture** | Take a profile selfie during signup or choose from gallery |
| ☁️ **Cloud Storage** | Photos & user logs stored on Cloudinary |
| 📍 **GPS Location** | Tracks and displays user's city/state on the dashboard |
| ☀️ **Smart Greeting** | Time-based greetings (Good Morning/Afternoon/Evening) |
| 🔐 **Local Auth** | SHA-256 hashed passwords stored in SharedPreferences |
| 🎨 **Premium UI** | Dark-mode glassmorphic design with gradient cards |
| ✨ **Animated Splash** | Typing animation, floating dots, pulsing glow ring |
| 👨‍💻 **Developer Page** | Credits for the development team |
| 📜 **Terms & Conditions** | Full professional T&C for company deployment |

---

## 📱 Screens

| Screen | Purpose |
|--------|---------|
| Splash | Animated launch screen with progress indicator |
| Panel Selection | Choose Admin 🛡️ or User 👤 panel |
| Login | Email & password authentication |
| Signup | Registration with selfie capture |
| Dashboard | Role-specific cards, greeting, location |
| Profile | View personal details & photo |
| Developer | Team credits and contributor info |
| Terms | Terms & Conditions |

---

## 🏗️ Tech Stack

- **Language:** Kotlin
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 35 (Android 15)
- **UI:** Material 3 + Custom Glassmorphic Components
- **Image Loading:** Glide 4.16
- **Cloud Storage:** Cloudinary Android SDK 3.0.2
- **Local Storage:** SharedPreferences (SHA-256 hashed)
- **Build System:** Gradle 9.1 + AGP 9.0.1

---

## 🏃 Getting Started

### Prerequisites
- Android Studio (latest stable)
- A Cloudinary account ([sign up free](https://cloudinary.com/))

### Setup

1. **Clone the repo:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/HiveKey.git
   cd HiveKey
   ```

2. **Open in Android Studio** → Sync Gradle

3. **Configure Cloudinary:**
   - Go to [Cloudinary Console](https://console.cloudinary.com/)
   - Your cloud name is already set to `dtmx6ftgr` in `CloudinaryHelper.kt`
   - Go to **Settings → Upload → Upload Presets**
   - Create an **unsigned** preset named: `hivekey_unsigned`

4. **Run the app** on an emulator or physical device

---

## 📂 Project Structure

```
app/src/main/
├── java/com/example/hivekey/
│   ├── SplashActivity.kt          # Animated splash screen
│   ├── PanelSelectionActivity.kt   # Admin/User panel chooser
│   ├── MainActivity.kt            # Login screen
│   ├── SignupActivity.kt          # Registration + selfie capture
│   ├── DashboardActivity.kt       # Role-based dashboard
│   ├── ProfileActivity.kt         # User profile viewer
│   ├── DeveloperActivity.kt       # Developer credits
│   ├── TermsActivity.kt           # Terms & Conditions
│   ├── UserManager.kt             # Local auth (SharedPreferences)
│   └── CloudinaryHelper.kt        # Cloud upload + user log
├── res/
│   ├── layout/                    # 8 XML layouts
│   ├── drawable/                  # Custom drawables & gradients
│   └── values/                    # Strings, colors, themes
└── AndroidManifest.xml
```

---

## 🔑 Admin vs User Panel

| Feature | User 👤 | Admin 🛡️ |
|---------|---------|----------|
| My Digital Passes | ✅ | ✅ |
| My Profile | ✅ | ✅ |
| Developer Info | ✅ | ✅ |
| Terms & Conditions | ✅ | ✅ |
| Issue Digital Pass | ❌ | ✅ |
| Manage Wi-Fi Credentials | ❌ | ✅ |
| View Access Logs | ❌ | ✅ |
| Cloud Database (Cloudinary) | ❌ | ✅ |

---

## 👨‍💻 Team

| Role | Name |
|------|------|
| Lead Developer | **Prateek Das** |
| Contributor | Abishek Kumar Singh |
| Contributor | Kishlay Mishra |

---

## 📄 License

This project is licensed under the MIT License.

---

<p align="center">
  Built with ❤️ by the HiveKey Team
</p>
