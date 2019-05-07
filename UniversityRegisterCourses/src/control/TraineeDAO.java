package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.StudentVO;
import model.TraineeVO;

public class TraineeDAO {
	// 로그인한 학생의 정보
	public StudentVO getStudentSubjectName(String sd_id) throws Exception{
		String sql = "select sd_num, sd_name, (select s_name from subject where s_num = "
				+ "(select s_num from student where sd_id = ?)) as s_num from student where sd_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentVO studentInfo = null;
		
		try {
				con = DBUtil.getConnection();
				pstmt =con.prepareStatement(sql);
				pstmt.setString(1, sd_id);
				pstmt.setString(2, sd_id);
				rs= pstmt.executeQuery();
				
				if(rs.next()) {
					studentInfo = new StudentVO();
					studentInfo.setSd_num(rs.getString("sd_num"));
					studentInfo.setSd_name(rs.getString("sd_name"));
					studentInfo.setS_num(rs.getString("s_num"));
				}
		}catch(SQLException se) {
			System.out.println(se);
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
					if(rs!=null)
						rs.close();
					if(pstmt!=null)
						pstmt.close();
					if(con!=null)
						con.close();
			}catch(SQLException se) {
				
			}
		}
		return studentInfo;
	}
	// 선택한 과목명의 과목 번호
	public String getLessonNum(String lessonName) throws Exception{
		String l_num ="";
		
		String sql ="select l_num from lesson where l_name=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, lessonName);
				rs= pstmt.executeQuery();
				
				if(rs.next()) {
					l_num=rs.getString("l_num");
				}else {
					Alert alert=new Alert(AlertType.WARNING);
					alert.setTitle("수강 과목의 과목 번호");
					alert.setHeaderText("선택한 "+lessonName+" 과목의 과목번호가 없습니다.");
					alert.setContentText("과목 검색 실패");
					alert.showAndWait();
					
				}
		}catch(SQLException se) {
			System.out.println(se);
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
					if(rs!=null)
						rs.close();
					if(pstmt!=null)
						pstmt.close();
					if(con!=null)
						con.close();
			}catch(SQLException se) {
				
			}
		}
		return l_num;
		
	}
	
	// 수강 신청
	public void getTraineeRegiste(TraineeVO tvo) throws Exception{
		String sql = "insert into trainee "+"(no,sd_num, l_num, t_section, t_date)" +" values"
					+"(trainee_seq.nextval, ?, ?, ?, sysdate)";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtill.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tvo.getSd_num());
			pstmt.setString(2, tvo.getL_num());
			pstmt.setString(3, tvo.getT_section());
		}
	}
}
