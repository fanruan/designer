package com.fr.design.widget;


import java.util.ArrayList;
import java.util.List;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.controlpane.ObjectUIControlPane;
import com.fr.design.present.CellWriteAttrPane;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.Nameable;

public class WidgetEventPane extends ObjectUIControlPane {
    private ElementCasePane ePane;
	
	public WidgetEventPane() {
	    this(null);
	}
    public WidgetEventPane(ElementCasePane pane) {
        super(pane);
        ePane = pane;
        this.setNameListEditable(false);
    }

    @Override
    public String getAddItemText() {
        return Inter.getLocText("FR-Designer_Add_Event");
    }

    /**
     * 生成添加按钮的NameableCreator
     * @return 按钮的NameableCreator
     */
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new EventCreator(Widget.EVENT_STATECHANGE, WidgetEventListenerUpdatePane.class)
            };
    }

	@Override
	public void saveSettings() {

        final TemplateElementCase tplEC = ePane.getEditingElementCase();
        final CellSelection finalCS = (CellSelection) ePane.getSelection();
        TemplateCellElement editCellElement = tplEC.getTemplateCellElement(finalCS.getColumn(), finalCS.getRow());
        if (editCellElement == null) {
            editCellElement = new DefaultTemplateCellElement(finalCS.getColumn(), finalCS.getRow());
            tplEC.addCellElement(editCellElement);
        }
        final BasicPane bp = populateBasicPane(editCellElement);

        // 需要先行后列地增加新元素。
        for (int j = 0; j < finalCS.getRowSpan(); j++) {
            for (int i = 0; i < finalCS.getColumnSpan(); i++) {
                int column = i + finalCS.getColumn();
                int row = j + finalCS.getRow();
                editCellElement = tplEC.getTemplateCellElement(column, row);
                if (editCellElement == null) {
                    editCellElement = new DefaultTemplateCellElement(column, row);
                    tplEC.addCellElement(editCellElement);
                }
                // alex:不加这一句话会导致跨行跨列的格子被多次update
                if (editCellElement.getColumn() != column || editCellElement.getRow() != row) {
                    continue;
                }
                updateBasicPane(bp, editCellElement);

                Object editElementValue = editCellElement.getValue();
                if (editElementValue != null && (editElementValue instanceof String || editElementValue instanceof Number)) {
                    // TODO ALEX_SEP 暂时用FIT_DEFAULT替代,不取reportsetting里面的设置,因为也不知道是应该放在report里面还是elementcase里面
                    GridUtils.shrinkToFit(ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT, tplEC, editCellElement);
                }
            }
        }
        ePane.fireTargetModified();
	}

    private BasicPane populateBasicPane(TemplateCellElement cellElement) {
        CellWriteAttrPane pane = new CellWriteAttrPane(ePane);
        //got simple cell element from column and row.
        pane.populate(cellElement);

        return pane;
    }

    private void updateBasicPane(BasicPane bp, TemplateCellElement cellElement) {
        CellWriteAttrPane pane = (CellWriteAttrPane) bp;
        if (cellElement.getWidget() == null) {
            pane.update(cellElement);
            return;
        }
        try {
            Widget oldWidget = (Widget) cellElement.getWidget().clone();
            pane.update(cellElement);
            //这边需要重新设置权限细粒度的hashset是因为Update是直接生成一个新的来update的，所以以前里面的hashset都没有了
            Widget newWidget = cellElement.getWidget();
            if (newWidget.getClass() == oldWidget.getClass()) {
                newWidget.setWidgetPrivilegeControl((WidgetPrivilegeControl) oldWidget.getWidgetPrivilegeControl().clone());
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

	@Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Event");
    }
    
    public static class WidgetEventListenerUpdatePane extends ListenerUpdatePane {
    	private  ElementCasePane epane;
    	public WidgetEventListenerUpdatePane() {
    		this(null);
    	}
    	public WidgetEventListenerUpdatePane(ElementCasePane epane){
    		this.epane = epane;
    		super.initComponents();
    	}

        /**
         *  根据有无单元格创建 DBManipulationPane
         * @return   有单元格。有智能添加单元格等按钮，返回 SmartInsertDBManipulationPane
         */
        private DBManipulationPane autoCreateDBManipulationInWidgetEventPane() {
            JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            return jTemplate.createDBManipulationPaneInWidget();
        }

		@Override
		protected JavaScriptActionPane createJavaScriptActionPane() {
			return new JavaScriptActionPane() {

				@Override
				protected DBManipulationPane createDBManipulationPane() {
					if(epane == null) {
						return autoCreateDBManipulationInWidgetEventPane();
					} 
					
					return new SmartInsertDBManipulationInWidgetEventPane(epane);
				}

				@Override
				protected String title4PopupWindow() {
					return Inter.getLocText("Set_Callback_Function");
				}
				
				@Override
				protected boolean isForm() {
					return false;
				}

				protected String[] getDefaultArgs() {
					return new String[0];
				}
			};
		}

		@Override
		protected boolean supportCellAction() {
			return false;
		}
		
	}

    public void populate(Widget widget) {
        if (widget == null) {
            return;
        }
        
        this.refreshNameableCreator(EventCreator.createEventCreator(widget.supportedEvents(), WidgetEventListenerUpdatePane.class));

        List<NameObject> list = new ArrayList<NameObject>();
        Listener listener;
        for (int i = 0, size = widget.getListenerSize(); i < size; i++) {
            listener = widget.getListener(i);
            if (!listener.isDefault()) //name+(i+1)需要确保名字不重复
            {
                list.add(new NameObject(EventCreator.switchLang(listener.getEventName()) + (i + 1), listener));
            }
        }
        this.populate(list.toArray(new NameObject[list.size()]));
    }

	/**
	 * 更新
	 * @return 监听器
	 */
    public Listener[] updateListeners() {
        Nameable[] res = this.update();
        Listener[] res_array = new Listener[res.length];
        for (int i = 0, len = res.length; i < len; i++) {
            res_array[i] = (Listener) ((NameObject)res[i]).getObject();
        }
        return res_array;
    }
}