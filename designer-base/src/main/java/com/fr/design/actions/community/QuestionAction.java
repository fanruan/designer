package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class QuestionAction extends UpdateAction
{

	public QuestionAction()
	{ this.setMenuKeySet(QUESTIONS);
    this.setName(getMenuKeySet().getMenuName());
    this.setMnemonic(getMenuKeySet().getMnemonic());
    this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs/question.png"));
	
}

@Override
public void actionPerformed(ActionEvent arg0)
{
	 String url = CloudCenter.getInstance().acquireUrlByKind("bbs.questions");
     BrowseUtils.browser(url);
}
 public static final MenuKeySet QUESTIONS = new MenuKeySet() {
       @Override
       public char getMnemonic() {
           return 'Q';
       }

       @Override
       public String getMenuName() {
           return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_Questions");
       }

       @Override
       public KeyStroke getKeyStroke() {
           return null;
       }
   };

}
