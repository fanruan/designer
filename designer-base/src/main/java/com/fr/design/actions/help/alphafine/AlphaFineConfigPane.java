package com.fr.design.actions.help.alphafine;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by XiaXiang on 2017/4/6.
 */
public class AlphaFineConfigPane extends BasicPane {
    private static final String TYPE = "pressed";
    private static final String DISPLAY_TYPE = "+";
    private static final String BACK_SLASH = "BACK_SLASH";
    private static final String DISPLAY_BACK_SLASH = "\\";
    private static final String SLASH = "SLASH";
    private static final String DISPLAY_SLASH = "/";
    private static final String CONTROL = "CONTROL";
    private static final String DISPLAY_CONTROL = "ctrl";
    private static final String OPEN_BRACKET = "OPEN_BRACKET";
    private static final String DISPLAY_OPEN_BRACKET = "{";
    private static final String CLOSE_BRACKET = "CLOSE_BRACKET";
    private static final String DISPLAY_CLOSE_BRACKET = "}";
    private static final String COMMA = "COMMA";
    private static final String DISPLAY_COMMA = ",";
    private static final String PERIOD = "PERIOD";
    private static final String DISPLAY_PERIOD = ".";
    private static final String SEMICOLON = "SEMICOLON";
    private static final String DISPLAY_SEMICOLON = ";";
    private static final String QUOTE = "QUOTE";
    private static final String DISPLAY_QUOTE = "'";
    private static final String EQUALS = "EQUALS";
    private static final String DISPLAY_EQUALS = "+";
    private static final String MINUS = "MINUS";
    private static final String DISPLAY_MINUS = "-";
    private static final String COMMAND = "META";
    private static final String SMALL_COMMAND = "meta";
    private static final String DISPLAY_COMMAND = "\u2318";


    private static final double COLUMN_GAP = 180;
    private static final double ROW_GAP = 25;
    private KeyStroke shortCutKeyStore = null;
    private UICheckBox isEnabledCheckbox, isSearchOnlineCheckbox, isContainRecommendCheckbox, isContainActionCheckbox, isContainDocumentCheckbox, isContainTemplateCheckbox, isContainPluginCheckbox, isContainFileContentCheckbox;
    private UITextField shortcutsField;

    public AlphaFineConfigPane() {
        this.initComponents();
    }

    private void initComponents() {
        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        createOpenPane(contentPane);
        createOnlinePane(contentPane);
        createShortcutsPane(contentPane);
        createSearchConfigPane(contentPane);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(contentPane, BorderLayout.NORTH);

    }

    private Component[][] initSearchRangeComponents() {
        Component[][] components = new Component[][]{
                new Component[]{isContainRecommendCheckbox, isContainActionCheckbox, isContainDocumentCheckbox},
                new Component[]{isContainTemplateCheckbox, isContainPluginCheckbox, isContainFileContentCheckbox}
        };
        return components;
    }

    private void createSearchConfigPane(JPanel contentPane) {
        double[] rowSize = {ROW_GAP, ROW_GAP};

        double[] columnSize = {COLUMN_GAP, COLUMN_GAP, COLUMN_GAP};

        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Search_Range"));
        isContainRecommendCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Recommend"));
        isContainActionCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Set"));
        isContainPluginCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon"));
        isContainDocumentCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_COMMUNITY_HELP"));
        isContainTemplateCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Templates"));
        isContainFileContentCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Templates_Content"));
        JPanel searchConfigPane = TableLayoutHelper.createTableLayoutPane(initSearchRangeComponents(), rowSize, columnSize);
        northPane.add(searchConfigPane);
        contentPane.add(northPane);
    }

