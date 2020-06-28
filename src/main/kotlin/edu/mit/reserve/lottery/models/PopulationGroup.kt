package edu.mit.reserve.lottery.models

import kotlin.math.max

class PopulationGroup(
	val categories: Set<Category> = setOf(),
	val demand: Int
) {

	var firstCutoff = ""
	var secondCutoff = ""
	var involvedInSecondLottery: Boolean = true

	override fun toString(): String {
		var res = ""
		this.categories.sortedBy { it.name }.forEach { res += it.name  + ", "}
		return res.substring(0, max(0,res.length - 2))
	}

	override fun hashCode(): Int = this.toString().hashCode()

	override fun equals(other: Any?): Boolean = this.toString() == other.toString()
}

