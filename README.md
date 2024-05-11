# Pustaka App v1.0 
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/792a2f50-4294-41d1-bb7a-07b00c9dfc99" alt="logo">

# A simple Android app with SQLite, OpenStreetMap, Nominatim, Picasso implementations
*By Malindu Liyanage

### Screenshots

<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/427425f4-1af3-4c09-be6c-f5aeaffeb6ee" alt="Image 1" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/d7b2d654-11a7-44cd-8aa2-30e4b3deedfc" alt="Image 2" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/9c4d54e3-c7e7-4e26-882e-0ffcdef144f7" alt="Image 3" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/e51c697d-677f-4659-b3e5-b62235b280d1" alt="Image 4" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/da123015-9894-4cf7-8994-fa3501a3690b" alt="Image 5" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/0ce82e69-8f1f-44f9-87cb-24d3e7121c0e" alt="Image 6" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/f687ee2f-1f06-4f45-b68f-25fcbfcb61b2" alt="Image 7" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/ae473025-e0bb-4d40-bb22-df24a000484c" alt="Image 8" width="250" height="480">
<img src="https://github.com/MalinduLiyanage/Pustaka_App_v1.0/assets/136006504/00b54ed4-d13f-495a-abe6-988a8052dc03" alt="Image 9" width="250" height="480">

### Overview
The app has a built in SQLite DB that stores data within the device. There is no any internet based DB here, all the operations are done in locally.

### Features
1. Save user location name from selected coordinates by Nominatim Reverse Geocoding
2. Handle user sessions
3. Handle user ID with SMS access (Demonstration to realworld apps)
4. Greeting message according to real time (Good Morning for 12 AM to 11.59 AM, etc)
5. Cards for each book + Thumbnails with caching
6. The UI updates with every DB update
7. Activity Lifecycle management
8. Modern UI + App icons
9. Textwatcher for book search + realtime update
10. New books can be donated with or w/o thumbnails, Thumbs are saved in Appdata
11. Custom Toast Messages with CuteToast

### Used libraries
OpenStreetMap and Nominatim - <a href="https://github.com/osmdroid/osmdroid">https://github.com/osmdroid/osmdroid</a><br>
Picasso Image loader - <a href="https://square.github.io/picasso/">https://square.github.io/picasso/</a><br>

### Last update
2024.05.07

### Known Issues
1. No method to check for Internet connectivity for loading initial book thumbs.

### If there is an Exporting issue
When you download this project and working with Android Studio, sometimes it may say JDK compilation errors.<br>
In that case, Close the project, Go to the project directory and delete .gradle directory and .idea directory, get back and re-open the project using the IDE.
Also try to open the project as a Trusted Project, if you prefer.

### About
This app is a development assignment for Mabile App development course.
Faculty of Applied Sciences,
Rajarata University of Sri Lanka.
