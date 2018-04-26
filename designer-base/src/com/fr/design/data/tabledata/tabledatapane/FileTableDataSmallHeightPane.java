package com.fr.design.data.tabledata.tabledatapane;

/**
 * Created with IntelliJ IDEA.
 * User: wikky
 * Date: 14-2-12
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class FileTableDataSmallHeightPane extends FileTableDataPane{
    //wikky:文件数据集在模板数据集下面的界面参数。
    private static final int SETPANELWIDTH = 265;
    private static final int WIDTH = 245;
    private static final int HEIGHT = 475;
    private static final int GAP = 13;
    public FileTableDataSmallHeightPane(){
        super(SETPANELWIDTH,WIDTH,HEIGHT,GAP);
    }
}