package Overview;

import java.util.ArrayList;



public class Major {
	
	//If the prereq or coreq has already been completed, then need to change coreqs and prereqs
	
	private int semestersToFinish = 5;
	private int maxCreditsPerSemester;
	private int fallValue;
	private int springValue;
	
	private ArrayList<ArrayList<Course>[]> possibilitiesList = new ArrayList<ArrayList<Course>[]>();

	private ArrayList<Course>[] semesterArray = new ArrayList[this.semestersToFinish+1];
	 
	
	private ArrayList<Course> ALLCOURSES = new ArrayList<Course>();
	private ArrayList<Course> coursesToTake = new ArrayList<Course>();
	private ArrayList<Course> coursesWithoutReqs = new ArrayList<Course>();
	private ArrayList<Course> coursesWithReqs = new ArrayList<Course>();
	
	private ArrayList<Course> coursesCompletedList = new ArrayList<Course>();
	private ArrayList<Course> coursesBeforeSemester = new ArrayList<Course>();
	
	private ArrayList<Integer> courseIndexListActual = new ArrayList<Integer>();
	private ArrayList<Integer> semesterIndexListActual = new ArrayList<Integer>();
	
	
	public Major(int semestersToFinish, ArrayList<Course> allCourses) {
		this.semestersToFinish = semestersToFinish;
		this.ALLCOURSES = allCourses;
		this.removeCoursesAlreadyTaken();
		this.removeCoursesWithoutReqs();
		this.removeCoreqsIfCompleted();
		this.groupAllInitial();
	}
	
	private void removeCoreqsIfCompleted() {
		int indexVal = 0;
		boolean hasPrereqsAlso = true;
		for (Course c:this.coursesWithReqs) {
			if (c.gethasCoreqs() == true) {
				for(String coreq:c.getCoreqs()) {
					for (Course d: this.coursesCompletedList) {
						if (d.getcourseID().toUpperCase() == coreq.toUpperCase()) {
							c.setNumberOfCoreqs(0);
							indexVal = this.coursesWithReqs.indexOf(c);
							hasPrereqsAlso = c.gethasPrereqs();
							if (hasPrereqsAlso == false) {
								this.coursesWithoutReqs.add(c);
							}
						}
					}
				}
			}
		}
		
		if (hasPrereqsAlso == false) {
			this.coursesWithReqs.remove(indexVal);
		}
		
	}

	public ArrayList<ArrayList<Course>[]> getPossibilties(int semestersLeft,
			int numberOfCreditsPerSemester, int fallValue) {
		
		this.semestersToFinish = semestersLeft;
		this.maxCreditsPerSemester = numberOfCreditsPerSemester;
		if(fallValue % 2 == 1) {
			this.fallValue = 1;
			this.springValue = 0;
		}
		if(fallValue % 2 == 0) {
			this.fallValue = 0;
			this.springValue = 1;
		}
		
		this.getAllPossible(this.semesterArray);
		System.out.println("Total number of possible combinations is :" + this.totalPossibilities());
		return this.possibilitiesList;
	}
	
	private void removeCoursesAlreadyTaken() {
		for (Course c:ALLCOURSES)
		{
			if (c.getcourseCompleted() == true)
			{
				coursesCompletedList.add(c);
			}
			else
				coursesToTake.add(c);
		}
	}

	 
	private void removeCoursesWithoutReqs() {
		
		for (Course c:coursesToTake)
		{
			if (c.gethasCoreqs() == false && c.gethasPrereqs() == false) {
				coursesWithoutReqs.add(c);
			}
			else
				coursesWithReqs.add(c);
		}
		
	}
	
	
	private void groupAllInitial() {
		this.semesterArray[0] = this.coursesCompletedList;
		this.semesterArray[1] = this.coursesWithReqs;
		this.semesterArray[2] = this.coursesWithoutReqs;
	}
	
	
	private void findCoreqs(Course c, int currentSemester, 
			ArrayList<Course>[] unfinishedSemesterArray) {
		
		ArrayList<Integer> courseIndexList = new ArrayList<Integer>();
		ArrayList<Integer> semesterIndexList = new ArrayList<Integer>();
		ArrayList<String> coreqsToCheck = new ArrayList<String>();
		coreqsToCheck = c.getCoreqs();
		
		for (int i = 1; i<=currentSemester;i++) {
			for (Course d: unfinishedSemesterArray[i]) {
				for (String courseName:coreqsToCheck) {
					if (d.getcourseID() == courseName) {
						courseIndexList.add(unfinishedSemesterArray[i].indexOf(d));
						semesterIndexList.add(i);
						break;
					}
				}
			}
			
		}
		this.courseIndexListActual = courseIndexList;
		this.semesterIndexListActual = semesterIndexList;
	}
	
	
	private Course getCourseFromIndices(int semesterIndex, int courseIndex, 
			ArrayList<Course>[] unfinishedSemesterArray) {
		
		ArrayList<Course> ac = new ArrayList<Course>();
		ac = unfinishedSemesterArray[semesterIndex];
		return (ac.get(courseIndex));
	}
	
