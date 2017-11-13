var idleTime = 0;

$(document).ready(function () {
    if(localStorage.getItem("timeoutDisabled") === null){
        localStorage.setItem("timeoutDisabled", false);
    }
    cheet('↑ ↑ ↓ ↓ ← → ← →', function () {
        localStorage.setItem("timeoutDisabled", true);
        createAlert("info", "Timeout has been disabled.");
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
    if (idleTime === 2 && Boolean(localStorage.getItem("timeoutDisabled"))) { // 20 minutes
        if (window.location.pathname !== "/Login") {
            alert('You have been signed out due to inactivity.');
            $('#sign-out').click();
        }
    }
}