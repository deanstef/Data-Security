#!/bin/bash

source config.sh


SUNFISH_CONF=$CATALINA_HOME/conf/sunfish

mkdir -p $SUNFISH_CONF
mkdir -p $PROXY_HOME


echo "*********************************"
echo "Welcome to the SUNFISH Installer!"
echo "*********************************"
echo



function createPEPConf {

    pepConfig=$SUNFISH_CONF/pep.config

    echo "pdp = $PEP_URL_PDP" > $pepConfig
    echo "pips = $PEP_URLS_PIPS" >> $pepConfig
    echo "zone = $PEP_ZONE" >> $pepConfig

    echo "PEP Config: $pepConfig"
}


function copyPIPAttributes {

    pipAttributes=$SUNFISH_CONF/pip/attributes

    mkdir -p $pipAttributes

    cp -rf ./tomcat/conf/sunfish/pip/attributes/ $pipAttributes

    echo "PIP Attributes: $pipAttributes"
}

function createPIPDatabase {

    pipDatabaseDir=$SUNFISH_CONF/pip/database

    mkdir -p $pipDatabaseDir

    pipDatabase=$pipDatabaseDir/pip_database.config

    for entry in "${!PIP_DATABASE[@]}"; do echo "$entry = ${PIP_DATABASE[$entry]}" >> $pipDatabase; done

}

function createProxyConf {

    proxyConfig=$PROXY_HOME/proxy.config

    echo "proxy.ip = $PROXY_IP" > $proxyConfig
    echo "proxy.port = $PROXY_PORT" >> $proxyConfig
    for entry in "${!PROXY_PEP[@]}"; do echo "pep.$entry = ${PROXY_PEP[$entry]}" >> $proxyConfig; done
    echo "PROXY Config: $proxyConfig"
}

function copyArtifacts {

    mkdir -p $CATALINA_HOME/webapps

    cp -rf ./tomcat/webapps/ $CATALINA_HOME/webapps

    echo "Artifacts: $CATALINA_HOME/webapps"

}

function copyProxy {

    cp ./proxy/pep-proxy.jar $PROXY_HOME
    cp ./proxy/start.sh $PROXY_HOME
    cp ./proxy/stop.sh $PROXY_HOME


    echo "Proxy: $PROXY_HOME"

}


createPEPConf
copyPIPAttributes
createPIPDatabase
createProxyConf
copyArtifacts
copyProxy



