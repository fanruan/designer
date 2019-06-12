package com.fr.design.dcm;

import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.fr.web.struct.impl.FineUI;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-05-16
 */
public class UniversalDatabaseComponent extends AssembleComponent {

    public static final UniversalDatabaseComponent KEY = new UniversalDatabaseComponent();

    private UniversalDatabaseComponent() {

    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("/com/fr/design/dcm/index.js");
    }

    @Override
    public StylePath style(RequestClient req) {
        return StylePath.build("/com/fr/design/dcm/style.css");
    }

    @Override
    public Atom[] refer() {
        return new Atom[]{
                FineUI.KEY
        };
    }
}
