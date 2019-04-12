package com.fr.design.upm;

import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.impl.FineUI;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 */
public class WarnComponent extends AssembleComponent {

    public static final WarnComponent KEY = new WarnComponent();

    private WarnComponent() {

    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("com/fr/design/upm/warn.js");
    }

    @Override
    public Atom[] refer() {
        return new Atom[]{
                FineUI.KEY
        };
    }
}
