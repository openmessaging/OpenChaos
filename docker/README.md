# Dockerized OpenMessaging-Chaos

Docker mode is transplanted from Jepsen.

This docker image attempts to simplify the setup required by openmessaging-chaos.
It is intended to be used by a CI tool or anyone with docker who wants to try chaos themselves.

It contains all the chaos dependencies and code. It uses [Docker Compose](https://github.com/docker/compose) to spin up the five
containers used by chaos.

To start run

```
./up.sh
docker exec -it chaos-control bash
```
