package project;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class LoginForm extends JFrame implements ActionListener,Runnable{	
	JPanel panId,panPw,PanButton;
	JTextField tfId,tfPw;
	JButton btLogin,btRegister;

	static BufferedReader br;//서버가 보내준거 받는쪽
	static PrintWriter pw; //서버에게 전달
	String str;	//서버로부터 받아오는 값
	
	int result;
	
	static boolean checkId = false, checkNick = false;
	
	static String wholeMember = "";
	static String memList = "";
	static String myNickName = "";
	
	public LoginForm() {
		super("login");
		this.setBounds(300, 300, 250, 150);
		tfId = new JTextField(18);
		tfPw = new JPasswordField(18);

		panId = new JPanel(new FlowLayout());
		panId.add(new JLabel("ID:  "));
		panId.add(tfId);
		
		panPw = new JPanel(new FlowLayout());
		panPw.add(new JLabel("pw:"));
		panPw.add(tfPw);
		
		btLogin = new JButton("Login");
		btRegister = new JButton("Register");	
		PanButton = new JPanel(new FlowLayout());
		PanButton.add(btLogin); PanButton.add(btRegister);
		
		//버튼 이벤트 적용
		btLogin.addActionListener(this);
		btRegister.addActionListener(this);
		
		try {
			br = new BufferedReader(new InputStreamReader(Singleton.getInstance().getInputStream()));	//원격에서 받아오기			
			pw = new PrintWriter(Singleton.getInstance().getOutputStream(),true);//접속한 원격컴퓨터의 출력객체, AUTOFLUSH 유무
		}catch(Exception e) {
			System.out.println("(Login)입출력 객체 생성 에러 : " + e);
		}
		
		 Thread ct=new Thread(this);
		 ct.start();//run()
		
		this.setResizable(false);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(panId);
		this.add(panPw);
		this.add(PanButton);
			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btRegister) {
			RegisterForm regi = new RegisterForm();	
		}else if(e.getSource() == btLogin) {
			queryLogin(tfId.getText(), tfPw.getText());
		}		
	}
	
	public synchronized boolean check() {
		boolean ret = false;
		if(result == 1) {
			//로그인 성공
			queryMemberList();					
			ret = true;
		}else {
			JOptionPane.showMessageDialog(this, "아이디 비밀번호를 확인해주세요.");		
			ret = false;
		}		
		return ret;
	}
	
	public synchronized void goMemListForm() {
		if(result == 1) {
			//성공했을 때만 가져온다.
			JOptionPane.showMessageDialog(this, "로그인에 성공했습니다.");
			this.setVisible(false);
			MemberListForm mlf = new MemberListForm();
		}
	}
	
	public synchronized void checkId(String result) {
		if(result.equals("1")) {
			//회원 존재
			JOptionPane.showMessageDialog(null, "아이디가 이미 사용중입니다.");
		}else {
			JOptionPane.showMessageDialog(null, "사용 가능한 아이디 입니다.");
			checkId = true;
		}
	}
	
	public synchronized void checkNick(String result) {
		if(result.equals("1")) {
			//회원 존재
			JOptionPane.showMessageDialog(null, "닉네임이 이미 사용중입니다.");
		}else {
			JOptionPane.showMessageDialog(null, "사용 가능한 닉네임 입니다.");
			checkNick = true;
		}
	}
	
	public synchronized void checkRegister(String result) {
		if(result.equals("1")) {
			//회원 존재
			JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");	
		}else {
			JOptionPane.showMessageDialog(null, "회원가입에 실패했습니다.");
		}
	}
	
	public void queryLogin(String id, String pw) {
		LoginForm.pw.println("login," + id + "," + pw);
		LoginForm.pw.flush();
	}
	
	// 전체멤버 요청
	public void queryMemberList() {
		LoginForm.pw.println("memberlist");
		LoginForm.pw.flush();
	}
	
	@Override
	public void run() {		
		try {
			while((str = br.readLine()) != null) {
				String strArr[] = str.split(",");
				if(strArr[0].equals("login")) {	
					if(strArr[1].equals("1")) {
						//로그인 성공
						result = 1;
						check();
					}else if(strArr[1].equals("2")) {
						//로그인 실패
						result = 2;
						check();
					}					
				}else if(strArr[0].equals("idcheck")) {				
					//아이디 체크면
					checkId(strArr[1]);
				}else if(strArr[0].equals("nickcheck")) {			
					//닉네임 체크면
					checkNick(strArr[1]);
				}else if(strArr[0].equals("regicheck")) {
					//회원가입이면
					checkRegister(strArr[1]);
				}else if(strArr[0].equals("memberlist")) {
					//전체 회원정보를 받아온다.					
					wholeMember = str;
					goMemListForm();
				}else if(strArr[0].equals("memlist")) {
					//온라인 회원리스트 받아옴.
					memList = str;	
					System.out.println(str);
					MemberListForm.updateMemberList(memList);
				}else if(strArr[0].equals("yourNickname")) {
					myNickName = strArr[1];	//내 닉네임 받아오기..
				}else if(strArr[0].equals("messageRecv")) {
					MemberListForm.updateMessage(str);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("(로그인) 서버로부터 리턴값 읽기 실패 : " + e);
		}		
	}
	
	public static void main(String[] args) {
		new LoginForm();		
	}
}
