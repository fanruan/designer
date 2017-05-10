package com.fr.design.mainframe.alphafine.searchManager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.cellModel.MoreModel;
import com.fr.design.mainframe.alphafine.cell.cellModel.ActionModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.toolbar.UpdateActionManager;

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

    @Override
    public synchronized SearchResult showLessSearchResult(String searchText) {
        this.filterModelList = new SearchResult();
        this.lessModelList = new SearchResult();
        this.moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainAction()) {
            List<UpdateAction> updateActions = UpdateActionManager.getUpdateActionManager().getUpdateActions();
            for (UpdateAction updateAction : updateActions) {
                if (updateAction.getName() != null && updateAction.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filterModelList.add(new ActionModel(updateAction.getName() ,updateAction));
                }
            }
            final int length = Math.min(5, filterModelList.size());
            for (int i = 0; i < length; i++) {
                lessModelList.add(filterModelList.get(i));
            }
            for (int i = length; i < filterModelList.size(); i++) {
                moreModelList.add(filterModelList.get(i));
            }
            if (filterModelList.size() > 0) {
                if (filterModelList.size() > 5) {
                    lessModelList.add(0, new MoreModel("设置", "显示全部",true, CellType.ACTION));
                } else {
                    lessModelList.add(0, new MoreModel("设置", CellType.ACTION));
                }
            }
        }
        return lessModelList;
    }

    @Override
    public SearchResult showMoreSearchResult() {
        return moreModelList;
    }
}
