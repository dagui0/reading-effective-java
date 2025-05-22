package lambdaspecial.kotlin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.BiPredicate
import java.util.function.Predicate
import java.util.stream.Collectors

class PlantTest {

    @Test
    fun testLambdaCurring() {
        val plant = Plant("Sunflower", "해바라기", Plant.LifeCycle.ANNUAL)
        val lifeCycleCriteria = Plant.LifeCycle.ANNUAL

        // $ (λ(p, lc). p.lifeCycle() == lc) (plant, lifeCycleCriteria) $
        val filterByLifeCycle: (Plant, Plant.LifeCycle) -> Boolean = { p, lc -> p.lifeCycle == lc }
        assertTrue(filterByLifeCycle(plant, lifeCycleCriteria))

        // object : BiPredicate<> { }
        // java의 new BiPredicate<>() { } 와 같음
        val filterByLifeCycle2 = object : BiPredicate<Plant, Plant.LifeCycle> {
            override fun test(p: Plant, lc: Plant.LifeCycle): Boolean {
                return p.lifeCycle == lc
            }
        }
        assertTrue(filterByLifeCycle2.test(plant, lifeCycleCriteria))

        // $ (λp. (λlc. p.lifeCycle() == lc) lifeCycleCriteria )  plant $
        val filterByLifeCycle3 = Predicate<Plant> { p ->
            val innerPredicate: (Plant.LifeCycle) -> Boolean = { lc -> p.lifeCycle == lc }
            innerPredicate(lifeCycleCriteria)
        }
        assertTrue(filterByLifeCycle3.test(plant))

        val filterByLifeCycle4 = object : Predicate<Plant> {
            override fun test(p: Plant): Boolean {
                return object : Predicate<Plant.LifeCycle> {
                    override fun test(lc: Plant.LifeCycle): Boolean {
                        return p.lifeCycle == lc
                    }
                }.test(lifeCycleCriteria)
            }
        }
        assertTrue(filterByLifeCycle4.test(plant))
    }

    /// 테이블 Plant
    /// |name|koName|lifeCycle|
    /// |----|------|---------|
    /// |Rose|장미|Perennial|
    /// |Tulip|튤립|Perennial|
    /// |Daisy|데이지|Biennial|
    /// |Lily|백합|Perennial|
    /// |Sunflower|해바라기|Annual|
    /// |Daffodil|수선화|Perennial|
    /// |Orchid|난초|Perennial|
    /// |Marigold|천수국|Annual|
    /// |Pansy|팬지|Biennial|
    /// |Chrysanthemum|국화|Biennial|
    /// |Petunia|피튜니아|Annual|
    private val plants: List<Plant> = listOf(
        Plant("Rose", "장미", Plant.LifeCycle.PERENNIAL),
        Plant("Tulip", "튤립", Plant.LifeCycle.PERENNIAL),
        Plant("Daisy", "데이지", Plant.LifeCycle.BIENNIAL),
        Plant("Lily", "백합", Plant.LifeCycle.PERENNIAL),
        Plant("Sunflower", "해바라기", Plant.LifeCycle.ANNUAL),
        Plant("Daffodil", "수선화", Plant.LifeCycle.PERENNIAL),
        Plant("Orchid", "난초", Plant.LifeCycle.PERENNIAL),
        Plant("Marigold", "천수국", Plant.LifeCycle.ANNUAL),
        Plant("Pansy", "팬지", Plant.LifeCycle.BIENNIAL),
        Plant("Chrysanthemum", "국화", Plant.LifeCycle.BIENNIAL),
        Plant("Petunia", "피튜니아", Plant.LifeCycle.ANNUAL),
        Plant("Zinnia", "백일홍", Plant.LifeCycle.ANNUAL)
    )

    @Test
    fun testProcedural() {
        val annual = Plant.LifeCycle.ANNUAL

        for (p in plants) {
            if (p.lifeCycle == annual) {
                println(p.name)
            }
        }
    }

    @Test
    fun testLambda() {
        val annual = Plant.LifeCycle.ANNUAL

        /*
         * SELECT name FROM plant;
         */

        plants.stream()
            .filter { it.lifeCycle == annual }
            .map { it.name }
            .forEach { println(it) }
    }

    @Test
    fun testLambdaUpper() {
        val annual = Plant.LifeCycle.ANNUAL

        /*
         * SELECT UPPER(name) FROM plant WHERE lifeCycle = 'annual';
         */

        plants.stream()
            .filter { it.lifeCycle == annual }
            .map { it.name.uppercase() }
            .forEach { println(it) }
    }

