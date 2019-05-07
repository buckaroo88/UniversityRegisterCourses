package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private Label lblLogin;
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnJoin;
	@FXML
	private Button btnLogin;
	@FXML
	private ToggleGroup loginGroup;
	@FXML
	private RadioButton rbManager;
	@FXML
	private RadioButton rbStudent;
	@FXML
	private Label lblIconImg;
	@FXML
	private ImageView iconImg;

	public static String managerName = "";
	public static String studentId = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtId.setOnKeyPressed(event -> handerTxtIdKeyPressed(event));
		txtPassword.setOnKeyPressed(event -> handerTxtPasswordKeyPressed(event));

		btnJoin.setOnAction(event -> handerBtnJoinAction(event)); // 관리자 등록창으로 전환
		btnLogin.setOnAction(event -> handlerBtnLoginActoion(event)); // 로그인
		btnCancel.setOnAction(event -> handlerBtnCancelActoion(event)); // 로그인창 닫기
		rbManager.setOnAction(event -> handlerRbManagerActoion(event));
		rbStudent.setOnAction(event -> hadlerRbStudentActoion(event));

	}

	// 학생 라디오 버튼 이벤트 핸들러
	private void hadlerRbStudentActoion(ActionEvent event) {
		URL srtImg = getClass().getResource("../image/student.png");
		Image image = new Image(srtImg.toString());
		iconImg.setImage(image);
		lblLogin.setText("학생 로그인");
		btnJoin.setDisable(true);
		btnLogin.setText("학생 로그인");
	}

	// 관리자 라디오 버튼 이벤트 핸들러
	private void handlerRbManagerActoion(ActionEvent event) {
		URL srtImg = getClass().getResource("../image/manager.png");
		Image image = new Image(srtImg.toString());
		iconImg.setImage(image);
		lblLogin.setText("관리자 로그인");
		btnJoin.setDisable(true);
		btnLogin.setText("관리자 로그인");
	}

	// 아이디 입력에서 Enter키 이벤트 적용
	private void handerTxtIdKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			txtPassword.requestFocus();
		}
	}

	// 패스워드 입력에서 Enter키 이벤트 적용
	private void handerTxtPasswordKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			login();
		}
	}

	// 관리자 등록창으로 전환
	private void handerBtnJoinAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/join.fxml"));
			Parent mainView = (Parent) loader.load();
			Scene scane = new Scene(mainView);
			Stage mainMtage = new Stage();
			mainMtage.setTitle("관리자 등록");
			mainMtage.setScene(scane);
			Stage oldStage = (Stage) btnLogin.getScene().getWindow();
			oldStage.close();
			mainMtage.show();
		} catch (IOException e) {
			System.err.println("오류" + e);
		}
	}

	// 로그인창 닫기
	private void handlerBtnCancelActoion(ActionEvent event) {
		Platform.exit();
	}

	// 로그인
	public void handlerBtnLoginActoion(ActionEvent event) {
		login();
	}

	// 로그인 메소드
	public void login() {
		LoginDAO login = new LoginDAO();
		StudentDAO sLogin = new StudentDAO();
		boolean sucess = false;

		try {
			if ("manager".equals(loginGroup.getSelectedToggle().getUserData().toString())) {
				managerName = managerLoginName();
				sucess = login.getLogin(txtId.getText().trim(), txtPassword.getText().trim());
				if (sucess) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
						Parent mainView = (Parent) loader.load();
						Scene scane = new Scene(mainView);
						Stage mainMtage = new Stage();
						mainMtage.setTitle("미래 대학교 학사관리");
						mainMtage.setResizable(false);
						mainMtage.setScene(scane);
						Stage oldStage = (Stage) btnLogin.getScene().getWindow();
						oldStage.close();
						mainMtage.show();
					} catch (IOException e) {
						System.err.println("오류" + e);
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("로그인 실패");
					alert.setHeaderText("아이디와 비밀번호 불일치.");
					alert.setContentText("아이디와 비밀번호가 일치하지 않습니다." + "\n 다시 제대로 입력하시오.");

					alert.setResizable(false);
					alert.showAndWait();
					txtId.clear();
					txtPassword.clear();
				}

			} else if ("student".contentEquals(loginGroup.getSelectedToggle().getUserData().toString())) {
				sucess = sLogin.getLogin(txtId.getText().trim(), txtPassword.getText().trim());
				if (sucess) {
					try {
						studentId = txtId.getText();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/trainee.fxml"));
						Parent mainView = (Parent) loader.load();
						Scene scane = new Scene(mainView);
						Stage mainMtage = new Stage();
						mainMtage.setResizable(false);
						mainMtage.setScene(scane);
						Stage oldStage = (Stage) btnLogin.getScene().getWindow();
						oldStage.close();
						mainMtage.show();
					} catch (IOException e) {
						System.err.println("오류" + e);
					}
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("로그인 실패");
					alert.setHeaderText("아이디와 비밀번호 불일치.");
					alert.setContentText("아이디와 비밀번호가 일치하지 않습니다." + "\n 다시 제대로 입력하시오.");

					alert.setResizable(false);
					alert.showAndWait();
					txtId.clear();
					txtPassword.clear();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (txtId.getText().contentEquals("") || txtPassword.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("로그인 실패");
			alert.setHeaderText("아이디와 비밀번호 미입력.");
			alert.setContentText("아이디와 비밀번호중 입력하지 않은 항목이 있습니다." + "\n 다시 제대로 입력하시오.");

			alert.setResizable(false);
			alert.showAndWait();
		}
	}

	public String managerLoginName() {
		StudentDAO sdao = new StudentDAO();
		String name = null;
		try {
			name = sdao.getLoginName(txtId.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public String studentLoginName() {
		StudentDAO sdao = new StudentDAO();
		String name = null;
		try {
			name = sdao.getLoginName(txtId.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
}
