package com.fr.design.parameter;

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

//	private JWorkBook workbook;
	private ParaDefinitePane paraPane;

	public static ParameterPropertyPane THIS;
	private boolean isEditing = false;
	
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

		initParameterListener();
		
        this.setLayout(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        this.add(toolbarPane, BorderLayout.CENTER);
	}
	
	private void setEditor(FormDesigner editor) {
		this.remove(FormHierarchyTreePane.getInstance());
		this.add(FormHierarchyTreePane.getInstance(editor), BorderLayout.NORTH);
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
						if (paraPane.isWithQueryButton())
							paraPane.addingParameter2Editor(toolbarPane.getTargetParameter(parameterSelectedLabel));
						else {
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