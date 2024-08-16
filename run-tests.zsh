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

junit_versions=( "5.6.3" "5.7.1" "5.8.0" "5.9.3" "5.10.3" "5.11.0" )
jackson_versions=( "2.10.5" "2.11.4" "2.12.5" "2.13.5" "2.14.3" "2.15.4" "2.16.2" "2.17.2" )
gson_versions=( "2.8.9" "2.9.1" "2.10.1" "2.11.0" )
snakeyaml_versions=( "1.33" "2.2" )
spring_versions=( "2.3.12.RELEASE" "2.4.13" "2.5.15" "2.6.15" "2.7.18" )
spring_boot_3_versions=( "3.0.13" "3.1.12" "3.2.8" "3.3.2" )

result=0

for junit_version in $junit_versions; do
  echo "Tests with JUnit ${junit_version}:"

  printf "\tRun tests with JUnit %s...\n" "${junit_version}"
  ./gradlew test -DINTEGRATION_TESTS=true -DJUNIT_VERSION="${junit_version}" &> /dev/null
  res=$?
  result=$(( $result + $res ))
  print_result "$junit_version" $res
done

for jackson_version in $jackson_versions; do
  echo "Tests with Jackson ${jackson_version}:"

  printf "\tRun tests with Jackson %s...\n" "${jackson_version}"
  ./gradlew test -DINTEGRATION_TESTS=true -DJACKSON_VERSION="${jackson_version}" &> /dev/null
  res=$?
  result=$(( $result + $res ))
  print_result "$jackson_version" $res
done

for gson_version in $gson_versions; do
  echo "Tests with GSON ${gson_version}:"

  printf "\tRun tests with GSON %s...\n" "${gson_version}"
  ./gradlew test -DINTEGRATION_TESTS=true -DGSON_VERSION="${gson_version}" &> /dev/null
  res=$?
  result=$(( $result + $res ))
  print_result "$gson_version" $res
done

for snakeyaml_version in $snakeyaml_versions; do
  echo "Tests with Snakeyaml ${snakeyaml_version}:"

  printf "\tRun tests with Snakeyaml %s...\n" "${snakeyaml_version}"
  ./gradlew test -DINTEGRATION_TESTS=true -DSNAKE_YAML_VERSION="${snakeyaml_version}" &> /dev/null
  res=$?
  result=$(( $result + $res ))
  print_result "$snakeyaml_version" $res
done

for spring_version in $spring_versions; do
  echo "Tests with Spring Boot ${spring_version}:"

  printf "\tRun tests with Spring Boot %s...\n" "${spring_version}"
  ./gradlew test -DINTEGRATION_TESTS=true -DSPRING_VERSION="${spring_version}" &> /dev/null
  res=$?
  result=$(( $result + $res ))
  print_result "$spring_version" $res
done

java_version=`java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}'`

if [[ "$java_version" > "17" ]]; then
  for spring_version in $spring_boot_3_versions; do
    echo "Tests with Spring Boot ${spring_version}:"

    printf "\tRun tests with Spring Boot %s...\n" "${spring_version}"
    ./gradlew test -DINTEGRATION_TESTS=true -DSPRING_VERSION="${spring_version}" &> /dev/null
    res=$?
    result=$(( $result + $res ))
    print_result "$spring_version" $res
  done
fi

exit $result
