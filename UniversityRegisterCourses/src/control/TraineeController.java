package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.StudentVO;
import model.TraineeVO;

public class TraineeController implements Initializable {
// 메뉴
	@FXML
	private MenuItem menuExit;
	@FXML
	private MenuItem menuLogout;
	@FXML
	private MenuItem menuInfo;
// 수강 신청 탭
	@FXML
	private TextField txtStudentName;
	@FXML
	private TextField txtStudentNum;
	@FXML
	private TextField txtSubjectName;
	@FXML
	private RadioButton rbMajor;
	@FXML
	private RadioButton rbMinor;
	@FXML
	private RadioButton rbCulture;
	@FXML
	private ComboBox<String> cbx_subjectName;
	@FXML
	private TextField txtSectionName;
	@FXML
	private Button btnTraineeInsert;
	@FXML
	private Button btnTraineeCancel;
	@FXML
	private Button btnTraineeExit;
	@FXML
	private TableView<TraineeVO> traineeTableView = new TableView<>();
	ObservableList<TraineeVO> traineeDataList = FXCollections.observableArrayList();
	ObservableList<TraineeVO> selectTrainee = null; // 테이블에서 선택한 정보 저장
	int selectedTraineeIndex; // 테이블에서 선택한 수강 신청 인덱스 저장
	String studentLoginId; // 로그인 아이디
	String l_num; // 과목 번호
	String t_section; // 과목 구분
	String sd_num; // 로그인한 학생의 학번

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			studentLoginId = LoginController.studentId;
			if (!studentLoginId.equals("")) {
				StudentVO sVo = new StudentVO();
				TraineeDAO tDao = new TraineeDAO();
				sVo = tDao.getStudentSubjectName(studentLoginId);
				txtStudentNum.setText(sVo.getSd_num());
				txtStudentName.setText(sVo.getSd_name());
				txtSubjectName.setText(sVo.getS_num());
				sd_num = txtStudentNum.getText().trim();
				btnTraineeCancel.setDisable(true);
				traineeTableView.setEditable(false);
// 학생 정보 수정 금지
				txtStudentName.setEditable(false);
				txtStudentNum.setEditable(false);
				txtSubjectName.setEditable(false);
				txtSectionName.setEditable(false);
// 수강 테이블 뷰 컬럼이름 설정
				TableColumn colNo = new TableColumn("NO.");
				colNo.setPrefWidth(50);
				colNo.setStyle("-fx-allignment: CENTER");
				colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
				TableColumn colSdNum = new TableColumn("학번");
				colSdNum.setPrefWidth(90);
				colSdNum.setStyle("-fx-allignment: CENTER");
				colSdNum.setCellValueFactory(new PropertyValueFactory<>("sd_num"));
				TableColumn colLNum = new TableColumn("과목명");
				colLNum.setPrefWidth(100);
				colLNum.setStyle("-fx-allignment: CENTER");
				colLNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));
				TableColumn colTSection = new TableColumn("과목 구분");
				colTSection.setPrefWidth(100);
				colTSection.setStyle("-fx-allignment: CENTER");
				colTSection.setCellValueFactory(new PropertyValueFactory<>("t_section"));
				TableColumn colTDate = new TableColumn("등록 날짜");
				colTDate.setPrefWidth(160);
				colTDate.setStyle("-fx-allignment: CENTER");
				colTDate.setCellValueFactory(new PropertyValueFactory<>("t_date"));
				traineeTableView.setItems(traineeDataList);
				traineeTableView.getColumns().addAll(colNo, colSdNum, colLNum, colTSection, colTDate);
// 수강 전체 목록
				traineeTotalList();
// 메뉴 이벤트 등록
				menuExit.setOnAction(event -> handlerMenuExitAction(event));
				menuLogout.setOnAction(event -> handlerMenuLogoutAction(event));
				menuInfo.setOnAction(event -> handlerMenuInfoAction(event));
// 수강 과목 선택 이벤트
				rbMajor.setOnAction(event -> handlerRbMajorAction(event));
				rbMinor.setOnAction(event -> handlerRbMinorAction(event));
				rbCulture.setOnAction(event -> handlerRbCultureAction(event));
				cbx_subjectName.setOnAction(event -> handlerCbx_subjectNameAction(event));
