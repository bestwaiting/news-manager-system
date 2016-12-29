package com.bn.util;
/*
 * 自定义的树节点
 */
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class BKJTreeNode extends DefaultMutableTreeNode{
	
	private String title; //标题
    private ImageIcon icon; //图标
    private String msg;//描述信息
    private int id;//本节点id
    private int pid;//父节点id
    
    public boolean ybyFlag=false;//左侧功能树裁剪辅助标志

	public BKJTreeNode(String title, ImageIcon icon, int id, int pid) {
		super();
		this.title = title;
		this.icon = icon;
		this.id = id;
		this.pid = pid;
	}
	public BKJTreeNode(String title, ImageIcon icon, int id) {
		this.title = title;
		this.id = id;
		this.icon=icon;
	}

	@Override
	public String toString()
	{
		return this.title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public BKJTreeNode getNode()
	{
		return this;
	}
}
