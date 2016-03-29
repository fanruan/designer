package com.fr.design.module;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.ModuleContext;
import com.fr.module.TopModule;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-24
 * Time   : 下午2:52
 * 所有设计器模块的父类
 */
public abstract class DesignModule extends TopModule {
	public void start() {
		super.start();
		App<?>[] apps = apps4TemplateOpener();
		for (App<?> app : apps) {
			DesignerFrame.registApp(app);
		}
        ModuleContext.registerStartedModule(DesignModule.class.getName(), this);
		StableFactory.registerMarkedClass(ExtraDesignClassManagerProvider.XML_TAG, ExtraDesignClassManager.class);
	}

    public boolean isStarted() {
        return ModuleContext.isModuleStarted(DesignModule.class.getName());
    }

	/**
	 * 返回设计器能打开的模板类型的一个数组列表
	 *
	 * @return 可以打开的模板类型的数组
	 */
	public abstract App<?>[] apps4TemplateOpener();

	/**
	 * 国际化文件路径
	 * @return 国际化文件路径
	 */
	public String[] getLocaleFile() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}


}