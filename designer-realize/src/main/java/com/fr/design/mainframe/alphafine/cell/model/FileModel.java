package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.log.FineLoggerFactory;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class FileModel extends AlphaCellModel {
    private String filePath;

    public FileModel(String name, String filePath, int searchCount) {
        this(name, filePath);
        setSearchCount(searchCount);
    }

    public FileModel(String name, String filePath) {
        super(name, null, CellType.FILE);
        this.filePath = filePath.replaceAll("\\\\", "/");
        setDescription(AlphaFineHelper.findFolderName(this.filePath));
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public JSONObject modelToJson() {
        JSONObject object = JSONObject.create();
        try {
            JSONObject modelObject = JSONObject.create();
            modelObject.put("filePath", getFilePath()).put("searchCount", getSearchCount());
            object.put("result", modelObject).put("cellType", getType().getTypeValue());
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
        return object;
    }

    @Override
    public String getStoreInformation() {
        return getFilePath();
    }

    @Override
    public void doAction() {
        DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(getFilePath(), false)));
    }

    @Override
    public boolean isNeedToSendToServer() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileModel)) {
            return false;
        }
        FileModel fileModel = (FileModel) o;
        return filePath != null ? filePath.equals(fileModel.filePath) : fileModel.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
