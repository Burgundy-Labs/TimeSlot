var idleTime = 0;

function startTimeout() {
    if(localStorage.getItem("timeoutDisabled") === null){
        localStorage.setItem("timeoutDisabled", false);
    }
    cheet('↑ ↑ ↓ ↓ ← → ← →', function () {
        localStorage.setItem("timeoutDisabled", true);
        createAlert("info", "Timeout has been disabled.");
    });
    setInterval(timerIncrement, 500000);
    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });
}

function timerIncrement() {
    idleTime = idleTime + 1;
    if (idleTime === 2 && Boolean(localStorage.getItem("timeoutDisabled"))) {
        if (window.location.pathname !== "/Login") {
            $.alert({
                title: 'Alert',
                content: 'You have been signed out due to inactivity.',
                buttons: {
                    confirm: {
                        text: "Okay",
                        btnClass: "btn-primary",
                        action: function() {
                            $('#sign-out').click();
                        }
                    }
            }
            });
        }
    }
}