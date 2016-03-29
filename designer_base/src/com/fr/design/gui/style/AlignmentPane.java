package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.fun.IndentationUnitProcessor;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.report.fun.VerticalTextProcessor;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pane to edit cell alignment.
 */
public class AlignmentPane extends AbstractBasicStylePane implements GlobalNameObserver {
	private static final int ANGEL = 90;

	private static final String[] TEXT = {Inter.getLocText("StyleAlignment-Wrap_Text"), Inter.getLocText("StyleAlignment-Single_Line"),
			Inter.getLocText("StyleAlignment-Single_Line(Adjust_Font)"), Inter.getLocText("StyleAlignment-Multi_Line(Adjust_Font)")};

	private static final String[] LAYOUT = {Inter.getLocText("FR-Designer-StyleAlignment_Layout_Default"), Inter.getLocText("FR-Designer-StyleAlignment_Layout_Image_Titled"),
			Inter.getLocText("FR-Designer-StyleAlignment_Layout_Image_Extend"), Inter.getLocText("FR-Designer-StyleAlignment_Layout_Image_Adjust")};

	private JPanel hPaneContainer;
	private JPanel vPaneContainer;
	private JPanel rotationBarCC;

	private UIComboBox textComboBox;
	private UIComboBox textRotationComboBox;
	private UIComboBox imageLayoutComboBox;

	private UIButtonGroup<Integer> hAlignmentPane;// 左对齐 水平居中 右对齐 水平分散
	private UIButtonGroup<Integer> vAlignmentPane;// 居上 垂直居中 居下

	private UINumberDragPane rotationPane;

	private UISpinner leftIndentSpinner;
	private UISpinner rightIndentSpinner;

	private UISpinner spaceBeforeSpinner;
	private UISpinner spaceAfterSpinner;
	private UISpinner lineSpaceSpinner;
	private GlobalNameListener globalNameListener = null;
	private IndentationUnitProcessor indentationUnitProcessor = null;

	public AlignmentPane() {
		this.initComponents();
	}

