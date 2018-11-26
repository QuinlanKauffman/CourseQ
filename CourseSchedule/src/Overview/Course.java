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
		return this.numberOfPrereqs;}
		
	public int getnumberOfCoreqs(){
		return this.numberOfCoreqs;}
	
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
	
	public void setNumberOfCoreqs(int newNumberOfCoreqs) {
		this.numberOfCoreqs = newNumberOfCoreqs;
		if (this.numberOfCoreqs == 0)
			this.Coreqs = null;
	}
	
	public void setPrereqs(ArrayList<String> additionalPrereqs) {
		int counter;
		for (String newPrereq: additionalPrereqs) {
			counter = 0;
			for (String oldPrereq : this.Prereqs) {
				if (newPrereq == oldPrereq)
					counter +=1;
			}
			if (counter == 0) {
				this.Prereqs.add(newPrereq);
			}
		}
		
		this.numberOfPrereqs = this.Prereqs.size();
	}
	
	
	
	public Course(String courseID, int creditHours, boolean availFall, boolean availSpring,
			boolean courseCompleted, boolean hasPrereqs, boolean hasCoreqs) throws IOException{
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
		
		if (hasPrereqs == false) {
			this.Prereqs = null;
			this.numberOfPrereqs = 0;
		}
		
		
		if (hasPrereqs == true) {
			this.setCoreqs();
			this.numberOfPrereqs = getCoreqs().size();
		}
		
		if (hasCoreqs == false) {
			this.Coreqs = null;
			this.numberOfCoreqs = 0;
		}
		
	}
	
	
	public Course() {
		// TODO Auto-generated constructor stub
	}

	private void setPrereqs() throws IOException {
		String sheetNm = "Prereqs";
		String preCourseID = "";
		int index = 0;
		ArrayList<String> prereqs = new ArrayList<String>();
		
		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(this.filePath));
        HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
		HSSFSheet sh = wb.getSheet(sheetNm);
				
		
		for(Row row : sh) {
			Cell cell = row.getCell(0);
			if (cell.getStringCellValue() == this.courseID)
				index = cell.getRowIndex();
		}
		
		if (index != 0) {
			for (Cell cell : sh.getRow(index)) {
				if (cell.getColumnIndex() == 0)
					continue;
				
			
				preCourseID = cell.getStringCellValue();
				prereqs.add(preCourseID);
				
			}
		}	
		this.Prereqs = prereqs;
	}
	
	private ArrayList<String> getPrereqs() {
		return this.Prereqs;
	}
	
	private void setCoreqs() throws IOException {
		String sheetNm = "Coreqs";
		String coCourseID = "";
		int index = 0;
		ArrayList<String> coreqs = new ArrayList<String>();
		
		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(this.filePath));
        HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
		HSSFSheet sh = wb.getSheet(sheetNm);
				
		
		for(Row row : sh) {
			Cell cell = row.getCell(0);
			if (cell.getStringCellValue() == this.courseID)
				index = cell.getRowIndex();
		}
		
		if (index != 0) {
			for (Cell cell : sh.getRow(index)) {
				if (cell.getColumnIndex() == 0)
					continue;
				
			
				coCourseID = cell.getStringCellValue();
				coreqs.add(coCourseID);
				
			}
		}		
		this.Coreqs = coreqs;
	}
	
	public ArrayList<String> getCoreqs() {
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
