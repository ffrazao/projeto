(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).controller(pNmController,
    ['$scope', '$uibModalInstance', 'conteudo', 'funcaoOk', 'funcaoCancelar', 'funcaoIncializar',
    function($scope, $uibModalInstance, conteudo, funcaoOk, funcaoCancelar, funcaoIncializar) {
        $scope.conteudo = conteudo;
        $scope.modalOk = function () {
            // Retorno da modal
            if (funcaoOk && !funcaoOk(conteudo, $uibModalInstance)) {
              return;
            }
            $uibModalInstance.close(conteudo);
        };
        $scope.modalCancelar = function () {
            // Cancelar a modal
            if (funcaoCancelar && !funcaoCancelar(conteudo)) {
              return;
            }
            $uibModalInstance.dismiss('cancel');
        };
        if (funcaoIncializar) {
            funcaoIncializar($scope);
        }
    }
]);

angular.module(pNmModulo).factory(pNmFactory,
  ['$uibModal',
    function($uibModal) {
        var formModal =  function (tipo, url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar) {
            var botaoCancelar = null;
            if ('alerta' === tipo) {
                if (!titulo){titulo = 'Atenção!'; }
                 botaoCancelar = '';
            } else if ('confirmacao' === tipo) {
                if (!titulo){titulo = 'Confirme!'; }
                 botaoCancelar = '<button class="btn btn-warning" ng-click=\"modalCancelar()\">Cancelar</button>';
            }
            var nomeForm = tipo + 'Frm';
            var modalInstance = $uibModal.open({
                      animation: true,
                      controller: pNmController,
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
                          funcaoIncializar: function(){
                            return funcaoIncializar;
                          }
                      },
                });
                return modalInstance.result;
        };

        var mensagemSrv = {
            alerta : function (url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar) {
                return formModal('alerta', url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar);
            },
            confirmacao: function (url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar) {
                return formModal('confirmacao', url, mensagem, titulo, conteudo, tamanho, funcaoOk, funcaoCancelar, funcaoIncializar);
            },
        };
        return mensagemSrv;
    }
]);

})('principal', 'mensagemSrv', 'MensagemCtrl');