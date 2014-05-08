var Q = {};

//配置信息
Q.config = {
    //是否开发环境，为true时在发起ajax时优先使用Q.devData中匹配到的数据（可以分离后台）
    isDevEnv: false,
    isHtml5Support: false,
    //web context 名称
    webContext: '',
    //ajax请求的根路径
    baseAjax: '',
    //Qin框架的前台web路径
    qinPath: 'qin',
    //token名称
    tokenName: '_token',
    //是否支持jsonp，需要服务端也支持
    isJsonpSupport: false,
    //各类错误页面web路径
    page401: null,
    page403: null,
    page404: null,
    page500: null,
    page504: null
};

Q.load = function (config) {
    if (null != config) {
        for (var customKey in config) {
            for (var key in Q.config) {
                if (customKey === key) {
                    Q.config[key] = config[customKey];
                }
            }
        }
    }
    if (!Q.config.page401) {
        Q.config.page401 = Q.config.webContext + '/' + Q.config.qinPath + '/401.html';
    }
    if (!Q.config.page403) {
        Q.config.page403 = Q.config.webContext + '/' + Q.config.qinPath + '/403.html';
    }
    if (!Q.config.page404) {
        Q.config.page404 = Q.config.webContext + '/' + Q.config.qinPath + '/404.html';
    }
    if (!Q.config.page500) {
        Q.config.page500 = Q.config.webContext + '/' + Q.config.qinPath + '/500.html';
    }
    if (!Q.config.page504) {
        Q.config.page504 = Q.config.webContext + '/' + Q.config.qinPath + '/504.html';
    }
    Q._init();
    Q.ajax = Q._ajax();
    Q.cookie = Q._cookie();
    Q.view = Q._view();
    Q.form = Q._form();
    Q.utils = Q._utils();
}

/**
 * 开发环境的mock数据，格式为：
 *  {
 *  <uri，支持正则>:
 *      {
 *         get/post/put/delete: <返回的数据>
 *      }
 *  }
 */
Q.devData = {};

/**
 * ajax 命名空间
 */
Q.ajax = null;
/**
 * cookie 命名空间
 */
Q.cookie = null;
/**
 * 视图 命名空间
 */
Q.view = null;
/**
 * 表单操作
 */
Q.form = null;
/**
 * 辅助工具 命名空间
 */
Q.utils = null;

//---------------------------------------Inner Function---------------------------------------

Q._init = function () {
    if (Q.config.isHtml5Support) {
        document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"\/>');
        document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap/js/bootstrap.min.js" type="text/javascript"><\/script>');
        document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"\/>');
        document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"><\/script>');
        document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"><\/script>');
        document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css" rel="stylesheet" type="text/css"\/>');
        document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/bootstrap-colorpicker/js/bootstrap-colorpicker.js" type="text/javascript"><\/script>');
    }
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/jquery.poshytip/tip-darkgray/tip-darkgray.css" rel="stylesheet" type="text/css"\/>');
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/validation/validationEngine.jquery.css" rel="stylesheet" type="text/css"\/>');
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_panel/q_panel.css" rel="stylesheet" type="text/css"\/>');
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_form/q_form.css" rel="stylesheet" type="text/css"\/>');
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_textarea/q_textarea.css" rel="stylesheet" type="text/css"\/>');
    document.write('<link href="' + Q.config.webContext + '/' + Q.config.qinPath + '/css/Qin.css" rel="stylesheet" type="text/css"\/>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/json2.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/underscore/underscore-min.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/date.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/jquery.poshytip/jquery.poshytip.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/validation/jquery.validationEngine-zh_CN.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/validation/jquery.validationEngine.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/jquery.serializeJSON/jquery.serializeJSON.min.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/ajaxfileupload.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_plugin/q_plugin.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_panel/q_panel.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_form/q_form.js" type="text/javascript"><\/script>');
    document.write('<script src="' + Q.config.webContext + '/' + Q.config.qinPath + '/plugin/q_textarea/q_textarea.js" type="text/javascript"><\/script>');
};

