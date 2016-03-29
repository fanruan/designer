package com.fr.start;

import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.report.ReportFooterAction;
import com.fr.design.actions.report.ReportHeaderAction;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 12/17/13
 * Time: 12:54 PM
 * 这是英文版的GridBI设计器启动类
 */
public class Designer4BI extends Designer {
	
	/**
	 * 启动BI的设计器
	 * 
	 * @param args 参数
	 * 
	 */
    public static void main(String[] args) {
        new Designer4BI(args);
    }

    public Designer4BI(String[] args) {
        super(args);
    }

    protected void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        FRContext.setLanguage(Constants.LANGUAGE_ENGLISH);
    }

    protected SplashPane createSplashPane() {
        return new BISplashPane();
    }

    @Override
    /**
     * 创建新建文件的快捷方式数组。
     * @return 返回快捷方式的数组
     */
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new NewWorkBookAction());
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    /**
	 * 获取模板-菜单选项
	 * 
	 * @param plus 当前的工作对象
	 * 
	 * @return 菜单栏对象数组
	 * 
	 */
    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus plus) {
        MenuDef[] menuDefs = plus.menus4Target();
        for (MenuDef m : menuDefs) {
            List<ShortCut> shortCuts = new ArrayList<ShortCut>();
            for (int i = 0, count = m.getShortCutCount(); i < count; i++) {
                ShortCut shortCut = m.getShortCut(i);
                if (!ComparatorUtils.equals(shortCut.getClass(), ReportHeaderAction.class)
                        && !ComparatorUtils.equals(shortCut.getClass(), ReportFooterAction.class)) {
                    shortCuts.add(shortCut);
                }
            }
            m.clearShortCuts();
            for (int i = 0, len = shortCuts.size(); i < len; i ++) {
                m.addShortCut(shortCuts.get(i));
            }

        }
        return menuDefs;
    }
}