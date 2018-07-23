package org.xinghuo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xinghuo.ddns.service.DdnsService;
import org.xinghuo.ddns.service.IpService;
import org.xinghuo.ddns.service.impl.DdnsServiceImpl;
import org.xinghuo.ddns.service.impl.IpServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    private static Logger logger;
    private static IpService ipService = new IpServiceImpl();
    private static DdnsService ddnsService = new DdnsServiceImpl(ClientFactory.get());
    private static String ddnsId;
    private static ScheduledExecutorService executors = new ScheduledThreadPoolExecutor(1, new DdnsThreadFactory("xinghuo"));

    static {
        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%d " + PatternLayout.TTCC_CONVERSION_PATTERN));
        BasicConfigurator.configure(consoleAppender);
        logger = LoggerFactory.getLogger(App.class);
    }


    public static void main(String[] args) throws Exception {

        final String domain = PropertiesUtils.get("domainName");
        final String ddnsName = PropertiesUtils.get("ddnsName");
        final String ddnsDomain = ddnsName + "." + domain;


        executors.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String currentIp = ipService.get();

                    HashMap<String, String> ddnsInfo = ddnsService.getDdnsInfo(domain, ddnsName);
                    ddnsId = ddnsInfo.get("id");
                    String ip = ddnsInfo.get("ip");
                    String parseIp = ipService.hostToIp(ddnsDomain);
                    logger.info("dns  ip {} change ip {} parseIp {}", ip, currentIp, parseIp);

                    if (ip.equals(currentIp)) {
                        logger.info("ip don't change " + ip + " " + currentIp);
                    } else {
                        if (StringUtils.isEmpty(currentIp)) {
                            logger.error("ip empty!");
                            return;
                        }

                        if (ddnsId == null) {
                            ddnsId = ddnsService.createDdns(domain, ddnsName, currentIp);
                        } else {
                            ddnsService.updateDdns(ddnsId, domain, ddnsName, currentIp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}