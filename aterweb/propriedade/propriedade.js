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
                       '<frz-endereco dados="cadastro.registro.endereco"></frz-endereco>' +
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

    // inicio: mapa 
    $scope.mapaModalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $modalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.mapaModalCancelar = function () {
        // Cancelar a modal
        $modalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.mapaModalAbrir = function (tipo, mapa) {
        // abrir a modal
        $scope.map = mapa; 
        console.log(mapa);
        var template = '<div class="modal-header">' +
                       '<h3 class="modal-title">Mapa de '+tipo+'</h3>' +
                       '</div>' +
                       '<div class="modal-body">'+
                       '<ui-gmap-google-map center=\'map.center\' zoom=\'map.zoom\'></ui-gmap-google-map>' +
                       '</div>' +
                       '<div class="modal-footer">' +
                       '    <button class="btn btn-primary" ng-click="mapaModalOk()" >OK</button>' +
                       '    <button class="btn btn-warning" ng-click="mapaModalCancelar()">Cancelar</button>' +
                       '</div>';
        
        var modalInstance = $modal.open({
            animation: true,
            template: template,
            controller: pNmController,
            size: 500,
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
    // fim: mapa

    // inicio: solo-pastagem
    $scope.soloPastagemModalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $modalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.soloPastagemModalCancelar = function () {
        // Cancelar a modal
        $modalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.soloPastagemModalAbrir = function (item) {
        // abrir a modal
        var conf =
'<div class="modal-header">' +
'    <h3 class="modal-title">Pastagem</h3>' +
'    {{conteudo}}' +
'</div>' +
'<div class="modal-body">' +
'' +
'    <div class="row"> ' +
'        <div class="panel-body">' +
'            <table class="table table-hover table-striped table-vcenter">' +
'                <thead>' +
'                    <tr>' +
'                        <th>Tipo de Pastagem</th>' +
'                        <th>Área (ha)</th>' +
'                    </tr>' +
'                </thead>' +
'                <tbody>' +
'                    <tr ng-repeat="item in conteudo.dados" >' +
'                        <td style="vertical-align:middle">                    ' +
'                            <label>{{item.nome}}</label>' +
'                        </td>' +
'                        <td> ' +
'                           <input id="area{{$index}}" name="area{{$index}}" type="text" class="form-control input-md text-right" ng-model="item.area" ui-number-mask="3" >' +
'                        </td>' +
'                    </tr>' +
'                </tbody>' +
'            </table>' +
'        </div>' +
'    </div>' +
'' +
'</div>';

        mensagemSrv.confirmacao(true, 'propriedade/tab-solo-pastagem-modal.html', 'Detalhoe dfa sdlfas', {dados: item}).then(function (conteudo) {
            // processar o retorno positivo da modal
            $rootScope.incluir($scope);
            $scope.cadastro.original = {tipoPessoa: conteudo.tipoPessoa};
            $scope.cadastro.registro = angular.copy($scope.cadastro.original);
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });


        /*$scope.tabSoloItem = item;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'propriedade/tab-solo-pastagem-modal.html',
            controller: pNmController,
            size: 500,
            resolve: {
                modalCadastro: function () {
                    $scope.tabSoloItem = item;
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
        });*/
    };
    // fim: mapa


    // inicio das operaçoes atribuidas ao navagador
    // fim das operaçoes atribuidas ao navagador

    // inicio ações especiais
    // fim ações especiais

    // Inicio Trabalho com tabs
       $scope.tab=[{nome:'Principal',url:'propriedade/tab-principal.html' },
                   {nome:'Uso do Solo',url:'propriedade/tab-solo.html', def:'true'}, 
                   {nome:'Registro IPA',url:'propriedade/tab-ipa.html'}, 
                   {nome:'Diagonóstico',url:'propriedade/tab-diagnostico.html'}, 
                   {nome:'Resultados',url:'propriedade/tab-resultado.html'}, 
                   {nome:'Pendências',url:'propriedade/tab-pendencia.html'}, 
                   {nome:'Arquivos',url:'propriedade/tab-arquivo.html'}, 
                   {nome:'Complementos',url:'propriedade/tab-complemento.html'}, 
                  ];
    // Fim Trabalho com tabs


 // Inicio Trabalho com uso do solo
       $scope.cadastro.registro.solo=[{nome:'Culturas Perenes',      area: 120, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Culturas Temporárias',  area: 13,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Pastagens',             area: 32,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 }, 
                                       detalhe:{ arquivo: 'tab-solo-pastagem-modal.html', funcao: 'soloPastagemModalAbrir', observacao: '', total : 0,
                                                 dados: [ {nome:'Área de Canavial',             valor: 12.00}, {nome:'Área de Capineira',          valor: 5.00},
                                                          {nome:'Área para Silagem',            valor:  0.00}, {nome:'Área para Feno',             valor: 1.20},
                                                          {nome:'Área de Pastagem Natural',     valor:  3.30}, {nome:'Área de Pastagem Artifical', valor: 5.00},
                                                          {nome:'Área de Pastagem Rotacionada', valor:  0.00}, {nome:'Área  ILP/ILPF',             valor: 0.00}
                                                        ]
                                               } 
                                      },
                                      {nome:'Benfeitorias',          area: 0.3, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Reserva Legal',         area: 54,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Preservação Permanete', area: 88,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Áreas Irrigadas',       area: 134, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                      {nome:'Outras',                area: 12,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                                     ];
    // Fim Trabalho com uso do solo


    //Trabalho com mapas
    $scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };

}]);

})('propriedade', 'PropriedadeCtrl', 'Cadastro de Propriedades Rurais');

    