package com.fr.design.mainframe.toolbar;

import com.fr.design.mainframe.vcs.ui.FileVersionCellEditor;
import com.fr.design.mainframe.vcs.ui.FileVersionCellRender;
import com.fr.design.mainframe.vcs.ui.FileVersionFirstRowPanel;
import com.fr.design.mainframe.vcs.ui.FileVersionRowPanel;
import com.fr.design.mainframe.vcs.ui.FileVersionTablePanel;
import com.fr.design.mainframe.vcs.ui.FileVersionsPanel;
import com.fr.third.javax.inject.Singleton;
import com.fr.third.springframework.context.annotation.Bean;
import com.fr.third.springframework.context.annotation.ComponentScan;
import com.fr.third.springframework.context.annotation.Configuration;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;
import com.fr.workspace.server.vcs.VcsOperatorImpl;
import com.fr.workspace.server.vcs.filesystem.VcsFileSystem;
import com.fr.workspace.server.vcs.git.FineGit;


/**
 * Created by XiaXiang on 2019/4/16.
 */
@Configuration
@ComponentScan({"com.fr.workspace.server.vcs", "com.fr.design.mainframe.vcs"})
public class VcsConfig {
    @Bean
    @Singleton
    public FileVersionsPanel fileVersionsPanel() {
        return new FileVersionsPanel(fileVersionTablePanel());
    }

    @Bean
    @Singleton
    public FileVersionTablePanel fileVersionTablePanel() {
        return new FileVersionTablePanel(vcsOperator(), fileVersionCellEditor(), fileVersionCellRender());
    }

    @Bean
    @Singleton
    public FileVersionCellEditor fileVersionCellEditor() {
        return new FileVersionCellEditor(fileVersionFirstRowPanel(), fileVersionRowPanel(), vcsOperator());
    }

    @Bean
    @Singleton
    public FileVersionFirstRowPanel fileVersionFirstRowPanel() {
        return new FileVersionFirstRowPanel();
    }

    @Bean
    public FileVersionRowPanel fileVersionRowPanel() {
        return new FileVersionRowPanel(vcsOperator());
    }

    @Bean
    @Singleton
    public FileVersionCellRender fileVersionCellRender() {
        return new FileVersionCellRender(fileVersionFirstRowPanel(), fileVersionRowPanel());
    }

    @Bean
    @Singleton
    public VcsOperator vcsOperator() {
        return new VcsOperatorImpl(vcsFileSystem(), new FineGit(vcsFileSystem().getVcsHistoryPath()));
    }

    @Bean
    @Singleton
    public VcsFileSystem vcsFileSystem() {
        return new VcsFileSystem(WorkContext.getCurrent());
    }


}
