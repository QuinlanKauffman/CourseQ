package Overview;

import java.util.ArrayList;



public class Major {
	
	//If the prereq or coreq has already been completed, then need to change coreqs and prereqs
	
	private int semestersToFinish;
	private int maxCreditsPerSemester;
	private int fallValue;
	private int springValue;
	
	private ArrayList<ArrayList<ArrayList<Course>>> possibilitiesList = new 
			ArrayList<ArrayList<ArrayList<Course>>>();
	
	private ArrayList<ArrayList<Course>> semesterList = new ArrayList<ArrayList<Course>>();
	
	 
	
	private ArrayList<Course> ALLCOURSES = new ArrayList<Course>();
	private ArrayList<Course> coursesToTake = new ArrayList<Course>();
	private ArrayList<Course> coursesWithoutReqs = new ArrayList<Course>();
	private ArrayList<Course> coursesWithReqs = new ArrayList<Course>();
	
	private ArrayList<Course> coursesCompletedList = new ArrayList<Course>();
	private ArrayList<Course> coursesBeforeSemester = new ArrayList<Course>();
	
	private ArrayList<Integer> courseIndexListActual = new ArrayList<Integer>();
	private ArrayList<Integer> semesterIndexListActual = new ArrayList<Integer>();
	
	
	public Major(ArrayList<Course> allCourses) {
		this.ALLCOURSES = allCourses;
		this.removeCoursesAlreadyTaken();
		this.removeCoursesWithoutReqs();
		//this.groupAllInitial();
	}
	
	public ArrayList<ArrayList<ArrayList<Course>>> getPossibilties(int semestersLeft,
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
		
		this.getAllPossible(this.semesterList);
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
			if (c.getnumberOfCoreqs() == 0 && c.getnumberOfPrereqs() == 0) {
				coursesWithoutReqs.add(c);
			}
			else
				coursesWithReqs.add(c);
		}
		
	}
	
	
	private void groupAllInitial() {
		this.semesterList.set(0,coursesCompletedList);
		//Add the list of completed semesters to the 0th semester
		
		this.semesterList.set(1,coursesWithReqs);
		//Put the courses with coreqs or prereqs in the first semester
	}
	
	
	private void findCoreqs(Course c, int currentSemester, 
			ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		
		ArrayList<Integer> courseIndexList = new ArrayList<Integer>();
		ArrayList<Integer> semesterIndexList = new ArrayList<Integer>();
		ArrayList<String> coreqsToCheck = new ArrayList<String>();
		coreqsToCheck = c.getCoreqs();
		
		for (int i = 1; i<=currentSemester;i++) {
			for (Course d: unfinishedSemesterList.get(i)) {
				for (String courseName:coreqsToCheck) {
					if (d.getcourseID() == courseName) {
						courseIndexList.add(unfinishedSemesterList.get(i).indexOf(d));
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
			ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		
		ArrayList<Course> ac = new ArrayList<Course>();
		ac = unfinishedSemesterList.get(semesterIndex);
		return (ac.get(courseIndex));
	}
	
	private boolean getAllPossible(ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		int indexCount;
		
		if (checkAll(unfinishedSemesterList) == true) {
			this.possibilitiesList.add(unfinishedSemesterList);
			
		}
		
		for (int i = 1; i<unfinishedSemesterList.size(); i++) {
			//Don't start on the 0th semester because those courses have already been taken
			//Really start on the second semester
			indexCount = 0;
			
			for (Course c:unfinishedSemesterList.get(i)) {
				
				ArrayList<Course> tempCourseListNext = new ArrayList<Course>();
				ArrayList<Course> tempCourseListBefore = new ArrayList<Course>();
				ArrayList<Course> tempCoreqList = new ArrayList<Course>();
				ArrayList<Course> ac = new ArrayList<Course>();
				
				if (c.prereqsMet(getcoursesBeforeSemester(i,unfinishedSemesterList)) == false) {
					//Need to add the course to all the remaining semesters and check
					
					
					//Check if the course has a coreqs
					//Doesn't just need one, can now have multiple
					
					tempCourseListBefore = unfinishedSemesterList.get(i);
					tempCourseListBefore.remove(indexCount);
					unfinishedSemesterList.set(i,tempCourseListBefore);
					
					if (c.getnumberOfCoreqs() != 0) {
						for (int a = 0; a<this.semesterIndexListActual.size(); a++) {
							ac = unfinishedSemesterList.get(this.semesterIndexListActual.get(a));
							//Course d = new Course();
							//d = ac.get(this.courseIndexListActual.get(a));
							//tempCoreqList.add(d);
						}
						
					}
					
					
					for (int j = i+1; j<=semestersToFinish; j++) {
						
						tempCourseListNext = unfinishedSemesterList.get(j);
							//Need to grab the next semester's ArrayList
						tempCourseListNext.add(c);
						
						unfinishedSemesterList.set(j,tempCourseListNext);
						getAllPossible(unfinishedSemesterList);
						
					
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
			ArrayList<ArrayList<Course>> unfinishedSemesterMap ) {
		//Can also add the APs if needed
		//And if using when not a freshmen
		
		ArrayList<Course> a = new ArrayList<Course>();
		//Already added the courses completed to the 0th semester
		for (int i=0; i < semesterVal;i++) {
			for (Course c : unfinishedSemesterMap.get(i)) {
				a.add(c);
			}
		}
		this.coursesBeforeSemester = a;
	}
	
	public ArrayList<Course> getcoursesBeforeSemester(int semesterVal,
			ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		
		setcoursesBeforeSemester(semesterVal,unfinishedSemesterList);
		return this.coursesBeforeSemester;
	}
	
	
	private boolean checkAll(ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		if (checkPrereqs(unfinishedSemesterList) == true &&
				checkCoreqs(unfinishedSemesterList) == true && 
				checkCredits(unfinishedSemesterList) == true &&
				checkFallAndSpring(unfinishedSemesterList) == true)
			return true;
		
		return false;
	}
	
	private boolean checkPrereqs(ArrayList<ArrayList<Course>> unfinishedSemesterList){
		//true by default
		for (int i = 1; i<unfinishedSemesterList.size();i++){
			setcoursesBeforeSemester(i,unfinishedSemesterList);
			for (Course c : unfinishedSemesterList.get(i)) {
				if (c.prereqsMet(this.coursesBeforeSemester) == false){
					return false;
				}
			}	
		}
		return true;
	}
		
	private boolean checkCoreqs(ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		//true by default
		//remove then add back
		//May have accounted for with the plus 1
		
		for(int i=1;i<unfinishedSemesterList.size();i++){
			for (Course c : unfinishedSemesterList.get(i)) { 
				if (c.coreqsMet(unfinishedSemesterList.get(i)) == false){
					//May need to adjust slightly
					//Remove and then add back
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkFallAndSpring(ArrayList<ArrayList<Course>> unfinishedSemesterList) {
		for (int i = 1; i < unfinishedSemesterList.size();i++) {
			for (Course c : unfinishedSemesterList.get(i)) { 
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
	
	private boolean checkCredits(ArrayList<ArrayList<Course>> unfinishedSemesterList){
		int count;
		for (int i = 1; i < unfinishedSemesterList.size();i++){
			count = 0;
			for (Course c : unfinishedSemesterList.get(i)) { 
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
}
