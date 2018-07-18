package com.fr.env;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yaohwu
 */
public class RemoteEnvURLTest {

    @Test
    public void testURLParser() {

        String a;
        RemoteEnvURL b;

        // https or http begin
        a = "www.baidu.com:9090/web/servlet/a";
        b = new RemoteEnvURL(a);
        Assert.assertFalse(b.getHttps());

        a = "http://www.baidu.com:9090/web/servlet/a";
        b = new RemoteEnvURL(a);
        Assert.assertFalse(b.getHttps());

        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteEnvURL(a);
        Assert.assertTrue(b.getHttps());
        // https or http end

        // host begin
        a = "https://www.baidu.com/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("www.baidu.com", b.getHost());

        a = "https://baidu.com/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("baidu.com", b.getHost());

        a = "https://192.168.1/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("192.168.1", b.getHost());

        a = "https://中文·o((⊙﹏⊙))o囖/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getHost());

        a = "https://a.b.c.d.e.f/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("a.b.c.d.e.f", b.getHost());
        // host end

        //port begin
        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("9090", b.getPort());

        a = "https://www.baidu.com:/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getPort());

        a = "https://www.baidu.com/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getPort());

        a = "https://www.baidu.com:kk/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("kk", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖///////";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());
        //port end


        //web begin

        a = "https://www.baidu.com:9090///";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090/";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090///web///servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090/中文·o((⊙﹏⊙))o囖/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getWeb());

        a = "https://www.baidu.com:9090/web///servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090///web/servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("web", b.getWeb());
        //web end

        //servlet begin
        a = "https://www.baidu.com:9090///web////servlet/a";
        b = new RemoteEnvURL(a);
        assertEquals("servlet", b.getServlet());

        a = "https://www.baidu.com:9090";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090/";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web//";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web/";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web//";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web/a/";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com:9090//web/a";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());


        a = "https://www.baidu.com:9090//web/a//";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com:9090//web/中文·o((⊙﹏⊙))o囖//";
        b = new RemoteEnvURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getServlet());

        a = "https://www.baidu.com//web//";
        b = new RemoteEnvURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com//web/a/";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com//web/a";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());


        a = "https://www.baidu.com//web/a//";
        b = new RemoteEnvURL(a);
        assertEquals("a", b.getServlet());
        // servlet end

        //others begin
        a = "https://www.baidu.com/web/servlet/ahttps://www.baidu.com/web/servlet/a";
        b = new RemoteEnvURL(a);
        Assert.assertTrue(b.getHttps());
        assertEquals("www.baidu.com", b.getHost());
        assertEquals("", b.getPort());
        assertEquals("web", b.getWeb());
        assertEquals("servlet", b.getServlet());
        //others begin
    }
}
