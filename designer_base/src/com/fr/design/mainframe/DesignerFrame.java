/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.NewTemplatePane;
import com.fr.design.file.SaveSomeTemplatePane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.fun.TitlePlaceProcessor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuHighLight;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.hold.DefaultTitlePlace;
import com.fr.design.mainframe.loghandler.LogMessageBar;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuManager;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.file.FILEFactory;
import com.fr.file.FileFILE;
import com.fr.file.FileNodeFILE;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.image4j.codec.ico.ICODecoder;
import com.fr.stable.plugin.PluginReadListener;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class DesignerFrame extends JFrame implements JTemplateActionListener, TargetModifiedListener {
	private static final long serialVersionUID = -8732559571067484460L;
	private static final int LEFT_ALIGN_GAP = -5;
	private static final int MENU_HEIGHT = 26;
	private static final Integer SECOND_LAYER = new Integer(100);
	private static final Integer TOP_LAYER = new Integer((200));

	public static final String DESIGNER_FRAME_NAME = "designer_frame";
	public static final Dimension MIN_SIZE = new Dimension(100, 100);

	private static java.util.List<App<?>> appList = new java.util.ArrayList<App<?>>();

	private ToolBarMenuDock ad;

	private DesktopCardPane centerTemplateCardPane;

	private JPanel toolbarPane;
	private JComponent toolbarComponent;

	private JPanel menuPane;
	private JMenuBar menuBar;
	private JPanel eastCenterPane;
	private UIToolbar combineUp;
	private NewTemplatePane newWorkBookPane = null;
	private Icon closeMode = UIConstants.CLOSE_OF_AUTHORITY;
	private JLayeredPane layeredPane = this.getLayeredPane();
	private JPanel basePane = new JPanel();
	// 上面的虚线
	private DottedLine upDottedLine;
	// 下面的虚线
	private DottedLine downDottedLine;

	// 左边的虚线
	private DottedLine leftDottedLine;

	// 右边的虚线
	private DottedLine rightDottedLine;

	private int contentWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	private int contentHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());

	private WindowAdapter windowAdapter = new WindowAdapter() {
		public void windowOpened(WindowEvent e) {
			HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setComposite();
			reCalculateFrameSize();
			HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().doResize();

		}

		@Override
		public void windowClosing(WindowEvent e) {
			SaveSomeTemplatePane saveSomeTempaltePane = new SaveSomeTemplatePane(true);
			// 只有一个文件未保存时
			if (HistoryTemplateListPane.getInstance().getHistoryCount() == 1) {
				int choose = saveSomeTempaltePane.saveLastOneTemplate();
				if (choose != JOptionPane.CANCEL_OPTION) {
					DesignerFrame.this.exit();
				}
			} else {
				if (saveSomeTempaltePane.showSavePane()) {
					DesignerFrame.this.exit();
				}
			}
		}

	};

	private JComponent closeButton = new JComponent() {
		protected void paintComponent(Graphics g) {
			g.setColor(UIConstants.NORMAL_BACKGROUND);
			g.fillArc(0, 0, UIConstants.CLOSE_AUTHORITY_HEIGHT_AND_WIDTH, UIConstants.CLOSE_AUTHORITY_HEIGHT_AND_WIDTH,
					0, 360);
			closeMode.paintIcon(this, g, 0, 0);
		}
	};

	/**
	 * 注册app.
	 * 
	 * @param app
	 *            注册app.
	 */
	public static void registApp(App<?> app) {
		if (app != null) {
			appList.add(app);
		}
	}

	protected DesktopCardPane getCenterTemplateCardPane() {
		return centerTemplateCardPane;
	}

	/**
	 * Constructor.
	 */
	public DesignerFrame(ToolBarMenuDock ad) {
		setName(DESIGNER_FRAME_NAME);
		this.ad = ad;
		this.initTitleIcon();
		this.setTitle();// james:因为有默认的了

		// set this to context.
		DesignerContext.setDesignerFrame(this);

		// the content pane
		basePane.setLayout(new BorderLayout());

		menuPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		menuPane.add(new UIMenuHighLight(), BorderLayout.SOUTH);
		menuPane.add(initNorthEastPane(ad), BorderLayout.EAST);

		toolbarPane = new JPanel() {
			public Dimension getPreferredSize() {
				Dimension dim = super.getPreferredSize();
				// dim.height = TOOLBAR_HEIGHT;
				return dim;
			}
		};
		toolbarPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel eastPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		eastPane.add(ad.createLargeToolbar(), BorderLayout.WEST);
		eastCenterPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		eastCenterPane.add(combineUp = combineUpTooBar(null), BorderLayout.NORTH);
		JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		panel.add(newWorkBookPane =ad.getNewTemplatePane(), BorderLayout.WEST);
		panel.add(MutilTempalteTabPane.getInstance(), BorderLayout.CENTER);
		eastCenterPane.add(panel, BorderLayout.CENTER);

		eastPane.add(eastCenterPane, BorderLayout.CENTER);
		toolbarPane.add(eastPane, BorderLayout.NORTH);
		toolbarPane.add(new UIMenuHighLight(), BorderLayout.SOUTH);

		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.add(centerTemplateCardPane = new DesktopCardPane(), BorderLayout.CENTER);
		centerPane.add(toolbarPane, BorderLayout.NORTH);

		basePane.add(menuPane, BorderLayout.NORTH);
		basePane.add(centerPane, BorderLayout.CENTER);
        laoyoutWestPane();
		basePane.add(EastRegionContainerPane.getInstance(), BorderLayout.EAST);
		basePane.setBounds(0, 0, contentWidth, contentHeight);

		// 数值越小。越在底层
		layeredPane.add(basePane);
		// 调整Window大小
		modWindowBounds();

		this.resetToolkitByPlus(null);

		// p:检查所有按钮的可见性和是否可以编辑性.
		checkToolbarMenuEnable();

		// window close listener.
		this.addWindowListeners(getFrameListeners());

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				reCalculateFrameSize();
				if (BaseUtils.isAuthorityEditing()) {
					doResize();
				}
			}
		});
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setVisible(false);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_MOVE, new FileDropTargetListener(), true));
		closeMode = UIConstants.CLOSE_OF_AUTHORITY;
	}

	public void initTitleIcon() {
		try {
			@SuppressWarnings("unchecked")
			List<BufferedImage> image = ICODecoder.read(DesignerFrame.class
					.getResourceAsStream("/com/fr/base/images/oem/logo.ico"));
			this.setIconImages(image);
		} catch (IOException e) {
			FRContext.getLogger().error(e.getMessage(), e);
			this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
		}
	}
	
	private JPanel initNorthEastPane(final ToolBarMenuDock ad){
		//顶部日志+登陆按钮
		final JPanel northEastPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        GeneralContext.addPluginReadListener(new PluginReadListener() {
            @Override
            public void success() {
                TitlePlaceProcessor processor = ExtraDesignClassManager.getInstance().getTitlePlaceProcessor();
                if (processor == null) {
                    processor = new DefaultTitlePlace();
                }
                processor.hold(northEastPane, LogMessageBar.getInstance(), ad.createBBSLoginPane());
            }
        });
		return northEastPane;
	}

	private void addWindowListeners(ArrayList<WindowListener> listeners){
		for(WindowListener listener : listeners){
			this.addWindowListener(listener);
		}
	}

	protected ArrayList<WindowListener> getFrameListeners(){
		ArrayList<WindowListener> arrayList = new ArrayList<WindowListener>();
		arrayList.add(windowAdapter);
		return arrayList;
	}


	protected void laoyoutWestPane(){
		basePane.add(WestRegionContainerPane.getInstance(), BorderLayout.WEST);
	}

	// 调整windows大小
	private void reCalculateFrameSize() {
		contentHeight = layeredPane.getHeight();
		contentWidth = layeredPane.getWidth();
		layeredPane.remove(basePane);
		basePane.setBounds(0, 0, contentWidth, contentHeight);
		layeredPane.add(basePane);
		layeredPane.repaint();
	}

	/**
	 * 更新
	 */
	public void populateAuthorityArea() {
		int centerWidth = contentWidth - WestRegionContainerPane.getInstance().getContainerWidth()
				- EastRegionContainerPane.getInstance().getContainerWidth();
		// 上面的虚线
		upDottedLine = new DottedLine(UIScrollBar.HORIZONTAL, centerWidth);
		upDottedLine.setBounds(WestRegionContainerPane.getInstance().getContainerWidth(), MENU_HEIGHT - 1, centerWidth,
				3);

		// 下面的虚线
		downDottedLine = new DottedLine(UIScrollBar.HORIZONTAL, centerWidth);
		downDottedLine.setBounds(WestRegionContainerPane.getInstance().getContainerWidth(), contentHeight - 3,
				centerWidth, 3);

		// 左边的虚线
		leftDottedLine = new DottedLine(UIScrollBar.VERTICAL, contentHeight - MENU_HEIGHT);
		leftDottedLine.setBounds(WestRegionContainerPane.getInstance().getContainerWidth(), MENU_HEIGHT, 3,
				contentHeight - MENU_HEIGHT);

		rightDottedLine = new DottedLine(UIScrollBar.VERTICAL, contentHeight - MENU_HEIGHT);
		rightDottedLine.setBounds(contentWidth - EastRegionContainerPane.getInstance().getContainerWidth() - 3,
				MENU_HEIGHT, 3, contentHeight - MENU_HEIGHT);

	}

	private void addDottedLine() {
		layeredPane.add(upDottedLine, SECOND_LAYER);
		layeredPane.add(downDottedLine, SECOND_LAYER);
		layeredPane.add(leftDottedLine, SECOND_LAYER);
		layeredPane.add(rightDottedLine, SECOND_LAYER);
		layeredPane.add(closeButton, TOP_LAYER);
	}

	private void removeDottedLine() {
		layeredPane.remove(upDottedLine);
		layeredPane.remove(downDottedLine);
		layeredPane.remove(leftDottedLine);
		layeredPane.remove(rightDottedLine);
		layeredPane.remove(closeButton);
	}

	public JLayeredPane getContentFrame() {
		return layeredPane;
	}

	/**
	 * 刷新
	 */
	public void refreshDottedLine() {
		if (BaseUtils.isAuthorityEditing()) {
			populateAuthorityArea();
			populateCloseButton();
			addDottedLine();
		} else {
			removeDottedLine();
		}
		layeredPane.repaint();
	}

	/**
	 * 刷新DottedLine
	 */
	public void doResize() {
		removeDottedLine();
		populateAuthorityArea();
		populateCloseButton();
		addDottedLine();
	}
	
	private MouseListener closeMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			closeMode = UIConstants.CLOSE_PRESS_AUTHORITY;
			closeButton.setBackground(UIConstants.NORMAL_BACKGROUND);
			closeButton.repaint();
		}

		public void mouseExited(MouseEvent e) {
			closeMode = UIConstants.CLOSE_OF_AUTHORITY;
			closeButton.setBackground(UIConstants.NORMAL_BACKGROUND);
			closeButton.repaint();
		}

		public void mouseMoved(MouseEvent e) {
			closeMode = UIConstants.CLOSE_OVER_AUTHORITY;
			closeButton.setBackground(UIConstants.NORMAL_BACKGROUND);
			closeButton.repaint();
		}

		public void mouseReleased(MouseEvent e) {
			if (BaseUtils.isAuthorityEditing()) {
				BaseUtils.setAuthorityEditing(false);
				WestRegionContainerPane.getInstance().replaceDownPane(
						TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()));
				EastRegionContainerPane.getInstance().replaceUpPane(
						HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEastUpPane());
				EastRegionContainerPane.getInstance().replaceDownPane(
						HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEastDownPane());
				DesignerContext.getDesignerFrame().resetToolkitByPlus(
						HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getToolBarMenuDockPlus());
				needToAddAuhtorityPaint();
				refreshDottedLine();
				fireAuthorityStateToNomal();
			}
		}

		public void mouseEntered(MouseEvent e) {
			closeMode = UIConstants.CLOSE_OVER_AUTHORITY;
			closeButton.setBackground(UIConstants.NORMAL_BACKGROUND);
			closeButton.repaint();
		}
	};

	/**
	 * 刷新CloseButton
	 */
	public void populateCloseButton() {

		closeButton.addMouseListener(closeMouseListener);
		closeButton.setBackground(UIConstants.NORMAL_BACKGROUND);
		closeButton.setBorder(null);
		int x = contentWidth - EastRegionContainerPane.getInstance().getContainerWidth() - closeMode.getIconWidth() / 2;
		int y = MENU_HEIGHT - closeMode.getIconHeight() / 2;
		closeButton.setBounds(x, y, UIConstants.CLOSE_AUTHORITY_HEIGHT_AND_WIDTH,
				UIConstants.CLOSE_AUTHORITY_HEIGHT_AND_WIDTH);
	}

	/**
	 * 退出权限编辑时，将所有的做过权限编辑的状态，作为一个状态赋给报、报表主体
	 */
	private void fireAuthorityStateToNomal() {
		java.util.List<JTemplate<?, ?>> opendedTemplate = HistoryTemplateListPane.getInstance().getHistoryList();
		for (int i = 0; i < opendedTemplate.size(); i++) {
			// 如果在权限编辑时做过操作，则将做过的操作作为一个整体状态赋给正在报表
			if (opendedTemplate.get(i).isDoSomethingInAuthority()) {
				opendedTemplate.get(i).fireAuthorityStateToNomal();
			}
		}
	}

	public void setCloseMode(Icon closeMode) {
		this.closeMode = closeMode;
	}

	private UIToolbar combineUpTooBar(JComponent[] toolbar4Form) {
		combineUp = new UIToolbar(FlowLayout.LEFT);
		combineUp.setBorder(new MatteBorder(new Insets(0, LEFT_ALIGN_GAP, 1, 0), UIConstants.LINE_COLOR));
		combineUp.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		UIButton[] fixButtons = ad.createUp();
		for (int i = 0; i < fixButtons.length; i++) {
			combineUp.add(fixButtons[i]);
		}
		if (!BaseUtils.isAuthorityEditing()) {
			combineUp.addSeparator(new Dimension(2, 16));
			if (toolbar4Form != null) {
				for (int i = 0; i < toolbar4Form.length; i++) {
					combineUp.add(toolbar4Form[i]);
				}
			}
		}
		
		//添加分享按钮
		addShareButton();
		
		return combineUp;
	}
	
	private void addShareButton(){
		JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		if(jt == null){
			return;
		}
		
		combineUp.addSeparator(new Dimension(2, 16));
		UIButton[] shareButtons = jt.createShareButton();
		for (int i = 0; i < shareButtons.length; i++) {
			combineUp.add(shareButtons[i]);
		}
	}

	/**
	 * 检查
	 * 
	 * @param flag
	 *            组件是否可见
	 * @param al
	 *            组件名称
	 */
	public void checkCombineUp(boolean flag, ArrayList<String> al) {
		combineUp.checkComponentsByNames(flag, al);

	}

	/**
	 * 刷新工具条.
	 */
	public void refreshToolbar() {
		this.ad.updateToolBarDef();
	}

	/**
	 * 重置相关的工具条.
	 * 
	 * @param plus
	 *            工具条中相关信息
	 */
	public void resetToolkitByPlus(ToolBarMenuDockPlus plus) {
		if (plus == null) {
			plus = ToolBarMenuDock.NULLAVOID;
		}

		DesignState designState = new DesignState(plus);
		MenuManager.getInstance().setMenus4Designer(designState);
		if (menuBar != null) {
			menuPane.remove(menuBar);
		}
		menuPane.add(menuBar = ad.createJMenuBar(plus), BorderLayout.CENTER);

		if (combineUp != null) {
			eastCenterPane.remove(combineUp);
			combineUp = null;
		}

		// 保存撤销那些按钮的面板
		eastCenterPane.add(combineUp = combineUpTooBar(ad.resetUpToolBar(plus)), BorderLayout.NORTH);

		if (toolbarComponent != null) {
			toolbarPane.remove(toolbarComponent);
		}

		// 颜色，字体那些按钮的工具栏
		toolbarPane.add(toolbarComponent = ad.resetToolBar(toolbarComponent, plus), BorderLayout.CENTER);

		this.checkToolbarMenuEnable();
	    this.validate();
		layeredPane.repaint();
	}

	public JComponent getToolbarComponent() {
		return this.toolbarComponent;
	}

	/**
	 * 判断是否在权限编辑状态，若是在权限编辑状态，则需要有虚线框和关闭突变
	 */
	public void needToAddAuhtorityPaint() {

		newWorkBookPane.setButtonGray(BaseUtils.isAuthorityEditing());
	}

	/**
	 * 检查工具条.
	 */
	private void checkToolbarMenuEnable() {
		if (this.ad != null) {
			this.ad.updateMenuDef();
			this.ad.updateToolBarDef();
		}
	}

	/**
	 * 设置标题
	 */
	public void setTitle() {
        JTemplate<?, ?> editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        StringBuilder defaultTitleSB = new StringBuilder();
        defaultTitleSB.append(ProductConstants.PRODUCT_NAME);
		defaultTitleSB.append(" ");
		defaultTitleSB.append(ProductConstants.BRANCH);
		// james：标识登录的用户和登录的ENV
		String envName = DesignerEnvManager.getEnvManager().getCurEnvName();
		Env env = DesignerEnvManager.getEnvManager().getEnv(envName);
		if (env != null) {
			defaultTitleSB.append(env.getUser()).append('@').append(envName).append('[');
			defaultTitleSB.append(env.getEnvDescription());
			defaultTitleSB.append(']');
			if (editingTemplate != null) {
				String path = editingTemplate.getEditingFILE().getPath();
				if (!editingTemplate.getEditingFILE().exists()) {
					path = FILEFactory.MEM_PREFIX + path;
				} else if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
					path = env.getPath() + File.separator + path;
				}
				defaultTitleSB.append("    " + path);
			}

		}
		setTitle(defaultTitleSB.toString());
	}

	/**
	 * modify window bounds.
	 */
	private void modWindowBounds() {
		// set the window bounds to the same as last closed
		DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
		Rectangle windowBounds = designerEnvManager.getWindowBounds();
		if (windowBounds != null) {
			int locX = windowBounds.x;
			int locY = windowBounds.y;

			if (!OperatingSystem.isWindows()) {
				locX = Math.max(1, locX);
				locY = Math.max(1, locY);
			}

			this.setLocation(new Point(locX, locY));

			int width = windowBounds.width;
			int height = windowBounds.height;
			if (width > MIN_SIZE.width && height > MIN_SIZE.height) {
				this.setSize(width, height);
			} else {
				GUICoreUtils.setWindowFullScreen(this);
			}
		} else {
			GUICoreUtils.setWindowFullScreen(this);
		}
	}

	/**
	 * 报表运行环境改变时,需要刷新某些面板
	 * 
	 * @param env
	 *            环境
	 */
	public void refreshEnv(Env env) {
		this.setTitle();
		DesignerFrameFileDealerPane.getInstance().refreshDockingView();
		TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
		TemplateTreePane.getInstance().refreshDockingView();
		DesignTableDataManager.clearGlobalDs();
		EastRegionContainerPane.getInstance().refreshDownPane();
	}

	/**
	 * 返回选中的模板.
	 */
	public JTemplate<?, ?> getSelectedJTemplate() {
		return this.centerTemplateCardPane.getSelectedJTemplate();
	}

	/**
	 * 保存当前编辑的模板
	 */

	public void saveCurrentEditingTemplate() {
		JTemplate<?, ?> editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		if (editingTemplate == null) {
			return;
		}
		if (editingTemplate.isSaved()) {// isSaved == true表示已经保存过，或者新建的一张模板
			if (editingTemplate.getEditingFILE().exists()) {// 表示磁盘上的某一张已经保存过的模板，要添加到历史中
				// HistoryTemplateListPane.getInstance().addHistory();
			}
		} else {
			editingTemplate.stopEditing();
			if (!editingTemplate.getEditingFILE().exists()) {
				int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(),
						Inter.getLocText("Utils-Would_you_like_to_save") + " \"" + editingTemplate.getEditingFILE()
								+ "\" ?", ProductConstants.PRODUCT_NAME, JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (returnVal == JOptionPane.YES_OPTION && editingTemplate.saveTemplate()) {
					editingTemplate.saveTemplate();
					FRLogger.getLogger().log(
							Level.INFO,
							Inter.getLocText(new String[] { "Template", "already-saved" }, new String[] {
									editingTemplate.getEditingFILE().getName(), "." }));
				}
			} else {
				if (editingTemplate.saveTemplate()) {
					editingTemplate.saveTemplate();
					FRLogger.getLogger().log(
							Level.INFO,
							Inter.getLocText(new String[] { "Template", "already-saved" }, new String[] {
									editingTemplate.getEditingFILE().getName(), "." }));
				}
			}
		}
	}

	/**
	 * 添加新建模板, 并激活.
	 */
	public void addAndActivateJTemplate() {
		addAndActivateJTemplate(ad.createNewTemplate());
		layeredPane.repaint();
	}

	/**
	 * 添加 模板, 并激活.
	 * 
	 * @param jt
	 *            添加的模板.
	 */
	public void addAndActivateJTemplate(JTemplate<?, ?> jt) {
		if (jt == null || jt.getEditingFILE() == null) {
			return;
		}
		jt.addJTemplateActionListener(this);
		jt.addTargetModifiedListener(this);
		centerTemplateCardPane.showJTemplate(jt);
		setTitle();
		layeredPane.repaint();
	}

	/**
	 * 激活已经存在的模板
	 * 
	 * @param jt
	 *            模板
	 */
	public void activateJTemplate(JTemplate<?, ?> jt) {
		if (jt == null || jt.getEditingFILE() == null) {
			return;
		}
		centerTemplateCardPane.showJTemplate(jt);
		setTitle();
		layeredPane.repaint();
	}

	/**
	 * 对象侦听
	 * 
	 * @param e
	 *            事件
	 */
	public void targetModified(TargetModifiedEvent e) {
		this.checkToolbarMenuEnable();
	}

	/**
	 * 模板关闭时 处理.
	 * 
	 * @param jt
	 *            模板
	 */
	public void templateClosed(JTemplate<?, ?> jt) {
	}

	/**
	 * 模板打开时 处理.
	 * 
	 * @param jt
	 *            模板
	 */
	public void templateOpened(JTemplate<?, ?> jt) {
	}

	/**
	 * 模板保存时 处理.
	 * 
	 * @param jt
	 *            模板
	 */
	public void templateSaved(JTemplate<?, ?> jt) {
		this.checkToolbarMenuEnable();
	}

	/**
	 * 打开模板文件,如果是已经打开的就激活此模板所对应的JInternalFrame
	 * 
	 * @param tplFile
	 *            文件
	 */
	public void openTemplate(FILE tplFile) {
		// 测试连接，如果连接失败，则提示
		try {
			if (FRContext.getCurrentEnv() != null
					&& !FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
				JOptionPane.showMessageDialog(
						DesignerContext.getDesignerFrame(),
						Inter.getLocText(new String[] { "FR-Chart-Server_disconnected", "FR-Server-Design_template_unopened" }, new String[] {
								",", "!" }), Inter.getLocText("FR-Server-All_Error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception e) {
			FRLogger.getLogger().error(e.getMessage());
		}

		// p:判断一下，如何文件为空或者文件不存在，直接返回.
		if (tplFile == null || !tplFile.exists()) {
			JOptionPane.showMessageDialog(this, Inter.getLocText("Warning-Template_Do_Not_Exsit"),
					ProductConstants.PRODUCT_NAME, JOptionPane.INFORMATION_MESSAGE);
			DesignerFrameFileDealerPane.getInstance().refresh();
			return;
		}

        try {
            openFile(tplFile);
        } catch (Throwable t) {
            FRLogger.getLogger().error(t.getMessage(), t);
            addAndActivateJTemplate();
        }

	}

	/**
	 * 是否不合版本的设计器
	 * 
	 * @param jt
	 *            当前模板
	 * 
	 * @return 是否不合版本
	 * 
	 *
	 * @date 2014-10-14-下午6:30:37
	 */
	private boolean inValidDesigner(JTemplate jt) {
		return jt.isOldDesigner() || (!jt.isJWorkBook() && jt.isNewDesigner());
	}

	/**
	 * 打开指定的文件
	 * 
	 * @param tplFile
	 *            指定的文件
	 * 
	 *
	 * @date 2014-10-14-下午6:31:05
	 */
	private void openFile(FILE tplFile) {
		String fileName = tplFile.getName();
		int indexOfLastDot = fileName.lastIndexOf(CoreConstants.DOT);
		if (indexOfLastDot < 0) {
			return;
		}
		String fileExtention = fileName.substring(indexOfLastDot + 1);
		for (int i = 0, len = appList.size(); i < len; i++) {
			App<?> app = appList.get(i);
			String[] defaultAppExtentions = app.defaultExtentions();
			boolean opened = false;
			for (int j = 0; j < defaultAppExtentions.length; j++) {
				if (defaultAppExtentions[j].equalsIgnoreCase(fileExtention)) {
					JTemplate jt = null;
					try {
						jt = app.openTemplate(tplFile);
					} catch (Exception e) {
						FRLogger.getLogger().error(e.getMessage(), e);
					}
					if (jt == null) {
						return;
					}
					// 新的form不往前兼容
					if (inValidDesigner(jt)) {
						this.addAndActivateJTemplate();
						MutilTempalteTabPane.getInstance().setTemTemplate(
								HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
					} else {
						activeTemplate(tplFile, jt);
					}
					opened = true;
					break;
				}
			}
			if (opened) {
				break;
			}
		}
	}

	/**
	 * 激活指定的模板
	 * 
	 * @param tplFile
	 *            模板文件
	 * @param jt
	 *            当前报表
	 * 
	 *
	 * @date 2014-10-14-下午6:31:23
	 */
	private void activeTemplate(FILE tplFile, JTemplate jt) {
		// 如果该模板已经打开，则进行激活就可以了
		String fullName = StableUtils.pathJoin(new String[] { ProjectConstants.REPORTLETS_NAME, tplFile.getName() });
		if (tplFile instanceof FileNodeFILE) {
			fullName = ((FileNodeFILE) tplFile).getEnvPath() + "/" + tplFile.getPath();
		}
		// 如果是从文件夹打开的文件，不是从设计器文件树打开的文件，则直接取path就行
		if (tplFile instanceof FileFILE) {
			fullName = tplFile.getPath();
		}
		fullName = fullName.replaceAll("/", "\\\\");
		int index = HistoryTemplateListPane.getInstance().contains(fullName);
		if (index != -1) {
			this.activateJTemplate(HistoryTemplateListPane.getInstance().getHistoryList().get(index));
		} else {
			this.addAndActivateJTemplate(jt);
		}

	}

	/**
	 * Exit退出
	 */
	public void exit() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				DesignerEnvManager.doEndMapSaveWorkersIndesign();
			}
		};
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			FRLogger.getLogger().error("Map Thread Error");
		}

		DesignerEnvManager.getEnvManager().setLastOpenFile(
				HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getEditingFILE().getPath());

		DesignerEnvManager.getEnvManager().setLastWestRegionToolPaneY(
				WestRegionContainerPane.getInstance().getToolPaneY());
		DesignerEnvManager.getEnvManager().setLastWestRegionContainerWidth(
				WestRegionContainerPane.getInstance().getContainerWidth());
		DesignerEnvManager.getEnvManager().setLastEastRegionToolPaneY(
				EastRegionContainerPane.getInstance().getToolPaneY());
		DesignerEnvManager.getEnvManager().setLastEastRegionContainerWidth(
				EastRegionContainerPane.getInstance().getContainerWidth());

		DesignerEnvManager.getEnvManager().saveXMLFile();

		Env currentEnv = FRContext.getCurrentEnv();
		try {
			currentEnv.signOut();
			GeneralContext.fireEnvSignOutListener();
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
		this.setVisible(false);
		this.dispose();
		
		this.ad.shutDown();

		System.exit(0);
	}

	// harry：添加程序外拖拽文件进来打开的功能
	class FileDropTargetListener implements DropTargetListener {

		@Override
		public void dragEnter(DropTargetDragEvent event) {
		}

		@Override
		public void dragExit(DropTargetEvent event) {
		}

		@Override
		public void dragOver(DropTargetDragEvent event) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent event) {
			if (!isDragAcceptable(event)) {
				event.rejectDrag();
				return;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void drop(DropTargetDropEvent event) {
			if (!isDropAcceptable(event)) {
				event.rejectDrop();
				return;
			}

			event.acceptDrop(DnDConstants.ACTION_MOVE);
			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				DataFlavor d = flavors[i];
				try {
					if (ComparatorUtils.equals(d, DataFlavor.javaFileListFlavor)) {
						List<File> fileList = (List<File>) transferable.getTransferData(d);
						Iterator<File> iterator = fileList.iterator();
						while (iterator.hasNext()) {
							File f = iterator.next();
							DesignerContext.getDesignerFrame().openTemplate(new FileFILE(f));
						}
					}
				} catch (Exception e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
			}
			event.dropComplete(true);
		}

		public boolean isDragAcceptable(DropTargetDragEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}

		public boolean isDropAcceptable(DropTargetDropEvent event) {
			return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
		}
	}

}