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

function isUndefOrNullOrEmpty(variavel) {
    return typeof variavel === "undefined" || variavel === null || variavel.length === 0;
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

function geraUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c==='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function isString(s) {
    return typeof(s) === 'string' || s instanceof String;
}

function isObject(s) {
    return typeof(s) === 'object' || s instanceof Object;
}

function isArray(s) {
    return isObject(s) || s instanceof Array;
}

function parseISODate (input) {
    var iso = /^(\d{4})(?:-?W(\d+)(?:-?(\d+)D?)?|(?:-(\d+))?-(\d+))(?:[T ](\d+):(\d+)(?::(\d+)(?:\.(\d+))?)?)?(?:Z(-?\d*))?$/;

    var parts = input.match(iso);

    if (parts === null) {
        throw new Error("Invalid Date");
    }

    var days, fractional, hours, localzone, milliseconds, minutes, months, seconds, timezone, weeks, year;

    year = Number(parts[1]);

    if (typeof parts[2] !== "undefined") {
        /* Convert weeks to days, months 0 */
        weeks = Number(parts[2]) - 1;
        days  = Number(parts[3]);

        if (typeof days === "undefined") {
            days = 0;
        }

        days += weeks * 7;

        months = 0;
    }
    else {
        if (typeof parts[4] !== "undefined") {
            months = Number(parts[4]) - 1;
        }
        else {
            /* it's an ordinal date... */
            months = 0;
        }

        days   = Number(parts[5]);
    }

    if (typeof parts[6] !== "undefined" &&
        typeof parts[7] !== "undefined")
    {
        hours        = Number(parts[6]);
        minutes      = Number(parts[7]);

        if (typeof parts[8] !== "undefined") {
            seconds      = Number(parts[8]);

            if (typeof parts[9] !== "undefined") {
                fractional   = Number(parts[9]);
                milliseconds = fractional / 100;
            }
            else {
                milliseconds = 0;
            }
        }
        else {
            seconds      = 0;
            milliseconds = 0;
        }
    }
    else {
        hours        = 0;
        minutes      = 0;
        seconds      = 0;
        fractional   = 0;
        milliseconds = 0;
    }

    if (typeof parts[10] !== "undefined") {
        /* Timezone adjustment, offset the minutes appropriately */
        localzone = -(new Date().getTimezoneOffset());
        timezone  = parts[10] * 60;

        minutes = Number(minutes) + (timezone - localzone);
    }

    return new Date(year, months, days, hours, minutes, seconds, milliseconds);
}

function converterStringParaData(obj) {
    if (!obj) {
        return obj;
    } else if (isString(obj)) {
        try {
            var data = parseISODate(obj);
            return data;
        } catch (exception) {
            return obj;
        }
    } else if (isObject(obj) || isArray(obj)) {
        for (var a in obj) {
            obj[a] = converterStringParaData(obj[a]);
        }
    }
    return obj;
}

function toCamelCase(sentenceCase) {
    if (isUndefOrNull(sentenceCase)) {
        return null;
    }
    var out = "";
    sentenceCase.split(" ").forEach(function (el, idx) {
        var add = el.toLowerCase();
        out += (idx === 0 ? add : add[0].toUpperCase() + add.slice(1));
    });
    return out;
}