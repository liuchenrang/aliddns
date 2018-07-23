package org.xinghuo.ddns.service.impl;

import org.xinghuo.ddns.service.IpService;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xinghuo
 */
public class IpServiceImpl implements IpService {
    class Mock{
        String getIp(){
            return "127.0.0.1";
        }
    }
    class IpSite{
        Pattern pattern = Pattern.compile("\\d+.?\\d+.?\\d+.?\\d+");
        String getIp(){
            try {
                URL url = new URL("http://ip.010hub.com/");

                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "curl/7.54.0");
                InputStream inputStream = connection.getInputStream();
                int c;
                StringBuffer stringBuffer = new StringBuffer();
                while ((c = inputStream.read()) != -1) {
                    stringBuffer.append((char) c);
                }
                byte[] bytes = stringBuffer.toString().getBytes("iso8859-1");
                String cn = new String(bytes, "UTF-8");
                Matcher matcher = pattern.matcher(cn);
                if (matcher.find()) {
                    return matcher.group(0);
                } else {
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public String get() {
        return new IpSite().getIp();
    }

    public static void main(String[] args) {

        System.out.println(new IpServiceImpl().get());
        try {
            System.out.println(new IpServiceImpl().hostToIp("ddns123.phpyiqiwan.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * host to ip
     *
     * @param host
     * @return
     */
    @Override
    public String hostToIp(String host) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(host);
        // IP 地址
        return inetAddress.getHostAddress();

    }
}
