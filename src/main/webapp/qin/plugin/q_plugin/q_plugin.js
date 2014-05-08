var QPlugins = {
    syncDeferred: $.Deferred(),
    startLoadTime: null,
    syncCounter: 0
};

QPlugins.config = {
    basePath: '',
    plugins: [],
    timeout: 5000,
    syncPlugins: [],
    successFun: null
};

QPlugins.AOP = {};

QPlugins.load = function (config) {
    if (null != config) {
        for (var customKey in config) {
            for (var key in QPlugins.config) {
                if (customKey === key) {
                    QPlugins.config[key] = config[customKey];
                }
            }
        }
    }
    $('body').append('<div id="q_plugin_files" style="display: none"></div>');
    if (QPlugins.config.plugins && 0 < QPlugins.config.plugins.length) {
        QPlugins.init(QPlugins.config.plugins, false);
    }
    if (QPlugins.config.syncPlugins && 0 < QPlugins.config.syncPlugins.length) {
        QPlugins.syncCounter = QPlugins.config.syncPlugins.length;
        QPlugins.init(QPlugins.config.syncPlugins, true);
    }
    QPlugins.startLoadTime = new Date().getTime();
    QPlugins.timeoutMonitor();
    $.when(QPlugins.syncDeferred).done(function () {
        QPlugins.config.successFun && QPlugins.config.successFun();
    }).fail(function () {
            Q.view.log(QPlugins.syncCounter + ' SyncPlugin(s) loaded fail.');
        });
};

QPlugins.init = function (plugins, sync) {
    var pluginName;
    for (var i in plugins) {
        pluginName = plugins[i];
        Q.view.log('Load plugin: ' + pluginName);
        $('#q_plugin_files').load(QPlugins.config.basePath + '/' + pluginName + '/' + pluginName + '.css');
        (function (pluginName) {
            $.getScript(QPlugins.config.basePath + '/' + pluginName + '/' + pluginName + '.js', function () {
                Q.view.log('Load plugin success:' + pluginName);
                try {
                    var exports = eval(pluginName).exports;
                    if (exports) {
                        $.each(exports, function (k, v) {
                            if (undefined === QPlugins.AOP[k]) {
                                QPlugins.AOP[k] = [];
                            }
                            QPlugins.AOP[k].push(v);
                        });
                        exports.onReady && exports.onReady();
                    }
                } catch (e) {
                    Q.view.log('Load plugin fail:' + pluginName);
                }
                if (sync) {
                    QPlugins.finishLoadSyncPlugin();
                }
            });
        })(pluginName);
    }
};

QPlugins.execute = function (aopName, param) {
    if (undefined !== QPlugins.AOP[aopName] && 0 < QPlugins.AOP[aopName].length) {
        for (var i in QPlugins.AOP[aopName]) {
            if (QPlugins.AOP[aopName][i]) {
                try {
                    QPlugins.AOP[aopName][i](param);
                } catch (e) {
                    Q.view.log('AOP:' + aopName + ' execute fail.');
                }
            }
        }
    }
};

QPlugins.finishLoadSyncPlugin = function () {
    QPlugins.syncCounter--;
    if (0 == QPlugins.syncCounter) {
        QPlugins.syncDeferred.resolve();
    }
};

QPlugins.timeoutMonitor = function () {
    var syncFun = setInterval(function () {
        if (0 == QPlugins.syncCounter) {
            window.clearInterval(syncFun);
        } else if (QPlugins.config.timeout < new Date().getTime() - QPlugins.startLoadTime) {
            window.clearInterval(syncFun);
            QPlugins.syncDeferred.reject();
        }
    }, 1000);
};
