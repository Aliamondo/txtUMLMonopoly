package monopoly.x;

import java.util.Random;

public class RandomNum implements RandomInterface {

	Random random = new Random();
	
	@Override
	public int nextInt(int lowerBound, int upperBound) {
		return random.nextInt(upperBound - lowerBound) + lowerBound;
	}
}
