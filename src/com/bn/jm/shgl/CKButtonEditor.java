package com.bn.jm.shgl;

import static com.bn.core.Constant.bpicPath;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.bn.jm.MainJFrame;

public class CKButtonEditor extends AbstractCellEditor implements TableCellEditor,ActionListener
{
	JButton jbCK = new JButton("",null);
	SHGLPanel shglpn;
	String shid;
	MainJFrame mf;
	//用来存储每次点击的单元格的位置
	int row,column;
	public CKButtonEditor(SHGLPanel shglpn,MainJFrame mf)
	{
		this.shglpn=shglpn;
		jbCK.addActionListener(this);
		this.mf=mf;
	}
	//jbxg的监听器
	@Override
	public void actionPerformed(ActionEvent e)
	{
		shid=(shglpn.tableData[row][0]).toString();
		mf.gotoCK(shid);	
	}
	
	
	//获取单元格编辑控件
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//设置显示的文字和图标
		String text="查看新闻";
		String path=bpicPath+"ckxw.png";
		//记录行 列 
		this.row=row;
		this.column=column;
		jbCK.setText(text);
		jbCK.setIcon(new ImageIcon(path));
		return jbCK;
	}

	//获得编辑器的值
	@Override
	public Object getCellEditorValue()
	{
		return jbCK;
	}
	
	
	

}
