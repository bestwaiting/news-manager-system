package com.bn.util;
/*
 * �����б�����ݻ�JList����
 * ���ڰ�ID������
 */
public class JCOMDataObj//�����б�����ݶ���
{
	private String id;//ID
	private String text;//����
	public JCOMDataObj(String id, String text) {
		super();
		this.setId(id);
		this.text = text;
	}
	@Override
	public String toString(){
		return this.text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText()
	{
		return this.text;
	}
	public void setText(String s)
	{
		this.text=s;
	}
}