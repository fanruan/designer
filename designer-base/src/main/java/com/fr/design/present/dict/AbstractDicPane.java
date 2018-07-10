package com.fr.design.present.dict;

import com.fr.data.Dictionary;
import com.fr.design.beans.FurtherBasicBeanPane;

public abstract class AbstractDicPane<T extends Dictionary> extends FurtherBasicBeanPane<T> {
	
	public abstract void resetComponets();

}