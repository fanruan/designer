package com.fr.design.data.tabledata.tabledatapane;

/**
 * Created with IntelliJ IDEA.
 * User: wikky
 * Date: 14-1-21
 * Time: 下午12:30
 * To change this template use File | Settings | File Templates.
 */
public class FileTableDataSmallPane extends FileTableDataPane{
    //wikky:文件数据集在服务器数据集下面的界面参数。
    private static final int SETPANELWIDTH = 265;
    private static final int WIDTH = 245;
    private static final int HEIGHT = 436;
    private static final int GAP = 13;
    public FileTableDataSmallPane(){
        super(SETPANELWIDTH,WIDTH,HEIGHT,GAP);
    }
}