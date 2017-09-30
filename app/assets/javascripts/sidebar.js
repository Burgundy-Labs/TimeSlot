$(function() {
    $("#slide-sidebar").on('click', function() {
        var sidebar = $(".sidebar");
            if(sidebar.hasClass("miniSidebar")) {
                $(this).html( $(this).html().replace("last","first") );
                sidebar.removeClass("miniSidebar");
            } else{
                $(this).html( $(this).html().replace("first","last") );
                sidebar.addClass("miniSidebar");
            }
        })
    });
