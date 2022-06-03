import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Point;
import javax.swing.SwingConstants;
import javax.swing.JList;

/**
 * 
 */

/**
 * @author pyf19
 *
 */



public class client {
	JFrame frame;


	client window = null;
	JPanel panel_session;
	JPanel panel1;	
	JPanel panel_register;
	JPanel panel_login;
	JPanel panel_server_client;
	
	JButton btnNewButton_1;
	JPasswordField password_Field_login;
	JPasswordField passwordField_register;
	JPasswordField passwordField_register_confirm;
	
	JTextArea textArea_session_recv;
	JTextArea textArea_sessionsinfo_recv_1;
	JTextArea textArea_usersinfo_recv_1;
	//information
	Socket socket = null;
	String user_name = null;
	String password = null;
	int session_id = -1;
	private JTextField text_newsession_name;
	private JTextField text_session_number;
	
	public void initialize1() {
		try {
	        socket = new Socket("127.0.0.1", 9999);
	        System.out.println("已经连上服务器了");
	        JOptionPane.showMessageDialog(null, "与服务器连接成功", "提示",JOptionPane.WARNING_MESSAGE);
	    } catch (Exception e) {
	            e.printStackTrace();
	    }
	  
	}
	
		
	public static void main(String[] args) {
		new client();
	}
	
