/* global criarEstadosPadrao */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv',
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance,
        modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv) {

        // inicializacao
        $scope.crudInit($scope, $state, null, pNmFormulario, PropriedadeRuralSrv);

        // código para verificar se o modal está ou não ativo
        $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);
        // inicio: atividades do Modal
        $scope.modalOk = function() {
            // Retorno da modal
            $uibModalInstance.close({cadastro: angular.copy($scope.cadastro), selecao: angular.copy($scope.navegador.selecao)});
        };
        $scope.modalCancelar = function() {
            // Cancelar a modal
            $uibModalInstance.dismiss('cancel');
            toastr.warning('Operação cancelada!', 'Atenção!');
        };
        $scope.modalAbrir = function(size) {
            // abrir a modal
            var template = '<ng-include src=\"\'' + pNmModulo + '/' + pNmModulo + '-modal.html\'\"></ng-include>';
            var modalInstance = $uibModal.open({
                animation: true,
                template: template,
                controller: pNmController,
                size: size,
                resolve: {
                    modalCadastro: function() {
                        return $scope.cadastroBase();
                    }
                }
            });
            // processar retorno da modal
            modalInstance.result.then(function(cadastroModificado) {
                // processar o retorno positivo da modal
                $scope.navegador.setDados(cadastroModificado.lista);
            }, function() {
                // processar o retorno negativo da modal
                $log.info('Modal dismissed at: ' + new Date());
            });
        };
    // fim: atividades do Modal

    var editarItem = function (destino, item) {
        mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', null, item.endereco, null, null).then(function (conteudo) {
            // processar o retorno positivo da modal
            $scope.cadastro.registro.endereco = angular.copy(conteudo);

            //conteudo.endereco.numero = formataEndereco(conteudo.endereco.numero);
            /*if (destino) {
                if (destino['cadastroAcao'] && destino['cadastroAcao'] !== 'I') {
                    destino['cadastroAcao'] = 'A';
                }
                destino.endereco = angular.copy(conteudo);
            } else {
                conteudo['cadastroAcao'] = 'I';
                $scope.cadastro.registro.enderecoList.push({endereco: conteudo});
            }*/
        }, function () {
            // processar o retorno negativo da modal
            //$log.info('Modal dismissed at: ' + new Date());
        });
    };


    // inicio: identificacao 
    $scope.identificacaoModalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $uibModalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.identificacaoModalCancelar = function () {
        // Cancelar a modal
        $uibModalInstance.dismiss('cancel');
        toastr.warning('Operação cancelada!', 'Atenção!');
    };
    $scope.identificacaoModalAbrir = function (size) {
        // abrir a modal
         
        EnderecoSrv.novo().success(function (resposta) {
            var item = {endereco: resposta.resultado};
            editarItem(null, item);
        });

    };
    // fim: identificacao

    // inicio: mapa 
    $scope.mapaModalOk = function () {
        // Retorno da modal
        $scope.cadastro.lista = [];
        $scope.cadastro.lista.push({id: 21, nome: 'Fernando'});
        $scope.cadastro.lista.push({id: 12, nome: 'Frazao'});

        $uibModalInstance.close($scope.cadastro);
        toastr.info('Operação realizada!', 'Informação');
    };
    $scope.mapaModalCancelar = function () {
        // Cancelar a modal
        $uibModalInstance.dismiss('cancel');
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
        
        var modalInstance = $uibModal.open({
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
    $scope.tabs = [
        {
            'nome': 'Principal',
            'include': 'propriedade-rural/tab-principal.html',
            'visivel': true,
        },
        {
            'nome': 'Diagnósticos',
            'include': 'propriedade-rural/tab-diagnostico.html',
            'visivel': true,
            'selecao': function() {
                $scope.$broadcast ('abaDiagnosticoAtivada');
            },
        },
        {
            'nome': 'Índice de Produção',
            'include': 'propriedade-rural/tab-indice-producao.html',
            'visivel': true,
        },
        {
            'nome': 'Arquivos',
            'include': 'propriedade-rural/tab-arquivo.html',
            'visivel': true,
        },
        /*{
            'nome': 'Pendências',
            'include': 'propriedade-rural/tab-pendencia.html',
            'visivel': true,
        },*/
        {
            'nome': 'Complementos',
            'include': 'propriedade-rural/tab-complemento.html',
            'visivel': false,
        },
    ];
    // {nome:'Complementos',url:'propriedade-rural/tab-complemento.html'}, 
    // Fim Trabalho com tabs

    // Inicio Trabalho com uso do solo
    // Fim Trabalho com uso do solo

    $scope.toggleChildren = function (scope) {
        scope.toggle();
    };
    $scope.visible = function (item) {
        return !($scope.cadastro.apoio.localFiltro && 
            $scope.cadastro.apoio.localFiltro.length > 0 && 
            item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
    };

    var confirmarSalvarAntes  = function (cadastro) {
        var i;
        if (cadastro.registro.arquivoList) {
            for (i in cadastro.registro.arquivoList) {
                console.log(cadastro.registro.arquivoList[i].arquivo.dataUpload);
            }
        }
    };

    $scope.confirmarIncluirAntes = function (cadastro) {
        confirmarSalvarAntes(cadastro);
    };

    $scope.confirmarEditarAntes = function (cadastro) {
        confirmarSalvarAntes(cadastro);
    };

    $scope.confirmarFiltrarAntes = function(filtro) {
        filtro.empresaList = [];
        filtro.unidadeOrganizacionalList = [];
        filtro.comunidadeList = [];
        var i, j, k;
        for (i in $scope.cadastro.apoio.localList) {
            // filtrar as empressas
            if ($scope.cadastro.apoio.localList[i].selecionado) {
                filtro.empresaList.push({id: $scope.cadastro.apoio.localList[i].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'});
            } else {
                for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                    // filtrar as unidades organizacionais
                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                        filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id});
                    } else {
                        for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                filtro.comunidadeList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id});
                            }
                        }
                    }
                }
            }
        }
        if ($scope.cadastro.apoio.unidadeOrganizacionalSomenteLeitura && !$scope.cadastro.filtro.unidadeOrganizacionalList.length && !$scope.cadastro.filtro.comunidadeList.length) {
            toastr.error('Informe pelo menos uma comunidade', 'Erro ao filtrar');
            throw 'Informe pelo menos uma comunidade';
        }
    };

    // inicio dos watches
    $scope.$watch('cadastro.registro.id + cadastro.registro.comunidade.id', function(newValue, oldValue) {
        $scope.tabs[1].visivel = $scope.cadastro.registro.id > 0;
        $scope.tabs[2].visivel = $scope.cadastro.registro.id > 0 && angular.isObject($scope.cadastro.registro.comunidade) && $scope.cadastro.registro.comunidade.id > 0;
    });
    // fim dos watches

}]);

