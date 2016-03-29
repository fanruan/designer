package com.fr.design.gui.ipasswordfield;


import javax.swing.*;
import javax.swing.text.Document;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-7-22
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class UIPassWordField extends JPasswordField {

    public UIPassWordField () {
        super();
    }

    public UIPassWordField (String text) {
        super(text);
    }

    public UIPassWordField (int columns) {
        super(columns);
    }

    public UIPassWordField (String text, int columns) {
        super(text, columns);
    }

    public UIPassWordField (Document doc, String txt, int columns) {
        super(doc, txt, columns);
    }
}