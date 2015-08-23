angular.module('principal', ['ui.bootstrap', 'ui.utils', 'ui.router', 'ngSanitize', 'ngAnimate', 'toastr', 'sticky', 'ui.mask', 'ui.utils.masks', 'ui.navbar', 'frz.arquivo', 'frz.endereco', 'frz.painel.vidro', 'frz.navegador', 'casa', 'contrato', 'pessoa', 'propriedade']);

// inicio: codigo para habilitar o modal recursivo
angular.module('principal').factory('$modalInstance', function () {
  return null;
});
angular.module('principal').factory('modalCadastro', function () {
  return null;
});
// fim: codigo para habilitar o modal recursivo

angular.module('principal').config(['$stateProvider', '$urlRouterProvider', 'toastrConfig', '$locationProvider',
  function($stateProvider, $urlRouterProvider, toastrConfig, $locationProvider) {

    //$locationProvider.html5Mode(true);
    $stateProvider.state('p', {templateUrl: 'casa/principal.html'});
    
    $stateProvider.state('p.bem-vindo', {
      url: '/',
      templateUrl: 'casa/bem-vindo.html'
    });

    $stateProvider.state('p.casa', {
      url: '',
      templateUrl: 'casa/index.html',
      controller: ['$stateParams', 'toastr', function($stateParams, toastr){
        console.log($stateParams.mensagem);
            //toastr.warning('Endereço não encontrado! ' + $stateParams.mensagem, 'Atenção!');
          }],
        });
    $stateProvider.state('login', {
      url: '/login',
      templateUrl: 'login/login.html',
      controller: 'LoginCtrl'
    });

    /* Add New States Above */
    $urlRouterProvider.otherwise(function ($injector, $location) {
      var $state = $injector.get('$state');

      $state.go('casa', {mensagem: 'Endereço não localizado!' + $location.$$absUrl}, {'location': true});
    });
    // configuracao do toastr
    angular.extend(toastrConfig, {
      allowHtml: false,
      autoDismiss: false,
      closeButton: true,
      closeHtml: '<button>&times;</button>',
      containerId: 'toast-container',
      extendedTimeOut: 1000,
      iconClasses: {
        error: 'toast-error',
        info: 'toast-info',
        success: 'toast-success',
        warning: 'toast-warning'
      },
      maxOpened: 0,    
      messageClass: 'toast-message',
      newestOnTop: true,
      onHidden: null,
      onShown: null,
      positionClass: 'toast-bottom-full-width',
      preventDuplicates: false,
      preventOpenDuplicates: true,
      progressBar: true,
      tapToDismiss: true,
      target: 'body',
      templates: {
        toast: 'directives/toast/toast.html',
        progressbar: 'directives/progressbar/progressbar.html'
      },
      timeOut: 4000,
      titleClass: 'toast-title',
      toastClass: 'toast'
    });

  }]);

angular.module('principal').run(['$rootScope', '$modal', 
  function($rootScope, $modal) {
    $rootScope.localizacao = "pt-br";
    $rootScope.safeApply = function(fn) {
      var phase = $rootScope.$$phase;
      if (phase === '$apply' || phase === '$digest') {
        if (fn && (typeof(fn) === 'function')) {
          fn();
        }
      } else {
        this.$apply(fn);
      }
    };
    // fim funcoes de apoio
}]);
