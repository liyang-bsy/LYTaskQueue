package test.net.vicp.lylab.util.tq.minitest;

public class LYTQ {

	private static Integer maxThread = 2;

	public volatile static Integer _tc = 0;
	
	public static void _inc() { synchronized (_tc) { _tc++; } }
	public static void _dec() { synchronized (_tc) {
		try {
			_tc.notifyAll();
		} catch (Exception e) { e.printStackTrace(); }
		_tc--;
		}
	}
	
	public static void main(String[] argv)
	{
		synchronized (_tc) {
			_tc.notifyAll();
			_tc.notifyAll();
			_tc.notifyAll();
			_tc.notifyAll();
			_tc.notifyAll();
		}
		for(int i=0;i<50;i++)
			execute((long)i);
	}

	private static void execute(long i)
	{
		while(true)
		{
			synchronized (_tc) {
				System.out.println("tc:" + _tc);
				if(_tc >= maxThread)
				{
					try {
						_tc.wait();
						continue;
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			_inc();
			new Thread(new TQTestTemp().setTaskId(i)).start();
			break;
		}
	}
	
	/**
	 * [!] Not real-time
	 * @return a number indicates running threads
	 */
	public static Integer getThreadCount() {
		return _tc;
	}
}
