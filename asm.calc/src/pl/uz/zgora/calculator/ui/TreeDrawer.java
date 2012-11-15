package pl.uz.zgora.calculator.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.Id;
import pl.uz.zgora.calculator.core.tree.TreeElemens;

public class TreeDrawer extends Composite {
	private Tree drawerTree;
	private pl.uz.zgora.calculator.core.tree.Tree tree;
	private Symbol[] symbols;

	public TreeDrawer(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout());
		drawerTree = new Tree(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		drawerTree.setHeaderVisible(false);
		drawerTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void setTree(pl.uz.zgora.calculator.core.tree.Tree tree) {
		this.tree = tree;
	}

	public void setSymbols(Symbol[] symbols) {
		this.symbols = symbols;
	}

	public void computeText() {
		TreeItem head = new TreeItem(drawerTree, SWT.NONE);
		head.setText(tree.getKind().name());
		head.setExpanded(true);
		appendNodes(tree, head);

	}

	private void appendNodes(pl.uz.zgora.calculator.core.tree.Tree tree,
			TreeItem parentItem) {
		if ((tree == null) || (symbols == null)) {
			return;
		}

		pl.uz.zgora.calculator.core.tree.Tree[] child = tree.getChild();
		for (pl.uz.zgora.calculator.core.tree.Tree element : child) {
			StringBuffer sb = new StringBuffer();
			sb.append(element.getKind().name());
			if (element.getKind().equals(TreeElemens.ID)) {
				int ref = ((Id) element).ref;
				sb.append(" :").append(symbols[ref]);
			}
			TreeItem newTree = new TreeItem(parentItem, SWT.NONE);
			newTree.setText(sb.toString());
			parentItem.setExpanded(true);
			newTree.setExpanded(true);
			appendNodes(element, newTree);
		}
	}

	public void setDrawerTree(Tree drawerTree) {
		this.drawerTree = drawerTree;
	}

	public Tree getDrawerTree() {
		return drawerTree;
	}
}
