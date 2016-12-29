package com.bn.jm;

import java.lang.AssertionError;//(*^__^*) ������������ʶһ�£�
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.tpicPath;
import static com.bn.core.Constant.picPath;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.winIcon;
import static com.bn.core.Constant.xwglxt;
import static com.bn.core.Constant.xwgl;
import static com.bn.core.Constant.xwxz;
import static com.bn.core.Constant.grxwgl;
import static com.bn.core.Constant.shgl;
import static com.bn.core.Constant.lmgl;
import static com.bn.core.Constant.yhqxgl;
import static com.bn.core.Constant.jbqxck;
import static com.bn.core.Constant.jsqxgl;
import static com.bn.core.Constant.ygxxgl;
import static com.bn.core.Constant.grxxgl;
import static com.bn.core.Constant.bmyggl;
import static com.bn.core.Constant.bmgl;
import static com.bn.core.Constant.fbxwck;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.bn.jm.bmgl.BMGLPanel;
import com.bn.jm.fbxwck.FBXWCKPanel;
import com.bn.jm.grxwgl.CKSHPanel;
import com.bn.jm.grxwgl.GRXWGLPanel;
import com.bn.jm.grxwgl.XWXGPanel;
import com.bn.jm.grxxxg.GRXXGLPanel;
import com.bn.jm.jsqxgl.JSQXGLPanel;
import com.bn.jm.jbqxck.JBQXCKPanel;
import com.bn.jm.lmgl.LMGLPanel;
import com.bn.jm.lmgl.XWCKPanel;
import com.bn.jm.shgl.CKPanel;
import com.bn.jm.shgl.SHGLPanel;
import com.bn.jm.shgl.SHPanel;
import com.bn.jm.xwxz.BSXZPanel;
import com.bn.jm.xwxz.Style1Panel;
import com.bn.jm.xwxz.Style2Panel;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.jm.ygxxgl.YGXXGLPanel;
import com.bn.util.BKJTreeCellRenderer;
import com.bn.util.BKJTreeNode;

