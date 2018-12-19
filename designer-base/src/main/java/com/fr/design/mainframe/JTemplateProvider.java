package com.fr.design.mainframe;

public interface JTemplateProvider<T> {

    void fireTargetModified();

    T getTarget();
}