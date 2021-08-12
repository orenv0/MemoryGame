import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
	private final int SHUFLLECOUNT = 60;
	private final int NUMBER_OF_AUDIOS = 6;
	private final int H = 250;
	private final int W = 200;
	private int seconds = 10;
	private int timeLevel = 10;
	private int numOfPairs = 0;
	private int maxMistakes = 0;
	private int mistakesDone = 0;
	private int openCards = 0;
	private int found = 0;
	private boolean firstGame = true;
	private HashSet<Integer> cardsRevealed = new HashSet<Integer>();
	private VBox vp = new VBox();
	private Stack<Card> cardStack = new Stack<Card>();
	private Stack<Integer> coordonates = new Stack<Integer>();
	private int rowFirst = 0;
	private int colFirst = 0;
	private Stage window1 = new Stage(), window2 = new Stage();;
	private GridPane gp = new GridPane(); // GridPane
	private BorderPane bp = new BorderPane();
	private Scene s1 = new Scene(bp), s2 = new Scene(vp);
	private Text title = new Text("Welcome to the most magical memory game ever!");
	private VBox lvbp = new VBox(), cvbp = new VBox();
	private HBox info = new HBox();
	private Button bPlay = new Button("Play");
	private StackPane tsp = new StackPane(), bsp = new StackPane(), csp = new StackPane();
	private ComboBox<String> mistakescb = new ComboBox<String>(), themecb = new ComboBox<String>(),
			cardscb = new ComboBox<String>(), timecb = new ComboBox<String>();
	private ObservableList<String> mistakesList, themeList, cardsList, timeList;
	private Timeline time = new Timeline();
	private String theme = "fruits";
	private Label timelb = new Label(), mistakeslb = new Label(), mistakeTitlelb = new Label(),
			timeTitlelb = new Label(), cardsTitlelb = new Label(), themeTitlelb = new Label();
	private MediaPlayer mpHurryup, mpStart, mpFinish;
	private MediaPlayer[] mp;
	private int ynDialog;
	private KeyFrame kf; 

	public void start(Stage primaryStage) {
		showSettingWindow();
	}

	public void showSettingWindow() {
		String[] themeOptions = {  "Fruits", "Animals" };
		String[] cardsOptions = { "8", "10", "12", "14", "16", "18", "20", "22", "24" };
		String[] mistakesOptions = { "Easy", "Medium", "Hard", "Unlimited" };
		String[] timeOptions = { "Easy", "Medium", "Hard", "Extreme", "Unlimited" };
		cardsList = FXCollections.observableArrayList(cardsOptions);
		cardscb.setItems(cardsList);
		themeList = FXCollections.observableArrayList(themeOptions);
		themecb.setItems(themeList);
		mistakesList = FXCollections.observableArrayList(mistakesOptions);
		mistakescb.setItems(mistakesList);
		timeList = FXCollections.observableArrayList(timeOptions);
		timecb.setItems(timeList);
		bsp.getChildren().add(bPlay);
		tsp.getChildren().add(title);
		cvbp.getChildren().addAll(cardscb, themecb, mistakescb, timecb);
		csp.getChildren().add(cvbp);
		bp.setLeft(lvbp);
		bp.setTop(tsp);
		bp.setBottom(bsp);
		bp.setCenter(csp);
		bp.setPadding(new Insets(10, 10, 10, 10));
		lvbp.setPadding(new Insets(20));
		lvbp.setSpacing(20);
		cvbp.setPadding(new Insets(20));
		cvbp.setSpacing(10);
		title.setFont(new Font(20));
		cardscb.setValue("8");
		mistakescb.setValue("Easy");
		themecb.setValue("Fruits");
		timecb.setValue("Easy");
		cardsTitlelb.setText("Number of cards:");
		themeTitlelb.setText("Choose a theme:");
		mistakeTitlelb.setText("Mistakes Limitation Difficulty:");
		timeTitlelb.setText("Time Limitation Difficulty:");
		lvbp.getChildren().addAll(cardsTitlelb, themeTitlelb, mistakeTitlelb, timeTitlelb);
		kf = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				seconds--;
				timelb.setText("    Time left: " + seconds);
				if (seconds == 10) {
					URL UrlHurryup = getClass().getResource(theme + "/audio/hurryup" + ".mp3");
					mpHurryup = new MediaPlayer(new Media(UrlHurryup.toString()));
					mpHurryup.stop();
					mpHurryup.play();
				}

				if (seconds <= 0) {
					gp.setDisable(true);
					System.out.println("Time is over!");
					time.stop();
				}

			}

		}); 
		time.setCycleCount(Timeline.INDEFINITE);
		time.getKeyFrames().add(kf);
		window1.setScene(s1);
		window1.show();
		bPlay.setOnAction(e -> {
			startGame();
		});
	}

	public void startGame() {

		numOfPairs = Integer.parseInt(cardscb.getValue()) / 2;
		switch (mistakescb.getValue()) {
		case "Easy":
			maxMistakes = numOfPairs * 2;
			break;
		case "Medium":
			maxMistakes = numOfPairs;

			break;
		case "Hard":
			maxMistakes = numOfPairs / 2;

			break;
		case "Unlimited":
			maxMistakes = 0;
			break;

		}
		switch (timecb.getValue()) {
		case "Easy":
			timeLevel = 10;
			countTime();
			break;
		case "Medium":
			timeLevel = 9;
			countTime();
			break;
		case "Hard":
			timeLevel = 8;
			countTime();
			break;
		case "Extreme":
			timeLevel = 7;
			countTime();
			break;
		case "Unlimited":
			break;

		}
		theme = themecb.getValue();
		int tableSize = (int) Math.sqrt(2 * numOfPairs) + 2;
		URL UrlStart = getClass().getResource(theme + "/audio/start" + ".mp3");
		mpStart = new MediaPlayer(new Media(UrlStart.toString()));
		Card[] cards = initCards();
		Card[][] cardsMat = putInMatrix(cards, tableSize);// Data Matrix - exact presentation of the board
		mp = initAudios();
		shuffle(cardsMat);
		presentInitBoard(tableSize);
		mpStart.play();
		if (maxMistakes != 0)
			mistakeslb.setText(" Mistakes: " + mistakesDone + "/" + maxMistakes);
		else
			mistakeslb.setText(" Mistakes: " + mistakesDone + "/Unlimited");
		mistakeslb.setFont(new Font("Ink Free", 30));
		timelb.setFont(new Font("Ink Free", 30));
		timelb.setText("    Time left: " + seconds);
		if (firstGame == true) {
			info.getChildren().addAll(mistakeslb, timelb);
			vp.getChildren().addAll(gp, info);
		}
		firstGame = false;
		window2.setScene(s2);
		window1.close();
		window2.show();

		gp.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					for (Node node : gp.getChildren()) {
						if (node instanceof ImageView) {
							if (node.getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
								if (openCards == 1) {
									if (rowFirst == GridPane.getRowIndex(node)
											&& colFirst == GridPane.getColumnIndex(node)) {
										return;
									}
									revealCard(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), cardsMat);
									return;

								}
								rowFirst = GridPane.getRowIndex(node);
								colFirst = GridPane.getColumnIndex(node);
								revealCard(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), cardsMat);
							}
						}
					}
				} catch (Exception ex) {

				}
			}
		});

	}

	public Card[] initCards() {// Creating an array of cards
		Card.setBack(new Image(theme + "/images/global.jpg"));
		Card[] cards = new Card[2 * numOfPairs];
		for (int i = 0; i < 2 * numOfPairs; i++) {
			cards[i] = new Card(new Image(theme + "/images/i" + i / 2 + ".jpg"), i, i / 2);
		}

		return cards;
	}

	public MediaPlayer[] initAudios() {// Load audio
		MediaPlayer[] mp = new MediaPlayer[NUMBER_OF_AUDIOS];
		for (int i = 0; i < NUMBER_OF_AUDIOS; i++) {
			URL anthemUrl = getClass().getResource(theme + "/audio/a" + i + ".mp3");
			mp[i] = new MediaPlayer(new Media(anthemUrl.toString()));
		}
		return mp;
	}

	public Card[][] putInMatrix(Card[] cards, int tableSize) {// creating a Data Matrix of cards

		Card[][] matOfCards = new Card[tableSize][tableSize];
		int index = 0;
		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				if (index < 2 * numOfPairs) {
					matOfCards[i][j] = cards[index];
					index++;
				}
			}
		}

		return matOfCards;
	}

	public void presentInitBoard(int tableSize) {// Building the initial cards board
		ImageView[] ivArr = new ImageView[2 * numOfPairs];
		for (int ind = 0; ind < 2 * numOfPairs; ind++) {
			ivArr[ind] = new ImageView(Card.getBack());
			ivArr[ind].setFitWidth(W);
			ivArr[ind].setFitHeight(H);
		}
		int index = 0;
		for (int i = 0; i < tableSize; i++) {
			for (int j = 0; j < tableSize; j++) {
				if (index < 2 * numOfPairs) {
					gp.add(ivArr[index], j, i);
					index++;
				}
			}
		}
	}

	public void shuffle(Card[][] cardsMat) {// shuffling the cards in matrix
		Random rand = new Random();
		for (int i = 0; i < SHUFLLECOUNT; i++) {
			int row1 = rand.nextInt(cardsMat.length);
			int col1 = rand.nextInt(cardsMat.length);
			int row2 = rand.nextInt(cardsMat.length);
			int col2 = rand.nextInt(cardsMat.length);
			if (cardsMat[row1][col1] != null && cardsMat[row2][col2] != null) {
				Card temp = cardsMat[row1][col1];
				cardsMat[row1][col1] = cardsMat[row2][col2];
				cardsMat[row2][col2] = temp;
			}
		}
	}

	public void revealCard(int row, int cul, Card[][] cardsMat) {
		if (cardsRevealed.contains(cardsMat[row][cul].getPairId()))
			return;
		ImageView iv = new ImageView(cardsMat[row][cul].getFront());
		FadeTransition ft = new FadeTransition(Duration.millis(500), iv);
		iv.setFitHeight(H);
		iv.setFitWidth(W);
		gp.add(iv, cul, row);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.play();
		cardStack.push(cardsMat[row][cul]);
		coordonates.push(row);
		coordonates.push(cul);
		openCards++;
		if (openCards == 2) {
			gp.setDisable(true);

			checkCards();
		}

	}

	public void checkCards() {
		Timer t1 = new Timer();

		Random r = new Random();
		int rSound = r.nextInt(3);
		openCards = 0;
		Card card2 = cardStack.pop();
		Card card1 = cardStack.pop();
		int col2 = coordonates.pop();
		int row2 = coordonates.pop();
		int col1 = coordonates.pop();
		int row1 = coordonates.pop();
		if (card1.getPairId() == card2.getPairId() && card1.getCardId() != card2.getCardId()) {
			mp[rSound].stop();
			mp[rSound].play();
			found = found + 2;
			cardsRevealed.add(card2.getPairId());
			gp.setDisable(false);
			if (found == 2 * numOfPairs) {// The player finished the game finished successfully
				Timer t2 = new Timer();
				gp.setDisable(true);
				time.stop();
				t2.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						URL UrlFinish = getClass().getResource(theme + "/audio/finish" + ".mp3");
						mpFinish = new MediaPlayer(new Media(UrlFinish.toString()));
						mpFinish.stop();
						mpFinish.play();
						ynDialog = JOptionPane.showConfirmDialog(null,
								"Well Done, you finished the game successfully! \nPress OK to exit",
								"Game Over", JOptionPane.DEFAULT_OPTION);
						if (ynDialog == JOptionPane.OK_OPTION) {		
							System.exit(1);
						}
					}

				}, 1000);
			}
		} else {
			mistakesDone++;
			if (maxMistakes != 0)
				mistakeslb.setText(" Mistakes: " + mistakesDone + "/" + maxMistakes);
			else
				mistakeslb.setText(" Mistakes: " + mistakesDone + "/Unlimited");
			mp[NUMBER_OF_AUDIOS - 1 - rSound].stop();
			mp[NUMBER_OF_AUDIOS - 1 - rSound].play();
			t1.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ImageView iv1 = new ImageView(Card.getBack());
					ImageView iv2 = new ImageView(Card.getBack());
					iv1.setFitHeight(H);
					iv1.setFitWidth(W);
					iv2.setFitHeight(H);
					iv2.setFitWidth(W);
					FadeTransition ft1 = new FadeTransition(Duration.millis(800), iv1);
					FadeTransition ft2 = new FadeTransition(Duration.millis(800), iv2);
					ft1.setFromValue(0);
					ft1.setToValue(1.0);
					ft2.setFromValue(0);
					ft2.setToValue(1.0);
					ft1.play();
					ft2.play();
					if (seconds != 0)
						gp.setDisable(false);
					if (mistakesDone == maxMistakes && maxMistakes != 0) {
						gp.setDisable(true);
						time.stop();
						System.out.println("Game Over!");
						System.exit(0);
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							gp.add(iv1, col1, row1);
							gp.add(iv2, col2, row2);

						}

					});
					t1.cancel();
				}

			}, 1500);

		}

	}

	public void countTime() {
		seconds = numOfPairs * timeLevel;


		if (time != null) {
			time.stop();
		}
		time.play();

	}

	public void playAgain() {
		mistakesDone = 0;
		found = 0;
		cardStack.clear();
    	cardsRevealed.clear();
		coordonates.clear();
		rowFirst = 0;
    	colFirst = 0;


		gp.setDisable(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				window2.close();
				window1.show();
			}
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
