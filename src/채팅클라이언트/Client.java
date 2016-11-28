
package ä��Ŭ���̾�Ʈ;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import java.awt.Font;

public class Client extends JFrame implements ActionListener,KeyListener {
// �ڵ� ������ ctrl+shift+o
	
   //Login GUI ����
  
   final ImageIcon logo_img = new ImageIcon("C:\\chat\\login_logo.jpg");
   private JFrame Login_GUI = new JFrame();
   private JPanel Login_Pane = new JPanel();
   private JPanel logo_pane = new JPanel(){
	   public void paintComponent(Graphics g){
		   g.drawImage(logo_img.getImage(),0,0,null);
		   setOpaque(false);
		   super.paintComponent(g);
	   };
   }; 
   private JTextField ip_tf; //ip �ؽ�Ʈ�ʵ�
   private JTextField port_tf; //port �ؽ�Ʈ�ʵ�
   private JTextField id_tf; //id �ؽ�Ʈ�ʵ�
   private JButton login_btn = new JButton("��  ��");
   
   
   //Main GUI ����
   ImageIcon main_img = new ImageIcon("C:\\chat\\main_sheet.jpg"); // ä��â ��� �ҷ�����
   final ImageIcon chat_img = new ImageIcon("C:\\chat\\mainchat_sheet.jpg"); // ä�� ���� ��� �ҷ�����
  
   private JPanel contentPane = new JPanel(){
	   public void paintComponent(Graphics g){
			  g.drawImage(main_img.getImage(), 0, 0, null); // ä��â ���� ���� ���
			  setOpaque(false);
			  super.paintComponent(g);
		  };
   };
   
   private JTextField message_tf;
   private JButton notesend_btn = new JButton("����������");
   private JButton joinroom_btn = new JButton("ä�ù�����");
   private JButton createroom_btn = new JButton("�游���");
   private JButton send_btn = new JButton("����");
   private JButton exit_btn = new JButton("������");
   //======================= ���ȭ�� ��ư ���� ===============
   private JButton backgnd1_btn = new JButton("3");
   private JButton backgnd2_btn = new JButton("2");
   private JButton backgnd3_btn = new JButton("1");
   //=======================================================

   private JList User_list = new JList();
   private JList Room_list = new JList();
   
   private JTextArea Chat_area = new JTextArea();
   JScrollPane scrollPane = new JScrollPane(){
   public void paintComponent(Graphics g){
	  g.drawImage(chat_img.getImage(), 0, 0, null); // ä�� ���� ���� ���
	  setOpaque(false);
	  super.paintComponent(g);
   }  
};
   
   //��Ʈ��ũ�� ���� �ڿ� ����

   private Socket socket;
   private String ip="";// �� ��ȣ�� �ڱ��ڽ�
   private int port;
   private String id="";
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;
  
   //�׿� ������
   Vector user_list = new Vector();
   Vector room_list = new Vector();
   StringTokenizer st;
   
   private String My_Room; // ���� ���� ������ �� �̸�
 
   
   
   Client()
   {
	  super("LAGS_CAHT");
      Login_init();
      Main_init();
      start();
     
   }
   private void start()
   {
      login_btn.addActionListener(this);
      notesend_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      notesend_btn.setBackground(Color.WHITE);
      notesend_btn.addActionListener(this);
      joinroom_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      joinroom_btn.setBackground(Color.WHITE);
      joinroom_btn.addActionListener(this);
      createroom_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      createroom_btn.setBackground(Color.WHITE);
      createroom_btn.addActionListener(this);
      send_btn.addActionListener(this);
      exit_btn.addActionListener(this);
      backgnd1_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      backgnd1_btn.setBackground(Color.WHITE);
      //================ ��� �̹��� ��ư ���� Ȯ��=================
      backgnd1_btn.addActionListener(this);
      backgnd2_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      backgnd2_btn.setBackground(Color.WHITE);
      backgnd2_btn.addActionListener(this);
      backgnd3_btn.setFont(new Font("HY����", Font.PLAIN, 10));
      backgnd3_btn.setBackground(Color.WHITE);
      backgnd3_btn.addActionListener(this);
      //=======================================================
    
      message_tf.addKeyListener(this);
   }
   
