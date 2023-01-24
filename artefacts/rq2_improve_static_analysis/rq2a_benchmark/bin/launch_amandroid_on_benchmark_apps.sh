#!/bin/bash

. ./common.sh --source-only

for app in ../benchmark_apps/old_apis_for_old_tools/*.apk
do
    print_info "Processing $app"
    java -jar amandroid.jar t $app 
done
