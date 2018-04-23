package com.fr.design.report;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.report.cell.Elem;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * 
 * 这个类主要用于插入图片时的设置
 *
 */
public class SelectImagePane extends BasicPane{
    private ImagePreviewPane previewPane = null;
    
    private ImageFileChooser imageFileChooser = null;
    private UIRadioButton defaultRadioButton = null;
    private UIRadioButton tiledRadioButton = null;
    private UIRadioButton extendRadioButton = null;
    private UIRadioButton adjustRadioButton = null;

    private Style imageStyle = null;
    private Image previewImage = null;
    
    private File imageFile;

    public SelectImagePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // preview pane
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(previewContainerPane, BorderLayout.CENTER);

        JPanel previewOwnerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        previewContainerPane.add(previewOwnerPane, BorderLayout.CENTER);

        previewOwnerPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Preview"),null));

        previewPane = new ImagePreviewPane();
        previewOwnerPane.add(new JScrollPane(previewPane));

        JPanel selectFilePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        previewContainerPane.add(selectFilePane, BorderLayout.EAST);
        selectFilePane.setBorder(BorderFactory
                .createEmptyBorder(8, 2, 4, 0));

        UIButton selectPictureButton = new UIButton(
                Inter.getLocText("Image-Select_Picture"));
        selectFilePane.add(selectPictureButton, BorderLayout.NORTH);
        selectPictureButton.setMnemonic('S');
        selectPictureButton.addActionListener(selectPictureActionListener);
        
        JPanel layoutPane=FRGUIPaneFactory.createMediumHGapHighTopFlowInnerContainer_M_Pane();
        selectFilePane.add(layoutPane, BorderLayout.CENTER);
        
        //布局
        defaultRadioButton = new UIRadioButton(Inter.getLocText("Default"));
        tiledRadioButton = new UIRadioButton(Inter.getLocText("Image-Titled"));
        extendRadioButton = new UIRadioButton(Inter.getLocText("Image-Extend"));
        adjustRadioButton = new UIRadioButton(Inter.getLocText("Image-Adjust"));

        defaultRadioButton.addActionListener(layoutActionListener);
        tiledRadioButton.addActionListener(layoutActionListener);
        extendRadioButton.addActionListener(layoutActionListener);
        adjustRadioButton.addActionListener(layoutActionListener);
        
        JPanel jp = new JPanel(new GridLayout(4, 1, 15, 15));
        jp.add(defaultRadioButton);
        jp.add(tiledRadioButton);
        jp.add(extendRadioButton);
        jp.add(adjustRadioButton);
        layoutPane.add(jp);

        ButtonGroup layoutBG = new ButtonGroup();
        layoutBG.add(defaultRadioButton);
        layoutBG.add(tiledRadioButton);
        layoutBG.add(extendRadioButton);
        layoutBG.add(adjustRadioButton);

        defaultRadioButton.setSelected(true);

        // init image file chooser.
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
    }

    /**
     * Select picture.
     */
    ActionListener selectPictureActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser
                    .showOpenDialog(SelectImagePane.this);
            if (returnVal != JFileChooser.CANCEL_OPTION) {
                File selectedFile = imageFileChooser.getSelectedFile();

                if (selectedFile != null && selectedFile.isFile()) {
                    Image image = BaseUtils.readImage(selectedFile.getPath());
                    CoreGraphHelper.waitForImage(image);
                    
                    imageFile = selectedFile;
                    setImageStyle();
                    previewPane.setImage(image);
                    previewPane.setImageStyle(imageStyle);
                    previewImage = image;
                } else {
                    previewPane.setImage(null);
                }
                previewPane.repaint();
            }
        }
    };
    
    // 调整图片样式，只有水平和垂直对齐以及拉伸。相对于背景，平铺不予考虑。
    private void changeImageStyle() {
    	previewPane.setImageStyle(this.imageStyle);
    	previewPane.repaint();
    }
    
    private void setImageStyle(){
        if(tiledRadioButton.isSelected()){
        	imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_TILED);
        }else if(adjustRadioButton.isSelected()){
        	imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_ADJUST);
        }else if(extendRadioButton.isSelected()){
        	imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_EXTEND);
        }else{
        	imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_CENTER);
        }
    }

    ActionListener layoutActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
        	setImageStyle();
            changeImageStyle();
        }
    };
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Image");
    }

    public void populate(Elem cell) {
    	Style style = null;
    	if (cell != null) {
	    	Object value = cell.getValue();
	    	if (value != null && value instanceof Image) {
	    		Image image = (Image)value;
	    		previewPane.setImage(image);
	    		this.previewImage = image;
	    	}
	    	
	    	style = cell.getStyle();
    	}

    	if(style.getImageLayout() == Constants.IMAGE_TILED){
    		tiledRadioButton.setSelected(true);
    	}else if(style.getImageLayout() == Constants.IMAGE_EXTEND){
    		extendRadioButton.setSelected(true);
    	}else if(style.getImageLayout() == Constants.IMAGE_ADJUST){
    		adjustRadioButton.setSelected(true);
    	}else{
    		style.deriveImageLayout(Constants.IMAGE_CENTER);
    		defaultRadioButton.setSelected(true);
    	}
    	this.imageStyle = style;
    	changeImageStyle();
    }

    public CellImage update(){
    	CellImage cellImage = new CellImage();
    	cellImage.setImage(previewPane.getImage());
    	cellImage.setStyle(this.imageStyle);
    	return cellImage;
    }
    
    public File getSelectedImage(){
    	return imageFile;
    }
}