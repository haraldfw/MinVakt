/**
 * Created by Kenan on 1/17/2017.
 */
$(document).ready(() => {
    console.log("DP init");
    datepickers($('#inputFrom'), $('#inputTil'));

    // $("#availability").form(json => {
    //     console.log(json)
    // })
});

const fmt = "YYYY-MM-DD HH:mm";

function datepickers(from, to){
    console.log("DP set");
    from.datetimepicker({
        // locale: 'no',
        format: fmt,
        // pick12HourFormat: false
    });
    to.datetimepicker({
        // locale: 'no',
        format: fmt,
        useCurrent: false,
        // pick12HourFormat: false
    });

    from.on("dp.change", function (e) {
        to.data("DateTimePicker").minDate(e.date);
    });

    to.on("dp.change", function (e) {
        from.data("DateTimePicker").maxDate(e.date);
    });
}

function dateProcces(json) {
    json.date_from = moment(json.date_from, fmt).toJSON();
    json.date_to   = moment(json.date_to, fmt).toJSON();
    return json;
}