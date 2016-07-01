(function(pNmModulo, pNmController, pNmFormulario) {

  'use strict';

angular.module(pNmModulo).controller(pNmController,
      ['$scope', '$uibModalInstance', 'toastr', 'SegurancaSrv',
    function ($scope, $uibModalInstance, toastr, SegurancaSrv) {
    'ngInject';
    
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

    SegurancaSrv.esqueciSenha($scope.registro.email).success(function (resposta) {
      if (resposta && resposta.mensagem === 'OK') {
        toastr.success('Foi encaminhado um e-mail com instrucoes para recuperar seu acesso ao sistema');
        $uibModalInstance.close();
      } else {
        toastr.error(resposta.mensagem, 'Erro ao renovar senha');
      }
    });
  };

  $scope.cancelar = function () {
    $uibModalInstance.dismiss('cancel');
  };

}]);

})('principal', 'EsqueciMinhaSenhaCtrl', 'Recuperar Senha');