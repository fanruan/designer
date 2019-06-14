package com.fr.start.module;

import com.fr.module.Activator;
import com.fr.start.DesignerInitial;

/**
 * Created by juhaoyu on 2019-06-14.
 */
public class DesignerShowActivator extends Activator {
    
    @Override
    public void start() {
        
        DesignerInitial.show();
    }
    
    @Override
    public void stop() {
    
    }
}
