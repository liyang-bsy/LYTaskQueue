package net.vicp.lylab.util.tq;

import java.util.LinkedList;
import java.util.Queue;

import net.vicp.lylab.core.Executor;

/**
 * 	Manager class to execute all task.<br>
 * 	Finish tasks within certain threads.<br>
 * 
 * 	<br>Release Under GNU Lesser General Public License (LGPL).
 * 
 * @author Young Lee
 * @since 2015.03.17
 * @version 1.0.0
 * 
 */
public final class LYTaskQueue extends Thread implements Runnable{

	private volatile static Queue<Executor> tqs = new LinkedList<Executor>();
	public volatile static Boolean isRunning = false;

	private static Integer lastTaskId = 0;
	private static Integer maxQueue = 1000;
	private static Integer maxThread = 200;

	/**
	 * Reserved entrance for multi-threaded. DO NOT call this method.
	 */
	@Override
	public void run() { execute(); }

	/**
	 * At your service!
	 * @param
	 * task the task which should be executed
	 * @return
	 * taskId used to recognise specific task.
	 */
	public static Integer addTask(Task task)
	{
		if(task == null)
			return null;
		synchronized (tqs) {
			while (true) {
				Integer size = tqs.size();
				if (size >= maxQueue) {
					try {
						tqs.wait();
						continue;// 继续执行等待中的检入任务。
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (size <= maxQueue && size >= 0) {
					synchronized (lastTaskId) {
						task.setTaskId(lastTaskId++);
						tqs.offer(task);
					}
					break;
				}
			}
		}
		if(!isRunning)
		{
			isRunning = true;
			new LYTaskQueue().start();
		}
		return task.getTaskId();
	}

	/**
	 * Use this to cancel this task.
	 * <br><tt>[HINT]</tt> If you own the 'task', you may also cancel you task by invoking task.cancel()
	 * @param
	 * taskId the taskId which you will get from LYTaskQueue.addTask()
	 * @return
	 * true: cancelled<br>false: cancel failed
	 */
	public static Boolean cancel(Integer taskId) throws Exception {
		synchronized (tqs) {
			if(lastTaskId < taskId)
				return false;
			try{
				for(Executor tk: ((LinkedList<Executor>) tqs))
					if(taskId.equals(((Task) tk).getTaskId()))
						return ((Task) tk).cancel();
			} catch(Exception e) {
				System.out.println("tqs.size():" + tqs.size() + "\tlastTaskId:" + lastTaskId + "\ttaskId:" + taskId);
				e.printStackTrace();
			}
		}
		return false;
	}

	private static void execute()
	{
		while(tqs.peek() != null)
		{
			Task task = (Task) tqs.poll();
			while(task.getState() == Task.BEGAN)
			{
				if(LYTaskQueue.activeCount() > maxThread)
				{
					synchronized (isRunning) {
						try {
							isRunning.wait();
							continue;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				new Thread(task).start();
				synchronized (tqs) {
					tqs.notifyAll();
				}
			}
		}
		isRunning = false;
	}

	public static Integer getMaxQueue() {
		return maxQueue;
	}

	public static void setMaxQueue(Integer maxQueue) {
		LYTaskQueue.maxQueue = maxQueue;
	}

	public static Integer getMaxThread() {
		return maxThread;
	}

	public static void setMaxThread(Integer maxThread) {
		LYTaskQueue.maxThread = maxThread;
	}

}
