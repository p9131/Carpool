var isbypass=false;

function bypass() {
    isbypass = !isbypass;
    populateUsersTable();

}

function addNewTrip() {
    alert("Status updated!");
}
function getParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function back() {
    var url =  "driver.html";
    window.location.href = url;
}

function addNewTrip() {
    alert("Status updated!");
}
function getParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

var tripId = getParameter('tripid');
var trip;

if (tripId) {
    var theTrip = Android.getRouteUsers(tripId);
    var trip = JSON.parse(theTrip);
}
else {
     }

var userRequestStatusMap = trip.userRequestStatusMap;
var users;

for (var user in userRequestStatusMap) {
        var status = userRequestStatusMap[user];
        console.log(user + ": " + status);
 }
populateUsersTable();

function populateUsersTable() {

    var tableBody = document.getElementById('usersBody');

    tableBody.innerHTML = '';

    for (var user in userRequestStatusMap) {
            var status = userRequestStatusMap[user];
            var row = tableBody.insertRow();
            var cellName = row.insertCell(0);
            var cellAction = row.insertCell(1);
            var cellStatus = row.insertCell(2);

            cellName.innerHTML = user;

            cellStatus.innerHTML = status;



            if (isBeforeCutoff(trip.startTime) ||isbypass){
            var row = document.createElement("row");
            var acceptButton = document.createElement("button");
                acceptButton.innerHTML = "+";
                 acceptButton.classList.add("accept-button");
                acceptButton.addEventListener("click", function () {
                    Android.changeStatus(tripId.toString(),user,"Accepted");
                    cellStatus.innerHTML = "Accepted";
                    userRequestStatusMap[user]="Accepted";
                    console.log("Driver accepted trip with ID:", trip.id);
                });

                var rejectButton = document.createElement("button");
                rejectButton.innerHTML = "-";
                rejectButton.classList.add("reject-button");
                rejectButton.addEventListener("click", function () {
                    Android.changeStatus(tripId,user,"Rejected");
                    cellStatus.innerHTML = "Rejected";
                    userRequestStatusMap[user]="Rejected";
                    console.log("Driver rejected trip with ID:", trip.id);
                });
                row.appendChild(acceptButton);
                row.appendChild(rejectButton);
                cellAction.appendChild(row);
                }
                else{
                    cellAction.innerHTML="No Action";
                }
        };

        function isBeforeCutoff(dateTimeString) {
            var targetDateTime = new Date(dateTimeString);

            var cutoffTime = new Date(targetDateTime);
            if (targetDateTime.getHours() === 7) {
                cutoffTime.setDate(cutoffTime.getDate() - 1); // Previous day
                cutoffTime.setHours(23, 30, 0, 0);
            } else if (targetDateTime.getHours() === 17) {
                cutoffTime.setHours(16, 30, 0, 0);
            } else {
                console.error("Invalid hour specified in the date string.");
                return false;
            }
            var currentTime = new Date();
            return currentTime < cutoffTime;
        }


}