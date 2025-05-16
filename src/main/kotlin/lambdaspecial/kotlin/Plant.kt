package lambdaspecial.kotlin

data class Plant(
    val name: String,
    val koName: String,
    val lifeCycle: LifeCycle
) : Comparable<Plant> {

    enum class LifeCycle(val koName: String) {
        ANNUAL("한해살이"),
        BIENNIAL("두해살이"),
        PERENNIAL("여러해살이");

        override fun toString(): String = koName
    }

    override fun compareTo(other: Plant): Int = koName.compareTo(other.koName)
    override fun toString(): String = koName
}
