package com.iyuce.entity;

import java.io.Serializable;

public class Book implements Serializable {
	
	private static final long serialVersionUID = -6317540274386431229L;
	
	public String bookid;
	public String bookname;
	public String bookimg;
	public String timestamp;
	public String booktype;

	/*
	 * 主要参数说明：
	 * bookid:书籍id 
	 * bookname:书籍名称
	 * bookimg:书籍封面图片 
	 * timestamp: 书籍封面图片最后更改时间
	 * booktype:书籍类别(1：表示听力，4：表示阅读，5：表示写作，6：表示口语)
	 */
}
