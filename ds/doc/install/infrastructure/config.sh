#!/usr/local/bin/bash
#!/bin/bash


# Tomcat Home
CATALINA_HOME=~/Desktop/tomcat-infrastructure



# PAP Config
#**********************************************
PAP_URL_RI="http://localhost:8081/ri/mocked"


# PDP Config
#**********************************************
# All possible PRPs (seperated by comma)
PDP_URLS_PRPS="http://localhost:8081/prp/v1"
# All possible PIPs (seperated by comma)
PDP_URLS_PIPS="http://localhost:8081/pip/v1"


# PRP Config
#**********************************************
PRP_MAX_THREADS=8
PRP_MAX_POLICY_COUNT=1000
PRP_URL_RI="http://localhost:8081/ri/mocked"



# RI Config
#**********************************************
RI_ROOT_POLICY=~/Desktop/tomcat/conf/sunfish/ri
RI_MAX_THREADS=8
RI_MAX_POLICY_COUNT=1000


# PIP Database
#**********************************************
declare -A PIP_DATABASE
PIP_DATABASE["sample.entry"]="test"

