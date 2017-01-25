function getCurrentDateTime() {
    return (new Date()).toJSON();
}
function punchIn(form) {
    form.elements["start_time"].value = getCurrentDateTime();
    return jsonSubmit(form, '/timebank');
}
function punchOut(form) {
    form.elements["end_time"].value = getCurrentDateTime();
    return jsonSubmit(form, '/timebank');
}