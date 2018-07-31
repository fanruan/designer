package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.FormatPane;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;

/**
 * 数据点提示内容面板，包含系列值、系列百分比
 * @author jerry
 *
 */
public class TooltipContentsPane extends BasicPane{
	protected UICheckBox showValueCB;
	protected UICheckBox showPercent;
	protected Format format;
	protected Format percentFormat;
	protected JPanel contentPane;


	public TooltipContentsPane() {
		setLayout(FRGUIPaneFactory.createBorderLayout());
		contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		add(contentPane, BorderLayout.CENTER);
		contentPane.add(createJPanel4Label());
	}

	 private JPanel createJPanel4Label() {
		return createTableLayoutPane(new Component[][]{
				createComponents4Value(),
				createComponents4PercentValue(),
				new Component[0]
		});
	}

	protected Component[] createComponents4Value() {
		return new Component[]{null, createJPanel4Value()};
	}

	protected JPanel createJPanel4Value() {
		if (showValueCB == null) {
			showValueCB = new UICheckBox(getValueString());
		}
		showValueCB.setSelected(true);

		JPanel valuePane = new JPanel();
		valuePane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));// 默认左未0对齐.

		valuePane.add(showValueCB);

		UIButton valueFormatButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Format"));
		valuePane.add(valueFormatButton);

		valueFormatButton.addActionListener(listener);
		valueFormatButton.setPreferredSize(new Dimension(60, 20));//默认太高了.
		return valuePane;
	}

	protected String getValueString() {
		return com.fr.design.i18n.Toolkit.i18nText("Value");
	}

	protected Component[] createComponents4PercentValue() {
		if (showPercent == null) {
			showPercent = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Chart_ValueIntPercent"));
		}

		JPanel percentValuePane = new JPanel();
		percentValuePane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));// 默认左未0对齐.

		percentValuePane.add(showPercent);

		UIButton valueFormatButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Format"));
		percentValuePane.add(valueFormatButton);

		valueFormatButton.addActionListener(percentListener);
		valueFormatButton.setPreferredSize(new Dimension(60, 20));//默认太高了.
		return new Component[]{null, percentValuePane};
	}

	protected JPanel createTableLayoutPane(Component[][] components) {
		double[] colSize = {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
		double[] rowSize = {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED};
		return TableLayoutHelper.createTableLayoutPane(components, rowSize, colSize);
	}

	public String title4PopupWindow() {
		return "DataLabel";
	}

	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			final FormatPane formatPane = new FormatPane();
			if (format != null) {
				formatPane.populate(format);
			}

			paneActionPerformed(formatPane, false);
		}
	};

	private ActionListener percentListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			final FormatPane percentPane = new FormatPane();
			percentPane.percentFormatPane();

			if (percentFormat != null) {
				percentPane.populate(percentFormat);
			}
			paneActionPerformed(percentPane, true);
		}
	};

	private void paneActionPerformed(final FormatPane formatPane, final boolean isPercent) {

		formatPane.showWindow(SwingUtilities.getWindowAncestor(TooltipContentsPane.this),
				new DialogActionAdapter() {
			public void doOk() {
				if (isPercent) {
					percentFormat = formatPane.update();
				} else {
					format = formatPane.update();
				}
			}
		}).setVisible(true);
	}
	
	public void populate(AttrContents seriesAttrContents) {
		format = seriesAttrContents.getFormat();
		percentFormat = seriesAttrContents.getPercentFormat();
		String dataLabel = seriesAttrContents.getSeriesLabel();
		if (dataLabel != null) {
			if (showValueCB != null) {
				showValueCB.setSelected(dataLabel.contains(ChartConstants.VALUE_PARA));
			}
			if (showPercent != null) {
				showPercent.setSelected(dataLabel.contains(ChartConstants.PERCENT_PARA));
			}
		} else {
			if (showValueCB != null) {
				showValueCB.setSelected(false);
			}
			if (showPercent != null) {
				showPercent.setSelected(false);
			}
		}
	}
	
	public void update(AttrContents seriesAttrContents) {
    	String contents = StringUtils.EMPTY;
    	contents += ChartConstants.SERIES_PARA + ChartConstants.BREAKLINE_PARA + ChartConstants.CATEGORY_PARA;
		if (showValueCB.isSelected() && !showPercent.isSelected()) {
			contents += ChartConstants.BREAKLINE_PARA + ChartConstants.VALUE_PARA;
		} else if (!showValueCB.isSelected() && showPercent.isSelected()){
			contents += ChartConstants.BREAKLINE_PARA + ChartConstants.PERCENT_PARA;
		} else if (showValueCB.isSelected() && showPercent.isSelected()) {
			contents += ChartConstants.BREAKLINE_PARA + ChartConstants.VALUE_PARA 
					+ ChartConstants.BREAKLINE_PARA + ChartConstants.PERCENT_PARA;
		} else {
			contents = null;
		}
		seriesAttrContents.setSeriesLabel(contents);
    	seriesAttrContents.setFormat(format);
		seriesAttrContents.setPercentFormat(percentFormat);
    }
}