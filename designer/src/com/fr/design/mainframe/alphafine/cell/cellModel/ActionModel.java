package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ActionModel extends AlphaCellModel implements Serializable {
    private Action action;

    public ActionModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    public ActionModel(String name, Action action) {
        super(name, null, CellType.ACTION);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
