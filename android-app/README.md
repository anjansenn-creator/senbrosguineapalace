# Sen Bros Customer Book

Open this project folder in Android Studio. It uses Android Gradle Plugin 9.4.0 with Gradle 9.6. The app works offline and keeps customer records in its private SQLite database.

## Features
- Search and edit customers, and search or edit purchases.
- Filter purchases by date and search item, category, or notes.
- Record purchase date, item, category, quantity, price, payment, amount paid, notes, and a scanned bill attachment.
- Export, share, and print customer PDFs; export an Excel workbook with Customers and Purchases sheets.
- Back up and restore all records. In Android's file chooser, choose Google Drive as the file location. To attach a bill, scan it in Google Drive first, then select its PDF or image from the attachment picker.

## Keep existing records when updating
Install the new APK over the existing app on the same phone; do not uninstall the old app first. The app upgrades its database in place. Keep a backup in Drive before restoring a backup, because restore replaces records currently on the phone.

The app requests no network access. Google Drive access uses Android's document picker and requires the Drive app/account to be available on the phone. Backup and restore are user initiated.