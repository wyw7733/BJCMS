/**
 * ESL (Enterprise Standard Loader)
 * Copyright 2013 Baidu Inc. All rights reserved.
 *
 * @file Browser�˱�׼������������AMD�淶
 * @author errorrik(errorrik@gmail.com)
 *         Firede(firede@firede.us)
 */
/* jshint ignore:start */
var define;
var require;
var esl;
/* jshint ignore:end */
(function (global) {
  // "mod"��ͷ�ı�������Ϊ�ڲ�ģ�������
  // Ϊ���ѹ���ʣ���ʹ��function��object��װ
  /**
     * ģ������
     *
     * @inner
     * @type {Object}
     */
  var modModules = {
  };
  /**
     * �Զ������ģ���
     *
     * ģ��define factory���õ�ʱ��ִ�У��������¼��������Ҫ�Զ�����ִ�У�
     * 1. require( [moduleId], callback )
     * 2. plugin module: require( 'plugin!resource' )
     *
     * @inner
     * @type {Object}
     */
  var autoDefineModules = {
  };
  // ģ��״̬ö����
  var MODULE_PRE_DEFINED = 1;
  var MODULE_ANALYZED = 2;
  var MODULE_PREPARED = 3;
  var MODULE_DEFINED = 4;
  /**
     * �ڽ�module���Ƽ���
     *
     * @inner
     * @type {Object}
     */
  var BUILDIN_MODULE = {
    require: globalRequire,
    exports: 1,
    module: 1
  };
  /**
     * ȫ��require����
     *
     * @inner
     * @type {Function}
     */
  var actualGlobalRequire = createLocalRequire();
  // #begin-ignore
  /**
     * ��ʱ���Ѷ�ʱ��
     *
     * @inner
     * @type {number}
     */
  var waitTimeout;
  // #end-ignore
  /* eslint-disable fecs-key-spacing */
  /* eslint-disable key-spacing */
  /**
     * require����
     *
     * @inner
     * @type {Object}
     */
  var requireConf = {
    baseUrl: './',
    paths: {
    },
    config: {
    },
    map: {
    },
    packages: [
    ],
    // #begin-ignore
    waitSeconds: 0,
    // #end-ignore
    noRequests: {
    },
    urlArgs: {
    }
  };
  /* eslint-enable key-spacing */
  /**
     * ����ģ��
     *
     * @param {string|Array} requireId ģ��id��ģ��id���飬
     * @param {Function=} callback ������ɵĻص�����
     * @return {*} requireIdΪstringʱ����ģ�鱩¶����
     */
  function globalRequire(requireId, callback) {
    // #begin-ignore
    // #begin assertNotContainRelativeId
    // ȷ��require��ģ��id���������id������global require����ǰԤ�����Ը��ٵĴ������
    var invalidIds = [
    ];
    /**
         * ���ģ��id�Ƿ�relative id
         *
         * @inner
         * @param {string} id ģ��id
         */
    function monitor(id) {
      if (id.indexOf('.') === 0) {
        invalidIds.push(id);
      }
    }
    if (typeof requireId === 'string') {
      monitor(requireId);
    } 
    else {
      each(requireId, function (id) {
        monitor(id);
      }
      );
    }
    // �������idʱ��ֱ���׳�����

    if (invalidIds.length > 0) {
      throw new Error('[REQUIRE_FATAL]Relative ID is not allowed in global require: '
      + invalidIds.join(', ')
      );
    }
    // #end assertNotContainRelativeId
    // ��ʱ����

    var timeout = requireConf.waitSeconds;
    if (timeout && (requireId instanceof Array)) {
      if (waitTimeout) {
        clearTimeout(waitTimeout);
      }
      waitTimeout = setTimeout(waitTimeoutNotice, timeout * 1000);
    }
    // #end-ignore

    return actualGlobalRequire(requireId, callback);
  }
  /**
     * �汾��
     *
     * @type {string}
     */

  globalRequire.version = '1.8.8';
  /**
     * loader����
     *
     * @type {string}
     */
  globalRequire.loader = 'esl';
  /**
     * ��ģ���ʶת������Ե�url
     *
     * @param {string} id ģ���ʶ
     * @return {string}
     */
  globalRequire.toUrl = actualGlobalRequire.toUrl;
  // #begin-ignore
  /**
     * ��ʱ���Ѻ���
     *
     * @inner
     */
  function waitTimeoutNotice() {
    var hangModules = [
    ];
    var missModules = [
    ];
    var hangModulesMap = {
    };
    var missModulesMap = {
    };
    var visited = {
    };
    /**
         * ���ģ��ļ��ش���
         *
         * @inner
         * @param {string} id ģ��id
         * @param {boolean} hard �Ƿ�װ��ʱ����
         */
    function checkError(id, hard) {
      if (visited[id] || modIs(id, MODULE_DEFINED)) {
        return;
      }
      visited[id] = 1;
      if (!modIs(id, MODULE_PREPARED)) {
        // HACK: Ϊgzip������Ż���������ȡ
        if (!hangModulesMap[id]) {
          hangModulesMap[id] = 1;
          hangModules.push(id);
        }
      }
      var mod = modModules[id];
      if (!mod) {
        if (!missModulesMap[id]) {
          missModulesMap[id] = 1;
          missModules.push(id);
        }
      } 
      else if (hard) {
        if (!hangModulesMap[id]) {
          hangModulesMap[id] = 1;
          hangModules.push(id);
        }
        each(mod.depMs, function (dep) {
          checkError(dep.absId, dep.hard);
        }
        );
      }
    }
    /* eslint-disable guard-for-in */

    for (var id in autoDefineModules) {
      checkError(id, 1);
    }
    /* eslint-enable guard-for-in */

    if (hangModules.length || missModules.length) {
      throw new Error('[MODULE_TIMEOUT]Hang( '
      + (hangModules.join(', ') || 'none')
      + ' ) Miss( '
      + (missModules.join(', ') || 'none')
      + ' )'
      );
    }
  }
  // #end-ignore
  /**
     * δԤ�����ģ�鼯��
     * ��Ҫ�洢������ʽdefine��ģ��
     *
     * @inner
     * @type {Array}
     */

  var wait4PreDefine = [
  ];
  /**
     * ���ģ��Ԥ���壬��ʱ�����ģ��������define��ģ��
     *
     * @inner
     * @param {string} currentId ����define��ģ���id
     */
  function completePreDefine(currentId) {
    // HACK: ������IE���и��������壬����ʹ���κα�����
    //       ����ò�ƻ��γɱ������ú��޸ĵĶ�д��������wait4PreDefine�ͷ�����
    each(wait4PreDefine, function (mod) {
      modPreDefine(currentId, mod.deps, mod.factory
      );
    });
    wait4PreDefine.length = 0;
    modAnalyse(currentId);
  }
  /**
     * ����ģ��
     *
     * @param {string=} id ģ���ʶ
     * @param {Array=} dependencies ����ģ���б�
     * @param {Function=} factory ����ģ��Ĺ�������
     */

  function globalDefine(id, dependencies, factory) {
    // define(factory)
    // define(dependencies, factory)
    // define(id, factory)
    // define(id, dependencies, factory)
    if (factory == null) {
      if (dependencies == null) {
        factory = id;
        id = null;
      } 
      else {
        factory = dependencies;
        dependencies = null;
        if (id instanceof Array) {
          dependencies = id;
          id = null;
        }
      }
    }
    if (factory == null) {
      return;
    }
    var opera = window.opera;
    // IE��ͨ��current script��data-require-id��ȡ��ǰid
    if (!id
    && document.attachEvent
    && (!(opera && opera.toString() === '[object Opera]'))
    ) {
      var currentScript = getCurrentScript();
      id = currentScript && currentScript.getAttribute('data-require-id');
    }
    if (id) {
      modPreDefine(id, dependencies, factory);
    } 
    else {
      // ��¼����������У���load��readystatechange�д���
      // ��׼������£�ʹ������defineʱ�������������֧
      wait4PreDefine[0] = {
        deps: dependencies,
        factory: factory
      };
    }
  }
  globalDefine.amd = {
  };
  /**
     * ģ�����û�ȡ����
     *
     * @inner
     * @return {Object} ģ�����ö���
     */
  function moduleConfigGetter() {
    var conf = requireConf.config[this.id];
    if (conf && typeof conf === 'object') {
      return conf;
    }
    return {
    };
  }
  /**
     * Ԥ����ģ��
     *
     * @inner
     * @param {string} id ģ���ʶ
     * @param {Array.<string>} dependencies ��ʽ����������ģ���б�
     * @param {*} factory ģ�鶨�庯����ģ�����
     */

  function modPreDefine(id, dependencies, factory) {
    // ��ģ���������
    //
    // ģ���ڲ���Ϣ����
    // -----------------------------------
    // id: module id
    // depsDec: ģ�鶨��ʱ����������
    // deps: ģ��������Ĭ��Ϊ['require', 'exports', 'module']
    // factory: ��ʼ�����������
    // factoryDeps: ��ʼ�������Ĳ�������
    // exports: ģ���ʵ�ʱ�¶����AMD���壩
    // config: ���ڻ�ȡģ��������Ϣ�ĺ�����AMD���壩
    // state: ģ�鵱ǰ״̬
    // require: local require����
    // depMs: ʵ��������ģ�鼯�ϣ�������ʽ
    // depMkv: ʵ��������ģ�鼯�ϣ�����ʽ�����ڲ���
    // depRs: ʵ����������Դ����
    // depPMs: ���ڼ�����Դ��ģ�鼯�ϣ�key��ģ������value��1�������ڿ�ݲ���
    // ------------------------------------
    if (!modModules[id]) {
      /* eslint-disable key-spacing */
      modModules[id] = {
        id: id,
        depsDec: dependencies,
        deps: dependencies || ['require',
        'exports',
        'module'],
        factoryDeps: [
        ],
        factory: factory,
        exports: {
        },
        config: moduleConfigGetter,
        state: MODULE_PRE_DEFINED,
        require: createLocalRequire(id),
        depMs: [
        ],
        depMkv: {
        },
        depRs: [
        ],
        depPMs: [
        ]
      };
      /* eslint-enable key-spacing */
    }
  }
  /**
     * Ԥ����ģ��
     *
     * ���ȣ���ɶ�factory�����������ķ�����ȡ
     * Ȼ�󣬳��Լ���"��Դ��������ģ��"
     *
     * ��Ҫ�ȼ���ģ���ԭ���ǣ����ģ�鲻���ڣ��޷�����resourceId normalize��
     * modAnalyse��ɺ���������������������������ģ��ļ���
     *
     * @inner
     * @param {string} id ģ��id
     */

  function modAnalyse(id) {
    var mod = modModules[id];
    if (!mod || modIs(id, MODULE_ANALYZED)) {
      return;
    }
    var deps = mod.deps;
    var factory = mod.factory;
    var hardDependsCount = 0;
    // ����function body�е�require
    // ���������ʽ��������������AMD�涨�����ܿ��ǣ����Բ�����factoryBody
    if (typeof factory === 'function') {
      hardDependsCount = Math.min(factory.length, deps.length);
      // If the dependencies argument is present, the module loader
      // SHOULD NOT scan for dependencies within the factory function.
      !mod.depsDec && factory.toString().replace(/(\/\*([\s\S]*?)\*\/|([^:]|^)\/\/(.*)$)/gm, '').replace(/require\(\s*(['"'])([^'"]+)\1\s*\)/g, function ($0, $1, depId) {
        deps.push(depId);
      }
      );
    }
    var requireModules = [
    ];
    each(deps, function (depId, index) {
      var idInfo = parseId(depId);
      var absId = normalize(idInfo.mod, id);
      var moduleInfo;
      var resInfo;
      if (absId && !BUILDIN_MODULE[absId]) {
        // ���������һ����Դ��������Ϣ��ӵ�module.depRs
        //
        // module.depRs�е����п������ظ��ġ�
        // ������׶Σ�����resource��module���ܻ�δdefined��
        // ���´�ʱresource id�޷���normalize��
        //
        // �����a/b/c���ԣ����漸��resource����ָ����ͬһ����Դ��
        // - js!../name.js
        // - js!a/name.js
        // - ../../js!../name.js
        //
        // ���Լ�����Դ��module readyʱ����Ҫ����module.depRs���д���
        if (idInfo.res) {
          resInfo = {
            id: depId,
            mod: absId,
            res: idInfo.res
          };
          autoDefineModules[absId] = 1;
          mod.depPMs.push(absId);
          mod.depRs.push(resInfo);
        }
        // ������ģ���id normalize�ܱ�֤��ȷ�ԣ��ڴ˴�����ȥ��

        moduleInfo = mod.depMkv[absId];
        if (!moduleInfo) {
          moduleInfo = {
            id: idInfo.mod,
            absId: absId,
            hard: index < hardDependsCount
          };
          mod.depMs.push(moduleInfo);
          mod.depMkv[absId] = moduleInfo;
          requireModules.push(absId);
        }
      } 
      else {
        moduleInfo = {
          absId: absId
        };
      }
      // �����ǰ���ڷ�������������define�������ģ�
      // ���¼��module.factoryDeps��
      // ��factory invokeǰ����������invoke arguments

      if (index < hardDependsCount) {
        mod.factoryDeps.push(resInfo || moduleInfo);
      }
    });
    mod.state = MODULE_ANALYZED;
    modInitFactoryInvoker(id);
    nativeRequire(requireModules);
  }
  /**
     * ��һЩ��Ҫ�Զ������ģ������Զ�����
     *
     * @inner
     */

  function modAutoInvoke() {
    /* eslint-disable guard-for-in */
    for (var id in autoDefineModules) {
      modUpdatePreparedState(id);
      modTryInvokeFactory(id);
    }
    /* eslint-enable guard-for-in */

  }
  /**
     * ����ģ���׼��״̬
     *
     * @inner
     * @param {string} id ģ��id
     */

  function modUpdatePreparedState(id) {
    var visited = {
    };
    update(id);
    function update(id) {
      if (!modIs(id, MODULE_ANALYZED)) {
        return false;
      }
      if (modIs(id, MODULE_PREPARED) || visited[id]) {
        return true;
      }
      visited[id] = 1;
      var mod = modModules[id];
      var prepared = true;
      each(mod.depMs, function (dep) {
        return (prepared = update(dep.absId));
      }
      );
      // �ж�resource�Ƿ������ɡ����resourceδ������ɣ�����Ϊδ׼����
      /* jshint ignore:start */
      prepared && each(mod.depRs, function (dep) {
        prepared = !!(dep.absId && modIs(dep.absId, MODULE_DEFINED));
        return prepared;
      }
      );
      /* jshint ignore:end */
      if (prepared) {
        mod.state = MODULE_PREPARED;
      }
      return prepared;
    }
  }
  /**
     * ��ʼ��ģ�鶨��ʱ�����factoryִ����
     *
     * @inner
     * @param {string} id ģ��id
     */

  function modInitFactoryInvoker(id) {
    var mod = modModules[id];
    var invoking;
    mod.invokeFactory = invokeFactory;
    /* eslint-disable max-nested-callbacks */
    each(mod.depPMs, function (pluginModuleId) {
      modAddDefinedListener(pluginModuleId, function () {
        each(mod.depRs, function (res) {
          if (!res.absId && res.mod === pluginModuleId) {
            res.absId = normalize(res.id, id);
            nativeRequire([res.absId], modAutoInvoke);
          }
        });
      }
      );
    }
    );
    /* eslint-enable max-nested-callbacks */
    /**
         * ��ʼ��ģ��
         *
         * @inner
         */
    function invokeFactory() {
      if (invoking || mod.state !== MODULE_PREPARED) {
        return;
      }
      invoking = 1;
      // ƴ��factory invoke�����arguments
      var factoryReady = 1;
      var factoryDeps = [
      ];
      each(mod.factoryDeps, function (dep) {
        var depId = dep.absId;
        if (!BUILDIN_MODULE[depId]) {
          modTryInvokeFactory(depId);
          if (!modIs(depId, MODULE_DEFINED)) {
            factoryReady = 0;
            return false;
          }
        }
        factoryDeps.push(depId);
      }
      );
      if (factoryReady) {
        try {
          var args = modGetModulesExports(factoryDeps, {
            require: mod.require,
            exports: mod.exports,
            module: mod
          }
          );
          // ����factory������ʼ��module
          var factory = mod.factory;
          var exports = typeof factory === 'function'
          ? factory.apply(global, args) 
          : factory;
          if (exports != null) {
            mod.exports = exports;
          }
          mod.invokeFactory = null;
          delete autoDefineModules[id];
        } 
        catch (ex) {
          invoking = 0;
          if (/^\[MODULE_MISS\]"([^"]+)/.test(ex.message)) {
            // ������˵����factory�������У���require��ģ������Ҫ��
            // ���԰�������ǿ������
            var hardCirclurDep = mod.depMkv[RegExp.$1];
            hardCirclurDep && (hardCirclurDep.hard = 1);
            return;
          }
          throw ex;
        }
        // ���define
        // ������try�������������д��������̵�

        modDefined(id);
      }
    }
  }
  /**
     * �ж�ģ���Ƿ������Ӧ��״̬
     *
     * @inner
     * @param {string} id ģ���ʶ
     * @param {number} state ״̬�룬ʹ��ʱ������Ӧ��ö�ٱ���������`MODULE_DEFINED`
     * @return {boolean} �Ƿ������Ӧ��״̬
     */

  function modIs(id, state) {
    return modModules[id] && modModules[id].state >= state;
  }
  /**
     * ����ִ��ģ��factory����������ģ���ʼ��
     *
     * @inner
     * @param {string} id ģ��id
     */

  function modTryInvokeFactory(id) {
    var mod = modModules[id];
    if (mod && mod.invokeFactory) {
      mod.invokeFactory();
    }
  }
  /**
     * ����ģ��id���飬��ȡ���exports����
     * ����ģ���ʼ����factory������require��callback��������
     *
     * @inner
     * @param {Array} modules ģ��id����
     * @param {Object} buildinModules �ڽ�ģ�����
     * @return {Array} ģ��exports����
     */

  function modGetModulesExports(modules, buildinModules) {
    var args = [
    ];
    each(modules, function (id, index) {
      args[index] = buildinModules[id] || modGetModuleExports(id);
    }
    );
    return args;
  }
  /**
     * ģ�鶨������¼�����������
     *
     * @inner
     * @type {Object}
     */

  var modDefinedListeners = {
  };
  /**
     * ���ģ�鶨�����ʱ��ļ�����
     *
     * @inner
     * @param {string} id ģ���ʶ
     * @param {Function} listener ��������
     */
  function modAddDefinedListener(id, listener) {
    if (modIs(id, MODULE_DEFINED)) {
      listener();
      return;
    }
    var listeners = modDefinedListeners[id];
    if (!listeners) {
      listeners = modDefinedListeners[id] = [
      ];
    }
    listeners.push(listener);
  }
  /**
     * ģ��״̬�л�Ϊ�������
     * ��Ϊ��Ҫ�����¼���MODULE_DEFINED״̬�л�ͨ���ú���
     *
     * @inner
     * @param {string} id ģ���ʶ
     */

  function modDefined(id) {
    var listeners = modDefinedListeners[id] || [];
    var mod = modModules[id];
    mod.state = MODULE_DEFINED;
    var len = listeners.length;
    while (len--) {
      // ���ﲻ��function���͵ļ��
      // ��Ϊlistener����ͨ��modOn����ģ�modOnΪ�ڲ�����
      listeners[len]();
    }
    // ����listeners

    listeners.length = 0;
    delete modDefinedListeners[id];
  }
  /**
     * ��ȡģ���exports
     *
     * @inner
     * @param {string} id ģ���ʶ
     * @return {*} ģ���exports
     */

  function modGetModuleExports(id) {
    if (modIs(id, MODULE_DEFINED)) {
      return modModules[id].exports;
    }
    return null;
  }
  /**
     * ��ȡģ��
     *
     * @param {string|Array} ids ģ�����ƻ�ģ�������б�
     * @param {Function=} callback ��ȡģ�����ʱ�Ļص�����
     * @param {string} baseId ����id�����ڵ�ids��relative idʱ��normalize
     * @param {Object} noRequests ���跢�������ģ�鼯��
     * @return {Object} ģ�����
     */

  function nativeRequire(ids, callback, baseId, noRequests) {
    // ���� https://github.com/amdjs/amdjs-api/wiki/require
    // It MUST throw an error if the module has not
    // already been loaded and evaluated.
    if (typeof ids === 'string') {
      modTryInvokeFactory(ids);
      if (!modIs(ids, MODULE_DEFINED)) {
        throw new Error('[MODULE_MISS]"' + ids + '" is not exists!');
      }
      return modGetModuleExports(ids);
    }
    noRequests = noRequests || {
    };
    var isCallbackCalled = 0;
    if (ids instanceof Array) {
      tryFinishRequire();
      if (!isCallbackCalled) {
        each(ids, function (id) {
          if (!(BUILDIN_MODULE[id] || modIs(id, MODULE_DEFINED))) {
            modAddDefinedListener(id, tryFinishRequire);
            if (!noRequests[id]) {
              (id.indexOf('!') > 0
              ? loadResource 
              : loadModule
              ) (id, baseId);
            }
            modAnalyse(id);
          }
        });
        modAutoInvoke();
      }
    }
    /**
         * �������require������callback
         * ��ģ����������ģ�鶼������ʱ����
         *
         * @inner
         */

    function tryFinishRequire() {
      if (!isCallbackCalled) {
        var isAllCompleted = 1;
        each(ids, function (id) {
          if (!BUILDIN_MODULE[id]) {
            return (isAllCompleted = !!modIs(id, MODULE_DEFINED));
          }
        });
        // ��Ⲣ����callback
        if (isAllCompleted) {
          isCallbackCalled = 1;
          (typeof callback === 'function') && callback.apply(global, modGetModulesExports(ids, BUILDIN_MODULE)
          );
        }
      }
    }
  }
  /**
     * ���ڼ��ص�ģ���б�
     *
     * @inner
     * @type {Object}
     */

  var loadingModules = {
  };
  /**
     * ����ģ��
     *
     * @inner
     * @param {string} moduleId ģ���ʶ
     */
  function loadModule(moduleId) {
    if (loadingModules[moduleId] || modModules[moduleId]) {
      return;
    }
    loadingModules[moduleId] = 1;
    // ����script��ǩ
    //
    // ���ﲻ�ҽ�onerror�Ĵ�����
    // ��Ϊ�߼��������devtool��console���ᱨ��
    // ��throwһ��Error���һ����
    var script = document.createElement('script');
    script.setAttribute('data-require-id', moduleId);
    script.src = toUrl(moduleId + '.js');
    script.async = true;
    if (script.readyState) {
      script.onreadystatechange = loadedListener;
    } 
    else {
      script.onload = loadedListener;
    }
    appendScript(script);
    /**
         * script��ǩ������ɵ��¼�������
         *
         * @inner
         */
    function loadedListener() {
      var readyState = script.readyState;
      if (typeof readyState === 'undefined'
      || /^(loaded|complete)$/.test(readyState)
      ) {
        script.onload = script.onreadystatechange = null;
        script = null;
        completePreDefine(moduleId);
        /* eslint-disable guard-for-in */
        for (var key in autoDefineModules) {
          modAnalyse(key);
        }
        /* eslint-enable guard-for-in */

        modAutoInvoke();
      }
    }
  }
  /**
     * ������Դ
     *
     * @inner
     * @param {string} pluginAndResource �������Դ��ʶ
     * @param {string} baseId ��ǰ������ģ���ʶ
     */

  function loadResource(pluginAndResource, baseId) {
    if (modModules[pluginAndResource]) {
      return;
    }
    var idInfo = parseId(pluginAndResource);
    var resource = {
      id: pluginAndResource,
      state: MODULE_ANALYZED
    };
    modModules[pluginAndResource] = resource;
    /**
         * plugin������ɵĻص�����
         *
         * @inner
         * @param {*} value resource��ֵ
         */
    function pluginOnload(value) {
      resource.exports = value || true;
      modDefined(pluginAndResource);
    }
    /* jshint ignore:start */
    /**
         * �÷�������pluginʹ�ü��ص���Դ����ģ��
         *
         * @param {string} id ģ��id
         * @param {string} text ģ�������ַ���
         */

    pluginOnload.fromText = function (id, text) {
      autoDefineModules[id] = 1;
      new Function(text) ();
      completePreDefine(id);
    };
    /* jshint ignore:end */
    /**
         * ������Դ
         *
         * @inner
         * @param {Object} plugin ���ڼ�����Դ�Ĳ��ģ��
         */
    function load(plugin) {
      var pluginRequire = baseId
      ? modModules[baseId].require 
      : actualGlobalRequire;
      plugin.load(idInfo.res, pluginRequire, pluginOnload, moduleConfigGetter.call({
        id: pluginAndResource
      })
      );
    }
    load(modGetModuleExports(idInfo.mod));
  }
  /**
     * ����require
     *
     * @param {Object} conf ���ö���
     */

  globalRequire.config = function (conf) {
    if (conf) {
      /* eslint-disable guard-for-in */
      for (var key in requireConf) {
        var newValue = conf[key];
        var oldValue = requireConf[key];
        if (!newValue) {
          continue;
        }
        if (key === 'urlArgs' && typeof newValue === 'string') {
          requireConf.urlArgs['*'] = newValue;
        } 
        else {
          // �򵥵Ķദ���û�����Ҫ֧�֣���������ʵ��Ϊ֧�ֶ���mix
          if (oldValue instanceof Array) {
            oldValue.push.apply(oldValue, newValue);
          } 
          else if (typeof oldValue === 'object') {
            for (var k in newValue) {
              oldValue[k] = newValue[k];
            }
          } 
          else {
            requireConf[key] = newValue;
          }
        }
      }
      /* eslint-enable guard-for-in */

      createConfIndex();
    }
    // ������Ϣ����clone���أ����ⷵ�ؽ�������û������޸Ŀ��ܵ��µ�����
    // return clone(requireConf);

  };
  /**
     * �����¡��֧��raw type, Array, raw Object
     *
     * @inner
     * @param {*} source Ҫ��¡�Ķ���
     * @return {*}
     */
  // function clone(source) {
  //     var result = source;
  //     if (source instanceof Array) {
  //         result = [];
  //         each(source, function (item, i) {
  //             result[i] = clone(item);
  //         });
  //     }
  //     else if (typeof source === 'object') {
  //         result = {};
  //         for (var key in source) {
  //             if (source.hasOwnProperty(key)) {
  //                 result[key] = clone(source[key]);
  //             }
  //         }
  //     }
  //     return result;
  // }
  // ��ʼ��ʱ��Ҫ������������
  createConfIndex();
  /**
     * paths�ڲ�����
     *
     * @inner
     * @type {Array}
     */
  var pathsIndex;
  /**
     * packages�ڲ�����
     *
     * @inner
     * @type {Array}
     */
  var packagesIndex;
  /**
     * mapping�ڲ�����
     *
     * @inner
     * @type {Array}
     */
  var mappingIdIndex;
  /**
     * urlArgs�ڲ�����
     *
     * @inner
     * @type {Array}
     */
  var urlArgsIndex;
  /**
     * noRequests�ڲ�����
     *
     * @inner
     * @type {Array}
     */
  var noRequestsIndex;
  /**
     * ��keyΪmodule id prefix��Object������������ʽ�������������ճ��Ⱥ���������
     *
     * @inner
     * @param {Object} value Դֵ
     * @param {boolean} allowAsterisk �Ƿ�����*�ű�ʾƥ������
     * @return {Array} ��������
     */
  function createKVSortedIndex(value, allowAsterisk) {
    var index = kv2List(value, 1, allowAsterisk);
    index.sort(descSorterByKOrName);
    return index;
  }
  /**
     * ����������Ϣ�ڲ�����
     *
     * @inner
     */

  function createConfIndex() {
    requireConf.baseUrl = requireConf.baseUrl.replace(/\/$/, '') + '/';
    // create paths index
    pathsIndex = createKVSortedIndex(requireConf.paths);
    // create mappingId index
    mappingIdIndex = createKVSortedIndex(requireConf.map, 1);
    each(mappingIdIndex, function (item) {
      item.v = createKVSortedIndex(item.v);
    }
    );
    // create packages index
    packagesIndex = [
    ];
    each(requireConf.packages, function (packageConf) {
      var pkg = packageConf;
      if (typeof packageConf === 'string') {
        pkg = {
          name: packageConf.split('/') [0],
          location: packageConf,
          main: 'main'
        };
      }
      pkg.location = pkg.location || pkg.name;
      pkg.main = (pkg.main || 'main').replace(/\.js$/i, '');
      pkg.reg = createPrefixRegexp(pkg.name);
      packagesIndex.push(pkg);
    }
    );
    packagesIndex.sort(descSorterByKOrName);
    // create urlArgs index
    urlArgsIndex = createKVSortedIndex(requireConf.urlArgs, 1);
    // create noRequests index
    noRequestsIndex = createKVSortedIndex(requireConf.noRequests);
    each(noRequestsIndex, function (item) {
      var value = item.v;
      var mapIndex = {
      };
      item.v = mapIndex;
      if (!(value instanceof Array)) {
        value = [
          value
        ];
      }
      each(value, function (meetId) {
        mapIndex[meetId] = 1;
      });
    });
  }
  /**
     * ��������Ϣ���������м���
     *
     * @inner
     * @param {string} value Ҫ������ֵ
     * @param {Array} index ��������
     * @param {Function} hitBehavior �������е���Ϊ����
     */

  function indexRetrieve(value, index, hitBehavior) {
    each(index, function (item) {
      if (item.reg.test(value)) {
        hitBehavior(item.v, item.k, item);
        return false;
      }
    });
  }
  /**
     * ��`ģ���ʶ+'.extension'`��ʽ���ַ���ת������Ե�url
     *
     * @inner
     * @param {string} source Դ�ַ���
     * @return {string} url
     */

  function toUrl(source) {
    // ���� ģ���ʶ �� .extension
    var extReg = /(\.[a-z0-9]+)$/i;
    var queryReg = /(\?[^#]*)$/;
    var extname = '';
    var id = source;
    var query = '';
    if (queryReg.test(source)) {
      query = RegExp.$1;
      source = source.replace(queryReg, '');
    }
    if (extReg.test(source)) {
      extname = RegExp.$1;
      id = source.replace(extReg, '');
    }
    var url = id;
    // paths�����ƥ��
    var isPathMap;
    indexRetrieve(id, pathsIndex, function (value, key) {
      url = url.replace(key, value);
      isPathMap = 1;
    });
    // packages�����ƥ��
    if (!isPathMap) {
      indexRetrieve(id, packagesIndex, function (value, key, item) {
        url = url.replace(item.name, item.location);
      });
    }
    // ���·��ʱ������baseUrl

    if (!/^([a-z]{2,10}:\/)?\//i.test(url)) {
      url = requireConf.baseUrl + url;
    }
    // ���� .extension �� query

    url += extname + query;
    // urlArgs�����ƥ��
    indexRetrieve(id, urlArgsIndex, function (value) {
      url += (url.indexOf('?') > 0 ? '&' : '?') + value;
    });
    return url;
  }
  /**
     * ����local require����
     *
     * @inner
     * @param {number} baseId ��ǰmodule id
     * @return {Function} local require����
     */

  function createLocalRequire(baseId) {
    var requiredCache = {
    };
    function req(requireId, callback) {
      if (typeof requireId === 'string') {
        if (!requiredCache[requireId]) {
          requiredCache[requireId] =
          nativeRequire(normalize(requireId, baseId));
        }
        return requiredCache[requireId];
      } 
      else if (requireId instanceof Array) {
        // �����Ƿ���resource��ȡ��pluginModule��
        var pluginModules = [
        ];
        var pureModules = [
        ];
        var normalizedIds = [
        ];
        each(requireId, function (id, i) {
          var idInfo = parseId(id);
          var absId = normalize(idInfo.mod, baseId);
          pureModules.push(absId);
          autoDefineModules[absId] = 1;
          if (idInfo.res) {
            pluginModules.push(absId);
            normalizedIds[i] = null;
          } 
          else {
            normalizedIds[i] = absId;
          }
        }
        );
        var noRequestModules = {
        };
        each(pureModules, function (id) {
          var meet;
          indexRetrieve(id, noRequestsIndex, function (value) {
            meet = value;
          }
          );
          if (meet) {
            if (meet['*']) {
              noRequestModules[id] = 1;
            } 
            else {
              each(pureModules, function (meetId) {
                if (meet[meetId]) {
                  noRequestModules[id] = 1;
                  return false;
                }
              });
            }
          }
        }
        );
        // ����ģ��
        nativeRequire(pureModules, function () {
          /* jshint ignore:start */
          each(normalizedIds, function (id, i) {
            if (id == null) {
              normalizedIds[i] = normalize(requireId[i], baseId);
            }
          });
          /* jshint ignore:end */
          nativeRequire(normalizedIds, callback, baseId);
        }, baseId, noRequestModules
        );
      }
    }
    /**
         * ��[module ID] + '.extension'��ʽ���ַ���ת����url
         *
         * @inner
         * @param {string} id ����������ʽ��Դ�ַ���
         * @return {string} url
         */

    req.toUrl = function (id) {
      return toUrl(normalize(id, baseId));
    };
    return req;
  }
  /**
     * id normalize��
     *
     * @inner
     * @param {string} id ��Ҫnormalize��ģ���ʶ
     * @param {string} baseId ��ǰ������ģ���ʶ
     * @return {string} normalize���
     */

  function normalize(id, baseId) {
    if (!id) {
      return '';
    }
    baseId = baseId || '';
    var idInfo = parseId(id);
    if (!idInfo) {
      return id;
    }
    var resourceId = idInfo.res;
    var moduleId = relative2absolute(idInfo.mod, baseId);
    each(packagesIndex, function (packageConf) {
      var name = packageConf.name;
      if (name === moduleId) {
        moduleId = name + '/' + packageConf.main;
        return false;
      }
    }
    );
    // ����config�е�map���ý���module id mapping
    indexRetrieve(baseId, mappingIdIndex, function (value) {
      indexRetrieve(moduleId, value, function (mdValue, mdKey) {
        moduleId = moduleId.replace(mdKey, mdValue);
      }
      );
    }
    );
    if (resourceId) {
      var mod = modGetModuleExports(moduleId);
      resourceId = mod.normalize
      ? mod.normalize(resourceId, function (resId) {
        return normalize(resId, baseId);
      }
      ) 
      : normalize(resourceId, baseId);
      moduleId += '!' + resourceId;
    }
    return moduleId;
  }
  /**
     * ���idת���ɾ���id
     *
     * @inner
     * @param {string} id Ҫת�������id
     * @param {string} baseId ��ǰ���ڻ���id
     * @return {string} ����id
     */

  function relative2absolute(id, baseId) {
    if (id.indexOf('.') === 0) {
      var basePath = baseId.split('/');
      var namePath = id.split('/');
      var baseLen = basePath.length - 1;
      var nameLen = namePath.length;
      var cutBaseTerms = 0;
      var cutNameTerms = 0;
      /* eslint-disable block-scoped-var */
      pathLoop: for (var i = 0; i < nameLen; i++) {
        switch (namePath[i]) {
          case '..':
            if (cutBaseTerms < baseLen) {
              cutBaseTerms++;
              cutNameTerms++;
            } 
            else {
              break pathLoop;
            }
            break;
          case '.':
            cutNameTerms++;
            break;
          default:
            break pathLoop;
        }
      }
      /* eslint-enable block-scoped-var */

      basePath.length = baseLen - cutBaseTerms;
      namePath = namePath.slice(cutNameTerms);
      return basePath.concat(namePath).join('/');
    }
    return id;
  }
  /**
     * ����id�����ش���module��resource���Ե�Object
     *
     * @inner
     * @param {string} id ��ʶ
     * @return {Object} id�����������
     */

  function parseId(id) {
    var segs = id.split('!');
    if (segs[0]) {
      return {
        mod: segs[0],
        res: segs[1]
      };
    }
    return null;
  }
  /**
     * ����������ת�������飬����ÿ���Ǵ���k��v��Object
     *
     * @inner
     * @param {Object} source ��������
     * @param {boolean} keyMatchable key�Ƿ�����ǰ׺ƥ��
     * @param {boolean} allowAsterisk �Ƿ�֧��*ƥ������
     * @return {Array.<Object>} ����ת������
     */

  function kv2List(source, keyMatchable, allowAsterisk) {
    var list = [
    ];
    for (var key in source) {
      if (source.hasOwnProperty(key)) {
        var item = {
          k: key,
          v: source[key]
        };
        list.push(item);
        if (keyMatchable) {
          item.reg = key === '*' && allowAsterisk
          ? /^/ 
          : createPrefixRegexp(key);
        }
      }
    }
    return list;
  }
  // ��лrequirejs��ͨ��currentlyAddingScript�����Ͼ�ie
  //
  // For some cache cases in IE 6-8, the script executes before the end
  // of the appendChild execution, so to tie an anonymous define
  // call to the module name (which is stored on the node), hold on
  // to a reference to this node, but clear after the DOM insertion.

  var currentlyAddingScript;
  var interactiveScript;
  /**
     * ��ȡ��ǰscript��ǩ
     * ����ie��defineδָ��module idʱ��ȡid
     *
     * @inner
     * @return {HTMLScriptElement} ��ǰscript��ǩ
     */
  function getCurrentScript() {
    if (currentlyAddingScript) {
      return currentlyAddingScript;
    } 
    else if (interactiveScript
    && interactiveScript.readyState === 'interactive'
    ) {
      return interactiveScript;
    }
    var scripts = document.getElementsByTagName('script');
    var scriptLen = scripts.length;
    while (scriptLen--) {
      var script = scripts[scriptLen];
      if (script.readyState === 'interactive') {
        interactiveScript = script;
        return script;
      }
    }
  }
  var headElement = document.getElementsByTagName('head') [0];
  var baseElement = document.getElementsByTagName('base') [0];
  if (baseElement) {
    headElement = baseElement.parentNode;
  }
  /**
     * ��ҳ���в���script��ǩ
     *
     * @inner
     * @param {HTMLScriptElement} script script��ǩ
     */

  function appendScript(script) {
    currentlyAddingScript = script;
    // If BASE tag is in play, using appendChild is a problem for IE6.
    // See: http://dev.jquery.com/ticket/2709
    baseElement
    ? headElement.insertBefore(script, baseElement) 
    : headElement.appendChild(script);
    currentlyAddingScript = null;
  }
  /**
     * ����idǰ׺ƥ����������
     *
     * @inner
     * @param {string} prefix idǰ׺
     * @return {RegExp} ǰ׺ƥ����������
     */

  function createPrefixRegexp(prefix) {
    return new RegExp('^' + prefix + '(/|$)');
  }
  /**
     * ѭ���������鼯��
     *
     * @inner
     * @param {Array} source ����Դ
     * @param {function(Array,Number):boolean} iterator ��������
     */

  function each(source, iterator) {
    if (source instanceof Array) {
      for (var i = 0, len = source.length; i < len; i++) {
        if (iterator(source[i], i) === false) {
          break;
        }
      }
    }
  }
  /**
     * ����Ԫ�ص�k��name����������ַ��������������
     *
     * @inner
     * @param {Object} a Ҫ�ȽϵĶ���a
     * @param {Object} b Ҫ�ȽϵĶ���b
     * @return {number} �ȽϽ��
     */

  function descSorterByKOrName(a, b) {
    var aValue = a.k || a.name;
    var bValue = b.k || b.name;
    if (bValue === '*') {
      return - 1;
    }
    if (aValue === '*') {
      return 1;
    }
    return bValue.length - aValue.length;
  }
  // ��¶ȫ�ֶ���

  if (!define) {
    define = globalDefine;
    // ��������������ʽ��loader�����ԣ���Ҫ�����˼�
    if (!require) {
      require = globalRequire;
    }
    // ������������汾��esl����define������жϹ��ˣ�������������֧
    // ��������Ͳ��ж��ˣ�ֱ��д

    esl = globalRequire;
  }
}) (this);
