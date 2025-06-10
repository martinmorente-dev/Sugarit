# SugarIt

**SugarIt** is an Android application designed to help users scan food labels, recognize ingredients, and access nutritional information using ML Kit and the Spoonacular API.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Technical Details](#technical-details)
- [Security Policy](#security-policy)
- [Backup Policy](#backup-policy)
- [Contributing](#contributing)
---

## Project Overview

SugarIt enables users to:
- Scan food labels using their device camera.
- Retrieve nutritional information and recipes using the Spoonacular API.
---

## Features

- Real-time label scanning and text recognition.
- Integration with Spoonacular API for nutritional data.
- User-friendly interface with adaptive layouts.
- Secure handling of user data and permissions.

---

## Project Structure

SugarIt/<br>
│<br>
├── backend/ # Controllers, models, utilities, viewmodels<br>
│ ├── controllers/ # Business logic controllers<br>
│ ├── models/ # Data models<br>
│ ├── utils/ # Utility classes and helpers<br>
│ └── viewmodels/ # ViewModel classes for MVVM architecture<br>
│<br>
├── Views/ # UI logic and activity/fragment classes<br>
│<br>
├── res/ # XML layouts, drawable resources, strings, etc.<br>
│<br>
├── AndroidManifest.xml # App manifest and permissions<br>
├── build.gradle # Gradle build configuration<br>
└── ... # Other standard Android project files<br>


---

## Installation

1. **Go to this url and follow the steps:**
https://github.com/martinmorente-dev/SugarItApk



## Technical Details

- **Language:** Kotlin
- **Minimum Android Version:** 8.0 (Oreo)
- **Libraries:** ML Kit, Spoonacular API, AndroidX, SQLite
- **Architecture:** MVVM (Model-View-ViewModel)

---

## Security Policy

- Sensitive data (API keys, user info) is stored securely and not hard-coded in the repository.
- All permissions are requested at runtime and explained to the user.
- No personal data is shared with third parties.

---

## Backup Policy

- Local database backups are handled via Android's backup system.
- Source code and documentation are versioned and backed up on GitHub.

---

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for improvements or bug fixes.
