package com.fr.design.mainframe;

import com.fr.base.iofile.attr.SharableAttrMark;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.form.share.SharableWidgetProvider;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.share.ShareConstants;
import com.fr.stable.StringUtils;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.Serializable;

/**
 * Coder: zack
 * Date: 2016/10/9
 * Time: 16:14
 */
public class ShareWidgetButton extends JPanel implements MouseListener, MouseMotionListener, Serializable {
    
    private static final Dimension TAB_DEFAULT_SIZE = new Dimension(500, 300);
    private SharableWidgetProvider bindInfo;
    private MouseEvent lastPressEvent;
    private JPanel reportPane;
    private boolean isEdit;
    private boolean isMarked;
    private Icon markedMode = IOUtils.readIcon("/com/fr/design/form/images/marked.png");
    private Icon unMarkedMode = IOUtils.readIcon("/com/fr/design/form/images/unmarked.png");
    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 60 / 100.0F);
    private JComponent markedButton = new JComponent() {
        protected void paintComponent(Graphics g) {
            markedMode.paintIcon(this, g, 0, 0);
        }
    };
    private JComponent unMarkedButton = new JComponent() {
        protected void paintComponent(Graphics g) {
            unMarkedMode.paintIcon(this, g, 0, 0);
        }
    };

    public ShareWidgetButton(SharableWidgetProvider bindInfo) {
        this.bindInfo = bindInfo;
        this.setPreferredSize(new Dimension(108, 68));
        initUI();
        this.setLayout(getCoverLayout());
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        new DragAndDropDragGestureListener(this, DnDConstants.ACTION_COPY_OR_MOVE);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(composite);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(oldComposite);
        super.paint(g);
    }

    public void setElementCaseEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            this.add(unMarkedButton, 0);
            repaint();
        }


    }

    private void initUI() {

        reportPane = new JPanel(new BorderLayout());
        reportPane.add(new UILabel(new ImageIcon(bindInfo.getCover())), BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        UILabel label = new UILabel(bindInfo.getName(), UILabel.CENTER);
        labelPane.setBackground(new Color(184, 220, 242));
        labelPane.add(label, BorderLayout.CENTER);
        reportPane.add(labelPane, BorderLayout.SOUTH);
        add(reportPane);
    }

    protected LayoutManager getCoverLayout() {
        return new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return parent.getPreferredSize();
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return null;
            }

            @Override
            public void layoutContainer(Container parent) {
                int width = parent.getWidth();
                int height = parent.getHeight();
                markedButton.setBounds((width - 25), 0, 25, 25);
                unMarkedButton.setBounds((width - 25), 0, 25, 25);
                reportPane.setBounds(0, 0, width, height);



            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }
        };
    }

    public SharableWidgetProvider getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(SharableWidgetProvider bindInfo) {
        this.bindInfo = bindInfo;
    }

    public String getFileName() {
        return  bindInfo.getName() +"." + bindInfo.getId() + ShareConstants.SUFFIX_MODULE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isEdit) {
            if (isMarked) {
                remove(markedButton);
                ShareLoader.getLoader().removeModuleForList(getFileName());
                isMarked = false;
            } else {
                add(markedButton,0);
                ShareLoader.getLoader().addModuleToList(getFileName());
                isMarked = true;
            }
        }

        repaint();
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
        if (DesignerMode.isAuthorityEditing()) {
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
            if (creatorSource != null) {
                ((AbstractBorderStyleWidget)creatorSource).addWidgetAttrMark(new SharableAttrMark(true));
                //tab布局WCardMainBorderLayout通过反射出来的大小是960*480
                XCreator xCreator = null;
                if (creatorSource instanceof WCardMainBorderLayout) {
                    xCreator = XCreatorUtils.createXCreator(creatorSource, TAB_DEFAULT_SIZE);
                } else {
                    xCreator = XCreatorUtils.createXCreator(creatorSource);
                }
                xCreator.setBackupBound(new Rectangle(no.getBindInfo().getWidth(), no.getBindInfo().getHeight()));
                xCreator.setShareId(shareId);
                WidgetToolBarPane.getTarget().startDraggingBean(xCreator);
                lastPressEvent = null;
                this.setBorder(null);
            }
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
