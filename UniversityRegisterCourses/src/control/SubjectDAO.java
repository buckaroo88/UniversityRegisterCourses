package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.SubjectVO;

public class SubjectDAO {
	// 학과 목록
	public ArrayList<SubjectVO> getSubjectTotalList() throws Exception {
		ArrayList<SubjectVO> list = new ArrayList<>();

		String sql = "select * from subject order by no";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SubjectVO sVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sVo = new SubjectVO();
				sVo.setNo(rs.getInt("no"));
				sVo.setS_num(rs.getString("s_num"));
				sVo.setS_name(rs.getString("s_name"));

				list.add(sVo);
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

	// 학과 등록
	public void getSubjectRegiste(SubjectVO svo) throws Exception {
		String sql = "insert into subject " + "(no, s_num, s_name)" + "values" + "(subject_seq.nextval, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, svo.getS_num());
			pstmt.setString(2, svo.getS_name());

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("학과 등록");
				alert.setHeaderText(svo.getS_name() + " 학과 등록 완료.");
				alert.setContentText("학과 등록 성공!!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("학과 등록");
				alert.setHeaderText("학과 등록 실패.");
				alert.setContentText("학과 등록 실패!!!");
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

	// 데이터 베이스에서 학과 테이블의 컬럼의 갯수
	public ArrayList<String> getSubjectColumnName() throws Exception {
		ArrayList<String> columnName = new ArrayList<String>();

		String sql = "select * from subject";
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
				columnName.add(rsmd.getColumnClassName(i));
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

	// 학과 수정
	public boolean getSubjectUpdate(int no, String s_num, String s_name) throws Exception {
		String sql = "update subject set s_num=?, s_name=? where no=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean subjectUpdateSucess = false;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, s_num);
			pstmt.setString(2, s_name);
			pstmt.setInt(3, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("학과 수정");
				alert.setHeaderText(s_name + " 학과 수정 완료.");
				alert.setContentText("학과 수정 성공!!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("학과 수정");
				alert.setHeaderText("학과 수정 실패.");
				alert.setContentText("학과 수정 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");

		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트르 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		}
		return subjectUpdateSucess;
	}

	// 학과 번호
	public String getSubjectNum(String s_name) throws Exception {
		String sql = "select s_num from subject where s_name=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String s_num = "";

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, s_name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				s_num = rs.getString("s_num");
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
		return s_num;
	}

	// 학과 삭제
	public boolean getSubjectDelete(int no) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from subject where no = ?");

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
				alert.setTitle("학과 삭제");
				alert.setHeaderText("학과 삭제 완료.");
				alert.setContentText("학과 삭제 성공!!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("학과 삭제");
				alert.setHeaderText("학과 삭제 실패.");
				alert.setContentText("학과 삭제 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 6) 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		}
		return subjectDeleteSucess;

	}
}
