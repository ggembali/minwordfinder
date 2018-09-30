package amz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/*
  1. Read file contents via buffered reader
  2. For each line
  3.  For each word in line
        Put into word occurences map word is the key and set of line numbers is the value. Note here word occuring more than once in same line still counts one being set and they are sorted array
  4. Find the word occuring in max lines 
  5. Remove the word from map and to curr min word set
  6. Add line numbers to removedLines array holding all lines which are already have a match with curr min word set
  7. If count of removed lines is equal to total lines, we found min words set
  8. Go through all remaining words
      for each word
        filter line numbers list by removing the lines which are just found by the last word added to set. Here we make use of the fact that both lists are sorted
  9. Repeat from step-4
  
  Note : Two major differences compared to solution given in interview
    - Count map is not adding value as we can remove a line even if a word occurs once
    - Instead of picking next word having max occurences from original map, first cleaning up the map to find next word having max occurences in remaining lines
  
*/
class MinWordFinder {

	public Set<String> findMinWordsSet(InputStream stream) throws IOException {
		Map<String, Set<Integer>> wordOccurences = new HashMap<>();
		int totalLines = populateWordStats(stream,wordOccurences);
		Set<String> minwordsSet = new HashSet<>();
		int removedPages = 0;

		while (removedPages < totalLines) {
			String wordWithMaxOccurences = findWordInMaxLines(wordOccurences);
			minwordsSet.add(wordWithMaxOccurences);
			removedPages += wordOccurences.get(wordWithMaxOccurences).size();
			Set<Integer> linesToBeRemoved = wordOccurences.get(wordWithMaxOccurences);
			wordOccurences.remove(wordWithMaxOccurences);
			filterLineNumbersFromOtherWords(linesToBeRemoved,wordOccurences);

		}
		return minwordsSet;
	}

	private int populateWordStats(InputStream inputStream, Map<String, Set<Integer>> wordOccurences) throws FileNotFoundException, IOException {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String currentLine = reader.readLine();
			int currentLineNumber = 0;
			while (currentLine != null) {
				currentLineNumber++;
				String[] words = currentLine.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (wordOccurences.containsKey(words[i])) {
						wordOccurences.get(words[i]).add(currentLineNumber);
					} else {
						Set<Integer> lineNumbers = new TreeSet<>();
						lineNumbers.add(currentLineNumber);
						wordOccurences.put(words[i], lineNumbers);
					}
				}
				currentLine = reader.readLine();
			}
			return currentLineNumber;
		} catch (IOException e) {
		} finally {
			inputStream.close();
		}
		return 0;
	}

	public void filterLineNumbersFromOtherWords(Set<Integer> linesTobeRemoved, Map<String, Set<Integer>> wordOccurences) {
		Iterator<String> iterator = wordOccurences.keySet().iterator();
		while (iterator.hasNext()) {
			String word = iterator.next();
			Set<Integer> lineNumbers = wordOccurences.get(word);
			lineNumbers.removeAll(linesTobeRemoved);
			if (lineNumbers.isEmpty()) {
				iterator.remove();
			}
		}
	}

	public String findWordInMaxLines(Map<String, Set<Integer>> wordOccurences) {
		Set<String> words = wordOccurences.keySet();
		int maxLines = 0;
		String maxOccWord = null;
		for (String word : words) {
			if (wordOccurences.get(word).size() > maxLines) {
				maxLines = wordOccurences.get(word).size();
				maxOccWord = word;
			}
		}
		return maxOccWord;
	}

}