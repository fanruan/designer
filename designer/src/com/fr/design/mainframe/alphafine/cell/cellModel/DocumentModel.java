package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class DocumentModel extends AlphaCellModel {
    private String documentUrl;

    public DocumentModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    public DocumentModel(String name, String content, String documentUrl) {
        super(name, content, CellType.DOCUMENT);
        this.documentUrl = documentUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    @Override
    public JSONObject ModelToJson() {
        JSONObject object = JSONObject.create();
        try {
            object.put("name", getName()).put("content", getContent()).put("documentUrl", getDocumentUrl()).put("cellType", getType().getCellType());
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentModel)) return false;

        DocumentModel that = (DocumentModel) o;

        return documentUrl != null ? documentUrl.equals(that.documentUrl) : that.documentUrl == null;
    }

    @Override
    public int hashCode() {
        return documentUrl != null ? documentUrl.hashCode() : 0;
    }

    public static DocumentModel jsonToModel(JSONObject object) {
        String name = object.optString("title");
        String content = object.optString("summary");
        String documentUrl = AlphaFineConstants.DOCUMENT_DOC_URL + object.optString("did") + ".html";
        return new DocumentModel(name, content, documentUrl);
    }
}
