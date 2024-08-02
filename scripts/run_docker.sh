#!/bin/bash
docker pull acchotsix/acc-hotsix
docker run -d -p 80:8080 acchotsix/acc-hotsix:latest