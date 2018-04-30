var coll = document.getElementsByClassName("colTest");
for (var i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function () {
        this.classList.toggle("active");
        var colTestEdit = this.nextElementSibling;
        if (colTestEdit.style.maxHeight) {
            colTestEdit.style.maxHeight = null;
        } else {
            colTestEdit.style.maxHeight = colTestEdit.scrollHeight + "px";
        }
    });
}
