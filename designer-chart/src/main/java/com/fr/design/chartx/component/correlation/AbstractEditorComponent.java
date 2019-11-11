package com.fr.design.chartx.component.correlation;

import java.awt.Component;

/**
 * Created by shine on 2019/6/10.
 */
public abstract class AbstractEditorComponent<T extends Component> implements FieldEditorComponentWrapper<T> {
    private String header;

    public AbstractEditorComponent(String header) {
        this.header = header;
    }

    @Override
    public String headerName() {
        return this.header;
    }
}
