package lambdaspecial.kotlin

class QuickSort<T : Comparable<T>> : Sort<T> {
    override fun sort(array: List<T>): List<T> {
        if (array.size <= 1) return array // 리스트가 비어있거나 하나의 요소만 있으면 그대로 반환

        val pivot = array[array.size / 2] // 피벗 선택 (중간 값)
        val less = array.filter { it < pivot } // 피벗보다 작은 값들
        val equal = array.filter { it == pivot } // 피벗과 같은 값들
        val greater = array.filter { it > pivot } // 피벗보다 큰 값들

        return sort(less) + equal + sort(greater) // 재귀적으로 정렬 후 병합
    }
}
