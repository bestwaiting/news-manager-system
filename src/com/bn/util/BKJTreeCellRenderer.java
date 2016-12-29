package com.bn.util;
import static com.bn.core.Constant.*;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/*
 * 自定义的树的节点绘制器
 */
public class BKJTreeCellRenderer implements TreeCellRenderer { 

    public BKJTreeCellRenderer() { } 

    @Override 
    public Component getTreeCellRendererComponent(JTree tree,Object value, boolean sel, 
        boolean expanded, boolean leaf, int row,boolean hasFocus) {  
        JLabel jl=new JLabel();
        BKJTreeNode	myNode = (BKJTreeNode)value;
    	jl.setText(myNode.getTitle()); 
    	jl.setIcon(myNode.getIcon()); 
    	if(sel)
    	{
    		//如果选中，则设置前景色为白，背景色为蓝，透明度为true
    		jl.setForeground(Color.white);
    		jl.setBackground(nodeBg);
    		jl.setOpaque(true);
    	}    	
        return jl; 
    } 
} 
