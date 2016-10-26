package monopoly.x;

import hu.elte.txtuml.api.model.external.ExternalClass;

public interface CardsInterface extends ExternalClass {
	public void addCard(int e);
	public void fillMissing();
	public void shuffleCards();
	public int nextCard();
}
