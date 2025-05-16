
const Person = require('./Person');

describe('Person example test', () => {

    test("testPrivateMember", () => {
        let person = Person("John Doe", 30);

        expect(person.name()).toBe("John Doe");
        expect(person.age()).toBe(30);

        person.setName("Jane Doe");
        person.setAge(25);

        expect(person.name()).toBe("Jane Doe");
        expect(person.age()).toBe(25);

        // modifying getters is not allowed
        person.name = "New Name"; // This won't change the name
        expect(person.name()).toBe("Jane Doe");

        // creating a new property is allowed
        person.a = 'A';
        expect(person.a).toBe("A");
    });

});
