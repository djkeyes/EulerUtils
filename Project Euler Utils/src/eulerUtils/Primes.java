package eulerUtils;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

// utility class that exposes some convenience datastructures
// these allow you to iterate over (infinite) lists of primes as if they were normal java lists

// TODO: support longs, biginteger, etc
// TODO: optimize for primes larger than 100 million, sieve starts to get slow
public class Primes {

	// cached set of primes.
	// TODO: make an object or a cache management thing to handle access to this shit
	private static Set<Integer> primeSetCache = null;
	private static List<Integer> primeListCache = null;
	private static int largestCachedPrime = 0;

	static {
		resizeCaches();
	}

	// initializes and resizes on demand
	private static void resizeCachesIfPrimeTooBig(int a) {
		while (a >= largestCachedPrime) {
			resizeCaches();
		}
	}

	private static void resizeCachesIfPrimeIndexTooBig(int index) {
		while (index >= primeListCache.size()) {
			resizeCaches();
		}
	}

	private static void resizeCaches() {
		System.out.println("resizing, max=" + largestCachedPrime);
		if (largestCachedPrime == 0) {
			largestCachedPrime = 10;
		}
		largestCachedPrime *= 2;

		primeListCache = Collections.unmodifiableList(PrimeSieve.genPrimes((int) (2 * Math.sqrt(largestCachedPrime)),
				largestCachedPrime));
		Set<Integer> temp = new HashSet<>();
		temp.addAll(primeListCache);
		primeSetCache = Collections.unmodifiableSet(temp);
		System.out.println("resize complete!");
	}

	public static boolean isPrime(int a) {
		resizeCachesIfPrimeTooBig(a);
		return primeSetCache.contains(a);
	}

	public static Set<Integer> primeSet() {
		return new PrimeSet();
	}

	// returns a list of primes
	// it's backed by a list that resizes whenever you run out of primes, so you can iterate through this until your computer
	// runs out of memory
	public static List<Integer> primeList() {
		return new PrimeList();
	}

	private static abstract class PrimeCollectionAdapter implements Collection<Integer> {

		@Override
		public boolean add(Integer e) {
			throw new UnsupportedOperationException("don't add to this collection, yo. it's immutable!");
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			throw new UnsupportedOperationException("don't add to this collection, yo. it's immutable!");
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException("don't delete from this collection, yo. it's immutable!");
		}

		@Override
		public boolean contains(Object o) {
			return o instanceof Integer && isPrime((int) o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object o : c) {
				if (!contains(o)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			return false; // primes exist
		}

		public ListIterator<Integer> listIterator(int index) {
			resizeCachesIfPrimeIndexTooBig(index);
			final int ind = index;
			return new ListIterator<Integer>() {
				// the iterator over the actual collection
				private ListIterator<Integer> backingIterator = primeListCache.listIterator(ind);
				private int last = -1;

				@Override
				public boolean hasNext() {
					return true; // there are always more primes
				}

				@Override
				public Integer next() {
					if (!backingIterator.hasNext()) {
						// if we're out of primes, we need to resize and get a new iterator
						resizeCaches();
						backingIterator = primeListCache.listIterator();
						// fast forward to our old position.
						// TODO: optimize this
						while (backingIterator.next() != last);
					}
					return last = backingIterator.next();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("don't delete from this collection, yo. it's immutable!");
				}

				@Override
				public void add(Integer e) {
					throw new UnsupportedOperationException("don't add to this collection, yo. it's immutable!");
				}

				@Override
				public boolean hasPrevious() {
					return backingIterator.hasPrevious();
				}

				@Override
				public int nextIndex() {
					return backingIterator.nextIndex();
				}

				@Override
				public Integer previous() {
					return last = backingIterator.previous();
				}

				@Override
				public int previousIndex() {
					return backingIterator.previousIndex();
				}

				@Override
				public void set(Integer e) {
					throw new UnsupportedOperationException("don't modify this collection, yo. it's immutable!");
				}
			};
		}
		public ListIterator<Integer> listIterator() {
			return listIterator(0);
		}

		@Override
		public Iterator<Integer> iterator() {
			return listIterator();
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException("don't delete from this collection, yo. it's immutable!");
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException("don't delete from this collection, yo. it's immutable!");
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException("don't delete from this collection, yo. it's immutable!");
		}

		@Override
		public int size() {
			throw new UnsupportedOperationException("there are infinite primes, yo.");
		}

		@Override
		public Object[] toArray() {
			throw new UnsupportedOperationException("don't represent this collection as an array, yo.");
		}

		@Override
		public <T> T[] toArray(T[] a) {
			throw new UnsupportedOperationException("don't represent this collection as an array, yo.");
		}

	}

	private static class PrimeSet extends PrimeCollectionAdapter implements Set<Integer> {

	}

	private static class PrimeList extends PrimeCollectionAdapter implements List<Integer> {

		@Override
		public void add(int index, Integer element) {
			throw new UnsupportedOperationException("don't add to this list, yo. it's immutable!");
		}

		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) {
			throw new UnsupportedOperationException("don't add to this list, yo. it's immutable!");
		}

		@Override
		public Integer get(int index) {
			resizeCachesIfPrimeIndexTooBig(index);
			return primeListCache.get(index);
		}

		@Override
		public int indexOf(Object o) {
			resizeCachesIfPrimeTooBig((Integer) o);
			return primeListCache.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return indexOf(o);
		}

		@Override
		public Integer remove(int index) {
			throw new UnsupportedOperationException("don't delete from this list, yo. it's immutable!");
		}

		@Override
		public Integer set(int index, Integer element) {
			throw new UnsupportedOperationException("don't modify this list, yo. it's immutable!");
		}

		@Override
		public List<Integer> subList(int fromIndex, int toIndex) {
			resizeCachesIfPrimeIndexTooBig(toIndex);
			return primeListCache.subList(fromIndex, toIndex);
		}

	}
	
	// here's a simple smoke test. run through positive integers and verify that our prime results are correct.
	// we can check our results against BigInteger.isProbablePrime().
	// on my machine, this runs successfully for 9385400 primes before stalling from the memory requirements.
	// notably, this isn't deterministic--it fails early on occasion, since isProbablePrime is a randomized algorithm.
	public static void main(String[] args){
		int count = 0;
		
		int last = 0;
		// here's a common use case
		// just iterate through the list until you're done. remember to break, because this list is infinite!
		for (Integer p : Primes.primeList()) {
			// check all the "composite" numbers between last and cur
			boolean verifiedComposites = true;
			for(int i=last+1; i < p; i++){
				BigInteger big = BigInteger.valueOf(i);
				if(big.isProbablePrime(10)){
					System.err.println("missed a prime number! (" + i + ")");
					verifiedComposites = false;
				}
			}
			if(!verifiedComposites){
				break;
			}
			
			// check that the current num is prime
			count++;
			BigInteger big = BigInteger.valueOf(p);
			boolean verified = big.isProbablePrime(10);
			if(verified){
				System.out.println(count + ": " + p + " -- verified");
			} else {
				System.err.println(count + ": " + p + " -- !!!!!!!!!!NOT PRIME!!!!!!!!!");
				break;
			}
			
			last = p;
		}
	}
}