   private void Main_init()
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 1462, 926);
      setResizable(false);
      contentPane.setBackground(Color.WHITE);
     
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      JLabel lbNewlabel = new JLabel("�� ü �� �� ��");
      lbNewlabel.setFont(new Font("HY����", Font.PLAIN, 12));
      lbNewlabel.setBounds(124, 37, 86, 15);
      contentPane.add(lbNewlabel);
      
      
      notesend_btn.setBounds(113, 124, 109, 23);
      contentPane.add(notesend_btn);
      
      JLabel lblNewLabel_1 = new JLabel("ä�ù���");
      lblNewLabel_1.setFont(new Font("HY����", Font.PLAIN, 12));
      lblNewLabel_1.setBounds(388, 37, 97, 15);
      contentPane.add(lblNewLabel_1);
    
   
      joinroom_btn.setBounds(431, 124, 109, 23);
      contentPane.add(joinroom_btn);
      
      createroom_btn.setBounds(313, 124, 109, 23);
      contentPane.add(createroom_btn);

      scrollPane.setBounds(54, 157, 1390, 699);         
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      contentPane.add(scrollPane);
      
      scrollPane.setBackground(Color.WHITE);   
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      
      
      message_tf = new JTextField();
      message_tf.setBounds(64, 866, 1233, 21);
      contentPane.add(message_tf);
      message_tf.setColumns(10);
      message_tf.setEnabled(false);
      
      send_btn.setBounds(1299, 865, 63, 23);
      contentPane.add(send_btn);
      send_btn.setEnabled(false);
      
      exit_btn.setBounds(1364, 865, 80, 23);
      contentPane.add(exit_btn);
      exit_btn.setEnabled(false);
      
      
    //================= ��� �̹��� ���� ��ư ��ġ����, ��ư�߰�==========
      backgnd1_btn.setBounds(1362, 124, 54, 23); 
      contentPane.add(backgnd1_btn);		  
      
      backgnd2_btn.setBounds(1297, 124, 54, 23);
      contentPane.add(backgnd2_btn);
       
      backgnd3_btn.setBounds(1229, 124, 54, 23);
      contentPane.add(backgnd3_btn);
      Chat_area.setBounds(54, 157, 1390, 699);
      contentPane.add(Chat_area);
      Chat_area.setDisabledTextColor(new Color(72, 61, 139));
      
      Chat_area.setBackground(Color.WHITE);
      Chat_area.setOpaque(false);
      Chat_area.setEditable(false);
      User_list.setFont(new Font("HY����", Font.PLAIN, 12));
      User_list.setBounds(124, 62, 86, 52);
      contentPane.add(User_list);
      Room_list.setFont(new Font("HY����", Font.PLAIN, 12));
      Room_list.setBounds(365, 56, 109, 52);
      contentPane.add(Room_list);
      //==========================================================
      
      
      this.setVisible(false);
   }
   
   
   private void Login_init()
   {
	 
	   
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Login_GUI.setBounds(100, 100, 226, 361);
      Login_GUI.setResizable(false);
      
      Login_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
      Login_GUI.setContentPane(Login_Pane);
      Login_Pane.setLayout(null);
      
      logo_pane.setBounds(20, 10, 186, 108);
      Login_Pane.add(logo_pane);
      
      JLabel lblNewLabel = new JLabel("Server IP");
      lblNewLabel.setBounds(27, 150, 57, 15);
      Login_Pane.add(lblNewLabel);
      
      JLabel lblNewLabel_1 = new JLabel("Server Port");
      lblNewLabel_1.setBounds(27, 193, 73, 15);
      Login_Pane.add(lblNewLabel_1);
      
      JLabel lblNewLabel_2 = new JLabel("User ID");
      lblNewLabel_2.setBounds(27, 237, 57, 15);
      Login_Pane.add(lblNewLabel_2);
      
      ip_tf = new JTextField();
      ip_tf.setBounds(105, 147, 100, 21);
      Login_Pane.add(ip_tf);
      ip_tf.setColumns(10);
      
      port_tf = new JTextField();
      port_tf.setBounds(105, 190, 100, 21);
      Login_Pane.add(port_tf);
      port_tf.setColumns(10);
      
      id_tf = new JTextField();
      id_tf.setBounds(105, 237, 100, 21);
      Login_Pane.add(id_tf);
      id_tf.setColumns(10);
      
      
      login_btn.setBounds(27, 294, 176, 23); // '����'��ư ��ġ ����
      Login_Pane.add(login_btn);
      
      Login_GUI.setVisible(true); 
   }
   
   private void Network()
   {
   
      try {
         socket = new Socket(ip,port);
         
         if(socket != null)//���������� ������ ���ܵǾ������
         {
            Connection();
         }
      } catch (UnknownHostException e) {
         
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      }
      
   }
   
   private void Connection() // �������� �޼ҵ� ����κ�
   {
      try{
      
      is = socket.getInputStream();
      dis = new DataInputStream(is);
      
      os = socket.getOutputStream();
      dos = new DataOutputStream(os);
      }
      catch(IOException e)
      {
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      } // Stream ���� ��
      
      
      this.setVisible(true); // main UIǥ��
      this.Login_GUI.setVisible(false);
      

      // ó�� ���ӽÿ� ID ����
      send_message(id);
      
      // User_list ���� ����� �߰�
      user_list.add(id);
      User_list.setListData(user_list);
      
      Thread th = new Thread(new Runnable() {
	
    	@Override
		public void run() {
		 
    		while(true) 
    		{
			
    			try {
    				String msg = dis.readUTF(); // �޼������� 
				
    				System.out.println("�����κ��� ���ŵ� �޼��� : "+msg);
				
    				inmessage(msg);
				
    			} catch (IOException e) {
			
    				try{
    				os.close();
    				is.close();
    				dos.close();
    				dis.close();
    				socket.close();
    				JOptionPane.showMessageDialog(null,"������ ���� ������","�˸�",JOptionPane.INFORMATION_MESSAGE);
    				}
    				catch(IOException e1){
    					
    				}
    				break;
    				
    			} 
			
					
    		}
		
		
    	}
      });
	   th.start();
	 
		
   
   }
   
   private void inmessage(String str) //�����κ��� ������ ��� �޼���
   {
	   StringTokenizer st = new StringTokenizer(str, "/");
	 
	
	   String protocol = st.nextToken();
	   String Message = st.nextToken();
	   
	   System.out.println("�������� :" +protocol);
	   System.out.println("���� :"+Message);
	 
	   if(protocol.equals("NewUser")) // ���ο� ������
	   {
		   user_list.add(Message);
		   User_list.setListData(user_list);
		   // AWT List add();
	   }
	   
	   else if(protocol.equals("OldUser"))
	   {
		   user_list.add(Message);	  
		   User_list.setListData(user_list);
	   }
	   else if(protocol.equals("Note"))
	   {
		   String note = st.nextToken();

		   System.out.println(Message+"����ڷκ��� �� ����"+note);
		   
		   JOptionPane.showMessageDialog
		   (null,note,Message+"������ ���� ����",JOptionPane.CLOSED_OPTION);
	   }

	   else if(protocol.equals("CreateRoom")) // ���� ���������
	   {
		   My_Room = Message;
		   message_tf.setEnabled(true);
		   send_btn.setEnabled(true);
		   joinroom_btn.setEnabled(false);
		   createroom_btn.setEnabled(false);
		   exit_btn.setEnabled(true);
		   
	   }
	   else if(protocol.equals("CreateRoomFail")) // �游��� �������� ���
	   {
		   JOptionPane.showMessageDialog(null,"���� �̸��� ���� ���� �մϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
	   }
	   else if(protocol.equals("New_Room")) // ���ο� ���� ���������
	   {
		   room_list.add(Message);
		   Room_list.setListData(room_list);   
	   }
	   else if(protocol.equals("Chatting"))
	   {
		   String msg = st.nextToken();
		   
		   Chat_area.append(Message+" : "+msg+"\n");
		   scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	   }
	   else if(protocol.equals("OldRoom"))
	   {
		   room_list.add(Message);
		   Room_list.setListData(room_list);
	   }
	   else if(protocol.equals("JoinRoom"))
	   {
		   My_Room = Message;
		   message_tf.setEnabled(true);
		   send_btn.setEnabled(true);
		   joinroom_btn.setEnabled(false);
		   createroom_btn.setEnabled(false);
		   exit_btn.setEnabled(true);
		   
		   JOptionPane.showMessageDialog(null,"ä�ù濡 �����߽��ϴ�","�˸�",JOptionPane.INFORMATION_MESSAGE);
	   }
	   else if(protocol.equals("User_out"))
	   {
		   user_list.remove(Message);
		   User_list.setListData(user_list);
	   }
	   else if(protocol.equals("Chat_area_Clear"))
	   {
		   Chat_area.removeAll();
	   }
	   else if(protocol.equals("Exiting"))
	   {

		   
		   message_tf.setEnabled(false);
		   send_btn.setEnabled(false);
		   joinroom_btn.setEnabled(true);
		   createroom_btn.setEnabled(true);
		   exit_btn.setEnabled(false);
		   
	   }
	   
   }
   
   private void send_message(String str)//�������� �޼����� ������ �޼ҵ�
   { 
      try {
         dos.writeUTF(str);
      } catch (IOException e) {
         
         e.printStackTrace();
      }
        
   }
   
   
   public static void main(String[] args) {
   
      new Client();

   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      
      if(e.getSource()==login_btn)
      {
         System.out.println("�α��ι�ư");
         
         if(ip_tf.getText().length()==0)
         {
        	 ip_tf.setText("IP�� �Է����ּ���");
        	 ip_tf.requestFocus();
         }
         else if(port_tf.getText().length()==0)
         {
        	 port_tf.setText("Port��ȣ�� �Է����ּ���");
        	 port_tf.requestFocus();
         }
         else if(id_tf.getText().length()==0)
         {
        	 id_tf.setText("ID�� �Է����ּ���");
        	 id_tf.requestFocus();
         }
         else
         {
        	 ip = ip_tf.getText().trim(); //trim�� ������� �����ϰ� �Է��� �Ȱɷ� �����ϰ� �ϴ°� , ip�� �޴°�
         
        	 port = Integer.parseInt(port_tf.getText().trim());//int������ ����ȯ
         
        	 id = id_tf.getText().trim(); //id�޾ƿ��� �κ�
         
        	 Network();
         }
      }
      
      // ====================== ��� �̹��� ���� ��ư Ŭ�� �� ===========================
      else if(e.getSource()==backgnd3_btn)  
      {
    	  main_img=new ImageIcon("C:\\chat\\3.jpg");
    	  contentPane.repaint();
      }
      else if(e.getSource()==backgnd2_btn)
      {
    	  main_img=new ImageIcon("C:\\chat\\2.jpg");
    	  contentPane.repaint();
      }
      else if(e.getSource()==backgnd1_btn)
      {
    	  main_img=new ImageIcon("C:\\chat\\excel.jpg");
    	  contentPane.repaint();
      }
      //================================================================
      
      else if(e.getSource()==notesend_btn)
      {
         System.out.println("���� ������ ��ư Ŭ��");
         String user = (String)User_list.getSelectedValue();
         String note = JOptionPane.showInputDialog("�����޼���");
         
         if(note!=null)
         {
        	 send_message("Note/"+user+"/"+note);
        	 // Note/User2/���� User1�̾�
        	 
         }
         System.out.println("�޴� ��� : "+user+"| ���� ���� :"+note);
         
      }
      else if(e.getSource()==joinroom_btn)
      {
    	 String JoinRoom = (String)Room_list.getSelectedValue();
    	 
    	 send_message("JoinRoom/"+JoinRoom); 
    	  
         System.out.println("��������ưŬ��");
      }
      else if(e.getSource()==createroom_btn)
      {
    	 String roomname = JOptionPane.showInputDialog("�� �̸�");
    	 if(!(roomname == null))
    	 {
    		 send_message("CreateRoom/"+roomname);
    	 }
    	
         System.out.println("�游����ưŬ��");
      }
      else if(e.getSource()==send_btn)
      {
    	  if(message_tf.getText() == null) // �ؽ�Ʈ�Է� ���ϰ� �����ϸ� ���ߴ����� �ذ�
  		{
  			String msg = message_tf.getText();
  			msg = " ";
  			send_message("Chatting/"+My_Room+"/"+msg);
  			message_tf.setText(" ");
  			message_tf.requestFocus();
  		}
  		else if(!(message_tf.getText() == null))
  		{
  			send_message("Chatting/"+My_Room+"/"+message_tf.getText());
  			message_tf.setText(" ");
  	   	 	message_tf.requestFocus();
  		}
    	 
    	 // Chatting + ���̸� + ����
    	

         System.out.println("���۹�ư");
    	 
      }
      else if(e.getSource()==exit_btn)
      {
    	  System.out.println("������ ��ư Ŭ��");
    	  send_message("Exiting/"+My_Room);
    	 
      }
   }
@Override
public void keyPressed(KeyEvent e) { // ������ ��
	// TODO Auto-generated method stub
	
}
@Override
public void keyReleased(KeyEvent e) { // �����ٶ��� ��
	
	if(e.getKeyCode()==10)
	{
		if(message_tf.getText() == null)
		{
			String msg = message_tf.getText();
			msg = " ";
			send_message("Chatting/"+My_Room+"/"+msg);
			message_tf.setText(" ");
			message_tf.requestFocus();
		}
		else if(!(message_tf.getText() == null))
		{
			send_message("Chatting/"+My_Room+"/"+message_tf.getText());
			message_tf.setText(" "); // �޼����� ������ ���� �޼��� ����â�� ����.
	   	 	message_tf.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
		}
	}
	// TODO Auto-generated method stub
	
}
@Override
public void keyTyped(KeyEvent e) { // Ÿ�������� ��
	// TODO Auto-generated method stub
	
}
}