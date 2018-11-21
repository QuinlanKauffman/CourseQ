package Overview;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
	
	
	public void setCourses() throws IOException {
		String sheetNm = "General Info";
		String courseID = null;
		int creditHours = 0;
		boolean availFall = false;
		boolean availSpring = false;
		boolean courseCompleted = false;
		boolean hasPrereqs = false;
		boolean hasCoreqs = false;
		
		ArrayList<Course> allCourses = new ArrayList<Course>();
		
		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(this.filePath));
        HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
		HSSFSheet sh = wb.getSheet(sheetNm);
				
		
		for(Row row : sh) {
			for (Cell cell : row) {
				if (cell.getRowIndex() <=2)
					continue;
				
				if (cell.getColumnIndex() == 0) {
					courseID = cell.toString();
					System.out.println(courseID);
				}
				
				if (cell.getColumnIndex() == 1) {
					creditHours = (int) cell.getNumericCellValue();
				}
				
				if (cell.getColumnIndex() == 2) {
					availFall = cell.getBooleanCellValue();
				}
				
				if (cell.getColumnIndex() == 3) {
					availSpring = cell.getBooleanCellValue();
				}
				
				if (cell.getColumnIndex() == 4) {
					courseCompleted = cell.getBooleanCellValue();
				}
				
				if (cell.getColumnIndex() == 5) {
					hasPrereqs = cell.getBooleanCellValue();
				}
				
				if (cell.getColumnIndex() == 6) {
					hasCoreqs = cell.getBooleanCellValue();
				}
			}
		

            Course c = new Course(courseID, creditHours, availSpring, availFall, courseCompleted,
					hasPrereqs,hasCoreqs);
			allCourses.add(c);
			
		}
				
		this.allCourses = allCourses;
	}
	
	 
	


	public void callMajor() throws IOException {
		ArrayList<ArrayList<ArrayList<Course>>> poss = new 
				ArrayList<ArrayList<ArrayList<Course>>>();
		
		setCourses();
		System.out.println(this.allCourses);
		Major m = new Major(this.allCourses);
		//poss = m.getPossibilties(5, 18, 2);
		//System.out.println(poss.size());
	}
}
