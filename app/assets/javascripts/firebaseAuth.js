initApp = function () {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.
            if (window.location.pathname === "/Login") {
                window.location = '/Dashboard';
            }
            $('#sign-out').show();

        } else {
            // User is signed out.
            $('#sign-out').hide();
            disableLinks();
            switch(window.location.pathname){
                case "/Login":
                case "/Terms":
                case "/Help":
                    break;
                default:
                    window.location = '/Login';
            }
        }
    }, function (error) {
        console.log(error);
    });
};

window.addEventListener('load', function () {
    initApp();
    document.getElementById('sign-out').addEventListener('click', function () {
        firebase.auth().signOut().then(function() {
            sessionStorage.clear();
            localStorage.removeItem('firebaseui::rememberedAccounts');
        }).catch(function(error) {
            console.log(error);
        });
    });
});

function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}