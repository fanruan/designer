package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.AssistUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StringUtils;
import com.fr.third.org.apache.commons.codec.digest.DigestUtils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

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
        String token = DigestUtils.md5Hex(AlphaFineConstants.ALPHA_ROBOT_SEARCH_TOKEN + titleStr);
        String url = AlphaFineConstants.ALPHA_GO_TO_WEB + titleStr + "&token=" + token;

        try {
            String result = HttpToolbox.get(url);
            if(StringUtils.isEmpty(result)){
                return StringUtils.EMPTY;
            }
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.optString("msg");
        } catch (Exception e) {
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
        JSONObject modelObject = JSONObject.create();
        modelObject.put("title", getTitle()).put("content", getContent()).put("searchCount", getSearchCount());
        object.put("result", modelObject).put("cellType", getType().getTypeValue());
        return object;
    }

    @Override
    public String getStoreInformation() {
        return null;
    }

    @Override
    public void doAction() {
        try {
            Desktop.getDesktop().browse(new URI(AlphaFineConstants.ALPHA_PREVIEW + URLEncoder.encode(super.getName(), EncodeConstants.ENCODING_UTF_8)));
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
