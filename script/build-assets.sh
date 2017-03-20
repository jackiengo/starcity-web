#!/usr/bin/env bash
mkdir resources/public/assets/css
echo "Installing external JS & CSS dependencies via Bower..."
bower install
echo "Installing NPM dependencies..."
npm install
echo "Compiling ant design LESS..."
lessc --clean-css style/less/antd.less resources/public/assets/css/antd.css
echo "Compiling public-facing SASS..."
sass style/sass/public.scss:resources/public/assets/css/public.css
echo "Compiling internal SASS..."
# TODO: Rename starcity.css to something else
sass style/sass/main.sass:resources/public/assets/css/starcity.css --style compressed
