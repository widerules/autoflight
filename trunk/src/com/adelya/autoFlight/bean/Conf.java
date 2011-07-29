package com.adelya.autoFlight.bean;

import java.util.Calendar;

/**
 * A configuration of the list view
 * @author laurent
 *
 */
public abstract class Conf {

	/** The label of the configuration menu */
	private String label = "";
	/** The value of the configuration menu */
	private Calendar value;
	/** The id of the icon */
	private int iconResource;
	
	public Conf(String label, Calendar value) {
		this(label, value, -1);
	}
	
	public Conf(String label, Calendar value, int iconResource) {
		setLabel(label);
		setValue(value);
		setIconResource(iconResource);
	}
	
	/**
	 * The action when the user click on the conf item
	 */
	public abstract void launchAction();
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Calendar getValue() {
		return value;
	}
	
	public void setValue(Calendar value) {
		this.value = value;
	}
	
	public int getIconResource() {
		return iconResource;
	}
	
	public void setIconResource(int iconResource) {
		this.iconResource = iconResource;
	}
}
