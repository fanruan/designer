package com.fr.design.mainframe.app;

import com.fr.base.extension.FileExtension;
import com.fr.file.FILE;
import com.fr.io.importer.Excel2007ReportImporter;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class XlsxApp extends AbstractWorkBookApp {
    
    @Override
    public String[] defaultExtensions() {
        
        return new String[]{FileExtension.XLSX.getExtension()};
    }
    
    @Override
    public WorkBook asIOFile(FILE tplFile) {
        
        WorkBook workbook = null;
        try {
            workbook = new Excel2007ReportImporter().generateWorkBookByStream(tplFile.asInputStream());
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error("Failed to generate xlsx from " + tplFile, exp);
        }
        return workbook;
    }
}
