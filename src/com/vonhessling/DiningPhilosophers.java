package com.vonhessling;

/**
 * Java implementation for the Dining Philosophers problem 
 * (http://en.wikipedia.org/wiki/Dining_philosophers_problem)
 * 
 * @author vonhessling
 *
 */
public class DiningPhilosophers {
	private Object[] forks;
	private Philosopher[] philosophers;
	private Thread[] threads;
	
	public DiningPhilosophers(int num) {
		forks = new Object[num];
		philosophers = new Philosopher[num];
		threads = new Thread[num];
		for (int i = 0; i < num; i++) {
			forks[i] = new Object();
			
			int fork1 = i;
			int fork2 = (i+1) % num;
			// solution for avoiding deadlock and livelock: 
			// first philosopher picks up right fork first...
			if (i == 0) {  
				philosophers[0] = new Philosopher(0, fork2, fork1);
			} else {
				philosophers[i] = new Philosopher(i, fork1, fork2);
			}
		}
	}
	
	public void startEating() throws InterruptedException {
		for (int i = 0; i < philosophers.length; i++) {
			Thread thread = new Thread(philosophers[i]);
			threads[i] = thread;
			thread.start();
		}
		
		threads[0].join(); // will never happen
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		DiningPhilosophers d = new DiningPhilosophers(5);
		d.startEating();
	}
	
	



	class Philosopher implements Runnable {
		
		private int id;
		private int fork1;
		private int fork2;
		
		Philosopher(int id, int fork1, int fork2) {
			this.id = id;
			this.fork1 = fork1;
			this.fork2 = fork2;
		}
		
		public void run() {
			status("Ready to eat using forks " + fork1 + " and " + fork2);
			pause(); // let others get ready
			while(true) {
				status("Picking up fork " + fork1);
				synchronized(forks[fork1]) {
					status("Picking up fork " + fork2);
					synchronized(forks[fork2]) {
						status("Eating...");
					}
				}
			}
		}
		
		private void pause() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				
			}
		}
		
		private void status(String message) {
			System.out.println("Philosopher " + id + ": " + message);
		}
	}

}