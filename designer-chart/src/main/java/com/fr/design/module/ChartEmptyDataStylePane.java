package com.fr.design.module;

import com.fr.base.BaseUtils;
import com.fr.base.ChartEmptyDataStyleConf;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.ImageUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;

import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by mengao on 2017/11/23.
 */
public class ChartEmptyDataStylePane extends AbstractAttrNoScrollPane {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 20;
    private static final int FIVE = 5;
    private static final int TEN = 10;
    private static final int THIRTY = 30;
    private static Image DEFAULT_EMPTY_DATA_IMAGE;


    private UIButtonGroup emptyData;
    private UIRadioButton defaultRadioButton;
    private UIRadioButton customRadioButton;
    private UIButton selectPictureButton;

    private ImagePreviewPane previewPane;
    private ImageFileChooser imageFileChooser;

    private Image emptyDataImage = DEFAULT_EMPTY_DATA_IMAGE;
    private SwingWorker<Void, Void> imageWorker;


    static {
        DEFAULT_EMPTY_DATA_IMAGE = GeneralContext.isChineseEnv() ? IOUtils.readImage("com/fr/design/images/zh_emptydata.png")
                : IOUtils.readImage("com/fr/design/images/us_emptydata.png");
    }

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        content.add(creatNorthPane(), BorderLayout.NORTH);
        content.add(creatCenterPane(), BorderLayout.CENTER);
        return content;
    }

    private JPanel creatNorthPane() {
        emptyData = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Close")});
        emptyData.setSelectedIndex(0);
        emptyData.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        emptyData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEmptyDataStyle();
                repaintPreviewPane();
            }
        });

        UILabel promptContent = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Tip_Content"));
        JPanel northPane = GUICoreUtils.createFlowPane(new Component[]{promptContent, emptyData}, FlowLayout.LEFT, TEN, 0);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, FIVE, 0, 0));
        return northPane;
    }

    private JPanel creatCenterPane() {
        JPanel centerPane = new JPanel(FRGUIPaneFactory.createBorderLayout());

        // preview pane
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        centerPane.add(previewContainerPane, BorderLayout.CENTER);

        JPanel previewOwnerPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Preview"));
        previewOwnerPane.setLayout(new BorderLayout());
        previewContainerPane.add(previewOwnerPane, BorderLayout.CENTER);
        previewContainerPane.add(initSelectFilePane(), BorderLayout.EAST);
        previewPane = new ImagePreviewPane();
        previewOwnerPane.add(new JScrollPane(previewPane));


        // init image file chooser.
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
        return centerPane;
    }

    public JPanel initSelectFilePane() {

        JPanel selectFilePane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        selectFilePane.setBorder(BorderFactory.createEmptyBorder(TEN, FIVE, 0, THIRTY));

        defaultRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_DEFAULT"));
        customRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget-Style_Custom"));
        ButtonGroup buttonGroup = new ButtonGroup();
        defaultRadioButton.setSelected(true);
        buttonGroup.add(defaultRadioButton);
        buttonGroup.add(customRadioButton);

        defaultRadioButton.addActionListener(getLayoutActionListener());
        customRadioButton.addActionListener(getLayoutActionListener());

        JPanel jp = new JPanel(new GridLayout(3, 1, 0, TEN));
        jp.add(defaultRadioButton);
        jp.add(customRadioButton);

        selectPictureButton = new UIButton(
                com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image_Select"));
        selectPictureButton.addActionListener(getSelectPictureActionListener());
        jp.add(selectPictureButton);


        selectFilePane.add(jp, BorderLayout.NORTH);
        return selectFilePane;
    }

    private ActionListener getLayoutActionListener() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                emptyDataImage = null;
                checkCustomImage();
                repaintPreviewPane();
            }
        };
    }

    /**
     * Select picture.
     */
    private ActionListener getSelectPictureActionListener() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int returnVal = imageFileChooser.showOpenDialog(ChartEmptyDataStylePane.this);
                if (returnVal != JFileChooser.CANCEL_OPTION) {
                    final File selectedFile = imageFileChooser.getSelectedFile();

                    if (selectedFile != null && selectedFile.isFile()) {
                        previewPane.showLoading();
                        if (imageWorker != null && !imageWorker.isDone()) {
                            imageWorker = null;
                        }
                        imageWorker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                emptyDataImage = imageFileChooser.isCheckSelected() ? ImageUtils.defaultImageCompress(selectedFile) : BaseUtils.readImage(selectedFile.getPath());
                                CoreGraphHelper.waitForImage(emptyDataImage);
                                repaintPreviewPane();
                                return null;
                            }
                        };
                        imageWorker.execute();
                    } else {
                        previewPane.setImage(null);
                    }
                }

            }
        };
    }

    private void checkEmptyDataStyle() {
        boolean b = emptyData.getSelectedIndex() == 0;
        defaultRadioButton.setEnabled(b);
        customRadioButton.setEnabled(b);
        selectPictureButton.setEnabled(b);
    }

    private void checkCustomImage() {
        selectPictureButton.setVisible(customRadioButton.isSelected());
    }

    private void repaintPreviewPane() {
        emptyDataImage = customRadioButton.isSelected() ? emptyDataImage : DEFAULT_EMPTY_DATA_IMAGE;
        previewPane.setImage(emptyData.getSelectedIndex() == 0 ? emptyDataImage : null);
        previewPane.repaint();
    }

    @Override
    public String getIconPath() {
        return StringUtils.EMPTY;
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Chart_Empty_Data");
    }

    public void populateBean() {
        ChartEmptyDataStyleConf manager = ChartEmptyDataStyleConf.getInstance();
        emptyData.setSelectedIndex(manager.isOpenEmptyDataStyle() == true ? 0 : 1);
        customRadioButton.setSelected(manager.isCustomEmptyDataStyle());
        emptyDataImage = manager.getEmptyDataImage();

        checkEmptyDataStyle();
        checkCustomImage();
        repaintPreviewPane();
    }

    public void updateBean() {
        ChartEmptyDataStyleConf manager = ChartEmptyDataStyleConf.getInstance();

        manager.setOpenEmptyDataStyle(emptyData.getSelectedIndex() == 0);
        manager.setCustomEmptyDataStyle(customRadioButton.isSelected());
        manager.setEmptyDataImage(emptyDataImage);
    }
}
