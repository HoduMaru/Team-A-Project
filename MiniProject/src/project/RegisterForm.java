package project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.DriverManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.net.*;

public class RegisterForm extends JFrame implements ActionListener {
	JPanel panInfo, panStat;
	JPanel panId, panPw, panPwCheck, panNick;
	JTextField tfId, tfPw, tfPwCheck, tfNick;
	JButton btCheckId, btCheckNick, btRegister;
	
	PrintWriter pw; // 서버에게 전달
	String str; // 서버로부터 받아오는 값

	public RegisterForm() {
		super("Register");
		this.setBounds(600, 300, 300, 250);
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		panInfo = new JPanel(new GridLayout(4, 1));
		panStat = new JPanel(new FlowLayout(FlowLayout.CENTER));

		panId = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panPw = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panPwCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panNick = new JPanel(new FlowLayout(FlowLayout.LEFT));

		tfId = new JTextField(10);
		btCheckId = new JButton("중복확인");
		tfPw = new JPasswordField(10);
		tfPwCheck = new JPasswordField(10);
		tfNick = new JTextField(10);
		btCheckNick = new JButton("중복확인");

		panId.add(new JLabel("I D "), "West");
		panId.add(tfId, "Center");
		panId.add(btCheckId, "East");
		panPw.add(new JLabel("pw"));
		panPw.add(tfPw);
		panPwCheck.add(new JLabel("pw check"));
		panPwCheck.add(tfPwCheck);
		panNick.add(new JLabel("NickName"));
		panNick.add(tfNick);
		panNick.add(btCheckNick);

		panInfo.add(panId);
		panInfo.add(panPw);
		panInfo.add(panPwCheck);
		panInfo.add(panNick);

		btRegister = new JButton("회원가입");
		panStat.add(btRegister);

		// 버튼 이벤트 적용
		btRegister.addActionListener(this);
		btCheckId.addActionListener(this);
		btCheckNick.addActionListener(this);

		this.add(panInfo, "Center");
		this.add(panStat, "South");

		// 이 프레임만 종료시키기 위한 이벤트 등록
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {		
				setVisible(false);
				dispose();
			}
		});

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btCheckId) {
			// 아이디 중복 체크
			
			if(tfId.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요!");
			}else {
				queryIdCheck(tfId.getText());
			}		
			
		} else if (e.getSource() == btCheckNick) {
			// 닉네임 중복체크			

			if(tfNick.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "닉네임을 입력해주세요!");
			}else {
				queryNickCheck(tfNick.getText());
			}
			
		} else if (e.getSource() == btRegister) {
			// 회원가입
			if (tfId.getText().equals("") || tfPw.getText().equals("") || tfPwCheck.getText().equals("")
					|| tfNick.getText().equals("")) {
				// 하나라도 비어있다면.
				JOptionPane.showMessageDialog(null, "빈칸 없이 모두 작성하여 주세요.");
			} else if (!tfPw.getText().equals(tfPwCheck.getText())) {
				// 비밀번호 체크 했더니 다르다면.
				JOptionPane.showMessageDialog(null, "비밀번호를 확인하여 주세요.");
			} else if (LoginForm.checkId == false) {
				// 중복체크를 하지 않았다면.
				JOptionPane.showMessageDialog(null, "아이디 중복확인을 해주세요.");
			} else if (LoginForm.checkNick == false) {
				// 닉네임중복체크 하지 않았다면.
				JOptionPane.showMessageDialog(null, "닉네임 중복확인을 해주세요.");
			} else {
				queryRegister(tfId.getText(),tfPw.getText(),tfNick.getText());
				
				this.setVisible(false);
				this.dispose();
			}
		}
	}

	// 아이디 중복체크 함수
	public void queryIdCheck(String id) {
		LoginForm.pw.println("register," + "idcheck," + id);
		LoginForm.pw.flush();
	}

	// 닉네임 중복체크 함수
	public void queryNickCheck(String nick) {
		LoginForm.pw.println("register," + "nickcheck," +  nick);
		LoginForm.pw.flush();
	}

	// 회원가입 함수
	public void queryRegister(String id, String pw, String nick) {
		LoginForm.pw.println("register," + "regicheck," +  id + "," + pw + "," + nick);
		LoginForm.pw.flush();
	}
}
