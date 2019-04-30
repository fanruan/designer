package com.fr.design.ui.report;

import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ParserType;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.fr.web.struct.impl.FineUI;

/**
 * Created by windy on 2019/3/25.
 * 报表服务器参数demo使用
 */
public class ReportServerParamComponent extends AssembleComponent {

    public static final ReportServerParamComponent KEY = new ReportServerParamComponent();

    private ReportServerParamComponent() {

    }

    @Override
    public Atom[] refer() {
        return new Atom[] {
                FineUI.KEY
        };
    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("/com/fr/design/ui/script/report.js");
    }

    @Override
    public StylePath style() {

        return StylePath.build("/com/fr/design/ui/script/report.css", ParserType.DYNAMIC);
    }
}
