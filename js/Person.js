let Person = function(name, age) {

    let _name = name || "John Doe"; // 클로저를 통해 private 상태 유지
    let _age = age;

    const personInstance = {
        home: "Seoul",      // public 속성
                            // delete person['home'] 같이 삭제도 가능
    };
    Object.defineProperties(personInstance, {
        isAdult: {
            // alice.isAdult() 형태로 접근 가능
            value: () => _age >= 18,
            enumerable: false,   // for (let prop in person) {} 같이
                                 // 순회할때 포함되지 않음
            configurable: false  // 프로퍼티를 read-only로 만듬
        },
        name:   {
            // alice.name = "Alice" 형태로 접근 가능
            get: () => _name,
            set: (newName) => { _name = newName;},
            enumerable: true,
            configurable: false
        },
        age:    {
            // alice.age = 30 형태로 접근 가능
            get: () => _age,
            set: (newAge) => { _age = newAge; },
            enumerable: true,
            configurable: false
        },
    });
    return personInstance;
}
module.exports = Person
