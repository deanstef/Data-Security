#/bin/sh
nohup java -jar pep-proxy.jar > /dev/null 2>&1 & echo $! > run.pid
