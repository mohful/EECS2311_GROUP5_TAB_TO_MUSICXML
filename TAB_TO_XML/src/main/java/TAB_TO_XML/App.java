package TAB_TO_XML;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import BassModel.*;
import BassModel.Note;
import DrumModel.*;
import guitarModel.*;

public class App {

	public static String runConversion(String tab, String customization) {
		String conversion = null;

		HashMap<Integer, Integer[]> timeSigs = getTimeSignatures(customization);

		switch (getInstrument(tab)) {
		case "Guitar":
			conversion = guitarTabToXML(getFileList(tab), timeSigs);
			break;
		case "Bass":
			conversion = bassTabToXML(getFileList(tab), timeSigs);
			break;
		case "Drums":
			conversion = drumTabToXML(getFileList(tab), timeSigs);
			break;
		default:
			break;
		}

		if (conversion != null) {
			conversion = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
					+ "<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.1 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">\n"
					+ conversion;
		}

		return conversion;
	}

	public static ArrayList<String> getFileList(String text) {
		// read input file, store in array list
		// return GuitarParser.readText(GuitarParser.getText()); // TODO: change
		// location of the used methods here

		ArrayList<String> textList = new ArrayList<>();
		Scanner in = null;
		try {
			in = new Scanner(text);
			while (in.hasNextLine())
				textList.add(in.nextLine());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return textList;
	}

	public static String getInstrument(String tab) {

		return identifyInstrument(getFileList(tab));
	}

	private static ArrayList<String> getTuningSteps(ArrayList<String> collection) {
		ArrayList<String> tuningSteps = new ArrayList<String>();
		for (int x = 0; x < collection.size(); x++) {
			for (int y = 0; y < collection.get(x).length(); y++) {
				if (collection.get(x).charAt(y) == '|') {
					tuningSteps.add(collection.get(x).substring(0, y));
					break;
				}
			}
		}

		return tuningSteps;

	}

	public static HashMap<Integer, Integer[]> getTimeSignatures(String input) {
		HashMap<Integer, Integer[]> map = new HashMap<>();

		String[] lines = input.split("\n");

		for (String line : lines) {
			String[] command = line.split(":");
			if (command.length == 2) {
				try {
					if (command[0].equals("*")) {
						for (int i = 1; i < 999; i++) {
							String[] temp = command[1].split("/");
							if (temp.length == 2) {
								Integer[] timeSignature = new Integer[2];
								timeSignature[0] = Integer.parseInt(temp[0]);
								timeSignature[1] = Integer.parseInt(temp[1]);
								map.put(i, timeSignature);
							}
						}
					} else if (command[0].contains("-")) {
						String[] range = command[0].split("-");

						if (range.length == 2 && Integer.parseInt(range[0]) <= Integer.parseInt(range[1])) {
							for (int i = Integer.parseInt(range[0]); i <= Integer.parseInt(range[1]); i++) {
								String[] temp = command[1].split("/");
								if (temp.length == 2) {
									Integer[] timeSignature = new Integer[2];
									timeSignature[0] = Integer.parseInt(temp[0]);
									timeSignature[1] = Integer.parseInt(temp[1]);
									map.put(i, timeSignature);
								}
							}
						}
					} else {
						int measure = Integer.parseInt(command[0]);
						String[] temp = command[1].split("/");
						if (temp.length == 2) {
							Integer[] timeSignature = new Integer[2];
							timeSignature[0] = Integer.parseInt(temp[0]);
							timeSignature[1] = Integer.parseInt(temp[1]);
							map.put(measure, timeSignature);
						}
					}

				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		return map;
	}

	public static String bassTabToXML(ArrayList<String> tabAsList, HashMap<Integer, Integer[]> timeSigs) {
		boolean isThereRepeat = false;
		try {
			ObjectMapper mapper = new XmlMapper();

			// get a set of collections
			ArrayList<ArrayList<String>> collections = new ArrayList<>();
			collections = BParser.tabToCollection(tabAsList);

			BassModel.ScorePartwise scorePartwise = new BassModel.ScorePartwise();
			scorePartwise.setVersion("3.1");
			BassModel.PartList partList = new BassModel.PartList();
			ArrayList<BassModel.ScorePart> scoreParts = new ArrayList<BassModel.ScorePart>();
			BassModel.ScorePart scorepart = new BassModel.ScorePart();
			scorepart.setId("P1");
			scorepart.setPartName("Bass Guitar");
			scoreParts.add(scorepart);

			partList.setScoreParts(scoreParts);
			scorePartwise.setPartList(partList);

			ArrayList<BassModel.Part> parts = new ArrayList<BassModel.Part>();
			BassModel.Part part = new BassModel.Part();
			part.setId("P1");
			parts.add(part);

			ArrayList<BassModel.Measure> measures = new ArrayList<BassModel.Measure>();

			int measureCount = 0;
			// iter through each collection
			for (int i = 0; i < collections.size(); i++) {

				// get tuning steps
				ArrayList<String> tuningSteps = getTuningSteps(collections.get(i));

				ArrayList<ArrayList<String>> measuresOfCollection = BParser.collectionToMeasure(collections.get(i));

				// iter through each measure set
				for (int j = 0; j < measuresOfCollection.size(); j++) {
					measureCount++;
					BassModel.Measure newMeasure = parseBassMeasure(measuresOfCollection.get(j), measureCount,
							tuningSteps, timeSigs, isThereRepeat);
					measures.add(newMeasure);
				}
			}

//			if (isThereRepeat == false) {
//				BassModel.BackwardBarline onebarline = new BassModel.BackwardBarline();
//				onebarline.setBarStyle("light-heavy");
//				onebarline.setLocation("right");
//				measures.get(measures.size() - 1).setBackwardBarline(onebarline);
//
//			}

			parts.get(0).setMeasures(measures);

			scorePartwise.setParts(parts);

			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// mapper.writeValue(new File("./Streamresult.musicxml"), scorePartwise);
			return mapper.writeValueAsString(scorePartwise).replace("backwardBarline", "barline");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static BassModel.Measure parseBassMeasure(ArrayList<String> meas, int measureNumber,
			ArrayList<String> tuningSteps, HashMap<Integer, Integer[]> timeSigs, boolean isThereRepeat) {

		BassModel.Measure newMeasure = new BassModel.Measure();
		newMeasure.setNumber(measureNumber);
		Integer[] timeSig = timeSigs.getOrDefault(measureNumber, new Integer[] { 4, 4 });
		int division = BParser.divisionCount(meas.get(2), timeSig[0]);

		// For Repeats
		Boolean forwardRepeatExist = false;
		Boolean backwardRepeatExist = false;
		Boolean variableRepeatExist = false;
		// if first measure, set the attributes
		if (measureNumber == 1) {
			BassModel.Attributes attributes = new BassModel.Attributes();
			BassModel.Clef clef = new BassModel.Clef();
			clef.setSign("TAB");
			clef.setLine("2");
			attributes.setClef(clef);

			attributes.setDivisions(division);

			BassModel.Key key = new BassModel.Key();
			key.setFifths("0");
			attributes.setKey(key);

			BassModel.StaffDetails staffDetails = new BassModel.StaffDetails();
			staffDetails.setStaffLines("4");

			// staff tunings
			ArrayList<BassModel.StaffTuning> staffTunings = new ArrayList<BassModel.StaffTuning>();

			for (int i = 0; i < tuningSteps.size(); i++) {
				BassModel.StaffTuning staffTuning0 = new BassModel.StaffTuning();
				staffTuning0.setLine(i + 1);
				if (!tuningSteps.get(i).equals("")) {
					staffTuning0.setTuningStep(tuningSteps.get(i));
				} else {
					switch (i) {
					case 0:
						staffTuning0.setTuningStep("E");
						break;
					case 1:
						staffTuning0.setTuningStep("A");
						break;

					case 2:
						staffTuning0.setTuningStep("D");
						break;

					case 3:
						staffTuning0.setTuningStep("G");
						break;
					}
				}

				switch (i) {
				case 0:
					staffTuning0.setTuningOctave("1");
					break;
				case 1:
					staffTuning0.setTuningOctave("1");
					break;

				case 2:
					staffTuning0.setTuningOctave("2");
					break;

				case 3:
					staffTuning0.setTuningOctave("3");
					break;
				}
				staffTunings.add(staffTuning0);
			}

			staffDetails.setStaffTunings(staffTunings);
			attributes.setStaffDetails(staffDetails);

			BassModel.Time time = new BassModel.Time();
			time.setBeats(timeSig[0].toString());
			time.setBeatType(timeSig[1].toString());
			attributes.setTime(time);

			newMeasure.setAttributes(attributes);
		} else {
			BassModel.Attributes attributes = new BassModel.Attributes();
			BassModel.StaffDetails staffDetails = new BassModel.StaffDetails();
			staffDetails.setStaffLines("4");

			// staff tunings
			ArrayList<BassModel.StaffTuning> staffTunings = new ArrayList<BassModel.StaffTuning>();

			for (int i = 0; i < tuningSteps.size(); i++) {
				BassModel.StaffTuning staffTuning0 = new BassModel.StaffTuning();
				staffTuning0.setLine(i + 1);
				if (!tuningSteps.get(i).equals("")) {
					staffTuning0.setTuningStep(tuningSteps.get(i));
				} else {
					switch (i) {
					case 0:
						staffTuning0.setTuningStep("E");
						break;
					case 1:
						staffTuning0.setTuningStep("A");
						break;

					case 2:
						staffTuning0.setTuningStep("D");
						break;

					case 3:
						staffTuning0.setTuningStep("G");
						break;
					}
				}

				switch (i) {
				case 0:
					staffTuning0.setTuningOctave("1");
					break;
				case 1:
					staffTuning0.setTuningOctave("1");
					break;

				case 2:
					staffTuning0.setTuningOctave("2");
					break;

				case 3:
					staffTuning0.setTuningOctave("3");
					break;
				}
				staffTunings.add(staffTuning0);
			}
			staffDetails.setStaffTunings(staffTunings);
			attributes.setStaffDetails(staffDetails);
			BassModel.Time time = new BassModel.Time();
			time.setBeats(timeSig[0].toString());
			time.setBeatType(timeSig[1].toString());
			attributes.setTime(time);
			attributes.setDivisions(division);
			newMeasure.setAttributes(attributes);
		}

//		newMeasure.setBarline(null);

		int BeforenumberP = 0; // for pull offs (before)
		int BeforenumberH = 0; // for hammer ons ""
		int AfternumberP = 0; // for pull offs (after)
		int AfternumberH = 0; // for hammer ons ""
		int BeforenumberS = 0;
		int AfternumberS = 0;
		ArrayList<BassModel.Note> note = new ArrayList<BassModel.Note>();
		int numVarRepeats = 0;
		// iter through each measure
		for (int y = 0; y < meas.get(0).length(); y++) {
			Boolean hasPrevColNote = false;

			Boolean isGrace = false;
			int nextColumn1 = 0;
			int prevColumn1 = 0;
			if (y + 1 < meas.get(0).length()) { // prevent index out of bounds
				nextColumn1 = y + 1;
			}
			if (y - 1 > 0) {
				prevColumn1 = y - 1;
			}
			for (int x = meas.size() - 1; x >= 0; x--) {
				char character = meas.get(x).charAt(y);
				Boolean isDoubleDigit = false;
				// Repeats: Variable Number Of Times
				if (Character.isDigit(character) && y == meas.get(0).length() - 1) {
					variableRepeatExist = true;
					numVarRepeats = character;
					break;
				}

				// Repeats: Forward Direction
				if (character == '|' && meas.get(x).charAt(nextColumn1) == '*') {
					forwardRepeatExist = true;

				}
				// Repeats: Backward Direction
				else if (character == '|' && meas.get(x).charAt(prevColumn1) == '*') {
					backwardRepeatExist = true;
				}

				String doubleDigit = "";
				String correctDoubleDigit = "";
				if (Character.isDigit(character)) {
					// Variables to store double digit frets

					// if it's a double digit fret tuning
					if (meas.get(x).charAt(nextColumn1) == 'p' || meas.get(x).charAt(nextColumn1) == 'P') {
						BeforenumberP++;

					} else {
						BeforenumberP = 0;
					}

					if (meas.get(x).charAt(prevColumn1) == 'p' || meas.get(x).charAt(prevColumn1) == 'P') {
						AfternumberP++;

					}

					else {

						AfternumberP = 0;

					}

					// Incrementing numbers for consecutive occurrences of Hammer ons
					if (meas.get(x).charAt(nextColumn1) == 'h' || meas.get(x).charAt(nextColumn1) == 'H') {

						BeforenumberH++;

					} else {
						BeforenumberH = 0;
					}
					if (meas.get(x).charAt(prevColumn1) == 'h' || meas.get(x).charAt(prevColumn1) == 'H') {
						AfternumberH++;

					} else {
						AfternumberH = 0;
					}

					// Incrementing numbers for consecutive occurrences of Slides
					if (meas.get(x).charAt(nextColumn1) == 's') {

						BeforenumberS++;

					} else {
						BeforenumberS = 0;
					}
					if (meas.get(x).charAt(prevColumn1) == 's') {

						AfternumberS++;

					} else {
						AfternumberS = 0;
					}

					if (meas.get(x).charAt(prevColumn1) == 'g') {
						isGrace = true;
						BassGraceNote grace1 = new BassModel.BassGraceNote();
						grace1.setStem();// sets the stem value to "none"
						grace1.setDuration();
						grace1.removeNoteType();
						note.add(grace1);
					} else if (hasPrevColNote) {
						note.add(new BassModel.ChordNote());
					} else {
						note.add(new BassModel.Note());
					}

					Integer duration = BParser.durationCount(meas, y, division);

					if (isGrace == false) {
						note.get(note.size() - 1).setDuration(duration.toString());
						note.get(note.size() - 1).setType(BParser.typeDeclare(duration, division));
					}

					note.get(note.size() - 1).setVoice("1");

					// if the note is length 2, it contains a sharp
					if (BParser.stepCount(x, Character.getNumericValue(character), tuningSteps).length() == 2) {
						BassModel.AlteredPitch pitch = new BassModel.AlteredPitch();
						pitch.setAlter("1");
						pitch.setStep(BParser.stepCount(x, Character.getNumericValue(character), tuningSteps)
								.substring(0, 1));
						pitch.setOctave(BParser.octaveCount(x, Character.getNumericValue(character)));
						note.get(note.size() - 1).setPitch(pitch);
					} else {
						BassModel.Pitch pitch = new BassModel.Pitch();
						pitch.setStep(BParser.stepCount(x, Character.getNumericValue(character), tuningSteps));
						pitch.setOctave(BParser.octaveCount(x, Character.getNumericValue(character)));
						note.get(note.size() - 1).setPitch(pitch);
					}

					// set notations, technical is a sub-element of notations
					BassModel.Notations notations = new BassModel.Notations();
					BassModel.Technical technical = new BassModel.Technical();
					BassModel.BassHarmonicTech tech = new BassModel.BassHarmonicTech();
					ArrayList<BassModel.BassPullOff> pullList = new ArrayList<BassModel.BassPullOff>();
					ArrayList<BassModel.BassHammer> hammerList = new ArrayList<BassModel.BassHammer>();
					ArrayList<BassModel.BassSlide> slideList = new ArrayList<BassModel.BassSlide>();

					// Natural Harmonics or not

					if (meas.get(x).charAt(prevColumn1) == '[' && meas.get(x).charAt(nextColumn1) == ']') {
						BassModel.BassHarmonic h = new BassModel.BassHarmonic();
						h.setNatural();
						tech.setHarmonics(h);

						// Slide Techniques: START
						if (meas.get(x).charAt(nextColumn1) == 's') {
							BassModel.BassSlide slide = new BassModel.BassSlide();
							slide.setNumber(1);
							slide.setType("start");
							slideList.add(slide);
							notations.setSlides(slideList);
						}
						// Slide Techniques: END
						if (meas.get(x).charAt(prevColumn1) == 's') {
							BassModel.BassSlide s11 = new BassModel.BassSlide();
							s11.setNumber(1);
							s11.setType("stop");
							slideList.add(s11);
							notations.setSlides(slideList);
						}

						// Pull-off techniques: START
						if (meas.get(x).charAt(nextColumn1) == 'p' || meas.get(x).charAt(nextColumn1) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassPullOff pl = new BassModel.BassPullOff();
							pl.setNumber(1);
							pl.setType("start");
							pl.setSymbol("P");
							pullList.add(pl);

							BassModel.BassSlur su = new BassModel.BassSlur();
							su.setNumber(1);
							// su.setPlacement("above");
							su.setType("start");
							tech.setPulloff(pullList);
							notations.setSlur(su);

						}

						// Pull-off techniques: END
						if (meas.get(x).charAt(prevColumn1) == 'p' || meas.get(x).charAt(prevColumn1) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassPullOff pull = new BassModel.BassPullOff();
							pull.setNumber(1);
							pull.setType("stop");
							pullList.add(pull);
							BassModel.BassSlur sl = new BassModel.BassSlur();
							sl.setNumber(1);
							sl.setType("stop");
							tech.setPulloff(pullList);
							notations.setSlur(sl);

						}

						// Hammer-on technique: START
						if (meas.get(x).charAt(nextColumn1) == 'h' || meas.get(x).charAt(nextColumn1) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {

							BassModel.BassHammer ham = new BassModel.BassHammer();
							ham.setNumber(1);
							ham.setType("start");
							ham.setSymbol("H");
							hammerList.add(ham);

							BassModel.BassSlur sr = new BassModel.BassSlur();
							sr.setNumber(1);
							sr.setType("start");
							tech.setHammer(hammerList);
							notations.setSlur(sr);
						}

						// Hammer-on technique: END
						if (meas.get(x).charAt(prevColumn1) == 'h' || meas.get(x).charAt(prevColumn1) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassHammer hammer = new BassModel.BassHammer();
							hammer.setNumber(1);
							hammer.setType("stop");

							hammerList.add(hammer);

							BassModel.BassSlur slur = new BassModel.BassSlur();
							slur.setNumber(1);
							slur.setType("stop");
							tech.setHammer(hammerList);
							notations.setSlur(slur);
						}
						// if it's a double digit fret tuning
						if (Character.isDigit(meas.get(x).charAt(nextColumn1))) {

							doubleDigit = new StringBuilder("").append(character)
									.append(meas.get(x).charAt(nextColumn1)).append("").toString(); // concatenates the
																									// two digits
							int numRepresentation = Integer.parseInt(doubleDigit);

							correctDoubleDigit = String.valueOf(numRepresentation);

							// Skip the digit in the nextColumn in our iteration provided we don't go over
							// the length
							if (y + 1 < meas.get(0).length()) {

								y++;

							}
							if (x - 1 >= 0) {
								if (!Character.isDigit(meas.get(x - 1).charAt(y))) {
									y--;
								}
							}
							isDoubleDigit = true;

						}
						System.out.println(isDoubleDigit);
						if (isDoubleDigit != true) {

							tech.setFret(correctDoubleDigit);
						}

						else {
							tech.setFret("" + character);
						}
						Integer stringNumber = (x + 1);
						tech.setString(stringNumber.toString());
						notations.setTechnical(tech);
						note.get(note.size() - 1).setNotations(notations);

					}

					/*
					 * If it's not a Harmonic Technical Note
					 */
					else {
						// Slide Techniques: START
						if (meas.get(x).charAt(nextColumn1) == 's') {
							BassModel.BassSlide sd = new BassModel.BassSlide();
							sd.setNumber(BeforenumberS);

							sd.setType("start");
							slideList.add(sd);
							notations.setSlides(slideList);
						}
						// Slide Techniques: END
						if (meas.get(x).charAt(prevColumn1) == 's') {
							BassModel.BassSlide sl1 = new BassModel.BassSlide();
							sl1.setNumber(AfternumberS);
							sl1.setType("stop");
							slideList.add(sl1);
							notations.setSlides(slideList);
						}
						if (meas.get(x).charAt(nextColumn1) == 'p' || meas.get(x).charAt(nextColumn1) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassPullOff pl = new BassModel.BassPullOff();
							pl.setNumber(BeforenumberP);
							pl.setType("start");
							pl.setSymbol("P");
							pullList.add(pl);
							if (meas.get(x).charAt(prevColumn1) == 'p' || meas.get(x).charAt(prevColumn1) == 'P') {
								technical.setPulloff(pullList);
							}

							else {
								BassModel.BassSlur su = new BassModel.BassSlur();
								su.setNumber(BeforenumberP);
								// su.setPlacement("above");
								su.setType("start");
								technical.setPulloff(pullList);
								notations.setSlur(su);
							}

						}

						// Pull-off techniques: END
						if (meas.get(x).charAt(prevColumn1) == 'p' || meas.get(x).charAt(prevColumn1) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassPullOff pull = new BassModel.BassPullOff();
							pull.setNumber(AfternumberP);
							pull.setType("stop");

							pullList.add(pull);
							if (meas.get(x).charAt(nextColumn1) == 'p' || meas.get(x).charAt(nextColumn1) == 'P') {
								technical.setPulloff(pullList);
							} else {
								BassModel.BassSlur sl = new BassModel.BassSlur();
								sl.setNumber(AfternumberP);
								sl.setType("stop");
								technical.setPulloff(pullList);
								notations.setSlur(sl);
							}
						}

						// Hammer-on technique: START
						if (meas.get(x).charAt(nextColumn1) == 'h' || meas.get(x).charAt(nextColumn1) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {

							BassModel.BassHammer ham = new BassModel.BassHammer();
							ham.setNumber(BeforenumberH);
							ham.setType("start");
							ham.setSymbol("H");
							hammerList.add(ham);
							if (meas.get(x).charAt(prevColumn1) == 'h' || meas.get(x).charAt(prevColumn1) == 'H') {
								technical.setHammer(hammerList);
							} else {
								BassModel.BassSlur sr = new BassModel.BassSlur();
								sr.setNumber(BeforenumberH);
								sr.setType("start");
								technical.setHammer(hammerList);
								notations.setSlur(sr);
							}
						}

						// Hammer-on technique: END
						if (meas.get(x).charAt(prevColumn1) == 'h' || meas.get(x).charAt(prevColumn1) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn1))
								&& Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							BassModel.BassHammer hammer = new BassModel.BassHammer();
							hammer.setNumber(AfternumberH);
							hammer.setType("stop");

							hammerList.add(hammer);
							if (meas.get(x).charAt(nextColumn1) == 'h' || meas.get(x).charAt(nextColumn1) == 'H') {
								technical.setHammer(hammerList);
							} else {
								BassModel.BassSlur slur = new BassModel.BassSlur();
								slur.setNumber(AfternumberH);
								slur.setType("stop");
								technical.setHammer(hammerList);
								notations.setSlur(slur);
							}
						}
						// if it's a double digit fret tuning
						if (Character.isDigit(meas.get(x).charAt(nextColumn1))) {
							System.out.println("reached");
							doubleDigit = new StringBuilder("").append(character)
									.append(meas.get(x).charAt(nextColumn1)).append("").toString(); // concatenates the
																									// two digits
							int numRepresentation = Integer.parseInt(doubleDigit);
							correctDoubleDigit = String.valueOf(numRepresentation);

							// Skip the digit in the nextColumn in our iteration provided we don't go over
							// the length
							if (y + 1 < meas.get(0).length()) {

								y++;

							}
							if (x - 1 >= 0) {
								if (!Character.isDigit(meas.get(x - 1).charAt(y))) {
									y--;
								}
							}
							isDoubleDigit = true;

						}
						if (isDoubleDigit == true) {

							technical.setFret(correctDoubleDigit);
						} else {
							technical.setFret("" + character);
						}
						Integer stringNumber = (x + 1);
						technical.setString(stringNumber.toString());
						notations.setTechnical(technical);
						note.get(note.size() - 1).setNotations(notations);
					}

				}
				// set has note in the column to true
				hasPrevColNote = true;
			}
		}
		if (variableRepeatExist == true) {
			BassModel.BassDirection dir = new BassModel.BassDirection();
			dir.setPlacement("above");
			BassModel.BassDirectionType dirType = new BassModel.BassDirectionType();
			BassModel.BassWords gWord = new BassModel.BassWords();
			gWord.setRelativeX();
			gWord.setRelativeY();
			gWord.setRepeatText(numVarRepeats);
			dirType.setWord(gWord);
			dir.setDirectionType(dirType);
			newMeasure.setDirection(dir);
			isThereRepeat = true;

		}

		if (forwardRepeatExist == true) {
			BassModel.ForwardBarline barForward = new BassModel.ForwardBarline();
			barForward.setLocation("left");
			barForward.setBarStyle("heavy-light");
			BassModel.BassRepeat repeatForward = new BassModel.BassRepeat();
			repeatForward.setDirection("forward");
			barForward.setBassRepeat(repeatForward);
			newMeasure.setForwardBarline(barForward);
			isThereRepeat = true;
		}

		if (backwardRepeatExist == true) {
			BassModel.BackwardBarline barBackward = new BassModel.BackwardBarline();
			barBackward.setLocation("right");
			barBackward.setBarStyle("light-heavy");
			BassModel.BassRepeat repeatBackward = new BassModel.BassRepeat();
			repeatBackward.setDirection("backward");
			barBackward.setBassRepeat(repeatBackward);
			newMeasure.setBackwardBarline(barBackward);
			isThereRepeat = true;
		}
		if (backwardRepeatExist == false && forwardRepeatExist == false && variableRepeatExist == false) {
			isThereRepeat = false;
			BassModel.BackwardBarline onebarline = new BassModel.BackwardBarline();
			onebarline.setBarStyle("light-heavy");
			onebarline.setLocation("right");
			newMeasure.setBackwardBarline(onebarline);
		}

		newMeasure.setNote(note);

		return newMeasure;
	}

	public static String guitarTabToXML(ArrayList<String> tabAsList, HashMap<Integer, Integer[]> timeSigs) {
		boolean isThereRepeat = false;
		try {
			ObjectMapper mapper = new XmlMapper();
			// InputStream inputStream = new
			// FileInputStream("C:\\Users\\shawn\\Desktop\\parts1.xml");
			// TypeReference<List<Part>> typeReference = new TypeReference<List<Part>>() {};
			// List<Part> parts = mapper.readValue(inputStream, typeReference);
			//
			// for (Part p : parts) {
			// System.out.println("name is: " + p.getId());
			// }

			// get a set of collections
			ArrayList<ArrayList<String>> collections = new ArrayList<>();
			collections = GuitarParser.tabToCollection(tabAsList);

			guitarModel.ScorePartwise scorePartwise = new guitarModel.ScorePartwise();
			scorePartwise.setVersion("3.1");
			guitarModel.PartList partList = new guitarModel.PartList();
			ArrayList<guitarModel.ScorePart> scoreParts = new ArrayList<guitarModel.ScorePart>();
			guitarModel.ScorePart scorepart = new guitarModel.ScorePart();
			scorepart.setId("P1");
			scorepart.setPartName("Classical Guitar");
			scoreParts.add(scorepart);

			partList.setScoreParts(scoreParts);
			scorePartwise.setPartList(partList);

			ArrayList<guitarModel.Part> parts = new ArrayList<guitarModel.Part>();
			guitarModel.Part part = new guitarModel.Part();
			part.setId("P1");
			parts.add(part);

			ArrayList<guitarModel.Measure> measures = new ArrayList<guitarModel.Measure>();

			int measureCount = 0;
			// iter through each collection
			for (int i = 0; i < collections.size(); i++) {

				// get tuning steps
				ArrayList<String> tuningSteps = getTuningSteps(collections.get(i));

				ArrayList<ArrayList<String>> measuresOfCollection = GuitarParser
						.collectionToMeasure(collections.get(i));

				// iter through each measure set
				for (int j = 0; j < measuresOfCollection.size(); j++) {
					measureCount++;
					guitarModel.Measure newMeasure = parseGuitarMeasure(measuresOfCollection.get(j), measureCount,
							tuningSteps, timeSigs, isThereRepeat);
					measures.add(newMeasure);
				}
			}
			// set last measure to have barline values
//			if (isThereRepeat == false) {
//				guitarModel.BackwardBarline onebarline = new guitarModel.BackwardBarline();
//				onebarline.setBarStyle("light-heavy");
//				onebarline.setLocation("right");
//				measures.get(measures.size() - 1).setBackwardBarline(onebarline);
//
//			}

//			
//			
			parts.get(0).setMeasures(measures);

			scorePartwise.setParts(parts);

			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// mapper.writeValue(new File("./Streamresult.musicxml"), scorePartwise);
			return mapper.writeValueAsString(scorePartwise).replace("backwardBarline", "barline");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static guitarModel.Measure parseGuitarMeasure(ArrayList<String> meas, int measureNumber,
			ArrayList<String> tuningSteps, HashMap<Integer, Integer[]> timeSigs, boolean isThereRepeat) {

		guitarModel.Measure newMeasure = new guitarModel.Measure();
		newMeasure.setNumber(measureNumber);
		Integer[] timeSig = timeSigs.getOrDefault(measureNumber, new Integer[] { 4, 4 });
		int division = GuitarParser.divisionCount(meas.get(2), timeSig[0]);

		// Repeating Measures
		Boolean forwardRepeatExist = false;
		Boolean backwardRepeatExist = false;
		Boolean variableRepeatExist = false;

		// if first measure, set the attributes
		if (measureNumber == 1) {
			guitarModel.Attributes attributes = new guitarModel.Attributes();
			guitarModel.Clef clef = new guitarModel.Clef();
			clef.setSign("TAB");
			clef.setLine("5");
			attributes.setClef(clef);

			attributes.setDivisions(division);

			guitarModel.Key key = new guitarModel.Key();
			key.setFifths("0");
			attributes.setKey(key);

			guitarModel.StaffDetails staffDetails = new guitarModel.StaffDetails();
			staffDetails.setStaffLines("6");

			// staff tunings
			ArrayList<guitarModel.StaffTuning> staffTunings = new ArrayList<guitarModel.StaffTuning>();

			for (int i = 0; i < tuningSteps.size(); i++) {
				guitarModel.StaffTuning staffTuning0 = new guitarModel.StaffTuning();
				staffTuning0.setLine(i + 1);
				if (!tuningSteps.get(i).equals("")) {
					staffTuning0.setTuningStep(tuningSteps.get(i));
				} else {
					switch (i) {
					case 0:
						staffTuning0.setTuningStep("E");
						break;
					case 1:
						staffTuning0.setTuningStep("A");
						break;

					case 2:
						staffTuning0.setTuningStep("D");
						break;

					case 3:
						staffTuning0.setTuningStep("G");
						break;

					case 4:
						staffTuning0.setTuningStep("B");
						break;

					case 5:
						staffTuning0.setTuningStep("E");
						break;
					}
				}

				switch (i) {
				case 0:
					staffTuning0.setTuningOctave("2");
					break;
				case 1:
					staffTuning0.setTuningOctave("2");
					break;

				case 2:
					staffTuning0.setTuningOctave("3");
					break;

				case 3:
					staffTuning0.setTuningOctave("3");
					break;

				case 4:
					staffTuning0.setTuningOctave("3");
					break;

				case 5:
					staffTuning0.setTuningOctave("4");
					break;
				}

				staffTunings.add(staffTuning0);
			}

			staffDetails.setStaffTunings(staffTunings);
			attributes.setStaffDetails(staffDetails);

			guitarModel.Time time = new guitarModel.Time();
			time.setBeats(timeSig[0].toString());
			time.setBeatType(timeSig[1].toString());
			attributes.setTime(time);

			newMeasure.setAttributes(attributes);
		} else {
			guitarModel.Attributes attributes = new guitarModel.Attributes();
			guitarModel.StaffDetails staffDetails = new guitarModel.StaffDetails();
			staffDetails.setStaffLines("6");
			// staff tunings
			ArrayList<guitarModel.StaffTuning> staffTunings = new ArrayList<guitarModel.StaffTuning>();

			for (int i = 0; i < tuningSteps.size(); i++) {
				guitarModel.StaffTuning staffTuning0 = new guitarModel.StaffTuning();
				staffTuning0.setLine(i + 1);
				if (!tuningSteps.get(i).equals("")) {
					staffTuning0.setTuningStep(tuningSteps.get(i));
				} else {
					switch (i) {
					case 0:
						staffTuning0.setTuningStep("E");
						break;
					case 1:
						staffTuning0.setTuningStep("A");
						break;

					case 2:
						staffTuning0.setTuningStep("D");
						break;

					case 3:
						staffTuning0.setTuningStep("G");
						break;

					case 4:
						staffTuning0.setTuningStep("B");
						break;

					case 5:
						staffTuning0.setTuningStep("E");
						break;
					}
				}

				switch (i) {
				case 0:
					staffTuning0.setTuningOctave("2");
					break;
				case 1:
					staffTuning0.setTuningOctave("2");
					break;

				case 2:
					staffTuning0.setTuningOctave("3");
					break;

				case 3:
					staffTuning0.setTuningOctave("3");
					break;

				case 4:
					staffTuning0.setTuningOctave("3");
					break;

				case 5:
					staffTuning0.setTuningOctave("4");
					break;
				}

				staffTunings.add(staffTuning0);
			}

			staffDetails.setStaffTunings(staffTunings);
			attributes.setStaffDetails(staffDetails);

			guitarModel.Time time = new guitarModel.Time();
			time.setBeats(timeSig[0].toString());
			time.setBeatType(timeSig[1].toString());
			attributes.setTime(time);

			attributes.setDivisions(division);
			newMeasure.setAttributes(attributes);
		}

//		newMeasure.setBarline(null);
		int BeforenumberP = 0; // for pull offs (before)
		int BeforenumberH = 0; // for hammer ons ""
		int AfternumberP = 0; // for pull offs (after)
		int AfternumberH = 0; // for hammer ons ""
		int BeforenumberS = 0;
		int AfternumberS = 0;
		ArrayList<guitarModel.Note> note = new ArrayList<guitarModel.Note>();
		int numVarRepeats = 0; // Number of Variable Repeats
		// iter through each measure
		for (int y = 0; y < meas.get(0).length(); y++) {

			Boolean hasPrevHammer = false; // reset automatically when new col is reached
			Boolean hasPrevPull = false; // same condition above

			Boolean hasPrevColNote = false;
			Boolean isGrace = false;
			int nextColumn = 0;
			int prevColumn = 0;
			if (y + 1 < meas.get(0).length()) { // prevent index out of bounds
				nextColumn = y + 1;
			}
			if (y - 1 > 0) {
				prevColumn = y - 1;
			}
			for (int x = meas.size() - 1; x >= 0; x--) {

				boolean isDoubleDigit = false;
				char character = meas.get(x).charAt(y);

				// Repeats: Variable Number Of Times
				if (Character.isDigit(character) && y == meas.get(0).length() - 1) {
					variableRepeatExist = true;
					numVarRepeats = Character.getNumericValue(character);
					break;
				}

				// Repeats: Forward Direction
				if (character == '|' && meas.get(x).charAt(nextColumn) == '*') {
					forwardRepeatExist = true;
				}

				// Repeats: Backward Direction
				else if (character == '*' && meas.get(x).charAt(nextColumn) == '|') {
					backwardRepeatExist = true;
				}

				// Variables to store double digit frets
				String doubleDigit = "";
				String correctDoubleDigit = "";

				if (Character.isDigit(character)) {
					// if it's a double digit fret tuning
					if (meas.get(x).charAt(nextColumn) == 'p' || meas.get(x).charAt(nextColumn) == 'P') {
						BeforenumberP++;

					} else {
						BeforenumberP = 0;
					}

					if (meas.get(x).charAt(prevColumn) == 'p' || meas.get(x).charAt(prevColumn) == 'P') {
						AfternumberP++;

					}

					else {

						AfternumberP = 0;

					}

					// Incrementing numbers for consecutive occurrences of Hammer ons
					if (meas.get(x).charAt(nextColumn) == 'h' || meas.get(x).charAt(nextColumn) == 'H') {

						BeforenumberH++;

					} else {
						BeforenumberH = 0;
					}
					if (meas.get(x).charAt(prevColumn) == 'h' || meas.get(x).charAt(prevColumn) == 'H') {
						AfternumberH++;

					} else {
						AfternumberH = 0;
					}

					// Incrementing numbers for consecutive occurrences of Slides
					if (meas.get(x).charAt(nextColumn) == 's') {

						BeforenumberS++;

					} else {
						BeforenumberS = 0;
					}
					if (meas.get(x).charAt(prevColumn) == 's') {

						AfternumberS++;

					} else {
						AfternumberS = 0;
					}

					// if it's a double digit fret tuning
					if (Character.isDigit(meas.get(x).charAt(nextColumn))) {
						isDoubleDigit = true;
						doubleDigit = new StringBuilder("").append(character).append(meas.get(x).charAt(nextColumn))
								.append("").toString(); // concatenates the two digits
						int numRepresentation = Integer.parseInt(doubleDigit);
						correctDoubleDigit = String.valueOf(numRepresentation);

						// hasPrevColNote = true;
						// Skip the digit in the nextColumn in our iteration provided we don't go over
						// the length

						if (y + 1 < meas.get(0).length()) {
							y++;

						}
						if (x - 1 >= 0) {
							if (!Character.isDigit(meas.get(x - 1).charAt(y))) {
								y--;
							}
						}

					}

					// if it's a graced note
					if (meas.get(x).charAt(prevColumn) == 'g') {
						isGrace = true;
						GraceNote grace = new guitarModel.GraceNote();
						grace.setDuration();
						grace.setNoteType();
						grace.setStem();// sets the stem value to "none"
						note.add(grace);
					}
					// if has previous note in column
					else if (hasPrevColNote) {
						note.add(new guitarModel.ChordNote());
					} else {
						note.add(new guitarModel.Note());
					}

					Integer duration = GuitarParser.durationCount(meas, y, division);

					if (isGrace == false) {
						note.get(note.size() - 1).setDuration(duration.toString());
						note.get(note.size() - 1).setType(GuitarParser.typeDeclare(duration, division));
					}

					note.get(note.size() - 1).setVoice("1");

					// if the note is length 2, it contains a sharp
					if (GuitarParser.stepCount(x, Character.getNumericValue(character), tuningSteps).length() == 2) {
						guitarModel.AlteredPitch pitch = new guitarModel.AlteredPitch();
						pitch.setAlter("1");
						pitch.setStep(GuitarParser.stepCount(x, Character.getNumericValue(character), tuningSteps)
								.substring(0, 1));
						pitch.setOctave(GuitarParser.octaveCount(x, Character.getNumericValue(character)));
						note.get(note.size() - 1).setPitch(pitch);
					} else {
						guitarModel.Pitch pitch = new guitarModel.Pitch();
						pitch.setStep(GuitarParser.stepCount(x, Character.getNumericValue(character), tuningSteps));
						pitch.setOctave(GuitarParser.octaveCount(x, Character.getNumericValue(character)));
						note.get(note.size() - 1).setPitch(pitch);
					}

					// set notations, technical is a sub-element of notations
					guitarModel.Notations notations = new guitarModel.Notations();
					guitarModel.Technical technical = new guitarModel.Technical();
					guitarModel.HarmonicTechnical tech = new guitarModel.HarmonicTechnical();
					ArrayList<guitarModel.PullOff> pullList = new ArrayList<guitarModel.PullOff>();
					ArrayList<guitarModel.HammerOn> hammerList = new ArrayList<guitarModel.HammerOn>();
					ArrayList<guitarModel.Slides> slideList = new ArrayList<guitarModel.Slides>();

					// Natural Harmonics or not
					if (meas.get(x).charAt(prevColumn) == '[' && meas.get(x).charAt(nextColumn) == ']'
							|| meas.get(x).charAt(prevColumn) == '[' && meas.get(x).charAt(nextColumn + 1) == ']') {
						guitarModel.Harmonic h = new guitarModel.Harmonic();
						h.setNatural();
						tech.setHarmonics(h);

						// Slide Techniques: START
						if (meas.get(x).charAt(nextColumn) == 's') {
							guitarModel.Slides slide = new guitarModel.Slides();
							slide.setNumber(1);
							slide.setType("start");
							slideList.add(slide);
							notations.setSlides(slideList);
						}
						// Slide Techniques: END
						if (meas.get(x).charAt(prevColumn) == 's') {
							guitarModel.Slides s11 = new guitarModel.Slides();
							s11.setNumber(1);
							s11.setType("stop");
							slideList.add(s11);
							notations.setSlides(slideList);
						}

						// Pull-off techniques: START
						if (meas.get(x).charAt(nextColumn) == 'p' || meas.get(x).charAt(nextColumn) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							guitarModel.PullOff pl = new guitarModel.PullOff();
							pl.setNumber(1);
							pl.setType("start");
							pl.setSymbol("P");
							pullList.add(pl);

							guitarModel.Slur su = new guitarModel.Slur();
							su.setNumber(1);
							// su.setPlacement("above");
							su.setType("start");
							tech.setPull(pullList);
							notations.setSlur(su);

						}

						// Pull-off techniques: END
						if (meas.get(x).charAt(prevColumn) == 'p' || meas.get(x).charAt(prevColumn) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							guitarModel.PullOff pull = new guitarModel.PullOff();
							pull.setNumber(1);
							pull.setType("stop");
							pullList.add(pull);

							if (meas.get(x).charAt(nextColumn) == 'p' || meas.get(x).charAt(nextColumn) == 'P') {
								tech.setPull(pullList);
							} else {
								guitarModel.Slur sl = new guitarModel.Slur();
								sl.setNumber(1);
								sl.setType("stop");
								tech.setPull(pullList);
								notations.setSlur(sl);
							}

						}

						// Hammer-on technique: STARTmeas.get(x).charAt(nextColumn) == 'h'
						if (meas.get(x).charAt(nextColumn) == 'h' || meas.get(x).charAt(nextColumn) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {

							guitarModel.HammerOn ham = new guitarModel.HammerOn();
							ham.setNumber(1);
							ham.setType("start");
							ham.setSymbol("H");
							hammerList.add(ham);
							if (meas.get(x).charAt(prevColumn) == 'h' || meas.get(x).charAt(prevColumn) == 'h') {
								tech.setHammer(hammerList);
							} else {
								guitarModel.Slur sr = new guitarModel.Slur();
								sr.setNumber(1);
								sr.setType("start");
								tech.setHammer(hammerList);
								notations.setSlur(sr);
							}
						}

						// Hammer-on technique: END
						if (meas.get(x).charAt(prevColumn) == 'h' || meas.get(x).charAt(prevColumn) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							guitarModel.HammerOn hammer = new guitarModel.HammerOn();
							hammer.setNumber(1);
							hammer.setType("stop");

							hammerList.add(hammer);
							if (meas.get(x).charAt(nextColumn) == 'h' || meas.get(x).charAt(nextColumn) == 'H') {
								tech.setHammer(hammerList);
							} else {
								guitarModel.Slur slur = new guitarModel.Slur();
								slur.setNumber(1);
								slur.setType("stop");
								tech.setHammer(hammerList);
								notations.setSlur(slur);
							}
						}
						if (isDoubleDigit == true) {

							tech.setFret(correctDoubleDigit);

						} else {

							tech.setFret("" + character);

						}
						Integer stringNumber = (x + 1);
						tech.setString(stringNumber.toString());
						notations.setTechnical(tech);
						note.get(note.size() - 1).setNotations(notations);

					}

					/*
					 * If it's not a Harmonic Technical Note
					 */
					else {
						// Slide Techniques: START
						if (meas.get(x).charAt(nextColumn) == 's') {
							guitarModel.Slides sd = new guitarModel.Slides();
							sd.setNumber(BeforenumberS);

							sd.setType("start");
							slideList.add(sd);
							notations.setSlides(slideList);
						}
						// Slide Techniques: END
						if (meas.get(x).charAt(prevColumn) == 's') {
							guitarModel.Slides sl1 = new guitarModel.Slides();
							sl1.setNumber(AfternumberS);
							sl1.setType("stop");
							slideList.add(sl1);
							notations.setSlides(slideList);
						}

						// Pull Off: Beginning
						if (meas.get(x).charAt(nextColumn) == 'p' || meas.get(x).charAt(nextColumn) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							hasPrevPull = true;
							guitarModel.PullOff pl = new guitarModel.PullOff();

							pl.setNumber(BeforenumberP);
							pl.setType("start");
							pl.setSymbol("P");
							pullList.add(pl);
							if (meas.get(x).charAt(prevColumn) == 'p' || meas.get(x).charAt(prevColumn) == 'P') {
								technical.setPull(pullList);
							} else {
								guitarModel.Slur su = new guitarModel.Slur();
								su.setNumber(BeforenumberP);
								// su.setPlacement("above");
								su.setType("start");
								technical.setPull(pullList);
								notations.setSlur(su);
							}

						}

						// Pull-off techniques: END
						if (meas.get(x).charAt(prevColumn) == 'p' || meas.get(x).charAt(prevColumn) == 'P'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							hasPrevPull = true;
							guitarModel.PullOff pull = new guitarModel.PullOff();
							pull.setNumber(AfternumberP);
							pull.setType("stop");
							pullList.add(pull);
							if (meas.get(x).charAt(nextColumn) == 'p' || meas.get(x).charAt(nextColumn) == 'P') {
								technical.setPull(pullList);
							} else {
								guitarModel.Slur sl = new guitarModel.Slur();
								sl.setNumber(AfternumberP);
								sl.setType("stop");
								technical.setPull(pullList);
								notations.setSlur(sl);
							}

						}

						// Hammer-on technique: START
						if (meas.get(x).charAt(nextColumn) == 'h' || meas.get(x).charAt(nextColumn) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							hasPrevHammer = true;
							guitarModel.HammerOn ham = new guitarModel.HammerOn();
							ham.setNumber(BeforenumberH);
							ham.setType("start");
							ham.setSymbol("H");
							hammerList.add(ham);

							if (meas.get(x).charAt(prevColumn) == 'h' || meas.get(x).charAt(prevColumn) == 'H') {
								technical.setHammer(hammerList);
							}

							else {
								guitarModel.Slur sr = new guitarModel.Slur();
								sr.setNumber(BeforenumberH);
								sr.setType("start");
								technical.setHammer(hammerList);
								notations.setSlur(sr);
							}
						}

						// Hammer-on technique: END
						if (meas.get(x).charAt(prevColumn) == 'h' || meas.get(x).charAt(prevColumn) == 'H'
								&& Character.isDigit(meas.get(x).charAt(prevColumn))
								&& Character.isDigit(meas.get(x).charAt(nextColumn))) {
							hasPrevHammer = true;
							guitarModel.HammerOn hammer = new guitarModel.HammerOn();
							hammer.setNumber(AfternumberH);
							hammer.setType("stop");

							hammerList.add(hammer);
							if (meas.get(x).charAt(nextColumn) == 'h' || meas.get(x).charAt(nextColumn) == 'H') {
								technical.setHammer(hammerList);
							}

							else {
								guitarModel.Slur slur = new guitarModel.Slur();
								slur.setNumber(AfternumberH);
								slur.setType("stop");
								technical.setHammer(hammerList);
								notations.setSlur(slur);
							}

						}
						if (isDoubleDigit == true) {

							technical.setFret(correctDoubleDigit);

						}

						else {
							technical.setFret("" + character);
						}
						Integer stringNumber = (x + 1);
						technical.setString(stringNumber.toString());
						notations.setTechnical(technical);
						note.get(note.size() - 1).setNotations(notations);
					}

					// set has note in the column to true
					hasPrevColNote = true;
				}
			}
		}
		if (variableRepeatExist == true) {
			guitarModel.GuitarDirection dir = new guitarModel.GuitarDirection();
			dir.setPlacement("above");
			guitarModel.GuitarDirectionType dirType = new guitarModel.GuitarDirectionType();
			guitarModel.GuitarWords gWord = new guitarModel.GuitarWords();
			gWord.setRelativeX();
			gWord.setRelativeY();
			gWord.setRepeatText(numVarRepeats);
			dirType.setWord(gWord);
			dir.setDirectionType(dirType);
			newMeasure.setDirection(dir);
			isThereRepeat = true;

		}

		if (forwardRepeatExist == true) {
			guitarModel.ForwardBarline barForward = new guitarModel.ForwardBarline();
			barForward.setLocation("left");
			barForward.setBarStyle("heavy-light");
			guitarModel.GuitarRepeat repeatForward = new guitarModel.GuitarRepeat();
			repeatForward.setDirection("forward");
			barForward.setRepeat(repeatForward);
			newMeasure.setForwardBarline(barForward);
			isThereRepeat = true;
		}

//		System.out.println(backwardRepeatExist);
		if (backwardRepeatExist == true) {
			guitarModel.BackwardBarline barBackward = new guitarModel.BackwardBarline();
			barBackward.setLocation("right");
			barBackward.setBarStyle("light-heavy");
			guitarModel.GuitarRepeat repeatBackward = new guitarModel.GuitarRepeat();
			repeatBackward.setDirection("backward");
			barBackward.setRepeat(repeatBackward);
			newMeasure.setBackwardBarline(barBackward);
			isThereRepeat = true;
		}
		if (backwardRepeatExist == false && forwardRepeatExist == false && variableRepeatExist == false) {
			guitarModel.BackwardBarline onebarline = new guitarModel.BackwardBarline();
			onebarline.setBarStyle("light-heavy");
			onebarline.setLocation("right");
			newMeasure.setBackwardBarline(onebarline);
		}

		newMeasure.setNote(note);

		return newMeasure;
	}

	public static String drumTabToXML(ArrayList<String> tabAsList, HashMap<Integer, Integer[]> timeSigs) {
		boolean isThereRepeat = false;
		try {
			ObjectMapper mapper = new XmlMapper();

			// get a set of collections
			ArrayList<ArrayList<String>> collections = new ArrayList<>();
			collections = DParser.tabToCollection(tabAsList);

			DrumModel.ScorePartwise scorePartwise = new DrumModel.ScorePartwise();
			scorePartwise.setVersion("3.1");
			DrumModel.PartList partList = new DrumModel.PartList();
			ArrayList<DrumModel.ScorePart> scoreParts = new ArrayList<DrumModel.ScorePart>();
			DrumModel.ScorePart scorepart = new DrumModel.ScorePart();
			ArrayList<DrumModel.ScoreInstrument> scoreInst = new ArrayList<DrumModel.ScoreInstrument>();
			scorepart.setId("P1");
			scorepart.setPartName("Drumset");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I36");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Bass Drum 1");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I37");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Bass Drum 2");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I38");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Side Stick");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I39");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Snare");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I42");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Low Floor Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I43");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Closed Hi-Hat");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I44");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("High Floor Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I45");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Pedal Hi-Hat");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I46");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Low Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I47");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Open Hi-Hat");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I48");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Low-Mid Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I49");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Hi-Mid Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I50");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Crash Cymbal 1");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I51");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("High Tom");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I52");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Ride Cymbal 1");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I53");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Chinese Cymbal");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I54");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Ride Bell");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I55");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Tambourine");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I56");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Splash Cymbal");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I57");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Cowbell");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I58");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Crash Cymbal 2");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I60");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Ride Cymbal 2");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I64");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Open Hi Conga");
			scoreInst.add(new DrumModel.ScoreInstrument());
			scoreInst.get(scoreInst.size() - 1).setID("P1-I65");
			scoreInst.get(scoreInst.size() - 1).setInstrumentName("Low Conga");
			scorepart.setScoreInstruments(scoreInst);
			scoreParts.add(scorepart);

			partList.setScoreParts(scoreParts);
			scorePartwise.setPartList(partList);

			ArrayList<DrumModel.Part> parts = new ArrayList<DrumModel.Part>();
			DrumModel.Part part = new DrumModel.Part();
			part.setId("P1");
			parts.add(part);

			ArrayList<DrumModel.Measure> measures = new ArrayList<DrumModel.Measure>();

			int measureCount = 0;
			// iter through each collection
			for (int i = 0; i < collections.size(); i++) {
				ArrayList<ArrayList<String>> measuresOfCollection = DParser.collectionToMeasure(collections.get(i));
				ArrayList<ArrayList<String>> instrumentIDs = DParser.collectionToMeasureExtractID(collections.get(i));
				ArrayList<Integer> repeatedmeasures = DParser.collectionToMeasureRepeatedMeasure(collections.get(i));
				ArrayList<Integer> numberofrepeats = DParser.collectionToMeasureRepeatedAmount(collections.get(i));

				// iter through each measure set
				for (int j = 0; j < measuresOfCollection.size(); j++) {
					measureCount++;
					DrumModel.Measure newMeasure = parseDrumMeasure(measuresOfCollection.get(j), instrumentIDs.get(j),
							repeatedmeasures.get(j), numberofrepeats.get(j), measureCount, timeSigs, isThereRepeat);
					measures.add(newMeasure);
				}
			}

			// set last measure to have barline values
			guitarModel.BackwardBarline onebarline = new guitarModel.BackwardBarline();
			onebarline.setBarStyle("light-heavy");
			onebarline.setLocation("right");
			measures.get(measures.size() - 1).setBackwardBarline(onebarline);

			parts.get(0).setMeasures(measures);

			scorePartwise.setParts(parts);

			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			// mapper.writeValue(new File("./Streamresult.musicxml"), scorePartwise);
			return mapper.writeValueAsString(scorePartwise).replace("noteBack", "note").replace("backwardBarline", "barline");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static DrumModel.Measure parseDrumMeasure(ArrayList<String> meas, ArrayList<String> IDs,
			Integer repeatedmeasures, Integer repeatedAmount, int measureNumber, HashMap<Integer, Integer[]> timeSigs, boolean isThereRepeat) {

		DrumModel.Measure newMeasure = new DrumModel.Measure();
		newMeasure.setNumber(measureNumber);
		Integer[] timeSig = timeSigs.getOrDefault(measureNumber, new Integer[] { 4, 4 });
		int division = DParser.divisionCount(meas.get(2), timeSig[0]);

		DrumModel.Attributes attributes = new DrumModel.Attributes();
		// if first measure, set the attributes
		if (measureNumber == 1) {
			DrumModel.Clef clef = new DrumModel.Clef();
			clef.setSign("percussion");
			clef.setLine("2");
			attributes.setClef(clef);

			attributes.setDivisions(division);

			DrumModel.Key key = new DrumModel.Key();
			key.setFifths("0");
			attributes.setKey(key);

			DrumModel.Time time = new DrumModel.Time();
			time.setBeats("4");
			time.setBeatType("4");
			attributes.setTime(time);

			newMeasure.setAttributes(attributes);
		} else {
			DrumModel.Time time = new DrumModel.Time();
			time.setBeats(timeSig[0].toString());
			time.setBeatType(timeSig[1].toString());
			attributes.setTime(time);

			attributes.setDivisions(division);
			newMeasure.setAttributes(attributes);
		}

		ArrayList<Object> note = new ArrayList<Object>();
		Integer backupcount = 0;
		int beamcount = 0;
		// iter through each measure
		for (int y = 0; y < meas.get(0).length(); y++) {
			Boolean hasPrevColNote = false;
			for (int x = meas.size() - 2; x >= 0; x--) {
				char character = meas.get(x).charAt(y);
				String ID = IDs.get(x);

				if (character == 'x' || character == 'o' || character == 'd' || character == 'f' || character == 'X') {
					// if has previous note in column
					Integer duration = DParser.durationCount(meas, y);
					String type = DParser.typeDeclare(duration, meas.get(0).length());
					String gtype = DParser.typeDeclareGrace(type);
					String gtype2 = DParser.typeDeclareTwoGrace(type);

					Instrument instrument = new Instrument();
					instrument.setID(DParser.identifyID(ID, character));

					Unpitched unpitched = new Unpitched();
					unpitched.setDisplayOctave(DParser.octaveCount(ID));
					unpitched.setDisplayStep(DParser.stepCount(ID));

					GraceNoteD newd = new GraceNoteD();
					Grace grace = new Grace();
					grace.setSlash("yes");
					newd.setGrace(grace);

					GraceNoteD newd1 = new GraceNoteD();
					Grace grace1 = new Grace();
					grace1.setSlash("yes");
					newd1.setGrace(grace1);

					GraceNoteD newd2 = new GraceNoteD();
					Grace grace2 = new Grace();
					grace2.setSlash("yes");
					newd2.setGrace(grace2);

					if (hasPrevColNote == false) {
						if (character == 'o') {
							if (DParser.beamNumber(type) == 0) {
								if (ID.equals("CC") || ID.equals("C ") || ID.equals("HH") || ID.equals("Rd")
										|| ID.equals("RD") || ID.equals("R ")) {
									DrumModel.NoteNH newn = new DrumModel.NoteNH();
									newn.setNotehead("circle-x");
									note.add(newn);
									((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

									((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
								} else {
									note.add(new DrumModel.Note());
									((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note) note.get(note.size() - 1)).setType(type);

									((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

									((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
								}
							} else if (DParser.beamNumber(type) == 1) {
								if (ID.equals("CC") || ID.equals("C ") || ID.equals("HH") || ID.equals("Rd")
										|| ID.equals("RD") || ID.equals("R ")) {
									if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
										DrumModel.NoteNH1B newn = new DrumModel.NoteNH1B();
										DrumModel.Beam beam = new DrumModel.Beam();
										newn.setNotehead("circle-x");
										beam.setNumber("1");
										beam.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam);
										note.add(newn);
										beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
												beamcount);
										((DrumModel.NoteNH1B) note.get(note.size() - 1))
												.setDuration(duration.toString());

										((DrumModel.NoteNH1B) note.get(note.size() - 1)).setType(type);

										((DrumModel.NoteNH1B) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.NoteNH1B) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.NoteNH1B) note.get(note.size() - 1)).setStem("up");

										((DrumModel.NoteNH1B) note.get(note.size() - 1)).setUnpitched(unpitched);
									} else {
										DrumModel.NoteNH newn = new DrumModel.NoteNH();
										newn.setNotehead("circle-x");
										note.add(newn);
										((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

										((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

										((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
									}
								} else {
									if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
										DrumModel.Note1B newn = new DrumModel.Note1B();
										DrumModel.Beam beam = new DrumModel.Beam();
										beam.setNumber("1");
										beam.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam);
										note.add(newn);
										beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
												beamcount);
										((DrumModel.Note1B) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.Note1B) note.get(note.size() - 1)).setType(type);

										((DrumModel.Note1B) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.Note1B) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.Note1B) note.get(note.size() - 1)).setStem("up");

										((DrumModel.Note1B) note.get(note.size() - 1)).setUnpitched(unpitched);
									} else {
										note.add(new DrumModel.Note());
										((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.Note) note.get(note.size() - 1)).setType(type);

										((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

										((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
									}
								}
							} else if (DParser.beamNumber(type) == 2) {
								if (ID.equals("CC") || ID.equals("C ") || ID.equals("HH") || ID.equals("Rd")
										|| ID.equals("RD") || ID.equals("R ")) {
									if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
										DrumModel.NoteNH2B newn = new DrumModel.NoteNH2B();
										newn.setNotehead("circle-x");
										DrumModel.Beam beam1 = new DrumModel.Beam();
										beam1.setNumber("1");
										beam1.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam1, 0);
										DrumModel.Beam beam2 = new DrumModel.Beam();
										beam2.setNumber("2");
										beam2.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam2, 1);
										note.add(newn);
										beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
												beamcount);
										((DrumModel.NoteNH2B) note.get(note.size() - 1))
												.setDuration(duration.toString());

										((DrumModel.NoteNH2B) note.get(note.size() - 1)).setType(type);

										((DrumModel.NoteNH2B) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.NoteNH2B) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.NoteNH2B) note.get(note.size() - 1)).setStem("up");

										((DrumModel.NoteNH2B) note.get(note.size() - 1)).setUnpitched(unpitched);
									} else {
										DrumModel.NoteNH newn = new DrumModel.NoteNH();
										newn.setNotehead("circle-x");
										note.add(newn);
										((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

										((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

										((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
									}
								} else {
									if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
										DrumModel.Note2B newn = new DrumModel.Note2B();
										DrumModel.Beam beam1 = new DrumModel.Beam();
										beam1.setNumber("1");
										beam1.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam1, 0);
										DrumModel.Beam beam2 = new DrumModel.Beam();
										beam2.setNumber("2");
										beam2.setValue(DParser.beamState(meas, type, x, y, beamcount));
										newn.setBeam(beam2, 1);
										note.add(newn);
										beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
												beamcount);
										((DrumModel.Note2B) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.Note2B) note.get(note.size() - 1)).setType(type);

										((DrumModel.Note2B) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.Note2B) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.Note2B) note.get(note.size() - 1)).setStem("up");

										((DrumModel.Note2B) note.get(note.size() - 1)).setUnpitched(unpitched);
									} else {
										note.add(new DrumModel.Note());
										((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

										((DrumModel.Note) note.get(note.size() - 1)).setType(type);

										((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

										((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
										((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

										((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
									}
								}
							}
						} else if (character == 'x' || character == 'X' || character == '#') {
							String noteh = Character.toString(character);
							if (DParser.beamNumber(type) == 0) {
								DrumModel.NoteNH newn = new DrumModel.NoteNH();
								newn.setNotehead(noteh);
								note.add(newn);
								((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

								((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

								((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

								((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
								((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

								((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
							} else if (DParser.beamNumber(type) == 1) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									DrumModel.NoteNH1B newn = new DrumModel.NoteNH1B();
									newn.setNotehead(noteh);
									DrumModel.Beam beam = new DrumModel.Beam();
									beam.setNumber("1");
									beam.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setType(type);

									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setStem("up");

									((DrumModel.NoteNH1B) note.get(note.size() - 1)).setUnpitched(unpitched);
								} else {
									DrumModel.NoteNH newn = new DrumModel.NoteNH();
									newn.setNotehead(noteh);
									note.add(newn);
									((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

									((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
								}
							} else if (DParser.beamNumber(type) == 2) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									DrumModel.NoteNH2B newn = new DrumModel.NoteNH2B();
									newn.setNotehead(noteh);
									DrumModel.Beam beam1 = new DrumModel.Beam();
									beam1.setNumber("1");
									beam1.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam1, 0);
									DrumModel.Beam beam2 = new DrumModel.Beam();
									beam2.setNumber("2");
									beam2.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam2, 1);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setType(type);

									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setStem("up");

									((DrumModel.NoteNH2B) note.get(note.size() - 1)).setUnpitched(unpitched);
								} else {
									DrumModel.NoteNH newn = new DrumModel.NoteNH();
									newn.setNotehead(noteh);
									note.add(newn);
									((DrumModel.NoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.NoteNH) note.get(note.size() - 1)).setType(type);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.NoteNH) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.NoteNH) note.get(note.size() - 1)).setStem("up");

									((DrumModel.NoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
								}
							}
						} else if (character == 'f') {
							if (DParser.beamNumber(type) == 0) {
								note.add(newd);

								((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

								((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

								((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
								((GraceNoteD) note.get(note.size() - 1)).setStem("up");

								((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
								note.add(new DrumModel.Note());
								((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

								((DrumModel.Note) note.get(note.size() - 1)).setType(type);

								instrument.setID(DParser.identifyID(ID, character));
								((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

								((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
								((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

								unpitched.setDisplayOctave(DParser.octaveCount(ID));
								unpitched.setDisplayStep(DParser.stepCount(ID));
								((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
							} else if (DParser.beamNumber(type) == 1) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									note.add(newd);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									DrumModel.Note1B newn = new DrumModel.Note1B();
									DrumModel.Beam beam = new DrumModel.Beam();
									beam.setNumber("1");
									beam.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.Note1B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note1B) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note1B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note1B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note1B) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note1B) note.get(note.size() - 1)).setUnpitched(unpitched);

								} else {
									note.add(newd);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(new DrumModel.Note());
									((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);

								}
							} else if (DParser.beamNumber(type) == 2) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									note.add(newd);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									DrumModel.Note2B newn = new DrumModel.Note2B();
									DrumModel.Beam beam1 = new DrumModel.Beam();
									beam1.setNumber("1");
									beam1.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam1, 0);
									DrumModel.Beam beam2 = new DrumModel.Beam();
									beam2.setNumber("2");
									beam2.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam2, 1);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.Note2B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note2B) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note2B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note2B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note2B) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note2B) note.get(note.size() - 1)).setUnpitched(unpitched);
								} else {
									note.add(newd);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(new DrumModel.Note());
									((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
								}
							}
						} else if (character == 'd') {
							if (DParser.beamNumber(type) == 0) {
								note.add(newd1);

								((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

								((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

								((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
								((GraceNoteD) note.get(note.size() - 1)).setStem("up");

								((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
								note.add(newd2);

								((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

								instrument.setID(DParser.identifyID(ID, character));
								((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

								((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
								((GraceNoteD) note.get(note.size() - 1)).setStem("up");

								unpitched.setDisplayOctave(DParser.octaveCount(ID));
								unpitched.setDisplayStep(DParser.stepCount(ID));
								((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
								note.add(new DrumModel.Note());
								((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

								((DrumModel.Note) note.get(note.size() - 1)).setType(type);

								instrument.setID(DParser.identifyID(ID, character));
								((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

								((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
								((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

								unpitched.setDisplayOctave(DParser.octaveCount(ID));
								unpitched.setDisplayStep(DParser.stepCount(ID));
								((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
							} else if (DParser.beamNumber(type) == 1) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									note.add(newd1);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(newd2);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									instrument.setID(DParser.identifyID(ID, character));
									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									DrumModel.Note1B newn = new DrumModel.Note1B();
									DrumModel.Beam beam = new DrumModel.Beam();
									beam.setNumber("1");
									beam.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.Note1B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note1B) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note1B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note1B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note1B) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note1B) note.get(note.size() - 1)).setUnpitched(unpitched);

								} else {
									note.add(newd1);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(newd2);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									instrument.setID(DParser.identifyID(ID, character));
									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(new DrumModel.Note());
									((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);

								}
							} else if (DParser.beamNumber(type) == 2) {
								if (DParser.beamState(meas, type, x, y, beamcount) != "No beam") {
									note.add(newd1);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(newd2);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									instrument.setID(DParser.identifyID(ID, character));
									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									DrumModel.Note2B newn = new DrumModel.Note2B();
									DrumModel.Beam beam1 = new DrumModel.Beam();
									beam1.setNumber("1");
									beam1.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam1, 0);
									DrumModel.Beam beam2 = new DrumModel.Beam();
									beam2.setNumber("2");
									beam2.setValue(DParser.beamState(meas, type, x, y, beamcount));
									newn.setBeam(beam2, 1);
									note.add(newn);
									beamcount = DParser.countBeam(DParser.beamState(meas, type, x, y, beamcount),
											beamcount);
									((DrumModel.Note2B) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note2B) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note2B) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note2B) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note2B) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note2B) note.get(note.size() - 1)).setUnpitched(unpitched);
								} else {
									note.add(newd1);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(newd2);

									((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

									instrument.setID(DParser.identifyID(ID, character));
									((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

									((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
									((GraceNoteD) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
									note.add(new DrumModel.Note());
									((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

									((DrumModel.Note) note.get(note.size() - 1)).setType(type);

									instrument.setID(DParser.identifyID(ID, character));
									((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

									((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
									((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

									unpitched.setDisplayOctave(DParser.octaveCount(ID));
									unpitched.setDisplayStep(DParser.stepCount(ID));
									((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
								}
							}
						}
						backupcount = backupcount + duration;
					} else if (hasPrevColNote == true) {
						if (character == 'o') {
							if (ID.equals("CC") || ID.equals("C ") || ID.equals("HH") || ID.equals("Rd")
									|| ID.equals("RD") || ID.equals("R ")) {
								DrumModel.ChordNoteNH newn = new DrumModel.ChordNoteNH();
								newn.setNotehead("circle-x");
								note.add(newn);
								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setType(type);

								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setInstrument(instrument);

								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setVoice("1");
								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setStem("up");

								((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
							} else {
								note.add(new DrumModel.ChordNote());
								((DrumModel.ChordNote) note.get(note.size() - 1)).setDuration(duration.toString());

								((DrumModel.ChordNote) note.get(note.size() - 1)).setType(type);

								((DrumModel.ChordNote) note.get(note.size() - 1)).setInstrument(instrument);

								((DrumModel.ChordNote) note.get(note.size() - 1)).setVoice("1");
								((DrumModel.ChordNote) note.get(note.size() - 1)).setStem("up");

								((DrumModel.ChordNote) note.get(note.size() - 1)).setUnpitched(unpitched);
							}
						} else if (character == 'x' || character == 'X') {
							DrumModel.ChordNoteNH newn = new DrumModel.ChordNoteNH();
							newn.setNotehead(Character.toString(character));
							note.add(newn);
							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setDuration(duration.toString());

							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setType(type);

							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setInstrument(instrument);

							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setVoice("1");
							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setStem("up");

							((DrumModel.ChordNoteNH) note.get(note.size() - 1)).setUnpitched(unpitched);
						} else if (character == 'f') {
							note.add(newd);

							((GraceNoteD) note.get(note.size() - 1)).setType(gtype);

							((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

							((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
							((GraceNoteD) note.get(note.size() - 1)).setStem("up");

							((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
							note.add(new DrumModel.Note());
							((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

							((DrumModel.Note) note.get(note.size() - 1)).setType(type);

							instrument.setID(DParser.identifyID(ID, character));
							((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

							((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
							((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

							unpitched.setDisplayOctave(DParser.octaveCount(ID));
							unpitched.setDisplayStep(DParser.stepCount(ID));
							((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
						} else if (character == 'd') {
							note.add(newd1);

							((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

							((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

							((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
							((GraceNoteD) note.get(note.size() - 1)).setStem("up");

							((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
							note.add(newd2);

							((GraceNoteD) note.get(note.size() - 1)).setType(gtype2);

							instrument.setID(DParser.identifyID(ID, character));
							((GraceNoteD) note.get(note.size() - 1)).setInstrument(instrument);

							((GraceNoteD) note.get(note.size() - 1)).setVoice("1");
							((GraceNoteD) note.get(note.size() - 1)).setStem("up");

							unpitched.setDisplayOctave(DParser.octaveCount(ID));
							unpitched.setDisplayStep(DParser.stepCount(ID));
							((GraceNoteD) note.get(note.size() - 1)).setUnpitched(unpitched);
							note.add(new DrumModel.Note());
							((DrumModel.Note) note.get(note.size() - 1)).setDuration(duration.toString());

							((DrumModel.Note) note.get(note.size() - 1)).setType(type);

							instrument.setID(DParser.identifyID(ID, character));
							((DrumModel.Note) note.get(note.size() - 1)).setInstrument(instrument);

							((DrumModel.Note) note.get(note.size() - 1)).setVoice("1");
							((DrumModel.Note) note.get(note.size() - 1)).setStem("up");

							unpitched.setDisplayOctave(DParser.octaveCount(ID));
							unpitched.setDisplayStep(DParser.stepCount(ID));
							((DrumModel.Note) note.get(note.size() - 1)).setUnpitched(unpitched);
						}
					}
					// set has note in the column to true
					if (beamcount == 4) {
						beamcount = 0;
					}
					hasPrevColNote = true;
				}
			}
		}

		// backup here
		DrumModel.Backup bup = new DrumModel.Backup();
		ArrayList<Object> noteback = new ArrayList<Object>();
		bup.setDuration("0");
		for (int y = 0; y < meas.get(0).length(); y++) {
			char character = meas.get(meas.size() - 1).charAt(y);
			if (character == 'o') {
				Integer n = backupcount - y;
				bup.setDuration(n.toString());
				break;
			}
		}

		Integer sync = 0;
		String ID = IDs.get(IDs.size() - 1);
		for (int y = 0; y < meas.get(0).length(); y++) {
			char character = meas.get(meas.size() - 1).charAt(y);
			if (character == 'o') {
				noteback.add(new DrumModel.Note());
				for (int x = meas.size() - 2; x >= 2; x--) {
					if (meas.get(x).charAt(y) == 'o') {
						int tmp = 1;
						for (int i = y + 1; i < meas.get(0).length(); i++) {
							if (meas.get(x).charAt(i) == 'o') {
								tmp++;
							} else {
								break;
							}
						}
						if (tmp > sync) {
							sync = tmp;
						}
					}
				}

				Integer duration;
				Integer rest = 8 - sync;
				if (sync > 0) {
					duration = sync;
				} else {
					duration = DParser.durationCountLastLine(meas, y);
				}

				((DrumModel.Note) noteback.get(noteback.size() - 1)).setDuration(duration.toString());
				((DrumModel.Note) noteback.get(noteback.size() - 1)).setType(DParser.typeDeclare(duration, meas.get(0).length()));

				Instrument instrument = new Instrument();
				instrument.setID(DParser.identifyID(ID, character));
				((DrumModel.Note) noteback.get(noteback.size() - 1)).setInstrument(instrument);

				((DrumModel.Note) noteback.get(noteback.size() - 1)).setVoice("2");
				((DrumModel.Note) noteback.get(noteback.size() - 1)).setStem("down");

				Unpitched unpitched = new Unpitched();
				unpitched.setDisplayOctave(DParser.octaveCount(ID));
				unpitched.setDisplayStep(DParser.stepCount(ID));
				((DrumModel.Note) noteback.get(noteback.size() - 1)).setUnpitched(unpitched);

				if (sync > 0) {
					noteback.add(new DrumModel.RestNote());
					((DrumModel.RestNote) noteback.get(noteback.size() - 1)).setDuration(rest.toString());
					((DrumModel.RestNote) noteback.get(noteback.size() - 1)).setVoice("2");
					((DrumModel.RestNote) noteback.get(noteback.size() - 1)).setType(DParser.typeDeclare(rest, meas.get(0).length()));
				}
			}
			sync = 0;
		}

		if (repeatedmeasures == 1) {
			guitarModel.ForwardBarline barForward = new guitarModel.ForwardBarline();
			barForward.setLocation("left");
			barForward.setBarStyle("heavy-light");
			guitarModel.GuitarRepeat repeatForward = new guitarModel.GuitarRepeat();
			repeatForward.setDirection("forward");
			barForward.setRepeat(repeatForward);
			newMeasure.setForwardBarline(barForward);
			guitarModel.GuitarDirection dir = new guitarModel.GuitarDirection();
			dir.setPlacement("above");
			guitarModel.GuitarDirectionType dirType = new guitarModel.GuitarDirectionType();
			guitarModel.GuitarWords gWord = new guitarModel.GuitarWords();
			gWord.setRelativeX();
			gWord.setRelativeY();
			gWord.setRepeatText(repeatedAmount);
			dirType.setWord(gWord);
			dir.setDirectionType(dirType);
			newMeasure.setDirection(dir);
			isThereRepeat = true;
		}

//		System.out.println(backwardRepeatExist);
		if (repeatedmeasures == 2) {
			guitarModel.BackwardBarline barBackward = new guitarModel.BackwardBarline();
			barBackward.setLocation("right");
			barBackward.setBarStyle("light-heavy");
			guitarModel.GuitarRepeat repeatBackward = new guitarModel.GuitarRepeat();
			repeatBackward.setDirection("backward");
			barBackward.setRepeat(repeatBackward);
			newMeasure.setBackwardBarline(barBackward);
			isThereRepeat = true;
		}
		if (repeatedmeasures == 4) {
			guitarModel.ForwardBarline barForward = new guitarModel.ForwardBarline();
			barForward.setLocation("left");
			barForward.setBarStyle("heavy-light");
			guitarModel.GuitarRepeat repeatForward = new guitarModel.GuitarRepeat();
			repeatForward.setDirection("forward");
			barForward.setRepeat(repeatForward);
			newMeasure.setForwardBarline(barForward);
			guitarModel.GuitarDirection dir = new guitarModel.GuitarDirection();
			dir.setPlacement("above");
			guitarModel.GuitarDirectionType dirType = new guitarModel.GuitarDirectionType();
			guitarModel.GuitarWords gWord = new guitarModel.GuitarWords();
			gWord.setRelativeX();
			gWord.setRelativeY();
			gWord.setRepeatText(repeatedAmount);
			dirType.setWord(gWord);
			dir.setDirectionType(dirType);
			newMeasure.setDirection(dir);
			guitarModel.BackwardBarline barBackward = new guitarModel.BackwardBarline();
			barBackward.setLocation("right");
			barBackward.setBarStyle("light-heavy");
			guitarModel.GuitarRepeat repeatBackward = new guitarModel.GuitarRepeat();
			repeatBackward.setDirection("backward");
			barBackward.setRepeat(repeatBackward);
			newMeasure.setBackwardBarline(barBackward);
			isThereRepeat = true;
		}

		newMeasure.setNote(note);
		if (bup.getDuration() != "0") {
			newMeasure.setBackup(bup);
		}
		newMeasure.setNoteBack(noteback);
		return newMeasure;
	}

	/**
	 * This method will figure out which instrument the tab is meant to be played
	 * for.
	 * 
	 * <p>
	 * In this method, we assume that a guitar has 6 lines of strings and a bass has
	 * four lines of string
	 * <p/>
	 * .
	 * 
	 * @param content is what the tab contains but stored in an array list
	 * @return the name of the instrument as a string
	 */
	public static String identifyInstrument(ArrayList<String> content) {

		boolean isdrum = true;
		outerloop: for (int i = 0; i < content.size(); i++) {
			if (content.get(i).contains("|") && content.get(i).contains("-") && !(content.get(i).contains("REPEAT"))) {
				for (int j = 0; j < content.get(i).length(); j++) {
					if (Character.isDigit(content.get(i).charAt(j))) {
						isdrum = false;
						// System.out.println(content.get(i));
						break outerloop;
					}
				}
			}
		}
		if (isdrum == true) {
			return "Drums";
		} else {
			if (helpMe(content) == 4)
				return "Bass";
			else if (helpMe(content) == 6)
				return "Guitar";
		}

		return "No Instrument Detected";
	}

	/**
	 * This method helps identify if the instrument is a bass or a guitar.
	 * 
	 * @param content
	 * @return
	 */
	public static int helpMe(ArrayList<String> content) {
		int count = 0;
		for (int i = 0; i < content.size(); i++) {
			if (!content.get(i).equals(""))
				count++;
			else
				break;
		}
		return count;
	}
}

//push to resolve conflict