cd ddns-command
修改resources
ddns.properties 
替换自己的appkey 和 id 和domain

mvn clean compile assembly:single

运行  java -jar ddns-jar-with-dependencies.jar


域名扫描动态绑定
     java -jar dist/ScanDomain-jar-with-dependencies.jar

功能点
    历史域名扫描
        识别已绑定ip的域名 usedDomainIp.txt
        未绑定域名的ip   usedIp.txt
    快速绑定域名ip
        根据未使用的域名 unUsedDomain.txt与未使用的ip unUsedIp.txt进行绑定
    机器ip迁移
        新机器newMachineIp.txt ， 老机器ip removeMachineIp.txt， 新的对应关系 moveMachineResultDomainIp.txt
首先建立 allip.txt文件
removeMachineIp.txt 要迁移机器的IP 

