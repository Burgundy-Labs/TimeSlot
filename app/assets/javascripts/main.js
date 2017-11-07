$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});

function createAlert(alertType, messageHTML) {
    $("<div></div>").appendTo('.page-content').addClass('alert alert-link alert-dismissible fade show alert-' + alertType)
        .html(
        "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>\n" +
        "<span aria-hidden='true'>&times;</span>\n" +
        "</button>" +
        '<div>' + messageHTML + '</div>'

    ).alert();
}

function createModal(modalID, modalContent, buttonText, buttonFunction) {
    if ($('#'+modalID).length === 0) {
        $(' <div class="modal fade" id="'+modalID+'" tabindex="-1" role="dialog" aria-labelledby="'+modalID+'-label" aria-hidden="true">\n' +
            '        <div class="modal-dialog" role="document">\n' +
            '        <div class="modal-content">\n' +
            '        <div class="modal-header">\n' +
            '        <h5 class="modal-title" id="'+modalID+'-label">Modal title</h5>\n' +
            '    <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
            '        <span aria-hidden="true">&times;</span>\n' +
            '    </button>\n' +
            '    </div>\n' +
            '    <div class="modal-body">\n'
            + modalContent +
            '    </div>\n' +
            '    <div class="modal-footer">\n' +
            '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>\n' +
            '        <button type="button" onclick="' + buttonFunction + '" class="btn btn-primary">' + buttonText + '</button>\n' +
            '    </div>\n' +
            '    </div>\n' +
            '    </div>\n' +
            '    </div>\n'
        ).appendTo('.page-content');
    }
}