	protected void initComponents() {
		textComboBox = new UIComboBox(TEXT);
		imageLayoutComboBox = new UIComboBox(LAYOUT);
        initTextRotationCombox();

		Icon[] hAlignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_s_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/defaultAlignment.png")};
		Integer[] hAlignment = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT, Integer.valueOf(Constants.DISTRIBUTED), Constants.NULL};
		hAlignmentPane = new UIButtonGroup<Integer>(hAlignmentIconArray, hAlignment);
		hAlignmentPane.setAllToolTips(new String[]{Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Left"), Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Center"), Inter.getLocText("StyleAlignment-Tooltips_Right"),
		Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Distributed"),Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_DEFAULT")});
		hPaneContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		vPaneContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

		Icon[] vAlignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_top_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_center_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/v_down_normal.png")};
		Integer[] vAlignment = new Integer[]{Constants.TOP, Constants.CENTER, Constants.BOTTOM};
		vAlignmentPane = new UIButtonGroup<Integer>(vAlignmentIconArray, vAlignment);
		vAlignmentPane.setAllToolTips(new String[]{Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Top"), Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Center"), Inter.getLocText("FR-Designer-StyleAlignment_Tooltips_Bottom")});
		initOtherComponent();
		initAllNames();

		indentationUnitProcessor = ExtraDesignClassManager.getInstance().getIndentationUnitEditor();
		if (null == indentationUnitProcessor){
			indentationUnitProcessor = new DefaultIndentationUnitProcessor();
		}
	}


	private void initOtherComponent() {
		hPaneContainer.add(hAlignmentPane);
		vPaneContainer.add(vAlignmentPane);
		rotationPane = new UINumberDragPane(-ANGEL, ANGEL);

		leftIndentSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
		rightIndentSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

		spaceBeforeSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
		spaceAfterSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
		lineSpaceSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

		rotationBarCC = new JPanel(new CardLayout());
		rotationBarCC.add(rotationPane, "show");
		rotationBarCC.add(new JPanel(), "hide");

		this.setLayout(new BorderLayout());
		this.add(createPane(), BorderLayout.CENTER);

		textRotationComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				CardLayout cc = (CardLayout) rotationBarCC.getLayout();
				cc.show(rotationBarCC, textRotationComboBox.getSelectedIndex() == 0 ? "show" : "hide");
			}
		});
	}

    private void initTextRotationCombox(){
        ArrayList<String> selectOption = new ArrayList<String>();
        selectOption.add(Inter.getLocText("FR-Designer_Custom-Angle"));
        VerticalTextProcessor processor = ExtraClassManager.getInstance().getVerticalTextProcessor();
        if (processor != null){
            selectOption.addAll(Arrays.asList(processor.getComboxOption()));
        }

        textRotationComboBox = new UIComboBox(selectOption.toArray(new String[selectOption.size()]));
    }

	private void initAllNames() {
		hAlignmentPane.setGlobalName(Inter.getLocText("FR-Designer-StyleAlignment_Pane_Horizontal"));
		vAlignmentPane.setGlobalName(Inter.getLocText("FR-Designer-StyleAlignment_Pane_Vertical"));
		imageLayoutComboBox.setGlobalName(Inter.getLocText("Image-Image_Layout"));
		textComboBox.setGlobalName(Inter.getLocText("StyleAlignment-Text_Style"));
		textRotationComboBox.setGlobalName(Inter.getLocText("StyleAlignment-Text_Rotation"));
		rotationPane.setGlobalName(Inter.getLocText("StyleAlignment-Text_Rotation"));
		leftIndentSpinner.setGlobalName(Inter.getLocText("Style-Left_Indent"));
		rightIndentSpinner.setGlobalName(Inter.getLocText("Style-Right_Indent"));
		spaceBeforeSpinner.setGlobalName(Inter.getLocText("Style-Spacing_Before"));
		spaceAfterSpinner.setGlobalName(Inter.getLocText("Style-Spacing_After"));
		lineSpaceSpinner.setGlobalName(Inter.getLocText("Style-Line_Spacing"));
	}


	private JPanel createPane() {
		JPanel jp1 = new JPanel(new BorderLayout(0, 10));
		JPanel jp2 = new JPanel(new BorderLayout(0, 10));
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize1 = {p, f};
		double[] rowSize1 = {p, p, p, p, p, p, p, p, p, p, p, p, p, p};
		Component[][] components1 = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-StyleAlignment_Pane_Style")), null},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-StyleAlignment_Pane_Horizontal") + ":", UILabel.RIGHT), hPaneContainer},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-StyleAlignment_Pane_Vertical") + ":", UILabel.RIGHT), vPaneContainer},
				new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
				new Component[]{new UILabel(Inter.getLocText("Image-Image_Layout")), null},
				new Component[]{imageLayoutComboBox, null},
				new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
				new Component[]{new UILabel(Inter.getLocText("StyleAlignment-Text_Style")), null},
				new Component[]{textComboBox, null},
				new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
				new Component[]{new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), null},
				new Component[]{textRotationComboBox, null},
				new Component[]{rotationBarCC, null},
				new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
		};

		double[] columnSize2 = {p, f};
		double[] rowSize2 = {p, p, p, p};
		Component[][] components2 = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-StyleAlignment_Style_Indentation")), null},
				new Component[]{new UILabel(Inter.getLocText("Style-Left_Indent") + ":", SwingConstants.RIGHT), creatSpinnerPane(leftIndentSpinner)},
				new Component[]{new UILabel(Inter.getLocText("Style-Right_Indent") + ":", SwingConstants.RIGHT), creatSpinnerPane(rightIndentSpinner)},
				new Component[]{new JSeparator(JSeparator.HORIZONTAL), null},
		};

		double[] columnSize3 = {p, f};
		double[] rowSize3 = {p, p, p, p};
		Component[][] components3 = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-StyleAlignment_Style_Spacing")), null},
				new Component[]{new UILabel(Inter.getLocText("Style-Spacing_Before") + ":", SwingConstants.RIGHT), creatSpinnerPane(spaceBeforeSpinner)},
				new Component[]{new UILabel(Inter.getLocText("Style-Spacing_After") + ":", SwingConstants.RIGHT), creatSpinnerPane(spaceAfterSpinner)},
				new Component[]{new UILabel(Inter.getLocText("Style-Line_Spacing") + ":", SwingConstants.RIGHT), creatSpinnerPane(lineSpaceSpinner)},
		};
		JPanel northPane = TableLayoutHelper.createTableLayoutPane(components1, rowSize1, columnSize1);
		JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components2, rowSize2, columnSize2);
		JPanel southPane = TableLayoutHelper.createTableLayoutPane(components3, rowSize3, columnSize3);

		jp2.add(centerPane, BorderLayout.NORTH);
		jp2.add(southPane, BorderLayout.CENTER);
		jp1.add(northPane, BorderLayout.NORTH);
		jp1.add(jp2, BorderLayout.CENTER);
		return jp1;
	}

	private JPanel creatSpinnerPane(Component comp) {
		JPanel jp = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		jp.add(comp);
		return jp;
	}

	/**
	 * 标题
	 * @return 标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Designer-StyleAlignment_Style_Alignment");
	}

	/**
	 * Populate cellstyle border.
	 *
	 * @param style the new style.
	 */
	public void populateBean(Style style) {
		hAlignmentPane.setSelectedItem(BaseUtils.getAlignment4Horizontal(style));
		vAlignmentPane.setSelectedItem(style.getVerticalAlignment());

		if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINE) {
			this.textComboBox.setSelectedIndex(1);
		} else if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINEADJUSTFONT) {
			this.textComboBox.setSelectedIndex(2);
		} else if (style.getTextStyle() == Style.TEXTSTYLE_MULTILINEADJUSTFONT) {
			this.textComboBox.setSelectedIndex(3);
		} else {
			this.textComboBox.setSelectedIndex(0);
		}
		if (style.getVerticalText() == Style.VERTICALTEXT && ExtraClassManager.getInstance().getVerticalTextProcessor() != null) {
			textRotationComboBox.setSelectedIndex(style.getTextDirection() == Style.LEFT_TO_RIGHT ? 1 : 2);
		} else {
			textRotationComboBox.setSelectedIndex(0);
			rotationPane.populateBean((double) style.getRotation());
		}

		if (style.getImageLayout() == Constants.IMAGE_TILED) {
			imageLayoutComboBox.setSelectedIndex(1);
		} else if (style.getImageLayout() == Constants.IMAGE_EXTEND) {
			imageLayoutComboBox.setSelectedIndex(2);
		} else if (style.getImageLayout() == Constants.IMAGE_ADJUST) {
			imageLayoutComboBox.setSelectedIndex(3);
		} else {
			imageLayoutComboBox.setSelectedIndex(0);
		}

		int leftPadding = indentationUnitProcessor.paddingUnitProcessor(style.getPaddingLeft());
		int rightPadding = indentationUnitProcessor.paddingUnitProcessor(style.getPaddingRight());

		// alex:indent
		this.leftIndentSpinner.setValue(new Integer(leftPadding));
		this.rightIndentSpinner.setValue(new Integer(rightPadding));

		this.spaceBeforeSpinner.setValue(style.getSpacingBefore());
		this.spaceAfterSpinner.setValue(style.getSpacingAfter());
		this.lineSpaceSpinner.setValue(style.getLineSpacing());

	}

	/**
	 * Update cellstyle border
	 *
	 * @param style the new style.
	 */
	public Style update(Style style) {
		// peter:需要判断传递进来的值是否为null.
		if (style == null) {
			return style;
		}

		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FR-Designer-StyleAlignment_Pane_Horizontal"))) {
			Integer h = this.hAlignmentPane.getSelectedItem();
			style = style.deriveHorizontalAlignment(h == null ? -1 : h);
		}
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("FR-Designer-StyleAlignment_Pane_Vertical"))) {
			Integer vAlign = this.vAlignmentPane.getSelectedItem();
			if (vAlign != null) {
				style = style.deriveVerticalAlignment(vAlign);
			}
		}

		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("StyleAlignment-Text_Style"))) {
			if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[0])) {
				style = style.deriveTextStyle(Style.TEXTSTYLE_WRAPTEXT);
			} else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[1])) {
				style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINE);
			} else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[2])) {
				style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINEADJUSTFONT);
			} else {
				style = style.deriveTextStyle(Style.TEXTSTYLE_MULTILINEADJUSTFONT);
			}
		}

		style = updateImageLayout(style);
		style = updateTextRotation(style);
		style = updateOther(style);
		return style;
	}


	private Style updateImageLayout(Style style) {
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Image-Image_Layout"))) {
			if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[1])) {
				style = style.deriveImageLayout(Constants.IMAGE_TILED);
			} else if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[2])) {
				style = style.deriveImageLayout(Constants.IMAGE_EXTEND);
			} else if (ComparatorUtils.equals(this.imageLayoutComboBox.getSelectedItem(), LAYOUT[3])) {
				style = style.deriveImageLayout(Constants.IMAGE_ADJUST);
			} else {
				style = style.deriveImageLayout(Constants.IMAGE_CENTER);
			}
		}
		return style;
	}


	private Style updateTextRotation(Style style) {
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("StyleAlignment-Text_Rotation"))) {
			if (this.textRotationComboBox.getSelectedIndex() != 0) {
				style = style.deriveVerticalText(Style.VERTICALTEXT);
				style = style.deriveRotation(0);
				style = style.deriveTextDirection(this.textRotationComboBox.getSelectedIndex() == 1 ? Style.LEFT_TO_RIGHT : Style.RIGHT_TO_LEFT);
			} else {
				style = style.deriveVerticalText(Style.HORIZONTALTEXT);
				style = style.deriveRotation(rotationPane.updateBean().intValue());
			}
		}
		return style;
	}

	private Style updateOther(Style style) {
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Style-Left_Indent"))) {
			style = style.derivePaddingLeft(indentationUnitProcessor.paddingUnitGainFromSpinner((int)(this.leftIndentSpinner.getValue())));
		}

		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Style-Right_Indent"))) {
			style = style.derivePaddingRight(indentationUnitProcessor.paddingUnitGainFromSpinner((int)(this.rightIndentSpinner.getValue())));
		}
		//间距
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Style-Spacing_Before"))) {
			style = style.deriveSpacingBefore((int) (this.spaceBeforeSpinner.getValue()));
		}
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Style-Spacing_After"))) {
			style = style.deriveSpacingAfter((int) (this.spaceAfterSpinner.getValue()));
		}
		if (ComparatorUtils.equals(globalNameListener.getGlobalName(), Inter.getLocText("Style-Line_Spacing"))) {
			style = style.deriveLineSpacing((int) (this.lineSpaceSpinner.getValue()));
		}
		return style;
	}

	/**
	 * 注册监听事件
     *
	 * @param listener 观察者监听事件
	 */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

	/**
	 * 是否响应监听
	 *
     *  @return 否
	 */
	public boolean shouldResponseNameListener() {
		return false;
	}

	/**
	 *
	 * @param name
	 */
	public void setGlobalName(String name) {
	}
}