package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.widget.WidgetBoundsPaneFactory;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.ComparatorUtils;


import javax.swing.JOptionPane;
import java.awt.Rectangle;

/**
 * Created by kerry on 2017/12/4.
 */
public class WidgetCardTagBoundPane extends WidgetBoundPane {
    private UISpinner cardTagWidth ;

    public WidgetCardTagBoundPane(XCreator source) {
        super(source);
    }

    @Override
    public void initBoundPane() {
        cardTagWidth = new UIBoundSpinner(0, Integer.MAX_VALUE, 1);
        cardTagWidth.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        this.add(WidgetBoundsPaneFactory.createCardTagBoundPane(cardTagWidth));
    }

    @Override
    public void update() {
        if (parent == null) {
            return;
        }
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        Rectangle parentBounds = new Rectangle(parent.getBounds());

        WCardTagLayout tagLayout = (WCardTagLayout)creator.toData();
        WTabDisplayPosition displayPosition = tagLayout.getDisplayPosition();
        int size = (int)cardTagWidth.getValue();
        XLayoutContainer tabLayout = creator.getTopLayout();
        Rectangle rectangle = tabLayout.getBounds();
        if(ComparatorUtils.equals(displayPosition, WTabDisplayPosition.TOP_POSITION) || ComparatorUtils.equals(displayPosition, WTabDisplayPosition.BOTTOM_POSITION)){
            if(rectangle.height < size){
                FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Tablayout_Bounds"));
                return;
            }
            parentBounds.height = size;
        }else{
            if(rectangle.width < size){
                FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Tablayout_Bounds"));
                return;
            }
            parentBounds.width = size;
        }

        parent.setBounds(parentBounds);
        LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(formDesigner, parent);
        if (layoutAdapter != null) {
            parent.setBackupBound(parent.getBounds());
            layoutAdapter.fix(parent);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "absoluteBound";
    }

    @Override
    public void populate() {
        WCardTagLayout tagLayout = (WCardTagLayout)creator.toData();
        Rectangle bounds = new Rectangle(creator.getBounds());
        WTabDisplayPosition displayPosition = tagLayout.getDisplayPosition();
        if( ComparatorUtils.equals(displayPosition, WTabDisplayPosition.TOP_POSITION) || ComparatorUtils.equals(displayPosition, WTabDisplayPosition.BOTTOM_POSITION)){
            cardTagWidth.setValue(bounds.height);
        }else{
            cardTagWidth.setValue(bounds.width);
        }

    }

}
