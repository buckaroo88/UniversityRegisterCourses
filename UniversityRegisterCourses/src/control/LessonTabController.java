package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.LessonVO;

public class LessonTabController implements Initializable {
	// 과목 등록 탭
	@FXML
	private TextField txtLessonNum;
	@FXML
	private TextField txtLessonName;
	@FXML
	private TableView<LessonVO> lessonTableView = new TableView<>();
	@FXML
	private Button btnLessonInsert; // 과목 등록
	@FXML
	private Button btnLessonUpdate; // 과목 수정
	@FXML
	private Button btnLessonDelete; // 과목 삭제

	ObservableList<LessonVO> lessonDataList = FXCollections.observableArrayList();
	ObservableList<LessonVO> selectLesson = null; // 테이블에서 선택한 정보 저장
	int selectedLessonIndex; // 테이블에서 선택한 과목 정보 인덱스 저장

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			// 과목 등록 초기화
			btnLessonUpdate.setDisable(true);
			btnLessonDelete.setDisable(true);
			lessonTableView.setEditable(false);

			// 과목 테이블 뷰 컬럼이름 설정
			TableColumn colLessonNo = new TableColumn("NO.");
			colLessonNo.setPrefWidth(50);
			colLessonNo.setStyle("-fx-allignment: CENTER");
			colLessonNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			TableColumn colLessonNum = new TableColumn("과목 번호");
			colLessonNum.setPrefWidth(90);
			colLessonNum.setStyle("-fx-allignment: CENTER");
			colLessonNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));
			TableColumn colLessonName = new TableColumn("과목명");
			colLessonName.setPrefWidth(160);
			colLessonName.setStyle("-fx-allignment: CENTER");
			colLessonName.setCellValueFactory(new PropertyValueFactory<>("l_name"));

			lessonTableView.setItems(lessonDataList);
			lessonTableView.getColumns().addAll(colLessonNo, colLessonNum, colLessonName);

			// 과목 전체 목록
			lessonTotalList();

			// 과목 키 이벤트 등록
			txtLessonNum.setOnKeyPressed(event -> handlerTxtLessonNumKeyPressed(event));
			// 과목 등록, 수정, 삭제 이벤트 등록
			btnLessonInsert.setOnAction(event -> handlerBtnLessonInsertActoion(event));
			btnLessonDelete.setOnAction(event -> handlerBtnLessonDeleteActoion(event));
			btnLessonUpdate.setOnAction(event -> handlerBtnLessonUpdateActoion(event));
			lessonTableView.setOnMouseClicked(event -> handlerLessonTableViewActoion(event));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 과목 등록 텍스트 필드 키 이벤트 핸들러
	public void handlerTxtLessonNumKeyPressed(KeyEvent event) {
		if (txtLessonNum.getText().length() >= 3) {
			txtLessonNum.clear();
		}
		if (event.getCode() == KeyCode.ENTER) {
			txtLessonName.requestFocus();
		}
	}

	// 과목 등록 이벤트 핸들러
	public void handlerBtnLessonInsertActoion(ActionEvent event) {
		try {
			lessonDataList.removeAll(lessonDataList);

			LessonVO lvo = null;
			LessonDAO ldao = null;

			lvo = new LessonVO(txtLessonNum.getText().trim(), txtLessonName.getText().trim());
			ldao = new LessonDAO();
			ldao.getLessonRegiste(lvo);

			if (ldao != null) {
				lessonTotalList();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("과목 입력");
				alert.setHeaderText(txtLessonName.getText() + " 과목이 성공적으로 추가 되었습니다.");
				alert.setContentText("다음 과목을 입력하세요.");
				alert.showAndWait();

				txtLessonNum.clear();
				txtLessonName.clear();
				txtLessonNum.requestFocus();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("과목 정보 입력");
			alert.setHeaderText("과목 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요.");
			alert.showAndWait();
		}
	}

	// 과목 삭제 이벤트 핸들러
	public void handlerBtnLessonDeleteActoion(ActionEvent event) {
		try {
			boolean sucess;

			LessonDAO ldao = new LessonDAO();
			sucess = ldao.getLessonDelete(selectedLessonIndex);
			if (sucess) {
				lessonDataList.removeAll(lessonDataList);
				lessonTotalList();

				txtLessonNum.clear();
				txtLessonName.clear();
				btnLessonInsert.setDisable(false);
				btnLessonUpdate.setDisable(true);
				btnLessonDelete.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 과목 수정 이벤트 핸들러
	public void handlerBtnLessonUpdateActoion(ActionEvent event) {
		try {
			boolean sucess;

			LessonDAO ldao = new LessonDAO();
			sucess = ldao.getLessonUpdate(selectedLessonIndex, txtLessonNum.getText().trim(),
					txtLessonName.getText().trim());
			if (sucess) {
				lessonDataList.removeAll(lessonDataList);
				lessonTotalList();

				txtLessonNum.clear();
				txtLessonName.clear();
				btnLessonInsert.setDisable(false);
				btnLessonUpdate.setDisable(true);
				btnLessonDelete.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 과목 테이블뷰 더블클릭 선택 이벤트 핸들러
	public void handlerLessonTableViewActoion(MouseEvent event) {
		if (event.getClickCount() == 2) {
			try {
				selectLesson = lessonTableView.getSelectionModel().getSelectedItems();
				selectedLessonIndex = selectLesson.get(0).getNo();
				String selectedL_num = selectLesson.get(0).getL_num();
				String selectedL_name = selectLesson.get(0).getL_name();

				txtLessonNum.setText(selectedL_num);
				txtLessonName.setText(selectedL_name);

				btnLessonUpdate.setDisable(false);
				btnLessonDelete.setDisable(false);
				btnLessonInsert.setDisable(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 과목 전체 목록
	public void lessonTotalList() throws Exception {
		lessonDataList.removeAll(lessonDataList);

		LessonDAO lDao = new LessonDAO();
		LessonVO lVo = null;
		ArrayList<String> title;
		ArrayList<LessonVO> list;

		title = lDao.getLessonColumnName();
		int columnCount = title.size();

		list = lDao.getLessonTotalList();
		int rowCount = list.size();

		for (int index = 0; index < rowCount; index++) {
			lVo = list.get(index);
			lessonDataList.add(lVo);
		}

	}

}
