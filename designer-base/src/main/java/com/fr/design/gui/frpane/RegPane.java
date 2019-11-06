package com.fr.design.gui.frpane;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.reg.*;
import com.fr.general.ComparatorUtils;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;
import java.util.EventObject;

public class RegPane extends BasicPane {
    public static final RegExp[] ALL_REG_TYPE = {
            new NoneReg(),
            //new RequiredReg(),
            new LengthReg(),
            //new FloatReg(),
            new MailReg(),
            new IDCardReg(),
            new PostCardReg(),
            new PhoneReg(),
            new MobileReg(),
            new CustomReg()
    };

    public static final RegExp[] TEXTAREA_REG_TYPE = {
            new NoneReg(),
            new LengthReg(),
            new CustomReg()
    };

    public static final RegExp[] PASSWORD_REG_TYPE = TEXTAREA_REG_TYPE;

    private RegExp[] regType;
    private UIComboBox regComboBox;
    private CardLayout detailedCardLayout;
    private RegLengthPane regLengthPane;
    private RegPhonePane regPhonePane;
    private DefaultRegPane defaultRegPane;
    private CustomRegRexPane customRegRexPane;


    public UIComboBox getRegComboBox(){
        return regComboBox;
    }

    public RegPane() {
        this(ALL_REG_TYPE);
    }

    public RegPane(RegExp[] types) {
        this.regType = types;
        this.initComponents();
    }

    private void initComponents(){
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        regComboBox = new UIComboBox(regType);
        regComboBox.setRenderer(listCellRender);

        JPanel contentPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Input_Rule")), regComboBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();

        jPanel.add(contentPane, BorderLayout.NORTH);
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        final JPanel cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        detailedCardLayout = new CardLayout();
        cardPane.setLayout(detailedCardLayout);
        cardPane.add((defaultRegPane = new DefaultRegPane()), "Default");
        cardPane.add((regLengthPane = new RegLengthPane()), "Length");
        cardPane.add((regPhonePane = new RegPhonePane()), "Phone");
        cardPane.add((customRegRexPane = new CustomRegRexPane()), "Custom");
        centerPane.add(cardPane, BorderLayout.NORTH);
        jPanel.add(centerPane, BorderLayout.CENTER);
        this.add(jPanel, BorderLayout.NORTH);
        regComboBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                RegExp regExp = (RegExp)regComboBox.getSelectedItem();
                if(regExp instanceof PhoneReg) {
                    cardPane.setPreferredSize(new Dimension(220, 30));
                    Object selectItem = regPhonePane.dataTypeComboBox.getSelectedItem();
                    String regString = selectItem == null ? StringUtils.EMPTY : selectItem.toString();
                    firePhoneRegAction(regString);
                    detailedCardLayout.show(cardPane, "Phone");
                } else {
                    if (regExp instanceof LengthReg){
                        cardPane.setPreferredSize(new Dimension(220, 60));
                        detailedCardLayout.show(cardPane, "Length");
                    } else if (regExp instanceof CustomReg){
                        cardPane.setPreferredSize(new Dimension(220, 30));
                        detailedCardLayout.show(cardPane, "Custom");
                    } else {
                        cardPane.setPreferredSize(new Dimension(0,0 ));
                        detailedCardLayout.show(cardPane, "Default");
                    }
                }
                fireRegChangeAction();
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Input_Rule");
    }

    private int getRegTypeIndex(RegExp regex) {
        if (regex != null) {
            for (int i = 0; i < regType.length; i++) {
                if (regex.getClass().isInstance(regType[i])) {
                    return i;
                }
            }
        }

        return 0;
    }

    public void populate(RegExp regex) {
        regComboBox.setSelectedIndex(getRegTypeIndex(regex));
        if (regex instanceof LengthReg) {
            regLengthPane.populate(regex);
        } else if (regex instanceof PhoneReg) {
            regPhonePane.populate(regex);
        } else if (regex instanceof CustomReg) {
            customRegRexPane.populate(regex);
        } else {
            defaultRegPane.populate(regex);
        }
    }

    public RegExp update(){
        RegExp regExp = (RegExp)regComboBox.getSelectedItem();
        if (regExp instanceof LengthReg){
            return regLengthPane.update();
        } else if(regExp instanceof PhoneReg) {
            return regPhonePane.update();
        } else if(regExp instanceof NoneReg || regExp instanceof MailReg || regExp instanceof IDCardReg
                || regExp instanceof PostCardReg || regExp instanceof PhoneReg || regExp instanceof MobileReg) {
            return regExp;
        }
        else if (regExp instanceof CustomReg){
            if (customRegRexPane.isEmpty()) {
                return new NoneReg();
            }
            return customRegRexPane.update();
        } else {
            return defaultRegPane.update();
        }
    }




