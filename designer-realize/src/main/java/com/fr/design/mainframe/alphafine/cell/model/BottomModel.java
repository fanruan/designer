package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by alexsung on 2018/7/30.
 */
public class BottomModel extends AlphaCellModel {
    /**
     * 找不到答案？去论坛提问
     */
    public String getGoToWeb() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Internet_Forum");
    }

    public BottomModel(String name, String content) {
        super(name, content, CellType.BOTTOM);
    }

    public BottomModel() {
        super(null, null, CellType.BOTTOM);
    }

    @Override
    public JSONObject ModelToJson() throws JSONException {
        return null;
    }

    @Override
    public String getStoreInformation() {
        return null;
    }

    @Override
    public boolean hasAction() {
        return true;
    }

    @Override
    public void doAction() {
        try {
            Desktop.getDesktop().browse(new URI("http://bbs.fanruan.com/post_newthread_ajax.php?action=newthread&fid=39"));
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        } catch (URISyntaxException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }
}
