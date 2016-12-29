package com.bn.jm;
/*
 * 等待对话框
 */
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import static com.bn.core.Constant.*;
@SuppressWarnings("serial")
public class WaitDialog extends JFrame
{
    public WaitDialog(String statusInfo)
    {
    	JProgressBar progressBar = new JProgressBar();
    	JLabel lbStatus = new JLabel(statusInfo);
    	lbStatus.setFont(new Font("宋体", Font.ITALIC, 13));
        progressBar.setIndeterminate(true);
        
        JPanel jp = new JPanel(null)
        {
        	@Override
        	protected void paintComponent(Graphics g) 
        	{
        		Graphics2D g2 = (Graphics2D) g;  
                // 绘制渐变     起始坐标  起始颜色
    	        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
                g2.fillRect(0, 0, getWidth(), getHeight());  
        	}
        };
        this.add(jp);
        jp.setBounds(0, 0, 390, 120);
        jp.add(progressBar);
        jp.add(lbStatus);
        progressBar.setBounds(20, 20, 350, 15);
        lbStatus.setBounds(20, 50, 350, 25);
        
        this.setTitle("请稍后...");
        this.setSize(390, 120);
        this.setLocation((int) (SCREEN_WIDTH -390 ) / 2,   (int) (SCREEN_HEIGHT -120) / 2);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setIconImage(winIcon);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
    
 /*  
    public static void main(String[] args)
    {
    	new WaitDialog("请等待............");
    }
*/
}