    private static abstract class DisplayPane extends BasicPane {
        public abstract void populate(RegExp regRex);

        public abstract RegExp update();
    }

    private static class DefaultRegPane extends DisplayPane {
        public RegExp regRex;

        public DefaultRegPane(){

        }


        @Override
        protected String title4PopupWindow() {
            return "Default";
        }

        @Override
        public void populate(RegExp regRex) {

        }

        @Override
        public RegExp update() {

            return this.regRex;
        }
    }

    /**
     * 添加电话规则监听器
     *
     * @param listener 监听器
     *
     *
     * @date 2014-12-3-下午7:30:55
     *
     */
    public void addPhoneRegListener(PhoneRegListener listener) {
        this.listenerList.add(PhoneRegListener.class, listener);
    }

    /**
     * 移除电话规则监听器
     *
     * @param listener 监听器
     *
     *
     * @date 2014-12-3-下午7:30:55
     *
     */
    public void removePhoneRegListener(PhoneRegListener listener) {
        this.listenerList.remove(PhoneRegListener.class, listener);
    }

    /**
     * 添加正则监听器
     *
     * @param listener 监听器
     *
     *
     * @date 2014-12-3-下午7:29:48
     *
     */
    public void addRegChangeListener(RegChangeListener listener) {
        this.listenerList.add(RegChangeListener.class, listener);
    }

    /**
     * 移除正则监听器
     *
     * @param listener 监听器
     *
     *
     * @date 2014-12-3-下午7:29:48
     *
     */
    public void removeRegChangeListener(RegChangeListener listener) {
        this.listenerList.remove(RegChangeListener.class, listener);
    }

    public class PhoneRegEvent extends EventObject {
        private String phoneRegString;
        public PhoneRegEvent(Object source, String phoneRegString) {
            super(source);
            this.setPhoneRegString(phoneRegString);
        }
        public void setPhoneRegString(String phoneRegString) {
            this.phoneRegString = phoneRegString;
        }
        public String getPhoneRegString() {
            return phoneRegString;
        }
    }
    public class RegChangeEvent extends EventObject {
        private String regString;
        public RegChangeEvent(Object source, String regString) {
            super(source);
            this.setRegString(regString);
        }
        public void setRegString(String regString) {
            this.regString = regString;
        }
        public String getRegString() {
            return regString;
        }
    }

    public interface PhoneRegListener extends EventListener{

        /**
         * 电话规则变化监听
         *
         * @param e 变化事件
         *
         *
         * @date 2014-12-3-下午7:29:01
         *
         */
        void phoneRegChangeAction(PhoneRegEvent e);

    }

    public interface RegChangeListener extends EventListener {

