package com.iyuce.entity;

import java.io.Serializable;

public class WitContent implements Serializable{

	private static final long serialVersionUID = -5208041371406030198L;
	
	public String subid;
	public String subname;
	public String subimg;
	public String answerUrl;
	public String timestamp;

	/**
	 * 主要参数说明：
	 *  subid:题目ID 
	 *  subname:题目标题 
	 *  subimg:题目图片
	 *  answerUrl：题目答案链接
	 *  timestamp:图片最后更改时间
	 */
}
