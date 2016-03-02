# Zone Beacon

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

The `app` module contains all application specific code. The `api` module contains code related to connecting to a certain type of control unit. `api` also contains models for the database since they are shared with the API. Lastly, `test_helper` contains some abstracted testing classes that can be used anywhere, cutting down on duplication.