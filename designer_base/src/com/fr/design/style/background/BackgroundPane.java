/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.design.style.background;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageBackground;
import com.fr.base.background.PatternBackground;
import com.fr.base.background.TextureBackground;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;
import com.fr.design.style.background.gradient.GradientPane;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.DetailColorSelectPane;
import com.fr.design.utils.gui.GUICoreUtils;

public class BackgroundPane extends BasicPane {
    private static final int BACKGROUND_NULL = 0;
    private static final int BACKGROUND_COLOR = 1;
    private static final int BACKGROUND_TEXTURE = 2;
    private static final int BACKGROUND_PATTERN = 3;
    private static final int BACKGROUND_IMAGE = 4;
    private static final int BACKGROUND_GRADIENT_COLOR = 5;

    private UITabbedPane tabbedPane = null;
    private NullBackgroundPane nullBackgroundPane = null;
    private ColorBackgroundPane colorBackgroundPane = null;
    private TextureBackgroundPane textureBackgroundPane = null;
    private PatternBackgroundPane patternBackgroundPane = null;
    private ImageBackgroundPane imageBackgroundPane = null;
    private GradientPane gradientPane = null;
    private EventListenerList eventChangeList = new EventListenerList();
    private static boolean isBrowserBackgroundPane;

    public BackgroundPane(){
    	this(false);
    }
    
