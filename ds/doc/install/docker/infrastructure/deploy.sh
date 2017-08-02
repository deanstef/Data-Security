#!/bin/bash

source config.sh

CONTAINER=`docker run -it -d -p $TOMCAT_PORT:$TOMCAT_PORT sunfish:infrastructure /bin/sh`

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

function createPAPConf {


    echo "ri = $PAP_URL_RI" > pap.config
    docker cp pap.config $CONTAINER:$SUNFISH_CONF
    echo "PAP Config"
}


function createPDPConf {

    echo "prps = $PDP_URLS_PRPS" > pdp.config
    echo "pips = $PDP_URLS_PIPS" >> pdp.config
    docker cp pdp.config $CONTAINER:$SUNFISH_CONF
    echo "PDP Config"
}

function createPRPConf {
    

    echo "maxThreads = $PRP_MAX_THREADS" > prp.config
    echo "maxPolicyCount = $PRP_MAX_POLICY_COUNT" >> prp.config
    echo "ri = $PRP_URL_RI" >> prp.config
    docker cp prp.config $CONTAINER:$SUNFISH_CONF

    echo "PRP Config"
}

function copyPIPAttributes {

    docker cp attributes $CONTAINER:$SUNFISH_CONF/pip

    echo "PIP Attributes"
}

function createPIPDatabase {

    for entry in "${!PIP_DATABASE[@]}"; do echo "$entry = ${PIP_DATABASE[$entry]}" >> pip_database.config; done
    
    docker cp pip_database.config $CONTAINER:$SUNFISH_CONF/pip/database

}

createPAPConf
createPDPConf
createPRPConf
copyPIPAttributes
createPIPDatabase
rm pap.config pdp.config pip_database.config prp.config

docker exec -it $CONTAINER /usr/local/tomcat/bin/catalina.sh run

docker kill $CONTAINER
