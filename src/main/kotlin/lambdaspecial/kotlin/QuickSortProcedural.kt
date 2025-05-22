package lambdaspecial.kotlin

class QuickSortProcedural<T : Comparable<T>> : Sort<T> {

    override fun sort(array: List<T>): List<T> {
        if (array.isEmpty()) { // null 체크 대신 Kotlin 스타일로 isEmpty 사용 (인터페이스가 non-null 가정)
            return array
        }
        // 원본 리스트를 변경하지 않기 위해 가변 리스트로 복사
        val listToSort = array.toMutableList()
        quickSortRecursive(listToSort, 0, listToSort.size - 1)
        return listToSort
    }

    private fun quickSortRecursive(list: MutableList<T>, low: Int, high: Int) {
        if (low < high) {
            val pivotIndex = partition(list, low, high)
            quickSortRecursive(list, low, pivotIndex - 1)
            quickSortRecursive(list, pivotIndex + 1, high)
        }
    }

    private fun partition(list: MutableList<T>, low: Int, high: Int): Int {

        val mid = low + (high - low) / 2
        val pivot = list[mid]
        list.swap(mid, high) // 중간 피벗을 마지막으로 옮겨서 아래 로직과 호환

        var i = low - 1 // 작은 요소들의 마지막 위치

        for (j in low until high) { // high 인덱스는 피벗이므로 high - 1 까지 순회
            if (list[j] <= pivot) { // Kotlin에서는 compareTo 대신 <= 연산자 직접 사용 가능
                i++
                list.swap(i, j)
            }
        }

        list.swap(i + 1, high) // 피벗을 최종 위치로 이동
        return i + 1 // 피벗의 최종 위치 반환
    }

    // MutableList에 대한 확장 함수로 swap 정의 (또는 Collections.swap 사용)
    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        if (index1 == index2) return
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

    fun sort(array: IntArray) {
        if (array.size < 2) {
            return
        }
        quickSortRecursive(array, 0, array.lastIndex) // array.lastIndex는 array.size - 1과 동일
    }

    private fun quickSortRecursive(array: IntArray, low: Int, high: Int) {
        if (low < high) {
            val pivotIndex = partition(array, low, high)
            quickSortRecursive(array, low, pivotIndex - 1)
            quickSortRecursive(array, pivotIndex + 1, high)
        }
    }

    private fun partition(array: IntArray, low: Int, high: Int): Int {
        // 랜덤 피벗 선택
        val mid = low + (high - low) / 2
        val pivot = array[mid]
        array.swap(mid, high) // 피벗을 맨 뒤로 이동

        var i = low - 1 // 작은 요소들의 마지막 위치

        for (j in low until high) { // high 인덱스는 피벗이므로 high - 1 까지 순회
            if (array[j] <= pivot) {
                i++
                array.swap(i, j)
            }
        }

        array.swap(i + 1, high) // 피벗을 최종 위치로 이동
        return i + 1 // 피벗의 최종 위치 반환
    }

    // IntArray에 대한 확장 함수로 swap 정의
    private fun IntArray.swap(index1: Int, index2: Int) {
        if (index1 == index2) return
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}
