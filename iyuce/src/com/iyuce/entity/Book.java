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
	 * ��Ҫ����˵����
	 * bookid:�鼮id 
	 * bookname:�鼮����
	 * bookimg:�鼮����ͼƬ 
	 * timestamp: �鼮����ͼƬ������ʱ��
	 * booktype:�鼮���(1����ʾ������4����ʾ�Ķ���5����ʾд����6����ʾ����)
	 */
}
