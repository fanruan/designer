package com.fr.design.mainframe.alphafine.search.manager;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.ActionModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.toolbar.UpdateActionManager;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

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
    public synchronized SearchResult getLessSearchResult(String searchText) {
        filterModelList = new SearchResult();
        lessModelList = new SearchResult();
        moreModelList = new SearchResult();
        if (DesignerEnvManager.getEnvManager().getAlphafineConfigManager().isContainAction()) {
            List<UpdateAction> updateActions = UpdateActionManager.getUpdateActionManager().getUpdateActions();
            for (UpdateAction updateAction : updateActions) {
                if (updateAction.getName() != null && updateAction.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filterModelList.add(new ActionModel(updateAction.getName() ,updateAction));
                }
            }
            if (filterModelList != null && filterModelList.size() >0) {
                final int length = Math.min(AlphaFineConstants.SHOW_SIZE, filterModelList.size());
                for (int i = 0; i < length; i++) {
                    lessModelList.add(filterModelList.get(i));
                }
                for (int i = length; i < filterModelList.size(); i++) {
                    moreModelList.add(filterModelList.get(i));
                }
                if (filterModelList.size() > 0) {
                    if (filterModelList.size() > AlphaFineConstants.SHOW_SIZE) {
                        lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Set"), Inter.getLocText("FR-Designer_AlphaFine_ShowAll"),true, CellType.ACTION));
                    } else {
                        lessModelList.add(0, new MoreModel(Inter.getLocText("FR-Designer_Set"), CellType.ACTION));
                    }
                }
            }

        }
        return lessModelList;
    }

    @Override
    public SearchResult getMoreSearchResult() {
        return moreModelList;
    }

    /**
     * 根据类名反射获取对象
     * @param actionName
     * @return
     */
    public static ActionModel getModelFromCloud(String actionName ) {
        UpdateAction action = null;
        String name = null;
        try {
            Class<?> className =  Class.forName(actionName);
            action = (UpdateAction) className.newInstance();
            name = action.getName();
        } catch (ClassNotFoundException e) {
            FRLogger.getLogger().error(e.getMessage());
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error(e.getMessage());
        } catch (InstantiationException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new ActionModel(name, action);
    }
}
