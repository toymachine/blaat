FROM ubuntu:latest
MAINTAINER henkpunt@gmail.com
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get -y install nginx
run rm /etc/nginx/sites-enabled/default
add blaat.conf /etc/nginx/sites-enabled/
add server.crt /etc/nginx/ssl/
add server.key /etc/nginx/ssl/
RUN echo "daemon off;" >> /etc/nginx/nginx.conf
expose 443
cmd ["/usr/sbin/nginx"]
