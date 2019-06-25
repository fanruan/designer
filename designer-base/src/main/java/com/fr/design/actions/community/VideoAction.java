package com.fr.design.actions.community;

import com.fr.design.actions.UpdateAction;
import com.fr.design.locale.impl.VideoMark;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.IOUtils;
import com.fr.general.locale.LocaleCenter;
import com.fr.general.locale.LocaleMark;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class VideoAction extends UpdateAction
{

	public VideoAction()
	{
		 this.setMenuKeySet(VIDEO);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
		 this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/bbs/video.png"));
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		LocaleMark<String> localeMark = LocaleCenter.getMark(VideoMark.class);
		BrowseUtils.browser(localeMark.getValue());
	}
	  public static final MenuKeySet VIDEO = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'V';
	        }

	        @Override
	        public String getMenuName() {
	            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_Video");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
