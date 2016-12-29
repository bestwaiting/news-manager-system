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
	private JLabel jlTitle=new JLabel("�޸�����");
	private JLabel jlXWBT = new JLabel("���ű���:");
	private JLabel jlSZTP = new JLabel("����ͼƬ:");
	private JLabel jlXWLY = new JLabel("������Դ:");
	private JLabel jlFBSJ = new JLabel("����ʱ��:");
	private JLabel jlXWNR = new JLabel("��������:");
	private JLabel jlXWGS = new JLabel("���Ÿ���:");


	Style1Panel sytle1 = null;
	Style2Panel sytle2 = null;
	Style3Panel sytle3 = null;

	private JTextField jtfXWBT = new JTextField(1);// ���ű���
	private JTextField jtfXWLY = new JTextField(1);// ������Դ
	private DateChooserJButton dateChooser = new DateChooserJButton();// ����ʱ��
	private JTextArea jtaXWGS = new JTextArea();
	private JScrollPane jspXWGS = new JScrollPane(jtaXWGS);// ���Ÿ���
	public JScrollPane jspXWNR = new JScrollPane();
	private JButton jbPic = new JButton("�鿴����ͼƬ");

	

	
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

	

	
	
	private JButton jbBack=new JButton("����");
	private JButton jbSave=new JButton("�����޸�");
	private JButton jbSubmit=new JButton("�ύ���");
	
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
		
		//���ذ�ť
		jbBack.setBounds(780,15,90,30);
		jbBack.setOpaque(false);
		this.add(jbBack);
		
		//����ݸ尴ť
		jbSave.setBounds(900,15,90,30);
		jbSave.setOpaque(false);
		this.add(jbSave);
		
		//�ύ��˰�ť
		jbSubmit.setBounds(1020,15,90,30);
		jbSubmit.setOpaque(false);
		this.add(jbSubmit);	
		
		// �ָ���jtf
		jtfLine.setBounds(20, 60 - 8, 1140, 4);
		jtfLine.setEnabled(false);
		this.add(jtfLine);
		
		// ���ű���
		jlXWBT.setBounds(25 + 755, 80, 100, 20);
		jtfXWBT.setBounds(95 + 755, 80, 310, 20);
		jlXWBT.setFont(jltitle);
		this.add(jlXWBT);
		this.add(jtfXWBT);

		// ���Ÿ���
		jlXWGS.setBounds(780, 110, 100, 20);
		jspXWGS.setBounds(850, 110, 310, 40);
		jtaXWGS.setLineWrap(true);
		jlXWGS.setFont(jltitle);
		this.add(jlXWGS);
		this.add(jspXWGS);

		// ������Դ
		jlXWLY.setBounds(780, 110 + 50, 100, 20);
		jtfXWLY.setBounds(850, 110 + 50, 150, 20);
		jlXWLY.setFont(jltitle);
		this.add(jlXWLY);
		this.add(jtfXWLY);

		// ����ʱ��
		jlFBSJ.setBounds(780, 110 + 30 + 50, 100, 20);
		dateChooser.setBounds(850, 110 + 30 + 50, 150, 20);
		dateChooser.setEnabled(false);
		jlFBSJ.setFont(jltitle);
		this.add(jlFBSJ);
		this.add(dateChooser);

		// ����ͼƬ
		jlSZTP.setBounds(780, 110 + 30 + 30 + 50, 100, 20);
		jbPic.setBounds(850, 110 + 30 + 30 + 50, 150, 22);
		jbPic.setOpaque(false);
		jlSZTP.setFont(jltitle);
		this.add(jlSZTP);
		this.add(jbPic);
		
		// ��������
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
		//�����޸�
		jbSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(inputCheck())
				{
					updata_new(Integer.parseInt(ztid));//����ԭ��״̬

				}	
			}
			
		});
		
		
		//�ύ���
		jbSubmit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(inputCheck())
				{
				    updata_new(1);//1�ύδ���
				    //add_shjl(1);

				}
				
			}
			
		});
		
		//���ذ�ť
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
		
		// ����ͼƬ��ť
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
	
	
	//������֤
	private boolean inputCheck()
	{
		
		if(jtfXWBT.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"���ű��ⲻ��Ϊ�գ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			jtfXWBT.requestFocus();
			return false;
		}
		xwbt=jtfXWBT.getText().trim();
		
		if(jtaXWGS.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"�������߲���Ϊ�գ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			jtaXWGS.requestFocus();
			return false;
		}
		xwgs=jtaXWGS.getText().trim();
		
		if(jtfXWLY.getText().trim().length()<=0)
		{
			JOptionPane.showMessageDialog(XWXGPanel.this,"������Դ����Ϊ�գ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			jtfXWLY.requestFocus();
			return false;
		}
		xwly=jtfXWLY.getText().trim();
			
		//����ʱ��
		fbsj=this.dateChooser.getText();
		
		if(bsid==1)
		{
			if (sytle1.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "�������ݲ���Ϊ�գ�", "��ʾ",
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
				JOptionPane.showMessageDialog(XWXGPanel.this, "���Ų�ͼ��������Ϊ�գ�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic1MS=sytle2.getPic1Decrition();
			//content
			if (sytle2.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "�������ݲ���Ϊ�գ�", "��ʾ",
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
				JOptionPane.showMessageDialog(XWXGPanel.this, "���Ų�ͼ��������Ϊ�գ�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic1MS=sytle3.getPic1Decrition();
			//pic2
			pic2Path=sytle3.getPic2();
			if (sytle3.getPic2Decrition().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "���Ų�ͼ��������Ϊ�գ�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			pic2MS=sytle3.getPic2Decrition();
			//content
			if (sytle3.getContent().trim().length() <= 0) {
				JOptionPane.showMessageDialog(XWXGPanel.this, "�������ݲ���Ϊ�գ�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			xwnr=sytle3.getContent();
			sytle3.clear();
		}
		
		return true;	
	}
	
	
	//��������
	private void updata_new(final int ztid)
	{
		// �����߳�
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
						JOptionPane.showMessageDialog(XWXGPanel.this,"��ϲ�������޸ĳɹ���","��ʾ",JOptionPane.INFORMATION_MESSAGE);
					}else
					{
						JOptionPane.showMessageDialog(XWXGPanel.this,"��ϲ�������ύ�ɹ�,�����ĵȴ���ˣ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
					}
				}
				jtfXWBT.setText(null);
				jtfXWLY.setText(null);
				jtaXWGS.setText(null);
				jbPic.setText("�鿴����ͼƬ");
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
		// �����߳�
		LoginWindow.watchThread();
	}
	
	

	
	
	
	 //�������
	public void getNewById(String xwid)
	{
		//������Ϣ�������
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
	
	//��ʼ�������޸Ľ���
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
	
	
	//���ͼƬ����ط���
	/*
	 * ���ݰ�ʽ��Ϣ������ͬ�������߳̿�ʼ���ͼƬ���ݣ����Ҹ�����ʾ
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
	 * ���ָ��ͼƬ�������´�ͼƬ����ʾ��ͨ�÷�����
	 * 1.Ҫ�ж�ͼƬ����
	 * 2.Ҫ�жϰ�ʽ
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
	
	
	
	//���ָ��ͼƬ��Ϣ
	/*
	 * ���ָ��ͼƬ������ͼƬ������ݱ��浽�˳�Ա������
	 */
	public void getPic(String xwid,int picLX) {
		// ������Ϣ�������
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
		JLabel jlabelTip = new JLabel("��ʾ����ѡ���߱Ƚӽ�5:4��ͼƬ��");
		Image img = null;
		JPanel jpPic = new JPanel() {
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 400, 320, this);
			};
		};
		JButton jbuttonOK = new JButton("ȷ��");
		JButton jbuttonChange = new JButton("�޸�");

		public JdialogShowPic(String path) 
		{
			img = this.getToolkit().getImage(path);
			// �ļ�ѡ����
			jfc.removeChoosableFileFilter(jfc.getChoosableFileFilters()[0]);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPG,JEPGͼƬ�ļ�","jpg", "jpeg"));
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
			this.setTitle("����ͼƬԤ��");
			this.setModal(true);

			jbuttonOK.addActionListener(this);
			jbuttonChange.addActionListener(this);

			this.setVisible(true);
		}
		
		public JdialogShowPic(Image img) 
		{
			// �ļ�ѡ����
			this.img=img;
			jfc.removeChoosableFileFilter(jfc.getChoosableFileFilters()[0]);
			jfc.addChoosableFileFilter(new FileNameExtensionFilter("JPG,JEPGͼƬ�ļ�","jpg", "jpeg"));
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
			this.setTitle("����ͼƬԤ��");
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
        // ���ƽ���     ��ʼ����  ��ʼ��ɫ
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
