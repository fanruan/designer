package com.fr.design.remote.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileAuthorityTreeNode extends DefaultMutableTreeNode {

    public enum Status {
        SELECTED,
        UNSELECTED,
        INDETERMINATE
    }


    private Status status;


    public FileAuthorityTreeNode() {
        this(null);
    }

    public FileAuthorityTreeNode(Object userObject) {
        this(userObject, true, Status.UNSELECTED);
    }

    public FileAuthorityTreeNode(Object userObject, boolean allowsChildren, Status status) {
        super(userObject, allowsChildren);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isSelected() {
        return status == Status.SELECTED;
    }

    public boolean isUnselected() {
        return status == Status.UNSELECTED;
    }

    public boolean isIndeterminate() {
        return status == Status.INDETERMINATE;
    }


    public void setSelected(boolean selected) {
        setStatus(selected ? Status.SELECTED : Status.UNSELECTED);
    }

    public void setStatus(Status status) {
        this.status = status;
        switch (status) {
            case SELECTED:
                // 向下
                // 如果选中，则所有的子结点都要选中
                if (children != null) {
                    for (Object obj : children) {
                        FileAuthorityTreeNode node = (FileAuthorityTreeNode) obj;
                        if (!node.isSelected()) {
                            node.setSelected(true);
                        }
                    }
                }
                // 向上
                // 检查其父节点
                if (parent != null) {
                    FileAuthorityTreeNode pNode = (FileAuthorityTreeNode) parent;
                    int index = 0;
                    for (; index < pNode.children.size(); ++index) {
                        FileAuthorityTreeNode pChildNode = (FileAuthorityTreeNode) pNode.children.get(index);
                        if (!pChildNode.isSelected()) {
                            break;
                        }
                    }
                    // 如果父结点下的子结点都被选中了，那么选中父节点
                    if (index == pNode.children.size()) {
                        if (!pNode.isSelected()) {
                            pNode.setSelected(true);
                        }
                    }
                    // 如果父结点下的子结点有部分没被选中，那么标记父结点选中状况为未知
                    else {
                        if (!pNode.isIndeterminate()) {
                            pNode.setStatus(Status.INDETERMINATE);
                        }
                    }
                }
                break;
            case UNSELECTED:
                // 向下
                // 子结点都要取消选中
                if (children != null) {
                    for (Object aChildren : children) {
                        FileAuthorityTreeNode node = (FileAuthorityTreeNode) aChildren;
                        if (!node.isUnselected()) {
                            node.setSelected(false);
                        }
                    }
                }
                // 向上
                // 查看父节点的选中情况

                if (parent != null) {
                    FileAuthorityTreeNode pNode = (FileAuthorityTreeNode) parent;
                    int index = 0;
                    for (; index < pNode.children.size(); ++index) {
                        FileAuthorityTreeNode pChildNode = (FileAuthorityTreeNode) pNode.children.get(index);
                        if (!pChildNode.isUnselected()) {
                            break;
                        }
                    }
                    // 如果父结点下的子结点都是未被选中的，那么取消选中父节点
                    if (index == pNode.children.size()) {
                        if (!pNode.isUnselected()) {
                            pNode.setSelected(false);
                        }
                    }
                    // 如果父结点下的子结点有部分不是未被选中的，那么标记父结点选中状况为未知
                    else {
                        if (!pNode.isIndeterminate()) {
                            pNode.setStatus(Status.INDETERMINATE);
                        }
                    }
                }
                break;
            case INDETERMINATE:
                // todo 标记为未知



                break;


            default:
                break;
        }
    }
}


