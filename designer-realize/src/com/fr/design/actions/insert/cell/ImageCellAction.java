/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.core.WorkBookSupportable;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Image
 */
public class ImageCellAction extends AbstractCellAction implements WorkBookSupportable {
    public ImageCellAction() {
        initAction();
    }

    public ImageCellAction(ElementCasePane t) {
        super(t);
        initAction();
    }

    private void initAction() {
        this.setMenuKeySet(INSERT_IMAGE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
    }

    public static final MenuKeySet INSERT_IMAGE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'I';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Insert-Image");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    @Override
    public Class getCellValueClass() {
        return Image.class;
    }
}