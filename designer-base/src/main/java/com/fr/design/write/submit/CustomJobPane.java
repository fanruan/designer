package com.fr.design.write.submit;

import com.fr.base.GraphHelper;
import com.fr.data.AbstractClassJob;
import com.fr.design.data.tabledata.tabledatapane.ClassNameSelectPane;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.JavaEditorPane;
import com.fr.design.gui.frpane.ObjectProperiesPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;

import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author : Shockway
 * Date: 13-7-29
 * Time: 下午6:48
 */
public abstract class CustomJobPane  extends BasicBeanPane {
	protected UITextField classNameTextField;
	protected ObjectProperiesPane objectProperiesPane;
	private static final int DEFAULT_LENGTH = 25;

	public CustomJobPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel reportletNamePane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
		classNameTextField = new UITextField(getLengthOfTextField());
		reportletNamePane.add(classNameTextField);

		UIButton browserButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select"));
		browserButton.setPreferredSize(new Dimension(
				GraphHelper.getWidth(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select")) + 20,
				classNameTextField.getPreferredSize().height));

        UIButton editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
        editButton.setPreferredSize(new Dimension(
				GraphHelper.getWidth(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select")) + 20,
                classNameTextField.getPreferredSize().height));

		reportletNamePane.add(browserButton);
        reportletNamePane.add(editButton);

		browserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				final ClassNameSelectPane bPane = new ClassNameSelectPane();
				bPane.setClassPath(classNameTextField.getText());
				bPane.showWindow(
						SwingUtilities.getWindowAncestor(getWindowAncestor()),
						new DialogActionAdapter() {
							public void doOk() {
								classNameTextField.setText(bPane.getClassPath());
								checkAddButtonEnable();
							}
						}).setVisible(true);
			}
		});
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JavaEditorPane javaEditorPane = new JavaEditorPane(classNameTextField.getText(), JavaEditorPane.DEFAULT_SUBMIT_JOB);
                final BasicDialog dlg = javaEditorPane.showMediumWindow(SwingUtilities.getWindowAncestor(CustomJobPane.this),
                        new DialogActionAdapter() {
                            public void doOk() {
                                classNameTextField.setText(javaEditorPane.getClassText());
								checkAddButtonEnable();
                            }
                        });
				javaEditorPane.addSaveActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dlg.doOK();
					}
				});
                dlg.setVisible(true);
            }
        });

		reportletNamePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Class_Name"), null));
		this.add(reportletNamePane, BorderLayout.NORTH);

		objectProperiesPane = new ObjectProperiesPane();
		objectProperiesPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Property"), null));
		this.add(objectProperiesPane, BorderLayout.CENTER);

		UITextArea area = new UITextArea(2, 1);
		area.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Extend_Class", "com.fr.data.AbstractSubmitTask"));
		JPanel dsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		dsPane.add(area);
		dsPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom_Job_Description"), null));
		this.add(dsPane, BorderLayout.SOUTH);
		checkAddButtonEnable();
	}

	public int getLengthOfTextField() {
		return DEFAULT_LENGTH;
	}

	protected String title4PopupWindow() {
		return "CustomJob";
	}

	protected Component getWindowAncestor() {
		return this;
	}

	@Override
	public void populateBean(Object ob) {
		if (ob instanceof AbstractClassJob) {
			AbstractClassJob cj = (AbstractClassJob) ob;
			this.classNameTextField.setText(cj.getClassName());
			this.objectProperiesPane.populateBean(cj.getPropertyMap());
			checkAddButtonEnable();
		}
	}

	/**
	 * 添加按钮可用
	 */
	public void checkAddButtonEnable() {
		objectProperiesPane.enableAddButton(StringUtils.isNotEmpty(classNameTextField.getText()));
	}

	/**
	 * 重置
	 */
	public void reset() {
		this.classNameTextField.setText(null);
		this.checkAddButtonEnable();
	}
}
