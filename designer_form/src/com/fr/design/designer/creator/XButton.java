/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.fr.base.ScreenResolution;
import com.fr.base.background.GradientBackground;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.editors.ButtonTypeEditor;
import com.fr.design.mainframe.widget.editors.FontEditor;
import com.fr.design.mainframe.widget.editors.IconEditor;
import com.fr.design.mainframe.widget.editors.ImgBackgroundEditor;
import com.fr.design.mainframe.widget.editors.ShortCutTextEditor;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.design.mainframe.widget.renderer.IconCellRenderer;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 * @author richer
 * @since 6.5.3
 */
public class XButton extends XWidgetCreator {
	
	public final static Background DEFAULTBG = new GradientBackground(new Color(247,247,247),new Color(210,210,210),GradientBackground.TOP2BOTTOM);
	public final static Font DEFAULTFT = new Font("Song_TypeFace",0,12);
    public final static Color DEFAULTFOREGROUNDCOLOR = Color.BLACK;
	private Background bg;
	private UILabel contentLabel;
    
    public XButton(Button widget, Dimension initSize) {
        this(new FreeButton(widget),initSize);
    }

    public XButton(FreeButton widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    public XButton(FormSubmitButton widget, Dimension initSize) {
    	super(widget, initSize);
    }
    
	public Background getContentBackground() {
		return bg;
	}

	public void setContentBackground(Background bg) {
		this.bg = bg;
	}

	public UILabel getContentLabel() {
		return contentLabel;
	}

	public void setContentLabel(UILabel contentLabel) {
		this.contentLabel = contentLabel;
	}
    
    /**
     *根据下拉框选择返回按钮样式的默认设置或自定义设置列表
     * @return  列表
     * @throws IntrospectionException   抛错
     */
	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		CRPropertyDescriptor[] crp = ((FreeButton) data).isCustomStyle() ? getisCustomStyle() : getisnotCustomStyle();

		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), crp);
	}

    protected CRPropertyDescriptor creatNonListenerStyle(int i) throws IntrospectionException{
       CRPropertyDescriptor[] crPropertyDescriptors = {
               new CRPropertyDescriptor("text", this.data.getClass()).setI18NName(
					   Inter.getLocText(new String[] {"Form-Button", "Name"})),
               new CRPropertyDescriptor("customStyle", this.data.getClass()).setI18NName(
                       Inter.getLocText(new String[]{"Form-Button", "Style"})).setEditorClass(
                       ButtonTypeEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("initialBackground", this.data.getClass()).setEditorClass(
                       ImgBackgroundEditor.class).setI18NName(Inter.getLocText("FR-Designer_Background-Initial")).putKeyValue(
                       XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("overBackground", this.data.getClass()).setEditorClass(
                       ImgBackgroundEditor.class).setI18NName(Inter.getLocText("FR-Designer_Background-Over")).putKeyValue(
                       XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("clickBackground", this.data.getClass()).setEditorClass(
                       ImgBackgroundEditor.class).setI18NName(Inter.getLocText("FR-Designer_Background-Click")).putKeyValue(
                       XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("font", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_FRFont"))
                       .setEditorClass(FontEditor.class).setRendererClass(FontCellRenderer.class).putKeyValue(
                       XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("iconName", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_Icon"))
                       .setEditorClass(IconEditor.class).setRendererClass(IconCellRenderer.class).putKeyValue(
                       XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
               new CRPropertyDescriptor("hotkeys", this.data.getClass())
					   .setI18NName(Inter.getLocText("FR-Designer_Button-Hotkeys"))
                       .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
			   		   .setEditorClass(ShortCutTextEditor.class)

       };
        return crPropertyDescriptors[i];
    }
    protected CRPropertyDescriptor[] getisCustomStyle() throws IntrospectionException{
       return new CRPropertyDescriptor[]{
            creatNonListenerStyle(0) .setPropertyChangeListener(new PropertyChangeAdapter() {
                           @Override
                           public void propertyChange() {
                               setButtonText(((FreeButton) data).getText());
                           }
                       }),

            creatNonListenerStyle(1) .setPropertyChangeListener(new PropertyChangeAdapter() {
                           @Override
                           public void propertyChange() {
                               checkButonType();
                           }
                       }),
            creatNonListenerStyle(2).setPropertyChangeListener(
                       new PropertyChangeAdapter() {
                           @Override
                           public void propertyChange() {
                               bg = ((FreeButton) data).getInitialBackground();
                           }
                       }),
            creatNonListenerStyle(3),
            creatNonListenerStyle(4),
            creatNonListenerStyle(5) .setPropertyChangeListener(
                       new PropertyChangeAdapter() {
                           @Override
                           public void propertyChange() {
                               FreeButton button = (FreeButton) data;
                               if (button.getFont() != null) {
                                   contentLabel.setFont(button.getFont().applyResolutionNP(
                                           ScreenResolution.getScreenResolution()));
                                   contentLabel.setForeground(button.getFont().getForeground());
                               }
                           }
                       }),
            creatNonListenerStyle(6),
            creatNonListenerStyle(7)

       };
   }

    protected CRPropertyDescriptor[] getisnotCustomStyle() throws IntrospectionException {
		return new CRPropertyDescriptor[]{
			  new CRPropertyDescriptor("text", this.data.getClass())
					  .setI18NName(Inter.getLocText(new String[] {"Form-Button", "Name"}))
					  .setPropertyChangeListener(new PropertyChangeAdapter() {

						  @Override
						  public void propertyChange() {
							  setButtonText(((FreeButton) data).getText());
						  }
					  }),
			  new CRPropertyDescriptor("customStyle", this.data.getClass()).setI18NName(
					  Inter.getLocText(new String[]{"Form-Button", "Style"})).setEditorClass(
					  ButtonTypeEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
					  .setPropertyChangeListener(new PropertyChangeAdapter() {

						  @Override
						  public void propertyChange() {
							  checkButonType();
						  }
					  }),
			  new CRPropertyDescriptor("iconName", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_Icon"))
					  .setEditorClass(IconEditor.class).setRendererClass(IconCellRenderer.class).putKeyValue(
					  XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
			  new CRPropertyDescriptor("hotkeys", this.data.getClass()).setI18NName(
					  Inter.getLocText("FR-Designer_Button-Hotkeys"))
					  .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
				      .setEditorClass(ShortCutTextEditor.class)


		};

    }
    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = new UILabel();
            contentLabel = new UILabel();
        }
        return editor;
    }
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

        //不可见时，按钮.4f透明
        AlphaComposite composite = this.data.isVisible() ? (AlphaComposite)((Graphics2D)g).getComposite() : AlphaComposite.getInstance(AlphaComposite.SRC_OVER,HALF_OPACITY);
        ((Graphics2D)g).setComposite(composite);
		Dimension panelSize = (contentLabel).getSize();
		if(bg != null) {
			bg.paint(g, new Rectangle2D.Double(0, 0, panelSize.getWidth(), panelSize.getHeight()));
		}
	}
	
	public void setButtonText(String text) {
		contentLabel.setText(text);
	}
	
	private void checkButonType() {
		UILabel l = contentLabel;
		FreeButton button = (FreeButton) data;
	
		if (!button.isCustomStyle()) {
			l.setBorder(BorderFactory.createLineBorder(new Color(148, 148, 148)));
			bg = DEFAULTBG;
            contentLabel.setFont(DEFAULTFT);
            contentLabel.setForeground(DEFAULTFOREGROUNDCOLOR);
            editor.setLayout(new BorderLayout());
			editor.add(l, BorderLayout.CENTER);
		} else {
			l.setBorder(null);
			editor.setLayout(new BorderLayout());
			editor.add(l,BorderLayout.CENTER);
            if (button.getFont() != null) {
                contentLabel.setFont(button.getFont().applyResolutionNP(
                        ScreenResolution.getScreenResolution()));
                contentLabel.setForeground(button.getFont().getForeground());
            }
			l.setBounds(0, 0, button.getButtonWidth() == 0 ? this.getWidth() : button.getButtonWidth(), button
					.getButtonHeight() == 0 ? this.getHeight() : button.getButtonHeight());
			bg = button.getInitialBackground();
		}
	}
	
    @Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		checkButonType();
		UILabel l = contentLabel;
		FreeButton button = (FreeButton) data;
		l.setText(button.getText());
		if (button.isCustomStyle() && button.getFont() != null) {
			l.setFont(button.getFont().applyResolutionNP(ScreenResolution.getScreenResolution()));
			l.setForeground(button.getFont().getForeground());
		}

		l.setVerticalAlignment(SwingConstants.CENTER);
		l.setHorizontalAlignment(SwingConstants.CENTER);
		if (button.getButtonHeight() > 0 && button.getButtonWidth() > 0) {
			this.setSize(button.getButtonWidth(), button.getButtonHeight());
			l.setSize(button.getButtonWidth(), button.getButtonHeight());
			XLayoutContainer parent;
			if ((parent = XCreatorUtils.getParentXLayoutContainer(this)) instanceof XWAbsoluteLayout) {
				((XWAbsoluteLayout) parent).toData().setBounds(toData(), getBounds());
			}
		}
		l.setEnabled(button.isEnabled());
	}

    /**
     * 初始化按钮的Size
     * @return   尺寸
     */
	public Dimension initEditorSize() {
		FreeButton button = (FreeButton) data;
		if (checkbutton(button)) {
			return new Dimension(button.getButtonWidth(), button.getButtonHeight());
		}
		return super.initEditorSize();
	}
    private boolean checkbutton(FreeButton button){
        return (button.isCustomStyle() && button.getButtonHeight() > 0 && button.getButtonWidth() > 0) ;
    }
    @Override
    protected String getIconName() {
        return "button_16.png";
    }
}