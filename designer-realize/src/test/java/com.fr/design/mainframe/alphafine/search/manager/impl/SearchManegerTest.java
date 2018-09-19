package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearchManegerTest {
    SimilarSearchManeger similarSearchManeger;
    ComplementAdviceManager complementAdviceManager;
    DocumentSearchManager documentSearchManager;
    PluginSearchManager pluginSearchManager;
    RecommendSearchManager recommendSearchManager;
    RecentSearchManager recentSearchManager;
    ActionSearchManager actionSearchManager;
    FileSearchManager fileSearchManager;
    SegmentationManager segmentationManager;

    @Before
    public void setUp() {
        recentSearchManager = RecentSearchManager.getInstance();
        recommendSearchManager = RecommendSearchManager.getInstance();
        similarSearchManeger = SimilarSearchManeger.getInstance();
        complementAdviceManager = ComplementAdviceManager.getInstance();
        documentSearchManager = DocumentSearchManager.getInstance();
        pluginSearchManager = PluginSearchManager.getInstance();
        actionSearchManager = ActionSearchManager.getInstance();
        fileSearchManager = FileSearchManager.getInstance();
        segmentationManager = SegmentationManager.getInstance();
    }

    @Test
    public void getSearchResultTest() {

        //断网情况
        SearchResult noConnectList;
        noConnectList = similarSearchManeger.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(noConnectList.get(1).getName(), AlphaFineHelper.getNoConnectList(similarSearchManeger).get(1).getName());

        noConnectList = documentSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(noConnectList.get(1).getName(), AlphaFineHelper.getNoConnectList(documentSearchManager).get(1).getName());

        noConnectList = pluginSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(noConnectList.get(1).getName(), AlphaFineHelper.getNoConnectList(pluginSearchManager).get(1).getName());

        noConnectList = recommendSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(noConnectList.get(1).getName(), AlphaFineHelper.getNoConnectList(recommendSearchManager).get(1).getName());

        //正常搜索情况
        String[][] hotData = HotIssuesManager.getInstance().getHotIssues();
        SearchResult lessModelList;
        lessModelList = recentSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertNotNull(lessModelList);

        lessModelList = similarSearchManeger.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(lessModelList.get(1).getType(), CellType.ROBOT);

        lessModelList = complementAdviceManager.getAllSearchResult(new String[]{"数据集"});
        Assert.assertEquals(lessModelList.get(1).getType(), CellType.ROBOT);

        lessModelList = documentSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(lessModelList.get(1).getType(), CellType.DOCUMENT);

        lessModelList = pluginSearchManager.getLessSearchResult(new String[]{"数据集"});
        Assert.assertEquals(lessModelList.get(1).getType(), CellType.PLUGIN);

        //返回MoreSearchResult
        SearchResult moreModelList;
        moreModelList = similarSearchManeger.getMoreSearchResult("数据集");
        Assert.assertNotNull(moreModelList);

        moreModelList = documentSearchManager.getMoreSearchResult("数据集");
        Assert.assertNotNull(moreModelList);

        moreModelList = pluginSearchManager.getMoreSearchResult("数据集");
        Assert.assertNotNull(moreModelList);

        moreModelList = recommendSearchManager.getMoreSearchResult("数据集");
        Assert.assertNotNull(moreModelList);
    }

    @Test
    public void getModelFromCloudTest() {
        Assert.assertNotNull(SimilarSearchManeger.getModelFromCloud(new JSONObject()));
        Assert.assertNotNull(DocumentSearchManager.getModelFromCloud(new JSONObject()));
        Assert.assertNotNull(PluginSearchManager.getModelFromCloud(new JSONObject()));
    }

    @Test
    public void isNeedSegmentationTest() {
        Assert.assertEquals(segmentationManager.isNeedSegmentation("多维数据集"), true);
    }

    @Test
    public void startSegmentationTest() {
        String[] result = {"结果报表", "结果", "报表"};
        Assert.assertEquals(segmentationManager.startSegmentation("结果报表"), result);

    }

}
