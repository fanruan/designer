package com.fr.design.ui.report;

import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ParserType;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.fr.web.struct.impl.FineUI;

/**
 * Created by windy on 2019/3/26.
 * 模板Web属性demo使用
 */
public class TemplateWebSettingComponent extends AssembleComponent {

    public static final TemplateWebSettingComponent KEY = new TemplateWebSettingComponent();

    private TemplateWebSettingComponent() {

    }

    @Override
    public Atom[] refer() {
        return new Atom[] {
                FineUI.KEY
        };
    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("/com/fr/design/ui/script/template.js");
    }

    @Override
    public StylePath style() {

        return StylePath.build("/com/fr/design/ui/script/template.css", ParserType.DYNAMIC);
    }
}
