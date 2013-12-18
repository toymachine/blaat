
datomic-pro-0.9.4331.tgz:
	wget https://dl.dropboxusercontent.com/u/1201552/datomic-pro-0.9.4331.tgz

datomic: datomic-pro-0.9.4331.tgz

dev-container: datomic
	cp datomic-pro*.tgz dev
	cp ../project.clj dev
	cd dev; docker build -t blaat/dev .

transactor-container: datomic
	cp datomic-pro*.tgz transactor
	cd transactor; docker build -t blaat/transactor .

nginx-container:
	cd nginx; docker build -t blaat/nginx .

memcached-container:
	cd memcached; docker build -t blaat/memcached .


all-dev: dev-container transactor-container nginx-container memcached-container