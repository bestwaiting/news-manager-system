package com.bn.jm.ygxxgl;

import static com.bn.core.Constant.selectedBg;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class YGLZRenderer extends JLabel implements TableCellRenderer
{
	Map<String,String> map=new HashMap<String,String>();
    public YGLZRenderer()
    {
		map.put("0", "‘⁄÷∞");
		map.put("1", "¿Î÷∞");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String lzly=map.get(value.toString());
		this.setText(lzly);
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
