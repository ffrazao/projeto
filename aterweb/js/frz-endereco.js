/* global isUndefOrNull, google */ 

(function () {

    'use strict';

    /*
     * @author Fernando Frazao
     * @date 14/02/2015
     * @modify 14/02/2015
     */
    angular.module("frz.endereco", ['uiGmapgoogle-maps']).directive('frzEndereco', ['$http', 'toastr', '$rootScope', '$q', 'UtilSrv', 'uiGmapGoogleMapApi',
        function($http, toastr, $rootScope, $q, UtilSrv, uiGmapGoogleMap, uiGmapMarkers, uiGmapGoogleMapApi) {

            return {
                restrict : 'E',
                templateUrl : 'js/frz-endereco.html',
                scope : {
                    conteudo : "="
                },
                replace : true,
                transclude : true,
                link : function(scope, element, attrs) {

                    scope.apoio = {estadoList:[], municipioList: [], cidadeList: []};

                    UtilSrv.dominio({ent: ['Estado'], npk: 'pais.id', vpk: 1}).success(function(resposta) {
                        if (resposta && resposta.resultado) {
                            scope.apoio.estadoList = resposta.resultado[0];
                        }
                    });

                    scope.$watch('conteudo.estado.id', function(newValue, oldValue) {
                        if (newValue && newValue > 0) {
                            UtilSrv.dominioLista(scope.apoio.municipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
                        } else {
                            scope.apoio.municipioList = [];
                        }
                    });

                    scope.$watch('conteudo.municipio.id', function(newValue, oldValue) {
                        if (newValue && newValue > 0) {
                            UtilSrv.dominioLista(scope.apoio.cidadeList, {ent:['Cidade'], npk: ['municipio.id'], vpk: [newValue]});
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

                    // scope.ponto = new google.maps.Marker(
                    //   {
                    //     position: new google.maps.LatLng(-15.732687616157767, -47.90378594955473),
                    //     title: "Meu ponto personalizado! :-D",
                    //     map: scope.map
                    //   }
                    // );


                    // ativar o atualizador de endereço

                    /*scope.buscaCep = function() {
                    scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id = scope.brasil;
                    //$rootScope.emProcessamento(true);
                    if (!isUndefOrNull(scope.conteudo) && !isUndefOrNull(scope.conteudo.cep) && scope.conteudo.cep.length === 8) {
                    $http.get("/aterweb/pessoa-cad/buscarCep/", {"params": {"cep": scope.conteudo.cep}})
                    .success(function(data, status, headers, config) {
                    if (!data.executou) {
                    toastr.error("CEP {0} não localizado!".format(scope.conteudo.cep), data.mensagem);
                    console.log(data);
                    } else {
                    scope.conteudo.codigoIbge = data.resultado.ibge;
                    scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id = data.resultado.uf;
                    scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id = data.resultado.localidade;
                    scope.conteudo.pessoaGrupoCidadeVi.id = null;
                    scope.conteudo.bairro = data.resultado.bairro;
                    scope.conteudo.propriedadeRural.pessoaGrupoComunidadeVi.id = null;
                    scope.conteudo.propriedadeRural.pessoaGrupoBaciaHidrograficaVi.id = null;
                    scope.conteudo.logradouro = data.resultado.logradouro;
                    scope.conteudo.complemento = null;
                    scope.conteudo.numero = null;
                    toastr.info("CEP localizado", "O CEP {0} foi localizado!".format(scope.conteudo.cep));
                    }
                    //$rootScope.emProcessamento(false);
                    }).error(function(data) {
                    toastr.error("Erro ao acessar o serviço de busca de CEP", data.mensagem);
                    console.log(data);
                    //$rootScope.emProcessamento(false);
                    }, true);
                    } else {
                    toastr.error("CEP", "Informações incompletas!");
                    //$rootScope.emProcessamento(false);
                    }
                    };*/

                    // scope.map = {
                    //  center : {
                    //      latitude : -15.732805,
                    //      longitude : -47.903791
                    //  },
                    //  zoom : 10
                    // };

                    // uiGmapGoogleMapApi.then(function(maps) {
                    //  console.log(maps);
                    //    });



                },
            };
        }
    ]);
})();