    private void createShortcutsPane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Shortcut_Config"));
        shortcutsField = new UITextField();
        shortcutsField.setEditable(false);
        shortcutsField.selectAll();
        shortcutsField.setPreferredSize(new Dimension(100, 20));
        initFieldListener();
        northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Open") + ":"));
        northPane.add(shortcutsField);
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_SetShortcuts"));
        label.setForeground(Color.RED);
        northPane.add(label);
        contentPane.add(northPane);
    }

    private void initFieldListener() {
        shortcutsField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                shortcutsField.selectAll();
            }
        });
        shortcutsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int modifier = e.getModifiers();
                if (modifier == 0) {
                    return;
                }
                int keyCode = e.getKeyCode();
                shortCutKeyStore = KeyStroke.getKeyStroke(keyCode, modifier);
                String str = shortCutKeyStore.toString();
                shortcutsField.setText(getDisplayShortCut(str));
                shortcutsField.selectAll();
            }
        });
    }

    private void createOnlinePane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_Internet"));
        isSearchOnlineCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_Internet_Search"));
        isSearchOnlineCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSearchOnlineCheckbox.isSelected()) {
                    isContainRecommendCheckbox.setEnabled(false);
                    isContainPluginCheckbox.setEnabled(false);
                    isContainDocumentCheckbox.setEnabled(false);
                    isContainRecommendCheckbox.setSelected(false);
                    isContainPluginCheckbox.setSelected(false);
                    isContainDocumentCheckbox.setSelected(false);
                } else {
                    isContainRecommendCheckbox.setEnabled(true);
                    isContainPluginCheckbox.setEnabled(true);
                    isContainDocumentCheckbox.setEnabled(true);
                }
            }
        });
        northPane.add(isSearchOnlineCheckbox);
        contentPane.add(northPane);
    }

    private void createOpenPane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable"));
        isEnabledCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_AlphaFine"));
        northPane.add(isEnabledCheckbox);
        contentPane.add(northPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "AlphaFine";
    }

    public void populate(AlphaFineConfigManager alphaFineConfigManager) {
        this.isEnabledCheckbox.setSelected(alphaFineConfigManager.isEnabled());
        this.isSearchOnlineCheckbox.setEnabled(FRContext.isChineseEnv());
        this.isSearchOnlineCheckbox.setSelected(alphaFineConfigManager.isSearchOnLine());
        this.isContainActionCheckbox.setSelected(alphaFineConfigManager.isContainAction());
        this.isContainTemplateCheckbox.setSelected(alphaFineConfigManager.isContainTemplate());
        this.isContainFileContentCheckbox.setSelected(alphaFineConfigManager.isContainFileContent());
        this.isContainDocumentCheckbox.setSelected(alphaFineConfigManager.isContainDocument() && alphaFineConfigManager.isSearchOnLine());
        this.isContainDocumentCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.isContainPluginCheckbox.setSelected(alphaFineConfigManager.isContainPlugin() && alphaFineConfigManager.isSearchOnLine());
        this.isContainPluginCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.isContainRecommendCheckbox.setSelected(alphaFineConfigManager.isContainRecommend() && alphaFineConfigManager.isSearchOnLine());
        this.isContainRecommendCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.shortcutsField.setText(getDisplayShortCut(alphaFineConfigManager.getShortcuts()));
        shortCutKeyStore = convert2KeyStroke(alphaFineConfigManager.getShortcuts());
    }

    public void update() {
        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        AlphaFineConfigManager alphaFineConfigManager = designerEnvManager.getAlphaFineConfigManager();
        alphaFineConfigManager.setContainPlugin(this.isContainPluginCheckbox.isSelected());
        alphaFineConfigManager.setContainAction(this.isContainActionCheckbox.isSelected());
        alphaFineConfigManager.setContainDocument(this.isContainDocumentCheckbox.isSelected());
        alphaFineConfigManager.setContainRecommend(this.isContainRecommendCheckbox.isSelected());
        alphaFineConfigManager.setEnabled(this.isEnabledCheckbox.isSelected());
        alphaFineConfigManager.setSearchOnLine(this.isSearchOnlineCheckbox.isSelected());
        alphaFineConfigManager.setContainTemplate(this.isContainTemplateCheckbox.isSelected());
        alphaFineConfigManager.setContainFileContent(this.isContainFileContentCheckbox.isSelected());
        alphaFineConfigManager.setShortcuts(shortCutKeyStore != null ? shortCutKeyStore.toString().replace(TYPE, DISPLAY_TYPE) : this.shortcutsField.getText());
        designerEnvManager.setAlphaFineConfigManager(alphaFineConfigManager);
        try {
            DesignerEnvManager.loadLogSetting();
            DesignerEnvManager.getEnvManager().saveXMLFile();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }


    }

    private String getDisplayShortCut(String shortCut) {
        return shortCut.replace(TYPE, DISPLAY_TYPE).replace(BACK_SLASH, DISPLAY_BACK_SLASH).replace(SLASH, DISPLAY_SLASH)
                .replace(CONTROL, DISPLAY_CONTROL).replace(OPEN_BRACKET, DISPLAY_OPEN_BRACKET).replace(CLOSE_BRACKET, DISPLAY_CLOSE_BRACKET)
                .replace(COMMA, DISPLAY_COMMA).replace(PERIOD, DISPLAY_PERIOD).replace(SEMICOLON, DISPLAY_SEMICOLON).replace(QUOTE, DISPLAY_QUOTE)
                .replace(EQUALS, DISPLAY_EQUALS).replace(MINUS, DISPLAY_MINUS).replace(COMMAND, DISPLAY_COMMAND).replace(SMALL_COMMAND, DISPLAY_COMMAND);
    }


    private KeyStroke convert2KeyStroke(String ks) {
        return KeyStroke.getKeyStroke(ks.replace(DISPLAY_TYPE, TYPE));
    }

    public KeyStroke getShortCutKeyStore() {
        return shortCutKeyStore;
    }

    public void setShortCutKeyStore(KeyStroke shortCutKeyStore) {
        this.shortCutKeyStore = shortCutKeyStore;
    }

    public UICheckBox getIsContainFileContentCheckbox() {
        return isContainFileContentCheckbox;
    }

    public void setIsContainFileContentCheckbox(UICheckBox isContainFileContentCheckbox) {
        this.isContainFileContentCheckbox = isContainFileContentCheckbox;
    }
}
