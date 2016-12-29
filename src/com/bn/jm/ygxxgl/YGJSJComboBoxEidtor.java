package com.bn.jm.ygxxgl;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class YGJSJComboBoxEidtor extends AbstractCellEditor implements TableCellEditor,ItemListener
{
	JComboBox jcbJS=new JComboBox();
	YGXXGLPanel ygxxglPn;
	int row,col;
	String data;
	Map<String,String> JSMapforEditor;
	Map<String,String> JSMapforRender;
	public YGJSJComboBoxEidtor(YGXXGLPanel ygxxglPn) 
	{
		this.ygxxglPn=ygxxglPn;
		Set<String> keyset=this.ygxxglPn.JSMapforEditor.keySet();
		for(String item:keyset)
		{
			jcbJS.addItem(item);
		}
		jcbJS.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return data;
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if(e.getStateChange()==ItemEvent.DESELECTED)
		{
			String val=jcbJS.getSelectedItem().toString();
		    data=ygxxglPn.JSMapforEditor.get(val);
			ygxxglPn.tableDataYG[row][col]=data;
			ygxxglPn.tmYG.fireTableCellUpdated(row,col);
		}
		
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.row=row;
		this.col=column;
		String select=ygxxglPn.JSMapforRender.get(value.toString());
		jcbJS.setSelectedItem(select);
		return jcbJS;
	}

}
