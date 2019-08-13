package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.widget.ui.ButtonDefinePane;
import com.fr.form.ui.Button;
import com.fr.form.ui.Widget;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
public class WidgetDefinePaneFactoryTest {
    
    @Test
    @PrepareForTest({ExtraDesignClassManager.class, WidgetDefinePaneFactory.class})
    public void testCreateWidgetDefinePane() throws Exception {
        
        Map<Class<? extends Widget>, Appearance> map = new HashMap<>();
        ExtraDesignClassManager mockDesignManager = EasyMock.mock(ExtraDesignClassManager.class);
        EasyMock.expect(mockDesignManager.getCellWidgetOptionsMap()).andReturn(map).anyTimes();
        EasyMock.replay(mockDesignManager);
        
        PowerMock.mockStatic(ExtraDesignClassManager.class);
        EasyMock.expect(ExtraDesignClassManager.getInstance()).andReturn(mockDesignManager).anyTimes();
        PowerMock.replayAll(ExtraDesignClassManager.class);
        
        Button mockWidget = EasyMock.mock(Button.class);
        EasyMock.replay(mockWidget);
        
        ButtonDefinePane mockPane = EasyMock.mock(ButtonDefinePane.class);
        mockPane.populateBean(EasyMock.anyObject(Button.class));
        EasyMock.expectLastCall();
        EasyMock.replay(mockPane);
        
        
        Operator mockOperator = EasyMock.mock(Operator.class);
        mockOperator.did(EasyMock.anyObject(DataCreatorUI.class), EasyMock.anyString());
        EasyMock.replay(mockOperator);
        
        WidgetDefinePaneFactory.RN rn1 = WidgetDefinePaneFactory.createWidgetDefinePane(mockWidget, mockOperator);
        Assert.assertNull(rn1);
        
        Appearance appearance = new Appearance(ButtonDefinePane.class, "test");
        map.put(mockWidget.getClass(), appearance);
    
        WidgetDefinePaneFactory.RN rn2 = WidgetDefinePaneFactory.createWidgetDefinePane(mockWidget, mockOperator);
        Assert.assertNotNull(rn2);
        
        map.clear();
        
        WidgetDefinePaneFactory.RN rn3 = WidgetDefinePaneFactory.createWidgetDefinePane(mockWidget, mockOperator);
        Assert.assertNull(rn3);
    }
    
    
}