package com.iyuce.entity;

import java.io.Serializable;

public class Lesson implements Serializable {
	
	private static final long serialVersionUID = -1347472962226326981L;
	
	public String booktype;
	public String imgPath;

	/*
	 * booktype:书籍类别(1：表示听力，4：表示阅读，5：表示写作，6：表示口语)
	 * imgPath:图片路径
	 */
}
