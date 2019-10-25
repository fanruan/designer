package com.fr.design.gui.itree.filetree;

import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.report.ExtraReportClassManager;
import com.fr.report.fun.ReportSupportedFileProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by alex sung on 2019/7/23.
 */
public class FileNodeConstants {

    private static List<String> supportFileType;
    private static ReadWriteLock rwl = new ReentrantReadWriteLock();

    private FileNodeConstants() {
    }

    static {
        initSupportedTypes();

        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent pluginEvent) {
                initSupportedTypes();
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext pluginContext) {
                return pluginContext.contain(PluginModule.ExtraDesign);
            }
        });
    }

    private static void addAppExtensions(FileExtension[] extensions) {
        for (int i = 0, size = extensions.length; i < size; i++) {
            if (!supportFileType.contains(extensions[i].getExtension())) {
                supportFileType.add(extensions[i].getExtension());
            }
        }
    }

    private static void initSupportedTypes() {
        try {
            rwl.writeLock().lock();
            supportFileType = new ArrayList<String>();
            //通过插件扩展的
            Set<ReportSupportedFileProvider> providers = ExtraReportClassManager.getInstance().getArray(ReportSupportedFileProvider.XML_TAG);
            for (ReportSupportedFileProvider provider : providers) {
                addAppExtensions(provider.getFileExtensions());
            }
            supportFileType.addAll(Arrays.asList(FRContext.getFileNodes().getSupportedTypes()));

        } finally {
            rwl.writeLock().unlock();
        }
    }

    public static String[] getSupportFileTypes() {
        try {
            rwl.readLock().lock();
            return supportFileType.toArray(new String[0]);
        } finally {
            rwl.readLock().unlock();
        }
    }
}
