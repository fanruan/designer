package com.fr.start;

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
        DesignerLauncher.getInstance().start(args);
    }
}
