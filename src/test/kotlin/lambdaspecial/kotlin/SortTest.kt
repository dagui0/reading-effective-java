package lambdaspecial.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals

class SortTest {

    @Test
    fun testQuickSort() {
        val quickSort = QuickSort<Int>()
        val unsortedList = listOf(3, 6, 8, 10, 1, 2, 1)
        assertEquals(listOf(1, 1, 2, 3, 6, 8, 10), quickSort.sort(unsortedList))
    }
}