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

public class SHButtonEditor extends AbstractCellEditor implements TableCellEditor,ActionListener
{
	JButton jbSH = new JButton("",null);
	SHGLPanel shglpn;
	String ztmc,shid;
	MainJFrame mf;
	//用来存储每次点击的单元格的位置
	int row,column;
	public SHButtonEditor(SHGLPanel shglpn,MainJFrame mf)
	{
		this.shglpn=shglpn;
		jbSH.addActionListener(this);
		this.mf=mf;
	}
	//jbxg的监听器
	@Override
	public void actionPerformed(ActionEvent e)
	{
		ztmc=(shglpn.tableData[row][column-2]).toString();
		shid=(shglpn.tableData[row][0]).toString();
		//System.out.println(ztmc);
		if(ztmc.equals("提交未审核"))
		{
			mf.gotoSH(shid);
		}else
		{
			JOptionPane.showMessageDialog(shglpn,"此新闻已经审核过了！","提示",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	
	//获取单元格编辑控件
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//设置显示的文字和图标
		String text="审核";
		String path=bpicPath+"sh.png";
		//记录行 列 
		this.row=row;
		this.column=column;
		jbSH.setText(text);
		jbSH.setIcon(new ImageIcon(path));
		return jbSH;
	}

	//获得编辑器的值
	@Override
	public Object getCellEditorValue()
	{
		return jbSH;
	}
	
	
	

}