	private boolean getAllPossible(ArrayList<Course>[] unfinishedSemesterArray) {
		int indexCount;
		
		if (checkAll(unfinishedSemesterArray) == true) {
			this.possibilitiesList.add(unfinishedSemesterArray);
		}
		
		for (int i = 1; i<unfinishedSemesterArray.length; i++) {
			//Don't start on the 0th semester because those courses have already been taken
			//Really start on the second semester
			indexCount = 0;
			
			for (Course c:unfinishedSemesterArray[i]) {
				
				ArrayList<Course> tempCourseListNext = new ArrayList<Course>();
				ArrayList<Course> tempCourseListBefore = new ArrayList<Course>();
				ArrayList<Course> tempCoreqList = new ArrayList<Course>();
				int ac;
				
				if (c.prereqsMet(getcoursesBeforeSemester(i,unfinishedSemesterArray)) == false) {
					//Need to add the course to all the remaining semesters and check
					
					
					//Check if the course has a coreqs
					//Doesn't just need one, can now have multiple
					
					tempCourseListBefore = unfinishedSemesterArray[i];
					tempCourseListBefore.remove(indexCount);
					unfinishedSemesterArray[i] = tempCourseListBefore;
					
					if (c.getnumberOfCoreqs() != 0) {
						for (int a = 0; a < this.semesterIndexListActual.size(); a++) {
							ac = unfinishedSemesterArray[this.semesterIndexListActual.get(a)];
							//Course d = new Course();
							//d = ac.get(this.courseIndexListActual.get(a));
							//tempCoreqList.add(d);
						}
						
					}
					
					
					for (int j = i+1; j<= this.semestersToFinish; j++) {
						
						tempCourseListNext = unfinishedSemesterArray[i];
							//Need to grab the next semester's ArrayList
						tempCourseListNext.add(c);
						
						unfinishedSemesterArray[j] = tempCourseListNext;
						getAllPossible(unfinishedSemesterArray);
						
					
					}		
				}
				indexCount += 1;
			}
		}
		return false;
	}
	
	public int totalPossibilities() {
		int a = 0;
		a = factorial(coursesWithoutReqs.size());
		//Order matters
		
		return (a*possibilitiesList.size());
	}
	
	private int factorial(int i) {
		if (i == 1) {
			return i;
		}
		else
			return (factorial(i-1)*i);
	}
	
	public void setcoursesBeforeSemester(int semesterVal,
			ArrayList<Course>[] unfinishedSemesterArray ) {
		//Can also add the APs if needed
		//And if using when not a freshmen
		
		ArrayList<Course> a = new ArrayList<Course>();
		//Already added the courses completed to the 0th semester
		for (int i = 0; i < semesterVal;i++) {
			for (Course c : unfinishedSemesterArray[i]) {
				a.add(c);
			}
		}
		this.coursesBeforeSemester = a;
	}
	
	public ArrayList<Course> getcoursesBeforeSemester(int semesterVal,
			ArrayList<Course>[] unfinishedSemesterArray) {
		
		setcoursesBeforeSemester(semesterVal,unfinishedSemesterArray);
		return this.coursesBeforeSemester;
	}
	
	
	private boolean checkAll(ArrayList<Course>[] unfinishedSemesterArray) {
		if (checkPrereqs(unfinishedSemesterArray) == true &&
				checkCoreqs(unfinishedSemesterArray) == true && 
				checkCredits(unfinishedSemesterArray) == true &&
				checkFallAndSpring(unfinishedSemesterArray) == true)
			return true;
		
		return false;
	}
	
	private boolean checkPrereqs(ArrayList<Course>[] unfinishedSemesterArray){
		//true by default
		for (int i = 1; i<unfinishedSemesterArray.length;i++){
			setcoursesBeforeSemester(i,unfinishedSemesterArray);
			for (Course c : unfinishedSemesterArray[i]) {
				if (c.prereqsMet(this.coursesBeforeSemester) == false){
					return false;
				}
			}	
		}
		return true;
	}
		
