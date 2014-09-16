package eulerUtils;

import java.util.LinkedList;
import java.util.List;

public class PrimeSieve {

	public static List<Integer> genPrimes(int largestSieve, int largestPrime) {
		boolean[] isComposite = new boolean[largestPrime + 1];

		for (int i = 2; i <= largestSieve; i++) {
			if (!isComposite[i])
				for (int j = i * 2; j <= largestPrime; j += i)
					isComposite[j] = true;
		}
		LinkedList<Integer> primes = new LinkedList<Integer>();
		for (int i = 2; i <= largestPrime; i++) {
			if (!isComposite[i])
				primes.add(i);
		}
		return primes;
	}

}
