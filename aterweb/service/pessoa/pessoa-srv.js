/* global removerCampo */ 

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'FormularioSrv', 'ComunidadeSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, FormularioSrv, ComunidadeSrv) {
        'ngInject';
        
        var PessoaSrv = {
            funcionalidade: 'PESSOA',
            endereco: $rootScope.servicoUrl + '/pessoa',
            abrir : function(scp) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                UtilSrv.dominio({ent: [
                   'PessoaTipo',
                   'PessoaGenero',
                   'PessoaGeracao',
                   'PessoaSituacao',
                   'PublicoAlvoSegmento',
                   'PublicoAlvoCategoria',
                   'Setor',
                   'Comunidade',
                   'Confirmacao',
                   'ConfirmacaoDap',
                   'Pais',
                   'Escolaridade',
                   'Profissao',
                   'EstadoCivil',
                   'RegimeCasamento',
                   'PessoaNacionalidade',
                   'CamOrgao',
                   'CnhCategoria',
                   'UnidadeOrganizacional',
                   'MeioContatoFinalidade',
                   'TelefoneTipo',
                   'RelacionamentoConfiguracaoVi',
                   'PropriedadeRuralVinculoTipo',
                   'Situacao',
                   'OrganizacaoTipo',
                   'Estado',
                   'PendenciaTipo',
                   'GrupoSocialTipo',
                   'GrupoSocialEscopo'
                ]}).success(function(resposta) {
                    if (resposta && resposta.mensagem === 'OK') {
                        scp.cadastro.apoio.pessoaTipoList = resposta.resultado[0];
                        scp.cadastro.apoio.generoList = resposta.resultado[1];
                        scp.cadastro.apoio.pessoaGeracaoList = resposta.resultado[2];
                        scp.cadastro.apoio.pessoaSituacaoList = resposta.resultado[3];
                        scp.cadastro.apoio.publicoAlvoSegmentoList = resposta.resultado[4];
                        scp.cadastro.apoio.publicoAlvoCategoriaList = resposta.resultado[5];
                        scp.cadastro.apoio.publicoAlvoSetorList = resposta.resultado[6];
                        scp.cadastro.apoio.comunidadeList = resposta.resultado[7];
                        scp.cadastro.apoio.confirmacaoList = resposta.resultado[8];
                        scp.cadastro.apoio.confirmacaoDapList = resposta.resultado[9];
                        scp.cadastro.apoio.paisList = resposta.resultado[10];
                        scp.cadastro.apoio.escolaridadeList = resposta.resultado[11];
                        scp.cadastro.apoio.profissaoList = resposta.resultado[12];
                        scp.cadastro.apoio.estadoCivilList = resposta.resultado[13];
                        scp.cadastro.apoio.regimeCasamentoList = resposta.resultado[14];
                        scp.cadastro.apoio.nacionalidadeList = resposta.resultado[15];
                        scp.cadastro.apoio.camOrgaoList = resposta.resultado[16];
                        scp.cadastro.apoio.cnhCategoriaList = resposta.resultado[17];
                        scp.cadastro.apoio.unidadeOrganizacionalList = resposta.resultado[18];
                        scp.cadastro.apoio.meioContatoFinalidadeList = [];
                        var codigo = '';
                        var descricao = '';
                        for (var i in resposta.resultado[19]) {
                          scp.cadastro.apoio.meioContatoFinalidadeList.push(resposta.resultado[19][i]);
                          if (codigo !== '') {
                            codigo += ',';
                            descricao += ' & ';
                          }
                          codigo +=  resposta.resultado[19][i].codigo;
                          descricao +=  resposta.resultado[19][i].descricao;
                        }
                        scp.cadastro.apoio.meioContatoFinalidadeList.push({'codigo': codigo, 'descricao': descricao});
                        scp.cadastro.apoio.telefoneTipoList = resposta.resultado[20];
                        scp.cadastro.apoio.relacionamentoConfiguracaoViList = resposta.resultado[21];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[22];
                        scp.cadastro.apoio.situacaoList = resposta.resultado[23];
                        scp.cadastro.apoio.organizacaoTipoList = resposta.resultado[24];
                        scp.cadastro.apoio.estadoList = resposta.resultado[25];
                        scp.cadastro.apoio.pendenciaTipoList = resposta.resultado[26];
                        scp.cadastro.apoio.grupoSocialTipoList = resposta.resultado[27];
                        scp.cadastro.apoio.grupoSocialEscopoList = resposta.resultado[28];

                        scp.cadastro.apoio.tradicaoList = [];
                        var anoAtual = new Date().getFullYear();
                        for (var ano = anoAtual; ano > anoAtual - 100; ano--) {
                            scp.cadastro.apoio.tradicaoList.push(ano);
                        }

                        removerCampo(scp.cadastro.apoio.comunidadeList, ['@jsonId']);
                        scp.cadastro.apoio.publicoAlvoSegmentoListOriginal = angular.copy(scp.cadastro.apoio.publicoAlvoSegmentoList);

                        // preparar a lista de tipo de relacionamento
                        scp.cadastro.apoio.relacionamentoConfiguracaoViList.forEach(function(item) {
                          item.id = item.relacionadoId;
                          item.nomeSeFeminino = item.relacionadoNomeSeFeminino;
                          item.nomeSeMasculino = item.relacionadoNomeSeMasculino;
                        });

                        var involucro = [];
                        scp.cadastro.apoio.publicoAlvoSetorList.forEach(function(elemento) {
                          delete elemento['@jsonId'];
                          involucro.push({'setor': elemento});
                        });
                        scp.cadastro.apoio.publicoAlvoSetorList = involucro;
                    }
                });
                if ($rootScope.isAuthenticated()) {
                    var t = $rootScope.token;

                    scp.cadastro.apoio.localList = [];
                    var fltr = null;
                    if (scp.cadastro.apoio.unidadeOrganizacionalSomenteLeitura) {
                        fltr = {unidadeOrganizacionalList: scp.cadastro.filtro.unidadeOrganizacionalList};
                    } else {
                        fltr = {pessoaJuridicaList: (t && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) ? [angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)] : null};
                    }
                    ComunidadeSrv.lista(fltr, scp.cadastro.apoio.localList, t);

                    if (!t || !t.lotacaoAtual || !t.lotacaoAtual.pessoaJuridica) {
                        toastr.warning('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                    }
                }
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/novo', registro);
            },
            incluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', registro);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', registro);
            },
            excluir : function(registro) {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
                return $http.delete(this.endereco + '/excluir', {params: {'id': registro.id}});
            },
            // funcoes especiais
            formularioFiltrarComColeta : function(filtro) {
                return FormularioSrv.filtrarComColeta(filtro);
            },
            buscarCep : function(cep) {
                return $http.get(this.endereco + '/buscar-cep', {params: {'cep': cep}});
            },
            carteiraProdutorVerificar : function (filtro) {
                return $http.post(this.endereco + '/carteira-produtor-verificar', filtro);
            },
            carteiraProdutorRel : function (filtro) {
                return $http.post(this.endereco + '/carteira-produtor-rel', filtro);
            },
            publicoAlvoPorPessoaId : function (id) {
                return $http.get(this.endereco + '/publico-alvo-por-pessoa-id', {params: {'id': id}});
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');