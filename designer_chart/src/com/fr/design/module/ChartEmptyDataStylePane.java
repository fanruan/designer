package com.fr.design.module;

import com.fr.base.BaseUtils;
import com.fr.base.ChartEmptyDataStyleManagerProvider;
import com.fr.base.ChartEmptyDataStyleServerManager;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by mengao on 2017/11/23.
 */
public class ChartEmptyDataStylePane extends AbstractAttrNoScrollPane {

    private UIButtonGroup emptyData;
    private UIRadioButton defaultRadioButton;
    private UIRadioButton customRadioButton;
    private UIButton selectPictureButton;

    private ImagePreviewPane previewPane;
    private ImageFileChooser imageFileChooser;

    private Image emptyDataImage = null;


    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        content.add(creatNorthPane(), BorderLayout.NORTH);
        content.add(creatCenterPane(), BorderLayout.CENTER);
        return content;
    }

    private JPanel creatNorthPane() {
        emptyData = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        emptyData.setSelectedIndex(0);
        emptyData.setPreferredSize(new Dimension(150, 20));
        emptyData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEmptyDataStyle();
            }
        });

        UILabel promptContent = new UILabel(Inter.getLocText("FR-Designer_Tip_Content"));
        JPanel northPane = GUICoreUtils.createFlowPane(new Component[]{promptContent, emptyData}, FlowLayout.LEFT, 15, 0);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        return northPane;
    }

    private JPanel creatCenterPane() {
        JPanel centerPane = new JPanel(FRGUIPaneFactory.createBorderLayout());

        // preview pane
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        centerPane.add(previewContainerPane, BorderLayout.CENTER);

        JPanel previewOwnerPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Preview"));
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

        selectFilePane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 30));

        defaultRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_DEFAULT"));
        customRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer-Widget-Style_Custom"));
        ButtonGroup buttonGroup = new ButtonGroup();
        defaultRadioButton.setSelected(true);
        buttonGroup.add(defaultRadioButton);
        buttonGroup.add(customRadioButton);

        defaultRadioButton.addActionListener(getLayoutActionListener());
        customRadioButton.addActionListener(getLayoutActionListener());

        JPanel jp = new JPanel(new GridLayout(3, 1, 0, 10));
        jp.add(defaultRadioButton);
        jp.add(customRadioButton);

        selectPictureButton = new UIButton(
                Inter.getLocText("FR-Designer_Background_Image_Select"));
        selectPictureButton.addActionListener(getSelectPictureActionListener());
        jp.add(selectPictureButton);


        selectFilePane.add(jp, BorderLayout.NORTH);
        return selectFilePane;
    }

    private ActionListener getLayoutActionListener() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                checkCustomImage();
                changeEmptyDataStyle();
            }

            private void changeEmptyDataStyle() {
                previewPane.repaint();
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
                    File selectedFile = imageFileChooser.getSelectedFile();

                    if (selectedFile != null && selectedFile.isFile()) {
                        emptyDataImage = BaseUtils.readImage(selectedFile.getPath());
                        CoreGraphHelper.waitForImage(emptyDataImage);

                        previewPane.setImage(emptyDataImage);
                        previewPane.repaint();
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

    @Override
    public String getIconPath() {
        return StringUtils.EMPTY;
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Chart_Empty_Data");
    }

    public void populateBean() {
        ChartEmptyDataStyleManagerProvider manager = ChartEmptyDataStyleServerManager.getProviderInstance();
        emptyData.setSelectedIndex(manager.isOpenEmptyDataStyle() == true ? 0 : 1);
        customRadioButton.setSelected(manager.isCustomEmptyDataStyle());
        emptyDataImage = manager.getEmptyDataImage();

        checkEmptyDataStyle();
        checkCustomImage();
        previewPane.setImage(emptyDataImage);
        previewPane.repaint();
    }

    public void updateBean() {
        ChartEmptyDataStyleManagerProvider manager = ChartEmptyDataStyleServerManager.getProviderInstance();

        manager.setOpenEmptyDataStyle(emptyData.getSelectedIndex() == 0);
        manager.setCustomEmptyDataStyle(customRadioButton.isSelected());
        manager.setEmptyDataImage(emptyDataImage);

        try {
            manager.writeResource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通知报表整个刷新.
        DesignerFrame frame = DesignerContext.getDesignerFrame();
        if (frame != null) {
            frame.repaint();
        }
    }
}
