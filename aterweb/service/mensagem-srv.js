(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$modalInstance', 'conteudo', 'funcaoOk', 'funcaoCancelar',
    function($scope, $modalInstance, conteudo, funcaoOk, funcaoCancelar) {
        $scope.conteudo = conteudo;
        $scope.modalOk = function () {
            // Retorno da modal
            if (funcaoOk && !funcaoOk(conteudo)) {
              return;
            }
            $modalInstance.close(conteudo);
        };
        $scope.modalCancelar = function () {
            // Cancelar a modal
            if (funcaoCancelar && !funcaoCancelar(conteudo)) {
              return;
            }
            $modalInstance.dismiss('cancel');
        };
    }
]);

angular.module(pNmModulo).factory(pNmFactory,
  ['$modal',
    function($modal) {
        var formModal =  function (tipo, url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar) {
            var botaoCancelar = null;
            if ('alerta' === tipo) {
                if (!titulo){titulo = 'Atenção!'; }
                 botaoCancelar = '';
            } else if ('confirmacao' === tipo) {
                if (!titulo){titulo = 'Confirme!'; }
                 botaoCancelar = '<button class="btn btn-warning" ng-click=\"modalCancelar()\">Cancelar</button>';
            }
            var nomeForm = tipo + 'Frm';
            var modalInstance = $modal.open({
                      animation: true,
                      controller: 'MensagemCtrl',
                      size : (tamanho ? tamanho : 'lg' ),
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
                          },
                          funcaoOk: function() {
                            return funcaoOk;
                          },
                          funcaoCancelar: function(){
                            return funcaoCancelar;
                          },
                      },
                });
                return modalInstance.result;
        };

        var mensagemSrv = {
            alerta : function (url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar) {
                return formModal('alerta', url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar);
            },
            confirmacao: function (url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar) {
                return formModal('confirmacao', url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar);
            },
        };
        return mensagemSrv;
    }
]);

})('principal', 'mensagemSrv', 'MensagemCtrl');