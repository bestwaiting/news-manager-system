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
	//�����洢ÿ�ε���ĵ�Ԫ���λ��
	int row,column;
	public SHButtonEditor(SHGLPanel shglpn,MainJFrame mf)
	{
		this.shglpn=shglpn;
		jbSH.addActionListener(this);
		this.mf=mf;
	}
	//jbxg�ļ�����
	@Override
	public void actionPerformed(ActionEvent e)
	{
		ztmc=(shglpn.tableData[row][column-2]).toString();
		shid=(shglpn.tableData[row][0]).toString();
		//System.out.println(ztmc);
		if(ztmc.equals("�ύδ���"))
		{
			mf.gotoSH(shid);
		}else
		{
			JOptionPane.showMessageDialog(shglpn,"�������Ѿ���˹��ˣ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	
	//��ȡ��Ԫ��༭�ؼ�
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//������ʾ�����ֺ�ͼ��
		String text="���";
		String path=bpicPath+"sh.png";
		//��¼�� �� 
		this.row=row;
		this.column=column;
		jbSH.setText(text);
		jbSH.setIcon(new ImageIcon(path));
		return jbSH;
	}

	//��ñ༭����ֵ
	@Override
	public Object getCellEditorValue()
	{
		return jbSH;
	}
	
	
	

}
