package com.bn.jm.grxwgl;

import static com.bn.core.Constant.*;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//Ա������ Button ������
@SuppressWarnings("serial")
public class ButtonRenderer extends JButton implements TableCellRenderer
{
	public ButtonRenderer()
	{
		//�Ƿ�����Ϊ��͸��
		this.setOpaque(false);
		//����ˮƽ����
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
	//��õ�Ԫ�������
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		String path=null;
		String text=null;
		if(column==6)
		{
			text="�༭";
		    path=bpicPath+"xg.png";
		}else if(column==7)
		{
			text="ɾ��";
		    path=bpicPath+"sc.png";
		}
		//���û��Ƶ�JButton�����ֺ�ͼ��
		this.setText(text);
		this.setIcon(new ImageIcon(path));
		return this;
	}
}
