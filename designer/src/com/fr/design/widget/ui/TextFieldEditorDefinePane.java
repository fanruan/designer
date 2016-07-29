package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.RegPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.reg.NoneReg;
import com.fr.form.ui.reg.RegExp;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldEditorDefinePane extends FieldEditorDefinePane<TextEditor> {
	protected RegPane regPane;
	private WaterMarkDictPane waterMarkDictPane;
	private UITextField regErrorMsgTextField;

	public TextFieldEditorDefinePane() {
		this.initComponents();
	}

	@Override
	protected JPanel setFirstContentPane() {
		JPanel attrPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_S_Pane();
		contenter.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		attrPane.add(contenter);
		JPanel regErrorMsgPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		regErrorMsgTextField = new UITextField(16);
		regErrorMsgPane.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"),regErrorMsgTextField}, FlowLayout.LEFT,24));

		regErrorMsgTextField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
			}

			public void insertUpdate(DocumentEvent e) {
				regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				regErrorMsgTextField.setToolTipText(regErrorMsgTextField.getText());
			}
		});
		regPane = createRegPane();
		final RegPane.RegChangeListener rl = new RegPane.RegChangeListener() {

			@Override
			public void regChangeAction() {
				waterMarkDictPane.setWaterMark("");
				regPane.removeRegChangeListener(this);
			}
		};
		final RegPane.PhoneRegListener pl = new RegPane.PhoneRegListener() {
			public void phoneRegChangeAction(RegPane.PhoneRegEvent e) {
				if (StringUtils.isNotEmpty(e.getPhoneRegString())
						&& StringUtils.isEmpty(waterMarkDictPane.getWaterMark())) {
					waterMarkDictPane.setWaterMark(Inter.getLocText("Example") + ":" + e.getPhoneRegString());
					regPane.addRegChangeListener(rl);
				}
			}
		};
		regPane.addPhoneRegListener(pl);
		JPanel spp1 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Validate"));
		JPanel validateContent = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		getAllowBlankCheckBox().setPreferredSize(new Dimension(444,40));
		validateContent.add(GUICoreUtils.createFlowPane(getAllowBlankCheckBox(), FlowLayout.LEFT));
		validateContent.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"),getErrorMsgTextField()}, FlowLayout.LEFT,24));
		validateContent.add(GUICoreUtils.createFlowPane(regPane, FlowLayout.LEFT));
		validateContent.add(GUICoreUtils.createFlowPane(new JComponent[]{new UILabel(Inter.getLocText(new String[]{"Error", "Tooltips"}) + ":"),regErrorMsgTextField}, FlowLayout.LEFT,24));
		spp1.add(validateContent);
		JPanel spp2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Advanced"));
		waterMarkDictPane = new WaterMarkDictPane();
		waterMarkDictPane.addInputKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				regPane.removePhoneRegListener(pl);
				regPane.removeRegChangeListener(rl);
				waterMarkDictPane.removeInputKeyListener(this);
			}
		});
		//监听填写规则下拉框的值的变化
		regPane.getRegComboBox().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				RegExp regExp = (RegExp)regPane.getRegComboBox().getSelectedItem();
				if(regExp instanceof NoneReg){
					regErrorMsgTextField.setEnabled(false);
				}else{
					regErrorMsgTextField.setEnabled(true);
				}

			}
		});
		spp2.add(waterMarkDictPane);
		contenter.add(spp2, BorderLayout.NORTH);
		contenter.add(spp1, BorderLayout.CENTER);
		return attrPane;
	}

	protected RegPane createRegPane() {
		return new RegPane();
	}

	@Override
	protected String title4PopupWindow() {
		return "text";
	}

	@Override
	protected void populateSubFieldEditorBean(TextEditor e) {
		this.regPane.populate(e.getRegex());
		regErrorMsgTextField.setText(e.getRegErrorMessage());
		waterMarkDictPane.populate(e);
	}

	@Override
	protected TextEditor updateSubFieldEditorBean() {
		TextEditor ob = newTextEditorInstance();
		ob.setRegErrorMessage(this.regErrorMsgTextField.getText());
		ob.setRegex(this.regPane.update());
		waterMarkDictPane.update(ob);

		return ob;
	}

	protected TextEditor newTextEditorInstance() {
		return new TextEditor();
	}

}