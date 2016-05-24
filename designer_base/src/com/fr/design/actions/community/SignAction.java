package com.fr.design.actions.community;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.stable.StringUtils;

public class SignAction extends UpdateAction
{

	public SignAction()
	{
		 this.setMenuKeySet(SIGN);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/signature.png"));
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		 String url = SiteCenter.getInstance().acquireUrlByKind("bbs.aut");
	        if (StringUtils.isEmpty(url)) {
	            FRContext.getLogger().info("The URL is empty!");
	            return;
	        }
	        try {
	            Desktop.getDesktop().browse(new URI(url));
	        } catch (IOException exp) {
	            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
	            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
	        } catch (URISyntaxException exp) {
	            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
	        } catch (Exception exp) {
	            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
	            FRContext.getLogger().error("Can not open the browser for URL:  " + url);
	        }

	}
	  public static final MenuKeySet SIGN = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'S';
	        }

	        @Override
	        public String getMenuName() {
	            return Inter.getLocText("FR-Designer_COMMUNITY_SIGN");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
