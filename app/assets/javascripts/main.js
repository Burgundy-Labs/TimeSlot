$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover({html: true});
});

function createAlert(alertType, messageHTML) {
    $("<div></div>").appendTo('.page-content').addClass('alert alert-link alert-dismissible fade show alert-' + alertType)
        .html(
            "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>\n" +
            "<span aria-hidden='true'>&times;</span>\n" +
            "</button>" +
            '<div>' + messageHTML + '</div>'
        ).fadeTo(2000, 500).slideUp(500);
}