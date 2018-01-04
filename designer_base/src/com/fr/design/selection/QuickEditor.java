package com.fr.design.selection;

import com.fr.design.designer.TargetComponent;

import javax.swing.JComponent;

/**
 * 快速编辑区域
 *
 * @author zhou
 * @since 2012-7-12下午2:48:20
 */
@SuppressWarnings("rawtypes")
public abstract class QuickEditor<T extends TargetComponent> extends JComponent {
    private static final long serialVersionUID = 5434472104640676832L;

    protected T tc;

    protected boolean isEditing = false;

    public QuickEditor() {

    }

    public void populate(T tc) {
        isEditing = false;
        this.tc = tc;
        refresh();
        isEditing = true;
    }

    /**
     * 触发保存一定要用这个
     */
    protected void fireTargetModified() {
        if (!isEditing) {
            return;
        }
        tc.fireTargetModified();
    }

    /**
     * 刷新面板
     */
    protected abstract void refresh();


    /**
     * 关闭模板时释放模板对象
     * 所有持有tc的对象也必须置空或者丢弃对于tc的引用
     */
    public void release() {
        tc = null;
    }

    public static QuickEditor DEFAULT_EDITOR = new QuickEditor() {

        @Override
        protected void refresh() {
        }

    };

}