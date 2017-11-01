package com.fr.design.web;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.Icon;
import com.fr.base.IconManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.gui.itextarea.DescriptionTextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.WidgetManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.ListMap;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 
 * carl：自定义Icon编辑
 *
 */
public class CustomIconPane extends BasicPane {
	private String selectedIconName = null;
	private ListMap iconButtonMap = null;
	private JPanel iconPane = null;
	private ButtonGroup bg;
	private UIScrollPane jsPane;
	// 老一次次去拿真麻烦
	private IconManager iconManager = null;
	private UIButton removeButton;
	private UIButton editButton;



	private static final int THE_WIDTH = 180;
	private static final int HORIZONTAL_COUNT = 6;
	private static final int HEIGHT_PER = 29;
	private static final int GAP = 10;
	
	public CustomIconPane() {
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_Icon")));
		JPanel noNamePane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
		centerPane.add(noNamePane, BorderLayout.CENTER);
		
		iconPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		
		// 开始加图标选择按钮
		initIcons();

		jsPane = new UIScrollPane(iconPane);
		refreshIconPane(false);
		jsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		jsPane.setPreferredSize(new Dimension(200,180));
		noNamePane.add(jsPane);
		
		DescriptionTextArea des = new DescriptionTextArea(2);
		des.setText(Inter.getLocText("FR-Designer_Custom_Icon_Message1"));
		centerPane.add(des, BorderLayout.SOUTH);		
		
		this.add(centerPane, BorderLayout.CENTER);
		
		JPanel eastPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
		JPanel buttonPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		
		// 增加、删除、编辑按钮
		initAddButton(buttonPane);
		initRemoveButton(buttonPane);
		initEditButton(buttonPane);

		eastPane.add(buttonPane);
		this.add(eastPane, BorderLayout.EAST);
		
	}

	private void initIcons() {
		iconButtonMap = new ListMap();
		iconManager = WidgetManager.getProviderInstance().getIconManager();
		bg = new ButtonGroup();
		Object[] names = iconManager.getIconNames();
		Object name = null;
		for (int i = 0; i < names.length; i++) {
			name = names[i];
			if (name == null || !(name instanceof String)) {
				continue;
			}
			// carl:默认选第一个
			if (this.selectedIconName == null) {
				this.selectedIconName = (String)name;
			}
			IconButton iconButton = new IconButton((String)name);
			iconButtonMap.put(name, iconButton);
			iconPane.add(iconButton);
			bg.add(iconButton);
		}
	}

