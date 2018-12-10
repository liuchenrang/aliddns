package org.xinghuo;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinghuo.ddns.service.DdnsService;
import org.xinghuo.ddns.service.impl.DdnsServiceImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class ListNoDns {
    protected static Set<String> allip = new HashSet<>();
    protected static IAcsClient client = null;
    protected static Set<String> unUsedDomain = new HashSet<>();
    protected static Set<String> unUsedIp = new HashSet<>();
    protected static Set<String> usedIp = new HashSet<>();
    private static Logger logger;

    static {
        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%d " + PatternLayout.TTCC_CONVERSION_PATTERN));
        BasicConfigurator.configure(consoleAppender);
        logger = LoggerFactory.getLogger(DdnsService.class);
    }

    protected List<String> blackList = Arrays.asList("savingapp.cn lanlansoft.net cvedci.cn".split(" "));

    public ListNoDns(IAcsClient cs) {
        client = cs;
    }

    public static void main(String[] args) {
        new ListNoDns(ClientFactory.get()).run();
//        new ListNoDns(ClientFactory.get()).sendBind();
    }
    public void cleanFile(){
        new File("usedIp.txt").delete();
        new File("unUsedIp.txt").delete();
        new File("usedDomain.txt").delete();
        new File("unUsedDomain.txt").delete();
        new File("usedDomainIp.txt").delete();
    }
    public void run() {


        try {
//            cleanFile();
            readAllIp();
            FileWriter writerUsedIp = new FileWriter("usedIp.txt");
            FileWriter writerUnUsedIp = new FileWriter("unUsedIp.txt");
            FileWriter writerUsedDomain = new FileWriter("usedDomain.txt");
            FileWriter writerUnUsedDomain = new FileWriter("unUsedDomain.txt");
            FileWriter writerUsedDomainIp = new FileWriter("usedDomainIp.txt");
            Long page = 1L;
            List<DescribeDomainsResponse.Domain> domains;
            LinkedList<HashMap<String, List<DescribeDomainRecordsResponse.Record>>> linkedList = new LinkedList<>();
            do {
                System.out.println("page " + page);
                DescribeDomainsRequest describeDomainsRequest = new DescribeDomainsRequest();
                describeDomainsRequest.setPageNumber(page);
                DescribeDomainsResponse response = client.getAcsResponse(describeDomainsRequest);
                domains = response.getDomains();
                for (DescribeDomainsResponse.Domain domain : domains) {
                    if (!blackList.contains(domain)) {
                        getDdnsInfo(domain.getDomainName(), linkedList);
                    }
                }
                for (HashMap<String, List<DescribeDomainRecordsResponse.Record>> item : linkedList) {
                    for (String key : item.keySet()) {
                        List<DescribeDomainRecordsResponse.Record> records = item.get(key);
                        if (records.size() > 0) {
                            for (DescribeDomainRecordsResponse.Record record : records) {
                                if (record.getType().equals("A")) {
                                    System.out.println("usedDomain " + key + " value " + record.getValue() + " type " + record.getType());
                                    writerUsedIp.write(record.getValue() + "\r\n");
                                    writerUsedDomainIp.write(key + " " + record.getValue() + "\r\n");
                                    usedIp.add(record.getValue());
                                }
                            }
                            writerUsedDomain.write(key + "\r\n");

                        } else {
                            writerUnUsedDomain.write(key + "\r\n");
                            unUsedDomain.add(key);
                            System.out.println("unUsedDomain " + key);
                        }
                    }
                }
                page++;

            } while (domains.size() > 0);
            writerUsedDomain.close();
            writerUnUsedDomain.close();
            writerUsedIp.close();

            //unUsedIp
            unUsedIp.addAll(allip);
            boolean x = unUsedIp.removeAll(usedIp);
            for (String unUsed : unUsedIp) {
                writerUnUsedIp.write(unUsed + "\r\n");
            }
            writerUnUsedIp.close();
            writerUsedDomainIp.close();

            System.out.println("end " + x);
            System.out.println("usedIp " + usedIp);
            System.out.println("unUsedIp " + unUsedIp);
            System.out.println("unUsedDomain " + unUsedDomain);
//            bindDomainIp();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAllIp() {
        readSet("allip.txt", allip);
    }

    public void getDdnsInfo(String domain, LinkedList<HashMap<String, List<DescribeDomainRecordsResponse.Record>>> linkedList) throws Exception {
        DescribeDomainRecordsRequest recordsRequest = new DescribeDomainRecordsRequest();
        HashMap<String, List<DescribeDomainRecordsResponse.Record>> ll = new HashMap<>();
        recordsRequest.setDomainName(domain);
        DescribeDomainRecordsResponse recordsResponse;
        String ddnsId = null;
        recordsResponse = client.getAcsResponse(recordsRequest);
        List<DescribeDomainRecordsResponse.Record> recordList = recordsResponse.getDomainRecords();
        if (recordList.size() <= 1) {
            System.out.println("domain " + domain + " count " + recordList.size());
            ll.put(domain, recordList);
            linkedList.add(ll);
        }
    }

    public void bindDomainIp() {
        try {
            FileWriter writerBind = new FileWriter("bindDomainIp.txt", true);

            LinkedList<String> linkedList = setToListQueue(unUsedIp);
            DdnsService ddnsService = new DdnsServiceImpl(client);
            for (String domain : unUsedDomain) {
                String ip = linkedList.pop();

                try {
                    System.out.println("domain " + domain + " ip " + ip );

                    String recordId = "";
                    recordId = ddnsService.createDdns(domain, "*", ip);
                    if (!recordId.isEmpty()){
                        writerBind.write("domain " + domain + " ip " + ip + " recordId " + recordId);
                        System.out.println("domain " + domain + " ip " + ip + " success " + recordId);
                        sendBind(domain);
                    }
                    unUsedIp.remove(ip);
                    unUsedDomain.remove(domain);
                    break;
                } catch (NoSuchElementException e) {
                    System.out.println("ip不足，无法继续分配");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendBind(String domain){
        try {

            URL url = new URL(PropertiesUtils.get("apiAddUrl") + domain);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream())
            );
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ( (line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void readSet(String file, Set<String> sets) {
        try {
            FileReader readerUnUsed = new FileReader(file);
            BufferedReader br = new BufferedReader(readerUnUsed);
            String line = null;
            while ((line = br.readLine()) != null) {
                sets.add(line.trim());
            }
            br.close();
            System.out.println("read " + file + " count " + sets.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SetWrite(String file,Set<String> sets){
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (String item : sets) {
                fileWriter.write(item + "\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public LinkedList<String> setToListQueue(Set<String> sets) {
        return new LinkedList<>(sets);
    }
}
