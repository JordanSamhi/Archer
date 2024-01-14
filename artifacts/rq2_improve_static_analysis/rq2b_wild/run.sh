#!/bin/bash

# Initialize variables from script arguments
apk="$1"
platforms="$2"
redisServer="$3"
redisPort="$4"
redisPwd="$5"

# Initialize an empty string to hold optional parameters
optional_params=""

# Conditionally add optional parameters
[ -n "$redisServer" ] && optional_params+=" -s $redisServer"
[ -n "$redisPort" ] && optional_params+=" -n $redisPort"
[ -n "$redisPwd" ] && optional_params+=" -w $redisPwd"

# Run the Java command with conditional parameters
java -jar archer.jar -a "$apk" -p "$platforms" $optional_params

# Remove apk
rm "$apk"