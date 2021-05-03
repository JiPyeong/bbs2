package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// ServletContextListener : 웹 컨테이너가 실행되거나 종료될때 발생하는 이벤트 처리
@WebListener
public class WebAppInit implements ServletContextListener{
	private String pathname = "/WEB-INF/userNumbers.properties";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 서버가 시작되는 시점에 호출 
		
		// 실제 파일 경로
		pathname = sce.getServletContext().getRealPath(pathname);
		
		loadFile();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// 서버가 종료되기 직전에 호출
		saveFile();
	}
	
	private void saveFile() {
		// 서버에 접속자 수를 프로퍼티 파일로 저장
		long today, yesterday, total;
		FileOutputStream fos = null;
		Properties p = new Properties();
		
		try {
			today = CountManager.getTodayCount();
			yesterday = CountManager.getYesterdayCount();
			total = CountManager.getTotalCount();
			
			p.setProperty("today", Long.toString(today));
			p.setProperty("yesterday", Long.toString(yesterday));
			p.setProperty("total", Long.toString(total));
			
			fos = new FileOutputStream(pathname);
			p.store(fos, "count"); // Properties 내용을 파일에 저장
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	private void loadFile() {
		// 서버에 저장된 접속자 수를 불러오기
		long today=0, yesterday=0, total=0;
		FileInputStream fis = null;
		Properties p = new Properties();
		
		try {
			File f = new File(pathname);
			if(!f.exists()) {
				return;
			}
			
			fis = new FileInputStream(f);
			p.load(fis);
			
			today = Long.parseLong(p.getProperty("today"));
			yesterday = Long.parseLong(p.getProperty("yesterday"));
			total = Long.parseLong(p.getProperty("total"));
			
			CountManager.init(today, yesterday, total);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (Exception e2) {
				}
			}
		}
	}
}
