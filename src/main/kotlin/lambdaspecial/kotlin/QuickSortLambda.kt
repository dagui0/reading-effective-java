package lambdaspecial.kotlin

class QuickSortLambda<T : Comparable<T>> : Sort<T> {

    override fun sort(array: List<T>): List<T> {

        if (array.size <= 1) return array

        val pivot = array[array.size / 2]

        val grouped = array.groupBy {
            when {
                it < pivot -> -1
                it == pivot -> 0
                else -> 1
            }
        }

        val less = grouped[-1]?: emptyList()
        val equal = grouped[0]?: emptyList()
        val greater = grouped[1]?: emptyList()

        return sequenceOf(sort(less), equal, sort(greater))
                .flatMap { it.asSequence() }
                .toList()
    }
}
