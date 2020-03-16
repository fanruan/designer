package com.fr.common.detect;

import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/10
 */
public class CommonPortDetectorTest extends TestCase {

    private ServerSocket serverSocket;

    @Override
    public void setUp() throws Exception {
        serverSocket = new ServerSocket(55555);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket.accept();
                } catch (IOException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        }).start();
    }

    public void testCheckPort() {
        CommonPortDetector detector = CommonPortDetector.getInstance();
        boolean access = Reflect.on(detector).call("checkPort", 55555).get();
        Assert.assertTrue(access);
    }

    @Override
    public void tearDown() throws Exception {
        serverSocket.close();
    }
}