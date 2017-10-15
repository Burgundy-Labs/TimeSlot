initApp = function() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            // User is signed in.
            document.getElementById('sign-out').textContent = 'Sign out';
        } else {
            // User is signed out.
            disableLinks();
            if(window.location.pathname !== "/Login"){
                console.log("Redirect");
                window.location='/Login';
            }
        }
    }, function(error) {
        console.log(error);
    });
};

window.addEventListener('load', function() {
    initApp();
    document.getElementById('sign-out').addEventListener('click', function() {
        firebase.auth().signOut();
    });
});

function disableLinks() {
    $("a[data-placement='right']").attr('href', '/Login');
}