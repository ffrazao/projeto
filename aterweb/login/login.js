(function(pNmModulo, pNmController, pNmFormulario) {

  'use strict';

angular.module(pNmModulo).controller(pNmController,
      ['$scope', '$location', '$modal', 'toastr', '$state', '$http', 'TokenStorage',
    function ($scope, $location, $modal, toastr, $state, $http, TokenStorage) {
    $scope.iniciar = function() {
      $scope.registroOrig = $location.search();
      $scope.reiniciar();
    };
    $scope.reiniciar = function() {
      $scope.submitted = false;
      $scope.registro = angular.copy($scope.registroOrig);
      if ($scope.$parent.loginForm) {
        $scope.$parent.loginForm.$setPristine();
      }
      $('#usuario').focus();
    };

    $scope.iniciar();

  // métodos de apoio
  $scope.submitForm = function () {
    if (!$scope.$parent.loginForm.$valid) {
      $scope.submitted = true;
      toastr.error('Verifique os campos marcados', 'Erro');
      //$scope.mensagens.push({ tipo: 'danger', texto: 'Verifique os campos marcados' });
      return;
    }
    //$scope.renoveSuaSenha();
    $http.post('http://localhost:8080/api/login', { nomeUsuario: $scope.registro.usuario, senha: $scope.registro.senha }).success(function (result, status, headers) {
        $scope.authenticated = true;
        TokenStorage.store(headers('X-Auth-Token'));
        
        // For display purposes only
        $scope.token = JSON.parse(atob(TokenStorage.retrieve().split('.')[0]));
    });
};

$scope.mensagens = [
    // { tipo: 'danger', texto: 'Oh snap! Change a few things up and try submitting again.' },
    // { tipo: 'success', texto: 'Well done! You successfully read this important alert message.' }
    ];

    $scope.closeAlert = function(index) {
      $scope.mensagens.splice(index, 1);
    };

    $scope.esqueciMinhaSenha = function (size) {
      var modalInstance = $modal.open({
        templateUrl: 'login/esqueci-minha-senha.html',
        controller: 'EsqueciMinhaSenhaCtrl',
        size: size
      });

      modalInstance.result.then(function () {
        $('#usuario').focus();
      }, function () {
      //$log.info('Modal dismissed at: ' + new Date());
    });
    };

    $scope.renoveSuaSenha = function (size) {
      var modalInstance = $modal.open({
        templateUrl: 'login/renove-sua-senha.html',
        controller: 'RenoveSuaSenhaCtrl',
        size: size,
        resolve: {
          registroOrig: function () {
            return $scope.registro;
          }
        }
      });

      modalInstance.result.then(function (registro) {
        $('#usuario').focus();
        $scope.registro.senha = angular.copy(registro.novaSenha);
      }, function () {
      //$log.info('Modal dismissed at: ' + new Date());
    });
    };

  }]);

})('principal', 'LoginCtrl', 'Efetuar Login');