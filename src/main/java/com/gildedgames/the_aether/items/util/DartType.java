package com.gildedgames.the_aether.items.util;

public enum DartType {

	Golden(0, "golden"), Poison(1, "poison"), Enchanted(2, "enchanted");

	public int meta;

	public String name;

	private DartType(int meta, String name) {
		this.meta = meta;
		this.name = name;
	}

	public static DartType getType(int meta) {
		return meta == 1 ? Poison : meta == 2 ? Enchanted : Golden;
	}

	public int getMeta() {
		return this.meta;
	}

	public String toString() {
		return this.name;
	}

}
