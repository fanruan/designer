package com.fr.design.layout;

import java.awt.Container;

import javax.swing.JFrame;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;

public class TableLayoutTest extends JFrame
{

    public static void main (String args[])
    {
        new TableLayoutTest();
    }

    public TableLayoutTest ()
    {
        super("The Power of Preferred Sizes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();

        // b - border
        // f - FILL
        // p - PREFERRED
        // vs - vertical space between labels and text fields
        // vg - vertical gap between form elements
        // hg - horizontal gap between form elements

        double b = 10;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double vs = 5;
        double vg = 10;
        double hg = 10;

        double size[][] =
            {{b, f, hg, p, hg, p, b},
             {b, p, vs, p, vg, p, vs, p, vg, p, vs, p, vg, p, b}};

        TableLayout layout = new TableLayout(size);
        pane.setLayout (layout);

        // Create all controls
        UILabel labelName    = new UILabel("Name");
        UILabel labelAddress = new UILabel("Address");
        UILabel labelCity    = new UILabel("City");
        UILabel labelState   = new UILabel("State");
        UILabel labelZip     = new UILabel("Zip");

        UITextField textfieldName    = new UITextField(10);
        UITextField textfieldAddress = new UITextField(20);
        UITextField textfieldCity    = new UITextField(10);
        UITextField textfieldState   = new UITextField(2);
        UITextField textfieldZip     = new UITextField(5);

        UIButton buttonOk = new UIButton("OK");
        UIButton buttonCancel = new UIButton("Cancel");
        JPanel panelButton = new JPanel();
        panelButton.add(buttonOk);
        panelButton.add(buttonCancel);

        // Add all controls
        pane.add(labelName,        "1,  1, 5, 1");
        pane.add(textfieldName,    "1,  3, 5, 3");
        pane.add(labelAddress,     "1,  5, 5, 5");
        pane.add(textfieldAddress, "1,  7, 5, 7");
        pane.add(labelCity,        "1,  9");
        pane.add(textfieldCity,    "1, 11");
        pane.add(labelState,       "3,  9");
        pane.add(textfieldState,   "3, 11");
        pane.add(labelZip,         "5,  9");
        pane.add(textfieldZip,     "5, 11");
        pane.add(panelButton,      "1, 13, 5, 13");

        pack();
        show();
    }
}