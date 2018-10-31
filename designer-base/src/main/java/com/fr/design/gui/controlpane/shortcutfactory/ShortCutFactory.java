package com.fr.design.gui.controlpane.shortcutfactory;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.controlpane.ShortCutListenerProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.menu.LineSeparator;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.ComparatorUtils;
import com.fr.js.JavaScript;
import com.fr.stable.Filter;
import com.fr.stable.StringUtils;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import java.awt.event.ActionEvent;

/**
 * Created by plough on 2018/8/13.
 */
public class ShortCutFactory extends AbstractShortCutFactory {

    protected ShortCutFactory(ShortCutListenerProvider listenerProvider) {
        super(listenerProvider);
    }

    public static ShortCutFactory newInstance(ShortCutListenerProvider listenerProvider) {
        return new ShortCutFactory(listenerProvider);
    }

    @Override
    public ShortCut4JControlPane[] createShortCuts() {
        return new ShortCut4JControlPane[]{
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut(),
                removeItemShortCut()
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        /**
         * Gets component on toolbar.
         *
         * @return the created components on toolbar.
         */
        @Override
        public JComponent createToolBarComponent() {
            Object object = this.getValue(UIButton.class.getName());
            if (!(object instanceof AbstractButton)) {
                // 直接使用默认UI
                UIButton button = new UIButton();
                // 添加一个名字作为自动化测试用
                button.setName(getName());

                //设置属性.
                Integer mnemonicInteger = (Integer) this.getValue(Action.MNEMONIC_KEY);
                if (mnemonicInteger != null) {
                    button.setMnemonic((char) mnemonicInteger.intValue());
                }

                button.setIcon((Icon) this.getValue(Action.SMALL_ICON));
                button.addActionListener(this);

                button.registerKeyboardAction(this, this.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);

                this.putValue(UIButton.class.getName(), button);
                button.setText(StringUtils.EMPTY);
                button.setEnabled(this.isEnabled());

                //peter:产生tooltip
                button.setToolTipText(ActionFactory.createButtonToolTipText(this));
                object = button;
            }

            return (JComponent) object;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            listener.onAddItem(creator);
        }
    }

    /*
     * 增加项的MenuDef
     */
    protected class AddItemMenuDef extends MenuDef {
        public AddItemMenuDef(NameableCreator[] creators) {
            super(true);
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Add"));
            this.setMnemonic('A');
            this.setIconPath("/com/fr/design/images/control/addPopup.png");
            wrapActionListener(creators);
        }

        /**
         * 生成UIButton
         *
         * @return 菜单按钮
         */
        public UIButton createUIButton() {
            createdButton = super.createUIButton();
            // 此按钮单独抽出，不应使用工具栏外观
            if (!createdButton.isOpaque()) {
                createdButton.setOpaque(true);
                createdButton.setNormalPainted(true);
                createdButton.setBorderPaintedOnlyWhenPressed(false);
            }
            return createdButton;
        }

        private void wrapActionListener(NameableCreator[] creators) {
            for (final NameableCreator creator : creators) {
                Filter<Class<? extends JavaScript>> filter = DesignModuleFactory.getHyperlinkGroupType().getFilter();
                if (!filter.accept(creator.getHyperlink())) {
                    continue;
                }
                boolean isTrue = ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Datasource-Stored_Procedure")) ||
                        ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Relation_TableData")) || ComparatorUtils.equals(creator.menuName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Multi_Dimensional_Database"));
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
