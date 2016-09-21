/* global removerCampo */

(function(pNmModulo, pNmController, pNmFormulario) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', 'toastr', 'UtilSrv', 'mensagemSrv', '$log',
    function($scope, toastr, UtilSrv, mensagemSrv, $log) {

    'ngInject';

    // inicio rotinas de apoio
    var init = function() {
        
    };
    init();
    // fim rotinas de apoio

    // inicio das operaçoes atribuidas ao navagador

    // fim das operaçoes atribuidas ao navagador


} // fim função
]);

})('projetoCreditoRural', 'ProjetoCreditoRuralReceitaDespesaApoioCtrl', 'Apoio à Receitas e Despesas do Projeto de Crédito Rural');