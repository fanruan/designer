package com.fr.quickeditor.cellquick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.painter.BiasTextPainter;

public class CellBiasTextPainterEditor extends CellQuickEditor {

	@Override
	public JComponent createCenterBody() {
		UIButton editbutton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/m_insert/bias.png"));
		editbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showEditingDialog();
			}
		});
		editbutton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		editbutton.setMargin(null);
		editbutton.setOpaque(false);
		return editbutton;
	}

	private void showEditingDialog() {
		final BiasTextPainter oldbiasTextPainter = (BiasTextPainter)cellElement.getValue();
		final BiasTextPainterPane biasTextPainterPane = new BiasTextPainterPane();
		biasTextPainterPane.populate(oldbiasTextPainter);
		biasTextPainterPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

			@Override
			public void doOk() {
				BiasTextPainter newbiasTextPainter = biasTextPainterPane.update();
				if (!ComparatorUtils.equals(oldbiasTextPainter, newbiasTextPainter)) {
					cellElement.setValue(newbiasTextPainter);
					fireTargetModified();
				}
			}

		}).setVisible(true);
	}

	@Override
	protected void refreshDetails() {
		// TODO Auto-generated method stub

	}

}