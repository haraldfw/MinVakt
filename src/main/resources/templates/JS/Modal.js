/**
 * Created by Ingunn on 11.01.2017.
 */
$(document).ready(function() {
    $("#inputFra, #inputTil") //fra og til dato
        .datepicker({
            format: 'dd/mm/yyyy',
            startDate: '01/01/2010',
            endDate: '30/12/2020'
        });
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


    $("#go").click(function() {
        if(!($("#oneDay").is(":checked"))) {
            var fra = $("#fraText").val();
            var nr = fra.split("/");
            var month = (nr[1]-1);
            var startDato = new Date(nr[2], month, nr[0]);
            alert(startDato.toString());

            var til = $("#tilText").val();
            var nr2 = til.split("/");
            var month2 = (nr2[1]-1);
            alert(nr2[0] + " , " + month2 + " , " + nr2[2])
            var sluttDato = new Date(nr2[2], month2, nr2[0]);

            if(startDato > sluttDato) {
                alert("sluttdato kan ikke være før startdato")
            } else {
                if($("#chooseTime").is(":checked")) {
                    var fraTime = $("#timeFra").html().split("&nbsp;")[0];
                    var fraMin;
                    var tilTime;
                    var tilMin;

                    //TODO: alert= [Object object]
                    alert("startTid: " + fraTime);

                }
                $("#myModal").modal("hide");
            }
        } else {
            alert("test");
            $("#myModal").modal("hide");
        }



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

