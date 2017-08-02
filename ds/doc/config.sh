#!/usr/local/bin/bash
#!/bin/bash


# Tomcat Home
CATALINA_HOME=~/Desktop/tomcat


# PEP Config
#**********************************************
PEP_URL_PDP="http://localhost:8080/pdp/"
# All possible PIPs (seperated by comma)
PEP_URLS_PIPS="http://localhost:8080/pip/"
PEP_ZONE="demozone"


# PAP Config
#**********************************************
PAP_URL_RI="http://localhost:8080/ri/mocked"


# PDP Config
#**********************************************
# All possible PRPs (seperated by comma)
PDP_URLS_PRPS="http://localhost:8080/prp/v1"
# All possible PIPs (seperated by comma)
PDP_URLS_PIPS="http://localhost:8080/pip/v1"


# PRP Config
#**********************************************
PRP_MAX_THREADS=8
PRP_MAX_POLICY_COUNT=1000
PRP_URL_RI="http://localhost:8080/ri/mocked"



# RI Config
#**********************************************
RI_ROOT_POLICY=~/Desktop/tomcat/conf/sunfish/ri
RI_MAX_THREADS=8
RI_MAX_POLICY_COUNT=1000


# PIP Database
#**********************************************
declare -A PIP_DATABASE
PIP_DATABASE["host.localhost.10000"]="http://localhost:8080/"
PIP_DATABASE["zone.localhost.10000"]="demozone"
PIP_DATABASE["pep.demozone"]="http://localhost:8080/pep/v1"

