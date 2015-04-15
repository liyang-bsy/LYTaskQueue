package test.net.vicp.lylab.util.tq.minitest;

import java.util.Random;

public class TQTestTemp extends Task{
	@Override
	public void exec() {
		try {
	        int max=5000;
	        int min=1000;
	        Random random = new Random();
	        int s = random.nextInt(max)%(max-min+1) + min;
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\t\t[" + getTaskId() + "]Done\t\tThread:" + Thread.activeCount());
	}

}
