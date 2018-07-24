package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;

import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class VideoAction extends UpdateAction
{

	public VideoAction()
	{
		 this.setMenuKeySet(VIDEO);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/video.png"));
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String url;
		if (FRContext.getLocale().equals(Locale.US)) {
			url = CloudCenter.getInstance().acquireUrlByKind("bbs.video.en");
		} else {
	  		url = CloudCenter.getInstance().acquireUrlByKind("bbs.video");
		}
		if (StringUtils.isEmpty(url)) {
			FRContext.getLogger().info("The URL is empty!");
			return;
		}
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException exp) {
			JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Set_default_browser"));
			FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
		} catch (URISyntaxException exp) {
			FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
		} catch (Exception exp) {
			FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
			FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
		}

	}
	  public static final MenuKeySet VIDEO = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'V';
	        }

	        @Override
	        public String getMenuName() {
	            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_COMMUNITY_VIDEO");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
