package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;

import java.io.Serializable;

/**
 * Created by XiaXiang on 2017/4/27.
 */
public interface ModelHandle extends Serializable {
    CellType getType();
}
