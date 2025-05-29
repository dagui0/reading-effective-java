
const Person = require('./Person');

describe('Person example test', () => {

    test("testPrivateMember", () => {
        let person = Person("John Doe", 30);

        expect(person.name).toBe("John Doe");
        expect(person.age).toBe(30);

        person.name = "Jane Doe";
        person.age = 25;

        expect(person.name).toBe("Jane Doe");
        expect(person.age).toBe(25);

        // isAdult 메서드 재정의 시도
        expect(person.isAdult()).toBe(true);

        person.isAdult = () => false;
        expect(person.isAdult()).toBe(true);

        // creating a new property is allowed
        person.a = 'A';
        expect(person.a).toBe("A");

        expect(person.isAdult()).toBe(true);
    });

});
