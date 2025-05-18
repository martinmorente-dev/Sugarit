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
- [License](#license)

---

## Project Overview

SugarIt enables users to:
- Scan food labels using their device camera.
- Recognize and extract text from images via ML Kit.
- Retrieve nutritional information and recipes using the Spoonacular API.
- Save and review scan history.

---

## Features

- Real-time label scanning and text recognition.
- Integration with Spoonacular API for nutritional data.
- User-friendly interface with adaptive layouts.
- Local storage of scan history.
- Secure handling of user data and permissions.

---

## Project Structure

SugarIt/
│
├── backend/ # Controllers, models, utilities, viewmodels
│ ├── controllers/ # Business logic controllers
│ ├── models/ # Data models
│ ├── utils/ # Utility classes and helpers
│ └── viewmodels/ # ViewModel classes for MVVM architecture
│
├── Views/ # UI logic and activity/fragment classes
│
├── res/ # XML layouts, drawable resources, strings, etc.
│
├── AndroidManifest.xml # App manifest and permissions
├── build.gradle # Gradle build configuration
└── ... # Other standard Android project files



---

## Installation

1. **Clone the repository:**
git clone https://github.com/martinmorente-dev/Sugarit.git

text
2. **Open in Android Studio:**
- Open Android Studio.
- Select "Open an existing project" and choose the cloned directory.
3. **Build the project:**
- Ensure you have the required SDK (Android 8.0 or higher).
- Sync Gradle and resolve dependencies.
4. **Run the app:**
- Connect an Android device or use an emulator.
- Click "Run" to install and launch the app.

---

## Configuration

- **API Keys:**  
Add your Spoonacular API key in the appropriate configuration file (e.g., `local.properties` or a dedicated config file as specified in the code).
- **Permissions:**  
The app requests camera and storage permissions at runtime.

---

## Usage

- Open the app and grant necessary permissions.
- Use the main screen to scan food labels.
- View recognized ingredients and nutritional information.
- Access your scan history from the menu.

---

## Technical Details

- **Language:** Kotlin
- **Minimum Android Version:** 8.0 (Oreo)
- **Libraries:** ML Kit, Spoonacular API, AndroidX, SQLite
- **Architecture:** MVVM (Model-View-ViewModel)
- **Local Storage:** SQLite for scan history and user preferences

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
