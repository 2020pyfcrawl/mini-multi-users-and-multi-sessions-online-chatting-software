import java.net.Socket;

/**
 * 
 */

/**
 * @author pyf19
 *
 */
public class user {
	String name = null;
	String password = null;
	Socket socket_number = null;
	//some information and functions about time---later implement this 
	int ID = -1;
	boolean online = false;
	
	
	
	public user() {
		ID = -1;	//Illegal
	}
	
	public user(String name,String password,int id) {
		this.name = name;
		this.password = password;
		this.ID = id;
	}
	
	
	public void connect_succeed(Socket connect_socket) {
		this.socket_number = connect_socket;
	}
	
	public boolean equals(user u) {
		return (ID == u.ID)? true : false;
	}
	public boolean login_check(String name,String pwd) {
		return (this.name.equals(name) && this.password.equals(pwd) && !online)? true : false ;
	}
	public void set_online(boolean state) {
		this.online = state;
	}
	public String toString () {
		String ret = null;
		ret = " " + this.ID + "     " + this.name + "    " + ((this.online)? "on" : "off") + "$";
		return ret;
	}
}
