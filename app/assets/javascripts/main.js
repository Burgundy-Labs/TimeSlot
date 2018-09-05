$(function () {
    $("[data-toggle='tooltip']").tooltip();
    $("[data-toggle='popover']").popover({html: true});
});

function createAlert(alertType, messageHTML) {
    $("<div></div>").appendTo("body").addClass("alert alert-link alert-dismissible fade show alert-" + alertType)
        .html(
            "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>\n" +
            "<span aria-hidden='true'>&times;</span>\n" +
            "</button>" +
            "<div>" + messageHTML + "</div>"
        ).fadeTo(2000, 500).slideUp(500);
}

function appointmentDetailPopup(appointmentId) {
    let appointment;
    $.ajax({
        type: "GET",
        url: `/getAppointmentById/${appointmentId}`,
        success: function (response) {
            appointment = response;
        }
    });
    $.alert({
        theme: 'modern',
        type: "dark",
        escapeKey: true,
        backgroundDismiss: true,
        title: "Appointment Details",
        columnClass: "col-md-8",
        content: `<div class="row col-md-12">
                              <div class="col-md-4">
                              <b>Coach</b>
                              <br/>
                              <img src="${appointment.coachPhoto}" class="userAvatar"/>
                              <br/>
                              <i class="material-icons text-primary md-18">email</i> <a href="mailto:${appointment.coachEmail}">${appointment.coachName}</a>
                              </div>
                              ${appointment.studentId == null
            ? `<div class="col-md-4"><i class="material-icons md-64 text-warning">help</i>
                                   <br/>
                                   <small>No appointment scheduled.</small>
                                   </div>
                                   <div class="col-md-4">
                                   <b>Student</b>
                                   <br/>
                                   <i class="material-icons md-64">face</i>
                                   <br/>
                                   <i class="material-icons text-primary md-18">unsubscribe</i> No student yet
                                   </div>`
            : `<div class="col-md-4"><i class="material-icons md-64 text-success">check_circle</i>
                                   <br/>
                                   <small>An appointment is scheduled.</small>
                                   </div>
                                   <div class="col-md-4">
                                   <b>Student</b>
                                   <br/>
                                   <img src="${appointment.studentPhoto}" class="userAvatar"/>
                                   <br/>
                                   <i class="material-icons text-primary md-18">email</i> <a href="maito:${appointment.studentEmail}">${appointment.studentName}</a>
                                   </div>`
            }
                              <div class="col-md-12">&nbsp;</div>
                              <div class="appointmentSummary col-md-12 text-left">
                              ${moment(appointment.startDate).format("dddd, MMMM Do")}: <b>${moment(appointment.startDate).format("hh:mm")} - ${moment(appointment.endDate).format("hh:mm A")}</b>
                              <br/>
                              ${appointment.studentId == null
            ? `This appointment has not yet been taken.`
            : `Service: ${appointment.serviceType}
                                   <br/>
                                   Appointment Type: ${appointment.appointmentType}
                                   <br/>
                                   Weekly: ${appointment.weekly}
                                   </div>`
            }
                              </div>`
    });
}

