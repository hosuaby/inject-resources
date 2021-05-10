#!/usr/bin/env zsh

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

CHECK_CHAR='\U2713'
CROSS_CHAR='\U2717'

function print_result() {
  version=$1
  result=$2

  if [[ $result == 0 ]];
  then
    mark=$CHECK_CHAR
    color=$GREEN
  else
    mark=$CROSS_CHAR
    color=$RED
  fi

  echo "\t${color}${version} ${mark}${NC}"
}

junit_versions=( "5.6.3" "5.7.1" "5.8.0-M1" )
jackson_versions=( "2.10.5" "2.11.4" "2.12.3" )
gson_versions=( "2.8.6" )
snakeyaml_versions=( "1.28" )

for junit_version in $junit_versions; do
  echo "Tests with JUnit ${junit_version}:"

  printf "\tRun tests with JUnit %s...\n" "${junit_version}"
  ./gradlew clean build -DJUNIT_VERSION="${junit_version}" &> /dev/null
  print_result "$junit_version" $?
done

for jackson_version in $jackson_versions; do
  echo "Tests with Jackson ${jackson_version}:"

  printf "\tRun tests with Jackson %s...\n" "${jackson_version}"
  ./gradlew clean build -DJACKSON_VERSION="${jackson_version}" &> /dev/null
  print_result "$jackson_version" $?
done

for gson_version in $gson_versions; do
  echo "Tests with GSON ${gson_version}:"

  printf "\tRun tests with GSON %s...\n" "${gson_version}"
  ./gradlew clean build -DGSON_VERSION="${gson_version}" &> /dev/null
  print_result "$gson_version" $?
done

for snakeyaml_version in $snakeyaml_versions; do
  echo "Tests with Snakeyaml ${snakeyaml_version}:"

  printf "\tRun tests with Snakeyaml %s...\n" "${snakeyaml_version}"
  ./gradlew clean build -DSNAKE_YAML_VERSION="${snakeyaml_version}" &> /dev/null
  print_result "$snakeyaml_version" $?
done
