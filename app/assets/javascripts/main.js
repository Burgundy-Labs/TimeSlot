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

function zonedDate(m) {
    console.log(moment(m).format() + "    " + moment(m).isDST());
    if ((!moment().isDST() && moment(m).isDST())) {
        return moment(m).subtract(1, 'hour').tz("America/Detroit");
    } else {
        if ((moment().isDST() && !moment(m).isDST())) {
            return moment(m).add(1, 'hour').tz("America/Detroit");
        } else {
            return moment(m).tz("America/New_York");
        }
    }
}

