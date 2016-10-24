package monopoly.x;

import hu.elte.txtuml.api.model.external.ExternalClass;

public interface RandomInterface extends ExternalClass {
	public int nextInt(int lowerBound, int upperBound);
}