/**
 * Created by Ingunn on 11.01.2017.
 */
$(document).ready(function() {
    $("#inputFra, #inputTil") //fra og til dato
        .datepicker({
            format: 'dd/mm/yyyy',
            startDate: '01/01/2010',
            endDate: '30/12/2020'
        })
        .on('changeDate', function(e) {
            //fiks tilbakemelding?
            // Revalidate the date field
            $('#inputFra, #inputTil').formValidation('revalidateField', 'date');
        });

    $("#chooseTime /*#chooseTimeText*/").click(function() {
        if($(this).is(':checked')) {
            $("#selectTime").css("display", "inline");
        } else {
            $("#selectTime").css("display", "none");
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


    $("")

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

