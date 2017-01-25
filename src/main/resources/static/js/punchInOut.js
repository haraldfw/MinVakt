function getCurrentDateTime() {
    return (new Date()).toJSON();
}
function punchIn(form) {
    document.getElementById("start_time").value = getCurrentDateTime();
    return jsonSubmit(form, '/timebank');
}
function punchOut(form) {
    document.getElementById("end_time").value = getCurrentDateTime();
    return jsonSubmit(form, '/timebank');
}