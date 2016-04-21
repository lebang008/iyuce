package com.iyuce.entity;

import java.io.Serializable;

public class SubContent implements Serializable {

	private static final long serialVersionUID = -31518755465894723L;
	
	public String subid;
	public String subname;
	public String subanswer;
	public String subimg;
	public String timestamp;

	/*
	 * 主要参数说明：
	 *  subid:题目ID 
	 *  subname:题目名称 
	 *  subanswer:题目答案链接 
	 *  subimg:题目图片
	 *  timestamp:图片最后更改时间
	 */
}
