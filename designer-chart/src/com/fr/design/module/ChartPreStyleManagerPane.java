package com.fr.design.module;

import com.fr.base.ChartPreStyleConfig;
import com.fr.base.Utils;
import com.fr.chart.base.ChartPreStyle;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.ListMap;
import com.fr.stable.Nameable;

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
				new NameObjectCreator(Inter.getLocText("FR-Designer_PreStyle"),
						ChartPreStyle.class, ChartPreStylePane.class)
		};
	}

	@Override
	protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Chart-PreStyle");
	}
	
	public void populateBean() {
		ChartPreStyleConfig config = ChartPreStyleConfig.getInstance();
		
		ArrayList list = new ArrayList();
		
		Iterator keys = config.names();
		while(keys.hasNext()) {
			Object key = keys.next();
			ChartPreStyle value = (ChartPreStyle)config.getPreStyle(key);
			
			list.add(new NameObject(Utils.objectToString(key), value));
		}
		
		Nameable[] values = (Nameable[])list.toArray(new Nameable[list.size()]);
		populate(values);
		
		if(config.containsName(config.getCurrentStyle())) {
			this.setSelectedName(config.getCurrentStyle());
		}
	}
	
	public void updateBean() {
		ChartPreStyleConfig config = ChartPreStyleConfig.getInstance();

		config.setPreStyleMap(new ListMap());

		Nameable[] values = this.update();

		config.setCurrentStyle(getSelectedName());
		
		for(int i = 0; i < values.length; i++) {
			Nameable value = values[i];
			config.putPreStyle(value.getName(), ((NameObject)value).getObject());
		}

		// 通知报表整个刷新. 
		DesignerFrame frame = DesignerContext.getDesignerFrame();
		if(frame != null) {
			frame.repaint();
		}
	}

}