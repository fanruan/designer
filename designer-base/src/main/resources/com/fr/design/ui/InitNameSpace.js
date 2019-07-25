let arr ="%s".split(".").reverse();
let create = function (obj, names) {
    let name = names.pop();
    if (!name) {
        return;
    }
    if (!obj[name]) {
        obj[name] = {};
    }
    create(obj[name], names);
};
create(window, arr);