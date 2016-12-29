package com.bn.jm.shgl;

import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.ADD_NEW;
import static com.bn.core.Constant.GET_NEW_By_XWID;
import static com.bn.core.Constant.GET_PIC;
import static com.bn.core.Constant.GET_SH_By_SHID;
import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.UPDATE_SHJL;
import static com.bn.core.Constant.bpicPath;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.jltitle;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.grxwgl.XWXGPanel;
import com.bn.jm.grxxxg.GRXXGLPanel;
import com.bn.jm.shgl.CKPanel.JdialogShowPic;
import com.bn.jm.xwxz.Style1Panel;
import com.bn.jm.xwxz.Style2Panel;
import com.bn.jm.xwxz.Style3Panel;
import com.bn.sjb.PicObject;
import com.bn.util.DateChooserJButton;
import com.bn.util.PicUtils;
import com.bn.util.SocketUtil;
import com.sunking.swing.JDatePicker;


public class SHPanel extends JPanel
{
	private JTextField jtfLine = new JTextField();
	private JLabel jlTitle = new JLabel("查看新闻");
	private JLabel jlXWBT = new JLabel("新闻标题:");
	private JLabel jlSZTP = new JLabel("标题图片:");
	private JLabel jlXWLY = new JLabel("新闻来源:");
	private JLabel jlSHRXM=new JLabel("审核人名:");
	private JLabel jlFBSJ = new JLabel("提交时间:");
	private JLabel jlSHSJ = new JLabel("审核时间:");
	private JLabel jlXWNR = new JLabel("新闻内容:");
	private JLabel jlXWGS = new JLabel("新闻概述:");
	private JLabel jlSHYJ = new JLabel("审核意见:");

	Style1Panel sytle1 = null;
	Style2Panel sytle2 = null;
	Style3Panel sytle3 = null;

	private JTextField jtfXWBT = new JTextField(1);// 新闻标题
	private JTextField jtfXWLY = new JTextField(1);// 新闻来源
	private DateChooserJButton dateChooserFB = new DateChooserJButton();// 发布时间
	private DateChooserJButton dateChooserSH = new DateChooserJButton();// 审核时间
	
	private JTextArea jtaXWGS = new JTextArea();
	private JScrollPane jspXWGS = new JScrollPane(jtaXWGS);// 新闻概述
	
	public JScrollPane jspXWNR = new JScrollPane();//新闻内容
	private JButton jbPic = new JButton("查看标题图片");

	private JTextArea jtaSHYJ = new JTextArea();// 审核意见
	JScrollPane jspSHYJ = new JScrollPane(jtaSHYJ);


	private JTextField jtfSHRXM=new JTextField(1);//审核人姓名
	
	String[] jcbStr={"  通过审核","  返回修改","    封杀"	};
	private JComboBox jcb=new JComboBox(jcbStr);
	private JButton jbSubmit=new JButton("审核完毕");
	private JButton jbBack=new JButton("返回");
	
	

	private String shid;
	private String shrxm;
	private String shsj;
	private String shyj;
	private String xwnr;
	private int bsid;
	
	byte[] picTitle;
	byte[] pic1;
	byte[] pic2;
	String pic1MS;
	String pic2MS;
	
	

	

	
	
	
	
	String xwid;
	String ztid="3";
	String[] data;
	MainJFrame mf;
	
