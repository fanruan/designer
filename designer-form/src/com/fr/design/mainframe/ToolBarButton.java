package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.Serializable;

/*
 *august: 控件按钮
 */
public class ToolBarButton extends UIButton implements MouseListener, MouseMotionListener, Serializable {

	private WidgetOption no;
	private MouseEvent lastPressEvent;

	public ToolBarButton(WidgetOption no) {
		super(no.optionIcon());
		this.no = no;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setToolTipText(no.optionName());
		this.setBorder(null);
		this.setOpaque(false);
		this.setRequestFocusEnabled(false);
		this.set4ToolbarButton();
		// FormEditor那边用的DND的复杂方式 这边还必须也用，不然就方便多了
		new DragAndDropDragGestureListener(this, DnDConstants.ACTION_COPY_OR_MOVE);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 20);
	}

	public WidgetOption getNameOption() {
		return this.no;
	}

	public void setNameOption(WidgetOption no) {
		this.no = no;
	}


	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
		}
	}


	public void mousePressed(MouseEvent e) {
		lastPressEvent = e;
	}

	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			return;
		}
		if (lastPressEvent == null) {
			return;
		}
		getModel().setPressed(false);
		getModel().setSelected(false);
		getModel().setRollover(false);

		Object source = e.getSource();
		Widget creatorSource = null;
		if (source instanceof ToolBarButton) {
			ToolBarButton no = (ToolBarButton) e.getSource();
			if (no == null) {
				return;
			}
			creatorSource = no.getNameOption().createWidget();
		}
		if (creatorSource != null) {
			WidgetToolBarPane.getTarget().startDraggingBean(XCreatorUtils.createXCreator(creatorSource));
			lastPressEvent = null;
			this.setBorder(null);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public class DragAndDropDragGestureListener extends DragSourceAdapter implements DragGestureListener {
		private DragSource source;

		public DragAndDropDragGestureListener(ToolBarButton tt, int actions) {
			source = new DragSource();
			source.createDefaultDragGestureRecognizer(tt, actions, this);
		}

		public void dragGestureRecognized(DragGestureEvent dge) {
			ToolBarButton toolBarButton = (ToolBarButton) dge.getComponent();
			if (toolBarButton != null) {
				Widget widget = toolBarButton.getNameOption().createWidget();
				DragAndDropTransferable dragAndDropTransferable = new DragAndDropTransferable(widget);
				dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable, this);
			}
		}

		@Override
		public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {

		}
	}

	public class DragAndDropTransferable implements Transferable {
		private Widget widget;

		public DragAndDropTransferable(Widget widget) {
			this.widget = widget;
		}

		DataFlavor[] flavors = {new DataFlavor(Widget.class, "Widget")};

		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			for (DataFlavor df : flavors) {
				if (ComparatorUtils.equals(df, flavor)) {
					return true;
				}
			}
			return false;
		}

		public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
			return widget;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {


	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}