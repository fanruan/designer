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
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.json.JSONTokener;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by XiaXiang on 2017/3/31.
 */
public class RecommendSearchManager implements AlphaFineSearchProvider {
    private static RecommendSearchManager instance;
    private SearchResult modelList;
    private SearchResult recommendModelList;

    private SearchResult complementAdviceModelList;
    private SearchResult moreModelList = new SearchResult();

    public static RecommendSearchManager getInstance() {
        if (instance == null) {
            synchronized (RecentSearchManager.class) {
                if (instance == null) {
                    instance = new RecommendSearchManager();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String[] searchText) {

        if (ArrayUtils.isEmpty(searchText)) {
            return new SearchResult();
        }

        this.modelList = new SearchResult();
        this.recommendModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainRecommend()) {
            for (int j = 0; j < searchText.length; j++) {
                searchText[j] = searchText[j].replaceAll(StringUtils.BLANK, StringUtils.EMPTY);
                try {
                    String result = HttpToolbox.get(AlphaFineConstants.SEARCH_API + CodeUtils.cjkEncode(searchText[j]));
                    AlphaFineHelper.checkCancel();
                    Object json = new JSONTokener(result).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optString("status").equals("success")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("result");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    AlphaFineHelper.checkCancel();
                                    AlphaCellModel alphaCellModel = CellModelHelper.getModelFromJson((JSONObject) jsonArray.get(i));
                                    if (alphaCellModel != null && !alreadyContain(alphaCellModel)) {

                                        this.recommendModelList.add(alphaCellModel);
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    FineLoggerFactory.getLogger().error("recommend search error! :" + e.getMessage());
                } catch (IOException e) {
                    FineLoggerFactory.getLogger().error("recommend search get result error! :" + e.getMessage());
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
            if (recommendModelList.size() > 0) {
                if (complementAdviceModelList.size() == 0) {
                    if (recommendModelList.size() > AlphaFineConstants.SHOW_SIZE - 2) {
                        if (recommendModelList.size() > AlphaFineConstants.SHOW_SIZE) {
                            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
                        } else {
                            modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
                        }
                        modelList.addAll(recommendModelList.subList(0, AlphaFineConstants.SHOW_SIZE - 2));
                        moreModelList.addAll(recommendModelList.subList(AlphaFineConstants.SHOW_SIZE - 2, recommendModelList.size()));
                    } else {
                        modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
                        modelList.addAll(recommendModelList);
                    }
                } else {
                    if (recommendModelList.size() + complementAdviceModelList.size() > AlphaFineConstants.SHOW_SIZE) {
                        modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
                        if (recommendModelList.size() > AlphaFineConstants.SHOW_SIZE - 2) {
                            modelList.addAll(recommendModelList.subList(0, AlphaFineConstants.SHOW_SIZE - 2));
                            moreModelList.addAll(recommendModelList.subList(AlphaFineConstants.SHOW_SIZE - 2, recommendModelList.size()));

                            if (complementAdviceModelList.size() >= 2) {
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
            } else {
                if (complementAdviceModelList.size() > 0) {
                    if (complementAdviceModelList.size() > AlphaFineConstants.SHOW_SIZE) {
                        modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), true, CellType.RECOMMEND));
                        modelList.addAll(complementAdviceModelList.subList(0, AlphaFineConstants.SHOW_SIZE));
                        moreModelList.addAll(complementAdviceModelList.subList(AlphaFineConstants.SHOW_SIZE, complementAdviceModelList.size()));
                    } else {
                        modelList.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend"), false));
                        modelList.addAll(complementAdviceModelList);
                    }
                } else {
                    return modelList;
                }
            }
        }
        return modelList;
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

    private SearchResult getNoConnectList() {
        SearchResult result = new SearchResult();
        result.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend")));
        result.add(AlphaFineHelper.NO_CONNECTION_MODEL);
        return result;
    }


    @Override
    public SearchResult getMoreSearchResult(String searchText) {
        return moreModelList;
    }

    public List<AlphaCellModel> getRecommendModelList() {
        return recommendModelList;
    }

}
