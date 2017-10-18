var apiKey = 'AIzaSyDMGO9GuW31YCI2hKmw43UI-hEPZpcUw0o';

var map;
var drawingManager;
var placeIdArray = [];
var polylines = [];
var snappedCoordinates = [];
var cachedData=[];
var pointsTest =[{"lat":-35.2784167, "long":149.1294692,"condition":"red"},{"lat":-35.280321693840129, "long":149.12908274880189, "condition":"green"},
{"lat":-35.2803415, "long":149.1290788, "condition":"green"},{"lat":-35.280451499999991, "long":149.1290784, "condition":"green"},
{"lat":-35.2805167, "long":149.1290879, "condition":"green"},{"lat":-35.2805901, "long":149.1291104, "condition":"black"},{"lat":-35.284728724835304, "long":149.12835061713685, "condition":"green"}];
var points=[];
var centers=[
 {
   "ID": 101,
   "NAME": "Vironniemi (Estnäs)",
   "LAT": 60.1703,
   "LONG": 24.9568
 },
 {
   "ID": 102,
   "NAME": "Ullanlinna (Ulrikasborg)",
   "LAT": 60.15841,
   "LONG": 24.94904
 },
 {
   "ID": 103,
   "NAME": "Kampinmalmi (Kampmalmen)",
   "LAT": 60.1571993712,
   "LONG": 24.9083963664
 },
 {
   "ID": 104,
   "NAME": "Taka-Töölö (Bortre Tölö)",
   "LAT": 60.18164,
   "LONG": 24.91812
 },
 {
   "ID": 105,
   "NAME": "Lauttasaari[1] (Lauttasaari)",
   "LAT": 60.15733,
   "LONG": 24.87725
 },
 {
   "ID": 201,
   "NAME": "Reijola (Grejus)",
   "LAT": 62.54306,
   "LONG": 29.84068
 },
 {
   "ID": 202,
   "NAME": "Munkkiniemi[1] (Munksnäs)",
   "LAT": 60.19827,
   "LONG": 24.87599
 },
 {
   "ID": 203,
   "NAME": "Haaga[1] (Haga)",
   "LAT": 60.22186,
   "LONG": 24.89619
 },
 {
   "ID": 204,
   "NAME": "Pitäjänmäki (Sockenbacka)",
   "LAT": 60.22298,
   "LONG": 24.86206
 },
 {
   "ID": 205,
   "NAME": "Kaarela[1] (Kårböle)",
   "LAT": 60.25123,
   "LONG": 24.88134
 },
 {
   "ID": 301,
   "NAME": "Kallio (Berghäll)",
   "LAT": 60.18429,
   "LONG": 24.94927
 },
 {
   "ID": 302,
   "NAME": "Alppiharju[1] (Åshöjden)",
   "LAT": 60.188327,
   "LONG": 24.953095
 },
 {
   "ID": 303,
   "NAME": "Vallila (Vallgård)",
   "LAT": 60.19444,
   "LONG": 24.95702
 },
 {
   "ID": 304,
   "NAME": "Pasila[1] (Böle)",
   "LAT": 60.19898,
   "LONG": 24.93285
 },
 {
   "ID": 305,
   "NAME": "Vanhakaupunki (Gammelstaden)",
   "LAT": 60.21616,
   "LONG": 24.97881
 },
 {
   "ID": 401,
   "NAME": "Maunula (Månsas)",
   "LAT": 60.2298,
   "LONG": 24.93058
 },
 {
   "ID": 402,
   "NAME": "Länsi-Pakila[1] (Västra Baggböle)",
   "LAT": 60.24113,
   "LONG": 24.92712
 },
 {
   "ID": 403,
   "NAME": "Tuomarinkylä[1] (Domarby)",
   "LAT": 60.25637,
   "LONG": 24.96682
 },
 {
   "ID": 404,
   "NAME": "Oulunkylä (Åggelby)",
   "LAT": 60.22908,
   "LONG": 24.96342
 },
 {
   "ID": 405,
   "NAME": "Itä-Pakila (Östra Baggböle)",
   "LAT": 60.24352,
   "LONG": 24.95883
 },
 {
   "ID": 501,
   "NAME": "Latokartano (Ladugården)",
   "LAT": 60.22756,
   "LONG": 25.02753
 },
 {
   "ID": 502,
   "NAME": "Pukinmäki[1] (Bocksbacka)",
   "LAT": 60.24496,
   "LONG": 24.98904
 },
 {
   "ID": 503,
   "NAME": "Malmi (Malm)",
   "LAT": 60.25102,
   "LONG": 25.01056
 },
 {
   "ID": 504,
   "NAME": "Suutarila (Skomakarböle)",
   "LAT": 60.28092,
   "LONG": 25.01095
 },
 {
   "ID": 505,
   "NAME": "Puistola (Parkstad)",
   "LAT": 60.27136,
   "LONG": 25.04522
 },
 {
   "ID": 506,
   "NAME": "Jakomäki[1] (Jakobacka)",
   "LAT": 60.26013,
   "LONG": 25.07802
 },
 {
   "ID": 601,
   "NAME": "Kulosaari (Brändö)",
   "LAT": 60.18662,
   "LONG": 25.00897
 },
 {
   "ID": 602,
   "NAME": "Herttoniemi (Hertonäs)",
   "LAT": 60.19473,
   "LONG": 25.03262
 },
 {
   "ID": 603,
   "NAME": "Laajasalo (Degerö) 60.17197",
   "LAT": 25.04326,
   "LONG": null
 },
 {
   "ID": 701,
   "NAME": "Vartiokylä (Botby) 60.21723",
   "LAT": 25.09558,
   "LONG": null
 },
 {
   "ID": 702,
   "NAME": "Myllypuro[1] (Kvarnbäcken)",
   "LAT": 60.22359,
   "LONG": 25.06776
 },
 {
   "ID": 703,
   "NAME": "Mellunkylä[1] (Mellungsby)",
   "LAT": 60.23363,
   "LONG": 25.10208
 },
 {
   "ID": 704,
   "NAME": "Vuosaari[1] (Nordsjö)",
   "LAT": 60.20879,
   "LONG": 25.14373
 },
 {
   "ID": 801,
   "NAME": "Östersundom",
   "LAT": 60.2514,
   "LONG": 25.17374
 }
];
var param=[
  {
   "timestamp": 1488852000000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-07 04:00:00",
   "airTemperature": -4.3,
   "windSpeed": 7.7,
   "gustSpeed": 14.3,
   "windDirection": 46,
   "relativeHumidity": 68,
   "dewPointTemperature": -9.4,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "6.0",
   "airPressure": 1020.3,
   "visibility": 48900,
   "cloudAmount": 7,
   "presentWeather": 0
 },
 {
   "timestamp": 1488924000000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 00:00:00",
   "airTemperature": -1.1,
   "windSpeed": 6.9,
   "gustSpeed": 11.1,
   "windDirection": 77,
   "relativeHumidity": 98,
   "dewPointTemperature": -1.3,
   "precipation": 0.7,
   "precipationDensity": 1.4,
   "snowDepth": "7.0",
   "airPressure": 1014.1,
   "visibility": 1320,
   "cloudAmount": 8,
   "presentWeather": 71
 },
 {
   "timestamp": 1488931200000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 02:00:00",
   "airTemperature": -0.5,
   "windSpeed": 6.7,
   "gustSpeed": 10.2,
   "windDirection": 110,
   "relativeHumidity": 99,
   "dewPointTemperature": -0.6,
   "precipation": 0.8,
   "precipationDensity": 0.5,
   "snowDepth": "9.0",
   "airPressure": 1013.5,
   "visibility": 2010,
   "cloudAmount": 8,
   "presentWeather": 71
 },
  {
   "timestamp": 1488938400000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 04:00:00",
   "airTemperature": -0.3,
   "windSpeed": 6.1,
   "gustSpeed": 10.9,
   "windDirection": 146,
   "relativeHumidity": 99,
   "dewPointTemperature": -0.4,
   "precipation": 0.2,
   "precipationDensity": 0,
   "snowDepth": "10.0",
   "airPressure": 1014.1,
   "visibility": 7370,
   "cloudAmount": 8,
   "presentWeather": 71
 },
  {
   "timestamp": 1488945600000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 06:00:00",
   "airTemperature": 0.3,
   "windSpeed": 6.7,
   "gustSpeed": 10.4,
   "windDirection": 134,
   "relativeHumidity": 99,
   "dewPointTemperature": 0.1,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "10.0",
   "airPressure": 1015.2,
   "visibility": 35910,
   "cloudAmount": 8,
   "presentWeather": 22
 },
  {
   "timestamp": 1488952800000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 08:00:00",
   "airTemperature": 0.7,
   "windSpeed": 6.6,
   "gustSpeed": 12.8,
   "windDirection": 144,
   "relativeHumidity": 97,
   "dewPointTemperature": 0.3,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "10.0",
   "airPressure": 1016.1,
   "visibility": 47860,
   "cloudAmount": 8,
   "presentWeather": 80
 },
  {
   "timestamp": 1488960000000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-08 10:00:00",
   "airTemperature": 0.7,
   "windSpeed": 8.3,
   "gustSpeed": 12.7,
   "windDirection": 147,
   "relativeHumidity": 93,
   "dewPointTemperature": -0.4,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "10.0",
   "airPressure": 1016.6,
   "visibility": 38930,
   "cloudAmount": 8,
   "presentWeather": 71
 },
  {
   "timestamp": 1489010400000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 00:00:00",
   "airTemperature": -0.1,
   "windSpeed": 6.5,
   "gustSpeed": 11.6,
   "windDirection": 195,
   "relativeHumidity": 78,
   "dewPointTemperature": -3.5,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "9.0",
   "airPressure": 1019.1,
   "visibility": 29810,
   "cloudAmount": 8,
   "presentWeather": 71
 }, {
   "timestamp": 1489017600000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 02:00:00",
   "airTemperature": 0,
   "windSpeed": 5.5,
   "gustSpeed": 10.1,
   "windDirection": 180,
   "relativeHumidity": 86,
   "dewPointTemperature": -2,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "9.0",
   "airPressure": 1019.1,
   "visibility": 36760,
   "cloudAmount": 8,
   "presentWeather": 0
 },
 {
   "timestamp": 1489024800000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 04:00:00",
   "airTemperature": 0.2,
   "windSpeed": 3.9,
   "gustSpeed": 7.3,
   "windDirection": 183,
   "relativeHumidity": 90,
   "dewPointTemperature": -1.2,
   "precipation": 0.1,
   "precipationDensity": 0,
   "snowDepth": "10.0",
   "airPressure": 1018.6,
   "visibility": 26490,
   "cloudAmount": 8,
   "presentWeather": 22
 },
 {
   "timestamp": 1489032000000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 06:00:00",
   "airTemperature": 0.2,
   "windSpeed": 5.2,
   "gustSpeed": 7.4,
   "windDirection": 172,
   "relativeHumidity": 86,
   "dewPointTemperature": -1.8,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "9.0",
   "airPressure": 1018.7,
   "visibility": 22210,
   "cloudAmount": 5,
   "presentWeather": 0
 },
 {
   "timestamp": 1489039200000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 08:00:00",
   "airTemperature": 1,
   "windSpeed": 4.2,
   "gustSpeed": 7.4,
   "windDirection": 152,
   "relativeHumidity": 85,
   "dewPointTemperature": -1.2,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "9.0",
   "airPressure": 1018.5,
   "visibility": 17210,
   "cloudAmount": 8,
   "presentWeather": 0
 },
  {
   "timestamp": 1489046400000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-09 10:00:00",
   "airTemperature": 1.5,
   "windSpeed": 4.3,
   "gustSpeed": 8.1,
   "windDirection": 159,
   "relativeHumidity": 81,
   "dewPointTemperature": -1.4,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "9.0",
   "airPressure": 1017.7,
   "visibility": 18740,
   "cloudAmount": 8,
   "presentWeather": 0
 },

  {
   "timestamp": 1489104000000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-10 02:00:00",
   "airTemperature": 0.3,
   "windSpeed": 2,
   "gustSpeed": 2.8,
   "windDirection": 120,
   "relativeHumidity": 87,
   "dewPointTemperature": -1.7,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "8.0",
   "airPressure": 1013.7,
   "visibility": 24940,
   "cloudAmount": 7,
   "presentWeather": 0
 },

  {
   "timestamp": 1489111200000,
   "zoneId": "Helsinki-Kumpula",
   "date": "2017-03-10 04:00:00",
   "airTemperature": -0.6,
   "windSpeed": 1.4,
   "gustSpeed": 1.8,
   "windDirection": 97,
   "relativeHumidity": 92,
   "dewPointTemperature": -1.7,
   "precipation": 0,
   "precipationDensity": 0,
   "snowDepth": "8.0",
   "airPressure": 1013.1,
   "visibility": 16040,
   "cloudAmount": 3,
   "presentWeather": 0
 }
];
var isProcessing = false;
var currentSelectedParam = param[0];
var fromPoint;
var toPoint;
var circles=[];
var defaultPath = ["60.164903,24.925315|60.172247,24.948039","60.179368,24.923491|60.190603,24.949379","60.181465,24.927460|60.172439,24.934942","60.184758,24.943353|60.185985,24.959221","60.161085,24.925218|60.163552,24.945380","60.187961,24.956066|60.185478,24.951485","60.179975,24.949650|60.184200,24.950637"];
function initialize() {  
  var mapOptions = {
    zoom: 13,
    center: {lat:60.192059, lng: 24.945831},
    mapTypeControl: false,
    streetViewControl: false
  };
  map = new google.maps.Map(document.getElementById('map'), mapOptions);
  //Map control right
  map.controls[google.maps.ControlPosition.RIGHT_TOP].push(document.getElementById('bar'));
  var autocompleteFrom = new google.maps.places.Autocomplete(document.getElementById('from'));
  var autocompleteTo = new google.maps.places.Autocomplete(document.getElementById('to'));
  autocompleteFrom.addListener('place_changed', function() {
    var place = autocompleteFrom.getPlace();
    fromPoint = place.geometry.location;
  });
  autocompleteTo.addListener('place_changed', function() {
    var place = autocompleteFrom.getPlace();
    toPoint =place.geometry.location;
  });
  for(var mk=0;mk<centers.length;mk++){
    var latlng = new google.maps.LatLng(
        centers[mk].LAT,
        centers[mk].LONG);
    var marker = new google.maps.Marker({
        position: latlng
    });
    marker.setMap(map);
    
  }
        //Map control left
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(document.getElementById("dropdown"));

  // Clear button. Click to remove all polylines.
  $('#clear').click(function(ev) {
    for (var i = 0; i < polylines.length; ++i) {
      polylines[i].setMap(null);
    }
    polylines = [];
    ev.preventDefault();
    pickSample();
    return false;
  });
  pickSample();
}

