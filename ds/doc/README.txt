SUNFISH - DS
============

INSTALLATION
------------

Manual Process
--------------
1.) Install openaz from DataSecurity directory via Maven
2.) Import SUNFISH project from DS directory via Maven
3.) Build and Package Project with Maven
4.) Copy generated:
      - pap.war 
      - pdp.war
      - pep.war
      - pip.war
      - prp.war
      - demo-app.war
      - ri.war
    to Tomcat "webapps" directory (commons.war)


Precompiled Resources
---------------------
1.) Copy precompiled * .war files to Tomcat "webapps" directory



CONFIGURATION
---------------------------------------
1.) Copy "sunfish" directory to your Tomcat "conf" directory
2.) Make sure Tomcat is setup to listen on Port 8080 (if not - adapt config and database files in "sunfish" directory)



HOW TO
-------
1.) Start Tomcat
2.) A sample Postman collection with a suitable PEP request is provided (sunfish-api.json)


*****OPTIONAL******

TRANSPARENT SUNFISH Proxy
-------------------------

Manual Process
--------------
1.) Import SUNFISH project from DS directory via Maven
2.) Build and Package Project with Maven
3.) Copy generated pep-proxy-jar-with-dependencies.jar to any suitable directory
4.) Copy proxy.config to previous directory
5.) Adapt proxy.config if necesssary 


HOW TO
---------------------
1.) Run 'java -jar pep-proxy.jar' in proxy directory
2.) Open Web-Browser and navigate to: http://localhost:10000/demo-app/demo/index.html


