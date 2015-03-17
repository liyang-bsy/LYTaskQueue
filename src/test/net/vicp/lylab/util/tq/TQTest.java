package test.net.vicp.lylab.util.tq;

import net.vicp.lylab.util.tq.LYTaskQueue;

public class TQTest {
	
	public volatile static Boolean output = false;
	public static Integer MaxTestCase = 100000;
	
	public static void main(String[] arg) throws Exception
	{
//		MaxTestCase = new Integer(arg[0]);
		MaxTestCase = new Integer("10");
		TQTestTemp tmp = null;
		for(int i = 0; i < MaxTestCase ; i++)
		{
			tmp = new TQTestTemp();
			int tkId = LYTaskQueue.addTask(tmp);

    		synchronized (TQTest.output) {
    			while(TQTest.output);
    			TQTest.output = true;
    			System.out.println("[" + tkId + "]Created");
    			TQTest.output = false;
    		}
		}

//		Thread.sleep(5000);
		System.out.println("enter");
		tmp.waitingForFinish();
		System.out.println("finished:" + tmp.getTaskId());
	}
	
}
