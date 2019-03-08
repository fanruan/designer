package com.fr.design.ui;

import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.impl.FineUI;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-08
 */
public class StartComponent extends AssembleComponent {

    public static final StartComponent KEY = new StartComponent();

    private StartComponent() {

    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("/com/fr/design/ui/script/start.js");
    }

    @Override
    public Atom[] refer() {
        return new Atom[] {FineUI.KEY};
    }

}
