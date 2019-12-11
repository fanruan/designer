package com.fr.design.mainfarme.toolbar;

import	java.util.Locale;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.community.VideoAction;
import com.fr.design.actions.help.AboutAction;
import com.fr.design.actions.help.FineUIAction;
import com.fr.design.actions.help.TutorialAction;
import com.fr.design.actions.help.WebDemoAction;
import com.fr.design.actions.help.alphafine.AlphaFineAction;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.os.impl.SupportOSImpl;
import com.fr.design.update.actions.SoftwareUpdateAction;
import com.fr.general.GeneralContext;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;


/**
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2019/12/11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ToolBarMenuDock.class,
        GeneralContext.class,
        Locale.class,
        WorkContext.class,
        DesignerEnvManager.class,
        AlphaFineConfigManager.class})
public class ToolBarMenuDockTest {

    @Mock
    VideoAction videoAction;
    @Mock
    TutorialAction tutorialAction;
    @Mock
    WebDemoAction webDemoAction;
    @Mock
    SoftwareUpdateAction softwareUpdateAction;
    @Mock
    AlphaFineAction alphaFineAction;
    @Mock
    FineUIAction fineUIAction;
    @Mock
    AboutAction aboutAction;

    @Before
    public void testCreateHelpShortCutsBefore() {
        try {
            PowerMock.expectNew(VideoAction.class).andReturn(videoAction).anyTimes();
            PowerMock.expectNew(TutorialAction.class).andReturn(tutorialAction).anyTimes();
            PowerMock.expectNew(WebDemoAction.class).andReturn(webDemoAction).anyTimes();
            PowerMock.expectNew(SoftwareUpdateAction.class).andReturn(softwareUpdateAction).anyTimes();
            PowerMock.expectNew(AlphaFineAction.class).andReturn(alphaFineAction).anyTimes();
            PowerMock.expectNew(FineUIAction.class).andReturn(fineUIAction).anyTimes();
            PowerMock.expectNew(AboutAction.class).andReturn(aboutAction).anyTimes();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateHelpShortCuts() {

        // 处理判断条件
        GeneralContext context = EasyMock.createMock(GeneralContext.class);
        PowerMock.mockStatic(GeneralContext.class);
        EasyMock.expect(GeneralContext.getLocale()).andReturn(Locale.CHINA).once();
        EasyMock.expect(GeneralContext.getLocale()).andReturn(Locale.US).once();

        Workspace workspace = EasyMock.createMock(Workspace.class);
        PowerMock.mockStatic(WorkContext.class);
        EasyMock.expect(WorkContext.getCurrent()).andReturn(workspace).anyTimes();

        EasyMock.expect(workspace.isLocal()).andReturn(false).once();
        EasyMock.expect(workspace.isLocal()).andReturn(true).once();

        DesignerEnvManager envManager = EasyMock.createMock(DesignerEnvManager.class);
        PowerMock.mockStatic(DesignerEnvManager.class);
        EasyMock.expect(DesignerEnvManager.getEnvManager()).andReturn(envManager).anyTimes();

        EasyMock.expect(envManager.isOpenDebug()).andReturn(false).once();
        EasyMock.expect(envManager.isOpenDebug()).andReturn(true).once();

        AlphaFineConfigManager configManager = EasyMock.createMock(AlphaFineConfigManager.class);
        PowerMock.mockStatic(AlphaFineConfigManager.class);
        EasyMock.expect(AlphaFineConfigManager.isALPHALicAvailable()).andReturn(false).once();
        EasyMock.expect(AlphaFineConfigManager.isALPHALicAvailable()).andReturn(true).once();

        SupportOSImpl osImpl = EasyMock.createMock(SupportOSImpl.class);
        Whitebox.setInternalState(SupportOSImpl.class, "FINEUI", osImpl);
        EasyMock.expect(osImpl.support()).andReturn(false).anyTimes();

        EasyMock.replay(context, workspace, envManager, configManager, osImpl);
        PowerMock.replayAll();


        ToolBarMenuDock menuDock = new ToolBarMenuDock() {
            @Override
            public ShortCut[] createNewFileShortCuts() {
                return new ShortCut[0];
            }
        };

        ShortCut[] shortCuts1 = new ShortCut[] {SeparatorDef.DEFAULT, aboutAction};
        ShortCut[] shortCuts2 = new ShortCut[] {videoAction, tutorialAction, webDemoAction,
                softwareUpdateAction, alphaFineAction, SeparatorDef.DEFAULT, aboutAction};

        Assert.assertEquals(menuDock.createHelpShortCuts(), shortCuts1);
        Assert.assertEquals(menuDock.createHelpShortCuts(), shortCuts2);

    }
}
