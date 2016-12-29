package com.bn.util;
/*
 * ���ݽڵ�List������ģ��
 */
import static com.bn.core.Constant.tpicPath;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;
import com.bn.util.BKJTreeNode;
import java.lang.AssertionError;//(*^__^*) ������������ʶһ�£�
/*
 * ��List<String []> list��ȡ��String[]ת��ΪBKJTreeNode����
 * ����BKJTreeNode��������ģ��DefaultTreeModel����
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
 * ��ģ���齨������
 * 1.�õ����ڵ�root�����ã�Ȼ���ʣ��1��len-1���ڵ����
 * 2.�õ�ʣ�����нڵ�ĸ��ڵ�id��pid���pidΪ0�����˽ڵ����root��
 * 3.����˽ڵ�pid��Ϊ0������1��len-1�ڵ㣬�ֱ�ȡ����ڵ�id,ֱ����pid��ȣ����˽ڵ�ҽӵ��ҵ��Ľڵ��ϡ�
 */
 
 
