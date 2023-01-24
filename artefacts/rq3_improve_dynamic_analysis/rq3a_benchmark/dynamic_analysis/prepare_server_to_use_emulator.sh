cd
wget http://dl.google.com/android/android-sdk_r24.4.1-linux.tgz
tar -xvf android-sdk_r24.4.1-linux.tgz
rm android-sdk_r24.4.1-linux.tgz
cd android-sdk-linux/

SDK_PATH=$(pwd)
echo "export ANDROID_SDK_ROOT=$SDK_PATH" | tee -a ~/.bashrc
echo "export ANDROID_HOME=$SDK_PATH" | tee -a ~/.bashrc
echo "PATH=$PATH:$ANDROID_HOME/platform-tools" | tee -a ~/.bashrc
echo "PATH=$PATH:$ANDROID_HOME/tools"| tee -a ~/.bashrc
echo "PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin/"| tee -a ~/.bashrc
echo "PATH=$PATH:$ANDROID_HOME/emulator/"| tee -a ~/.bashrc
echo "ANDROID_AVD_HOME=$HOME/.android/avd" | tee -a ~/.bashrc
source ~/.bashrc

wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip
unzip -o commandlinetools-linux-8512546_latest.zip
rm commandlinetools-linux-8512546_latest.zip
cmdline-tools/bin/sdkmanager --install "cmdline-tools;latest" --sdk_root=$(pwd)
yes | ./cmdline-tools/bin/sdkmanager --licenses --sdk_root=$SDK_PATH
echo yes | cmdline-tools/bin/sdkmanager platform-tools emulator --sdk_root=$(pwd)
./tools/android update sdk --no-ui
./cmdline-tools/bin/sdkmanager --update --sdk_root=$SDK_PATH
mv build-tools/30.0.0-preview/ build-tools/30.0.0-rc1
cmdline-tools/bin/sdkmanager "build-tools;33.0.0" --sdk_root=$(pwd)

echo
echo "Environment installed"
echo

echo "HELP"
echo

echo "ZIPALIGN path:"
find ./ -name "zipalign"

echo "AAPT path:"
find ./ -name "aapt"

echo "APKSIGNER path:"
find ./ -name "apksigner"
echo

echo "EMULATOR"
echo "To list available system images: sdkmanager --list"
echo "To install image for API level 28: sdkmanager --install \"platforms;android-28\" \"system-images;android-28;default;x86_64\""
echo "To create new virtual device: echo \"no\" | avdmanager --verbose create avd --force --name \"avd86_64\"  --package \"system-images;android-28;default;x86_64\" --tag \"default\" --abi \"x86_64\""
echo "To launch enulator: ./emulator/emulator -no-window -no-audio -avd AVD_NAME"

echo "GRANT PERMISSION"
echo "adb shell pm grant PACKAGE_NAME PERMISSION"

echo "MONKEY:"
echo "adb shell monkey -p PACKAGE_NAME --throttle 1000 10"

echo "PROBLEMS:"
echo "if problems: check that java8 is used"
echo "check that you are in group kvm"
