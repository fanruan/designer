package com.fr.design.dialog;

import com.fr.common.annotations.Open;

@Open
public interface DialogActionListener {

    void doOk();

    void doCancel();
}