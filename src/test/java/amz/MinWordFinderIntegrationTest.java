package amz;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MinWordFinderIntegrationTest {
	MinWordFinder minwordFinder = new MinWordFinder();
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void shouldHandleTypicalFile() throws Exception {
		Set<String> minWordsSet = minwordFinder.findMinWordsSet(this.getClass().getClassLoader().getResourceAsStream("sample.txt"));
		assertEquals(minWordsSet.size(), 3);
	}
	@Test
	public void shouldHandleEmptyFile() throws Exception {
		Set<String> minWordsSet = minwordFinder.findMinWordsSet(this.getClass().getClassLoader().getResourceAsStream("empty.txt"));
		assertEquals(minWordsSet.size(), 0);
	}
}
