# default.pp - puppet provisioning

  $user = 'vagrant'

class { 'java':
  distribution => 'jdk',
  version      => 'latest',
}

  file { "leiningen/create-local-bin-folder":
    ensure => directory,
    path => "/home/$user/bin",
    owner => $user,
    group => $user,
    mode => '755',
  }

  $leiningen_url = "https://raw.github.com/technomancy/leiningen/preview/bin/lein"

  exec { "leiningen/install":
    user => $user,
    group => $user,
    path => ["/bin", "/usr/bin", "/usr/local/bin"],
    cwd => "/home/$user/bin",
    command => "wget ${leiningen_url} && chmod 755 lein",
    creates => ["/home/$user/.bin/lein",
                "/home/$user/.lein"],
    require => [Class["java"],
                File["leiningen/create-local-bin-folder"],
    ],
  }
 

 file { "/etc/profile.d/path.sh":
  content => "export PATH=\$PATH:~/bin"
 }

file { "/etc/init/datomic.conf":
    source => "/vagrant/puppet/modules/datomic/datomic.conf"
}


 group { "datomic/group":
  name => "datomic",
  ensure => "present",
 }

 user { "datomic/user":
  name => "datomic",
  ensure => "present",
  home => "/var/lib/datomic",
  gid => "datomic",
  require => [File["/var/lib/datomic"], Group["datomic/group"]]
 }

 file { ["/var", "/var/lib", "/var/lib/datomic"]:
   ensure => "directory",
 }

 file { "/var/lib/datomic/runtime":
   ensure => "directory",
 }

 file { "/var/lib/datomic/data":
   ensure => "directory",
   owner => "datomic",   
   group => "datomic",
   require => [File["/var/lib/datomic"], User["datomic/user"], Group["datomic/group"]],
 }

 file { "/var/log/datomic":
   ensure => "directory",
   owner => "datomic",   
   group => "datomic",
   require => [User["datomic/user"], Group["datomic/group"]],
 }

 exec { "datomic/install":
    command => "wget -qO- https://dl.dropboxusercontent.com/u/1201552/datomic-pro-0.9.4331.tgz | tar -zx -C /var/lib/datomic/runtime --strip-components=1",
    cwd => "/",
    path => ["/bin", "/usr/bin", "/usr/local/bin"],
    creates  =>  "/var/lib/datomic/runtime/transactor-pom.xml",
    require => [Class["java"], File["/var/lib/datomic/runtime"], File["/var/lib/datomic/data"], File["/var/log/datomic"], File["/etc/init/datomic.conf"], User["datomic/user"]]
 }

service { "datomic/service":
  name => "datomic", 
  ensure => "running",
  require => Exec["datomic/install"]
}

 exec { "datomic/create-db":
  cwd => "/vagrant",
    user => $user,
    group => $user,
    path => ["/home/$user/bin", "/bin", "/usr/bin", "/usr/local/bin"],
    environment => [ "HOME=/home/vagrant" ],
  command => "lein run -m blaat.db/create-db",
  require => [Exec['leiningen/install'], Service['datomic/service']]
}


package { 'maven':
      ensure => present,
}

 exec { "datomic/maven-install":
  cwd => "/var/lib/datomic/runtime",
    user => $user,
    group => $user,
    path => ["/var/lib/datomic/runtime/bin", "/home/$user/bin", "/bin", "/usr/bin", "/usr/local/bin"],
    environment => [ "HOME=/home/vagrant" ],
  command => "maven-install > /tmp/maven.txt 2>&1",
  require => [Exec['datomic/install'], Package['maven']],
   logoutput => true,
}

 exec { "leiningen/init-deps":
  cwd => "/vagrant",
    user => $user,
    group => $user,
    path => ["/home/$user/bin", "/bin", "/usr/bin", "/usr/local/bin"],
    environment => [ "HOME=/home/vagrant" ],
  command => "lein deps",
  require => [Exec['leiningen/install'], Exec['datomic/maven-install'], File["/etc/profile.d/path.sh"]],
}

 class { 'nginx': }

class { 'memcached':
      max_memory => 128,
      listen_ip => '127.0.0.1',
}


