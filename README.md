# ⚡ 5G Only Switcher

> A lightweight Android utility to instantly jump into your phone's hidden **Phone Information** screen — where you can force **NR Only (5G-only)** mode in seconds.

<p align="center">
  <img src="https://img.shields.io/badge/Android-API%2030%2B-brightgreen?logo=android" />
  <img src="https://img.shields.io/badge/Language-Java-orange?logo=java" />
  <img src="https://img.shields.io/badge/License-MIT-blue" />
  <img src="https://img.shields.io/badge/Ads-AdMob-yellow?logo=google" />
</p>

---

## 📖 About

Most Android devices don't expose a simple toggle to lock onto 5G-only (NR-Only) mode. This app bridges that gap by deep-linking directly into the system's hidden **Radio/Phone Information** screen — no root required.

If the direct launch fails on your device (OEM component names vary), the app automatically falls back to opening the dialer pre-filled with `*#*#4636#*#*` so you can navigate there manually.

---

## ✨ Features

- 🚀 **One-tap launch** into the system Phone Information screen
- 🔍 **Smart component detection** — tries 11+ known OEM activity names (AOSP, Samsung, MediaTek, Huawei)
- 📲 **Automatic fallback** to `*#*#4636#*#*` dialer code if direct launch fails
- 🌙 **Premium dark UI** — slate/cyan themed Material Design
- 📋 **Step-by-step instructions** shown in-app
- 📢 **AdMob banner** integration

---

## 📸 Screenshots

> _Coming soon_

---

## 🛠️ How It Works

Since changing modem/radio settings (NR-only mode) requires **system or carrier privileges**, this app does **not** change settings programmatically. Instead, it acts as a smart launcher:

```
App launches
    │
    ▼
Try known OEM component names for RadioInfo / PhoneInfo screen
(com.android.settings, com.android.phone, Samsung, MediaTek, Huawei…)
    │
    ├── SUCCESS → Opens Phone Information screen directly ✅
    │
    └── FAIL → Opens dialer with *#*#4636#*#* → User navigates manually 📞
```

Once inside **Phone Information**:
1. Find **"Set Preferred Network Type"**
2. Select **`NR Only`** from the dropdown
3. Done — your device is now locked to 5G only 🎉

---

## 📋 Requirements

| Requirement | Detail |
|---|---|
| Android Version | Android 11+ (API 30) |
| Permissions | None required |
| Root | ❌ Not required |
| Internet | For AdMob ads only |

---

## 🚀 Getting Started

### Clone the repo

```bash
git clone https://github.com/abhishek90-21/5GOnlySwitcher.git
cd 5GOnlySwitcher
```

### Build & Run

1. Open in **Android Studio**
2. Let Gradle sync
3. Run on a physical device (emulators won't have modem settings)

---

## 📦 Tech Stack

| Component | Library / Tool |
|---|---|
| UI | Material Components for Android |
| Layout | ConstraintLayout |
| Ads | Google Mobile Ads SDK (`play-services-ads:24.9.0`) |
| Language | Java |
| Build | Gradle (Kotlin DSL) |

---

## ⚠️ Compatibility Notice

> The hidden **Phone Information** screen uses **OEM-specific** Activity class names that differ across manufacturers and Android versions. This app tries the most common variants automatically.

| OEM | Likely Compatibility |
|---|---|
| Stock Android (Pixel) | ✅ High |
| Samsung One UI | ✅ High |
| MediaTek devices | ✅ Medium–High |
| Huawei / Honor | ⚠️ Variable |
| Other OEMs | ⚠️ Use fallback dialer code |

If the direct launch fails on your device, the app will open the dialer with `*#*#4636#*#*` — tap **Phone Information** from that menu.

---

## 🤝 Contributing

Contributions are welcome! If you know the exact component name for an OEM not listed, feel free to open a PR or issue.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/add-oem-support`)
3. Commit your changes (`git commit -m 'Add XYZ OEM support'`)
4. Push to the branch (`git push origin feature/add-oem-support`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Abhishek** — [@abhishek90-21](https://github.com/abhishek90-21)

---

<p align="center">Made with ❤️ for 5G enthusiasts</p>
