package com.fr.start;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/10
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DesignerJavaRuntime.class})
public class DesignerJavaRuntimeTest extends TestCase  {

    public void testIsInValidVmOptions() {
        PowerMock.mockStatic(DesignerJavaRuntime.class);
        DesignerJavaRuntime designerJavaRuntime = PowerMock.createPartialMock(DesignerJavaRuntime.class, "isInstallVersion", "getJvmOptions");
        String[] options = new String[]{"-Dfile.encoding=UTF-8", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"};
        EasyMock.expect(designerJavaRuntime.getJvmOptions()).andReturn(options).anyTimes();
        EasyMock.replay(designerJavaRuntime);
        PowerMock.replay(DesignerJavaRuntime.class);
        Assert.assertTrue(designerJavaRuntime.isInValidVmOptions());
    }
}