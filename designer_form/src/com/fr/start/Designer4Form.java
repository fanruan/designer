package com.fr.start;

import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.FormDesignerModule;


public class Designer4Form extends BaseDesigner {

    /**
     * 主函数
     * @param args 入口参数
     */
	public static void main(String[] args) {
		new Designer4Form(args);
	}

	public Designer4Form(String[] args) {
		super(args);
	}

	@Override
	protected String module2Start() {
		return FormDesignerModule.class.getName();
	}

    /**
     * 创建新建文件菜单
     * @return 菜单
     */
	public ShortCut[] createNewFileShortCuts() {
		return new ShortCut[]{
				new NewFormAction()
		};
	}
}