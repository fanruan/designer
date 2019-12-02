package com.fr.design.gui.icontainer;

import com.fr.base.vcs.DesignerMode;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class UIModeControlContainer extends JLayeredPane {
    private static int DIM_HEIGHT = 30;
    private static final int NUM32 = 32;
    private static final int NUM5 = 5;
    private JComponent upPane;
    private JComponent downPane;
    private JPanel horizontToolPane;

    private CoverPane coverPane;
    private HidePane hidePane;
    private AuthoritySheetInvisibleCoverPane sheetInvisibleCoverPane;

    private int toolPaneY = 300;
    private int sheetCorverGap = 33;
    private int toolPaneHeight = 10;
    private boolean upEditMode = false;
    private boolean isHideMode = false;
    private boolean isSheeetCovered = false;

    private AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);

    public UIModeControlContainer() {
        this(new JPanel(), new JPanel());
    }

    public UIModeControlContainer(JComponent up, JComponent down) {
        upPane = up;
        downPane = down;
        setLayout(containerLayout);
        initToolbar();
        sheetInvisibleCoverPane = new AuthoritySheetInvisibleCoverPane();

        if (upPane == null) {
            toolPaneY = 0;
        } else {
            toolPaneY = upPane.getPreferredSize().height;
            add(upPane);
            add(horizontToolPane);
        }
        add(downPane);
        add(coverPane = new CoverPane());
        add(hidePane = new HidePane());
        if (upPane != null) {
            setLayer(upPane, 1);
            setLayer(horizontToolPane, 2);
        }
        setLayer(downPane, 1);
        setLayer(coverPane, 3);
        setLayer(hidePane, 2);
    }


    public void setSheeetCovered(boolean isSheetCovered) {
        this.isSheeetCovered = isSheetCovered;
    }


    public void needToShowCoverAndHidPane() {
        if (DesignModeContext.isAuthorityEditing()) {
            this.remove(coverPane);
            this.remove(hidePane);
        } else {
            this.remove(sheetInvisibleCoverPane);
            this.add(coverPane);
            this.add(hidePane);
            setLayer(coverPane, 3);
            setLayer(hidePane, 2);
        }
    }


    public void setDownPane(JComponent downPane) {
        this.downPane = downPane;
    }

    protected void onResize(int distance) {

    }

    private void initToolbar() {
        horizontToolPane = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(UIConstants.DRAG_BAR, 0, 0, getWidth(), getHeight(), null);
                if (upEditMode) {
                    g.drawImage(UIConstants.DRAG_DOT, (getWidth() - toolPaneHeight) / 2, 3, toolPaneHeight, 5, null);
                }
            }
        };

        horizontToolPane.addMouseListener(mouseAdapter);

        horizontToolPane.addMouseMotionListener(mouseMotionListener);
    }

    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (isHideMode || !upEditMode) {
                return;
            }
            if (DesignModeContext.isAuthorityEditing()) {
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (isHideMode || !upEditMode) {
                return;
            }
            setCursor(Cursor.getDefaultCursor());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            refreshContainer();
        }
    };

    MouseMotionListener mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
            //do nothing
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (DesignModeContext.isAuthorityEditing()) {
                return;
            }
            boolean notUpEditMode = isHideMode || !upEditMode;
            if (notUpEditMode) {
                return;
            }
            // 拖动的距离，为正值表示往下拖动，为负值表示往上拖动
            int deltaY = e.getY();
            toolPaneY += deltaY;
            onResize(toolPaneY);
            refreshContainer();
        }
    };

    private LayoutManager containerLayout = new LayoutManager() {

        @Override
        public void removeLayoutComponent(Component comp) {
            //do nothing
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return parent.getSize();
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            if (toolPaneY < 0) {
                toolPaneY = 0;
                setUpPaneHeight(0);
            }

            if (DesignerMode.isAuthorityEditing() && isSheeetCovered) {
                sheetInvisibleCoverPane.setBounds(0, toolPaneY + toolPaneHeight + UIConstants.SIZE, getWidth(), getHeight() - toolPaneY - toolPaneHeight - sheetCorverGap);
                UIModeControlContainer.this.add(sheetInvisibleCoverPane);
                UIModeControlContainer.this.setLayer(sheetInvisibleCoverPane, 2);
                sheetInvisibleCoverPane.setVisible(true);
            } else {
                sheetInvisibleCoverPane.setVisible(false);
                UIModeControlContainer.this.remove(sheetInvisibleCoverPane);
            }

            if (isHideMode) {
                hidePane.setVisible(true);
                coverPane.setVisible(false);
                hidePane.setBounds(0, 0, parent.getWidth(), hidePane.getPreferredSize().height);
                if (upPane != null) {
                    upPane.setVisible(false);
                    horizontToolPane.setBounds(0, hidePane.getPreferredSize().height, parent.getWidth(), toolPaneHeight);
                }
                downPane.setBounds(0, hidePane.getPreferredSize().height + toolPaneHeight, parent.getWidth(), parent.getHeight() - hidePane.getPreferredSize().height - toolPaneHeight);
            } else {
                hidePane.setVisible(false);
                coverPane.setVisible(true);
                if (upPane != null) {
                    upPane.setVisible(true);
                    upPane.setBounds(0, 0, parent.getWidth(), toolPaneY + UIConstants.SIZE);
                    horizontToolPane.setBounds(0, toolPaneY + UIConstants.SIZE, parent.getWidth(), toolPaneHeight);
                }
                downPane.setBounds(0, toolPaneY + toolPaneHeight + UIConstants.SIZE, parent.getWidth(), parent.getHeight() - toolPaneY - toolPaneHeight - UIConstants.SIZE);
                if (upEditMode) {
                    coverPane.setBounds(0, toolPaneY + toolPaneHeight + UIConstants.SIZE, getWidth(), getHeight() - toolPaneY - toolPaneHeight);
                } else {
                    coverPane.setBounds(0, 0, getWidth(), toolPaneY + UIConstants.SIZE);
                }
            }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // do nothing
        }
    };

    protected void onModeChanged() {
            //do nothing here
    }

    /**
     * @return
     */
    public boolean isUpEditMode() {
        return upEditMode;
    }

    private void setEditMode() {
        this.upEditMode = !upEditMode;
        onModeChanged();
    }

    public void setAuthorityMode(boolean isUpMode) {
        this.upEditMode = isUpMode;
    }

    public void setUpPaneHeight(int height) {
        this.toolPaneY = height;
        refreshContainer();
    }

    /**
     *
     */
    public void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }

    private class HidePane extends JPanel {
        public HidePane() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, -3));
            setBackground(UIConstants.NORMAL_BACKGROUND);
            add(new UILabel("<html><font size='5' face='Microsoft YaHei' color='#999999999'><B>" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Parameter_Panel") + "</B></font></html>"));
            UIButton viewButton = new UIButton(UIConstants.VIEW_NORMAL_ICON, UIConstants.VIEW_PRESSED_ICON, UIConstants.VIEW_PRESSED_ICON) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(32, 32);
                }
            };
            viewButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isHideMode = false;
                    refreshContainer();
                }
            });
            add(viewButton);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            dim.height = DIM_HEIGHT;
            return dim;
        }
    }


    private class AuthoritySheetInvisibleCoverPane extends JPanel {
        public AuthoritySheetInvisibleCoverPane() {
            setLayout(invisibleCoverLayout);
            setBackground(null);
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                     // do nothing
                }
            });
        }


        private LayoutManager invisibleCoverLayout = new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
                // do nothing
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
                 // do nothing
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
                // do nothing
            }
        };

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(composite);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(oldComposite);
            super.paint(g);
        }


    }

    private class CoverPane extends JPanel {
        UIButton editButton;
        UIButton hideButton;

        public CoverPane() {
            setLayout(coverLayout);
            setBackground(null);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // do nothing
                }
            });

            editButton = new UIButton(UIConstants.EDIT_NORMAL_ICON, UIConstants.EDIT_PRESSED_ICON, UIConstants.EDIT_PRESSED_ICON) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(40, 40);
                }
            };
            hideButton = new UIButton(UIConstants.HIDE_NORMAL_ICON, UIConstants.HIDE_PRESSED_ICON, UIConstants.HIDE_PRESSED_ICON) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(40, 40);
                }
            };
            editButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setEditMode();
                    refreshContainer();
                    DesignerContext.getDesignerFrame().getContentFrame().repaint();
                }
            });

            hideButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isHideMode = true;
                    refreshContainer();
                }
            });

            add(editButton);
            add(hideButton);
        }

        private LayoutManager coverLayout = new LayoutManager() {

            @Override
            public void removeLayoutComponent(Component comp) {
                // do nothing
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
                int height = parent.getHeight() + NUM32;
                int preferWidth = editButton.getPreferredSize().width;
                int preferHeight = editButton.getPreferredSize().height;
                if (upEditMode) {
                    hideButton.setVisible(false);
                    editButton.setBounds((width - preferWidth) / 2, 84, preferWidth, preferHeight);
                } else {
                    hideButton.setVisible(true);
                    hideButton.setBounds(width / 2 - preferWidth - NUM5, height / 2 - preferHeight + 3, preferWidth, preferWidth);
                    editButton.setBounds(width / 2 + NUM5, height / 2 - preferHeight + 3, preferWidth, preferWidth);
                }
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
                // do nothing
            }
        };

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(composite);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(oldComposite);
            super.paint(g);
        }
    }

    /**
     * @param args
     */
    public static void main(String... args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        UIModeControlContainer bb = new UIModeControlContainer();
        content.add(bb, BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(500, 500);
        jf.setVisible(true);
    }


}