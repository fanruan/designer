package com.fr.start;

import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.process.FineProcess;
import com.fr.process.engine.FineJavaProcessFactory;
import com.fr.process.engine.core.FineProcessEngineEvent;

/**
 * 设计器主进程入口(无缝更换升级jar包，若使用其他类作为入口，需要重新打包designer.exe等，升级后仍然走的原来逻辑)
 *
 * 设计器逻辑/UI调试 see MainDesigner/Designer4Debug
 *
 * @author hades
 * @date 2019/8/27
 */
public class Designer {

    public static void main(String[] args) {
        // 创建进程
        final FineProcess process = FineJavaProcessFactory.create().
                entry("com.fr.start.MainDesigner").
                javaRuntime(DesignerJavaRuntime.getInstance().getJavaExec()).
                classPath(DesignerJavaRuntime.getInstance().getClassPath()).
                inheritJvmSettings().
                jvmSettings(DesignerJavaRuntime.getInstance().getJvmOptions()).
                arguments(args).
                startProcess(DesignerProcessType.INSTANCE);

        process.getPipe().listen(FineProcessEngineEvent.DESTROY, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                process.destroy();
            }
        });

    }
}
