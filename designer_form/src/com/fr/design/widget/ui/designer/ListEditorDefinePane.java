package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ListEditor;
import com.fr.general.Inter;

import javax.swing.*;

public class ListEditorDefinePane extends WriteUnableRepeatEditorPane<ListEditor> {
	private UICheckBox needHeadCheckBox;
	private DictionaryPane dictPane;

	public ListEditorDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		dictPane = new DictionaryPane();
	}

	@Override
	protected JPanel setThirdContentPane() {
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel centerPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		centerPane.add(needHeadCheckBox = new UICheckBox(Inter.getLocText("List-Need_Head")));
		contenter.add(centerPane);
		return contenter;
	}
	
	@Override
	public String title4PopupWindow() {
		return "List";
	}

	@Override
	protected void populateSubWriteUnableRepeatBean(ListEditor e) {
		needHeadCheckBox.setSelected(e.isNeedHead());
		this.dictPane.populateBean(e.getDictionary());
	}

	@Override
	protected ListEditor updateSubWriteUnableRepeatBean() {
		ListEditor ob = new ListEditor();

		ob.setNeedHead(needHeadCheckBox.isSelected());
		ob.setDictionary(this.dictPane.updateBean());

		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}