function mainCalculation(){
  for(var cls=0; cls<circles.length;cls++){
    circles[cls].setMap(null);
  }
  circles=[];
  isProcessing = true;

    points=[];
    snappedCoordinates=[];

    var centerReqArr=[];
    
    for(var c=0;c<centers.length;c++){
      var centerArrObj = $.extend(true, {}, currentSelectedParam);
      centerReqArr.push(centerArrObj);
    }
    for(var d=0;d<centerReqArr.length;d++){
      centerReqArr[d].zoneId = centers[d].ID;
    }
    $.ajax({
            type: 'POST',
            async: false,
            url: 'http://192.168.43.182:5000/predict',
            dataType: "json",
            data: JSON.stringify(centerReqArr),
            success: function (msg) {
              for(var fk=0;fk<msg.length;fk++){
                if(msg[fk].condition === 1){
                  centerReqArr[fk].condition = "red";         
                }
                else if(msg[fk].condition === 2){
                  centerReqArr[fk].condition = "blue";          
                }
                else if(msg[fk].condition === 3){
                  centerReqArr[fk].condition = "green";         
                }
              }
              for(var i=0;i<centers.length;i++){
                var latlng = new google.maps.LatLng(
                  centers[i].LAT,
                  centers[i].LONG);
                  var colorS= centerReqArr[i].condition;
                  var cityCircle = new google.maps.Circle({
                    strokeColor: colorS,
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: colorS,
                    fillOpacity: 0.35,
                    map: map,
                    center: latlng,
                    radius: 700
                  });
                  circles.push(cityCircle);
              }

            },
            error: function (request, status, error) {
            }
        });
  /*$.get('https://roads.googleapis.com/v1/snapToRoads', {
    interpolate: true,
    key: apiKey,
    path: mainPath
  }, function(data) {
    points = data.snappedPoints;
    var step = Math.round(data.snappedPoints.length/10);
    if(step ===0){
      step=1;
    }
    var reqArr=[];
    
    /*for(var i=0;i<data.snappedPoints.length; i++){
      var fineObj=currentSelectedParam;
      var nearest;
      var d=0;
      for(var k =0;k<centers.length;k++){
        var temp = getDistanceFromLatLonInKm(data.snappedPoints[i].location.latitude, data.snappedPoints[i].location.longitude, centers[k].LAT, centers[k].LONG);
        if(d==0){
          d=temp;
          nearest = centers[k];
        }
        else{
          if(d>temp){
            d=temp;
            nearest = centers[k];
          }
        }
      }
      fineObj.zoneId = nearest.ID;
      reqArr.push(fineObj);
    }*/


    

    /*$.ajax({
            type: 'POST',
            async: false,
            url: 'http://192.168.43.182:5000/predict',
            dataType: "json",
            data: JSON.stringify(reqArr),
            success: function (msg) {
              for(var fk=0;fk<msg.length;fk++){
                if(msg[fk].condition === 1){
                  points[fk].condition = "red";         
                }
                else if(msg[fk].condition === 2){
                  points[fk].condition = "blue";          
                }
                else if(msg[fk].condition === 3){
                  points[fk].condition = "green";         
                }
              }              

              /*var pathList = [];
              for(var j=0;j<points.length;j++){
                var st= points[j].lat.toString()+","+points[j].long.toString();
                pathList.push(st);
              }
              var pathCalled = pathList.join("|");
              processSnapToRoadResponse(points, path, pos);

            },
            error: function (request, status, error) {
            }
        });
  });*/
}

