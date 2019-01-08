$(function () {
    $("[data-toggle='tooltip']").tooltip();
    $("[data-toggle='popover']").popover({html: true});
    $(function() {
        $('a[role="tab"]').on('click', function(e) {
            window.localStorage.setItem('activeTab', $(e.target).attr('href'));
        });
        let activeTab = window.localStorage.getItem('activeTab');
        if (activeTab) {
            $('a[href="' + activeTab + '"]').tab('show');
            window.localStorage.removeItem("activeTab");
        }
    });
});

function createAlert(alertType, messageHTML) {
    $("<div></div>").appendTo("body").addClass("alertPopup alert alert-link alert-dismissible fade show alert-" + alertType)
        .html(
            "<button type='button' class='close' data-dismiss='alert' aria-label='Close'>\n" +
            "<span aria-hidden='true'>&times;</span>\n" +
            "</button>" +
            "<div>" + messageHTML + "</div>"
        ).fadeTo(2000, 500).slideUp(500);
}


function appointmentDetailPopup(userId, appointmentId) {
    $.ajax({
        type: "GET",
        url: `/getAppointmentById/${userId}/${appointmentId}`,
        success: function (response) {
            let appointment = response;
            $.alert({
                theme: 'modern',
                type: "dark",
                escapeKey: true,
                backgroundDismiss: true,
                title: "Appointment Details",
                columnClass: "col-md-8",
                buttons: {
                    cancelAppointment: {
                        text: "Cancel Appointment",
                        btnClass: "btn-danger",
                        action: function() {
                            $.alert({
                                theme: 'modern',
                                type: 'dark',
                                escapeKey: true,
                                backgroundDismiss: true,
                                title: "Cancel Notes",
                                columnClass: "col-md-8",
                                content: 'Reason for Cancelling:<textarea class="form-control" rows="5" id="cancelNotes" placeholder="I can\'t come to the appointment anymore because..."></textarea>',
                                buttons: {
                                    confirm: {
                                        text: "Cancel Appointment",
                                        btnClass: "btn-primary",
                                        action: function() {
                                            $.ajax({
                                                url: '/cancelAppointment',
                                                type: 'POST',
                                                data: JSON.stringify({appointmentId: appointment.appointmentId, cancelNotes: $(cancelNotes).val()}),
                                                contentType: 'application/json',
                                                success: function () {
                                                    setTimeout(function () {
                                                        createAlert("success", "Appointment Canceled!");
                                                    }, 50);
                                                    location.reload();
                                                },
                                                error: function (err) {
                                                    createAlert("danger", "Appointment Cancelling Failed!");
                                                }
                                            });
                                        }
                                    },
                                    cancel: {
                                        text: "Go Back",
                                        btnClass: "btn-primary"
                                    }
                                }
                            });
                        }
                    },
                    removeAvailability: {
                        text: "Remove Availability",
                        btnClass: "btn-danger",
                        action: function() {
                            $.ajax({
                                url: '/removeAppointment',
                                type: 'POST',
                                data: JSON.stringify({appointmentId: appointment.appointmentId, removeAll: false}),
                                contentType: 'application/json',
                                success: function () {
                                    setTimeout(function () {
                                        createAlert("success", "Availability Removed!");
                                        location.reload();
                                    }, 50);
                                },
                                error: function (err) {
                                    createAlert("danger", "Availability Removing Failed!");
                                }
                            });
                        }
                    },
                    ok: {
                        text: "Go Back",
                        btnClass: "btn-primary"
                    }
                },
                onOpenBefore: function() {
                    if (appointment.studentId == null) {
                        this.buttons.cancelAppointment.hide();
                    } else {
                        this.buttons.removeAvailability.hide();
                    }
                },
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
    });

}



