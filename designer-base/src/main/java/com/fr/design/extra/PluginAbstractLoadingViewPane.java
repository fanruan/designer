package com.fr.design.extra;

import com.fr.base.BaseUtils;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richie
 * @date 2015-03-11
 * @since 8.0
 */
public abstract class PluginAbstractLoadingViewPane<V, T> extends PluginAbstractViewPane {
    private static final String LOAD_CARD = "load";
    private static final String SUCCESS_CARD = "success";
    private static final String LOAD_ERROR = "error";

    private static final int BUSYANIMATIONRATE = 30;

    private Icon[] busyIcons = new Icon[15];
    private UILabel statusAnimationLabel;
    private Timer busyIconTimer;
    private int busyIconIndex;
    private CardLayout cardLayout;

    /**
     * 初始化cardlayout页面
     * @param tabbedPane
     */
    public PluginAbstractLoadingViewPane(UITabbedPane tabbedPane) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(initAndStartLoadingComponent(), LOAD_CARD);
        add(createSuccessPane(), SUCCESS_CARD);
        add(createErrorPane(), LOAD_ERROR);
        showLoadingCard();
        loadDataInAnotherThread();
    }
    /**
     * 创建成功页面
     * @return 创建的页面对象
     */
    public abstract JPanel createSuccessPane();

    /**
     * 创建错误页面
     * @return 创建的页面对象
     */
    public abstract JPanel createErrorPane();

    /**
     * 显示加载页面
     */
    public void showLoadingCard() {
        cardLayout.show(this, LOAD_CARD);
    }

    /**
     * 显示成功页面
     */
    public void showSuccessCard() {
        cardLayout.show(this, SUCCESS_CARD);
    }

    /**
     * 显示加载错误页面
     */
    public void showLoadErrorCard() {
        cardLayout.show(this, LOAD_ERROR);
    }

    /**
     * 停止加载
     */
    public void stopLoad() {
        busyIconTimer.stop();
    }


    private JPanel initAndStartLoadingComponent() {
         return new PluginStatusCheckCompletePane(){

            @Override
            public void pressInstallButton() {
                // do nothing
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return textForInstallFromDiskFileButton();
            }

            @Override
            public JComponent centerPane() {
                for (int i = 0; i < busyIcons.length; i++) {
                    busyIcons[i] = BaseUtils.readIcon("/com/fr/design/images/load/busy-icon" + i + ".png");
                }
                int busyAnimationRate = BUSYANIMATIONRATE;
                statusAnimationLabel = new UILabel();

                statusAnimationLabel.setText(textForLoadingLabel());
                busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

                        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
                        statusAnimationLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                });
                busyIconTimer.start();

                return statusAnimationLabel;
            }
        };


    }

    private void loadDataInAnotherThread() {
        new SwingWorker<V, T>(){

            @Override
            protected V doInBackground() throws Exception {
                return loadData();
            }

            public void done() {
                stopLoad();
                try {
                    V v = get();
                    loadOnSuccess(v);
                    showSuccessCard();
                } catch (Exception e) {
                    showLoadErrorCard();
                    loadOnFailed(e);
                }
            }

        }.execute();
    }

    protected abstract void installFromDiskFile();

    /**
     * 加载数据
     * @return 返回数据
     */
    public abstract V loadData() throws Exception;

    /**
     * 加载成功
     * @param v 将得到的数据传入
     */
    public abstract void loadOnSuccess(V v);

    /**
     * 加载失败
     * @param e 异常消息
     */
    public abstract void loadOnFailed(Exception e);

    /**
     * 正在加载页的标题
     * @return 标题字符串
     */
    public abstract String textForLoadingLabel();

    /**
     * 从磁盘安装按钮的提示
     * @return 按钮标题字符串
     */
    public abstract String textForInstallFromDiskFileButton();
}