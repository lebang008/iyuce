package com.iyuce.entity;

import java.io.Serializable;

public class Audio implements Serializable{
	
	private static final long serialVersionUID = 41303603446901115L;
	
	public String id;
	public String title;
	public String url;
	
	public String s_title;
	public String count;

	/**
	 * 主要参数说明；
	 * id : 标识id
	 * title : 标题
	 * url : 对应音频的url
	 * 余下两个参数暂不知道作用
	 */
}
