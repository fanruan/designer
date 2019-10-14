package com.fr.design.style.color;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.fr.design.gui.ibutton.SpecialUIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.islider.UISlider;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.log.FineLoggerFactory;


/**
 * 颜色选择器自定义面板
 *
 * @author focus
 */
class CustomChooserPanel extends AbstractColorChooserPanel implements ColorSelectable {
    /**
     * The gradient image displayed.
     */
    private transient Image gradientImage;

    /**
     * The Panel that holds the gradient image.
     */
    private transient JPanel gradientPanel;

    /**
     * The track gradient image.
     */
    private transient Image trackImage;

    /**
     * The panel that holds the track.
     */
    private transient JPanel trackPanel;

    /**
     * The slider for the locked HSB value.
     */
    private transient UISlider slider;

    /**
     * The RadioButton that controls the Hue.
     */
    private transient UIRadioButton hRadio;

    /**
     * The RadioButton that controls the Saturation.
     */
    private transient UIRadioButton sRadio;

    /**
     * The RadioButton that controls the Brightness.
     */
    private transient UIRadioButton bRadio;

    /**
     * The UIBasicSpinner that controls the Hue.
     */
    private transient UIBasicSpinner hSpinner;

    /**
     * The UIBasicSpinner that controls the Saturation.
     */
    private transient UIBasicSpinner sSpinner;

    /**
     * The UIBasicSpinner that controls the Brightness.
     */
    private transient UIBasicSpinner bSpinner;

    /**
     * The UIBasicSpinner that controls the Red.
     */
    private transient UIBasicSpinner rSpinner;

    /**
     * The UIBasicSpinner that controls the Green.
     */
    private transient UIBasicSpinner gSpinner;

    /**
     * The UIBasicSpinner that controls the Blue.
     */
    private transient UIBasicSpinner bbSpinner;

    private transient UITextField field;

//    private transient PickColorButton pickColorButton;


    /**
     * The default width of the gradient image.
     */
    private static final int IMG_WIDTH = 200;

    /**
     * The default height of the gradient image.
     */
    private static final int IMG_HEIGHT = 205;

    /**
     * The default width of the track gradient.
     */
    private static final int TRACK_WIDTH = 20;

    /**
     * The UILabel for Red.
     */
    private static final UILabel R = new UILabel("R");

    /**
     * The UILabel for Green.
     */
    private static final UILabel G = new UILabel("G");

    /**
     * The UILabel for Blue.
     */
    private static final UILabel B = new UILabel("B");

    private static final int H_MAX = 365;
    private static final int S_MAX = 100;
    private static final int L_MAX = 100;

    private static final float HSPINNER_VALUE = 360f;
    private static final float SSPINNER_VALUE = 100f;
    private static final float LSPINNER_VALUE = 100f;

    private static final int BINARY_FOR_EIGHT = 255;
    private static final int BINARY_FOR_FOUR = 16;
    private static final int BINARY_FOR_THTEE = 8;

    private static final int TWENTY_FOUR = 24;
    private static final int HEX_FF = 0xff;

    // 占位label
    private static final UILabel PLACE_HOLDER_LABEL = new UILabel();

    private ImageRGBScrollListener rgbScroll = new ImageRGBScrollListener();


    /**
     * The point that is displayed in the gradient image.
     */
    private transient Point gradientPoint = new Point();

    /**
     * This indicates that the change to the slider or point is triggered
     * internally.
     */
    private transient boolean internalChange = false;

    /**
     * This indicates that the change to the spinner is triggered internally.
     */
    private transient boolean spinnerTrigger = false;

    /**
     * This int identifies which spinner is currently locked.
     */
    private transient int locked = -1;

    /**
     * This value indicates that the Hue spinner is locked.
     */
    static final int HLOCKED = 0;

    /**
     * This value indicates that the Saturation spinner is locked.
     */
    static final int SLOCKED = 1;

    /**
     * This value indicates that the Brightness spinner is locked.
     */
    static final int BLOCKED = 2;

    /**
     * This method indicates that the mouse event is in the process of being
     * handled.
     */
    private transient boolean handlingMouse;

    // 文本监听器，监听十六进制文本输入
    class TextDocumentListener implements DocumentListener {

