#!/bin/env bash
mvn -Dmaven.test.skip=true package
rm -rf dist
mkdir dist
cp ddns-command/target/*.jar dist
mkdir dist/config
mkdir dist/result
cp -rf config dist/
cp README.md dist/