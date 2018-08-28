package project;

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Singleton {
	//1.객체를 생성 -> 한개만 생성 -> 공유 -> 정적멤버변수
			private static Socket instance = null; //선언
			
			//클래스 내부에서만 객체를 생성할 수 있게..
			//2.객체를 생성->생성자호출 -> 형식) private 생성자(){}
			private Singleton() {
				System.out.println("인스턴스 생성");
			}
			
			//3.공유객체를 다른 모든 사람들에게 공유 -> 정적메서드
			public static Socket getInstance() {
				if(instance == null) {
					//만들고자 하는 객체를 공유객체로 등록 => synchronized(클래스명.class){}
					synchronized (Singleton.class) {//싱글톤객체를 공유하겠다. (여러명 같이 접근하기때문) like make connection to Database
						try {
							instance = new Socket("192.168.0.61", 5000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				return instance; //이미 만들어져있으면 만들어진 객체 계속 반환
			}
			
}
