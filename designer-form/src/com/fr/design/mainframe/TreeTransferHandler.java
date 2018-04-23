package com.fr.design.mainframe;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.gui.LayoutUtils;

public class TreeTransferHandler extends TransferHandler {

    private static int PAD = 4;

    public TreeTransferHandler() {
        super("selectionPath");
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        DataFlavor[] flavors = support.getDataFlavors();

        if ((flavors != null) && (flavors.length > 0)) {
            ComponentTree tree = (ComponentTree) support.getComponent();

            try {
				Object transferdata = support.getTransferable().getTransferData(flavors[0]);
				if (!(transferdata instanceof TreePath)) {
					return false;
				}
				TreePath path = (TreePath) transferdata;
				XCreator draggedComponent = (XCreator) path.getLastPathComponent();
				DropLocation loc = support.getDropLocation();
				Point p = loc.getDropPoint();
				TreePath newpath = tree.getPathForLocation(p.x, p.y);

				if (newpath == null) {
					TreePath closestPath = tree.getClosestPathForLocation(p.x, p.y);

					if (closestPath != null) {
						return canPathAccept(tree, closestPath, draggedComponent, p);
					} else {
						return false;
					}
                } else {
                    return canPathAccept(tree, newpath, draggedComponent, p);
                }
            } catch (Exception ex) {
                FRContext.getLogger().error(ex.getMessage(), ex);
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean canPathAccept(ComponentTree tree, TreePath path, XCreator dragged, Point p) {
        XCreator hovering = (XCreator) path.getLastPathComponent();

        if (SwingUtilities.isDescendingFrom(hovering, dragged)) {
            return false;
        } else {
            if (SwingUtilities.isDescendingFrom(dragged, hovering)) {
                if (hovering instanceof XLayoutContainer) {
                    return ((XLayoutContainer) hovering).getLayoutAdapter().canAcceptMoreComponent();
                } else {
                    return false;
                }
            } else {
            	if (hovering instanceof XLayoutContainer) {
                    return ((XLayoutContainer) hovering).getLayoutAdapter().canAcceptMoreComponent();
                }

                Rectangle bounds = tree.getRowBounds(tree.getRowForLocation(p.x, p.y));

                if (bounds == null) {
                    return false;
                } else {
                    bounds.y += PAD;
                    bounds.height -= (2 * PAD);

                    if (bounds.contains(p)) {
                        return false;
                    } else {
                    	XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(hovering);
                        return parent.getLayoutAdapter().canAcceptMoreComponent();
                    }
                }
            }
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (canImport(support)) {
            DataFlavor[] flavors = support.getDataFlavors();
            ComponentTree tree = (ComponentTree) support.getComponent();

            try {
                TreePath path = (TreePath) support.getTransferable().getTransferData(flavors[0]);
                XCreator draggedComponent = (XCreator) path.getLastPathComponent();
                DropLocation loc = support.getDropLocation();
                Point p = loc.getDropPoint();
                TreePath newpath = tree.getPathForLocation(p.x, p.y);

                if (newpath == null) {
                    newpath = tree.getClosestPathForLocation(p.x, p.y);
                }

                accept(tree, newpath, draggedComponent, p);
                tree.refreshUI();
                tree.fireTreeChanged();

                return true;
            } catch (Exception ex) {
                FRContext.getLogger().error(ex.getMessage(), ex);

                return false;
            }
        } else {
            return false;
        }
    }

    private void accept(ComponentTree tree, TreePath path, XCreator dragged, Point p) {
        XCreator hovering = (XCreator) path.getLastPathComponent();

        if (SwingUtilities.isDescendingFrom(dragged, hovering)) {
            if (XCreatorUtils.getParentXLayoutContainer(dragged) != hovering) {
                removeComponent(dragged);
               ((XLayoutContainer)hovering).getLayoutAdapter().addNextComponent(dragged);
            }
        } else {
            if (hovering instanceof XLayoutContainer) {
                removeComponent(dragged);
                ((XLayoutContainer)hovering).getLayoutAdapter().addNextComponent(dragged);
            } else {
                Rectangle bounds = tree.getRowBounds(tree.getRowForLocation(p.x, p.y));
                XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(dragged);
                LayoutAdapter containerAdapter = container.getLayoutAdapter();

                if (p.y < (bounds.y + PAD)) {
                    removeComponent(dragged);
                    containerAdapter.addBefore(hovering, dragged);
                } else if (p.y > ((bounds.y + bounds.height) - PAD)) {
                    removeComponent(dragged);
                    containerAdapter.addAfter(hovering, dragged);
                }
            }
        }
    }

    private void removeComponent(final Component dragged) {
        Container container = dragged.getParent();
        container.remove(dragged);
        container.invalidate();
        LayoutUtils.layoutRootContainer(container);
    }
}