function changeDate(value){
  for (var i = 0; i < polylines.length; ++i) {
      polylines[i].setMap(null);
    }
    polylines = [];
  for(var j =0;j<param.length;j++){
    if(value === param[j].date){
      currentSelectedParam = param[j];
    }
  }
  pickSample();
}

function getPathFromTo(fromPoint, toPoint){
  var path = "60.179368,24.923491|60.190603,24.949379";
  if(fromPoint || toPoint){
    if(fromPoint){
      path= fromPoint.lat()+","+fromPoint.lng() + "|" + path.split("|")[1];
    }
    if(toPoint){
      path= path.split("|")[0] + "|" + toPoint.lat()+","+toPoint.lng();
    }
  }
  return path;
}

function pickSample(){
  mainCalculation();
  /*if(typeof path === 'string'){
    mainCalculation(path);
  }
  else if(typeof path === 'object'){
    mainCalculation(path,0);
  }*/
}

// Snap a user-created polyline to roads and draw the snapped path
function runSnapToRoad(pathCalled, path, pos) {
  
  $.get('https://roads.googleapis.com/v1/snapToRoads', {
    interpolate: true,
    key: apiKey,
    path: pathCalled
  }, function(data) {
    processSnapToRoadResponse(data, path, pos);
  });
}

// Store snapped polyline returned by the snap-to-road service.
function processSnapToRoadResponse(points, path, pos) {
	var noSample=0;
  placeIdArray = [];
  for (var i = 0; i < points.length; i++) {
    var latlng = new google.maps.LatLng(
        points[i].location.latitude,
        points[i].location.longitude);	
		snappedCoordinates.push(latlng);
	  drawSnappedPolyline(points[i].condition, path, pos);
  }

  if(pos!==undefined && pos!==null){
    pos++;
  }
  if(pos!==undefined && pos!==null && path && typeof path ==='object' && pos<path.length){
    mainCalculation(path, pos);
  }
}

function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
            var R = 6371; // Radius of the earth in km
            var dLat = deg2rad(lat2 - lat1); // deg2rad below
            var dLon = deg2rad(lon2 - lon1);
            var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            var d = R * c; // Distance in km
            return d;
        }

        function deg2rad(deg) {
            return deg * (Math.PI / 180)
        }

// Draws the snapped polyline (after processing snap-to-road response).
function drawSnappedPolyline(color, path, pos) {
  var snappedPolyline = new google.maps.Polyline({
    path: snappedCoordinates,
    strokeColor: color,
    strokeWeight: 7
  });

  snappedPolyline.setMap(map);
  polylines.push(snappedPolyline);
  var last = snappedCoordinates[snappedCoordinates.length-1];
  snappedCoordinates = [];
  snappedCoordinates.push(last);
  isProcessing=false;
}


$(window).load(initialize);