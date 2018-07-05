/**
 * Main desk reservation app
 */

var app = angular.module('hsbcApp', ['ngRoute', 'ngTable', 'ngStorage']);
var urlBase = "/HSBCDeskReservationApp";

/**
 * Global Variables
 */
var resBase = {"employee": "", "startDate": "", "startTime": "", "endDate": "", "endTime": ""};
var resInfo = {"building": "", "floor": "", "section": "", "desk": ""};
var desk = {};
var listOfDesks = {};

/**
 * Configure reservation Routes
 */
app.config(['$routeProvider', function ($routeProvider) {

        // Main reservation page  
        $routeProvider
                .when("/", {
                    templateUrl: "partials/reservation/reservation.html",
                    controller: "ReservationQueryController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/selection", {
                    templateUrl: "partials/reservation/selection.html",
                    controller: "ReservationSelectController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/confirmation", {
                    templateUrl: "partials/reservation/confirmation.html",
                    controller: "ReservationConfirmController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/myreservations", {
                    templateUrl: "partials/myreservations/myreservations.html",
                    controller: "MyReservationsController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/inquiry", {
                    templateUrl: "partials/inquiry/inquiry.html",
                    controller: "InquiryController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/inquiry/employee", {
                    templateUrl: "partials/inquiry/employee.html",
                    controller: "InquiryEmployeeController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin", {
                    templateUrl: "partials/admin/admin.html",
                    controller: "AdminController",
                    resolve: {
                        check: function (empInfo, checkAdmin, $location, $q, $localStorage) {
                            var deferred = $q.defer();
                            if ($localStorage.current_user == null) {
                                var name = "John Doe";
                            } else {
                                var name = $localStorage.current_user.name;
                            }
                            empInfo.fetchEmp(name).then(function (emp) {
                                checkAdmin.isAdmin(emp['id']).then(function (isAdmin) {
                                    if (!isAdmin) {
                                        $location.path('/');
                                        alert('You do not have an access to this page.');
                                    }
                                });
                            });
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin/reservation", {
                    templateUrl: "partials/admin/reservation.html",
                    controller: "AdminReservationController",
                    resolve: {
                        check: function (empInfo, checkAdmin, $location, $q, $localStorage) {
                            var deferred = $q.defer();
                            if ($localStorage.current_user == null) {
                                var name = "John Doe";
                            } else {
                                var name = $localStorage.current_user.name;
                            }
                            empInfo.fetchEmp(name).then(function (emp) {
                                checkAdmin.isAdmin(emp['id']).then(function (isAdmin) {
                                    if (!isAdmin) {
                                        $location.path('/');
                                        alert('You do not have an access to this page.');
                                    }
                                });
                            });
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin/desk", {
                    templateUrl: "partials/admin/desk.html",
                    controller: "AdminDeskController",
                    resolve: {
                        check: function (empInfo, checkAdmin, $location, $q, $localStorage) {
                            var deferred = $q.defer();
                            if ($localStorage.current_user == null) {
                                var name = "John Doe";
                            } else {
                                var name = $localStorage.current_user.name;
                            }
                            empInfo.fetchEmp(name).then(function (emp) {
                                checkAdmin.isAdmin(emp['id']).then(function (isAdmin) {
                                    if (!isAdmin) {
                                        $location.path('/');
                                        alert('You do not have an access to this page.');
                                    }
                                });
                            });
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin/location", {
                    templateUrl: "partials/admin/location.html",
                    controller: "AdminLocationController",
                    resolve: {
                        check: function (empInfo, checkAdmin, $location, $q, $localStorage) {
                            var deferred = $q.defer();
                            if ($localStorage.current_user == null) {
                                var name = "John Doe";
                            } else {
                                var name = $localStorage.current_user.name;
                            }
                            empInfo.fetchEmp(name).then(function (emp) {
                                checkAdmin.isAdmin(emp['id']).then(function (isAdmin) {
                                    if (!isAdmin) {
                                        $location.path('/');
                                        alert('You do not have an access to this page.');
                                    }
                                });
                            });
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin/database", {
                    templateUrl: "partials/admin/database.html",
                    controller: "AdminDatabaseController",
                    resolve: {
                        check: function (empInfo, checkAdmin, $location, $q, $localStorage) {
                            var deferred = $q.defer();
                            if ($localStorage.current_user == null) {
                                var name = "John Doe";
                            } else {
                                var name = $localStorage.current_user.name;
                            }
                            empInfo.fetchEmp(name).then(function (emp) {
                                checkAdmin.isAdmin(emp['id']).then(function (isAdmin) {
                                    if (!isAdmin) {
                                        $location.path('/');
                                        alert('You do not have an access to this page.');
                                    }
                                });
                            });
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (!adminExists) {
                                    $location.path('/admin/registration');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .when("/admin/registration", {
                    templateUrl: "partials/admin/registration.html",
                    controller: "AdminRegistrationController",
                    resolve: {
                        check: function (checkAdmin, $location, $q) {
                            var deferred = $q.defer();
                            checkAdmin.adminExists().then(function (adminExists) {
                                if (adminExists) {
                                    $location.path('/');
                                }
                            });
                            deferred.resolve('done');
                            return deferred.promise;
                        }
                    }
                })
                .otherwise({
                    template: 'does not exists'
                });
    }]);

app.factory("sectionMap", function ($http) {
    var sectionMap = null;
    var setMap = function(map) {
        sectionMap = map;
    };
    var getMap = function() {
        return sectionMap;
    };
    
    return {
        setMap: setMap,
        getMap: getMap
    };
});

app.factory("empInfo", function ($rootScope, $http, $q) {
    return {
        fetchEmp: function (name) {
            var deferred = $q.defer();
            if ($rootScope.employee == null || $rootScope.employee.name != name) {
                $http.get(urlBase + '/inquiry/mocked/myhsbc/uservariables/' + name).then(function (data) {
                    if (data['data']['exception'] == null) {
                        var str = data['data']['string'].split(";");
                        var id = str[1];
                        var name = str[0];
                        var dept = str[5];
                        var phoneNum = str[10];
                        var email = str[7];
                        $rootScope.employee = {
                            "id": id.substring(id.indexOf("'") + 1, id.length - 1),
                            name: name.substring(name.indexOf("'") + 1, name.length - 1),
                            dept: dept.substring(dept.indexOf("'") + 1, dept.length - 1),
                            phoneNum: phoneNum.substring(phoneNum.indexOf("'") + 1, phoneNum.length - 1),
                            email: email.substring(email.indexOf("'") + 1, email.length - 1)
                        };
                        deferred.resolve($rootScope.employee);

                    } else {
                        console.log("failed hsbc user");
                        deferred.reject(false);
                    }
                });
            } else {
                deferred.resolve($rootScope.employee);
            }
            return deferred.promise;
        }
    }
});

app.factory("building", function ($http, $q) {
    return {
        getBuildings: function () {
            var deferred = $q.defer();
            $http.get(urlBase + '/inquiry/view_all_floors_sections/').then(function (data) {
                if (data['data']['exception'] == null) {
                    deferred.resolve(data['data']);
                } else {
                    console.log(data['data']['exception']);
                    deferred.reject(false);
                }
            });
            return deferred.promise;
        }
    }
});

app.factory("checkAdmin", function ($http, $q, $rootScope) {
    return {
        adminExists: function () {
            var deferred = $q.defer();
            var adminExists = false;
            $http.get(urlBase + '/admin/view_admins/').then(function (data) {
                if (data['data']['exception'] == null) {
                    if (data['data'].length > 0) {
                        deferred.resolve(true);
                    } else {
                        adminExists = false;
                        deferred.resolve(false);
                    }
                } else {
                    console.log("failed to view admins.");
                    deferred.reject(false);
                }
            });
            return deferred.promise;
        },
        isAdmin: function (emp_id) {
            var deferred = $q.defer();
            $http.get(urlBase + '/admin/isAdmin/' + emp_id).then(function (data) {
                if (data['data']['exception'] == null) {
                    if (data['data']) {
                        deferred.resolve(true);
                    } else {
                        deferred.resolve(false);
                    }
                } else {
                    console.log("failed to check if you are admin");
                    deferred.reject(false);
                }
            });
            return deferred.promise;
        }
    }
});

app.directive("fileread", [function () {
        return {
            scope: {
                fileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var reader = new FileReader();
                    reader.onload = function (loadEvent) {
                        scope.$apply(function () {
                            scope.fileread = loadEvent.target.result;
                        });
                    };
                    reader.readAsDataURL(changeEvent.target.files[0]);
                });
            }
        }
    }]);

/**
 * Controllers
 */

app.controller('BaseController', function ($rootScope, $scope, $location, $http, empInfo, checkAdmin, $route, $localStorage, $sessionStorage) {
    // local storage is used just for demonstration purposes
    $scope.$storage = $localStorage;

    //initialize employee
    $scope.initializeEmp = function () {
        if ($localStorage.current_user == null) {
            var name = "John Doe";
        } else {
            var name = $localStorage.current_user.name;
        }
        empInfo.fetchEmp(name).then(function (emp) {
            $localStorage.current_user = emp;
            checkAdmin.isAdmin($scope.employee['id']).then(function (isAdmin) {
                $scope.isAdmin = isAdmin;
            });
        });
    };
    
    // Validate integer input
    $scope.validateInteger = function (input) {
        var idFormat = /^[1-9]\d*$/;
        if (!input.match(idFormat)) {
            return true;
        }
        return false;
    };
    
    $scope.validateMapUrl = function (input) {
        var urlFormat = /(http)s?:(\/\/[^"' ]*\.(?:png|jpg|jpeg|gif|png|svg))/;
        if (!input.match(urlFormat)) {
            return true;
        }
        return false;
    };
    
    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.reload = function () {
        $route.reload();
    };

    $scope.switchUser = function(name) {
        empInfo.fetchEmp(name).then(function (emp) {
            $rootScope.employee = emp;
            $localStorage.current_user = emp;
            $scope.reload();
        });       
    };
});


app.controller('ReservationQueryController', function ($scope, $location, $http, empInfo, checkAdmin, building, sectionMap) {
    $scope.inquiryClick = false;
    $scope.adminClick = false;
    $scope.onInquiry = false;
    $scope.onAdmin = false;
    $scope.linkToRes = true;
    
    // Initialize employee obj
    $scope.initializeEmp();

    // send request for locatcion select options
    building.getBuildings().then(function(result) {
        $scope.sectionMaps = result.sectionMaps;
        
        $scope.buildingsObj = result.buildings;
        
        var locations = [];
        for (var building_name in result.buildings) {
            locations.push(building_name);
        }
        $scope.locations = locations;
        $scope.reservation.building = $scope.locations[0];
        
        $scope.updateFloors();
    });
    
    $scope.updateFloors = function() {
        var floors = [];
        for (var floor in $scope.buildingsObj[$scope.reservation.building].floors) {
            floors.push(floor);
        }
        $scope.floors = floors;
        $scope.reservation.floor = $scope.floors[0];
        
        $scope.updateSections();
    };
    
    $scope.updateSections = function() {
        $scope.sections = $scope.buildingsObj[$scope.reservation.building].floors[$scope.reservation.floor];
        if ($scope.sections != null) {
            $scope.reservation.section = $scope.sections[0];
        }
        $scope.updateMap();
    };
    
    $scope.updateMap = function() {
        var key = $scope.reservation.building + '.' + $scope.reservation.floor + '.' + $scope.reservation.section;
        $scope.floorPlan = $scope.sectionMaps[key];
        sectionMap.setMap($scope.floorPlan);
    };

    $scope.timeOptions = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
    $scope.ampmOptions = ["AM", "PM"];
    $scope.floorOptions = [1, 2, 3, 4];
    $scope.sectionOptions = ["A", "B"];

    // Set date
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    today = mm + '/' + dd + '/' + yyyy;
    $scope.startdate = today;
    $scope.enddate = today;

    // functions to pan and drag image
    var img_ele = null,
            frame_ele = null,
            x_cursor = 0,
            y_cursor = 0,
            x_img_ele = 0,
            y_img_ele = 0;

    $scope.zoom = function (zoomincrement) {
        img_ele = document.getElementById('floor-maps');
        frame_ele = document.getElementById('image-frame');
        var pre_center_x = frame_ele.getBoundingClientRect().width / 2 - img_ele.offsetLeft;
        var pre_center_y = frame_ele.getBoundingClientRect().height / 2 - img_ele.offsetTop;
        var center_x = pre_center_x * zoomincrement;
        var center_y = pre_center_y * zoomincrement;
        var pre_width = img_ele.getBoundingClientRect().width, pre_height = img_ele.getBoundingClientRect().height;
        img_ele.style.left = img_ele.offsetLeft - center_x + pre_center_x + 'px';
        img_ele.style.top = img_ele.offsetTop - center_y + pre_center_y + 'px';
        img_ele.style.width = pre_width * zoomincrement + 'px';
        img_ele.style.height = pre_height * zoomincrement + 'px';
        img_ele = null;
    };

    function start_drag() {
        img_ele = document.getElementById('floor-maps');
        x_img_ele = window.event.clientX - document.getElementById('floor-maps').offsetLeft;
        y_img_ele = window.event.clientY - document.getElementById('floor-maps').offsetTop;
    }
    ;

    function stop_drag() {
        img_ele = null;
    }

    function while_drag() {
        var x_cursor = window.event.clientX;
        var y_cursor = window.event.clientY;
        if (img_ele !== null) {
            img_ele.style.left = (x_cursor - x_img_ele) + 'px';
            img_ele.style.top = (window.event.clientY - y_img_ele) + 'px';
        }
    }

    document.ondragstart = function () {
        return false;
    };

    document.getElementById('image-frame').addEventListener('mousedown', start_drag);
    document.getElementById('image-frame').addEventListener('mousemove', while_drag);
    document.addEventListener('mouseup', stop_drag);


    // Desk reservation query button onclicked
    $scope.update = function (reservation, startdate, enddate) {

        var update = true;
        var error = "";

        // Convert AM/PM to 24 hour clock
        var starttime = reservation.starttime;
        var endtime = reservation.endtime;
        if (reservation.startampm === "PM") {
            if (parseInt(starttime) < 12) {
                starttime = (parseInt(starttime) + 12).toString();
            }
        } else {
            if (parseInt(starttime) === 12) {
                starttime = "00";
            }
            if (parseInt(starttime) < 10) {
                startime = "0" + starttime;
            }
        }
        if (reservation.endampm === "PM") {
            if (parseInt(endtime) < 12) {
                endtime = (parseInt(endtime) + 12).toString();
            }
        } else {
            if (parseInt(starttime) < 10) {
                if (parseInt(endtime) === 12) {
                    endtime = "00";
                }
                startime = "0" + starttime;
            }
        }

        // Check that dates work
        var sDate = new Date(startdate);
        var eDate = new Date(enddate);
        var currDate = new Date();
        var daysBooked = (eDate - sDate) / 86400000;

        // Error cases
        if ($scope.reservation.floor == null || $scope.reservation.floor == '') {
            error += 'Floor is required.\n';
        }
        if ($scope.reservation.section == null || $scope.reservation.section == '') {
            error += 'Section is required.\n';
        }
        if (sDate > eDate) {
            error += "Start date begins after end date.\n";
        }
        if (daysBooked > 5) {
            error += "You are only allowed a booking of 5 days maximum.\n";
        }
        if ((sDate - currDate) / 86400000 > 30) {
            error += "You are not allowed to book more than 30 days ahead of time.\n";
        }
        if ((sDate - eDate === 0) && (starttime >= endtime)) {
            error += "Please specify a valid time range.\n";
        }

        if (error != "") {
            alert(error);
            return false;
        }

        // Send query parameters 
        resBase = {
            "employee": $scope.employee,
            "startDate": startdate,
            "startTime": starttime + ':00',
            "endDate": enddate,
            "endTime": endtime + ':00'
        };
        var queryParams = {
            "base": resBase,
            "building": reservation.building,
            "floor": reservation.floor,
            "section": reservation.section
        };
        
        floor = reservation.floor;
        if (update) {
            $http.post(urlBase + "/reservation/floor_preference", queryParams)
                    .then(function (data) {
                        if (data['data']['exception'] == null) {
                            if (data['data'].length == 0) {
                                alert("No desk found.");
                                return false;
                            } else {
                                listOfDesks = data['data'];
                                // Should receive map ids
                                $location.path("/selection");
                            }
                        } else {
                            alert(data['data']['exception']);
                        }
                    });
        }
    };
});

app.controller('ReservationSelectController', function ($scope, $location, $http, empInfo, checkAdmin, sectionMap) {
    $scope.inquiryClick = false;
    $scope.adminClick = false;
    $scope.onInquiry = false;
    $scope.onAdmin = false;
    $scope.linkToRes = true;
    
    // Initialize employee obj 
    $scope.initializeEmp();
    
    // Loop through list of desks
    var desks = [];
    for (var i = 0; i < listOfDesks.length; i++) {
        desks.push(listOfDesks[i].building + ": " + listOfDesks[i].deskID);
    }
    $scope.desks = desks;


    // Get map
    $scope.floorPlan = sectionMap.getMap();

    // functions to pan and drag image
    var img_ele = null,
            frame_ele = null,
            x_cursor = 0,
            y_cursor = 0,
            x_img_ele = 0,
            y_img_ele = 0;

    $scope.zoom = function (zoomincrement) {
        img_ele = document.getElementById('floor-maps');
        frame_ele = document.getElementById('image-frame');
        var pre_center_x = frame_ele.getBoundingClientRect().width / 2 - img_ele.offsetLeft;
        var pre_center_y = frame_ele.getBoundingClientRect().height / 2 - img_ele.offsetTop;
        var center_x = pre_center_x * zoomincrement;
        var center_y = pre_center_y * zoomincrement;
        var pre_width = img_ele.getBoundingClientRect().width, pre_height = img_ele.getBoundingClientRect().height;
        img_ele.style.left = img_ele.offsetLeft - center_x + pre_center_x + 'px';
        img_ele.style.top = img_ele.offsetTop - center_y + pre_center_y + 'px';
        img_ele.style.width = pre_width * zoomincrement + 'px';
        img_ele.style.height = pre_height * zoomincrement + 'px';
        img_ele = null;
    };

    function start_drag() {
        img_ele = document.getElementById('floor-maps');
        x_img_ele = window.event.clientX - document.getElementById('floor-maps').offsetLeft;
        y_img_ele = window.event.clientY - document.getElementById('floor-maps').offsetTop;
    }
    ;

    function stop_drag() {
        img_ele = null;
    }

    function while_drag() {
        var x_cursor = window.event.clientX;
        var y_cursor = window.event.clientY;
        if (img_ele !== null) {
            img_ele.style.left = (x_cursor - x_img_ele) + 'px';
            img_ele.style.top = (window.event.clientY - y_img_ele) + 'px';
        }
    }

    document.ondragstart = function () {
        return false;
    };

    document.getElementById('image-frame').addEventListener('mousedown', start_drag);
    document.getElementById('image-frame').addEventListener('mousemove', while_drag);
    document.addEventListener('mouseup', stop_drag);

    // Desk selected button onclicked
    $scope.updateSelection = function (selected) {

        // Parse desk selection
        var parsed_res = selected.desk.split(": ");

        // Send query parameters 
        desk = {
            "building": parsed_res[0],
            "deskID": parsed_res[1]
        };
        var queryParams = {
            "base": resBase,
            "desk": desk
        };

        // Send selected desk
        // Retrieve status
        $http.post(urlBase + "/reservation/hold", queryParams)
                .then(function (data) {
                    if (data['data']['exception'] == null) {
                        // Parse for floor, section, and desk
                        var parsed_desk = parsed_res[1].split(".");
                        // Assign official reservation for confirmation
                        resInfo = {
                            "building": desk.building,
                            "floor": parsed_desk[0],
                            "section": parsed_desk[1],
                            "desk": parsed_desk[2]
                        };
                        $location.path("/confirmation");
                    } else {
                        alert(data['data']['exception']);
                    }
                });
    };
});


app.controller('ReservationConfirmController', function ($scope, $location, $http, $interval, empInfo, checkAdmin, sectionMap) {
    $scope.inquiryClick = false;
    $scope.adminClick = false;
    $scope.onInquiry = false;
    $scope.onAdmin = false;
    $scope.linkToRes = true;
    
    // Initialize employee obj 
    $scope.initializeEmp();
    
    $scope.startdt = resBase.startDate + ' - ' + resBase.startTime;
    $scope.enddt = resBase.endDate + ' - ' + resBase.endTime;
    $scope.building = resInfo.building;
    $scope.floor = resInfo.floor;
    $scope.section = resInfo.section;
    $scope.desk = resInfo.desk;

    // Countdown
    var s = 0;
    var m = 10;
    $scope.minutes = m;
    $scope.seconds = s;
    $scope.callAtInterval = function () {
        if (s === 0 && m === 0) {

            var holdDeskParams = {
                "base": resBase,
                "desk": desk
            };

            // Time Up
            $http.post(urlBase + "/reservation/delete_hold", holdDeskParams)
                    .then(function (data) {
                        if (data['data']['exception'] == null) {
                            console.log("10 min. time's up.");
                            $location.path("/");
                        } else {
                            alert(data['data']['exception']);
                        }
                    });
        } else if (s === 0 && m > 0) {
            s = 59;
            m -= 1;
        } else {
            s -= 1;
        }
        $scope.minutes = m;
        $scope.seconds = s;
    };
    var promise = $interval(function () {
        $scope.callAtInterval();
    }, 1000);

    $http.post(urlBase + "/reservation/get_map_url", desk)
            .then(function (data) {
                if (data['data']['exception'] == null) {
                    var mapURL = data['data']['string'];
                    $scope.floorPlan = mapURL;
                } else {
                    console.log(data['data']['exception']);
                }
            });

    // functions to pan and drag image
    var img_ele = null,
            frame_ele = null,
            x_cursor = 0,
            y_cursor = 0,
            x_img_ele = 0,
            y_img_ele = 0;

    $scope.zoom = function (zoomincrement) {
        img_ele = document.getElementById('floor-maps');
        frame_ele = document.getElementById('image-frame');
        var pre_center_x = frame_ele.getBoundingClientRect().width / 2 - img_ele.offsetLeft;
        var pre_center_y = frame_ele.getBoundingClientRect().height / 2 - img_ele.offsetTop;
        var center_x = pre_center_x * zoomincrement;
        var center_y = pre_center_y * zoomincrement;
        var pre_width = img_ele.getBoundingClientRect().width, pre_height = img_ele.getBoundingClientRect().height;
        img_ele.style.left = img_ele.offsetLeft - center_x + pre_center_x + 'px';
        img_ele.style.top = img_ele.offsetTop - center_y + pre_center_y + 'px';
        img_ele.style.width = pre_width * zoomincrement + 'px';
        img_ele.style.height = pre_height * zoomincrement + 'px';
        img_ele = null;
    };

    function start_drag() {
        img_ele = document.getElementById('floor-maps');
        x_img_ele = window.event.clientX - document.getElementById('floor-maps').offsetLeft;
        y_img_ele = window.event.clientY - document.getElementById('floor-maps').offsetTop;
    }
    ;

    function stop_drag() {
        img_ele = null;
    }

    function while_drag() {
        var x_cursor = window.event.clientX;
        var y_cursor = window.event.clientY;
        if (img_ele !== null) {
            img_ele.style.left = (x_cursor - x_img_ele) + 'px';
            img_ele.style.top = (window.event.clientY - y_img_ele) + 'px';
        }
    }

    document.ondragstart = function () {
        return false;
    };

    document.getElementById('image-frame').addEventListener('mousedown', start_drag);
    document.getElementById('image-frame').addEventListener('mousemove', while_drag);
    document.addEventListener('mouseup', stop_drag);


    var confirmedRes = false;

    // Confirmation button onclicked
    $scope.confirm = function (email) {

        var queryParams = {
            "base": resBase,
            "desk": desk,
            "emailMe": $scope.emailCheck,
            "confirm": true
        };

        $http.post(urlBase + "/reservation/reserve", queryParams)
                .then(function (data) {
                    if (data['data']['exception'] == null) {
                        confirmedRes = true;
                        alert(data['data']['string']);
                        $location.path("/");
                    } else {
                        console.log(data['data']['exception']);
                        alert("An error has occured. Your reservation was not made. Please try again.");
                        $location.path("/");
                    }
                });
    };
    
    window.onbeforeunload = function (event) {
        var holdDeskParams = {
            "base": resBase,
            "desk": desk
        };
        
        if (!confirmedRes) {
            // Let go of hold
            $http.post(urlBase + "/reservation/delete_hold", holdDeskParams).then(function(){
            });          
        }
        
        return undefined;
        
    };
    
    window.onunload = function (event){
        if (!confirmedRes) {
            // Let go of hold
            $http.post(urlBase + "/reservation/delete_hold", holdDeskParams).then(function(){
            });          
        }
    };

    // Exit to another page of the app
    $scope.$on("$destroy", function () {

        $interval.cancel(promise);
        window.onbeforeunload = undefined;
        window.onUnload = undefined;

        var holdDeskParams = {
            "base": resBase,
            "desk": desk
        };

        if (!confirmedRes) {
            // Let go of hold
            $http.post(urlBase + "/reservation/delete_hold", holdDeskParams)
                    .then(function (data) {
                        if (data['data']['exception'] != null) {
                            console.log(data['data']['exception']);
                        }
                    });
        }
    });
    
    $scope.$on('$locationChangeStart', function( event , next ) {  
        var length = next.length;
        if (next.substr(length-1,length-1) == "/") return;
        var answer = confirm("If you leave this page your reservation will no longer be on hold, are you sure you want to leave?");
        if (!answer) {
            event.preventDefault();
        }
    });

});

app.controller('MyReservationsController', function ($scope, $http, NgTableParams, empInfo, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = false;
    $scope.onInquiry = false;
    $scope.onAdmin = false;
    $scope.linkToMyRes = true;
    
     // Initialize employee obj 
    $scope.initializeEmp();

    //set params for the followig http request
    var queryParams = {"employeeID": $scope.employee.id, "building": "", "deskID": "", "startDate": "", "startTime": "", "endDate": "", "endTime": ""};

    //fetch all reservation entries from database
    $http.post(urlBase + '/admin/reservation_entry/view', queryParams).then(function (data) {
        if (data['data']['exception'] == null) {
            $scope.entries = data['data'];
            $scope.entryTable = new NgTableParams({}, {dataset: $scope.entries});
        } else {
            console.log("failed to get reservation entries");
        }
    });
    
    //function to delete an entry
    $scope.confirmResDelete = function (reserveEntry) {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"reserveEntry": reserveEntry, "admin": $scope.employee};
            $http.post(urlBase + '/admin/reservation_entry/delete', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };
});

app.controller('InquiryController', function ($scope, $location, $http, empInfo, checkAdmin, building) {
    $scope.inquiryClick = true;
    $scope.adminClick = false;
    $scope.onInquiry = true;
    $scope.onAdmin = false;
    $scope.linkToInqDesk = true;
    
    $scope.inquiry = {};
    // Initialize employee obj
    $scope.initializeEmp();

    // set default date to today
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    today = mm + '/' + dd + '/' + yyyy;
    $scope.inquiry.date = today;


    // send request for location select options
    building.getBuildings().then(function(result) {
        $scope.sectionMaps = result.sectionMaps;
        
        $scope.buildingsObj = result.buildings;
        
        var locations = [];
        for (var building_name in result.buildings) {
            locations.push(building_name);
        }
        $scope.locations = locations;
        $scope.inquiry.location = $scope.locations[0];
        
        $scope.updateFloors();
    });
    
    $scope.updateFloors = function() {
        var floors = [];
        for (var floor in $scope.buildingsObj[$scope.inquiry.location].floors) {
            floors.push(floor);
        }
        $scope.floors = floors;
        $scope.inquiry.floor = $scope.floors[0];
        
        $scope.updateSections();
    };
    
    $scope.updateSections = function() {
        $scope.sections = $scope.buildingsObj[$scope.inquiry.location].floors[$scope.inquiry.floor];
        if ($scope.sections != null) {
            $scope.inquiry.section = $scope.sections[0];
        }
        $scope.updateMap();
    };
    
    $scope.updateMap = function() {
        var key = $scope.inquiry.location + '.' + $scope.inquiry.floor + '.' + $scope.inquiry.section;
        $scope.floorPlan = $scope.sectionMaps[key];
    };
    
     // functions to pan and drag image
    var img_ele = null,
            frame_ele = null,
            x_cursor = 0,
            y_cursor = 0,
            x_img_ele = 0,
            y_img_ele = 0;

    $scope.zoom = function (zoomincrement) {
        img_ele = document.getElementById('floor-maps');
        frame_ele = document.getElementById('image-frame');
        var pre_center_x = frame_ele.getBoundingClientRect().width / 2 - img_ele.offsetLeft;
        var pre_center_y = frame_ele.getBoundingClientRect().height / 2 - img_ele.offsetTop;
        var center_x = pre_center_x * zoomincrement;
        var center_y = pre_center_y * zoomincrement;
        var pre_width = img_ele.getBoundingClientRect().width, pre_height = img_ele.getBoundingClientRect().height;
        img_ele.style.left = img_ele.offsetLeft - center_x + pre_center_x + 'px';
        img_ele.style.top = img_ele.offsetTop - center_y + pre_center_y + 'px';
        img_ele.style.width = pre_width * zoomincrement + 'px';
        img_ele.style.height = pre_height * zoomincrement + 'px';
        img_ele = null;
    };

    function start_drag() {
        img_ele = document.getElementById('floor-maps');
        x_img_ele = window.event.clientX - document.getElementById('floor-maps').offsetLeft;
        y_img_ele = window.event.clientY - document.getElementById('floor-maps').offsetTop;
    }
    ;

    function stop_drag() {
        img_ele = null;
    }

    function while_drag() {
        var x_cursor = window.event.clientX;
        var y_cursor = window.event.clientY;
        if (img_ele !== null) {
            img_ele.style.left = (x_cursor - x_img_ele) + 'px';
            img_ele.style.top = (window.event.clientY - y_img_ele) + 'px';
        }
    }

    document.ondragstart = function () {
        return false;
    };

    document.getElementById('image-frame').addEventListener('mousedown', start_drag);
    document.getElementById('image-frame').addEventListener('mousemove', while_drag);
    document.addEventListener('mouseup', stop_drag);

    // Inquiry button onclicked
    $scope.search = function () {
        // Form Data Validation
        var error_message = '';
        if ($scope.inquiry.date == null || $scope.inquiry.date == '') {
            error_message += 'Date is required.\n';
        }
        if ($scope.inquiry.location == null || $scope.inquiry.location == '') {
            error_message += 'Location is required.\n';
        }
        if ($scope.inquiry.floor == null || $scope.inquiry.floor == '') {
            error_message += 'Floor is required.\n';
        }
        if ($scope.inquiry.section == null || $scope.inquiry.section == '') {
            error_message += 'Section is required.\n';
        }
        if ($scope.inquiry.desk == null || $scope.inquiry.desk == '') {
            error_message += 'Desk ID is required.\n';
        } else if ($scope.validateInteger($scope.inquiry.desk)) {
            error_message += 'Desk ID has to be integer.\n';
        }

        if (error_message != '') {
            alert(error_message);
            return false;
        }

        //function to check if desk in an array of deskObjects
        function checkDesk(desks, deskID) {
            for (var i = 0; i < desks.length; i++) {
                if (desks[i]['desk']['deskID'] == deskID) {
                    return true;
                }
            }
            return false;
        }

        //check if desk exists
        var queryParams = {"building": $scope.inquiry.location, "section": $scope.inquiry.section, "floor": $scope.inquiry.floor};
        $http.post(urlBase + "/admin/view_desks", queryParams)
                .then(function (data) {
                    var deskID = $scope.inquiry.floor + '.' + $scope.inquiry.section + '.' + $scope.inquiry.desk;
                    if (data['data']['exception'] == null && data['data'].length != 0 && checkDesk(data['data'], deskID)) {
                        // Set query parameters
                        var queryParams = {"desk": {"building": $scope.inquiry.location, "deskID": deskID}, "date": $scope.inquiry.date};

                        // Send HTTP Post request
                        $http.post(urlBase + "/inquiry/desk", queryParams)
                                .then(function (data) {
                                    if (data['data']['exception'] == null) {
                                        $scope.results = data['data'];
                                        $scope.form_submitted = true;
                                    } else {
                                        console.log(data['data']['exception']);
                                    }
                                });
                    } else {
                        alert('The desk does not exist.');
                        return false;
                    }
                });


    };
});

app.controller('InquiryEmployeeController', function ($scope, $location, $http, $timeout, empInfo, checkAdmin) {
    $scope.inquiryClick = true;
    $scope.adminClick = false;
    $scope.onInquiry = true;
    $scope.onAdmin = false;
    $scope.linkToInqEmp = true;
    
    $scope.inquiry = {};
    // Initialize employee obj 
    $scope.initializeEmp();

    $scope.inquiry.emp = null;

    $http.get(urlBase + "/inquiry/get_all_employee")
            .then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.emps = data['data'];
                    $timeout(function() {
                        $('select').selectpicker('refresh');
                    });
                } else {
                    alert(data['data']['exception']);
                }
            });
            
            
    // set default date to today
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    today = mm + '/' + dd + '/' + yyyy;
    $scope.inquiry = {"date": today};

    // Inquiry button onclicked
    $scope.inquiryEmp = function () {
        // Form Data Validation
        var error_message = '';
        if ($scope.inquiry.date == null || $scope.inquiry.date == '') {
            error_message += 'Date is required.\n';
        }
        if ($scope.emps.length == 0) {
            error_message += 'Employee name is required. Probably no employee has made any reservation yet.';
        } else if ($scope.inquiry.emp == null || $scope.inquiry.emp == '') {
            error_message += 'Employee name is required.\n';
        }
        if (error_message != '') {
            alert(error_message);
            return false;
        }
        
        var queryParams = {
            "inquireDate": $scope.inquiry.date,
            "employeeId": $scope.inquiry.emp
        };
        
        $http.post(urlBase + "/inquiry/employee_reservation", queryParams)
                .then(function(data) {
                    if (data['data']['exception'] == null) {
                        var emp_select = document.getElementById("emp");
                        $scope.emp_select = emp_select.options[emp_select.selectedIndex].text;
                        $scope.entries = data['data'];
                        $scope.form_submitted = true;
                    } else {
                        alert(data['data']['exception']);
                    }
        });
    };
});

app.controller('AdminController', function ($scope, $http, NgTableParams, empInfo, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = true;
    $scope.onInquiry = false;
    $scope.onAdmin = true;
    $scope.linkToAdmin = true;
    
    // Initialize employee obj 
    $scope.initializeEmp();

    // function to change array of string to json
    function toJson(arr) {
        var newArr = [];
        for (var i = 0; i < arr.length; i++) {
            if (newArr.push({'emp_id': arr[i]}))
                ;
        }
        return newArr;
    }

    //fetch all admins from database
    $http.get(urlBase + '/admin/view_admins').then(function (data) {
        if (data['data']['exception'] == null) {
            $scope.admins = toJson(data['data']);
            $scope.adminTable = new NgTableParams({}, {dataset: $scope.admins});
        } else {
            console.log("failed to get admins");
        }
    });

    //switch to add
    $scope.switchToAdd = function () {
        document.getElementById('entries').style.display = 'none';
        document.getElementById('add').style.display = 'inline-block';
    }

    //function to add admin
    $scope.admin = function () {
        // data validation
        var error = '';
        if ($scope.addAdmin.id == null || $scope.addAdmin.id == '') {
            error += 'Employee ID is required\n';
        } else if ($scope.validateInteger($scope.addAdmin.id)) {
            error += 'Employee ID has to be integers.\n';
        }
        if (error != '') {
            alert(error);
            return false;
        }

        $http.get(urlBase + '/admin/add_admin/' + $scope.addAdmin.id).then(function (data) {
            if (data['data']['exception'] == null) {
                alert(data['data']['string']);
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });
    };

    //function to delete an admin 
    $scope.confirmAdminDelete = function (id) {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            $http.get(urlBase + '/admin/delete_admin/' + id['emp_id']).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };
});

app.controller('AdminReservationController', function ($scope, $http, NgTableParams, empInfo, building, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = true;
    $scope.onInquiry = false;
    $scope.onAdmin = true;
    $scope.linkToAdminRes = true;
    
    $scope.resEdit = {};
    // Initialize employee obj 
    $scope.initializeEmp();

    //set params for the followig http request
    var queryParams = {"employeeID": "", "building": "", "deskID": "", "startDate": "", "startTime": "", "endDate": "", "endTime": ""};

    //fetch all reservation entries from database
    $http.post(urlBase + '/admin/reservation_entry/view', queryParams).then(function (data) {
        if (data['data']['exception'] == null) {
            $scope.entries = data['data'];
            $scope.entryTable = new NgTableParams({}, {dataset: $scope.entries});
        } else {
            console.log("failed to get reservation entries");
        }
    });

    // send request for location select options
    building.getBuildings().then(function(result) {
        $scope.sectionMaps = result.sectionMaps;
        
        $scope.buildingsObj = result.buildings;
        
        var locations = [];
        for (var building_name in result.buildings) {
            locations.push(building_name);
        }
        $scope.locations = locations;
    });
    
    $scope.updateFloors = function() {
        var floors = [];
        for (var floor in $scope.buildingsObj[$scope.resEdit.building].floors) {
            floors.push(floor);
        }
        $scope.floors = floors;
        $scope.resEdit.floor = $scope.floors[0];
        
        $scope.updateSections();
    };
    
    $scope.updateSections = function() {
        $scope.sections = $scope.buildingsObj[$scope.resEdit.building].floors[$scope.resEdit.floor];
        if ($scope.sections != null) {
            $scope.resEdit.section = $scope.sections[0];
        }
    };

    // function to split desk id to floor, section, and id
    // returns array
    $scope.splitDeskId = function (desk) {
        var arr = [];
        split = desk.split(".");
        arr['floor'] = split[0];
        arr['section'] = split[1];
        arr['deskID'] = split[2];
        return arr;
    };

    // function to change 24 hour mysql time format to ampm format
    // return hour and ampm
    $scope.convertTime = function (time) {
        var arr = [];
        split = time.split(":");
        hour = split[0];
        hour = parseInt(hour);
        if (hour < 12) {
            if (hour == 0) {
                arr['hour'] = "12";
            } else {
                arr['hour'] = hour.toString();
            }
            arr['ampm'] = "AM";
        } else {
            if (hour == 12) {
                arr['hour'] = "12";
            } else {
                hour = hour - 12;
                arr['hour'] = hour.toString();
            }
            arr['ampm'] = "PM";
        }
        return arr;
    };

    // function to convert Date to the format yyyy
    $scope.convertDate = function (date) {
        split = date.split('-');
        yyyy = split[0];
        mm = split[1];
        dd = split[2];
        return mm + '/' + dd + '/' + yyyy;
    };

    // function to convert date back to the 
    $scope.convertBackDate = function (date) {
        split = date.split('/');
        mm = split[0];
        dd = split[1];
        yyyy = split[2];
        return yyyy + '-' + mm + '-' + dd;
    };

    $scope.switchToEdit = function (oldEntry) {
        $scope.oldEntry = oldEntry;
        var deskSplit = $scope.splitDeskId(oldEntry.desk.deskID);
        $scope.resEdit.desk = deskSplit.deskID;
        $scope.resEdit.building = oldEntry.desk.building;
        $scope.updateFloors();
        $scope.resEdit.floor = deskSplit.floor;
        $scope.resEdit.section = deskSplit.section;
        $scope.resEdit.emp_name = oldEntry.base.employee.name;
        $scope.resEdit.start_date = $scope.convertDate(oldEntry.base.startDate);
        $scope.resEdit.end_date = $scope.convertDate(oldEntry.base.endDate);
        var convertedStartTime = $scope.convertTime(oldEntry.base.startTime);
        $scope.resEdit.start_time = convertedStartTime.hour;
        $scope.resEdit.start_ampm = convertedStartTime.ampm;
        var convertedEndTime = $scope.convertTime(oldEntry.base.endTime);
        $scope.resEdit.end_time = convertedEndTime.hour;
        $scope.resEdit.end_ampm = convertedEndTime.ampm;
        document.getElementById('entries').style.display = 'none';
        document.getElementById('edit_res').style.display = 'inline-block';
    };

    $scope.editRes = function () {
        // Convert AM/PM to 24 hour clock
        var starttime = $scope.resEdit.start_time;
        var endtime = $scope.resEdit.end_time;
         if ($scope.resEdit.start_ampm === "PM") {
            if (parseInt(starttime) < 12) {
                starttime = (parseInt(starttime) + 12).toString();
            }
        } else {
            if (parseInt(starttime) === 12) {
                starttime = "00";
            }
            if (parseInt(starttime) < 10) {
                startime = "0" + starttime
            }
        }
        if ($scope.resEdit.end_ampm === "PM") {
            if (parseInt(endtime) < 12) {
                endtime = (parseInt(endtime) + 12).toString();
            }
        } else {
            if (parseInt(starttime) < 10) {
                if (parseInt(endtime) === 12) {
                    endtime = "00";
                }
                startime = "0" + starttime
            }
        }

        // Check that dates work
        var sDate = new Date($scope.resEdit.start_date);
        var eDate = new Date($scope.resEdit.end_date);
        var currDate = new Date();
        var daysBooked = (eDate - sDate) / 86400000;

        // Error cases
        var error = '';
        if ($scope.resEdit.desk == null || $scope.resEdit.desk == '') {
            error += 'Desk ID is required\n'
        } else if ($scope.validateInteger($scope.resEdit.desk)) {
            error += 'Desk ID has to be integers.\n';
        }
        if (sDate > eDate) {
            error += "Start date begins after end date.\n";
        }
        if (daysBooked > 5) {
            error += "You are only allowed a booking of 5 days maximum.\n";
        }
        if ((sDate - currDate) / 86400000 > 30) {
            error += "You are not allowed to book more than 30 days ahead of time.\n";
        }
        if ((sDate - eDate === 0) && (parseInt(starttime) >= parseInt(endtime))) {
            error += "Please specify a valid time range.\n";
        }

        if (error != "") {
            alert(error);
            return false;
        }

        var deskID = $scope.resEdit.floor + '.' + $scope.resEdit.section + '.' + $scope.resEdit.desk;

        var newEntry = {
            "base": {
                "employee": $scope.oldEntry.base.employee,
                "startDate": $scope.convertBackDate($scope.resEdit.start_date),
                "startTime": starttime + ':00:00',
                "endDate": $scope.convertBackDate($scope.resEdit.end_date),
                "endTime": endtime + ':00:00'
            },
            "desk": {
                "building": $scope.resEdit.building,
                "deskID": deskID
            }
        };

        var queryParams = {
            "oldEntry": $scope.oldEntry,
            "newEntry": newEntry,
            "admin": $scope.employee
        };
        
        $http.post(urlBase + "/admin/reservation_entry/modify", queryParams).then(function (data) {
            if (data['data']['exception'] == null) {
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });
        

    };


    //function to delete an entry
    $scope.confirmResDelete = function (reserveEntry) {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"reserveEntry": reserveEntry, "admin": $scope.employee};
            $http.post(urlBase + '/admin/reservation_entry/delete', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };

});

app.controller('AdminDeskController', function ($scope, $http, $location, NgTableParams, empInfo, building, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = true;
    $scope.onInquiry = false;
    $scope.onAdmin = true;
    $scope.linkToAdminDesk = true;
    
    // Initialize employee obj 
    $scope.initializeEmp();

    // set variable
    $scope.addDesk = {};
    $scope.editDesk = {};
    $scope.delDesk = {};

    // function to split desk id to floor, section, and id
    // returns new array
    $scope.splitDeskIds = function (arr) {
        var newArr = [];
        for (var i = 0; i < arr.length; i += 1) {
            split = arr[i]['desk']['deskID'].split(".");
            arr[i]['floor'] = split[0];
            arr[i]['section'] = split[1];
            arr[i]['deskID'] = parseInt(split[2]);
            newArr.push(arr[i]);
        }
        return newArr;
    };

    //set params for the following http request
    var queryParamsForView = {"building": "", "floor": "", "section": ""};

    //fetch all the desks from database 
    $http.post(urlBase + '/admin/view_desks', queryParamsForView).then(function (data) {
        if (data['data']['exception'] == null) {
            var newDeskArrays = $scope.splitDeskIds(data['data']);
            $scope.desks = newDeskArrays;
            $scope.deskTable = new NgTableParams({}, {dataset: $scope.desks});
        } else {
            console.log(data['data']['exception']);
        }
    });
    
    //function to open a map in new window
    $scope.openMap = function(url) {
        window.open(url);
    };

   // send request for location select options
    building.getBuildings().then(function(result) {
        $scope.sectionMaps = result.sectionMaps;
        
        $scope.buildingsObj = result.buildings;
        
        var locations = [];
        for (var building_name in result.buildings) {
            locations.push(building_name);
        }
        $scope.locations = locations;
        $scope.addDesk.location = $scope.locations[0];
        $scope.delDesk.location = $scope.locations[0];
        
        $scope.updateFloors($scope.addDesk);
        $scope.updateFloors($scope.delDesk);
    });
    
    $scope.updateFloors = function(model) {
        var floors = [];
        for (var floor in $scope.buildingsObj[model.location].floors) {
            floors.push(floor);
        }
        $scope.floors = floors;
        model.floor = $scope.floors[0];
        
        $scope.updateSections(model);
    };
    
    $scope.updateSections = function(model) {
        $scope.sections = $scope.buildingsObj[model.location].floors[model.floor];
        if ($scope.sections != null) {
            model.section = $scope.sections[0];
        }
    };

    $scope.switchToAdd = function () {
        document.getElementById('desk').style.display = 'none';
        document.getElementById('add').style.display = 'inline-block';
        document.getElementById('edit').style.display = 'none';
        document.getElementById('del').style.display = 'none';

    };
    $scope.switchToEdit = function (oldEntry) {
        $scope.oldDesk = oldEntry;
        $scope.editDesk.location = oldEntry.desk.building;
        $scope.updateFloors($scope.editDesk);
        $scope.editDesk.floor = oldEntry.floor;
        $scope.editDesk.section = oldEntry.section;
        $scope.editDesk.deskID = oldEntry.deskID;
        $scope.editDesk.mapurl = oldEntry.mapUrl;
        document.getElementById('desk').style.display = 'none';
        document.getElementById('add').style.display = 'none';
        document.getElementById('edit').style.display = 'inline-block';
        document.getElementById('del').style.display = 'none';
    };
    $scope.switchToDel = function () {
        document.getElementById('desk').style.display = 'none';
        document.getElementById('add').style.display = 'none';
        document.getElementById('edit').style.display = 'none';
        document.getElementById('del').style.display = 'inline-block';
    };
    //function to add desk
    $scope.deskAdd = function () {
        // data validation
        var error = '';
        if ($scope.addDesk.floor == null || $scope.addDesk.floor == '') {
            error += 'Floor is required.\n'
        }
        if ($scope.addDesk.section == null || $scope.addDesk.section == '') {
            error += 'Section is required.\n'
        }
        if ($scope.addDesk.desk_from == null || $scope.addDesk.desk_from == '') {
            error += 'Desk ID (from) is required.\n'
        } else if ($scope.validateInteger($scope.addDesk.desk_from)) {
            error += 'Desk ID (from) has be integers.\n';
        }
        if ($scope.addDesk.desk_to == null || $scope.addDesk.desk_to == '') {
            error += 'Desk ID (to) is required\n'
        } else if ($scope.validateInteger($scope.addDesk.desk_to)) {
            error += 'Desk ID (to) has to be integers.\n';
        }
        if ($scope.addDesk.mapurl == null || $scope.addDesk.mapurl == '') {
            var isConfirmed = confirm("Reservation confirmation page would show no map for these desks without map URL. Would you like to proceed?");
            if (isConfirmed) {
                $scope.addDesk.mapurl = '';
            } else {
                return false;
            }
        } else if ($scope.validateMapUrl($scope.addDesk.mapurl)) {
            error += "Map URL is not valid.\n"
        }
        if (error != '') {
            alert(error);
            return false;
        }

        /* dealing with mapID amd exception handling
        var map = document.getElementById("image").files[0];
        //console.log($scope.addDesk);
        //var imageID = $scope.addDesk.mapID.name;
        var ad = {"building": $scope.addDesk.location, "floor": $scope.addDesk.floor, "section": $scope.addDesk.section, "from": $scope.addDesk.desk_from, "to": $scope.addDesk.desk_to};

        var formData = new FormData();
        formData.append("file", map);
        formData.append('ad', new Blob([JSON.stringify(ad)], {
            type: "application/json"
        }));

        $http.post(urlBase + '/admin/add_desks', formData, {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        }).then(function (data) {
            if (data['data']['exception'] == null) {
                alert('Successfully added desks!');
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });*/
        var queryParamsForAdd = {
            "building": $scope.addDesk.location,
            "floor": $scope.addDesk.floor,
            "section": $scope.addDesk.section,
            "imageUrl": $scope.addDesk.mapurl,
            "from": $scope.addDesk.desk_from,
            "to": $scope.addDesk.desk_to
        };
        
        $http.post(urlBase + '/admin/add_desks', queryParamsForAdd)
                .then(function(data) {
                    if (data['data']['exception'] == null) {
                        alert(data['data']['string']);
                        $scope.reload();
                    } else {
                        alert(data['data']['exception']);
                    }
        });
    };

    $scope.deskEdit = function () {
        var error = '';
        if ($scope.editDesk.floor == null || $scope.editDesk.floor == '') {
            error += 'Floor is required.\n';
        }
        if ($scope.editDesk.section == null || $scope.editDesk.section == '') {
            error += 'Section is required.\n';
        }
        if ($scope.editDesk.deskID == null || $scope.editDesk.deskID == '') {
            error += 'Desk ID is required.\n';
        } else if ($scope.validateInteger($scope.editDesk.deskID.toString())) {
            error += 'Desk ID has be integers.\n';
        }
        if ($scope.editDesk.mapurl == null || $scope.editDesk.mapurl == '') {
            $scope.editDesk.mapurl = '';
            var isConfirmed = confirm("Reservation confirmation page would show no map for this desk without map URL. Would you like to proceed?");
            if (isConfirmed) {
                $scope.addDesk.mapurl = '';
            } else {
                return false;
            }
        } else if ($scope.validateMapUrl($scope.editDesk.mapurl)) {
            error += "Map URL is not valid.\n";
        }
        if (error != '') {
            alert(error);
            return false;
        }
        var queryParamsForEdit = {
            "oldDesk": {
                "building": $scope.oldDesk.desk.building,
                "deskID": $scope.oldDesk.floor + '.' + $scope.oldDesk.section + '.' + $scope.oldDesk.deskID
            },
            "newDesk": {
                "building": $scope.editDesk.location,
                "deskID": $scope.editDesk.floor + '.' + $scope.editDesk.section + '.' + $scope.editDesk.deskID
            },
            "mapURL": $scope.editDesk.mapurl
        };
        
        $http.post(urlBase + '/admin/modify_desks', queryParamsForEdit).then(function (data) {
            if (data['data']['exception'] == null) {
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });
    };

    //function to delete a desk
    $scope.confirmDeskDelete = function (floor, section, id, building) {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"building": building, "floor": floor, "section": section, "from": id, "to": id};
            $http.post(urlBase + '/admin/delete_desks', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };

    $scope.confirmDesksDelete = function () {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"building": $scope.delDesk.location, "floor": $scope.delDesk.floor, "section": $scope.delDesk.section, "from": $scope.delDesk.desk_from, "to": $scope.delDesk.desk_to};
            $http.post(urlBase + '/admin/delete_desks', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };


});

app.controller('AdminLocationController', function ($scope, $http, $location, NgTableParams, $route, empInfo, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = true;
    $scope.onInquiry = false;
    $scope.onAdmin = true;
    $scope.linkToAdminLoc = true;
    
    // Initialize employee obj 
    $scope.initializeEmp();

    $scope.editLoc = {};
    //show map image icon on admin location page condition

    //fetch all the locations when the page loads
    var queryParamsForViewLoc = {"officename": "", "address": ""};
    $http.get(urlBase + '/admin/view_buildings', queryParamsForViewLoc).then(function (data) {
        if (data['data']['exception'] == null) {
            $scope.locations = data['data'];
            $scope.locationTable = new NgTableParams({}, {dataset: $scope.locations});
        } else {
            console.log(data['data']['exception']);
        }
    });

    //switch to add
    $scope.switchToAdd = function () {
        document.getElementById('loc').style.display = 'none';
        document.getElementById('add').style.display = 'inline-block';
        document.getElementById('edit').style.display = 'none';
    };

    //function to add a location
    $scope.locationAdd = function () {
        var error = '';
        if ($scope.addLoc.name == null || $scope.addLoc.name == '') {
            error += 'Building Name is required\n';
        }
        ;
        if ($scope.addLoc.address == null || $scope.addLoc.address == '') {
            error += 'Address is required\n';
        }
        ;
        if (error != '') {
            alert(error);
            return false;
        }
        ;
        var queryParams = {"officename": $scope.addLoc.name, "address": $scope.addLoc.address};
        $http.post(urlBase + '/admin/add_building', queryParams).then(function (data) {
            if (data['data']['exception'] == null) {
                alert(data['data']['string']);
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });
    };
    
    
    $scope.mapIcon = function(url) {
        window.open(url);
    };

    //switch layout to edit a location
    $scope.switchToEdit = function (location) {
        $scope.editLoc.name = location.officename;
        $scope.editLoc.address = location.address;
                //get map url for mapIcon()
        
            document.getElementById('loc').style.display = 'none';
            document.getElementById('add').style.display = 'none';
            document.getElementById('edit').style.display = 'inline-block';

            var buildingName = location.officename;
            $http.get(urlBase + '/admin/view_all_sections/'+ buildingName).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.floormap = data['data'];
                    $scope.floormapTable = new NgTableParams({}, {dataset: $scope.floormap});
                } else {
                    console.log(data['data']['exception']);
                }
        });
    };

    //function to edit a location
    $scope.locationEdit = function () {
        var officename = document.getElementById('edit-officename').value;
        var error = '';
        if (officename == null || officename == '') {
            error += 'Building Name is required\n';
        }
        ;
        if ($scope.editLoc.address == null || $scope.editLoc.address == '') {
            error += 'Address is required\n';
        }
        ;
        if (error != '') {
            alert(error);
            return false;
        }
        ;
        var queryParams = {"officename": officename, "address": $scope.editLoc.address};
        $http.post(urlBase + '/admin/modify_building', queryParams).then(function (data) {
            if (data['data']['exception'] == null) {
                $scope.reload();
            } else {
                alert(data['data']['exception']);
            }
        });
    };

    //function to delete a location
    $scope.confirmLocDelete = function (location) {
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"officename": location.officename, "address": location.address};
            $http.post(urlBase + '/admin/delete_building', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $scope.reload();
                } else {
                    alert(data['data']['exception']);
                }
            });
        }
    };
    
// Add Section Map in Location page --------------------------------
    $scope.addLocSection = function() {};
    $scope.editLocSection = function() {};
    $scope.editMap = function() {};
    $scope.addMap = function() {};
    $scope.floorMapEdit = function (floormap) {
        $scope.editMap.building = floormap.building;
        $scope.editLocSection.sec_floor = floormap.floor;
        $scope.editLocSection.sec_section = floormap.section;
        $scope.editLocSection.mapURL = floormap.mapURL;
        document.getElementById('edit').style.display = 'none';
        document.getElementById('edit_map').style.display = 'inline-block';
        document.getElementById('edit_section_add').style.display = 'none';
    };
    
    $scope.submitLocSecDelete = function (floor, section) {
        var officename = document.getElementById('edit-officename').value;
        var x = confirm("Are you sure you want to delete?");
        if (x == true) {
            var queryParams = {"building": officename, "floor": floor.toString(), "section": section};
            $http.post(urlBase + '/admin/delete_section', queryParams).then(function (data) {
                if (data['data']['exception'] == null) {
                    $http.get(urlBase + '/admin/view_all_sections/'+ officename).then(function (data) {
                    if (data['data']['exception'] == null) {
                        $scope.floormap = data['data'];
                        $scope.floormapTable = new NgTableParams({}, {dataset: $scope.floormap});
                    }
                    });
                    document.getElementById('edit').style.display = 'inline-block';
                    document.getElementById('edit_section_add').style.display = 'none';
                    document.getElementById('edit_map').style.display='none';
                } else {
                    alert(data['data']['exception']);
                };
            });
        };
    };
    
    $scope.submitLocSecEdit = function() {
        var officename = document.getElementById('editmap-officename').value;
        var error = "";
        if ($scope.editLocSection.mapURL == null || $scope.editLocSection.mapURL == "") {
            var isConfirmed = confirm("Inquiry (by Desk ID) and Reserve page would show no map for this setion without map URL. Would you like to proceed?");
            if (isConfirmed) {
                $scope.editLocSection.mapURL = '';
            } else {
                return false;
            }
        }
        else if ($scope.validateMapUrl($scope.editLocSection.mapURL)) {
            error += "Map URL is not valid.\n"
        }
        if (error != "") {
            alert(error);
            return false;
        }
        ;
        var queryParams = {"building": officename, "floor": $scope.editLocSection.sec_floor, "section": $scope.editLocSection.sec_section, "mapURL": $scope.editLocSection.mapURL};
        $http.post(urlBase + '/admin/modify_section_map', queryParams).then(function (data) {
            if (data['data']['exception'] == null) {
                //alert('Successfully edited a Section Map.');
                $http.get(urlBase + '/admin/view_all_sections/'+ officename).then(function (data) {
                    if (data['data']['exception'] == null) {
                        $scope.floormap = data['data'];
                        $scope.floormapTable = new NgTableParams({}, {dataset: $scope.floormap});
                    }
                });
                document.getElementById('edit').style.display = 'inline-block';
                document.getElementById('edit_section_add').style.display = 'none';
                document.getElementById('edit_map').style.display='none';
            } else {
                alert(data['data']['exception']);
            }
        });
    };
    
    $scope.submitLocSecAdd = function () {
        var officename = document.getElementById('addmap-officename').value;
        var error = '';
        if (officename == null || officename == '') {
            error += 'Building Name is required\n';
        }
        ;
        if ($scope.addLocSection.sec_floor == null || $scope.addLocSection.sec_floor == '') {
            error += 'Floor is required\n';
        }
        ;
        if ($scope.addLocSection.sec_section == null || $scope.addLocSection.sec_section == '') {
            error += 'Section is required\n';
        }
        ;
        if($scope.addLocSection.mapID == null || $scope.addLocSection.mapID == '') {
            var isConfirmed = confirm("Inquiry (by Desk ID) and Reserve page would show no map for this setion without map URL. Would you like to proceed?");
            if (isConfirmed) {
                $scope.addLocSection.mapID = '';
            } else {
                return false;
            }
        }
        else if ($scope.validateMapUrl($scope.addLocSection.mapID)) {
            error += "Map URL is not valid.\n";
        }
        if (error !== '') {
            alert(error);
            return false;
        }
        ;
        var queryParams = {"building": officename, "floor": $scope.addLocSection.sec_floor, "section": $scope.addLocSection.sec_section, "mapURL": $scope.addLocSection.mapID};
        $http.post(urlBase + '/admin/add_new_section', queryParams).then(function (data) {
            if (data['data']['exception'] == null) {
                $http.get(urlBase + '/admin/view_all_sections/'+ officename).then(function (data) {
                    if (data['data']['exception'] == null) {
                        $scope.floormap = data['data'];
                        $scope.floormapTable = new NgTableParams({}, {dataset: $scope.floormap});
                    }
                });
                document.getElementById('edit').style.display = 'inline-block';
                document.getElementById('edit_section_add').style.display = 'none';
                document.getElementById('edit_map').style.display='none';
                
            } else {
                alert(data['data']['exception']);
            };
        });
    };

    $scope.reloadToEdit = function ()    {
        document.getElementById('edit').style.display = 'inline-block';
        document.getElementById('edit_section_add').style.display = 'none';
        document.getElementById('edit_map').style.display='none';
    };
    
    $scope.reloadToAddSection = function () {
        $scope.addMap.building = document.getElementById('edit-officename').value;
        $scope.addLocSection.sec_floor = null;
        $scope.addLocSection.sec_section = null;
        $scope.addLocSection.mapID = null;
        document.getElementById('edit').style.display = 'none';
        document.getElementById('edit_section_add').style.display = 'inline-block';
        document.getElementById('edit_map').style.display = 'none';
    };
});

app.controller('AdminDatabaseController', function ($scope, $location, $http, empInfo, checkAdmin) {
    $scope.inquiryClick = false;
    $scope.adminClick = true;
    $scope.onInquiry = false;
    $scope.onAdmin = true;
    $scope.linkToAdminDB = true;
    
    $scope.logMessage = '';
    // Initialize employee obj 
    $scope.initializeEmp();
    
    $scope.archive = function() {
        $http.get(urlBase + '/admin/archive_inactive_bookings')
                .then(function(data) {
                    var currentdate = new Date();
                    var datetime = (currentdate.getMonth() < 9?'0':'') + (currentdate.getMonth() + 1) + "/"
                            + (currentdate.getDate() < 10?'0':'') + currentdate.getDate() + "/"
                            + currentdate.getFullYear() + "@"
                            + (currentdate.getHours()<10?'0':'') + currentdate.getHours() + ":"
                            + (currentdate.getMinutes()<10?'0':'') + currentdate.getMinutes() + ":"
                            + (currentdate.getSeconds()<10?'0':'') + currentdate.getSeconds();
                    if (data['data']['exception'] == null) {
                        $scope.logMessage += datetime + ' - ' + data['data']['string'] + '\n';
                    } else {
                        $scope.logMessage += datetime + ' - ' + data['data']['exception'] + '\n';
                    }
        });  
    };
    
    $scope.dbBackup = function() {
        $http.get(urlBase + '/admin/backup_database')
                .then(function(data) {
                    var currentdate = new Date();
                    var datetime = (currentdate.getMonth() < 9?'0':'') + (currentdate.getMonth() + 1) + "/"
                            + (currentdate.getDate() < 10?'0':'') + currentdate.getDate() + "/"
                            + currentdate.getFullYear() + "@"
                            + (currentdate.getHours()<10?'0':'') + currentdate.getHours() + ":"
                            + (currentdate.getMinutes()<10?'0':'') + currentdate.getMinutes() + ":"
                            + (currentdate.getSeconds()<10?'0':'') + currentdate.getSeconds();
                    if (data['data']['exception'] == null) {
                        $scope.logMessage += datetime + ' - ' + data['data']['string'] + '\n';
                    } else {
                        $scope.logMessage += datetime + ' - ' + data['data']['exception'] + '\n';
                    }
        });
    };
});

app.controller('AdminRegistrationController', function ($scope, $location, $http, empInfo, checkAdmin) {
    // Initialize employee obj
    $scope.initializeEmp();
    
    $scope.admin = {};
    // Admin Registration button onclicked
    $scope.register = function () {
        // Form Data Validation
        var message = '';
        if ($scope.admin.password == null || $scope.admin.password == '') {
            alert('Please enter password.');
            return false;
        } else {
            // Send HTTP request to verify password input
            $http.get(urlBase + '/admin/verify_admin_password/' + $scope.admin.password)
                    .then(function (data) {
                        if (data['data']['exception'] == null && data['data']==true) {
                            // If password is correct, send HTTP request to add as an admin
                            $http.get(urlBase + '/admin/add_admin/' + $scope.employee['id']).then(function (data) {
                                if (data['data']['exception'] == null) {
                                    alert('Successfully registered as admin!');
                                    $scope.go('/');
                                } else {
                                    alert(data['data']['exception']);
                                }
                            });
                        } else {
                            alert('Incorrect password');
                        }
                    });
        }
    };
});
