package j0622;

//신입=>부서배치->팀장에 정보=>상속기법
//형식) class  자식클래스명 extends 부모클래스명
class Manager extends Employee{
	/* 눈에는 보이지 않지만 가지고 있다.
	String name;
	int age;
	String sung;
	String addr;
	long salary;
	*/
	String department;//부서명
	//객체배열->기본배열(int su[]),객체(Employee)만 따로 저장한 배열
	Employee sub[];
	//생성자는 상속이 안되기에 
	public Manager() {}
	public Manager(String name, int age, String sung,long salary, String addr,
			                String department) {
		this.name=name;
		this.age=age;
		this.sung=sung;
		this.salary=salary;
		this.addr=addr;
		//추가
		this.department=department;
		// TODO Auto-generated constructor stub
	}
	//추가
	 //보너스 (내용을 다시 한번 써주는 기법)
		double bonus() {
			return salary*3.0; 
		} 
	 //--------------------------------------------
}

public class Company {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       //1.부하직원 3명 합격자->총무부
		Employee e1=new Employee("홍길동",23,"남","서울시 강남구",1500);
		Employee e2=new Employee("이길수",35,"남","대전시 중구",1700);
		Employee e3=new Employee("임시",30,"여","부산시~",1800);
		
		//2.영업부에 배치->팀장
		Manager m1=new Manager("테스트",42,"남",2500,"서울시 강남구","영업부");
		//3.부서배치=>자료형 배열명=new 자료형[갯수]
		m1.sub=new Employee[3];
		m1.sub[0]=e1; //e1.name
		m1.sub[1]=e2;
		m1.sub[2]=e3;
		System.out.println("e1=>"+e1+",m1.sub[0]=>"+m1.sub[0]);
		//4.부하직원,팀장 정보출력
		for(int i=0;i<m1.sub.length;i++) {
			//System.out.println("직원이름=>"+m1.sub[i].name);//e1.name
			//System.out.println("직원나이=>"+m1.sub[i].age);//e1.age
			System.out.println("================");
			m1.sub[i].display();
		}
		//팀장 1명
		m1.display();
		System.out.println("팀장의 부서명=>"+m1.department);
	}
}





