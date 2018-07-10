package com.fr.design.gui.itree.refreshabletree.loader;

import com.fr.general.NameObject;

public abstract class ChildrenLoaderFactory {

	public static ChildrenNodesLoader createChildNodesLoader(Object obj) {
		if (obj instanceof ChildrenNodesLoader) {
			return (ChildrenNodesLoader) obj;
		} else if (obj instanceof NameObject) {
			return createChildNodesLoader(((NameObject) obj).getObject());
		} else {
			return ChildrenNodesLoader.NULL;
		}
	}

}