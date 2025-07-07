const destinationSelect = document.getElementById('tripDestination1');
const startSelect = document.getElementById('tripStartPoint1');
var x;
var trips;
var currentDate = new Date().toISOString().split("T")[0];
var date = document.getElementById("tripDate");
var time = document.getElementById("tripTime");
date.min = currentDate;


populateTripsTable(true);


function switchPage(parameters) {
    var url =  "driver2.html" + '?' + "tripid=" +parameters;
    window.location.href = url;
}


function addNewTrip() {
      var dateTimeString = date.value + "T" + time.value;
      var DateTime = new Date(dateTimeString);
      var dateStored = DateTime.toString().replace(/:\d{2}\sGMT.*$/, '');
      addRoute(dateStored,startSelect.value,destinationSelect.value);
}

function switchPoints() {
    // Get the selected values from the dropdowns


                // Clone the options from the first dropdown
                const optionsDropdown1 = Array.from(destinationSelect.options);
                destinationSelect.innerHTML = '';  // Clear the first dropdown

                // Clone the options from the second dropdown and append to the first dropdown
                for (const option of Array.from(startSelect.options)) {
                    destinationSelect.add(new Option(option.text, option.value));
                }
                startSelect.innerHTML = '';
                // Append the cloned options from the first dropdown to the second dropdown
                for (const option of optionsDropdown1) {
                    startSelect.add(new Option(option.text, option.value));
                }
}

  function addRoute(startTime, startPoint, destination) {
    // Call the Android function to add a route to Firebase
    var x = Android.addRouteToFirebase(startTime,startPoint,destination);
    var trip = JSON.parse(x);
    trips.push(trip);
    populateTripsTable(false);

   }


function LogOut() {
        Android2.LogOut();
}

// Function to populate the table with trip details
function populateTripsTable(updateList) {

    if(updateList){
        x = Android.sendDataToJavaScript();
        trips = JSON.parse(x);
    }

    var x1 = Android.getdrivermail();
    var tableBody = document.getElementById('tripsBody');

    tableBody.innerHTML = '';

    trips.forEach(function (trip) {
        addTripToTable(trip);
    });
}

function addTripToTable(trip) {
    var tableBody = document.getElementById('tripsBody');
    var row = tableBody.insertRow();
    var cellStartTime = row.insertCell(0);
    var cellStartPoint = row.insertCell(1);
    var cellDestination = row.insertCell(2);
    var cellTotalUsers = row.insertCell(3);


    cellStartTime.innerHTML = trip.startTime;
    cellStartPoint.innerHTML = trip.startPoint;
    cellDestination.innerHTML = trip.destination;

    ;

    // Create a button for the row that calls navigateToDetailsScreen with the trip ID
    var button = document.createElement("button");
    button.innerHTML = "View Users";
    button.addEventListener("click", function () {
        switchPage(trip.id);
    });
    cellTotalUsers.appendChild(button);
}

