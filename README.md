# Static

### Lane Kersten

### DVP6-1903

​	Static is an app that allows gamers to find people with similar game interests, both online or in person. It functions by presenting users with profiles of other users that they may be interested in playing with, and allowing them to approve or decline that suggestion. Users are entered into a chat message channel with other users they have matched with so they can setup times to game or just talk. All background data, such as user profiles, is handled through Firebase.



### Installation:

To Install Static you will need the application's APK and an android device/emulator. For installation on an emulator all you need to do is drag the APK file onto the home screen of the emulator. For hardware devices you need to download the APK onto the device and open it from the file system.

*Please be sure to check specifications below for compatible devices and operating systems*



### Specifications:

**Minimum SDK Version:** API 21

**Maximum SDK Version:** API 28

**Target SDK Version:** API 28

**Testing Emulator:** Pixel XL (Android 9, API 28)



### Testing Information:

**Test User:**

- Email: test@test.com
- Password: TestTest321

For testing you can either create a new user through the sign up process or use the test user above. The Firebase database is connected to user profiles now so the test user might be preferred given that it already has some data in the database. If you create a new user, you will have to edit the profile before any custom information will appear on the profile screen.



#### Known Issues:

- Currently the browse page doesn't do anything but display the current users profile just to test how it looks.
- Users can't currently approve or decline connections with users, which was part of Milestone #1, due to the full profile database implementation being part of Milestone #2.
- UI is mostly placeholder and doesn't reflect the goal for the final design