package com.bn.jm.ygxxgl;

import java.awt.Component;
import java.util.Map;
import static com.bn.core.Constant.selectedBg;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings({ "unused", "serial" })
public class YGJSRenderer extends JLabel implements TableCellRenderer
{
	YGXXGLPanel ygxxglPn;
	public YGJSRenderer(YGXXGLPanel ygxxglPn) 
	{
		this.ygxxglPn=ygxxglPn;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String key=value.toString();
		String jsmc=ygxxglPn.JSMapforRender.get(key);
		this.setText(jsmc);
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
