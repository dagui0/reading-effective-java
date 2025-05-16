
function Person(name, age) {

    let _name = name; // 클로저를 통해 private 상태 유지
    let _age = age;

    const personInstance = {};
    Object.defineProperties(personInstance, {
        name:   {
            value: () => _name,
            enumerable: true,
            configurable: false
        },
        setName:{
            value: (newName) => { _name = newName;},
            enumerable: false,
            configurable: false
        },
        age:    {
            value: () => _age,
            enumerable: true,
            configurable: false
        },
        setAge: {
            value: (newAge) => { _age = newAge; },
            enumerable: false,
            configurable: false
        },
    });

    return personInstance;
}
module.exports = Person
