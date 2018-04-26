package com.fr.design.webattr;

import java.util.ArrayList;
import java.util.List;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
/**
 * 
 * @author zhou
 * 2012-3-22下午3:58:40
 */
public abstract class WidgetToolBarPane extends BasicBeanPane<ToolBarManager[]> {
	protected ToolBarPane northToolBar;
	protected ToolBarPane southToolBar;
	protected ToolBarManager defaultToolBar;

	public void setDefaultToolBar(ToolBarManager defaultToolBar) {
		this.defaultToolBar = defaultToolBar;
	}

	@Override
	public void populateBean(ToolBarManager[] toolBarManager) {
		if (ArrayUtils.isEmpty(toolBarManager)) {
			return;
		}
		for (int i = 0; i < toolBarManager.length; i++) {
			Location location = toolBarManager[i].getToolBarLocation();
			if (location instanceof Location.Embed) {
				if (((Location.Embed)location).getPosition() == Constants.TOP) {
					northToolBar.populateBean(toolBarManager[i].getToolBar());
				} else if (((Location.Embed)location).getPosition() == Constants.BOTTOM) {
					southToolBar.populateBean(toolBarManager[i].getToolBar());
				}
			}
		}
	}

	@Override
	public ToolBarManager[] updateBean() {
		List<ToolBarManager> toolBarManagerList = new ArrayList<ToolBarManager>();
		if (!northToolBar.isEmpty()) {
			ToolBarManager north = new ToolBarManager();
			north.setToolBar(northToolBar.updateBean());
			north.setToolBarLocation(Location.createTopEmbedLocation());
			toolBarManagerList.add(north);
		}

		if (!southToolBar.isEmpty()) {
			ToolBarManager south = new ToolBarManager();
			south.setToolBar(southToolBar.updateBean());
			south.setToolBarLocation(Location.createBottomEmbedLocation());
			toolBarManagerList.add(south);
		}
		return toolBarManagerList.toArray(new ToolBarManager[toolBarManagerList.size()]);
	}
}