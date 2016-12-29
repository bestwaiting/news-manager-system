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
	//�����洢ÿ�ε���ĵ�Ԫ���λ��
	int row,column;
	public CKButtonEditor(SHGLPanel shglpn,MainJFrame mf)
	{
		this.shglpn=shglpn;
		jbCK.addActionListener(this);
		this.mf=mf;
	}
	//jbxg�ļ�����
	@Override
	public void actionPerformed(ActionEvent e)
	{
		shid=(shglpn.tableData[row][0]).toString();
		mf.gotoCK(shid);	
	}
	
	
	//��ȡ��Ԫ��༭�ؼ�
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//������ʾ�����ֺ�ͼ��
		String text="�鿴����";
		String path=bpicPath+"ckxw.png";
		//��¼�� �� 
		this.row=row;
		this.column=column;
		jbCK.setText(text);
		jbCK.setIcon(new ImageIcon(path));
		return jbCK;
	}

	//��ñ༭����ֵ
	@Override
	public Object getCellEditorValue()
	{
		return jbCK;
	}
	
	
	

}