// 수강 등록, 삭제 이벤트 등록
				btnTraineeInsert.setOnAction(event -> handlerBtnTraineeInsertActoion(event));
				btnTraineeCancel.setOnAction(event -> handlerBtnTraineeCancelActoion(event));
				btnTraineeExit.setOnAction(event -> handlerBtnTraineeExitActoion(event));
				traineeTableView.setOnMouseClicked(event -> handlerTraineeTableViewActoion(event));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//수강 과목 선택 이벤트 핸들러
	public void handlerRbCultureAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("국어", "수학", "영어", "역사"));
		t_section = rbCulture.getText();
	}

	public void handlerRbMinorAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("교육학이론"));
		t_section = rbMinor.getText();
	}

	public void handlerRbMajorAction(ActionEvent event) {
		cbx_subjectName.setItems(FXCollections.observableArrayList("프로그래밍", "데이터베이스"));
		t_section = rbMajor.getText();
	}

	public void handlerCbx_subjectNameAction(ActionEvent event) {
		txtSectionName.setText(cbx_subjectName.getSelectionModel().getSelectedItem());
		selectLessonNameToLessonNum();
	}

//수강 신청한 과목명의 과목 번호
	public void selectLessonNameToLessonNum() {
		try {
			TraineeDAO tDao = new TraineeDAO();
			if (txtSectionName.getText() != null) {
				l_num = tDao.getLessonNum(txtSectionName.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handlerBtnTraineeInsertActoion(ActionEvent event) {
		try {
			traineeDataList.removeAll(traineeDataList);
			TraineeVO tvo = null;
			TraineeDAO tdao = null;
			tvo = new TraineeVO(txtStudentNum.getText().trim(), l_num, t_section);
			tdao = new TraineeDAO();
			tdao.getTraineeRegiste(tvo);
			if (tdao != null) {
				traineeTotalList();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("수강 신청");
				alert.setHeaderText(txtStudentName.getText() + " 수강 신청이 되었습니다..");
				alert.setContentText("다른 과목 수강 신청를 하세요");
				alert.showAndWait();
				txtSectionName.clear();
				l_num = "";
				t_section = "";
				rbCulture.setSelected(false);
				rbMajor.setSelected(false);
				rbMinor.setSelected(false);
				cbx_subjectName.setItems(FXCollections.observableArrayList("선택"));
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("학과 정보 입력");
			alert.setHeaderText("학과 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

// 수강 신청 취소
	public void handlerBtnTraineeCancelActoion(ActionEvent event) {
		try {
			boolean sucess;
			TraineeDAO tdao = new TraineeDAO();
			sucess = tdao.getTraineeDelete(selectedTraineeIndex);
			if (sucess) {
				traineeDataList.removeAll(traineeDataList);
				traineeTotalList();
				btnTraineeCancel.setDisable(true);
				btnTraineeInsert.setDisable(false);
				txtSectionName.clear();
				l_num = "";
				t_section = "";
				rbCulture.setSelected(false);
				rbMajor.setSelected(false);
				rbMinor.setSelected(false);
				cbx_subjectName.setItems(FXCollections.observableArrayList("선택"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

// 메뉴 이벤트 핸들러
	public void handlerMenuLogoutAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
			Parent mainView = (Parent) loader.load();
			Scene scane = new Scene(mainView);
			Stage mainMtage = new Stage();
			mainMtage.setTitle("미래 대학교 학사관리");
			mainMtage.setResizable(false);
			mainMtage.setScene(scane);
			Stage oldStage = (Stage) btnTraineeExit.getScene().getWindow();
			oldStage.close();
			mainMtage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handlerMenuInfoAction(ActionEvent event) {
		Alert alert;
		alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("미래 대학교");
		alert.setHeaderText("미래 대학교 수강신청 프로그램");
		alert.setContentText("Future Universit Register Courses Version 0.01");
		alert.setResizable(false);
		alert.showAndWait();
	}

	public void handlerMenuExitAction(ActionEvent event) {
		Platform.exit();
	}

	public void handlerBtnTraineeExitActoion(ActionEvent event) {
		Platform.exit();
	}

// 수강 테이블뷰 더블클릭 선택 이벤트 핸들러
	public void handlerTraineeTableViewActoion(MouseEvent event) {
		if (event.getClickCount() == 2) {
			try {
				selectTrainee = traineeTableView.getSelectionModel().getSelectedItems();
				selectedTraineeIndex = selectTrainee.get(0).getNo();
				String selectedL_num = selectTrainee.get(0).getL_num();
				txtSectionName.setText(selectedL_num);
				btnTraineeCancel.setDisable(false);
				btnTraineeInsert.setDisable(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

// 수강 전체 리스트
	public void traineeTotalList() throws Exception {
		TraineeDAO tDao = new TraineeDAO();
		TraineeVO tVo = null;
		ArrayList<String> title;
		ArrayList<TraineeVO> list;
		title = tDao.getTraineeColumnName();
		int columnCount = title.size();
		list = tDao.getTraineeTotalList(sd_num);
		int rowCount = list.size();
		for (int index = 0; index < rowCount; index++) {
			tVo = list.get(index);
			traineeDataList.add(tVo);
		}
	}
}
