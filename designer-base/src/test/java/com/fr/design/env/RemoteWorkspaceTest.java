package com.fr.design.env;

import com.fr.base.operator.common.CommonOperator;
import com.fr.invoke.ReflectException;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2020/1/2
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkContext.class})
public class RemoteWorkspaceTest {
    @Test
    public void testIsWarDeploy() {

        Workspace workspace = EasyMock.mock(Workspace.class);
        PowerMock.mockStatic(WorkContext.class);
        EasyMock.expect(WorkContext.getCurrent()).andReturn(workspace).anyTimes();

        CommonOperator operator = EasyMock.createMock(CommonOperator.class);
        EasyMock.expect(workspace.get(CommonOperator.class)).andReturn(operator).anyTimes();

        EasyMock.expect(operator.isWarDeploy()).andReturn(true).once();
        EasyMock.expect(operator.isWarDeploy()).andReturn(false).once();
        EasyMock.expect(operator.isWarDeploy()).andThrow(new ReflectException()).once();

        EasyMock.replay(workspace, operator);
        PowerMock.replayAll();

        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo("url", "username", "password", "certPath", "certSecretKey");

        RemoteWorkspace remoteWorkspace= new RemoteWorkspace(null, info);

        Assert.assertTrue(remoteWorkspace.isWarDeploy());
        Assert.assertFalse(remoteWorkspace.isWarDeploy());
        Assert.assertFalse(remoteWorkspace.isWarDeploy());

        EasyMock.verify(workspace, operator);
        PowerMock.verifyAll();


    }
}