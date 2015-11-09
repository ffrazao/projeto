/* global isUndefOrNull */ 

(function () {

	'use strict';

	/*
	 * @author Fernando Frazao
	 * @date 14/02/2015
	 * @modify 14/02/2015
	 */

	angular.module("frz.endereco", []).directive('frzEndereco', ['$http', 'toastr', '$rootScope', '$q', 'UtilSrv', 
		function($http, toastr, $rootScope, $q, UtilSrv/*, uiGmapGoogleMapApi*/) {

			return {
				restrict : 'E',
				templateUrl : 'js/frz-endereco.html',
				scope : {
					conteudo : "="
				},
				replace : true,
				transclude : true,
				link : function(scope, element, attrs) {

					scope.apoio = {estadoList:[], municipioList: [], cidadeList: []};

					UtilSrv.dominio({ent: ['Estado'], npk: 'pais.id', vpk: 1}).success(function(resposta) {
						if (resposta && resposta.resultado) {
							scope.apoio.estadoList = resposta.resultado[0];
						}
					});

					scope.$watch('conteudo.estado.id', function(newValue, oldValue) {
						if (newValue && newValue > 0) {
							UtilSrv.dominioLista(scope.apoio.municipioList, {ent:['Municipio'], npk: ['estado.id'], vpk: [newValue]});
						} else {
							scope.apoio.municipioList = [];
						}
					});

					scope.$watch('conteudo.municipio.id', function(newValue, oldValue) {
						if (newValue && newValue > 0) {
							UtilSrv.dominioLista(scope.apoio.cidadeList, {ent:['Cidade'], npk: ['municipio.id'], vpk: [newValue]});
						} else {
							scope.apoio.cidadeList = [];
						}
					});

					scope.map = { center: { latitude: -15.732687616157767, longitude: -47.90378594955473 }, zoom: 15 };


					// ativar o atualizador de endereço

					/*scope.buscaCep = function() {
					scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.pessoaGrupoPaisVi.id = scope.brasil;
					//$rootScope.emProcessamento(true);
					if (!isUndefOrNull(scope.conteudo) && !isUndefOrNull(scope.conteudo.cep) && scope.conteudo.cep.length === 8) {
					$http.get("/aterweb/pessoa-cad/buscarCep/", {"params": {"cep": scope.conteudo.cep}})
					.success(function(data, status, headers, config) {
					if (!data.executou) {
					toastr.error("CEP {0} não localizado!".format(scope.conteudo.cep), data.mensagem);
					console.log(data);
					} else {
					scope.conteudo.codigoIbge = data.resultado.ibge;
					scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.pessoaGrupoEstadoVi.id = data.resultado.uf;
					scope.conteudo.pessoaGrupoCidadeVi.pessoaGrupoMunicipioVi.id = data.resultado.localidade;
					scope.conteudo.pessoaGrupoCidadeVi.id = null;
					scope.conteudo.bairro = data.resultado.bairro;
					scope.conteudo.propriedadeRural.pessoaGrupoComunidadeVi.id = null;
					scope.conteudo.propriedadeRural.pessoaGrupoBaciaHidrograficaVi.id = null;
					scope.conteudo.logradouro = data.resultado.logradouro;
					scope.conteudo.complemento = null;
					scope.conteudo.numero = null;
					toastr.info("CEP localizado", "O CEP {0} foi localizado!".format(scope.conteudo.cep));
					}
					//$rootScope.emProcessamento(false);
					}).error(function(data) {
					toastr.error("Erro ao acessar o serviço de busca de CEP", data.mensagem);
					console.log(data);
					//$rootScope.emProcessamento(false);
					}, true);
					} else {
					toastr.error("CEP", "Informações incompletas!");
					//$rootScope.emProcessamento(false);
					}
					};*/

					// scope.map = {
					//  center : {
					//      latitude : -15.732805,
					//      longitude : -47.903791
					//  },
					//  zoom : 10
					// };

					// uiGmapGoogleMapApi.then(function(maps) {
					//  console.log(maps);
					//    });



				},
			};
		}
	]);
})();