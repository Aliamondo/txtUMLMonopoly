package monopoly.x;

import hu.elte.txtuml.api.model.external.ExternalClass;

public interface GUIInterface extends ExternalClass {
	public void view();
	public void showPlayer(int pos);
	public void showOriginalPlayer(int pos);
	public void showDice(int x, int y);
}
