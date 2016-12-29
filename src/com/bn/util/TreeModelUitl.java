package com.bn.util;
/*
 * 根据节点List产生树模型
 */
import static com.bn.core.Constant.tpicPath;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;
import com.bn.util.BKJTreeNode;
import java.lang.AssertionError;//(*^__^*) 嘻嘻……，标识一下！
/*
 * 从List<String []> list中取出String[]转换为BKJTreeNode对象
 * 并将BKJTreeNode对象搭建成树模型DefaultTreeModel对象
 */

public class TreeModelUitl {

	public static DefaultTreeModel getTreeModel(List<String []> list)
	{
		BKJTreeNode root =null;
		int len=list.size();
		BKJTreeNode []temNode = new BKJTreeNode[len];
		for(int i=0;i<len;i++){
			String[] tem=list.get(i);
			int id=Integer.parseInt(tem[0]);
			int pid=Integer.parseInt(tem[1]);
			String title=tem[2];
			String msg=tem[3];
			if(i==0){
				root=new BKJTreeNode(title, new ImageIcon(tpicPath+"root.png"), id, pid);
				root.setMsg(msg);
			}else{
				temNode[i]=new BKJTreeNode(title, new ImageIcon(tpicPath+"bm.png"), id, pid);
				temNode[i].setMsg(msg);
			}
		}
		DefaultTreeModel dtm=new DefaultTreeModel(root);
		for(int i=1;i<len;i++){
			if(temNode[i].getPid()==0){
				root.add(temNode[i]);
			}else{
				int pid=temNode[i].getPid();
				BKJTreeNode temp = null;
				
				for(int j=1;j<len;j++)
				{
					if(temNode[j].getId()==pid)
					{
						temp=temNode[j];
						break;
					}
				}
				if(temp!=null)
					temp.add(temNode[i]);
			}
		}
		return dtm;
	}
}

/*
 * 树模型组建分析：
 * 1.拿到根节点root的引用，然后对剩下1到len-1个节点遍历
 * 2.拿到剩下所有节点的父节点id，pid如果pid为0，将此节点接在root上
 * 3.如果此节点pid不为0，遍历1到len-1节点，分别取出其节点id,直到和pid相等，将此节点挂接到找到的节点上。
 */
 
 
