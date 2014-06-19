package net.fishear.web.t5.jquery.pages;

import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class DialogTest extends ComponentBase {

	@Persist
	private int act1Num;

	@Persist
	private int act2Num;

	@Property
	int loopVar;
	
	@Property
	String loopVar2;
	
	@Property
	@Persist
	String dialog2Value;
	
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
	
	@OnEvent("ajaxDialogEvent_1")
	public void ajax1(int context) {
		act1Num = context;
	}
	
	@OnEvent("ajaxDialogEvent_2")
	public void ajax2(String code) {
		act2Num++;
		System.err.println("ajaxDialogEvent_2 called - act2Num=" + act2Num + ",  new code = '" + code + "'");
		dialog2Value = code;
	}
	
}
