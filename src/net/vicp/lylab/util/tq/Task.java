package net.vicp.lylab.util.tq;

import net.vicp.lylab.core.Executor;

/**
 * 	Extends Task and reference to TaskQueue(manage class).<br>
 * 	Override exec() to satisfy your needs.<br>
 * 
 * 	<br>Release Under GNU Lesser General Public License (LGPL).
 * 
 * @author Young Lee
 * @since 2015.03.17
 * @version 1.0.0
 * 
 */
public abstract class Task implements Runnable, Executor {

	private Integer taskId;
	private Integer state = BEGAN;

	static public final Integer FAILED = -1;
	static public final Integer BEGAN = 0;
	static public final Integer EXECUTED = 1;
	static public final Integer COMPLETED = 2;
	static public final Integer CANCELLED = 3;

	/**
	 * Reserved entrance for multi-threaded. DO NOT call this method.
	 */
	public final void run()
	{
		if(getState() != BEGAN)
			return;
		setState(EXECUTED);
		try {
			exec();
			setState(COMPLETED);
		} catch (Throwable e) {
			e.printStackTrace();
			setState(FAILED);
		}

		synchronized (LYTaskQueue.isRunning) {
			LYTaskQueue.isRunning.notifyAll();
		}
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * Call it to cancel this task.
	 * @return
	 * true: cancelled<br>false: cancel failed
	 */
	public final Boolean cancel() {
		synchronized (getState()) {
			if (getState() != BEGAN)
				return false;
			setState(CANCELLED);
			return true;
		}
	}

	/**
	 * Alert! This function will block current thread!
	 * But the task will finish if this function is completed.
	 */
	public final void waitingForFinish() {
		synchronized (this)
		{
			while(getState() == BEGAN || getState() == EXECUTED)
			{
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public final Integer getTaskId() {
		return taskId;
	}

	public final Task setTaskId(Integer taskId) {
		this.taskId = taskId;
		return this;
	}

	public synchronized Integer getState() {
		return state;
	}

	public synchronized Task setState(Integer state) {
		this.state = state;
		return this;
	}

}
