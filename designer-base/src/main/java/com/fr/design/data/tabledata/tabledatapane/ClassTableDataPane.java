package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.Parameter;
import com.fr.data.impl.ClassTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.JavaEditorPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.IOUtils;

import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassTableDataPane extends AbstractTableDataPane<ClassTableData> {
    private UITextField classNameTextField;
    private UITableEditorPane<ParameterProvider> editorPane;

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

        UIButton browserButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select"));
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
        UIButton editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
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
        		{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Class_Name") + ":"), reportletNamePane},
        		{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Function_The_Class_Must_Implement_The_Interface") + "\"com.fr.data.Tabledata\"")},
        		{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Example") + ":\"com.fr.data.impl.ArrayTableData\"")},
                {null,new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Class_Location_Description", StableUtils.pathJoin(ProjectConstants.WEBINF_NAME, ProjectConstants.CLASSES_NAME)))}
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.add(northPane, BorderLayout.NORTH);
        this.add(initSouthPanel(), BorderLayout.SOUTH);
    }
    private JPanel initSouthPanel() {
        JPanel jpanel = new JPanel();
        jpanel.setPreferredSize(new Dimension(-1, 150));
        jpanel.setLayout(new BorderLayout());

        editorPane = new UITableEditorPane<ParameterProvider>(new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return new UITableEditAction[]{
                        new AddParaAction(),
                        new RemoveParaAction()
                };
            }
        }, " " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_TableData_Default_Para"));

        jpanel.add(editorPane, BorderLayout.CENTER);

        return jpanel;
    }

    public class AddParaAction extends UITableEditAction {
        public AddParaAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
            this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            java.util.List<ParameterProvider> oldParas = editorPane.update();
            oldParas.add(new Parameter());
            editorPane.populate(oldParas.toArray(new ParameterProvider[oldParas.size()]));
        }

        @Override
        public void checkEnabled() {
            //do nothing
        }
    }
    private class RemoveParaAction extends UITableEditAction {
        public RemoveParaAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));
            this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/control/remove.png"));
        }

        public void actionPerformed(ActionEvent e) {
            ParameterProvider selectedPara = editorPane.getTableModel().getSelectedValue();
            java.util.List<ParameterProvider> oldParas = editorPane.update();
            oldParas.remove(selectedPara);
            editorPane.populate(oldParas.toArray(new ParameterProvider[oldParas.size()]));
        }

        @Override
        public void checkEnabled() {
            //do nothing
        }
    }

    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Class_TableData");
    }

    @Override
    public void populateBean(ClassTableData ob) {
        this.editorPane.populate(ob.getParameters(Calculator.createCalculator()));
        this.classNameTextField.setText(ob.getClassName());
    }

    @Override
    public ClassTableData updateBean() {
        ClassTableData tableData = new ClassTableData(this.classNameTextField.getText());
        java.util.List<ParameterProvider> paras = this.editorPane.update();
        tableData.setParameters(paras.toArray(new ParameterProvider[paras.size()]));

        return tableData;
    }


}