        // 十六进制颜色验证的正则表达式
        private static final String HEX_PATTERN = "([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        private Pattern pattern;
        private Matcher matcher;

        @Override
        public void insertUpdate(DocumentEvent e) {
            Document doc = e.getDocument();
            updateColorForHex(doc);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            Document doc = e.getDocument();
            updateColorForHex(doc);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        private void updateColorForHex(Document doc) {
            try {
                String str = doc.getText(0, doc.getLength());
                pattern = Pattern.compile(HEX_PATTERN);
                matcher = pattern.matcher(str);
                if (matcher.matches()) {
                    Color color = new Color(Integer.parseInt(str, 16));
                    CustomChooserPanel.this.getColorSelectionModel().setSelectedColor(color);
                }

            } catch (BadLocationException e1) {
                FineLoggerFactory.getLogger().error(e1.getMessage());
            }
        }
    }

    /**
     * This helper class handles mouse events on the gradient image.
     */
    class MainGradientMouseListener extends MouseAdapter
            implements MouseMotionListener {
        /**
         * This method is called when the mouse is pressed over the gradient
         * image. The JColorChooser is then updated with new HSB values.
         *
         * @param e The MouseEvent.
         */
        public void mousePressed(MouseEvent e) {
            gradientPoint = e.getPoint();
            update(e.getPoint());
        }

        /**
         * This method is called when the mouse is dragged over the gradient
         * image. The JColorChooser is then updated with the new HSB values.
         *
         * @param e The MouseEvent.
         */
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            if (isMouseOutOfImage(p)) {
                return;
            }

            gradientPoint = p;
            update(p);
        }

        private boolean isMouseOutOfImage(Point p) {
            return (p.x < 0 || p.y < 0 || p.y > IMG_HEIGHT || p.x > IMG_WIDTH);
        }

        /**
         * This method is called when the mouse is moved over the gradient image.
         *
         * @param e The MouseEvent.
         */
        public void mouseMoved(MouseEvent e) {
            // Do nothing.
        }

        /**
         * This method updates the JColorChooser with the new values.
         *
         * @param p The Point where the MouseEvent occurred.
         */
        private void update(Point p) {
            handlingMouse = true;
            if (hSpinner.isEnabled()) {
                updateH(p);
            } else if (sSpinner.isEnabled()) {
                updateS(p);
            } else {
                updateB(p);
            }
            handlingMouse = false;
        }

        /**
         * This method updates the SB values if Hue is locked.
         *
         * @param p The point where the MouseEvent occurred.
         */
        private void updateH(Point p) {
            float s = (IMG_WIDTH - p.x * 1f) / IMG_WIDTH;
            float b = (IMG_HEIGHT - p.y * 1f) / IMG_HEIGHT;

            // Avoid two changes to the model by changing internalChange to true.
            internalChange = true;
            sSpinner.setValue(new Integer((int) (s * S_MAX)));
            internalChange = false;
            bSpinner.setValue(new Integer((int) (b * L_MAX)));

            revalidate();
        }

        /**
         * This method updates the HB values if Saturation is locked.
         *
         * @param p The point where the MouseEvent occurred.
         */
        private void updateS(Point p) {
            double h = p.x * 1D / IMG_WIDTH;
            double b = (IMG_HEIGHT - p.y * 1D) / IMG_HEIGHT;

            internalChange = true;
            hSpinner.setValue(new Integer((int) (h * H_MAX)));
            internalChange = false;
            bSpinner.setValue(new Integer((int) (b * L_MAX)));

            revalidate();
        }

        /**
         * This method updates the HS values if Brightness is locked.
         *
         * @param p The point where the MouseEvent occurred.
         */
        private void updateB(Point p) {
            double h = p.x * 1D / IMG_WIDTH;
            double s = (IMG_HEIGHT - p.y * 1D) / IMG_HEIGHT;

            internalChange = true;
            hSpinner.setValue(new Integer((int) (h * H_MAX)));
            internalChange = false;
            sSpinner.setValue(new Integer((int) (s * S_MAX)));

            revalidate();
        }
    }