	public SHPanel(MainJFrame mf,String UserName) 
	{
        this.mf=mf;
        this.shrxm=UserName;
        
		// 新增新闻标题
		this.setLayout(null);
		jlTitle.setBounds(25, 15, 100, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);

		//审核结果下拉列表
		jcb.setBounds(780,15,90,29);
		jcb.setOpaque(false);
		this.add(jcb);	
		
		//提交审核按钮
		jbSubmit.setBounds(900,15,90,30);
		jbSubmit.setOpaque(false);
		this.add(jbSubmit);	
		
		//返回按钮
		jbBack.setBounds(1020,15,90,30);
		jbBack.setOpaque(false);
		this.add(jbBack);	

		// 分割线jtf
		jtfLine.setBounds(20, 60 - 8, 1140, 4);
		jtfLine.setEnabled(false);
		this.add(jtfLine);

		// 新闻标题
		jlXWBT.setBounds(25 + 755, 80, 100, 20);
		jtfXWBT.setBounds(95 + 755, 80, 310, 20);
		jlXWBT.setFont(jltitle);
		this.add(jlXWBT);
		this.add(jtfXWBT);

		// 新闻概述
		jlXWGS.setBounds(780, 110, 100, 20);
		jspXWGS.setBounds(850, 110, 310, 40);
		jtaXWGS.setLineWrap(true);
		jlXWGS.setFont(jltitle);
		this.add(jlXWGS);
		this.add(jspXWGS);

		// 新闻来源
		jlXWLY.setBounds(780, 110 + 50, 100, 20);
		jtfXWLY.setBounds(850, 110 + 50, 150, 20);
		jlXWLY.setFont(jltitle);
		this.add(jlXWLY);
		this.add(jtfXWLY);
		
        //审核人姓名
        jlSHRXM.setBounds(780,110 + 50+30,100,20);
        jtfSHRXM.setBounds(850,110 + 50+30, 150, 20);
        jlSHRXM.setFont(jltitle); 
        this.add(jlSHRXM);
        this.add(jtfSHRXM);

		// 发布时间
		jlFBSJ.setBounds(780, 110 + 30 + 50+30, 100, 20);
		dateChooserFB.setBounds(850, 110 + 30 + 50+30, 150, 20);
		dateChooserFB.setEnabled(false);
		jlFBSJ.setFont(jltitle);
		this.add(jlFBSJ);
		this.add(dateChooserFB);
		
		// 审核时间
		jlSHSJ.setBounds(780, 110 + 30 + 50+30+30, 100, 20);
		dateChooserSH.setBounds(850, 110 + 30 + 50+30+30, 150, 20);
		dateChooserSH.setEnabled(false);
		jlSHSJ.setFont(jltitle);
		this.add(jlSHSJ);
		this.add(dateChooserSH);

		// 设置图片
		jlSZTP.setBounds(780, 110 + 30 + 30 + 50+30+30, 100, 20);
		jbPic.setBounds(850, 110 + 30 + 30 + 50+30+30, 150, 22);
		jbPic.setOpaque(false);
		jlSZTP.setFont(jltitle);
		this.add(jlSZTP);
		this.add(jbPic);

		// 审核意见
		jlSHYJ.setBounds(780, 110 + 30 + 30 + 30 +30+30+ 50, 100, 20);
		jspSHYJ.setBounds(850, 110 + 30 + 30 + 30 +30+30+ 50, 310, 150);
		jlSHYJ.setFont(jltitle);
		this.add(jlSHYJ);
		this.add(jspSHYJ);

		// 新闻内容
		jlXWNR.setBounds(25, 60, 100, 20);
		jspXWNR.setBounds(25, 80, 710, 600);
		jspXWNR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jspXWNR.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspXWNR.setWheelScrollingEnabled(true);
		jlXWNR.setFont(jltitle);
		this.add(jlXWNR);
		this.add(jspXWNR);       
        
		addButtonListener();
		
	}
	
