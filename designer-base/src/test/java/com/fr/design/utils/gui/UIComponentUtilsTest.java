package com.fr.design.utils.gui;

import com.fr.design.gui.core.UITextComponent;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.stable.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.swing.Icon;

import java.awt.Component;
import java.awt.Graphics;

import static org.junit.Assert.*;

/**
 * Created by plough on 2019/1/11.
 */
public class UIComponentUtilsTest {
    private static final String HTML_TAG_TPL = "<html><body style='width: %dpx'>";
    private static final String HTML_TAG = "<html>";
    private UIButton textButton;
    private UIButton emptyTextButton;
    private UIButton iconButton;

    private UILabel textLabel;
    private UILabel emptyTextLabel;
    private UILabel iconLabel;

    @Before
    public void setUp() {
        textButton = new UIButton("hello");
        emptyTextButton = new UIButton(StringUtils.EMPTY);
        iconButton = new UIButton(createMockIcon());

        textLabel = new UILabel("hello");
        emptyTextLabel = new UILabel(StringUtils.EMPTY);
        iconLabel = new UILabel(createMockIcon());
    }

    @Test
    public void testSetLineWrap() {
        UITextComponent[] noWrapComps = {emptyTextButton, emptyTextLabel, iconButton, iconLabel};
        UITextComponent[] wrapComps = {textLabel, textButton};

        for (UITextComponent comp : wrapComps) {
            UIComponentUtils.setLineWrap(comp);
            assertTrue(isLineWrapped(comp));
        }

        for (UITextComponent comp : noWrapComps) {
            UIComponentUtils.setLineWrap(comp);
            assertFalse(isLineWrapped(comp));
        }
    }

    @Test
    public void testSetLineWrapWithLineWidth() {
        UILabel label1 = new UILabel("l1");
        UILabel label2 = new UILabel("l2");
        UILabel label3 = new UILabel("l3");
        UIComponentUtils.setLineWrap(label1, 50);
        assertTrue(isLineWrappedWithLineWidth(label1, 50));

        UIComponentUtils.setLineWrap(label2, 0);
        assertTrue(isLineWrappedWithLineWidth(label2, 10));

        UIComponentUtils.setLineWrap(label3, -10);
        assertTrue(isLineWrappedWithLineWidth(label3, 10));
    }

    @Test
    public void testAddHtmlTwice() {
        UIComponentUtils.setLineWrap(textLabel, 50);
        UIComponentUtils.setLineWrap(textLabel, 20);  // 第二次应该不生效
        assertTrue(isLineWrappedWithLineWidth(textLabel, 50));
    }

    private boolean isLineWrapped(UITextComponent comp) {
        String text = comp.getText();
        return StringUtils.isNotEmpty(text) && text.startsWith(HTML_TAG);
    }

    private boolean isLineWrappedWithLineWidth(UITextComponent comp, int width) {
        String text = comp.getText();
        return StringUtils.isNotEmpty(text) && text.startsWith(String.format(HTML_TAG_TPL, width));
    }


    private Icon createMockIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                // do nothing
            }

            @Override
            public int getIconWidth() {
                return 0;
            }

            @Override
            public int getIconHeight() {
                return 0;
            }
        };
    }
}