/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$modal', '$log', '$modalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeSrv',
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $modal, $log, $modalInstance,
        modalCadastro, UtilSrv, mensagemSrv, PropriedadeSrv) {

    // inicializacao
    $scope.funcionalidade = PropriedadeSrv.funcionalidade;
    $scope.crudInit($scope, $state, null, pNmFormulario, PropriedadeSrv);

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


    // inicio: abre modal
    $scope.abreModal = function (item) {
        // abrir a modal
        mensagemSrv.confirmacao(true, 'propriedade-rural/'+item.arquivo, item.descricao, item, item.tamanho ).then(function (conteudo) {
            // processar o retorno positivo da modal
            $rootScope.incluir($scope);
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });

    };

    // Inicio Trabalho com tabs
    $scope.tabs =
      [
          {
              'nome': 'Principal',
              'include': 'propriedade-rural/tab-principal.html',
              'visivel': true,
          },
          {
              'nome': 'Diagonóstico',
              'include': 'propriedade-rural/tab-diagnostico.html',
              'visivel': true,
          },
          {
              'nome': 'Resultados',
              'include': 'propriedade-rural/tab-resultado.html',
              'visivel': true,
          },
          {
              'nome': 'RegistroIPA',
              'include': 'propriedade-rural/tab-ipa.html',
              'visivel': true,
          },
          {
                'nome': 'Arquivos',
                'include': 'propriedade-rural/tab-arquivo.html',
                'visivel': true,
          },
          {
              'nome': 'Pendências',
              'include': 'propriedade-rural/tab-pendencia.html',
              'visivel': true,
          },
          {
              'nome': 'Complementos',
              'include': 'propriedade-rural/tab-complemento.html',
              'visivel': false,
          },
          
      ];
//                   {nome:'Complementos',url:'propriedade-rural/tab-complemento.html'}, 
    // Fim Trabalho com tabs

 // Inicio Trabalho com uso do solo
    // Fim Trabalho com uso do solo

 // inicio diagnostico
    $scope.cadastro.registro.diagnostico =
    [ { data: '19/02/1970', nome: 'Coleta X', 
        situacao: { codigo: '1', descricao: 'Em Aberto'}, 
        versao: { codigo:'1', descricao: '1.01'},
        responsavel: {codigo: '154', descricao: 'Fulano de Tal'},
        formaUtilizacao: {codigo: '2', descricao: 'Lazer'},
        fontePrincipal: {codigo: '3', descricao: 'Canal'}, fonteVazao: 54, fonteDomestico : {codigo: '3', descricao:'Poço'},
        maoObraContratada: 4, maoObraTemporaria: 23, maoObraFamiliar: 4, moraPropriedade :12, moraFamilia : 5,
        solo :[ {nome:'Culturas Perenes',      area: 120, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Culturas Temporárias',  area: 13,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Pastagens',             area: 32,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 }, 
                     detalhe:{arquivo: 'tab-diagnostico-solo-pastagem-modal.html', descricao: 'Detalahemento da Pastagem', observacao: '', total : 0,
                              dados: [ {nome:'Área de Canavial',             area: 12.00}, {nome:'Área de Capineira',          area: 5.00},
                                       {nome:'Área para Silagem',            area:  0.00}, {nome:'Área para Feno',             area: 1.20},
                                       {nome:'Área de Pastagem Natural',     area:  3.30}, {nome:'Área de Pastagem Artifical', area: 5.00},
                                       {nome:'Área de Pastagem Rotacionada', area:  0.00}, {nome:'Área  ILP/ILPF',             area: 0.00}
                                     ]
                             } 
                },
                {nome:'Reserva Legal',         area: 54,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Preservação Permanete', area: 88,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Áreas Irrigadas',       area: 134, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 }, 
                     detalhe:{arquivo: 'tab-diagnostico-solo-irrigada-modal.html', descricao: 'Detalahamento da Irrigação', observacao: '2332', total : 0, tamanho:1000,
                              dados:[ {nome:'Aspersão Convencinal',  area: 12.00}, {nome:'Auto-propelido', area: 5.00},
                                      {nome:'Pivô Central',          area:  0.00}, {nome:'Gotejamento',    area: 1.20},
                                      {nome:'Micro-aspersão',        area:  3.30}, {nome:'Superfície',     area: 5.00},
                                      {nome:'Outros',                area:  0.00}
                                   ]
                             } 
                },
                {nome:'Outras',                area: 12,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
              ], //solo
        benfeitoria: [ {nome:'Aspersão Convencinal', descricao:'Casa Principal',     unidade:'m²', area: 12.00, valor:123.87, dtAtul:'19/02/1970' }, 
                       {nome:'Outros',               descricao:'Galpão de Trabalho', unidade:'m²', area:  0.00, valor:458.56, dtAtul:'19/02/1980' }
                     ],
      }, //diagnostico1
      { data: '26/03/2015', nome: 'Coleta Y', 
        situacao: { codigo: '2', descricao: 'Fechado'}, 
        versao: { codigo:'2', descricao: '1.02'},
        responsavel: {codigo: '222', descricao: 'Beltrano de Tal'},
        formaUtilizacao: {codigo: '1', descricao: 'Lazer'},
        fontePrincipal: {codigo: '1', descricao: 'Canal'}, fonteVazao: 333, fonteDomestico : {codigo: '3', descricao:'Poço'},
        maoObraContratada: 4, maoObraTemporaria: 23, maoObraFamiliar: 4, moraPropriedade :12, moraFamilia : 5,
        solo :[ {nome:'Culturas Perenes',      area: 22.11, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Culturas Temporárias',  area: 33.55, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Pastagens',             area: 44.66, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 }, 
                     detalhe:{arquivo: 'tab-diagnostico-solo-pastagem-modal.html', descricao: 'Detalahemento da Pastagem', observacao: '', total : 0,
                              dados: [ {nome:'Área de Canavial',             area: 33.00}, {nome:'Área de Capineira',          area: 5.55},
                                       {nome:'Área para Silagem',            area:  9.00}, {nome:'Área para Feno',             area: 9.26},
                                       {nome:'Área de Pastagem Natural',     area:  7.30}, {nome:'Área de Pastagem Artifical', area: 7.88},
                                       {nome:'Área de Pastagem Rotacionada', area:  9.00}, {nome:'Área  ILP/ILPF',             area: 1.55}
                                     ]
                             } 
                },
                {nome:'Reserva Legal',         area: 33.33,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Preservação Permanete', area: 12.44,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
                {nome:'Áreas Irrigadas',       area: 233.44, unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 }, 
                     detalhe:{arquivo: 'tab-diagnostico-solo-irrigada-modal.html', descricao: 'Detalahamento da Irrigação', observacao: '2332', total : 0, tamanho:1000,
                              dados:[ {nome:'Aspersão Convencinal',  area: 12.00}, {nome:'Auto-propelido', area: 5.00},
                                      {nome:'Pivô Central',          area:  0.00}, {nome:'Gotejamento',    area: 1.20},
                                      {nome:'Micro-aspersão',        area:  3.30}, {nome:'Superfície',     area: 5.00},
                                      {nome:'Outros',                area:  0.00}
                                   ]
                             } 
                },
                {nome:'Outras',                area: 12,  unitario: 250, mapa : { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 } },
              ], //solo2
        benfeitoria: [ {nome:'Aspersão Convencinal', descricao:'Casa Principal',     unidade:'m²', area: 12.00, valor:123.87, dtAtul:'19/02/1970' }, 
                       {nome:'Outros',               descricao:'Galpão de Trabalho', unidade:'m²', area:  5.00, valor:458.56, dtAtul:'19/02/1922' },
                       {nome:'Outros',               descricao:'Paiol',              unidade:'m²', area: 54.00, valor:355.89, dtAtul:'19/02/1977' }
                     ],
       }, //diagnostico2
    ];
 // fim diagnostico



    //Trabalho com mapas
    $scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };

}]);

})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');