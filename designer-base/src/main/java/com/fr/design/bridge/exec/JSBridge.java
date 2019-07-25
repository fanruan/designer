package com.fr.design.bridge.exec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 * 用于标记一个方法是用于和JS做桥接的，避免被误删除
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface JSBridge {
}
