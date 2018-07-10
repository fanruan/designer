package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.log.FineLoggerFactory;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class DocumentModel extends AlphaCellModel {
    private String documentUrl;
    private String informationUrl;
    private int documentId;

    public DocumentModel(String name, String content, int documentId) {
        super(name, content, CellType.DOCUMENT);
        this.documentId = documentId;
        this.informationUrl = AlphaFineConstants.DOCUMENT_INFORMATION_URL + documentId;
        this.documentUrl = AlphaFineConstants.DOCUMENT_DOC_URL + documentId + ".html";
    }

    public DocumentModel(String name, String content, int documentId, int searchCount) {
        this(name, content, documentId);
        setSearchCount(searchCount);
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
            JSONObject modelObject = JSONObject.create();
            modelObject.put("title", getName()).put("summary", getContent()).put("did", getDocumentId()).put("searchCount", getSearchCount());
            object.put("result", modelObject).put("cellType", getType().getTypeValue());
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error("DocumentModel: " + e.getMessage());
        }
        return object;
    }

    @Override
    public String getStoreInformation() {
        return getInformationUrl();
    }

    @Override
    public void doAction() {
        try {
            Desktop.getDesktop().browse(new URI(getDocumentUrl()));
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        } catch (URISyntaxException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentModel)) {
            return false;
        }
        DocumentModel that = (DocumentModel) o;

        return documentUrl != null ? documentUrl.equals(that.documentUrl) : that.documentUrl == null;
    }

    @Override
    public int hashCode() {
        return documentUrl != null ? documentUrl.hashCode() : 0;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }
}
