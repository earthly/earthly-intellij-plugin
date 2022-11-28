#!/bin/bash
set -e
bundle_path=$1
if [ "${bundle_path: -4}" != ".zip" ]; then
  folder=$(pwd)
  cd $bundle_path;
  zip -rqq vsc-bundle.zip *
  cd $folder;
  bundle_path=$bundle_path/vsc-bundle.zip;
fi
mv $bundle_path src/main/resources/