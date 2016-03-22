/* global criarEstadosPadrao, isUndefOrNull */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

'use strict';

angular.module(pNmModulo, ['ui.bootstrap','ui.utils','ui.router','ngAnimate', 'frz.navegador']);

angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {

    criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);

}]);

angular.module(pNmModulo).factory('ponteControllerSrv', [function () {
    var factory = {
        scp: null,
        getScp: function() {
            return this.scp;
        },
        setScp: function(scp) {
            this.scp = scp;
        },
    }; 
    return factory;
}]);

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
    'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv', 'uiGmapGoogleMapApi', 'uiGmapIsReady',
    'ponteControllerSrv', 
    function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, 
        modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv, uiGmapGoogleMapApi, uiGmapIsReady,
        ponteControllerSrv) {

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
        mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', null, item, null, null).then(function (conteudo) {
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
        if (!$scope.cadastro.registro.endereco) {
            EnderecoSrv.novo().success(function (resposta) {
                editarItem(null, resposta.resultado);
            });
        } else {
            editarItem(null, angular.copy($scope.cadastro.registro.endereco));
        }
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
    $scope.$watch('cadastro.registro.comunidade', function(newValue, oldValue) {
        if (newValue && newValue.id) {
            $scope.cadastro.apoio.baciaHidrograficaList = [];
            $scope.cadastro.apoio.comunidadeBaciaHidrograficaList.forEach(function(item) {
                if (newValue.id === item.comunidade.id) {
                    $scope.cadastro.apoio.baciaHidrograficaList.push(item.baciaHidrografica);
                }
            });
        }
    });
    $scope.$watch('cadastro.registro.endereco', function(newValue, oldValue) {
        $scope.cadastro.apoio.enderecoResumo = '';
        var e = $scope.cadastro.registro.endereco;
        if (!e) {
            return "";
        }
        var linhas = ['', '', ''];
        if (e.logradouro) {
            linhas[0] += e.logradouro
        }
        if (e.complemento) {
            linhas[0] += (linhas[0].length ? ', ' : '') + e.complemento
        }
        if (e.numero) {
            linhas[0] += (linhas[0].length ? ', ' : '') + 'numero ' + e.numero
        }
        if (e.cidade) {
            linhas[1] += e.cidade.nome;
        }
        if (e.bairro) {
            linhas[1] += (linhas[1].length ? ', ' : '') + e.bairro;
        }
        if (e.cep) {
            linhas[2] += e.cep;
        }
        if (e.municipio) {
            linhas[2] += (linhas[2].length ? ' ' : '') + e.municipio.nome;
        }
        if (e.estado) {
            linhas[2] += (linhas[2].length ? ' ' : '') + e.estado.sigla;
        }
        var result = '';
        linhas.forEach(function(linha) {
            if (linha.length) {
                if (result.length) {
                    result += '\n';
                }
                result += linha;
            }
        });
        $scope.cadastro.apoio.enderecoResumo =  result;
    });

    // fim dos watches

    // inicio: mapa
    $scope.elementoId = angular.extend(0, 0, $scope.elementoId);

    $scope.visualizarDepois = function(registro) {
        return $scope.incluirDepois(registro);
    };
    $scope.incluirDepois = function (registro) {
        $scope.executarOuPostergar(function() {
            return $scope.map;
        }, function(registro) {
            $scope.map.markers = [];
            $scope.map.polys = [];

            if (!registro || !registro.endereco) {
                return;
            }

            if (registro.endereco.entradaPrincipal) {
                $scope.map.markers.push({"elementoId": ++$scope.elementoId, "latitude": registro.endereco.entradaPrincipal.coordinates[0], "longitude": registro.endereco.entradaPrincipal.coordinates[1]});
                // centralizar o ponto
                $scope.executarOuPostergar(function(){return $scope.map.controle.getGMap;}, function(pos) {
                    $scope.map.controle.getGMap().panTo(pos);
                    $scope.map.controle.getGMap().setZoom(15);
                }, {lat: $scope.map.markers[0].latitude, lng: $scope.map.markers[0].longitude}, 4000, true);
            }

            if (registro.endereco.areaList) {        
                registro.endereco.areaList.forEach(function(area) {
                    var poly = {"elementoId": ++$scope.elementoId, "path": [], "id": area.id, "nome": area.nome};
                    area.poligono.coordinates.forEach(function(coordinate) {
                        coordinate.forEach(function (c) {
                            poly.path.push({latitude: c[0], longitude: c[1]});
                        });
                    });
                    $scope.map.polys.push(poly);
                });
            }
        }, registro, 500);
    };
    $scope.confirmarEditarAntes = function (cadastro) {
        return $scope.confirmarIncluirAntes(cadastro);
    };
    $scope.confirmarIncluirAntes = function (cadastro) {
        if (!cadastro.registro.endereco) {
            cadastro.registro.endereco = {};
        }
        if ($scope.map.markers && $scope.map.markers.length) {
            cadastro.registro.endereco.entradaPrincipal = {type: 'Point', coordinates: [$scope.map.markers[0].latitude, $scope.map.markers[0].longitude]};
        } else {
            cadastro.registro.endereco.entradaPrincipal = null;
        }

        if ($scope.map.polys && $scope.map.polys.length) {
            if (!cadastro.registro.endereco.entradaPrincipal) {
                throw "Favor informar a entrada principal da propriedade rural no mapa";
            }
            cadastro.registro.endereco.areaList = [];

            $scope.map.polys.forEach(function(poly) {
                var area = {"id": poly.id, "nome": poly.nome, "poligono": {type: 'Polygon', coordinates: []}};
                var coord = [];
                poly.path.forEach(function(p) {
                    coord.push([p.latitude, p.longitude]);
                });
                // ajustar o ultimo ponto se necessario
                if (coord.length && (coord[0][0] !== coord[coord.length-1][0] || coord[0][1] !== coord[coord.length-1][1])) {
                    coord.push([coord[0][0], coord[0][1]]);
                }
                area.poligono.coordinates.push(coord);
                cadastro.registro.endereco.areaList.push(area);
            });
        } else {
            cadastro.registro.endereco.areaList = null;
        }
    };

    uiGmapGoogleMapApi.then(function(maps) {

        $scope.map = angular.extend({}, {
            bounds: {},
            center: {latitude: -15.732687616157767, longitude: -47.90378594955473,},
            controle: {},
            events: {},
            maps: maps,
            markers: [],
            options: { disableDefaultUI: false, scrollwheel: true, },
            pan: true,
            polys: [],
            refresh: true,
            zoom: 15,
        }, $scope.map);

        $scope.drawingManager = angular.extend({}, {
            controle: {}, 
            opcoes: {
                drawingControl: true,
                drawingControlOptions: {
                    position: maps.ControlPosition.TOP_CENTER,
                    drawingModes: [
                        maps.drawing.OverlayType.MARKER,
                        maps.drawing.OverlayType.POLYGON,
                    ]
                }
            }, 
            eventos: {
                markercomplete: function(gObject, eventName, model, marker) {
                    marker[0].setMap(null);

                    if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                        toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                        return;
                    }

                    if ($scope.map.markers.length >= 1) {
                        toastr.error('Só é possível marcar o ponto principal da Propriedade Rural', 'Erro de Marcação');
                        return;
                    }
                    $scope.map.markers.push({"elementoId": ++$scope.elementoId, "latitude": marker[0].getPosition().lat(), "longitude": marker[0].getPosition().lng(), });
                },
                polygoncomplete: function(gObject, eventName, model, polygon) {
                    polygon[0].setMap(null);

                    if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                        toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                        return;
                    }

                    var path = [];
                    polygon[0].getPath().forEach(function(element,index) {
                        path.push({"latitude": element.lat(), "longitude": element.lng()});
                    });
                    $scope.map.polys.push({"elementoId": ++$scope.elementoId, "path": path});
                },
            }
        }, $scope.drawingManager);

        $scope.marker = {
            controle: {},
            coords : 'self',
            opcoes : {
                draggable : true,
            },
            eventos : {
                click: function(a,b,c,d,e) {
                    $scope.$apply(function() {
                        if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                            toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                            return;
                        }
                        $scope.map.selecionado = c;
                    });
                },
            },
        };

        $scope.poly = {
            controle: {},
            path : 'path',
            eventos :{
                click: function(a,b,c,d,e) {
                    $scope.$apply(function() {
                        if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                            toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                            return;
                        }
                        a.setDraggable(true);
                        a.setEditable(true);
                        $scope.map.selecionado = c;
                    });
                },
            },
        };

    });

    $scope.searchbox = {
        template: "searchBox.tpl.html",
        eventos: {
            places_changed: function (searchBox) {
                var place = searchBox.getPlaces();
                if (!place || place === 'undefined' || place.length === 0) {
                    console.log('no place data :(');
                    return;
                }

                $scope.map.zoom = 15;
                $scope.map.center = {
                    "latitude": place[0].geometry.location.lat(),
                    "longitude": place[0].geometry.location.lng(),
                };
            }
        }
    };

    $scope.removeElemento = function() {
        if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
            toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
            return;
        }

        var i;
        bloco: {
            for (i = $scope.map.markers.length-1; i >= 0; i--) {
                if ($scope.map.markers[i].elementoId === $scope.map.selecionado.elementoId) {
                    $scope.map.markers.splice(i, 1);
                    break bloco;
                }
            }
            for (i = $scope.map.polys.length-1; i >= 0; i--) {
                if ($scope.map.polys[i].elementoId === $scope.map.selecionado.elementoId) {
                    $scope.map.polys.splice(i, 1);
                    break bloco;
                }
            }
        }
        $scope.map.selecionado = null;
    };
    ponteControllerSrv.setScp($scope);
    // fim: mapa 
}]);

angular.module(pNmModulo).controller('MapaCtrl',
    ['$scope', 'ponteControllerSrv', 
    function($scope, ponteControllerSrv) {
    $scope.removeElemento = function() {
        ponteControllerSrv.getScp().removeElemento();
    };
    $scope.selecionado = function() {
        if (!ponteControllerSrv.getScp() || !ponteControllerSrv.getScp().map) {
            return null;
        } else {
            return ponteControllerSrv.getScp().map.selecionado;
        }
    };
}]);

angular.module(pNmModulo).run(['$templateCache','uiGmapLogger', function ($templateCache,Logger) {
    Logger.doLog = true;
    $templateCache.put('control.tpl.html', '<button class=\"btn btn-sm btn-danger\" ng-click=\"removeElemento()\" ng-show=\"selecionado()\">Excluir</button>');
    $templateCache.put('searchBox.tpl.html', '<input type=\"text\" ng-model=\"enderecoPesquisa\" placeholder=\"Procurar\" ng-dblclick=\"enderecoPesquisa = \'\'\"/>');
}]);

})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');