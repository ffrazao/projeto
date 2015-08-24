/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    criarEstadosPadrao($stateProvider, pNmModulo, pNmController);

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$modal', '$log', '$modalInstance',
    'modalCadastro', 'utilSrv', 'mensagemSrv',
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $modal, $log, $modalInstance,
        modalCadastro, utilSrv, mensagemSrv) {

    // inicializacao
    $scope.crudInit($scope, $state, null, pNmFormulario);

    // código para verificar se o modal está ou não ativo
    $scope.verificaEstado($modalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);

    // inicio: atividades do Modal
    $scope.modalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $modalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.modalCancelar = function () {
        // Cancelar a modal
        $modalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.modalAbrir = function (size) {
        // abrir a modal
        var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
        var modalInstance = $modal.open({
            animation: true,
            template: template,
            controller: pNmController,
            size: size,
            resolve: {
                modalCadastro: function () {
                    return angular.copy($scope.cadastro);
                }
            }
        });
        // processar retorno da modal
        modalInstance.result.then(function (cadastroModificado) {
            // processar o retorno positivo da modal
            $scope.navegador.setDados(cadastroModificado.lista);
        }, function () {
            // processar o retorno negativo da modal
            $log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim: atividades do Modal

    // inicio das operaçoes atribuidas ao navagador
    $scope.incluir = function(scp) {
        var conf =  '<div class="form-group">' +
                    '    <label class="col-md-4 control-label" for="cnfTipoPessoa">Incluir que tipo de Pessoa?</label>' +
                    '    <div class="col-md-8">' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-0">' +
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-0" value="PJ" ng-model="conteudo.tipoPessoa" required>' +
                    '            Pessoa Jurídica' +
                    '        </label>' +
                    '        <label class="radio-inline" for="cnfTipoPessoa-1">' +
                    '            <input type="radio" name="cnfTipoPessoa" id="cnfTipoPessoa-1" value="PF" ng-model="conteudo.tipoPessoa" required>' +
                    '            Pessoa Física' +
                    '        </label>' +
                    '         <div class="label label-danger" ng-show="confirmacaoFrm.cnfTipoPessoa.$error.required">' +
                    '            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
                    '             Campo Obrigatório' +
                    '        </div>' +
                    '    </div>' +
                    '</div>';
        mensagemSrv.confirmacao(conf, {tipoPessoa: null}).then(function (conteudo) {
            // processar o retorno positivo da modal
            $rootScope.incluir($scope);
            $scope.cadastro.original = {tipoPessoa: conteudo.tipoPessoa};
            $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };
    // fim das operaçoes atribuidas ao navagador

    // inicio ações especiais
    // fim ações especiais

}]);

})('pessoa', 'PessoaCtrl', 'Cadastro de Pessoas');