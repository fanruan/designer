package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.file.newReport.NewPolyReportAction;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.server.ServerConfigManagerAction;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.actions.server.WidgetManagerAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIPreviewButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.bbs.UserInfoLabel;
import com.fr.design.mainframe.bbs.UserInfoPane;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.module.DesignerModule;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLTools;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Designer extends BaseDesigner {
    private static final int TOOLBARPANEVGAP = -4;
    private static final int PREVIEW_DOWN_X_GAP = 92;
    private static final int GAP = 7;
    
    private static final String OLD_ENV_FOLDER_71 = ".FineReport71";
    private static final String OLD_ENV_FOLDER_70 = ".FineReport70";

    private UserInfoPane userInfoPane;
    
    private UIButton saveButton;
    private UIButton undo;
    private UIButton redo;
    private UIPreviewButton run;
    

    /**
     * 设计器启动的Main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        new Designer(args);
    }

    public Designer(String[] args) {
        super(args);
    }

    @Override
    protected String module2Start() {
        return DesignerModule.class.getName();
    }

    @Override
    /**
     * 创建新建文件的快捷方式数组。
     * @return 返回快捷方式的数组
     */
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new NewWorkBookAction());
        shortCuts.add(new NewPolyReportAction());
        try {
            if (DesignModuleFactory.getNewFormAction() != null) {
                shortCuts.add((ShortCut) DesignModuleFactory.getNewFormAction().newInstance());
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    @Override
    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = super.createServerMenuDef(plus);

        if (FRContext.getCurrentEnv() == null) {
            return menuDef;
        }

        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(SeparatorDef.DEFAULT);

            if (FRContext.getCurrentEnv().isRoot()) {
                menuDef.addShortCut(new ServerConfigManagerAction(), new StyleListAction(), new WidgetManagerAction());
                if (ActionUtils.getChartPreStyleAction() != null) {
                    menuDef.addShortCut(ActionUtils.getChartPreStyleAction());
                }
            }

            insertMenu(menuDef, MenuHandler.SERVER);
        }

        return menuDef;
    }

    @Override
    /**
     * 创建设计器上几个比较大的图标：新建cpt，保存，前进，后退，运行。
     * @return 返回大图标对应的工具栏
     */
    public UILargeToolbar createLargeToolbar() {
        UILargeToolbar largeToolbar = super.createLargeToolbar();
        largeToolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 4));
        largeToolbar.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = 1;
                return dim;
            }
        });
        createRunButton(largeToolbar);
        largeToolbar.add(run);
        largeToolbar.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = GAP;
                return dim;
            }
        });
        largeToolbar.addSeparator(new Dimension(2, 42));
        largeToolbar.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), UIConstants.LINE_COLOR));
        return largeToolbar;
    }

    /**
     * 创建上面一排的工具栏按钮
     * @return 按钮
     */
    public UIButton[] createUp() {
        return new UIButton[]{createSaveButton(), createUndoButton(), createRedoButton()};
    }


    private UIButton createSaveButton() {
        saveButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/save.png"));
        saveButton.setToolTipText(KeySetUtils.SAVE_TEMPLATE.getMenuKeySetName());
        saveButton.set4ToolbarButton();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jt.stopEditing();
                jt.saveTemplate();
                jt.requestFocus();
            }
        });
        return saveButton;
    }
    
    private UIButton createUndoButton() {
        undo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/undo.png"));
        undo.setToolTipText(KeySetUtils.UNDO.getMenuKeySetName());
        undo.set4ToolbarButton();
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.undo();
                }
            }
        });
        return undo;
    }

    private UIButton createRedoButton() {
        redo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/redo.png"));
        redo.setToolTipText(KeySetUtils.REDO.getMenuKeySetName());
        redo.set4ToolbarButton();
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.redo();
                }
            }
        });
        return redo;
    }

    private void createRunButton(UILargeToolbar largeToolbar) {
        run = new UIPreviewButton(new UIButton(UIConstants.PAGE_BIG_ICON) {
            public Dimension getPreferredSize() {
                return new Dimension(34, 34);
            }
        }, new UIButton(UIConstants.PREVIEW_DOWN) {
            public Dimension getPreferredSize() {
                return new Dimension(34, 10);
            }
        }
        ) {
            @Override
            protected void upButtonClickEvent() {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }
                if (jt instanceof JWorkBook) {
                    WebPreviewUtils.onWorkbookPreview(jt);
                } else if (jt instanceof BaseJForm) {
                    WebPreviewUtils.onFormPreview(jt);
                }
            }

            @Override
            protected void downButtonClickEvent() {
                final JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }

                UIPopupMenu menu = new UIPopupMenu();

                UIMenuItem[] items = jt.createMenuItem4Preview();
                for (int i = 0; i < items.length; i++) {
                    menu.add(items[i]);
                }
                GUICoreUtils.showPopupMenu(menu, MutilTempalteTabPane.getInstance(), MutilTempalteTabPane.getInstance().getX() - PREVIEW_DOWN_X_GAP, MutilTempalteTabPane.getInstance().getY() - 1 + MutilTempalteTabPane.getInstance().getHeight());
            }

            @Override
            public Dimension getPreferredSize() {
                // TODO Auto-generated method stub
                return new Dimension(34, 46);
            }
        };
        run.setExtraPainted(false);
        run.set4Toolbar();
        run.getUpButton().setToolTipText(Inter.getLocText("FR-Designer_Preview"));
        run.getDownButton().setToolTipText(Inter.getLocText("FR-Designer_Dropdown-More-Preview"));
    }

    @Override
    protected void refreshLargeToolbarState() {
        JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jt == null) {
            return;
        }
        saveButton.setEnabled(!jt.isSaved());
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(HistoryTemplateListPane.getInstance().getHistoryList());
        MutilTempalteTabPane.getInstance().repaint();
        if (DesignerEnvManager.getEnvManager().isSupportUndo()) {
            undo.setEnabled(jt.canUndo());
            redo.setEnabled(jt.canRedo());
        } else {
            undo.setEnabled(false);
            redo.setEnabled(false);
        }

        run.getUpButton().setIcon(jt.getPreviewLargeIcon());

    }

    /**
     * 生成工具栏
     * @param toolbarComponent 工具栏
     * @param plus             对象
     * @return  更新后的toolbar
     */
    public JComponent resetToolBar(JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        //如果是处于权限编辑状态
        if (BaseUtils.isAuthorityEditing()) {
            if (plus instanceof JWorkBook && plus.toolbars4Target() == null) {
                //聚合块编辑
                return super.polyToolBar(Inter.getLocText(new String[]{"Polybolck", "DashBoard-Potence", "Edit"}));
            } else {
                return plus.toolBar4Authority();
            }
        }

        if (plus.toolbarPanes4Form().length == 0) {
            return super.resetToolBar(toolbarComponent, plus);
        } else {
            JPanel toolbarPane;
            toolbarPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, TOOLBARPANEVGAP));
            Dimension dim = new Dimension();
            dim.height = plus.getToolBarHeight();
            toolbarPane.setPreferredSize(dim);
            toolbarPane.setFocusable(true);
            JPanel[] paneArray = plus.toolbarPanes4Form();
            for (int i = 0; i < paneArray.length; i++) {
                toolbarPane.add(paneArray[i]);
            }
            return toolbarPane;
        }
    }


    @Override
    /**
     * 生成报表设计和表单设计的编辑区域
     * @return 返回编辑区域
     */
    public JTemplate<?, ?> createNewTemplate() {
        return new JWorkBook();
    }
    
    /**
	 * 创建论坛登录面板, chart那边不需要
	 * 
	 * @return 面板组件
	 * 
	 */
    public Component createBBSLoginPane(){
        if (userInfoPane == null){
			userInfoPane = new UserInfoPane();
        }
        return userInfoPane;
    }
    
    protected SplashPane createSplashPane() {
        return new ReportSplashPane();
    }
    
    /**
	 * 收集用户信息吗
	 * 
	 */
    protected void collectUserInformation() {
    	//定制的就不弹出来了
    	if (!ComparatorUtils.equals(ProductConstants.APP_NAME, ProductConstants.DEFAULT_APPNAME)) {
			return;
		}
    	
    	DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
    	final String key = envManager.getActivationKey();
    	//本地验证通过
    	if(ActiveKeyGenerator.localVerify(key)){
    		onLineVerify(envManager, key);
			UserInfoLabel.showBBSDialog();
    		return;
    	}
    	
    	if(StableUtils.checkDesignerActive(readOldKey())){
			//只要有老的key, 就不弹窗, 下次启动的时候, 在线验证下就行.
			String newKey = ActiveKeyGenerator.generateActiveKey();
			envManager.setActivationKey(newKey);
			UserInfoLabel.showBBSDialog();
			return;
    	}
    	
        CollectUserInformationDialog activeDialog = new CollectUserInformationDialog(
                    DesignerContext.getDesignerFrame());
        activeDialog.setVisible(true);
    }
    
    private void onLineVerify(DesignerEnvManager envManager, final String key){
		int status = envManager.getActiveKeyStatus();
		//没有联网验证过
		if (status != 0) {
			Thread authThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					ActiveKeyGenerator.onLineVerify(key);
				}
			});
			authThread.start();
		}
    }
    
    private File getOldEnvFile(String folderName){
		String userHome = System.getProperty("user.home");
		if (userHome == null) {
			userHome = System.getProperty("userHome");
		}
		String filePath = StableUtils.pathJoin(userHome, folderName, ProductConstants.APP_NAME + "Env.xml");
        return new File(filePath);
    }
    
    private String getOldActiveKeyFromFile(File envFile){
        if (!envFile.exists()) {
			return StringUtils.EMPTY;
		}
        
        DesignerEnvManager temp = new DesignerEnvManager();
        try {
            XMLTools.readFileXML(temp, envFile);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return temp.getActivationKey();
    }
    
    //默认只从7.0和711的设计器里读取key
    private String readOldKey(){
    	File file71 = getOldEnvFile(OLD_ENV_FOLDER_71);
    	if (!file71.exists()) {
			File file70 = getOldEnvFile(OLD_ENV_FOLDER_70);
			return getOldActiveKeyFromFile(file70);
		}
    	
    	return getOldActiveKeyFromFile(file71);
    }
    
    /**
	 * 设计器退出时, 做的一些操作.
	 * 
	 */
    public void shutDown(){
    	InformationCollector collector = InformationCollector.getInstance();
    	collector.collectStopTime();
    	collector.saveXMLFile();
    }

}