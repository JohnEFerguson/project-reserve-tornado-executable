package edu.mit.reserve.lottery.models

import java.time.LocalDate


class Patient(
	private val id: String,
	private val name: String,
	private var populationGroup: PopulationGroup,
	private val date: LocalDate
) {

	fun getPopulationGroup() = this.populationGroup
	fun setPopulationGroup(populationGroup: PopulationGroup) { this.populationGroup = populationGroup }
}