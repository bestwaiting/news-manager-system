package com.bn.jm.ygxxgl;

import static com.bn.core.Constant.*;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//员工级别 Button 绘制器
@SuppressWarnings("serial")
public class YGButtonRenderer extends JButton implements TableCellRenderer
{
	public YGButtonRenderer()
	{
		//是否设置为不透明
		this.setOpaque(false);
		//设置水平居中
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
	//获得单元格绘制器
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{

		String path=null;
		String text=null;
	    text="修改";
	    path=bpicPath+"xg.png";
		//设置绘制的JButton的文字和图标
		this.setText(text);
		this.setIcon(new ImageIcon(path));
		return this;
	}
}