	private void addButtonListener()
	{
		
		jcb.addItemListener(new ItemListener()
		{
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.DESELECTED)
			     {
					int index=jcb.getSelectedIndex();
					if(index==0)
					{
						ztid="3";
					}else if(index==1)
					{
						ztid="2";
					}else
					{
						ztid="4";
					}	
			   }
		   }
		}
		);
		
		//审核完毕
		jbSubmit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(inputCheck())
				{
					updata_sh(SHPanel.this.ztid);//提交审核
				}
				
			}
		});
		
		//返回
		jbBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
					mf.gotoBackSHGL(false);
			}
		});
		
		// 设置图片按钮
		jbPic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
					new JdialogShowPic(PicUtils.bytesToImage(picTitle));
			}

		});			
	}
	
	//输入验证
	private boolean inputCheck()
	{
		
		if(jtfSHRXM.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(SHPanel.this,"审核人姓名不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
			jtfSHRXM.requestFocus();
			return false;
		}
		shrxm=jtfSHRXM.getText().trim();
		
		
		if(jtaSHYJ.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(SHPanel.this,"审核意见不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		shyj=jtaSHYJ.getText();
		
		//审核时间
		shsj=dateChooserSH.getText();
		return true;	
	}
	
	//初始化新闻修改界面
	public void flushData(String shid)
	{
		// xw.xwbt, xw.xwgs, xw.xwly, xw.fbsj, xw.xwnr, xw.bsid ,shjl.shyj,shjl.xwid
		this.shid = shid;
		getSHById(shid);
		
		jtfXWBT.setText(data[0]);
		jtfSHRXM.setText(shrxm);
		jtaXWGS.setText(data[1]);
		jtfXWLY.setText(data[2]);
		dateChooserFB.setText(data[3].substring(0,19));
		xwnr = data[4];
		bsid = Integer.parseInt(data[5]);
		jtaSHYJ.setText(data[6]);
		xwid = data[7];
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					if (bsid == 1) {
						if (sytle1 == null) {
							sytle1 = new Style1Panel();
						}
						sytle1.flushContent(xwnr);
						jspXWNR.setViewportView(sytle1);
					} else if (bsid == 2) {
						if (sytle2 == null) {
							sytle2 = new Style2Panel();
							sytle2.isListened(false);
						}
						sytle2.flushContent(xwnr);
						jspXWNR.setViewportView(sytle2);
					} else if (bsid == 3) {
						if (sytle3 == null) {
							sytle3 = new Style3Panel();
							sytle3.isListened(false);
						}
						sytle3.flushContent(xwnr);
						jspXWNR.setViewportView(sytle3);
	
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	 //获得审核记录里的新闻先关信息
	public void getSHById(String shid)
	{
		//发送消息获得数据
		String msg = GET_SH_By_SHID;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(shid);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());
		final List<String[]> list = SocketUtil.strToList(result);
		data=list.get(0);
		// xw.xwbt, xw.xwgs, xw.xwly, shjl.tjsj, xw.xwnr, xw.bsid ,shjl.shyj,shjl.xwid
	}
	
	//获得图片的相关方法
	/*
	 * 根据板式信息启动不同个数的线程开始获得图片数据，并且更新显示
	 */
	public void flushPics()
	{
		new Thread() 
		{
			public void run() {
				flushDataPic(0);
			}
		}.start();
		
		if(bsid==2)
		{
			new Thread() 
			{
				public void run() {
					flushDataPic(1);
				}
			}.start();
			
		}else if(bsid==3)
		{
			new Thread() 
			{
				public void run() {
					flushDataPic(1);
				}
			}.start();
			new Thread() 
			{
				public void run() {
					flushDataPic(2);
				}
			}.start();
		}
	}
	
	
	/*
	 * 获得指定图片，并更新此图片的显示（通用方法）
	 * 1.要判断图片类型
	 * 2.要判断板式
	 */
	public void flushDataPic(final int picLX)
	{
		getPic(xwid,picLX);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
				{
					if(picLX==1)
					{
						if(bsid==2)
						{
							sytle2.flushPic1(PicUtils.bytesToImage(pic1), pic1MS);
						}else if(bsid==3)
						{
							sytle3.flushPic1(PicUtils.bytesToImage(pic1), pic1MS);
						}
					}else if(picLX==2)
					{
						sytle3.flushPic2(PicUtils.bytesToImage(pic2), pic2MS);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	
	//获得指定图片信息
	/*
	 * 获得指定图片，并把图片相关数据保存到了成员变量中
	 */
	public void getPic(String xwid,int picLX) {
		// 发送消息获得数据
		String msg = GET_PIC;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(xwid+"<->");
		sb.append(picLX);
		sb.append(msg);
		PicObject pico=SocketUtil.sendAndGetPic(sb.toString());
		if(picLX==0)
		{
			this.picTitle=pico.pic;
		}else if(picLX==1)
		{
			this.pic1=pico.pic;
			this.pic1MS=pico.picMs;
			
		}else if(picLX==2)
		{
			this.pic2=pico.pic;
			this.pic2MS=pico.picMs;
		}
	}
	
	
	
	

	
	
	          //更新新闻
	        private void updata_sh(final String ztid)
	         {
	        	// 任务线程
	    		dataGeted = false;
	        	new Thread() 
				{
					public void run() 
					{
						StringBuilder sb=new StringBuilder();
						sb.append(UPDATE_SHJL);
						sb.append(shid+"<->");
						sb.append(ztid+"<->");
						sb.append(shrxm+"<->");
						sb.append(shsj+"<->");
						sb.append(shyj);
						sb.append(UPDATE_SHJL);
						System.out.println(sb.toString());
						String msg=SocketUtil.sendAndGetMsg(sb.toString());
						dataGeted=true;
						if(msg.equals("ok"))
						{
						    JOptionPane.showMessageDialog(SHPanel.this,"恭喜，审核完毕！","提示",JOptionPane.INFORMATION_MESSAGE);
							jtfXWBT.setText(null);
							jtfSHRXM.setText(null);
							jtaXWGS.setText(null);
							jtfXWLY.setText(null);
							jtaSHYJ.setText(null);
							mf.gotoBackSHGL(true);
						}		
					}
				}.start();
				// 监视线程
				LoginWindow.watchThread();
	         }

	
	
	    	//显示标题图片的JDialog（每次new新的出来）
	    	class JdialogShowPic extends JDialog implements ActionListener {
	    		JLabel jlabelTip = new JLabel("提示：请选择宽高比接近5:4的图片！");
	    		Image img = new ImageIcon(bpicPath+"pic.jpg").getImage();
	    		JPanel jpPic = new JPanel() {
	    			public void paint(Graphics g) {
	    				g.drawImage(img, 0, 0, 400, 320, this);
	    			};
	    		};
	    		JButton jbuttonOK = new JButton("确定");

	    		public JdialogShowPic(Image img) {
	    			if(img!=null)
	    			{
	    				this.img=img;
	    			}
	    			jpPic.setLayout(null);
	    			this.setLayout(null);
	    			jlabelTip.setBounds(20, 10, 360, 20);
	    			this.add(jlabelTip);
	    			jpPic.setBounds(20, 40, 400, 320);
	    			this.add(jpPic);
	    			jbuttonOK.setBounds(20, 370 + 5, 80, 20);
	    			this.add(jbuttonOK);
	    			this.setSize(445, 440);
	    			this.setLocation((int) (SCREEN_WIDTH - 230) / 2,
	    					(int) (SCREEN_HEIGHT - 400) / 2);
	    			this.setResizable(false);
	    			this.setTitle("标题图片预览");
	    			this.setModal(true);
	    			jbuttonOK.addActionListener(this);
	    			this.setVisible(true);
	    		}

	    		@Override
	    		public void actionPerformed(ActionEvent e) 
	    		{
	    			this.dispose();	
	    		}
	    	
	    	}
	
	        
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;  
        // 绘制渐变     起始坐标  起始颜色
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
