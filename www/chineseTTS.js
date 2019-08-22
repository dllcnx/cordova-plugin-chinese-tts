var exec = require('cordova/exec');

exports.init = function (arg0, success, error) {
    exec(success, error, 'ChineseTTS', 'init', [arg0]);
}

exports.speak = function (arg0, success, error) {
    exec(success, error, 'ChineseTTS', 'speak', [arg0]);
}

exports.state = function(arg0, success, error){
    exec(success, error, 'ChineseTTS', 'state', [arg0]);
}