Q._ajax = function () {

    var $loading = $('<div style="position:absolute;top:50%;left:50%;width:100px;margin-left:-50px;border: 3px double #000;padding:4px;z-index:10000;font-size: 9pt;background-color:#FFF;display: none">加载中...</div>');
    var $shade = $('<div style="position:absolute;top:0;left:0;width:100%;height:100%;z-index:9999;background-color: #cecece;filter:alpha(opacity=50); -moz-opacity:0.5;opacity:0.5;display: none"></div>');
    var modalCount = 0;
    var $notice = $('<dl style="position: fixed;right:10px;bottom: 10px;border-color:#ec8248;border-style: solid;border-width:2px 1px 1px 1px;background-color: white;z-index: 1000;width: 250px;min-height: 200px;font-size: 9pt;overflow:auto;display: none;"><dt style="height: 30px;line-height: 30px;text-align: center;background-color: #FFEBD0;font-weight: bold;">提示 <a href="#this"> [ X ]</a></dt></dl>');

    function _init() {
        $('body').append($shade);
        $('body').append($loading);
        $('body').append($notice);
        $notice.data('isExpand', true);
        $notice.find('dt a').click(function () {
            $notice.find('dd').remove();
            $notice.css('height', null);
            $notice.data('isExpand', true);
            $notice.hide();
        });
    }

    $(function () {
        _init();
    });

    function ajax(type, url, param, successFun, errorFun, isModal, dataType, contentType, isAsync, beforeSendFun, completeFun, customSuccessFun) {
        Q.view.log('Load URL:' + url + ',Type:' + type);
        if (Q.config.isDevEnv) {
            if (null != Q.devData) {
                for (var urlKey in Q.devData) {
                    if (new RegExp(urlKey).test(url)) {
                        if (null != Q.devData[urlKey]) {
                            for (var methodKey in Q.devData[urlKey]) {
                                if (type.toUpperCase() === methodKey.toUpperCase()) {
                                    if (undefined != customSuccessFun && null != customSuccessFun) {
                                        customSuccessFun({code: 200, msg: '', body: Q.devData[urlKey][methodKey]});
                                        if (isAsync) {
                                            return Q.devData[urlKey][methodKey];
                                        }
                                        return null;
                                    } else {
                                        undefined != successFun && null != successFun && successFun(Q.devData[urlKey][methodKey]);
                                        if (isAsync) {
                                            return Q.devData[urlKey][methodKey];
                                        }
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Q.view.log('Not found local data at method:[ ' + type + ' ] url:[ ' + url + ' ].');
        }
        if (undefined == contentType || null == contentType) {
            contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        } else if ('json' == contentType.toLocaleLowerCase()) {
            contentType = 'application/json;charset=UTF-8';
            if (undefined != param && null != param) {
                param = JSON.stringify(param);
            }
        }
        if (undefined == dataType || null == dataType) {
            dataType = "JSON";
        }
        if ("JSON" == dataType.toUpperCase() && Q.config.isJsonpSupport) {
            dataType = "JSONP";
            contentType = 'application/jsonp;charset=UTF-8';
        }
        if (undefined == isAsync || null == isAsync) {
            isAsync = true;
        }
        url = Q.config.baseAjax + '/' + url;
        if (-1 == url.indexOf('?')) {
            url += '?';
        } else {
            url += '&';
        }
        url += '_token=' + Q.cookie.get(Q.config.tokenName) + '&_method=' + type;
        var syncResult = null;
        $.ajax({
            type: type,
            url: encodeURI(url),
            data: param,
            dataType: dataType,
            jsonpCallback: 'JsonpCallback',
            contentType: contentType,
            cache: false,
            async: isAsync,
            beforeSend: function () {
                if (undefined != isModal && isModal == true) {
                    if (undefined != beforeSendFun && null != beforeSendFun) {
                        beforeSendFun();
                    } else {
                        ++modalCount;
                        $shade.show();
                        $loading.show();
                    }
                }
            },
            complete: function () {
                if (undefined != isModal && isModal == true) {
                    if (undefined != completeFun && null != completeFun) {
                        completeFun();
                    } else {
                        if (--modalCount == 0) {
                            $shade.hide();
                            $loading.hide();
                        }
                    }
                }
            }
        }).done(function (result) {
                if (!isAsync) {
                    syncResult = result.body;
                }
                if (undefined != customSuccessFun && null != customSuccessFun) {
                    customSuccessFun(result);
                } else {
                    switch (result.code) {
                        case 200:
                            undefined != successFun && null != successFun && successFun(result.body);
                            break;
                        case 401:
                            //TODO 无法获取最外层，暂时用此方法
                            window.parent.parent.parent.location = Q.config.page401;
                            break;
                        default :
                            _showNotice(result.msg);
                    }
                }
            }).fail(function (result, textStatus, info) {
                if (!isAsync) {
                    syncResult = result;
                }
                if (undefined != errorFun && null !== errorFun) {
                    errorFun(result, textStatus, info);
                } else {
                    _showNotice(info);
                }
            });
        return syncResult;
    }

    function upload(url, fileId, successFun, errorFun) {
        url = Q.config.baseAjax + '/' + url;
        if (-1 == url.indexOf('?')) {
            url += '?';
        } else {
            url += '&';
        }
        url += '_token=' + Q.cookie.get(Q.config.tokenName) + '&_model=POST';
        $.ajaxFileUpload({
                url: encodeURI(url),
                secureuri: false,
                fileElementId: fileId,
                dataType: 'json',
                success: function (result) {
                    undefined != successFun && null != successFun && successFun(result.body);
                },
                error: function (result, textStatus, info) {
                    undefined != errorFun && null !== errorFun && errorFun(result, textStatus, info);
                }
            }
        );
    }

    function _showNotice(msg) {
        if ((null == $notice.data('isExpand') || true == $notice.data('isExpand')) && $notice.outerHeight() + 100 >= $(window).innerHeight()) {
            $notice.css('height', $notice.outerHeight());
            $notice.data('isExpand', false);
        }
        $notice.append('<dd style="margin: 2px;padding:1px;color: red;border-bottom: solid 1px #dddddd;">' + msg + '</dd>');
        $notice.show();
    }

    return {
        original: function (type, url, param, successFun, errorFun, isModal, dataType, contentType, isAsync, beforeSendFun, completeFun, customSuccessFun) {
            ajax(type, url, param, successFun, errorFun, isModal, dataType, contentType, isAsync, beforeSendFun, completeFun, customSuccessFun);
        },
        get: function (url, successFun) {
            ajax('GET', url, null, successFun, null, true, 'JSON', 'JSON', true, null, null, null);
        },
        post: function (url, param, successFun) {
            ajax('POST', url, param, successFun, null, true, 'JSON', 'JSON', true, null, null, null);
        },
        put: function (url, param, successFun) {
            ajax('PUT', url, param, successFun, null, true, 'JSON', 'JSON', true, null, null, null);
        },
        del: function (url, successFun) {
            ajax('DELETE', url, null, successFun, null, true, 'JSON', 'JSON', true, null, null, null);
        },
        getSync: function (url, customSuccessFun) {
            return ajax('GET', url, null, null, null, true, 'JSON', 'JSON', false, null, null, customSuccessFun);
        },
        postSync: function (url, param, customSuccessFun) {
            return ajax('POST', url, param, null, null, true, 'JSON', 'JSON', false, null, null, customSuccessFun);
        },
        putSync: function (url, param, customSuccessFun) {
            return ajax('PUT', url, param, null, null, true, 'JSON', 'JSON', false, null, null, customSuccessFun);
        },
        delSync: function (url, customSuccessFun) {
            return ajax('DELETE', url, null, null, null, true, 'JSON', 'JSON', false, null, null, customSuccessFun);
        },
        upload: function (url, fileId, successFun, errorFun) {
            upload(url, fileId, successFun, errorFun);
        }
    };
};

Q._cookie = function () {
    function add(objName, objValue, objHours) {
        var str = objName + "=" + encodeURIComponent(objValue);
        if (objHours > 0) {
            var date = new Date();
            var ms = objHours * 3600 * 1000;
            date.setTime(date.getTime() + ms);
            str += '; expires=' + date.toGMTString();
        }
        str += ';path=' + Q.config.webContext + '/';
        document.cookie = $.trim(str);
    }

    function get(objName) {
        objName = $.trim(objName);
        var arrStr = document.cookie.split(';');
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split('=');
            if ($.trim(temp[0]) == objName)
                return decodeURIComponent(temp[1]);
        }
    }

    function del(objName) {
        objName = $.trim(objName);
        var date = new Date();
        date.setTime(date.getTime() - 10000);
        document.cookie = objName + '=; expires=' + date.toGMTString() + ';path=' + Q.config.webContext + '/';
    }

    return {
        add: function (objName, objValue, objHours) {
            add(objName, objValue, objHours);
        },
        get: function (objName) {
            return get(objName);
        },
        del: function (objName) {
            del(objName);
        }
    };
};

Q._view = function () {

    function load($container, url, params, $customContainer, customFun) {
        $container.load(url, function () {
            var fun = eval(url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')));
            if (undefined != fun) {
                fun(params, $customContainer);
            }
            undefined != customFun && customFun();
        });
    }

    function renderHelp() {
        $('*[title]:not([position])').poshytip({
            className: 'tip-darkgray',
            bgImageFrameSize: 11,
            offsetX: -25
        }).each(function () {
                $(this).html($(this).html() + '<span style="font-weight: normal;font-style: italic">[?]</span>');
            });
    }

    return {
        load: function ($container, url, params, $customContainer, customFun) {
            load($container, url, params, $customContainer, customFun);
        },
        log: function (msg) {
            'undefined' !== typeof(console) && console.log(msg);
        },
        renderHelp: function () {
            renderHelp();
        }
    };
};

Q._form = function () {

    function fill(data, $form, prefixName) {
        var $ele;
        var eleName;
        var value;
        $form.find('input[name],textarea[name],select[name]').each(function () {
            $ele = $(this);
            eleName = $ele.attr('name');
            value=null;
            if (prefixName && eleName.indexOf(prefixName) == -1) {
                value = null;
            } else {
                if (!prefixName && eleName.indexOf('~') != -1) {
                    value = null;
                } else {
                    try {
                        value = eval('data["' + (prefixName && eleName.indexOf(prefixName) != -1 ? eleName.substring(prefixName.length) : eleName+'"]'));
                    } catch (e) {
                    }
                }
            }
            if (undefined !== value && null !== value) {
                if (undefined !== $ele.attr('type') && 'radio' === $ele.attr('type')) {
                    //对于需要simulate的元素其val要求不能为''，因为没有选中的情况下保存到数据库就是''，组装DOM时value=''会导致找不到元素。
                    '' !== value && $form.find('input[name=' + eleName + '][value=' + value + ']').trigger('click');
                } else if (undefined !== $ele.attr('type') && 'checkbox' === $ele.attr('type')) {
                    if ('string' === $.type(value) && value.indexOf('[') == 0) {
                        //数组处理
                        value = eval(value);
                    }
                    if (!$.isArray(value)) {
                        '' !== value && $form.find('input[name=' + eleName + '][value=' + value + ']').trigger('click');
                    } else {
                        for (var i = 0; i < value; i++) {
                            '' !== value[i] && $form.find('input[name=' + eleName + '][value=' + value[i] + ']').trigger('click');
                        }
                    }
                } else {
                    $ele.val(value);
                    if ('SELECT' == $ele[0].tagName) {
                        $ele.trigger('change');
                    }
                }
            }
        });
    }

    return {
        fill: function (data, $form, prefixName) {
            return fill(data, $form, prefixName);
        }
    };
};

Q._utils = function () {

    function getRequestParam(param, url) {
        if (undefined == url) {
            url = location.href;
        }
        var val = null;
        url.replace(/(\w+)=([^&#]*)/ig, function (a, b, c) {
            if (param == b) {
                val = c;
            }
        });
        return val;
    }

    function delRequestParams(paramKeys, url) {

    }

    function addOrReplaceRequestParam(key, value, url) {
        if (undefined == url) {
            url = location.href;
        }
        url = url.split('?');
        var strParam = url.length == 2 ? url[1] : "";
        url = url[0] + '?';
        var isFind = false;
        strParam.replace(/(\w+)=([^&]+)/ig, function (a, b, c) {
            if (b == key) {
                isFind = true;
                url += b + '=' + value + '&';
            } else {
                url += a + '&';
            }
        });
        if (!isFind) {
            url += key + '=' + value + '&';
        }
        return  url.substring(0, url.length - 1);
    }

    function arrayToString(array) {
        var str = "";
        for (var key in array) {
            str += key + "=" + array[key] + " ";
        }
        return str;
    }

    function sync(waitFun, successfun, delay) {
        var syncFun = setInterval(function () {
            if (!waitFun()) {
                window.clearInterval(syncFun);
                successfun();
            }
        }, delay);
    }

    function getRootPath(){
        var curWwwPath=window.document.location.href;
        var pathName=window.document.location.pathname;
        var pos=curWwwPath.indexOf(pathName);
        var localhostPaht=curWwwPath.substring(0,pos);
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
        return(localhostPaht+projectName);
    }

    return {
        getRequestParam: function (param, url) {
            return getRequestParam(param, url);
        },
        addOrReplaceRequestParam: function (key, value, url) {
            return addOrReplaceRequestParam(key, value, url);
        },
        delRequestParams: function (paramKeys, url) {
            return delRequestParams(paramKeys, url);
        },
        arrayToString: function (array) {
            return arrayToString(array);
        },
        sync: function (waitFun, successfun, delay) {
            sync(waitFun, successfun, delay);
        },
        getRootPath: function () {
            return  getRootPath();
        }
    };
};