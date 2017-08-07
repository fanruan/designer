package com.fr.design.widget.ui.designer;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.data.core.FormatField;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.DateEditor;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.UtilEvalError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditorDefinePane extends DirectWriteEditorDefinePane<DateEditor> {
    private UIComboBox returnTypeComboBox;
    private UILabel sampleLabel;// preview
    private UIComboBox dateFormatComboBox;
    private ValueEditorPane startDv;
    private ValueEditorPane endDv;
    private WaterMarkDictPane waterMarkDictPane;
    private FormWidgetValuePane formWidgetValuePane;
    private UISpinner fontSizePane;
    private UIHeadGroup formatHeader;

    public DateEditorDefinePane(XCreator xCreator) {
        super(xCreator);
    }


    @Override
    public String title4PopupWindow() {
        return "Date";
    }

    @Override
    protected JPanel setFirstContentPane() {
        waterMarkDictPane = new WaterMarkDictPane();
        formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
        fontSizePane = new UISpinner(0, 20, 1, 0);
        JPanel returnTypePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        returnTypePane.add(new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type") + ":"), BorderLayout.WEST);
        returnTypeComboBox = new UIComboBox(new String[]{Inter.getLocText("String"), Inter.getLocText("Date")});
        returnTypeComboBox.setPreferredSize(new Dimension(70, 20));
        // sample pane
        sampleLabel = new UILabel("");
        sampleLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sampleLabel.setFont(FRContext.getDefaultValues().getFRFont());
        JPanel previewPane = FRGUIPaneFactory.createTitledBorderPane("示例");
        previewPane.add(sampleLabel);
        // content pane
        String[] arr = getDateFormateArray();
        dateFormatComboBox = new UIComboBox(arr);
        dateFormatComboBox.setPreferredSize(new Dimension(150, 20));
        dateFormatComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshPreviewLabel();
            }

        });
        startDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        endDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        initFormatHeader();

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")), formWidgetValuePane},
                new Component[]{new UILabel(Inter.getLocText("FR-Engine_Format") + ":"), dateFormatComboBox},
                new Component[]{null, previewPane},
                new Component[]{new UILabel(Inter.getLocText("FS_Start_Date") + ":"), startDv},
                new Component[]{new UILabel(Inter.getLocText("FS_End_Date") + ":"), endDv},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark") + ":"), waterMarkDictPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane},
                new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type") + ":"), returnTypeComboBox}

        };
        double[] rowSize = {p, p, p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return panel;
    }

    protected void initFormatHeader() {
        String [] tabTitles = getDateFormateArray();
        formatHeader = new UIHeadGroup(tabTitles){
            protected void tabChanged(int newSelectedIndex) {

            }
        };
    }

    private String[] getDateFormateArray() {
        return FormatField.getInstance().getDateFormatArray();
    }

    protected JPanel initStartEndDatePane() {
        JPanel rangePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        rangePane.add(new UILabel(Inter.getLocText("FS_Start_Date") + ":"));
        startDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        rangePane.add(startDv);
        rangePane.add(new UILabel(Inter.getLocText("FS_End_Date") + ":"));
        endDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        rangePane.add(endDv);

        return rangePane;
    }


    private void refreshPreviewLabel() {
        String text = (String) dateFormatComboBox.getSelectedItem();
        if (text != null && text.length() > 0) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
                String sample = simpleDateFormat.format(new Date());
                Color c = Color.black;
                if (!ArrayUtils.contains(FormatField.getInstance().getDateFormatArray(), text)) {
                    sample += " " + Inter.getLocText("DateFormat-Custom_Warning");
                    c = Color.red;
                }
                this.sampleLabel.setText(sample);
                this.sampleLabel.setForeground(c);
            } catch (Exception exp) {
                this.sampleLabel.setForeground(Color.red);
                this.sampleLabel.setText(exp.getMessage());
            }
        } else {
            this.sampleLabel.setText(new Date().toString());
        }
    }

    @Override
    protected void populateSubDirectWriteEditorBean(DateEditor e) {
        String formatText = e.getFormatText();
		dateFormatComboBox.setSelectedItem(formatText);

		returnTypeComboBox.setSelectedIndex(e.isReturnDate() ? 1 : 0);
        formWidgetValuePane.populate(e);
        populateStartEnd(e);
    }

    @Override
    protected DateEditor updateSubDirectWriteEditorBean() {
        DateEditor ob = new DateEditor();

        ob.setFormatText(this.getSimpleDateFormat().toPattern());
        ob.setReturnDate(returnTypeComboBox.getSelectedIndex() == 1);
        formWidgetValuePane.update(ob);
        updateStartEnd(ob);

        return ob;
    }

    /**
     * 初始起止日期
     *
     * @param dateWidgetEditor 日期控件
     */
    public void populateStartEnd(DateEditor dateWidgetEditor) {
        Formula startFM = dateWidgetEditor.getStartDateFM();
        Formula endFM = dateWidgetEditor.getEndDateFM();
        if (startFM != null) {
            startDv.populate(startFM);
        } else {
            String startStr = dateWidgetEditor.getStartText();
            startDv.populate(StringUtils.isEmpty(startStr) ? null : DateUtils.string2Date(startStr, true));
        }
        if (endFM != null) {
            endDv.populate(endFM);
        } else {
            String endStr = dateWidgetEditor.getEndText();
            endDv.populate(StringUtils.isEmpty(endStr) ? null : DateUtils.string2Date(endStr, true));
        }
    }

    /**
     * 更新日期控件的起止日期
     *
     * @param dateWidgetEditor 日期控件
     */
    public void updateStartEnd(DateEditor dateWidgetEditor) {
        Object startObject = startDv.update();
        Object endObject = endDv.update();
        // wei : 对公式的处理
        Calculator cal = null;
        if (startObject instanceof Formula) {
            cal = Calculator.createCalculator();
            Formula startFormula = (Formula) startObject;
            try {
                startFormula.setResult(cal.evalValue(startFormula.getContent()));
            } catch (UtilEvalError e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            startObject = startFormula.getResult();
            dateWidgetEditor.setStartDateFM(startFormula);
            dateWidgetEditor.setStartText(null);
        } else {
            try {
                dateWidgetEditor.setStartText(startObject == null ? "" : DateUtils.getDate2Str("MM/dd/yyyy", (Date) startObject));
            } catch (ClassCastException e) {
                //wei : TODO 说明应用的公式不能转化成日期格式，应该做些处理。
            }
        }
        if (endObject instanceof Formula) {
            cal = Calculator.createCalculator();
            Formula endFormula = (Formula) endObject;
            try {
                endFormula.setResult(cal.evalValue(endFormula.getContent()));
            } catch (UtilEvalError e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            endObject = endFormula.getResult();
            dateWidgetEditor.setEndDateFM(endFormula);
            dateWidgetEditor.setEndText(null);
        } else {
            try {
                dateWidgetEditor.setEndText(endObject == null ? "" : DateUtils.getDate2Str("MM/dd/yyyy", (Date) endObject));
            } catch (ClassCastException e) {

            }
        }
    }

    private SimpleDateFormat getSimpleDateFormat() {
        String text = (String) dateFormatComboBox.getSelectedItem();
        SimpleDateFormat simpleDateFormat;
        if (text != null && text.length() > 0) {
            try {
                simpleDateFormat = new SimpleDateFormat(text);
                this.sampleLabel.setText(simpleDateFormat.format(new Date()));
            } catch (Exception exp) {
                simpleDateFormat = new SimpleDateFormat("");
            }
        } else {
            simpleDateFormat = new SimpleDateFormat("");
        }

        return simpleDateFormat;

    }

}