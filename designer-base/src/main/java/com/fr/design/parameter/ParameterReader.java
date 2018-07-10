package com.fr.design.parameter;

import com.fr.base.Parameter;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * 参数读取接口，可以根据不同的实现读取不同类型的模板参数（图表、表单、报表等具体的实现）
 */
public interface ParameterReader {

    /**
     * 读取模板参数指定路径下的模板参数
     * @param tplPath 模板路径
     * @return 参数数组
     */
    public Parameter[] readParameterFromPath(String tplPath);

    /**
     * 接受的类型
     * @param tplPath 模板路径
     * @param acceptTypes 类型
     * @return 如果接受该种类型，就执行读取操作
     */
    public boolean accept(String tplPath, String... acceptTypes);

}