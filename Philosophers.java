public class Philosophers {
	static final int num_philosophers = 5;
	static Object[] forks;
	
	static class Philosopher implements Runnable {
		int index;
		int left, right;
		
		public Philosopher(int index) {
			this.index = index;
			this.left = index == 0 ? num_philosophers - 1 : index - 1;
			this.right = index == num_philosophers - 1 ? 0 : index + 1;
		}
		
		public void run() {
			for (int i = 0; i < 100000; i++) {
				int ind_first = Math.min(left, right); // left;
				int ind_second = Math.max(left, right); // right;
				synchronized(forks[ind_first]) {
					synchronized (forks[ind_second]) {
						for (int j = 0; j < 1000; j++) {}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		forks = new Object[num_philosophers];
		for(int i = 0; i < num_philosophers; i++) {
			forks[i] = new Object();
		}
		
		Thread[] philosophers = new Thread[num_philosophers];
		for (int i = 0; i < num_philosophers; i++) {
			philosophers[i] = new Thread(new Philosopher(i));
			philosophers[i].start();
		}
		
		for (int i = 0; i < num_philosophers; i++) {
			try {
				philosophers[i].join();
			}
			catch (InterruptedException e) {
				continue;
			}
		}
		
		System.out.println("Done");
	}
}
