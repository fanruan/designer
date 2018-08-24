package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.AssistUtils;
import org.apache.commons.codec.digest.DigestUtils;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by alex.sung on 2018/8/3.
 */
public class RobotModel extends AlphaCellModel {

    private String title;
    private String content;

    //热门问题列表的list不需要渲染图标，所以这里需要区分一下
    private boolean hotItemModel = false;

    public boolean isHotItemModel() {
        return hotItemModel;
    }

    public void setHotItemModel(boolean hotItemModel) {
        this.hotItemModel = hotItemModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getContent(String titleStr) {
        String result;
        String token = DigestUtils.md5Hex(AlphaFineConstants.ALPHA_ROBOT_SEARCH_TOKEN + titleStr);
        String url = AlphaFineConstants.ALPHA_GO_TO_WEB + titleStr + "&token=" + token;
        HttpClient httpClient = new HttpClient(url);
        httpClient.asGet();
        result = httpClient.getResponseText();
        AlphaFineHelper.checkCancel();
        try {
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.optString("msg");
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error("get robotmodel content error: " + e.getMessage());
        }
        return null;
    }

    public RobotModel(String title, String content) {
        super(title, content, CellType.ROBOT);
        this.title = title;
    }

    @Override
    public JSONObject modelToJson() throws JSONException {
        JSONObject object = JSONObject.create();
        try {
            JSONObject modelObject = JSONObject.create();
            modelObject.put("title", getTitle()).put("content", getContent()).put("searchCount", getSearchCount());
            object.put("result", modelObject).put("cellType", getType().getTypeValue());
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error("RobotModel: " + e.getMessage());
        }
        return object;
    }

    @Override
    public String getStoreInformation() {
        return null;
    }

    @Override
    public void doAction() {
        try {
            Desktop.getDesktop().browse(new URI("http://robot.finereport.com?send=" + super.getName()));
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        } catch (URISyntaxException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof RobotModel
                && AssistUtils.equals(this.title, ((RobotModel)obj).title)
                && AssistUtils.equals(this.content, ((RobotModel) obj).content);
    }

    @Override
    public int hashCode() {
        return AssistUtils.hashCode(title, content);
    }
}