    @Test
    fun testLambdaSimultaneity() {

        /*
         * SELECT koName || '(' || name || ', ' || lifeCycle || ')' AS plant
         *   FROM plant
         *  ORDER BY koName;
         * SELECT lifeCycle, count(*) FROM plant GROUP BY lifeCycle;
         */

        plants
            .sorted()
            .map { "${it.koName}(${it.name}, ${it.lifeCycle})" }
            .forEach(::println)

        plants
            .groupingBy { it.lifeCycle }
            .eachCount()
            .toSortedMap()
            .map { "${it.key}: ${it.value}" }
            .forEach(::println)
    }

    @Test
    fun testLambdaSimultaneity2() {

        /*
         * WITH P AS (SELECT koName, name, lifeCycle FROM plant)
         * SELECT 'formattedList' type,
         *        koName || '(' || name || ', ' || lifeCycle || ')' data,
         *        null cnt
         *   FROM P
         * UNION ALL
         * SELECT 'countsMap' type,
         *        lifeCycle data,
         *        count(*) cnt
         *   FROM P
         *  GROUP BY lifeCycle;
         */

        data class Report(
            val formattedList: List<String>,
            val countsMap: Map<Plant.LifeCycle, Long>
        )

        val r = plants.stream()
            .sorted()
            .collect(
                Collectors.teeing(
                    Collectors.mapping(
                        { p -> "${p.koName}(${p.name}, ${p.lifeCycle})" },
                        Collectors.toList()
                    ),
                    Collectors.groupingBy(
                        { p -> p.lifeCycle },
                        { TreeMap<Plant.LifeCycle, Long>() },
                        Collectors.counting()
                    ),
                    { formattedList, countsMap ->
                        Report(formattedList, countsMap)
                    }
                )
            )

        r.formattedList.forEach { println(it) }
        r.countsMap.forEach { (k, v) -> println("$k: $v") }
    }

    @Test
    fun testLambdaSimultaneity3() {

        /*
         * CREATE PROCEDURE p_formattedListAndCountMap AS
         * BEGIN
         *     DECLARE @result TABLE(type VARCHAR(20),
         *                           data VARCHAR(100),
         *                           cnt  INT);
         *     DECLARE @countsMap TABLE(lifeCycle VARCHAR(20),
         *                              cnt INT);
         *     DECLARE @koName      VARCHAR(20);
         *     DECLARE @name        VARCHAR(20);
         *     DECLARE @lifeCycle   VARCHAR(20);
         *     DECLARE @formatted   VARCHAR(100);
         *
         *     CURSOR cur FOR
         *          SELECT koName, name, lifeCycle FROM plant;
         *
         *     OPEN cur;
         *     FETCH NEXT FROM cur INTO @koName, @name, @lifeCycle;
         *     WHILE @@FETCH_STATUS = 0
         *     BEGIN
         *         SET @formatted = @koName + '(' + @name + ', ' + @lifeCycle + ')';
         *         INSERT INTO @result VALUES('formattedList', @formatted, NULL);
         *
         *         MERGE @countsMap AS target
         *         USING (SELECT @lifeCycle AS lifeCycle) AS source
         *         ON (target.lifeCycle = source.lifeCycle)
         *         WHEN MATCHED THEN
         *            UPDATE SET target.cnt = target.cnt + 1
         *         WHEN NOT MATCHED THEN
         *            INSERT (lifeCycle, cnt) VALUES (source.lifeCycle, 1);
         *
         *         FETCH NEXT FROM cur INTO @koName, @name, @lifeCycle;
         *     END
         *     CLOSE cur;
         *     DEALLOCATE cur;
         *
         *     INSERT INTO @result
         *     SELECT 'countsMap' AS type, lifeCycle, cnt FROM @countsMap;
         *
         *     SELECT * FROM @result ORDER BY type, data;
         * END
         */

        class Report {
            val formattedList = mutableListOf<String>()
            val countsMap = TreeMap<Plant.LifeCycle, Int>()

            fun accumulate(p: Plant) {
                val lifeCycle = p.lifeCycle
                formattedList += "${p.koName}(${p.name}, $lifeCycle)" // List의 + 연산자는 새로운 리스트를 생성함
                countsMap.merge(lifeCycle, 1) { oldVal, newVal -> oldVal + newVal }
            }

            fun combine(other: Report) {
                formattedList += other.formattedList // List의 + 연산자는 새로운 리스트를 생성함
                other.countsMap.forEach { (k, v) ->
                    countsMap.merge(k, v) { oldVal, newVal -> oldVal + newVal }
                }
            }
        }

        val r = plants
            .sorted()
            .fold(Report()) { acc, p ->
                acc.accumulate(p)
                acc
            }

        r.formattedList.forEach { println(it) }
        r.countsMap.forEach { (k, v) -> println("$k: $v") }
    }
}
