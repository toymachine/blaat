# default.pp - puppet provisioning

  $user = 'vagrant'

  # define a variable for the webupd8team ppa sources list
  $webupd8src = '/etc/apt/sources.list.d/webupd8team.list'
 
  # Ensure the sources list exists
  # See http://stackoverflow.com/a/10463734/428876 for sharing files and configuring a puppet fileserver
  file { $webupd8src:
    content => "deb http://ppa.launchpad.net/webupd8team/java/ubuntu lucid main\ndeb-src http://ppa.launchpad.net/webupd8team/java/ubuntu lucid main\n",
  } ->
  # Authorise the webupd8 ppa
  # At the time of writing this key was correct, but check the PPA page on launchpad!
  # https://launchpad.net/~webupd8team/+archive/java
  exec { 'add-webupd8-key':
    command => 'apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886',
    path => '/usr/bin/:/bin',
  } ->
  # update the apt keystore
  exec { 'apt-key-update':
    command => 'apt-key update',
    path => '/usr/bin/:/bin',
  } ->
  # update apt sources 
  exec { 'apt-update':
    command => 'apt-get update',
    path => '/usr/bin/:/bin',
  } ->
  # set license acceptance with debconf
  # thanks to Gert van Dijk on http://askubuntu.com/a/190674
  exec { 'accept-java-license':
    command => '/bin/echo /usr/bin/debconf shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections;/bin/echo /usr/bin/debconf shared/accepted-oracle-license-v1-1 seen true | sudo /usr/bin/debconf-set-selections;',
  } ->
  # finally install the package
  # oracle-java6-installer and oracle-java8-installer also available from the ppa
  package { 'oracle-java7-installer':
    ensure => present,
  }


  file { "leiningen/create-local-bin-folder":
    ensure => directory,
    path => "/home/$user/bin",
    owner => $user,
    group => $user,
    mode => '755',
  }

  $leiningen_url = "https://raw.github.com/technomancy/leiningen/preview/bin/lein"

  exec { "leiningen/install-script":
    user => $user,
    group => $user,
    path => ["/bin", "/usr/bin", "/usr/local/bin"],
    cwd => "/home/$user/bin",
    command => "wget ${leiningen_url} && chmod 755 lein",
    creates => ["/home/$user/.bin/lein",
                "/home/$user/.lein"],
    require => [#Class["java::install"],
                File["leiningen/create-local-bin-folder"],
                #Package["leiningen/install-wget"]
                ],
  }


 file { "/etc/profile.d/path.sh":
  content => "export PATH=\$PATH:~/bin
  "
 }


