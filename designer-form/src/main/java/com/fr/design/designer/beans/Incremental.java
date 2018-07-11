/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fr.design.designer.beans;

/**
 * 编辑器的增量，包括上下左右4个方向的增量
 * @author richer
 * @since 6.5.3
 */
public class Incremental {
    public int top = 0;
    public int left = 0;
    public int bottom = 0;
    public int right = 0;

    public Incremental() {
        this(0, 0, 0, 0);
    }
    
    public Incremental(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}