package project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MemberListForm extends JFrame implements ActionListener{
	static JList memberList;
	DefaultListModel model = new DefaultListModel();
	String[] wholeMembers;
	static String[] members;
	JButton onlyOnlineMember, deleteMember;	//온라인멤버만 볼수있는 새로고침 버튼
	JScrollPane jsp;
	ChattingDialog cd;
	JPanel buttonPanel;
	
	static String newMessage = "", pre_Message = "";

	public MemberListForm() {
		super("MemberList");
		
		this.setBounds(300, 300, 200, 400);
		this.setResizable(false);
		wholeMembers = LoginForm.wholeMember.split(",");
				
		for(int i = 1 ; i < wholeMembers.length ; i++) {
			if(LoginForm.myNickName.equals(wholeMembers[i])) {
				wholeMembers[i] = "나";
			}
			model.addElement(wholeMembers[i]);			
		}
		
		onlyOnlineMember = new JButton("온라인사용자만 보기");
		deleteMember = new JButton("회 원 탈 퇴");
		
		memberList = new JList(model);	//모델 더함		
		jsp = new JScrollPane(memberList);
		
		memberList.addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(e.getClickCount() == 2) {
					//더블클릭하면...					
					System.out.println("더블클릭 들어옴");

					String to = memberList.getSelectedValue().toString();
					
					cd = new ChattingDialog(LoginForm.myNickName, to);
				}
			}
		});	

		deleteMember.addActionListener(this);
		onlyOnlineMember.addActionListener(this);

		buttonPanel = new JPanel(new GridLayout(2, 1));
		buttonPanel.add(onlyOnlineMember); buttonPanel.add(deleteMember);
		
		this.add(buttonPanel, "South");
		this.add(jsp,"Center");		
		
		// 이 프레임만 종료시키기 위한 이벤트 등록
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		this.setVisible(true);
	}	

	public static void updateMemberList(String memList) {	
		//들어오고 나오는 멤버들 갱신해주는 메서드
		members = LoginForm.memList.split(",");			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == onlyOnlineMember) {
			//접속한 회원만 보여주는 새로고침
			try {
				model.removeAllElements();
				
				for(int i = 1 ; i < members.length ; i++) {
					if(LoginForm.myNickName.equals(members[i])) {
						members[i] = "나";
					}
					model.addElement(members[i]);
				}				
				
				memberList.setModel(model);
				
				memberList.update(getGraphics());
			}catch(Exception e2) {
				System.out.println(e2);
			}		
		}else if(e.getSource() == deleteMember) {
			//회원탈퇴
			int result = JOptionPane.showConfirmDialog(null, "정말 탈퇴하시겠습니까?","회원탈퇴",JOptionPane.YES_NO_OPTION);
			System.out.println(result);
			if(result == 0) {
				//예 (정말 회원탈퇴)
				queryDeleteMember("deleteMember," + LoginForm.myNickName);
				System.exit(0);
			}
		}
	}
	
	public void queryDeleteMember(String message) {
		LoginForm.pw.println(message);
		LoginForm.pw.flush();
	}
	
	public static void updateMessage(String messageNew) {
		newMessage = messageNew;
		
		if(!newMessage.equals(pre_Message)) {
			String[] strArr = newMessage.split(",");
			
			String from = strArr[1];
			String msg = strArr[2];
			
			pre_Message = newMessage;
			new ChattingDialog(from, msg, true);		
		}
	}
}

class ChattingDialog extends JFrame{
	JTextArea ta = new JTextArea();
	JButton btSend;
	
	public ChattingDialog(String from, String message, boolean a) {
		super(from + "님의 메시지 입니다.");
		this.setBounds(600,400, 350, 300);
		this.setLayout(new BorderLayout());
		message = message.replaceAll("0n", System.lineSeparator());
		ta.setText(message);
		ta.setEditable(false);
		this.add(ta,"Center");		
		btSend = new JButton("답장하기");
		this.add(btSend, "South");
		
		this.addWindowListener(new WindowAdapter() {	//클래스 띄웠다가 없애기
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				dispose();
			}
		});
		
		btSend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChattingDialog(LoginForm.myNickName, from);
				setVisible(false);
				dispose();				
			}
		});
		this.setVisible(true);
	}
	
	public ChattingDialog(String from, String to) {
		//모드1 : 보내기모드
		//모드2 : 받기 모드		
		super(to + "님에게 메시지를 전달합니다.");
		this.setBounds(600,400, 350, 300);
		this.setLayout(new BorderLayout());
		this.add(ta,"Center");		
		btSend = new JButton("보내기");
		this.add(btSend, "South");
		
		this.addWindowListener(new WindowAdapter() {	//클래스 띄웠다가 없애기
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				dispose();
			}
		});
		
		btSend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				queryMessage("messageSend," + from + "," +to + "," + ta.getText());
				setVisible(false);
				dispose();				
			}
		});
		this.setVisible(true);
	}
	
	public void queryMessage(String message) {
		message = message.replaceAll("\\r\\n|\\r|\\n", "0n");
		System.out.println(message);
		LoginForm.pw.println(message);
		LoginForm.pw.flush();
	}
}
