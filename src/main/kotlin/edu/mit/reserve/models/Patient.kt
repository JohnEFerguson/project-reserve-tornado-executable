package edu.mit.reserve.models

class Patient(
	private val name: String,
	private val populationGroups: MutableSet<PopulationGroup> = mutableSetOf()
) {

	fun addPoplationGroup(group: PopulationGroup) {
		populationGroups.add(group)
	}
}