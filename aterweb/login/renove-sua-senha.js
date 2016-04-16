(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$uibModalInstance', 'toastr', 'registroOrig', '$http', 'CestaDeValores', 
    function ($scope, $uibModalInstance, toastr, registroOrig, $http,  CestaDeValores) {

        $scope.reiniciar = function() {
            $scope.submitted = false;
            $scope.registro = {};
            angular.extend($scope.registro, registroOrig);
            var pss = CestaDeValores.pegarValor('password');
            if (pss) {
                $scope.registro.password = pss.valor;
                CestaDeValores.removerValor('password');
            }
            if ($scope.$parent.renoveSuaSenhaForm) {
                $scope.$parent.renoveSuaSenhaForm.$setPristine();
            }
            $('#newPassword').focus();
        };

        // métodos de apoio
        $scope.submitForm = function () {
            if (!$scope.$parent.renoveSuaSenhaForm.$valid) {
                $scope.submitted = true;
                toastr.error('Verifique os campos marcados', 'Erro');
                return;
            }
            $http.post($scope.servicoUrl + '/api/renovar-senha', $scope.registro).success(function (resposta) {
                if (resposta && resposta.mensagem === 'OK') {
                    toastr.success('Sua senha foi renovada');
                    $uibModalInstance.close(angular.copy($scope.registro));
                } else {
                    toastr.error(resposta.mensagem, 'Erro ao renovar senha');
                }
            });
        };

        $scope.cancelar = function () {
            $uibModalInstance.dismiss('cancel');
        };
        
        if (!$scope.registro) {
            $scope.reiniciar();
        }
    }
]);

})('principal', 'RenoveSuaSenhaCtrl', 'Renovação de Senha');