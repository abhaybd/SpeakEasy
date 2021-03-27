#!/bin/bash

npm install
npm run build

rm -rf ../speakeasy-backend/src/main/resources/public
mkdir -p ../speakeasy-backend/src/main/resources/public
cp -r build/* ../speakeasy-backend/src/main/resources/public
