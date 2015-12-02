(function(pNmModulo, pNmController, pNmFormulario) {

  'use strict';

angular.module(pNmModulo).controller(pNmController,
      ['$scope', '$uibModalInstance', 'toastr',
    function ($scope, $uibModalInstance, toastr) {
    $scope.iniciar = function() {
      $scope.registroOrig = {};
      $scope.reiniciar();
    };
    $scope.reiniciar = function() {
      $scope.submitted = false;
      $scope.registro = angular.copy($scope.registroOrig);
      if ($scope.$parent.esqueciMinhaSenhaForm) {
        $scope.$parent.esqueciMinhaSenhaForm.$setPristine();
      }
      $('#email').focus();
    };

    $scope.iniciar();

  // métodos de apoio
  $scope.submitForm = function () {
    if (!$scope.$parent.esqueciMinhaSenhaForm.$valid) {
      $scope.submitted = true;
      toastr.error('Verifique os campos marcados', 'Erro');
      return;
    }
    toastr.success('Foi encaminhado um e-mail com instrucoes para recuperar seu acesso ao sistema');
    $uibModalInstance.close();
  };

  $scope.cancelar = function () {
    $uibModalInstance.dismiss('cancel');
  };

}]);

})('principal', 'EsqueciMinhaSenhaCtrl', 'Recuperar Senha');