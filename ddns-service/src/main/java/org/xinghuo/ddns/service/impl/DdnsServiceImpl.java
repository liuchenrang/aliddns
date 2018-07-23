package org.xinghuo.ddns.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinghuo.ddns.service.DdnsService;

import java.util.HashMap;
import java.util.List;

/**
 * @author xinghuo
 */
public class DdnsServiceImpl implements DdnsService {
    private static IAcsClient client = null;
    private static Logger logger;

    public DdnsServiceImpl(IAcsClient cs){
        client = cs;
    }

    static {
        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%d " + PatternLayout.TTCC_CONVERSION_PATTERN));
        BasicConfigurator.configure(consoleAppender);
        logger = LoggerFactory.getLogger(DdnsService.class);
    }

    /**
     * 创建ddns 记录
     *
     * @param domain
     * @param ddnsName
     * @param Ip
     * @result String
     */
    @Override
    public String createDdns(String domain, String ddnsName, String Ip) throws Exception {
        AddDomainRecordRequest addDomainRecordRequest = new AddDomainRecordRequest();
        addDomainRecordRequest.setDomainName(domain);
        addDomainRecordRequest.setType("A");
        addDomainRecordRequest.setValue(Ip);
        addDomainRecordRequest.setRR(ddnsName);
        AddDomainRecordResponse addDomainRecordResponse;
        addDomainRecordResponse = client.getAcsResponse(addDomainRecordRequest);
        return addDomainRecordResponse.getRecordId();
    }

    /**
     * 更新ddsn 记录
     *
     * @param ddnsId
     * @param domain
     * @param ddnsName
     * @param Ip
     */
    @Override
    public void updateDdns(String ddnsId, String domain, String ddnsName, String Ip) throws Exception {
        if (true) {
            return;
        }
        UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
        updateDomainRecordRequest.setRecordId(ddnsId);
        updateDomainRecordRequest.setRR(ddnsName);
        updateDomainRecordRequest.setType("A");
        updateDomainRecordRequest.setValue(Ip);
        UpdateDomainRecordResponse updateDomainRecordResponse = client.getAcsResponse(updateDomainRecordRequest);
        System.out.println(updateDomainRecordResponse.getRecordId());
    }

    /**
     * 获得ddns 标示
     *
     * @param domain
     * @param ddnsName
     */
    @Override
    public String getDdnsId(String domain, String ddnsName) throws Exception {
        DescribeDomainRecordsRequest recordsRequest = new DescribeDomainRecordsRequest();
        recordsRequest.setDomainName(domain);
        DescribeDomainRecordsResponse recordsResponse;
        String ddnsId = null;

        recordsResponse = client.getAcsResponse(recordsRequest);
        List<DescribeDomainRecordsResponse.Record> recordList = recordsResponse.getDomainRecords();
        for (DescribeDomainRecordsResponse.Record record : recordList) {

            if (ddnsName.equals(record.getRR())) {
                ddnsId = record.getRecordId();
                break;
            }
        }

        return ddnsId;
    }

    @Override
    public HashMap<String, String> getDdnsInfo(String domain, String ddnsName) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>(2);
        DescribeDomainRecordsRequest recordsRequest = new DescribeDomainRecordsRequest();
        recordsRequest.setDomainName(domain);
        DescribeDomainRecordsResponse recordsResponse;
        String ddnsId = null;

        recordsResponse = client.getAcsResponse(recordsRequest);
        List<DescribeDomainRecordsResponse.Record> recordList = recordsResponse.getDomainRecords();
        for (DescribeDomainRecordsResponse.Record record : recordList) {

            if (ddnsName.equals(record.getRR())) {
                ddnsId = record.getRecordId();
                hashMap.put("id", ddnsId);
                hashMap.put("ip", record.getValue());
                break;
            }
        }

        return hashMap;
    }
}
