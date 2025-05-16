
const LifeCycle = {
    ANNUAL: '한해살이',
    BIENNIAL: '두해살이',
    PERENNIAL: '여러해살이',
};
Object.freeze(LifeCycle);

function Plant(name, koName, lifeCycle) {
    let _name = name;
    let _koName = koName;
    let _lifeCycle = lifeCycle;

    const plantInstance = {};
    Object.defineProperties(plantInstance, {
        name: {
            value: () => _name,
            enumerable: true,
            configurable: false
        },
        koName: {
            value: () => _koName,
            enumerable: true,
            configurable: false
        },
        lifeCycle: {
            value: () => _lifeCycle,
            enumerable: true,
            configurable: false
        },
    });

    return plantInstance;
}

module.exports = {
    LifeCycle,
    Plant,
};
