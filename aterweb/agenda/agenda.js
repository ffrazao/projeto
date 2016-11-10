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

    angular.module(pNmModulo).controller(pNmController, ['$scope', 'toastr', 'FrzNavegadorParams', '$state', '$rootScope', '$uibModal', '$log', '$uibModalInstance', 'modalCadastro', 'UtilSrv', 'mensagemSrv', 'AtividadeSrv',
         '$compile', 'uiCalendarConfig',
        function($scope,  toastr, FrzNavegadorParams, $state, $rootScope, $uibModal, $log, $uibModalInstance, modalCadastro, UtilSrv, mensagemSrv, AtividadeSrv, $compile, uiCalendarConfig) {
            'ngInject';            

            // inicializacao
            $scope.crudInit($scope, $state, null, pNmFormulario, AtividadeSrv);

            // código para verificar se o modal está ou não ativo
            $scope.verificaEstado($uibModalInstance, $scope, 'filtro', modalCadastro, pNmFormulario);

            // inicio: atividades do Modal
            var date = new Date();
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();
            
    
            $scope.eventSource = {
                    googleCalendarApiKey: 'AIzaSyA7cCD_hoZyFmAS1IXGOizoBO1xMaHDRvc',
                    /*url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",*/
                    className: 'gcal-event',           // an option!
                    currentTimezone: 'America/Chicago' // an option!
            };
            /* event source that contains custom events on the scope */
            $scope.events = [];
            /* event source that calls a function on every view switch */
            $scope.eventsF = function (start, end, timezone, callback) {
              var s = new Date(start).getTime() / 1000;
              var e = new Date(end).getTime() / 1000;
              var m = new Date(start).getMonth();
              var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
              callback(events);
            };

            $scope.calEventsExt = {
               color: '#f00',
               textColor: 'yellow',
               events: [ 
                  {type:'party',title: 'Lunch',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
                  {type:'party',title: 'Lunch 2',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
                  {type:'party',title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
                ]
            };
            /* alert on eventClick */
            $scope.alertOnEventClick = function( date, jsEvent, view){
                $scope.alertMessage = (date.title + ' was clicked ');
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
                title: 'Novo Evento',
                start: new Date(y, m, d),
                end: new Date(y, m, d),
                className: ['novoEvento']
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

                $scope.uiConfig.calendar.dayNames = ["Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"];
                $scope.uiConfig.calendar.dayNamesShort = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

            /* event sources array*/
            $scope.eventSources = [$scope.events, $scope.eventSource, $scope.eventsF];
            $scope.eventSources2 = [$scope.calEventsExt, $scope.eventsF, $scope.events];
        }
    ]);

})('agenda', 'AgendaCtrl', 'Cadastro de Agenda', 'agenda');