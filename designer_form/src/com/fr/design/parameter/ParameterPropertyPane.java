package com.fr.design.parameter;

import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ParameterPropertyPane extends JPanel{
	private ParameterToolBarPane toolbarPane;
	private ParaDefinitePane paraPane;
	private JPanel formHierarchyTreePaneWrapper;  // 封装一层，加边框
    private JPanel addParaPane;


    private static ParameterPropertyPane THIS;
	private boolean isEditing = false;
    private static final int HIDE_HEIGHT = 40;

	public static final ParameterPropertyPane getInstance() {
		if(THIS == null) {
			THIS = new ParameterPropertyPane();
		}
		return THIS;
	}

	public static final ParameterPropertyPane getInstance(FormDesigner editor) {
		if(THIS == null) {
			THIS = new ParameterPropertyPane();
		}
		THIS.setEditor(editor);
		return THIS;
	}

	public void repaintContainer() {
		validate();
		repaint();
		revalidate();
	}

	public ParameterPropertyPane() {
		toolbarPane = new ParameterToolBarPane();
		BasicScrollPane basicScrollPane = new BasicScrollPane() {
			@Override
			protected JPanel createContentPane() {
				return toolbarPane;
			}

			@Override
			public void populateBean(Object ob) {

			}

			@Override
			protected String title4PopupWindow() {
				return null;
			}
		};
        JPanel scrollPaneWrapperInner = new JPanel(new BorderLayout());
        scrollPaneWrapperInner.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 5));
        scrollPaneWrapperInner.add(basicScrollPane, BorderLayout.CENTER);
        addParaPane = new JPanel(new BorderLayout());
        addParaPane.add(scrollPaneWrapperInner, BorderLayout.CENTER);
        addParaPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.SPLIT_LINE));


        initParameterListener();
        this.setLayout(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(addParaPane, BorderLayout.CENTER);
	}

    public void setAddParaPaneVisible(boolean isVisible) {
        if (isVisible == addParaPane.isVisible()) {
            return;
        }
        if (isVisible && toolbarPane.hasSelectedLabelItem()) {
            addParaPane.setVisible(true);
            this.setPreferredSize(null);
        } else {
            addParaPane.setVisible(false);
            this.setPreferredSize(new Dimension(getWidth(), formHierarchyTreePaneWrapper.getPreferredSize().height + UIConstants.GAP_NORMAL));
        }
        repaintContainer();
    }
	
	private void setEditor(FormDesigner editor) {
		if (formHierarchyTreePaneWrapper == null) {
			formHierarchyTreePaneWrapper = new JPanel(new BorderLayout());
			formHierarchyTreePaneWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
			this.add(formHierarchyTreePaneWrapper, BorderLayout.SOUTH);
		}
		formHierarchyTreePaneWrapper.remove(FormHierarchyTreePane.getInstance());
		formHierarchyTreePaneWrapper.add(FormHierarchyTreePane.getInstance(editor), BorderLayout.CENTER);
	}

	private void initParameterListener() {
		toolbarPane.setParaMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(paraPane == null) {
					return;
				}
				final UIButton parameterSelectedLabel = (UIButton) e.getSource();
				// 不用多线程可能会出现死循环
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (paraPane.isWithQueryButton()) {
							paraPane.addingParameter2Editor(toolbarPane.getTargetParameter(parameterSelectedLabel));
						} else {
							paraPane.addingParameter2EditorWithQueryButton(toolbarPane.getTargetParameter(parameterSelectedLabel));
						}
					}
				});
			}
		});

		toolbarPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(paraPane == null) {
					return;
				}
				paraPane.addingAllParameter2Editor();
			}
		});
	}

	public ParameterToolBarPane getParameterToolbarPane() {
		return toolbarPane;
	}

	public void populateBean(ParaDefinitePane paraPane) {
		this.isEditing = false;
		this.paraPane = paraPane;
		this.isEditing = true;
	}
}