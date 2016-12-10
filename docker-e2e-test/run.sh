#!/bin/bash

docker build -t oglimmer/lunchy-e2e .

docker run -d --name=webe2e -p 8080:8080 oglimmer/lunchy-e2e

while ! docker logs webe2e 2>&1 | grep -q "\[INFO\] Started Jetty Server"; do sleep 1; done

docker run --rm --link=webe2e:webe2e -v $PWD/../src/integration/js:/project --env BASEURL=http://webe2e:8080/ mrsheepuk/protractor

docker rm -f webe2e
