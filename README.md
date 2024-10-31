# Connect Android to Windows

This project enables clipboard synchronization between an Android device and a Windows computer using Bluetooth. The Android app detects clipboard changes and sends the data to the Windows application, which updates the clipboard on the Windows computer.

## Setup Instructions

### Android App

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Add the necessary permissions for accessing Bluetooth in the `AndroidManifest.xml` file.
4. Implement the main activity and background service in the Android app.
5. Build and install the app on your Android device.

### Windows Application

1. Clone the repository to your local machine.
2. Open the project in Visual Studio.
3. Implement the Windows application to receive clipboard data via Bluetooth.
4. Build and run the application on your Windows computer.

## Usage Instructions

### Android App

1. Launch the app on your Android device.
2. Allow the app to access Bluetooth and clipboard data.
3. The app will run in the background and monitor clipboard changes.

### Windows Application

1. Run the application on your Windows computer.
2. Ensure Bluetooth is enabled on both the Android device and Windows computer.
3. The application will receive clipboard data from the Android device and update the clipboard on the Windows computer.
