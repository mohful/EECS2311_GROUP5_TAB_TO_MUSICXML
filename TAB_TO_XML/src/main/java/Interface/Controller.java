package Interface;

import java.awt.Desktop;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

import javax.xml.transform.stream.StreamResult;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import TAB_TO_XML.App;

public class Controller implements Initializable {

	private static Window window = new Stage();
	Desktop screen = Desktop.getDesktop();
	File tablature;
	File xmlFile;
	File textFile;
	BufferedReader input;
	StreamResult output;
	FileChooser fc, saveFile, saveEdits;
	static String obtainText;
	static String message = "";
	static ArrayList<Integer> getErrors = new ArrayList<>();
	static ArrayList<Integer> storeLine = new ArrayList<>();
	static ArrayList<Integer> storeCharacter = new ArrayList<>();
	static boolean pressed = false;

	@FXML
	Button browse, convert, save, edits;

	@FXML
	Label path, getInstrument;

	@FXML
	TextArea view, customization;

	@FXML
	private CodeArea write, anotherCodeArea;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		save.setDisable(true);
		edits.setDisable(true);
		write.setStyle("-fx-font-family: Monospace; -fx-font-size: 12pt; -fx-font-weight: bold;");
		write.setParagraphGraphicFactory(LineNumberFactory.get(write));
		new ErrorHighlightingInput(write, view).enableHighlighting();
	}



	/**
	 * This method enables browsing through the file explorer for .txt files. After browsing, it shows the text on the file in a 
	 * text area. 
	 * @param event
	 */

	@FXML
	public void handleButtonBrowse(ActionEvent event) {
		pressed = false;
		save.setDisable(true);
		edits.setDisable(true);
		write.clear();
		getInstrument.setText("");
		fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		browse.setDisable(true);
		tablature = fc.showOpenDialog(null);
		if (tablature != null) {
			Scanner sc = null;
			try {
				sc = new Scanner(tablature);
				while (sc.hasNextLine()) {
					write.appendText(sc.nextLine() + "\n"); // else read the next token
				}				
				String instrument = App.identifyInstrument(App.getFileList(write.getText()));
				if (instrument.equals("Guitar")) getInstrument.setText("Instrument: Guitar");
				else if (instrument.equals("Drums")) getInstrument.setText("Instrument: Drums");
				else if (instrument.equals("Bass")) getInstrument.setText("Instrument: Bass");
				else getInstrument.setText("No Instrument Found");
				browse.setDisable(false);
				pressed = true;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else getInstrument.setText("");
		browse.setDisable(false);
		edits.setDisable(false);
	}


	/**
	 * This method handles the convert button in the Graphic User Interface. It shows an alert once the file is converted.
	 * @param event
	 */

	@FXML
	public void handleButtonConvert(ActionEvent event) {
		view.clear();
		obtainText = write.getText();
		try {
			if (write.getText() != "") {
				String storeText = write.getText();
				String instrument = App.identifyInstrument(App.getFileList(storeText));
				if (instrument.equals("Guitar")) getInstrument.setText("Instrument: Guitar");
				else if (instrument.equals("Drums")) getInstrument.setText("Instrument: Drums");
				else if (instrument.equals("Bass")) getInstrument.setText("Instrument: Bass");
				else getInstrument.setText("No Instrument Found");
				String customizationText = customization.getText();
				String getConversion = App.runConversion(storeText, customizationText);
				view.appendText(getConversion);
				save.setDisable(false);
			}
			else {
				getInstrument.setText("");
				Alert errorAlert = new Alert(AlertType.ERROR); 
				errorAlert.setHeaderText("Input not valid!"); 
				errorAlert.setContentText("Provide text file or paste tablature on the textbox to your left."); 
				errorAlert.showAndWait();
				errorAlert.close();
			}
		}
		catch (Exception e) {
		}
		edits.setDisable(false);
	}

	/**
	 * This is the method that controls the save button in the User Interface.
	 * @param event
	 */
	@FXML
	public void handleButtonSave(ActionEvent event) {
		saveFile = new FileChooser();
		saveFile.getExtensionFilters().add(new ExtensionFilter("musicXML file", "*.musicxml"));
		saveFile.getExtensionFilters().add(new ExtensionFilter("MXL file", "*.mxl"));
		xmlFile = saveFile.showSaveDialog(null);
		PrintWriter write = null;
		try {
			if (xmlFile.getAbsolutePath() != null) {
				write = new PrintWriter(xmlFile.getAbsolutePath());
				write.println(view.getText());
				Alert saveAlert = new Alert(AlertType.CONFIRMATION); //creates a displayable error allert window
				saveAlert.setHeaderText("The converted file has been saved to " + xmlFile.getAbsolutePath()); 
				saveAlert.setContentText("Thank you for using Allegro Tab Converter!"); //Shows this stage and waits for it to be hidden (closed) before returning to the caller.
				saveAlert.showAndWait();
				write.close();
			}
			else {
				Alert errorAlert = new Alert(AlertType.ERROR); 
				errorAlert.setHeaderText("File cannot be saved!"); 
				errorAlert.setContentText("Please convert a file in order to save it!"); 
				errorAlert.showAndWait();
			}
		}
		catch(Exception e) {}
	}

	@FXML
	public void handleButtonEdits(ActionEvent event) {
		write.setEditable(false);
		if (!pressed) {
			PrintWriter write = null;
			try {
				if (textFile.getAbsolutePath() != null) {
					write = new PrintWriter(textFile.getAbsolutePath());
					write.println(this.write.getText());
					write.close();
					this.write.setEditable(true);
					return;
				}
			}
			catch(Exception e) {}
			saveEdits = new FileChooser();
			saveEdits.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
			textFile = saveEdits.showSaveDialog(null);
			try {
				if (textFile.getAbsolutePath() != null) {
					write = new PrintWriter(textFile.getAbsolutePath());
					write.println(this.write.getText());
					write.close();
				}
				else {
					Alert errorAlert = new Alert(AlertType.ERROR); 
					errorAlert.setHeaderText("File cannot be saved!"); 
					errorAlert.setContentText("An error occured. Please try again"); 
					errorAlert.showAndWait();
				}
			}
			catch(Exception e) {}
		}
		else {
			PrintWriter overwrite = null;
			try {
				overwrite = new PrintWriter(tablature.getAbsolutePath());
				overwrite.println(this.write.getText());
				overwrite.close();
			}
			catch(Exception e) {}
		}
		write.setEditable(true);
	}
	/**
	 * This method just opens up a popup (which is a different scene). The idea for this was to open up 
	 * a pop-up so we can add time signatures and whatever
	 * @param fxml which is the scene fxml file
	 * @param name title of the scene
	 * @return
	 */
	public Window openNewWindow(String fxml, String name) {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));

			Stage stage = new Stage();
			stage.setTitle(name);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(Runner.stage);
			stage.setResizable(false);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			return scene.getWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method validates the entire guitar tablature and adds certain numbers to the arraylist.
	 * It also stores the line number at which the error is occuring.
	 * 1 - length is not right (major)
	 * 2 - incorrect tunings (anything above a G) (medium)
	 * 3 - symbols are not right (p, h, s, g, etc) (medium)
	 * 4 - Harmonics are not right (no ending bracket, starting bracket, etc) (high)
	 * 5 - if symbols are there, but its not right (for example, they put p5 instead of 2p5). (high)
	 * @param guitarTab
	 * @return
	 */
	public static boolean validateGuitarTab(String guitarTab) {
		boolean invalid = false;
		getErrors.clear();
		storeLine.clear();
		storeCharacter.clear();
		Scanner guitar = new Scanner(guitarTab);
		ArrayList<String> errorGuitar = new ArrayList<>();
		while (guitar.hasNextLine()) 
			errorGuitar.add(guitar.nextLine());
		try {
			if (!lengthOK(errorGuitar) && !guitarTuningsOK(errorGuitar) && !harmonicsOK(errorGuitar) && !symbolsOK(errorGuitar))
				invalid = true;
			guitar.close();
		}
		catch(Exception e) {

		}
		return invalid;
	}

	public static boolean validateBassTab(String bassTab) {
		boolean invalid = false;
		Scanner bass = new Scanner(bassTab);
		ArrayList<String> errorBass = new ArrayList<>();
		while (bass.hasNextLine()) 
			errorBass.add(bass.next());
		try {
			if (!lengthOK(errorBass) && !harmonicsOK(errorBass) && !symbolsOK(errorBass)) {
				invalid = true;
			}
			bass.close();
		}
		catch (Exception e) {}
		return invalid;
	}

	public static boolean lengthOK(ArrayList<String> errorGuitar) {
		boolean ok = true;
		for (int i = 0; i < errorGuitar.size() - 1; i++) {
			//if block to check if the lines are right
			if (errorGuitar.get(i).length() != errorGuitar.get(i + 1).length()) {
				getErrors.add(1);
				storeLine.add(i);
				storeCharacter.add(0);
				ok = false;
				message += "Length of tablature is incorrect at " + i + "\n";
			}
		}
		return ok;
	}

	public static boolean guitarTuningsOK(ArrayList<String> errorGuitar) {
		boolean ok = true;
		for (int i = 0; i < errorGuitar.size(); i++) {
			//if block to check if the tunings are right
			if ((errorGuitar.get(i).charAt(0) >= 65 && errorGuitar.get(i).charAt(0) <= 71) || (errorGuitar.get(i).charAt(0) >= 97 && errorGuitar.get(i).charAt(0) <= 103)) {
				getErrors.add(2);
				storeLine.add(i);
				storeCharacter.add(0);
				ok = false;
				message += "Tuning of tablature is incorrect at " + i + "\n";
			}
		}
		return ok;
	}

	public static boolean harmonicsOK(ArrayList<String> errorGuitar) {
		boolean ok = true;
		for (int i = 0; i < errorGuitar.size(); i++) {
			for (int j = 0; j < errorGuitar.get(i).length() - 1; j++) {
				//harmonics check (brackets are there, or is there a digit inside the harmonics and stuff
				if (errorGuitar.get(i).charAt(j) == '[' && errorGuitar.get(i).contains("]")) {
					if (!Character.isDigit(errorGuitar.get(i).charAt(j + 1))) {
						getErrors.add(4);
						storeLine.add(i);
						storeCharacter.add(j + 1);
						ok = false;
						message += "Harmonics is incorrect at " + i + " line at" + j+1 + " character" + "\n";
					}
				}
			}
		}
		return ok;
	}

	public static boolean symbolsOK(ArrayList<String> errorGuitar) {
		boolean ok = true;
		for (int i = 0; i < errorGuitar.size(); i++) {
			for (int j = 0; j < errorGuitar.get(0).length() - 1; j++) {
				//if block to check if the tab includes any random characters it cannot parse
				if (errorGuitar.get(i).charAt(j) == 'x' || errorGuitar.get(i).charAt(j) == 'o' ||
						errorGuitar.get(i).charAt(j) == 'v' || errorGuitar.get(i).charAt(j) == 'b') {
					getErrors.add(3);
					storeLine.add(i);
					storeCharacter.add(j);
					ok = false;
					message += "Unsupported symbols used at " + i + " line at " + j+1 + " character " + "\n";
				}
				//if block to check for digits behind and in front of the pull offs and hammer-ons
				if (errorGuitar.get(i).charAt(j) == 'p' || errorGuitar.get(i).charAt(j) == 'P' || 
						errorGuitar.get(i).charAt(j) == 'h' || errorGuitar.get(i).charAt(j) == 'H') {
					if (!Character.isDigit(errorGuitar.get(i).charAt(j - 1)) && 
							!Character.isDigit(errorGuitar.get(i).charAt(j + 1))) {
						getErrors.add(5);
						storeLine.add(i);
						storeCharacter.add(j + 1);
						ok = false;
						message += "Supported symbols are incorrect implemented at " + i + " line at " + j+1 + " character " + "\n";
					}
				}
				//if block to check for grace notes and slides
				if (errorGuitar.get(i).charAt(j) == 'g' || errorGuitar.get(i).charAt(j) == 's') {
					if (!Character.isDigit(errorGuitar.get(i).charAt(j + 1))) {
						getErrors.add(5);
						storeLine.add(i);
						storeCharacter.add(j + 1);
						ok = false;
					}
				}
			}
		}
		return ok;
	}

	public void drumSymbolsOK(ArrayList<String> errorDrums) {
		for (int i = 0; i < errorDrums.size(); i++) {

		}
	}

	public void textAreaPopups(ArrayList<Integer> errors) {
		write.setParagraphGraphicFactory(LineNumberFactory.get(write));
		new ErrorHighlightingInput(write, view).enableHighlighting();
		anotherCodeArea = write;
		Popup popup = new Popup();
		Label message = new Label();
		message.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 5pt;");
		popup.getContent().add(message);
		write.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			String messagePop = ErrorHighlightingInput.message;
			if (messagePop.isEmpty()) 
				return;
			Point2D point = e.getScreenPosition();
			popup.show(popup, point.getX(), point.getY() + 10);

		});
		write.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e ->{
			popup.hide();
		});
	}

}