# Carton
_v0.1.0 - turn the phone to glass of your smart device_

__Get more information about this project on the official website [carton.mobi](http://carton.mobi)__

## Getting Started
Either you start a new app or you adapt an existing one, there are few easy steps to do in order to fully enjoy a CARTON Viewer.
1. Add the library to your gradle app file
```java
dependencies {
    ...

    compile 'mobi.carton:library:0.1.0'
}
```
2. Update your manifest: set the orientation of your activity (all of them) to `landscape`, add a category to make your launcher (only) activity compatible, finally, add a simple description.
```xml
<application
    ...
    android:description="@string/app_description">

    <activity
        ...
        android:screenOrientation="landscape">
        <intent-filter>
            <action android:name="android.intent.action.MAIN"/>
            <category android:name="android.intent.category.LAUNCHER"/>
            ...
            <category android:name="mobi.carton.intent.category.COMPATIBLE"/>
        </intent-filter>
    </activity>
</application>
```
3. Extend your activity with CartonActivity
```java
public class MyActivity extends CartonActivity {
    ...
```

## Auto adaptive screens
In order to fully use the Carton viewer, everything on the screen needs to be horizontally reversed, the brightness set the maximum, and the size and margin has to be set to respectively 60x35mm and 10x10mm (from top left).
Carton SDK make it easy by extending `CartonActivity` class instead of `Activity`.

```java
public class MainActivity extends CartonActivity {

    ...
```


## Default Launcher Activity
The default launcher provide help to the user to place the mobile phone into the Carton viewer.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    startDefaultLauncher();

    ...
}
```

![Alt text](/misc/launcher.png?raw=true "Default Launcher")

## Head Gesture Recognition
This library is here to help head gesture recognition when using Carton. Three kinds of gestures are available : `tilting`, `nodding`, and `shaking`.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ...

    HeadRecognition headRecognition = new HeadRecognition(this);
    headRecognition.setOnHeadGestureListener(new HeadRecognition.OnHeadGestureListener() {
        @Override
        public void onTilt(int direction) {
            switch (direction) {
                case HeadRecognition.TILT_RIGHT:
                    // do whatever
                break;
                case HeadRecognition.TILT_LEFT:
                    // do whatever
                break;
            }
        }

        @Override
        public void onNod(int direction) {
            // ... do whatever
        }

        @Override
        public void onShake() {
            // ... do whatever
        }
    });
}
```

## Download
### Gradle
```java
compile 'mobi.carton:library:0.1.0'
```

## Contributing
If you would like to contribute code, you can do so through GitHub by forking the repository and sending a pull request.
When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Authors
It is an open-source and open-design project, everyone is welcome or encouraged to contribute. Besides, this project is funded and supported by [Natural Sciences and Engineering Research Council](http://www.nserc-crsng.gc.ca/index_eng.asp) of Canada and [44 screens](http://44screens.com/en-us).
44 Screens is a start-up specialized in augmented reality, mobile and wearable apps.
