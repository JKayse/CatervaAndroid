package com.flock;

public class Group {

	String mName;
	Friend[] friends;
	
	public Group(){
		mName = "placeHolder";
	}
	public void setFriends(Friend[] values){
		this.friends = values;
	}
	
	public String getFriendName(Integer position){
		return friends[position].getName();
	}
	
	public String getFriendId(Integer position){
		return friends[position].getID();
	}
	
	public Friend getFriend(Integer position){
		return friends[position];
	}
	
	public Friend[] getFriends(){
		return friends;
	}
	
	public void setName(String value){
		this.mName = value;
	}
	public String getName(){
		return mName;
	}
	
}
