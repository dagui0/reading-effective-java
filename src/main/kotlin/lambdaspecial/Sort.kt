package lambdaspecial

interface Sort<T : Comparable<T>> {
    fun sort(array: List<T>): List<T>
}
