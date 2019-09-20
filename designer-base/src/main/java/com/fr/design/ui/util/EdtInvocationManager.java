package com.fr.design.ui.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * 事件分发线程管理器。用于管理用户线程
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/9/16
 */
public abstract class EdtInvocationManager {
    @NotNull
    private static EdtInvocationManager ourInstance = new SwingEdtInvocationManager();

    public abstract boolean isEventDispatchThread();

    public abstract void invokeLater(@NotNull Runnable task);

    public abstract void invokeAndWait(@NotNull Runnable task) throws InvocationTargetException, InterruptedException;

    @NotNull
    public static EdtInvocationManager getInstance() {
        return ourInstance;
    }

    /**
     * The default {@link EdtInvocationManager} implementation which works with the EDT via SwingUtilities.
     */
    private static class SwingEdtInvocationManager extends EdtInvocationManager {
        @Override
        public boolean isEventDispatchThread() {
            return SwingUtilities.isEventDispatchThread();
        }

        @Override
        public void invokeLater(@NotNull Runnable task) {
            SwingUtilities.invokeLater(task);
        }

        @Override
        public void invokeAndWait(@NotNull Runnable task) throws InvocationTargetException, InterruptedException {
            SwingUtilities.invokeAndWait(task);
        }
    }
}
