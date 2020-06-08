package edu.mit.reserve.lottery.models

class Category(
	val name: String,
	val odds: Double
) {
	override fun toString() = name

	override fun equals(other: Any?) = this.toString() == other.toString()
}