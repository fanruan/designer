package com.fr.design.mainframe.alphafine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.NoResultModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.search.manager.impl.DocumentSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.HotIssuesManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.PluginSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.RecentSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.RecommendSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.SimilarSearchManager;
import com.fr.general.ProcessCanceledException;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.json.JSONUtils;
import com.fr.stable.StringUtils;

import java.util.List;

/**
 * Created by XiaXiang on 2017/5/8.
 */
public class AlphaFineHelper {
    public static final NoResultModel NO_CONNECTION_MODEL = new NoResultModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Connection_Failed"));
    private static AlphaFineDialog alphaFineDialog;
    private static final String URL_FOR_TEST_NETWORK = "https://www.baidu.com";

    /**
     * 弹出alphafine搜索面板
     */
    public static void showAlphaFineDialog(boolean forceOpen) {
        if (!AlphaFineConfigManager.isALPHALicAvailable()) {
            return;
        }
        if (alphaFineDialog == null) {
            alphaFineDialog = new AlphaFineDialog(DesignerContext.getDesignerFrame(), forceOpen);
            alphaFineDialog.setVisible(true);
            final AlphaFineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphaFineConfigManager();
            manager.setNeedRemind(false);
        } else {
            alphaFineDialog.setVisible(!alphaFineDialog.isVisible());
        }


    }


    /**
     * 获取文件名上级目录
     *
     * @param text
     * @return
     */
    public static String findFolderName(String text) {
        return getSplitText(text, 2);
    }

    /**
     * 分割字符串，获取文件名，文件名上级目录等
     *
     * @param text
     * @param index
     * @return
     */
    private static String getSplitText(String text, int index) {
        if (StringUtils.isNotBlank(text)) {
            String[] textArray = text.replaceAll("\\\\", "/").split("/");
            if (textArray != null && textArray.length > 1) {
                return textArray[textArray.length - index];
            }
        }
        return null;
    }

    /**
     * 获取文件名
     *
     * @param text
     * @return
     */
    public static String findFileName(String text) {
        return getSplitText(text, 1);
    }

    /**
     * 中断当前线程的搜索
     */
    public static void checkCancel() {
        if (Thread.interrupted()) {
            throw new ProcessCanceledException();
        }
    }

    public static List<AlphaCellModel> getFilterResult() {
        List<AlphaCellModel> recentList = RecentSearchManager.getInstance().getRecentModelList();
        List<AlphaCellModel> recommendList = RecommendSearchManager.getInstance().getRecommendModelList();
        SearchResult filterResult = new SearchResult();
        filterResult.addAll(recentList);
        filterResult.addAll(recommendList);
        return filterResult;
    }

    public static SearchResult getModelListFromJSONArray(String result, String keyword) throws ClassCastException, JSONException {
        SearchResult allModelList = new SearchResult();
        JSONArray jsonArray = (JSONArray) JSONUtils.jsonDecode(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            AlphaFineHelper.checkCancel();
            JSONObject jsonObject = jsonArray.optJSONObject(i);

            String temp = jsonObject.optString(keyword);
            if (StringUtils.isNotEmpty(temp)) {
                RobotModel robotModel = new RobotModel(temp, null);
                if (!allModelList.contains(robotModel)) {
                    allModelList.add(robotModel);
                }
            }
        }
        return allModelList;
    }

    /**
     * 网络异常时的处理
     * @param object
     * @return
     */
    public static SearchResult getNoConnectList(Object object) {
        if (isNetworkOk()){
            return null;
        }
        SearchResult result = new SearchResult();
        if (object instanceof RecommendSearchManager) {
            result.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Recommend")));
        } else if (object instanceof DocumentSearchManager) {
            result.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help")));
        } else if (object instanceof PluginSearchManager) {
            result.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon")));
        } else if (object instanceof SimilarSearchManager) {
            result.add(0, new MoreModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Relation_Item")));
        } else if (object instanceof HotIssuesManager) {
            return new SearchResult();
        }
        result.add(AlphaFineHelper.NO_CONNECTION_MODEL);
        return result;
    }

    /**
     * 判断网络是否异常
     * @return
     */
    public static boolean isNetworkOk(){
        try {
            HttpToolbox.get(URL_FOR_TEST_NETWORK);
            return true;
        } catch (Exception ignore) {
            // 网络异常
            return false;
        }
    }
}
