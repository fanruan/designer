package com.fr.design.parameter;

import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.JTemplate;

import javax.swing.*;
import java.awt.*;


/**
 * 参数设计界面接口
 */
public interface ParameterDesignerProvider {

    void addListener(ParaDefinitePane paraDefinitePane);

    Component createWrapper();

    void setDesignHeight(int height);

    Dimension getDesignSize();

    Dimension getPreferredSize();

    void populate(ParameterUI p);

    void refreshAllNameWidgets();

    void refresh4TableData(String oldName, String newName);

    void refreshParameter(ParaDefinitePane paraDefinitePane);

    void refreshParameter(ParaDefinitePane paraDefinitePane, JTemplate jt);

    boolean isWithQueryButton();

    java.util.List<String> getAllXCreatorNameList();

    boolean isWithoutParaXCreator(Parameter[] ps);

    boolean isBlank();

    ParameterUI getParaTarget();

    boolean addingParameter2Editor(Parameter parameter, int index);

    boolean addingParameter2EditorWithQueryButton(Parameter parameter, int index);

    void addingAllParameter2Editor(Parameter[] parameterArray, int currentIndex);

    JPanel[] toolbarPanes4Form();

    JComponent[] toolBarButton4Form();

    void initBeforeUpEdit();

    void populateParameterPropertyPane(ParaDefinitePane p);

    void initWidgetToolbarPane();

    AuthorityEditPane getAuthorityEditPane();

    JPanel getEastUpPane();

    JPanel getEastDownPane();

    boolean isSupportAuthority();

    void removeSelection();

    ParameterBridge getParaComponent();
}