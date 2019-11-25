/**
 *
 */
package com.fr.design.mainframe;

import com.fr.file.FILEChooserPane;
import com.fr.main.impl.WorkBook;
import com.fr.report.poly.PolyWorkSheet;

/**
 * 聚合报表Book, 跟WorkBook区别在于不能放入WorkSheet.
 *
 * @author neil
 *
 * @date: 2015-2-5-上午8:58:39
 */
public class JPolyWorkBook extends JWorkBook {

    private static final String DEFAULT_NAME = "Poly";

    /**
     * 构造函数
     */
    public JPolyWorkBook() {
        super(new WorkBook(new PolyWorkSheet()), DEFAULT_NAME);
        populateReportParameterAttr();
    }

    /**
     * 创建sheet名称tab面板
     *
     * @param reportCompositeX 当前组件对象
     *
     * @return sheet名称tab面板
     *
     * @date 2015-2-5-上午11:42:12
     *
     */
    @Override
    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportCompositeX){
        return new PolySheetNameTabPane(reportCompositeX);
    }

    @Override
    public void refreshEastPropertiesPane() {
        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.POLY);
    }

    protected void addExtraChooseFILEFilter(FILEChooserPane fileChooser) {

    }
}
