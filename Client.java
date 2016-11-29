
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

//ActionListener�� KeyListener�� ��ӹ޴´�. 
public class Client extends JFrame implements ActionListener,KeyListener {
// �ڵ� ������ ctrl+shift+o
	
   //Login GUI ����
   final ImageIcon logo_img = new ImageIcon("a.png");
   private JFrame Login_GUI = new JFrame();
   private JPanel Login_Pane = new JPanel();
   private JPanel logo_pane = new JPanel(){
	   public void paintComponent(Graphics g){//logo_pane�� �̹������
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
   final ImageIcon main_img = new ImageIcon("b.png");
   final ImageIcon chat_img = new ImageIcon("c.png");
   private JPanel contentPane = new JPanel(){//ContentPane�� �̹������
	   public void paintComponent(Graphics g){
			  g.drawImage(main_img.getImage(), 0, 0, null);
			  setOpaque(false);
			  super.paintComponent(g);
		  };
   };
   private JTextField message_tf;//ä�ù� ��ȭ
   private JButton notesend_btn = new JButton("����������");
   private JButton joinroom_btn = new JButton("ä�ù�����");
   private JButton createroom_btn = new JButton("�游���");
   private JButton send_btn = new JButton("����");
   private JButton exit_btn = new JButton("������");
   
   private JList User_list = new JList();
   private JList Room_list = new JList();
   
   private JTextArea Chat_area = new JTextArea();//ä�ù� ��ȭȭ��
   JScrollPane scrollPane = new JScrollPane(Chat_area){
   public void paintComponent(Graphics g){//paintComponent�� �̹������
	  g.drawImage(chat_img.getImage(), 0, 0, null);
	  setOpaque(false);
	  super.paintComponent(g);
   }  
};
   
   //��Ʈ��ũ�� ���� �ڿ� ����

   private Socket socket;
   private String ip="";// �� ��ȣ�� �ڱ��ڽ�
   private int port;
   private String id="";//�г���
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;
  
   //�׿� ������
   Vector user_list = new Vector();//User ���� ����
   Vector room_list = new Vector();//Room ���� ����
   StringTokenizer st;
   
   private String My_Room; // ���� ���� ������ �� �̸�
 
   
   
   Client()//client������
   {
	  super("LAGS_CAHT");
      Login_init();//Login GUI���� �޼ҵ�
      Main_init();//Main GUI���� �޼ҵ�
      start();//�̺�Ʈ������ ���� �żҵ�
     
   }
   private void start()//�̺�Ʈ������ ���� �żҵ�
   {//��ư 6�� �׼� ������
      login_btn.addActionListener(this);
      notesend_btn.addActionListener(this);
      joinroom_btn.addActionListener(this);
      createroom_btn.addActionListener(this);
      send_btn.addActionListener(this);
      exit_btn.addActionListener(this);
    //�ؽ�Ʈ�ʵ� 1�� Ű ������
      message_tf.addKeyListener(this);
   }
   
   private void Main_init()//Main GUI���� �޼ҵ�
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 580, 455);
      setResizable(false);
     
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      JLabel lbNewlabel = new JLabel("�� ü �� �� ��");
      lbNewlabel.setBounds(12, 10, 86, 15);
      contentPane.add(lbNewlabel);
      
      JScrollPane scrollPane_2 = new JScrollPane();
      scrollPane_2.setBounds(12, 32, 109, 117);
      contentPane.add(scrollPane_2);
      
      scrollPane_2.setViewportView(User_list);
      
      
      notesend_btn.setBounds(12, 159, 109, 23);
      contentPane.add(notesend_btn);
      
      JLabel lblNewLabel_1 = new JLabel("ä�ù���");
      lblNewLabel_1.setBounds(12, 192, 97, 15);
      contentPane.add(lblNewLabel_1);
      
      JScrollPane scrollPane_1 = new JScrollPane();
      scrollPane_1.setBounds(12, 213, 109, 135);
      contentPane.add(scrollPane_1);
      
      scrollPane_1.setViewportView(Room_list);
    
   
      joinroom_btn.setBounds(12, 386, 109, 23);
      contentPane.add(joinroom_btn);
      
      createroom_btn.setBounds(12, 358, 109, 23);
      contentPane.add(createroom_btn);

      scrollPane.setBounds(133, 29, 418, 347);         
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      contentPane.add(scrollPane);
      
      Chat_area.setBackground(null);
      Chat_area.setOpaque(false);
      
      scrollPane.setBackground(null);   
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      
      scrollPane.setViewportView(Chat_area);
      Chat_area.setEditable(false);
      
      
      message_tf = new JTextField();
      message_tf.setBounds(133, 387, 279, 21);
      contentPane.add(message_tf);
      message_tf.setColumns(10);
      message_tf.setEnabled(false);
      
      send_btn.setBounds(414, 386, 63, 23);
      contentPane.add(send_btn);
      send_btn.setEnabled(false);
      
      exit_btn.setBounds(479, 386, 80, 23);
      contentPane.add(exit_btn);
      exit_btn.setEnabled(false);
      
      
      this.setVisible(false);
   }
   
   
   private void Login_init()//Login GUI���� �޼ҵ�
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
      
      
      login_btn.setBounds(27, 294, 176, 23);
      Login_Pane.add(login_btn);
      
