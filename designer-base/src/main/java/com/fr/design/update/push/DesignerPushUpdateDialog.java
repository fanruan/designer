package com.fr.design.update.push;

import com.fr.design.dialog.UIDialog;
import com.fr.design.ui.ModernUIPane;
import com.fr.design.utils.BrowseUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.intelli.record.FocusPoint;
import com.fr.intelli.record.MetricRegistry;
import com.fr.intelli.record.Original;
import com.fr.stable.StringUtils;
import com.fr.web.struct.AssembleComponent;
import com.fr.web.struct.Atom;
import com.fr.web.struct.browser.RequestClient;
import com.fr.web.struct.category.ScriptPath;
import com.fr.web.struct.category.StylePath;
import com.fr.web.struct.impl.FineUI;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

/**
 * Created by plough on 2019/4/10.
 */
class DesignerPushUpdateDialog extends UIDialog {
    public static final Dimension DEFAULT = new Dimension(640, 360);

    private ModernUIPane<Model> jsPane;

    private DesignerPushUpdateDialog(Frame parent) {
        super(parent);
        setModal(true);
        initComponents();
    }

    static void createAndShow(final Frame parent, final DesignerUpdateInfo updateInfo) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DesignerPushUpdateDialog dialog = new DesignerPushUpdateDialog(parent);
                dialog.populate(updateInfo);
                dialog.showDialog();
            }
        });

    }

    private void initComponents() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        jsPane = new ModernUIPane.Builder<Model>()
                .withComponent(new AssembleComponent() {
                    @Override
                    public ScriptPath script(RequestClient req) {
                        return ScriptPath.build("/com/fr/design/ui/update/push/pushUpdate.js");
                    }

                    @Override
                    public StylePath style(RequestClient req) {
                        return StylePath.build("/com/fr/design/ui/update/push/pushUpdate.css");
                    }

                    @Override
                    public Atom[] refer() {
                        return new Atom[]{FineUI.KEY};
                    }
                }).namespace("Pool").build();

        contentPane.add(jsPane);
    }

    private void populate(DesignerUpdateInfo updateInfo) {
        Model model = createModel(updateInfo);
        jsPane.populate(model);
    }

    private Model createModel(DesignerUpdateInfo updateInfo) {
        Model model = new Model();
        model.setVersion(updateInfo.getLatestVersion());
        model.setContent(updateInfo.getPushContent());
        model.setMoreInfoUrl(updateInfo.getMoreInfoUrl());
        model.setBackgroundUrl(updateInfo.getBackgroundUrl());
        return model;
    }

    @Override
    public void checkValid() throws Exception {
        // do nothing
    }

    /**
     * 显示窗口
     */
    private void showDialog() {
        setSize(DEFAULT);
        setUndecorated(true);
        GUICoreUtils.centerWindow(this);
        setVisible(true);
    }

    public class Model {
        private String version;
        private String content;
        private String moreInfoUrl;
        private String backgroundUrl;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void browseMoreInfoUrl() {
            if (StringUtils.isNotEmpty(moreInfoUrl)) {
                BrowseUtils.browser(moreInfoUrl);
            }
        }

        public void setMoreInfoUrl(String moreInfoUrl) {
            this.moreInfoUrl = moreInfoUrl;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public void updateNow() {
            DesignerPushUpdateManager.getInstance().doUpdate();
            FocusPointManager.submit(FocusPointManager.OperateType.UPDATE);
            exit();
        }

        public void remindNextTime() {
            FocusPointManager.submit(FocusPointManager.OperateType.REMIND_NEXT_TIME);
            exit();
        }

        public void skipThisVersion() {
            DesignerPushUpdateManager.getInstance().skipCurrentPushVersion();
            FocusPointManager.submit(FocusPointManager.OperateType.SKIP);
            exit();
        }

        public void closeWindow() {
            FocusPointManager.submit(FocusPointManager.OperateType.CLOSE_WINDOW);
            exit();
        }

        public String i18nText(String key) {
            return com.fr.design.i18n.Toolkit.i18nText(key);
        }

        private void exit() {
            DesignerPushUpdateDialog.this.dialogExit();
        }
    }

    private static class FocusPointManager {

        private static final String ID = "com.fr.update.push";
        private static final String TITLE = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Push_Update_Focus_Point");

        private enum OperateType {
            CLOSE_WINDOW(0), UPDATE(1), REMIND_NEXT_TIME(2), SKIP(3);
            private int index;
            OperateType(int index) {
                this.index = index;
            }
            private String toText() {
                return String.valueOf(this.index);
            }
        }

        private static void submit(OperateType opType) {
            FocusPoint focusPoint = FocusPoint.create(ID, opType.toText(), Original.EMBED);
            focusPoint.setTitle(TITLE);
            MetricRegistry.getMetric().submit(focusPoint);
        }
    }
}
