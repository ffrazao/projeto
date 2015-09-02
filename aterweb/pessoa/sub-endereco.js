
'use strict';

angular.module('pessoa').controller('PessoaEnderecoCtrl', ['$scope', 'FrzNavegadorParams', '$modal', '$modalInstance', 'toastr', 'utilSrv', 'mensagemSrv',
	function($scope, FrzNavegadorParams, $modal, $modalInstance, toastr, utilSrv, mensagemSrv) {

  var init = function() {
      if (!angular.isObject($scope.cadastro.registro.enderecoList)) {
          $scope.cadastro.registro.enderecoList = [];
      }
      $scope.pessoaEnderecoNvg = new FrzNavegadorParams($scope.cadastro.registro.enderecoList);
  };

  if (!$modalInstance) {
      init();
  }

  $scope.abrir = function () {
	   $scope.pessoaEnderecoNvg.mudarEstado('ESPECIAL');
  };

  $scope.editar = function (id) {

  };

  $scope.excluir = function () {

  };


  $scope.abreModal = function (item) {

    // abrir a modal
    mensagemSrv.confirmacao(false, '<frz-endereco conteudo="conteudo"/>', 'Cadastro de Endereço', item, 'lg').then(function (conteudo) {
        // processar o retorno positivo da modal
        if (!angular.isArray($scope.cadastro.registro.enderecoList)) {
          init();
        }
        $scope.cadastro.registro.enderecoList.push(conteudo);
    }, function () {
        // processar o retorno negativo da modal
        //$log.info('Modal dismissed at: ' + new Date());
    });

  };


  $scope.incluir = function (size) {
    $scope.abreModal({cep: '12345678'});

    /*var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'pessoaEnderecoFrm.html',
      controller: 'PessoaEnderecoCtrl',
      size: size,
      resolve: {
        registro: function () {
          return $scope.cadastro.registro;
        }
      }
    });

    modalInstance.result.then(function (registro) {
    	if (!registro) {
    		return;
    	}
      if (!$scope.cadastro.registro) {
        $scope.cadastro.registro = {};
      }
      if (!$scope.cadastro.registro.endereco) {
        $scope.cadastro.registro.endereco = [];
      }
    	if (angular.isArray(registro)) {
    		for (var r in registro) {
    			$scope.cadastro.registro.endereco.push(r);
    		}
    	} else {
    		$scope.cadastro.registro.endereco.push(registro);
    	}
    }, function () {
      console.log('Modal dismissed at: ' + new Date());
    });*/
  };

  $scope.pesquisaPessoa = function(size) {

    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'views/pessoa/_modal.html',
      controller: 'PessoaCtrl',
      size: size,
      resolve: {
        registro: function () {
          //return $scope.cadastro.registro;
        }
      }
    });

    modalInstance.result.then(function (registro) {
      if (!registro) {
        return;
      }
      if (!$scope.endereco) {
        $scope.endereco = {};
      }
      if (angular.isArray(registro)) {
        $scope.endereco.pessoa = angular.copy(registro[0]);
      } else {
        $scope.endereco.pessoa = angular.copy(registro);
      }
    }, function () {
      console.log('Modal dismissed at: ' + new Date());
    });
  }

  $scope.items = [];
  $scope.selected = {
    item: $scope.items[0]
  };

  $scope.ok = function () {
  	$modalInstance.close($scope.endereco);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

  $scope.navegarPrimeiro = function () {
  };

  $scope.navegarAnterior = function () {
  };

  $scope.navegarPosterior = function () {
  };

  $scope.navegarUltimo = function () {
  };

} // fim função
]);