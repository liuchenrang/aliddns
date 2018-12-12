#打包配置
    cd ddns-command
    修改resources
    ddns.properties 
    替换自己的appkey 和 id 和domain
    sh ./pub.sh
    
# 运行
    java -jar dist/ScanDomain-jar-with-dependencies.jar
        扫描配置信息
    java -jar DomainQuicklyBind-jar-with-dependencies.jar
        快速绑定扫描结果
    java -jar MoveMachine-jar-with-dependencies.jar 
        机器迁移

# 域名扫描动态绑定
     java -jar dist/ScanDomain-jar-with-dependencies.jar
     
# 首先修改配置
    - allip.txt
    - willRemoveMachineIp.txt
    - newMachineIp.txt
    
# 功能点

    - 历史域名扫描
        - 识别已绑定ip的域名 usedDomainIp.txt
        - 未绑定域名的ip   usedIp.txt
    - 快速绑定域名ip
        - 根据未使用的域名 unUsedDomain.txt与未使用的ip unUsedIp.txt进行绑定
    - 机器ip迁移
        - 新机器newMachineIp.txt ， 老机器ip removeMachineIp.txt， 新的对应关系 moveMachineResultDomainIp.txt



