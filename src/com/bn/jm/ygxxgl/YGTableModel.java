package com.bn.jm.ygxxgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class YGTableModel extends AbstractTableModel
{

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	YGXXGLPanel ygxxglpn;
	
	public YGTableModel(YGXXGLPanel ygxxglpn)
	{
		this.ygxxglpn=ygxxglpn;
	}
	//�������
	@Override
	public int getColumnCount() {
		return ygxxglpn.headYG.length;
	}

	//�������
	@Override
	public int getRowCount() {
		return ygxxglpn.tableDataYG.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {	
		return ygxxglpn.tableDataYG[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return ygxxglpn.typeArrayYG[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return ygxxglpn.headYG[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c>5)return true;
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
	@Override
	public void setValueAt(Object value, int r, int c)
	{
		    //��¼�ո��޸����ݵ���
		    ygxxglpn.lastEditRow=r;
			//System.out.println(value);
			if(value!=null)
			{
				if(c!=9)
				{
					//�޸ı��ģ������
					//String data=new String(value.toString());
					if(!ygxxglpn.origindataYG[r][c].equals(value))
					{
						ygxxglpn.tableDataYG[r][c]=value;
						this.fireTableCellUpdated(r, c);
					}
		
				}else
				{
					ygxxglpn.tableDataYG[r][c]=value;
				}	
			}
			
			
	}	
	
	
}

