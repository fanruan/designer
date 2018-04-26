package com.fr.design.extra.ucenter;

import com.fr.base.FRContext;
import com.fr.general.SiteCenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author lp
 * @date 2016/9/9
 */
public class Client extends AbstractClient {

    private static String UC_IP = "";
    private static String UC_API = "";
    private static String UC_CONNECT = "";
    private static String UC_KEY = "Rc85U37411p4zdvcedm8D4t4D3l9Sa42H0kd98Gbd82aA99a61S2Z5LbQ9u430M0";
    private static String UC_APPID = "4";
    private static String UC_CLIENT_RELEASE = "20090212";
    public static String UC_ROOT = "";
    private static String UC_API_FUNC = "mysql".equals(UC_CONNECT) ? UC_API_MYSQL : UC_API_POST;

    public String ucUserLogin(String username, String password) {
        return ucUserLogin(username, password, 0, 0);
    }

    public String ucUserLogin(String username, String password, int isUid, int checkQues) {
        return ucUserLogin(username, password, isUid, checkQues, "", "");
    }

    /**
     * 用户登录
     *
     * @param username   用户名
     * @param password   密码
     * @param isUid      是否为uid
     * @param checkQues  是否使用安全问题
     * @param questionId 安全提问
     * @param answer     安全提问答案
     * @return array     (uid/status, username, password, email)
     */
    public String ucUserLogin(String username, String password, int isUid, int checkQues, String questionId, String answer) {
        Map<String, Object> args = new HashMap<>(6);
        args.put("username", username);
        args.put("password", password);
        args.put("isUid", isUid);
        args.put("checkQues", checkQues);
        args.put("questionId", questionId);
        args.put("answer", answer);
        String res = callUserFunc(UC_API_FUNC, "user", "login", args);
        return "mysql".equals(UC_CONNECT) ? res : res;
    }

    /**
     * 拼接发送的post请求
     *
     * @param module 模块
     * @param action 操作模式
     * @param arg    参数
     * @return 发送的请求加密内容
     */
    @Override
    public String ucApiPost(String module, String action, Map<String, Object> arg) {
        StringBuilder str = new StringBuilder();
        String sep = "";
        for (String k : arg.keySet()) {
            Object v = arg.get(k);
            k = urlEncode(k);
            if (v.getClass().isAssignableFrom(Map.class)) {
                StringBuilder s2 = new StringBuilder();
                String sep2 = "";
                for (String k2 : ((Map<String, Object>) v).keySet()) {
                    Object v2 = ((Map<String, Object>) v).get(k2);
                    k2 = urlEncode(k2);
                    s2.append(sep2).append("{").append(k).append("}[").append(k2).append("]=").append(urlEncode(String.valueOf(v2)));
                    sep2 = "&";
                }
                str.append(sep).append(s2);
            } else {
                str.append(sep).append(k).append("=").append(urlEncode(String.valueOf(v), "GBK"));
            }
            sep = "&";
        }
        String postData = ucApiRequestdata(module, action, str.toString(), "");
        UC_API = SiteCenter.getInstance().acquireUrlByKind("bbs.ucapi");
        UC_IP = SiteCenter.getInstance().acquireUrlByKind("bbs.ip");
        return ucFopen2(UC_API + "/index.php", 500000, postData, "", true, UC_IP, 20, true);
    }

    @Override
    public String ucApiMysql(String model, String action, Map args) {
        return "";
    }

    public String ucApiInput(String data) {
        return urlEncode(ucAuthCode(data + "&agent=" + md5("") + "&time=" + time(), "ENCODE", UC_KEY), "GBK");
    }

    protected String ucApiRequestdata(String module, String action, String arg, String extra) {
        String input = ucApiInput(arg);
        return "m=" + module + "&a=" + action + "&inajax=2&release=" + UC_CLIENT_RELEASE + "&input=" + input + "&appid=" + UC_APPID + extra;
    }

    public String ucAuthCode(String string, String operation, String key) {
        return ucAuthCode(string, operation, key, 0);
    }

