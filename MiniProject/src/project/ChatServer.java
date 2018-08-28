package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChatServer {
	ServerSocket ss; // 클라이언트와 접속할 때 -> 포트제공 (접속만 관리)
	Socket s; // 통신하기 위해서(문자열 전송)
	Vector v; // 실시간으로 접속할 클라이언트의 정보를 저장
	List<String> onlineMember;	//로그인한 클라이언트 정보 저장

	// Thread 객체가 필요 -> has a 관계로 연결
	ServerThread st;
	
	static Connection con;
	static PreparedStatement pstmt;
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:orcl";
	static ResultSet rs;

	public ChatServer() {
		// 서버를 가동 -> 클라이언트가 접속할때까지 기다린다.(무한루프)
		v = new Vector();
		onlineMember = new ArrayList<String>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "test1", "t1234");			
		}catch(Exception e) {
			System.out.println("DB connection error : " + e);
		}
		
		try {
			ss = new ServerSocket(5000);
		
			// 무한대기(언제나 접속 가능하도록)
			while (true) {
				s = ss.accept(); // (ip, port)
				System.out.println("Accepted from " + s); // 접속한 클라이언트의 정보
				st = new ServerThread(this, s);
				addThread(st);

				st.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("서버접속실패!!! =>" + e);
		}
	}// Constructor
		// 1. 접속한 클라이언트의 정보를 Vector에 저장

	public void addThread(ServerThread st) {
		v.add(st);
	}

	// 2.퇴장한 클라이언트의 정보
	public void removeThread(ServerThread st) {
		v.remove(st);
	}
	
	public void addOnlineMember(String mem) {
		onlineMember.add(mem);
		broadCastNickname();
	}

	// 2.퇴장한 클라이언트의 정보
	public void removeOnlineMember(String mem) {
		onlineMember.remove(mem);
		broadCastNickname();
	}

	// 3.각 클라이언트에게 실시간으로 메세지를 전달시켜주는 메서드
	public void broadCast(String str) {
		int len = v.size();
		for (int i = 0; i < len; i++) {
			ServerThread st = (ServerThread) v.get(i);
			// st는 접속한 클라이언트 정보를 가지고 있음. 입장/퇴장 정보 알려줘야 함.
			st.send(str);
		}
	}
	
	//각 클라이언트에게 현재 로그온 되어있는 사용자 닉네임 뿌려주기.
	public void broadCastNickname() {
		int onlineMemNum = v.size(); //접속한 모든사람
		
		for(int m = 0 ; m < onlineMemNum ; m++) {
			ServerThread st = (ServerThread) v.get(m);	//각각의 클라이언트에게 전송해야 하므로, 서버쓰레드 객체 각자에게 전송
			
			String onMem = "memlist,";
			int len = onlineMember.size();
			if(len >= 1) {
				for(int i = 0 ; i < len - 1 ; i++) {
					onMem += onlineMember.get(i)+",";		//,로 나눠주기
				}
				onMem += onlineMember.get(len-1);	//마지막 멤버는 ,없이 전송
			}			
			st.send(onMem);
		} 	
	}
	
	public void whisper(String message) {	//각 개인에게 메시지 뿌려줌..
		String[] strArr = message.split(",");
		int len = v.size();
		for(int i = 0 ; i < len ; i++) {
			ServerThread st = (ServerThread) v.get(i);
			if(st.nickname.equals(strArr[2])) {
				st.send("messageRecv," + strArr[1] + "," + strArr[3]);				
			}			
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer();
	}
}

// 실시간으로 데이터를 전송 => thread
class ServerThread extends Thread {
	Socket s; // 클라이언트와 통신
	ChatServer cg;
	// 입출력 스트림 객체가 필요
	BufferedReader br;
	PrintWriter pw; // BufferedWriter를 쓰지 않는 이유? printwriter기능 이용
	String str; // 전달할 문자열
	String name; // 대화명(id)->처음 실행할때 대화명 입력하게 되어있음
	List<String> onlineList = new ArrayList<String>();
	
	String nickname = "";
	boolean isLogon = false;

