package lambdaspecial.kotlin

import java.util.*
import java.util.function.Consumer
import kotlin.test.Test
import kotlin.test.assertEquals

class SortTest {

    @Test
    fun testQuickSortLambda() {
        val quickSortLambda = QuickSortLambda<Int>()
        val unsortedList = listOf(3, 6, 8, 10, 1, 2, 1)
        assertEquals(listOf(1, 1, 2, 3, 6, 8, 10), quickSortLambda.sort(unsortedList))
    }

    @Test
    fun testQuickSortProcedural() {
        val quickSortProcedural = QuickSortProcedural<Int>()
        val unsortedList = listOf(3, 6, 8, 10, 1, 2, 1)
        assertEquals(listOf(1, 1, 2, 3, 6, 8, 10), quickSortProcedural.sort(unsortedList))
    }

    @Test
    fun testQuickSortArray() {
        val original = intArrayOf(3, 6, 8, 10, 1, 2, 1)
        var sorted = intArrayOf(1, 1, 2, 3, 6, 8, 10)
        val quickSortProcedural = QuickSortProcedural<Int>()
        quickSortProcedural.sort(original)
        assertEquals(sorted.toList(), original.toList())
    }


    class Report {
        val sample: Int
        val data: IntArray
        var basis: Long = 0L
        val times: MutableMap<String, Long> = TreeMap<String, Long>()

        constructor(sample: Int, data: IntArray) {
            this.sample = sample
            this.data = data
        }

        val dataArray: IntArray
            get() {
                val copy = IntArray(data.size)
                System.arraycopy(data, 0, copy, 0, data.size)
                return copy
            }

        val dataList: MutableList<Int>
            get() {
                val list: MutableList<Int> = ArrayList<Int>(data.size)
                for (i in data) {
                    list.add(i)
                }
                return list
            }

        fun setTime(method: String, func: Runnable) {
            setTime(method, func, false)
        }

        fun setTime(method: String, func: Runnable, isBasis: Boolean) {
            val startTime = System.nanoTime()
            func.run()
            val endTime = System.nanoTime()
            val time = endTime - startTime
            times.put(method, time)
            if (isBasis) {
                basis = time
            }
        }
    }

    @org.junit.jupiter.api.Test
    fun testPerfomances() {
        val MAXVALUE = 1000000
        val r = Random()
        val samples = intArrayOf( 3, 10, 100, 1000, 10000, 100000, 1000000 )
        val reports: MutableMap<Int, Report> = TreeMap<Int, Report>()

        val javaProcedural = lambdaspecial.java.QuickSortProcedural<Int>()
        val javaLambda = lambdaspecial.java.QuickSortLambda<Int>()
        val kotlinProcedural = QuickSortProcedural<Int>()
        val kotlinLambda = QuickSortLambda<Int>()

        for (sample in samples) {
            val array = IntArray(sample)
            for (i in 0..<sample) {
                val rn = r.nextInt(MAXVALUE)
                array[i] = rn
            }

            val report = Report(sample, array)
            reports.put(sample, report)

            // array
            val copiedArray = report.dataArray
            report.setTime("java | array", Runnable {
                javaProcedural.sort(copiedArray)
            }, true)

            // java list
            val copiedList1 = report.dataList
            report.setTime("java | list", Runnable {
                javaProcedural.sort(copiedList1)
            })

            // java lambda
            val copiedList2 = report.dataList
            report.setTime("java | lambda", Runnable {
                javaLambda.sort(copiedList2)
            })

            // kotlin array
            val copiedArray1 = report.dataArray
            report.setTime("kotlin | array", Runnable {
                kotlinProcedural.sort(copiedArray1)
            })

            // kotlin list
            val copiedList11 = report.dataList
            report.setTime("kotlin | list", Runnable {
                kotlinProcedural.sort(copiedList11)
            })

            // kotlin lambda
            val copiedList21 = report.dataList
            report.setTime("kotlin | lambda", Runnable {
                kotlinLambda.sort(copiedList21)
            })
        }

        println("| lang | method | 3 | 10 | 100 | 1k | 10k | 100k | 1M |")
        println("|------|--------|---|----|-----|----|-----|------|----|")
        listOf<String>("java | array", "java | list", "java | lambda",
            "kotlin | array", "kotlin | list", "kotlin | lambda")
            .forEach(Consumer {
                    method: String ->
                System.out.printf("| %s |", method)
                reports.forEach { (sample: Int, report: Report) ->
                    val basis: Long = report.basis
                    val time = report.times[method]?: 0L
                    System.out.printf(" %.1f |", (time.toDouble() / basis))
                }
                println()
            }
            )
        println()

        println("| lang | method | 3 |  10 | 100 | 1k | 10k | 100k | 1M |")
        println("|------|--------|---|----|-----|----|-----|------|----|")
        listOf<String>("java | array", "java | list", "java | lambda",
                       "kotlin | array", "kotlin | list", "kotlin | lambda")
            .forEach(Consumer {
                method: String ->
                    System.out.printf("| %s |", method)
                    reports.forEach { (sample: Int, report: Report) ->
                        val time = report.times[method]?: 0L
                        System.out.printf(" %.3f |", (time.toDouble() / 1000000.0))
                    }
                    println()
                }
        )
        println()
    }
}
