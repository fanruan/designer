package com.fr.design.gui;

import com.fr.design.gui.borders.UIFrameBorder;
import com.fr.design.gui.borders.UIInternalFrameBorder;
import com.fr.design.gui.borders.UITableHeaderBorder;
import com.fr.design.gui.borders.UITableHeaderRolloverBorder;
import com.fr.design.gui.borders.UITextFieldBorder;
import com.fr.design.gui.frpane.UIBasicOptionPaneUI;
import com.fr.design.gui.ibutton.UIBasicButtonUI;
import com.fr.design.gui.ibutton.UIButtonBorder;
import com.fr.design.gui.ibutton.UIRadioButtonMenuItemUI;
import com.fr.design.gui.ibutton.UIRadioButtonUI;
import com.fr.design.gui.icheckbox.UICheckBoxUI;
import com.fr.design.gui.icombobox.UIBasicComboBoxUI;
import com.fr.design.gui.icontainer.UIScrollPaneBorder;
import com.fr.design.gui.icontainer.UIScrollPaneUI;
import com.fr.design.gui.icontainer.UITableScrollPaneBorder;
import com.fr.design.gui.imenu.UIBasicMenuItemUI;
import com.fr.design.gui.imenu.UIBasicMenuUI;
import com.fr.design.gui.imenu.UIPopupMenuBorder;
import com.fr.design.gui.imenu.UIPopupMenuSeparatorUI;
import com.fr.design.gui.iprogressbar.UIProgressBarBorder;
import com.fr.design.gui.iprogressbar.UIProgressBarUI;
import com.fr.design.gui.iscrollbar.UIBasicScrollBarUI;
import com.fr.design.gui.ispinner.UISpinnerUI;
import com.fr.design.gui.isplitpanedivider.UISplitPaneUI;
import com.fr.design.gui.itable.UIBasicTableUI;
import com.fr.design.gui.itoolbar.UIToolBarBorder;
import com.fr.design.gui.itoolbar.UIToolBarSeparatorUI;
import com.fr.design.gui.itooltip.UIToolTipBorder;
import com.fr.design.gui.itree.UITreeUI;
import com.fr.design.i18n.Toolkit;
import com.fr.log.FineLoggerFactory;
import com.fr.general.IOUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Insets;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-31
 * Time: 上午9:38
 */
public class UILookAndFeel extends MetalLookAndFeel {


    static {
        UIManager.put("ProgressMonitor.progressText", Toolkit.i18nText("Fine-Design_Basic_ProgressBar_Title"));
        UIManager.put("OptionPane.cancelButtonText", Toolkit.i18nText("Fine-Design_Basic_Cancel"));
    }

    public static boolean CONTROL_PANEL_INSTANTIATED = false;


    protected void createDefaultTheme() {
        setCurrentTheme(new UIDefaultTheme());
    }


