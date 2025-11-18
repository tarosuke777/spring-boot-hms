package com.tarosuke777.hms.enums;

import java.util.HashMap;
import java.util.Map;

public enum TargetArea {
	CHEST(1, "胸"), SHOULDER(2, "肩"), TRICEPS(3, "上腕三頭筋"), BACK(4, "背中"), BICEPS(5,
			"上腕二頭筋"), OBLIQUES(6, "腹斜筋"), QUADS_GLUTES(7,
					"太もも（大腿四頭筋）・お尻（大臀筋）"), LEGS(8, "太もも（大腿四頭筋／ハムストリングス／内転筋群）");

	private final int id;
	private final String label;

	TargetArea(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public static Map<Integer, String> getTargetAreaMap() {
		Map<Integer, String> map = new HashMap<>();
		for (TargetArea targetArea : values()) {
			map.put(targetArea.getId(), targetArea.getLabel());
		}
		return map;
	}
}
