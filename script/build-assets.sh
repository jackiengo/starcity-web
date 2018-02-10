#!/usr/bin/env bash
mkdir resources/public/assets/css
echo "Installing NPM dependencies..."
npm install
echo "Compiling SASS..."
sass -E "UTF-8" style/sass/public.scss:resources/public/assets/css/public.css
echo "Putting slick assets where they're supposed to go..."
cp -r node_modules/slick-carousel/slick/fonts resources/public/assets/css
cp node_modules/slick-carousel/slick/ajax-loader.gif resources/public/assets/css
