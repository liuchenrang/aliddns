#!/bin/env bash
mvn -Dmaven.test.skip=true package
cp target/*.jar dist