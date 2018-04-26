package com.fr.design.write.submit;

import com.fr.data.ClassSubmitJob;
import com.fr.data.SubmitJob;


public class CustomSubmitJobPane extends CustomJobPane {
    

    @Override
    public SubmitJob updateBean() {
        ClassSubmitJob classSubmitJob = new ClassSubmitJob(this.classNameTextField.getText());
        classSubmitJob.setPropertyMap(this.objectProperiesPane.updateBean());
        checkAddButtonEnable();

        return classSubmitJob;
    }

}