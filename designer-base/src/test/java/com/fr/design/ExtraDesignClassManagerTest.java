package com.fr.design;

import com.fr.config.dao.DaoContext;
import com.fr.config.dao.impl.LocalClassHelperDao;
import com.fr.config.dao.impl.LocalEntityDao;
import com.fr.config.dao.impl.LocalXmlEntityDao;
import com.fr.design.fun.ToolbarItemProvider;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JVirtualTemplate;
import com.fr.general.ModuleContext;
import com.fr.log.FineLoggerFactory;
import com.fr.report.restriction.CellCountRestriction;
import com.fr.report.restriction.ReportRestrictionScene;
import com.fr.restriction.Restrictions;
import com.fr.stable.Filter;
import com.fr.stable.module.Module;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zack
 * @version 10.0
 *          Created by zack on 2019/9/17
 */
public class ExtraDesignClassManagerTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        DaoContext.setEntityDao(new LocalEntityDao());
        DaoContext.setClassHelperDao(new LocalClassHelperDao());
        DaoContext.setXmlEntityDao(new LocalXmlEntityDao());
        ModuleContext.startModule(Module.PAGE_MODULE);
        Restrictions.register(ReportRestrictionScene.CELL_COUNT, new CellCountRestriction());
    }

    public void testGetWebOption() {
        try {
            final JTemplate jTemplate = new JVirtualTemplate(null);
            ToolbarItemProvider item = EasyMock.mock(ToolbarItemProvider.class);
            ToolbarItemProvider item1 = EasyMock.mock(ToolbarItemProvider.class);
            EasyMock.expect(item.accept(jTemplate)).andReturn(false).anyTimes();
            EasyMock.expect(item.classForWidget()).andReturn(null).anyTimes();
            EasyMock.expect(item.iconPathForWidget()).andReturn("").anyTimes();
            EasyMock.expect(item.nameForWidget()).andReturn("1").anyTimes();
            EasyMock.expect(item1.accept(jTemplate)).andReturn(true).anyTimes();
            EasyMock.expect(item1.classForWidget()).andReturn(null).anyTimes();
            EasyMock.expect(item1.iconPathForWidget()).andReturn("").anyTimes();
            EasyMock.expect(item1.nameForWidget()).andReturn("2").anyTimes();
            EasyMock.replay(item);
            EasyMock.replay(item1);

            Set<ToolbarItemProvider> set = new HashSet<>();
            set.add(item);
            set.add(item1);
            WidgetOption[] widgetOptions = ExtraDesignClassManager.getInstance().getWebWidgetOptions(set, new Filter<ToolbarItemProvider>() {
                @Override
                public boolean accept(ToolbarItemProvider toolbarItemProvider) {
                    return toolbarItemProvider.accept(jTemplate);
                }
            });
            Assert.assertEquals(1, widgetOptions.length);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}