package net.fishear.data.generic.data;

import net.fishear.data.generic.entities.AbstractEntity;

public class TestEntity extends AbstractEntity
{

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String text;
	
	private String name;
	
}
