# TFL Road Status

This is an app that consumes the API from TFL to display the status of a road in London.

* Retrofit + OKHttp
* RxJava2
* Dagger2
* MVP architecture (with pure Kotlin presenter for quick JUnit tests)
* Android Kotlin extensions
* JUnit tests (with Mockito)
* UI tests with Espresso (run on offlineDebug variant!)
* Offline and connected variants (for offline work and tests)

## TFL keys

In order to run the app, please replace the placeholder APP_ID and APP_KEY fields in the `build.gradle`
files with your own app id and key. Please note the syntax: the strings need to be
wrapped by both single quotes and double quotes like this:

```
buildConfigField "String", "TFL_APP_ID", '"change_me"'
buildConfigField "String", "TFL_APP_KEY", '"change_me"'
```

## Offline vs connected flavors

Please note which variant you are running. Offline variant is meant for tests and development while
offline. The data is mocked so it will always respond with "A2" for any road ID that is not "ERROR_ROAD"
(for which an error is displayed). You probably want the online version for playing around with the app.

## Running tests

The app has 2 set of tests: the first ones are JUnit tests that look at the presenter and instrumented
Espresso tests that follow the User Stories provided. There are no special steps to running these, you
only want to make sure you're running them in offline mode:

```./gradlew testOfflineDebug```
```./gradlew connectedOfflineDebugAndroidTest```

## Architecture

This is a basic MVP implementation.

![](https://cdn.rawgit.com/acristescu/GreenfieldTemplate/86c6e7a/architecture.svg)

The flow of events and data:

1. The __Activity__ reacts to the user input by informing the __Presenter__ that data is required.
1. The __Presenter__ fires off the appropriate request in the service layer (and instructs the __Activity__ to display a busy indicator).
1. The __Service__ then issues the correct REST call to the __Retrofit__ layer.
1. The __Retrofit__ layer exchanges HTTP requests and responses with the Server and returns an `Observable` (or perhaps `Single`).
1. The __Service__ possibly inspects the `Observable`, schedules it on the correct thread (in order to keep the __Presenter__ free of Android Schedulers and thus pure JUnit testable) and returns it back to the __Presenter__.
1. The __Presenter__ receives the `Observable`, retrieves the data or error and issues the correct commands to the __Activity__ to update the UI (and dismiss the busy indicator).
1. The __Activity__ presents the user with the data or error message.
