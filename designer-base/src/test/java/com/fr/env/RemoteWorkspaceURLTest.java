package com.fr.env;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yaohwu
 */
public class RemoteWorkspaceURLTest {


    @Test
    public void testUrlReset() {

        String a = "https://yaohwu:8080/webroot/app/c/d";
        RemoteWorkspaceURL workspaceURL = new RemoteWorkspaceURL(a);
        Assert.assertEquals(a, workspaceURL.getURL());
        Assert.assertEquals("app", workspaceURL.getServlet());
        Assert.assertEquals("webroot", workspaceURL.getWeb());
        Assert.assertEquals("yaohwu", workspaceURL.getHost());
        Assert.assertEquals("8080", workspaceURL.getPort());
        Assert.assertTrue(workspaceURL.getHttps());

        workspaceURL.setHttps(false);
        workspaceURL.setHost("finereport");

        Assert.assertEquals(a, workspaceURL.getURL());
        Assert.assertEquals("app", workspaceURL.getServlet());
        Assert.assertEquals("webroot", workspaceURL.getWeb());
        Assert.assertEquals("finereport", workspaceURL.getHost());
        Assert.assertEquals("8080", workspaceURL.getPort());
        Assert.assertFalse(workspaceURL.getHttps());

        workspaceURL.resetUrl();

        Assert.assertEquals("http://finereport:8080/webroot/app", workspaceURL.getURL());
        Assert.assertEquals("app", workspaceURL.getServlet());
        Assert.assertEquals("webroot", workspaceURL.getWeb());
        Assert.assertEquals("finereport", workspaceURL.getHost());
        Assert.assertEquals("8080", workspaceURL.getPort());
        Assert.assertFalse(workspaceURL.getHttps());
    }

    @Test
    public void testURLParser() {

        String a;
        RemoteWorkspaceURL b;

        // https or http begin
        a = "www.baidu.com:9090/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        Assert.assertFalse(b.getHttps());

        a = "http://www.baidu.com:9090/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        Assert.assertFalse(b.getHttps());

        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        Assert.assertTrue(b.getHttps());
        // https or http end

        // host begin
        a = "https://www.baidu.com/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("www.baidu.com", b.getHost());

        a = "https://baidu.com/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("baidu.com", b.getHost());

        a = "https://192.168.1/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("192.168.1", b.getHost());

        a = "https://中文·o((⊙﹏⊙))o囖/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getHost());

        a = "https://a.b.c.d.e.f/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a.b.c.d.e.f", b.getHost());
        // host end

        //port begin
        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("9090", b.getPort());

        a = "https://www.baidu.com:/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getPort());

        a = "https://www.baidu.com/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getPort());

        a = "https://www.baidu.com:kk/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("kk", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());

        a = "https://www.baidu.com:中文·o((⊙﹏⊙))o囖///////";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getPort());
        //port end


        //web begin

        a = "https://www.baidu.com:9090///";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090/";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getWeb());

        a = "https://www.baidu.com:9090///web///servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090/中文·o((⊙﹏⊙))o囖/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getWeb());

        a = "https://www.baidu.com:9090/web///servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("web", b.getWeb());

        a = "https://www.baidu.com:9090///web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("web", b.getWeb());
        //web end

        //servlet begin
        a = "https://www.baidu.com:9090///web////servlet/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("servlet", b.getServlet());

        a = "https://www.baidu.com:9090";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090/";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web/";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com:9090//web/a/";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com:9090//web/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());


        a = "https://www.baidu.com:9090//web/a//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com:9090//web/中文·o((⊙﹏⊙))o囖//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("中文·o((⊙﹏⊙))o囖", b.getServlet());

        a = "https://www.baidu.com//web//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("", b.getServlet());

        a = "https://www.baidu.com//web/a/";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());

        a = "https://www.baidu.com//web/a";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());


        a = "https://www.baidu.com//web/a//";
        b = new RemoteWorkspaceURL(a);
        assertEquals("a", b.getServlet());
        // servlet end

        //others begin
        a = "https://www.baidu.com/web/servlet/ahttps://www.baidu.com/web/servlet/a";
        b = new RemoteWorkspaceURL(a);
        Assert.assertTrue(b.getHttps());
        assertEquals("www.baidu.com", b.getHost());
        assertEquals("", b.getPort());
        assertEquals("web", b.getWeb());
        assertEquals("servlet", b.getServlet());
        //others begin
    }
}
