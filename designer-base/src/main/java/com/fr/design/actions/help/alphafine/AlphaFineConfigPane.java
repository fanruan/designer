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
    private UICheckBox enabledCheckbox, searchOnlineCheckbox, needSegmentationCheckbox, needIntelligentCustomerService, containRecommendCheckbox, containActionCheckbox, containDocumentCheckbox, containTemplateCheckbox, containPluginCheckbox, containFileContentCheckbox;
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
                new Component[]{containRecommendCheckbox, containActionCheckbox, containDocumentCheckbox},
                new Component[]{containTemplateCheckbox, containPluginCheckbox, containFileContentCheckbox},
                new Component[]{needIntelligentCustomerService, null, null}
        };
        return components;
    }

    private Component[][] initOnlineComponents() {
        Component[][] components = new Component[][]{
                new Component[]{searchOnlineCheckbox, needSegmentationCheckbox, null}
        };
        return components;
    }

    private void createSearchConfigPane(JPanel contentPane) {
        double[] rowSize = {ROW_GAP, ROW_GAP, ROW_GAP};

        double[] columnSize = {COLUMN_GAP, COLUMN_GAP, COLUMN_GAP};

        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Search_Range"));
        containRecommendCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Recommend"));
        containActionCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set"));
        containPluginCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Plugin_Addon"));
        containDocumentCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Community_Help"));
        containTemplateCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Templates"));
        containFileContentCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Templates_Content"));
        needIntelligentCustomerService = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Intelligent_Customer_Service"));
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
        northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Open") + ":"));
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
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Search_Type"));
        searchOnlineCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_Internet_Search"));
        needSegmentationCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_Segmentation"));
        searchOnlineCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!searchOnlineCheckbox.isSelected()) {
                    containRecommendCheckbox.setEnabled(false);
                    containPluginCheckbox.setEnabled(false);
                    containDocumentCheckbox.setEnabled(false);
                    needIntelligentCustomerService.setEnabled(false);
                    containRecommendCheckbox.setSelected(false);
                    containPluginCheckbox.setSelected(false);
                    containDocumentCheckbox.setSelected(false);
                    needIntelligentCustomerService.setSelected(false);
                } else {
                    containRecommendCheckbox.setEnabled(true);
                    containPluginCheckbox.setEnabled(true);
                    containDocumentCheckbox.setEnabled(true);
                    needIntelligentCustomerService.setEnabled(true);
                }
            }
        });
        double[] rowSize = {ROW_GAP};
        double[] columnSize = {COLUMN_GAP, COLUMN_GAP, COLUMN_GAP};
        JPanel onlinePane = TableLayoutHelper.createTableLayoutPane(initOnlineComponents(), rowSize, columnSize);
        northPane.add(onlinePane);
        contentPane.add(northPane);
    }

    private void createOpenPane(JPanel contentPane) {
        JPanel northPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable"));
        enabledCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_AlphaFine_Enable_AlphaFine"));
        northPane.add(enabledCheckbox);
        contentPane.add(northPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "AlphaFine";
    }

    public void populate(AlphaFineConfigManager alphaFineConfigManager) {
        this.enabledCheckbox.setSelected(alphaFineConfigManager.isEnabled());
        this.searchOnlineCheckbox.setEnabled(FRContext.isChineseEnv());
        this.searchOnlineCheckbox.setSelected(alphaFineConfigManager.isSearchOnLine());
        this.containActionCheckbox.setSelected(alphaFineConfigManager.isContainAction());
        this.containTemplateCheckbox.setSelected(alphaFineConfigManager.isContainTemplate());
        this.containFileContentCheckbox.setSelected(alphaFineConfigManager.isContainFileContent());
        this.containDocumentCheckbox.setSelected(alphaFineConfigManager.isContainDocument() && alphaFineConfigManager.isSearchOnLine());
        this.containDocumentCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.containPluginCheckbox.setSelected(alphaFineConfigManager.isContainPlugin() && alphaFineConfigManager.isSearchOnLine());
        this.containPluginCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.containRecommendCheckbox.setSelected(alphaFineConfigManager.isContainRecommend() && alphaFineConfigManager.isSearchOnLine());
        this.containRecommendCheckbox.setEnabled(alphaFineConfigManager.isSearchOnLine());
        this.shortcutsField.setText(getDisplayShortCut(alphaFineConfigManager.getShortcuts()));

        this.needSegmentationCheckbox.setSelected(alphaFineConfigManager.isNeedSegmentationCheckbox());
        this.needIntelligentCustomerService.setSelected(alphaFineConfigManager.isNeedIntelligentCustomerService() && alphaFineConfigManager.isSearchOnLine());
        this.needIntelligentCustomerService.setEnabled(alphaFineConfigManager.isSearchOnLine());
        shortCutKeyStore = convert2KeyStroke(alphaFineConfigManager.getShortcuts());
    }

    public void update() {
        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        AlphaFineConfigManager alphaFineConfigManager = designerEnvManager.getAlphaFineConfigManager();
        alphaFineConfigManager.setContainPlugin(this.containPluginCheckbox.isSelected());
        alphaFineConfigManager.setContainAction(this.containActionCheckbox.isSelected());
        alphaFineConfigManager.setContainDocument(this.containDocumentCheckbox.isSelected());
        alphaFineConfigManager.setContainRecommend(this.containRecommendCheckbox.isSelected());
        alphaFineConfigManager.setEnabled(this.enabledCheckbox.isSelected());
        alphaFineConfigManager.setSearchOnLine(this.searchOnlineCheckbox.isSelected());
        alphaFineConfigManager.setContainTemplate(this.containTemplateCheckbox.isSelected());
        alphaFineConfigManager.setContainFileContent(this.containFileContentCheckbox.isSelected());
        alphaFineConfigManager.setNeedSegmentationCheckbox(this.needSegmentationCheckbox.isSelected());
        alphaFineConfigManager.setNeedIntelligentCustomerService(this.needIntelligentCustomerService.isSelected());
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
        return containFileContentCheckbox;
    }

    public void setIsContainFileContentCheckbox(UICheckBox isContainFileContentCheckbox) {
        this.containFileContentCheckbox = isContainFileContentCheckbox;
    }
}
