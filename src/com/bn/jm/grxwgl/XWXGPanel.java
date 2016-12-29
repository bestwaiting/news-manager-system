package com.bn.jm.grxwgl;

import static com.bn.core.Constant.ADD_NEW;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_PIC;
import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.UPDATE_NEW;
import static com.bn.core.Constant.GET_JS;
import static com.bn.core.Constant.bpicPath;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.jltitle;
import static com.bn.core.Constant.GET_NEW_By_XWID;
import static com.bn.core.Constant.ADD_SHJL_BY_XWID;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.grxxxg.GRXXGLPanel;
import com.bn.jm.jsqxgl.JSQXGLPanel;
import com.bn.jm.jsqxgl.JSTableModel;
import com.bn.jm.xwxz.Style1Panel;
import com.bn.jm.xwxz.Style2Panel;
import com.bn.jm.xwxz.Style3Panel;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.sjb.NewPC;
import com.bn.sjb.NewPC1;
import com.bn.sjb.NewPC2;
import com.bn.sjb.NewPC3;
import com.bn.sjb.PicObject;
import com.bn.util.DateChooserJButton;
import com.bn.util.PicUtils;
import com.bn.util.SocketUtil;
import com.sunking.swing.JDatePicker;

public class XWXGPanel extends JPanel
{
	
	private JTextField jtfLine=new JTextField();
	private JLabel jlTitle=new JLabel("修改新闻");
	private JLabel jlXWBT = new JLabel("新闻标题:");
	private JLabel jlSZTP = new JLabel("标题图片:");
	private JLabel jlXWLY = new JLabel("新闻来源:");
	private JLabel jlFBSJ = new JLabel("发布时间:");
	private JLabel jlXWNR = new JLabel("新闻内容:");
	private JLabel jlXWGS = new JLabel("新闻概述:");


	Style1Panel sytle1 = null;
	Style2Panel sytle2 = null;
	Style3Panel sytle3 = null;

	private JTextField jtfXWBT = new JTextField(1);// 新闻标题
	private JTextField jtfXWLY = new JTextField(1);// 新闻来源
	private DateChooserJButton dateChooser = new DateChooserJButton();// 发布时间
	private JTextArea jtaXWGS = new JTextArea();
	private JScrollPane jspXWGS = new JScrollPane(jtaXWGS);// 新闻概述
	public JScrollPane jspXWNR = new JScrollPane();
	private JButton jbPic = new JButton("查看标题图片");

	

	
	private String xwbt;
	private String xwgs;
	private String xwly;
	private String fbsj;
	private String xwnr;
	private int bsid;
	
	
	byte[] picTitle;
	byte[] pic1;
	byte[] pic2;
	String pic1MS;
	String pic2MS;
	
	private String picTitelPath;
	private String pic1Path;
	private String pic2Path;
	
	private boolean picTitleChanged=false;

	

	
	
	private JButton jbBack=new JButton("返回");
	private JButton jbSave=new JButton("保存修改");
	private JButton jbSubmit=new JButton("提交审核");
	
	String xwid;
	String ztid;
	String[] data;
	MainJFrame mf;
	String ygid;
	