      Login_GUI.setVisible(true); 
   }
   
   private void Network()
   {
   
      try {
         socket = new Socket(ip,port);//���ϻ����Ͽ� ip�� port�� ����
         
         if(socket != null)//���������� ������ ����Ǿ������
         {
            Connection();//���� ���� �� Main GUI�� �Ѿ�� �޼ҵ�
         }
      } catch (UnknownHostException e) {//ȣ��Ʈ�� �Һи��� ���
         
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {//IO������ ���
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      }
      
   }
   
   private void Connection() // �������� �޼ҵ� ����κ�
   {
      try{//Stream ����
      
      is = socket.getInputStream();
      dis = new DataInputStream(is);
      
      os = socket.getOutputStream();
      dos = new DataOutputStream(os);
      }
      catch(IOException e)
      {
    	  JOptionPane.showMessageDialog(null,"���� ����","�˸�",JOptionPane.INFORMATION_MESSAGE);
      } // Stream ���� ��
      
      
      this.setVisible(true); // Main GUIǥ��
      this.Login_GUI.setVisible(false);//Login GUI�����
      

      // ó�� ���ӽÿ� ID ����
      send_message(id);
      
      // User_list ���� ����� �߰�
      user_list.add(id);
      User_list.setListData(user_list);
      
      Thread th = new Thread(new Runnable() {//������ ����
	
    	@Override
		public void run() {//������ ����
		 
    		while(true) //���α׷� ���������� ��� ����
    		{
			
    			try {
    				String msg = dis.readUTF(); // �޼������� 
				
    				System.out.println("�����κ��� ���ŵ� �޼��� : "+msg);
				
    				inmessage(msg);//�޽��� ó��
				
    			} catch (IOException e) {//IO������ ��� stream�� ���� ����
			
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
	   th.start();//������ ����
	 
		
   
   }
   
   private void inmessage(String str) //�����κ��� ������ �޼��� ó��
   {
	   StringTokenizer st = new StringTokenizer(str, "/");
	   //str�޽����� �Ľ��Ͽ� ��ū(/) �и�
	
	   String protocol = st.nextToken();//str���� ù��°/ ������ ���ڿ�
	   String Message = st.nextToken();//str���� �ι�°/ ������ ���ڿ�
	   
	   System.out.println("�������� :" +protocol);
	   System.out.println("���� :"+Message);
	 
	   if(protocol.equals("NewUser")) // ���ο� ������
	   {
		   user_list.add(Message);
		   User_list.setListData(user_list);
		   // AWT List add();
	   }
	   
	   else if(protocol.equals("OldUser"))//���� ������
	   {
		   user_list.add(Message);	  
		   User_list.setListData(user_list);
	   }
	   else if(protocol.equals("Note"))//�������� ��
	   {
		   String note = st.nextToken();//str���� ����°/ ������ ���ڿ�

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
	   else if(protocol.equals("Chatting"))//ä�ù濡�� ��ȭ �ְ���� ��
	   {
		   String msg = st.nextToken();//str���� ����°/ ������ ���ڿ�
		   
		   Chat_area.append(Message+" : "+msg+"\n");
		   System.out.println(Chat_area.getText());
		   scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	   }
	   else if(protocol.equals("OldRoom"))//������ �ִ� ��
	   {
		   room_list.add(Message);
		   Room_list.setListData(room_list);
	   }
	   else if(protocol.equals("JoinRoom"))//�濡 �� ��
	   {
		   My_Room = Message;
		   message_tf.setEnabled(true);
		   send_btn.setEnabled(true);
		   joinroom_btn.setEnabled(false);
		   createroom_btn.setEnabled(false);
		   exit_btn.setEnabled(true);
		   
		   JOptionPane.showMessageDialog(null,"ä�ù濡 �����߽��ϴ�","�˸�",JOptionPane.INFORMATION_MESSAGE);
	   }
	   else if(protocol.equals("User_out"))//�����ڰ� ���� ��
	   {
		   user_list.remove(Message);
		   User_list.setListData(user_list);
	   }
	   else if(protocol.equals("Chat_area_Clear"))//ä�ù� ��ȭ Clear
	   {
		   Chat_area.removeAll();
	   }
	   else if(protocol.equals("Exiting"))//ä�ù� ���� ��
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
   
   
   public static void main(String[] args) {//����
   
      new Client();//client��ü ����

   }

   @Override
   public void actionPerformed(ActionEvent e) {//�׼��̺�Ʈ ����
      // TODO Auto-generated method stub
      
      if(e.getSource()==login_btn)//login_btn������ ��
      {
         System.out.println("�α��ι�ư");
         
         if(ip_tf.getText().length()==0)//ip�� �Է����� �ʾ��� ��
         {
        	 ip_tf.setText("IP�� �Է����ּ���");
        	 ip_tf.requestFocus();
         }
         else if(port_tf.getText().length()==0)//port�� �Է����� �ʾ��� ��
         {
        	 port_tf.setText("Port��ȣ�� �Է����ּ���");
        	 port_tf.requestFocus();
         }
         else if(id_tf.getText().length()==0)//id�� �Է����� �ʾ��� ��
         {
        	 id_tf.setText("ID�� �Է����ּ���");
        	 id_tf.requestFocus();
         }
         else//���� ���� ��
         {
        	 ip = ip_tf.getText().trim(); //trim�� ������� �����ϰ� �Է��� �Ȱɷ� �����ϰ� �ϴ°� , ip�� �޴°�
         
        	 port = Integer.parseInt(port_tf.getText().trim());//int������ ����ȯ
         
        	 id = id_tf.getText().trim(); //id�޾ƿ��� �κ�
         
        	 Network();//������ ���������� �����ϱ� ���� �޼ҵ�
         }
      }
      else if(e.getSource()==notesend_btn)//notesend_btn�� ������ ��
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
      else if(e.getSource()==joinroom_btn)//joinroom_btn�� ������ ��
      {
    	 String JoinRoom = (String)Room_list.getSelectedValue();
    	 
    	 send_message("JoinRoom/"+JoinRoom); 
    	  
         System.out.println("��������ưŬ��");
      }
      else if(e.getSource()==createroom_btn)//createroom_btn�� ������ ��
      {
    	 String roomname = JOptionPane.showInputDialog("�� �̸�");
    	 if(!(roomname == null))
    	 {
    		 send_message("CreateRoom/"+roomname);
    	 }
    	
         System.out.println("�游����ưŬ��");
      }
      else if(e.getSource()==send_btn)//send_btn�� ������ ��
      {
    	  if(message_tf.getText() == null) // �ؽ�Ʈ�Է� ���ϰ� �����ϸ� ���ߴ����� �ذ�
  		{
  			String msg = message_tf.getText();
  			msg = " ";
  			send_message("Chatting/"+My_Room+"/"+msg);
  			message_tf.setText(" ");
  			message_tf.requestFocus();
  		}
  		else if(!(message_tf.getText() == null))//�ؽ�Ʈ �Է��Ͽ� ����
  		{
  			send_message("Chatting/"+My_Room+"/"+message_tf.getText());
  			message_tf.setText(" ");
  	   	 	message_tf.requestFocus();
  		}
    	 
    	 // Chatting + ���̸� + ����
    	System.out.println("���۹�ư");
    	 
      }
      else if(e.getSource()==exit_btn)//exit_btn�� ������ ��
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
	
	if(e.getKeyCode()==10)//�Է°��� ������ ��
	{
		if(message_tf.getText() == null)//�޽��� �Է¾��ϰ� �����ϸ� ���ߴ� ���� �ذ�
		{
			String msg = message_tf.getText();
			msg = " ";
			send_message("Chatting/"+My_Room+"/"+msg);
			message_tf.setText(" ");
			message_tf.requestFocus();
		}
		else if(!(message_tf.getText() == null))//�޽��� �Է��ϰ� ����
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