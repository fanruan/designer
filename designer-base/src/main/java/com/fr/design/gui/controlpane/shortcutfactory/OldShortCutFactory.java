package com.fr.design.gui.controlpane.shortcutfactory;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.HyperlinkFilterHelper;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.controlpane.ShortCutListenerProvider;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.Icon;
import java.awt.event.ActionEvent;

/**
 * Created by plough on 2018/8/13.
 */
public class OldShortCutFactory extends AbstractShortCutFactory {

    private OldShortCutFactory(ShortCutListenerProvider listenerProvider) {
        super(listenerProvider);
    }

    public static OldShortCutFactory newInstance(ShortCutListenerProvider listenerProvider) {
        return new OldShortCutFactory(listenerProvider);
    }

    @Override
    public ShortCut4JControlPane[] createShortCuts() {
        return new ShortCut4JControlPane[]{
                addItemShortCut(),
                removeItemShortCut(),
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut()
        };
    }

    @Override
    public ShortCut createAddItemUpdateAction(NameableCreator[] creators) {
        return new AddItemUpdateAction(creators);
    }

    @Override
    public ShortCut createAddItemMenuDef(NameableCreator[] creators) {
        return new AddItemMenuDef(creators);
    }


    /**
     * 增加项的UpdateAction
     */
    protected class AddItemUpdateAction extends UpdateAction {
        final NameableCreator creator;

        public AddItemUpdateAction(NameableCreator[] creators) {
            this.creator = creators[0];
            this.setName(com.fr.design.i18n.Toolkit.i18nText(("Fine-Design_Basic_Action_Add")));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            listener.onAddItem(creator);
        }
    }

    /*
     * 增加项的MenuDef
     */
    private class AddItemMenuDef extends MenuDef {
        AddItemMenuDef(NameableCreator[] creators) {
            this.setName(com.fr.design.i18n.Toolkit.i18nText(("Fine-Design_Basic_Action_Add")));
            this.setMnemonic('A');
            this.setIconPath("/com/fr/design/images/control/addPopup.png");
            wrapActionListener(creators);
        }

        private void wrapActionListener(NameableCreator[] creators) {
            for (final NameableCreator creator : creators) {
                if (!HyperlinkFilterHelper.whetherAddHyperlink4cell(creator.menuName())) {
                    continue;
                }
                boolean isTrue = ComparatorUtils.equals(creator.menuName(), Inter.getLocText("Datasource-Stored_Procedure")) ||
                        ComparatorUtils.equals(creator.menuName(), Inter.getLocText("DS-Relation_TableData")) || ComparatorUtils.equals(creator.menuName(), Inter.getLocText("DS-Multi_Dimensional_Database"));
                if (isTrue) {
                    this.addShortCut(new LineSeparator());
                }
                this.addShortCut(new UpdateAction() {
                    {
                        this.setName(creator.menuName());
                        Icon icon = creator.menuIcon();
                        if (icon != null) {
                            this.setSmallIcon(icon);
                        }
                    }

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listener.onAddItem(creator);
                    }
                });
            }
        }
    }

}
