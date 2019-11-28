package com.fr.design.gui.xpane;

import com.fr.base.background.GradientBackground;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XEditorHolder;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValueUtils;
import com.fr.general.Background;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ToolTipEditor extends JWindow {
    
    private static volatile ToolTipEditor editor = new ToolTipEditor();
    
    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                editor = new ToolTipEditor();
            }
        }, new PluginFilter() {
            
            @Override
            public boolean accept(PluginContext context) {
                
                return context.contain(PluginModule.ExtraDesign);
            }
        });
    }
    
    public static ToolTipEditor getInstance() {
        
        return editor;
    }

	private XEditorHolder holder;
	private Border buttonBorder = new UIRoundedBorder(new Color(149, 149, 149), 1, 5);
    private int Num = 24;

	private ToolTipEditor() {
		this.initComponents();
	}

	private void setEditor(XEditorHolder holder) {
		this.holder = holder;
	}

	private void transform(Widget createWidget) {
		XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(holder);
		EditorHolder widget = (EditorHolder) holder.toData();
		createWidget.setWidgetName(widget.getWidgetName());
		createWidget.setEnabled(widget.isEnabled());
		createWidget.setVisible(widget.isVisible());
		WidgetValueUtils.convertWidgetValue((DataControl) createWidget, widget.getWidgetValue().getValue());
		XCreator creator = null;
		creator = parent.replace(createWidget, holder);
		Component designer = holder.getDesignerEditor().getEditorTarget().getParent();
		if (designer instanceof FormDesigner) {
			((FormDesigner) designer).getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_EDITED);
			((FormDesigner) designer).getSelectionModel().setSelectedCreator(creator);
		}
	}

    /**
     * 显示XEditorHolder可成为类型弹出框
     * @param holder  控件
     * @param xAbs 横坐标
     * @param yAbs   纵坐标
     */
	public void showToolTip(XEditorHolder holder, int xAbs, int yAbs) {
		this.setEditor(holder);
		if (!this.isVisible()) {
			this.setVisible(true);
			if (xAbs + this.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width) {
				xAbs -= this.getWidth();
			}
			this.setLocation(xAbs, yAbs);
		}
	}

    /**
     * 判断是否隐藏
     * @return  是返回true
     */
	public boolean isEditorVisible() {
		return this.isVisible();
	}

    /**
     * 隐藏弹出框
     */
	public void hideToolTip() {
		this.setVisible(false);
	}

    /**
     * 充值大小
     * @param xEditorHolder       控件
     * @param bounds       新尺寸
     * @param oldBounds     旧尺寸
     */
	public void resetBounds(XEditorHolder xEditorHolder, Rectangle bounds, Rectangle oldBounds) {
		if (this.isVisible() && this.holder == xEditorHolder) {
			if (!GUICoreUtils.isTheSameRect(bounds, oldBounds)) {
				this.setVisible(false);
			}
		}
	}

	protected void initComponents() {
		EditorChoosePane pane = new EditorChoosePane();
		this.getContentPane().add(pane);
		this.setSize(pane.getPreferredSize());
	}

	class EditorChoosePane extends JPanel {
		private Background background;

		public EditorChoosePane() {
			super();
			background = new GradientBackground(Color.WHITE, new Color(234, 246, 254), GradientBackground.TOP2BOTTOM);
			this.setLayout(new EditorLayout());
			this.initComponents();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isOpaque()) {
				return;
			}
			Rectangle r = this.getBounds();
			background.paint(g, new RoundRectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight(), 5, 5));
		}

		protected void initComponents() {
            WidgetOption[] options = WidgetOption.getFormWidgetIntance();
            options = (WidgetOption[]) ArrayUtils.addAll(
                    options, ExtraDesignClassManager.getInstance().getParameterWidgetOptions()
            );
			for (WidgetOption o : options) {
				if (DataControl.class.isAssignableFrom(o.widgetClass())) {
					this.add(new EditorButton(o));
				}
			}
			this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2), new UIRoundedBorder(new Color(139, 139, 140), 1, 5)));
		}
	}

	class EditorLayout implements LayoutManager {
		int top = 4, left = 4, right = 4, bottom = 4, hgap = 2, vgap = 4, maxLine = 9;

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void layoutContainer(Container target) {
			synchronized (target.getTreeLock()) {
				Insets insets = target.getInsets();
				int nMembers = target.getComponentCount();
				for (int i = 0; i < nMembers; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						Dimension d = m.getPreferredSize();
						m.setBounds(insets.left + left + i % maxLine * (hgap + d.width), top + insets.top + i / maxLine * (vgap + d.height), d.width, d.height);
					}
				}
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			int nmembers = parent.getComponentCount();
			return new Dimension(maxLine * Num + insets.left + insets.right + right + left, (nmembers + maxLine - 1)
					/ maxLine * Num + insets.top + insets.bottom + top + bottom);
		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

	}

	class EditorButton extends UILabel {

		public EditorButton(final WidgetOption option) {
			super(option.optionIcon());
			this.setToolTipText(option.optionName());
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					ToolTipEditor.this.setVisible(false);
					ToolTipEditor.this.transform(option.createWidget());
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					EditorButton.this.setBorder(buttonBorder);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					EditorButton.this.setBorder(null);
				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(22, 22);
		}
	}
}