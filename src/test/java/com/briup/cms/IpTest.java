package com.briup.cms;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author YuYan
 * @date 2023-12-08 15:07:39
 */
public class IpTest {

    public static void main(String[] args) throws Exception {
        /* 定义要访问的服务地址 */
        String address = "http://whois.pconline.com.cn/ipJson.jsp"
                + "?json=true&ip=180.101.50.188";
        /* 封装为一个URL对象 */
        URL url = new URL(address);
        /* 创建一个连接对象 */
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        /* 连接对象调用连接方法建立和服务器的连接 */
        urlConnection.connect();
        /* 剩余的操作就变成纯IO流读写数据 */
        /* 获取一个服务器输入流对象，可以读取响应报文体部数据 */
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        int i = -1;
        String s = "";
        while ((i = isr.read()) != -1) {
            s += (char) i;
        }
        System.out.println(s.trim());

    }

}
