package com.fr.design.module;

import com.fr.base.ChartColorMatching;
import com.fr.base.ChartPreStyleConfig;
import com.fr.base.Utils;
import com.fr.config.Configuration;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;

import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 图表预定义管理 界面, 在工具栏-服务器管理中.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-21 下午02:33:48
 */
public class ChartPreStyleManagerPane extends JListControlPane {

	@Override
    /**
     * 创建有名字的creator
     * @return 有名字的creator数组
     */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[]{
				new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_PreStyle"),
						ChartColorMatching.class, ChartPreStylePane.class)
		};
	}

	@Override
	protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Chart-PreStyle");
	}
	
	public void populateBean() {
		ChartPreStyleConfig config = ChartPreStyleConfig.getInstance().mirror();
		ArrayList list = new ArrayList();
		
		Iterator keys = config.names();
		while(keys.hasNext()) {
			Object key = keys.next();
			ChartColorMatching value = (ChartColorMatching) config.getPreStyle(key);

			list.add(new NameObject(Utils.objectToString(key), value));
		}
		
		Nameable[] values = (Nameable[])list.toArray(new Nameable[list.size()]);
		populate(values);
		
		if(config.containsName(config.getCurrentStyle())) {
			this.setSelectedName(config.getCurrentStyle());
		}
	}
	
	public void updateBean() {
		Configurations.update(new Worker() {
			@Override
			public void run() {
				ChartPreStyleConfig config = ChartPreStyleConfig.getInstance();

				config.setCurrentStyle(getSelectedName());

				Nameable[] values = update();
				config.clearAllPreStyle();

				for(Nameable value : values) {
					config.putPreStyle(value.getName(), ((NameObject) value).getObject());
				}

				// 通知报表整个刷新.
				DesignerFrame frame = DesignerContext.getDesignerFrame();
				if(frame != null) {
					frame.repaint();
				}
			}

			@Override
			public Class<? extends Configuration>[] targets() {
				return new Class[]{ChartPreStyleConfig.class};
			}
		});

	}

}