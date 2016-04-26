#!/bin/bash

set -e

# ANDROID

# download android sdk
curl -LO http://dl.google.com/android/android-sdk_r24.2-linux.tgz

tar -xvf android-sdk_r24.2-linux.tgz

# install all sdk packages
#./android-sdk-linux/tools/android update sdk --no-ui

expect -c '
set timeout -1;
spawn android - update sdk --no-ui;
expect {
    "Do you accept the license" { exp_send "y\r" ; exp_continue }
    eof
}
'

export PATH=${PATH}:$HOME/sdk/android-sdk-linux/platform-tools:$HOME/sdk/android-sdk-linux/tools:$HOME/sdk/android-sdk-linux/build-tools/22.0.1/

# adb
sudo apt-get install libc6:i386 libstdc++6:i386
# aapt
sudo apt-get install zlib1g:i386

# BUILD

gradlew build test