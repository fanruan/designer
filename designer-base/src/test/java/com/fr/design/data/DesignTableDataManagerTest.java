package com.fr.design.data;

import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.tabledata.wrapper.TableDataFactory;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.file.ProcedureConfig;
import com.fr.file.TableDataConfig;
import com.fr.invoke.Reflect;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TableDataFactory.class, StoreProcedure.class, TableDataConfig.class, ProcedureConfig.class})
@SuppressStaticInitializationFor({"com.fr.design.data.tabledata.wrapper.TableDataFactory"})
public class DesignTableDataManagerTest {


    @Test
    public void testFireDataChange() {

        DesignTableDataManager.envChange();


        PowerMock.mockStatic(TableDataConfig.class);

        TableDataConfig config = EasyMock.mock(TableDataConfig.class);
        TableDataConfig config2 = EasyMock.mock(TableDataConfig.class);

        TableData td1 = EasyMock.mock(TableData.class);
        TableData td2 = EasyMock.mock(TableData.class);
        TableData td3 = EasyMock.mock(TableData.class);
        TableData td4 = EasyMock.mock(TableData.class);
        EasyMock.expect(config.getTableData("firstData")).andReturn(td1).once();
        EasyMock.expect(config.getTableData("secondData")).andReturn(td2).once();
        EasyMock.expect(config2.getTableData("firstData")).andReturn(td3).once();
        EasyMock.expect(config2.getTableData("secondData")).andReturn(td4).once();

        EasyMock.expect(TableDataConfig.getInstance()).andReturn(config).once().andReturn(config2).once();

        PowerMock.mockStatic(TableDataFactory.class);
        EasyMock.expect(TableDataFactory.getSortOfChineseNameOfServerData(config))
                .andReturn(new String[]{"firstData", "secondData"}).once();
        EasyMock.expect(TableDataFactory.getSortOfChineseNameOfServerData(config2))
                .andReturn(new String[]{"firstData", "secondData"}).once();


        ProcedureConfig proConfig = EasyMock.mock(ProcedureConfig.class);
        StoreProcedure pc1 = PowerMock.createMock(StoreProcedure.class);
        StoreProcedure pc2 = PowerMock.createMock(StoreProcedure.class);
        ProcedureConfig proConfig2 = EasyMock.mock(ProcedureConfig.class);
        StoreProcedure pc3 = PowerMock.createMock(StoreProcedure.class);
        StoreProcedure pc4 = PowerMock.createMock(StoreProcedure.class);

        Map<String, StoreProcedure> procedureMap = new HashMap<>();
        procedureMap.put("firstPRO", pc1);
        procedureMap.put("secondPRO", pc2);
        EasyMock.expect(proConfig.getProcedure("firstPRO")).andReturn(pc1).once();
        EasyMock.expect(proConfig.getProcedure("secondPRO")).andReturn(pc2).once();

        Map<String, StoreProcedure> procedureMap2 = new HashMap<>();
        procedureMap2.put("firstPRO", pc3);
        procedureMap2.put("secondPRO", pc4);
        EasyMock.expect(proConfig2.getProcedure("firstPRO")).andReturn(pc3).once();
        EasyMock.expect(proConfig2.getProcedure("secondPRO")).andReturn(pc4).once();

        EasyMock.expect(proConfig.getProcedures()).andReturn(procedureMap).once();
        EasyMock.expect(proConfig2.getProcedures()).andReturn(procedureMap2).once();

        PowerMock.mockStatic(ProcedureConfig.class);
        EasyMock.expect(ProcedureConfig.getInstance()).andReturn(proConfig).once().andReturn(proConfig2).once();

        EasyMock.replay(proConfig, config, config2, proConfig2);
        PowerMock.replay(TableDataFactory.class, TableDataConfig.class, ProcedureConfig.class);


        Map<String, TableDataWrapper> map = DesignTableDataManager.getAllEditingDataSet(null);
        Assert.assertEquals(4, map.size());
        Assert.assertSame(pc1, map.get("firstPRO").getTableData());
        Assert.assertSame(pc2, map.get("secondPRO").getTableData());
        Assert.assertSame(td1, map.get("firstData").getTableData());
        Assert.assertSame(td2, map.get("secondData").getTableData());

        DesignTableDataManager.fireDSChanged(new HashMap<String, String>());

        Map<String, TableDataWrapper> map2 = DesignTableDataManager.getAllEditingDataSet(null);
        Assert.assertEquals(4, map2.size());
        Assert.assertSame(pc3, map2.get("firstPRO").getTableData());
        Assert.assertSame(pc4, map2.get("secondPRO").getTableData());
        Assert.assertSame(td3, map2.get("firstData").getTableData());
        Assert.assertSame(td4, map2.get("secondData").getTableData());


    }

    @Test
    public void testGetSelectedColumnNames() throws Exception {
        PowerMock.mockStatic(TableDataConfig.class);
        PowerMock.mockStatic(TemplateTableDataWrapper.class);
        TableDataConfig config = EasyMock.mock(TableDataConfig.class);
        TableData td1 = EasyMock.mock(TableData.class);
        TableData td2 = EasyMock.mock(TableData.class);
        TableDataSource dataSource = EasyMock.mock(TableDataSource.class);
        EasyMock.expect(config.getTableData("ds1")).andReturn(td1).anyTimes();
        EasyMock.expect(config.getTableData("ds2")).andReturn(td2).anyTimes();
        EasyMock.expect(dataSource.getTableData("ds1")).andReturn(td1).anyTimes();
        EasyMock.expect(dataSource.getTableData("ds2")).andReturn(td2).anyTimes();
        EasyMock.expect(TableDataConfig.getInstance()).andReturn(config).anyTimes();
        PowerMock.mockStatic(TableDataFactory.class);
        EasyMock.expect(TableDataFactory.getSortOfChineseNameOfServerData(config))
                .andReturn(new String[]{"ds1", "ds2"}).anyTimes();
        EasyMock.expect(TableDataFactory.getSortOfChineseNameOfTemplateData(dataSource))
                .andReturn(new String[]{"ds1", "ds2"});
        Map<TableDataSource, Map<String, String[]>> cache = Reflect.on(DesignTableDataManager.class).field(
                "columnCache").get();
        Map<String, String[]> map = new HashMap<>();
        map.put("ds1", new String[]{"a", "b", "c"});
        cache.put(dataSource, map);
        ProcedureConfig proConfig = EasyMock.mock(ProcedureConfig.class);
        Map<String, StoreProcedure> procedureMap = new HashMap<>();
        EasyMock.expect(proConfig.getProcedures()).andReturn(procedureMap).anyTimes();
        PowerMock.mockStatic(ProcedureConfig.class);
        EasyMock.expect(ProcedureConfig.getInstance()).andReturn(proConfig).anyTimes();
        EasyMock.replay(config, proConfig, dataSource);
        PowerMock.replay(TableDataFactory.class, TableDataConfig.class, ProcedureConfig.class,
                         TemplateTableDataWrapper.class);
        Assert.assertEquals(new String[]{"a", "b", "c"},
                            DesignTableDataManager.getSelectedColumnNames(dataSource, "ds1"));
    }

    @Test
    public void testAddDsColumnNames() {
        DesignTableDataManager.addDsColumnNames("ds1", new String[]{"a", "b", "c"});
        Map<TableDataSource, Map<String, String[]>> map = Reflect.on(DesignTableDataManager.class).field(
                "columnCache").get();
        Assert.assertEquals(new String[]{"a", "b", "c"}, map.get(null).get("ds1"));
    }
}
