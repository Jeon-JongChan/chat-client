
package 채팅클라이언트;


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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame implements ActionListener,KeyListener {
// 자동 재정의 ctrl+shift+o
	
   //Login GUI 변수
  
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
   private JTextField ip_tf; //ip 텍스트필드
   private JTextField port_tf; //port 텍스트필드
   private JTextField id_tf; //id 텍스트필드
   private JButton login_btn = new JButton("입  장");
   
   //=============User pw 필드, 회원가입 버튼 생성========================
   // author : 재혁, date : 2016.11.28 
   private JPasswordField pw_tf;
   private JButton join_btn = new JButton("가 입");
   //===============================================================
   
   //Main GUI 변수
   final ImageIcon main_img = new ImageIcon("C:\\chat\\main_sheet.jpg");
   final ImageIcon chat_img = new ImageIcon("C:\\chat\\mainchat_sheet.jpg");
   private JPanel contentPane = new JPanel(){
	   public void paintComponent(Graphics g){
			  g.drawImage(main_img.getImage(), 0, 0, null);
			  setOpaque(false);
			  super.paintComponent(g);
		  };
   };
   private JTextField message_tf;
   private JButton notesend_btn = new JButton("쪽지보내기");
   private JButton joinroom_btn = new JButton("채팅방참여");
   private JButton createroom_btn = new JButton("방만들기");
   private JButton send_btn = new JButton("전송");
   private JButton exit_btn = new JButton("나가기");
   
   private JList User_list = new JList();
   private JList Room_list = new JList();
   
   private JTextArea Chat_area = new JTextArea();
   JScrollPane scrollPane = new JScrollPane(Chat_area){
   public void paintComponent(Graphics g){
	  g.drawImage(chat_img.getImage(), 0, 0, null);
	  setOpaque(false);
	  super.paintComponent(g);
   }  
};
   
   //네트워크를 위한 자원 변수

   private Socket socket;
   private String ip="";// 이 번호는 자기자신
   private int port;
   private String id="";
   
   //=================sendMassage string 변수 생성=================
   //   author : 재혁, date : 2016.11.28
   private String sendMassge="";
   //====================================================
   
   private InputStream is;
   private OutputStream os;
   private DataInputStream dis;
   private DataOutputStream dos;
  
   //그외 변수들
   Vector user_list = new Vector();
   Vector room_list = new Vector();
   StringTokenizer st;
   
   private String My_Room; // 내가 현재 접속한 방 이름
 
   
   
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
      
      //=================회원가입 버튼  Listener 객체 생성================================
      // join_btn 버튼의  Listener 객체를 셋팅하기 위해 addActionListener() 함수 사용
      //   author : 재혁, date : 2016.11.28
      join_btn.addActionListener(this);
      //========================================================================
      
      notesend_btn.addActionListener(this);
      joinroom_btn.addActionListener(this);
      createroom_btn.addActionListener(this);
      send_btn.addActionListener(this);
      exit_btn.addActionListener(this);
    
      message_tf.addKeyListener(this);
   }
   
   private void Main_init()
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 580, 455);
      setResizable(false);
     
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      JLabel lbNewlabel = new JLabel("전 체 접 속 자");
      lbNewlabel.setBounds(12, 10, 86, 15);
      contentPane.add(lbNewlabel);
      
      JScrollPane scrollPane_2 = new JScrollPane();
      scrollPane_2.setBounds(12, 32, 109, 117);
      contentPane.add(scrollPane_2);
      
      scrollPane_2.setViewportView(User_list);
      
      
      notesend_btn.setBounds(12, 159, 109, 23);
      contentPane.add(notesend_btn);
      
      JLabel lblNewLabel_1 = new JLabel("채팅방목록");
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
      
      JLabel lblNewLabel_3 = new JLabel("User PW");
      lblNewLabel_3.setBounds(27, 267, 57, 15);
      Login_Pane.add(lblNewLabel_3);
      
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
      
      //================= User pw필드 입력창 크기 설정=================
      //   author : 재혁, date : 2016.11.28
      pw_tf = new JPasswordField();
      pw_tf.setBounds(105, 267, 100, 21);
      Login_Pane.add(pw_tf);
      pw_tf.setColumns(10);
      //=========================================================
      
      //================= 회원가입 버튼 크기 설정=======================
      //   author : 재혁, date : 2016.11.28
      join_btn.setBounds(27, 294, 76, 23);
      Login_Pane.add(join_btn);
      //=========================================================
      
      login_btn.setBounds(117, 294, 76, 23);
      Login_Pane.add(login_btn);
      
      Login_GUI.setVisible(true); 
   }
   
   private void Network()
   {
   
      try {
         socket = new Socket(ip,port);
         
         if(socket != null)//정상적으로 소켓이 연결되었을 경우
         {
            Connection();
         }
      } catch (UnknownHostException e) {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      }
      
   }
   
 //================= 회원가입시 따로 처리하는 함수 =====================================================
 //        author : 재혁, date : 2016.11.28
   private void NetworkJoin()
   {
   
      try {
         socket = new Socket(ip,port);
         
         if(socket != null)//정상적으로 소켓이 연결되었을 경우
         {
        	ConnectionJoin();
         }
      } catch (UnknownHostException e) {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      }
      
   }
  //==========================================================================================
   
  //================= 회원정보 true, false 체크하는 함수 ==============================================
  //        author : 재혁, date : 2016.11.28
   private boolean readCheck(){
	   try {
			String check = dis.readUTF();
			System.out.println("서버의 메세지 " + check);
			if( check.equals("false") )
				return false;
			else 
				return true;
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}
	   
	   return true;
   }
   //=========================================================================================
   
   private void Connection() // 실제적인 메소드 연결부분
   {
      try{
      
      is = socket.getInputStream();
      dis = new DataInputStream(is);
      
      os = socket.getOutputStream();
      dos = new DataOutputStream(os);
      }
      catch(IOException e)
      {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      } // Stream 설정 끝
      
      
      // 처음 접속시에 ID 전송
      send_message(sendMassge);
      
      
      this.setVisible(true); // main UI표시
      this.Login_GUI.setVisible(false);
 //================= ID,PW 오류 (false) 처리 부분 ========================================================================
 //          author : 재혁, date : 2016.11.28
      if ( !readCheck() ){
    	  JOptionPane.showMessageDialog(null,"아이디 혹은 패스워드가 잘못 됐습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
    	  System.exit(0);
      }
 //============================================================================================================
      
      // User_list 에서 사용자 추가
      user_list.add(id);
      User_list.setListData(user_list);
      
      Thread th = new Thread(new Runnable() {
	
    	@Override
		public void run() {
		 
    		while(true) 
    		{
			
    			try {
    				String msg = dis.readUTF(); // 메세지수신 
				
    				System.out.println("서버로부터 수신된 메세지 : "+msg);
				
    				inmessage(msg);
				
    			} catch (IOException e) {
			
    				try{
    				os.close();
    				is.close();
    				dos.close();
    				dis.close();
    				socket.close();
    				JOptionPane.showMessageDialog(null,"서버와 접속 끊어짐","알림",JOptionPane.INFORMATION_MESSAGE);
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
   
   //========================= 회원가입시 따로 처리하는 부분 ===============================================
   //                   author : 재혁, date : 2016.11.28
   private void ConnectionJoin() // 가입정보
   {
      try{
 
      is = socket.getInputStream();
      dis = new DataInputStream(is);
      
      os = socket.getOutputStream();
      dos = new DataOutputStream(os);
      
      // 처음 접속시에 ID 전송
      send_message(sendMassge);
      
      dis.close();
      dos.close();
      }
      catch(IOException e)
      {
    	  JOptionPane.showMessageDialog(null,"연결 실패","알림",JOptionPane.INFORMATION_MESSAGE);
      } // Stream 설정 끝
  //===============================================================================================  
		
   
   }
   
   private void inmessage(String str) //서버로부터 들어오는 모든 메세지
   {
	   StringTokenizer st = new StringTokenizer(str, "/");
	 
	
	   String protocol = st.nextToken();
	   String Message = st.nextToken();
	   
	   System.out.println("프로토콜 :" +protocol);
	   System.out.println("내용 :"+Message);
	 
	   if(protocol.equals("NewUser")) // 새로운 접속자
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

		   System.out.println(Message+"사용자로부터 온 쪽지"+note);
		   
		   JOptionPane.showMessageDialog
		   (null,note,Message+"님으로 부터 쪽지",JOptionPane.CLOSED_OPTION);
	   }

	   else if(protocol.equals("CreateRoom")) // 방을 만들었을때
	   {
		   My_Room = Message;
		   message_tf.setEnabled(true);
		   send_btn.setEnabled(true);
		   joinroom_btn.setEnabled(false);
		   createroom_btn.setEnabled(false);
		   exit_btn.setEnabled(true);
		   
	   }
	   else if(protocol.equals("CreateRoomFail")) // 방만들기 실패했을 경우
	   {
		   JOptionPane.showMessageDialog(null,"같은 이름의 방이 존재 합니다","알림",JOptionPane.ERROR_MESSAGE);
	   }
	   else if(protocol.equals("New_Room")) // 새로운 방을 만들었을때
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
		   
		   JOptionPane.showMessageDialog(null,"채팅방에 입장했습니다","알림",JOptionPane.INFORMATION_MESSAGE);
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
   
   private void send_message(String str)//서버에게 메세지를 보내는 메소드
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
         System.out.println("로그인버튼");
         
         if(ip_tf.getText().length()==0)
         {
        	 ip_tf.setText("IP를 입력해주세요");
        	 ip_tf.requestFocus();
         }
         else if(port_tf.getText().length()==0)
         {
        	 port_tf.setText("Port번호를 입력해주세요");
        	 port_tf.requestFocus();
         }
         else if(id_tf.getText().length()==0)
         {
        	 id_tf.setText("ID를 입력해주세요");
        	 id_tf.requestFocus();
         }
         else
         {
        	 ip = ip_tf.getText().trim(); //trim은 빈공간을 제외하고 입력이 된걸로 가능하게 하는것 , ip를 받는곳
        	 port = Integer.parseInt(port_tf.getText().trim());//int형으로 형변환
        	 id = id_tf.getText().trim(); //id받아오는 부분
        	 
//================= sendMassge에 id, pw필드 정보 포함하여 전송 =====================================
//              author : 재혁, date : 2016.11.28       	 
        	 sendMassge = id +  ":"+pw_tf.getText().trim(); // id : pw 같이 전송
//==========================================================================================
        	 
        	 Network();
         }
      }
      
//================= sendMassge에 id, pw필드 정보 포함하여 전송 =========================================
//                 author : 재혁, date : 2016.11.28      
      else if(e.getSource()==join_btn)
      {
         System.out.println("가입 버튼 클릭");
       
         
         if(id_tf.getText().length()==0)
         {
        	 id_tf.setText("ID를 입력해주세요");
        	 id_tf.requestFocus();
         }
         
         ip = ip_tf.getText().trim(); //trim은 빈공간을 제외하고 입력이 된 걸로 가능하게 하는 것 , ip를 받는곳
    	 port = Integer.parseInt(port_tf.getText().trim());//int형으로 형변환
     
    	 id = id_tf.getText().trim(); //id받아오는 부분
    	 
    	 /*    sendMassge가 처음 전송 하는 패킷인데...
    	       Join:이 붙은 패킷일 경우에
    	       Server에서 회원가입으로 간주함                */
    	 
    	 sendMassge = "Join:"+id +  ":"+pw_tf.getText().trim(); // id : pw 같이 전송 
    	 NetworkJoin();
    	 id_tf.setText("");
    	 pw_tf.setText("");
    	 
    	 JOptionPane.showMessageDialog(null,"회원가입 완료","알림",JOptionPane.INFORMATION_MESSAGE);
      }
//=============================================================================================
      
      else if(e.getSource()==joinroom_btn)
      {
    	 String JoinRoom = (String)Room_list.getSelectedValue();
    	 
    	 send_message("JoinRoom/"+JoinRoom); 
    	  
         System.out.println("방참여버튼클릭");
      }
      
      else if(e.getSource()==notesend_btn)
      {
         System.out.println("쪽지 보내기 버튼 클릭");
         String user = (String)User_list.getSelectedValue();
         String note = JOptionPane.showInputDialog("보낼메세지");
         
         if(note!=null)
         {
        	 send_message("Note/"+user+"/"+note);
        	 // Note/User2/나는 User1이야
        	 
         }
         System.out.println("받는 사람 : "+user+"| 보낼 내용 :"+note);
         
      }
      else if(e.getSource()==joinroom_btn)
      {
    	 String JoinRoom = (String)Room_list.getSelectedValue();
    	 
    	 send_message("JoinRoom/"+JoinRoom); 
    	  
         System.out.println("방참여버튼클릭");
      }
      else if(e.getSource()==createroom_btn)
      {
    	 String roomname = JOptionPane.showInputDialog("방 이름");
    	 if(!(roomname == null))
    	 {
    		 send_message("CreateRoom/"+roomname);
    	 }
    	
         System.out.println("방만들기버튼클릭");
      }
      else if(e.getSource()==send_btn)
      {
    	  if(message_tf.getText() == null) // 텍스트입력 안하고 전송하면 멈추는현상 해결
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
    	 
    	 // Chatting + 방이름 + 내용
    	

         System.out.println("전송버튼");
    	 
      }
      else if(e.getSource()==exit_btn)
      {
    	  System.out.println("나가기 버튼 클릭");
    	  send_message("Exiting/"+My_Room);
    	 
      }
   }
@Override
public void keyPressed(KeyEvent e) { // 눌렀을 때
	// TODO Auto-generated method stub
	
}
@Override
public void keyReleased(KeyEvent e) { // 눌렀다땠을 때
	
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
			message_tf.setText(" "); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
	   	 	message_tf.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
		}
	}
	// TODO Auto-generated method stub
	
}
@Override
public void keyTyped(KeyEvent e) { // 타이핑했을 때
	// TODO Auto-generated method stub
	
}


}