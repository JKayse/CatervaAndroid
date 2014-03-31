package com.flock;

public class UserData {
	private static UserData instance = null;
	
	private UserData(){}
	
	public static UserData getInstance() {
		
		if(instance == null){
			instance = new UserData();
		}
		return instance;
			
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getUserId() {
		return UserId;
	}
	public String getUsername() {
		return username;
	}
	
	
	private  String username;
	private  String password;
	private int	UserId;
	}
