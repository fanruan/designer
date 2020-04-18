package com.fr.design.mainframe.widget.accessibles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import com.fr.base.BaseUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.editors.TextField;
import com.fr.design.dialog.BasicPane;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.design.utils.gui.GUIPaintUtils;

/**
 * @since 6.5.3
 */
public class BaseAccessibleEditor extends BasicPane implements AccessibleEditor {

    private ArrayList<ChangeListener> listeners;
    private boolean showButton;
    protected Encoder encoder;
    private Decoder decoder;
    private UIButton btPopup;
    protected ITextComponent txtValue;

    public BaseAccessibleEditor(Encoder enc, Decoder dec, boolean showBtn) {
        listeners = new ArrayList<ChangeListener>();
        this.showButton = showBtn;
        this.encoder = enc;
        this.decoder = dec;
        initComponents();
        txtValue.setEditable(dec != null);
        ((JComponent) txtValue).setOpaque(true);
        ((JComponent) txtValue).setBorder(BorderFactory.createLineBorder(Color.lightGray));
        ((JComponent) txtValue).setBackground(Color.WHITE);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        if (decoder != null) {
            ((JComponent) txtValue).requestFocus();
        } else if (showButton) {
            btPopup.requestFocus();
        }
    }

    protected ITextComponent createTextField() {
        return new TextField() {
            public void registerChangeListener(UIObserverListener listener) {
                return;
            }

            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
    }

    private void initComponents() {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        txtValue = createTextField();
        setLayout(FRGUIPaneFactory.createBorderLayout());

        txtValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                txtValueActionPerformed(evt);
            }
        });
        add((JComponent) txtValue, BorderLayout.CENTER);
        setOpaque(false);

        if (showButton) {
            btPopup = new UIButton(){
                @Override
                public ButtonUI getUI() {
                    return new UIButtonUI() {
                        @Override
                        protected boolean isPressed(AbstractButton b) {
                            return model.isArmed() && model.isPressed();
                        }

                        @Override
                        protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                            if (isPressed(b) && b.isPressedPainted()) {
                                GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), UIConstants.COMBOBOX_BTN_PRESS);
                            } else if (isRollOver(b)) {
                                GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.COMBOBOX_BTN_ROLLOVER);
                            } else if (b.isNormalPainted()) {
                                GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.COMBOBOX_BTN_NORMAL);
                            }
                        }
                    };
                }
            };
            initPopupButton();
            btPopup.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    showEditorPane();
                }
            });
            add(btPopup, BorderLayout.EAST);
        }
    }
    
    @Override
    protected String title4PopupWindow() {
    	return "Base";
    }

    // 显示编辑器细节
    protected void showEditorPane() {
    }

    protected void initPopupButton() {
        if (!isComboButton()) {
            btPopup.setIcon(new ImageIcon(UIConstants.ACCESSIBLE_EDITOR_DOT));
            btPopup.setPreferredSize(new Dimension(20, 20));
        } else {
            btPopup.setRolloverEnabled(true);
            btPopup.setFocusPainted(false);
            btPopup.setPreferredSize(new Dimension(15, 19));
            btPopup.setBorderPainted(false);
            btPopup.setContentAreaFilled(false);
            btPopup.setMargin(new Insets(0, 0, 0, 0));
            btPopup.setIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_up.png"));
            btPopup.setPressedIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_down.png"));
            btPopup.setRolloverIcon(BaseUtils.readIcon("/com/fr/design/images/form/designer/drop_over.png"));
        }
    }

    // 有的编辑器是使用下拉框来直接选择的,这时候就把按钮显示成下拉框
    protected boolean isComboButton() {
        return false;
    }

    private void txtValueActionPerformed(ActionEvent evt) {
        try {
            validateValue();
            fireStateChanged();
        } catch (ValidationException e) {
            showMessage(e.getMessage(), this);
            txtValue.selectAll();
            ((JComponent) txtValue).requestFocus();
        }
    }

    @Override
    public Component getEditor() {
        return this;
    }

    @Override
    public Object getValue() {
        return decoder.decode(txtValue.getText());
    }

    @Override
    public void setValue(Object v) {
    	if(encoder != null) {
    		txtValue.setText(encoder.encode(v));
    	}
        txtValue.setValue(v);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    protected void fireStateChanged() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener l : listeners) {
            l.stateChanged(e);
        }
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void validateValue() throws ValidationException {
        if (decoder != null) {
            decoder.validate(txtValue.getText());
        }
    }

	public static void showMessage(String message, Component editorComponent) {
	    FineJOptionPane.showMessageDialog(editorComponent, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
	}


}