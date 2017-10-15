var idleTime = 0;
$(document).ready(function () {
    var idleInterval = setInterval(timerIncrement, 60000); // 1 minute
    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });
});

function timerIncrement() {
    idleTime = idleTime + 1;
    if (idleTime === 1) { // 20 minutes
        if (window.location.pathname !== "/Login") {
            alert('You have been signed out due to inactivity.');
            $('#sign-out').click();
        }
    }
}