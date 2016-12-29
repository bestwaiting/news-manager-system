package com.bn.jm.grxwgl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.bn.jm.LoginWindow;
import com.bn.jm.jsqxgl.JSQXGLPanel;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.util.SocketUtil;

import static com.bn.core.Constant.DEL_NEW;
import static com.bn.core.Constant.bpicPath;
import static com.bn.core.Constant.dataGeted;

@SuppressWarnings("serial")
public class SCButtonEditor extends AbstractCellEditor implements TableCellEditor,ActionListener
{//新闻删除的JButton的编辑器
	
	JButton jbSC = new JButton("",null);
	GRXWGLPanel grxwglpn;
	String ztmc,xwid;
	//用来存储每次点击的单元格的位置
	int row,column;
	public SCButtonEditor(GRXWGLPanel grxwglpn)
	{
		this.grxwglpn=grxwglpn;
		jbSC.addActionListener(this);
	}
	//jbxg的监听器
	@Override
	public void actionPerformed(ActionEvent e)
	{
		ztmc=(grxwglpn.tableData[row][column-3]).toString();
		xwid=(grxwglpn.tableData[row][0]).toString();
		if(ztmc.equals("未提交审核")||ztmc.equals("未通过审核"))
		{
			int i = JOptionPane.showConfirmDialog(grxwglpn, "确认要删除此条新闻吗?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(i==0)//如果点"是"
			{
				delNEW(xwid);
			}
		}else
		{
			JOptionPane.showMessageDialog(grxwglpn,"只允许删除未提交审核,未通过审核的新闻！","提示",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	
	//获取单元格编辑控件
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//设置显示的文字和图标
		String text="删除";
		String path=bpicPath+"sc.png";
		//记录行 列 
		this.row=row;
		this.column=column;
		jbSC.setText(text);
		jbSC.setIcon(new ImageIcon(path));
		return jbSC;
	}

	//获得编辑器的值
	@Override
	public Object getCellEditorValue()
	{
		return jbSC;
	}
	
	
	//删除新闻
	public void delNEW(final String xwid)//参数是 角色ID
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = DEL_NEW;//能否删除该角色
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(xwid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.equals("ok"))
				{
					JOptionPane.showMessageDialog(grxwglpn, "新闻删除成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					grxwglpn.flushData();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(grxwglpn, "新闻删除失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
}
