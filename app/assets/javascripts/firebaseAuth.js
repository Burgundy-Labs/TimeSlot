initApp = function () {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            // User is signed in.
            $('#sign-out').show();
        } else {
            // User is signed out.
            $('#sign-out').hide();
            disableLinks();
        }
    }, function (error) {
        console.log(error);
    });
};

window.addEventListener('load', function () {
    initApp();
    if(document.getElementById('sign-out')) {
        document.getElementById('sign-out').addEventListener('click', function () {
            signout();
        });
    }
});


function signOut() {
    firebase.auth().signOut().then(function() {
        sessionStorage.clear();
        localStorage.clear();
        window.location="/Logout";
    }).catch(function(error) {
        console.log(error);
    });
}

function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}