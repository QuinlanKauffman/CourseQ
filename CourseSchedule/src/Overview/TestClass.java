package Overview;

import java.io.IOException;

import org.junit.Test;

public class TestClass {
	 
	@Test
	public void test1() {
		Major m = new Major();
		m.setFallValue(2);
		System.out.println(m.getFallValue());
	}

	@Test
	public void test2() throws IOException {
		BuildCourseList bcl = new BuildCourseList();
		bcl.callMajor();
	}
}