    /**
     * This method listens for slider value changes.
     */
    class SliderChangeListener implements ChangeListener {
        /**
         * This method is called when the slider value changes. It should change
         * the color of the JColorChooser.
         *
         * @param e The ChangeEvent.
         */
        public void stateChanged(ChangeEvent e) {
            if (internalChange) {
                return;
            }


            Integer value = new Integer(slider.getValue());

            switch (locked) {
                case HLOCKED:
                    hSpinner.setValue(value);
                    break;
                case SLOCKED:
                    sSpinner.setValue(value);
                    break;
                case BLOCKED:
                    bSpinner.setValue(value);
                    break;
            }
            updateImageAndTrack();
        }
    }

    /**
     * This helper class determines the active UIBasicSpinner.
     */
    class RadioStateListener extends MouseAdapter
            implements MouseMotionListener {
        /**
         * This method is called when there is a new UIRadioButton that was
         * selected. As a result, it should activate the associated UIBasicSpinner.
         *
         * @param e The ChangeEvent.
         */
        public void mousePressed(MouseEvent e) {
            UIBasicSpinner change;
            if (e.getSource() == hRadio) {
                locked = HLOCKED;
                change = hSpinner;
            } else if (e.getSource() == sRadio) {
                locked = SLOCKED;
                change = sSpinner;
            } else {
                locked = BLOCKED;
                change = bSpinner;
            }

            hSpinner.setEnabled(false);
            sSpinner.setEnabled(false);
            bSpinner.setEnabled(false);
            change.setEnabled(true);
            updateSlider();
            updateTrack();
            updateImage();
            repaint();

        }
    }

    /**
     * hsl 监听
     */
    class ImageScrollListener implements ChangeListener {
        /**
         * This method is called whenever one of the UIBasicSpinner values change. The
         * JColorChooser should be updated with the new HSB values.
         *
         * @param e The ChangeEvent.
         */
        public void stateChanged(ChangeEvent e) {
            if (internalChange) {
                return;
            }
            updateImageAndTrack();
            updateSlider();
        }
    }

    /**
     * rgb 监听
     */
    class ImageRGBScrollListener implements ChangeListener {
        /**
         * This method is called whenever one of the UIBasicSpinner values change. The
         * JColorChooser should be updated with the new HSB values.
         *
         * @param e The ChangeEvent.
         */
        public void stateChanged(ChangeEvent e) {
            if (internalChange) {
                return;
            }


            int r = (Integer) rSpinner.getValue();
            int g = (Integer) gSpinner.getValue();
            int bb = (Integer) bbSpinner.getValue();

            spinnerTrigger = true;
            getColorSelectionModel().setSelectedColor(new Color(r, g, bb));
            spinnerTrigger = false;

            updateChooser();
            repaint();
        }
    }

    /**
     * Creates a new DefaultHSBChooserPanel object.
     */
    CustomChooserPanel() {
        super();
    }

