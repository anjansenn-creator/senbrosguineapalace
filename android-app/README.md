# Sen Bros Customer Book

An Android Studio project for keeping customer details and purchase history on one Android device.

## Open in Android Studio

Open this `android-app` folder in Android Studio. Allow Android Studio to sync Gradle and install the Android SDK platform requested by the project (API 35). Run the `app` configuration on an Android phone or emulator.

## What it does

- Creates a sequential customer code (`SBGP-000001`, and so on).
- Saves customer name, address, primary and alternate phone, and email.
- Searches customer name, code, and primary phone.
- Records each purchase date, item, quantity, unit price, payment details, and amount paid.
- Exports a customer statement as a PDF through Android's Save dialog.
- Stores data in the app's private SQLite database. The app does not request network access and Android backup is disabled.

The database belongs to this app installation on this device. Uninstalling the app or losing the device can remove the records. Save customer PDFs somewhere backed up if you need copies outside the phone.

## Notes

This starter app has no account login or cloud synchronization. Customer records are only available inside this app on this device. PDF export includes contact fields and purchase history in a table.
