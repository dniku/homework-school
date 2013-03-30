import java.util.*;

public class BatcherSort {
	
	// Testing
	
	public static void shuffle(int[] a) {
		Random r = new Random();
		int n = a.length;
		for (int i = 0; i < n; i++) {
			int j = r.nextInt(n);
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
		}
	}
	
	public static boolean isSorted(int[] a) {
		for (int i = 0; i < a.length - 1; i++) {
			if (a[i] > a[i + 1])
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		int number_of_tests = 100;
		int array_size = 16384;
		for (int i = 0; i < number_of_tests; i++) {
			System.out.printf("%d/%d", i + 1, number_of_tests);
			int[] a = new int[array_size];
			for (int j = 0; j < array_size; j++)
				a[j] = j;
			shuffle(a);
			int[] b = Arrays.copyOf(a, a.length);
			Sorter.batcherSort(b);
			if (!isSorted(b)) {
				System.out.printf(": %s -> %s", Arrays.toString(a), Arrays.toString(b));
			}
			System.out.println();
		}
		System.out.print("Done");
	}
}

class Sorter {
	private static int[] array;
	
	// helper classes
	
	private static class WorkerPool {
		private static final int nworkers = Runtime.getRuntime().availableProcessors() + 1;
		private Thread[] threads;
		private Queue<Comparator> queue;
		private int busy;
		private Object monitor;
		
		private class Worker implements Runnable {
			public void run() {
				while (true) {
					try {
						Comparator comp = getJob();
						int i = comp.a;
						int j = comp.b;
						if (array[i] > array[j]) {
							int t = array[i];
							array[i] = array[j];
							array[j] = t;
						}
					}
					catch (InterruptedException e) {
						return;
					}
				}
			}
		}
		
		public WorkerPool() throws InterruptedException{
			busy = nworkers;
			monitor = new Object();
			threads = new Thread[nworkers];
			queue = new LinkedList<Comparator>();
			for (int i = 0; i < threads.length; i++) {
				if (threads[i] == null) {
					threads[i] = new Thread(new Worker());
					threads[i].start();
				}
			}
			waitForWorkers();
		}
		
		public void stop() {
			for (int i = 0; i < threads.length; i++) {
				threads[i].interrupt();
			}
		}

		private Comparator getJob() throws InterruptedException {
			synchronized (monitor) {
				busy--;
				monitor.notifyAll();
				Comparator result;
				while (queue.isEmpty()) {
					monitor.wait();
				}
				result = queue.poll();
				busy++;
				return result;			
			}
		}
		
		public void addJob(Comparator comp) throws InterruptedException {
			synchronized (monitor) {
				queue.add(comp);
				monitor.notifyAll();
			}
		}
		
		public void waitForWorkers() throws InterruptedException {
			synchronized (monitor) {
				while (true) {
					if (queue.isEmpty() && busy == 0) {
						return;
					}
					monitor.wait();
				}
			}
		}
	}
	
	private static class Comparator {
		int a, b;

		public Comparator(int a, int b) {
			this.a = a;
			this.b = b;
		}
	}

	// helper functions

	private static boolean isPowerOfTwo(int n) {
		return (n & (n - 1)) == 0;
	}


	// Main algorithm

	public static void batcherSort(int[] a) {
		array = a;
		int array_size = a.length;
		if (!isPowerOfTwo(array_size))
			throw new IllegalArgumentException("The length of the array should be a power of two.");
		WorkerPool pool = null;
		try {
			pool = new WorkerPool();
		}
		catch (InterruptedException e) {
			return;
		}
		for (int subarray_size = 1; subarray_size < array_size; subarray_size *= 2) {
			for (int comparison_distance = subarray_size; comparison_distance > 0; comparison_distance /= 2) {
				// Layer start
				for (int subarray_top = 0; subarray_top < array_size; subarray_top += subarray_size * 2) {
					for (int top_of_group = subarray_size == comparison_distance ? subarray_top : subarray_top + comparison_distance;
					top_of_group + comparison_distance < subarray_top + subarray_size * 2; top_of_group += comparison_distance * 2) {
						for (int comparator_index = 0; comparator_index < comparison_distance; comparator_index++) {
							int comparator_top = comparator_index + top_of_group;
							int comparator_bottom = comparator_top + comparison_distance;
							Comparator comp = new Comparator(comparator_top, comparator_bottom);
							try {
								pool.addJob(comp);
							} catch (InterruptedException e) {
								return;
							}
						}	
					}
				}
				// Layer end
				try {
					pool.waitForWorkers();
				}
				catch (InterruptedException e) {
					return;
				}
			}
		}
		pool.stop();
	}
}