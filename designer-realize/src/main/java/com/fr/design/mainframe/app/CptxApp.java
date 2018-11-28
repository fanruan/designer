package com.fr.design.mainframe.app;

import com.fr.base.extension.FileExtension;
import com.fr.base.frpx.exception.FRPackageRunTimeException;
import com.fr.base.frpx.exception.InvalidWorkBookException;
import com.fr.design.i18n.Toolkit;
import com.fr.exception.DecryptTemplateException;
import com.fr.exception.RemoteDesignPermissionDeniedException;
import com.fr.exception.TplLockedException;
import com.fr.file.FILE;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;
import com.fr.main.impl.WorkBookAdapter;
import com.fr.main.impl.WorkBookX;

import java.io.InputStream;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class CptxApp extends AbstractWorkBookApp {

    @Override
    public String[] defaultExtensions() {

        return new String[]{FileExtension.CPTX.getExtension()};
    }

    @Override
    public WorkBook asIOFile(FILE file) {

        FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Template_Opening_And_Waiting", file.getName()) + "...");
        WorkBookX tpl = new WorkBookX();
        InputStream inputStream;
        try {
            inputStream = file.asInputStream();
            long time = System.currentTimeMillis();
            tpl = new WorkBookX(inputStream);
            FineLoggerFactory.getLogger().error("cost: " + (System.currentTimeMillis() - time) + " ms");
        } catch (DecryptTemplateException e) {
            throw e;
        } catch (RemoteDesignPermissionDeniedException exp) {
            FineLoggerFactory.getLogger().error(Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied") + file, exp);
        } catch (TplLockedException exp) {
            FineLoggerFactory.getLogger().error(file + Toolkit.i18nText("Fine-Design_Basic_Template_Status_Locked"), exp);
        } catch (Exception exp) {
            if (exp instanceof FRPackageRunTimeException) {
                throw (FRPackageRunTimeException) exp;
            }
            throw new InvalidWorkBookException(file + ":" + exp.getMessage(), exp);
        }


        return new WorkBookAdapter(tpl);
    }
}
