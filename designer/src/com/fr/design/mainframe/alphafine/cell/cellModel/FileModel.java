package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class FileModel extends AlphaCellModel{
    private String filePath;

    public FileModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    public FileModel(String name, String content, String filePath) {
        super(name, content, CellType.FILE);
        this.filePath = filePath;
        setDescription(AlphaFineHelper.findFolderName(content));
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
