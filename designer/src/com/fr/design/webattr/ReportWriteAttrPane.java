package com.fr.design.webattr;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.VerifierListPane;
import com.fr.design.report.WriteShortCutsPane;
import com.fr.design.write.submit.SubmiterListPane;
import com.fr.general.Inter;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.bridge.StableFactory;
import com.fr.write.ReportWriteAttrProvider;

import javax.swing.*;
import java.awt.*;

public class ReportWriteAttrPane extends LoadingBasicPane {
	private SubmiterListPane submiterListPane;
//	private ValueVerifierEditPane valueVerifierEditPane;
	private VerifierListPane verifierListPane;
	private WriteShortCutsPane writeShortCutsPane;
	private ElementCasePane ePane;
	public ReportWriteAttrPane(ElementCasePane ePane){
		this.ePane = ePane;
	}

	@Override
	protected void initComponents(JPanel container) {
		container.setLayout(FRGUIPaneFactory.createBorderLayout());
		final UITabbedPane tabbedPane = new UITabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		container.add(tabbedPane, BorderLayout.CENTER);

		// peter: writeSQLAttrList的编辑.
		if (submiterListPane == null) {
			submiterListPane = new SubmiterListPane(ePane);
		}
//		valueVerifierEditPane = new ValueVerifierEditPane();
		if (verifierListPane == null) {
			verifierListPane = new VerifierListPane(ePane);
		}
		writeShortCutsPane = new WriteShortCutsPane();

		tabbedPane.addTab(Inter.getLocText("FR-Utils_Submit"), submiterListPane);
		tabbedPane.addTab(Inter.getLocText("Verify-Data_Verify"), verifierListPane);
		tabbedPane.addTab(Inter.getLocText("Writer-ShortCuts_Setting"), writeShortCutsPane);

//		tabbedPane.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				// 切换的时候这里会先于UITabbedPaneUI的MousePressed事件
//				// 会导致弹窗两次
//				int idx = ReportWriteAttrPane.this.getInvalidIndex();
//				if (idx >= 0 && idx != tabbedPane.getSelectedIndex()) {
//					try {
//	                    if (idx == 0) {
//							ReportWriteAttrPane.this.submiterListPane.checkValid();
//						} else {
//							ReportWriteAttrPane.this.verifierListPane.checkValid();
//						}
//					} catch (Exception exp) {
//						JOptionPane.showMessageDialog(ReportWriteAttrPane.this, exp.getMessage());
//						tabbedPane.setSelectedIndex(idx);
//					}
//				}
//			}
//		});
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("ReportD-Report_Write_Attributes");
	}

	public void populate(WorkSheet report) {
		if (report == null) {
			return;
		}

		ReportWriteAttrProvider reportWriteAttr = report.getReportWriteAttr();
		if (reportWriteAttr == null) {
			reportWriteAttr =  StableFactory.getMarkedInstanceObjectFromClass(ReportWriteAttrProvider.XML_TAG, ReportWriteAttrProvider.class);
		}

		this.submiterListPane.populate(reportWriteAttr);
		this.verifierListPane.populate(reportWriteAttr);

	}

    public ReportWriteAttrProvider update() {
        ReportWriteAttrProvider reportWriteAttr = StableFactory.getMarkedInstanceObjectFromClass(ReportWriteAttrProvider.XML_TAG, ReportWriteAttrProvider.class);

        this.submiterListPane.updateReportWriteAttr(reportWriteAttr);
        this.verifierListPane.updateReportWriteAttr(reportWriteAttr);
        return reportWriteAttr;
    }

	/**
	 * 检查是否合法
	 * 通过检查填报设置面板和校验设置面板
	 * @throws Exception
	 */
	public void checkValid() throws Exception {
		this.submiterListPane.checkValid();
		this.verifierListPane.checkValid();
	}

	private int getInvalidIndex() {
		int i = 0;
		try {
			submiterListPane.checkValid();
			i ++;
			verifierListPane.checkValid();
		} catch (Exception e) {
			return i;
		}
		return -1;
	}

}