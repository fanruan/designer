package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.DesignUtils;
import com.fr.env.EnvListPane;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.license.exception.RegistEditionException;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EnvChangedListener;
import com.fr.workspace.WorkContext;
import com.fr.workspace.WorkContextCallback;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.AuthException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//TODO: august TemplatePane和TemplateTreePane最好合并成一个类
public class TemplatePane extends JPanel implements MouseListener {

    private static final long NUM = 1L;

    private static int NUM200 = 200;

    public static TemplatePane getInstance() {

        return HOLDER.singleton;
    }

    private static class HOLDER {

        private static TemplatePane singleton = new TemplatePane();
    }

    private static final long serialVersionUID = 2108412478281713143L;

    public static final int HEIGHT = 23;// 最好和日志的高度统一 用同一个变量

    private static javax.swing.Icon leftIcon = BaseUtils.readIcon("/com/fr/design/images/docking/left.png");

    private static javax.swing.Icon rightIcon = BaseUtils.readIcon("/com/fr/design/images/docking/right.png");

    private boolean isExpanded = false;

    private UIButton editButton;

    private UILabel envLabel;

    private TemplatePane() {

        super();
        this.initComponents();
        this.setFocusable(true);
        this.addMouseListener(this);
        isExpanded = DesignerEnvManager.getEnvManager().isTemplateTreePaneExpanded();
        TemplateTreePane.getInstance().setVisible(isExpanded);
        TemplateTreePane.getInstance().setVisible(true);
    }

    private void initComponents() {

        GeneralContext.addEnvChangedListener(new EnvChangedListener() {

            public void envChanged() {

                setJLabel(DesignerEnvManager.getEnvManager().getCurEnvName());
            }
        });
        this.setLayout(new BorderLayout(25, 0));
        editButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/control/control-center2.png")) {

            private static final long serialVersionUID = NUM;

            @Override
            public Point getToolTipLocation(MouseEvent event) {

                return new Point(25, 2);
            }
        };
        editButton.setOpaque(false);
        editButton.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 10));
        editButton.setMargin(null);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Env_Configure_Workspace"));
        this.add(new UILabel("   "), BorderLayout.WEST);
        this.add(editButton, BorderLayout.EAST);
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                editItems();
            }
        });
        envLabel = new UILabel();
        envLabel.setForeground(new Color(102, 102, 102));
        setJLabel(DesignerEnvManager.getEnvManager().getCurEnvName());
        this.add(envLabel, BorderLayout.CENTER);
    }

    /**
     * 是否可扩展
     *
     * @return 同上
     */
    public boolean IsExpanded() {

        return this.isExpanded;
    }

    public void setExpand(boolean b) {

        this.isExpanded = b;
        this.repaint();
    }

    private boolean envListOkAction(EnvListPane envListPane) {

        final String selectedName = envListPane.updateEnvManager();
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        DesignerWorkspaceInfo selectedEnv = envManager.getWorkspaceInfo(selectedName);
        try {
            Workspace workspace = DesignerWorkspaceGenerator.generate(selectedEnv);
            boolean checkValid = workspace == null ? false : selectedEnv.checkValid();
            if (!checkValid) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Switch_Workspace_Failed"),
                        null, 0, UIManager.getIcon("OptionPane.errorIcon"));
                return false;
            }
            WorkContext.switchTo(workspace, new WorkContextCallback() {
                @Override
                public void done() {
                    DesignerEnvManager.getEnvManager().setCurEnvName(selectedName);
                    DesignUtils.refreshDesignerFrame();
                }
            });
            JTemplate template = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if (template != null) {
                template.refreshToolArea();
            }
            setJLabel(selectedName);
        } catch (AuthException e) {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remote_Connect_Auth_Failed"),
                    null, 0, UIManager.getIcon("OptionPane.errorIcon"));
            return false;
        } catch (RegistEditionException e) {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Lic_Does_Not_Support_Remote"),
                    null, 0, UIManager.getIcon("OptionPane.errorIcon"));
        } catch (Exception exception) {
            FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
        }
        TemplateTreePane.getInstance().refreshDockingView();
        DesignModelAdapter<?, ?> model = DesignModelAdapter.getCurrentModelAdapter();
        if (model != null) {
            model.envChanged();
        }
        return true;
    }

    /**
     * 编辑items
     */
    public void editItems() {

        final EnvListPane envListPane = new EnvListPane();
        final BasicDialog envListDialog = envListPane.showWindow(SwingUtilities.getWindowAncestor(DesignerContext.getDesignerFrame()));

        envListPane.populateEnvManager(envLabel.getText());
        envListDialog.addDialogActionListener(new DialogActionAdapter() {

            public void doOk() {

                envListOkAction(envListPane);
            }

            public void doCancel() {

                envListDialog.setVisible(false);
            }
        });
        envListDialog.setVisible(true);
    }

    private void setJLabel(String name) {

        DesignerWorkspaceInfo config = DesignerEnvManager.getEnvManager().getWorkspaceInfo(name);
        if (config != null) {
            switch (config.getType()) {
                case Remote: {
                    envLabel.setIcon(IOUtils.readIcon("com/fr/design/images/data/bind/localconnect.png"));
                    break;
                }
                case Local: {
                    envLabel.setIcon(IOUtils.readIcon("com/fr/design/images/data/bind/distanceconnect.png"));
                    break;
                }
            }
        }

        envLabel.setText(name);
        envLabel.repaint();
    }

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(250, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        paintBackgroundIcon(g);
    }

    private void paintBackgroundIcon(Graphics g) {

        int w = this.getWidth();
        int h = this.getHeight();
        Graphics2D g2d = (Graphics2D) g;
        Color lightColor = new Color(226, 230, 234);
        Color darkColor = new Color(183, 188, 195);
        GradientPaint gp = new GradientPaint(1, 1, lightColor, 1, h - 1, darkColor);
        g2d.setPaint(gp);
        g2d.fillRect(1, 1, w - 2, h - 1);
        g2d.setColor(lightColor);
        g2d.drawLine(0, 2, 0, h - 1);
        g2d.setColor(darkColor);
        g2d.drawLine(w - 1, 2, w - 1, h - 1);
        Icon icon = !isExpanded ? leftIcon : rightIcon;
        icon.paintIcon(this, g2d, 4, 4);

    }

    /**
     * 鼠标点击
     *
     * @param e 事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * 鼠标按下
     *
     * @param e 事件
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getX() < NUM200) {
            isExpanded = !isExpanded;
            TemplateTreePane.getInstance().setVisible(isExpanded);
            this.setExpand(isExpanded);
            DesignerEnvManager.getEnvManager().setTemplateTreePaneExpanded(isExpanded);
        }
    }

    /**
     * 鼠标放开
     *
     * @param e 事件
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * 鼠标进入
     *
     * @param e 事件
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * 鼠标离开
     *
     * @param e 事件
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 处理异常
     */
    public void dealEvnExceptionWhenStartDesigner() {

        final EnvListPane envListPane = new EnvListPane();
        envListPane.populateEnvManager(envLabel.getText());
        BasicDialog envListDialog = envListPane.showWindow(SwingUtilities.getWindowAncestor(DesignerContext.getDesignerFrame()));
        envListDialog.addDialogActionListener(new DialogActionAdapter() {

            public void doOk() {

                if (!envListOkAction(envListPane)) {
                    System.exit(0);
                }

            }

            public void doCancel() {

                System.exit(0);
            }
        });
        envListDialog.setVisible(true);
    }

}
