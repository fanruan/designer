package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;

public enum Location implements Direction{
    // 枚举里面定义10个位置
	outer(new Outer()), add(new Add()), inner(new Inner()),
    left_top(new LeftTop()), top(new Top()), right_top(new RightTop()),
    right(new Right()), right_bottom(new RightBottom()),
    bottom(new Bottom()),left_bottom(new LeftBottom()), left(new Left());
    
    private Direction direction;

    private Location(Direction l) {
        direction = l;
    }

	public void drag(int dx, int dy, FormDesigner desinger) {
		direction.drag(dx, dy, desinger);
	}

    public int getActual() {
        return direction.getActual();
    }
    
	@Override
	public void updateCursor(FormDesigner formEditor) {
		direction.updateCursor(formEditor);
	}

	@Override
	public void backupBounds(FormDesigner formEditor) {
		direction.backupBounds(formEditor);
	}
}