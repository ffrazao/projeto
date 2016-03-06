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

// inicio: Trabalho com mapas
angular.module(pNmModulo).factory('Channel', function() {
    return function () {
        var callbacks = [];
        this.add = function (cb) {
            callbacks.push(cb);
        };
        this.invoke = function () {
            callbacks.forEach(function (cb) {
                cb();
            });
        };
        return this;
    };
})
.service('drawChannel',['Channel',function(Channel) {
    return new Channel();
}])
.service('clearChannel',['Channel',function(Channel) {
    return new Channel();
}])
.controller('mapWidgetCtrl', ['$scope', 'drawChannel','clearChannel', function ($scope, drawChannel, clearChannel) {
    $scope.drawWidget = {
        controlText: 'Área',
        glyphicon: 'glyphicon-edit',
        controlClick: function () {
            drawChannel.invoke();
        }
    };
    $scope.clearWidget = {
        controlText: 'Limpar Área',
        glyphicon: 'glyphicon-remove-circle',
        controlClick: function () {
            clearChannel.invoke();
        }
    };
}])
.run(['$templateCache','uiGmapLogger', function ($templateCache,Logger) {
    Logger.doLog = true;
    $templateCache.put('marcarPontoPrincipal.tpl.html', '<button class="btn btn-primary btn-sm sr-only" ng-class="{\'btn-warning\': marcarPontoPrincipal}" ng-click="marcarPontoPrincipalClk()" ng-hide="map.markers.length || marcarArea"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Entrada Principal</button>');
    $templateCache.put('draw.tpl.html', '<button class="btn btn-sm btn-primary sr-only" ng-click="drawWidget.controlClick()"><span class="glyphicon {{drawWidget.glyphicon}}" aria-hidden="true"></span>{{drawWidget.controlText}}</button>');
    $templateCache.put('clear.tpl.html', '<button class="btn btn-sm btn-primary" ng-click="clearWidget.controlClick()"><span class="glyphicon {{clearWidget.glyphicon}}" aria-hidden="true"></span>{{clearWidget.controlText}}</button>');
}]);

angular.module(pNmModulo).controller('MapaCtrl',
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv', 'uiGmapGoogleMapApi', 'uiGmapIsReady',
    'drawChannel','clearChannel',
function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance,
    modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv, uiGmapGoogleMapApi, uiGmapIsReady,
    drawChannel, clearChannel) {

    uiGmapGoogleMapApi.then(function(maps) {
        $scope.map = angular.extend({}, {
            center: {
                latitude: -15.732687616157767,
                longitude: -47.90378594955473,
            },
            pan: true,
            zoom: 15,
            refresh: false,
            options: {
                disableDefaultUI: false,
                scrollwheel: true,
            },
            events: {},
            bounds: {},
            markers: [],
            polys: [],
            draw: function() {},
        }, $scope.map);

        $scope.drawingManagerOptions = {
            drawingMode: google.maps.drawing.OverlayType.MARKER,
            drawingControl: true,
            drawingControlOptions: {
                position: google.maps.ControlPosition.TOP_CENTER,
                drawingModes: [
                    google.maps.drawing.OverlayType.MARKER,
                    google.maps.drawing.OverlayType.POLYGON,
                ]
            },
        };

        var clear = function() {
            if ($scope.map.polys) {
                $scope.map.polys.forEach(function(p) {
                    p.setMap(null);
                });
            }
            $scope.map.polys = [];
            if ($scope.map.markers) {
                $scope.map.markers.forEach(function(p) {
                    p.setMap(null);
                });
            }
            $scope.map.markers = [];
        };
        var draw = function() {
            $scope.map.draw();//should be defined by now
        };
        //add beginDraw as a subscriber to be invoked by the channel, allows controller to controller coms
        drawChannel.add(draw);
        clearChannel.add(clear);

        // esta verificacao garante que a instancia do mapa esta construida e pode ser usada
        if ($scope.map.data) {

//            $scope.drawingManagerControl = angular.extend({}, new maps.drawing.DrawingManager(), $scope.drawingManagerControl);
            $scope.drawingManagerControl = new maps.drawing.DrawingManager({
                drawingMode: google.maps.drawing.OverlayType.MARKER,
                drawingControl: true,
                drawingControlOptions: {
                    position: google.maps.ControlPosition.TOP_CENTER,
                    drawingModes: [
                        google.maps.drawing.OverlayType.MARKER,
                        google.maps.drawing.OverlayType.POLYGON,
                    ]
                }
            });
            $scope.drawingManagerControl.setMap($scope.map.data.map);

            uiGmapIsReady.promise().then(function(intances) {
                maps.event.addListener($scope.drawingManagerControl, 'polygoncomplete', function(polygon) {
                    polygon.setDraggable(true);
                    polygon.setEditable(true);

                    $scope.map.polys.push(polygon);
                });
                maps.event.addListener($scope.drawingManagerControl, 'markercomplete', function(marker) {
                    marker.setDraggable(true);

                    if ($scope.map.markers.length >= 1) {
                        marker.setMap(null);
                        toastr.error('Só é possível marcar o ponto principal de acesso à propriedade rural', 'Erro de Marcação');
                    } else {
                        $scope.map.markers.push(marker);
                    }
                });
            });

            var coords = [
                {lat: $scope.map.center.lat() -0.001, lng: $scope.map.center.lng() -0.001},
                {lat: $scope.map.center.lat() -0.001, lng: $scope.map.center.lng() +0.001},
                {lat: $scope.map.center.lat() +0.001, lng: $scope.map.center.lng() +0.001},
                {lat: $scope.map.center.lat() +0.001, lng: $scope.map.center.lng() -0.001},
                {lat: $scope.map.center.lat() -0.001, lng: $scope.map.center.lng() -0.001},
            ];
            $scope.map.polys.push(new maps.Polygon({
                    map: $scope.map.data.map,
                    visible: true, 
                    editable: true, 
                    draggable: true, 
                    geodesic: false, 
                    stroke: { 
                        weight: 3,
                        color: '#6060FB',
                    }, 
                    path: coords,
                }
            ));

            maps.event.addListener($scope.map.data.map, 'click', function(event) {

                if (!$scope.marcarPontoPrincipal) {
                    return;
                }

                var marker = new maps.Marker({
                    position: event.latLng,
                    map: $scope.map.data.map,
                    id: $scope.map.markers.length,
                    draggable: true,
                });

                marker.addListener("dblclick", function() {
                    for (var i in $scope.map.markers) {
                        if ($scope.map.markers[i].id === marker.id) {
                            $scope.map.markers.splice(i, 1);
                        }
                    }
                    marker.setMap(null);
                    $scope.$apply(function () {
                        $scope.marcarPontoPrincipal = false;
                    });
                });

                $scope.$apply(function () {
                    $scope.map.markers.push({
                        id: marker.id, 
                        latitude: event.latLng.lat(), 
                        longitude: event.latLng.lng(),
                    });
                    $scope.marcarPontoPrincipal = false;
                });
            });
        }
    });
}]);
// fim: Trabalho com mapas

})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');