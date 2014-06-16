package net.fishear.web.t5.pages;

import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;

public class DialogTest extends ComponentBase {

	@Persist
	private int act1Num;

	@Persist
	private int act2Num;

	/**
	 * @return the act1Num
	 */
	public int getAct1Num() {
		return act1Num;
	}

	/**
	 * @param act1Num the act1Num to set
	 */
	public void setAct1Num(int act1Num) {
		this.act1Num = act1Num;
	}

	/**
	 * @return the act2Num
	 */
	public int getAct2Num() {
		return act2Num;
	}

	/**
	 * @param act2Num the act2Num to set
	 */
	public void setAct2Num(int act2Num) {
		this.act2Num = act2Num;
	}
	
	@OnEvent(value="Mujevent")
	public void onMujevent() {
		System.err.println("(1)XXXXXXXXXXXXXXXXXXXXXXXXXx " + act1Num++);
	}
	
	@OnEvent(value="mujevent2")
	public void ajax2() {
		System.err.println("(2)XXXXXXXXXXXXXXXXXXXXXXXXXx " + act2Num++);
	}
	
}
