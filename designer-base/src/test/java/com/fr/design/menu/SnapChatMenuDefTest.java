package com.fr.design.menu;

import com.fr.config.dao.DaoContext;
import com.fr.config.dao.impl.LocalClassHelperDao;
import com.fr.config.dao.impl.LocalEntityDao;
import com.fr.config.dao.impl.LocalXmlEntityDao;
import com.fr.design.actions.UpdateAction;
import com.fr.design.notification.SnapChatAllTypes;
import com.fr.design.notification.SnapChatConfig;
import com.fr.stable.xml.XMLableReader;
import com.fr.store.StateHubManager;
import com.fr.store.impl.MemoryLock;
import com.fr.store.impl.MemoryStore;
import com.fr.transaction.Configurations;
import com.fr.transaction.LocalConfigurationHelper;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.awt.event.ActionEvent;
import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SnapChatConfig.class, SnapChatUpdateAction.class})
@PowerMockIgnore({"javax.crypto.*","javax.net.ssl.*","sun.security.ssl.*","com.sun.*"})
public class SnapChatMenuDefTest {
    
    @Before
    public void before() throws Exception {
    
        DaoContext.setXmlEntityDao(new LocalXmlEntityDao());
        DaoContext.setClassHelperDao(new LocalClassHelperDao());
        DaoContext.setEntityDao(new LocalEntityDao());
        StateHubManager.setLock(new MemoryLock());
        StateHubManager.setStorage(new MemoryStore());
        Configurations.setHelper(new LocalConfigurationHelper());
    }
    
    @Test
    public void testAddShortCut() throws Exception {
    
        PowerMock.suppress(MemberMatcher.constructor(UpdateAction.class));
        SnapChatConfig snapChatConfig = EasyMock.partialMockBuilder(SnapChatConfig.class)
                .addMockedMethod("readXML")
                .createMock();
        Whitebox.setInternalState(snapChatConfig, "markReadMap", new HashMap());
        snapChatConfig.readXML(EasyMock.anyObject(XMLableReader.class));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(snapChatConfig);
        
        PowerMock.mockStatic(SnapChatConfig.class);
        EasyMock.expect(SnapChatConfig.getInstance()).andReturn(snapChatConfig).anyTimes();
        PowerMock.replayAll();
        
        SnapChatMenuDef menuDef = new SnapChatMenuDef("test", SnapChatAllTypes.Menu.BBS);
        ShortCut action = new SnapChatUpdateAction(SnapChatAllTypes.Menu.BBS) {
            @Override
            protected void actionPerformed0(ActionEvent e) {
            
            }
        };
    
        Assert.assertTrue(menuDef.hasRead());
        
        menuDef.addShortCut(action);
        Assert.assertFalse(menuDef.hasRead());
    }
    
    @Test
    public void testInsertShortCut() throws Exception {
    
        PowerMock.suppress(MemberMatcher.constructor(UpdateAction.class));
        SnapChatConfig snapChatConfig = EasyMock.partialMockBuilder(SnapChatConfig.class)
                .addMockedMethod("readXML")
                .createMock();
        Whitebox.setInternalState(snapChatConfig, "markReadMap", new HashMap());
        snapChatConfig.readXML(EasyMock.anyObject(XMLableReader.class));
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(snapChatConfig);
    
        PowerMock.mockStatic(SnapChatConfig.class);
        EasyMock.expect(SnapChatConfig.getInstance()).andReturn(snapChatConfig).anyTimes();
        PowerMock.replayAll();
    
        SnapChatMenuDef menuDef = new SnapChatMenuDef("test", SnapChatAllTypes.Menu.BBS);
        ShortCut action = new SnapChatUpdateAction(SnapChatAllTypes.Menu.BBS) {
            @Override
            protected void actionPerformed0(ActionEvent e) {
            
            }
        };
    
        Assert.assertTrue(menuDef.hasRead());
    
        menuDef.insertShortCut(0, action);
        Assert.assertFalse(menuDef.hasRead());
    }
}