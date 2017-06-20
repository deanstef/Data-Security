#!/bin/bash

TOMCAT_PORT=8080

# PEP Config
#**********************************************
PEP_URL_PDP="http://localhost:8081/pdp/"
# All possible PIPs (seperated by comma)
PEP_URLS_PIPS="http://localhost:8080/pip/"
PEP_ZONE="tenant"



# PIP Database
#**********************************************
declare -A PIP_DATABASE
PIP_DATABASE["host.localhost.10000"]="http://localhost:8080/"
PIP_DATABASE["zone.localhost.10000"]="tenant"
PIP_DATABASE["pep.demozone"]="http://localhost:8080/pep/v1"


# PEP Proxy
#**********************************************
PROXY_PORT=10000
PROXY_PEP="http://localhost:8080/pep/v1/"

