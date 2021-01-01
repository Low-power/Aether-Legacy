package com.gildedgames.the_aether.entities.hostile.swet;

public enum SwetType {
	BLUE(), GOLDEN();

	public int get_id() {
		return this.ordinal();
	}

	public static SwetType get(int id) {
		return values()[id];
	}
}
