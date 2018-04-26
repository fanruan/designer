package com.fr.design.mainframe.chart.gui.style.legend;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartBackgroundNoImagePane;
import com.fr.design.mainframe.chart.gui.style.ChartBorderPane;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 属性表, 图表样式-图例 界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-5 下午03:36:44
 */
public class ChartLegendPane extends BasicScrollPane<Chart>{
	private UICheckBox isLegendVisible;
	private ChartTextAttrPane textAttrPane;
	private ChartBorderPane borderPane;
	private UIButtonGroup<Integer> location;
	private ChartBackgroundNoImagePane backgroundPane;
	
	private JPanel legendPane;
	
	private static ChartLegendPane CONTEXT;
	
	public static final ChartLegendPane getInstance() {
		if(CONTEXT == null) {
			CONTEXT = new ChartLegendPane();
		}
		return CONTEXT;
	}

	private class ContentPane extends JPanel {
		public ContentPane() {
			initComponents();
		}

		private void initComponents(){
			isLegendVisible = new UICheckBox(Inter.getLocText("Chart_Legend_Is_Visible"));
			textAttrPane = new ChartTextAttrPane();
			borderPane = new ChartBorderPane();
			
			String[] textArray = {Inter.getLocText("StyleAlignment-Top"), Inter.getLocText("StyleAlignment-Bottom"),
					Inter.getLocText("StyleAlignment-Left"), Inter.getLocText("StyleAlignment-Right"), Inter.getLocText("Right_Top")};
			Integer[] valueArray = {Constants.TOP, Constants.BOTTOM, Constants.LEFT, Constants.RIGHT, Constants.RIGHT_TOP};
            Icon[] iconArray = {BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top.png"),
                                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_bottom.png"),
                                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_left.png"),
                                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_right.png"),
                                BaseUtils.readIcon("/com/fr/design/images/chart/ChartLegend/layout_top_right.png")
            };

			location = new UIButtonGroup<Integer>(iconArray, valueArray);
            location.setAllToolTips(textArray);
			backgroundPane = new ChartBackgroundNoImagePane();

			double p = TableLayout.PREFERRED;
			double f = TableLayout.FILL;
			double[] columnSize = { LayoutConstants.CHART_ATTR_TOMARGIN, f };
			double[] rowSize = { p,p,p,p,p,p};

            Component[][] components = new Component[][]{
                    new Component[]{null,textAttrPane},
                    new Component[]{new JSeparator(),null},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Layout")),location} ,
                    new Component[]{new JSeparator(),null},
                    new Component[]{borderPane,null},
                    new Component[]{backgroundPane,null}
            };
            legendPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
            double[] col = {f};
            double[] row = {p, p};
            JPanel panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
            									new Component[]{isLegendVisible},new Component[]{legendPane},},row,col);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);
            isLegendVisible.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkBoxUse();
				}
			});
		}
	}
	
	private void checkBoxUse() {
		isLegendVisible.setEnabled(true);
		legendPane.setVisible(isLegendVisible.isSelected());
	}

    /**
     * 标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_LEGNED_TITLE;
	}

	@Override
	protected JPanel createContentPane() {
		return new ContentPane();
	}

	@Override
	public void updateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		Plot plot = chart.getPlot();
		if(plot == null) {
			return;
		}
		Legend legend = plot.getLegend();
		if(legend == null) {
			legend = new Legend();
		}
		legend.setLegendVisible(isLegendVisible.isSelected());
		legend.setFRFont(textAttrPane.updateFRFont());
		borderPane.update(legend);
		legend.setPosition(location.getSelectedItem());
		backgroundPane.update(legend);
	}

	@Override
	public void populateBean(Chart chart) {
		if(chart != null && chart.getPlot() != null) {
			Legend legend = chart.getPlot().getLegend();
			if (legend != null) {
				isLegendVisible.setSelected(legend.isLegendVisible());
				textAttrPane.populate(legend.getFRFont());
				borderPane.populate(legend);
				location.setSelectedItem(legend.getPosition());
				backgroundPane.populate(legend);
			}
		}
		
		checkBoxUse();
	}
}