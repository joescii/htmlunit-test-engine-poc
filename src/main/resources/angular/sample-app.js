angular.module("MyApp", [])

.controller("MyController", ["$scope", function($scope){
  $scope.fun = function() {
    return "success";
  }
}]);