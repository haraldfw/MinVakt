/**
 * Created by alan on 17/01/2017.
 */
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function jsonSubmit(form, redirectUrl, actionUrl) {
    $.ajax({
        type: "post",
        url: actionUrl,
        data: JSON.stringify($(form).serializeObject()),
        contentType: "application/json; charset=utf-8",
        success: function () {
            window.location = redirectUrl
        }
    }).fail(err => {
        console.error(err);
    });
    // prevent form default action
    return false;
}
