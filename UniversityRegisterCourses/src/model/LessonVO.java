package model;

public class LessonVO {
	int no;
	String l_num;
	String l_name;
	
	public LessonVO() {
		super();
	}

	public LessonVO(String l_num, String l_name) {
		super();
		this.l_num = l_num;
		this.l_name = l_name;
	}

	public LessonVO(int no, String l_num, String l_name) {
		super();
		this.no = no;
		this.l_num = l_num;
		this.l_name = l_name;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getL_num() {
		return l_num;
	}

	public void setL_num(String l_num) {
		this.l_num = l_num;
	}

	public String getL_name() {
		return l_name;
	}

	public void setL_name(String l_name) {
		this.l_name = l_name;
	}
	
		
}
