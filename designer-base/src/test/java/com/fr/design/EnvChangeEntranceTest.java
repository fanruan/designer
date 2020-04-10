package com.fr.design;

import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.DesignerWorkspaceType;
import com.fr.env.CheckServiceDialog;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.channel.http.FunctionalHttpRequest;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author: Maksim
 * @Date: Created in 2020/3/5
 * @Description:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FunctionalHttpRequest.class,EnvChangeEntrance.class,CheckServiceDialog.class})
public class EnvChangeEntranceTest {

    @Test
    public void showServiceDialog() throws Exception {
        try {
            EnvChangeEntrance entrance = EnvChangeEntrance.getInstance();
            DesignerWorkspaceInfo selectedEnv = EasyMock.mock(DesignerWorkspaceInfo.class);
            WorkspaceConnectionInfo connectionInfo = EasyMock.mock(WorkspaceConnectionInfo.class);

            String remoteBranch = "Build#persist-2020.02.15.01.01.12.12";
            EasyMock.expect(selectedEnv.getConnection()).andReturn(connectionInfo);
            EasyMock.expect(selectedEnv.getType()).andReturn(DesignerWorkspaceType.Remote);
            EasyMock.expect(selectedEnv.getRemindTime()).andReturn(null);

            FunctionalHttpRequest request = EasyMock.mock(FunctionalHttpRequest.class);
            EasyMock.expect(request.getServiceList()).andReturn(null);

            PowerMock.expectNew(FunctionalHttpRequest.class, connectionInfo).andReturn(request).anyTimes();
            EasyMock.expect(request.getServerBranch()).andReturn(remoteBranch);

            CheckServiceDialog dialog = EasyMock.mock(CheckServiceDialog.class);
            PowerMock.expectNew(CheckServiceDialog.class, EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject()).andReturn(dialog);

            EasyMock.replay(request);
            EasyMock.replay(selectedEnv);
            EasyMock.replay(connectionInfo);
            PowerMock.replayAll();

            entrance.showServiceDialog(selectedEnv);
            Assert.assertTrue(true);
        }catch (Exception e){
            Assert.assertTrue(false);
        }
    }

}