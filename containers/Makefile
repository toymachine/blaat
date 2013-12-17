
datomic-pro-0.9.4331.tgz:
	wget https://dl.dropboxusercontent.com/u/1201552/datomic-pro-0.9.4331.tgz

datomic: datomic-pro-0.9.4331.tgz

dev: datomic
	cp datomic-pro*.tgz dev
	cp ../project.clj dev
	cd dev; docker build -t blaat/dev .

