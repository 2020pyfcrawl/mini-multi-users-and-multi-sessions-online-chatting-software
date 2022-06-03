import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author pyf19
 *
 */
public class server {
	static Object lock_sendtoclients = new Object();
	static ServerSocket serverSocket = null;
	static server server1= new server();
	static int id_user = 0;
	static int session_id = 0;
	
	static Socket socket = null;
	static ArrayList<user> users = new ArrayList<user>();
	static ArrayList<Socket> socketList=new ArrayList<Socket>();
	static ArrayList<session> sessionList=new ArrayList<session>();
	
	static {
		users.add(new user("aaa","123",id_user));
		id_user++;
		users.add(new user("bbb","123",id_user));
		id_user ++;
	}
	
	private server() {// ���췽��
		try {
            server.serverSocket = new ServerSocket(9999);
            System.out.println("������Ѿ�����");
        } catch (IOException e) {
            e.printStackTrace();
          }
    }
	
	
	public static void main(String[] args) {
		int count = 0;
				
/*		
 * this part is deprecated 
 * global sending thread 
		//�ȿ���һ��ȫ�ֵ�send��������������ȫ�ַ�����Ϣ��֮������չ�ɹ��ܣ�����ָ���û�ɶ�ģ�
		sendmsg_server send_message = new sendmsg_server();
	    Thread s_send = new Thread(send_message);
	    s_send.start();
* */

		
	    while (true) {          
	        try {
	            //System.out.println("�˿�9999�ȴ�������......");
	            socket = serverSocket.accept();
	            System.out.println("�����м����ͻ�������"+server.socketList.size());
	            count++;
	            System.out.println("��" + count + "���ͻ�������");
	            socketList.add(socket);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        //����һ���µ��̣߳�Ȼ�������ͻ��˵�����������д���
	        //���������Ự����¼��������ͨ��Ϣ��ѯ�ʿͻ�����Ϣ��---������Ϣ��$��ͷ��ʾ
	        //ͨ��text����Ȼ���ڿͻ��˼ӹ����ڷ���˽�ӹ�����ý��
	       
	        each_client clients = new each_client(socket);//��ÿ���ͻ�������һ��ר�ŵ��߳�
	        Thread clients_one = new Thread(clients);
	        clients_one.start();
	        
	    }		
	}
}


//���ܿͻ��˵���Ϣ���ҷ���
/**
 * @author pyf19
 * the class used for subthread
 */
class each_client implements Runnable{
	
	Socket client_socket = null;
	user user_client;
	/**
	 * @param socket
	 */
	public each_client(Socket socket) {
		// TODO Auto-generated constructor stub
		this.client_socket = socket;
	}

	
	/* 
	 * new stop until the client quit
	 * run
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			
            Thread.sleep(1000);
            //send_prompt();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(client_socket
                    .getInputStream()));
            
            //log process
            while (true) {
                String message = in.readLine();
                System.out.println(message);
                
                if(message.indexOf("$login") == 0) {
                	boolean user_exist = check_user(message);
                	 String response = null;
                     if(user_exist) {
                     	response = "$login success";                	
                     }
                     else {
                     	//send sth back
                     	response = "$login failure";
                     }
                     System.out.println(response);
                     synchronized (server.lock_sendtoclients) {
         	            PrintWriter out = new PrintWriter(client_socket.getOutputStream());
         	            // System.out.println("�Կͻ���˵��");
         	            out.println(response);
         	            out.flush();    	                    	            
                     }
                     if(user_exist) {
                     	break;
                     }
    			}
                else if(message.indexOf("$register") == 0) {
                	process(message);
                }
                
               
            	
            }
            //send_prompt();
            
            
            
            while (true) {
                String message = in.readLine();
                System.out.println(message);
                if(message.length() > 0) {
                	process(message);
                }
                
            	//may send back
            	
            }
        } catch (Exception e) {
            //e.printStackTrace();
            server.socketList.remove(client_socket);
            System.out.println(client_socket + "\r\n" + ((this.user_client != null)?("�ͻ�"+user_client.name):"") + "�Ѿ��˳�����");
            if((this.user_client != null)) {
            	this.user_client.set_online(false);
            }
            this.user_client = null;
            System.out.println("���ڻ�ʣ��" + server.socketList.size() + "���ͻ�����");
        }
	}
	
	
	/**
	 * log information check
	 * @param message
	 * @return
	 */
	private boolean check_user(String message) {
		// TODO Auto-generated method stub
		int name_left = message.indexOf("$login");
		int name_right = message.indexOf("$password");
		if(name_left == -1 | name_right == -1) {
			//sth is wrong
			System.out.println("Wrong request to login!");
			return false;
		}
		name_left += 6;
		String name = message.substring(name_left, name_right);
		System.out.println("user_name = " + name);
		String pwd = message.substring(name_right + 9);
		System.out.println("pwd = " + pwd);
		boolean check = false;
		for (int i = 0; i < server.users.size(); i++) {
			user tmp = server.users.get(i);
			check = tmp.login_check(name, pwd);
			if(check == true) {
				tmp.set_online(true);
				tmp.connect_succeed(client_socket);
				this.user_client = tmp;
				return check;
			}
		}
		return check;

	}

