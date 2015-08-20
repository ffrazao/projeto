/* global isUndefOrNull */ 

(function () {

  'use strict';

    /*
     * @author Fernando Frazao
     * @date 14/02/2015
     * @modify 14/02/2015
     */

     angular.module("frz.arquivo", ['flow']).directive('frzArquivo', [function() {

      return {
        restrict : 'E',
        templateUrl : 'js/frz-arquivo.html',
        replace : true,
        transclude : true,
        link : function(scope, element, attrs) {
        }
      };
    }]);

   })();