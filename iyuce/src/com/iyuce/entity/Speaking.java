package com.iyuce.entity;

import java.io.Serializable;

public class Speaking implements Serializable {

	private static final long serialVersionUID = -4135068359629721163L;
	
	public String id;
	public String uname;
	public String examroom;
	public String sub;
	public String message;
	public String vtime;

	/*
	 * 主要参数说明： 主要参数说明：
	 * 
	 * id:对应id 
	 * uname:投票（分享）用户名 
	 * examroom:投票（分享）考场 
	 * sub：被投票（分享）的题目 
	 * message：投票（分享）心得
	 */
}
