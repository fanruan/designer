package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BBSAction extends UpdateAction
{
	

	public BBSAction()
	{
		 this.setMenuKeySet(BBS);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs.png"));
		
	}

	 /**
     * 动作
     * @param e 事件
     */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
	        String url = CloudCenter.getInstance().acquireUrlByKind("bbs");
	        if (StringUtils.isEmpty(url)) {
	            FRContext.getLogger().info("The URL is empty!");
	            return;
	        }
	        try {
	            Desktop.getDesktop().browse(new URI(url));
	        } catch (IOException exp) {
	            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
	            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
	        } catch (URISyntaxException exp) {
	            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
	        } catch (Exception exp) {
	            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
	            FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
	        }
	}
	  public static final MenuKeySet BBS = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'B';
	        }

	        @Override
	        public String getMenuName() {
	            return Inter.getLocText("FR-Designer_COMMUNITY_BBS");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
