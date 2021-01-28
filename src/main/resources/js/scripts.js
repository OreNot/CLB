function openNewWin(url) {
    myWin = open(url);
}


function openLink(ev, link) {
    window.open(link);
    if (ev.cancelable) {
        ev.preventDefault();
    }

}
