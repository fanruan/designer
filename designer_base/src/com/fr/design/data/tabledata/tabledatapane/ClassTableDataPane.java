package com.fr.design.data.tabledata.tabledatapane;

import com.fr.data.impl.ClassTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.JavaEditorPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClassTableDataPane extends AbstractTableDataPane<ClassTableData> {
    private UITextField classNameTextField;

    public ClassTableDataPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        // TableLayout
		double p = TableLayout.PREFERRED;
		
		double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, p};
        //Reportlet.
        JPanel reportletNamePane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        classNameTextField = new UITextField(36);
        reportletNamePane.add(classNameTextField);

        UIButton browserButton = new UIButton(Inter.getLocText("Select"));
        browserButton.setPreferredSize(new Dimension(
                browserButton.getPreferredSize().width,
                classNameTextField.getPreferredSize().height));
        reportletNamePane.add(browserButton);
        browserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                final ClassNameSelectPane bPane = new ClassNameSelectPane();
                bPane.setClassPath(classNameTextField.getText());
                BasicDialog dlg= bPane.showWindow(
                        (Dialog) SwingUtilities.getWindowAncestor(ClassTableDataPane.this),
                        new DialogActionAdapter() {
					public void doOk() {
						 classNameTextField.setText(bPane.getClassPath());
					}                
                });
                dlg.setVisible(true);
            }
        });
        UIButton editButton = new UIButton(Inter.getLocText("Edit"));
        editButton.setPreferredSize(new Dimension(
                editButton.getPreferredSize().width,
                classNameTextField.getPreferredSize().height));
        reportletNamePane.add(editButton);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JavaEditorPane javaEditorPane = new JavaEditorPane(classNameTextField.getText(), JavaEditorPane.DEFAULT_TABLEDATA_STRING);
                final BasicDialog dlg = javaEditorPane.showMediumWindow(SwingUtilities.getWindowAncestor(ClassTableDataPane.this),
                        new DialogActionAdapter() {
                            public void doOk() {
                                classNameTextField.setText(javaEditorPane.getClassText());
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

        Component[][] components = {
        		{new UILabel(Inter.getLocText("DS-Class_Name") + ":"), reportletNamePane},
        		{null, new UILabel(Inter.getLocText("Function-The_class_must_implement_the_interface") + "\"com.fr.data.Tabledata\"")},
        		{null, new UILabel(Inter.getLocText("Example") + ":\"com.fr.data.impl.ArrayTableData\"")},
                {null,new UILabel(Inter.getLocText(new String[]{"Function-The_class_must_be_located_in","Function-J2EE_server"},
                        new String[]{" ","\"",File.separator,ProjectConstants.WEBINF_NAME,File.separator,"classes\""}))}
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.add(northPane, BorderLayout.NORTH);
		// TODO alex_ENV
//        northPane.add(flowTableLayoutHelper.createLabelFlowPane(
//                "  ", new UILabel(Inter.getLocText("Example") + ":" +
//                BaseCoreUtils.pathJoin(new String[] {FRContext.getCurrentEnv().getPath(), "classes"}))));

    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("DS-Class_TableData");
    }

    @Override
    public void populateBean(ClassTableData ob) {
        this.classNameTextField.setText(ob.getClassName());
    }

    @Override
    public ClassTableData updateBean() {
    	return new ClassTableData(this.classNameTextField.getText());
    }


}