angular.module('principal').controller('MensagemCtrl', ['$scope', '$modalInstance', 'conteudo',
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

angular.module('principal').factory('mensagemSrv', ['$modal',
    function($modal) {
        var formModal =  function (tipo, mensagem, conteudo) {
            var titulo = null;
            var botaoCancelar = null;
            if ('alerta' === tipo) {
                titulo = 'Atenção!';
                 botaoCancelar = '';
            } else if ('confirmacao' === tipo) {
                 titulo = 'Confirme!';
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
                  mensagem +
                  '     <fieldset>' +
                  '  </ng-form>' +
                  '</div>' +
                  '<div class="modal-footer">' +
                  '  <button class="btn btn-primary" ng-click=\"modalOk()\" ng-show="' + nomeForm + '.$valid">OK</button>' +
                  botaoCancelar +
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
            alerta : function (mensagem, conteudo) {
                return formModal('alerta', mensagem, conteudo);
            },
            confirmacao: function (mensagem, conteudo) {
                return formModal('confirmacao', mensagem, conteudo);
            },
        };
        return mensagemSrv;
    }
]);