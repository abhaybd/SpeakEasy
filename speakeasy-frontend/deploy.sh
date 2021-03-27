#!/bin/bash

npm run build

mkdir -p ../speakeasy-backend/src/main/resources/public
cp -r build/* ../speakeasy-backend/src/main/resources/public
