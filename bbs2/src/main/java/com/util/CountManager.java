package com.util;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

// HttpSessionListener : 세션이 생성되거나 소멸될때 발생하는 세션 이벤트 처리
@WebListener
public class CountManager implements HttpSessionListener{
	private static int currentCount;
	private static long todayCount, yesterdayCount, totalCount;
	
	public static void init(long today, long yesterday, long total) {
		todayCount = today;
		yesterdayCount = yesterday;
		totalCount = total;
	}
	
	 public CountManager() {
		 // 자정이 되면 어제 인원은 오늘 인원으로 변경하고 오늘인원은 0으로 설정
		 TimerTask task = new TimerTask() {
			@Override
			public void run() {
				yesterdayCount = todayCount;
				todayCount = 0;
			}
		 };
		 Timer timer = new Timer();
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.DATE, 1);
		 
		 cal.set(Calendar.HOUR, 0);
		 cal.set(Calendar.MINUTE, 0);
		 cal.set(Calendar.SECOND, 0);
		 cal.set(Calendar.MILLISECOND, 0);
		 
		 // 밤 12시마다 1번씩 실행
		 timer.schedule(task, cal.getTime(), 1000*60*60*24);
	 }
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// 세션이 생성될 때
		HttpSession session = se.getSession();
		ServletContext context = session.getServletContext();
		
		synchronized (se) {
			currentCount++;
			todayCount++;
			totalCount++;
			
			context.setAttribute("currentCount", currentCount);
			context.setAttribute("todayCount", todayCount);
			context.setAttribute("totalCount", totalCount);
			context.setAttribute("yesterdayCount", yesterdayCount);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// 세션이 소멸될 때
		HttpSession session = se.getSession();
		ServletContext context = session.getServletContext();
		
		synchronized (se) {
			currentCount--;
			if(currentCount<0) {
				currentCount = 0;
			}
			
			context.setAttribute("currentCount", currentCount);
			context.setAttribute("todayCount", todayCount);
			context.setAttribute("totalCount", totalCount);
			context.setAttribute("yesterdayCount", yesterdayCount);
		}
	}

	public static int getCurrentCount() {
		return currentCount;
	}

	public static long getTodayCount() {
		return todayCount;
	}

	public static long getYesterdayCount() {
		return yesterdayCount;
	}

	public static long getTotalCount() {
		return totalCount;
	}
}
