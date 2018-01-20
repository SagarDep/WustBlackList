package com.example.wustblacklist.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;

public class TimeTool {
	/**
	 * ������ʱ��ת��Ϊ����ʽ������ʱ���ַ���
	 * @param milliTime ��ת���ĺ������ݣ��Ӹ�������ʱ�俪ʼ��
	 * @return ����ʽ������ʱ���ַ���
	 */
	@SuppressLint("SimpleDateFormat")
	public static String milliTime2LongStr(long milliTime){
		Calendar c = Calendar.getInstance() ;
		c.setTimeInMillis(milliTime) ;
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss/EEEE").format(c.getTime());
		
		return time ;
	}
	
	/**
	 * ������ʱ��ת��Ϊ�̸�ʽ������ʱ���ַ���
	 * @param milliTime ��ת���ĺ������ݣ��Ӹ�������ʱ�俪ʼ��
	 * @return ����ʽ������ʱ���ַ���
	 */
	@SuppressLint("SimpleDateFormat")
	public static String milliTime2ShortStr(long milliTime){
		Calendar c = Calendar.getInstance() ;
		c.setTimeInMillis(milliTime) ;
		String time = new SimpleDateFormat("MM-dd HH:mm/EEEE").format(c.getTime());
		
		return time ;
	}
	
	/**
	 * ������ʱ��ת��ΪMM-dd/EEEE
	 * @param milliTime ��ת���ĺ������ݣ��Ӹ�������ʱ�俪ʼ��
	 * @return ����ʽ������ʱ���ַ���
	 */
	@SuppressLint("SimpleDateFormat")
	public static String milliTime2dayWeek(long milliTime){
		Calendar c = Calendar.getInstance() ;
		c.setTimeInMillis(milliTime) ;
		String time = new SimpleDateFormat("MM-dd/EEEE").format(c.getTime());
		return time ;
	}
}
