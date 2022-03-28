package com.kau.dgscalender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notifications {
	private int count;

	public Notifications(int count) {
		this.count = count;
	}

	public void increment() {
		this.count++;
	}
}
