package edu.mit.reserve

import edu.mit.reserve.models.Patient
import edu.mit.reserve.models.PopulationGroup

class Lottery(
	private val globalSupply: Int,
	private val globalDemand: Int,
	private val populationGroupDemands: HashMap<PopulationGroup, Int> = HashMap()
) {

	fun addPopulationGroupDemand(populationGroup: PopulationGroup, demand: Int) {
		populationGroupDemands[populationGroup] = demand
	}

	fun processPatient(patient: Patient) {
	}

}

