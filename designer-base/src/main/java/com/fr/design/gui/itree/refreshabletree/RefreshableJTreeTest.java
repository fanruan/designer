package com.fr.design.gui.itree.refreshabletree;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeModel;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

public class RefreshableJTreeTest {
	private static final Integer[][] ii = new Integer[][] {
		new Integer[] {new Integer(0), new Integer(1), new Integer(2)},
		new Integer[] {new Integer(0), new Integer(2), new Integer(3)},
		new Integer[] {new Integer(6), new Integer(7), new Integer(1)},
		new Integer[] {new Integer(3), new Integer(4), new Integer(2)},
		new Integer[] {new Integer(4), new Integer(2), new Integer(3)}
	};

	public static void main(String[] args) {
		JFrame frm = new JFrame("LOADING...");
		JPanel contentPane = (JPanel)frm.getContentPane();
		contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		final RefreshableJTree tree = new RefreshableJTree() {
			
			protected boolean interceptRefresh(ExpandMutableTreeNode eTreeNode) {
				return eTreeNode.getChildCount() == 0 
				|| ((ExpandMutableTreeNode)eTreeNode.getFirstChild()).getUserObject() == PENDING;
			}
			
			protected ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode) {
				Object[] os = ii[new Random().nextInt(5)];
				ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[os.length];
				for(int i = 0; i < os.length; i++) {
					res[i] = new ExpandMutableTreeNode(os[i]);
				}
				
				return res;
			}
			
		};
		contentPane.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		ExpandMutableTreeNode root = (ExpandMutableTreeNode)model.getRoot();
		root.removeAllChildren();
		
		ExpandMutableTreeNode node001 = new ExpandMutableTreeNode(new Integer(0));
		root.add(node001);
		node001.add(new ExpandMutableTreeNode("NODE0011"));
		node001.add(new ExpandMutableTreeNode("NODE0012"));
		node001.add(new ExpandMutableTreeNode("NODE0013"));
		
		ExpandMutableTreeNode node002 = new ExpandMutableTreeNode(new Integer(1));
		root.add(node002);
		node002.add(new ExpandMutableTreeNode(RefreshableJTree.PENDING));
		
		ExpandMutableTreeNode node003 = new ExpandMutableTreeNode(new Integer(2));
		root.add(node003);
		
		model.reload(root);
		
		UIButton btn = new UIButton("refresh");
		contentPane.add(GUICoreUtils.createFlowPane(btn, FlowLayout.LEFT), BorderLayout.NORTH);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tree.refresh();
			}
		});
		
		frm.setSize(600, 400);
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	
}