        /**
         * 正则表达规则变化监听
         *
         *
         * @date 2014-12-3-下午7:29:01
         *
         */
        void regChangeAction();
    }
    protected void firePhoneRegAction(String phoneReg) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==PhoneRegListener.class) {
                ((PhoneRegListener)listeners[i+1]).phoneRegChangeAction(new PhoneRegEvent(this , phoneReg));
            }
        }
    }
    protected void fireRegChangeAction() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==RegChangeListener.class) {
                ((RegChangeListener)listeners[i+1]).regChangeAction();
            }
        }
    }
    private class RegPhonePane extends DisplayPane {
        private static final String EMB_REG1 = "025-85679591";
        private static final String EMB_REG2 = "02585679591";
        private static final String EMB_REG3 = "025 85679591";
        private static final int LIMIT_LENGTH = 20;
        private static final String REG_PATTERN = "0123456789-*# ";

        private UIComboBox dataTypeComboBox;
        private final String[] dataType = {EMB_REG1, EMB_REG2, EMB_REG3, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom")};
        DefaultComboBoxModel DefaultComboBoxModel= new DefaultComboBoxModel(dataType);
        public RegPhonePane() {
            this.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, 0, 0));
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            dataTypeComboBox = new UIComboBox(DefaultComboBoxModel);
            JTextField editFiled = (JTextField)(dataTypeComboBox.getEditor().getEditorComponent());
            UILabel dataTypeLable = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Data_Type"));
            dataTypeLable.setPreferredSize(new Dimension(60, 20));
            JPanel panel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{dataTypeLable, dataTypeComboBox}}, TableLayoutHelper.FILL_LASTCOLUMN, 10, 0);
            this.add(panel);
            editFiled.setDocument(new LimitedDocument(LIMIT_LENGTH, REG_PATTERN));
            dataTypeComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange()   ==   ItemEvent.SELECTED) {
                        if(ComparatorUtils.equals(e.getItem(), dataType[3])) {
                            dataTypeComboBox.setSelectedItem(null);
                            dataTypeComboBox.setEditable(true);
                            firePhoneRegAction(EMB_REG1);
                        } else {
                            dataTypeComboBox.setEditable(false);
                            firePhoneRegAction(dataTypeComboBox.getSelectedItem().toString());
                        }
                    }
                }
            });
            dataTypeComboBox.setSelectedIndex(0);
            firePhoneRegAction(dataTypeComboBox.getSelectedItem().toString());
        }
        @Override
        protected String title4PopupWindow() {
            return "PHONE";
        }

        @Override
        public void populate(RegExp regRex) {
            if(!(regRex instanceof PhoneReg)) {
                return ;
            }
            String regstr = ((PhoneReg)regRex).getRegString();
            if (checkEmbedded(regstr)){
                DefaultComboBoxModel.addElement(regstr);
            }
            dataTypeComboBox.setSelectedItem(((PhoneReg)regRex).getRegString());
        }

        private boolean checkEmbedded(String regstr){
            return !ComparatorUtils.equals(EMB_REG1, regstr) &&
                    !ComparatorUtils.equals(EMB_REG2, regstr) &&
                    !ComparatorUtils.equals(EMB_REG3, regstr);
        }

        @Override
        public RegExp update() {
            PhoneReg regRex = new PhoneReg();
            regRex.setRegString((String)(dataTypeComboBox.getSelectedItem()));
            return regRex;
        }
    }

    private static class RegLengthPane extends DisplayPane {
        private UISpinner minLenSpinner;
        private UISpinner maxLenSpinner;

        public RegLengthPane(){
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, 0, 0));
            this.setPreferredSize(new Dimension(210, 56));
            minLenSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
            maxLenSpinner = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
            UILabel minLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Reg_Min_Length"));
            UILabel maxLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Reg_Max_Length"));
            minLabel.setPreferredSize(new Dimension(60, 20));
            maxLabel.setPreferredSize(new Dimension(60, 20));
            double f = TableLayout.FILL;
            double p = TableLayout.PREFERRED;
            Component[][] components = new Component[][]{
                    new Component[]{minLabel, minLenSpinner },
                    new Component[]{maxLabel, maxLenSpinner},
            };
            double[] rowSize = {p, p};
            double[] columnSize = {p,f};
            int[][] rowCount = {{1, 1},{1, 1}};
            JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, LayoutConstants.VGAP_MEDIUM);
            this.add(panel);


        }

        @Override
        protected String title4PopupWindow() {
            return "LENGTH";
        }

        @Override
        public void populate(RegExp regRex) {
            if (!(regRex instanceof LengthReg)){
                return;
            }

            int minLength = ((LengthReg)regRex).getMinLen();
            int maxLength = ((LengthReg)regRex).getMaxLen();
            minLenSpinner.setValue(minLength);
            maxLenSpinner.setValue(maxLength);
        }

        @Override
        public RegExp update() {
            int startLength = ((Number)minLenSpinner.getValue()).intValue();
            int endLength = ((Number)maxLenSpinner.getValue()).intValue();
            LengthReg regRex = new LengthReg();
            regRex.setMinLen(startLength);
            regRex.setMaxLen(endLength);

            return regRex;
        }
    }

    private static class CustomRegRexPane extends DisplayPane{
        private UITextField regTextField;

        public CustomRegRexPane(){
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, 0, 0));
            regTextField = new UITextField();
            JPanel panel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Reg_Expressions")), regTextField}}, TableLayoutHelper.FILL_LASTCOLUMN, 10, LayoutConstants.VGAP_MEDIUM);
            this.add(panel);
        }

        @Override
        protected String title4PopupWindow() {
            return "CUSTOM";
        }

        @Override
        public void populate(RegExp regRex) {
            if (!(regRex instanceof CustomReg)){
                return;
            }
            regTextField.setText(regRex.toRegText());
        }

        @Override
        public RegExp update() {
            return new CustomReg(regTextField.getText());
        }

        public boolean isEmpty() {
            return StringUtils.isEmpty(regTextField.getText());
        }
    }


    ListCellRenderer listCellRender = new UIComboBoxRenderer(){
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof NoneReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_None"));
            } else if (value instanceof LengthReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Length"));
            } else if (value instanceof MailReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Email"));
            } else if (value instanceof PhoneReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Phone"));
            } else if (value instanceof MobileReg) {
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Mobile_Phone"));
            } else if (value instanceof IDCardReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ID_Card"));
            } else if (value instanceof PostCardReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Post_Code"));
            } else if (value instanceof CustomReg){
                this.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom"));
            }
            return this;
        }

    };
}