	private boolean checkCoreqs(ArrayList<Course>[] unfinishedSemesterArray) {
		//true by default
		//remove then add back
		//May have accounted for with the plus 1
		
		for(int i=1;i<unfinishedSemesterArray.length;i++){
			for (Course c : unfinishedSemesterArray[i]) { 
				if (c.coreqsMet(unfinishedSemesterArray[i]) == false){
					//May need to adjust slightly
					//Remove and then add back
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkFallAndSpring(ArrayList<Course>[] unfinishedSemesterArray) {
		for (int i = 1; i < unfinishedSemesterArray.length;i++) {
			for (Course c : unfinishedSemesterArray[i]) { 
				if (c.getfallAvailability() == true && c.getspringAvailability() == true)
					continue;
			
				if (c.getfallAvailability() == true && (i % 2 == this.fallValue))
					continue;
				
				if (c.getspringAvailability() == true && (i % 2 == this.springValue))
					continue;
				else
					return false;
			}
		
		}
		
		return true;
	}
	
	private boolean checkCredits(ArrayList<Course>[] unfinishedSemesterArray){
		int count;
		for (int i = 1; i < unfinishedSemesterArray.length;i++){
			count = 0;
			for (Course c : unfinishedSemesterArray[i]) { 
				count += c.getcreditHours();
			}
			if (count > this.maxCreditsPerSemester)
				return false;
		}
		return true;
	}
	
	
	public void sortPrereqs() {
		
	}
	
	private int coursesWithCoreqs(ArrayList<Course> courseList) {
		int counter = 0;
		for (Course c: courseList) {
			counter += c.getnumberOfCoreqs();
		}
		return counter;
	}
	
//	private void groupCoreqs() {
//		int oldNumberOfCoreqs;
//		int index;
//		
//		while(coursesWithCoreqs(this.coursesWithReqs) != 0) {
//			for (Course c: this.coursesWithReqs) {
//				if (c.getnumberOfCoreqs() == 1)
//				{
//					index = this.coursesWithReqs.indexOf(c);
//					for (int i = index; i < coursesWithReqs.size();i++) {
//						Course d = coursesWithReqs.get(i);
//						if (d.getcourseID() == c.getcourseID())
//							break;
//				}
//			}
//			String courseID = c.getcourseID() + d.getcourseID();
//			int creditHours = c.getcreditHours() + d.getcreditHours();
//			boolean
//			
//			Course e = new Course()
//			
//		}
//		
//				
//				
//				
//				c.setNumberOfCoreqs(0);
//		
//	}

	public Major(){}
	//Default constructor

	
	public void setFallValue(int fallValue) {
		this.fallValue = fallValue;
	}
	
	public int getFallValue() {
		return this.fallValue;
	}
	
	public void setSpringValue(int SpringValue) {
		this.springValue = SpringValue;
	}
	
	public int getSpringValue() {
		return this.springValue;
	}
	
	public void setSemestersToFinish(int semestersToFinish){
		this.semestersToFinish = semestersToFinish;}
	
	public int getSemesterToFinish() {
		return this.semestersToFinish;
	}
	
	public void setmaxCreditsPerSemester(int maxCreditsPerSemester){
		this.maxCreditsPerSemester = maxCreditsPerSemester;}
	
	public int getmaxCreditsPerSemester() {
		return this.maxCreditsPerSemester;
	}
	
	public long numberOfPoss() {
		long a = 1;
		int coursesLeft = 26;
		int semesters = 5;
		int coursesPerSemester = 5;
		int b;
		for (int i = 0; i< semesters - 1 ;i++){
			b = coursesLeft - i*coursesPerSemester;
			a *= binomialCoeff(b,coursesPerSemester);
		}
		//Don't need to do the last semester because will just multiply by 1
		return a;
	
	}
	
	public long binomialCoeff(int n, int k)  
    { 
      
        // Base Cases 
        if (k == 0 || k == n) 
            return 1; 
          
        // Recur 
        return binomialCoeff(n - 1, k - 1) + binomialCoeff(n - 1, k); 
    } 
	
	
	public void printCoursesWithReqs() {
		System.out.println(this.coursesWithReqs.size());
		for (Course c: this.coursesWithReqs) {
			System.out.println(c.getcourseID());
		}
	}
	
	public void printCoursesWithoutReqs() {
		System.out.println(this.coursesWithoutReqs.size());
		for (Course c: this.coursesWithoutReqs) {
			System.out.println(c.getcourseID());
		}
	}
	
	public void printCoursesCompleted() {
		for (Course c: this.coursesCompletedList) {
			System.out.println(c.getcourseID());
		}
	}
	
	public void printAllCourses() {
		for (Course c: this.ALLCOURSES) {
			System.out.println(c.getcourseID());
		}
	}
	
	public void printSemesters() {
		for(int i = 0; i<3;i++) {
			System.out.println("Semester: " + i);
			for(Course c:semesterArray[i]) {
				System.out.println(c.getcourseID());
			}
		}
	}
}
