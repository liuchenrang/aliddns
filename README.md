cd ddns-command
修改resources
ddns.properties 
替换自己的appkey 和 id 和domain

mvn clean compile assembly:single

运行  java -jar ddns-jar-with-dependencies.jar


