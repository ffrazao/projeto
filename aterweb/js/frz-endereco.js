/* global isUndefOrNull, google */ 

(function () {

    'use strict';

    /*
     * @author Fernando Frazao
     * @date 14/02/2015
     * @modify 14/02/2015
     */
    angular.module('frz.endereco', ['uiGmapgoogle-maps']).directive('frzEndereco', ['$http', 'toastr', '$rootScope', '$q', 'UtilSrv', 'uiGmapGoogleMapApi', 
        'PessoaSrv',
        function($http, toastr, $rootScope, $q, UtilSrv, uiGmapGoogleMap, uiGmapMarkers, uiGmapGoogleMapApi, PessoaSrv) {

            return {
                restrict : 'E',
                templateUrl : 'js/frz-endereco.html',
                scope : {
                    conteudo : '='
                },
                replace : true,
                transclude : true,
                link : function(scope, element, attrs) {

                    scope.apoio = {estadoList:[], municipioList: [], cidadeList: []};

                    UtilSrv.dominio({ent: ['Estado'], npk: 'pais.id', vpk: scope.conteudo.pais.id}).success(function(resposta) {
                        if (resposta && resposta.resultado) {
                            scope.apoio.estadoList = resposta.resultado[0];
                        }
                    });

                    scope.$watch('conteudo.estado', function(newValue, oldValue) {
                        if (newValue && newValue.id) {
                            UtilSrv.dominioLista(scope.apoio.municipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue.id]});
                        } else {
                            scope.apoio.municipioList = [];
                        }
                    });

                    scope.$watch('conteudo.municipio', function(newValue, oldValue) {
                        if (newValue && newValue.id) {
                            UtilSrv.dominioLista(scope.apoio.cidadeList, {ent:['Cidade'], npk: ['municipio.id'], vpk: [newValue.id]});
                        } else {
                            scope.apoio.cidadeList = [];
                        }
                    });

                    // scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };

                    scope.map = {center: {latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15, bounds: {}};
                    scope.options = {scrollwheel: false};
                    scope.drawingManagerOptions = {
                      drawingMode: google.maps.drawing.OverlayType.MARKER,
                      drawingControl: true,
                      drawingControlOptions: {
                        position: google.maps.ControlPosition.TOP_CENTER,
                          drawingModes: [
                            google.maps.drawing.OverlayType.MARKER,
                            google.maps.drawing.OverlayType.CIRCLE,
                            google.maps.drawing.OverlayType.POLYGON,
                            google.maps.drawing.OverlayType.POLYLINE,
                            google.maps.drawing.OverlayType.RECTANGLE
                          ]
                      },
                      circleOptions: {
                        fillColor: '#ffff00',
                          fillOpacity: 0.3,
                          strokeWeight: 5,
                          clickable: true,
                          editable: true,
                          zIndex: 1
                        }
                      };
                    scope.markersAndCircleFlag = true;
                    scope.drawingManagerControl = {};
                    scope.$watch('markersAndCircleFlag', function() {
                      if (!scope.drawingManagerControl.getDrawingManager) {
                        return;
                      }
                      var controlOptions = angular.copy(scope.drawingManagerOptions);
                      if (!scope.markersAndCircleFlag) {
                        controlOptions.drawingControlOptions.drawingModes.shift();
                        controlOptions.drawingControlOptions.drawingModes.shift();
                      }
                      scope.drawingManagerControl.getDrawingManager().setOptions(controlOptions);
                      console.log(scope.drawingManagerControl.getDrawingManager());
                    });

                    scope.$watch('drawingManagerControl.getDrawingManager', function() {
                      if (scope.drawingManagerControl.getDrawingManager) {
                        console.log(scope.drawingManagerControl.getDrawingManager());
                        // scope.drawingManager = scope.drawingManagerControl.getDrawingManager();
                        // scope.mapa = scope.drawingManager.getMap();
                        //scope.mapa.getMap().
                      }
                    });

                    // uiGmapGoogleMapApi.then(function(maps) {
                    //   console.log(maps);
                    // });

                    scope.markerList = [
                      {
                        id: 10,
                        latitude: -15.145,
                        longitude: -47.660,
                        nome: 'tesge a'
                      },
                      {
                        id: 11,
                        latitude: -15.141,
                        longitude: -47.668,
                        nome: 'tesge b'
                      },
                      {
                        id: 12,
                        latitude: -15.151,
                        longitude: -47.6680,
                        nome: 'tesge c'
                      },
                    ];

                    // ativar o atualizador de endereço
                    scope.buscarCep = function(cep) {
                        if (!isUndefOrNull(cep) && cep.length === 8) {
                            $http.get($rootScope.servicoUrl + '/pessoa' + '/buscar-cep', {params: {'cep': cep}}).success(
                            function(data, status, headers, config) {
                                if (!data.resultado || !data.mensagem || data.mensagem !== 'OK') {
                                    toastr.error('CEP {0} não localizado!'.format(scope.conteudo.cep), 'Erro');
                                    console.log(data);
                                } else {
                                    scope.conteudo.codigoIbge = data.resultado.codigoIbge;
                                    scope.conteudo.pais = data.resultado.pais;
                                    scope.conteudo.estado = data.resultado.estado;
                                    scope.conteudo.municipio = data.resultado.municipio;
                                    scope.conteudo.cidade = data.resultado.cidade;
                                    scope.conteudo.bairro = data.resultado.bairro;
                                    scope.conteudo.logradouro = data.resultado.logradouro;
                                    scope.conteudo.complemento = data.resultado.complemento;
                                    scope.conteudo.numero = null;
                                    toastr.info('O CEP {0} foi localizado!'.format(scope.conteudo.cep), 'Sucesso');
                                }
                            }).error(
                            function(data) {
                                toastr.error(data.mensagem, 'Erro ao acessar o serviço de busca de CEP');
                            }, true);
                        } else {
                            toastr.error('Informações incompletas!', 'CEP');
                        }
                    };
                },
            };
        }
    ]);
})();