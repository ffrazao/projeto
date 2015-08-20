(function () {

  'use strict';

  angular.module('principal').controller('RenoveSuaSenhaCtrl', ['$scope', '$modalInstance', 'toastr', 'registroOrig', 
    function ($scope, $modalInstance, toastr, registroOrig) {
    $scope.iniciar = function() {
    //$scope.registroOrig = {};
    console.log("ddd", registroOrig);
    $scope.reiniciar();
  };

  $scope.reiniciar = function() {
    $scope.submitted = false;
    $scope.registro = angular.copy(registroOrig);
    if ($scope.$parent.renoveSuaSenhaForm) {
      $scope.$parent.renoveSuaSenhaForm.$setPristine();
    }
    $('#novaSenha').focus();
  };
  $scope.iniciar();

  // métodos de apoio
  $scope.submitForm = function () {
    if (!$scope.$parent.renoveSuaSenhaForm.$valid) {
      $scope.submitted = true;
      toastr.error('Verifique os campos marcados', 'Erro');
      return;
    }
    toastr.success('Sua senha foi renovada');
    console.log('retornando ', angular.copy($scope.registro));
    $modalInstance.close(angular.copy($scope.registro));
  };
  $scope.cancelar = function () {
    $modalInstance.dismiss('cancel');
  };
}]);
  
})();