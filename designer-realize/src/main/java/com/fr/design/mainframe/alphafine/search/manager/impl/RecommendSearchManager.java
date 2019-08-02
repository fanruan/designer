package com.fr.design.mainframe.alphafine.search.manager.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.fun.AlphaFineSearchProvider;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class RecommendSearchManager implements AlphaFineSearchProvider {
    private SearchResult modelList = new SearchResult();
    private SearchResult recommendModelList = new SearchResult();

    private SearchResult complementAdviceModelList;
    private SearchResult moreModelList = new SearchResult();

    private RecommendSearchManager() {

    }

    public static RecommendSearchManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RecommendSearchManager INSTANCE = new RecommendSearchManager();
    }

    @Override
    public SearchResult getLessSearchResult(String[] searchText) {
        this.modelList = new SearchResult();
        this.recommendModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainRecommend()) {
            if (ArrayUtils.isEmpty(searchText)) {
                return new SearchResult();
            }
            SearchResult noConnectList = AlphaFineHelper.getNoConnectList(Holder.INSTANCE);
            if (noConnectList != null) {
                return noConnectList;
            }
            for (int j = 0; j < searchText.length; j++) {
                searchText[j] = searchText[j].replaceAll(StringUtils.BLANK, StringUtils.EMPTY);
                try {
                    String url = AlphaFineConstants.SEARCH_API + CodeUtils.cjkEncode(searchText[j]);
                    String result = HttpToolbox.get(url);
                    AlphaFineHelper.checkCancel();
                    JSONObject jsonObject = new JSONObject(result);
                    if ("success".equals(jsonObject.optString("status"))) {
                        JSONArray jsonArray = jsonObject.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                AlphaFineHelper.checkCancel();
                                AlphaCellModel alphaCellModel = CellModelHelper.getModelFromJson((JSONObject) jsonArray.get(i));
                                if (alphaCellModel != null && !alreadyContain(alphaCellModel) && !this.recommendModelList.contains(alphaCellModel)) {
                                    this.recommendModelList.add(alphaCellModel);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().debug("recommend search get result error! search str {}", searchText[j]);
                }
            }

            Iterator<AlphaCellModel> modelIterator = recommendModelList.iterator();
            while (modelIterator.hasNext()) {
                AlphaCellModel model = modelIterator.next();
                if (model.getType() == CellType.ACTION && !((ActionModel) model).getAction().isEnabled()) {
                    modelIterator.remove();
                }
            }
            complementAdviceModelList = ComplementAdviceManager.getInstance().getAllSearchResult(searchText);
            moreModelList.clear();

            if (!recommendModelList.isEmpty()) {
                if (complementAdviceModelList.isEmpty()) {
                    getRecommendSearchResult();
                } else {
                    getRecommendAndAdviceSearchResult();
                }
            } else {
                if (!complementAdviceModelList.isEmpty()) {
                    getComplementAdviceSearchResult();
                } else {
                    return modelList;
                }
            }
        }
        return modelList;
    }

    /**
     * 将推荐接口获取的数据分别放入“显示部分”，“显示更多”的list
     */
    private void getRecommendSearchResult() {
        if (recommendModelList.size() > AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM) {
            if (recommendModelList.size() > AlphaFineConstants.SHOW_SIZE) {
                modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
            } else {
                modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
            }
            modelList.addAll(recommendModelList.subList(0, AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM));
            moreModelList.addAll(recommendModelList.subList(AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM, recommendModelList.size()));
        } else {
            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
            modelList.addAll(recommendModelList);
        }
    }

    /**
     * 将补全接口获取的数据分别放入“显示部分”，“显示更多”的list
     */
    private void getComplementAdviceSearchResult() {
        if (complementAdviceModelList.size() > AlphaFineConstants.SHOW_SIZE) {
            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
            modelList.addAll(complementAdviceModelList.subList(0, AlphaFineConstants.SHOW_SIZE));
            moreModelList.addAll(complementAdviceModelList.subList(AlphaFineConstants.SHOW_SIZE, complementAdviceModelList.size()));
        } else {
            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
            modelList.addAll(complementAdviceModelList);
        }
    }

    /**
     * 将推荐接口和补全接口获取的数据分别放入“显示部分”，“显示更多”的list
     */
    private void getRecommendAndAdviceSearchResult() {
        if (recommendModelList.size() + complementAdviceModelList.size() > AlphaFineConstants.SHOW_SIZE) {
            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
            if (recommendModelList.size() > AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM) {
                modelList.addAll(recommendModelList.subList(0, AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM));
                moreModelList.addAll(recommendModelList.subList(AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM, recommendModelList.size()));

                if (complementAdviceModelList.size() >= AlphaFineConstants.SHOW_SIZE - AlphaFineConstants.RECOMMEND_MAX_ITEM_NUM) {
                    modelList.addAll(complementAdviceModelList.subList(0, 2));
                    moreModelList.addAll(complementAdviceModelList.subList(2, complementAdviceModelList.size()));
                } else {
                    modelList.addAll(complementAdviceModelList);
                }
            } else {
                modelList.addAll(recommendModelList);
                if (complementAdviceModelList.size() >= (AlphaFineConstants.SHOW_SIZE - recommendModelList.size())) {
                    modelList.addAll(complementAdviceModelList.subList(0, AlphaFineConstants.SHOW_SIZE - recommendModelList.size()));
                    moreModelList.addAll(complementAdviceModelList.subList(2, complementAdviceModelList.size()));
                } else {
                    modelList.addAll(complementAdviceModelList);
                }
            }
        } else {
            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
            modelList.addAll(recommendModelList);
            modelList.addAll(complementAdviceModelList);
        }
    }

    /**
     * 是否已包含该model
     *
     * @param cellModel
     * @return
     */
    private boolean alreadyContain(AlphaCellModel cellModel) {
        return RecentSearchManager.getInstance().getRecentModelList().contains(cellModel) || this.recommendModelList.contains(cellModel);
    }

    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return moreModelList;
    }

    public List<AlphaCellModel> getRecommendModelList() {
        SearchResult result = new SearchResult();
        result.addAll(recommendModelList);
        result.addAll(modelList);
        result.addAll(moreModelList);
        return result;
    }

}