	public XWXGPanel(MainJFrame mf,String userid) 
	{
		this.mf=mf;
		this.ygid=userid;
		this.setLayout(null);
		jlTitle.setBounds(25,15,100,20);
		jlTitle.setFont(subtitle); 
		this.add(jlTitle);
		
		//返回按钮
		jbBack.setBounds(780,15,90,30);
		jbBack.setOpaque(false);
		this.add(jbBack);
		
		//保存草稿按钮
		jbSave.setBounds(900,15,90,30);
		jbSave.setOpaque(false);
		this.add(jbSave);
		
		//提交审核按钮
		jbSubmit.setBounds(1020,15,90,30);
		jbSubmit.setOpaque(false);
		this.add(jbSubmit);	
		
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

		// 发布时间
		jlFBSJ.setBounds(780, 110 + 30 + 50, 100, 20);
		dateChooser.setBounds(850, 110 + 30 + 50, 150, 20);
		dateChooser.setEnabled(false);
		jlFBSJ.setFont(jltitle);
		this.add(jlFBSJ);
		this.add(dateChooser);

		// 设置图片
		jlSZTP.setBounds(780, 110 + 30 + 30 + 50, 100, 20);
		jbPic.setBounds(850, 110 + 30 + 30 + 50, 150, 22);
		jbPic.setOpaque(false);
		jlSZTP.setFont(jltitle);
		this.add(jlSZTP);
		this.add(jbPic);
		
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
		//保存修改
		jbSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(inputCheck())
				{
					updata_new(Integer.parseInt(ztid));//保持原有状态

				}	
			}
			
		});
		
		
		//提交审核
		jbSubmit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(inputCheck())
				{
				    updata_new(1);//1提交未审核
				    //add_shjl(1);

				}
				
			}
			
		});
		
		//返回按钮
		jbBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				picTitleChanged=false;
				pic1Path=null;
				pic2Path=null;
				 mf.gotoBackGRXWGL(false);		
			}
			
		});
		
		// 设置图片按钮
		jbPic.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(picTitleChanged)
				{
					new JdialogShowPic(picTitelPath);
				}else
				{
					new JdialogShowPic(PicUtils.bytesToImage(picTitle));
					//System.out.println("image******************");
				}
			}

		});

		
	}
	
	
	//输入验证
	private boolean inputCheck()
	{
		
		if(jtfXWBT.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"新闻标题不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
			jtfXWBT.requestFocus();
			return false;
		}
		xwbt=jtfXWBT.getText().trim();
		
		if(jtaXWGS.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"新闻作者不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
			jtaXWGS.requestFocus();
			return false;
		}
		xwgs=jtaXWGS.getText().trim();
		
		if(jtfXWLY.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"新闻来源不能为空！","提示",JOptionPane.INFORMATION_MESSAGE);
			jtfXWLY.requestFocus();
			return false;
		}
		xwly=jtfXWLY.getText().trim();
			
		//发布时间
		fbsj=this.dateChooser.getText();
		
		if(bsid==1)
		{
			if (sytle1.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻内容不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			xwnr=sytle1.getContent();
			sytle1.clear();
			
		}else if(bsid==2)
		{
			//pic1
				pic1Path=sytle2.getPic1();
			if (sytle2.getPic1Decrition().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻插图描述不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic1MS=sytle2.getPic1Decrition();
			//content
			if (sytle2.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻内容不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			xwnr=sytle2.getContent();
			sytle2.clear();
			
		}else if(bsid==3)
		{
			//pic1
			pic1Path=sytle3.getPic1();
			if (sytle3.getPic1Decrition().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻插图描述不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic1MS=sytle3.getPic1Decrition();
			//pic2
			pic2Path=sytle3.getPic2();
			if (sytle3.getPic2Decrition().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻插图描述不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic2MS=sytle3.getPic2Decrition();
			//content
			if (sytle3.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "新闻内容不能为空！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			xwnr=sytle3.getContent();
			sytle3.clear();
		}
		
		return true;	
	}
	
	
	//更新新闻
	private void updata_new(final int ztid)
	{
		// 任务线程
		dataGeted = false;
		new Thread() {
			public void run() {
				NewPC newpc=null;
				if(bsid==1)
				{
					//System.out.println("picpaht========"+picTitelPath);
					newpc=new NewPC1(xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid
							,picTitleChanged?PicUtils.getBytePic(picTitelPath):picTitle);
				}else if(bsid==2)
				{
					newpc=new NewPC2(xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid
							,picTitleChanged?PicUtils.getBytePic(picTitelPath):picTitle,
								pic1Path!=null?PicUtils.getBytePic(pic1Path):pic1,pic1MS);
				}else if(bsid==3)
				{
					newpc=new NewPC3(xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid,
							picTitleChanged?PicUtils.getBytePic(picTitelPath):picTitle,
							pic1Path!=null?PicUtils.getBytePic(pic1Path):pic1,
							pic1MS,
							pic2Path!=null?PicUtils.getBytePic(pic2Path):pic2,
							pic2MS);
				}
				System.out.println("**********xwnr"+xwnr);
				String msg=SocketUtil.sendNewObject(newpc,false,xwid);
				dataGeted = true;
				if(msg.equals("ok"))
				{
					if(ztid==2||ztid==0)
					{
						JOptionPane.showMessageDialog(XWXGPanel.this,"恭喜，保存修改成功！","提示",JOptionPane.INFORMATION_MESSAGE);
					}else
					{
						JOptionPane.showMessageDialog(XWXGPanel.this,"恭喜，新闻提交成功,请耐心等待审核！","提示",JOptionPane.INFORMATION_MESSAGE);
					}
				}
				jtfXWBT.setText(null);
				jtfXWLY.setText(null);
				jtaXWGS.setText(null);
				jbPic.setText("查看标题图片");
				picTitelPath=null;
				picTitleChanged=false;
				pic1Path=null;
				pic2Path=null;
				if(ztid==2||ztid==0)
				{
					mf.gotoBackGRXWGL(false);	
				}else
				{
					mf.gotoBackGRXWGL(true);	
				}
			}
		}.start();
		// 监视线程
		LoginWindow.watchThread();
	}
	
	

	
	
	
	 //获得新闻
	public void getNewById(String xwid)
	{
		//发送消息获得数据
		String msg = GET_NEW_By_XWID;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(xwid);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());
		final List<String[]> list = SocketUtil.strToList(result);
		data=list.get(0);
		//xw.xwbt, xw.xwgs, xw.xwly, xw.fbsj, xw.xwnr, xw.ztid ,xw.bsid
	}
	
	//初始化新闻修改界面
	public void flushData(String xwid)
	{
		this.xwid=xwid;
		getNewById(xwid);
		jtfXWBT.setText(data[0]);
		jtaXWGS.setText(data[1]);
		jtfXWLY.setText(data[2]);
		this.dateChooser.setText(data[3]);
		xwnr = data[4];
		ztid=data[5];
		bsid = Integer.parseInt(data[6]);
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
						}
						sytle2.flushContent(xwnr);
						jspXWNR.setViewportView(sytle2);
					} else if (bsid == 3) {
						if (sytle3 == null) {
							sytle3 = new Style3Panel();
							//System.out.println("************"+xwnr);
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
	
	
	class JdialogShowPic extends JDialog implements ActionListener 
	{
		private JFileChooser jfc = new JFileChooser();
		JLabel jlabelTip = new JLabel("提示：请选择宽高比接近5:4的图片！");
		Image img = null;
		JPanel jpPic = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 400, 320, this);
			};
		};
		JButton jbuttonOK = new JButton("确定");
		JButton jbuttonChange = new JButton("修改");

		public JdialogShowPic(String path) 
		{
			img = this.getToolkit().getImage(path);
			// 文件选择器
			jfc.removeChoosableFileFilter(jfc.getChoosableFileFilters()[0]);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPG,JEPG图片文件","jpg", "jpeg"));
			jpPic.setLayout(null);
			this.setLayout(null);
			jlabelTip.setBounds(20, 10, 360, 20);
			this.add(jlabelTip);
			jpPic.setBounds(20, 40, 400, 320);
			this.add(jpPic);
			jbuttonOK.setBounds(20, 370 + 5, 80, 20);
			jbuttonChange.setBounds(120, 370 + 5, 80, 20);
			this.add(jbuttonOK);
			this.add(jbuttonChange);
			this.setSize(445, 440);
			this.setLocation((int) (SCREEN_WIDTH - 230) / 2,
					(int) (SCREEN_HEIGHT - 400) / 2);
			this.setResizable(false);
			this.setTitle("标题图片预览");
			this.setModal(true);

			jbuttonOK.addActionListener(this);
			jbuttonChange.addActionListener(this);

			this.setVisible(true);
		}
		
		public JdialogShowPic(Image img) 
		{
			// 文件选择器
			this.img=img;
			jfc.removeChoosableFileFilter(jfc.getChoosableFileFilters()[0]);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPG,JEPG图片文件","jpg", "jpeg"));
			jpPic.setLayout(null);
			this.setLayout(null);
			jlabelTip.setBounds(20, 10, 360, 20);
			this.add(jlabelTip);
			jpPic.setBounds(20, 40, 400, 320);
			this.add(jpPic);
			jbuttonOK.setBounds(20, 370 + 5, 80, 20);
			jbuttonChange.setBounds(120, 370 + 5, 80, 20);
			this.add(jbuttonOK);
			this.add(jbuttonChange);
			this.setSize(445, 440);
			this.setLocation((int) (SCREEN_WIDTH - 230) / 2,
					(int) (SCREEN_HEIGHT - 400) / 2);
			this.setResizable(false);
			this.setTitle("标题图片预览");
			this.setModal(true);

			jbuttonOK.addActionListener(this);
			jbuttonChange.addActionListener(this);

			this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jbuttonOK) {
				picTitleChanged=true;
				this.dispose();
			} else {
				int result = jfc.showOpenDialog(XWXGPanel.this);
				File pic = jfc.getSelectedFile();
				this.dispose();
				if (pic != null && result == JFileChooser.APPROVE_OPTION) {
					jbPic.setText(pic.getName());
					picTitelPath = pic.getPath();
					new JdialogShowPic(picTitelPath);
				}
			}
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
