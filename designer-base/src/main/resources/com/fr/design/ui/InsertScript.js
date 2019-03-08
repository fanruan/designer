var arr = "%s".split(",");
var header = document.getElementsByTagName("head")[0];
arr.forEach(function(el) {
    var script = document.createElement("script")
    script.type = "text/javascript";
    script.src = "emb:" + el;
    header.appendChild(script);
});