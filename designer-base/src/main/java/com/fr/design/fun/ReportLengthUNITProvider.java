package com.fr.design.fun;

import com.fr.stable.fun.mark.Mutable;
import com.fr.stable.unit.UNIT;

/**
 * Created by kerry on 2020-04-09
 */
public interface ReportLengthUNITProvider extends Mutable {
    String MARK_STRING = "ReportLengthUNITProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 标尺单位显示字符
     * @return 标尺单位字符
     */
    String unitText();

    /**
     * 标尺单位类型(之前是将int类型的值直接保存在数据库里面的)
     * @return 返回标尺单位类型
     */
    int unitType();

    /**
     * UNIT转标尺单位值
     * @param value UNIT
     * @return 标尺单位值
     */
    float unit2Value4Scale(UNIT value);

    /**
     * 标尺单位值转UNIT
     * @param value 标尺单位值
     * @return UNIT
     */
    UNIT float2UNIT(float value);
}
