package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.PageToSheetExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.report.core.ReportUtils;

public class PageToSheetExcelExportAction extends AbstractExcelExportAction {

	public PageToSheetExcelExportAction(JWorkBook jwb) {
		super(jwb);
		
        this.setMenuKeySet(KeySetUtils.PAGETOSHEET_EXCEL_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png"));
    }

    @Override
	protected Exporter getExporter() {
    	TemplateWorkBook tpl = this.getTemplateWorkBook();
        return new PageToSheetExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(tpl));
    }
    
    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"xls"}, Inter.getLocText("Export-Excel"));
    }

    @Override
	protected String getDefaultExtension() {
        return "xls";
    }
}