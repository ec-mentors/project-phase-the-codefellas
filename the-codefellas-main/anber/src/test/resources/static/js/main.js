function setMenuEntryActive() {
    const targetLink = document.querySelector("a[href='" + window.location.pathname + "']");
    targetLink.classList.add("active");
}

window.onload = function() {
    setMenuEntryActive();
}
