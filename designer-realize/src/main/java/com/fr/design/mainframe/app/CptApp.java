package com.fr.design.mainframe.app;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.base.TempNameStyle;
import com.fr.base.extension.FileExtension;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DecodeDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.ComparatorUtils;

import com.fr.io.utils.ResourceIOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class CptApp extends AbstractWorkBookApp {
    
    @Override
    public String[] defaultExtensions() {
        
        return new String[]{FileExtension.CPT.getExtension()};
    }
    
    @Override
    public WorkBook asIOFile(FILE file) {
        
        if (XMLEncryptUtils.isCptEncoded() &&
            !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey())) {
            if (!new DecodeDialog(file).isPwdRight()) {
                FRContext.getLogger().error(com.fr.design.i18n.Toolkit.i18nText("ECP-error_pwd"));
                return new WorkBook();
            }
        }
        
        WorkBook tpl = new WorkBook();
        // richer:打开报表通知
        FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Template_Opening_And_Waiting", file.getName()) + "...");
        TempNameStyle namestyle = TempNameStyle.getInstance();
        namestyle.clear();
        String checkStr = StringUtils.EMPTY;
        try {
            checkStr = ResourceIOUtils.inputStream2String(file.asInputStream());
            tpl.readStream(file.asInputStream());
        } catch (Exception exp) {
            String errorMessage = ComparatorUtils.equals(RemoteDeziConstants.INVALID_USER, checkStr) ? Toolkit.i18nText("Fine-Design_Template_Permission_Denied")
                : com.fr.design.i18n.Toolkit.i18nText("NS-exception_readError");
            FineLoggerFactory.getLogger().error(errorMessage + file, exp);
        }
        checkNameStyle(namestyle);
        return tpl;
    }
    
    private static void checkNameStyle(TempNameStyle namestyle) {
        
        Iterator it = namestyle.getIterator();
        ArrayList<String> al = new ArrayList<String>();
        while (it.hasNext()) {
            al.add((String) it.next());
        }
        if (!al.isEmpty()) {
            showConfirmDialog(al);
        }
    }
    
    private static void showConfirmDialog(final ArrayList<String> namelist) {
        
        final JDialog jd = new JDialog();
        // 模态一下，因为可能会多个样式丢失
        // jd.setModal(true);
        jd.setAlwaysOnTop(true);
        jd.setSize(450, 150);
        jd.setResizable(false);
        jd.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
        String message = namelist.toString().replaceAll("\\[", "").replaceAll("\\]", "");
        UILabel jl = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Template_Global_Style_Missed", message));
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jd.add(jl, BorderLayout.CENTER);
        JPanel jp = new JPanel();
        
        // ”是“按钮，点击之后将生成一个全局样式，并写入xml
        UIButton confirmButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Yes"));
        confirmButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    for (String name : namelist) {
                        ServerPreferenceConfig.getInstance().putStyle(name, Style.DEFAULT_STYLE);
                    }
                } catch (Exception ex) {
                    FineLoggerFactory.getLogger().error(ex.getMessage());
                }
                jd.dispose();
                new StyleListAction().actionPerformed(e);// 弹窗
            }
        });
        
        UIButton noButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_No"));
        noButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                jd.dispose();
            }
        });
        
        jp.add(confirmButton);
        jp.add(noButton);
        jd.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Template_Custom_Style_Missed"));
        jd.add(jp, BorderLayout.SOUTH);
        GUICoreUtils.centerWindow(jd);
        jd.setVisible(true);
    }
    
}
