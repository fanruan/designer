package com.fr.design.scrollruler;

public class VerticalRuler extends BaseRuler {

	public VerticalRuler(ScrollRulerComponent rc) {
		super(rc);
	}

	@Override
	protected RulerUI getRulerUI() {
		return new VerticalRulerUI(getRulerComponent());
	}

	@Override
	public int getExtra() {
		return getRulerComponent().getVerticalValue();
	}
}