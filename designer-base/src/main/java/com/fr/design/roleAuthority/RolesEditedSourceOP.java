package com.fr.design.roleAuthority;

import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.JTemplate;
import com.fr.privilege.PrivilegeEditedRoleProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author : daisy
 * Date: 13-9-25
 * Time: 下午4:57
 */
public class RolesEditedSourceOP extends RoleSourceOP {

	protected ExpandMutableTreeNode[] getNodeArrayFromMap(Map<String, RoleDataWrapper> map) {
		List<ExpandMutableTreeNode> roleList = new ArrayList<ExpandMutableTreeNode>();
		Iterator<Map.Entry<String, RoleDataWrapper>> entryIt = map.entrySet().iterator();
		while (entryIt.hasNext()) {
			Map.Entry<String, RoleDataWrapper> entry = entryIt.next();
			RoleDataWrapper t = entry.getValue();
			
			JTemplate jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
			PrivilegeEditedRoleProvider pe = (PrivilegeEditedRoleProvider) jt.getTarget();
			
			ExpandMutableTreeNode[] expand = t.load(Arrays.asList(pe.getAllEditedRoleSet()));
			for (ExpandMutableTreeNode expandMutableTreeNode : expand) {
				roleList.add(expandMutableTreeNode);
			}
		}
		return roleList.toArray(new ExpandMutableTreeNode[roleList.size()]);
	}
}