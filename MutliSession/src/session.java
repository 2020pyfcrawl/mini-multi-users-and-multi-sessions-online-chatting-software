import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author pyf19
 *
 */
public class session {
	ArrayList<user> user_list = null;
	user session_creater = null;
	String password = null;
	String name = null;
	int ID = 0;	//need to finish further
	
	public session() {
		user_list = new ArrayList<user>();
	}
	
	public session(user creater,int ID) {
		session_creater = creater;
		user_list = new ArrayList<user>();
		user_list.add(creater);
		this.ID = ID;
	}
	
	public session(user creater,String name,int ID) {
		this(creater,ID);
		this.name = name;
	}
	
	public session(user creater,String name,String password,int ID) {
		this(creater,name,ID);
		this.password = password;
	}
	
	public boolean add_user(user new_user) {
		if(find_user(new_user)) {
			//the user already existed
			//some information
			JOptionPane.showMessageDialog(null, "用户已经在会话中", "提示",JOptionPane.WARNING_MESSAGE);
			System.out.println("用户已经在会话中");
			return false;
		}
		else {
			//success
			//some information---GUI---messagebox
			JOptionPane.showMessageDialog(null, "添加成功", "提示",JOptionPane.WARNING_MESSAGE);
			System.out.println("添加成功");
			user_list.add(new_user);
			return true;
		}
	}
	
	public boolean remove_user(user old_user) {
		if(find_user(old_user)) {
			//the user already existed
			//some information
			JOptionPane.showMessageDialog(null, "退出成功", "提示",JOptionPane.WARNING_MESSAGE);
			System.out.println("用户移除成功");
			user_list.remove(old_user);
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "用户不在此会话中，无需移除", "提示",JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	public boolean find_user(user user1) {
		
		return user_list.contains(user1);		
	}
	
	public String toString() {
		String ret = " " + this.ID + "      " + this.name + "$" ;
		return ret;
	}
	public boolean find_session_id(int ID) {
		return this.ID == ID;
	}
}
