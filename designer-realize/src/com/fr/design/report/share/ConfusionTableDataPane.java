package com.fr.design.report.share;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

/**
 * 数据集混淆的面板
 * 
 * @author neil
 *
 * @date: 2015-3-7-上午11:04:16
 */
public class ConfusionTableDataPane extends BasicBeanPane<ConfusionInfo>{
	
	private static final int TABLE_WIDTH = 300;
	private static final int TABLE_HEIGHT = 20;
	
	//混淆的相关信息
	private ConfusionInfo info;
	
	private Component centerPane;

	//混淆关键字的所有面板, 用于update
	private UITextField[] keyFields;
	
	/**
	 * 构造函数
	 */
	public ConfusionTableDataPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel northPane = initNorthPane();
		this.add(northPane, BorderLayout.NORTH);
		
		centerPane = new JPanel();
        this.add(centerPane, BorderLayout.CENTER);
	}
	
	private JPanel initNorthPane(){
		JPanel northPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		UILabel tipLabel = new UILabel(Inter.getLocText("FR-Designer_Choose-Data-Confusion-Tip"));
		northPane.add(tipLabel, BorderLayout.CENTER);
		UIButton previewBtn = initPreviewButton();
		northPane.add(previewBtn, BorderLayout.EAST);
		
		return northPane;
	}
	
	//预览按钮
	private UIButton initPreviewButton(){
		UIButton previewBtn = new UIButton();
		previewBtn.setIcon(BaseUtils.readIcon("/com/fr/web/images/preview.png"));
		previewBtn.set4ToolbarButton();
		previewBtn.setToolTipText(Inter.getLocText("FR-Designer_Preview-Data-Confusion"));
		previewBtn.addActionListener(previewListener);
		
		return previewBtn;
	}

	@Override
	public void populateBean(ConfusionInfo ob) {
		this.remove(centerPane);
		info = ob;
		
		int columnCount = ArrayUtils.getLength(info.getColumnNames());
        double p = TableLayout.PREFERRED;
        double rowSize[] = initRowSize(columnCount + 1);
        double columnSize[] = {p, p, p};
        keyFields = new UITextField[columnCount];
        
        //根据不同的内容, 生成table内部组件
        Component[][] portComponents = initTableComponents(columnCount, columnSize.length);
        centerPane = new UIScrollPane(TableLayoutHelper.createTableLayoutPane(portComponents, rowSize, columnSize));
        this.add(centerPane, BorderLayout.CENTER);

		this.revalidate();
	}
	
	private Component[][] initTableComponents(int rowCount, int columnCount){
        //默认有一行标题行 所以+1
        Component[][] portComponents = new Component[rowCount + 1][columnCount];
        
        for (int i = 0; i < portComponents.length; i++) {
        	if(i == 0){
        		//表格头部: 字段名称 + 混淆关键字 + 预览按钮
        		portComponents[i] = initTableHeaderPanel();
        		continue;
        	}
        	portComponents[i] = initTableContentRow(i);
		}
        
        return portComponents;
	}
	
	private double[] initRowSize(int columnCount){
		double p = TableLayout.PREFERRED;
        double rowSize[] = new double[columnCount];
		
		for (int i = 0; i < columnCount; i++) {
			rowSize[i] = p;
		}
		
		return rowSize;
	}
	
	private Component[] initTableContentRow(int i){
		String[] columnNameArray = info.getColumnNames();
    	//扣去表头一行
    	int colIndex = i - 1;
    	boolean isNumberColumn = info.isNumberColumn(colIndex);
    	Component keyComponent = getKeyComponent(isNumberColumn, colIndex);
    	return  new Component[]{new UILabel(), new UILabel(columnNameArray[colIndex]), keyComponent};
	}
	
	//获取放置混淆key的面板组件, 如果是字符串, 返回UITextField; 
	//如果是数字就返回UINumberField+UIBasicSpinner
	private Component getKeyComponent(boolean isNumberColumn, int colIndex){
		String[] confusionKeyArray = info.getConfusionKeys();
		if(!isNumberColumn){
			keyFields[colIndex] = new UITextField(confusionKeyArray[colIndex]);
			return keyFields[colIndex];
		}
		
    	JPanel numberPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
    	UINumberField numberField = new UINumberField();
    	numberPanel.add(numberField);
    	UIBasicSpinner spinner = populateNumberSpinner(confusionKeyArray, colIndex);
    	numberPanel.add(spinner, BorderLayout.CENTER);
    	keyFields[colIndex] = new SpinnerWapper(spinner);
    	
    	return numberPanel;
	}
	
	private UIBasicSpinner populateNumberSpinner(String[] confusionKeyArray, int colIndex){
		UIBasicSpinner spinner = new UIBasicSpinner();
    	Number spinnerValue = GeneralUtils.objectToNumber(confusionKeyArray[colIndex], false);
    	//数字默认混淆关键字为1
    	spinner.setValue(spinnerValue.intValue() == 0 ? 1 : spinnerValue);
    	
    	return spinner;
	}
	
	
	private Component[] initTableHeaderPanel(){
		JPanel headPanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		UILabel keyLabel = new UILabel(Inter.getLocText("FR-Designer_Confusion-key"));
		UIButton helpBtn = initHelpButton();
		headPanel.add(keyLabel);
		headPanel.add(helpBtn);
		headPanel.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		
		UILabel colNameLabel = new UILabel(Inter.getLocText("FR-Designer_Confusion-key"));
		return new Component[]{new UILabel(), colNameLabel, headPanel};
	}
	
	private UIButton initHelpButton(){
		UIButton helpButton = new UIButton();
		helpButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/help.png"));
		helpButton.set4ToolbarButton();
		helpButton.setToolTipText(getConfusionTooltip());
		
		return helpButton;
	}
	
	private String getConfusionTooltip(){
		try {
			InputStream in = IOUtils.readResource("/com/fr/design/report/share/shareToolTip.html");
			return IOUtils.inputStream2String(in);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage());
		}
		
		return StringUtils.EMPTY;
	}

	@Override
	public ConfusionInfo updateBean() {
		int len = ArrayUtils.getLength(keyFields);
		String[] confusionKeys = info.getConfusionKeys();
		for (int i = 0; i < len; i++) {
			confusionKeys[i] = keyFields[i].getText();
		}
		
		return info;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Data-confusion");
	}
	
	private ActionListener previewListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ConfusionInfo info = ConfusionTableDataPane.this.updateBean();
			String tdName = info.getTabledataName();
			TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
			
			try {
				EmbeddedTableData tabledata = (EmbeddedTableData) source.getTableData(tdName).clone();
				new ConfuseTabledataAction().confuse(info, tabledata);
                new TemplateTableDataWrapper(tabledata).previewData();
			} catch (CloneNotSupportedException e1) {
				FRContext.getLogger().error(e1.getMessage());
			}
		}
	};
	
	//Spinner的一个封装, 为了方便update时都用textfield的getText方法, 不然要加接口, 没必要
	private class SpinnerWapper extends UITextField{
		
		private UIBasicSpinner spinner;

		public SpinnerWapper(UIBasicSpinner spinner) {
			this.spinner = spinner;
		}

		public UIBasicSpinner getSpinner() {
			return spinner;
		}

		public void setSpinner(UIBasicSpinner spinner) {
			this.spinner = spinner;
		}
		
		public String getText() {
			return Utils.objectToString(this.spinner.getValue());
		}
	}
}