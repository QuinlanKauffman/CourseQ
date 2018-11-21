package Overview;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BuildCourseList {
	
	public BuildCourseList() {
		//No-arg constructor
	}
	
	
	private String filePath = "C:\\Users\\qskau\\OneDrive\\Desktop\\CourseQ.xls";
	private ArrayList<Course> allCourses = new ArrayList<Course>(); 
	
	
	public void setCourses() {
		String sheetNm = "General Info";
		ArrayList<Course> allCourses = new ArrayList<Course>();
		
		FileInputStream fs = new FileInputStream(this.filePath);
		Workbook wb = Workbook.getWorkbook(fs);
		
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetNm);
		
		int totalNoOfRows = sh.getRows();

		String courseID;
		int creditHours;
		boolean availFall;
		boolean availSpring;
		boolean courseCompleted;
		boolean hasPrereqs;
		boolean hasCoreqs;
		
		for (int row = 3; row < totalNoOfRows; row++) {
			
			courseID = (String) sh.getCell(0, row).getContents();
			creditHours = (Integer) sh.getCell(1, row).getContents();
			availFall = (Boolean) sh.getCell(2, row).getContents();	
			availSpring = (Boolean) sh.getCell(3, row).getContents();
			courseCompleted = (Boolean) sh.getCell(4, row).getContents();
			hasPrereqs = (Boolean) sh.getCell(5, row).getContents();
			hasCoreqs = (Boolean) sh.getCell(6, row).getContents();
			
			Course c = new Course(courseID, creditHours, availSpring, availFall, courseCompleted,
					hasPrereqs,hasCoreqs);
			allCourses.add(c);
			
		}
				
		this.allCourses = allCourses;
	}
	
	
	public void callMajor() {
		ArrayList<ArrayList<ArrayList<Course>>> poss = new 
				ArrayList<ArrayList<ArrayList<Course>>>();
		
		Major m = new Major(this.allCourses);
		poss = m.getPossibilties(5, 18, 2);
		System.out.println(poss.size());
	}
}
