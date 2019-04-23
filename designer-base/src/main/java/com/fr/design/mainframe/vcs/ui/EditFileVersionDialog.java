package com.fr.plugin.vcs.ui;

import com.fr.design.dialog.UIDialog;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.vcs.Vcs;
import com.fr.plugin.vcs.common.proxy.VcsCacheFileNodeFileProxy;
import com.fr.stable.StringUtils;
import com.fr.third.guava.base.Preconditions;
import com.google.inject.Inject;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.fr.plugin.vcs.common.CommonUtils.editingFilename;
import static com.fr.plugin.vcs.common.Constants.CURRENT_USERSNAME;

/**
 * 保存保本时候弹出的对话框，输入commit msg，点确定保存版本
 * Created by hzzz on 2017/12/11.
 */
public class SaveFileVersionDialog extends UIDialog {

    private final UITextArea msgTestArea = new UITextArea();
    private final UILabel versionLabel = new UILabel();
    private Vcs vcs;
    private FileVersionTablePanel fileVersionTablePanel;

    @Inject
    public SaveFileVersionDialog(FileVersionTablePanel fileVersionTablePanel, Vcs vcs) {
        this(DesignerContext.getDesignerFrame());
        this.vcs = Preconditions.checkNotNull(vcs, "vcs is null");
        this.fileVersionTablePanel = Preconditions.checkNotNull(fileVersionTablePanel, "fileVersionTablePanel is null");
    }

    private SaveFileVersionDialog(Frame parent) {
        super(parent);

        initComponents();
        setModal(true);
        setTitle(Inter.getLocText("Plugin-VCS_Save_Version"));
        setSize(300, 220);
        setResizable(false);
        GUICoreUtils.centerWindow(this);

    }

    private void initComponents() {

        JPanel fontPane = new JPanel(new BorderLayout());
        fontPane.add(new UILabel("   " + Inter.getLocText("Plugin-VCS_Version_Message") + "："), BorderLayout.NORTH);

        msgTestArea.setBorder(null);
        JScrollPane jScrollPane = new UIScrollPane(msgTestArea);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel("   " + Inter.getLocText("Plugin-VCS_Version_Number") + "："), versionLabel},
                new Component[]{fontPane, jScrollPane}
        };
        double[] rowSizes = new double[]{25, 100};
        double[] columnSizes = new double[]{70, 200};

        add(TableLayoutHelper.createTableLayoutPane(components, rowSizes, columnSizes), BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(buttonPane, BorderLayout.SOUTH);

        UIButton ok = new UIButton(Inter.getLocText("OK"));
        UIButton cancel = new UIButton(Inter.getLocText("Cancel"));

        buttonPane.add(ok);
        buttonPane.add(cancel);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jt.stopEditing();
                jt.saveTemplate();
                String filename = editingFilename();
                String versionLabelText = versionLabel.getText();
                // V.3 -> 3
                String version = versionLabelText.substring(2);
                //TODO refactor
                if (jt.getEditingFILE() instanceof VcsCacheFileNodeFileProxy) {
                    vcs.saveVersionFromCache(CURRENT_USERSNAME, filename, msgTestArea.getText(), Integer.parseInt(version));
                    fileVersionTablePanel.updateModel(1);
                } else {
                    vcs.saveVersion(CURRENT_USERSNAME, filename, msgTestArea.getText(), Integer.parseInt(version));
                }
                jt.requestFocus();
                doOK();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });
    }

    private void refresh() {
        int latestFileVersion = vcs.getLatestFileVersion(editingFilename());
        versionLabel.setText("V." + String.valueOf(latestFileVersion + 1));
        msgTestArea.setText(StringUtils.EMPTY);
    }

    public void showMsgInputDialog() {
        refresh();
        setVisible(true);
    }

    @Override
    public void checkValid() throws Exception {

    }
}
