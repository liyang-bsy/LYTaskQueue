package test.net.vicp.lylab.util.tq.minitest;

import net.vicp.lylab.core.Executor;

/**
 * 	Extends Task and reference to TaskQueue(manage class).<br>
 * 	Override exec() to satisfy your needs.<br>
 * 
 * 	<br>Release Under GNU Lesser General Public License (LGPL).
 * 
 * @author Young Lee
 * @since 2015.03.17
 * @version 1.0.1
 * 
 */
public abstract class Task implements Runnable, Executor, Cloneable {

	private Long taskId;

	public final Long getTaskId() {
		return taskId;
	}

	public final Task setTaskId(Long taskId) {
		this.taskId = taskId;
		return this;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public final void run()
	{
		exec();
		LYTQ._dec();
	}

}
