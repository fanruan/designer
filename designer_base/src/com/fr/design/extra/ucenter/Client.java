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

/**
 * Created by lp on 2016/9/9.
 */
public class Client extends PHPFunctions{

    public static String UC_IP = "211.149.195.54";
    public static String UC_API = "";
    public static String UC_CONNECT = "";
    public static String UC_KEY = "Rc85U37411p4zdvcedm8D4t4D3l9Sa42H0kd98Gbd82aA99a61S2Z5LbQ9u430M0";
    public static String UC_APPID = "4";
    public static String UC_CLIENT_RELEASE = "20090212";
    public static String UC_ROOT = "";
    public static String UC_API_FUNC = UC_CONNECT.equals("mysql") ? "uc_api_mysql" : "uc_api_post";

    public String uc_user_login(String username, String password) {
        return uc_user_login(username, password, 0, 0);
    }

    public String uc_user_login(String username, String password, int isuid, int checkques) {
        return uc_user_login(username, password, isuid, checkques, "", "");
    }

    /**
     * 用户登录
     * @param username   用户名
     * @param password   密码
     * @param isuid      是否为uid
     * @param checkques  是否使用安全问题
     * @param questionid 安全提问
     * @param answer     安全提问答案
     * @return array     (uid/status, username, password, email)
     */
    public String uc_user_login(String username, String password, int isuid, int checkques, String questionid, String answer) {
        Map<String, Object> args = new HashMap<>();
        args.put("username", username);
        args.put("password", password);
        args.put("isuid", isuid);
        args.put("checkques", checkques);
        args.put("questionid", questionid);
        args.put("answer", answer);
        String $return = call_user_func(UC_API_FUNC, "user", "login", args);
        return UC_CONNECT.equals("mysql") ? $return : $return;
    }

    /**
     * 拼接发送的post请求
     * @param module 模块
     * @param action 操作模式
     * @param arg    参数
     * @return       发送的请求加密内容
     */
    public String uc_api_post(String module, String action, Map<String, Object> arg) {
        StringBuffer str = new StringBuffer();
        String sep = "";
        for (String k : arg.keySet()) {
            Object v = arg.get(k);
            k = urlencode(k);
            if (v.getClass().isAssignableFrom(Map.class)) {
                String s2 = "";
                String sep2 = "";
                for (String k2 : ((Map<String, Object>) v).keySet()) {
                    Object v2 = ((Map<String, Object>) v).get(k2);
                    k2 = urlencode(k2);
                    s2 += sep2 + "{" + k + "}[" + k2 + "]=" + urlencode(String.valueOf(v2));
                    sep2 = "&";
                }
                str.append(sep).append(s2);
            } else {
                str.append(sep).append(k).append("=").append(urlencode(String.valueOf(v), "GBK"));
            }
            sep = "&";
        }
        String $postdata = uc_api_requestdata(module, action, str.toString(), "");
        UC_API = SiteCenter.getInstance().acquireUrlByKind("bbs.ucapi");
        return uc_fopen2(UC_API + "/index.php", 500000, $postdata, "", true, UC_IP, 20, true);
    }

    public String uc_api_mysql(String model, String action, Map args) {
        return "";
    }

    public String uc_api_input(String data) {
        String str = urlencode(uc_authcode(data + "&agent=" + md5("") + "&time=" + time(), "ENCODE", UC_KEY), "GBK");
        return str;
    }

    protected String uc_api_requestdata(String module, String action, String arg, String extra) {
        String input = uc_api_input(arg);
        String post = "m=" + module + "&a=" + action + "&inajax=2&release=" + UC_CLIENT_RELEASE + "&input=" + input + "&appid=" + UC_APPID + extra;
        return post;
    }

    public String uc_authcode(String string, String operation, String key) {
        return uc_authcode(string, operation, key, 0);
    }

    /**
     * 内容加密
     * @param string    原文
     * @param operation decode或者encode
     * @param key       密钥
     * @param expiry    密文有效时限
     * @return          加密之后的原文
     */
    public String uc_authcode(String string, String operation, String key, int expiry) {
        int ckey_length = 4;
        key = md5(key != null ? key : UC_KEY);
        String keya = md5(substr(key, 0, 16));
        String keyb = md5(substr(key, 16, 16));
        String keyc = ckey_length > 0 ? (operation.equals("DECODE") ? substr(string, 0, ckey_length) : substr(md5(microtime()), - ckey_length)) : "";
        String cryptkey = keya + md5(keya + keyc);
        int key_length = cryptkey.length();
        string = operation.equals("DECODE") ? base64_decode(substr(string, ckey_length)) : sprintf("%010d", expiry > 0 ? expiry + time() : 0) + substr(md5(string + keyb), 0, 16) + string;
        int string_length = string.length();
        StringBuffer result1 = new StringBuffer();
        int[] box = new int[256];
        for (int i = 0; i < 256; i++) {
            box[i] = i;
        }
        int[] rndkey = new int[256];
        for (int i = 0; i <= 255; i++) {
            rndkey[i] = (int) cryptkey.charAt(i % key_length);
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
        for (int i = 0; i < string_length; i++) {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;
            result1.append((char) (((int) string.charAt(i)) ^ (box[(box[a] + box[j]) % 256])));
        }
        if (operation.equals("DECODE")) {
            String result = result1.toString();
            try {
                result = new String(result.getBytes("iso-8859-1"), "gbk");
            } catch (Exception e) {
                result = result1.substring(0, result1.length());
            }
            if ((Integer.parseInt(substr(result.toString(), 0, 10)) == 0 || Long.parseLong(substr(result.toString(), 0, 10)) - time() > 0) && substr(result.toString(), 10, 16).equals(substr(md5(substr(result.toString(), 26) + keyb), 0, 16))) {
                return substr(result.toString(), 26);
            } else {
                return "";
            }
        } else {
            return keyc + base64_encode(result1.toString()).replaceAll("=", "");
        }
    }

    protected String uc_fopen2(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
        url += url.indexOf("?") > 0 ? "&" : "?" + "__times__=1";
        return uc_fopen(url, limit, post, cookie, bysocket, ip, timeout, block);
    }

    /**
     * 本地模网络请求取数据
     * @param url      打开的url
     * @param limit    取返回的数据的长度
     * @param post     要发送的 POST 数据，如uid=1&password=1234
     * @param cookie   要模拟的 COOKIE 数据，如uid=123&auth=a2323sd2323
     * @param bysocket TRUE/FALSE 是否通过SOCKET打开
     * @param ip       IP地址
     * @param timeout  连接超时时间
     * @param block    是否为阻塞模式 defaul valuet:true
     * @return         取到的字符串
     */
    protected String uc_fopen(String url, int limit, String post, String cookie, boolean bysocket, String ip, int timeout, boolean block) {
        String result = "";
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
        StringBuffer out = new StringBuffer();
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
                    if (header == null || header.equals("") || header == "\r\n" || header == "\n") {
                        break;
                    }
                }
                while (true) {
                    String data = reader.readLine();
                    if (data == null || data.equals("")) {
                        break;
                    } else {
                        result += data;
                    }
                }
                fp.close();
            }
        } catch (IOException e) {
            FRContext.getLogger().info(e.getMessage());
        }
        return result;
    }
}