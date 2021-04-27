package Interface;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import TAB_TO_XML.App;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class ErrorHighlightingInput {

	protected static ExecutorService executor = Executors.newSingleThreadExecutor();
	private CodeArea codeArea;
	public static CodeArea getArea;
	private TextArea textarea;
	public static TextArea getTextArea;
	private static TreeMap<Range, Integer> errorsOnTab = new TreeMap<>();
	static String message;

	public ErrorHighlightingInput(CodeArea textArea, TextArea textarea){
		this.codeArea = textArea;
		getArea = this.codeArea;
		this.textarea = textarea;
	}

	public Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
		String text = codeArea.getText();

		Task<StyleSpans<Collection<String>>> task = new Task<>() {
			@Override
			protected StyleSpans<Collection<String>> call() {
				return computeHighlighting(text);
			}
		};
		executor.execute(task);
		task.isDone();
		return task;
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		String instrument = App.identifyInstrument(App.getFileList(text));
		if (instrument.equals("Guitar")) 
				Controller.validateGuitarTab(text);
		else if (instrument.equals("Bass")) 
				Controller.validateBassTab(text);
		else;
		errorsOnTab = fillTreeMap(errorsOnTab, text);
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		ArrayList<Range> errorRanges = new ArrayList<>(errorsOnTab.keySet());
		int errorEnd = 0;
		for (int i = 0; i < errorRanges.size(); i++) {
			Range range = errorRanges.get(i);
			int priority = errorsOnTab.get(range);
			 String styleClass = getErrorStyleClass(priority);
	         spansBuilder.add(Collections.emptyList(), range.getStart() - errorEnd);
	        // spansBuilder.add(Collections.singleton(styleClass), range.getSize());
	         errorEnd = range.getEnd();
		}
		try {
			spansBuilder.add(Collections.emptyList(), text.length() - errorEnd);
		}
		catch(Exception e) {}
		return spansBuilder.create();
	}


	private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	}

	public void enableHighlighting() {
		Subscription cleanupWhenDone = this.codeArea.multiPlainChanges()
				.successionEnds(Duration.ofMillis(350))
				.supplyTask(this::computeHighlightingAsync)
				.awaitLatest(this.codeArea.multiPlainChanges())
				.filterMap(t -> {
					if(t.isSuccess()) {
						return Optional.of(t.get());
					} else {
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				})
				.subscribe(this::applyHighlighting);
	}

	private static String getErrorStyleClass(int priority) {
		switch (priority) {
		case 1: return "highPriorityError";
		case 2: return "mediumPriorityError";
		case 3: return "mediumPriorityError";
		case 4: return "highPriorityError";
		case 5: return "highPriorityError";
		default: return "";
		}
	}
	
	private static TreeMap<Range, Integer> fillTreeMap(TreeMap<Range, Integer> treeMap, String text) {
		for (int i = 0; i < Controller.getErrors.size(); i++) {
			int priority = Controller.getErrors.get(i);
			int line = Controller.storeLine.get(i);
			int character = Controller.storeCharacter.get(i);
			int lineIndex = getLineIndex(line, text, priority);
			Range range = new Range(lineIndex + character, lineIndex + character + 1);
			treeMap.put(range, priority);
		}
		return treeMap;
	}

	private static int getLineIndex(int line, String text, int priority) {
		int lastIndex = 0;
		String[] lines = text.split("\n");
		for (int i = 0; i < text.length(); i++) {
			if (i == line) 
				return lastIndex;
			lastIndex += lines[i].length();
		}
		return lastIndex;
	}

	public static String getMessage(int characterIndex, int priority) {
		 
		if (priority == 1) return "The length of the line is not the same for the rest of the measure";
		else if (priority == 2) return "The tuning should be between A and G";
		else if (priority == 3) return "The system does not recognize this symbol";
		else if (priority == 4) return "Declaration of symbols like p, h, s, or g are improper";
		else return "Harmonic declaration is improper";
		
	}
}
