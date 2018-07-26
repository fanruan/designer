/**
 * 
 */
package com.fr.design.report.share;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.CloudCenter;

import com.fr.general.NameObject;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.stable.ActorConstants;
import com.fr.stable.ActorFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;

/**
 * @author neil
 *
 * @date: 2015-3-9-下午3:14:56
 */
public class ShareButton extends UIButton{

	private static final int SHARE_COUNTS = 4;
	private static final String SHARE_KEY = "share";
	
	/**
	 * 构造函数
	 */
	public ShareButton() {
    	this.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/share.png"));
    	this.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Share-Template"));
    	this.set4ToolbarButton();
    	this.addActionListener(shareListener);
	}
	
	//打开论坛, url可在bbs.properties中配置
	private void openBBS(){
		try {
			Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("bbs.share")));
		} catch (Exception e1) {
			FRContext.getLogger().error(e1.getMessage());
		}
	}
	
	//导出图片到指定文件夹
	private String exportAsImage(){
		//要导出内置数据集后才计算报表, 导出图片, 因为他导出内置后需要手动混淆数据
		JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate(); 

		TemplateWorkBook workbook = (TemplateWorkBook) jt.getTarget();
		//导出的时候已经弹窗让他输过一次密码了, 再弹不好, 反正数据源那边的参数已经有了, 就不弹出来取参数了
		ResultWorkBook res = workbook.execute(new HashMap<String, Object>(), ActorFactory.getActor(ActorConstants.TYPE_PAGE));
		ImageExporter exporter = new ImageExporter();
		File imageFile = new File(getImagePath(jt));
		try {
			exporter.export(new FileOutputStream(imageFile), res);
		} catch (Exception e2) {
			FRContext.getLogger().error(e2.getMessage());
		}
		
		return imageFile.getParent();
	}
	
	//获取默认导出图片位置
	private String getImagePath(JTemplate<?, ?> jt){
		FILE file = jt.getEditingFILE();
		String envPath = WorkContext.getCurrent().getPath();
		String folderPath = file.getParent().getPath();
		String imageName = file.getName().replaceAll(ProjectConstants.CPT_SUFFIX, StringUtils.EMPTY) + ".png";
		
		return StableUtils.pathJoin(envPath, folderPath, imageName);
	}
	
	private ActionListener shareListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			UIPopupMenu menu = new UIPopupMenu();
			boolean isSharable = isSharable();
			
			//点击后当前编辑模板会跳转到内置数据集的share模板上进行混淆数据.
			UIMenuItem directShare = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Share-Template"));
			directShare.setEnabled(!isSharable);
			directShare.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/directShare.png"));
            if (directShare.isEnabled()) {
                directShare.addMouseListener(directShareListener);
            }
			
			//默认必须是先点击分享模板, 然后跳转到导出内置数据集的模板上, 才可以点完成修改并分享
			UIMenuItem modifyShare = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Finish-Modify-Share"));
			modifyShare.setEnabled(isSharable);
			modifyShare.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/modifyShare.png"));
			if (modifyShare.isEnabled()) {
                modifyShare.addMouseListener(modifyShareListener);
            }
			menu.add(directShare);
			menu.add(modifyShare);
			
			GUICoreUtils.showPopupMenu(menu, ShareButton.this, 0, 20);
		}
		
		private boolean isSharable(){
			JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			FILE file = jt.getEditingFILE();
			String fileName = file.getEnvFullName();
			
			//eg, share/WorkBook2011_share/WorkBook2011_share.cpt
			return ArrayUtils.getLength(fileName.split(SHARE_KEY)) >= SHARE_COUNTS;
		}
	};
	
	//完成修改后分享监听
	private MouseListener modifyShareListener = new MouseAdapter() {
		
		public void mousePressed(MouseEvent e) {
			//导出缩略图
			exportAsImage();
			//打开论坛
			openBBS();
		};
		
	};
	
	//点击分享监听
	private MouseListener directShareListener = new MouseAdapter() {
		
		public void mousePressed(MouseEvent e) {
			JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			jt.stopEditing();
			jt.saveShareFile();
			
	        final ConfusionManagerPane managerPane = new ConfusionManagerPane();
	        boolean hasEmb = managerPane.populateTabledataManager();
	        if(!hasEmb){
	        	//如果没有内置数据集, 就不展示混淆的空面板了.
	        	return;
	        }
	        
	        BasicDialog managerDialog = managerPane.showMediumWindow(DesignerContext.getDesignerFrame(), 
	        		new DialogActionAdapter() {
	        	
	        		public void doOk() {
	        			//点确定时, 开始将混淆的数据设置进workbook
	        			updateManagerDialog(managerPane);
	        		};
	        });
	        managerDialog.setModal(false);
	        managerDialog.setVisible(true);
		};
		
	};
	
	/**
	 * 将面板上设置的混淆应用到workbook的数据集中
	 * 
	 * @param managerPane 当前管理混淆的面板
	 * 
	 */
    public void updateManagerDialog(ConfusionManagerPane managerPane) {
    	Nameable[] confusionArray = managerPane.update();
    	for (int i = 0, length = confusionArray.length; i < length; i++) {
    		//混淆的tabledata名称
			String name = confusionArray[i].getName();
			//混淆的相关信息
			ConfusionInfo info = (ConfusionInfo) ((NameObject)confusionArray[i]).getObject();
			JTemplate<?, ?> template = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			TableDataSource workbook = template.getTarget();
			EmbeddedTableData tabledata = (EmbeddedTableData) workbook.getTableData(name);
			//混淆数据集里的数据
			new ConfuseTabledataAction().confuse(info, tabledata);
    	}
    }
    
}