    /**
     * 内容加密
     *
     * @param string    原文
     * @param operation decode或者encode
     * @param key       密钥
     * @param expiry    密文有效时限
     * @return 加密之后的原文
     */
    public String ucAuthCode(String string, String operation, String key, int expiry) {
        int ckeyLength = 4;
        key = md5(key != null ? key : UC_KEY);
        String keya = md5(subStr(key, 0, 16));
        String keyb = md5(subStr(key, 16, 16));
        String keyc = "DECODE".equals(operation) ? subStr(string, 0, ckeyLength) : subStr(md5(microTime()), -ckeyLength);
        String cryptkey = keya + md5(keya + keyc);
        int keyLength = cryptkey.length();
        string = "DECODE".equals(operation) ? base64Decode(subStr(string, ckeyLength)) : sprintf("%010d", expiry > 0 ? expiry + time() : 0) + subStr(md5(string + keyb), 0, 16) + string;
        int stringLength = string.length();
        StringBuilder result1 = new StringBuilder();
        int[] box = new int[256];
        for (int i = 0; i < 256; i++) {
            box[i] = i;
        }
        int[] rndkey = new int[256];
        for (int i = 0; i <= 255; i++) {
            rndkey[i] = (int) cryptkey.charAt(i % keyLength);
        }
        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + box[i] + rndkey[i]) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }
        j = 0;
        int a = 0;
        for (int i = 0; i < stringLength; i++) {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;
            result1.append((char) (((int) string.charAt(i)) ^ (box[(box[a] + box[j]) % 256])));
        }
        if ("DECODE".equals(operation)) {
            String result = result1.toString();
            try {
                result = new String(result.getBytes("iso-8859-1"), "gbk");
            } catch (Exception e) {
                result = result1.substring(0, result1.length());
            }
            if ((Integer.parseInt(subStr(result, 0, 10)) == 0 || Long.parseLong(subStr(result, 0, 10)) - time() > 0) && subStr(result, 10, 16).equals(subStr(md5(subStr(result, 26) + keyb), 0, 16))) {
                return subStr(result, 26);
            } else {
                return "";
            }
        } else {
            return keyc + base64Encode(result1.toString()).replaceAll("=", "");
        }
    }

    protected String ucFopen2(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
        url += url.indexOf("?") > 0 ? "&" : "?" + "__times__=1";
        return ucFopen(url, limit, post, cookie, bysocket, ip, timeout, block);
    }

    /**
     * 本地模网络请求取数据
     *
     * @param url      打开的url
     * @param limit    取返回的数据的长度
     * @param post     要发送的 POST 数据，如uid=1&password=1234
     * @param cookie   要模拟的 COOKIE 数据，如uid=123&auth=a2323sd2323
     * @param bysocket TRUE/FALSE 是否通过SOCKET打开
     * @param ip       IP地址
     * @param timeout  连接超时时间
     * @param block    是否为阻塞模式 defaul valuet:true
     * @return 取到的字符串
     */
    private String ucFopen(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
        StringBuilder result = new StringBuilder();
        URL matches;
        String host = "";
        String path = "";
        int port = 80;
        try {
            matches = new URL(url);
            host = matches.getHost();
            path = matches.getPath() != null ? matches.getPath() + (matches.getQuery() != null ? "?" + matches.getQuery() : "") : "/";
            if (matches.getPort() > 0) port = matches.getPort();
        } catch (Exception e1) {
            FRContext.getLogger().info(e1.getMessage());
        }
        StringBuilder out = new StringBuilder();
        if (post != null && post.length() > 0) {
            out.append("POST ").append(path).append(" HTTP/1.0\r\n");
            out.append("Accept: */*\r\n");
            out.append("Accept-Language: zh-cn\r\n");
            out.append("Content-Type: application/x-www-form-urlencoded\r\n");
            out.append("User-Agent: \r\n");
            out.append("Host: ").append(host).append("\r\n");
            out.append("Content-Length: ").append(post.length()).append("\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cache-Control: no-cache\r\n");
            out.append("Cookie: \r\n\r\n");
            out.append(post);
        } else {
            out.append("GET $path HTTP/1.0\r\n");
            out.append("Accept: */*\r\n");
            out.append("Accept-Language: zh-cn\r\n");
            out.append("User-Agent: Java/1.5.0_01\r\n");
            out.append("Host: $host\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cookie: $cookie\r\n\r\n");
        }
        try {
            Socket fp = new Socket(ip != null && ip.length() > 10 ? ip : host, port);
            if (!fp.isConnected()) {
                return "";
            } else {
                OutputStream os = fp.getOutputStream();
                os.write(out.toString().getBytes());
                InputStream ins = fp.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "iso-8859-1"));
                while (true) {
                    String header = reader.readLine();
                    if (header == null || "".equals(header) || Objects.equals(header, "\r\n") || Objects.equals(header, "\n")) {
                        break;
                    }
                }
                while (true) {
                    String data = reader.readLine();
                    if (data == null || "".equals(data)) {
                        break;
                    } else {
                        result.append(data);
                    }
                }
                fp.close();
            }
        } catch (IOException e) {
            FRContext.getLogger().info(e.getMessage());
        }
        return result.toString();
    }
}