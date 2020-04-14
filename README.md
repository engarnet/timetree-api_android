# TimeTree API Client for Android

## Overview

this is an TimeTree API client library, written in kotlin

## Description

official api document is [here](https://developers.timetreeapp.com/en/docs/api)

## Install

add repository
```:build.gradle
allprojects {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/engarnet/timetree-api_android")
            credentials {
                username = $GITHUB_USER // GitHub's username
                password = $ACCESS_TOKEN // GitHub's Personal access token
            }
        }
    }
}
```

add dependency
```:app/build.gradle
dependencies {
    implementation 'com.engarnet:timetree-api-android:0.1.2'
}
```

## Usage

see [here](https://github.com/engarnet/timetree-api_android/tree/master/app/src/main/java/com/engarnet/timetree)

## Requirement

android:minSdkVersion="19"

## Licence

[MIT](https://github.com/tcnksm/tool/blob/master/LICENCE)

## Author

[engarnet](https://github.com/engarnet)