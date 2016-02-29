(function() {
  window.jasmine = jasmineRequire.core(jasmineRequire);

  /**
   * Create the Jasmine environment. This is used to run all specs in a project.
   */
  var env = jasmine.getEnv();

  /**
   * ## The Global Interface
   *
   * Build up the functions that will be exposed as the Jasmine public interface. A project can customize, rename or alias any of these functions as desired, provided the implementation remains unchanged.
   */
  var jasmineInterface = jasmineRequire.interface(jasmine, env);

  /**
   * Add all of the Jasmine global/public interface to the global scope, so a project can use the public interface directly. For example, calling `describe` in specs instead of `jasmine.getEnv().describe`.
   */
  extend(window, jasmineInterface);

  var jasmineReq = getJasmineRequireObj();

  jasmineReq.console(jasmineReq, window.jasmine);

  env.addReporter(getJasmineRequireObj().ConsoleReporter()({
    print: function(it){ console.log(it); },
    showColors: true
  }));

  function extend(destination, source) {
    for (var property in source) destination[property] = source[property];
    return destination;
  }

  function getJasmineRequireObj() {
    if (typeof module !== 'undefined' && module.exports) {
      return exports;
    } else {
      window.jasmineRequire = window.jasmineRequire || {};
      return window.jasmineRequire;
    }
  }

}());
