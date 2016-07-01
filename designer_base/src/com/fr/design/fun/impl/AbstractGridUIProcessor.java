package com.fr.design.fun.impl;

import com.fr.design.fun.GridUIProcessor;
import com.fr.stable.fun.mark.API;

@API(level = GridUIProcessor.CURRENT_LEVEL)
public abstract class AbstractGridUIProcessor implements GridUIProcessor {
	public int currentAPILevel() {
		return CURRENT_LEVEL;
	}

	public int layerIndex() {
		return DEFAULT_LAYER_INDEX;
	}
}
