# My SNS Demo

An Android social app featuring offline-first architecture, Firebase sync, and modern UI with Jetpack Compose.

##  Features

- **Authentication** with Firebase Email/Password
- **Real-time Post Management** via Firestore
- **Offline-first** timeline with Room and WorkManager
- **Automatic Syncing** between Firebase and local DB
- **Network Awareness** for seamless offline experience
- **Clean Architecture** with MVI + Hilt


## Demo Videos


### Authentication

<table>
  <tr>
    <td align="center">
      <strong>Register</strong><br/>
      <video src="https://github.com/user-attachments/assets/6e1f8709-f3db-4c1f-aca9-0b127d4b76bd" controls width="200"></video>
    </td>
    <td align="center">
      <strong>Login</strong><br/>
      <video src="https://github.com/user-attachments/assets/7dae5dae-e174-42b6-98f1-b0b651921955" controls width="200"></video>
    </td>
    <td align="center">
      <strong>Logout</strong><br/>
      <video src="https://github.com/user-attachments/assets/1116576b-cd0e-43db-a495-25fc1180edce" controls width="200"></video>
    </td>
  </tr>
</table>

### Post Creation

<table>
  <tr>
    <td align="center">
      <strong>Post text</strong><br/>
      <video src="https://github.com/user-attachments/assets/8679ccd7-67d5-423e-8cf2-23b62d69ed5c" controls width="200"></video>
    </td>
    <td align="center">
      <strong>Post pic</strong><br/>
      <video src="https://github.com/user-attachments/assets/81e8ab42-f325-4f27-aa11-260acaccb74b" controls width="200"></video>
    </td>
  </tr>
</table>

### Sync & Offline Support

<table>
  <tr>
    <td align="center">
      <strong>Firebase sync</strong><br/>
      <video src="https://github.com/user-attachments/assets/2fe15487-a9d7-4ac7-9986-3af87944ed34" controls width="200"></video>
    </td>
    <td align="center">
      <strong>Offline post</strong><br/>
      <video src="https://github.com/user-attachments/assets/fea0d5e6-9402-42d1-922a-18716c41e31b" controls width="200"></video>
    </td>
  </tr>
</table>

## Technical Highlights

1. **Modern Architecture**
   - Clean Architecture for maintainable and testable code
   - MVI pattern for unidirectional data flow
   - Single source of truth for UI state

2. **Single Source of Truth**
   - Room Database as the single source of truth
   - Firebase data synchronization with local database
   - Consistent data flow: Firebase → Room → UI
   - Offline-first approach with background sync

3. **Reactive Programming**
   - Kotlin Coroutines and Flow for asynchronous operations
   - Reactive UI updates

4. **Offline Support**
   - Room Database for local data storage
   - WorkManager for background tasks and data synchronization

5. **Dependency Injection**
   - Hilt for dependency injection
   - Modular design for better testing and maintenance

6. **Modern UI**
   - Jetpack Compose for modern UI construction

7. **Network Handling**
   - Network status monitoring
   - Offline mode support

## 🧱 Tech Stack & Architecture

- **Language**: Kotlin 1.9.0
- **UI**: Jetpack Compose 1.5.0
- **Architecture**: Clean Architecture with MVI pattern (unidirectional data flow)
- **State Management**: Kotlin Coroutines & Flow 1.7.3
- **Persistence**: Room Database 2.6.0 as single source of truth
- **Cloud Services**: Firebase (Auth, Firestore, Storage) – BOM 32.7.0
- **Background Tasks**: WorkManager 2.9.0
- **Dependency Injection**: Hilt 2.48

## Module Overview

### Data Layer
- `data/local`: Local data storage (Room Database)
- `data/repository`: Repository implementations
- `data/worker`: Background work processing (WorkManager)

### Domain Layer
- `domain`: Business logic interfaces
- `model`: Data model definitions

### UI Layer
- `ui/component`: Reusable UI components
- `ui/theme`: Application theme definitions
- `ui/screens`: Feature screen implementations
  - Login/Register
  - Timeline
  - Post Creation
  - Profile

### Utils
- Network status monitoring
- Common utility classes

## Testing

This project includes unit tests to ensure reliability and maintainability of the business logic.

- ViewModel intent → state verification
- Repository mocking with controlled test data
- Use case logic validation

<img src="https://github.com/user-attachments/assets/17ea6699-dd8e-416d-a994-a3f352b9d9e6" width="600"/>

## Development Environment Requirements

- Android Studio Hedgehog | 2023.1.1 or higher
- Kotlin 1.9.0 or higher
- Android SDK 34 or higher
- Gradle 8.0 or higher

## Installation Instructions

1. Clone the project
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the application


## Notes

- Firebase project configuration and corresponding configuration files are required
- Ensure all necessary SDK tools are installed 
