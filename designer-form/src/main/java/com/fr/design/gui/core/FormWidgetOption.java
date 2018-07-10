package com.fr.design.gui.core;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.form.ui.container.WVerticalBoxLayout;
import com.fr.general.Inter;

/**
 * Author : Shockway
 * Date: 13-6-17
 * Time: 上午10:40
 */
public class FormWidgetOption extends WidgetOption {
	
	/**
	 * 返回名字
	 * @return 名字
	 */
	@Override
	public String optionName() {
		return null;
	}

	/**
	 * 返回图标
	 * @return 图标
	 */
	@Override
	public Icon optionIcon() {
		return null;
	}

	/**
	 * 组件类
	 * @return 类
	 */
	@Override
	public Class<? extends Widget> widgetClass() {
		return null;
	}

	/**
	 * 返回组件
	 * @return 控件
	 */
	@Override
	public Widget createWidget() {
		return null;
	}

	/*
	 * 表单容器
	 */
	public static WidgetOption[] getFormContainerInstance() {
		return new WidgetOption[] { ABSOLUTELAYOUTCONTAINER, BORDERLAYOUTCONTAINER, HORIZONTALBOXLAYOUTCONTAINER, VERTICALBOXLAYOUTCONTAINER,
				CARDLAYOUTCONTAINER, FITLAYOUTCONTAINER };
	}

    /**
     * 表单工具栏上的布局
     * @return   控件
     */
    public static WidgetOption[] getFormLayoutInstance() {
        return new WidgetOption[] {CARDLAYOUTCONTAINER, ABSOLUTELAYOUTCONTAINER};
    }

	public static final WidgetOption ABSOLUTELAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_Layout_Block_Absolute"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/layout_absolute_new.png"),
			WAbsoluteLayout.class);

	public static final WidgetOption BORDERLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_BorderLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_border.png"),
			WBorderLayout.class);

	public static final WidgetOption CARDLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_Layout_Block_Tab"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/card_layout_16.png"),
			WCardLayout.class);

	public static final WidgetOption HORIZONTALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_Layout-HBox"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_h_16.png"),
			WHorizontalBoxLayout.class);

	public static final WidgetOption VERTICALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_VerticalBoxLayout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"),
			WVerticalBoxLayout.class);
	
	public static final WidgetOption FITLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter.getLocText("FR-Designer-Layout_Adaptive_Layout"), 
			BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"),
			WFitLayout.class);

	public static final WidgetOption PARAMETERCONTAINER = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_Para-Body"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter.png"),
			WParameterLayout.class);
	
	public static final WidgetOption ELEMENTCASE = WidgetOptionFactory.createByWidgetClass(Inter
			.getLocText("FR-Designer_Form-Report"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/report_16.png"),
			ElementCaseEditor.class);
}