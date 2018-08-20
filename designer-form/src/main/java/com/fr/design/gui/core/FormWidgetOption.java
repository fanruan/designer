package com.fr.design.gui.core;

import com.fr.base.BaseUtils;
import com.fr.design.i18n.Toolkit;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.form.ui.container.WVerticalBoxLayout;

import javax.swing.Icon;


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

	public static final WidgetOption ABSOLUTELAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Layout_Block_Absolute"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/layout_absolute_new.png"),
			WAbsoluteLayout.class);

	public static final WidgetOption BORDERLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Report_Border_Layout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_border.png"),
			WBorderLayout.class);

	public static final WidgetOption CARDLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Layout_Block_Tab"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/card_layout_16.png"),
			WCardLayout.class);

	public static final WidgetOption HORIZONTALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Layout_HBox"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_h_16.png"),
			WHorizontalBoxLayout.class);

	public static final WidgetOption VERTICALBOXLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Vertical_Box_Layout"), BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"),
			WVerticalBoxLayout.class);
	
	public static final WidgetOption FITLAYOUTCONTAINER = WidgetOptionFactory.createByWidgetClass(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Adaptive_Layout"),
			BaseUtils.readIcon("/com/fr/web/images/form/resources/boxlayout_v_16.png"),
			WFitLayout.class);

	public static final WidgetOption PARAMETERCONTAINER = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Parameter_Body"), BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter.png"),
			WParameterLayout.class);
	
	public static final WidgetOption ELEMENTCASE = WidgetOptionFactory.createByWidgetClass(Toolkit.i18nText("Fine-Design_Form_Report"), BaseUtils.readIcon("/com/fr/design/images/buttonicon/report_16.png"),
			ElementCaseEditor.class);
}
