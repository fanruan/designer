var arr ="%s".split(".").reverse();
var create = function (obj, names) {
    var name = names.pop();
    if (!name) {
        return;
    }
    if (!obj[name]) {
        obj[name] = {};
    }
    create(obj[name], names);
};
create(window, arr);