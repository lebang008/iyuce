package com.iyuce.entity;

import java.io.Serializable;

public class Content implements Serializable {

	
	private static final long serialVersionUID = 935845736619226739L;
	
	public String pageid;
	public String contentimg;
	public String timestamp;

	/*
	 * 主要参数说明：
	 * pageid:页码id
	 * contentimg:内容页图片
	 * timestamp: 内容页图片最后修改时间
	 */
}