@SuppressWarnings("serial")
public class MainJFrame extends JFrame {
	public static MainJFrame mf = null;
	private JSplitPane jSplitPane = new JSplitPane();
	private JScrollPane jRightScrollPane = new JScrollPane();
	private JScrollPane jLeftScrollPane = new JScrollPane();
	private JLabel jlRightDef = new JLabel();
	private JPanel jLeftTreePanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			// ���ƽ��� ��ʼ���� ��ʼ��ɫ
			g2.setPaint(new GradientPaint(0, 0, C_START, 0, getHeight(), C_END));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
	};

	private Set<Integer> qxSet = null;
	public String UserId = null;
	String UserName = null;

	public MainJFrame(Set<Integer> qxSet, String userid, String ygxm) {
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jSplitPane.setDividerLocation(170);// ��ߴ�С
		jSplitPane.setLeftComponent(jLeftScrollPane);
		jSplitPane.setRightComponent(jRightScrollPane);

		// ����ͼƬ
		ImageIcon imageicon = new ImageIcon(picPath + "rightMain.jpg");
		Image image = imageicon.getImage().getScaledInstance(
				SCREEN_WIDTH - 170, SCREEN_HEIGHT, Image.SCALE_SMOOTH);
		jlRightDef.setIcon(new ImageIcon(image));
		jlRightDef.setPreferredSize(new Dimension(SCREEN_WIDTH - 200,
				SCREEN_HEIGHT - 70));
		jRightScrollPane.setViewportView(jlRightDef);
		jRightScrollPane.setMinimumSize(new Dimension(SCREEN_WIDTH - 170,
				SCREEN_HEIGHT));
		this.qxSet = qxSet;
		this.UserId = userid;
		this.UserName = ygxm;
		initMainTree();
		this.add(jSplitPane);

		// =====================����������===================
		this.setIconImage(winIcon);
		this.setTitle("���Ź�����ϵͳ");
		this.setSize((int) (SCREEN_WIDTH * 0.8), (int) (SCREEN_HEIGHT * 0.75));
		this.setLocation((int) (SCREEN_WIDTH - (SCREEN_WIDTH * 0.8)) / 2,
				(int) (SCREEN_HEIGHT - (SCREEN_HEIGHT * 0.75)) / 2);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);// ���
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// ��ҳ������
	private JBQXCKPanel jbqxckPanel = null;// ����Ȩ�޲鿴
	private JSQXGLPanel jsqxglPanel = null;// ��ɫȨ�޹���
	private YGXXGLPanel ygxxglPanel = null;// Ա����Ϣ����
	private BMGLPanel bmglPanel = null;// Ա����Ϣ����
	private GRXXGLPanel grxxgPanel = null;// Ա����Ϣ����
	private XWXZPanel xwxzPanel = null;// ��������
	private GRXWGLPanel grxwglPanel = null;// �������Ź���
	private XWXGPanel xwxgPanel = null;// �����޸�
	private SHGLPanel shglPanel = null;// ��˹���
	private SHPanel shPanel = null;// ��˽���
	private LMGLPanel lmglPanel = null;// ��˽���
	private FBXWCKPanel fbxwckPanel = null;// �������Ų鿴����
	private CKPanel ckPanel = null;// ������Ų鿴����
	private CKSHPanel ckshPanel = null;// �鿴���������˼�¼����
	private BSXZPanel bsxzPanel = null;// ��ʽѡ�����
	private XWCKPanel xwckPanel = null;// ���Ų鿴����

	// =========================��ʼ�������湦����================================================================================
	public void initMainTree() {
		BKJTreeNode root = new BKJTreeNode("���Ź���ϵͳ", new ImageIcon(tpicPath
				+ "root.png"), xwglxt);

		BKJTreeNode node1 = new BKJTreeNode("���Ź���", new ImageIcon(tpicPath
				+ "node.png"), xwgl);
		BKJTreeNode node11 = new BKJTreeNode("��������", new ImageIcon(tpicPath
				+ "xwxz.png"), xwxz);
		BKJTreeNode node12 = new BKJTreeNode("�������Ź���", new ImageIcon(tpicPath
				+ "grxwgl.png"), grxwgl);
		BKJTreeNode node13 = new BKJTreeNode("�������Ų鿴", new ImageIcon(tpicPath
				+ "fbxwck.png"), fbxwck);
		root.add(node1);
		node1.add(node11);
		node1.add(node12);
		node1.add(node13);

		BKJTreeNode node2 = new BKJTreeNode("��˹���", new ImageIcon(tpicPath
				+ "shjl.png"), shgl);
		root.add(node2);

		BKJTreeNode node3 = new BKJTreeNode("��Ŀ����", new ImageIcon(tpicPath
				+ "lmgl.png"), lmgl);
		root.add(node3);

		BKJTreeNode node4 = new BKJTreeNode("�û�Ȩ�޹���", new ImageIcon(tpicPath
				+ "node.png"), yhqxgl);
		BKJTreeNode node41 = new BKJTreeNode("����Ȩ�޲鿴", new ImageIcon(tpicPath
				+ "jbqxck.png"), jbqxck);
		BKJTreeNode node42 = new BKJTreeNode("��ɫȨ�޹���", new ImageIcon(tpicPath
				+ "jsqxgl.png"), jsqxgl);
		root.add(node4);
		node4.add(node41);
		node4.add(node42);

		BKJTreeNode node5 = new BKJTreeNode("����Ա������", new ImageIcon(tpicPath
				+ "node.png"), bmyggl);
		BKJTreeNode node51 = new BKJTreeNode("���Ź���", new ImageIcon(tpicPath
				+ "bmgl.png"), bmgl);
		BKJTreeNode node52 = new BKJTreeNode("Ա����Ϣ����", new ImageIcon(tpicPath
				+ "ygxxgl.png"), ygxxgl);
		root.add(node5);
		node5.add(node51);
		node5.add(node52);

		BKJTreeNode node6 = new BKJTreeNode("������Ϣ����", new ImageIcon(tpicPath
				+ "grxxgl.png"), grxxgl);
		root.add(node6);

		// ����๦���������û�Ȩ�޽��м���
		jcGNS(this.qxSet, root);

		DefaultTreeModel dtm = new DefaultTreeModel(root);
		final JTree jTree = new JTree(dtm);
		jTree.setOpaque(false);
		jTree.setBounds(0, 0, 170, SCREEN_HEIGHT);
		jLeftTreePanel.setLayout(null);
		jLeftTreePanel.add(jTree);
		jLeftTreePanel.setPreferredSize(new Dimension(150, 500));
		jLeftScrollPane.setViewportView(jLeftTreePanel);
		jTree.setCellRenderer(new BKJTreeCellRenderer());// �Զ���ڵ����
		jTree.setEditable(false);
		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath = e.getNewLeadSelectionPath();
				if (treePath != null) {
					BKJTreeNode currNode = (BKJTreeNode) treePath
							.getLastPathComponent();
					int currId = currNode.getId();
					switch (currId) {
					case xwxz:
						gotoBSXZ();
						break;
						
					case grxwgl:
						gotoGRXWGL();
						break;
						
					case fbxwck:
						gotoFBXWCK();
						break;

					case shgl:
						gotoSHGL();
						break;
					// ===================1

					case lmgl:
						gotoLMGL();
						break;

					// ===================2
					case jbqxck:
						gotoJBQXCK();
						break;
					case jsqxgl:
						gotoJSQXGL();
						break;

					// ======================3
					case bmgl:
						gotoBMGL();
						break;

					case ygxxgl:
						gotoYGXXGL();
						break;

					// ======================4
					case grxxgl:
						gotoGRXXXG(UserId);
						break;

					default:
						jRightScrollPane.setViewportView(jlRightDef);
					}
				}
			}
		});
	}

	// ����Ȩ�޲鿴
	protected void gotoJBQXCK() {
		if (jbqxckPanel == null) {
			jbqxckPanel = new JBQXCKPanel();
		}
		jbqxckPanel.setPreferredSize(new Dimension(550, 650));
		jRightScrollPane.setViewportView(jbqxckPanel);

		dataGeted = false;
		new Thread() {
			public void run() {
				jbqxckPanel.flushDataJSQX();
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// ��ɫȨ�޹���
	protected void gotoJSQXGL() {
		if (jsqxglPanel == null) {
			jsqxglPanel = new JSQXGLPanel();
		}
		jsqxglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(jsqxglPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				jsqxglPanel.flushDataJS();
				jsqxglPanel.flushDataJSQX(1);
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// Ա����Ϣ����
	protected void gotoYGXXGL() {
		if (ygxxglPanel == null) {
			ygxxglPanel = new YGXXGLPanel();
		}
		ygxxglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(ygxxglPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				ygxxglPanel.flushDataBM();
				ygxxglPanel.flushData(ygxxglPanel.lzyf);
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// ���Ź���
	public void gotoBMGL() {
		if (bmglPanel == null) {
			bmglPanel = new BMGLPanel();
		}
		bmglPanel.setPreferredSize(new Dimension(400, 500));
		jRightScrollPane.setViewportView(bmglPanel);
		bmglPanel.flushData();
	}

	// ������Ϣ�޸�
	protected void gotoGRXXXG(String UserId) {
		if (grxxgPanel == null) {
			grxxgPanel = new GRXXGLPanel(UserId);
		}
		grxxgPanel.setPreferredSize(new Dimension(550, 650));
		jRightScrollPane.setViewportView(grxxgPanel);
	}

	// ��ʽѡ��
	protected void gotoBSXZ() {
		if (bsxzPanel == null) {
			bsxzPanel = new BSXZPanel(UserId,this);
		}
		bsxzPanel.setPreferredSize(new Dimension(550, 650));
		jRightScrollPane.setViewportView(bsxzPanel);
	}

	// ��������
	public void gotoXWXZ(int BSid) {
		if (xwxzPanel == null) {
			xwxzPanel = new XWXZPanel(UserId,BSid);
			
		}else
		{
			xwxzPanel.BSid=BSid;
			xwxzPanel.setDefault();
		}
		xwxzPanel.setSytle();
		xwxzPanel.setPreferredSize(new Dimension(550, 650));
		jRightScrollPane.setViewportView(xwxzPanel);
	}

	// �������Ź���
	public void gotoGRXWGL() {
		if (grxwglPanel == null) {
			grxwglPanel = new GRXWGLPanel(this,UserId);
		}
		grxwglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(grxwglPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				grxwglPanel.flushData();
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// �鿴���������˼�¼�����ڴӸ������Ź�����棬��ת����˼�¼����ķ�����
	public void gotoCKSH(final String xwid) {
		if (ckshPanel == null) {
			ckshPanel = new CKSHPanel(this);
		}
		ckshPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(ckshPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				ckshPanel.flushData(xwid);
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// �޸����ţ����ڴӸ������Ź�����棬��ת�������޸Ľ���ķ�����
	public void gotoXWXG(final String xwid) {
		if (xwxgPanel == null) {
			xwxgPanel = new XWXGPanel(this,UserId);
		}
		xwxgPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(xwxgPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				xwxgPanel.flushData(xwid);
				dataGeted = true;
				new Thread() 
				{
					public void run() {
						xwxgPanel.flushPics();
					}
				}.start();
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}
	
	// ���Ų鿴�����ڴ���Ŀ������棬��ת�����Ų鿴���桢�����ߣ��ӷ������Ž�����ת�����Ų鿴����ķ�����
	public void gotoXWCK(final String xwid,boolean backLm) {
		if (xwckPanel == null) {
			xwckPanel = new XWCKPanel(this);
		}
		xwckPanel.backlm=backLm;
		xwckPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(xwckPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				xwckPanel.flushData(xwid);
				dataGeted = true;
				new Thread() 
				{
					public void run() {
						xwckPanel.flushPics();
					}
				}.start();
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}
	
	// ������Ŀ����
	public void gotoBackLMGL() 
	{
		if (lmglPanel == null) 
		{
			lmglPanel = new LMGLPanel(this);
		}
		lmglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(lmglPanel);
	}
	

	// ���ظ������Ź���
	public void gotoBackGRXWGL(boolean flag) {
		if (grxwglPanel == null) {
			grxwglPanel = new GRXWGLPanel(this,UserId);
		}
		grxwglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(grxwglPanel);
		if (flag == true)// �ύ��˺󷵻�
		{
			dataGeted = false;
			new Thread() {
				public void run() {
					if (grxwglPanel.filter == null) {
						grxwglPanel.flushData();
					} else {
						grxwglPanel.flushDataFilter(grxwglPanel.filter);
					}
					dataGeted = true;
				}
			}.start();
			// �����߳�
			LoginWindow.watchThread();
		}
	}

	// ��˹���
	public void gotoSHGL() {
		if (shglPanel == null) {
			shglPanel = new SHGLPanel(this);
		}
		shglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(shglPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				shglPanel.flushData();
				//shglPanel.flushDataFilter(shglPanel.filter);
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// ������˹���
	public void gotoBackSHGL(boolean flag) {
		if (shglPanel == null) {
			shglPanel = new SHGLPanel(this);
		}
		shglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(shglPanel);
		if (flag == true)// �ύ��˺󷵻�
		{
			dataGeted = false;
			new Thread() {
				public void run() {
					if (shglPanel.filter == null) {
						shglPanel.flushData();
					} else {
						shglPanel.flushDataFilter(shglPanel.filter);
					}
					dataGeted = true;
				}
			}.start();
			// �����߳�
			LoginWindow.watchThread();
		}
	}

	// ������ˣ����ڴ���˹�����棬��ת��������˽���ķ�����
	public void gotoSH(final String shid) {
		if (shPanel == null) {
			shPanel = new SHPanel(this, UserName);
		}
		shPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(shPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				shPanel.flushData(shid);
				dataGeted = true;
				new Thread() 
				{
					public void run() {
						shPanel.flushPics();
					}
				}.start();
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// ���Ų鿴�����ڴ���˹�����棬��ת�����Ų鿴����ķ�����
	public void gotoCK(final String shid) {
		if (ckPanel == null) {
			ckPanel = new CKPanel(this);
		}
		ckPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(ckPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				ckPanel.flushData(shid);
				dataGeted = true;
				new Thread() 
				{
					public void run() {
						ckPanel.flushPics();
					}
				}.start();
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// ��Ŀ����
	protected void gotoLMGL() {
		if (lmglPanel == null) {
			lmglPanel = new LMGLPanel(this);
		}
		lmglPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(lmglPanel);

		dataGeted = false;
		new Thread() {
			public void run() {
				lmglPanel.flushDataLM();
				lmglPanel.flushDataXW();
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}

	// �������Ų鿴
	public void gotoFBXWCK() {
		if (fbxwckPanel == null) {
			fbxwckPanel = new FBXWCKPanel(this);
		}
		fbxwckPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(fbxwckPanel);
		dataGeted = false;
		new Thread() {
			public void run() {
				fbxwckPanel.flushDataLM();
				dataGeted = true;
			}
		}.start();
		// �����߳�
		LoginWindow.watchThread();
	}
	
	// ���ط������Ų鿴
	public void gotoBackFBXWCK() 
	{
		if (fbxwckPanel == null) 
		{
			fbxwckPanel = new FBXWCKPanel(this);
		}
		fbxwckPanel.setPreferredSize(new Dimension(650, 650));
		jRightScrollPane.setViewportView(fbxwckPanel);
	}

	// ����๦���������û�Ȩ�޽��м���
	public void jcGNS(Set<Integer> idSet, BKJTreeNode root) {
		blGNSBiaoshi(idSet, root);
		blGNSShanchu(root);
	}

	// ����������,ɾ����Ҫ�ڵ�
	private void blGNSShanchu(BKJTreeNode root) {
		if (!root.isLeaf()) {
			@SuppressWarnings("unchecked")
			Enumeration<BKJTreeNode> clist = (Enumeration<BKJTreeNode>) root
					.children();
			List<BKJTreeNode> tempList = new ArrayList<BKJTreeNode>();
			while (clist.hasMoreElements()) {
				BKJTreeNode nextNode = clist.nextElement();
				tempList.add(nextNode);
			}

			for (BKJTreeNode nextNode : tempList) {
				blGNSShanchu(nextNode);
			}
		}
		if (root.ybyFlag == false) {
			root.removeFromParent();
		}
	}

	// ��������������ʾ�����ڵ��Ҫ��Ҫ״̬
	private void blGNSBiaoshi(Set<Integer> idSet, BKJTreeNode root) {
		if (idSet.contains(root.getId())) {
			root.ybyFlag = true;

			BKJTreeNode fatherTemp = (BKJTreeNode) root.getParent();
			while (fatherTemp != null) {
				fatherTemp.ybyFlag = true;
				fatherTemp = (BKJTreeNode) fatherTemp.getParent();
			}
		}
		if (!root.isLeaf()) {
			@SuppressWarnings("unchecked")
			Enumeration<BKJTreeNode> clist = (Enumeration<BKJTreeNode>) root
					.children();
			while (clist.hasMoreElements()) {
				BKJTreeNode nextNode = clist.nextElement();
				blGNSBiaoshi(idSet, nextNode);
			}
		}
	}


}
