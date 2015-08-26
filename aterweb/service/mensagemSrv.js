(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$modalInstance', 'conteudo',
    function($scope, $modalInstance, conteudo) {
        $scope.conteudo = conteudo;
        $scope.modalOk = function () {
            // Retorno da modal
            $modalInstance.close(conteudo);
        };
        $scope.modalCancelar = function () {
            // Cancelar a modal
            $modalInstance.dismiss('cancel');
        };
    }
]);

angular.module(pNmModulo).factory(pNmFactory,
  ['$modal',
    function($modal) {
        var formModal =  function (tipo, url, mensagem, titulo, conteudo) {
            var botaoCancelar = null;
            if ('alerta' === tipo) {
                if (!titulo) {titulo = 'Atenção!'};
                 botaoCancelar = '';
            } else if ('confirmacao' === tipo) {
                if (!titulo) {titulo = 'Confirme!'};
                 botaoCancelar = '<button class="btn btn-warning" ng-click=\"modalCancelar()\">Cancelar</button>';
            }
            var nomeForm = tipo + 'Frm';
            var modalInstance = $modal.open({
                      animation: true,
                      controller: 'MensagemCtrl',
                      template:
                      '<div class="modal-header">' +
                      '  <h3 class="modal-title">' + titulo + '</h3>' +
                      '</div>' +
                      '<div class="modal-body">' +
                      '  <ng-form name="' + nomeForm + '" class="form-horizontal" novalidate>' +
                      '     <fieldset>' +
                      (url ? '<ng-include src="\'' + mensagem + '\'"></ng-include>' : mensagem) +
                      '     <fieldset>' +
                      '  </ng-form>' +
                      '</div>' +
                      '<div class="modal-footer">' +
                      '  <button class="btn btn-primary" ng-click=\"modalOk()\" ng-show="' + nomeForm + '.$valid">OK</button>' + botaoCancelar +
                      '</div>',
                      resolve: {
                          conteudo: function() {
                              return conteudo;
                          }
                      },
                });
                return modalInstance.result;
        };

        var mensagemSrv = {
            alerta : function (url, mensagem, titulo, conteudo) {
                return formModal('alerta', url, mensagem, titulo, conteudo);
            },
            confirmacao: function (url, mensagem, titulo, conteudo) {
                return formModal('confirmacao', url, mensagem, titulo, conteudo);
            },
        };
        return mensagemSrv;
    }
]);

})('principal', 'mensagemSrv', 'MensagemCtrl');