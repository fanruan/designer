package com.fr.design.reportfit.menupane;

/**
 * 字体的两个选项组成的group
 * <p>
 * Created by Administrator on 2016/5/5/0005.
 */
public class FontRadioGroup extends FitRadioGroup {

    public void selectFontFit(boolean isFontFit) {
        selectIndexButton(isFontFit ? 0 : 1);
    }

    public boolean isFontFit() {
        return getSelectRadioIndex() == 0;
    }
}
