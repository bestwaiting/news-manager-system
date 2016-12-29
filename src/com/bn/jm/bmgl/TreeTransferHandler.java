package com.bn.jm.bmgl;

import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.CZ_BM;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import com.bn.util.SocketUtil;
import com.bn.util.BKJTreeNode;
import com.bn.jm.LoginWindow;

@SuppressWarnings("serial")
public class TreeTransferHandler extends TransferHandler 
{
    DataFlavor nodesFlavor;//���ʼ��а�����
    DataFlavor[] flavors = new DataFlavor[1];
    BKJTreeNode[] nodesToRemove;
    
    //*******************������ק���̵ı�������*************
    BMGLPanel BMGLpn;//���Ź�������
    int currentNodeID;//��ǰ��ס�Ľڵ��id
    String currentNodeText;//��ǰ��ס�Ľڵ���ı�
    ImageIcon currentNodeIcon;//��ǰ��ס�Ľڵ��ͼ��
    int x,y;
    
    public TreeTransferHandler(BMGLPanel bmglpn)
    {
    	this.BMGLpn=bmglpn;
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +";class=\"" + BKJTreeNode.class.getName()+"\"";
            //����mimeType��Object���͹���BKJTreeNode.class
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
    
    	
    //�ж��ܷ��϶���ָ��λ��
    @Override
    public boolean canImport(TransferHandler.TransferSupport support)
    {
    	//�ж��Ƿ��ϷŶ�����ֻ�����϶�
        if(!support.isDrop()) {
            return false;
        }
        //����Ϊ���䣨�����ʾһ�����ò��������ӵ�ָ������λ�á�
        support.setShowDropLocation(true);
        //�ж��Ƿ�֧�ָ��������� flavor��
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        
        //�� TransferSupport ��ʾһ�����ò���ʱ����������ĵ�ǰ����λ�� TransferHandler.DropLocation��
        //JTree.DropLocation�� TransferHandler.DropLocationd�����࣬����JTree��DropLocation
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        //���ش˴����Ŀ�������
        JTree tree = (JTree)support.getComponent();
        //dl.getPath()��������Ӧ����������������ݵ�·����
        //tree.getRowForPath ������ʾ��ָ��·����ʶ�Ľڵ���С�
        int dropRow = tree.getRowForPath(dl.getPath());
        
        //������ѽڵ���ק������ڵ�
        //�������е�ǰѡ����С�
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
        };
        
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        TreePath path = tree.getPathForRow(selRows[0]);
        BKJTreeNode firstNode = (BKJTreeNode)path.getLastPathComponent();
        int action = support.getDropAction();
        if(action == javax.swing.TransferHandler.MOVE)
        {
        	currentNodeIcon = firstNode.getIcon();
        	currentNodeText = firstNode.getTitle();
        	BMGLpn.jl.setIcon(currentNodeIcon);
        	BMGLpn.jl.setText(currentNodeText);
        	Point dp = dl.getDropPoint();
        	x = dp.x;
        	y = dp.y;
        	//��ȡ�����λ�ã���ʽ��ָ��������Ͻǵ�һ���㡣��λ��������ڸ�������ռ�ġ�
        	BMGLpn.jl.setBounds(x+BMGLpn.jSTree.getLocation().x-30, y+BMGLpn.jSTree.getLocation().y-5, 100, 20);
            return selectedAllChildNode(tree);
        }
        return true;
    }
    
