package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.toolbar.UpdateActionManager;
import com.fr.design.mainframe.toolbar.UpdateActionModel;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

import java.util.List;

/**
 * Created by XiaXiang on 2017/3/27.
 */
public class ActionSearchManager implements AlphaFineSearchProcessor {
    private static ActionSearchManager actionSearchManager = null;
    private SearchResult filterModelList;
    private SearchResult lessModelList;
    private SearchResult moreModelList;

    public synchronized static ActionSearchManager getActionSearchManager() {
        if (actionSearchManager == null) {
            actionSearchManager = new ActionSearchManager();
        }
        return actionSearchManager;
    }

    /**
     * 根据类名获取对象
     *
     * @param object
     * @return
     */
    public static ActionModel getModelFromCloud(JSONObject object) {
        String actionName = object.optString("className");
        int searchCount = object.optInt("searchCount");
        List<UpdateActionModel> updateActions = UpdateActionManager.getUpdateActionManager().getUpdateActions();
        for (UpdateActionModel updateActionModel : updateActions) {
            if (ComparatorUtils.equals(actionName, updateActionModel.getClassName())) {
                return new ActionModel(updateActionModel.getActionName(), updateActionModel.getParentName(), updateActionModel.getAction(), searchCount);
            }
        }
        return null;
    }

    @Override
    public synchronized SearchResult getLessSearchResult(String searchText) {
        filterModelList = new SearchResult();
        lessModelList = new SearchResult();
        moreModelList = new SearchResult();
        if (StringUtils.isBlank(searchText)) {
            lessModelList.add(new MoreModel(Inter.getLocText("FR-Designer_Set")));
            return lessModelList;
        }
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isContainAction()) {
            List<UpdateActionModel> updateActions = UpdateActionManager.getUpdateActionManager().getUpdateActions();
            for (UpdateActionModel updateActionModel : updateActions) {
                if (StringUtils.isNotBlank(updateActionModel.getSearchKey())) {
                    if (updateActionModel.getSearchKey().contains(searchText) && updateActionModel.getAction().isEnabled()) {
                        filterModelList.add(new ActionModel(updateActionModel.getActionName(), updateActionModel.getParentName(), updateActionModel.getAction()));
                    }
                }
            }
            SearchResult result = new SearchResult();
            for (AlphaCellModel object : filterModelList) {
                if (!AlphaFineHelper.getFilterResult().contains(object)) {
                    result.add(object);
                }
            }
            if (result.isEmpty()) {
                return lessModelList;
            } else if (result.size() < AlphaFineConstants.SHOW_SIZE + 1) {
                lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Set")));
                lessModelList.addAll(result);
            } else {
                lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Set"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"), true, CellType.ACTION));
                lessModelList.addAll(result.subList(0, AlphaFineConstants.SHOW_SIZE));
                moreModelList.addAll(result.subList(AlphaFineConstants.SHOW_SIZE, result.size()));
            }

        }
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return moreModelList;
    }
}