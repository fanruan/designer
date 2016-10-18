package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.ElCaseBindInfo;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

/**
 * Coder: zack
 * Date: 2016/10/9
 * Time: 16:14
 */
public class ShareWidgetButton extends JPanel implements MouseListener, MouseMotionListener, Serializable {
    private ElCaseBindInfo bindInfo;
    private MouseEvent lastPressEvent;

    public ShareWidgetButton(ElCaseBindInfo bindInfo) {
        this.bindInfo = bindInfo;
        initUI();
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        new DragAndDropDragGestureListener(this, DnDConstants.ACTION_COPY_OR_MOVE);
    }


    private void initUI() {
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setPreferredSize(new Dimension(108, 72));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        ImagePanel imagePanel = new ImagePanel((BufferedImage) bindInfo.getCover());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.add(imagePanel, BorderLayout.NORTH);
        UILabel label = new UILabel(bindInfo.getName(), SwingConstants.HORIZONTAL);
        label.setOpaque(true);
        label.setBackground(new Color(184, 220, 242));
        this.add(label, BorderLayout.SOUTH);
    }

    private class ImagePanel extends JPanel {

        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
            this.setPreferredSize(new Dimension(108, 52));
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }

    }

    public ElCaseBindInfo getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(ElCaseBindInfo bindInfo) {
        this.bindInfo = bindInfo;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPressEvent = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (BaseUtils.isAuthorityEditing()) {
            return;
        }
        if (lastPressEvent == null) {
            return;
        }
        Object source = e.getSource();
        Widget creatorSource = null;
        String shareId = StringUtils.EMPTY;
        if (source instanceof ShareWidgetButton) {
            ShareWidgetButton no = (ShareWidgetButton) e.getSource();
            if (no == null) {
                return;
            }
            shareId = no.getBindInfo().getId();
            creatorSource = ShareLoader.getLoader().getElCaseEditorById(shareId);
        }
        if (creatorSource != null) {
            XCreator xCreator = XCreatorUtils.createXCreator(creatorSource);
            xCreator.setShareId(shareId);
            WidgetToolBarPane.getTarget().startDraggingBean(xCreator);
            lastPressEvent = null;
            this.setBorder(null);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public class DragAndDropDragGestureListener extends DragSourceAdapter implements DragGestureListener {
        private DragSource source;

        public DragAndDropDragGestureListener(ShareWidgetButton tt, int actions) {
            source = new DragSource();
            source.createDefaultDragGestureRecognizer(tt, actions, this);
        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            ShareWidgetButton shareWidgetButton = (ShareWidgetButton) dge.getComponent();
            if (shareWidgetButton != null) {
                Widget widget = ShareLoader.getLoader().getElCaseEditorById(shareWidgetButton.getBindInfo().getId());
                DragAndDropTransferable dragAndDropTransferable = new DragAndDropTransferable(widget);
                dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable, this);
            }
        }

        @Override
        public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {

        }
    }

    public class DragAndDropTransferable implements Transferable {
        private Widget widget;

        public DragAndDropTransferable(Widget widget) {
            this.widget = widget;
        }

        DataFlavor[] flavors = {new DataFlavor(Widget.class, "Widget")};

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            for (DataFlavor df : flavors) {
                if (ComparatorUtils.equals(df, flavor)) {
                    return true;
                }
            }
            return false;
        }

        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            return widget;
        }
    }
}
