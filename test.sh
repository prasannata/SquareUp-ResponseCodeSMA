#!/bin/bash

if [ $# -ne 2 ]; then
  echo "Usage: ./tesh.sh <hostname> <port>"
  exit 1
fi

hostname=$1
port=$2
url="http://$hostname:$port/locations"
errorUrl="http://$hostname:$port/error"

echo
echo "Sending 60 successful (200) requests ..."
echo "----------------------------------------"
for (( i=1; i<=60; i++))
do
  echo -n " $i."
  curl -sL -w "%{http_code};" -X PUT -o/dev/null http://localhost:9080/locations/$i
done

echo
echo
echo "Sending 60 successful (200) requests, 30 client error (400) requests and 30 server error (500) requests..."
echo "----------------------------------------------------------------------------------------------------------"
for (( i=1; i<=60; i++))
do
  echo -n " $i."
  modulo=$(expr $i % 2)
  if [ $modulo -eq 0 ]; then
    curl -sL -w "%{http_code};" -X GET -o/dev/null $url/$i
    curl -sL -w "%{http_code};" -X DELETE -o/dev/null $url/$i
  else
    curl -sL -w "%{http_code};" -X PUT -o/dev/null $url/$i
    curl -sL -w "%{http_code};" -X GET -o/dev/null $errorUrl
  fi
done
echo
