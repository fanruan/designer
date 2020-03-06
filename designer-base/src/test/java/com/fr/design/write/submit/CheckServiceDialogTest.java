package com.fr.design.write.submit;

import com.fr.design.mainframe.DesignerContext;
import org.junit.Test;

/**
 * @author: Maksim
 * @Date: Created in 2020/3/5
 * @Description:
 */
public class CheckServiceDialogTest {

    @Test
    public void dialogTest(){
        CheckServiceDialog dialog = new CheckServiceDialog(DesignerContext.getDesignerFrame(), "无","2020-02-01","2020-03-01");
        dialog.setVisible(true);
    }

}