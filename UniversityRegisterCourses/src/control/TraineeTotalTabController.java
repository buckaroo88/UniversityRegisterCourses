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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TraineeVO;

public class TraineeTotalTabController implements Initializable{
	@FXML
	ComboBox<String> cbx_searchList;
	@FXML
	TextField txtSearchWord;
	@FXML
	Button btnSearch;
	@FXML
	Label lblCount;
	@FXML
	TableView<TraineeVO> traineeTotalTableView = new TableView<>();
	
	ObservableList<TraineeVO> traineeDataList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			cbx_searchList.setItems(FXCollections.observableArrayList("학번","과목명","학생이름"));
			
			// 수강 테이블 뷰 컬럼이름 설정
			TableColumn colNo = new TableColumn("NO.");
			colNo.setPrefWidth(50);
			colNo.setStyle("-fx-allignment: CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			
			TableColumn colSdNum = new TableColumn("학번");
			colSdNum.setPrefWidth(150);
			colSdNum.setStyle("-fx-allignment: CENTER");
			colSdNum.setCellValueFactory(new PropertyValueFactory<>("sd_num"));
			
			TableColumn colSdName = new TableColumn("학생이름");
			colSdName.setPrefWidth(150);
			colSdName.setStyle("-fx-allignment: CENTER");
			colSdName.setCellValueFactory(new PropertyValueFactory<>("sd_name"));
			
			TableColumn colLNum = new TableColumn("과목명");
			colLNum.setPrefWidth(150);
			colLNum.setStyle("-fx-allignment: CENTER");
			colLNum.setCellValueFactory(new PropertyValueFactory<>("l_num"));
			
			TableColumn colTSection = new TableColumn("과목구분");
			colTSection.setPrefWidth(150);
			colTSection.setStyle("-fx-allignment: CENTER");
			colTSection.setCellValueFactory(new PropertyValueFactory<>("t_section"));
			
			TableColumn colTDate = new TableColumn("등록 날짜");
			colTDate.setPrefWidth(250);
			colTDate.setStyle("-fx-allignment: CENTER");
			colTDate.setCellValueFactory(new PropertyValueFactory<>("t_date"));
			
			traineeTotalTableView.setItems(traineeDataList);
			traineeTotalTableView.getColumns().addAll(colNo, colSdNum, colSdName, colLNum, colTSection, colTDate);
			
			// 수강 전체 목록
			traineeTotalList();
			
			btnSearch.setOnAction(event->handlerBtnSearchAction(event));
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void handlerBtnSearchAction(ActionEvent event) {
		String search = "";
		search = cbx_searchList.getSelectionModel().getSelectedItem();
		
		TraineeVO tVo = new TraineeVO();
		TraineeDAO tDao = new TraineeDAO();
		
		String searchName="";
		boolean searchResult = false;
		
		searchName = txtSearchWord.getText().trim();
		
		try {
				if(searchName.equals("")||search.equals("")) {
					try {
						searchResult = true;
						Alert alert=new Alert(AlertType.WARNING);
						alert.setTitle("수강 정보 검색");
						alert.setHeaderText("수강 검색 정보를 입력하시오.");
						alert.setContentText("다음에는 주의하세요!");
						alert.showAndWait();
						
						traineeTotalList();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}else {
					ArrayList<String> title;
					ArrayList<TraineeVO> list =null;
					
					title = tDao.getTraineeColumnName();
					int columnCount = title.size();
					
					if(search.equals("학번")) {
						list = tDao.getTraineeStudentNumSearchList(searchName);
						
						if(list.size()==0) {
							txtSearchWord.clear();
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("학생 학번 정보 검색");
							alert.setHeaderText(searchName+" 학번의 수강 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요.");
							alert.showAndWait();
							
							list = tDao.getTraineeTotalList();
						}
					}
					if(search.equals("과목명")) {
						list = tDao.getTraineeSubjectSearchList(searchName);
						
						if(list.size()==0) {
							list=tDao.getTraineeTotalList();
						}
					}
					if(search.equals("학생이름")) {
						list = tDao.getTraineeStudentNameSearchList(searchName);
						
						if(list.size()==0) {
							txtSearchWord.clear();
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("학생 이름 정보 검색");
							alert.setHeaderText(searchName+" 이름의 수강 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요.");
							alert.showAndWait();
							
							list = tDao.getTraineeTotalList();
							
						}
					}
					txtSearchWord.clear();
					traineeDataList.removeAll(traineeDataList);
					
					int rowCount = list.size();
					lblCount.setText("검색 : "+rowCount+" 명");
					for(int index =0; index<rowCount; index++) {
						tVo=list.get(index);
						traineeDataList.add(tVo);
					}
					searchResult = true;
				}
				
				if(!searchResult) {
					txtSearchWord.clear();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("학생 정보 검색");
					alert.setHeaderText(searchName+" 학생이 리스트에 없습니다.");
					alert.setContentText("다시 검색하세요.");
					alert.showAndWait();
					
				}
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	// 수강 전체 리스트
	public void traineeTotalList() throws Exception{
		traineeDataList.removeAll(traineeDataList);
		
		TraineeDAO tDao = new TraineeDAO();
		TraineeVO tVo = null;
		ArrayList<String> title;
		ArrayList<TraineeVO> list;
		
		title =tDao.getTraineeColumnName();
		int columnCount=title.size();
		
		list =tDao.getTraineeTotalList();
		int rowCount=list.size();
		
		lblCount.setText("총원 : "+rowCount+" 명");
		for(int index = 0; index<rowCount;index++) {
			tVo=list.get(index);
			traineeDataList.add(tVo);
		}
	}
	
	


}
