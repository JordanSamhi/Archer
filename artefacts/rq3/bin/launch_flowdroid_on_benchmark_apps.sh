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

sources_sinks="sourcesAndSinks_flowdroid.txt"

for app in ../benchmark_apps/*.apk
do
    print_info "Processing $app"
    java -jar flowdroid.jar -p $PLATFORMS -a $app -s $sources_sinks
done