angular.module(pNmModulo).controller('MapaCtrl',
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv', 'uiGmapGoogleMapApi', 'uiGmapIsReady',
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance,
        modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv, uiGmapGoogleMapApi, uiGmapIsReady) {

    /*var marker1 = {id: 1, latitude: -15.7399298, longitude: -47.940};
    var marker2 = {id: 2, latitude: -15.7399298, longitude: -47.945};
    var marker3 = {id: 3, latitude: -15.7399298, longitude: -47.946};*/
    $scope.markers = [];
    /*$scope.markers[0] = marker1;
    $scope.markers[1] = marker2;
    $scope.markers[2] = marker3;*/

    $scope.map = {
        center: {
            latitude: -15.732687616157767,
            longitude: -47.90378594955473
        },
        zoom: 15
    };
    $scope.options = {
        scrollwheel: true
    };

    uiGmapGoogleMapApi.then(function(maps) {
        //Trabalho com mapas

/*        google.maps.event.addListener($scope.map, 'click', function(event) {
            placeMarker(event.latLng);
        });

        function placeMarker(location) {
            var marker = new google.maps.Marker({
                position: location,
                map: $scope.map,
            });
            var infowindow = new google.maps.InfoWindow({
                content: 'Latitude: ' + location.lat() +
                '<br>Longitude: ' + location.lng()
            });
            infowindow.open($scope.map,marker);
        }*/

        uiGmapIsReady.promise(1).then(function(instances) {


            instances.forEach(function(inst) {
                maps.event.addListener(inst.map, 'click', function(event) {

                    if (!$scope.marcarPontoPrincipal) {
                        return;
                    }

                    var marker = new maps.Marker({
                        position: event.latLng,
                        map: inst.map,
                        id: $scope.markers.length,
                        draggable: true,
                    });

                    marker.addListener("dblclick", function() {
                        for (var i in $scope.markers) {
                            if ($scope.markers[i].id === marker.id) {
                                $scope.markers.splice(i, 1);
                            }
                        }
                        marker.setMap(null);
                        $scope.$apply(function () {
                            $scope.marcarPontoPrincipal = false;
                        });
                    });

                    $scope.$apply(function () {
                        $scope.markers.push({
                            id: marker.id, 
                            latitude: event.latLng.lat(), 
                            longitude: event.latLng.lng(),
                        });

                        $scope.marcarPontoPrincipal = false;
                    });

                });
            });
        });

    });

    $scope.marcarPontoPrincipal = false;
    $scope.marcarPontoPrincipalClk = function () {
        $scope.marcarPontoPrincipal = !$scope.marcarPontoPrincipal;
        $scope.marcarArea = false;
    };
    $scope.marcarArea = false;
    $scope.marcarAreaClk = function () {
        $scope.marcarArea = !$scope.marcarArea;
        $scope.marcarPontoPrincipal = false;
    };

}]);


})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');