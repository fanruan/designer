package com.fr.design.designer.properties;

import com.fr.design.Exception.ValidationException;

/**
 * 将字符串转化成控件控件属性,辅以判断该字符串是否合乎属性规则
 * @since 6.5.2
 */
public interface Decoder<T> {

    T decode(String txt);

    void validate(String txt) throws ValidationException;
}