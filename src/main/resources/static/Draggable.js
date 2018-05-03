

let dragMoveListener = window.dragMoveListener = function (e) {
    let target = e.target,
        x = (parseFloat(target.getAttribute('data-x')) || 0) + e.dx,
        y = (parseFloat(target.getAttribute('data-y')) || 0) + e.dy;

    target.style.zIndex = 1000; // bring dragged element to front

    target.style.webkitTransform =
        target.style.transform =
            `translate(${x}px, ${y}px)`;

    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
};

let add = function (node){
    node.appendTo('.roundTable');
}

let cleanNode = function (node) {
    node.removeAttribute('data-x');
    node.removeAttribute('data-y');
    node.removeAttribute('style');
    return node;
};

let handleEnd = function (e) {
    if (e.target.classList.contains('blueprint')) {
        let nodeCopy = cleanNode(e.target.cloneNode(true)); // copy node and remove attributes
        e.target.parentNode.insertBefore(nodeCopy, e.target);
        e.target.classList.remove('blueprint');
    }

    e.target.style.zIndex = 1; // reset bring to front
};

interact('.draggable').draggable({
    restrict: {
        restriction: "body" // let object be dropped within the entire body
    },
    onmove: dragMoveListener,
    onend: handleEnd
});
  
