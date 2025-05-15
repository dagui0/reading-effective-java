package lambdaspecial.kotlin

interface Sort<T : Comparable<T>> {
    fun sort(array: List<T>): List<T>
}
