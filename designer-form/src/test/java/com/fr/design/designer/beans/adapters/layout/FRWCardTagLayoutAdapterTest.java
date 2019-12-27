package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.XElementCase;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Dimension;

/**
 * Created by kerry on 2019-12-10
 */
public class FRWCardTagLayoutAdapterTest {
    @Test
    public void testAccept() {
        FRWCardTagLayoutAdapter adapter = new FRWCardTagLayoutAdapter(
                new XWCardTagLayout(new WCardTagLayout(), new Dimension(100, 100)));
        Assert.assertFalse(adapter.accept(new XElementCase(
                new ElementCaseEditor(), new Dimension(100, 100)), 1, 1));
    }
}
