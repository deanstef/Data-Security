#!/bin/bash

source config.sh


SUNFISH_CONF=$CATALINA_HOME/conf/sunfish

mkdir -p $SUNFISH_CONF


echo "*********************************"
echo "Welcome to the SUNFISH Installer!"
echo "*********************************"
echo


function copyConf {
    echo 
    echo "*****************"
    echo "Copying config..."
    echo "*****************"
    echo

    cp -rf ./tomcat/conf/sunfish $CATALINA_HOME/conf

    echo "Config copied to: $CATALINA_HOME/conf"
}


function createPEPConf {

    pepConfig=$SUNFISH_CONF/pep.config

    echo "pdp = $PEP_URL_PDP" > $pepConfig
    echo "pips = $PEP_URLS_PIPS" >> $pepConfig
    echo "zone = $PEP_ZONE" >> $pepConfig
    echo "dm.service.url = $PEP_URL_DM" >> $pepConfig
    echo "anon.service.url = $PEP_URL_ANON" >> $pepConfig

    echo "PEP Config: $pepConfig"
}

function createPAPConf {

    papConfig=$SUNFISH_CONF/pap.config

    echo "ri = $PAP_URL_RI" > $papConfig

    echo "PAP Config: $papConfig"
}


function createPDPConf {

    pdpConfig=$SUNFISH_CONF/pdp.config

    echo "prps = $PDP_URLS_PRPS" > $pdpConfig
    echo "pips = $PDP_URLS_PIPS" >> $pdpConfig

    echo "PDP Config: $pdpConfig"
}

function createPRPConf {
    
    prpConfig=$SUNFISH_CONF/prp.config

    echo "maxThreads = $PRP_MAX_THREADS" > $prpConfig
    echo "maxPolicyCount = $PRP_MAX_POLICY_COUNT"           >> $prpConfig
    echo "ri = $PRP_URL_RI" >> $prpConfig


    echo "PRP Config: $prpConfig"
}

function createRIConf {

    riConfig=$SUNFISH_CONF/ri.config

    echo "rootPolicy.dir = $RI_ROOT_POLICY" > $riConfig
    echo "maxThreads = $RI_MAX_THREADS" >> $riConfig
    echo "maxPolicyCount = $RI_MAX_POLICY_COUNT" >> $riConfig

    echo "PRP Config: $riConfig"

}

function copyPIPAttributes {

    pipAttributes=$SUNFISH_CONF/pip/attributes

    mkdir -p $pipAttributes

    cp -rf ./tomcat/conf/sunfish/pip/attributes $pipAttributes

    echo "PIP Attributes: $pipAttributes"
}

function createPIPDatabase {

    pipDatabaseDir=$SUNFISH_CONF/pip/database

    mkdir -p $pipDatabaseDir

    pipDatabase=$pipDatabaseDir/pip_database.config

    for entry in "${!PIP_DATABASE[@]}"; do echo "$entry = ${PIP_DATABASE[$entry]}" >> $pipDatabase; done

}


createPEPConf
createPAPConf
createPDPConf
createPRPConf
createRIConf
copyPIPAttributes
createPIPDatabase









