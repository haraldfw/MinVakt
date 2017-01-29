/**
 * Created by Kenan on 1/17/2017.
 */
$(document).ready(() => {
    datepickers($('#inputFrom'), $('#inputTil'));

    // $("#availability").form(json => {
    //     console.log(json)
    // })
});

const fmt = "YYYY-MM-DD HH:mm";

function datepickers(from, to){
    from.datetimepicker({
        // locale: 'no',
        format: fmt,
        sideBySide: true,
    });
    to.datetimepicker({
        // locale: 'no',
        format: fmt,
        sideBySide: true,
        useCurrent: false,
    });

    from.on("dp.change", function (e) {
        to.data("DateTimePicker").minDate(e.date);
    });

    to.on("dp.change", function (e) {
        from.data("DateTimePicker").maxDate(e.date);
    });
}

function dateProcces(fields) {
    return function (json) {
        for (field of fields) {
            json[field] = moment(json[field], fmt).toJSON();
        }

        return json;
    }
}