	/**
	 * according to the specific message from the client
	 * do the specific response
	 * @param message
	 * @throws IOException
	 */
	private void process(String message) throws IOException {
		String response = null;
		
		//������Ϣ����
		//������Ӧ����Ӧ
		
		//��ѯ��������Ϣ
		if(message.charAt(0) == '$') {
			
			if(message.equals("$allsessionsinfo")) {//all sessions info
				StringBuilder msg = new StringBuilder("sessions$ ID  Session name$");
				for(int i = 0; i < server.sessionList.size() ; i++) {
					msg.append(server.sessionList.get(i));
				}
				System.out.println(msg);
				//send it back to the client
				response = "$allsessionsinforet" + msg ;
				sends_to_c(response,client_socket);	
				
			}
			else if(message.equals("$allusersinfo")) {//all users info
				StringBuilder msg = new StringBuilder("users$ ID  user name   online$");
				for(int i = 0; i < server.users.size() ; i++) {
					msg.append(server.users.get(i));
				}
				System.out.println(msg);
				//send it back to the client
				response = "$allusersinforet" + msg ;
				sends_to_c(response,client_socket);			
			}
			else if(message.contains("$newsession")) {//create a new session
				//�����µ�session
				String new_session_name = message.substring(message.indexOf('=')+1);
				session new_session = new session(user_client,new_session_name,server.session_id);
				server.session_id++;
				server.sessionList.add(new_session);
				response = "$newsession_success$id=" + (server.session_id-1);
				sends_to_c(response,client_socket);	
			}
			else if(message.contains("$entersession")) {//enter a session
				boolean enter_success = false;
				int id = Integer.parseInt(message.substring(message.indexOf("$id=") + 4));
				for(int i = 0; i < server.sessionList.size(); i++) {
					session tmp = server.sessionList.get(i);
					if(tmp.find_session_id(id)) {
						tmp.add_user(user_client);
						response = "$entersessionsuccess$id=" + id;
						enter_success = true;
						break;
					}
					
				}
				if(!enter_success) {
					response = "$entersessionfailure";
				}
				sends_to_c(response,client_socket);
			}
			else if(message.contains("$quitsession")) {//quit a session
				boolean quit_success = false;
				int id = Integer.parseInt(message.substring(message.indexOf("$id=") + 4));
				for(int i = 0; i < server.sessionList.size(); i++) {
					session tmp = server.sessionList.get(i);
					if(tmp.find_session_id(id)) {
						tmp.remove_user(user_client);
						response = "$quitsessionsuccess$id=" + id;
						quit_success = true;
						break;
					}
					
				}
				if(!quit_success) {
					response = "$quitsessionfailure";
				}
				sends_to_c(response,client_socket);				
			}
			else if(message.contains("$registernewuser")) {//register a new user
				String newu_name = message.substring(message
						.indexOf("$name=")+6, message.indexOf("$password="));
				String newu_pwd = message.substring(message.indexOf("$password=")+10);
				user new_user = new user(newu_name,newu_pwd,server.id_user);
				server.id_user++;
				server.users.add(new_user);
				response = "$registersuccess!";
				sends_to_c(response,client_socket);				
			}
			
			
		}
		else {
			if(message.indexOf("sessionid=") == 0) {//message from session id = ..
				String msg = message.substring(message.indexOf('$')+1);
				String retransmition = null;
				int session_id = Integer.parseInt(message
						.substring("sessionid=".length(), message.indexOf('$')));
				retransmition = "sessionid=" + session_id + "$" + user_client.name + ": " + msg;
				session this_session = null;
				for(int i = 0; i < server.sessionList.size(); i++) {
					this_session = server.sessionList.get(i);
					if(this_session.find_session_id(session_id)) {
						break;
					}				
				}
				for(int i = 0; i < this_session.user_list.size(); i++) {
					user tmp_user = this_session.user_list.get(i);
					if(tmp_user.equals(user_client)) {
						//do nothing
					}
					else {
						//send msg
						sends_to_c(retransmition,tmp_user.socket_number);
					}
				}
				response = "$retransmit success!";
				sends_to_c(response,client_socket);							
			}			
		}
	}
	

	
	//���ܸĳɶ�һϵ�пͻ�������Ϣ---------------------------------------
	public void sends_to_c(String information,ArrayList<Socket> sockets) throws IOException {
		try {
			synchronized (server.lock_sendtoclients) {
				for (int i = 0; i < sockets.size(); i++) {
	            	//������������
	            	Socket c_socket=sockets.get(i);
	            	if(c_socket == client_socket) {
	            		continue;
	            	}
	                PrintWriter out = new PrintWriter(c_socket.getOutputStream());
	                // System.out.println("�Կͻ���˵��");
	                out.println("�ͻ���"+client_socket.getPort()+ ": " +information);
	                out.flush();
	                
	            }
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	//send msg to one client
	public void sends_to_c(String information,Socket target_socket) throws IOException {
		try {
			synchronized (server.lock_sendtoclients) {
				PrintWriter out = new PrintWriter(target_socket.getOutputStream());
				out.println(information);
	            out.flush();
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
}



//�����пͻ��˷���֪ͨ��Ϣ��Ⱥ����Ϣ��---��һ��������һ���������̣߳�������ɷ�����������
/**
 * @author pyf19
 * this class is used for the server to send msg to the clients positively
 * when sth serious happens
 * it not used in the project
 * but it can be used in the near future
 */
//class sendmsg_server implements Runnable{
//
//	Scanner input = new Scanner(System.in);
//    static String name=null;
//	public sendmsg_server() {
//		
//	}
//	
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		try {
//            Thread.sleep(1000);
//            while (true) {
//            	//����λ�øı�
//                String msg = input.nextLine();
//                //������Ҫ��ȫ---ѡ��
//                //--------------------------------------------------------------
//                
//                synchronized (server.lock_sendtoclients) {
//                	for (int i = 0; i < server.socketList.size(); i++) {
//    	            	//������������
//    	            	Socket c_socket=server.socketList.get(i);
//    	                PrintWriter out = new PrintWriter(c_socket.getOutputStream());
//    	                // System.out.println("�Կͻ���˵��");
//    	                out.println("�����˵��"+msg);
//    	                out.flush();
//    	                
//    	            }
//                }
//	            //mutex
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//	}
//	
//}



