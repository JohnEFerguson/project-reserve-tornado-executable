package edu.mit.reserve.models

class PopulationGroup(
	private val categories: Set<Category> = setOf()
) {

	// TODO: implement these for mapping in lottery

	override fun toString(): String {
		var res = ""
		this.categories.forEach { res += it.name }
		return res
	}

	override fun hashCode(): Int = this.toString().hashCode()
}