package com.fr.design.parameter;

import com.fr.stable.StringUtils;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * 参数读取的抽象实现
 */
public abstract class AbstractParameterReader implements ParameterReader {

    /**
     * 接受的类型
     * @param tplPath 模板路径
     * @param acceptTypes 类型
     * @return 如果接受该种类型，就执行读取操作
     */
    public boolean accept(String tplPath, String... acceptTypes) {
        if (StringUtils.isEmpty(tplPath)) {
            return false;
        }
        for (String accept : acceptTypes) {
            if (tplPath.endsWith(accept)) {
                return true;
            }
        }
        return false;
    }
}