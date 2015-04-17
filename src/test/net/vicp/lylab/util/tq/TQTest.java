package test.net.vicp.lylab.util.tq;

import net.vicp.lylab.utils.tq.LYTaskQueue;

public class TQTest {
	
	public volatile static Boolean output = false;
	public static Integer MaxTestCase = 100000;
	
	public static void main(String[] arg) throws Exception
	{
//		MaxTestCase = new Integer(arg[0]);
		MaxTestCase = new Integer("100");
		TQTestTemp tmp = null;
		for(int i = 0; i < MaxTestCase ; i++)
		{
			tmp = new TQTestTemp();
			long tkId = LYTaskQueue.addTask(tmp);
			if(i%10==0)
				System.out.println("cur:"+tkId);
    		synchronized (TQTest.output) {
    			while(TQTest.output);
    			TQTest.output = true;
    			System.out.println("[" + tkId + "]Created");
    			TQTest.output = false;
    		}
    		if(i%50==0)
    		{
    			Thread.sleep(1000);
    		}
		}

//		Thread.sleep(5000);
		System.out.println("enter tc:" + LYTaskQueue.getThreadCount());
		tmp.waitingForFinish();
		System.out.println("finished:" + tmp.getTaskId());
	}
	
}
