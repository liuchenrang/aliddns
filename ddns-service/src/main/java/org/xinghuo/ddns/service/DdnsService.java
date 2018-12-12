package org.xinghuo.ddns.service;

import java.util.HashMap;

public interface DdnsService {
    /**
     * 创建ddns 记录
     * @param domain
     * @param ddnsName
     * @param Ip
     * @throws Exception
     */
    public  String  createDdns(String domain,String ddnsName,String Ip) throws Exception;

    /**
     * 更新ddsn 记录
     * @param recordId
     * @param domain
     * @param ddnsName
     * @param Ip
     * @throws Exception
     */
    public  void  updateDdns(String recordId,String domain,String ddnsName,String Ip) throws Exception;

    /**
     * 获得ddns 标示
     * @param domain
     * @param ddnsName
     * @throws Exception
     */
    public String getDdnsId(String domain, String ddnsName) throws Exception;

    /**
     * 获得ddns 标示
     * @param domain
     * @param ddnsName
     * @throws Exception
     */
    public HashMap<String,String> getDdnsInfo(String domain, String ddnsName) throws Exception;
}
