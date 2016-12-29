package com.bn.jm.ygxxgl;

import static com.bn.core.Constant.selectedBg;
import static com.bn.core.Constant.tpicPath;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class YGBMRenderer extends JLabel implements TableCellRenderer
{
	YGXXGLPanel ygxxglPn;
	public YGBMRenderer(YGXXGLPanel ygxxglPn) 
	{
		this.ygxxglPn=ygxxglPn;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) 
	{
		String bmid=value.toString();
		for(int i=0;i<ygxxglPn.listBM.size();i++)
		{
			String[] str=ygxxglPn.listBM.get(i);
			if(bmid.equals(str[0]))
			{
				this.setText(str[2]);
				this.setIcon(new ImageIcon(tpicPath+"bm.png"));
			}
		}
		this.setBackground(selectedBg);
		if(isSelected)
		{
			this.setOpaque(true);
		}else
		{
			this.setOpaque(false);
		}
		return this;
	}

}
