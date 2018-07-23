package org.xinghuo.ddns.service;

import java.io.IOException;

public interface IpService {
    /**
     * 返回外网ip
     * @return
     */
    String get();

    /**
     * host to ip
     * @param host
     * @return
     */
    String hostToIp(String host)  throws IOException;
}
