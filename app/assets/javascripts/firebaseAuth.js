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
    document.getElementById('sign-out').addEventListener('click', function () {
       signout();
    });
});


function signout() {
    firebase.auth().signOut().then(function() {
        $.get( "/Logout", function( data ) {});
        sessionStorage.clear();
        localStorage.clear();
    }).catch(function(error) {
        console.log(error);
    });
}

function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}