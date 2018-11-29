package Overview;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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
	private int rowIndex;
	private boolean hasPrereqs;
	private boolean hasCoreqs;
	
	 
	
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
			boolean courseCompleted, boolean hasPrereqs, boolean hasCoreqs, int rowIndex) throws IOException{
		this.courseID = courseID;
		this.creditHours = creditHours;
		this.availableFall = availFall;
		this.availableSpring = availSpring;
		this.courseCompleted = courseCompleted;
		this.numberOfPrereqs = 0;
		this.numberOfCoreqs = 0;
		this.rowIndex = rowIndex;
		this.hasPrereqs = hasPrereqs;
		this.hasCoreqs = hasCoreqs;
		 
		if (hasPrereqs == true) {
			this.setPrereqs();
			this.numberOfPrereqs = this.Prereqs.size();
		}
		
		if (hasCoreqs == true) {
			this.setCoreqs();
			this.numberOfCoreqs = this.Coreqs.size();
		}
		
		
	}
	

 
	public Course() {}

	public void changeCoreqs() {
		for (String coreq : this.Coreqs)
			if (coreq == "") {
				Coreqs.remove(Coreqs.indexOf(coreq));
				break;
			}
			
		this.numberOfCoreqs = this.Coreqs.size();
	}
	
	public void setCoreqs(int indexVal, String newString) {
		this.Coreqs.set(indexVal, newString);
	}
	
	private void setPrereqs() throws IOException {
		String sheetNm = "Prereqs";
		
		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(this.filePath));
        HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
		HSSFSheet sh = wb.getSheet(sheetNm);
				
		for(Cell cell : sh.getRow(this.rowIndex)) {
			if (cell.getColumnIndex() == 0)
				continue;
			this.Prereqs.add(cell.getStringCellValue());
          
		 }
	}
	
	private ArrayList<String> getPrereqs() {
		return this.Prereqs;
	}
	
	private void setCoreqs() throws IOException {
		String sheetNm = "Coreqs";
		
		POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(this.filePath));
        HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
		HSSFSheet sh = wb.getSheet(sheetNm);	
		
		for(Cell cell : sh.getRow(this.rowIndex)) {
			if (cell.getColumnIndex() == 0)
				continue;
			this.Coreqs.add(cell.getStringCellValue());
          
		 }
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

	public boolean gethasPrereqs() {
		return this.hasPrereqs;
	}
	public boolean gethasCoreqs() {
		return this.hasCoreqs;
	}	
	
	private boolean isCellEmpty(HSSFCell cell) {
	    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
	        return true;
	    }

	    return false;
	}
}
