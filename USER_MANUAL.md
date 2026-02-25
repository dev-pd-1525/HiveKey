# 📖 HiveKey — User Manual

## Table of Contents
1. [Installation](#installation)
2. [First Launch](#first-launch)
3. [Panel Selection](#panel-selection)
4. [Creating an Account](#creating-an-account)
5. [Logging In](#logging-in)
6. [User Dashboard](#user-dashboard)
7. [Admin Dashboard](#admin-dashboard)
8. [Profile](#profile)
9. [Logging Out](#logging-out)
10. [Troubleshooting](#troubleshooting)

---

## 1. Installation

1. Transfer the `app-release.apk` file to your Android phone
2. Open the APK file on your phone
3. If prompted, enable **"Install from unknown sources"** in Settings
4. Tap **Install** → **Open**

> **Requires:** Android 8.0 (Oreo) or higher

---

## 2. First Launch

When you open HiveKey for the first time, you'll see an **animated splash screen** with the HiveKey logo and a progress bar. The app will then navigate you to the **Panel Selection** screen.

---

## 3. Panel Selection

You'll see two options:

- **🛡️ Admin Panel** — For administrators who need full control over access management, including issuing digital passes, managing Wi-Fi credentials, viewing access logs, and accessing cloud storage.

- **👤 User Panel** — For regular users who need to view their digital passes and manage their profile.

**Tap on the panel that matches your role.** You'll be taken to the login screen.

---

## 4. Creating an Account

If you don't have an account yet:

1. Tap **"Create Account"** on the login screen
2. Fill in your details:
   - **Full Name**
   - **Email Address**
   - **Password** (minimum 6 characters)
   - **Confirm Password**
3. **Take a selfie** (optional but recommended):
   - Tap the **"📸 Tap to take a selfie"** button to use your camera
   - Or tap the avatar circle to choose between camera and gallery
4. Tap **Sign Up**

Your account will be created and your selfie (if taken) will be uploaded to the cloud.

---

## 5. Logging In

1. Enter your **email** and **password**
2. Tap **Login**
3. You'll be taken to the dashboard based on the panel you selected

---

## 6. User Dashboard

As a **User**, you'll see:

| Card | What it does |
|------|-------------|
| 🎫 **My Digital Passes** | View your issued digital passes (coming soon) |
| 👤 **My Profile** | View your profile photo, name, email, and role |
| 👨‍💻 **Developer Info** | View info about the development team |
| 📜 **Terms & Conditions** | Read the app's terms of service |

The dashboard also shows:
- ☀️ A time-based greeting (Good Morning/Afternoon/Evening)
- 📍 Your current location (city, state)
- Your profile picture and name

---

## 7. Admin Dashboard

As an **Admin**, you'll see everything a user sees, **plus**:

| Card | What it does |
|------|-------------|
| 🔑 **Issue Digital Pass** | Create and issue new digital access passes (coming soon) |
| 📶 **Manage Wi-Fi** | Manage Wi-Fi credentials for access points (coming soon) |
| 📊 **Access Logs** | View detailed access logs and history (coming soon) |
| ☁️ **Cloud Database** | Opens the Cloudinary console in your browser to view all uploaded photos and user logs |

---

## 8. Profile

Tap the **👤 My Profile** card on the dashboard to see:
- Your profile photo (from Cloudinary)
- Your name
- Your email
- Your assigned role (Admin/User)

---

## 9. Logging Out

1. Scroll to the bottom of the dashboard
2. Tap the **"Logout"** button
3. You'll be taken back to the Panel Selection screen
4. Your local session is cleared

---

## 10. Troubleshooting

| Problem | Solution |
|---------|----------|
| **App won't install** | Enable "Install from unknown sources" in phone Settings → Security |
| **Login fails** | Double-check your email and password. Password is case-sensitive |
| **Selfie not uploading** | Ensure you have an internet connection. The Cloudinary upload preset `hivekey_unsigned` must be created |
| **Location not showing** | Grant location permission when prompted. Ensure GPS is turned on |
| **Camera not working** | Grant camera permission when prompted |
| **Admin cards not showing** | Make sure you selected **Admin Panel** on the panel selection screen |

---

## 📞 Support

For any issues or feature requests, contact the development team:
- **Lead Developer:** Prateek Das
- **Contributors:** Abishek Kumar Singh, Kishlay Mishra

---

<p align="center">
  HiveKey v1.0 — Secure Access Control Management System
</p>