    //需求说: 如果是浏览器背景, 隐藏掉几个button
    public BackgroundPane(boolean isBrowserBackgroundPane) {
    	BackgroundPane.isBrowserBackgroundPane = isBrowserBackgroundPane;
        this.initComponents();
        this.setPreferredSize(new Dimension(400, 300));
    }


    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        previewPane = new BackgroundPreviewLabel();
        previewPane.addBackgroundChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireStateChanged();
            }
        });
        tabbedPane = new UITabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(Inter.getLocText("Background-Null"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addTab(Inter.getLocText("Color"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addTab(Inter.getLocText("Background-Texture"),FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addTab(Inter.getLocText("Background-Pattern"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addTab(Inter.getLocText("Image"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addTab(Inter.getLocText("Gradient-Color"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        tabbedPane.addChangeListener(backgroundChangeListener);
        tabbedPane.setPreferredSize(new Dimension(200, 210));
    }

    public void addChangeListener(ChangeListener changeListener) {
        eventChangeList.add(ChangeListener.class, changeListener);
    }

    /**
     */
    public void fireStateChanged() {
        Object[] listeners = eventChangeList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Background");
    }

    private NullBackgroundPane getNullBackgroundPane() {
        if (this.nullBackgroundPane == null) {
            this.nullBackgroundPane = new NullBackgroundPane();
            nullBackgroundPane.addChangeListener(backgroundChangeListener);
        }
        return this.nullBackgroundPane;
    }

    private ColorBackgroundPane getColorBackgroundPane() {
        if (this.colorBackgroundPane == null) {
            this.colorBackgroundPane = new ColorBackgroundPane();
            colorBackgroundPane.addChangeListener(backgroundChangeListener);
        }
        return this.colorBackgroundPane;
    }

    private TextureBackgroundPane getTextureBackgroundPane() {
        if (this.textureBackgroundPane == null) {
            this.textureBackgroundPane = new TextureBackgroundPane();
            textureBackgroundPane.addChangeListener(backgroundChangeListener);
        }
        return this.textureBackgroundPane;
    }

    private PatternBackgroundPane getPatternBackgroundPane() {
        if (this.patternBackgroundPane == null) {
            this.patternBackgroundPane = new PatternBackgroundPane();
            patternBackgroundPane.addChangeListener(backgroundChangeListener);
        }
        return this.patternBackgroundPane;
    }

    private ImageBackgroundPane getImageBackgroundPane() {
        if (this.imageBackgroundPane == null) {
            this.imageBackgroundPane = new ImageBackgroundPane();
            imageBackgroundPane.addChangeListener(backgroundChangeListener);
        }
        return this.imageBackgroundPane;
    }

    private GradientPane getGradientPane() {
        if (this.gradientPane == null) {
            this.gradientPane = new GradientPane();
            gradientPane.addChangeListener(backgroundChangeListener);
        }

        return this.gradientPane;
    }

    /**
     * Populate background.
     */
    public void populate(Background background) {
        if (background == null) {
            tabbedPane.setComponentAt(0, this.getNullBackgroundPane());
            tabbedPane.setSelectedIndex(0);
            nullBackgroundPane.populate(background);
        } else {
            if (background instanceof ColorBackground) {
                tabbedPane.setComponentAt(1, this.getColorBackgroundPane());
                tabbedPane.setSelectedIndex(1);
                colorBackgroundPane.populate(background);
            } else if (background instanceof TextureBackground) {
                tabbedPane.setComponentAt(2, this.getTextureBackgroundPane());
                tabbedPane.setSelectedIndex(2);
                textureBackgroundPane.populate(background);
            } else if (background instanceof PatternBackground) {
                tabbedPane.setComponentAt(3, this.getPatternBackgroundPane());
                tabbedPane.setSelectedIndex(3);
                patternBackgroundPane.populate(background);
            } else if (background instanceof ImageBackground) {
                tabbedPane.setComponentAt(4, this.getImageBackgroundPane());
                tabbedPane.setSelectedIndex(4);
                imageBackgroundPane.populate(background);
            } else if (background instanceof GradientBackground) {
                tabbedPane.setComponentAt(5, this.getGradientPane());
                tabbedPane.setSelectedIndex(5);
                gradientPane.populate(background);
            }
        }

        tabbedPane.doLayout();
        tabbedPane.validate();
    }

    /**
     * Update background.
     */
    public Background update() {
        try {
            Component selectComponent = tabbedPane.getSelectedComponent();
            if (selectComponent.getClass() == JPanel.class) {//需要初始化.
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == BACKGROUND_NULL) {
                    selectComponent = this.getNullBackgroundPane();
                } else if (selectedIndex == BACKGROUND_COLOR) {
                    selectComponent = this.getColorBackgroundPane();
                } else if (selectedIndex == BACKGROUND_TEXTURE) {
                    selectComponent = this.getTextureBackgroundPane();
                } else if (selectedIndex == BACKGROUND_PATTERN) {
                    selectComponent = this.getPatternBackgroundPane();
                } else if (selectedIndex == BACKGROUND_IMAGE) {
                    selectComponent = this.getImageBackgroundPane();
                } else if (selectedIndex == BACKGROUND_GRADIENT_COLOR) {
                    selectComponent = this.getGradientPane();
                }

                tabbedPane.setComponentAt(selectedIndex, selectComponent);
            }

            if (selectComponent instanceof BackgroundSettingPane) {
                return ((BackgroundSettingPane) selectComponent).update();
            } else if (selectComponent instanceof GradientPane) {
                return ((GradientPane) selectComponent).update();
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        return null;
    }
    /**
     * Change listener.
     */
    private ChangeListener backgroundChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent evt) {
            previewPane.setBackgroundObject(update());
            previewPane.repaint();
        }
    };
    private BackgroundPreviewLabel previewPane = null;

    public static abstract class BackgroundSettingPane extends JPanel {

        public abstract void populate(Background background);

        public abstract Background update() throws Exception;

        public abstract void addChangeListener(ChangeListener changeListener);
    }

    /**
     * Null background pane.
     */
    private static class NullBackgroundPane extends BackgroundSettingPane {

        public NullBackgroundPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            UILabel centerLabel = new UILabel(
                Inter.getLocText("Background-Background_is_NULL") + "...");
            this.add(centerLabel);
            centerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            centerLabel.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
        }

        public void populate(Background background) {
            // do nothing.
        }

        public Background update() throws Exception {
            return null;
        }

        public void addChangeListener(ChangeListener changeListener) {
            // do nothing.
        }
    }

    /**
     * Color background pane.
     */
    private static class ColorBackgroundPane extends BackgroundSettingPane {

        private DetailColorSelectPane detailColorSelectPane;

        public ColorBackgroundPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            detailColorSelectPane = new DetailColorSelectPane();
            this.add(detailColorSelectPane, BorderLayout.CENTER);
        }

        public void populate(Background background) {
            if (background instanceof ColorBackground) {
                ColorBackground colorBackgroud = (ColorBackground) background;
                this.detailColorSelectPane.populate(colorBackgroud.getColor());
            } else {
                this.detailColorSelectPane.populate(Color.white);
            }
        }

        public Background update() throws Exception {
            return ColorBackground.getInstance(this.detailColorSelectPane.update());
        }

        public void addChangeListener(ChangeListener changeListener) {
            detailColorSelectPane.addChangeListener(changeListener);
        }
    }
    
    private static abstract class BPane extends BackgroundSettingPane {
    	BPane(int nColumn) {
            this.initComponents(nColumn);
        }
    	
    	private void initComponents(int nColumn) {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
            this.add(contentPane, BorderLayout.NORTH);
//            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            // type type.

            JPanel typePane=FRGUIPaneFactory.createTitledBorderPane(titleOfTypePane());
            contentPane.add(typePane);
            JPanel typePane2 = new JPanel();
            typePane.add(typePane2);
            typePane2.setLayout(layoutOfTypePane(nColumn));
            setChildrenOfTypePane(typePane2);
            
            setChildrenOfContentPane(contentPane);
    	}
    	
    	protected abstract String titleOfTypePane();
    	protected abstract LayoutManager layoutOfTypePane(int nColumn);
    	protected abstract void setChildrenOfTypePane(JPanel typePane2);
    	protected void setChildrenOfContentPane(JPanel contentPane) {}
    }

    /**
     * Texture background pane.  TODO kunsnat: 拆出去. 真特么的长..
     */
    public static class TextureBackgroundPane extends BPane {

        private TexturePaint texturePaint;
        private TextureButton[] textureButtonArray;
        
        public TextureBackgroundPane() {
        	super(8);// 默认的. 
        }
        
        public TextureBackgroundPane(int colum) {
        	super(colum);// 自定义的. 
        }
        
        protected LayoutManager layoutOfTypePane(int nColumn) {
        	return FRGUIPaneFactory.createNColumnGridLayout(nColumn);
        }
        
        protected String titleOfTypePane() {
        	return Inter.getLocText("Background-Texture");
        }
        protected void setChildrenOfTypePane(JPanel typePane2) {
            ButtonGroup patternButtonGroup = new ButtonGroup();
            textureButtonArray = new TextureButton[EMBED_TEXTURE_PAINT_ARRAY.length];
            for (int i = 0; i < EMBED_TEXTURE_PAINT_ARRAY.length; i++) {
                textureButtonArray[i] = new TextureButton(
                    EMBED_TEXTURE_PAINT_ARRAY[i], EMBED_TEXTURE_PAINT_DES_ARRAY[i]);
                patternButtonGroup.add(textureButtonArray[i]);
                typePane2.add(textureButtonArray[i]);
            }
        }

        public void populate(Background background) {
            if (background instanceof TextureBackground) {
                TextureBackground textureBackground = (TextureBackground) background;

                this.texturePaint = textureBackground.getTexturePaint();

                for (int i = 0; i < textureButtonArray.length; i++) {
                    if (ComparatorUtils.equals(textureButtonArray[i].getTexturePaint(), this.texturePaint)) {
                        textureButtonArray[i].setSelected(true);
                        break;
                    }
                }
            } else {
                this.textureButtonArray[0].setSelected(true);
                this.texturePaint = textureButtonArray[0].getTexturePaint();
            }
        }

        public Background update() throws Exception {
            return new TextureBackground(this.texturePaint);
        }

        public void addChangeListener(ChangeListener changeListener) {
            for (int i = 0; i < this.textureButtonArray.length; i++) {
                this.textureButtonArray[i].addChangeListener(changeListener);
            }
        }

        /**
         * Texture type button.
         */
        class TextureButton extends JToggleButton implements ActionListener {

            private TexturePaint buttonTexturePaint;

            public TextureButton(TexturePaint buttonTexturePaint, String tooltip) {
                this.buttonTexturePaint = buttonTexturePaint;
                this.setToolTipText(tooltip);

                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                this.addActionListener(this);
                this.setBorder(null);
            }

            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                Dimension d = getSize();

                g2d.setPaint(this.buttonTexturePaint);
                GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, d.width - 1,
                    d.height - 1));

                if (ComparatorUtils.equals(texturePaint, this.buttonTexturePaint)) {// it's
                    // selected.
                    g2d.setPaint(Color.black);
                } else {
                    g2d.setPaint(Color.gray);
                }
                GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1,
                    d.height - 1));
            }

            public Dimension getPreferredSize() {
                return new Dimension(36, 32);
            }

            public TexturePaint getTexturePaint() {
                return this.buttonTexturePaint;
            }

            /**
             * set Pattern index.
             */
            public void actionPerformed(ActionEvent evt) {
                TextureBackgroundPane.this.texturePaint = this.getTexturePaint();

                fireChagneListener();
                TextureBackgroundPane.this.repaint(); // repaint.
            }

            public void addChangeListener(ChangeListener changeListener) {
                this.changeListener = changeListener;
            }

            private void fireChagneListener() {
                if (this.changeListener != null) {
                    ChangeEvent evt = new ChangeEvent(this);
                    this.changeListener.stateChanged(evt);
                }
            }
        }
    }
    
    public static class PatternBackgroundPaneNoFore extends PatternBackgroundPane {
    	
    	public PatternBackgroundPaneNoFore(int nColumn) {
    		super(nColumn);
    	}
    	
    	// 重载 不加载两个前后按钮
    	protected void setChildrenOfContentPane(JPanel contentPane) {
    		foregroundColorPane = new ColorSelectBox(80);
    		backgroundColorPane = new ColorSelectBox(80);
    		foregroundColorPane.setSelectObject(Color.lightGray);
    		backgroundColorPane.setSelectObject(Color.black);
    	}
    }

    /**
     * Pattern background pane.
     */
    public static class PatternBackgroundPane extends BPane {

        private int patternIndex = 0; // pattern index.
        protected ColorSelectBox foregroundColorPane;
        protected ColorSelectBox backgroundColorPane;
        private PatternButton[] patternButtonArray;
        
        public PatternBackgroundPane() {
        	super(12);
        	
        	foregroundColorPane.addSelectChangeListener(colorChangeListener);
        	backgroundColorPane.addSelectChangeListener(colorChangeListener);
        }
        
        public PatternBackgroundPane(int nColumn) {
        	super(nColumn);
        	
        	if(foregroundColorPane != null) {
        		foregroundColorPane.addSelectChangeListener(colorChangeListener);
        	}
        	if(backgroundColorPane != null) {
        		backgroundColorPane.addSelectChangeListener(colorChangeListener);
        	}
        }
        
        protected String titleOfTypePane() {
        	return Inter.getLocText("Background-Pattern");
        }
        protected LayoutManager layoutOfTypePane(int nColumn) {
        	return FRGUIPaneFactory.createNColumnGridLayout(nColumn);
        }
        
        protected void setChildrenOfTypePane(JPanel typePane2) {
            ButtonGroup patternButtonGroup = new ButtonGroup();
            patternButtonArray = new PatternButton[PatternBackground.PATTERN_COUNT];
            for (int i = 0; i < PatternBackground.PATTERN_COUNT; i++) {
                patternButtonArray[i] = new PatternButton(i);
                patternButtonGroup.add(patternButtonArray[i]);
                typePane2.add(patternButtonArray[i]);
            }
        }

        protected void setChildrenOfContentPane(JPanel contentPane) {
        	// colors
        	JPanel colorPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Colors"));
        	contentPane.add(colorPane);
        	
            foregroundColorPane = new ColorSelectBox(80);
            backgroundColorPane = new ColorSelectBox(80);
            foregroundColorPane.setSelectObject(Color.lightGray);
            backgroundColorPane.setSelectObject(Color.black);
            
            colorPane.add(Box.createHorizontalStrut(2));
            colorPane.add(this.createLabelColorPane(Inter.getLocText("Foreground")
                + ":", foregroundColorPane));

            colorPane.add(Box.createHorizontalStrut(8));

            colorPane.add(this.createLabelColorPane(Inter.getLocText("Background")
                + ":", backgroundColorPane));
        }

        private JPanel createLabelColorPane(String text,
            ColorSelectBox colorPane) {
            JPanel labelColorPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            labelColorPane.add(new UILabel(text));
            labelColorPane.add(colorPane);

            return labelColorPane;
        }

        public void populate(Background background) {
            if (background != null && background instanceof PatternBackground) {
                PatternBackground patternBackground = (PatternBackground) background;
                int patternIndex = patternBackground.getPatternIndex();

                if (patternIndex >= 0
                    && patternIndex < this.patternButtonArray.length) {
                    this.patternButtonArray[patternIndex].setSelected(true);
                    this.patternIndex = patternIndex;
                } else {
                    this.patternIndex = 0;
                }

                foregroundColorPane.setSelectObject(patternBackground.getForeground());
                backgroundColorPane.setSelectObject(patternBackground.getBackground());
            } else {
                patternIndex = 0;
                this.patternButtonArray[0].setSelected(true);

                foregroundColorPane.setSelectObject(Color.lightGray);
                backgroundColorPane.setSelectObject(Color.black);
            }
        }

        public Background update() throws Exception {
            return new PatternBackground(patternIndex, foregroundColorPane.getSelectObject(), backgroundColorPane.getSelectObject());
        }

        public void addChangeListener(ChangeListener changeListener) {
            foregroundColorPane.addSelectChangeListener(changeListener);
            backgroundColorPane.addSelectChangeListener(changeListener);

            for (int i = 0; i < this.patternButtonArray.length; i++) {
                this.patternButtonArray[i].addChangeListener(changeListener);
            }
        }
        // Foreground or Background changed.
        ChangeListener colorChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                for (int i = 0; i < patternButtonArray.length; i++) {
                    patternButtonArray[i].setPatternForeground(foregroundColorPane.getSelectObject());
                    patternButtonArray[i].setPatternBackground(backgroundColorPane.getSelectObject());
                }

                PatternBackgroundPane.this.repaint();// repaint
            }
        };

        /**
         * Pattern type button.
         */
        class PatternButton extends JToggleButton implements ActionListener {

            public PatternButton(int pIndex) {
                this.pIndex = pIndex;
                this.addActionListener(this);

                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                this.setBorder(null);
                this.patternBackground = new PatternBackground(this.pIndex,
                    Color.lightGray, Color.black);
            }

            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                Dimension d = getSize();
                this.patternBackground.paint(g2d, new Rectangle2D.Double(0, 0,
                    d.width - 1, d.height - 1));

                if (this.pIndex == patternIndex) {// it's selected.
                    g2d.setPaint(new Color(255, 51, 0));
                } else {
                    g2d.setPaint(Color.gray);
                }
                GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1,
                    d.height - 1));
            }

            public Dimension getPreferredSize() {
                return new Dimension(24, 24);
            }

            public void setPatternForeground(Color foreground) {
                this.patternBackground.setForeground(foreground);
            }

            public void setPatternBackground(Color background) {
                this.patternBackground.setBackground(background);
            }

            /**
             * set Pattern index.
             */
            public void actionPerformed(ActionEvent evt) {
                PatternBackgroundPane.this.patternIndex = pIndex;

                fireChagneListener();
                PatternBackgroundPane.this.repaint();// repaint
            }

            public void addChangeListener(ChangeListener changeListener) {
                this.changeListener = changeListener;
            }

            private void fireChagneListener() {
                if (this.changeListener != null) {
                    ChangeEvent evt = new ChangeEvent(this);
                    this.changeListener.stateChanged(evt);
                }
            }
            private int pIndex = 0;
            private PatternBackground patternBackground;
        }
    }

    /**
     * Image background pane.
     */
    private static class ImageBackgroundPane extends BackgroundSettingPane {

        private ImagePreviewPane previewPane = null;
        private Style imageStyle = null;
        private ChangeListener changeListener = null;
        private ImageFileChooser imageFileChooser = null;
        private UILabel imageSizeLabel = new UILabel();
        
        private JRadioButton defaultRadioButton = null;
        private JRadioButton tiledRadioButton = null;
        private JRadioButton extendRadioButton = null;
        private JRadioButton adjustRadioButton = null;

        public ImageBackgroundPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            // preview pane
            JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
            this.add(previewContainerPane, BorderLayout.CENTER);

            JPanel previewOwnerPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preview"));
            previewOwnerPane.setLayout(new BorderLayout());
            previewContainerPane.add(previewOwnerPane, BorderLayout.CENTER);


            previewPane = new ImagePreviewPane();
            previewOwnerPane.add(new JScrollPane(previewPane));
            previewPane.addChangeListener(imageSizeChangeListener);

            JPanel selectFilePane = FRGUIPaneFactory.createBorderLayout_L_Pane();
            previewContainerPane.add(selectFilePane, BorderLayout.EAST);
            selectFilePane.setBorder(BorderFactory.createEmptyBorder(8, 2, 4, 0));

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
            if(!isBrowserBackgroundPane){
		        jp.add(extendRadioButton);
		        jp.add(adjustRadioButton);
            }
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
                int returnVal = imageFileChooser.showOpenDialog(ImageBackgroundPane.this);
                if (returnVal != JFileChooser.CANCEL_OPTION) {
                    File selectedFile = imageFileChooser.getSelectedFile();

                    if (selectedFile != null && selectedFile.isFile()) {
                        Image image = BaseUtils.readImage(selectedFile.getPath());
                        CoreGraphHelper.waitForImage(image);

                        previewPane.setImage(image);
                        setImageStyle();
                        previewPane.setImageStyle(imageStyle);
                        previewPane.repaint();
                    } else {
                        previewPane.setImage(null);
                    }
                }

                fireChagneListener();
            }
        };
        
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

            private void changeImageStyle() {
                previewPane.setImageStyle(ImageBackgroundPane.this.imageStyle);
                previewPane.repaint();
            }
        };

        public void populate(Background background) {

            if (background instanceof ImageBackground) {
                ImageBackground imageBackground = (ImageBackground) background;
                
                if (imageBackground.getLayout() == Constants.IMAGE_CENTER) {
                    defaultRadioButton.setSelected(true);
                    imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_CENTER);
                }else if(imageBackground.getLayout() == Constants.IMAGE_EXTEND){
            		extendRadioButton.setSelected(true);
            		imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_EXTEND);
            	}else if(imageBackground.getLayout() == Constants.IMAGE_ADJUST){
            		adjustRadioButton.setSelected(true);
            		imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_ADJUST);
            	}else {
                    tiledRadioButton.setSelected(true);
                    imageStyle = Style.DEFAULT_STYLE.deriveImageLayout(Constants.IMAGE_TILED);
                }
                
                previewPane.setImageStyle(ImageBackgroundPane.this.imageStyle);
                if (imageBackground.getImage() != null) {
                    previewPane.setImage(imageBackground.getImage());
                    imageSizeLabel.setText(previewPane.getImage().getWidth(null)
                        + " X " + previewPane.getImage().getHeight(null));
                }

                if (imageBackground.getImage() != null) {
                    previewPane.setImage(imageBackground.getImage());
                }
            } else {
                previewPane.setImage(null);
                tiledRadioButton.setSelected(true);

                imageSizeLabel.setText("");
            }

            fireChagneListener();
        }

        public Background update() throws Exception {
            ImageBackground imageBackground = new ImageBackground(previewPane.getImage());
            setImageStyle();
            imageBackground.setLayout(imageStyle.getImageLayout());
            return imageBackground;
        }

        public void addChangeListener(ChangeListener changeListener) {
            this.changeListener = changeListener;
        }

        private void fireChagneListener() {
            if (this.changeListener != null) {
                ChangeEvent evt = new ChangeEvent(this);
                this.changeListener.stateChanged(evt);
            }
        }
        ChangeListener imageSizeChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                Image image = ((ImagePreviewPane) evt.getSource()).getImage();

                if (image == null) {
                    imageSizeLabel.setText("");
                } else {
                    imageSizeLabel.setText(Inter.getLocText(new String[] {"Size", "px"}, new String[] {": "  + image.getWidth(null) + "x" + image.getHeight(null)}));
                }
            }
        };
    }
    
    public static final TexturePaint[] EMBED_TEXTURE_PAINT_ARRAY = new TexturePaint[]{
        TextureBackground.NEWSPRINT_TEXTURE_PAINT,
        TextureBackground.RECYCLED_PAPER_TEXTURE_PAINT,
        TextureBackground.PARCHMENT_TEXTURE_PAINT,
        TextureBackground.STATIONERY_TEXTURE_PAINT,
        TextureBackground.GREEN_MARBLE_TEXTURE_PAINT,
        TextureBackground.WHITE_MARBLE_TEXTURE_PAINT,
        TextureBackground.BROWN_MARBLE_TEXTURE_PAINT,
        TextureBackground.GRANITE_TEXTURE_PAINT,
        TextureBackground.BLUE_TISSUE_PAPER_TEXTURE_PAINT,
        TextureBackground.PINK_TISSUE_PAPER_TEXTURE_PAINT,
        TextureBackground.PURPLE_MESH_TEXTURE_PAINT,
        TextureBackground.BOUQUET_TEXTURE_PAINT,
        TextureBackground.PAPYRUS_TEXTURE_PAINT,
        TextureBackground.CANVAS_TEXTURE_PAINT,
        TextureBackground.DENIM_TEXTURE_PAINT,
        TextureBackground.WOVEN_MAT_TEXTURE_PAINT,
        TextureBackground.WATER_DROPLETS_TEXTURE_PAINT,
        TextureBackground.PAPER_BAG_TEXTURE_PAINT,
        TextureBackground.FISH_FOSSIL_TEXTURE_PAINT,
        TextureBackground.SAND_TEXTURE_PAINT,
        TextureBackground.CORK_TEXTURE_PAINT,
        TextureBackground.WALNUT_TEXTURE_PAINT,
        TextureBackground.OAK_TEXTURE_PAINT,
        TextureBackground.MEDIUM_WOOD_TEXTURE_PAINT};
    private static final String[] EMBED_TEXTURE_PAINT_DES_ARRAY = new String[]{
        Inter.getLocText("BackgroundTexture-Newsprint"),
        Inter.getLocText("BackgroundTexture-RecycledPaper"),
        Inter.getLocText("BackgroundTexture-Parchment"),
        Inter.getLocText("BackgroundTexture-Stationery"),
        Inter.getLocText("BackgroundTexture-GreenMarble"),
        Inter.getLocText("BackgroundTexture-WhiteMarble"),
        Inter.getLocText("BackgroundTexture-BrownMarble"),
        Inter.getLocText("BackgroundTexture-Granite"),
        Inter.getLocText("BackgroundTexture-BlueTissuePaper"),
        Inter.getLocText("BackgroundTexture-PinkTissuePaper"),
        Inter.getLocText("BackgroundTexture-PurpleMesh"),
        Inter.getLocText("BackgroundTexture-Bouquet"),
        Inter.getLocText("BackgroundTexture-Papyrus"),
        Inter.getLocText("BackgroundTexture-Canvas"),
        Inter.getLocText("BackgroundTexture-Denim"),
        Inter.getLocText("BackgroundTexture-WovenMat"),
        Inter.getLocText("BackgroundTexture-WaterDroplets"),
        Inter.getLocText("BackgroundTexture-PaperBag"),
        Inter.getLocText("BackgroundTexture-FishFossil"),
        Inter.getLocText("BackgroundTexture-Sand"),
        Inter.getLocText("BackgroundTexture-Cork"),
        Inter.getLocText("BackgroundTexture-Walnut"),
        Inter.getLocText("BackgroundTexture-Oak"),
        Inter.getLocText("BackgroundTexture-MediumWood")
    };
}