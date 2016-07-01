(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$rootScope', 'toastr', 'UtilSrv', 'PerfilSrv',
    function($scope, $rootScope, toastr, UtilSrv, PerfilSrv) {
    'ngInject';
    
    // inicio rotinas de apoio
    $scope.exibeConceder = function (indice, elemento) {
        var campo = 'funcionalidadeComando_' + indice + '_' + elemento;
        return $scope.frm.formulario[campo] && $scope.frm.formulario[campo].$modelValue === true;
    };

    $scope.exibeErroConceder = function (indice, elemento, conceder) {
        return $scope.exibeConceder(indice, elemento) && (angular.isUndefined(conceder) || conceder === null);
    };

    $scope.funcionalidadeComandoCompare = function(obj1, obj2) {
        return obj1.funcionalidadeComando.id === obj2.funcionalidadeComando.id;
    };

    var adicionaFuncionalidade = function(funcionalidade, acao) {
        if (!funcionalidade) {
            return;
        }
        if (!$scope.cadastro.registro.perfilFuncionalidadeComandoList) {
            $scope.cadastro.registro.perfilFuncionalidadeComandoList = [];
        }
        var i, j, encontrou;
        for (i = 0; i < funcionalidade.length; i++) {
            encontrou = false;
            funcionalidade[i]['conceder'] = acao;
            for (j = 0; j < $scope.cadastro.registro.perfilFuncionalidadeComandoList.length; j++) {
                if (funcionalidade[i].funcionalidadeComando.id === $scope.cadastro.registro.perfilFuncionalidadeComandoList[j].funcionalidadeComando.id) {
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                $scope.cadastro.registro.perfilFuncionalidadeComandoList.push(funcionalidade[i]);
            }
        }
    };

    $scope.concederFuncionalidade = function(funcionalidade) {
        return adicionaFuncionalidade(funcionalidade, 'S');
    };

    $scope.negarFuncionalidade = function(funcionalidade) {
        return adicionaFuncionalidade(funcionalidade, 'N');
    };

    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador

    // fim das operaçoes atribuidas ao navagador

} // fim função
]);

})('perfil', 'PerfilFuncionalidadeComandoCtrl', 'Funcionalidades do Perfil!');