	public client() {
		initialize1();
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void send_to_server(String msg) {
		PrintWriter out;
		synchronized(socket) {
			try {
				out = new PrintWriter(socket.getOutputStream());
				out.println(msg);
	            out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 648, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		panel_session = new JPanel();
		panel_session.setBackground(new Color(250, 250, 210));
		panel_session.setBounds(0, 0, 634, 363);
		frame.getContentPane().add(panel_session);
		panel_session.setLayout(null);
		
		JTextField textField_msgsend = new JTextField();
		textField_msgsend.setFont(new Font("宋体", Font.PLAIN, 14));
		textField_msgsend.setForeground(Color.DARK_GRAY);
		textField_msgsend.setToolTipText("\u8BF7\u8F93\u5165\u8981\u53D1\u9001\u7684\u4FE1\u606F");
		textField_msgsend.setBackground(new Color(255, 255, 255));
		textField_msgsend.setBounds(25, 242, 422, 43);
		

		panel_session.add(textField_msgsend);
		textField_msgsend.setColumns(10);
		
		JScrollPane scrollPane_session = new JScrollPane();
		scrollPane_session.setToolTipText("\u6D88\u606F\u6846");
		scrollPane_session.setBounds(25, 34, 561, 161);
		panel_session.add(scrollPane_session);
		
		textArea_session_recv = new JTextArea();
		textArea_session_recv.setBackground(new Color(250, 235, 215));
		textArea_session_recv.setForeground(new Color(0, 0, 128));
		scrollPane_session.setViewportView(textArea_session_recv);
		textArea_session_recv.setFont(new Font("华文楷体", Font.BOLD, 13));
		textArea_session_recv.setEditable(false);
		textArea_session_recv.setLineWrap(true);        //激活自动换行功能 
		textArea_session_recv.setWrapStyleWord(true);            // 激活断行不断字功能
		
		
		JButton Button_session_send = new JButton("\u53D1\u9001");
		Button_session_send.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String msg = textField_msgsend.getText();
				if(msg.length() == 0) {
					JOptionPane.showMessageDialog(null, "发送的内容不能为空", "提示",JOptionPane.WARNING_MESSAGE);
					
				}
				else {
					textField_msgsend.setText("");
					textArea_session_recv.append("\r\n你：" + msg);
					msg = "sessionid=" + session_id + "$" + msg;
					send_to_server(msg);
					
				}
				

				System.out.println("点击发送");
			}
		});
		Button_session_send.setFont(new Font("楷体", Font.BOLD, 17));
		Button_session_send.setForeground(new Color(255, 0, 0));
		
				
				
		
		Button_session_send.setBackground(new Color(127, 255, 212));
		Button_session_send.setBounds(485, 240, 101, 44);
		
		panel_session.add(Button_session_send);
		
		JButton Button_session_send_1 = new JButton("\u9000\u51FA\u4F1A\u8BDD");
		Button_session_send_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			String request_sessions_msg = "$quitsession$id=" + session_id ;
			send_to_server(request_sessions_msg);														
			}
		});
		Button_session_send_1.setForeground(new Color(0, 0, 255));
		Button_session_send_1.setFont(new Font("楷体", Font.BOLD, 17));
		Button_session_send_1.setBackground(new Color(245, 255, 250));
		Button_session_send_1.setBounds(485, 309, 101, 22);
		panel_session.add(Button_session_send_1);
		
		JButton Button_session_send_1_1 = new JButton("\u8FD4\u56DE\u4E0A\u4E00\u9875");
		Button_session_send_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//继续接收消息，但是不显示
				panel_session.setVisible(false);
				panel_server_client.setVisible(true);
			}
		});
		Button_session_send_1_1.setForeground(new Color(127, 255, 212));
		Button_session_send_1_1.setFont(new Font("楷体", Font.BOLD, 17));
		Button_session_send_1_1.setBackground(new Color(248, 248, 255));
		Button_session_send_1_1.setBounds(340, 310, 135, 22);
		panel_session.add(Button_session_send_1_1);
		
		panel_session.setVisible(false);
		
		//
		panel_server_client = new JPanel();
		panel_server_client.setBackground(new Color(255, 255, 153));
		panel_server_client.setBounds(0, 0, 634, 363);
		frame.getContentPane().add(panel_server_client);
		panel_server_client.setLayout(null);
		
		JScrollPane scrollPane_sessions = new JScrollPane();
		scrollPane_sessions.setBounds(10, 22, 435, 139);
		panel_server_client.add(scrollPane_sessions);
		
		textArea_sessionsinfo_recv_1 = new JTextArea();
		scrollPane_sessions.setViewportView(textArea_sessionsinfo_recv_1);
		textArea_sessionsinfo_recv_1.setWrapStyleWord(true);
		textArea_sessionsinfo_recv_1.setLineWrap(true);
		textArea_sessionsinfo_recv_1.setForeground(new Color(0, 0, 128));
		textArea_sessionsinfo_recv_1.setFont(new Font("华文楷体", Font.BOLD, 16));
		textArea_sessionsinfo_recv_1.setEditable(false);
		textArea_sessionsinfo_recv_1.setBackground(new Color(250, 235, 215));

		
		JButton Button_sessionsinfo_send = new JButton("\u67E5\u8BE2\u6240\u6709\u4F1A\u8BDD");
		Button_sessionsinfo_send.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//点击之后返回所有的会话信息

				String request_sessions_msg = "$allsessionsinfo" ;
				send_to_server(request_sessions_msg);
				
			}
		});
		Button_sessionsinfo_send.setBounds(455, 24, 155, 29);
		Button_sessionsinfo_send.setForeground(Color.RED);
		Button_sessionsinfo_send.setFont(new Font("楷体", Font.BOLD, 17));
		Button_sessionsinfo_send.setBackground(new Color(127, 255, 212));
		panel_server_client.add(Button_sessionsinfo_send);
		
		JScrollPane scrollPane_usersinfo = new JScrollPane();
		scrollPane_usersinfo.setBounds(10, 230, 435, 106);
		panel_server_client.add(scrollPane_usersinfo);
		
		textArea_usersinfo_recv_1 = new JTextArea();
		scrollPane_usersinfo.setViewportView(textArea_usersinfo_recv_1);
		textArea_usersinfo_recv_1.setWrapStyleWord(true);
		textArea_usersinfo_recv_1.setLineWrap(true);
		textArea_usersinfo_recv_1.setForeground(new Color(0, 0, 128));
		textArea_usersinfo_recv_1.setFont(new Font("华文楷体", Font.BOLD, 16));
		textArea_usersinfo_recv_1.setEditable(false);
		textArea_usersinfo_recv_1.setBackground(new Color(250, 235, 215));
		
		JButton Button_usersinfo_send_1 = new JButton("\u67E5\u8BE2\u6240\u6709\u7528\u6237");
		Button_usersinfo_send_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//所有用户信息返回
				String request_users_msg = "$allusersinfo" ;
				send_to_server(request_users_msg);

			}
		});
		Button_usersinfo_send_1.setBounds(455, 230, 155, 29);
		Button_usersinfo_send_1.setForeground(Color.RED);
		Button_usersinfo_send_1.setFont(new Font("楷体", Font.BOLD, 17));
		Button_usersinfo_send_1.setBackground(new Color(127, 255, 212));
		panel_server_client.add(Button_usersinfo_send_1);
		
		JButton Button_sessionsinfo_send_2 = new JButton("\u521B\u5EFA\u65B0\u4F1A\u8BDD");
		Button_sessionsinfo_send_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//创建新的会话
				String request_newsession_msg = "$newsession$name=" + text_newsession_name.getText() ;
				send_to_server(request_newsession_msg);
				
			}
		});
		Button_sessionsinfo_send_2.setForeground(Color.RED);
		Button_sessionsinfo_send_2.setFont(new Font("楷体", Font.BOLD, 17));
		Button_sessionsinfo_send_2.setBackground(new Color(127, 255, 212));
		Button_sessionsinfo_send_2.setBounds(151, 181, 137, 29);
		panel_server_client.add(Button_sessionsinfo_send_2);
		
		text_newsession_name = new JTextField();
		text_newsession_name.setFont(new Font("楷体", Font.BOLD, 12));
		text_newsession_name.setForeground(new Color(0, 0, 205));
		text_newsession_name.setToolTipText("\u70B9\u51FB\u8F93\u5165\u65B0\u4F1A\u8BDD\u540D\u79F0");
		text_newsession_name.setBounds(34, 181, 107, 29);
		panel_server_client.add(text_newsession_name);
		text_newsession_name.setColumns(10);
		
		text_session_number = new JTextField();
		text_session_number.setText("\u4F1A\u8BDDID");
		text_session_number.setToolTipText("\u70B9\u51FB\u8F93\u5165\u8981\u8FDB\u5165\u7684\u4F1A\u8BDD\u7684\u7F16\u53F7\uFF0C\u53EA\u80FD\u662F\u6570\u5B57");
		text_session_number.setForeground(new Color(0, 0, 205));
		text_session_number.setFont(new Font("楷体", Font.BOLD, 14));
		text_session_number.setColumns(10);
		text_session_number.setBounds(342, 181, 60, 29);
		panel_server_client.add(text_session_number);
		
		JButton Button_sessionsinfo_send_2_1 = new JButton("\u8FDB\u5165\u8BE5\u4F1A\u8BDD");
		Button_sessionsinfo_send_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//进入该会话
				String id_numbers = text_session_number.getText();
			    String regEx = "[0-9]*";
			    Pattern p = Pattern.compile(regEx);
			    Matcher m = p.matcher(id_numbers);
			    System.out.println(m.matches());

				if(m.matches()) {
					//全是数字
					
					String enter_session_msg = "$entersession$id=" + id_numbers;
					send_to_server(enter_session_msg);
				}
				else {
					//文本框有错误
					JOptionPane.showMessageDialog(null, "只能输入数字", "提示",JOptionPane.WARNING_MESSAGE);
					text_session_number.setText("");
				}
			}
		});
		Button_sessionsinfo_send_2_1.setForeground(Color.RED);
		Button_sessionsinfo_send_2_1.setFont(new Font("楷体", Font.BOLD, 17));
		Button_sessionsinfo_send_2_1.setBackground(new Color(255, 165, 0));
		Button_sessionsinfo_send_2_1.setBounds(417, 181, 137, 29);
		panel_server_client.add(Button_sessionsinfo_send_2_1);
		
		panel_server_client.setVisible(false);
		

		
		panel_register = new JPanel();
		panel_register.setLocation(0, 0);
		panel_register.setSize(634, 363);
		panel_register.setBackground(new Color(240, 255, 240));
		frame.getContentPane().add(panel_register, BorderLayout.CENTER);
		panel_register.setLayout(null);
		
		
		
		passwordField_register = new JPasswordField();
		passwordField_register.setBounds(247, 160, 135, 28);
		panel_register.add(passwordField_register);
		
		JLabel lblNewJgoodiesLabel_register = DefaultComponentFactory.getInstance().createLabel("\u5BC6\u7801");
		lblNewJgoodiesLabel_register.setBounds(165, 160, 33, 28);
		lblNewJgoodiesLabel_register.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewJgoodiesLabel_register.setForeground(new Color(0, 0, 205));
		panel_register.add(lblNewJgoodiesLabel_register);
		
		JLabel lblNewJgoodiesLabel_register_1 = DefaultComponentFactory.getInstance().createLabel("\u7528\u6237\u540D");
		lblNewJgoodiesLabel_register_1.setBounds(158, 106, 50, 28);
		lblNewJgoodiesLabel_register_1.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewJgoodiesLabel_register_1.setForeground(new Color(0, 0, 205));
		panel_register.add(lblNewJgoodiesLabel_register_1);
		
		JFormattedTextField formattedTextField_register_1 = new JFormattedTextField();
		formattedTextField_register_1.setBounds(247, 106, 135, 28);
		panel_register.add(formattedTextField_register_1);
		
		JLabel lblNewJgoodiesLabel_register_2 = DefaultComponentFactory.getInstance().createLabel("\u6CE8\u518C\u9875\u9762");
		lblNewJgoodiesLabel_register_2.setBounds(265, 48, 92, 37);
		lblNewJgoodiesLabel_register_2.setFont(new Font("楷体", Font.BOLD, 20));
		lblNewJgoodiesLabel_register_2.setForeground(new Color(255, 0, 0));
		panel_register.add(lblNewJgoodiesLabel_register_2);
		
		passwordField_register_confirm = new JPasswordField();
		passwordField_register_confirm.setBounds(247, 212, 135, 28);
		panel_register.add(passwordField_register_confirm);
		
		JLabel lblNewJgoodiesLabel_register_3 = DefaultComponentFactory.getInstance().createLabel("\u786E\u8BA4\u5BC6\u7801");
		lblNewJgoodiesLabel_register_3.setBounds(150, 212, 73, 28);
		lblNewJgoodiesLabel_register_3.setForeground(new Color(0, 0, 205));
		lblNewJgoodiesLabel_register_3.setFont(new Font("楷体", Font.BOLD, 16));
		panel_register.add(lblNewJgoodiesLabel_register_3);
		
		JButton Button_register_Register = new JButton("\u6CE8\u518C");
		Button_register_Register.setBounds(267, 266, 90, 28);
		Button_register_Register.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Button_register_Register.setBackground(Color.orange);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Button_register_Register.setBackground(new Color(0, 255, 255));
			}			
			@Override
			public void mouseEntered(MouseEvent e) {
				Button_register_Register.setBackground(Color.orange);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Button_register_Register.setBackground(new Color(0, 255, 255));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				String register_msg = null;
				String u_name =formattedTextField_register_1.getText();
				String u_pwd = passwordField_register.getText();
				String u_pwd_cfm = passwordField_register_confirm.getText();
				if(!u_pwd.equals(u_pwd_cfm)) {
					JOptionPane.showMessageDialog(null, "密码与确认密码不一致", "提示",JOptionPane.WARNING_MESSAGE);
					return ;
				}
				//the same password
				register_msg = "$registernewuser" + "$name=" + u_name + "$password=" + u_pwd ;
				send_to_server(register_msg);
				

			}
		});


		Button_register_Register.setBackground(new Color(0, 255, 255));
		Button_register_Register.setFont(new Font("楷体", Font.BOLD, 16));
		Button_register_Register.setFocusable(false);

		panel_register.add(Button_register_Register);
		
		panel_register.setVisible(false);
		
		//

		
		panel_login = new JPanel();
		panel_login.setBounds(0, 0, 634, 363);
		panel_login.setBackground(new Color(240, 255, 240));
		frame.getContentPane().add(panel_login);
		panel_login.setLayout(null);
		
		
		password_Field_login = new JPasswordField();
		password_Field_login.setBounds(247, 160, 135, 28);
		panel_login.add(password_Field_login);
		
		JLabel lblNewJgoodiesLabel_login = DefaultComponentFactory.getInstance().createLabel("\u5BC6\u7801");
		lblNewJgoodiesLabel_login.setBounds(165, 160, 33, 28);
		lblNewJgoodiesLabel_login.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewJgoodiesLabel_login.setForeground(new Color(0, 0, 205));
		panel_login.add(lblNewJgoodiesLabel_login);
		
		JLabel lblNewJgoodiesLabel_login_1 = DefaultComponentFactory.getInstance().createLabel("\u7528\u6237\u540D");
		lblNewJgoodiesLabel_login_1.setBounds(158, 106, 50, 28);
		lblNewJgoodiesLabel_login_1.setFont(new Font("楷体", Font.BOLD, 16));
		lblNewJgoodiesLabel_login_1.setForeground(new Color(0, 0, 205));
		panel_login.add(lblNewJgoodiesLabel_login_1);
		
		JFormattedTextField username_login_1 = new JFormattedTextField();
		username_login_1.setBounds(247, 106, 135, 28);
		panel_login.add(username_login_1);
		
		JButton Button_login = new JButton("\u767B\u5F55");
		Button_login.setBounds(226, 218, 90, 28);
		Button_login.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Button_login.setBackground(Color.orange);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Button_login.setBackground(new Color(0, 255, 255));
			}			
			@Override
			public void mouseEntered(MouseEvent e) {
				Button_login.setBackground(Color.orange);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Button_login.setBackground(new Color(0, 255, 255));
			}
			
			/**
			 * log in
			 * @param e
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				//点击登录，与服务器进行数据校验
				user_name = username_login_1.getText();
				password = password_Field_login.getText();
				String login_msg = "$login" + user_name + "$password" + password; 
				System.out.println(login_msg);

				send_to_server(login_msg);
				
			}
		});


		Button_login.setBackground(new Color(0, 255, 255));
		Button_login.setFont(new Font("楷体", Font.BOLD, 16));
		Button_login.setFocusable(false);
		panel_login.add(Button_login);
		
		JLabel lblNewJgoodiesLabel_login_2 = DefaultComponentFactory.getInstance().createLabel("\u6B22\u8FCE\u8FDB\u5165\u767B\u5F55\u9875\u9762");
		lblNewJgoodiesLabel_login_2.setBounds(228, 46, 172, 37);
		lblNewJgoodiesLabel_login_2.setFont(new Font("楷体", Font.BOLD, 20));
		lblNewJgoodiesLabel_login_2.setForeground(new Color(255, 0, 0));
		panel_login.add(lblNewJgoodiesLabel_login_2);
		
		JLabel convert_to_rigister_login_3 = DefaultComponentFactory.getInstance().createLabel("\u6CA1\u6709\u8D26\u6237\uFF0C\u70B9\u51FB\u6CE8\u518C");
		convert_to_rigister_login_3.setBounds(339, 219, 151, 28);
		convert_to_rigister_login_3.setForeground(new Color(0, 0, 139));
		convert_to_rigister_login_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//到下一个页面---是注册页面
				
				panel_register.setVisible(true);
				panel_login.setVisible(false);
				frame.setTitle("注册");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				convert_to_rigister_login_3.setText("<html><u>注册</u></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				convert_to_rigister_login_3.setText("<html><u>没有账户，点击注册</u></html>");
			}
		});
		convert_to_rigister_login_3.setFont(new Font("楷体", Font.BOLD, 15));
		panel_login.add(convert_to_rigister_login_3);
				
		client.recvmsg recv_msg = new client.recvmsg(socket);
		Thread recv = new Thread(recv_msg);
	    recv.start();
	    
	}
	
	
	class recvmsg implements Runnable{

		Socket socket;
		
		public recvmsg(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
	            Thread.sleep(1000);
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket
	                    .getInputStream()));
	            //textArea_session_recv.append(in.readLine());
	            while (true) {
	                String response = in.readLine();
	                
	                System.out.println(response);
	                if(response.length() > 0) {
	                	process(response);
	                }
	                
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}

		/**
		 * @param response
		 */
		public void process(String response) {
			System.out.println(response);
			// TODO Auto-generated method stub
			boolean login_success = false;
			boolean receive_success = false;
			if(response.charAt(0) == '$') {//control message
				//$login success/failure
				System.out.println("$");
				if(response.contains("$login")) {//response from login
					//System.out.println("login");
					if(response.equals("$login success")) {//success
						login_success = true;
						System.out.println("chengonglianjie");
						JOptionPane.showMessageDialog(null, "登录成功", "提示",JOptionPane.WARNING_MESSAGE);
						panel_login.setVisible(false);
						panel_server_client.setVisible(true);
					}
					else {//failure
						JOptionPane.showMessageDialog(null, "登录失败，请重新登录", "提示",JOptionPane.WARNING_MESSAGE);
					}
				}
				//$all session/user
				else if(response.contains("$all")) {
					if(response.contains("$allsessionsinforet")) {//all sessions info
						receive_success = true;	
						if(receive_success) {
							JOptionPane.showMessageDialog(null, "获取所有会话信息成功", "提示",JOptionPane.WARNING_MESSAGE);
							textArea_sessionsinfo_recv_1.setText(response.substring(19).replace('$', '\n'));
						}
//						else {
//							JOptionPane.showMessageDialog(null, "获取所有会话信息失败，系统出现故障", "提示",JOptionPane.WARNING_MESSAGE);
//						}
					}
					else if(response.contains("$allusersinforet")) {//all users info
						receive_success = true;
						if(receive_success) {
							JOptionPane.showMessageDialog(null, "获取所有用户信息成功", "提示",JOptionPane.WARNING_MESSAGE);
							textArea_usersinfo_recv_1.setText(response.substring(16).replace('$', '\n'));
						}
					}					
				}
				else if(response.contains("$newsession_success")) {//response from create new session
					String id = response.substring(response.indexOf("id=")+3);
					JOptionPane.showMessageDialog(null, "建立新会话" + id + "成功", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else if(response.contains("$entersession")) {//response from enter a new session
					if(response.contains("success")) {
						String id = response.substring(response.indexOf("$id=")+4);
						if(session_id != Integer.parseInt(id)) {
							session_id = Integer.parseInt(id);
							textArea_session_recv.setText("");
						}
						JOptionPane.showMessageDialog(null, "进入会话" + id + "成功", "提示",JOptionPane.WARNING_MESSAGE);
						panel_session.setVisible(true);
						panel_server_client.setVisible(false);
						session_id = Integer.parseInt(id);
						if(textArea_session_recv.getText().length() == 0) {
							textArea_session_recv.append("欢迎进入会话" + session_id);
						}						
		            	textArea_session_recv.setCaretPosition(textArea_session_recv.getDocument().getLength());//调整光标的位置
						
					}
					else if(response.contains("failure")) {
						JOptionPane.showMessageDialog(null, "没有这个会话，请重新输入", "提示",JOptionPane.WARNING_MESSAGE);
					}
				}
				else if(response.equals("$retransmit success!")) {//response from send msg
					JOptionPane.showMessageDialog(null, "发送成功", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else if(response.equals("$quitsessionfailure")) {
					JOptionPane.showMessageDialog(null, "退出会话失败", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else if(response.contains("$quitsessionsuccess")) {//response from quit session
					int s_id = Integer.parseInt(response.substring(response.indexOf("$id=")+4));
					if(s_id == session_id) {
						session_id = -1;
						JOptionPane.showMessageDialog(null, "返回原页面", "提示",JOptionPane.WARNING_MESSAGE);
						textArea_session_recv.setText("");
						panel_session.setVisible(false);
						panel_server_client.setVisible(true);
																	
					}
					else {
						// do nothing
					}
					
				}
				else if(response.equals("$registersuccess!")) {//response from register
					JOptionPane.showMessageDialog(null, "注册成功，请重新登录", "提示",JOptionPane.WARNING_MESSAGE);
					panel_register.setVisible(false);
					panel_login.setVisible(true);
					frame.setTitle("\u767B\u5F55");
				}
				

				
			}
			else if(response.indexOf("session") == 0) {//sent from other user
				if(response.indexOf("sessionid=") == 0) {
					int s_id = Integer.parseInt(response
							.substring("sessionid=".length(), response.indexOf('$')));
					String msg = response.substring(response.indexOf('$')+1);
					if(s_id == session_id) {//judge if the info should be on the textarea
						//set on text
						textArea_session_recv.append("\r\n" + msg);
		            	textArea_session_recv.setCaretPosition(textArea_session_recv.getDocument().getLength());//调整光标的位置
					}
					else {
						//do nothing
						//no display
					}
										
				}
				////
			}
			
		}

	}
}
