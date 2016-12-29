package com.bn.jm.ygxxgl;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.bn.jm.ygxxgl.YGXXGLPanel;

@SuppressWarnings("serial")
public class YGLZJComboBoxEditor extends AbstractCellEditor implements TableCellEditor,ItemListener 
{
	String[] strLZYF={"‘⁄÷∞","¿Î÷∞"};
	JComboBox jcb=new JComboBox(strLZYF);
	int row,col;
	String data;
	YGXXGLPanel ygxxglPn;
	public YGLZJComboBoxEditor(YGXXGLPanel ygxxglPn) 
	{
		this.ygxxglPn=ygxxglPn;
		jcb.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {

		return data;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.DESELECTED)
		{
			if(jcb!=null)
			{
				String data=new String(jcb.getSelectedIndex()+"");
				ygxxglPn.tableDataYG[row][col]=data;
				ygxxglPn.tmYG.fireTableCellUpdated(row,col);
			}
		}
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.row=row;
		this.col=column;
		int lzid=Integer.parseInt(value.toString());
		jcb.setSelectedIndex(lzid);
		return jcb;
	}

}
