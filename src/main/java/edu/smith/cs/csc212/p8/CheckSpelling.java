package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.smith.cs.csc212.p8.WordSplitter;

public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	/**
	 * Read all lines from the Project Gutenberg book.
	 * @return a list of words!
	 */
	public static List<String> readBook() {
		long start = System.nanoTime();
		List<String> words;
		//String line;
		try {
			words = Files.readAllLines(new File("src/main/resources/Alice.txt").toPath());
			//List<String> wordsList= WordSplitter.splitTextToWords(line);
			for(String line: words) {
			words=WordSplitter.splitTextToWords(line);
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find book.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " book entries in " + time +" seconds.");
		return words;
	}
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found="+fractionFound+" time="+nsPerItem+" ns/item");
	}
	
	public static List<String> createMixedDataset(List<String> listOfWords, int numSamples, double fractionYes) {
		ArrayList<String> realWords = new ArrayList<String>();
		for(int i=0;i<numSamples*fractionYes;i++) {
				realWords.add(listOfWords.get(i));
		}
		for (int i= (int) (numSamples*fractionYes);i<numSamples+1;i++) {
			realWords.add(listOfWords.get(i)+"XXYYYZZZ");
		}
		return realWords;
	}
	
//	public static void percentHitMiss(int numSamples, double fractionYes) {
//		double percent=(fractionYes/(double)numSamples)*100;
//	}
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		List<String> listofBookWords=readBook();
		
		// --- Create a bunch of data structures for testing:
		long startTreeTime = System.nanoTime();
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		long endTreeTime = System.nanoTime();
		System.out.println("Tree Creation time is");
		System.out.println(endTreeTime-startTreeTime);
		
		long startTreeLoop=System.nanoTime();
		TreeSet<String> treeOfWords2 = new TreeSet<>();
		for (String w : listOfWords) {
			treeOfWords2.add(w); }
		long endTreeLoop=System.nanoTime();
		System.out.println("Tree Loop Creation Time is");
		System.out.println(endTreeLoop-startTreeLoop);
		
		long startHashTime = System.nanoTime();
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		long endHashTime=System.nanoTime();
		System.out.println("Hash Creation Time is");
		System.out.println(endHashTime-startHashTime);
		
		long startHashLoop=System.nanoTime();
		HashSet<String> hashOfWords2 = new HashSet<>();
		for (String w : listOfWords) {
			hashOfWords2.add(w); }
		long endHashLoop=System.nanoTime();
		System.out.println("Hash Loop Creation Time is");
		System.out.println(endHashLoop-startHashLoop);
		
		long startListTime=System.nanoTime();
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		long endListTime=System.nanoTime();
		System.out.println("List Creation Time is");
		System.out.println(endListTime-startListTime);
		
		long startCharTime=System.nanoTime();
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);}
		long endCharTime=System.nanoTime();
		System.out.println("CharTrie Creation Time is");
		System.out.println(endCharTime-startCharTime);
		
		long startLLTime=System.nanoTime();
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);}
		Long endLLTime=System.nanoTime();
		System.out.println("LLHash Creation Time is");
		System.out.println(endLLTime-startLLTime);
		
		// --- Make sure that every word in the dictionary is in the dictionary:
//		timeLookup(listOfWords, treeOfWords);
//		timeLookup(listOfWords, hashOfWords);
//		timeLookup(listOfWords, bsl);
//		timeLookup(listOfWords, trie);
//		timeLookup(listOfWords, hm100k);
		

		//---Make sure every word in book is in dictionary
		timeLookup(listofBookWords, treeOfWords);
		timeLookup(listofBookWords, hashOfWords);
		timeLookup(listofBookWords, bsl);
		timeLookup(listofBookWords, trie);
		timeLookup(listofBookWords, hm100k);
		
		
		// --- Create a dataset of mixed hits and misses:
		//List<String> hitsAndMisses = new ArrayList<>();
		//Warm Up, gets rid of spikes in graph
		for (int j=0; j<2; j++) {
			System.out.println("Warm-up, j="+j);
			for (int i=0; i<11; i++) {
				double fraction = i / 10.0;
				// --- Create a dataset of mixed hits and misses:
//				List<String> hitsAndMisses = createMixedDataset(listOfWords, 10000, fraction);
//				timeLookup(hitsAndMisses, treeOfWords);
//				timeLookup(hitsAndMisses, hashOfWords);
//				timeLookup(hitsAndMisses, bsl);
//				timeLookup(hitsAndMisses, trie);
//				timeLookup(hitsAndMisses, hm100k);
			}
		}
		
		
		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		System.out.println("Done!");
	}
}
