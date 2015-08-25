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



    // inicio: identificacao 
    $scope.identificacaoModalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $modalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.identificacaoModalCancelar = function () {
        // Cancelar a modal
        $modalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.identificacaoModalAbrir = function (size) {
        // abrir a modal
         
        var template = '<div class="modal-header">' +
                       '<h3 class="modal-title">Selecione uma pessoa!</h3>' +
                       '</div>' +
                       '<div class="modal-body">' +
                       '<frz-endereco dados="cadastro.registro.endereco"></frz-endereco>;' +
                       '</div>' +
                       '<div class="modal-footer">' +
                       '    <button class="btn btn-primary" ng-click="identificacaoModalOk()" >OK</button>' +
                       '    <button class="btn btn-warning" ng-click="identificacaoModalCancelar()">Cancelar</button>' +
                       '</div>';
        
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
    // fim: identificacao



    // inicio das operaçoes atribuidas ao navagador
    // fim das operaçoes atribuidas ao navagador

    // inicio ações especiais
    // fim ações especiais

    // Inicio Trabalho com tabs
       $scope.tab=[{nome:'Principal',url:'propriedade/tab-principal.html'},
                   {nome:'Uso do Solo',url:'propriedade/tab-solo.html'}, 
                   {nome:'Registro IPA',url:'propriedade/tab-ipa.html'}, 
                   {nome:'Diagonóstico',url:'propriedade/tab-diagnostico.html'}, 
                   {nome:'Resultados',url:'propriedade/tab-resultado.html'}, 
                   {nome:'Pendências',url:'propriedade/tab-pendencia.html'}, 
                   {nome:'Arquivos',url:'propriedade/tab-arquivo.html'}, 
                   {nome:'Complementos',url:'propriedade/tab-complemento.html'}, 
                  ]
    // Fim Trabalho com tabs


    //Trabalho com mapas
    $scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };

}]);

})('propriedade', 'PropriedadeCtrl', 'Cadastro de Propriedades Rurais');

    