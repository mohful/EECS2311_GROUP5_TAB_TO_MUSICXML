package TAB_TO_XML;


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import Interface.Controller;


	public class BParser {

			
	
		/**
		 * Counts duration of a note.
		 * 
		 * @param parse is the array list of strings that contains a whole line of notes
		 * @return an integer representing the duration of the note.
		 */
		public static Integer durationCount(ArrayList<String> parse, int row, int division) {
			Integer count = 1;
			
			outerloop: 
			for (int i = row + 1; i < parse.get(0).length(); i++) {
					for (int j = 0; j < parse.size(); j++) {
						if (Character.isDigit(parse.get(j).charAt(i))) {
							break outerloop;
						}
						else {
							if (j == parse.size() - 1) {
								count++;
							}
						}
					}
				}
			count = count / division;
			if (count > 8) {
				count = 8;
			}	else if (count == 0) {
				count = 1;
			}
			return count;
		}

		/// May require fix again
		/**
		 * Counts the division of the whole tablature
		 * 
		 * @param parse is the array list of strings that contains a whole line of notes
		 * @return an integer that represents the division of the whole tablature.
		 */
		public static int divisionCount(String line, int numerator) {
			line = line.replaceAll("|", "");
			line = line.replaceAll("\\*", "");
			return line.length() / numerator; 
		}


		/**
		 * Method used to get the step count.
		 * 
		 * @param parse is the array list of strings that contains a whole line of notes
		 * @return an String that represents the fret of the note.
		 */
		public static String stepCount(int row, int column, ArrayList<String> tuningSteps) {

			String[][] fretboard = new String[][] {
				{ "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G" },
				{ "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D" },
				{ "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A" },
				{ "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E" }};
				
			for (int i = 0; i < fretboard.length; i++) {
				if (fretboard[i][0].equals(tuningSteps.get(row))) {
					return fretboard[i][column];
				}
			}
			
			return fretboard[row][column];
		}

		/**
		 * Method used to get the type
		 * 
		 * @param parse is the array list of strings that contains a whole line of notes
		 * @return an String that represents the type of the note.
		 */
		public static String typeDeclare(int duration, int division) {
			double noteValue = (4.0 * (double) division) / duration;
			if (noteValue >= 1024 ) {return "1024th";} 
			else if (noteValue >= 512) {return "512th"; }
			else if (noteValue >= 256) { return "256th"; } 
			else if (noteValue >= 128) { return "128th"; } 
			else if (noteValue >= 64) { return "64th"; } 
			else if (noteValue >= 32) { return "32nd"; } 
			else if (noteValue >= 16) { return "16th"; } 
			else if (noteValue >= 8) { return "8th"; } 
			else if (noteValue >= 4) { return "quarter";} 
			else if (noteValue >= 2) { return "half" ; } 
			else if (noteValue >= 1) { return "whole" ; } 
			else if (noteValue >= 0.5) { return "breve"; } 
			else if (noteValue >= 0.25) { return "long"; } 
			else if (noteValue >= 0.125) {return "maxima"; }
			return ""; 
			
		}

		public static String octaveCount(int row, int column) {
			String[][] fretboard = new String[][] { 
				{ "2", "2", "2", "2", "3", "3", "3", "3", "3", "3", "3", "3", "3" },
				{ "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "3", "3" },
				{ "1", "1", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" },
				{ "1", "1", "1", "1", "1", "1", "1", "1", "2", "2", "2", "2", "2" }};

				return fretboard[row][column];
		}
		
		// inputing strings of lines.
		public static ArrayList<ArrayList<String>> tabToCollection(ArrayList<String> inputFile){	
			
			
			ArrayList<ArrayList<String>> Collections = new ArrayList<ArrayList<String>>();
			
			ArrayList<String> eachCollection = new ArrayList<String>();
			
			for (int i = 0; i < inputFile.size(); i++) {
				if (inputFile.get(i).contains("|")) {
					for (int j = 0; j < 4; j++) {
						eachCollection.add(inputFile.get(i+j));
					}
					eachCollection.add(" ");
					Collections.add(eachCollection);
					eachCollection = new ArrayList<String>();
					i = i + 4;
				}
				
					
			}
			
			// returns 2d arrays of the input lines 
			return Collections;
		}
		
		
		// input of six lines
		public static ArrayList<ArrayList<String>> collectionToMeasure(ArrayList<String> input){ 
			
			ArrayList<ArrayList<String>> sections = new ArrayList<ArrayList<String>>();
			ArrayList<String> eachSection = new ArrayList<String>();
			
			String line="";

			// assumes that all the measures have 17 dashes/notes excluding the vertical lines
			for (int z = 1; z < input.size(); z=z+4){
				for (int i = 0; i < (input.get(z-1).split("\\|").length-1); i++) {	
					for (int j = 0; j < 4; j++) {
						if (!(input.get(j+z-1).subSequence(0, 1).equals("|"))) {
							int count=0;
							while(!(input.get(j+z-1).subSequence(count, count+1).equals("|"))) {
								count++;
							}
							line=splitter(input.get(j+z-1),count,i);
							if(!(line.equals("")))
								eachSection.add(line);
						} 
						else {
							line=splitter(input.get(j+z-1),0,i);
							if(!(line.equals("")))
								eachSection.add(line);
						}
					}
					if(!(line.equals(""))) {
						sections.add(eachSection);
						eachSection = new ArrayList<String>();
					}
				}
			}
			

			// returns  2d array of substrings of the measure excluding the vertical lines	
			return sections;

					
		}
							

		private static String splitter(String note, int count,int element) { 
			note=note.substring(count);
			int digits=0;
			int start=0,end=0;
			for (int i = 0; i < note.length(); i++) { 
				if (note.subSequence(i, i+1).equals("|")) { 
					digits++;
					if (digits==element+1)
						start=i;
					else if(digits==element+2)
						end=i;
				}
			}
			if (start+1==end)
				return "";
			else {
				if(start>0 && end<(note.length()-1) && note.substring(start-1, start).equals("|")
						&& note.substring(end+1, end+2).equals("|")) {
					return "|"+note.substring(start+1, end)+"|";
				}
				if(start>0 &&  note.substring(start-1, start).equals("|")){
					return "|"+note.substring(start+1, end);
				}
				if(end==(note.length()-2) && note.substring(end+1, end+2).equals("|")) {
					return note.substring(start+1, end)+"|";
				}
				else {
					return note.substring(start+1, end);
				}
			}
		}

		
		
		
		public static String parseAlter(String note) { 
			
			for (int i = 0; i < note.length(); i++) { 
				if (note.charAt(i) == '#') { 
					return "1";
				}
			
			}
			return "0";
		}
		public static boolean isChord(ArrayList<String> line, char note) { 
			return false;
			
		}
		public static String[] parseChord() {return null;}
	}
	
	
	
	

