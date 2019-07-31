# Zone Beacon

![Screenshot](preview.png)

### Building

`./gradlew build`

This will assemble the APK, sign it, minify it, and run tests on it.

### Testing

`./gradlew test`

### Organization

The app currently contains a few differnt modules:

1. app
2. api
3. test_helper

The `app` module contains all application specific code. The `api` module contains code 
related to connecting to a certain type of control unit. `api` also contains models for the 
database since they are shared with the API. Lastly, `test_helper` contains some abstracted 
testing classes that can and should be used in other modules.

### Play Store Release

There is a `debug.keystore` and a `keystore.jks` that are used for signing the application in
debug mode and release mode, respectively. Neither of these should be used for signing the app
to be released onto the Play Store!!! The best way to publish to the Play Store would be setting
the app up on a build server like Jenkins and replacing the `keystore.jks` file with a new, secure
version. To do this, a shell script like the following would work well:

```
curl -o keystore.jks <url-to-your-release-keystore-to-download>

cat > keystore.properties << EOF
keystorepassword=<your-keystore-password>
keyalias=<your-key-alias>
keypassword=<your-key-password>
EOF

cat > api_keys.properties << EOF
NEARBY=<your-google-nearby-api-key>
EOF

./gradlew clean assemble testDebugUnitTestCoverage
```

After running the above and replacing the appropriate fields, you'll have a build of the
app in `app/build/outputs/apk/Zone-Beacon-<version-number>-release.apk`. You'll also have
a test coverage file taken using JaCoCo in 
`app/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html`.

#### From CI 

This Play Store release system is designed to be done on a continuous integration server. You can look at files in the cicd folder to see how we have used environment variables from our build server to configurate the Play Store release build.  These files may need to be altered to work on your own AWS account.

### Google Play Services "Nearby" Integration

Nearby is used to transfer the settings from one device to the other on the initial setup. Currently, if you make a debug version of this app, this functionality will not work, because the API Key that is provided in this repo corresponds to the release keystore that is included. If you make a release build (without using the steps above for a Play Store release), then the nearby integration will work as expected.

For a Play Store release, you will have to set up a new API key that corresponds with the signing configuration keystore that you use.

https://developers.google.com/nearby/messages/android/get-started