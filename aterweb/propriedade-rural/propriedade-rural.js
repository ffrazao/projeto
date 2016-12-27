/* global criarEstadosPadrao, isUndefOrNull, removerCampo */

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {
    'use strict';
    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador']);
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);
    angular.module(pNmModulo).factory('ponteControllerSrv', [
        function() {
            'ngInject';

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
        }
    ]);
    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance',
        'modalCadastro', 'UtilSrv', 'mensagemSrv', 'PropriedadeRuralSrv', 'EnderecoSrv', 'uiGmapGoogleMapApi', 'uiGmapIsReady',
        'ponteControllerSrv', '$interval',
        function($scope, toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance,
            modalCadastro, UtilSrv, mensagemSrv, PropriedadeRuralSrv, EnderecoSrv, uiGmapGoogleMapApi, uiGmapIsReady,
            ponteControllerSrv, $interval) {
            'ngInject';
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

            $scope.UtilSrv = UtilSrv;

            var editarItem = function(destino, item) {
                mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', null, item, null, null).then(function(conteudo) {
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
                }, function() {
                    // processar o retorno negativo da modal
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            // inicio: identificacao 
            $scope.identificacaoModalOk = function() {
                // Retorno da modal
                $scope.cadastro.lista = [];
                $scope.cadastro.lista.push({
                    id: 21,
                    nome: 'Fernando'
                });
                $scope.cadastro.lista.push({
                    id: 12,
                    nome: 'Frazao'
                });

                $uibModalInstance.close($scope.cadastro);
                toastr.info('Operação realizada!', 'Informação');
            };
            $scope.identificacaoModalCancelar = function() {
                // Cancelar a modal
                $uibModalInstance.dismiss('cancel');
                toastr.warning('Operação cancelada!', 'Atenção!');
            };
            $scope.identificacaoModalAbrir = function(size) {
                // abrir a modal
                if (!$scope.cadastro.registro.endereco) {
                    EnderecoSrv.novo().success(function(resposta) {
                        editarItem(null, resposta.resultado);
                    });
                } else {
                    editarItem(null, angular.copy($scope.cadastro.registro.endereco));
                }
            };
            // fim: identificacao

            // inicio: abre modal
            $scope.abreModal = function(item) {
                // abrir a modal
                mensagemSrv.confirmacao(true, 'propriedade-rural/' + item.arquivo, item.descricao, item, item.tamanho).then(function(conteudo) {
                    // processar o retorno positivo da modal
                    $rootScope.incluir($scope);
                }, function() {
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
                        $scope.$broadcast('abaDiagnosticoAtivada');
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
                {
                    'nome': 'Pendências',
                    'include': 'propriedade-rural/tab-pendencia.html',
                    'visivel': true,
                },
                /*{
                    'nome': 'Complementos',
                    'include': 'propriedade-rural/tab-complemento.html',
                    'visivel': false,
                },*/
            ];
            // {nome:'Complementos',url:'propriedade-rural/tab-complemento.html'}, 
            // Fim Trabalho com tabs

            // Inicio Trabalho com uso do solo
            // Fim Trabalho com uso do solo


            // Filtros de segurança by Emerson
            $scope.editar = function(scp) {
                if (! $rootScope.token.lotacaoAtual) {
                    toastr.error('Usuário não possui lotação!', 'Erro'); 
                    return;
                }
                //Não pode editar atividade de outra gerência
                else if ( $scope.cadastro.registro.comunidade.unidadeOrganizacional.id !== $rootScope.token.lotacaoAtual.id ) {
                     toastr.error('Propriedade cadastrada em outra unidade organizacional!', 'Erro'); 
                } else {
                    $rootScope.editar(scp);
                }
            };


            $scope.toggleChildren = function(scope) {
                scope.toggle();
            };
            $scope.visible = function(item) {
                return !($scope.cadastro.apoio.localFiltro &&
                    $scope.cadastro.apoio.localFiltro.length > 0 &&
                    item.nome.trim().toLowerCase().latinize().indexOf($scope.cadastro.apoio.localFiltro.trim().toLowerCase().latinize()) === -1);
            };

            $scope.limpar = function(scp) {
                var e = scp.navegador.estadoAtual();
                if ('FILTRANDO' === e) {
                    $scope.cadastro.apoio.localFiltro = $scope.limparRegistroSelecionado($scope.cadastro.apoio.localList);
                    $scope.cadastro.apoio.filtro.map.markers = [];
                    $scope.cadastro.apoio.filtro.map.polys = [];
                }
                $rootScope.limpar(scp);
            };


            // inicio dos watches
            $scope.$watch('cadastro.registro.id + cadastro.registro.comunidade.id', function(newValue, oldValue) {
                $scope.tabs[1].visivel = $scope.cadastro.registro.id > 0;
                $scope.tabs[2].visivel = $scope.cadastro.registro.id > 0 && angular.isObject($scope.cadastro.registro.comunidade) && $scope.cadastro.registro.comunidade.id > 0;
            });
            $scope.$watch('cadastro.registro.comunidade', function(newValue, oldValue) {
                if (newValue && newValue.id) {
                    $scope.cadastro.apoio.baciaHidrograficaList = [];
                    if ($scope.cadastro.apoio.comunidadeBaciaHidrograficaList) {
                        $scope.cadastro.apoio.comunidadeBaciaHidrograficaList.forEach(function(item) {
                            if (newValue.id === item.comunidade.id) {
                                $scope.cadastro.apoio.baciaHidrograficaList.push(item.baciaHidrografica);
                            }
                        });
                    }
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
                    linhas[0] += e.logradouro;
                }
                if (e.complemento) {
                    linhas[0] += (linhas[0].length ? ', ' : '') + e.complemento;
                }
                if (e.numero) {
                    linhas[0] += (linhas[0].length ? ', ' : '') + 'numero ' + e.numero;
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
                $scope.cadastro.apoio.enderecoResumo = result;
            });
            $scope.$watch('cadastro.registro.pendenciaList', function(newValue, oldValue) {
                $scope.alertas = [];
                if ($scope.cadastro.registro.pendenciaList && $scope.cadastro.registro.pendenciaList.length > 0) {
                    var erro = null, alerta = null;
                    $scope.cadastro.registro.pendenciaList.forEach(function(item) {
                        if (item.tipo === 'E') {
                            erro = {tipo:'danger', mensagem: 'Este registro possui ERROS de cadastro!'};
                        }
                        if (item.tipo === 'A') {
                            alerta = {tipo:'warning', mensagem: 'Este registro possui AVISOS de cadastro!'};
                        }
                    });
                    if (erro) {
                        $scope.alertas.push(erro);
                    }
                    if (alerta) {
                        $scope.alertas.push(alerta);
                    }
                    $interval(function(){
                        $(".piscar").fadeOut();
                        $(".piscar").fadeIn();
                    }, 2000);
                }
            });

            // fim dos watches

            // inicio: mapa formulario
            $scope.cadastro.apoio.form = {};
            $scope.cadastro.apoio.form.elementoId = angular.extend(0, 0, $scope.cadastro.apoio.form.elementoId);

            $scope.visualizarDepois = function(registro) {
                if (registro.formaUtilizacaoEspacoRuralList) {
                    registro.formaUtilizacaoEspacoRuralList.forEach(function(elemento) {
                        delete elemento['@jsonId'];
                        delete elemento['id'];
                        delete elemento['formaUtilizacaoEspacoRural']['@jsonId'];
                    });
                }

                return $scope.incluirDepois(registro);
            };
            $scope.incluirDepois = function(registro) {
                $scope.executarOuPostergar(function() {
                    return $scope.cadastro.apoio.form.map;
                }, function(registro) {
                    $scope.cadastro.apoio.form.map.markers = [];
                    $scope.cadastro.apoio.form.map.polys = [];

                    if (!registro || !registro.endereco) {
                        return;
                    }

                    if (registro.endereco.entradaPrincipal) {
                        $scope.cadastro.apoio.form.map.markers.push({
                            "elementoId": ++$scope.cadastro.apoio.form.elementoId,
                            "latitude": registro.endereco.entradaPrincipal.coordinates[0],
                            "longitude": registro.endereco.entradaPrincipal.coordinates[1]
                        });
                        // centralizar o ponto
                        $scope.executarOuPostergar(function() {
                            return $scope.cadastro.apoio.form.map.controle.getGMap;
                        }, function(pos) {
                            $scope.cadastro.apoio.form.map.controle.getGMap().panTo(pos);
                            $scope.cadastro.apoio.form.map.controle.getGMap().setZoom(15);
                        }, {
                            lat: $scope.cadastro.apoio.form.map.markers[0].latitude,
                            lng: $scope.cadastro.apoio.form.map.markers[0].longitude
                        }, 4000, true);
                    }

                    if (registro.endereco.areaList) {
                        registro.endereco.areaList.forEach(function(area) {
                            var poly = {
                                "elementoId": ++$scope.cadastro.apoio.form.elementoId,
                                "path": [],
                                "id": area.id,
                                "nome": area.nome
                            };
                            area.poligono.coordinates.forEach(function(coordinate) {
                                coordinate.forEach(function(c) {
                                    poly.path.push({
                                        latitude: c[0],
                                        longitude: c[1]
                                    });
                                });
                            });
                            $scope.cadastro.apoio.form.map.polys.push(poly);
                        });
                    }
                }, registro, 500);
            };
            $scope.confirmarEditarAntes = function(cadastro) {
                removerCampo($scope.cadastro.registro, ['@jsonId']);
                return $scope.confirmarIncluirAntes(cadastro);
            };
            $scope.confirmarIncluirAntes = function(cadastro) {
                if (!cadastro.registro.endereco) {
                    cadastro.registro.endereco = {};
                }
                if ($scope.cadastro.apoio.form.map.markers && $scope.cadastro.apoio.form.map.markers.length) {
                    cadastro.registro.endereco.entradaPrincipal = {
                        type: 'Point',
                        coordinates: [$scope.cadastro.apoio.form.map.markers[0].latitude, $scope.cadastro.apoio.form.map.markers[0].longitude]
                    };
                } else {
                    cadastro.registro.endereco.entradaPrincipal = null;
                }

                if ($scope.cadastro.apoio.form.map.polys && $scope.cadastro.apoio.form.map.polys.length) {
                    if (!cadastro.registro.endereco.entradaPrincipal) {
                        throw "Favor informar a entrada principal da propriedade rural no mapa";
                    }
                    cadastro.registro.endereco.areaList = [];

                    $scope.cadastro.apoio.form.map.polys.forEach(function(poly) {
                        var area = {
                            "id": poly.id,
                            "nome": poly.nome,
                            "poligono": {
                                type: 'Polygon',
                                coordinates: []
                            }
                        };
                        var coord = [];
                        poly.path.forEach(function(p) {
                            coord.push([p.latitude, p.longitude]);
                        });
                        // ajustar o ultimo ponto se necessario
                        if (coord.length && (coord[0][0] !== coord[coord.length - 1][0] || coord[0][1] !== coord[coord.length - 1][1])) {
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

                $scope.cadastro.apoio.form.map = angular.extend({}, {
                    center: {
                        latitude: -15.732687616157767,
                        longitude: -47.90378594955473,
                        lat: -15.732687616157767,
                        lng: -47.90378594955473,
                    },
                    bounds: {},
                    controle: {},
                    events: {},
                    maps: maps,
                    markers: [],
                    options: {
                        disableDefaultUI: false,
                        scrollwheel: true,
                        mapTypeId: google.maps.MapTypeId.SATELLITE,
                        label: true,
                    },
                    pan: true,
                    polys: [],
                    refresh: true,
                    zoom: 15,
                }, $scope.cadastro.apoio.form.map);

                $scope.cadastro.apoio.form.drawingManager = angular.extend({}, {
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

                            if ($scope.cadastro.apoio.form.map.markers.length >= 1) {
                                toastr.error('Só é possível marcar o ponto principal da Propriedade Rural', 'Erro de Marcação');
                                return;
                            }
                            $scope.cadastro.apoio.form.map.markers.push({
                                "elementoId": ++$scope.cadastro.apoio.form.elementoId,
                                "latitude": marker[0].getPosition().lat(),
                                "longitude": marker[0].getPosition().lng(),
                            });
                        },
                        polygoncomplete: function(gObject, eventName, model, polygon) {
                            polygon[0].setMap(null);

                            if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                                toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                                return;
                            }

                            var path = [];
                            polygon[0].getPath().forEach(function(element, index) {
                                path.push({
                                    "latitude": element.lat(),
                                    "longitude": element.lng()
                                });
                            });

                            if (path.length > 2) {
                                $scope.cadastro.apoio.form.map.polys.push({
                                    "elementoId": ++$scope.cadastro.apoio.form.elementoId,
                                    "path": path
                                });
                            }
                        },
                    }
                }, $scope.cadastro.apoio.form.drawingManager);

                $scope.cadastro.apoio.form.marker = {
                    controle: {},
                    coords: 'self',
                    opcoes: {
                        draggable: true,
                    },
                    eventos: {
                        click: function(a, b, c, d, e) {
                            $scope.$apply(function() {
                                if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                                    toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                                    return;
                                }
                                $scope.cadastro.apoio.form.map.selecionado = c;
                            });
                        },
                    },
                };

                $scope.cadastro.apoio.form.poly = {
                    controle: {},
                    path: 'path',
                    eventos: {
                        click: function(a, b, c, d, e) {
                            $scope.$apply(function() {
                                if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                                    toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                                    return;
                                }
                                a.setDraggable(true);
                                a.setEditable(true);
                                $scope.cadastro.apoio.form.map.selecionado = c;
                            });
                        },
                    },
                };

            });

            $scope.cadastro.apoio.form.searchbox = {
                template: "form.searchBox.tpl.html",
                eventos: {
                    places_changed: function(searchBox) {
                        var place = searchBox.getPlaces();
                        if (!place || place === 'undefined' || place.length === 0) {
                            console.log('no place data :(');
                            return;
                        }

                        $scope.cadastro.apoio.form.map.zoom = 15;
                        $scope.cadastro.apoio.form.map.center = {
                            "latitude": place[0].geometry.location.lat(),
                            "longitude": place[0].geometry.location.lng(),
                        };
                    }
                }
            };

            $scope.cadastro.apoio.form.removeElemento = function() {
                if (['INCLUINDO', 'EDITANDO'].indexOf($scope.navegador.estadoAtual()) < 0) {
                    toastr.error('Primeiro ative a inclusão ou edição de registros', 'Erro ao marcar');
                    return;
                }

                var i;
                bloco: {
                    for (i = $scope.cadastro.apoio.form.map.markers.length - 1; i >= 0; i--) {
                        if ($scope.cadastro.apoio.form.map.markers[i].elementoId === $scope.cadastro.apoio.form.map.selecionado.elementoId) {
                            $scope.cadastro.apoio.form.map.markers.splice(i, 1);
                            break bloco;
                        }
                    }
                    for (i = $scope.cadastro.apoio.form.map.polys.length - 1; i >= 0; i--) {
                        if ($scope.cadastro.apoio.form.map.polys[i].elementoId === $scope.cadastro.apoio.form.map.selecionado.elementoId) {
                            $scope.cadastro.apoio.form.map.polys.splice(i, 1);
                            break bloco;
                        }
                    }
                }
                $scope.cadastro.apoio.form.map.selecionado = null;
            };
            ponteControllerSrv.setScp($scope);
            // fim: mapa formulario

            // inicio: mapa filtro
            $scope.cadastro.apoio.filtro = {};
            $scope.cadastro.apoio.filtro.elementoId = angular.extend(0, 0, $scope.cadastro.apoio.filtro.elementoId);

            $scope.confirmarFiltrarAntes = function(filtro) {

                filtro.empresaList = [];
                filtro.unidadeOrganizacionalList = [];
                filtro.comunidadeList = [];
                var i, j, k;
                for (i in $scope.cadastro.apoio.localList) {
                    // filtrar as empressas
                    if ($scope.cadastro.apoio.localList[i].selecionado) {
                        filtro.empresaList.push({
                            id: $scope.cadastro.apoio.localList[i].id,
                            '@class': 'br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica'
                        });
                    } else {
                        for (j in $scope.cadastro.apoio.localList[i].unidadeList) {
                            // filtrar as unidades organizacionais
                            if ($scope.cadastro.apoio.localList[i].unidadeList[j].selecionado) {
                               filtro.unidadeOrganizacionalList.push({id: $scope.cadastro.apoio.localList[i].unidadeList[j].id, '@class': 'br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional'});
                            } else {
                                for (k in $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList) {
                                    // filtrar as unidades organizacionais
                                    if ($scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].selecionado) {
                                        filtro.comunidadeList.push({
                                            id: $scope.cadastro.apoio.localList[i].unidadeList[j].comunidadeList[k].id
                                        });
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

                if ($scope.cadastro.apoio.filtro.map && $scope.cadastro.apoio.filtro.map.polys && $scope.cadastro.apoio.filtro.map.polys.length) {
                    filtro.areaList = [];

                    $scope.cadastro.apoio.filtro.map.polys.forEach(function(poly) {
                        var area = {
                            "id": poly.id,
                            "nome": poly.nome,
                            "poligono": {
                                type: 'Polygon',
                                coordinates: []
                            }
                        };
                        var coord = [];
                        poly.path.forEach(function(p) {
                            coord.push([p.latitude, p.longitude]);
                        });
                        // ajustar o ultimo ponto se necessario
                        if (coord.length && (coord[0][0] !== coord[coord.length - 1][0] || coord[0][1] !== coord[coord.length - 1][1])) {
                            coord.push([coord[0][0], coord[0][1]]);
                        }
                        area.poligono.coordinates.push(coord);
                        filtro.areaList.push(area);
                    });
                } else {
                    filtro.areaList = null;
                }
            };

            uiGmapGoogleMapApi.then(function(maps) {

                $scope.cadastro.apoio.filtro.map = angular.extend({}, {
                    bounds: {},
                    center: {
                        latitude: -15.732687616157767,
                        longitude: -47.90378594955473,
                        lat: -15.732687616157767,
                        lng: -47.90378594955473
                    },
                    controle: {},
                    events: {},
                    maps: maps,
                    markers: [],
                    options: {
                        disableDefaultUI: false,
                        scrollwheel: true,
                        mapTypeId: google.maps.MapTypeId.SATELLITE,
                        label: true,
                    },
                    pan: true,
                    polys: [],
                    refresh: true,
                    zoom: 15,
                }, $scope.cadastro.apoio.filtro.map);

                $scope.cadastro.apoio.filtro.drawingManager = angular.extend({}, {
                    controle: {},
                    opcoes: {
                        drawingControl: true,
                        drawingControlOptions: {
                            position: maps.ControlPosition.TOP_CENTER,
                            drawingModes: [
                                maps.drawing.OverlayType.POLYGON,
                            ]
                        }
                    },
                    eventos: {
                        markercomplete: function(gObject, eventName, model, marker) {
                            marker[0].setMap(null);

                            if ($scope.cadastro.apoio.filtro.map.markers.length >= 1) {
                                toastr.error('Só é possível marcar o ponto principal da Propriedade Rural', 'Erro de Marcação');
                                return;
                            }
                            $scope.cadastro.apoio.filtro.map.markers.push({
                                "elementoId": ++$scope.cadastro.apoio.filtro.elementoId,
                                "latitude": marker[0].getPosition().lat(),
                                "longitude": marker[0].getPosition().lng(),
                            });
                        },
                        polygoncomplete: function(gObject, eventName, model, polygon) {
                            polygon[0].setMap(null);

                            var path = [];
                            polygon[0].getPath().forEach(function(element, index) {
                                path.push({
                                    "latitude": element.lat(),
                                    "longitude": element.lng()
                                });
                            });

                            if (path.length > 2) {
                                $scope.cadastro.apoio.filtro.map.polys.push({
                                    "elementoId": ++$scope.cadastro.apoio.filtro.elementoId,
                                    "path": path
                                });
                            }
                        },
                    }
                }, $scope.cadastro.apoio.filtro.drawingManager);

                $scope.cadastro.apoio.filtro.marker = {
                    controle: {},
                    coords: 'self',
                    opcoes: {
                        draggable: true,
                    },
                    eventos: {
                        click: function(a, b, c, d, e) {
                            $scope.$apply(function() {
                                $scope.cadastro.apoio.filtro.map.selecionado = c;
                            });
                        },
                    },
                };

                $scope.cadastro.apoio.filtro.poly = {
                    controle: {},
                    path: 'path',
                    eventos: {
                        click: function(a, b, c, d, e) {
                            $scope.$apply(function() {
                                a.setDraggable(true);
                                a.setEditable(true);
                                $scope.cadastro.apoio.filtro.map.selecionado = c;
                            });
                        },
                    },
                };

            });

            $scope.cadastro.apoio.filtro.searchbox = {
                template: "filtro.searchBox.tpl.html",
                eventos: {
                    places_changed: function(searchBox) {
                        var place = searchBox.getPlaces();
                        if (!place || place === 'undefined' || place.length === 0) {
                            console.log('no place data :(');
                            return;
                        }

                        $scope.cadastro.apoio.filtro.map.zoom = 15;
                        $scope.cadastro.apoio.filtro.map.center = {
                            "latitude": place[0].geometry.location.lat(),
                            "longitude": place[0].geometry.location.lng(),
                        };
                    }
                }
            };

            $scope.cadastro.apoio.filtro.removeElemento = function() {
                var i;
                bloco: {
                    for (i = $scope.cadastro.apoio.filtro.map.markers.length - 1; i >= 0; i--) {
                        if ($scope.cadastro.apoio.filtro.map.markers[i].elementoId === $scope.cadastro.apoio.filtro.map.selecionado.elementoId) {
                            $scope.cadastro.apoio.filtro.map.markers.splice(i, 1);
                            break bloco;
                        }
                    }
                    for (i = $scope.cadastro.apoio.filtro.map.polys.length - 1; i >= 0; i--) {
                        if ($scope.cadastro.apoio.filtro.map.polys[i].elementoId === $scope.cadastro.apoio.filtro.map.selecionado.elementoId) {
                            $scope.cadastro.apoio.filtro.map.polys.splice(i, 1);
                            break bloco;
                        }
                    }
                }
                $scope.cadastro.apoio.filtro.map.selecionado = null;
            };
            ponteControllerSrv.setScp($scope);
            // fim: mapa filtro

        }
    ]);

    // inicio: mapa formulario
    angular.module(pNmModulo).controller('MapaCtrl', ['$scope', 'ponteControllerSrv',
        function($scope, ponteControllerSrv) {
            'ngInject';

            $scope.removeElemento = function(local) {
                if (local === 'form') {
                    ponteControllerSrv.getScp().cadastro.apoio.form.removeElemento();
                } else {
                    ponteControllerSrv.getScp().cadastro.apoio.filtro.removeElemento();
                }
            };
            $scope.selecionado = function(local) {
                if (local === 'form') {
                    if (!ponteControllerSrv.getScp() || !ponteControllerSrv.getScp().cadastro.apoio.form.map) {
                        return null;
                    } else {
                        return ponteControllerSrv.getScp().cadastro.apoio.form.map.selecionado;
                    }
                } else {
                    if (!ponteControllerSrv.getScp() || !ponteControllerSrv.getScp().cadastro.apoio.filtro.map) {
                        return null;
                    } else {
                        return ponteControllerSrv.getScp().cadastro.apoio.filtro.map.selecionado;
                    }
                }
            };
        }
    ]);

    angular.module(pNmModulo).run(['$templateCache', 'uiGmapLogger',
        function($templateCache, Logger) {
            'ngInject';
            Logger.doLog = true;
            $templateCache.put('form.control.tpl.html', '<button class=\"btn btn-sm btn-danger\" ng-click=\"removeElemento(\'form\')\" ng-show=\"selecionado(\'form\')\">Excluir</button>');
            $templateCache.put('form.searchBox.tpl.html', '<input type=\"text\" ng-model=\"enderecoPesquisa\" placeholder=\"Procurar\" ng-dblclick=\"enderecoPesquisa = \'\'\" style="width: 80%;"/>');
            $templateCache.put('filtro.control.tpl.html', '<button class=\"btn btn-sm btn-danger\" ng-click=\"removeElemento(\'filtro\')\" ng-show=\"selecionado(\'filtro\')\">Excluir</button>');
            $templateCache.put('filtro.searchBox.tpl.html', '<input type=\"text\" ng-model=\"enderecoPesquisa\" placeholder=\"Procurar\" ng-dblclick=\"enderecoPesquisa = \'\'\" style="width: 80%;"/>');
        }
    ]);
    // inicio: mapa formulario

})('propriedadeRural', 'PropriedadeRuralCtrl', 'Cadastro de Propriedades Rurais', 'propriedade-rural');