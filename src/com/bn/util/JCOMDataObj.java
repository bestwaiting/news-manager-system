package com.bn.util;
/*
 * 下拉列表框数据或JList对象
 * 用于绑定ID与名称
 */
public class JCOMDataObj//下拉列表框数据对象
{
	private String id;//ID
	private String text;//名称
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