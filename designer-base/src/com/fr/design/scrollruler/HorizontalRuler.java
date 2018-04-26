package com.fr.design.scrollruler;

public class HorizontalRuler extends BaseRuler {

	public HorizontalRuler(ScrollRulerComponent rc) {
		super(rc);
	}

	@Override
	protected RulerUI getRulerUI() {
		return new HorizontalRulerUI(getRulerComponent());
	}

	@Override
	public int getExtra() {
		return getRulerComponent().getHorizontalValue();
	}
}