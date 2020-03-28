package com.fr.design.designer;

public interface EditingState {
	public void revert();

	public static EditingState NULL = new EditingState() {

		@Override
		public void revert() {
			//do nothing
		}

	};
}