package com.fr.design.upm.database;

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
public class UniverseDatabaseComponent extends AssembleComponent {

    public static final UniverseDatabaseComponent KEY = new UniverseDatabaseComponent();

    private UniverseDatabaseComponent() {

    }

    @Override
    public ScriptPath script(RequestClient req) {
        return ScriptPath.build("/com/fr/design/upm/database/database.js");
    }

    @Override
    public StylePath style(RequestClient req) {
        return StylePath.EMPTY;
    }

    @Override
    public Atom[] refer() {
        return new Atom[]{
                FineUI.KEY
        };
    }
}
