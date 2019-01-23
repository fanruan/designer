package com.fr.design.mainframe;

import com.fr.file.FILE;
import com.fr.stable.CoreConstants;
import com.fr.third.javax.annotation.Nonnull;
import com.fr.third.javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class JTemplateFactory {
    private static final List<App<?>> ALL_APP = new ArrayList<App<?>>();

    private JTemplateFactory() {
    }

    /**
     * 生成设计器编辑模板对象
     *
     * @param file 包含了模板名称，类型以及内容的文件
     * @return 设计器编辑的模板对象
     */
    @Nullable
    public static JTemplate<?, ?> createJTemplate(@Nonnull FILE file) {

        String fileName = file.getName();
        int indexOfLastDot = fileName.lastIndexOf(CoreConstants.DOT);
        if (indexOfLastDot < 0) {
            return null;
        }
        String fileExtension = fileName.substring(indexOfLastDot + 1);
        for (App<?> app : ALL_APP) {
            String[] defaultAppExtensions = app.defaultExtensions();
            for (String defaultAppExtension : defaultAppExtensions) {
                if (defaultAppExtension.equalsIgnoreCase(fileExtension)) {
                    JTemplate<?, ?> jt = app.openTemplate(file);
                    if (jt != null) {
                        return jt;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 注册app.
     *
     * @param app 注册app.
     */
    public static void register(App<?> app) {
        if (app != null) {
            ALL_APP.add(app);
        }
    }

    public static void remove(App<?> app) {
        if (app != null) {
            ALL_APP.remove(app);
        }
    }
}