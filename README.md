# Static

### Lane Kersten

### DVP6-1903

​	Static is an app that allows gamers to find people with similar game interests, both online or in person. It functions by presenting users with profiles of other users that they may be interested in playing with, and allowing them to approve or decline that suggestion. Users are entered into a chat message channel with other users they have matched with so they can setup times to game or just talk. All background data, such as user profiles, is handled through Firebase.



### Installation:

To Install Static you will need the application's APK and an android device/emulator. For installation on an emulator all you need to do is drag the APK file onto the home screen of the emulator. For hardware devices you need to download the APK onto the device and open it from the file system.

*Please be sure to check specifications below for compatible devices and operating systems*



### Specifications:

**Minimum SDK Version:** API 23

**Maximum SDK Version:** API 28

**Target SDK Version:** API 28

**Testing Emulator:** Pixel XL (Android 9, API 28)



### Testing Information:

**Test User #1:**

- Email: test@test.com
- Password: TestTest321

**Test User #2:**

- Email: test2@test.com
- Password: TestPassword

For testing you can either create a new user through the sign up process or use a test user above. Connections are currently made via platform. In the future users will select a game to search for from their list of games. Location searching is currently functional, but only searches in a fixed 10 Kilometer radius around the user. Users are placed on a blacklist after another user accepts or declines their profile, so no user will show up twice (this makes test somewhat difficult). In the future a larger pool of users will be added to prevent this issue.



#### Known Issues:

- No game based searching
- Slow profile loading