var arr = "%s".split(",");
var header = document.getElementsByTagName("head")[0];
arr.forEach(function(el) {
    var css = document.createElement("link");
    css.type = "text/css";
    css.rel = "stylesheet";
    css.href = "emb:" + el;
    header.appendChild(css);
});