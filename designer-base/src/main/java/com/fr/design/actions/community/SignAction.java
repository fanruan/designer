package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;

import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;

import javax.swing.*;

import java.awt.event.ActionEvent;

public class SignAction extends UpdateAction
{

	public SignAction()
	{
		 this.setMenuKeySet(SIGN);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs/sign.png"));
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		 String url = CloudCenter.getInstance().acquireUrlByKind("bbs.aut");
		 BrowseUtils.browser(url);

	}
	  public static final MenuKeySet SIGN = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'S';
	        }

	        @Override
	        public String getMenuName() {
	            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_sign");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
