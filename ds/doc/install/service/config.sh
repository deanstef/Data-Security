#!/bin/bash


# Tomcat Home
CATALINA_HOME=~/Desktop/tomcat-tenant
PROXY_HOME=~/Desktop/pep-proxy


# PEP Config
#**********************************************
PEP_URL_PDP="http://localhost:8081/pdp/"
# All possible PIPs (seperated by comma)
PEP_URLS_PIPS="http://localhost:8080/pip/"
PEP_ZONE="tenant"



# PIP Database
#**********************************************
declare -A PIP_DATABASE
PIP_DATABASE["host.local"]="http://localhost:8080/"
PIP_DATABASE["zone.local"]="tenant"
PIP_DATABASE["pep.tenant"]="http://localhost:8080/pep/v1"


# PEP Proxy
#**********************************************
PROXY_IP="127.0.0.1"
PROXY_PORT=10000
declare -A PROXY_PEP
PROXY_PEP["local"]="http://localhost:8080/pep/v1/"
