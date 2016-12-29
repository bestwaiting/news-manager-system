package com.bn.util;
/*
 * �Զ�������ڵ�
 */
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class BKJTreeNode extends DefaultMutableTreeNode{
	
	private String title; //����
    private ImageIcon icon; //ͼ��
    private String msg;//������Ϣ
    private int id;//���ڵ�id
    private int pid;//���ڵ�id
    
    public boolean ybyFlag=false;//��๦�����ü�������־

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
