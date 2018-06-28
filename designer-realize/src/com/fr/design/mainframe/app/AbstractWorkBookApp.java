package com.fr.design.mainframe.app;

import com.fr.design.mainframe.App;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JWorkBook;
import com.fr.file.FILE;
import com.fr.main.impl.WorkBook;

/**
 * Created by juhaoyu on 2018/6/27.
 */
abstract class AbstractWorkBookApp implements App<WorkBook> {
    
    
    @Override
    public int currentAPILevel() {
        
        return CURRENT_LEVEL;
    }
    
    @Override
    public JTemplate<WorkBook, ?> openTemplate(FILE tplFile) {
        
        return new JWorkBook(asIOFile(tplFile), tplFile);
    }
    
    @Override
    public String mark4Provider() {
        
        return getClass().getName();
    }
    
    @Override
    public void process() {
    
    }
    
    @Override
    public void undo() {
    
    }
}