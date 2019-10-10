package com.fr.design.ui.util;

import com.fr.log.FineLoggerFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;

/**
 * 一些常用的 GUI 工具。
 * <p>
 * 为什么提供 invokeLaterIfNeeded 和 invokeAndWaitIfNeeded这样的方法？
 * 因为 swing 渲染 UI 是单线程的，如果直接使用
 * {@link SwingUtilities#invokeLater(Runnable)}，当 invokeLater 方法
 * 嵌套的时候，Runnable 会被放到事件循环队列的末尾，从而变成异步而非立即执行，
 * 这是一处坑点。invokeLaterIfNeeded 的行为，当处于事件分发线程（EDT），
 * 则直接运行，当处于其他线程，则使用 EDT 来执行。
 * <p>
 * 方法{@link SwingUtilities#invokeAndWait(Runnable)}，也有一个注意点，
 * 不允许在事件分发线程（EDT）中调用，否则抛错，所以也有必要加上判断 EDT 的逻辑。
 *
 * @author vito
 * @version 10.0
 * Created by vito on 2019/9/16
 */
public class UIUtil {
    /**
     * 在 AWT 线程上立即调用runnable，否则使用  {@link SwingUtilities#invokeLater(Runnable)} 代替。
     *
     * @param runnable 等待调用的 runnable
     */
    public static void invokeLaterIfNeeded(@NotNull Runnable runnable) {
        if (EdtInvocationManager.getInstance().isEventDispatchThread()) {
            runnable.run();
        } else {
            EdtInvocationManager.getInstance().invokeLater(runnable);
        }
    }

    /**
     * 在 AWT 线程上立即调用runnable，否则使用  {@link SwingUtilities#invokeAndWait(Runnable)} 代替。
     *
     * @param runnable 等待调用的 runnable
     */
    public static void invokeAndWaitIfNeeded(@NotNull Runnable runnable) {
        if (EdtInvocationManager.getInstance().isEventDispatchThread()) {
            runnable.run();
        } else {
            try {
                EdtInvocationManager.getInstance().invokeAndWait(runnable);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}
