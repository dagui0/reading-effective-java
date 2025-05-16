
const { LifeCycle, Plant } = require('./Plant');

let plants = [
    Plant("Rose", "장미", LifeCycle.PERENNIAL),
    Plant("Tulip", "튤립", LifeCycle.PERENNIAL),
    Plant("Daisy", "데이지", LifeCycle.BIENNIAL),
    Plant("Lily", "백합", LifeCycle.PERENNIAL),
    Plant("Sunflower", "해바라기", LifeCycle.ANNUAL),
    Plant("Daffodil", "수선화", LifeCycle.PERENNIAL),
    Plant("Orchid", "난초", LifeCycle.PERENNIAL),
    Plant("Marigold", "천수국", LifeCycle.ANNUAL),
    Plant("Pansy", "팬지", LifeCycle.BIENNIAL),
    Plant("Chrysanthemum", "국화", LifeCycle.BIENNIAL),
    Plant("Petunia", "피튜니아", LifeCycle.ANNUAL),
    Plant("Zinnia", "백일홍", LifeCycle.ANNUAL)
]

describe('Plant exmple test', () => {

    test("testProcedural", () => {

        let annual = LifeCycle.ANNUAL;

        /*
         * SELECT name FROM plant WHERE lifeCycle = 'annual';
         */

        for (let p of plants) {
            // 1. lifeCycle이 annual인 Plant 필터링
            if (p.lifeCycle() == annual) {
                // 2. Plant 객체에서 name(영문 이름) 추출
                let name = p.name();
                // 3. 문자열 출력
                console.log(name);
            }
        }
    });

    test("testLambda", () => {

        let annual = LifeCycle.ANNUAL;

        /*
         * SELECT name FROM plant WHERE lifeCycle = 'annual';
         */

        plants
            .filter(p => p.lifeCycle() == annual)
            .map(p => p.name())
            .forEach(n => console.log(n));
    });

    test("testLambdaCurring", () => {

        const plant = Plant("Sunflower", "해바라기", LifeCycle.ANNUAL);
        const lifeCycleCriteria = LifeCycle.ANNUAL;

        /*
         * BiPredicate<T, U>: (T, U) -> boolean
         * - T: Type
         * - U: Secondary Type
         */

        // $ (λ(p, lc). p.lifeCycle() == lc) (plant, lifeCycleCriteria) $
        let filterByLifeCycle = (p, lc) => p.lifeCycle() == lc;

        expect(filterByLifeCycle(plant, lifeCycleCriteria))
            .toBe(true);

        let filterByLifeCycle2 = function(p, lc) {
            return p.lifeCycle() == lc;
        };

        expect(filterByLifeCycle2(plant, lifeCycleCriteria))
            .toBe(true);

        /*
         * Predicate<T>: (T) -> boolean
         * - T: Type
         */

        // $ (λp. (λlc. p.lifeCycle() == lc) lifeCycleCriteria )  plant $
        let filterByLifeCycle3 =
            (p) => (lc => p.lifeCycle() == lc)(lifeCycleCriteria);

        expect(filterByLifeCycle3(plant))
            .toBe(true);

        let filterByLifeCycle4 = function(p) {
            return (function(lc) {
                return p.lifeCycle() == lc;
            })(lifeCycleCriteria);
        };

        expect(filterByLifeCycle4(plant))
            .toBe(true);

    });

    test("testLambdaSimultaneity", () => {

        /*
         * SELECT koName || '(' || name || ', ' || lifeCycle || ')' AS plant
         *   FROM plant
         *  ORDER BY koName;
         * SELECT lifeCycle, count(*) FROM plant GROUP BY lifeCycle;
         */

        plants
            .sort((p1, p2) => p1.koName().localeCompare(p2.koName()))
            .map(p => `${p.koName()}(${p.name()}, ${p.lifeCycle()})`)
            .forEach(s => console.log(s));

        Object.entries(plants
            .reduce((acc, p) => {
                acc[p.lifeCycle] = (acc[p.lifeCycle] || 0) + 1;
                return acc;
            }, {}))
            .forEach(([k, v]) => console.log(k + ": " + v));
    });

    test("testLambdaSimultaneity2", () => {

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

        const report = {};
        plants
            .sort((p1, p2) => p1.koName().localeCompare(p2.koName()))
            .map(p => {
                if (p.lifeCycle() in report)
                    report[p.lifeCycle()]++;
                else
                    report[p.lifeCycle()] = 1;
                return p;
            })
            .map(p => `${p.koName()}(${p.name()}, ${p.lifeCycle()})`)
            .forEach(s => console.log(s));

        Object.entries(report)
            .forEach(([k, v]) => console.log(k + ": " + v));
    });

    test("testLambdaSimultaneity2_1", () => {

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

        let formattedList = [];
        let countsMap = {};
        for (const p of plants.sort(
            (p1, p2) => p1.koName().localeCompare(p2.koName()))) {
            formattedList.push(`${p.koName()}(${p.name()}, ${p.lifeCycle()})`);

            const lifeCycle = p.lifeCycle();
            countsMap[lifeCycle] = (countsMap[lifeCycle] || 0) + 1;
        }

        formattedList.forEach(s => console.log(s));
        Object.entries(countsMap)
            .forEach(([k, v]) => console.log(k + ": " + v));
    });

    test("testLambdaSimultaneity3", () => {

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

        let report = plants
            .sort((p1, p2) => p1.koName().localeCompare(p2.koName()))
            .reduce((acc, p) => {
                acc.formattedList.push(`${p.koName()}(${p.name()}, ${p.lifeCycle()})`);
                acc.countsMap[p.lifeCycle] = (acc.countsMap[p.lifeCycle] || 0) + 1;
                return acc;
            }, {
                formattedList: [],
                countsMap: {}
            });

        report.formattedList.forEach(s => console.log(s));
        Object.entries(report.countsMap)
            .forEach(([k, v]) => console.log(k + ": " + v));
    });

});
