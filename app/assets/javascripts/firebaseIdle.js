var idleTime = 0;

$(document).ready(function () {
    if(sessionStorage.getItem("timeoutDisabled") === null){
        sessionStorage.setItem("timeoutDisabled", false);
    }
    cheet('↑ ↑ ↓ ↓ ← → ← →', function () {
        sessionStorage.setItem("timeoutDisabled", true);
        createAlert("warning", "Timeout has been disabled.");
    });
    setInterval(timerIncrement, 60000); // 1 minute
    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });
});

function timerIncrement() {
    idleTime = idleTime + 1;
    if (idleTime === 2 && Boolean(sessionStorage.getItem("timeoutDisabled"))) { // 20 minutes
        if (window.location.pathname !== "/Login") {
            alert('You have been signed out due to inactivity.');
            $('#sign-out').click();
        }
    }
}