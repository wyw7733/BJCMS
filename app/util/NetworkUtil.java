package util;

import org.apache.http.HttpRequest;
import play.mvc.Http;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zuopanpan on 2016/3/7.
 */
public class NetworkUtil {
    public static String getRealIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
    public static String getMyIP() throws IOException {
        InputStream ins = null;
        try {
            URL url = new URL("http://iframe.ip138.com/ic.asp");
            URLConnection con = url.openConnection();
            ins = con.getInputStream();
            InputStreamReader isReader = new InputStreamReader(ins, "GB2312");
            BufferedReader bReader = new BufferedReader(isReader);
            StringBuffer webContent = new StringBuffer();
            String str = null;
            while ((str = bReader.readLine()) != null) {
                webContent.append(str);
            }
            int start = webContent.indexOf("[") + 1;
            int end = webContent.indexOf("]");
            return webContent.substring(start, end);
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
    }
    // 外网IP提供者的网址
    static String externalIpProviderUrl;

    // 本机外网IP地址
    static String myExternalIpAddress;

   /* public ExternalIpAddressFetcher(String externalIpProviderUrl) {
        this.externalIpProviderUrl = externalIpProviderUrl;

        String returnedhtml = fetchExternalIpProviderHTML(externalIpProviderUrl);

        parse(returnedhtml);
    }*/

    /**
     * 从外网提供者处获得包含本机外网地址的字符串
     * 从http://checkip.dyndns.org返回的字符串如下
     * <html><head><title>Current IP Check</title></head><body>Current IP Address: 123.147.226.222</body></html>
     * @param externalIpProviderUrl
     * @return
     */
    private String fetchExternalIpProviderHTML(String externalIpProviderUrl) {
        // 输入流
        InputStream in = null;

        // 到外网提供者的Http连接
        HttpURLConnection httpConn = null;

        try {
            // 打开连接
            URL url = new URL(externalIpProviderUrl);
            httpConn = (HttpURLConnection) url.openConnection();

            // 连接设置
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

            // 获取连接的输入流
            in = httpConn.getInputStream();
            byte[] bytes=new byte[1024];// 此大小可根据实际情况调整

            // 读取到数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead=in.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            // 将字节转化为为UTF-8的字符串
            String receivedString=new String(bytes,"UTF-8");

            // 返回
            return receivedString;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                httpConn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 出现异常则返回空
        return null;
    }

    /**
     * 使用正则表达式解析返回的HTML文本,得到本机外网地址
     * @param html
     */
    private void parse(String html){
        Pattern pattern=Pattern.compile("(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})", Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(html);
        while(matcher.find()){
            myExternalIpAddress=matcher.group(0);
        }
    }

    /**
     * 得到本机外网地址,得不到则为空
     * @return
     */
    public static String getMyExternalIpAddress() {
        return myExternalIpAddress;
    }
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
