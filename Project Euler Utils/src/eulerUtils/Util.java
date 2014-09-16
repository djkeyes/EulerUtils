package eulerUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Util {

	public static boolean sameDigits(int a, int b) {
		int[] digitsA = new int[10];
		int[] digitsB = new int[10];
		while (a > 0) {
			digitsA[a % 10]++;
			a /= 10;
		}
		while (b > 0) {
			digitsB[b % 10]++;
			b /= 10;
		}

		return Arrays.equals(digitsA, digitsB);
	}
	
	// length in base 10 for positive x
	public static int length(int x) {
		int le = 1;
		while (x > 0) {
			x /= 10;
			le++;
		}
		return le;
	}

	public static int gcd(int a, int b) {
		if (b == 0) {
			return a;
		}

		return gcd(b, a % b);
	}


	// returns a map from primes to exponents
	// ie 12 returns
	// 2 => 2
	// 3 => 1
	// only works for positive inputs
	public static Map<Integer, Integer> primeFactors(int x) {
		if(primeFactorsCache == null) {
			primeFactorsCache = new HashMap<Integer, Map<Integer, Integer>>();
			System.out.println("Prefilling prime factors cache...");
			// fill cache
			for(int i=1; i < 1000000; i++){
				primeFactorsCache.put(i, primeFactors(i));
			}
			System.out.println("Prime factors cache filled!");
		}
		
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		// terminate early if x is prime. otherwise we're just re-proving something we already know, more slowly.
		if(Primes.primeSet().contains(x)){
			result.put(x, 1);
			return result;
		}
		
		// iterate through primes until we've destroyed x
		for (Integer p : Primes.primeList()) {
			if (x == 1) {
				break;
			}
			
			// optimization: we cache prime factorization of some small numbers
			if(x < 1000000){
				if(primeFactorsCache.containsKey(x)){
					Map<Integer, Integer> cached = primeFactorsCache.get(x);
					for(Integer cachedP : cached.keySet()){
						if(result.containsKey(cachedP)) {
							result.put(cachedP, result.get(cachedP) + cached.get(cachedP));
						} else {
							result.put(cachedP, cached.get(cachedP));
						}
					}
					return result;
				}
			}

			if (x % p == 0) {
				// TODO: do exponential division
				int count = 0;
				while (x % p == 0) {
					x /= p;
					count++;
				}
				result.put(p, count);
			}
		}
		return result;
	}
	private static Map<Integer, Map<Integer, Integer>> primeFactorsCache = null;
	static {
	}
			
	
	// euler's totient function
	// the euler phi function
	// number of positive integers < a that are relatively prime to a (counting 1)
	public static int totient(int a) {
		// first find prime factors, then calculate those explicitly
		// phi(m*n) = phi(m)*phi(n) if n and m are relatively prime
		// phi(p^k) = p^(k-1) * (p-1) if p is prime

		Map<Integer, Integer> primeFactors = primeFactors(a);
		int result = 1;
		for (Integer p : primeFactors.keySet()) {
			Integer k = primeFactors.get(p);

			result *= Math.pow(p, k - 1) * (p - 1);
		}
		return result;
	}

}
