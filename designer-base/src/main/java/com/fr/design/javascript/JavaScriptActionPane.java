package com.fr.design.javascript;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.fun.JavaScriptActionProvider;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.ui.WebContentUtils;
import com.fr.js.JavaScript;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class JavaScriptActionPane extends UIComboBoxPane<JavaScript> {

    private static final long serialVersionUID = 1L;

    private List contentDBManiPane;

    private JavaScript call = null;

    public JavaScriptActionPane() {
        super();
    }

    protected List<FurtherBasicBeanPane<? extends JavaScript>> initPaneList() {
        List<FurtherBasicBeanPane<? extends JavaScript>> paneList = new ArrayList<FurtherBasicBeanPane<? extends JavaScript>>();
        // JS脚本,表单提交,提交入库,流程管理,发送邮件. 703中去掉表单提交和流程管理
        paneList.add(new JavaScriptImplPane(getDefaultArgs()));
//		paneList.add(new FormSubmitJavaScriptPane(this));
        contentDBManiPane = new ArrayList();
        contentDBManiPane.add(createDBManipulationPane());
        paneList.add(new Commit2DBJavaScriptPane(this, contentDBManiPane));
        paneList.add(initEmaiPane());
        Set<JavaScriptActionProvider> javaScriptActionProviders = ExtraDesignClassManager.getInstance().getArray(JavaScriptActionProvider.XML_TAG);
        if (javaScriptActionProviders != null) {
            for (JavaScriptActionProvider jsp : javaScriptActionProviders) {
                if(jsp.accept(DesignerContext.getDesignerFrame().getSelectedJTemplate())){
                    paneList.add(jsp.getJavaScriptActionPane(this));
                }
            }
        }
        // 自定义事件
//		paneList.add(new CustomActionPane());
        return paneList;
    }

    protected EmailPane initEmaiPane() {
        return new EmailPane();
    }

    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 6));
        JPanel northPane = new JPanel(new BorderLayout());
        northPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 0, 10));
        this.add(northPane, BorderLayout.NORTH);
        northPane.add(jcb, BorderLayout.CENTER);
        this.add(cardPane, BorderLayout.CENTER);

    }

    /**
     * 生成回调函数的按钮
     *
     * @return 返回按钮对象
     */
    public UIButton createCallButton() {
        UIButton callButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Callback_Function"));
        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JavaScriptActionPane callPane = new JavaScriptActionPane() {
                    @Override
                    protected String title4PopupWindow() {
                        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Callback_Function");
                    }

                    @Override
                    protected DBManipulationPane createDBManipulationPane() {
                        DBManipulationPane dbPane = JavaScriptActionPane.this.createDBManipulationPane();
                        dbPane.setParentJavaScriptActionPane(JavaScriptActionPane.this);
                        return dbPane;
                    }

                    @Override
                    public boolean isForm() {
                        return JavaScriptActionPane.this.isForm();
                    }

                    public String[] getDefaultArgs() {
                        return new String[]{WebContentUtils.FR_SUBMITINFO};
                    }

                };

                callPane.populateBean(getCall());

                BasicDialog dialog = callPane.showWindow(SwingUtilities.getWindowAncestor(JavaScriptActionPane.this));
                dialog.addDialogActionListener(new DialogActionAdapter() {

                    @Override
                    public void doOk() {
                        super.doOk();
                        setCall(callPane.updateBean());
                    }
                });

                dialog.setVisible(true);
            }
        });

        return callButton;
    }

    public void setCall(JavaScript call) {
        this.call = call;
    }

    public JavaScript getCall() {
        return call;
    }

    // 默认参数
    protected abstract String[] getDefaultArgs();

    //用于区别报表跟表单
    protected abstract boolean isForm();

    protected abstract DBManipulationPane createDBManipulationPane();

    public List getContentDBManiPane() {
        return contentDBManiPane;
    }

    public static JavaScriptActionPane defaultJavaScriptActionPane = new JavaScriptActionPane() {

        private static final long serialVersionUID = 1L;

        @Override
        public DBManipulationPane createDBManipulationPane() {
            JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            return jTemplate == null ? new DBManipulationPane() : jTemplate.createDBManipulationPane();
        }

        @Override
        protected String title4PopupWindow() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Callback_Function");
        }

        @Override
        public boolean isForm() {
            return false;
        }

        public String[] getDefaultArgs() {
            return new String[0];
        }
    };

    /**
     * 生成界面默认的组建
     *
     * @return 返回生成的面板

     */
            public static JavaScriptActionPane createDefault() {
                return new JavaScriptActionPane() {
            @Override
            public DBManipulationPane createDBManipulationPane() {
                return new DBManipulationPane();
            }

            @Override
            protected String title4PopupWindow() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Callback_Function");
            }

            @Override
            public boolean isForm() {
                return false;
            }

            public String[] getDefaultArgs() {
                return new String[0];
            }
        };
    }
}