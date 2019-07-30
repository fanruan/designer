package com.fr.design.mainframe.alphafine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.general.GeneralContext;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class AlphaFineHelperTest {
    
    @Test
    @PrepareForTest({GeneralContext.class, DesignerEnvManager.class})
    @SuppressStaticInitializationFor("com.fr.design.mainframe.alphafine.AlphaFineHelper")
    public void testSwitchConfig4Locale() throws Exception {
    
        PowerMock.mockStatic(GeneralContext.class);
        EasyMock.expect(GeneralContext.isChineseEnv()).andReturn(true).times(1).andReturn(false).times(1);
    
        DesignerEnvManager manager = EasyMock.mock(DesignerEnvManager.class);
        EasyMock.expect(manager.getAlphaFineConfigManager()).andReturn(new AlphaFineConfigManager()).anyTimes();
        EasyMock.replay(manager);
    
        PowerMock.mockStatic(DesignerEnvManager.class);
        EasyMock.expect(DesignerEnvManager.getEnvManager()).andReturn(manager).anyTimes();
        
        PowerMock.replayAll();
    
        AlphaFineHelper.switchConfig4Locale();
        AlphaFineConfigManager config = manager.getAlphaFineConfigManager();
        Assert.assertEquals(true, config.isSearchOnLine());
    
        AlphaFineHelper.switchConfig4Locale();
        Assert.assertEquals(false, config.isSearchOnLine());
        
    
    }
}