	private void initAddButton(JPanel buttonPane) {
		UIButton addButton = new UIButton(Inter.getLocText("FR-Designer_Add"));
		addButton.setPreferredSize(new Dimension(80, 25));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final EditIconDialog add = new EditIconDialog();
				add.populate(null);
				add.showWindow(SwingUtilities.getWindowAncestor(CustomIconPane.this), new DialogActionAdapter() {
					@Override
					public void doOk() {
						Icon icon = add.update();
						if (iconManager.addIcon(icon, false)) {
							IconButton iconButton = null;//初始化

							addIcon(icon, iconButton);

						}else {
							// add failed
							JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_Custom_Icon_Message2"), Inter.getLocText("FR-Designer_Tooltips"), JOptionPane.WARNING_MESSAGE);
						}
					}
                }).setVisible(true);

			}
		});
		buttonPane.add(addButton);
	}

	private void initRemoveButton(JPanel buttonPane) {
		removeButton = new UIButton(Inter.getLocText("FR-Designer_Remove"));
		removeButton.setPreferredSize(new Dimension(80, 25));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iconManager.isSystemIcon(selectedIconName)) {
					return;
				}
				if (iconManager.removeIcon(selectedIconName)) {
					IconButton iconButton = (IconButton)iconButtonMap.get(selectedIconName);
					iconPane.remove(iconButton);
					iconButtonMap.remove(selectedIconName);
					bg.remove(iconButton);
					selectedIconName = ((IconButton)iconButtonMap.getByIndex(0)).iconName;
					refreshIconPane(false);
					CustomIconPane.this.validate();
					CustomIconPane.this.repaint();
				} else {
					// remove failed
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
							Inter.getLocText("FR-Designer_Custom_Icon_Message2"),
		    				Inter.getLocText("FR-Designer_Tooltips"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		buttonPane.add(removeButton);
	}

	private void initEditButton(JPanel buttonPane) {
		editButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
		editButton.setPreferredSize(new Dimension(80, 25));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (iconManager.isSystemIcon(selectedIconName)) {
					return;
				}
				Icon oldIcon = null;
				try {
					oldIcon= iconManager.getIcon(selectedIconName);
				} catch (CloneNotSupportedException e1) {
                    // do nothing
				}
				if (oldIcon == null) {
					JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
							"Error",
		    				"Error",
							JOptionPane.ERROR_MESSAGE);
				}

				final EditIconDialog edit = new EditIconDialog();
				edit.populate(oldIcon);
				edit.showWindow(DesignerContext.getDesignerFrame(), new IconDialogActionListener(oldIcon) {
					@Override
					public void doOk() {
						iconManager.removeIcon(oldIcon.getName());
						Icon icon = edit.update();
						if (iconManager.addIcon(icon, false)) {
							IconButton iconButton = (IconButton)iconButtonMap.get(oldIcon.getName());
							iconPane.remove(iconButton);
							bg.remove(iconButton);

							addIcon(icon, iconButton);

						} else {
							// 失败了再弄回去
							iconManager.addIcon(oldIcon, true);
							// edit failed
							JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_Custom_Icon_Message2"), Inter.getLocText("FR-Designer_Tooltips"), JOptionPane.WARNING_MESSAGE);
						}
					}
                }).setVisible(true);
			}
		});
		buttonPane.add(editButton);
	}

	private void updateButtonPane() {
		if (iconManager.isSystemIcon(selectedIconName)) {
			editButton.setEnabled(false);
            removeButton.setEnabled(false);
		} else {
            editButton.setEnabled(true);
            removeButton.setEnabled(true);
        }
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Custom_Icon_SelectIcon");
	}

    /**
     * 添加按钮
     *
     * @param icon 图标
     * @param iconButton 按钮
     */
	public void addIcon(Icon icon,IconButton iconButton){
		selectedIconName = icon.getName();
		iconButton = new IconButton(icon.getName());
		iconButtonMap.put(icon.getName(), iconButton);
		iconPane.add(iconButton);
		bg.add(iconButton);
		refreshIconPane(true);
		CustomIconPane.this.validate();
		CustomIconPane.this.repaint();
	}
	
	public static class IconDialogActionListener extends DialogActionAdapter {
		protected Icon oldIcon;
		
		public IconDialogActionListener(Icon oldIcon) {
			this.oldIcon = oldIcon;
		}	
	}
	
	public void populate(String iconName) {
		if (iconName == null) {
			return;
		}
		this.selectedIconName = iconName;
        updateButtonPane();
		this.repaint();
	}
	
	public String update() {
		return selectedIconName;
	}
	
	// 不知道怎么动态布局，就这么傻傻的调一下大小
	private void refreshIconPane(boolean down) {
		iconPane.setPreferredSize(new Dimension(THE_WIDTH, (iconButtonMap.size() / HORIZONTAL_COUNT + 1) * HEIGHT_PER + GAP));
		UIScrollBar jsBar = jsPane.createVerticalScrollBar();
		try {
			if (down) {
				// 将滚动条滚到最后
				jsBar.setValue(jsBar.getMaximum() - jsBar.getVisibleAmount());
			} else {
				jsBar.setValue(0);
			}
		} catch (RuntimeException re) {
            return;
		}
        updateButtonPane();
	}
	
	private class IconButton extends JToggleButton implements ActionListener{
		private String iconName;
		private Image iconImage = null;
        private static final int ICON_BUTTON_SIZE = 24;
        private static final int ICON_X = 4;
        private static final int ICON_Y = 4;
		public IconButton(String name) {
			this.iconName = name;
			this.addActionListener(this);
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setBorder(null);
            this.iconImage = WidgetManager.getProviderInstance().getIconManager().getIconImage(name);
            this.setToolTipText(iconName);
		}

        @Override
        public void updateUI() {
            setUI(new BasicButtonUI(){
                public void paint(Graphics g, JComponent c) {
                    super.paint(g, c);
                }
            });
        }
		
		@Override
		public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // carl:这里缩放显示 16 × 16
            if (iconImage != null) {
            	g2d.drawImage(iconImage, ICON_X, ICON_Y, IconManager.DEFAULT_ICONWIDTH, IconManager.DEFAULT_ICONHEIGHT, null);
            }
            if (this.iconName != null && ComparatorUtils.equals(this.iconName, selectedIconName)) {
            	g2d.setPaint(Color.RED);
            } else {
                g2d.setPaint(Color.LIGHT_GRAY);
            }
            GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, IconManager.DEFAULT_ICONWIDTH + 7,
            		IconManager.DEFAULT_ICONHEIGHT + 7), Constants.LINE_THICK);
        }
		
        @Override
		public Dimension getPreferredSize() {
            return new Dimension(ICON_BUTTON_SIZE, ICON_BUTTON_SIZE);
        }
        
        public void actionPerformed(ActionEvent evt) {
            CustomIconPane.this.selectedIconName = iconName;

            fireChagneListener();
			updateButtonPane();
            CustomIconPane.this.repaint();// repaint
        }

        @Override
		public void addChangeListener(ChangeListener changeListener) {
            this.changeListener = changeListener;
        }

        private void fireChagneListener() {
            if (this.changeListener != null) {
                ChangeEvent evt = new ChangeEvent(this);
                this.changeListener.stateChanged(evt);
            }
        }
		
	}
	
	private static class EditIconDialog extends BasicPane {
		private UITextField nameTextField;
		private UILabel showImageLabel;
		private Image iconImage = null;
		private String oldName = null;
	    
	    protected EditIconDialog() {
            init();
	    }

        private void init() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            double p = TableLayout.PREFERRED;
            double rowSize[] = {p, p};
            double columnSize[] = {p, p, p};

            UIButton browseButton = new UIButton(Inter.getLocText("FR-Designer_Custom_Icon_SelectIcon"));
            browseButton.setPreferredSize(new Dimension(80, 25));
            browseButton.setToolTipText(Inter.getLocText("FR-Designer_Click_this_button"));
            nameTextField = new UITextField(20);

            browseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
                    onBrowseButtonClicked();
				}
			});

            // 焦点丢失时看看名称是否已经存在
            nameTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    // do nothing
                }
                public void focusLost(FocusEvent e) {
                    if (oldName != null && ComparatorUtils.equals(oldName, nameTextField.getText())) {
                        return;
                    }
                    if (WidgetManager.getProviderInstance().getIconManager().contains(nameTextField.getText())) {
                        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                                Inter.getLocText("FR-Designer_Custom_Icon_Message3"),
                                Inter.getLocText("FR-Designer_Tooltips"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            JPanel imagePane = new JPanel();
            imagePane.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
            showImageLabel = new UILabel();
            showImageLabel.setPreferredSize(new Dimension(20,20));
            imagePane.add(showImageLabel);
            imagePane.add(browseButton);
            Component[][] components = {{new UILabel(Inter.getLocText("FR-Designer_Name") + ":"), nameTextField}, {new UILabel(Inter.getLocText("FR-Designer_Icon") + ":"), imagePane}};

            JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            this.add(centerPane, BorderLayout.CENTER);
        }

        private void onBrowseButtonClicked() {
            JFileChooser jf = new JFileChooser();
            // carl:不知道是否只要png格式,反正导出时全部都转成png了
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Icon Image File", "jpg", "jpeg", "png", "gif");
            jf.setFileFilter(fileFilter);

            if (JFileChooser.APPROVE_OPTION == jf.showOpenDialog(DesignerContext.getDesignerFrame())) {
                String path = jf.getSelectedFile().getAbsolutePath();
                // 将图片转化到16 × 16大小
                Image image = BaseUtils.readImage(path);
                BufferedImage bufferedImage = CoreGraphHelper.createBufferedImage(IconManager.DEFAULT_ICONWIDTH,
IconManager.DEFAULT_ICONHEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(image, 0, 0, IconManager.DEFAULT_ICONWIDTH, IconManager.DEFAULT_ICONHEIGHT, null);
                bufferedImage.flush();
                g2d.dispose();
                iconImage = bufferedImage;
                if (iconImage != null) {
                    showImageLabel.setIcon(new ImageIcon(iconImage));
                }
            }
        }

        @Override
	    protected String title4PopupWindow() {
	    	return Inter.getLocText(new String[]{"Add", "Icon"});
	    }
	    
	    // 用户自定义的只有name 和 imgae两属性， path不要去管
	    public void populate(Icon icon) {
	    	if (icon == null) {
				return;
			}
	    	oldName = icon.getName();
	    	nameTextField.setText(icon.getName());
	    	if (icon.getImage() != null) {
	    		showImageLabel.setIcon(new ImageIcon(icon.getImage()));
	    	}
	    	iconImage = icon.getImage();
	    }
	    
	    public Icon update() {
	    	// 有一个是空，都返回null，反正不会添加的
	    	if (StringUtils.isBlank(nameTextField.getText()) || iconImage == null) {
				return null;
			}
	    	return new Icon(nameTextField.getText(), iconImage);
	    }
	}
}