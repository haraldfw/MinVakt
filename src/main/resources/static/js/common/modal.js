/**
 * Created by Ingunn on 11.01.2017.
 */
$(document).ready(function() {
   /* $("#inputFra, #inputTil") //fra og til dato
        .datepicker({
            format: 'dd/mm/yyyy',
            startDate: '01/01/2010',
            endDate: '30/12/2020'
        });*/ //TODO: brukes dette?
    /*.on('changeDate', function(e) {
     //fiks tilbakemelding?
     // Revalidate the date field
     $('#inputFra, #inputTil').formValidation('revalidateField', 'date');*/


    $("#chooseTime /*#chooseTimeText*/").click(function() {
        if($(this).is(':checked')) {
            $("#selectTime, #selectTime2").css("display", "inline");
        } else {
            $("#selectTime, #selectTime2").css("display", "none");
        }
    });

    $("#oneDay").click( function() {
        if($(this).is(':checked')) {
            $("#tilDato").css("display", "none");
            $("#fraDatoText").text("Dato");
        } else {
            $("#tilDato").css("display", "inline");
            $("#fraDatoText").text("Fra dato");
        }

    });


    $(".dropdown-menu li a").click(function(){

        $(this).parents(".btn-group").find('.selection').text($(this).text());
        $(this).parents(".btn-group").find('.selection').val($(this).text());

    });



    //Legg til fravær
    $("#go").click(function() {
        //TODO: legg inn error hvis fortid
        if(!($("#absenceCause").html() === "&nbsp;Fraværsgrunn&nbsp;")) {
            var fraTid = $("#timeFra").html().split("&nbsp;")[0] + $("#minFra").html().split("&nbsp;")[0];
            var tilTid = $("#timeTil").html().split("&nbsp;")[0] + $("#minTil").html().split("&nbsp;")[0];

            if (!($("#oneDay").is(":checked"))) {
                if ($("#fraText").val() === "" || $("#tilText").val() === "") {
                    alert("Velg dato");
                } else {

                    var fra = $("#fraText").val();
                    var nr = fra.split("/");
                    var month = (nr[1] - 1);
                    var startDato = new Date(nr[2], month, nr[0]);

                    var til = $("#tilText").val();
                    var nr2 = til.split("/");
                    var month2 = (nr2[1] - 1);
                    var sluttDato = new Date(nr2[2], month2, nr2[0]);

                    if (startDato > sluttDato) {
                        alert("sluttdato kan ikke være før startdato")
                    } else {
                        if ($("#chooseTime").is(":checked")) {
                            if ($("#timeFra").html() === "&nbsp;Time&nbsp;" || $("#timeTil").html() === "&nbsp;Time&nbsp;" || $("#minFra").html() === "&nbsp;Min&nbsp;" || $("#minTil").html() === "&nbsp;Min&nbsp;") {
                                alert("Velg tid checkbox er checked, husk å velg tid");
                            } else {
                                if (startDato.getTime() === sluttDato.getTime()) {
                                    if (fraTid > tilTid) {
                                        alert("Du kan ikke velge starttidspunkt etter sluttidspunkt");
                                    } else {
                                        $("#absenceModal").modal("hide");

                                    }

                                } else {
                                    $("#absenceModal").modal("hide");

                                }
                            }
                        } else {
                            $("#absenceModal").modal("hide");
                        }
                    }
                }
            } else {
                if ($("#fraText").val() === "") {
                    alert("Velg dato");
                } else {

                    if ($("#chooseTime").is(":checked")) {
                        if ($("#timeFra").html() === "&nbsp;Time&nbsp;" || $("#timeTil").html() === "&nbsp;Time&nbsp;" || $("#minFra").html() === "&nbsp;Min&nbsp;" || $("#minTil").html() === "&nbsp;Min&nbsp;") {
                            alert("Velg tid checkbox er checked, husk å velg tid");
                        } else {

                            if (fraTid > tilTid) {
                                alert("Du kan ikke velge fra tid etter til tid");
                            } else {
                                $("#absenceModal").modal("hide");
                            }
                        }
                    } else {
                        $("#absenceModal").modal("hide");
                    }
                }
            }
        } else {
            alert("legg til fraværsgrunn");
        }
        //TODO: legg til bedre alert
        //TODO: legg til tootip

    });

});
/*$("#chooseTimeText").click(function() {

 if($("#chooseTime").is(':checked')) {
 $("#chooseTime").prop("checked", false);
 $("#selectTime").css("display", "inline");
 } else {
 $("#chooseTime").prop("checked", true);
 $("#selectTime").css("display", "none");
 }
 });*/

