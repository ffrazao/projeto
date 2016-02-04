/* global removerCampo */ 

(function(pNmModulo, pNmFactory, pNmController) {

'use strict';

angular.module(pNmModulo).factory(pNmFactory,
  ['$rootScope', '$http', 'toastr', 'SegurancaSrv', 'UtilSrv', '$stateParams', 'FormularioSrv', 'TokenStorage', 'UnidadeOrganizacionalSrv',
    function($rootScope, $http, toastr, SegurancaSrv, UtilSrv, $stateParams, FormularioSrv, TokenStorage, UnidadeOrganizacionalSrv) {
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
                   'RelacionamentoFuncao',
                   'PropriedadeRuralVinculoTipo',
                   'Situacao',
                ]}).success(function(resposta) {
                    if (resposta && resposta.resultado) {
                        scp.cadastro.apoio.pessoaTipoList = resposta.resultado[0];
                        scp.cadastro.apoio.generoList = resposta.resultado[1];
                        scp.cadastro.apoio.pessoaGeracaoList = resposta.resultado[2];
                        scp.cadastro.apoio.pessoaSituacaoList = resposta.resultado[3];
                        scp.cadastro.apoio.publicoAlvoSegmentoList = resposta.resultado[4];
                        scp.cadastro.apoio.publicoAlvoCategoriaList = resposta.resultado[5];
                        scp.cadastro.apoio.setorList = resposta.resultado[6];
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
                        scp.cadastro.apoio.relacionamentoFuncaoList = resposta.resultado[21];
                        scp.cadastro.apoio.propriedadeRuralVinculoTipoList = resposta.resultado[22];
                        scp.cadastro.apoio.situacaoList = resposta.resultado[23];

                        scp.cadastro.apoio.tradicaoList = [];
                        var anoAtual = new Date().getFullYear();
                        for (var ano = anoAtual; ano > anoAtual - 100; ano--) {
                            scp.cadastro.apoio.tradicaoList.push(ano);
                        }

                        removerCampo(scp.cadastro.apoio.comunidadeList, ['@jsonId', 'unidadeOrganizacional']);
                    }
                });
                var t = TokenStorage.token();
                if (t && t.lotacaoAtual && t.lotacaoAtual && t.lotacaoAtual.pessoaJuridica) {
                    UnidadeOrganizacionalSrv.comunidade(angular.fromJson(t.lotacaoAtual.pessoaJuridica.id)).success(function(resposta) {
                        if (resposta && resposta.resultado && resposta.resultado.length) {
                            var r = resposta.resultado;
                            var empresa = angular.copy(t.lotacaoAtual.pessoaJuridica);
                            var unid = {id: null}; var comunid = {id: null};
                            for (var i in r) {
                                if (unid.id !== r[i].unidadeOrganizacional.id) {
                                    unid = angular.copy(r[i].unidadeOrganizacional);
                                    if (!empresa.unidadeList) {
                                        empresa.unidadeList = [];
                                    }
                                    if (unid.id === t.lotacaoAtual.id) {
                                        unid.selecionado = true;
                                    }
                                    empresa.unidadeList.push(unid);
                                }
                                if (comunid.id !== r[i].comunidade.id) {
                                    if (!unid.comunidadeList) {
                                        unid.comunidadeList = [];
                                    }
                                    comunid = angular.copy(r[i].comunidade);
                                    unid.comunidadeList.push(comunid);
                                }
                            }
                            scp.cadastro.apoio.localList = [];
                            scp.cadastro.apoio.localList.push(empresa);
                        }
                    });
                } else {
                    toastr.error('Não foi possível identificar a sua lotação', 'Erro ao carregar os dados');
                }
            },
            filtrar : function(filtro) {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
                return $http.post(this.endereco + '/filtro-executar', filtro);
            },
            executarFiltro : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'CONSULTAR');
            },
            novo : function(pessoaTipo) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.get(this.endereco + '/novo', {params: {modelo: pessoaTipo}});
            },
            incluir : function(pessoa) {
                SegurancaSrv.acesso(this.funcionalidade, 'INCLUIR');
                return $http.post(this.endereco + '/incluir', pessoa);
            },
            visualizar : function(id) {
                SegurancaSrv.acesso(this.funcionalidade, 'VISUALIZAR');
                return $http.get(this.endereco + '/visualizar', {params: {'id': id}});
            },
            editar : function(pessoa) {
                SegurancaSrv.acesso(this.funcionalidade, 'EDITAR');
                return $http.post(this.endereco + '/editar', pessoa);
            },
            excluir : function() {
                SegurancaSrv.acesso(this.funcionalidade, 'EXCLUIR');
            },

            // funcoes especiais
            formularioFiltrarComColeta : function(filtro) {
                return FormularioSrv.filtrarComColeta(filtro);
            },
        };
        return PessoaSrv;
    }
]);

})('principal', 'PessoaSrv');