package com.fr.design.module;

import com.fr.base.ChartPreStyleConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.IOUtils;
import com.fr.transaction.CallBackAdaptor;
import com.fr.transaction.Configurations;
import com.fr.transaction.WorkerFacade;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

/**
 * 图表预定义样式Action.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-20 下午04:38:48
 */
public class ChartPreStyleAction extends UpdateAction {

	public ChartPreStyleAction() {
        this.setMenuKeySet(CHART_DEFAULT_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(IOUtils.readIcon("com/fr/design/images/chart/ChartType.png"));
		this.generateAndSetSearchText(ChartPreStyleManagerPane.class.getName());
	}

    /**
     * 动作
     * @param e 事件
     */
	public void actionPerformed(ActionEvent e) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		final ChartPreStyleManagerPane pane = new ChartPreStyleManagerPane();
		BasicDialog dialog = pane.showWindow(designerFrame);
		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				Configurations.modify(new WorkerFacade(ChartPreStyleConfig.class) {
					@Override
					public void run() {
						pane.updateBean();
					}

				}.addCallBack(new CallBackAdaptor() {
					@Override
					public void afterCommit() {
						DesignerFrame frame = DesignerContext.getDesignerFrame();
						if (frame != null) {
							frame.repaint();
						}
					}
				}));
			}
        });

		pane.populateBean();
		dialog.setVisible(true);
		
	}

	public static final MenuKeySet CHART_DEFAULT_STYLE = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'C';
		}

		@Override
		public String getMenuName() {
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Server_PreStyle");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};
}