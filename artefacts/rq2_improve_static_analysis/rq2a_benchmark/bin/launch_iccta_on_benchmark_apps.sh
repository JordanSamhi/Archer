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

for app in ../benchmark_apps/old_apis_for_old_tools/*.apk
do
    print_info "Processing $app"
    ./launch_iccta.sh -p $PLATFORMS -d cc -a $app
done
