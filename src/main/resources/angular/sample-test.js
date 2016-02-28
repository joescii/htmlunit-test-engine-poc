describe("Angular tests", function(){
  var scope;

  beforeEach(function(){
    module("MyApp")
  });

  beforeEach(inject(["$rootScope", "$controller", function($rootScope, $controller){
    scope = $rootScope.$new();
    $controller("MyController", {$scope: scope});
  }]));

  it("should pass a test", function(){
    expect(scope.fun()).toBe("success");
  });

  it("should fail a test", function(){
    expect(scope.fun()).toBe("fail");
  });
});