	// ServerThread의 생성자가 필요 : 입출력스트림 -> 통신을 할 준비 초기화
	// 1.메서드호출(broadCase()) , 2.클라이언트와 통신하기 위해서
	public ServerThread(ChatServer cg, Socket s) {
		this.cg = cg;
		this.s = s;

		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream())); // 원격에서 받아오기
			pw = new PrintWriter(s.getOutputStream(), true);// 접속한 원격컴퓨터의 출력객체, AUTOFLUSH 유무
		} catch (Exception e) {
			System.out.println("연결실패!" + e);
		}
	}

	public void send(String str) {
		System.out.println("(서버)쏜다 " + str);
		pw.println(str);
		pw.flush();// 버퍼에 쌓아두지 말고 바로 출력		
	}

	@Override
	public void run() {		
		// 실시간으로 데이터 전송, 받는 코딩 | 클라이언트 종료시 퇴장
		try {
			while ((str = br.readLine()) != null) {
				System.out.println(str);
				String[] strArr = str.split(",");
				if(strArr[0].equals("login")) {			//-----------로그인폼------------------//
					//로그인이 들어오면
					String sql = "select count(*) from member where id=? and pw=?";
					ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
					ChatServer.pstmt.setString(1, strArr[1]);
					ChatServer.pstmt.setString(2, strArr[2]);
					ChatServer.rs = ChatServer.pstmt.executeQuery();
					ChatServer.rs.next();
					
					if(ChatServer.rs.getInt(1) > 0) {
						send("login,1");
						//로그인 성공하면 해당 아이디로 검색해서 닉네임을 받아온 뒤 로그온 리스트에 추가 후, 클라이언트로 전송 
						isLogon = true;
						
						sql = "select nick from member where id=?";
						ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
						ChatServer.pstmt.setString(1, strArr[1]);
						ChatServer.rs = ChatServer.pstmt.executeQuery();
						ChatServer.rs.next();
						
						String nick = ChatServer.rs.getString(1);
						send("yourNickname," + nick);
						cg.addOnlineMember(nick);
						this.nickname = nick;
					}else {
						send("login,2");
						isLogon = false;
					}				
				}else if(strArr[0].equals("register")) {		//-----------회원가입폼------------------//
					//회원가입이 들어오면
					String sql = "";
					if(strArr[1].equals("idcheck")) {	//회원가입 아이디 체크
						//회원가입에서 아이디 체크
						sql = "select count(*) from member where id =?";
						ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
						ChatServer.pstmt.setString(1, strArr[2]);
						ChatServer.rs = ChatServer.pstmt.executeQuery();
						ChatServer.rs.next();
						
						if(ChatServer.rs.getInt(1) > 0) {
							send("idcheck,1");	//회원 존재
						}else {
							send("idcheck,2");	//회원 존재하지 않음 -> 가능
						}		
					}else if(strArr[1].equals("nickcheck")) {	//회원가입 닉네임 체크
						//닉네임 체크
						sql = "select count(*) from member where nick=?";
						ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
						ChatServer.pstmt.setString(1, strArr[2]);
						ChatServer.rs = ChatServer.pstmt.executeQuery();
						ChatServer.rs.next();
						
						if(ChatServer.rs.getInt(1) > 0) {
							send("nickcheck,1");	//회원 존재
						}else {
							send("nickcheck,2");	//회원 존재하지 않음 -> 가능
						}
					}else if(strArr[1].equals("regicheck")) {	//회원가입 체크
						//회원가입 체크
						sql = "insert into member values(?,?,?)";
						ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
						ChatServer.pstmt.setString(1, strArr[2]);
						ChatServer.pstmt.setString(2, strArr[3]);
						ChatServer.pstmt.setString(3, strArr[4]);
						int res = ChatServer.pstmt.executeUpdate();
						
						send("regicheck," + String.valueOf(res));
					}
				}else if(strArr[0].equals("memberlist")) {	//멤버리스트 뿌리기
					String sql = "select count(*) from member";
					ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
					ChatServer.rs = ChatServer.pstmt.executeQuery();	
					ChatServer.rs.next();
					int lastRow = ChatServer.rs.getInt(1);	//마지막 로우 알아내기				
					
					sql = "select nick from member";
					ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
					ChatServer.rs = ChatServer.pstmt.executeQuery();				
					
					String snedMembers = "memberlist,";					
					while(ChatServer.rs.next()) {
						if(ChatServer.rs.getRow() == (lastRow)) {
							snedMembers += ChatServer.rs.getString(1);
						}else {
							snedMembers += ChatServer.rs.getString(1) + ",";
						}
					}
					send(snedMembers);
				}else if(strArr[0].equals("messageSend")) {
					//사용자에게 메세지 들어왔을 때.					
					cg.whisper(str);
					System.out.println(str);
				}else if(strArr[0].equals("deleteMember")) {
					String sql = "delete from member where nick=?";
					ChatServer.pstmt = ChatServer.con.prepareStatement(sql);
					ChatServer.pstmt.setString(1, strArr[1]);
					int result = ChatServer.pstmt.executeUpdate();
					ChatServer.con.commit();
				}
			}
			// 더 이상 입력받을 문자가 없는 동안 계속해서 전달
		} catch (Exception e) {// x버튼클릭 -> 종료시 연결해제 -> 퇴장으로 간주
			System.out.println("에러 발생" + e);
			cg.removeThread(this);// 현재 접속자 메모리에서 삭제
			
			if(isLogon) {	//로그온 되어있었다면 없애기.
				cg.removeOnlineMember(this.nickname);
				System.out.println(this.nickname + "이 종료함");
			}
			
			cg.broadCast("[" + name + "]님이 퇴장하셨습니다.");
			// s.getInetAddress() -> 상대방의 IP주소
			System.out.println(s.getInetAddress() + "의 연결이 종료됨!");
		}
	}
}
