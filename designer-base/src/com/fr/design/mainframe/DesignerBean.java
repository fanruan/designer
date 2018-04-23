package com.fr.design.mainframe;


public interface DesignerBean {

	void refreshBeanElement();

	public static final DesignerBean NULL = new DesignerBean() {

		@Override
		public void refreshBeanElement() {
			// TODO Auto-generated method stub

		}
	};

}