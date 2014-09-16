package eulerUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// a fraction class that provides several utility functions and reduces itself to simplest form automatically
public class Fraction {
	
	private int num;
	private int denom;
	
	private boolean simplified;
	
	public Fraction(int n, int d){
		this.num = n;
		this.denom = d;
		this.simplified = false;
		
		simplify();
	}
	
	
	
	public void simplify(){
		if(this.simplified)
			return;
		this.simplified = true;
		// we're doing a cool method here--represent the numerator and denominator as prime decompositions, and remove
		// anything in common.
		// ignoring the overhead of calculating primes (which is a one-time operation that takes a few seconds at most),
		// this is much faster than the dumber method of try-every-number to see if it is a common divisor.
		// a possible optimization would be to cache the decomposition so we wouldn't have to recalculate it as often. meh.
		
		Map<Integer,Integer> numFactors = Util.primeFactors(num);
		Map<Integer,Integer> denomFactors = Util.primeFactors(denom);
		
		Set<Integer> numPrimes = new HashSet<Integer>(numFactors.keySet());
		for(Integer factor : numPrimes){
			if(denomFactors.containsKey(factor)){
				int numPower = numFactors.get(factor);
				int denomPower = denomFactors.get(factor);
				if(numPower < denomPower){
					numFactors.remove(factor);
					denomFactors.put(factor, denomPower-numPower);
				} else if(numPower > denomPower){
					denomFactors.remove(factor);
					numFactors.put(factor, numPower-denomPower);
				} else {
					// equal
					numFactors.remove(factor);
					denomFactors.remove(factor);
				}
			}
		}
		
		int newNum = 1;
		int newDenom = 1;
		for(Integer p : numFactors.keySet()){
			int k = numFactors.get(p);
			newNum *= Math.pow(p, k);
		}
		for(Integer p : denomFactors.keySet()){
			int k = denomFactors.get(p);
			newDenom *= Math.pow(p, k);
		}
		this.num = newNum;
		this.denom = newDenom;
	}
	
	@Override
	public boolean equals(Object other){
		Fraction that = (Fraction) other;
		return this.num*that.denom == that.num*this.denom;
	}
	
	@Override
	public int hashCode(){
		// just use the Double hashcode
		// if we simplify the fraction beforehand, this should be deterministic (so long as there's not overflow i think).
		simplify();
		
		Double decimal = (double)num/denom;
		return decimal.hashCode();
	}
	
	@Override
	public String toString(){
		return "[Fraction: " + num + "/" + denom + "]";
	}
}