    /**
     * Initializes the uiClassID to BasicComponentUI mapping.
     * The JComponent classes define their own uiClassID constants. This table
     * must map those constants to a BasicComponentUI class of the appropriate
     * type.
     *
     * @param table The ui defaults table.
     */
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);

        table.putDefaults(new Object[]{
                "TreeUI", UITreeUI.class.getName(),
                "PopupMenuSeparatorUI", UIPopupMenuSeparatorUI.class.getName(),
                "SplitPaneUI", UISplitPaneUI.class.getName(),
                "SpinnerUI", UISpinnerUI.class.getName(),
                "ScrollPaneUI", UIScrollPaneUI.class.getName(),
                "RadioButtonUI", UIRadioButtonUI.class.getName(),
                "CheckBoxUI", UICheckBoxUI.class.getName(),
                "ToolBarSeparatorUI", UIToolBarSeparatorUI.class.getName(),
                "ScrollBarUI", UIBasicScrollBarUI.class.getName(),
                "ComboBoxUI", UIBasicComboBoxUI.class.getName(),
                "ButtonUI", UIBasicButtonUI.class.getName(),
                "ToggleButtonUI", UIBasicButtonUI.class.getName(),
                "TableUI", UIBasicTableUI.class.getName(),
                "ProgressBarUI", UIProgressBarUI.class.getName(),
                "MenuUI", UIBasicMenuUI.class.getName(),
                "MenuItemUI", UIBasicMenuItemUI.class.getName(),
                "RadioButtonMenuItemUI", UIRadioButtonMenuItemUI.class.getName(),
                "OptionPaneUI", UIBasicOptionPaneUI.class.getName(),
        });
    }


    /**
     * Initializes the default values for many ui widgets and puts them in the
     * given ui defaults table.                                                                T
     * Here is the place where borders can be changed.
     *
     * @param table The ui defaults table.
     */
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        table.put("Button.border",
                new BorderUIResource.CompoundBorderUIResource(
                        new UIButtonBorder(),
                        new BasicBorders.MarginBorder()));
        Border border = new EmptyBorder(2, 4, 2, 4);
        table.put("Menu.border", border);
        table.put("CheckBoxMenuItem.border", border);
        table.put("MenuItem.border", border);
        table.put("RadioButtonMenuItem.border", border);
        table.put("ToolTip.border", new BorderUIResource(new UIToolTipBorder(true)));
        table.put("ToolTip.borderInactive", new BorderUIResource(new UIToolTipBorder(false)));
        table.put("PopupMenu.border", new UIPopupMenuBorder());
        table.put("PopupMenu.foreground", new ColorUIResource(255, 0, 0));
        table.put("SplitPane.dividerSize", new Integer(7));
        table.put("ToolBar.border", new UIToolBarBorder());
        table.put("Table.scrollPaneBorder", new UITableScrollPaneBorder());

        border = new UIInternalFrameBorder();
        table.put("InternalFrame.border", border);
        table.put("InternalFrame.paletteBorder", border);
        table.put("InternalFrame.optionDialogBorder", border);
        table.put("Table.scrollPaneBorder", new UITableScrollPaneBorder());


        table.put("TabbedPane.tabInsets", new Insets(1, 6, 4, 6));
        table.put("TabbedPane.selectedTabPadInsets", new Insets(2, 2, 1, 2));
        table.put("TabbedPane.unselected", new ColorUIResource(0, 0, 0));
        table.put("TabbedPane.tabAreaInsets", new Insets(6, 2, 0, 0));
        table.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 3, 3));
        initOtherComponents(table);

    }

    private void initOtherComponents(UIDefaults table) {
        table.put("RootPane.colorChooserDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.errorDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.fileChooserDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.frameBorder", UIFrameBorder.getInstance());
        table.put("RootPane.informationDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.plainDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.questionDialogBorder", UIFrameBorder.getInstance());
        table.put("RootPane.warningDialogBorder", UIFrameBorder.getInstance());
        table.put("TableHeader.cellBorder", new UITableHeaderBorder());
        table.put("TableHeader.cellRolloverBorder", new UITableHeaderRolloverBorder());

        table.put("Tree.expandedIcon", loadIcon("TreeMinusIcon.png", this));
        table.put("Tree.collapsedIcon", loadIcon("TreePlusIcon.png", this));
        table.put("Tree.openIcon", loadIcon("TreeFolderOpenedIcon.png", this));
        table.put("Tree.closedIcon", loadIcon("TreeFolderClosedIcon.png", this));
        table.put("Tree.leafIcon", loadIcon("TreeLeafIcon.png", this));
        table.put("FileView.directoryIcon", loadIcon("DirectoryIcon.png", this));
        table.put("FileView.computerIcon", loadIcon("ComputerIcon.png", this));
        table.put("FileView.fileIcon", loadIcon("FileIcon.png", this));
        table.put("FileView.floppyDriveIcon", loadIcon("FloppyIcon.png", this));
        table.put("FileView.hardDriveIcon", loadIcon("HarddiskIcon.png", this));
        table.put("FileChooser.detailsViewIcon", loadIcon("FileDetailsIcon.png", this));
        table.put("FileChooser.homeFolderIcon", loadIcon("HomeFolderIcon.png", this));
        table.put("FileChooser.listViewIcon", loadIcon("FileListIcon.png", this));
        table.put("FileChooser.newFolderIcon", loadIcon("NewFolderIcon.png", this));
        table.put("FileChooser.upFolderIcon", loadIcon("ParentDirectoryIcon.png", this));
        table.put("OptionPane.errorIcon", loadIcon("Information_Icon_Error_32x32.png", this));
        table.put("OptionPane.informationIcon", loadIcon("Information_Icon_OK_32x32.png", this));
        table.put("OptionPane.warningIcon", loadIcon("WarningIcon.png", this));
        table.put("OptionPane.questionIcon", loadIcon("QuestionIcon.png", this));
        table.put("ScrollPane.border", new UIScrollPaneBorder());
        table.put("ProgressBar.border", new UIProgressBarBorder());
        table.put("Spinner.border", new UITextFieldBorder(new Insets(2, 2, 2, 2)));
        table.put("FormattedTextField.border", new UITextFieldBorder());
        table.put("TextField.border", new UITextFieldBorder());
        table.put("PasswordField.border", new UITextFieldBorder());
        table.put("TextArea.border", new UITextFieldBorder());
    }


    /**
     * 加载图片
     *
     * @param fileName 文件名
     * @param invoker  参数
     * @return 加载的图片
     */
    public static ImageIcon loadIcon(final String fileName, final Object invoker) {
        // This should work for both applications and applets
        URL url = Thread.currentThread().getContextClassLoader().getResource(
                "com/fr/design/images/lookandfeel/" + fileName);

        if (url == null) {
            // Another try
            url = IOUtils.getResource(
                    "com/fr/design/images/lookandfeel/" + fileName, UILookAndFeel.class);

            if (url == null) {
                FineLoggerFactory.getLogger().error("Icon directory could not be resolved.");
                return null;
            }
        }

        return new ImageIcon(url);
    }


}