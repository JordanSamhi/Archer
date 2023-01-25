#!/bin/bash

. ./common.sh --source-only

while getopts p: option
do
    case "${option}"
        in
        p) PLATFORMS=${OPTARG};;
    esac
done

if [ -z "$PLATFORMS" ]
then
    echo
    read -p "Platforms path: " PLATFORMS
fi

for app in ../benchmark_apps/*.apk
do
    print_info "Processing $app"
    java -jar archer.jar -p $PLATFORMS -a $app -t
done
