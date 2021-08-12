import javafx.scene.image.Image;

public class Card {
	private int cardId;
	private int pairId;
	private Image front;
	private static Image back;

	public Card(Image image, int cardId, int pairId) {
		setFront(image);
		setCardId(cardId);
		setPairId(pairId);
	}

	public Image getFront() {
		return front;
	}

	public void setFront(Image image) {
		this.front = image;
	}

	public static Image getBack() {
		return back;
	}

	public static void setBack(Image global) {
		Card.back = global;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getPairId() {
		return pairId;
	}

	public void setPairId(int pairId) {
		this.pairId = pairId;
	}

}
