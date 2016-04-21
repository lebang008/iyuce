package com.iyuce.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = -8422382681274324757L;

	public String userId;
	public String userName;
	public String Permission;
	public String money;
	public String token;
	public String update;

	/*
	 * 主要参数说明：
	 *  userId:用户id 
	 *  username:登录名
	 *  Permission:权限(1：表示听力，4：表示阅读，5：表示写作，6：表示口语)
	 *  money: 金币 
	 *  token: 登录成功后，服务器自动分配的token，后面其他接口会用到。后续服务器用来判断该用户是否登录。
	 *	update:用户登录时间
	 */
}
