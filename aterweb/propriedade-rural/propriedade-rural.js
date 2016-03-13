/* global criarEstadosPadrao, isUndefOrNull */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);

}]);

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv', 'uiGmapGoogleMapApi', 'uiGmapIsReady',
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, 
        modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv, uiGmapGoogleMapApi, uiGmapIsReady) {

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

    // inicio: mapa
    $scope.visualizarDepois = function(registro) {
        return $scope.incluirDepois(registro);
    };
    $scope.incluirDepois = function (registro) {
        $scope.map.markers = [];
        $scope.map.polys = [];

        $scope.map.markers.push({id: $scope.map.markers.length, latitude: registro.endereco.entradaPrincipal.coordinates[0], longitude: registro.endereco.entradaPrincipal.coordinates[1]});

        if ($scope.map.getGMap) {
            $scope.map.getGMap().panTo(new $scope.map.maps.LatLng($scope.map.markers[0].latitude, $scope.map.markers[0].longitude));
        }

        registro.endereco.areaList.forEach(function(area) {
            var poly = {id: $scope.map.polys.length, path: []};
            area.poligono.coordinates.forEach(function(coordinate) {
                coordinate.forEach(function (c) {
                    poly.path.push({id: poly.path.length, latitude: c[0], longitude: c[1]});
                });
            });
            $scope.map.polys.push(poly);
        });
    };
    $scope.confirmarEditarAntes = function (cadastro) {
        return $scope.confirmarIncluirAntes(cadastro);
    };
    $scope.confirmarIncluirAntes = function (cadastro) {
        cadastro.registro.endereco.entradaPrincipal = {type: 'Point', coordinates: [$scope.map.markers[0].latitude, $scope.map.markers[0].longitude]};

        cadastro.registro.endereco.areaList = [];

        $scope.map.polys.forEach(function(poly) {
            var area = {nome: 'teste', poligono: {type: 'Polygon', coordinates: []}};
            var coord = [];
            poly.path.forEach(function(p) {
                coord.push([p.latitude, p.longitude]);
            });
            if (coord.length && (coord[0][0] !== coord[coord.length-1][0] || coord[0][1] !== coord[coord.length-1][1])) {
                coord.push([coord[0][0], coord[0][1]]);
            }
            area.poligono.coordinates.push(coord);
            cadastro.registro.endereco.areaList.push(area);
        });

        cadastro.registro.endereco.logradouro = "teste";
    };

    $scope.exibe = function(item) {
        console.log(item);
    };

    $scope.map = angular.extend({}, {
            center: {
                latitude: -15.732687616157767,
                longitude: -47.90378594955473,
            },
            pan: true,
            zoom: 15,
            refresh: true,
            options: {
                disableDefaultUI: false,
                scrollwheel: true,
            },
            events: {},
            bounds: {},
            markers: [
                // {
                //     id: 3,
                //     latitude: -15.732687616157767,
                //     longitude: -47.90378594955473,
                // }
            ],
            polys: [
                // {
                //     id: 433,
                //     path: [
                //         {latitude: -15.732687616157767, longitude: -47.90378594955473},
                //         {latitude: -15.7, longitude: -47.90378594955473},
                //         {latitude: -15.732687616157767, longitude: -47.9},
                //         {latitude: -15.732687616157767, longitude: -47.90378594955473},
                //     ],
                // }
            ],
            draw: function() {},
        }, $scope.map);

    $scope.elementoId = angular.extend(0, 0, $scope.elementoId);

    $scope.drawingManagerControl = angular.extend({}, {}, $scope.drawingManagerControl);

    $scope.marker = {
        coords : 'self',
        opcoes : {
            draggable : true,
        },
        eventos : {
            click: function(a,b,c,d,e) {
                a.setDraggable(true);
            }
        },
    };

    $scope.poly = {
        path : 'path',
        eventos :{
            click: function(a,b,c,d,e) {
                a.setDraggable(true);
                a.setEditable(true);
            }
        }
    };

    $scope.limpaElementos = function(array, mapa, forcar) {
        for (var i = (array.length - 1); i >= 0; i--) {
            if ((forcar) || 
                (!isUndefOrNull(array[i].uiGmap_id) && array[i].uiGmap_id !== mapa.uiGmap_id) ||
                (array[i].getMap && (array[i].getMap() === null || array[i].getMap().uiGmap_id !== mapa.uiGmap_id))) {
                array[i].setMap(null);
                array.splice(i, 1);
            }
        }
    };

    uiGmapGoogleMapApi.then(function(maps) {
        var infoCreate = function(elemento) {
            var result = new maps.InfoWindow({
                content: 'ptz',
            });
            console.log(elemento);
            return result;
        };

        $scope.map.maps = maps;

        $scope.drawingManagerOptions = {
            drawingMode: maps.drawing.OverlayType.MARKER,
            drawingControl: true,
            drawingControlOptions: {
                position: maps.ControlPosition.TOP_CENTER,
                drawingModes: [
                    maps.drawing.OverlayType.MARKER,
                    maps.drawing.OverlayType.POLYGON,
                ]
            },
            events: {
                markercomplete: function(gObject, eventName, model, marker) {
                    $scope.limpaElementos($scope.map.markers, marker[0].getMap());

                    marker[0]['id'] = ++$scope.elementoId;

                    if ($scope.map.markers.length >= 1) {
                        toastr.error('Só é possível marcar o ponto principal da Propriedade Rural', 'Erro de Marcação');
                        marker[0].setMap(null);
                        return;
                    }
                    marker[0].setDraggable(true);
                    var info = infoCreate(marker[0]);
                    marker[0].addListener('click', function() {
                        info.setPosition(marker[0].getPosition());
                        info.setContent('ptz!' + marker[0].id);
                        info.open(marker[0].getMap(), marker[0]);
                        marker[0].map.panTo(marker[0].getPosition());
                    });
                    marker[0].setMap(null);
                    $scope.map.markers.push({id: marker[0]['id'], latitude: marker[0].getPosition().lat(), longitude: marker[0].getPosition().lng(), });
                },
                polygoncomplete: function(gObject, eventName, model, polygon) {
                    $scope.limpaElementos($scope.map.polys, polygon[0].getMap());

                    polygon[0]['id'] = ++$scope.elementoId;

                    polygon[0].setDraggable(true);
                    polygon[0].setEditable(true);
                    var info = infoCreate(polygon[0]);
                    polygon[0].addListener('click', function() {
                        var bounds = new maps.LatLngBounds();
                        polygon[0].getPath().forEach(function(element,index){bounds.extend(element);});
                        info.setPosition(bounds.getCenter());
                        info.setContent('ptz!' + polygon[0].id);
                        info.open(polygon[0].getMap(), polygon[0]);
                        polygon[0].map.panTo(bounds.getCenter());
                    });
                    var path = [];

                    polygon[0].getPath().forEach(function(element,index) {
                        path.push({latitude: element.lat(), longitude: element.lng()});
                    });
                    polygon[0].setMap(null);
                    $scope.map.polys.push({id: polygon[0]['id'], path: path});
                },
            },
        };
    });
    // fim: mapa 
}]);

})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');