    //��Ҷ�ӽڵ㣬�ж��Ƿ�ѡ���˴˽ڵ�������ӽڵ�
    private boolean selectedAllChildNode(JTree tree) 
    {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        BKJTreeNode first = (BKJTreeNode)path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected.
        if(childCount > 0 && selRows.length == 1)
            return false;
        // first may have children.
        for(int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            BKJTreeNode next =
                (BKJTreeNode)path.getLastPathComponent();
            //��� next �Ǵ˽ڵ�first���ӽڵ㣬�򷵻� true��
            if(first.isNodeChild(next)) {
                // Found a child of first.
                if(childCount > selRows.length-1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }
 
    //Ϊ��������ṩ���ݵ��ࡣ (��װ��Ҫ��������BKJTreeNode����)
    public class NodesTransferable implements Transferable 
    {
        BKJTreeNode[] nodes;
 
        public NodesTransferable(BKJTreeNode[] nodes) {
            this.nodes = nodes;
         }
		
        
        //����һ�����󣬸ö����ʾ��Ҫ����������ݡ�
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException 
        {
        	//��֧�ֵ�flavor��Exception��
            if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return nodes;
        }
 
        //���� �������ṩ���ݵ� flavor��
		@Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        
 
        //���ش˶����Ƿ�֧��ָ�������� flavor
		@Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
			//���� DataFlavor ��� DataFlavor ������ԡ����ҽ������� DataFlavor �� MIME �������͡������ͺͱ�ʾ��ʽ�඼���ʱ������Ϊ��������ȵġ�
      	    return nodesFlavor.equals(flavor);
        }
    }
    
    //����һ��Ҫ�������ݴ���Դ�� Transferable������һ��NodesTransferable����(��װ��Ҫ�����BKJTreeNode)
    @Override
    protected Transferable createTransferable(JComponent c) {
    	
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) 
        {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<BKJTreeNode> copies = new ArrayList<BKJTreeNode>();
            List<BKJTreeNode> toRemove = new ArrayList<BKJTreeNode>();
            BKJTreeNode node = (BKJTreeNode)paths[0].getLastPathComponent();
            BKJTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            //Ҫ����ȥ�����ط������ڵ�
            BKJTreeNode[] nodes = copies.toArray(new BKJTreeNode[copies.size()]);
            //Ҫ��ԭ����ɾ�������ڵ㡣
            nodesToRemove = toRemove.toArray(new BKJTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }
 
    /** Defensive copy used in createTransferable. */
    private BKJTreeNode copy(BKJTreeNode node) {
    	BKJTreeNode bknode = new BKJTreeNode(node.toString(),node.getIcon(),node.getId());
    	bknode.setMsg(node.getMsg());
    	currentNodeID = node.getId();
    	currentNodeText = node.getTitle();
        return bknode;
    }
 
   
    //����֧�ֵĴ��䶯�������ͣ�COPY��MOVE �� LINK �����ⰴλ�����
    @Override
    public int getSourceActions(JComponent c) {
        return javax.swing.TransferHandler.COPY_OR_MOVE;
    }
 
    //�Ӽ�������ϷŲ�����������
    @Override
    public boolean importData(TransferHandler.TransferSupport support) 
    {

        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.(��transfer�л��Ҫ���������)
        BKJTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (BKJTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.(���Ҫ�����Ŀ��λ����Ϣ)
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        
        TreePath dest = dl.getPath();
        BMGLpn.jl.setIcon(null);
        BMGLpn.jl.setText(null);
        final BKJTreeNode parent = (BKJTreeNode)dest.getLastPathComponent();
        //���ش˴����Ŀ�������
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        //���� getPath() ���ص�·��������Ӧ�ò���������ݵ�λ�õ�������
        int childIndex = dl.getChildIndex();
        int index = childIndex;    // DropMode.INSERT��ʾ����ڵ�֮�䡣
        if(childIndex == -1) {     // DropMode.ON��ʾ�����ڽڵ�֮�ϡ�
            index = parent.getChildCount();
        }
        // Add data to model.(�ؼ�������һ�䣬�õ����ڵ㣬Ҫ����λ�õ�������Ҫ����Ľڵ�����)
        for(int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
    
        
    	//���鲿�ţ��޸ķ��������ݣ�currentNodeIDҪ�ƶ��Ľڵ�id��parentҪ�ƶ�����λ�õĸ��ڵ㣩
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				StringBuffer sb = new StringBuffer();
			    String msg = CZ_BM;
			    sb.append(msg);
			    sb.append(currentNodeID+"");
			    sb.append("<->");
			    sb.append(parent.getId()+"");
			    sb.append(msg);
			    String result = SocketUtil.sendAndGetMsg(sb.toString());
			    
			    if(result.equals("ok"))
			    {
			    	dataGeted=true;
			    	JOptionPane.showMessageDialog(BMGLpn, "��������ɹ���", "��ʾ", JOptionPane.NO_OPTION);
			    	//System.out.println(currentNodeText);
			    	BMGLpn.expandNode(currentNodeID);
			    }else
			    {
			    	dataGeted=true;
			    	JOptionPane.showMessageDialog(BMGLpn, "������ϣ�����ʧ�ܣ�", "��ʾ", JOptionPane.NO_OPTION);
			    	return ;
			    }
				dataGeted=true;
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
        return true;
    }
    
    //�ڵ�������֮�����
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) 
    {
    	BMGLpn.jl.setIcon(null);
    	BMGLpn.jl.setText(null);
        if((action & javax.swing.TransferHandler.MOVE) == javax.swing.TransferHandler.MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for(int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }
 
    public String toString() {
        return getClass().getName();
    }
    
}


/*
 * ʵ�����ڵ���ק��Ч�����裺
 * 1.����Ҫ����Ķ���ָ����Ӧ��DataFlavor
 * 2.canImport()�ж��ܷ���д���
 * 3.createTransferable()������װ��Ҫ��������ݵ�Transferable����
 * 4.importData()�������ݴ���
 * 5.exportDone()��������֮�󣬴�ԭ��������ģ���а��ƶ��˵Ľڵ�ɾ��
 * 
 */

