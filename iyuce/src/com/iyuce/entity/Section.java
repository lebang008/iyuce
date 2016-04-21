package com.iyuce.entity;

import java.io.Serializable;

public class Section implements Serializable {
	
	private static final long serialVersionUID = -2850776503349293990L;
	
	public String sectionid;
	public String sectionname;
	public String sectioncolor;
	public String sectionstate;

	/*
	 *主要参数说明：
	 *sectionid:篇章id
	 *sectionname:篇章名称
     *sectioncolor:篇章背景色
     *sectionstate:篇章状态 (1：表示启用，0：表示禁用)
	 */
}
