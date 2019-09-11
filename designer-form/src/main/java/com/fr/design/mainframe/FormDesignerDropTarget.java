package com.fr.design.mainframe;

import com.fr.design.designer.creator.XCreator;
import com.fr.form.data.DataBinding;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.IframeEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.concept.data.ValueInitializer;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;

public class FormDesignerDropTarget extends DropTargetAdapter {

    private FormDesigner designer;

    public FormDesignerDropTarget(FormDesigner designer) {
        this.designer = designer;
        new DropTarget(designer, this);
    }

    /**
     * 响应拖拽进入事件
     *
     * @param dtde 需要被处理的拖拽事件
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(dtde.getDropAction());
    }

    /**
     * 处理拖拽经过事件
     *
     * @param dtde 需要被处理的拖拽事件
     */
    public void dragOver(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        FormDesigner designer = (FormDesigner) dtc.getComponent();
        designer.doMousePress(p.getX(), p.getY());

        dtde.acceptDrag(dtde.getDropAction());
    }

    /**
     * 处理拖拽释放事件
     *
     * @param dtde 需要被处理的拖拽事件
     */
    public void drop(DropTargetDropEvent dtde) {
        Transferable tr = dtde.getTransferable();
        DataFlavor[] df = tr.getTransferDataFlavors();
        try {
            Object o = tr.getTransferData(df[0]);
            XCreator creator = designer.getComponentAt(dtde.getLocation());
            Widget widget = creator.toData();
            if (o instanceof String) {
                String selectedPath = (String) o;
                if (widget instanceof IframeEditor) {
                    ((IframeEditor) widget).setSrc(selectedPath);
                }
                creator.rebuid();
                designer.getSelectionModel().setSelectedCreator(creator);
            }
            if (!(o instanceof String[][]) || ((String[][]) o).length < 1) {
                return;
            }
            if (!(widget instanceof DataControl)) {
                return;
            }
            ValueInitializer oldValue = ((DataControl) widget).getWidgetValue();
            ValueInitializer newValue = new WidgetValue(new DataBinding((String[][]) o));
            if (!ComparatorUtils.equals(oldValue, newValue)) {
                ((DataControl) widget).setWidgetValue(newValue);
                designer.fireTargetModified();
            }
            creator.rebuid();
            designer.getSelectionModel().setSelectedCreator(creator);
        } catch (UnsupportedFlavorException | IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}