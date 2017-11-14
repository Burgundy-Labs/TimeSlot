initApp = function () {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.
            if (window.location.pathname === "/Login" || window.location.pathname === "/") {
                window.location.href = '/Dashboard';
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
                    window.location.href = '/Login';
            }
        }
    }, function (error) {
        console.log(error);
    });
};

window.addEventListener('load', function () {
    initApp();
    document.getElementById('sign-out').addEventListener('click', function () {
       signout();
    });
});


function signout() {
    firebase.auth().signOut().then(function() {
        sessionStorage.clear();
        localStorage.clear();
        window.location = "/Logout";
    }).catch(function(error) {
        console.log(error);
    });
}
function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}