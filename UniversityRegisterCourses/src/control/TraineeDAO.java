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
	public StudentVO getStudentSubjectName(String sd_id) throws Exception {
		String sql = "select sd_num, sd_name, (select s_name from subject where s_num = "
				+ "(select s_num from student where sd_id = ?)) as s_num from student where sd_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StudentVO studentInfo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sd_id);
			pstmt.setString(2, sd_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				studentInfo = new StudentVO();
				studentInfo.setSd_num(rs.getString("sd_num"));
				studentInfo.setSd_name(rs.getString("sd_name"));
				studentInfo.setS_num(rs.getString("s_num"));
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {

			}
		}
		return studentInfo;
	}

	// 선택한 과목명의 과목 번호
	public String getLessonNum(String lessonName) throws Exception {
		String l_num = "";

		String sql = "select l_num from lesson where l_name=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, lessonName);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				l_num = rs.getString("l_num");
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 과목의 과목 번호");
				alert.setHeaderText("선택한 " + lessonName + " 과목의 과목번호가 없습니다.");
				alert.setContentText("과목 검색 실패");
				alert.showAndWait();

			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {

			}
		}
		return l_num;

	}

	// 수강 신청
	public void getTraineeRegiste(TraineeVO tvo) throws Exception {
		String sql = "insert into trainee " + "(no,sd_num, l_num, t_section, t_date)" + " values"
				+ "(trainee_seq.nextval, ?, ?, ?, sysdate)";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tvo.getSd_num());
			pstmt.setString(2, tvo.getL_num());
			pstmt.setString(3, tvo.getT_section());

			int i = pstmt.executeUpdate();
			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("수강 신청");
				alert.setHeaderText("수강 신청 완료");
				alert.setContentText("수강 신청 성공!!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("수강 신청");
				alert.setHeaderText("수강 신청 실패");
				alert.setContentText("수강 신청 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		}
	}

	// 로그인 한 학생 수강 신청 전체 목록(학번으로 검색)
	public ArrayList<TraineeVO> getTraineeTotalList(String sd_num) throws Exception {
		ArrayList<TraineeVO> list = new ArrayList<>();

		String sql = "select tr.no as no, sd_num, le.l_name as l_num, t_section, t_date "
				+ "from trainee tr, lesson le " + "where tr.l_num = le.l_num and tr.sd_num = ? " + "order by t_date";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TraineeVO tVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sd_num);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tVo = new TraineeVO();
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_section"));
				tVo.setT_date(rs.getString("t_date"));

				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	public ArrayList<String> getTraineeColumnName() throws Exception {
		ArrayList<String> columnName = new ArrayList<String>();

		String sql = "select * from trainee";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// ResultSetMetaData 객체 변수 선언
		ResultSetMetaData rsmd = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return columnName;
	}

	// 수강 신청 삭제
	public boolean getTraineeDelete(int no) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from trainee where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean subjectDeleteSucess = false;
		try {

			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("수강 신청 취소");
				alert.setHeaderText("수강 신청 취소 완료.");
				alert.setContentText("수강 신청 취소 성공!!!");
				alert.showAndWait();
				subjectDeleteSucess = true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수강 신청 취소");
				alert.setHeaderText("수강 신청 취소 실패.");
				alert.setContentText("수강 신청 취소 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return subjectDeleteSucess;
	}

	// 수강 신청 전체 목록
	public ArrayList<TraineeVO> getTraineeTotalList() throws Exception {
		ArrayList<TraineeVO> list = new ArrayList<>();

		String sql = "select tr.no as no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name,t_section, t_date "
				+ "from trainee tr, lesson le , student st "
				+ "where tr.l_num = le.l_num and tr.sd_num = st.sd_num order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TraineeVO tVo = null;
		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tVo = new TraineeVO();
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_section"));
				tVo.setT_date(rs.getString("t_date"));

				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	// 전체 수강 탭에서 학번 검색
	public ArrayList<TraineeVO> getTraineeStudentNumSearchList(String sd_num) throws Exception {
		ArrayList<TraineeVO> list = new ArrayList<>();

		String sql = "select tr.no as no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name,t_section, t_date "
				+ "from trainee tr, lesson le , student st "
				+ "where tr.l_num = le.l_num and tr.sd_num = st.sd_num and tr.sd_num = ? order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TraineeVO tVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sd_num);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tVo = new TraineeVO();
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("t_section"));
				tVo.setT_date(rs.getString("t_date"));
				list.add(tVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;

	}

	// 전체 수강 탭에서 과목 검색
	public ArrayList<TraineeVO> getTraineeSubjectSearchList(String l_name) throws Exception {
		String l_num = getLessonNum(l_name);

		ArrayList<TraineeVO> list = new ArrayList<>();

		String sql = "select tr.no, tr.sd_num, le.l_name as l_num, st.sd_name as sd_name, t_section, t_date"
				+ "from trainee tr, lesson le, student st "
				+ "where tr.l_num + le.l_num and tr.l_num = ? and tr.sd_num = st.sd_num order by t_date";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TraineeVO tVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, l_num);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tVo = new TraineeVO();
				tVo.setNo(rs.getInt("no"));
				tVo.setSd_num(rs.getString("sd_num"));
				tVo.setSd_name(rs.getString("sd_name"));
				tVo.setL_num(rs.getString("l_num"));
				tVo.setT_section(rs.getString("T_section"));
				tVo.setT_date(rs.getString("t_date"));

				list.add(tVo);

			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {

			}
		}
		return list;
	}

	// 전체 수강 탭에서 학생 이름 검색
	public ArrayList<TraineeVO> getTraineeStudentNameSearchList(String sd_name) throws Exception{
		ArrayList<TraineeVO> list = new ArrayList<>();
		
		String sql = "select tr.no as no, tr.sd_num, le.l_num, st.sd_name as sd_name, t_section, t_date"
					+ "from trainee tr, lesson le, student st"
					+ "where tr.l_num = le.l_num and tr.sd_num and st.sd_name = ? order by t_date";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		TraineeVO tVo = null;
		try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, sd_name);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					tVo = new TraineeVO();
					tVo.setNo(rs.getInt("no"));
					tVo.setSd_num(rs.getString("sd_num"));
					tVo.setSd_name(rs.getString("sd_name"));
					tVo.setL_num(rs.getString("l_num"));
					tVo.setT_section(rs.getString("t_section"));
					tVo.setT_date(rs.getString("t_date"));
					
					list.add(tVo);
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
		return list;
	}
}