    /**
     * This method returns the name displayed by the JColorChooser tab that
     * holds this panel.
     *
     * @return The name displayed in the JColorChooser tab.
     */
    public String getDisplayName() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom");
    }

    /**
     * This method updates the various components inside the HSBPanel (the
     * UIBasicSpinners, the JSlider, and the gradient image point) with updated
     * values when the JColorChooser color value changes.
     */
    public void updateChooser() {
        Color c = getColorSelectionModel().getSelectedColor();
        float[] hsbVals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),
                null);
        internalChange = true;

        adjustHSLValue(hsbVals);

        internalChange = false;

        if (!handlingMouse && slider != null && !slider.getValueIsAdjusting()) {
            updateImage();
        }

        if (!handlingMouse || locked != HLOCKED) {
            updateTrack();
        }
        updateTextFields();
        updateHexFields();
    }

    private void adjustHSLValue(float[] hsbVals) {
        if (!spinnerTrigger) {
            hSpinner.setValue(new Integer((int) ((double) hsbVals[0] * HSPINNER_VALUE)));
            sSpinner.setValue(new Integer((int) ((double) hsbVals[1] * SSPINNER_VALUE)));
            bSpinner.setValue(new Integer((int) ((double) hsbVals[2] * LSPINNER_VALUE)));
        }
        switch (locked) {
            case HLOCKED:
                if (slider != null) {
                    slider.setValue(((Number) hSpinner.getValue()).intValue());
                }
                if (!handlingMouse) {
                    gradientPoint.x = (int) ((1
                            - ((Number) sSpinner.getValue()).intValue() / (double) SSPINNER_VALUE) * IMG_WIDTH);
                    gradientPoint.y = (int) ((1
                            - ((Number) bSpinner.getValue()).intValue() / (double) LSPINNER_VALUE) * IMG_HEIGHT);
                }
                break;
            case SLOCKED:
                if (slider != null) {
                    slider.setValue(((Number) sSpinner.getValue()).intValue());
                }
                if (!handlingMouse) {
                    gradientPoint.x = (int) (((Number) hSpinner.getValue()).intValue() / (double) HSPINNER_VALUE * IMG_WIDTH);
                    gradientPoint.y = (int) ((1
                            - ((Number) bSpinner.getValue()).intValue() / (double) LSPINNER_VALUE) * IMG_HEIGHT);
                }
                break;
            case BLOCKED:
                if (slider != null) {
                    slider.setValue(((Number) bSpinner.getValue()).intValue());
                }
                if (!handlingMouse) {
                    gradientPoint.x = (int) (((Number) hSpinner.getValue()).intValue() / (double) HSPINNER_VALUE * IMG_WIDTH);
                    gradientPoint.y = (int) ((1
                            - ((Number) sSpinner.getValue()).intValue() / (double) SSPINNER_VALUE) * IMG_HEIGHT);
                }
                break;
        }
    }

    private void updateImageAndTrack() {
        float h = (float) (((Number) hSpinner.getValue()).intValue() / (double) HSPINNER_VALUE);
        float s = (float) (((Number) sSpinner.getValue()).intValue() / (double) SSPINNER_VALUE);
        float b = (float) (((Number) bSpinner.getValue()).intValue() / (double) LSPINNER_VALUE);

        spinnerTrigger = true;
        getColorSelectionModel().setSelectedColor(new Color(Color.HSBtoRGB(h, s, b)));
        spinnerTrigger = false;

        if (!handlingMouse && slider != null && !slider.getValueIsAdjusting()) {
            updateImage();
            updateTrack();
        }
        updateTextFields();
        updateHexFields();
        repaint();
    }

    /**
     * This method builds the DefaultHSBChooserPanel.
     */
    protected void buildChooser() {
        setLayout(new BorderLayout());
        add(buildRightPanel(), BorderLayout.EAST);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        gradientPanel = createGradientPanel();
        MouseAdapter ml = new MainGradientMouseListener();
        gradientPanel.addMouseListener(ml);
        gradientPanel.addMouseMotionListener((MouseMotionListener) ml);

        trackPanel = createTrackPanel();

        slider = new UISlider();
        slider.setPaintTrack(false);
        slider.setPaintTicks(false);

        slider.setOrientation(SwingConstants.VERTICAL);

        updateSlider();

        container.add(gradientPanel, BorderLayout.WEST);
        container.add(slider, BorderLayout.CENTER);
        container.add(trackPanel, BorderLayout.EAST);

        add(container, BorderLayout.WEST);
        slider.addChangeListener(new SliderChangeListener());

        updateTextFields();
        updateHexFields();
        repaint();
    }

    private JPanel createGradientPanel() {
        return new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(IMG_WIDTH, IMG_HEIGHT);
            }

            public void paint(Graphics g) {
                if (gradientImage != null) {
                    g.drawImage(gradientImage, 0, 0, this);
                }

                Color saved = g.getColor();
                g.setColor(Color.WHITE);
                g.drawOval(gradientPoint.x - 3, gradientPoint.y - 3, 6, 6);
                g.setColor(saved);
            }
        };
    }

    private JPanel createTrackPanel() {
        return new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(TRACK_WIDTH, IMG_HEIGHT);
            }

            public void paint(Graphics g) {
                if (trackImage != null) {
                    g.drawImage(trackImage, 0, 0, this);
                }

            }
        };
    }

    /**
     * This method uninstalls the DefaultHSBPanel.
     *
     * @param chooser The JColorChooser to remove this panel from.
     */
    public void uninstallChooserPanel(JColorChooser chooser) {
        trackImage = null;
        gradientImage = null;
        gradientPanel = null;
        slider = null;

        hSpinner = null;
        sSpinner = null;
        bSpinner = null;

        hRadio = null;
        sRadio = null;
        bRadio = null;

        removeAll();
        super.uninstallChooserPanel(chooser);
    }

    /**
     * This helper method creates the right side panel (the panel with the
     * Spinners and TextFields).
     *
     * @return The right side panel.
     */
    private Container buildRightPanel() {
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // 主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 10));
        UILabel label = new UILabel();
        label.setSize(200, 200);
        container.add(label);
        container.add(mainPanel);

        // hsl和rgb面板
        JPanel hslAndRgbPanel = new JPanel();
        hslAndRgbPanel.setLayout(new BorderLayout(0, 16));
        hslAndRgbPanel.add(initialHSLPanel(), BorderLayout.CENTER);
        hslAndRgbPanel.add(initialRGBPanel(), BorderLayout.SOUTH);

        // 十六进制面板
        JPanel hexPanel = new JPanel();
        hexPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        hexPanel.add(new UILabel("#"));
        hexPanel.add(field);

        mainPanel.add(hslAndRgbPanel, BorderLayout.CENTER);
        mainPanel.add(hexPanel, BorderLayout.SOUTH);

        JPanel rightPane = new JPanel(new BorderLayout());
        JButton pickColorButton = PickColorButtonFactory.getPickColorButton(this, PickColorButtonFactory.IconType.ICON18, true);
        JPanel blankArea = new JPanel();
        blankArea.setPreferredSize(new Dimension(100, 175));
        rightPane.add(blankArea, BorderLayout.CENTER);
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(pickColorButton, BorderLayout.WEST);
        rightPane.add(buttonPane, BorderLayout.SOUTH);
        container.add(rightPane);

        return container;
    }

    //初始化HSL部分
    private JPanel initialHSLPanel() {
        hRadio = new UIRadioButton("H");
        sRadio = new UIRadioButton("S");
        bRadio = new UIRadioButton("L");
        ButtonGroup group = new ButtonGroup();
        group.add(hRadio);
        group.add(sRadio);
        group.add(bRadio);

        hSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 359, 1));
        sSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        bSpinner = new UIBasicSpinner(new SpinnerNumberModel(100, 0, 100, 1));
        hSpinner.setEnabled(true);
        sSpinner.setEnabled(false);
        bSpinner.setEnabled(false);

        MouseAdapter cl = new RadioStateListener();
        locked = HLOCKED;
        hRadio.setSelected(true);
        hRadio.addMouseListener(cl);
        sRadio.addMouseListener(cl);
        bRadio.addMouseListener(cl);

        ChangeListener scroll = new ImageScrollListener();
        hSpinner.addChangeListener(scroll);
        sSpinner.addChangeListener(scroll);
        bSpinner.addChangeListener(scroll);

        JPanel hslPanel = new JPanel();
        hslPanel.setLayout(new GridLayout(3, 0, 0, 5));

        JPanel hPanel = new JPanel();
        hPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        hPanel.add(hRadio);
        hPanel.add(hSpinner);
        hslPanel.add(hPanel);

        JPanel sPanel = new JPanel();
        sPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sPanel.add(sRadio);
        sPanel.add(sSpinner);
        hslPanel.add(sPanel);

        JPanel lPanel = new JPanel();
        lPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        lPanel.add(bRadio);
        lPanel.add(bSpinner);
        hslPanel.add(lPanel);
        return hslPanel;
    }

    private JPanel initialRGBPanel() {
        // 初始化RGB部分
        rSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        gSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        bbSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 255, 1));

        rSpinner.setEnabled(true);
        gSpinner.setEnabled(true);
        bbSpinner.setEnabled(true);

        addRGBSpinnerChangeLisener();

        TextDocumentListener listen = new TextDocumentListener();
        field = new UITextField();
        field.setPreferredSize(new Dimension(70, 18));
        field.getDocument().addDocumentListener(listen);

        JPanel rgbPanel = new JPanel();
        rgbPanel.setLayout(new GridLayout(3, 0, 0, 5));

        JPanel rPanel = new JPanel();
        rPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rPanel.add(R);
        rPanel.add(rSpinner);
        rgbPanel.add(rPanel);

        JPanel gPanel = new JPanel();
        gPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        gPanel.add(G);
        gPanel.add(gSpinner);
        rgbPanel.add(gPanel);

        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bPanel.add(B);
        bPanel.add(bbSpinner);
        rgbPanel.add(bPanel);

        return rgbPanel;
    }

    /**
     * This method returns the small display icon.
     *
     * @return The small display icon.
     */
    public Icon getSmallDisplayIcon() {
        return null;
    }

    /**
     * This method returns the large display icon.
     *
     * @return The large display icon.
     */
    public Icon getLargeDisplayIcon() {
        return null;
    }

    /**
     * This method updates the gradient image with a new one taking the Hue
     * value as the constant.
     */
    private void updateHLockImage() {
        int index = 0;
        int[] pix = new int[IMG_WIDTH * IMG_HEIGHT];
        float hValue = (float)(((Number) hSpinner.getValue()).intValue() / (double) HSPINNER_VALUE );

        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < IMG_WIDTH; i++) {
                pix[index++] = Color.HSBtoRGB(hValue, (float) ((IMG_WIDTH - i * 1D) / IMG_WIDTH),
                        (float) ((IMG_HEIGHT - j * 1D) / IMG_HEIGHT)) | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        gradientImage = createImage(new MemoryImageSource(IMG_WIDTH, IMG_HEIGHT,
                pix, 0, IMG_WIDTH));
    }

    /**
     * This method updates the gradient image with a new one taking the
     * Brightness value as the constant.
     */
    private void updateBLockImage() {
        int[] pix = new int[IMG_WIDTH * IMG_HEIGHT];
        float bValue = ((Number) bSpinner.getValue()).intValue() / LSPINNER_VALUE;

        int index = 0;
        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < IMG_WIDTH; i++) {
                pix[index++] = Color.HSBtoRGB(i * 1f / IMG_WIDTH,
                        (IMG_HEIGHT - j * 1f) / IMG_HEIGHT, bValue) | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        gradientImage = createImage(new MemoryImageSource(IMG_WIDTH, IMG_HEIGHT,
                pix, 0, IMG_WIDTH));
    }

    /**
     * This method updates the gradient image with a new one taking the
     * Saturation value as the constant.
     */
    private void updateSLockImage() {
        int[] pix = new int[IMG_WIDTH * IMG_HEIGHT];
        float sValue = ((Number) sSpinner.getValue()).intValue() / SSPINNER_VALUE;

        int index = 0;
        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < IMG_WIDTH; i++) {
                pix[index++] = Color.HSBtoRGB(i * 1f / IMG_WIDTH, sValue,
                        (IMG_HEIGHT - j * 1f) / IMG_HEIGHT) | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        gradientImage = createImage(new MemoryImageSource(IMG_WIDTH, IMG_HEIGHT,
                pix, 0, IMG_WIDTH));
    }

    /**
     * This method calls the appropriate method to update the gradient image
     * depending on which HSB value is constant.
     */
    private void updateImage() {
        switch (locked) {
            case HLOCKED:
                updateHLockImage();
                break;
            case SLOCKED:
                updateSLockImage();
                break;
            case BLOCKED:
                updateBLockImage();
                break;
        }
    }

    /**
     * This method updates the TextFields with the correct RGB values.
     */
    private void updateTextFields() {
        int c = getColorSelectionModel().getSelectedColor().getRGB();

        removeRGBSpinnerChangeLisener();
        rSpinner.setValue(Integer.parseInt("" + (c >> BINARY_FOR_FOUR & HEX_FF)));
        gSpinner.setValue(Integer.parseInt("" + (c >> BINARY_FOR_THTEE & HEX_FF)));
        bbSpinner.setValue(Integer.parseInt("" + (c & HEX_FF)));
        addRGBSpinnerChangeLisener();

        repaint();
    }

    private void removeRGBSpinnerChangeLisener() {
        rSpinner.removeChangeListener(rgbScroll);
        gSpinner.removeChangeListener(rgbScroll);
        bbSpinner.removeChangeListener(rgbScroll);
    }

    private void addRGBSpinnerChangeLisener() {
        rSpinner.addChangeListener(rgbScroll);
        gSpinner.addChangeListener(rgbScroll);
        bbSpinner.addChangeListener(rgbScroll);
    }

    private void updateHexFields() {
        Color color = getColorSelectionModel().getSelectedColor();

        String R = Integer.toHexString(color.getRed());
        R = R.length() < 2 ? ('0' + R) : R;
        String B = Integer.toHexString(color.getBlue());
        B = B.length() < 2 ? ('0' + B) : B;
        String G = Integer.toHexString(color.getGreen());
        G = G.length() < 2 ? ('0' + G) : G;

        try {
            field.setText(R + G + B);
        } catch (Exception e) {
            // 因为有了DocumentListener的监听，导致setText()的时候报错但不影响使用
            // 所以只捕捉，不处理
        }
        repaint();
    }

    /**
     * This method updates the slider in response to making a different HSB
     * property the constant.
     */
    private void updateSlider() {
        if (slider == null) {
            return;
        }

        slider.setMinimum(0);
        if (locked == HLOCKED) {
            internalChange = true;
            slider.setValue(((Number) hSpinner.getValue()).intValue());
            slider.setMaximum(359);
            internalChange = false;
            slider.setInverted(true);
        } else {
            slider.setInverted(false);
            if (locked == SLOCKED) {
                slider.setValue(((Number) sSpinner.getValue()).intValue());
            } else {
                slider.setValue(((Number) bSpinner.getValue()).intValue());
            }
            slider.setMaximum(100);
            slider.setInverted(false);
        }
        repaint();
    }

    /**
     * This method updates the track gradient image depending on which HSB
     * property is constant.
     */
    private void updateTrack() {
        switch (locked) {
            case HLOCKED:
                updateHTrack();
                break;
            case SLOCKED:
                updateSTrack();
                break;
            case BLOCKED:
                updateBTrack();
                break;
        }
    }

    /**
     * This method updates the track gradient image if the Hue value is allowed
     * to change (according to the UIRadioButtons).
     */
    private void updateHTrack() {
        int trackIndex = 0;
        int[] trackPix = new int[TRACK_WIDTH * IMG_HEIGHT];

        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < TRACK_WIDTH; i++) {
                trackPix[trackIndex++] = Color.HSBtoRGB(j * 1f / IMG_HEIGHT, 1f, 1f)
                        | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        trackImage = createImage(new MemoryImageSource(TRACK_WIDTH, IMG_HEIGHT,
                trackPix, 0, TRACK_WIDTH));
    }

    /**
     * This method updates the track gradient image if the Saturation value is
     * allowed to change (according to the UIRadioButtons).
     */
    private void updateSTrack() {
        int[] trackPix = new int[TRACK_WIDTH * IMG_HEIGHT];

        float hValue = ((Number) hSpinner.getValue()).intValue() / HSPINNER_VALUE;
        float bValue = ((Number) bSpinner.getValue()).intValue() / LSPINNER_VALUE;

        int trackIndex = 0;
        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < TRACK_WIDTH; i++) {
                trackPix[trackIndex++] = Color.HSBtoRGB(hValue,
                        (IMG_HEIGHT - j * 1f) / IMG_HEIGHT,
                        bValue) | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        trackImage = createImage(new MemoryImageSource(TRACK_WIDTH, IMG_HEIGHT,
                trackPix, 0, TRACK_WIDTH));
    }

    /**
     * This method updates the track gradient image if the Brightness value is
     * allowed to change (according to the UIRadioButtons).
     */
    private void updateBTrack() {
        int[] trackPix = new int[TRACK_WIDTH * IMG_HEIGHT];

        float hValue = ((Number) hSpinner.getValue()).intValue() / HSPINNER_VALUE;
        float sValue = ((Number) sSpinner.getValue()).intValue() / SSPINNER_VALUE;

        int trackIndex = 0;
        for (int j = 0; j < IMG_HEIGHT; j++) {
            for (int i = 0; i < TRACK_WIDTH; i++) {
                trackPix[trackIndex++] = Color.HSBtoRGB(hValue, sValue,
                        (IMG_HEIGHT - j * 1f) / IMG_HEIGHT) | (BINARY_FOR_EIGHT << TWENTY_FOUR);
            }
        }

        trackImage = createImage(new MemoryImageSource(TRACK_WIDTH, IMG_HEIGHT,
                trackPix, 0, TRACK_WIDTH));
    }

    public Color getColor() {
        return getColorSelectionModel().getSelectedColor();
    }

    public void setColor(Color color) {
        getColorSelectionModel().setSelectedColor(color);
    }

    public void colorSetted(ColorCell cc) {
    }

}
