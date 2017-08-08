package com.fr.design.designer.properties;

/**
 * 将控件属性 转化成字符串
 * @since 6.5.2
 */
public interface Encoder<T> {

    String encode(T v);
}