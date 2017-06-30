package com.fr.design.mainframe.alphafine.model;

import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;

import java.util.ArrayList;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class SearchResult extends ArrayList<AlphaCellModel> {
    private boolean needMore;


    public boolean isNeedMore() {
        return needMore;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }
}
