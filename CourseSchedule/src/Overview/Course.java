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


public class Course {

	private String filePath = "C:\\Users\\qskau\\OneDrive\\Desktop\\CourseQ.xls";
	private String courseID;
	private int creditHours;
	private boolean availableFall;
	private boolean availableSpring;
	private boolean courseCompleted;
	private int numberOfPrereqs;
	private int numberOfCoreqs;
	
	
	private ArrayList<String> Prereqs = new ArrayList<String>();
	private ArrayList<String> Coreqs = new ArrayList<String>();
	
	
	public void setcourseID(String courseID){
		this.courseID = courseID;}
	 
	public String getcourseID(){
		return this.courseID;}
	
	public boolean getcourseCompleted(){
		return this.courseCompleted;}
	
	public void setcourseCompleted(boolean courseCompleted) {
		this.courseCompleted = courseCompleted;}
	
	public int getnumberOfPrereqs() {
		//No setter, only a getter
		this.numberOfPrereqs = Prereqs.size();
		return Prereqs.size();}
		
	public int getnumberOfCoreqs(){
		//No setter, only a getter
		this.numberOfCoreqs = Coreqs.size();
		return (Coreqs.size()+1);}
	
	public void setcreditHours(int creditHours){
		this.creditHours = creditHours;}
	
	public int getcreditHours(){
		return this.creditHours;}
	
	public boolean getfallAvailability(){
		return this.availableFall;}
	
	public boolean getspringAvailability(){
		return this.availableSpring;}
	
	public void setfallAvailability(boolean availableFall) {
		this.availableFall = availableFall;}
	
	public void setspringAvailability(boolean availableSpring) {
		this.availableSpring = availableSpring;}
	
	public Course(String courseID, int creditHours, boolean availFall, boolean availSpring,
			boolean courseCompleted, boolean hasPrereqs, boolean hasCoreqs){
		this.courseID = courseID;
		this.creditHours = creditHours;
		this.availableFall = availFall;
		this.availableSpring = availSpring;
		this.courseCompleted = courseCompleted;
		this.numberOfPrereqs = 0;
		this.numberOfCoreqs = 0;
		
		if (hasPrereqs == true) {
			this.setPrereqs();
			this.numberOfPrereqs = getPrereqs().size();
		}
		
		if (hasPrereqs == true) {
			this.setCoreqs();
			this.numberOfPrereqs = getCoreqs().size();
		}
		
	}
	
	
	private void setPrereqs() {
		String sheetNm = "Prereqs";
		ArrayList<String> prereqs = new ArrayList<String>();
		FileInputStream fs = new FileInputStream(this.filePath);
		Workbook wb = Workbook.getWorkbook(fs);
		
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetNm);
		
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		int col = 0;
		for (int row = 0; row < totalNoOfRows; row++) {

			if ((String) sh.getCell(col, row).getContents() == this.courseID)
			{
				for (col = 1;col < totalNoOfCols; col ++) {
					if (sh.getCell(col, row).getContents() == "")
						continue;
					prereqs.add((String) sh.getCell(col, row).getContents());
				}
			}

		}
		
		
				
		this.Prereqs = prereqs;
	}
	
	private ArrayList<String> getPrereqs() {
		return this.Prereqs;
	}
	
	private void setCoreqs() {
		String sheetNm = "Coreqs";
		ArrayList<String> coreqs = new ArrayList<String>();
				
		FileInputStream fs = new FileInputStream(this.filePath);
		Workbook wb = Workbook.getWorkbook(fs);
		
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetNm);
		
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		int col = 0;
		for (int row = 0; row < totalNoOfRows; row++) {

			if ((String) sh.getCell(col, row).getContents() == this.courseID)
			{
				for (col = 1;col < totalNoOfCols; col ++) {
					if (sh.getCell(col, row).getContents() == "")
						continue;
					coreqs.add((String) sh.getCell(col, row).getContents());
				}
			}

		}
		
		this.Coreqs = coreqs;
	}
	
	private ArrayList<String> getCoreqs() {
		return this.Coreqs;
	}
	
	public boolean prereqsMet(ArrayList<Course> courseSet){
		
		int counter = 0;
		for (Course c : courseSet) {
	        if(this.courseID == c.getcourseID())
	        	counter +=1;
		}
		if (counter == getnumberOfPrereqs())
			return true;
		return false;
	}
		
	public boolean coreqsMet(ArrayList<Course> courseSet)
	{
		int counter = 0;
		for (Course c : courseSet) {
	        if(this.courseID == c.getcourseID())
	        	counter += 1;
		}
		if (counter == getnumberOfCoreqs())
			return true;
		return false;
	}	
}
