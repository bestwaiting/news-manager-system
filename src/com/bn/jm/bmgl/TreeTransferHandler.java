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
    DataFlavor nodesFlavor;//访问剪切板数据
    DataFlavor[] flavors = new DataFlavor[1];
    BKJTreeNode[] nodesToRemove;
    
    //*******************用于拖拽过程的变量引用*************
    BMGLPanel BMGLpn;//部门管理引用
    int currentNodeID;//当前拖住的节点的id
    String currentNodeText;//当前拖住的节点的文本
    ImageIcon currentNodeIcon;//当前拖住的节点的图标
    int x,y;
    
    public TreeTransferHandler(BMGLPanel bmglpn)
    {
    	this.BMGLpn=bmglpn;
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +";class=\"" + BKJTreeNode.class.getName()+"\"";
            //构建mimeType，Object类型关联BKJTreeNode.class
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
    
    	
    //判断能否拖动到指定位置
    @Override
    public boolean canImport(TransferHandler.TransferSupport support)
    {
    	//判断是否拖放动作，只允许拖动
        if(!support.isDrop()) {
            return false;
        }
        //设置为传输（必须表示一个放置操作）可视地指出放置位置。
        support.setShowDropLocation(true);
        //判断是否支持给定的数据 flavor。
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        
        //此 TransferSupport 表示一个放置操作时，返回组件的当前放置位置 TransferHandler.DropLocation。
        //JTree.DropLocation是 TransferHandler.DropLocationd的子类，代表JTree的DropLocation
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        //返回此传输的目标组件。
        JTree tree = (JTree)support.getComponent();
        //dl.getPath()返回树中应该用来放入放置数据的路径。
        //tree.getRowForPath 返回显示由指定路径标识的节点的行。
        int dropRow = tree.getRowForPath(dl.getPath());
        
        //不允许把节点拖拽到自身节点
        //返回所有当前选择的行。
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
        	//获取组件的位置，形式是指定组件左上角的一个点。该位置是相对于父级坐标空间的。
        	BMGLpn.jl.setBounds(x+BMGLpn.jSTree.getLocation().x-30, y+BMGLpn.jSTree.getLocation().y-5, 100, 20);
            return selectedAllChildNode(tree);
        }
        return true;
    }
    
    //非叶子节点，判断是否选中了此节点的所有子节点
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
            //如果 next 是此节点first的子节点，则返回 true。
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
 
    //为传输操作提供数据的类。 (封装了要传输对象的BKJTreeNode的类)
    public class NodesTransferable implements Transferable 
    {
        BKJTreeNode[] nodes;
 
        public NodesTransferable(BKJTreeNode[] nodes) {
            this.nodes = nodes;
         }
		
        
        //返回一个对象，该对象表示将要被传输的数据。
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException 
        {
        	//不支持的flavor抛Exception。
            if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return nodes;
        }
 
        //返回 可用于提供数据的 flavor。
		@Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        
 
        //返回此对象是否支持指定的数据 flavor
		@Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
			//测试 DataFlavor 与此 DataFlavor 的相等性。当且仅当两个 DataFlavor 的 MIME 基本类型、子类型和表示形式类都相等时，才认为它们是相等的。
      	    return nodesFlavor.equals(flavor);
        }
    }
    
    //创建一个要用作数据传输源的 Transferable，返回一个NodesTransferable对象(封装了要传输的BKJTreeNode)
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
            //要拖拉去其他地方的树节点
            BKJTreeNode[] nodes = copies.toArray(new BKJTreeNode[copies.size()]);
            //要从原树中删除的树节点。
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
 
   
    //返回支持的传输动作的类型；COPY、MOVE 和 LINK 的任意按位或组合
    @Override
    public int getSourceActions(JComponent c) {
        return javax.swing.TransferHandler.COPY_OR_MOVE;
    }
 
    //从剪贴板或拖放操作传输数据
    @Override
    public boolean importData(TransferHandler.TransferSupport support) 
    {

        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.(从transfer中获得要传输的数据)
        BKJTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (BKJTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.(获得要传输的目标位置信息)
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        
        TreePath dest = dl.getPath();
        BMGLpn.jl.setIcon(null);
        BMGLpn.jl.setText(null);
        final BKJTreeNode parent = (BKJTreeNode)dest.getLastPathComponent();
        //返回此传输的目标组件。
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        //对于 getPath() 返回的路径，返回应该插入放置数据的位置的索引。
        int childIndex = dl.getChildIndex();
        int index = childIndex;    // DropMode.INSERT表示插入节点之间。
        if(childIndex == -1) {     // DropMode.ON表示放置在节点之上。
            index = parent.getChildCount();
        }
        // Add data to model.(关键就在这一句，得到父节点，要插入位置的索引，要插入的节点数据)
        for(int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
    
        
    	//重组部门，修改服务器数据（currentNodeID要移动的节点id，parent要移动到的位置的父节点）
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
			    	JOptionPane.showMessageDialog(BMGLpn, "部门重组成功！", "提示", JOptionPane.NO_OPTION);
			    	//System.out.println(currentNodeText);
			    	BMGLpn.expandNode(currentNodeID);
			    }else
			    {
			    	dataGeted=true;
			    	JOptionPane.showMessageDialog(BMGLpn, "网络故障，重组失败！", "提示", JOptionPane.NO_OPTION);
			    	return ;
			    }
				dataGeted=true;
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
        return true;
    }
    
    //在导出数据之后调用
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
 * 实现树节点拖拽的效果步骤：
 * 1.根据要传输的对象，指定对应的DataFlavor
 * 2.canImport()判断能否进行传输
 * 3.createTransferable()创建封装了要传输的数据的Transferable对象
 * 4.importData()进行数据传输
 * 5.exportDone()导出数据之后，从原树的数据模型中把移动了的节点删除
 * 
 */

