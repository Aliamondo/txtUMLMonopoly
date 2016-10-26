package monopoly.x;

import java.util.ArrayList;
import java.util.Collections;

public class Cards implements CardsInterface {
	private int numberOfCards;
	private ArrayList<Integer> cardsQueue;
	
	public Cards(int numberOfCards) {
		this.numberOfCards = numberOfCards;
		cardsQueue = new ArrayList<>();
	}
	
	@Override
	public void addCard(int card) {
		cardsQueue.add(card);
	}
	
	@Override
	public void fillMissing() {
		for (int i = cardsQueue.size(); i <= numberOfCards; i++) {
			cardsQueue.add(-1);
		}
	}

	@Override
	public int nextCard() {
		int card = cardsQueue.get(0);
		cardsQueue.remove(0);
		cardsQueue.add(card);
		return card;
	}

	@Override
	public void shuffleCards() {
		Collections.shuffle(cardsQueue);
	}
}
