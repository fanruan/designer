package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;

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
}
