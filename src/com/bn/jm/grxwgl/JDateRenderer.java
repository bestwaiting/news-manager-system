package com.bn.jm.grxwgl;

import static com.bn.core.Constant.selectedBg;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class JDateRenderer extends JLabel implements TableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		String text=value.toString();
        if(value.toString().equals("��������"))
		{
			text=value.toString();
		}
		else{
			//��õ�ǰѡ��ֵ ������Ϊ��JLabel��ֵ
			 text = value.toString().substring(0, 19);
		    }
			this.setText(text);
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
