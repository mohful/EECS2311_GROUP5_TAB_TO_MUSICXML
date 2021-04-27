package Interface;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import org.fxmisc.richtext.CodeArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import TAB_TO_XML.App;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


@ExtendWith(ApplicationExtension.class)
public class RunnerTest {
	
	@Start
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Interface/Intro.fxml"));
		primaryStage.setTitle("Allegro Tab Converter");
		Scene scene = new Scene(root, 935, 700);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("Interface/Intro.css").toExternalForm());
		Image logo = new Image("/Interface/music-background.jpg");
		primaryStage.getIcons().add(logo);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Test
	void browseButtonTest(FxRobot robot) {
		robot.clickOn("#browse");
		FxAssert.verifyThat("#browse", LabeledMatchers.hasText("Browse..."));
	}
	
	@Test
	void saveButtonTest(FxRobot robot) {
		CodeArea codeArea = Interface.ErrorHighlightingInput.getArea;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String newText = "CC|x----x-----x--x-|--------x-------|\r\n"
						+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
						+ "SD|----o-------o---|oooo------------|\r\n"
						+ "HT|----------------|----oo----------|\r\n"
						+ "MT|----------------|------oo--------|\r\n"
						+ "BD|o-------o-------|o-------o-------|";
				codeArea.replaceText(new IndexRange(0, codeArea.getText().length()), newText);
			}
		});
		robot.clickOn("#convert");
		robot.clickOn("#edits");
		FxAssert.verifyThat("#edits", LabeledMatchers.hasText("Save..."));
		
	}
	
//	@Test
//	void exportAndConvertForDrumsTest(FxRobot robot) {
//		CodeArea codeArea = Interface.ErrorHighlightingInput.getArea;
//		Platform.runLater(new Runnable() {
//
//			@Override
//			public void run() {
//				String newText = "CC|x---------------|--------x-------|\r\n"
//						+ "HH|--x-x-x-x-x-x-x-|----------------|\r\n"
//						+ "SD|----o-------o---|oooo------------|\r\n"
//						+ "HT|----------------|----oo----------|\r\n"
//						+ "MT|----------------|------oo--------|\r\n"
//						+ "BD|o-------o-------|o-------o-------|";
//				codeArea.replaceText(new IndexRange(0, codeArea.getText().length()), newText);
//			}
//			
//		});
//		robot.clickOn("#convert");
//		FxAssert.verifyThat("#convert", LabeledMatchers.hasText("Convert"));
//		robot.clickOn("#save");
//		FxAssert.verifyThat("#save", LabeledMatchers.hasText("Export"));
//	}
	
//	@Test
//	void exportAndConvertForGuitarTest(FxRobot robot) {
//		CodeArea codeArea = Interface.ErrorHighlightingInput.getArea;
//		Platform.runLater(new Runnable() {
//
//			@Override
//			public void run() {
//				String newText = "E|---------------------------|-15p12-10p9-12p10-6p5-8p6-----|\r\n"
//						+ "B|---------------------------|--------------------------8-5-|\r\n"
//						+ "G|---------------------------|------------------------------|\r\n"
//						+ "D|--[7]----------------------|------------------------------|\r\n"
//						+ "A|--[7]----------------------|------------------------------|\r\n"
//						+ "D|--[7]----------------------|------------------------------|";
//				codeArea.replaceText(new IndexRange(0, codeArea.getText().length()), newText);
//			}
//			
//		});
//		robot.clickOn("#convert");
//		FxAssert.verifyThat("#convert", LabeledMatchers.hasText("Convert"));
//		robot.clickOn("#save");
//		FxAssert.verifyThat("#save", LabeledMatchers.hasText("Export"));
//	}
	
	@Test
	void exportAndConvertForBassGuitarTest(FxRobot robot) {
		CodeArea codeArea = Interface.ErrorHighlightingInput.getArea;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String newText = "|---------------------------|-15p12-10p9-12p10-6p5-8p6-----|\r\n"
						+ "|---------------------------|--------------------------8-5-|\r\n"
						+ "|---------------------------|------------------------------|\r\n"
						+ "|--[7]----------------------|------------------------------|\r\n";
				codeArea.replaceText(new IndexRange(0, codeArea.getText().length()), newText);
			}
			
		});
		robot.clickOn("#convert");
		FxAssert.verifyThat("#convert", LabeledMatchers.hasText("Convert"));
		FxAssert.verifyThat("#getInstrument", LabeledMatchers.hasText("Instrument: " + App.getInstrument(codeArea.getText())));
		robot.clickOn("#save");
		FxAssert.verifyThat("#save", LabeledMatchers.hasText("Export"));
	}
	
}
