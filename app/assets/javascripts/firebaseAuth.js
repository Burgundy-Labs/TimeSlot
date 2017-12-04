initApp = function () {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.

            /* Make sure they're in the application - as defined by sidebar being displayed */
            if ($('.sidebar').length) {
                if (window.location.pathname === "/Login" || window.location.pathname === "/") {
                    window.location.href = '/Dashboard';
                }
                $('[data-original-title="Account"]').html('<img src="' + firebase.auth().currentUser.photoURL + '" class="userAvatar"/>');
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
        window.location.href ='/Logout';
    }).catch(function(error) {
        console.log(error);
    });
}

function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}