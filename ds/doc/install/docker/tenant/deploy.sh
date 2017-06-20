#!/bin/bash

source config.sh

CONTAINER=`docker run -it -d -p $TOMCAT_PORT:$TOMCAT_PORT -p $PROXY_PORT:$PROXY_PORT sunfish:tenant /bin/sh`

SUNFISH_CONF=/usr/local/tomcat/conf/sunfish/

PROXY_HOME=/usr/local/tomcat/


echo "                            *********************************"
echo "                            Welcome to the SUNFISH Installer!"
echo "                            *********************************"
echo
echo "-----------------------------------------------------------------------------------------"
echo "Container $CONTAINER up and running"
echo "-----------------------------------------------------------------------------------------"
echo



function createPEPConf {
    echo "pdp = $PEP_URL_PDP" > pep.config
    echo "pips = $PEP_URLS_PIPS" >> pep.config
    echo "zone = $PEP_ZONE" >> pep.config
    
    docker cp pep.config $CONTAINER:$SUNFISH_CONF
    echo "PEP Config"
}


function copyPIPAttributes {

    docker cp attributes $CONTAINER:$SUNFISH_CONF/pip

    echo "PIP Attributes"
}

function createPIPDatabase {

    for entry in "${!PIP_DATABASE[@]}"; do echo "$entry = ${PIP_DATABASE[$entry]}" >> pip_database.config; done
    
    docker cp pip_database.config $CONTAINER:$SUNFISH_CONF/pip/database

}

function createProxyConf {

    echo "proxy.ip = 127.0.0.1" > proxy.config
    echo "proxy.port = $PROXY_PORT" >> proxy.config
    echo "pep.url = 127.0.0.1:$TOMCAT_PORT" >> proxy.config
    docker cp proxy.config $CONTAINER:$PROXY_HOME
    echo "PROXY Config done"
}


function copyProxy {

    docker cp pep-proxy.jar $CONTAINER:$PROXY_HOME
    echo "Proxy setup"

}


createPEPConf
copyPIPAttributes
createPIPDatabase
createProxyConf
copyProxy
rm proxy.config pep.config pip_database.config

docker exec -it -d $CONTAINER /usr/bin/java -jar pep-proxy.jar

docker exec -it $CONTAINER /usr/local/tomcat/bin/catalina.sh run

docker kill $CONTAINER
