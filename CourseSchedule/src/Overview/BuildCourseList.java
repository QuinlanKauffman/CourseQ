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
	
	
	public void setCourses() throws FileNotFoundException {
		String sheetNm = "General Info";
		String courseID = null;
		int creditHours = 0;
		boolean availFall = false;
		boolean availSpring = false;
		boolean courseCompleted = false;
		boolean hasPrereqs = false;
		boolean hasCoreqs = false;
		
		ArrayList<Course> allCourses = new ArrayList<Course>();
		FileInputStream fis = new FileInputStream(filePath);
		
		Workbook wb = getWorkbook(fis);
		
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetNm);
		Iterator<Row> rowIterator = sh.iterator();
		Row firstRow = sh.getRow(3);
		Iterator<Cell> cellIterator = firstRow.cellIterator();
				
		while (rowIterator.hasNext()) {
            Iterator cellIterator = row.cellIterator();

            //Iterating over each cell (column wise)  in a particular row.
            while (cellIterator.hasNext()) {

                Cell cell = (Cell) cellIterator.next();
                if (cell.getColumnIndex() == 0) {
                    courseID = cell.toString();
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
                	hasCoreqs = cell.getBooleanCellValue();
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
	
	
	private Workbook getWorkbook(FileInputStream fis) {
		// TODO Auto-generated method stub
		return null;
	}


	public void callMajor() {
		ArrayList<ArrayList<ArrayList<Course>>> poss = new 
				ArrayList<ArrayList<ArrayList<Course>>>();
		
		Major m = new Major(this.allCourses);
		poss = m.getPossibilties(5, 18, 2);
		System.out.println(poss.size());
	}
}
