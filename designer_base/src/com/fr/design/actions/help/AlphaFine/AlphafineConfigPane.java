package com.fr.design.actions.help.AlphaFine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by XiaXiang on 2017/4/6.
 */
public class AlphafineConfigPane extends BasicPane {
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
    private static final double COLUMN_GAP = 180;
    private static final double ROW_GAP = 25;
    private KeyStroke shortCutKeyStore = null;
    private UICheckBox isEnabledCheckbox, isSearchOnlineCheckbox, isContainConcludeCheckbox, isContainActionCheckbox, isContainDocumentCheckbox, isContainTemplateCheckbox, isContainPluginCheckbox, isContainFileContentCheckbox;
    private UITextField shortcutsField;

    public AlphafineConfigPane() {
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

    private Component[][] initsearchRangeComponents() {
        Component[][] components = new Component[][]{
                new Component[]{isContainConcludeCheckbox, isContainActionCheckbox, isContainDocumentCheckbox},
                new Component[]{isContainTemplateCheckbox, isContainPluginCheckbox, isContainFileContentCheckbox}
        };
        return components;
    }

    private void createSearchConfigPane(JPanel contentPane) {
        double[] rowSize = {ROW_GAP, ROW_GAP};

        double[] columnSize = {COLUMN_GAP, COLUMN_GAP, COLUMN_GAP};

        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_AlphaFine_SearchRange"));
        isContainConcludeCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_AlphaFine_Conclude"));
        isContainActionCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_Set"));
        isContainPluginCheckbox = new UICheckBox(Inter.getLocText("FR-Designer-Plugin_Addon"));
        isContainDocumentCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_COMMUNITY_HELP"));
        isContainTemplateCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_Templates"));
        isContainFileContentCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_Templates_Content"));
        JPanel searchConfigPane = TableLayoutHelper.createTableLayoutPane(initsearchRangeComponents(), rowSize, columnSize);
        northPane.add(searchConfigPane);
        contentPane.add(northPane);
    }

    private void createShortcutsPane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_AlphaFine_Shortcut_Config"));
        shortcutsField = new UITextField();
        shortcutsField.setPreferredSize(new Dimension(100, 20));
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
            }
        });
        northPane.add(new UILabel(Inter.getLocText("FR-Designer_Open") + ":"));
        northPane.add(shortcutsField);
        contentPane.add(northPane);
    }

    private void createOnlinePane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_AlphaFine_EnableInternet"));
        isSearchOnlineCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_AlphaFine_EnableInternetSearch"));
        isSearchOnlineCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSearchOnlineCheckbox.isSelected()) {
                    isContainConcludeCheckbox.setEnabled(false);
                    isContainPluginCheckbox.setEnabled(false);
                    isContainDocumentCheckbox.setEnabled(false);
                    isContainConcludeCheckbox.setSelected(false);
                    isContainPluginCheckbox.setSelected(false);
                    isContainDocumentCheckbox.setSelected(false);
                } else {
                    isContainConcludeCheckbox.setEnabled(true);
                    isContainPluginCheckbox.setEnabled(true);
                    isContainDocumentCheckbox.setEnabled(true);
                }
            }
        });
        northPane.add(isSearchOnlineCheckbox);
        contentPane.add(northPane);
    }

    private void createOpenPane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_AlphaFine_Enable"));
        isEnabledCheckbox = new UICheckBox(Inter.getLocText("FR-Designer_AlphaFine_EnableAlphaFine"));
        northPane.add(isEnabledCheckbox);
        contentPane.add(northPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "AlphaFine";
    }

    public void populate(AlphafineConfigManager alphafineConfigManager) {
        this.isEnabledCheckbox.setSelected(alphafineConfigManager.isEnabled());
        this.isSearchOnlineCheckbox.setSelected(alphafineConfigManager.isSearchOnLine());
        this.isContainActionCheckbox.setSelected(alphafineConfigManager.isContainAction());
        this.isContainTemplateCheckbox.setSelected(alphafineConfigManager.isContainTemplate());
        this.isContainDocumentCheckbox.setSelected(alphafineConfigManager.isContainDocument() && alphafineConfigManager.isSearchOnLine());
        this.isContainDocumentCheckbox.setEnabled(alphafineConfigManager.isSearchOnLine());
        this.isContainPluginCheckbox.setSelected(alphafineConfigManager.isContainPlugin() && alphafineConfigManager.isSearchOnLine());
        this.isContainPluginCheckbox.setEnabled(alphafineConfigManager.isSearchOnLine());
        this.isContainConcludeCheckbox.setSelected(alphafineConfigManager.isContainConclude() && alphafineConfigManager.isSearchOnLine());
        this.isContainConcludeCheckbox.setEnabled(alphafineConfigManager.isSearchOnLine());
        this.shortcutsField.setText(alphafineConfigManager.getShortcuts());
        shortCutKeyStore = convert2KeyStroke(alphafineConfigManager.getShortcuts());
    }

    public void update() {
        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        AlphafineConfigManager alphafineConfigManager = designerEnvManager.getAlphafineConfigManager();
        alphafineConfigManager.setContainPlugin(this.isContainPluginCheckbox.isSelected());
        alphafineConfigManager.setContainAction(this.isContainActionCheckbox.isSelected());
        alphafineConfigManager.setContainDocument(this.isContainActionCheckbox.isSelected());
        alphafineConfigManager.setContainConclude(this.isContainConcludeCheckbox.isSelected());
        alphafineConfigManager.setEnabled(this.isEnabledCheckbox.isSelected());
        alphafineConfigManager.setSearchOnLine(this.isSearchOnlineCheckbox.isSelected());
        alphafineConfigManager.setContainTemplate(this.isContainTemplateCheckbox.isSelected());
        alphafineConfigManager.setContainFileContent(this.isContainFileContentCheckbox.isSelected());
        alphafineConfigManager.setShortcuts(shortCutKeyStore != null ? shortCutKeyStore.toString().replace(TYPE, DISPLAY_TYPE) : this.shortcutsField.getText());
        designerEnvManager.setAlphafineConfigManager(alphafineConfigManager);
        try {
            DesignerEnvManager.loadLogSetting();
            DesignerEnvManager.getEnvManager().saveXMLFile();
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }


    }

    private String getDisplayShortCut(String shotrCut) {
        return shotrCut.replace(TYPE, DISPLAY_TYPE).replace(BACK_SLASH, DISPLAY_BACK_SLASH).replace(SLASH, DISPLAY_SLASH)
                .replace(CONTROL, DISPLAY_CONTROL).replace(OPEN_BRACKET, DISPLAY_OPEN_BRACKET).replace(CLOSE_BRACKET, DISPLAY_CLOSE_BRACKET)
                .replace(COMMA, DISPLAY_COMMA).replace(PERIOD, DISPLAY_PERIOD).replace(SEMICOLON, DISPLAY_SEMICOLON).replace(QUOTE, DISPLAY_QUOTE)
                .replace(EQUALS, DISPLAY_EQUALS).replace(MINUS, DISPLAY_MINUS);
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
