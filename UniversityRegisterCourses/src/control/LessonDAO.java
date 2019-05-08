package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.LessonVO;

public class LessonDAO {
	// 과목 목록
	public ArrayList<LessonVO> getLessonTotalList() throws Exception {
		ArrayList<LessonVO> list = new ArrayList<>();

		String sql = "select * from lesson order by no";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LessonVO lVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				lVo = new LessonVO();
				lVo.setNo(rs.getInt("no"));
				lVo.setL_num(rs.getString("l_num"));
				lVo.setL_name(rs.getString("l_name"));

				list.add(lVo);
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

	// 과목 등록
	public void getLessonRegiste(LessonVO lvo) throws Exception {
		String sql = "insert into lesson " + "(no, l_num, l_name)" + " values" + "(lesson_seq.nextval, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, lvo.getL_num());
			pstmt.setString(2, lvo.getL_name());

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 등록");
				alert.setHeaderText(lvo.getL_name() + " 과목 등록 완료.");
				alert.setContentText("과목 등록 성공!!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("과목 등록");
				alert.setHeaderText("과목 등록 실패.");
				alert.setContentText("과목 등록 실패!!!");
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

	// 데이터베이스에서 과목 테이블의 컬럼의 갯수
	public ArrayList<String> getLessonColumnName() throws Exception {
		ArrayList<String> columnName = new ArrayList<String>();

		String sql = "select * from lesson";
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

	// 과목 수정
	public boolean getLessonUpdate(int no, String l_num, String l_name) throws Exception {
		String sql = "update lesson set l_num=?, l_name=? where no=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean lessonUpdateSucess = false;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, l_num);
			pstmt.setString(2, l_name);
			pstmt.setInt(3, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 수정");
				alert.setHeaderText(l_name + " 과목 수정 완료.");
				alert.setContentText("과목 수정 성공!!!");
				alert.showAndWait();
				lessonUpdateSucess = true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("과목 수정");
				alert.setHeaderText(l_name + " 과목 수정 실패.");
				alert.setContentText("과목 수정 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");

		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터 베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		}
		return lessonUpdateSucess;
	}

	// 과목 삭제
	public boolean getLessonDelete(int no) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from lesson where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean lessonDeleteSucess = false;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 삭제");
				alert.setHeaderText("과목 삭제 완료.");
				alert.setContentText("과목 삭제 성공!!!");
				alert.showAndWait();
				lessonDeleteSucess = true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("과목 삭제");
				alert.setHeaderText("과목 삭제 실패.");
				alert.setContentText("과목 삭제 실패!!!");
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
		return lessonDeleteSucess;
	}

}
