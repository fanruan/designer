package com.fr.design.formula;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxTextArea;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.EncodeConstants;
import com.fr.stable.JavaCompileInfo;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class JavaEditorPane extends BasicPane {
    private RSyntaxTextArea javaText;
    private String className;
    private java.util.List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    public JavaEditorPane(String className, String defaultText) {
        this.className = className;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        javaText = new RSyntaxTextArea();
        configRSyntax(javaText);
        if (StringUtils.isNotEmpty(className)) {
            javaText.setText("//Loading source code from server...");
            new SwingWorker<String, Void>(){

                @Override
                protected String doInBackground() throws Exception {
                    InputStream in = getJavaSourceInputStream();
                    if (in == null) {
                        return null;
                    } else {
                        try {
                            return StableUtils.inputStream2String(in, EncodeConstants.ENCODING_UTF_8);
                        } catch (IOException e) {
                            FRContext.getLogger().error(e.getMessage(), e);
                        }
                    }
                    return null;
                }

                @Override
                public void done() {
                    try {
                        String source = get();
                        if (StringUtils.isNotEmpty(source)) {
                            javaText.setText(source);
                        } else {
                            javaText.setText("//Didn't find java source match the class");
                        }
                    } catch (Exception e) {
                        FRLogger.getLogger().error(e.getMessage(), e);
                        javaText.setText("//Loading source code from server error");
                    }
                }

            }.execute();
        } else {
            javaText.setText(defaultText);
        }

        UIScrollPane jt = new UIScrollPane(javaText);
        JPanel toolbarPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        UIButton saveButton = new UIButton(Inter.getLocText("Save"));
        saveButton.setAction(new SaveAction());
        UIButton compileButton = new UIButton(Inter.getLocText("Compile"));
        compileButton.setAction(new CompilerAction());
        toolbarPane.add(saveButton);
        toolbarPane.add(compileButton);

        this.add(toolbarPane, BorderLayout.NORTH);
        this.add(jt, BorderLayout.CENTER);
        UILabel label = new UILabel();
        label.setText("<html><font color='red'>" +
                Inter.getLocText(new String[]{"Attention", "Compile_Success_And_Then_Save"}, new String[]{":", "!"}) +
                "</font></html>");
        label.setPreferredSize(new Dimension(label.getWidth(),label.getHeight() + 20));
        this.add(label, BorderLayout.SOUTH);
    }

    public void addSaveActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    private void fireSaveActionListener() {
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(null);
        }
    }

    public String getClassText() {
        return this.className;
    }

    private InputStream getJavaSourceInputStream() {
        String javaPath = getJavaPath();
        try {
            return FRContext.getCurrentEnv().readBean(javaPath, ProjectConstants.CLASSES_NAME);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    private String getJavaPath() {
        String[] dirs = className.split("\\.");
        String path = StableUtils.pathJoin(dirs);
        return path + ".java";
    }

    private void configRSyntax(RSyntaxTextArea javaText) {
        javaText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        javaText.setAnimateBracketMatching(true);
        javaText.setAntiAliasingEnabled(true);
        javaText.setAutoIndentEnabled(true);
        javaText.setCodeFoldingEnabled(true);
        javaText.setUseSelectedTextColor(true);
        javaText.setCloseCurlyBraces(true);
        javaText.setBracketMatchingEnabled(true);
        javaText.setAntiAliasingEnabled(true);
        javaText.setCloseMarkupTags(true);
    }

    private class SaveAction extends AbstractAction { //新建文件命令
        public SaveAction() {
            super(Inter.getLocText("Save"));
        }
        public void actionPerformed(ActionEvent e) {
            saveTextToFile(javaText.getText());
        }
    }

    private void saveTextToFile(String text) {
        if (StringUtils.isEmpty(text)) {
             return;
        }
        if (StringUtils.isEmpty(className)) {
            return;
        }
        try {
            OutputStream out = FRContext.getCurrentEnv().writeBean(getJavaPath(), ProjectConstants.CLASSES_NAME);
            Writer writer = new BufferedWriter(new OutputStreamWriter(out, EncodeConstants.ENCODING_UTF_8));
            writer.write(text);
            writer.flush();
            writer.close();
            JOptionPane.showMessageDialog(null, Inter.getLocText(new String[]{"Save", "Successfully"}) + "！");
            fireSaveActionListener();
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, Inter.getLocText(new String[]{"Save", "Failed"}) + "！");
        }

    }

    private class CompilerAction extends AbstractAction {
        public CompilerAction() {
            super(Inter.getLocText("Compile"));
        }
        public void actionPerformed(ActionEvent e) {
            new SwingWorker<JavaCompileInfo, Void>() {

                @Override
                protected JavaCompileInfo doInBackground() throws Exception {
                    Env env = FRContext.getCurrentEnv();
                    return env.compilerSourceCode(javaText.getText());
                }

                public void done() {
                    try {
                        JavaCompileInfo info = get();
                        className = info.getIntactClassName();
                        String message = info.getCompileMessage();
                        if (StringUtils.isEmpty(message)) {
                            message = Inter.getLocText("Compile_Success") + "!";
                        }
                        JOptionPane.showMessageDialog(null, message);
                    } catch (InterruptedException e1) {
                        FRLogger.getLogger().error(e1.getMessage(), e1);
                    } catch (ExecutionException e1) {
                        FRLogger.getLogger().error(e1.getMessage(), e1);
                    }
                }
            }.execute();
        }
    }

    protected String title4PopupWindow() {
        return Inter.getLocText("FormulaD-Custom_Function");
    }

    public static final String DEFAULT_TABLEDATA_STRING = "package com.fr.data;\n" +
            "\n" +
            "\n" +
            "import com.fr.data.AbstractTableData;\n" +
            "import com.fr.general.data.TableDataException;\n" +
            "\n" +
            "public class CustomTableData extends AbstractTableData {\n" +
            "    public CustomTableData() {\n" +
            "        \n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 获取数据集的列数\n" +
            "     * @return 数据集的列\n" +
            "     * @throws TableDataException\n" +
            "     */\n" +
            "    public int getColumnCount() throws TableDataException {\n" +
            "        return 0;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 获取数据集指定列的列名\n" +
            "     * @param columnIndex 指定列的索引\n" +
            "     * @return 指定列的列名\n" +
            "     * @throws TableDataException\n" +
            "     */\n" +
            "    public String getColumnName(int columnIndex) throws TableDataException {\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 获取数据集的行数\n" +
            "     * @return 数据集数据行数\n" +
            "     * @throws TableDataException\n" +
            "     */\n" +
            "    public int getRowCount() throws TableDataException {\n" +
            "        return 0;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 获取数据集指定位置上的值\n" +
            "     * @param rowIndex 指定的行索引\n" +
            "     * @param columnIndex  指定的列索引\n" +
            "     * @return  指定位置的值\n" +
            "     */\n" +
            "    public Object getValueAt(int rowIndex, int columnIndex) {\n" +
            "        return null;\n" +
            "    }\n" +
            "}\n";

    public static final String DEFAULT_FUNCTION_STRING = "package com.fr.function;\n" +
            "\n" +
            "import com.fr.script.AbstractFunction;\n" +
            "\n" +
            "/**\n" +
            " * 自定义函数\n" +
            " */\n" +
            "public class CustomFun extends AbstractFunction {\n" +
            "    /**\n" +
            "     * @param args 函数的参数，是经过了算子处理了其中特殊参数的\n" +
            "     * @return 经过函数处理的值，用于参与最终计算\n" +
            "     */\n" +
            "    public Object run(Object[] args) {\n" +
            "        return null;\n" +
            "    }\n" +
            "}\n";

    public static final String DEFAULT_SUBMIT_JOB = "package com.fr.data;\n" +
            "\n" +
            "import com.fr.script.Calculator;\n" +
            "import com.fr.data.DefinedSubmitJob;\n" +
            "\n" +
            "public class CustomSubmitJob extends DefinedSubmitJob {\n" +
            "\n" +
            "\n" +
            "    public void doJob(Calculator calculator) throws Exception {\n" +
            "\n" +
            "    }\n" +
            "}\n";

}