FROM ubuntu:latest
MAINTAINER henkpunt@gmail.com
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get -y install memcached
EXPOSE 11211
ENTRYPOINT ["/usr/bin/memcached", "-u", "memcache", "-m", "128"]