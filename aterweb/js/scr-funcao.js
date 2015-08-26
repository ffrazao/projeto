/* Arquivo de funções
*/
/**
 * Script Geral
 */
// codigo para criar funcao paracida com o String.format do java
// neste caso a sintaxe é: 
// 'The {0} is dead. Don\'t code {0}. Code {1} that is open source!'.format('ASP', 'PHP');

function isUndefOrNull(variavel) {
    return typeof variavel === "undefined" || variavel === null;
}

String.prototype.format = function() {
    var formatted = this;
    for (var i = 0; i < arguments.length; i++) {
        var regexp = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regexp, arguments[i]);
    }
    return formatted;
};

function humanFileSize(bytes) {
    var thresh = 1024;
    if(bytes < thresh) {return bytes + ' B';}
    var units = ['kB','MB','GB','TB','PB','EB','ZB','YB'];
    var u = -1;
    do {
        bytes /= thresh;
        ++u;
    } while(bytes >= thresh);
    return bytes.toFixed(1)+' '+units[u];
}

function dataToInputData(data) {
    if (isUndefOrNull(data) || data.length !== 10) {
        return null;
    }
    var d = data.split("/");
    return d[2] + "-" + d[1] + "-" + d[0];
}

function inputDataToData(data) {
    if (isUndefOrNull(data) || data.length !== 10) {
        return null;
    }
    var d = data.split("-");
    return d[2] + "/" + d[1] + "/" + d[0];
}

function trocar(pagina){
        var url = '//' + window.location.host + window.location.pathname + '#/' + pagina;
        window.location.href = url;
}
// Menu criatividade!
$.fn.serializeAnything = function(tag) {
    var toReturn = {};
    if (tag !== undefined) {
        tag = '[tag="' + tag + '"]';
    } else {
        tag = '';
    }
    $(this)
            .find(tag + ':input')
            .each(
                    function() {

                        if ((this.name || $(this).attr('nomeCampo')) && 
                            (!this.disabled) && 
                            (this.checked || /select|textarea/i.test(this.nodeName) || /text|hidden|password/i.test(this.type))) {
                            var val = $(this).val();
                            var name = this.name.split('.');

                            if ($(this).attr('nomeCampo') !== undefined) {
                                name = $(this).attr('nomeCampo').split('.');
                            }

                            if (name.length <= 0) {
                                toReturn[name] = val;
                            } else if (name.length > 0 && name.length <= 2) {
                                toReturn[name[1]] = val;
                            }
                        }
                    });

    return toReturn;

};

function removerCampo(contexto, campos) {
    if (isUndefOrNull(contexto)) {
        return;
    } else {
        for (var idx in campos) {
            if (!isUndefOrNull(contexto[campos[idx]])) {
                delete contexto[campos[idx]];
            }
        }
    }
    if (contexto instanceof Array || contexto instanceof Object) {
        $.each(contexto, function(chave, valor) {
            removerCampo(valor, campos);
        });
    }
}

function soNumeros(numero) {
    if (isUndefOrNull(numero)) {
        return null;
    }
    var result = "";
    for (var i = 0; i < numero.length; i++) {
        if (numero[i] >= '0' && numero[i] <= '9') {
            result = result + numero[i];
        }
    }
    return result;
}

function preenche(valor, tamanho, caractere, esquerda) {
    if (isUndefOrNull(valor)) {
        valor = "";
    }
    while (valor.length < tamanho) {
        valor = esquerda? caractere + valor: valor + caractere;
    }
    return valor;
}

function zeroEsq(numero, tamanho) {
    return preenche(numero, tamanho, '0', true);
}

function tudoRepetido(valor) {
    if (isUndefOrNull(valor)) {
        return null;
    }
    if (valor.length < 2) {
        return true;
    }
    var pos = 0;
    var c = valor[pos];
    while (++pos < valor.length) {
        if (valor[pos] !== c) {
            return false;
        }
    }
    return true;
}
