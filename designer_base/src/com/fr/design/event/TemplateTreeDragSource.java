package com.fr.design.event;

import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.design.mainframe.dnd.ArrayTransferable;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.general.web.ParameterConsts;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 13-11-4
 * Time: 下午2:17
 */
public class TemplateTreeDragSource extends DragSourceAdapter implements DragGestureListener {
    private DragSource source;

    public TemplateTreeDragSource(JTree tree, int actions) {
        source = new DragSource();
        source.createDefaultDragGestureRecognizer(tree, actions, this);

    }
    public void dragGestureRecognized(DragGestureEvent dge) {
        Component comp = dge.getComponent();
        if (comp instanceof TemplateFileTree) {
            String selectedPath = ((TemplateFileTree)comp).getSelectedTemplatePath();
            source.startDrag(dge, DragSource.DefaultLinkDrop, new SerializableTransferable(selectedPath), this);
        }
    }
}