package com.fr.design.mainframe.form;

import com.fr.base.ScreenResolution;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JSliderPane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.report.worksheet.FormElementCase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 整个FormElementCase编辑区域 包括滚动条、中间的grid或者聚合块、下面的sheetTab
 */
public class FormReportComponentComposite extends JComponent implements TargetModifiedListener, FormECCompositeProvider{

    private static final int MAX = 400;
    private static final int HUND = 100;
    private static final int MIN = 10;
	public FormElementCaseDesigner elementCaseDesigner;
    private BaseJForm jForm;

    private FormTabPane sheetNameTab;
    private JPanel hbarContainer;
    private JSliderPane jSliderContainer;


    public FormReportComponentComposite(BaseJForm jform, FormElementCaseDesigner elementCaseDesign, FormElementCaseContainerProvider ecContainer) {
        this.jForm = jform;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.elementCaseDesigner = elementCaseDesign;
        this.add(elementCaseDesigner, BorderLayout.CENTER);
        sheetNameTab = new FormTabPane(ecContainer, jform);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
        jSliderContainer.getShowVal().addChangeListener(showValSpinnerChangeListener);
        jSliderContainer.getSelfAdaptButton().addItemListener(selfAdaptButtonItemListener);
        elementCaseDesigner.addTargetModifiedListener(this);
    }

    ChangeListener showValSpinnerChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = (int) ((UIBasicSpinner)e.getSource()).getValue();
            value = value > MAX ? MAX : value;
            value = value < MIN ? MIN : value;
            int resolution = (int) (ScreenResolution.getScreenResolution()*value/HUND);
            JForm jf = (JForm) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setScale(resolution);
        }
    };

    ItemListener selfAdaptButtonItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (jSliderContainer.getSelfAdaptButton().isSelected()){
                int resolution = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().selfAdaptUpdate();
                jSliderContainer.getShowVal().setValue(resolution*HUND/ScreenResolution.getScreenResolution());
            }
        }
    };

    private java.util.List<TargetModifiedListener> targetModifiedList = new java.util.ArrayList<TargetModifiedListener>();

    /**
     *  添加目标改变的监听
     * @param targetModifiedListener     目标改变事件
     */
    public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
    	targetModifiedList.add(targetModifiedListener);
    }

    /**
     * 目标改变
     * @param e      事件
     */
	public void targetModified(TargetModifiedEvent e) {
        for (TargetModifiedListener l : targetModifiedList) {
            l.targetModified(e);
        }
	}
    public void setEditingElementCase(FormElementCase formElementCase){
        elementCaseDesigner.setTarget(formElementCase);
        fireTargetModified();
    }

    private JComponent createSouthControlPane() {
        JPanel southPane = new JPanel(new BorderLayout());
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(elementCaseDesigner.getHorizontalScrollBar());
        jSliderContainer = JSliderPane.getInstance();

        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sheetNameTab, jSliderContainer);
        southPane.add(hbarContainer,BorderLayout.NORTH);
        southPane.add(splitpane,BorderLayout.CENTER);
        splitpane.setBorder(null);
        splitpane.setDividerSize(3);
        splitpane.setResizeWeight(1);
        return southPane;
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
    	elementCaseDesigner.stopEditing();
    }

    public void setComposite() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus((ToolBarMenuDockPlus) jForm);
        this.validate();
        this.repaint(40);
    }
    
	public void setSelectedWidget(FormElementCaseProvider fc) {
        if (fc != null){
            elementCaseDesigner.setTarget(fc);
        }
	}

    /**
     * 模板更新
     */
    public void fireTargetModified() {
        jForm.fireTargetModified();
    }


}