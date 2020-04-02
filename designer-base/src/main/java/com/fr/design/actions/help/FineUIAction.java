package com.fr.design.actions.help;

import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.ui.ModernUIPane;
import com.fr.locale.InterProviderFactory;
import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.impl.FineUI;

import java.awt.event.ActionEvent;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-08
 */
public class FineUIAction extends UpdateAction {

    public FineUIAction() {
        setName("FineUI");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        ModernUIPane<?> pane = new ModernUIPane.Builder<>()
//                .prepare(new ScriptContextAdapter() {
//                    @Override
//                    public void onScriptContextCreated(ScriptContextEvent event) {
//                        JSValue pool = event.getBrowser().executeJavaScriptAndReturnValue("window.Pool");
//                        pool.asObject().setProperty("i18n", new I18n());
//                    }
//                })
                .withComponent(new AssembleComponent() {

                    @Override
                    public ScriptPath script(RequestClient req) {
                        return ScriptPath.build("/com/fr/design/ui/help/demo.js");
                    }

                    @Override
                    public Atom[] refer() {
                        return new Atom[] {FineUI.KEY};
                    }
                })
                .build();
       BasicDialog dialog = pane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                // Do nothing
            }
        });
       dialog.setVisible(true);

    }

    public static class I18n {

        public String i18nText(String key) {
            return InterProviderFactory.getProvider().getLocText(key);
        }
    }
}
