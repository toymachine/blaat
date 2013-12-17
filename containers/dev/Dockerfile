# Dockerfile to create a machine with
# - basic linux functions (curl,wget,python, etc)
# - Lastest JDK installed

FROM ubuntu:latest

# Update the APT cache
RUN sed -i.bak 's/main$/main universe/' /etc/apt/sources.list
RUN apt-get update
RUN apt-get upgrade -y

# Install and setup project dependencies
RUN apt-get install -y curl wget
RUN locale-gen en_US en_US.UTF-8

#prepare for Java download
RUN apt-get install -y python-software-properties
RUN apt-get install -y software-properties-common

#grab oracle java (auto accept licence)
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java7-installer

#install maven
RUN apt-get install -y maven

#download and install datomic
ADD datomic-pro-0.9.4331.tgz /opt
RUN ln -s /opt/datomic-pro-0.9.4331 /opt/datomic

#create a user blaat
RUN useradd -d /home/blaat -s /bin/bash -m blaat
ENV HOME /home/blaat

#add leiningen
ADD lein /bin/
RUN chmod a+x /bin/lein
#run lein once as blaat to make sure it is installed
#also deps once to make startup of repl faster, e.g. load all deps already in container build
#note that currently docker does not allow symlinks so i copied project file, TODO fix
USER blaat
ADD project.clj /tmp/
#make sure datomic deps are in local maven
RUN cd /opt/datomic; ./bin/maven-install
RUN cd /tmp; lein deps

WORKDIR /var/www/blaat
EXPOSE 12345
CMD lein repl :headless :host 0.0.0.0 :port 12345

