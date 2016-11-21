/* jslint evil: true, browser: true, plusplus: true, loopfunc: true */
/* global criarEstadosPadrao, removerCampo, isUndefOrNull */ 

(function(pNmModulo, pNmController, pNmFormulario, pUrlModulo) {

    'use strict';

    angular.module(pNmModulo, ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngAnimate', 'frz.navegador', 'frz.form', 'ngSanitize']);
    
    angular.module(pNmModulo).config(['$stateProvider', function($stateProvider) {
        'ngInject';

        criarEstadosPadrao($stateProvider, pNmModulo, pNmController, pUrlModulo);
    }]);

    angular.module(pNmModulo).constant('uiCalendarConfig', {calendars: {}});

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'AgendaSrv', 'AtividadeSrv',
         '$compile', 'uiCalendarConfig',
        function($scope,  toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, AgendaSrv, AtividadeSrv, $compile, uiCalendarConfig) {
            'ngInject';            

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, AgendaSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);            
    
            $scope.eventSource = {
                    googleCalendarApiKey: 'AIzaSyA7cCD_hoZyFmAS1IXGOizoBO1xMaHDRvc',
                    className: 'gcal-event',           // an option!
                    currentTimezone: 'America/Chicago' // an option!
            };
            /* event source that contains custom events on the scope */
            $scope.confirmarFiltrarDepois = function(){
              $scope.eventSources[0].length = 0;
              $scope.cadastro.lista.forEach(function (v) {
                $scope.eventSources[0].push(v);
              });
            };

            $scope.events = [];

            var salvar = function(registro, modal) {
                removerCampo(registro, ['@jsonId']);
                AtividadeSrv.salvar(registro.cadastro).success(function(resposta) {
                    if (resposta && resposta.mensagem && resposta.mensagem === 'OK') {
                        $scope.confirmarFiltrar($scope, $scope.cadastro.filtro.numeroPagina, $scope.cadastro.filtro.temMaisRegistros);
                        toastr.info('Operação realizada!', 'Informação');
                        modal.dismiss();
                    } else {
                        toastr.error(resposta && resposta.mensagem ? resposta.mensagem : resposta, 'Erro ao salvar');
                    }
                }).error(function(erro) {
                    toastr.error(erro, 'Erro ao salvar');
                });

            };
            
            var editarItem = function (destino, item) {
                var arr = [];
                if (!destino) {
                    arr.push({registro: {}});
                } else {                
                    arr.push({registro: item});
                }
                if (!arr.length) {
                    toastr.error('Dados não informados', 'Erro ao abrir formulário');
                } else {
                    arr.forEach(function(registro) {
                        var modalInstance = $uibModal.open({
                            animation: true,
                            controller: 'AtividadeCtrl',
                            size : 'lg',
                            templateUrl: 'atividade/atividade-form-modal.html',
                            resolve: {
                                modalCadastro: function() {
                                    registro.modalOk = salvar;
                                    return registro;
                                },
                            },
                        });
                        modalInstance.result.then(function(conteudo) {
                        }, function () {
                        });
                    });
                }
            };

            /* alert on eventClick */
            $scope.alertOnEventClick = function( date, jsEvent, view){
                editarItem($scope.events, {id: date.id});
                $scope.alertMessage = (date.title + ' was clicked ');
            };

            $scope.eventMouseover = function (Events, jsEvent) {
            var tooltip = '<div class="tooltipevent">' + calEvent.description + '</div>';
            $("body").append(tooltip);
            $(this).mouseover(function (e) {
            $(this).css('z-index', 10000);
            $('.tooltipevent').fadeIn('500');
            $('.tooltipevent').fadeTo('10', 1.9);
            }).mousemove(function (e) {
            $('.tooltipevent').css('top', e.pageY + 10);
            $('.tooltipevent').css('left', e.pageX + 20);
            });
            }

            $scope.eventMouseout = function (calEvent, jsEvent) {
            $(this).css('z-index', 8);
            $('.tooltipevent').remove();
            },
            $scope.eventRender = function( event, element, view ) { 
              $timeout(function(){
            $(element).attr('tooltip', event.title);
            $compile(element)($scope);
            });
            };

            /* alert on Drop */
             $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){
               $scope.alertMessage = ('Event Droped to make dayDelta ' + delta);
            };
            /* alert on Resize */
            $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){
               $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
            };
            /* add and removes an event source of choice */
            $scope.addRemoveEventSource = function(sources,source) {
              var canAdd = 0;
              angular.forEach(sources,function(value, key){
                if(sources[key] === source){
                  sources.splice(key,1);
                  canAdd = 1;
                }
              });
              if(canAdd === 0){
                sources.push(source);
              }
            };
            /* add custom event*/
            $scope.addEvent = function() {
              $scope.events.push({
                title: 'Nova Atividade',
                start: new Date(y, m, d),
                end: new Date(y, m, d),
                className: ['novaAtividade']
              });
            };
            /* remove event */
            $scope.remove = function(index) {
              $scope.events.splice(index,1);
            };
            /* Change View */
            $scope.changeView = function(view,calendar) {
              uiCalendarConfig.calendars[calendar].fullCalendar('changeView',view);
            };
            /* Change View */
            $scope.renderCalendar = function(calendar) {
              if(uiCalendarConfig.calendars[calendar]){
                uiCalendarConfig.calendars[calendar].fullCalendar('render');
              }
            };
             /* Render Tooltip */
            $scope.eventRender = function( event, element, view ) { 
                element.attr({'tooltip': event.title,
                             'tooltip-append-to-body': true});
                $compile(element)($scope);
            };
            /* config object */
            $scope.uiConfig = {
              calendar:{
                height: 450,
                editable: true,
                header:{
                  left: 'title',
                  center: '',
                  right: 'today prev,next'
                },
                eventClick: $scope.alertOnEventClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize,
                eventRender: $scope.eventRender
              }
            };
                
                $scope.uiConfig.calendar.months = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
                $scope.uiConfig.calendar.monthsShort = ["Jan", "Fev", "Mar", "Abr", "Maio", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"];
                $scope.uiConfig.calendar.dayNames = ["Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"];
                $scope.uiConfig.calendar.dayNamesShort = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

            /* event sources array*/
            $scope.eventSources = [$scope.events];
          }
    ]);

})('agenda', 'AgendaCtrl', 'Cadastro de Agenda', 'agenda');