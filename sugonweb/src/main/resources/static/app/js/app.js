/*!
 *
 * Angle - Bootstrap Admin App + AngularJS
 *
 * Author: @themicon_co
 * Website: http://themicon.co
 * License: http://support.wrapbootstrap.com/knowledge_base/topics/usage-licenses
 *
 */

if (typeof $ === 'undefined') {
    throw new Error('This application\'s JavaScript requires jQuery');
}

// APP START
// -----------------------------------

var App = angular.module('angle', [
    'ngRoute',
    'ngAnimate',
    'ngStorage',
    'ngCookies',
    'pascalprecht.translate',
    'ui.bootstrap',
    'ui.router',
    'oc.lazyLoad',
    'cfp.loadingBar',
    'ngSanitize',
    'ngResource',
    'tmh.dynamicLocale',
    'ui.utils'
]);

App.run(["$rootScope", "$state", "$stateParams", '$window', '$templateCache', '$localStorage', function ($rootScope, $state, $stateParams, $window, $templateCache, $localStorage) {
    // Set reference to access them from any scope
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$storage = $window.localStorage;

    // Uncomment this to disable template cache
    /*$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      if (typeof(toState) !== 'undefined'){
        $templateCache.remove(toState.templateUrl);
      }
  });*/

    // Scope Globals
    // -----------------------------------
    $rootScope.app = {
        name: 'Sugon',
        description: 'Angular Bootstrap Admin Template',
        year: ((new Date()).getFullYear()),
        searchKey: '',
        layout: {
            isFixed: true,
            isCollapsed: false,
            isBoxed: false,
            isRTL: false,
            horizontal: false,
            isFloat: false,
            asideHover: false,
            theme: null
        },
        useFullLayout: false,
        hiddenFooter: false,
        viewAnimation: 'ng-fadeInUp' //淡入淡出
    };
    $rootScope.user = {
        name: $localStorage.userName,
        id: $localStorage.userId,
        job: 'ng-developer',
        picture: 'app/img/user/02.jpg'
    };

}]);

/**=========================================================
 * Module: config.js
 * App routes and resources configuration
 =========================================================*/

App.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
    function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
        'use strict';

        // Set the following to true to enable the HTML5 Mode
        // You may have to set <base> tag in index and a routing configuration in your server
        $locationProvider.html5Mode(false);

        // defaults to dashboard
        //$urlRouterProvider.otherwise('/app/dashboard');
        $urlRouterProvider.otherwise('/page/login');

        //
        // Application Routes
        // -----------------------------------
        $stateProvider
            .state('app', {
                url: '/app',
                abstract: true,
                templateUrl: helper.basepath('app.html'),
                controller: 'AppController',
                resolve: helper.resolveFor('fastclick', 'modernizr', 'icons', 'screenfull', 'animo', 'sparklines', 'slimscroll', 'classyloader', 'toaster', 'whirl')
            })
            .state('app.regularGroup', {
                url: '/regularGroup',
                title: 'RegularGroup',
                templateUrl: helper.basepath('regular/regularGroup.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.mcgc', {
                url: '/mcgc',
                title: 'Mcgc',
                templateUrl: helper.basepath('actualCenter/mcgc.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.tupu1', {
                url: '/tupu1',
                title: 'Tupu1',
                templateUrl: helper.basepath('actualCenter/tupu1.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.xxlJob', {
                url: '/xxlJob',
                title: 'xxlJob',
                templateUrl: helper.basepath('actualCenter/xxlJob.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.xxlJob_yxbb', {
                url: '/xxlJob_yxbb',
                title: 'xxlJob_yxbb',
                templateUrl: helper.basepath('actualCenter/xxlJob_yxbb.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.xxlJob_rwgl', {
                url: '/xxlJob_rwgl',
                title: 'xxlJob_rwgl',
                templateUrl: helper.basepath('actualCenter/xxlJob_rwgl.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.xxlJob_ddrz', {
                url: '/xxlJob_ddrz',
                title: 'xxlJob_ddrz',
                templateUrl: helper.basepath('actualCenter/xxlJob_ddrz.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.xxlJob_zxqgl', {
                url: '/xxlJob_zxqgl',
                title: 'xxlJob_zxqgl',
                templateUrl: helper.basepath('actualCenter/xxlJob_zxqgl.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.dataMerge', {
                url: '/dataMerge',
                title: 'DataMerge',
                templateUrl: helper.basepath('file/dataMerge.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.abnormalTrading', {
                url: '/abnormalTrading',
                title: 'AbnormalTrading',
                templateUrl: helper.basepath('statistics/abnormalTrading.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.blockTrade', {
                url: '/blockTrade',
                params: {'id': null},
                title: 'BlockTrade',
                templateUrl: helper.basepath('statistics/blockTrade.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.rollInPoint', {
                url: '/rollInPoint',
                params: {'id': null},
                title: 'RollInPoint',
                templateUrl: helper.basepath('statistics/rollInPoint.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.rollOutPoint', {
                url: '/rollOutPoint',
                params: {'id': null},
                title: 'RollOutPoint',
                templateUrl: helper.basepath('statistics/rollOutPoint.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.inOut', {
                url: '/inOut',
                params: {'id': null},
                title: 'InOut',
                templateUrl: helper.basepath('statistics/inOut.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.inOrOutOnly', {
                url: '/inOrOutOnly',
                params: {'id': null},
                title: 'InOrOutOnly',
                templateUrl: helper.basepath('statistics/inOrOutOnly.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select')
            })
            .state('app.fileRinseField', {
                url: '/fileRinseField',
                title: 'FileRinseField',
                templateUrl: helper.basepath('file/fileRinseField.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.declaration', {
                url: '/declaration',
                title: 'Declaration',
                templateUrl: helper.basepath('declare/declaration.html'),
                controller: 'declarationController',
                cache: false,
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.neo4jModelOne', {
                url: '/neo4jModelOne',
                title: 'Neo4jModelOne',
                templateUrl: helper.basepath('neo4j/neo4jModelOne.html'),
                cache: false,
                resolve: helper.resolveFor('cytoscape', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.neo4jModel2', {
                url: '/neo4jModel2',
                title: 'Neo4jModel2',
                templateUrl: helper.basepath('neo4j/neo4jModel2.html'),
                cache: false,
                resolve: helper.resolveFor('cytoscape', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.neo4jInitData', {
                url: '/neo4jInitData',
                title: 'Neo4jInitData',
                templateUrl: helper.basepath('neo4j/neo4jInitData.html'),
                cache: false,
                resolve: helper.resolveFor('ngWebsocket', 'cytoscape', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.neo4jRelation', {
                url: '/neo4jRelation',
                title: 'Neo4jRelation',
                templateUrl: helper.basepath('neo4j/neo4jRelation.html'),
                cache: false,
                resolve: helper.resolveFor('ngDraggable', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular', 'angularFileUpload', 'filestyle')
            })
            .state('app.declaration.declar', {
                url: '/declarDetail/:status/:declarName',
                title: 'DeclarDetail',
                templateUrl: helper.basepath('declare/declarDetail.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.businessCheck', {
                url: '/businessCheck',
                title: 'BusinessCheck',
                templateUrl: helper.basepath('declare/businessCheck.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userRole', {
                url: '/userRole',
                title: 'UserRole',
                templateUrl: helper.basepath('system/userRole.html'),
                controller: 'userRoleController',
                cache: false,
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.infoSearch', {
                url: '/infoSearch',
                title: 'InfoSearch',
                templateUrl: helper.basepath('search/infoSearch.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.importCount', {
                url: '/importCount',
                title: 'ImportCount',
                templateUrl: helper.basepath('file/fileImportCount.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'taginput', 'inputmask', 'localytics.directives', 'codemirror', 'moment', 'ui.bootstrap-slider', 'ngWig', 'angularFileUpload', 'filestyle')
            })
            .state('app.userRole.AddOrModify', {
                url: '/userRoleAddOrModify/:active/:id',
                title: 'UserRoleAddOrModify',
                templateUrl: helper.basepath('system/userRoleAddOrModify.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.dataAuthority', {
                url: '/dataAuthority/:id',
                title: 'DataAuthority',
                templateUrl: helper.basepath('file/dataAuthority.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.dataAuthority.AddOrModify', {
                url: '/dataAuthorityAddOrModify/:active/:id',
                title: 'DataAuthorityAddOrModify',
                templateUrl: helper.basepath('file/dataAuthorityAddOrModify.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.dataAuthority.dataGroupTable', {
                url: '/dataAuthorityDataGroupTable/:groupId',
                title: 'DataAuthorityDataGroupTable',
                templateUrl: helper.basepath('file/dataGroupTable.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userGroup.groupRole', {
                url: '/groupRole/:groupId/:groupName',
                title: 'GroupRole',
                templateUrl: helper.basepath('system/groupRole.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userRole.userRolePage', {
                url: '/userRolePage/:userRoleId',
                title: 'UserRolePage',
                templateUrl: helper.basepath('system/userRolePage.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.pageRegister', {
                url: '/pageRegister',
                title: 'PageRegister',
                templateUrl: helper.basepath('system/pageRegister.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userGroup', {
                url: '/userGroup',
                title: 'UserGroup',
                templateUrl: helper.basepath('system/userGroup.html'),
                controller: 'userGroupController',
                cache: false,
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userGroup.AddOrModify', {
                url: '/userGroupAddOrModify/:active/:id',
                title: 'UserGroupAddOrModify',
                templateUrl: helper.basepath('system/userGroupAddOrModify.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userGroup.users', {
                url: '/userGroupUsers/:userGroupid',
                title: 'UserGroupUsers',
                templateUrl: helper.basepath('system/userGroupUsers.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.dataAuthority.users', {
                url: '/dataAuthorityUsers/:dataGroupId',
                title: 'DataAuthorityUsers',
                templateUrl: helper.basepath('file/dataAuthorityUsers.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.userGroup.edit', {
                url: '/userGroupEdit',
                title: 'UserGroupEdit',
                templateUrl: helper.basepath('system/userGroupEdit.html'),
                resolve: helper.resolveFor('ngWig', 'flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.caseManager', {
                url: '/caseManager',
                title: 'CaseManager',
                templateUrl: helper.basepath('file/caseManager.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.fileTemplateGroup', {
                url: '/fileTemplateGroup',
                title: 'FileTemplateGroup',
                templateUrl: helper.basepath('file/fileTemplateGroup.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.fileTemplate', {
                url: '/fileTemplate',
                title: 'FileTemplate',
                templateUrl: helper.basepath('file/fileTemplate.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'textAngular')
            })
            .state('app.fileUpload', {
                url: '/fileUpload',
                title: 'FileUpload',
                templateUrl: helper.basepath('file/fileUpload.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'angularFileUpload', 'filestyle')
            })
            .state('app.fileManager', {
                url: '/fileManager',
                title: 'FileManager',
                templateUrl: helper.basepath('file/fileManager.html'),
                cache: false,
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'ngWebsocket')
            })
            .state('app.checkUser', {
                url: '/checkUser',
                title: 'CheckUser',
                templateUrl: helper.basepath('system/checkUser.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'taginput', 'inputmask', 'localytics.directives', 'codemirror', 'moment', 'ui.bootstrap-slider', 'ngWig')
            })
            .state('app.config', {
                url: '/config',
                title: 'Config',
                templateUrl: helper.basepath('system/config.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins')
            })
            .state('app.dictionary', {
                url: '/dictionary',
                title: 'Dictionary',
                templateUrl: helper.basepath('system/dictionary.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'taginput', 'inputmask', 'localytics.directives', 'codemirror', 'moment', 'ui.bootstrap-slider', 'ngWig')
            })
            .state('app.whiteList', {
                url: '/whiteList',
                title: 'whiteList',
                templateUrl: helper.basepath('system/whiteList.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'taginput', 'inputmask', 'localytics.directives', 'codemirror', 'moment', 'ui.bootstrap-slider', 'ngWig')
            })
            .state('app.bussLog', {
                url: '/bussLog',
                title: 'bussLog',
                templateUrl: helper.basepath('system/bussLog.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'datatables', 'ui.select', 'taginput', 'inputmask', 'localytics.directives', 'codemirror', 'moment', 'ui.bootstrap-slider', 'ngWig')
            })
            .state('app.dashboard', {
                url: '/dashboard',
                title: 'Dashboard',
                templateUrl: helper.basepath('dashboard.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins')
            })
            .state('app.dashboard_v2', {
                url: '/dashboard_v2',
                title: 'Dashboard v2',
                templateUrl: helper.basepath('dashboard_v2.html'),
                controller: ["$rootScope", function ($rootScope) {
                    $rootScope.app.layout.isCollapsed = true;
                }],
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins')
            })
            .state('app.dashboard_v3', {
                url: '/dashboard_v3',
                title: 'Dashboard v3',
                templateUrl: helper.basepath('dashboard_v3.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins', 'vector-map', 'vector-map-maps')
            })
            .state('app.widgets', {
                url: '/widgets',
                title: 'Widgets',
                templateUrl: helper.basepath('widgets.html'),
                resolve: helper.resolveFor('loadGoogleMapsJS', function () {
                    return loadGoogleMaps();
                }, 'ui.map')
            })
            .state('app.buttons', {
                url: '/buttons',
                title: 'Buttons',
                templateUrl: helper.basepath('buttons.html')
            })
            .state('app.colors', {
                url: '/colors',
                title: 'Colors',
                templateUrl: helper.basepath('colors.html')
            })
            .state('app.localization', {
                url: '/localization',
                title: 'Localization',
                templateUrl: helper.basepath('localization.html')
            })
            .state('app.infinite-scroll', {
                url: '/infinite-scroll',
                title: 'Infinite Scroll',
                templateUrl: helper.basepath('infinite-scroll.html'),
                resolve: helper.resolveFor('infinite-scroll')
            })
            .state('app.navtree', {
                url: '/navtree',
                title: 'Nav Tree',
                templateUrl: helper.basepath('nav-tree.html'),
                resolve: helper.resolveFor('angularBootstrapNavTree')
            })
            .state('app.nestable', {
                url: '/nestable',
                title: 'Nestable',
                templateUrl: helper.basepath('nestable.html'),
                resolve: helper.resolveFor('ng-nestable')
            })
            .state('app.sortable', {
                url: '/sortable',
                title: 'Sortable',
                templateUrl: helper.basepath('sortable.html'),
                resolve: helper.resolveFor('htmlSortable')
            })
            .state('app.notifications', {
                url: '/notifications',
                title: 'Notifications',
                templateUrl: helper.basepath('notifications.html'),
                controller: 'NotificationController'
            })
            .state('app.carousel', {
                url: '/carousel',
                title: 'Carousel',
                templateUrl: helper.basepath('carousel.html'),
                resolve: helper.resolveFor('angular-carousel')
            })
            .state('app.ngdialog', {
                url: '/ngdialog',
                title: 'ngDialog',
                templateUrl: helper.basepath('ngdialog.html'),
                resolve: angular.extend(helper.resolveFor('ngDialog'), {
                    tpl: function () {
                        return {path: helper.basepath('ngdialog-template.html')};
                    }
                }),
                controller: 'DialogIntroCtrl'
            })
            .state('app.interaction', {
                url: '/interaction',
                title: 'Interaction',
                templateUrl: helper.basepath('interaction.html')
            })
            .state('app.spinners', {
                url: '/spinners',
                title: 'Spinners',
                templateUrl: helper.basepath('spinners.html'),
                resolve: helper.resolveFor('loaders.css', 'spinkit')
            })
            .state('app.animations', {
                url: '/animations',
                title: 'Animations',
                templateUrl: helper.basepath('animations.html')
            })
            .state('app.dropdown-animations', {
                url: '/dropdown-animations',
                title: 'Dropdown Animations',
                templateUrl: helper.basepath('dropdown-animations.html')
            })
            .state('app.panels', {
                url: '/panels',
                title: 'Panels',
                templateUrl: helper.basepath('panels.html')
            })
            .state('app.portlets', {
                url: '/portlets',
                title: 'Portlets',
                templateUrl: helper.basepath('portlets.html'),
                resolve: helper.resolveFor('jquery-ui', 'jquery-ui-widgets')
            })
            .state('app.maps-google', {
                url: '/maps-google',
                title: 'Maps Google',
                templateUrl: helper.basepath('maps-google.html'),
                resolve: helper.resolveFor('loadGoogleMapsJS', function () {
                    return loadGoogleMaps();
                }, 'ui.map')
            })
            .state('app.maps-vector', {
                url: '/maps-vector',
                title: 'Maps Vector',
                templateUrl: helper.basepath('maps-vector.html'),
                controller: 'VectorMapController',
                resolve: helper.resolveFor('vector-map', 'vector-map-maps')
            })
            .state('app.grid', {
                url: '/grid',
                title: 'Grid',
                templateUrl: helper.basepath('grid.html')
            })
            .state('app.grid-masonry', {
                url: '/grid-masonry',
                title: 'Grid Masonry',
                templateUrl: helper.basepath('grid-masonry.html')
            })
            .state('app.grid-masonry-deck', {
                url: '/grid-masonry-deck',
                title: 'Grid Masonry',
                templateUrl: helper.basepath('grid-masonry-deck.html'),
                resolve: helper.resolveFor('spinkit', 'akoenig.deckgrid')
            })
            .state('app.typo', {
                url: '/typo',
                title: 'Typo',
                templateUrl: helper.basepath('typo.html')
            })
            .state('app.icons-font', {
                url: '/icons-font',
                title: 'Icons Font',
                templateUrl: helper.basepath('icons-font.html')
            })
            .state('app.icons-weather', {
                url: '/icons-weather',
                title: 'Icons Weather',
                templateUrl: helper.basepath('icons-weather.html')
            })
            .state('app.form-standard', {
                url: '/form-standard',
                title: 'Form Standard',
                templateUrl: helper.basepath('form-standard.html')
            })
            .state('app.form-extended', {
                url: '/form-extended',
                title: 'Form Extended',
                templateUrl: helper.basepath('form-extended.html'),
                resolve: helper.resolveFor('codemirror', 'moment', 'taginput', 'inputmask', 'localytics.directives', 'ui.bootstrap-slider', 'ngWig', 'filestyle', 'textAngular', 'textAngularSetup')
            })
            .state('app.form-validation', {
                url: '/form-validation',
                title: 'Form Validation',
                templateUrl: helper.basepath('form-validation.html'),
                resolve: helper.resolveFor('ui.select', 'taginput', 'inputmask', 'localytics.directives')
            })
            .state('app.form-parsley', {
                url: '/form-parsley',
                title: 'Form Validation - Parsley',
                templateUrl: helper.basepath('form-parsley.html'),
                resolve: helper.resolveFor('parsley')
            })
            .state('app.form-wizard', {
                url: '/form-wizard',
                title: 'Form Wizard',
                templateUrl: helper.basepath('form-wizard.html'),
                resolve: helper.resolveFor('parsley')
            })
            .state('app.form-upload', {
                url: '/form-upload',
                title: 'Form upload',
                templateUrl: helper.basepath('form-upload.html'),
                resolve: helper.resolveFor('angularFileUpload', 'filestyle')
            })
            .state('app.form-xeditable', {
                url: '/form-xeditable',
                templateUrl: helper.basepath('form-xeditable.html'),
                resolve: helper.resolveFor('xeditable')
            })
            .state('app.form-imagecrop', {
                url: '/form-imagecrop',
                templateUrl: helper.basepath('form-imagecrop.html'),
                resolve: helper.resolveFor('ngImgCrop', 'filestyle')
            })
            .state('app.form-uiselect', {
                url: '/form-uiselect',
                templateUrl: helper.basepath('form-uiselect.html'),
                controller: 'uiSelectController',
                resolve: helper.resolveFor('ui.select')
            })
            .state('app.chart-flot', {
                url: '/chart-flot',
                title: 'Chart Flot',
                templateUrl: helper.basepath('chart-flot.html'),
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins')
            })
            .state('app.chart-radial', {
                url: '/chart-radial',
                title: 'Chart Radial',
                templateUrl: helper.basepath('chart-radial.html'),
                resolve: helper.resolveFor('classyloader')
            })
            .state('app.chart-js', {
                url: '/chart-js',
                title: 'Chart JS',
                templateUrl: helper.basepath('chart-js.html'),
                resolve: helper.resolveFor('chartjs')
            })
            .state('app.chart-rickshaw', {
                url: '/chart-rickshaw',
                title: 'Chart Rickshaw',
                templateUrl: helper.basepath('chart-rickshaw.html'),
                resolve: helper.resolveFor('angular-rickshaw')
            })
            .state('app.chart-morris', {
                url: '/chart-morris',
                title: 'Chart Morris',
                templateUrl: helper.basepath('chart-morris.html'),
                resolve: helper.resolveFor('morris')
            })
            .state('app.chart-chartist', {
                url: '/chart-chartist',
                title: 'Chart Chartist',
                templateUrl: helper.basepath('chart-chartist.html'),
                resolve: helper.resolveFor('angular-chartist')
            })
            .state('app.table-standard', {
                url: '/table-standard',
                title: 'Table Standard',
                templateUrl: helper.basepath('table-standard.html')
            })
            .state('app.table-extended', {
                url: '/table-extended',
                title: 'Table Extended',
                templateUrl: helper.basepath('table-extended.html')
            })
            .state('app.table-datatable', {
                url: '/table-datatable',
                title: 'Table Datatable',
                templateUrl: helper.basepath('table-datatable.html'),
                resolve: helper.resolveFor('datatables')
            })
            .state('app.table-xeditable', {
                url: '/table-xeditable',
                templateUrl: helper.basepath('table-xeditable.html'),
                resolve: helper.resolveFor('xeditable')
            })
            .state('app.table-ngtable', {
                url: '/table-ngtable',
                templateUrl: helper.basepath('table-ngtable.html'),
                resolve: helper.resolveFor('ngTable', 'ngTableExport')
            })
            .state('app.table-nggrid', {
                url: '/table-nggrid',
                templateUrl: helper.basepath('table-ng-grid.html'),
                resolve: helper.resolveFor('ngGrid')
            })
            .state('app.table-uigrid', {
                url: '/table-uigrid',
                templateUrl: helper.basepath('table-uigrid.html'),
                resolve: helper.resolveFor('ui.grid')
            })
            .state('app.table-angulargrid', {
                url: '/table-angulargrid',
                templateUrl: helper.basepath('table-angulargrid.html'),
                resolve: helper.resolveFor('angularGrid')
            })
            .state('app.timeline', {
                url: '/timeline',
                title: 'Timeline',
                templateUrl: helper.basepath('timeline.html')
            })
            .state('app.calendar', {
                url: '/calendar',
                title: 'Calendar',
                templateUrl: helper.basepath('calendar.html'),
                resolve: helper.resolveFor('jquery-ui', 'jquery-ui-widgets', 'moment', 'fullcalendar')
            })
            .state('app.invoice', {
                url: '/invoice',
                title: 'Invoice',
                templateUrl: helper.basepath('invoice.html')
            })
            .state('app.search', {
                url: '/search',
                title: 'Search',
                templateUrl: helper.basepath('search.html'),
                resolve: helper.resolveFor('moment', 'localytics.directives', 'ui.bootstrap-slider')
            })
            .state('app.todo', {
                url: '/todo',
                title: 'Todo List',
                templateUrl: helper.basepath('todo.html'),
                controller: 'TodoController'
            })
            .state('app.profile', {
                url: '/profile',
                title: 'Profile',
                templateUrl: helper.basepath('profile.html'),
                resolve: helper.resolveFor('loadGoogleMapsJS', function () {
                    return loadGoogleMaps();
                }, 'ui.map')
            })
            .state('app.code-editor', {
                url: '/code-editor',
                templateUrl: helper.basepath('code-editor.html'),
                resolve: {
                    deps: helper.resolveFor('codemirror', 'ui.codemirror', 'codemirror-modes-web', 'angularBootstrapNavTree').deps,
                    filetree: ["LoadTreeService", function (LoadTreeService) {
                        return LoadTreeService.get().$promise.then(function (res) {
                            return res.data;
                        });
                    }]
                },
                controller: ["$rootScope", "$scope", "filetree", function ($rootScope, $scope, filetree) {
                    // Set the tree data into the scope
                    $scope.filetree_data = filetree;
                    // Setup the layout mode
                    $rootScope.app.useFullLayout = true;
                    $rootScope.app.hiddenFooter = true;
                    $rootScope.app.layout.isCollapsed = true;
                    // Restore layout
                    $scope.$on('$destroy', function () {
                        $rootScope.app.useFullLayout = false;
                        $rootScope.app.hiddenFooter = false;
                    });
                }]
            })
            .state('app.template', {
                url: '/template',
                title: 'Blank Template',
                templateUrl: helper.basepath('template.html')
            })
            .state('app.documentation', {
                url: '/documentation',
                title: 'Documentation',
                templateUrl: helper.basepath('documentation.html'),
                resolve: helper.resolveFor('flatdoc')
            })
            // Forum
            // -----------------------------------
            .state('app.forum', {
                url: '/forum',
                title: 'Forum',
                templateUrl: helper.basepath('forum.html')
            })
            .state('app.forum-topics', {
                url: '/forum/topics/:catid',
                title: 'Forum Topics',
                templateUrl: helper.basepath('forum-topics.html')
            })
            .state('app.forum-discussion', {
                url: '/forum/discussion/:topid',
                title: 'Forum Discussion',
                templateUrl: helper.basepath('forum-discussion.html')
            })
            // Blog
            // -----------------------------------
            .state('app.blog', {
                url: '/blog',
                title: 'Blog',
                templateUrl: helper.basepath('blog.html'),
                resolve: helper.resolveFor('angular-jqcloud')
            })
            .state('app.blog-post', {
                url: '/post',
                title: 'Post',
                templateUrl: helper.basepath('blog-post.html'),
                resolve: helper.resolveFor('angular-jqcloud')
            })
            .state('app.articles', {
                url: '/articles',
                title: 'Articles',
                templateUrl: helper.basepath('blog-articles.html'),
                resolve: helper.resolveFor('datatables')
            })
            .state('app.article-view', {
                url: '/article/:id',
                title: 'Article View',
                templateUrl: helper.basepath('blog-article-view.html'),
                resolve: helper.resolveFor('ui.select', 'textAngular')
            })
            // eCommerce
            // -----------------------------------
            .state('app.orders', {
                url: '/orders',
                title: 'Orders',
                templateUrl: helper.basepath('ecommerce-orders.html'),
                resolve: helper.resolveFor('datatables')
            })
            .state('app.order-view', {
                url: '/order-view',
                title: 'Order View',
                templateUrl: helper.basepath('ecommerce-order-view.html')
            })
            .state('app.products', {
                url: '/products',
                title: 'Products',
                templateUrl: helper.basepath('ecommerce-products.html'),
                resolve: helper.resolveFor('datatables')
            })
            .state('app.product-view', {
                url: '/product/:id',
                title: 'Product View',
                templateUrl: helper.basepath('ecommerce-product-view.html')
            })
            // Mailbox
            // -----------------------------------
            .state('app.mailbox', {
                url: '/mailbox',
                title: 'Mailbox',
                abstract: true,
                templateUrl: helper.basepath('mailbox.html'),
                controller: 'MailboxController'
            })
            .state('app.mailbox.folder', {
                url: '/folder/:folder',
                title: 'Mailbox',
                templateUrl: helper.basepath('mailbox-inbox.html')
            })
            .state('app.mailbox.view', {
                url: "/{mid:[0-9]{1,4}}",
                title: 'View mail',
                templateUrl: helper.basepath('mailbox-view.html'),
                resolve: helper.resolveFor('ngWig')
            })
            .state('app.mailbox.compose', {
                url: '/compose',
                title: 'Mailbox',
                templateUrl: helper.basepath('mailbox-compose.html'),
                resolve: helper.resolveFor('ngWig')
            })
            //
            // Multiple level example
            // -----------------------------------
            .state('app.multilevel', {
                url: '/multilevel',
                title: 'Multilevel',
                template: '<h3>Multilevel Views</h3>' + '<div class="lead ba p">View @ Top Level ' + '<div ui-view=""></div> </div>'
            })
            .state('app.multilevel.level1', {
                url: '/level1',
                title: 'Multilevel - Level1',
                template: '<div class="lead ba p">View @ Level 1' + '<div ui-view=""></div> </div>'
            })
            .state('app.multilevel.level1.item', {
                url: '/item',
                title: 'Multilevel - Level1',
                template: '<div class="lead ba p"> Menu item @ Level 1</div>'
            })
            .state('app.multilevel.level1.level2', {
                url: '/level2',
                title: 'Multilevel - Level2',
                template: '<div class="lead ba p">View @ Level 2' + '<div ui-view=""></div> </div>'
            })
            .state('app.multilevel.level1.level2.level3', {
                url: '/level3',
                title: 'Multilevel - Level3',
                template: '<div class="lead ba p">View @ Level 3' + '<div ui-view=""></div> </div>'
            })
            .state('app.multilevel.level1.level2.level3.item', {
                url: '/item',
                title: 'Multilevel - Level3 Item',
                template: '<div class="lead ba p"> Menu item @ Level 3</div>'
            })
            //
            // Single Page Routes
            // -----------------------------------
            //helper.resolveFor  传参是常量
            .state('page', {
                url: '/page',
                templateUrl: 'app/pages/page.html',
                resolve: helper.resolveFor('modernizr', 'icons'),
                controller: ["$rootScope", function ($rootScope) {
                    $rootScope.app.layout.isBoxed = false;
                }]
            })
            .state('page.login', {
                url: '/login',
                title: "Login",
                templateUrl: 'app/pages/login.html'
            })
            .state('page.home', {
                url: '/home',
                title: "Home",
                templateUrl: 'app/pages/home.html',
            })
            .state('page.register', {
                url: '/register',
                title: "Register",
                templateUrl: 'app/pages/register.html'
            })
            .state('page.recover', {
                url: '/recover',
                title: "Recover",
                templateUrl: 'app/pages/recover.html'
            })
            .state('page.lock', {
                url: '/lock',
                title: "Lock",
                templateUrl: 'app/pages/lock.html'
            })
            .state('page.404', {
                url: '/404',
                title: "Not Found",
                templateUrl: 'app/pages/404.html'
            })
            //
            // Horizontal layout
            // -----------------------------------
            .state('app-h', {
                url: '/app-h',
                abstract: true,
                templateUrl: helper.basepath('app-h.html'),
                controller: 'AppController',
                resolve: helper.resolveFor('fastclick', 'modernizr', 'icons', 'screenfull', 'animo', 'sparklines', 'slimscroll', 'classyloader', 'toaster', 'whirl')
            })
            .state('app-h.dashboard_v2', {
                url: '/dashboard_v2',
                title: 'Dashboard v2',
                templateUrl: helper.basepath('dashboard_v2.html'),
                controller: ["$rootScope", "$scope", function ($rootScope, $scope) {
                    $rootScope.app.layout.horizontal = true;
                    $scope.$on('$destroy', function () {
                        $rootScope.app.layout.horizontal = false;
                    });
                }],
                resolve: helper.resolveFor('flot-chart', 'flot-chart-plugins')
            })
        //
        // CUSTOM RESOLVES
        //   Add your own resolves properties
        //   following this object extend
        //   method
        // -----------------------------------
        // .state('app.someroute', {
        //   url: '/some_url',
        //   templateUrl: 'path_to_template.html',
        //   controller: 'someController',
        //   resolve: angular.extend(
        //     helper.resolveFor(), {
        //     // YOUR RESOLVES GO HERE
        //     }
        //   )
        // })
        ;


    }]).config(['$ocLazyLoadProvider', 'APP_REQUIRES', function ($ocLazyLoadProvider, APP_REQUIRES) {
    'use strict';

    // Lazy Load modules configuration
    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: APP_REQUIRES.modules
    });

}]).config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
        'use strict';
        // registering components after bootstrap
        App.controller = $controllerProvider.register;
        App.directive = $compileProvider.directive;
        App.filter = $filterProvider.register;
        App.factory = $provide.factory;
        App.service = $provide.service;
        App.constant = $provide.constant;
        App.value = $provide.value;

    }]).config(['$translateProvider', function ($translateProvider) {

    $translateProvider.useStaticFilesLoader({
        prefix: 'app/i18n/',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('zh_cn');//默认显示语言
    $translateProvider.useLocalStorage();
    $translateProvider.usePostCompiling(true);

}]).config(['tmhDynamicLocaleProvider', function (tmhDynamicLocaleProvider) {

    tmhDynamicLocaleProvider.localeLocationPattern('vendor/angular-i18n/angular-locale_{{locale}}.js');

    // tmhDynamicLocaleProvider.useStorage('$cookieStore');

}]).config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {

    cfpLoadingBarProvider.includeBar = true;
    cfpLoadingBarProvider.includeSpinner = false;
    cfpLoadingBarProvider.latencyThreshold = 500;
    cfpLoadingBarProvider.parentSelector = '.wrapper > section';

}]).config(['$tooltipProvider', function ($tooltipProvider) {

    $tooltipProvider.options({appendToBody: true});

}])
;

/**=========================================================
 * Module: constants.js
 * Define constants to inject across the application
 =========================================================*/
App
    .constant('APP_COLORS', {
        'primary': '#5d9cec',
        'success': '#27c24c',
        'info': '#23b7e5',
        'warning': '#ff902b',
        'danger': '#f05050',
        'inverse': '#131e26',
        'green': '#37bc9b',
        'pink': '#f532e5',
        'purple': '#7266ba',
        'dark': '#3a3f51',
        'yellow': '#fad732',
        'gray-darker': '#232735',
        'gray-dark': '#3a3f51',
        'gray': '#dde6e9',
        'gray-light': '#e4eaec',
        'gray-lighter': '#edf1f2'
    })
    .constant('APP_MEDIAQUERY', {
        'desktopLG': 1200,
        'desktop': 992,
        'tablet': 768,
        'mobile': 480
    })
    .constant('APP_REQUIRES', {
        // jQuery based and standalone scripts
        scripts: {
            'ngWebsocket': ['vendor/ng-websocket/ng-websocket.js'],
            'drag': ['vendor/drag/angularDrag.js'],
            'cytoscape': ['vendor/cytoscape/cytoscape.min.js'],
            'ngDraggable': ['vendor/ngDraggable/ngDraggable.js'],
            'whirl': ['vendor/whirl/dist/whirl.css'],
            'classyloader': ['vendor/jquery-classyloader/js/jquery.classyloader.min.js'],
            'animo': ['vendor/animo.js/animo.js'],
            'fastclick': ['vendor/fastclick/lib/fastclick.js'],
            'modernizr': ['vendor/modernizr/modernizr.js'],
            'animate': ['vendor/animate.css/animate.min.css'],
            'icons': ['vendor/skycons/skycons.js',
                'vendor/fontawesome/css/font-awesome.min.css',
                'vendor/simple-line-icons/css/simple-line-icons.css',
                'vendor/weather-icons/css/weather-icons.min.css'],
            'sparklines': ['app/vendor/sparklines/jquery.sparkline.min.js'],
            'wysiwyg': ['vendor/bootstrap-wysiwyg/bootstrap-wysiwyg.js',
                'vendor/bootstrap-wysiwyg/external/jquery.hotkeys.js'],
            'slimscroll': ['vendor/slimScroll/jquery.slimscroll.min.js'],
            'screenfull': ['vendor/screenfull/dist/screenfull.js'],
            'vector-map': ['vendor/ika.jvectormap/jquery-jvectormap-1.2.2.min.js',
                'vendor/ika.jvectormap/jquery-jvectormap-1.2.2.css'],
            'vector-map-maps': ['vendor/ika.jvectormap/jquery-jvectormap-world-mill-en.js',
                'vendor/ika.jvectormap/jquery-jvectormap-us-mill-en.js'],
            'loadGoogleMapsJS': ['app/vendor/gmap/load-google-maps.js'],
            'flot-chart': ['vendor/Flot/jquery.flot.js'],
            'flot-chart-plugins': ['vendor/flot.tooltip/js/jquery.flot.tooltip.min.js',
                'vendor/Flot/jquery.flot.resize.js',
                'vendor/Flot/jquery.flot.pie.js',
                'vendor/Flot/jquery.flot.time.js',
                'vendor/Flot/jquery.flot.categories.js',
                'vendor/flot-spline/js/jquery.flot.spline.min.js'],
            // jquery core and widgets
            'jquery-ui': ['vendor/jquery-ui/ui/core.js',
                'vendor/jquery-ui/ui/widget.js'],
            // loads only jquery required modules and touch support
            'jquery-ui-widgets': ['vendor/jquery-ui/ui/core.js',
                'vendor/jquery-ui/ui/widget.js',
                'vendor/jquery-ui/ui/mouse.js',
                'vendor/jquery-ui/ui/draggable.js',
                'vendor/jquery-ui/ui/droppable.js',
                'vendor/jquery-ui/ui/sortable.js',
                'vendor/jqueryui-touch-punch/jquery.ui.touch-punch.min.js'],
            'moment': ['vendor/moment/min/moment-with-locales.min.js'],
            'inputmask': ['vendor/jquery.inputmask/dist/jquery.inputmask.bundle.min.js'],
            'flatdoc': ['vendor/flatdoc/flatdoc.js'],
            'codemirror': ['vendor/codemirror/lib/codemirror.js',
                'vendor/codemirror/lib/codemirror.css'],
            // modes for common web files
            'codemirror-modes-web': ['vendor/codemirror/mode/javascript/javascript.js',
                'vendor/codemirror/mode/xml/xml.js',
                'vendor/codemirror/mode/htmlmixed/htmlmixed.js',
                'vendor/codemirror/mode/css/css.js'],
            'taginput': ['vendor/bootstrap-tagsinput/dist/bootstrap-tagsinput.css',
                'vendor/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js'],
            'filestyle': ['vendor/bootstrap-filestyle/src/bootstrap-filestyle.js'],
            'parsley': ['vendor/parsleyjs/dist/parsley.min.js'],
            'fullcalendar': ['vendor/fullcalendar/dist/fullcalendar.min.js',
                'vendor/fullcalendar/dist/fullcalendar.css'],
            'gcal': ['vendor/fullcalendar/dist/gcal.js'],
            'chartjs': ['vendor/Chart.js/Chart.js'],
            'morris': ['vendor/raphael/raphael.js',
                'vendor/morris.js/morris.js',
                'vendor/morris.js/morris.css'],
            'loaders.css': ['vendor/loaders.css/loaders.css'],
            'spinkit': ['vendor/spinkit/css/spinkit.css']
        },
        // Angular based script (use the right module name)
        modules: [
            {
                name: 'toaster', files: ['vendor/angularjs-toaster/toaster.js',
                    'vendor/angularjs-toaster/toaster.css']
            },
            {
                name: 'localytics.directives', files: ['vendor/chosen_v1.2.0/chosen.jquery.min.js',
                    'vendor/chosen_v1.2.0/chosen.min.css',
                    'vendor/angular-chosen-localytics/chosen.js']
            },
            {
                name: 'ngDialog', files: ['vendor/ngDialog/js/ngDialog.min.js',
                    'vendor/ngDialog/css/ngDialog.min.css',
                    'vendor/ngDialog/css/ngDialog-theme-default.min.css']
            },
            {name: 'ngWig', files: ['vendor/ngWig/dist/ng-wig.min.js']},
            {
                name: 'ngTable', files: ['vendor/ng-table/dist/ng-table.min.js',
                    'vendor/ng-table/dist/ng-table.min.css']
            },
            {name: 'ngTableExport', files: ['vendor/ng-table-export/ng-table-export.js']},
            {
                name: 'angularBootstrapNavTree', files: ['vendor/angular-bootstrap-nav-tree/dist/abn_tree_directive.js',
                    'vendor/angular-bootstrap-nav-tree/dist/abn_tree.css']
            },
            {
                name: 'htmlSortable', files: ['vendor/html.sortable/dist/html.sortable.js',
                    'vendor/html.sortable/dist/html.sortable.angular.js']
            },
            {
                name: 'xeditable', files: ['vendor/angular-xeditable/dist/js/xeditable.js',
                    'vendor/angular-xeditable/dist/css/xeditable.css']
            },
            {name: 'angularFileUpload', files: ['vendor/angular-file-upload/angular-file-upload.js']},
            {
                name: 'ngImgCrop', files: ['vendor/ng-img-crop/compile/unminified/ng-img-crop.js',
                    'vendor/ng-img-crop/compile/unminified/ng-img-crop.css']
            },
            {
                name: 'ui.select', files: ['vendor/angular-ui-select/dist/select.js',
                    'vendor/angular-ui-select/dist/select.css']
            },
            {name: 'ui.codemirror', files: ['vendor/angular-ui-codemirror/ui-codemirror.js']},
            {
                name: 'angular-carousel', files: ['vendor/angular-carousel/dist/angular-carousel.css',
                    'vendor/angular-carousel/dist/angular-carousel.js']
            },
            {
                name: 'ngGrid', files: ['vendor/ng-grid/build/ng-grid.min.js',
                    'vendor/ng-grid/ng-grid.css']
            },
            {name: 'infinite-scroll', files: ['vendor/ngInfiniteScroll/build/ng-infinite-scroll.js']},
            {
                name: 'ui.bootstrap-slider', files: ['vendor/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js',
                    'vendor/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css',
                    'vendor/angular-bootstrap-slider/slider.js']
            },
            {
                name: 'ui.grid', files: ['vendor/angular-ui-grid/ui-grid.min.css',
                    'vendor/angular-ui-grid/ui-grid.min.js']
            },
            {name: 'textAngularSetup', files: ['vendor/textAngular/src/textAngularSetup.js']},
            {
                name: 'textAngular', files: ['vendor/textAngular/dist/textAngular-rangy.min.js',
                    'vendor/textAngular/src/textAngular.js',
                    'vendor/textAngular/src/textAngularSetup.js',
                    'vendor/textAngular/src/textAngular.css'], serie: true
            },
            {
                name: 'angular-rickshaw', files: ['vendor/d3/d3.min.js',
                    'vendor/rickshaw/rickshaw.js',
                    'vendor/rickshaw/rickshaw.min.css',
                    'vendor/angular-rickshaw/rickshaw.js'], serie: true
            },
            {
                name: 'angular-chartist', files: ['vendor/chartist/dist/chartist.min.css',
                    'vendor/chartist/dist/chartist.js',
                    'vendor/angular-chartist.js/dist/angular-chartist.js'], serie: true
            },
            {name: 'ui.map', files: ['vendor/angular-ui-map/ui-map.js']},
            {
                name: 'datatables', files: ['vendor/datatables/media/css/jquery.dataTables.css',
                    'vendor/datatables/media/js/jquery.dataTables.js',
                    'vendor/angular-datatables/dist/angular-datatables.js'], serie: true
            },
            {
                name: 'angular-jqcloud', files: ['vendor/jqcloud2/dist/jqcloud.css',
                    'vendor/jqcloud2/dist/jqcloud.js',
                    'vendor/angular-jqcloud/angular-jqcloud.js']
            },
            {
                name: 'angularGrid', files: ['vendor/ag-grid/dist/angular-grid.css',
                    'vendor/ag-grid/dist/angular-grid.js',
                    'vendor/ag-grid/dist/theme-dark.css',
                    'vendor/ag-grid/dist/theme-fresh.css']
            },
            {
                name: 'ng-nestable', files: ['vendor/ng-nestable/src/angular-nestable.js',
                    'vendor/nestable/jquery.nestable.js']
            },
            {name: 'akoenig.deckgrid', files: ['vendor/angular-deckgrid/angular-deckgrid.js']}
        ]
    })
;

App.service('drawCodaService', function () {

    lineX = function () {
        var lineX = Math.floor(Math.random() * 80);
        return lineX;
    }

    lineY = function () {
        var lineY = Math.floor(Math.random() * 30);
        return lineY;
    }

    this.drawCoda = function (c) {
        c.style.border = "1px solid black";
        c.width = 80;
        c.height = 30;
        r1 = Math.floor(Math.random() * 255);
        g1 = Math.floor(Math.random() * 255);
        b1 = Math.floor(Math.random() * 255);
        var context = c.getContext('2d');
        context.fillStyle = "#ccc";
        context.fillRect(0, 0, 80, 30);
        context.fillStyle = "red";
        var text = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        context.font = "25px 黑体";
        context.rotate(Math.random() * 8 * Math.PI / 180)
        context.fillStyle = "rgb(" + r1 + ',' + g1 + ',' + b1 + ")";
        var first = text[Math.floor(Math.random() * text.length)];
        context.fillText(first, 10, 20);
        r2 = Math.floor(Math.random() * 255);
        g2 = Math.floor(Math.random() * 255);
        b2 = Math.floor(Math.random() * 255);

        context.font = "25px 黑体";
        context.rotate(Math.random() * 8 * Math.PI / 180)
        context.fillStyle = "rgb(" + r2 + ',' + g2 + ',' + b2 + ")";
        var second = text[Math.floor(Math.random() * text.length)];
        context.fillText(second, 25, 20);
        r3 = Math.floor(Math.random() * 255);
        g3 = Math.floor(Math.random() * 255);
        b3 = Math.floor(Math.random() * 255);

        context.font = "25px 黑体";
        context.rotate(Math.random() * -8 * Math.PI / 180)
        context.fillStyle = "rgb(" + r3 + ',' + g3 + ',' + b3 + ")";
        var third = text[Math.floor(Math.random() * text.length)];
        context.fillText(third, 40, 20);
        r4 = Math.floor(Math.random() * 255);
        g4 = Math.floor(Math.random() * 255);
        b4 = Math.floor(Math.random() * 255);
        context.font = "25px 黑体";

        context.rotate(Math.random() * -8 * Math.PI / 180)
        context.fillStyle = "rgb(" + r4 + ',' + g4 + ',' + b4 + ")";
        var fourth = text[Math.floor(Math.random() * text.length)];
        context.fillText(fourth, 55, 20);

        for (var i = 1; i <= 5; i++) {
            r = Math.floor(Math.random() * 255);
            g = Math.floor(Math.random() * 255);
            b = Math.floor(Math.random() * 255);
            context.beginPath();
            context.strokeStyle = "rgb(" + r + ',' + g + ',' + b + ")";
            context.moveTo(lineX(), lineY());
            context.lineTo(lineX(), lineY());
            context.lineWidth = 0.5;
            context.closePath();
            context.stroke();
        }

        return first + second + third + fourth;
    }

});

App.service('myservice', function ($window, $state, $http) {
//**************************************************************************//
    /*
      说明：通用方法对对象进行非空校验，包括null，undefined和空字符串。
    */
    this.isEmpty = function (object) {
        if (object == null || object == undefined || object.length == 0 || (object + '').trim() == '') {
            return true;
        } else {
            return false;
        }
    }
    this.errors = function (restResult) {
        if (restResult.flag == "FAILED") {
            var message = "";
            var isExpired = false;
            var isLocked = false;
            var unRegister = false;
            angular.forEach(restResult.errorList, function (e) {
                message += e.errorMessage + "\r\n";
                if (e.errorCode.indexOf("SYS-00-000") != -1) {
                    isExpired = true;
                }
                if (e.errorCode.indexOf("SYS-01-000") != -1) {
                    isLocked = true;
                }
                if (e.errorCode.indexOf("SYS-02-000") != -1) {
                    unRegister = true;
                }
            })
            alert(message);
            if (isExpired) {
                // $window.location.href ='/';
                $state.go('page.login');
            }
            if (isLocked) {
                $state.go('page.lock');
            }
            if (unRegister) {
                $state.go('page.register');
            }
            return message + restResult.message;
        }
    }
//**************************************************************************//

    this.setCookie = function (name, value)//两个参数，一个是cookie的名子，一个是值
    {
        var Days = 1; //此 cookie 将被保存 1 天
        var exp = new Date();    //new Date("December 31, 9998");
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }

    this.getCookie = function (name)//取cookies函数
    {
        var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
        if (arr != null) return unescape(arr[2]);
        return null;

    }

    this.deleteCookie = function (name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();

    }

    this.loginLockCheck = function () {
        var url = "/checkLoginLok";
        var that = this;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            that.errors(temp);
        }).error(function (data) {
            alert("登录和锁校验出错！");
        });
    }

    this.setSerialNumber = function (obj) {
        var i = 1;
        angular.forEach(obj, function (item) {
            item.no = i++;
        });
        return obj;
    }

    this.dragFuncAll = function (id) {
        var Drag = document.getElementById(id);
        Drag.onmousedown = function (event) {
            var ev = event || window.event;
            event.stopPropagation();
            var disX = ev.clientX - Drag.offsetLeft;
            var disY = ev.clientY - Drag.offsetTop;
            document.onmousemove = function (event) {
                var ev = event || window.event;
                Drag.style.left = ev.clientX - disX + "px";
                Drag.style.top = ev.clientY - disY + "px";
                Drag.style.cursor = "move";
            };
        };
        Drag.onmouseup = function () {
            document.onmousemove = null;
            this.style.cursor = "default";
        };
    }

    this.dragFunc = function (id) {
        var elmnt = document.getElementById((id));
        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        if (document.getElementById(elmnt.id + "header")) {
            /* if present, the header is where you move the DIV from:*/
            document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
        } else {
            /* otherwise, move the DIV from anywhere inside the DIV:*/
            elmnt.onmousedown = dragMouseDown;
        }

        function dragMouseDown(e) {
            e = e || window.event;
            // get the mouse cursor position at startup:
            pos3 = e.clientX;
            pos4 = e.clientY;
            document.onmouseup = closeDragElement;
            // call a function whenever the cursor moves:
            document.onmousemove = elementDrag;
        }

        function elementDrag(e) {
            e = e || window.event;
            // calculate the new cursor position:
            pos1 = pos3 - e.clientX;
            pos2 = pos4 - e.clientY;
            pos3 = e.clientX;
            pos4 = e.clientY;
            // set the element's new position:
            elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
            elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
        }

        function closeDragElement() {
            /* stop moving when mouse button is released:*/
            document.onmouseup = null;
            document.onmousemove = null;
        }
    }

    this.doPost = function (url, params, message) {
        var that = this;
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            that.errors(temp);
            if (message) {
                alert(message);
            }
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

});

App.controller("mcgcController", function ($http, $timeout, $scope, $sce,
                                           myservice) {
    myservice.loginLockCheck();
    var url = '/actualCenter/getMcgcUrl';
    $scope.skip = function () {
        $('.hide').removeClass('hide'); // 打开模态框
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            var dataSrc = data.obj;
            dataSrc = $sce.trustAsResourceUrl(dataSrc);
            $('#ruleModalIfram').attr('src', dataSrc);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.skip();
})

App.controller("dashBoardController", ['$http', '$timeout', '$scope', '$rootScope', '$localStorage', 'myservice', function ($http, $timeout, $scope, $rootScope, $localStorage,
                                                                                                                            myservice) {
    myservice.loginLockCheck();
    var url = 'api/account/getUserInfo4Ca';
    $scope.skip = function () {
        $('.hide').removeClass('hide'); // 打开模态框
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $localStorage.userName = temp.obj.userName;
            $localStorage.userId = temp.obj.id;
            $rootScope.user.name = temp.obj.userName;
            $rootScope.user.id = temp.obj.id;

        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.skip();
}]);

App.controller("xxljob_yxbbController", function ($http, $timeout, $scope, $sce,
                                             myservice) {
    myservice.loginLockCheck();

})

App.controller("xxljob_ddrzController", function ($http, $timeout, $scope, $sce,
                                                  myservice) {
    myservice.loginLockCheck();

})

App.controller("xxljob_rwglController", function ($http, $timeout, $scope, $sce,
                                                  myservice) {
    myservice.loginLockCheck();

})

App.controller("xxljob_zxqglController", function ($http, $timeout, $scope, $sce,
                                                  myservice) {
    myservice.loginLockCheck();

})


App.controller("dataMergeController", function ($http, $timeout, $scope,
                                                myservice) {

    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    myservice.dragFunc("cnDivDetail");
    myservice.dragFunc("recordCnDivDetail");
    myservice.dragFunc("checkFieldCnDivDetail");

    $("#cnDivDetail").hide();
    $("#recordCnDivDetail").hide();
    $("#checkFieldCnDivDetail").hide();


    //获取案件信息
    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/dataMerge/getCases";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.query();

    $scope.detail = function (caseName, fileTableDtoList) {
        $("#cnDivDetail").show();
        $scope.caseName_div = caseName;
        $scope.tables = myservice.setSerialNumber(fileTableDtoList);
    }
    $scope.closeDetail = function () {
        $("#cnDivDetail").hide();
    }

    var checkFields = [];
    $scope.itemsPerPage = 5;
    $scope.offset = 0;

    $scope.tableDetail = function (item) {
        $scope.caseName_record = $scope.caseName_div;
        $scope.templateName_record = item.fileTemplateDto.templateName;
        $scope.item_01 = item;
        $("#checkFieldCnDivDetail").show();
        checkFields = [];
        $scope.tableInfo = item;
        $scope.getFileRinseDetailsByGroupId(item.fileTemplateDto.fileRinseGroupId);
        $scope.getQuantity(item);
        $scope.getTableRecord(item);
    }

    $scope.tableDetail_01 = function (item) {
        $scope.getQuantity(item);
        $scope.getTableRecord(item);
    }


    //初始化数据
    //分页参数（参数名固定不可变）
    $scope.pageConfig = {
        pageSize:5,    //每页条数（不设置时，默认为10）
        pageIndex: 1,    //当前页码
        totalCount: 0,   //总记录数
        totalPage: 0,     //总页码
        prevPage: '< 上一页',     //上一页（不设置时，默认为：<）
        nextPage: '下一页 >',     //下一页（不设置时，默认为：>）
        firstPage: '<< 首页',     //首页（不设置时，默认为：<<）
        lastPage: '末页 >>',     //末页（不设置时，默认为：>>）
        degeCount: 3,             //当前页前后两边可显示的页码个数（不设置时，默认为3）
        isShowEllipsis: true    //是否显示省略号不可点击按钮（true：显示，false：不显示）
    }

    $scope.getQuantity = function (item) {
        var url = "/dataMerge/getTableRecordQuantity";
        var params = {
            tableName: item.tableName,
            checkFields: checkFields
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.bigTotalItems = temp.obj;
            $scope.pageConfig.totalCount = temp.obj;
            $scope.pageConfig.totalPage = Math.ceil($scope.pageConfig.totalCount / $scope.pageConfig.pageSize); //总页数
            console.log('总记录数：' + $scope.pageConfig.totalCount + '； 总页数：' + $scope.pageConfig.totalPage + '；当前页码：' + $scope.pageConfig.pageIndex);
            $scope.$broadcast("initPage")   //调用分页组件里的初始化页码函数
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.closeRecordDetail = function () {
        $("#recordCnDivDetail").hide();
        $("#checkFieldCnDivDetail").hide();
    }

    $scope.getTableRecord = function (item) {
        $("#recordCnDivDetail").show();
        var url = "/dataMerge/getTableRecord";
        var params = {
            tableName: item.tableName,
            fileTemlateId: item.fileTemplateDto.id,
            perSize: $scope.itemsPerPage,
            offset: $scope.offset,
            checkFields: checkFields
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.mppTableDto = temp.obj;
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.changePage = function (pageNo) {
        $scope.offset = (pageNo - 1) * $scope.itemsPerPage;
        $scope.getTableRecord($scope.tableInfo);
    }

    //监听分页组件中的分页点击事件
    $scope.$on("clickPage", function (e, m) {
        $scope.pageIndex = $scope.pageConfig.pageIndex;
        $scope.itemsPerPage = $scope.pageConfig.pageSize;
        $scope.changePage($scope.pageIndex);
        console.log('pageSize=' + $scope.pageConfig.pageSize + 'pageIndex=' + $scope.pageIndex);
    })

    $scope.getFileRinseDetailsByGroupId = function (rinseGroupId) {

        var url = "/fileRinse/getFileRinseDetailsByGroupId?groupId=" + rinseGroupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.fileRinseDetails = temp.obj;
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.zoomIn = function () {
        $scope.zoomHeight = $scope.fileRinseDetails.length * 55 + "px";
    }

    $scope.selectLabel = function (label) {
        if (label.checked) {
            checkFields.push(label.id);
        } else {
            checkFields.splice(checkFields.indexOf(label.id), 1);
        }
        //重新计算总数
        $scope.tableDetail_01($scope.item_01);
    }

    $scope.saveRecordDetail = function () {
        var url = "/dataMerge/getCsv";
        var params = {
            tableName: $scope.item_01.tableName,
            fileTemlateId: $scope.item_01.fileTemplateDto.id,
            caseName: $scope.caseName_div,
            templateName: $scope.item_01.fileTemplateDto.templateName,
            checkFields: checkFields
        }
        $http({
            url: url,
            data: params,
            method: 'POST',
            responseType: 'arraybuffer'
        }).success(function (data) {
            var blob = new Blob([data], {type: 'text/csv,charset=UTF-8'});
            //var blob = new Blob([data], {type: "application/x-zip-compressed"});
            var objectUrl = URL.createObjectURL(blob);
            //var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href",objectUrl);
            //$("body").append(aForExcel);
            //$(".forExcel").click();
            //aForExcel.remove();
            var fileName = $scope.caseName_record + '_' + $scope.templateName_record;
            var link = document.createElement('a');
            link.download = fileName + ".csv"; //文件名字
            link.href = objectUrl;
            link.click();
        });
    }

    $scope.mergeExport = function (caseId) {
        var url = "/dataMerge/mergeExport?caseId=" + caseId;
        $http({
            url: url,
            method: 'GET',
            responseType: 'arraybuffer'
        }).success(function (data) {
            var blob = new Blob([data], {type: "application/x-zip-compressed"});
            var objectUrl = URL.createObjectURL(blob);
            var aForZip = $("<a><span class='forExcel'>下载zip</span></a>").attr("href", objectUrl);
            $("body").append(aForZip);
            $(".forExcel").click();
            aForZip.remove();
        });
    }

    $scope.doUserDefinedRinse = function (id) {
        var url = "/dataMerge/doUserDefinedRinse?caseId=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
});

App.controller("abnormalTradingController", function ($http, $timeout, $scope,$state,$rootScope, myservice) {

    //登录和锁定校验
    myservice.loginLockCheck();

    $scope.caseId='';

    $scope.data = {
        title:'案件信息',
        menus:[
            {
                title: '',

            },
            {
                title: '',
            },
            {
                title: '',
            },
            {
                title: '异常交易行为统计',
                id: '0100',

                menus: [
                    {
                        title: '大额交易',
                        id: '0101',
                    },
                    {
                        title: '集中转入点',
                        id: '0102',
                    },
                    {
                        title: '集中转出点',
                        id: '0103',
                    },
                    {
                        title: '快进快出交易点',
                        id: '0104',
                    },
                    {
                        title: '只进不出或只出不进',
                        id: '0105',
                    }
                ]
            }, {
                title: '',
            },
            {
                title: '',
            },
        ]
    }
    for(var i = 0;i<$scope.data.menus.length;i++){
        if($scope.data.menus[i].title){
            $scope.data.menus[i].click=function(e,data){
                //console.log('1:'+data);
            }
            for(var j = 0;j<$scope.data.menus[i].menus.length;j++){
                if($scope.data.menus[i].menus[j].title){
                    $scope.data.menus[i].menus[j].click=function(e,data){
                        if('0101'==data.id){
                            $scope.blockTrade($scope.caseId);
                        }
                        if('0102'==data.id){
                            $scope.rollInPoint($scope.caseId);
                        }
                        if('0103'==data.id){
                            $scope.rollOutPoint($scope.caseId);
                        }
                        if('0104'==data.id){
                            $scope.inOut($scope.caseId);
                        }
                        if('0105'==data.id){
                            $scope.inOrOutOnly($scope.caseId);
                        }
                    }
                }
            }
        }
    }
    var ele = document.querySelector('#circle-menu1');
    var cmenu = CMenu(ele)
        .config({
            hideAfterClick:false,
            totalAngle: 360,//deg,
            spaceDeg: 1,//deg
            background: "#003FC7",
            backgroundHover: "#189DE3",
            //pageBackground: "#52be7f",
            percent: 0.32,//%
            diameter: 300,//px
            // position: 'top',
            // horizontal: true,
            //start: -45,//deg
            menus: $scope.data.menus
        });
    cmenu.hide();
    $('.cn-menu-title').html($scope.data.title);

    $scope.query = function () {
        //根据案件编号和案件名称，查询案件
        var params = {
            caseNo: $scope.caseNo,
            caseName: $scope.caseName
        }
        var url = "/fileCase/getCasesByCaseIdOrCaseName";
        $http.post(url,params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);

            if(temp.obj){
                cmenu.show();
                $rootScope.caseNo_abnormalTrading = $scope.caseNo;
            }else{
                cmenu.hide();
                return;
            }

            $('.cn-menu-title').html(temp.obj.caseName);
            $scope.caseId = temp.obj.id;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    if(!$scope.caseNo && $rootScope.caseNo_abnormalTrading){
        $scope.caseNo = $rootScope.caseNo_abnormalTrading
        $scope.query();
    }

    /*
    $scope.query = function () {
        var url = "/fileCase/queryFileCaseEntityListHasTable";

        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    */

    //大额交易
    $scope.blockTrade = function (id) {

        $state.go('app.blockTrade', {id: id});
    }

    //集中转入点
    $scope.rollInPoint = function (id) {

        $state.go('app.rollInPoint', {id: id});
    }

    //集中转出点
    $scope.rollOutPoint = function (id) {

        $state.go('app.rollOutPoint', {id: id});
    }

    //快进快出交易点
    $scope.inOut = function (id) {

        $state.go('app.inOut', {id: id});
    }

    //只进不出或只出不进
    $scope.inOrOutOnly = function (id) {

        $state.go('app.inOrOutOnly', {id: id});

    }

});

App.controller("inOrOutOnlyController", function ($http, $timeout, $scope, $stateParams, $state, myservice) {

    $("#pleaseWait1").hide();
    $("#pleaseWait2").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    myservice.dragFunc("cnDivDetail");
    $("#cnDivDetail").hide();

    //案件编号$stateParams.id

    //通过案件编号查询 账户信息
    $scope.query = function () {
        $("#pleaseWait1").show();
        $("#pleaseWait2").show();
        var url = "/statistics/getInOrOutOnly?caseId=" + $stateParams.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;

            var obj1 = [];
            var obj2 = [];
            angular.forEach(temp.obj, function (item) {
                if (item.casePersonnelInfoInList.length > 0) {
                    obj1.push(item);
                }

                if (item.casePersonnelInfoOutList.length > 0) {
                    obj2.push(item);
                }
            });

            $scope.obj1 = myservice.setSerialNumber(obj1);
            $scope.obj2 = myservice.setSerialNumber(obj2);

            $("#pleaseWait1").hide();
            $("#pleaseWait2").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });
    }

    $scope.query();

    $scope.detail = function (casePersonnelInfoList) {
        $("#cnDivDetail").show();
        $scope.list = myservice.setSerialNumber(casePersonnelInfoList);
    }

    $scope.closeDetail = function (casePersonnelInfoList) {
        $("#cnDivDetail").hide();
    }

    $scope.return = function () {

        $state.go('app.abnormalTrading');

    }

});

App.controller("inOutController", function ($http, $timeout, $scope, $stateParams, $state, myservice) {

    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();

    myservice.dragFunc("cnDivDetail");
    $("#cnDivDetail").hide();

    //案件编号$stateParams.id

    //通过案件编号查询 账户信息
    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/statistics/getInOutTrading?caseId=" + $stateParams.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

        $scope.inOutDetail = function (inOutList) {
            $scope.inOutObj = myservice.setSerialNumber(inOutList);
            $("#cnDivDetail").show();
        }
    }

    // $scope.query();

    $scope.return = function () {

        $state.go('app.abnormalTrading');

    }

    //通过账号查询交易明细
    $scope.blockTradeDetail = function (cardId) {

        $("#cnDivDetail").show();
        var url = "/blockTrade/getBlockTradeDetailByAccountNo?caseId=" + $stateParams.id + "&threshold=" + $scope.threshold + "&cardId=" + cardId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.tradingObj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

    }

    $scope.closeDetail = function () {
        $("#cnDivDetail").hide();
    }

});

App.controller("blockTradeController", function ($http, $timeout, $scope, $stateParams, $state, myservice) {

    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();

    myservice.dragFunc("cnDivDetail");
    $("#cnDivDetail").hide();

    //案件编号$stateParams.id

    //通过案件编号查询 账户信息
    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/blockTrade/getPersonnelInfoByCaseId?caseId=" + $stateParams.id + "&threshold=" + $scope.threshold;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

    }

    // $scope.query();

    $scope.return = function () {

        $state.go('app.abnormalTrading');

    }

    //通过账号查询交易明细
    $scope.blockTradeDetail = function (cardId) {

        $("#cnDivDetail").show();
        var url = "/blockTrade/getBlockTradeDetailByAccountNo?caseId=" + $stateParams.id + "&threshold=" + $scope.threshold + "&cardId=" + cardId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.tradingObj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

    }

    $scope.closeDetail = function () {
        $("#cnDivDetail").hide();
    }

});

App.controller("rollInPointController", function ($http, $timeout, $scope, $stateParams, $state, myservice) {

    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();


    //案件编号$stateParams.id

    //通过案件编号查询 账户信息
    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/statistics/centrostigmaInAnalyse?caseId=" + $stateParams.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

    }

    $scope.query();

    $scope.return = function () {

        $state.go('app.abnormalTrading');

    }
});

App.controller("rollOutPointController", function ($http, $timeout, $scope, $stateParams, $state, myservice) {

    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();


    //案件编号$stateParams.id

    //通过案件编号查询 账户信息
    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/statistics/centrostigmaOutAnalyse?caseId=" + $stateParams.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
            $("#pleaseWait").hide();
        });

    }

    $scope.query();

    $scope.return = function () {

        $state.go('app.abnormalTrading');

    }
});


App.controller("fileRinseController", function ($http, $timeout, $scope,
                                                myservice) {
    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    myservice.dragFunc("cnDivGroupAdd");
    myservice.dragFunc("cnDivUpdate");
    myservice.dragFunc("cnDivDetail");
    myservice.dragFunc("cnDivDetailAdd");

    $("#cnDivGroupAdd").hide();
    $("#cnDivUpdate").hide();
    $("#cnDivDetail").hide();
    $("#cnDivDetailAdd").hide();
    $scope.query = function () {
        var url = "/fileRinse/getFileRinses";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.query();

    $scope.add = function () {
        $("#cnDivGroupAdd").show();
        $scope.rinseName = "";
        $scope.websocketUrl = "";
        $scope.comment = "";
    }

    $scope.close = function () {
        $("#cnDivGroupAdd").hide();
    }

    $scope.save = function () {
        if (myservice.isEmpty($scope.rinseNameAdd)) {
            alert("名称不能为空！");
            return;
        }

        var url = "/fileRinse/addFileRinse";
        var params = {
            fileRinseName: $scope.rinseNameAdd,
            comment: $scope.commentAdd,
            websocketUrl: $scope.websocketUrlAdd
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDivGroupAdd").hide();
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.update = function (item) {
        $("#cnDivUpdate").show();
        $scope.rinseId = item.id;
        $scope.rinseNameUpdate = item.fileRinseName;
        $scope.commentUpdate = item.comment;
        $scope.websocketUrlUpdate = item.websocketUrl;
    }

    $scope.closeUpdate = function () {
        $("#cnDivUpdate").hide();
    }

    $scope.updateSave = function () {
        if (myservice.isEmpty($scope.rinseNameUpdate)) {
            alert("名称不能为空！");
            return;
        }
        var url = "/fileRinse/modifyFileRinse";
        var params = {
            id: $scope.rinseId,
            fileRinseName: $scope.rinseNameUpdate,
            comment: $scope.commentUpdate,
            websocketUrl: $scope.websocketUrlUpdate
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDivUpdate").hide();
            $scope.query();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileRinse/deleteFileRinseGroup?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeAddDetail = function () {
        cnDivDetail
        $("#cnDivDetail").hide();
    }

    $scope.detail = function (id) {
        $scope.fileRinseGroupId = id;
        $("#cnDivDetail").show();
        var url = "/fileRinse/getFileRinseDetailsByGroupId?groupId=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.details = myservice.setSerialNumber(temp.obj)
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.template = {};
    $scope.addDetail = function () {
        $("#cnDivDetailAdd").show();
        $scope.rinseFieldTypeAdd = "";
        $scope.regularAdd = "";
        $scope.commentAdd = "";
        $scope.template.categories1 = [];
        $scope.template.categories2 = [];
    }

    $scope.rinseFieldAddClose = function () {
        $("#cnDivDetailAdd").hide();
    }

    $scope.detailAdd = function () {
        if (myservice.isEmpty($scope.rinseFieldTypeAdd)) {
            alert("名称不能为空！");
            return;
        }
        var url = "/fileRinse/addFileRinseDetail";
        var params = {
            fileRinseGroupId: $scope.fileRinseGroupId,
            typeName: $scope.rinseFieldTypeAdd,
            comment: $scope.commentAdd
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDivDetailAdd").hide();
            $scope.detail($scope.fileRinseGroupId);
            angular.forEach($scope.template.categories1, function (e) {
                $scope.saveRinseRegular(temp.obj, e.substring(e.lastIndexOf("::") + 2), "1");
            });

            angular.forEach($scope.template.categories2, function (e) {
                $scope.saveRinseRegular(temp.obj, e.substring(e.lastIndexOf("::") + 2), "2");
            });
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.saveRinseRegular = function (rinseDetailId, regularId, type) {
        var url = "/fileRinse/saveFileRinseRegular";
        var params = {
            fileRinseDetailId: rinseDetailId,
            regularDetailId: regularId,
            type: type
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    //获取正则列表
    var availableCategories = [];
    $scope.getRegular = function () {
        var url = "/regular/getRegularDetailsByUserId";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            angular.forEach(temp.obj, function (e) {
                availableCategories.push(e);
            })
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.getRegular();
    $scope.availableCategories = availableCategories;

    $scope.deleteOneDetail = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileRinse/deleteFileRinse?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.detail($scope.fileRinseGroupId);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

})

App.controller("regularGroupController", function ($http, $timeout, $scope, $localStorage,
                                                   myservice) {
    var msg = "您真的确定要删除吗？\n\n请确认！";
    $("#pleaseWait").hide();
    $scope.userId = $localStorage.userId;
    //登录和锁定校验
    myservice.loginLockCheck();
    myservice.dragFunc("cnDivGroupAdd");
    myservice.dragFunc("cnDivDetailList");
    myservice.dragFunc("cnDivDetailAdd");
    myservice.dragFunc("cnDivGroupUpdate");
    myservice.dragFunc("cnDivDetailUpdate");
    $("#cnDivGroupAdd").hide();
    $("#cnDivDetailList").hide();
    $("#cnDivDetailAdd").hide();
    $("#cnDivGroupUpdate").hide();
    $("#cnDivDetailUpdate").hide();

    $scope.queryGroup = function () {
        var url = "/regular/getGroups";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj)
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.queryGroup();
    $scope.addGroup = function () {
        $scope.groupNameAdd = "";
        $scope.groupCommentAdd = "";
        $scope.commentAdd = "";
        $scope.groupSort = "";
        $("#cnDivGroupAdd").show();
    }

    $scope.closeGroupAdd = function () {
        $("#cnDivGroupAdd").hide();
    }

    $scope.saveGroup = function () {

        var reg = /^\d+$/;
        if (!reg.test($scope.groupSort)) {
            alert("排序必须为数字！");
            return;
        }

        var url = "/regular/groupAdd";
        var params = {
            groupName: $scope.groupNameAdd,
            comment: $scope.groupCommentAdd,
            sort: $scope.groupSort
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.queryGroup();
            $("#cnDivGroupAdd").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.updateGroup = function (item) {
        $("#cnDivGroupUpdate").show();
        $scope.groupNameUpdate = item.groupName;
        $scope.groupCommentUpdate = item.comment;
        $scope.groupSortUpdate = item.sort;
        $scope.groupId = item.id;
    }

    $scope.closeGroupUpdate = function () {
        $("#cnDivGroupUpdate").hide();
    }

    $scope.updateGroupSave = function () {
        var reg = /^\d+$/;
        if (!reg.test($scope.groupSortUpdate)) {
            alert("排序必须为数字！");
            return;
        }
        if (myservice.isEmpty($scope.groupNameUpdate)) {
            alert("名称不能为空！");
            return;
        }

        var url = "/regular/updateGroupByPrimaryKey";
        var params = {
            groupName: $scope.groupNameUpdate,
            comment: $scope.groupCommentUpdate,
            sort: $scope.groupSortUpdate,
            id: $scope.groupId
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDivGroupUpdate").hide();
            $scope.queryGroup();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.detail = function (item) {
        $("#cnDivDetailList").show();
        $scope.groupId = item.id;
        $scope.queryDetail();
    }

    $scope.closeDetailList = function () {
        $("#cnDivDetailList").hide();
    }


    $scope.checkSort = function (sort, field) {
        var reg = /^\d+$/;
        if (!reg.test(sort)) {
            alert("排序必须为数字！");
            $scope[field] = "";
            return;
        }
    }

    $scope.deleteGroup = function (id) {

        if (confirm(msg) != true) {
            return;
        }
        var url = "/regular/groupRemoveByPrimaryKey?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.queryGroup();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.addDetail = function () {
        $("#cnDivDetailAdd").show();
        $scope.regularName = "";
        $scope.format = "";
        $scope.regularValue = "";
        $scope.sort = "";
        $scope.comment = "";
    }


    $scope.queryDetail = function () {
        var url = "/regular/getRegularDetailsByGroupId?regularGroupId=" + $scope.groupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.details = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.saveDetail = function () {
        if (myservice.isEmpty($scope.regularName)) {
            alert("名称不能为空！");
            return;
        }
        if (myservice.isEmpty($scope.regularValue)) {
            alert("表达式不能为空！");
            return;
        }
        if (myservice.isEmpty($scope.sort)) {
            alert("排序不能为空！");
            return;
        }
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test($scope.sort)) {
            alert("排序为1-3位数字！");
            return;
        }
        var url = "/regular/addRegularDetails";
        var params = {
            regularName: $scope.regularName,
            format: $scope.format,
            regularValue: $scope.regularValue,
            sort: $scope.sort,
            comment: $scope.comment,
            regularGroupId: $scope.groupId
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.queryDetail();
            $("#cnDivDetailAdd").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeDetailAdd = function () {
        $("#cnDivDetailAdd").hide();
    }

    $scope.updateDetail = function (item) {
        $("#cnDivDetailUpdate").show();
        $scope.updateDetailId = item.id;
        $scope.regularNameUpdate = item.regularName;
        $scope.formatUpdate = item.format;
        $scope.regularValueUpdate = item.regularValue;
        $scope.sortUpdate = item.sort;
        $scope.commentUpdate = item.comment;
    }

    $scope.updateDetailSave = function () {
        if (myservice.isEmpty($scope.regularNameUpdate)) {
            alert("名称不能为空！");
            return;
        }
        if (myservice.isEmpty($scope.regularValueUpdate)) {
            alert("正则表达式不能为空！");
            return;
        }
        if (myservice.isEmpty($scope.sortUpdate)) {
            alert("排序不能为空！");
            return;
        }
        var url = "/regular/modifyRegularDetails";
        var params = {
            id: $scope.updateDetailId,
            regularName: $scope.regularNameUpdate,
            format: $scope.formatUpdate,
            regularValue: $scope.regularValueUpdate,
            sort: $scope.sortUpdate,
            comment: $scope.commentUpdate,
            regularGroupId: $scope.groupId
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.queryDetail();
            $("#cnDivDetailUpdate").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeDetailUpdate = function () {
        $("#cnDivDetailUpdate").hide();
    }

    $scope.deleteOneDetail = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/regular/removeRegularDetailByPrimaryKey?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.queryDetail();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
})


App.controller("neo4jRelationController", function ($http, FileUploader, $timeout, $scope,
                                                    myservice, ngDraggable) {
    $("#cnDiv").hide();
    var vm = this;

    // myservice.dragFunc("cnDiv");

    $scope.initTree = function () {

        var url = "/neo4jRelation/getNeo4jAttribute";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            vm.tree = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.initTree();


    $scope.onDropComplete = function (flag, obj, $event) {
        if (flag == 1) {
            $scope.itemSource.name = obj.name;
            $scope.itemSource.id = obj.id;
        }
        if (flag == 2) {
            $scope.itemTarget.name = obj.name;
            $scope.itemTarget.id = obj.id;
        }
    }

    $scope.add = function () {
        $("#cnDiv").show();
    }


    loadDictionaryColor = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "neo4j_relation_color";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesColor = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionaryShape = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "neo4j_relation_shape";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesShape = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $(function () {
        loadDictionaryColor();
        loadDictionaryShape();
    })

    $scope.close = function () {
        /**
         if($scope.uploader.queue.length > 0){
            angular.forEach($scope.uploader.queue, function(item) {
                item.remove();
            });
        }*/
        $("#cnDiv").hide();
        $scope.relationship = "";
        $scope.itemSource.name = "";
        $scope.itemTarget.name = "";
        $scope.selectedOptionsColor = "";
        $scope.selectedOptionsShape = "";
        $scope.program = "";
    }

    $scope.queryRelation = function () {
        var url = "/neo4jRelation/getRelations";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.queryRelation();
    $scope.save = function () {
        var url = "/neo4jRelation/saveRelation";
        var params = {
            relationship: $scope.relationship,
            differentiate: $scope.differentiate,
            sourceAttributeId: $scope.itemSource.id,
            targetAttributeId: $scope.itemTarget.id,
            color: $scope.selectedOptionsColor,
            shape: $scope.selectedOptionsShape,
            program: $scope.program
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.close();
            $scope.queryRelation();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.initRelation = function (item) {

        var url = "/neo4jRelation/initRelation";

        $http.post(url, item).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
})

App.controller("neo4jInitDataController", function ($http, $timeout, $scope,
                                                    myservice, $websocket) {
    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    // myservice.dragFunc("cnDiv");
    myservice.dragFunc("cnDivAttribute");
    myservice.dragFunc("cnDivLabel");
    $("#cnDivAttribute").hide();
    $("#cnDiv").hide();
    $("#cnDivLabel").hide();
    $scope.query = function () {
        var url = "/neo4jInitData/getFileTables";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.query();

    loadDictionarySize = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "neo4j_node_size";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesSize = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionaryColor = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "neo4j_node_color";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesColor = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionaryShape = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "neo4j_node_shape";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesShape = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $(function () {
        loadDictionarySize();
        loadDictionaryColor();
        loadDictionaryShape();
    })

    $scope.close = function () {
        $("#cnDiv").hide();
    }
    $scope.closeAttribute = function () {
        $("#cnDivAttribute").hide();
    }
    $scope.initData = function (item) {
        var url = "/neo4jInitData/initData";
        $http.post(url, item).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.attribute = function (item) {
        $("#cnDivAttribute").show();
        $scope.fileTableId = item.id;
        $scope.getAttribute();
        //获取表字段
        url = "/fileTemplate/getFileTemplateDetails";
        var params = {
            templateId: item.fileTemplateId
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sitesContent = temp.obj;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.getAttribute = function () {
        var url = "/neo4jInitData/getAttribute?fileTableId=" + $scope.fileTableId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.attributes = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.addAttribute = function () {
        $("#cnDiv").show();
        $scope.save = true;
        $scope.update = false;
        $scope.selectedOptionsWidth = '';
        $scope.selectedOptionsHeight = '';
        $scope.selectedOptionsColor = '';
        $scope.selectedOptionsShape = '';
        $scope.attributeName = '';
    }

    $scope.updateAttribute = function (id) {
        $("#cnDiv").show();
        $scope.AttributeId = id;
        $scope.save = false;
        $scope.update = true;
        $scope.selectedOptionsWidth = '';
        $scope.selectedOptionsHeight = '';
        $scope.selectedOptionsColor = '';
        $scope.selectedOptionsShape = '';
        $scope.attributeName = '';
    }

    $scope.attributeUpdate = function (id) {
        var url = "/neo4jInitData/attributeUpdate"
        var params = {
            id: $scope.AttributeId,
            fileTableId: $scope.fileTableId,
            attributeName: $scope.attributeName,
            width: $scope.selectedOptionsWidth,
            height: $scope.selectedOptionsHeight,
            color: $scope.selectedOptionsColor,
            shape: $scope.selectedOptionsShape,
            content: $scope.selectedOptionsContent
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getAttribute();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDiv").hide();

    }

    $scope.deleteAttribute = function (id) {
        var url = "/neo4jInitData/attributeDelete?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getAttribute();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.attributeSave = function () {
        var url = "/neo4jInitData/attributeSave";
        if (myservice.isEmpty($scope.attributeName)) {
            alert("样式名称为必填项！");
            return;
        }
        if (myservice.isEmpty($scope.selectedOptionsWidth)) {
            alert("宽度为必填项！");
            return;
        }
        if (myservice.isEmpty($scope.selectedOptionsHeight)) {
            alert("高度为必填项！");
            return;
        }
        if (myservice.isEmpty($scope.selectedOptionsShape)) {
            alert("形状为必填项！");
            return;
        }
        if (myservice.isEmpty($scope.selectedOptionsContent)) {
            alert("显示名称为必填项！");
            return;
        }
        var params = {
            fileTableId: $scope.fileTableId,
            attributeName: $scope.attributeName,
            width: $scope.selectedOptionsWidth,
            height: $scope.selectedOptionsHeight,
            color: $scope.selectedOptionsColor,
            shape: $scope.selectedOptionsShape,
            content: $scope.selectedOptionsContent
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getAttribute();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDiv").hide();

    }

    $scope.updateLable = function (item) {
        $("#cnDivLabel").show();

        //获取标签
        $scope.label = item.label;

        $scope.item4Label = item;
    }

    $scope.closeLabel = function () {
        $scope.label = "";
        $("#cnDivLabel").hide();
    }

    $scope.saveLabel = function () {
        var url = "/neo4jInitData/modifyNodeInfo";
        var params = {
            fileTableId: $scope.item4Label.fileTableId,
            label: $scope.label
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $scope.closeLabel();
    }

    $scope.setProgress = function (id, value) {
        angular.forEach(
            $scope.obj, function (item) {
                if (item.id == id) {
                    item.progress = value;
                }
            }
        );
    }


    var websocket = null;
    //判断当前浏览器是否支持WebSocket, 主要此处要更换为自己的地址
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8090/websocket/one");
    } else {
        alert('Not support websocket')
    }
});

App.controller("CytoscapeCtrl2", function ($http, $timeout, $scope, $state, $rootScope) {

})

App.controller("Neo4jModelOneController", function ($http, $timeout, $scope, $state, $rootScope, myservice, $q) {

    //登录和锁定校验
    myservice.loginLockCheck();

    myservice.dragFunc("cnDivNeo");

    $("#cnDivNeo").hide();
    $("#pleaseWait").hide();
    //获取所有的图谱信息

    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/neo4jModelOne/getAllNeo4jInfos";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.query();

    $scope.cnDivNeoClose = function () {
        $("#cnDivNeo").hide();
    }

    $scope.selectNeo4jRelation = function (relation) {
        $("#cnDivNeo").show();
        var url = "/neo4jRelation/getNeo4jRelations?relationship=" + relation.relationship + "&relationId=" + relation.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            $scope.eles = temp.obj;
            var cy = window.cy = cytoscape({
                container: document.getElementById('cy'),
                boxSelectionEnabled: true,
                style: [
                    {
                        selector: 'node[type="source"]',
                        style: {
                            'background-color': 'data(sourceNodeColor)',
                            'text-valign': 'center',
                            'content': 'data(sourceNodeContent)',
                            "height": 'data(sourceNodeHeight)',
                            "width": 'data(sourceNodeWidth)',
                            'shape': 'data(sourceNodeShape)'
                        }
                    },
                    {
                        selector: 'node[type="target"]',
                        style: {
                            'background-color': 'data(targetNodeColor)',
                            'text-valign': 'center',
                            'content': 'data(targetNodeContent)',
                            "height": 'data(targetNodeHeight)',
                            "width": 'data(targetNodeWidth)',
                            'shape': 'data(targetNodeShape)'
                        }
                    },
                    {
                        selector: 'edge',
                        style: {
                            'content': 'data(relationship)',
                            'curve-style': 'bezier',
                            'target-arrow-shape': 'data(shape)',
                            'target-arrow-color': 'data(color)',
                            'line-color': '#ccc',
                            'width': 1
                        }
                    }
                ],
                elements: $scope.eles,
                layout: {
                    name: 'circle',
                    fit: true,
                    //name: 'grid',
                    padding: 5
                }
            })
        })
    }


});

App.controller("declarationController", function ($http, $timeout, $scope, $state,
                                                  myservice) {
    //登录和锁定校验
    myservice.loginLockCheck();
    $scope.query = function () {
        var url = "/declar/getDeclarInfo";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.query();

});
App.controller("businessCheckController", function ($http, $timeout, $scope, $state,
                                                    myservice) {
    //登录和锁定校验
    myservice.loginLockCheck();
    $("#pleaseWait").hide();
    $scope.query = function () {
        var url = "/declar/getAllDeclarDetail?declarType=" + $scope.selectedOptions1 + "&declarStatus=" + $scope.selectedOptions2;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.query();

    //已选择的文件
    $scope.selected = [];

    //label:要赋值的标签，dic_group：字典组名称
    loadDictionary1 = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "declarType";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    loadDictionary2 = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "declarStatus";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites2 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionary1();
    loadDictionary2();

    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.id);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.id), 1);
        }
    }

    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                $scope.selected.push(e.id);
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.id), 1);
            })
        }
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/declar/deleteDeclarDetail?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.failedOne = function (id) {
        var url = "/declar/failedDeclar?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.approveOne = function (id) {
        var url = "/declar/approveDeclar?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.deleteSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/declar/deleteDeclarDetail?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.failedSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/declar/failedDeclar?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.approveSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/declar/approveDeclar?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
});
App.controller('declarDetailController', ['$http', '$scope', '$stateParams', 'myservice', function ($http, $scope, $stateParams, myservice) {
    $scope.status = $stateParams.status === 'inbox' ? '' : $stateParams.status;
    $scope.declarName = $stateParams.declarName === 'inbox' ? '' : $stateParams.declarName;

    $scope.query = function () {
        var url = "/declar/getDeclarDetail?status=" + $scope.status;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.query();

}]);

App.controller("infoSearchController", function ($http, $timeout, $scope, $rootScope, $state,
                                                 myservice) {
    //登录和锁定校验
    myservice.loginLockCheck();

    $scope.itemsPerPage = 5;
    $scope.offset = 0;

    $scope.pageConfig = {
        pageSize: 5,    //每页条数（不设置时，默认为10）
        pageIndex: 1,    //当前页码
        totalCount: 0,   //总记录数
        totalPage: 0,     //总页码
        prevPage: '< 上一页',     //上一页（不设置时，默认为：<）
        nextPage: '下一页 >',     //下一页（不设置时，默认为：>）
        firstPage: '<< 首页',     //首页（不设置时，默认为：<<）
        lastPage: '末页 >>',     //末页（不设置时，默认为：>>）
        degeCount: 3,             //当前页前后两边可显示的页码个数（不设置时，默认为3）
        isShowEllipsis: true    //是否显示省略号不可点击按钮（true：显示，false：不显示）
    }

    $scope.changePage = function (pageNo) {
        $scope.offset = (pageNo - 1) * $scope.itemsPerPage;
        //$scope.getTableRecord($scope.tableInfo);
        $scope.queryDetail($scope.offset,$scope.itemsPerPage);
        console.log('offset=' + $scope.offset + 'itemsPerPage=' + $scope.itemsPerPage);
    }

    //监听分页组件中的分页点击事件
    $scope.$on("clickPage", function (e, m) {
        $scope.pageIndex = $scope.pageConfig.pageIndex;
        $scope.itemsPerPage = $scope.pageConfig.pageSize;
        $scope.changePage($scope.pageIndex);
        console.log('pageSize=' + $scope.pageConfig.pageSize + 'pageIndex=' + $scope.pageIndex);
    })

    $scope.query = function () {
        if(!$scope.pageConfig.totalCount){//获取总数据量
           var urlCount = "/search/searchAllTablesTotalCount?condition=" + $rootScope.app.searchKey;

            $http.post(urlCount).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.pageConfig.totalCount = temp.obj;
                $scope.pageConfig.totalPage = Math.ceil($scope.pageConfig.totalCount / $scope.pageConfig.pageSize); //总页数
                console.log('总记录数：' + $scope.pageConfig.totalCount + '； 总页数：' + $scope.pageConfig.totalPage + '；当前页码：' + $scope.pageConfig.pageIndex);
                $scope.$broadcast("initPage")   //调用分页组件里的初始化页码函数

                $scope.queryDetail(0,$scope.itemsPerPage);
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        }
    }

    //offset:偏移量；perSize:每页条数
    $scope.queryDetail = function(offset,perSize){
        var url = "/search/searchAllTables?condition=" + $rootScope.app.searchKey+"&offset="+offset+"&perSize="+perSize;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.query();


});

App.controller("fileImportCountController", function ($http, $timeout, $scope, $state, FileUploader,
                                                      myservice) {
    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();

    myservice.dragFunc("importDetailDiv");
    myservice.dragFunc("importFailedFileDiv");
    myservice.dragFunc("importFailedDetailDiv");
    myservice.dragFunc("fileUploadDiv");

    $("#importDetailDiv").hide();
    $("#importFailedFileDiv").hide();
    $("#importFailedDetailDiv").hide();
    $("#fileUploadDiv").hide();

    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/fileImportCount/getImportCount";
        var params = {
            caseNo: $scope.caseNo,
            caseName: $scope.caseName
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
            $("#pleaseWait").hide();
            $scope.kedian = true;
            //$scope.obj =  myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.query();

    $scope.showFileDetail = function (arrs) {
        $("#importDetailDiv").show();
        $scope.details = arrs;
    }

    $scope.showFileDetailDtoFailed = function (arrs) {
        $("#importFailedFileDiv").show();
        $scope.failedFiles = arrs;
    }

    $scope.closeImportDetail = function () {
        $("#importDetailDiv").hide();
    }

    $scope.closeImportFailedFile = function () {
        $("#importFailedFileDiv").hide();
    }

    $scope.showFileDetailDtoFailed = function (arrs) {
        $("#importFailedFileDiv").show();
        $scope.failedFiles = arrs;
    }

    $scope.closeImportFailedDetail = function () {
        $("#importFailedDetailDiv").hide();
    }

    $scope.errorDetail = function (id) {
        $("#importFailedDetailDiv").show();

        var url = "/fileImportCount/getFailedDetail?fileDetailId=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.failedDetails = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.export = function (item) {
        var url = "/fileImportCount/getExcel?fileDetailId=" + item.id;
        var fileName = item.fileName.substring(0, item.fileName.lastIndexOf(".")) + "_校验不通过";
        $http({
            url: url,
            responseType: 'arraybuffer'
        }).success(function (data) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            //var blob = new Blob([data], {type: "application/x-zip-compressed"});
            var objectUrl = URL.createObjectURL(blob);
            //var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href",objectUrl);
            //$("body").append(aForExcel);
            //$(".forExcel").click();
            //aForExcel.remove();
            var link = document.createElement('a');
            link.download = fileName + ".xls"; //文件名字
            link.href = objectUrl;
            link.click();
        });
    }

    $scope.importFile = function (fileDetailId) {

        $("#fileUploadDiv").show();
        $scope.fileDetailId = fileDetailId;
        if ($scope.uploader.queue.length > 0) {
            angular.forEach($scope.uploader.queue, function (item) {
                item.remove();
            });
        }
    }

    $scope.closeUploadFile = function () {
        $("#fileUploadDiv").hide();
    }


    var uploader = $scope.uploader = new FileUploader({
        url: '/fileImportCount/dataAmendment'
    });

    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function (item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 1;
        }
    });

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function (fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function (addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function (item) {
        item.url += "?fileDetailId=" + $scope.fileDetailId;
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function (fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function (progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        $("#importDetailDiv").hide();
        $("#fileUploadDiv").hide();
        $scope.query();
        myservice.errors(response);
        //console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function (fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function (fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function (fileItem, response, status, headers) {
        //console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function () {
        console.info('onCompleteAll');
    };

});

App.controller("dataGroupController", function ($http, $timeout, $scope, $state,
                                                myservice) {
    myservice.loginLockCheck();

    $scope.active = {
        save: "0",
        update: "1"
    }

    //获取所有的数据组信息
    $scope.getDataGroup = function () {
        var url = "/fileDataGroup/getFileDataGroup";
        $http.post(url, {}).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getDataGroup();

    $scope.dataGroupOnClick = function (item) {
        $scope.item = item;
        $scope.id = item.id;
        $scope.hasClick = true;
    }

    $scope.removeDataGroup = function () {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileDataGroup/deleteFileDataGroup?id=" + $scope.id;
        myservice.doPost(url);
        $state.go('app.dataAuthority', {}, {reload: true});
    }
});

App.controller('dataGroupUsersController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.dataGroupId = $stateParams.dataGroupId === 'inbox' ? '' : $stateParams.dataGroupId;
    $scope.selected_01 = [];
    $scope.selected_02 = [];
    var getUsers_1 = function () {
        var url = "/fileDataGroupDetail/findUsersNotInDataGroupsByUserId?groupId=" + $scope.dataGroupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.users_01 = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    var getUsers_2 = function () {
        var url = "/fileDataGroupDetail/findFileDataGroupUsersByGroupId?groupId=" + $scope.dataGroupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.users_02 = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $(function () {
        getUsers_1();
        getUsers_2();
    })

    //单个勾选去勾选
    $scope.selectOne_01 = function (item) {
        if (item.checked) {
            $scope.selected_01.push(item.id);
        } else {
            $scope.selected_01.splice($scope.selected_01.indexOf(item.id), 1);
        }
    }

    //单个勾选去勾选
    $scope.selectOne_02 = function (item) {
        if (item.checked) {
            $scope.selected_02.push(item.id);
        } else {
            $scope.selected_02.splice($scope.selected_02.indexOf(item.id), 1);
        }
    }
    //全选全去勾选
    $scope.selectAll_01 = function (checked_01) {
        $scope.selected_01 = [];
        if (checked_01) {
            angular.forEach($scope.users_01, function (e) {
                e.checked = true;
                $scope.selected_01.push(e.id);
            })
        } else {
            angular.forEach($scope.users_01, function (e) {
                e.checked = false;
                $scope.selected_01.splice($scope.selected_01.indexOf(e.id), 1);
            })
        }
    }

    //全选全去勾选
    $scope.selectAll_02 = function (checked_02) {
        $scope.selected_02 = [];
        if (checked_02) {
            angular.forEach($scope.users_02, function (e) {
                e.checked = true;
                $scope.selected_02.push(e.id);
            })
        } else {
            angular.forEach($scope.users_02, function (e) {
                e.checked = false;
                $scope.selected_02.splice($scope.selected_02.indexOf(e.id), 1);
            })
        }
    }

    function DataGroupDetailDto(userId, dataGroupId) {
        var o = new Object();
        o.userId = userId;
        o.fileDataGroupId = dataGroupId;
        return o;
    }


    $scope.addUserToDataGroup = function (userId) {

        var url = "/fileDataGroupDetail/saveUserFromDataGroupDetail";
        dataGroupDetailDtoArr = [];
        dataGroupDetailDtoArr.push(DataGroupDetailDto(userId, $scope.dataGroupId));
        $http.post(url, JSON.stringify(dataGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }


    $scope.addToDataGroupBatch = function () {
        if (myservice.isEmpty($scope.selected_01)) {
            return;
        }
        dataGroupDetailDtoArr = [];
        angular.forEach($scope.selected_01, function (e) {
            dataGroupDetailDtoArr.push(DataGroupDetailDto(e, $scope.dataGroupId));
        })
        var url = "/fileDataGroupDetail/saveUserFromDataGroupDetail";
        $http.post(url, JSON.stringify(dataGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.selected_01 = [];
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

    }

    $scope.deleteDataGroup = function (userId) {
        var url = "/fileDataGroupDetail/deleteUserFromDataGroup";
        dataGroupDetailDtoArr = [];
        dataGroupDetailDtoArr.push(DataGroupDetailDto(userId, $scope.dataGroupId));
        $http.post(url, JSON.stringify(dataGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }


    $scope.deleteUserFromDataGroupBatch = function () {
        if (myservice.isEmpty($scope.selected_02)) {
            return;
        }
        dataGroupDetailDtoArr = [];
        angular.forEach($scope.selected_02, function (e) {
            dataGroupDetailDtoArr.push(DataGroupDetailDto(e, $scope.dataGroupId));
        })

        var url = "/fileDataGroupDetail/deleteUserFromDataGroup";
        $http.post(url, JSON.stringify(dataGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.selected_02 = [];
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

}]);

App.controller('dataGroupAddOrModifyController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.active = $stateParams.active === 'inbox' ? '' : $stateParams.active;
    $scope.id = $stateParams.id === 'inbox' ? '' : $stateParams.id;
    if ($scope.active == "0") {
        $scope.showSave = true;
        $scope.showUpdate = false;
    } else if ($scope.active == "1") {
        $scope.showSave = false;
        $scope.showUpdate = true;

        var url = "/fileDataGroup/getFileDataGroup";
        var params = {
            id: $scope.id
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.groupName = temp.obj[0].groupName;
            $scope.description = temp.obj[0].description;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });


    }
    $scope.cancel = function () {
        $state.go('app.dataAuthority', {}, {reload: true});
    }
    $scope.save = function () {
        if (myservice.isEmpty($scope.groupName)) {
            alert("请填写用户组名称！")
            return;
        }
        var url = "/fileDataGroup/fileDataGroupSave";
        var params = {
            groupName: $scope.groupName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.dataAuthority', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.update = function () {
        if (myservice.isEmpty($scope.groupName)) {
            alert("请填写角色名称！")
            return;
        }
        var url = "/fileDataGroup/updateFileDataGroup";
        var params = {
            id: $scope.id,
            groupName: $scope.groupName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.dataAuthority', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

}]);

App.controller('dataGroupTableController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.groupId = $stateParams.groupId === 'inbox' ? '' : $stateParams.groupId;


    var vm = this;

    $scope.initTree = function () {

        var url = "/fileDataGroupTable/getFileDataGroupTable?dataGroupId=" + $scope.groupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            vm.tree = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.initTree();

    $scope.deleteUrl_1 = "/fileDataGroupTable/deleteFileDataGroupTables";
    $scope.addUrl_1 = "/fileDataGroupTable/saveFileDataGroupTables";
    $scope.param_1 = $scope.groupId;


}]);

App.controller("userRoleController", function ($http, $timeout, $scope, $state,
                                               myservice) {
    myservice.loginLockCheck();

    $scope.active = {
        save: "0",
        update: "1"
    }

    $scope.userRoleOnClick = function (item) {
        $scope.id = item.id;
        $scope.hasClick = true;
    }

    //获取用户组列表
    $scope.getUserRole = function () {
        var url = "/userRole/getUserRole";
        $http.post(url, {}).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.getUserRole();

    $scope.removeUserRole = function () {
        if (id == 1) {
            alert("管理员角色无法删除！");
            return;
        }
        if (id == 2) {
            alert("经侦角色无法删除！");
            return;
        }
        if (myservice.isEmpty($scope.id)) {
            return;
        }
        var url = "/userRole/removeUserRole?id=" + $scope.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.userRole', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

    }

});

App.controller('userRoleAddOrModifyController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.active = $stateParams.active === 'inbox' ? '' : $stateParams.active;
    $scope.id = $stateParams.id === 'inbox' ? '' : $stateParams.id;
    if ($scope.active == "0") {
        $scope.showSave = true;
        $scope.showUpdate = false;
    } else if ($scope.active == "1") {
        $scope.showSave = false;
        $scope.showUpdate = true;
        var url = "/userRole/getUserRole";
        var params = {
            id: $scope.id
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.roleName = temp.obj[0].roleName;
            $scope.description = temp.obj[0].description;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.save = function () {
        if (myservice.isEmpty($scope.roleName)) {
            alert("请填写用户组名称！")
            return;
        }
        var url = "/userRole/saveUserRole";
        var params = {
            roleName: $scope.roleName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.userRole', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.update = function () {
        if ($scope.id == 1) {
            alert("管理员角色无法修改！");
            return;
        }
        if ($scope.id == 2) {
            alert("经侦角色无法修改！");
            return;
        }
        if (myservice.isEmpty($scope.roleName)) {
            alert("请填写角色名称！")
            return;
        }
        var url = "/userRole/modifyUserRole";
        var params = {
            id: $scope.id,
            roleName: $scope.roleName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.userRole', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.cancel = function(){
        $state.go('app.userRole', {}, {reload: true});
    }
}]);

App.controller('groupRoleController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.groupId = $stateParams.groupId === 'inbox' ? '' : $stateParams.groupId;
    $scope.groupName = $stateParams.groupName === 'inbox' ? '' : $stateParams.groupName;
    $scope.getGroupRole = function () {
        var url = "/groupRole/getGroupRole?id=" + $scope.groupId;

        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.getGroupRole();

    $scope.addOrDelRole = function (item) {
        var url = "";
        if (item.checked) {
            url = "/groupRole/saveGroupRole";
        }
        else {
            url = "/groupRole/deleteGroupRole";
        }
        var params = {
            groupId: $scope.groupId,
            roleId: item.id
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

}]);

App.controller("userRolePageController", function ($http, $timeout, $scope, $state,
                                                   myservice, $stateParams) {
    //登录和锁定校验
    myservice.loginLockCheck();
    $scope.userRoleId = $stateParams.userRoleId === 'inbox' ? '' : $stateParams.userRoleId;
    //通过userRoleId获取页面


    var vm = this;

    $scope.initTree = function () {

        var url = "/rolePage/getRolePages?id=" + $scope.userRoleId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            vm.tree = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.initTree();

    $scope.deleteUrl_1 = "/rolePage/deleteRolePage";
    $scope.addUrl_1 = "/rolePage/saveRolePage";
    $scope.param_1 = $scope.userRoleId;
});


App.controller("pageRegisterController", function ($http, $timeout, $scope, $state,
                                                   myservice) {
    //登录和锁定校验
    myservice.loginLockCheck();
    //获取字典项
    loadDictionary_1 = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "isOrNot";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionary_2 = function () {
        var url = "/menu/getMenu";
        var params = {
            tier: 1
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites2 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionary_3 = function () {
        if (myservice.isEmpty($scope.selectedOptions_02)) {
            return;
        }
        var url = "/menu/getMenu";
        var params = {
            fatherId: $scope.selectedOptions_02,
            tier: 2
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites3 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionary_4 = function () {
        if (myservice.isEmpty($scope.selectedOptions_03)) {
            return;
        }
        var url = "/menu/getMenu";
        var params = {
            fatherId: $scope.selectedOptions_03,
            tier: 3
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites4 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $(function () {
        loadDictionary_1();
        loadDictionary_2();
        loadDictionary_3();
    })

    $scope.options02Changed = function () {
        loadDictionary_3();
    }
    $scope.options03Changed = function () {
        loadDictionary_4();
    }
    $scope.options10Changed = function (id) {
        if (myservice.isEmpty(id)) {
            return;
        }
        var url = "/menu/getNodeInfo?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.oneUpdateName = temp.obj.name;
            $scope.oneUpdateText = temp.obj.text;
            $scope.oneUpdateTranslate = temp.obj.translate;
            $scope.oneUpdateSort = temp.obj.sort;
            $scope.oneUpdateHeading = temp.obj.heading;
            $scope.oneUpdateTier = temp.obj.tier;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.updateOne = function () {
        var url = "/menu/updateMenu";
        var params = {
            id: $scope.selectedOptions_10,
            name: $scope.oneUpdateName,
            text: $scope.oneUpdateText,
            translate: $scope.oneUpdateTranslate,
            sort: $scope.oneUpdateSort,
            heading: $scope.oneUpdateHeading,
            tier: $scope.oneUpdateTier
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            loadDictionary_2();
            $scope.oneUpdateName = "";
            $scope.oneUpdateText = "";
            $scope.oneUpdateTranslate = "";
            $scope.oneUpdateSort = "";
            $scope.$broadcast('to-child-reload', "child");
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    function checkSort(sortNo) {
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test(sortNo)) {
            return false;
        }
        return true;
    }


    $scope.headSortCheck = function (sort) {
        if (!checkSort(sort)) {
            $scope.oneSort = "";
            $scope.oneUpdateSort = "";
            alert("排序编号必须为1-999三位数字！");
        }
    }

    $scope.twoSortCheck = function (sort) {
        if (!checkSort(sort)) {
            $scope.twoSort = "";
            alert("排序编号必须为1-999三位数字！");
        }
    }


    $scope.addOne = function () {
        var params = {
            name: $scope.oneName,
            text: $scope.oneText,
            translate: $scope.oneTranslate,
            sort: $scope.oneSort,
            heading: true,
            tier: 1,
        }
        $scope.doSave(params, 2);
        $scope.oneName = "";
        $scope.oneText = "";
        $scope.oneTranslate = "";
        $scope.oneSort = "";

    }

    $scope.addTwo = function () {
        var params = {
            fatherId: $scope.selectedOptions_02,
            name: $scope.two_name,
            text: $scope.two_text,
            sref: $scope.two_sref,
            icon: $scope.two_icon,
            translate: $scope.two_translate,
            alert: $scope.two_alert,
            label: $scope.two_label,
            sort: $scope.two_sort,
            heading: false,
            tier: 2,
        }
        $scope.doSave(params, 3);
        $scope.two_name = "";
        $scope.two_text = "";
        $scope.two_sref = "";
        $scope.two_icon = "";
        $scope.two_translate = "";
        $scope.two_alert = "";
        $scope.two_label = "";
        $scope.two_sort = "";
    }

    $scope.addThree = function () {
        var params = {
            fatherId: $scope.selectedOptions_03,
            name: $scope.three_name,
            text: $scope.three_text,
            sref: $scope.three_sref,
            icon: $scope.three_icon,
            translate: $scope.three_translate,
            alert: $scope.three_alert,
            label: $scope.three_label,
            sort: $scope.three_sort,
            heading: false,
            tier: 3,
        }
        $scope.doSave(params, 4);
        $scope.three_name = "";
        $scope.three_text = "";
        $scope.three_sref = "";
        $scope.three_icon = "";
        $scope.three_translate = "";
        $scope.three_alert = "";
        $scope.three_label = "";
        $scope.three_sort = "";
    }

    $scope.addFour = function () {
        var params = {
            fatherId: $scope.selectedOptions_04,
            name: $scope.four_name,
            text: $scope.four_text,
            sref: $scope.four_sref,
            icon: $scope.four_icon,
            translate: $scope.four_translate,
            alert: $scope.four_alert,
            label: $scope.four_label,
            sort: $scope.four_sort,
            heading: false,
            tier: 4,
        }
        $scope.doSave(params, 4);
        $scope.four_name = "";
        $scope.four_text = "";
        $scope.four_sref = "";
        $scope.four_icon = "";
        $scope.four_translate = "";
        $scope.four_alert = "";
        $scope.four_label = "";
        $scope.four_sort = "";
    }


    $scope.doSave = function (params, type) {
        var url = "/menu/saveMenu";
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            if (type == 2) {
                loadDictionary_2();
            }
            if (type == 3) {
                loadDictionary_3();
            }
            if (type == 4) {
                loadDictionary_4();
            }
            $scope.$broadcast('to-child-reload', "child");
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.$on('to-parent_select_02', function (d, data1, data2, data3) {
        if (data1) {
            $scope.select_03 = false;
            $scope.select_04 = false;
        }
        $scope.select_02 = data1;
        $scope.select2Btn = data2;
        $scope.select02Item = data3;
        $scope.two_name = data3.name;
        $scope.two_text = data3.text;
        $scope.two_sref = data3.sref;
        $scope.two_icon = data3.icon;
        $scope.two_translate = data3.translate;
        $scope.two_alert = data3.alert;
        $scope.two_label = data3.label;
        $scope.two_sort = data3.sort;

        $scope.two_checked = false;
        $scope.three_checked = false;
        $scope.four_checked = false;
    });

    $scope.$on('to-parent_select_03', function (d, data1, data2, data3) {
        if (data1) {
            $scope.select_02 = false;
        }
        $scope.select_03 = data1;
        $scope.select3Btn = data2;
        $scope.select03Item = data3;
        $scope.three_name = data3.name;
        $scope.three_text = data3.text;
        $scope.three_sref = data3.sref;
        $scope.three_icon = data3.icon;
        $scope.three_translate = data3.translate;
        $scope.three_alert = data3.alert;
        $scope.three_label = data3.label;
        $scope.three_sort = data3.sort;

        $scope.two_checked = false;
        $scope.three_checked = false;
        $scope.four_checked = false;
    });

    $scope.$on('to-parent_select_04', function (d, data1, data2, data3) {
        if (data1) {
            $scope.select_03 = false;
        }
        $scope.select_04 = data1;
        $scope.select4Btn = data2;
        $scope.select04Item = data3;
        $scope.four_name = data3.name;
        $scope.four_text = data3.text;
        $scope.four_sref = data3.sref;
        $scope.four_icon = data3.icon;
        $scope.four_translate = data3.translate;
        $scope.four_alert = data3.alert;
        $scope.four_label = data3.label;
        $scope.four_sort = data3.sort;

        $scope.two_checked = false;
        $scope.three_checked = false;
        $scope.four_checked = false;
    });

    $scope.selectBox = function (checked) {
        if (checked) {
            $scope.select_02 = false;
            $scope.select_03 = false;
            $scope.select_04 = false;
            $scope.select2Btn = true;
            $scope.select3Btn = true;
            $scope.select4Btn = true;

            $scope.two_name = "";
            $scope.two_text = "";
            $scope.two_sref = "";
            $scope.two_icon = "";
            $scope.two_translate = "";
            $scope.two_alert = "";
            $scope.two_label = "";
            $scope.two_sort = "";

            $scope.three_name = "";
            $scope.three_text = "";
            $scope.three_sref = "";
            $scope.three_icon = "";
            $scope.three_translate = "";
            $scope.three_alert = "";
            $scope.three_label = "";
            $scope.three_sort = "";

            $scope.four_name = "";
            $scope.four_text = "";
            $scope.four_sref = "";
            $scope.four_icon = "";
            $scope.four_translate = "";
            $scope.four_alert = "";
            $scope.four_label = "";
            $scope.four_sort = "";
        }
    }

    var updateUrl = "/menu/updateMenu"
    $scope.updateTwo = function () {
        var params = {
            id: $scope.select02Item.id,
            fatherId: $scope.select02Item.fatherId,
            name: $scope.two_name,
            text: $scope.two_text,
            sref: $scope.two_sref,
            icon: $scope.two_icon,
            translate: $scope.two_translate,
            alert: $scope.two_alert,
            label: $scope.two_label,
            sort: $scope.two_sort,
            heading: false,
            tier: 2,
        }
        myservice.doPost(updateUrl, params, "修改成功！");
        $scope.$broadcast('to-child-reload', "child");

    }

    $scope.updateThree = function () {
        var params = {
            id: $scope.select03Item.id,
            fatherId: $scope.select03Item.fatherId,
            name: $scope.three_name,
            text: $scope.three_text,
            sref: $scope.three_sref,
            icon: $scope.three_icon,
            translate: $scope.three_translate,
            alert: $scope.three_alert,
            label: $scope.three_label,
            sort: $scope.three_sort,
            heading: false,
            tier: 3,
        }
        myservice.doPost(updateUrl, params, "修改成功！");
        $scope.$broadcast('to-child-reload', "child");
    }

    $scope.updateFour = function () {

        var params = {
            id: $scope.select04Item.id,
            fatherId: $scope.select04Item.fatherId,
            name: $scope.four_name,
            text: $scope.four_text,
            sref: $scope.four_sref,
            icon: $scope.four_icon,
            translate: $scope.four_translate,
            alert: $scope.four_alert,
            label: $scope.four_label,
            sort: $scope.four_sort,
            heading: false,
            tier: 4,
        }
        myservice.doPost(updateUrl, params, "修改成功！");
        $scope.$broadcast('to-child-reload', "child");
    }


    var deleteUrl = "/menu/deleteMenu";
    $scope.deleteOne = function () {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        myservice.doPost(deleteUrl + "?id=" + $scope.selectedOptions_10, null, "删除成功！");
        $scope.selectedOptions_01 = "";
        loadDictionary_1();
        loadDictionary_2();
        loadDictionary_3();
        $scope.$broadcast('to-child-reload', "child");
    }

    $scope.deleteTwo = function () {

        myservice.doPost(deleteUrl + "?id=" + $scope.select02Item.id, null, "删除成功！");
        $scope.select_02 = false;
        loadDictionary_1();
        loadDictionary_2();
        loadDictionary_3();
        $scope.$broadcast('to-child-reload', "child");
    }

    $scope.deleteThree = function () {

        myservice.doPost(deleteUrl + "?id=" + $scope.select03Item.id, null, "删除成功！");
        $scope.select_03 = false;
        loadDictionary_1();
        loadDictionary_2();
        loadDictionary_3();
        $scope.$broadcast('to-child-reload', "child");
    }

    $scope.deleteFour = function () {

        myservice.doPost(deleteUrl + "?id=" + $scope.select04Item.id, null, "删除成功！");
        $scope.select_04 = false;
        loadDictionary_1();
        loadDictionary_2();
        loadDictionary_3();
        $scope.$broadcast('to-child-reload', "child");
    }

});

App.controller("registerSidebarController", ['$rootScope', '$scope', '$state', '$http', '$timeout', 'Utils', 'myservice',
    function ($rootScope, $scope, $state, $http, $timeout, Utils, myservice) {
        //登录和锁定校验
        myservice.loginLockCheck();


        var collapseList = [];

        // demo: when switch from collapse to hover, close all items
        $rootScope.$watch('app.layout.asideHover', function (oldVal, newVal) {
            if (newVal === false && oldVal === true) {
                closeAllBut(-1);
            }
        });

        // Check item and children active state
        var isActive = function (item) {

            if (!item) return;

            if (!item.sref || item.sref == '#') {
                var foundActive = false;
                angular.forEach(item.submenu, function (value, key) {
                    if (isActive(value)) foundActive = true;
                });
                return foundActive;
            } else
                return $state.is(item.sref) || $state.includes(item.sref);
        };

        // Load menu from json file
        // -----------------------------------

        $scope.getMenuItemPropClasses = function (item) {
            return (item.heading ? 'nav-heading' : '') +
                (isActive(item) ? ' active' : '');
        };

        $scope.loadSidebarMenu = function () {
            var menuURL = "/menu/getAllSiderBarMenu";
            $http.post(menuURL).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                $scope.menuItems = temp.obj;
            }).error(function (data, status, headers, config) {
                alert('Failure loading menu');
            });
        };

        $scope.loadSidebarMenu();

        // Handle sidebar collapse items
        // -----------------------------------

        $scope.addCollapse = function ($index, item) {
            collapseList[$index] = $rootScope.app.layout.asideHover ? true : !isActive(item);
        };

        $scope.isCollapse = function ($index) {
            return (collapseList[$index]);
        };

        $scope.toggleCollapse = function ($index, isParentItem) {


            // collapsed sidebar doesn't toggle drodopwn
            if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) return true;

            // make sure the item index exists
            if (angular.isDefined(collapseList[$index])) {
                if (!$scope.lastEventFromChild) {
                    collapseList[$index] = !collapseList[$index];
                    closeAllBut($index);
                }
            } else if (isParentItem) {
                closeAllBut(-1);
            }

            $scope.lastEventFromChild = isChild($index);

            return true;

        };

        function closeAllBut(index) {
            index += '';
            for (var i in collapseList) {
                if (index < 0 || index.indexOf(i) < 0)
                    collapseList[i] = true;
            }
        }

        function isChild($index) {
            return (typeof $index === 'string') && !($index.indexOf('-') < 0);
        }


        var lock1 = false;
        var lock2 = false;
        $scope.updateOrDelete2 = function (item) {
            if (item.tier == 2) {
                if (lock1) {
                    lock1 = !lock1;
                    return;
                }
                $scope.$emit('to-parent_select_02', !$scope.select_02, false, item);
            }
        }

        $scope.updateOrDelete3 = function (item) {
            if (item.tier == 3) {
                if (lock2) {
                    lock2 = !lock2;
                    return;
                }
                lock1 = true;
                $scope.$emit('to-parent_select_03', !$scope.select_03, false, item);
            }
            if (item.tier == 4) {
                lock1 = true;
                lock2 = true;
                $scope.$emit('to-parent_select_04', !$scope.select_04, false, item);
            }
        }

        $scope.$on('to-child-reload', function (d, data) {
            $scope.loadSidebarMenu();
        });
    }]);

App.controller("userGroupController", function ($http, $timeout, $scope, $state,
                                                myservice) {
    //登录和锁定校验
    myservice.loginLockCheck();

    $scope.active = {
        save: "0",
        update: "1"
    }

    $scope.userGroupOnClick = function (item) {
        $scope.groupName = item.groupName;
        $scope.id = item.id;
        $scope.hasClick = true;
    }

    //获取用户组列表
    $scope.getUsergroup = function () {
        var url = "/userGroup/getAllUserGroup";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = temp.obj;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.getUsergroup();


    $scope.removeUserGroup = function () {

        if (myservice.isEmpty($scope.id)) {
            return;
        }
        var url = "/userGroup/removeUserGroup?id=" + $scope.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            //$scope.getUsergroup();
            //$scope.hasClick = false;
            $state.go('app.userGroup', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

    }

});

App.controller('userGroupAddOrModifyController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.active = $stateParams.active === 'inbox' ? '' : $stateParams.active;
    $scope.id = $stateParams.id === 'inbox' ? '' : $stateParams.id;
    if ($scope.active == "0") {
        $scope.showSave = true;
        $scope.showUpdate = false;
    } else if ($scope.active == "1") {
        $scope.showSave = false;
        $scope.showUpdate = true;
        var url = "/userGroup/getUserGroupById?id=" + $scope.id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.groupName = temp.obj.groupName;
            $scope.description = temp.obj.description;
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.save = function () {
        debugger;
        if (myservice.isEmpty($scope.groupName)) {
            alert("请填写用户组名称！")
            return;
        }
        var url = "/userGroup/saveUserGroup";
        var params = {
            groupName: $scope.groupName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.userGroup', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.update = function () {
        debugger;
        if (myservice.isEmpty($scope.groupName)) {
            alert("请填写用户组名称！")
            return;
        }
        var url = "/userGroup/modifyUserGroup";
        var params = {
            id: $scope.id,
            groupName: $scope.groupName,
            description: $scope.description
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $state.go('app.userGroup', {}, {reload: true});
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.cancel = function(){
        $state.go('app.userGroup', {}, {reload: true});
    }
}]);

App.controller('userGroupUsersController', ['$http', '$timeout', '$state', '$scope', '$stateParams', 'myservice', function ($http, $timeout, $state, $scope, $stateParams, myservice) {
    $scope.userGroupid = $stateParams.userGroupid === 'inbox' ? '' : $stateParams.userGroupid;
    $scope.selected_01 = [];
    $scope.selected_02 = [];
    var getUsers_1 = function () {
        var url = "/userGroup/getUsers?userGroupId=" + $scope.userGroupid;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.users_01 = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    var getUsers_2 = function () {
        var url = "/userGroup/getGroupUsers?userGroupId=" + $scope.userGroupid;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.users_02 = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $(function () {
        getUsers_1();
        getUsers_2();
    })

    //单个勾选去勾选
    $scope.selectOne_01 = function (item) {
        if (item.checked) {
            $scope.selected_01.push(item.id);
        } else {
            $scope.selected_01.splice($scope.selected_01.indexOf(item.id), 1);
        }
    }

    //单个勾选去勾选
    $scope.selectOne_02 = function (item) {
        if (item.checked) {
            $scope.selected_02.push(item.id);
        } else {
            $scope.selected_02.splice($scope.selected_02.indexOf(item.id), 1);
        }
    }
    //全选全去勾选
    $scope.selectAll_01 = function (checked_01) {
        $scope.selected_01 = [];
        if (checked_01) {
            angular.forEach($scope.users_01, function (e) {
                e.checked = true;
                $scope.selected_01.push(e.id);
            })
        } else {
            angular.forEach($scope.users_01, function (e) {
                e.checked = false;
                $scope.selected_01.splice($scope.selected_01.indexOf(e.id), 1);
            })
        }
    }

    //全选全去勾选
    $scope.selectAll_02 = function (checked_02) {
        $scope.selected_02 = [];
        if (checked_02) {
            angular.forEach($scope.users_02, function (e) {
                e.checked = true;
                $scope.selected_02.push(e.id);
            })
        } else {
            angular.forEach($scope.users_02, function (e) {
                e.checked = false;
                $scope.selected_02.splice($scope.selected_02.indexOf(e.id), 1);
            })
        }
    }


    $scope.addUserToUserGroup = function (userId) {

        var url = "/userGroup/addUserToUserGroup";
        var params = {
            userId: userId,
            userGroupId: $scope.userGroupid
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    function UserGroupDetailDto(userId, userGroupId) {
        var o = new Object();
        o.userId = userId;
        o.userGroupId = userGroupId;
        return o;
    }

    $scope.addToUserGroupBatch = function () {
        if (myservice.isEmpty($scope.selected_01)) {
            return;
        }
        userGroupDetailDtoArr = [];
        angular.forEach($scope.selected_01, function (e) {
            userGroupDetailDtoArr.push(UserGroupDetailDto(e, $scope.userGroupid));
        })
        var url = "/userGroup/addUserToUserGroupBatch";
        $http.post(url, JSON.stringify(userGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.selected_01 = [];
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

    }

    $scope.deleteUserGroup = function (userId) {
        var url = "/userGroup/removeUserFromUserGroup";
        var params = {
            userId: userId,
            userGroupId: $scope.userGroupid
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }


    $scope.deleteUserFromUserGroupBatch = function () {
        if (myservice.isEmpty($scope.selected_02)) {
            return;
        }
        userGroupDetailDtoArr = [];
        angular.forEach($scope.selected_02, function (e) {
            userGroupDetailDtoArr.push(UserGroupDetailDto(e, $scope.userGroupid));
        })

        var url = "/userGroup/removeUserFromUserGroupBatch";
        $http.post(url, JSON.stringify(userGroupDetailDtoArr)).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.selected_02 = [];
            getUsers_1();
            getUsers_2();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
}]);

App.controller("caseManagerController", function ($http, $timeout, $scope,
                                                  myservice) {
    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    //已选择的文件
    $scope.selected = [];

    // myservice.dragFunc("cnDiv");

    $("#cnDiv").hide();

    //单个勾选去勾选
    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.id);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.id), 1);
        }
    }
    //全选全去勾选
    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                $scope.selected.push(e.id);
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.id), 1);
            })
        }
    }

    $scope.close = function () {
        $("#cnDiv").hide();
    }

    $scope.add = function () {
        $("#cnDiv").show();
        $scope.flag = 0;
        $scope.id = "";
        $scope.caseNo_1 = "";
        $scope.caseName_1 = "";
        $scope.caseInfo_1 = "";
        $scope.rinseUrl_1 = "";
    }

    $scope.saveOrUpdate = function () {
        var url = "";
        if ($scope.flag == 0) {
            url = "/fileCase/saveCase";
        } else if ($scope.flag == 1) {
            url = "/fileCase/updateCase";
        }
        var params = {
            id: $scope.id,
            caseNo: $scope.caseNo_1,
            caseName: $scope.caseName_1,
            caseInfo: $scope.caseInfo_1,
            rinseUrl: $scope.rinseUrl_1
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDiv").hide();
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.query = function () {
        //清空选择项
        $scope.selected = [];
        $scope.checked = false;
        $("#pleaseWait").show();
        var url = "/fileCase/getCases";
        var params = {
            caseName: $scope.caseName
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.query();

    $scope.updateDetail = function (item) {
        $("#cnDiv").show();
        $scope.id = item.id;
        $scope.flag = 1;
        $scope.caseNo_1 = item.caseNo;
        $scope.caseName_1 = item.caseName;
        $scope.caseInfo_1 = item.caseInfo;
        $scope.rinseUrl_1 = item.rinseUrl;
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileCase/deleteCase?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.deleteSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileCase/deleteCase?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }
});

App.controller("fileTemplateGroupController", function ($http, $timeout, $scope,
                                                        myservice) {
    $("#pleaseWait").hide();
    //登录和锁定校验
    myservice.loginLockCheck();
    //已选择的文件
    $scope.selected = [];

    // myservice.dragFunc("cnDiv");
    myservice.dragFunc("fieldCompleteDetail");
    myservice.dragFunc("fieldCompleteAdd");
    myservice.dragFunc("modifySortNoDiv");
    $("#cnDiv").hide();
    $("#fieldCompleteDetail").hide();
    $("#fieldCompleteAdd").hide();
    $("#modifySortNoDiv").hide();

    //单个勾选去勾选
    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.groupId);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.groupId), 1);
        }
    }
    //全选全去勾选
    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                if (e.groupId != 1) {
                    $scope.selected.push(e.groupId);
                }
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.groupId), 1);
            })
        }
    }


    $scope.add = function () {
        $("#cnDiv").show();
        $scope.groupName = "";
        $scope.template.categories = [];
        $scope.comment = "";
    }


    $scope.template = {};
    var availableCategories = [];
    //获取模板
    $scope.queryTemplates = function () {
        var url = "/fileTemplate/getFileTemplates";
        var params = {}
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            angular.forEach(temp.obj, function (e) {
                var cell = e.templateName + "::" + e.id;
                availableCategories.push(cell);
            })
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.availableCategories = availableCategories;
    $scope.queryTemplates();


    //查询所有的模板组信息
    $scope.query = function () {
        var url = "/fileTemplateGroup/getFileTemplateGroupDtoListByThisUserId";
        var params = {}
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.query();
    $scope.close = function () {
        $("#cnDiv").hide();
    }


    function FileTemplateBean(id, templateName) {
        var o = new Object();
        o.id = id;
        o.templateName = templateName;
        return o;
    }

    $scope.save = function () {

        if (myservice.isEmpty($scope.template.categories)) {
            alert("请选择模板！");
        }
        if (myservice.isEmpty($scope.groupName)) {
            alert("请填写择模板名称！");
        }
        //查询模板组名称有没有使用
        var url = "/fileTemplateGroup/getFileTemplateGroups";
        var params = {
            groupName: $scope.groupName
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            if (!myservice.isEmpty(temp.obj)) {
                alert("模板组名称已经存在！");
                return;
            }
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });


        //进行保存
        fileTemplateDtoArr = [];

        angular.forEach($scope.template.categories, function (e) {
            var arr = e.split("::");
            fileTemplateDtoArr.push(FileTemplateBean(arr[1], arr[0]));
        })

        url = "/fileTemplateGroup/fileTemplateGroupInsert";
        params = {
            groupName: $scope.groupName,
            fileTemplateDtoList: fileTemplateDtoArr
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDiv").hide();
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileTemplateGroup/deleteFileTemplateGroup?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.deleteSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileTemplateGroup/deleteFileTemplateGroup?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.showFileFieldCompletes = function (groupId, groupName) {
        $("#fieldCompleteDetail").show();
        $scope.groupName_2 = groupName;
        $scope.groupId_2 = groupId;
        //获取配置的补全信息
        var url = "/fileTemplateGroup/getFileFieldCompletes?groupId=" + groupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.details = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

        //获取模板组内模板信息
        var url = "/fileTemplateGroup/getFileTemplateByGroupId?groupId=" + groupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.sortNoChange = function () {
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test($scope.sortNo)) {
            $scope.sortNo = "";
            alert("请填写0-999数字！");
        }
    }

    $scope.selectedOptionsChange = function (index1, index2) {
        var url = "/fileTemplate/getFileTemplateDetails";
        var params = {
            templateId: $scope['selectedOptions' + index1]
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope['templateDetailList' + index1] = myservice.setSerialNumber(temp.obj);
            $scope['templateDetailList' + index2] = angular.copy(myservice.setSerialNumber(temp.obj));
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.selectFieldOne = function (item, index) {
        angular.forEach($scope['templateDetailList' + index], function (e) {
            e.checked = false;
            if (item.id == e.id) {
                e.checked = true;
                $scope['checkedId' + index] = item;
            }
        })
    }

    //源字段可以是多个，有一个匹配就进行匹配
    $scope.selectFieldComb2 = [];
    $scope.selectFieldComb = function (item) {
        if (item.checked) {
            $scope.selectFieldComb2.push(item.id);
        } else {
            $scope.selectFieldComb2.splice($scope.selectFieldComb2.indexOf(item.id), 1);
        }
    }

    $scope.fieldRelations = [];
    $scope.createFieldRelation = function () {
        if (myservice.isEmpty($scope.checkedId3) || myservice.isEmpty($scope.checkedId4)) {
            alert("请先勾选！");
            return;
        }
        //判断是否重复配置
        var flag = false;
        if (!myservice.isEmpty($scope.fieldRelations)) {
            angular.forEach($scope.fieldRelations, function (e) {
                if (e.fieldRelationId == ($scope.checkedId3.id + "++" + $scope.checkedId4.id)) {
                    alert("请勿重复配置！");
                    flag = true;
                }
            })
        }
        if (flag) {
            return;
        }
        var fieldRelationObj = new Object();
        fieldRelationObj.fieldRelation = $scope.checkedId3.fieldKey + "++" + $scope.checkedId4.fieldKey;
        fieldRelationObj.fieldRelationId = $scope.checkedId3.id + "++" + $scope.checkedId4.id;
        $scope.fieldRelations.push(fieldRelationObj);
        $scope.fieldRelations = myservice.setSerialNumber($scope.fieldRelations);
    }

    //进行关系删除
    $scope.deleteFieldRelation = function (no) {
        $scope.fieldRelations.splice($scope.fieldRelations.indexOf(no), 1);
    }

    //保存补全配置
    $scope.saveFieldComplete = function () {

        $("#fieldCompleteAdd").hide();

        if (myservice.isEmpty($scope.checkedId1) || myservice.isEmpty($scope.selectFieldComb2) || myservice.isEmpty($scope.fieldRelations) || myservice.isEmpty($scope.sortNo)) {
            alert("目标字段，源字段，关联关系，排序字段,是否都已配置！");
            return;
        }

        var relation = "";
        angular.forEach($scope.fieldRelations, function (e) {
            relation += e.fieldRelationId + "--";
        });

        var fieldComb = "";
        angular.forEach($scope.selectFieldComb2, function (e) {
            fieldComb += e + "++";
        });

        var url = "/fileTemplateGroup/saveFileFieldCompletes";
        var params = {
            sortNo: $scope.sortNo,
            destFileTemplateId: $scope.selectedOptions1,
            sourceFileTemplateId: $scope.selectedOptions2,
            fieldRelation: relation,
            fieldSource: fieldComb,
            fileTemplateGroupId: $scope.groupId_2,
            fieldDest: $scope.checkedId1.id
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.showFileFieldCompletes($scope.groupId_2, $scope.groupName_2);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteFieldComplete = function (id) {
        var url = "/fileTemplateGroup/removeFileFieldComplete?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.showFileFieldCompletes($scope.groupId_2, $scope.groupName_2);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeAddFieldComplete = function () {
        $("#fieldCompleteDetail").hide();
    }

    $scope.addFieldComplete = function () {
        $("#fieldCompleteAdd").show();
        $scope.checkedId1 = "";
        $scope.selectFieldComb2 = [];
        $scope.checkedId3 = "";
        $scope.fieldRelations = [];
        $scope.checkedId4 = "";
        $scope.templateDetailList1 = [];
        $scope.templateDetailList2 = [];
        $scope.templateDetailList3 = [];
        $scope.templateDetailList4 = [];
    }

    $scope.closeFieldCompleteAdd = function () {
        $("#fieldCompleteAdd").hide();
    }

    $scope.modifyFieldCompleteSortNo = function (id, sortNo) {
        $scope.mod_sortNo_id = id;
        $("#modifySortNoDiv").show();
        $scope.mod_sortNo = sortNo;
    }
    $scope.mod_sortNoChange = function () {
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test($scope.mod_sortNo)) {
            $scope.mod_sortNo = "";
            alert("请填写0-999数字！");
        }
    }
    $scope.modifySortNo = function () {

        var url = "/fileTemplateGroup/modifyCompletesSortNoById?id=" + $scope.mod_sortNo_id + "&sortNo=" + $scope.mod_sortNo;

        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            alert("排序编号修改成功！");
            $scope.showFileFieldCompletes($scope.groupId_2, $scope.groupName_2);
            $("#modifySortNoDiv").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeModifySortNoDiv = function () {
        $("#modifySortNoDiv").hide();
    }

});

App.controller("fileTemplateController", function ($http, $timeout, $scope, $rootScope,
                                                   myservice, $modal) {
    $("#pleaseWait").hide();
    $scope.userId = $rootScope.user.id;
    myservice.loginLockCheck();
    //已选择的文件
    $scope.selected = [];

    //获取勾选数组
    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.id);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.id), 1);
        }
    }

    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                $scope.selected.push(e.id);
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.id), 1);
            })
        }
    }
    // myservice.dragFunc("cnDiv");
    myservice.dragFunc("cnDivUpdate");
    myservice.dragFunc("cnDivDetail");
    myservice.dragFunc("cnDivDetailAddOrUpdate");
    myservice.dragFunc("cnDivRinseBusiness");
    myservice.dragFunc("cnRepetBussDivDetail");
    myservice.dragFunc("cnNullBussDivDetail");
    myservice.dragFunc("cnReplaceBussDivDetail");
    myservice.dragFunc("cnSuffixBussDivDetail");
    myservice.dragFunc("cnPrefixBussDivDetail");
    myservice.dragFunc("cnIpBussDivDetail");
    myservice.dragFunc("cnPhoneBussDivDetail");
    myservice.dragFunc("cnIdCardNoBussDivDetail");
    myservice.dragFunc("cnImportTxt");
    $("#cnDiv").hide();
    $("#cnDivUpdate").hide();
    $("#cnDivDetail").hide();
    $("#cnDivDetailAddOrUpdate").hide();
    $("#cnDivRinseBusiness").hide();
    $("#cnRepetBussDivDetail").hide();
    $("#cnNullBussDivDetail").hide();
    $("#cnReplaceBussDivDetail").hide();
    $("#cnSuffixBussDivDetail").hide();
    $("#cnPrefixBussDivDetail").hide();
    $("#cnIpBussDivDetail").hide();
    $("#cnPhoneBussDivDetail").hide();
    $("#cnIdCardNoBussDivDetail").hide();
    $("#cnImportTxt").hide();
    loadDictionary1 = function () {
        var url = "/fileRinse/getFileRinses";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    loadDictionary1();

    $scope.add = function () {
        $("#cnDiv").show();
        $scope.templateName_1 = "";
        $scope.tablePrefix_1 = "";
        $scope.templateKey_1 = "";
        $scope.exclude_1 = "";
        $scope.comment_1 = "";
    }

    $scope.update = function (item) {
        $("#cnDivUpdate").show();
        $scope.reBound = false;
        $scope.id_2 = item.id;
        $scope.selectedOptions2 = item.fileRinseGroupId;
        $scope.templateName_2 = item.templateName;
        $scope.tablePrefix_2 = item.tablePrefix;
        $scope.templateKey_2 = item.templateKey;
        $scope.exclude_2 = item.exclude;
        $scope.comment_2 = item.comment;
        $scope.templateId = item.id;

    }

    $scope.importTxt = function () {
        $("#cnImportTxt").show();
    }
    $scope.closeCnImportTxt = function () {
        $("#cnImportTxt").hide();
    }

    $scope.uploadTxt = function () {

        mydata = document.getElementById("txt1").files[0];
        formData = new FormData();
        formData.append("fileTxt", mydata);

        $.ajax({
            contentType: "multipart/form-data",
            url: "/fileTemplate/uploadFileTemplateTxt",
            type: "POST",
            data: formData,
            dataType: "text",
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            success: function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);
                $scope.query();
            }
        });
    }

    $scope.updateSave = function () {
        var url = "/fileTemplate/updateFileTemplate";
        if (myservice.isEmpty($scope.templateName_2)) {
            alert("模板名称必填！");
            return;
        }
        if (myservice.isEmpty($scope.tablePrefix_2)) {
            alert("表前缀必填！");
            return;
        }
        if (myservice.isEmpty($scope.templateKey_2)) {
            alert("关键字必填！");
            return;
        }
        var params = {
            id: $scope.id_2,
            templateName: $scope.templateName_2,
            tablePrefix: $scope.tablePrefix_2,
            templateKey: $scope.templateKey_2,
            exclude: $scope.exclude_2,
            comment: $scope.comment_2,
            fileRinseGroupId: $scope.selectedOptions2
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();
            $scope.removeDetailBoundByTemplateId($scope.templateId);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDivUpdate").hide();
    }

    //模板下的字段与清洗字段进行解绑
    $scope.removeDetailBoundByTemplateId = function (templateId) {
        var url = "/fileTemplate/removeBoundByTemplateId?templateId=" + templateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/fileTemplate/getFileTemplates";
        var params = {
            templateName: $scope.templateName,
            tablePrefix: $scope.tablePrefix,
            templateKey: $scope.templateKey
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.obj = myservice.setSerialNumber(temp.obj);
            angular.forEach($scope.obj, function (e) {
                $scope[e.fileRinseGroupId] = e.fileRinseGroupId;
            });
            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDiv").hide();
    }

    $scope.query();

    $scope.save = function () {
        if (myservice.isEmpty($scope.templateName_1)) {
            alert("模板名称必填！");
            return;
        }
        if (myservice.isEmpty($scope.tablePrefix_1)) {
            alert("表前缀必填！");
            return;
        }
        if (myservice.isEmpty($scope.templateKey_1)) {
            alert("关键字必填！");
            return;
        }
        var url = "/fileTemplate/fileTemplateInsert";
        var params = {
            templateName: $scope.templateName_1,
            tablePrefix: $scope.tablePrefix_1,
            templateKey: $scope.templateKey_1,
            exclude: $scope.exclude_1,
            comment: $scope.comment_1,
            fileRinseGroupId: $scope.selectedOptions1
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDiv").hide();
            $scope.query();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.close = function () {
        $("#cnDiv").hide();
    }

    $scope.closeUpdate = function () {
        $("#cnDivUpdate").hide();
    }

    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                $scope.selected.push(e.id);
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.id), 1);
            })
        }
    }

    //获取勾选数组
    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.id);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.id), 1);
        }
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileTemplate/deleteFileTemplate?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.deleteSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileTemplate/deleteFileTemplate?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.downloadTemplateTXT = function (id) {

        var url = "/fileTemplate/downloadTemplateTXT?fileTemplateId=" + id;
        location.href = url
    }

    $scope.detail = function (item) {
        $("#cnDivDetail").show();
        $scope.fileRinseGroupId = item.fileRinseGroupId;
        $scope.item = item;
        $scope.templateName_3 = item.templateName;
        $scope.templateId = item.id;
        var url = "/fileTemplate/getFileTemplateDetails";
        var params = {
            templateId: item.id
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.details = myservice.setSerialNumber(temp.obj);
            $scope.getFileRinseDetailByGroupId();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.detail2 = function (item) {
        $("#cnDivDetail").show();
        $scope.fileRinseGroupId = item.fileRinseGroupId;
        $scope.item = item;
        $scope.templateName_3 = item.templateName;
        $scope.templateId = item.id;
        var url = "/fileTemplate/getFileTemplateDetails";
        var params = {
            templateId: item.id
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.details = myservice.setSerialNumber(temp.obj);
            //$scope.getFileRinseDetailByGroupId();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getFileRinseDetailByGroupId = function () {
        var url = "/fileRinse/getFileRinseDetailsByGroupId?groupId=" + $scope.fileRinseGroupId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.sites2 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.closeUpdateDetail = function () {
        $("#cnDivDetail").hide();
    }

    $scope.addDetail = function () {

        $("#cnDivDetailAddOrUpdate").show();
        $scope.flag = 0;
        $scope.fieldName = "";
        $scope.fieldKey = "";
        $scope.regular = "";
        $scope.exclude = "";
        $scope.sortNo = "";
        $scope.comment = "";
        $scope.selectedOptions3 = "";
    }

    $scope.updateDetail = function (item) {
        $("#cnDivDetailAddOrUpdate").show();
        $scope.flag = 1;
        $scope.id = item.id;
        $scope.fieldName = item.fieldName;
        $scope.fieldKey = item.fieldKey;
        $scope.regular = item.regular;
        $scope.exclude = item.exclude;
        $scope.sortNo = item.sortNo;
        $scope.comment = item.comment;
        $scope.selectedOptions3 = item.fileRinseDetailId;
    }

    $scope.closeUpdateDetailAddOrUpdate = function () {

        $("#cnDivDetailAddOrUpdate").hide();
    }

    $scope.detailAddOrUpdate = function () {

        if (myservice.isEmpty($scope.fieldName)) {
            alert("请填写字段名称！");
            return;
        }
        if (myservice.isEmpty($scope.fieldKey)) {
            alert("请填写字段关键字以&&分隔！");
            return;
        }
        if (myservice.isEmpty($scope.sortNo)) {
            alert("请填写字段排序编号为0-999数字！");
            return;
        }

        var url = "";
        if ($scope.flag == 0) {
            url = "/fileTemplate/saveFileTemplateDetails";
        } else if ($scope.flag == 1) {   //修改
            url = "/fileTemplate/updateFileTemplateDetails";
        }
        var params = {
            id: $scope.flag == 1 ? $scope.id : "",
            templateId: $scope.templateId,
            fieldName: $scope.fieldName,
            fieldKey: $scope.fieldKey,
            regular: $scope.regular,
            exclude: $scope.exclude,
            sortNo: $scope.sortNo,
            comment: $scope.comment,
            fileRinseDetailId: $scope.selectedOptions3
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnDivDetailAddOrUpdate").hide();
            console.log($scope.item)
            $scope.detail2($scope.item);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.sortNoChange = function () {
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test($scope.sortNo)) {
            $scope.sortNo = "";
            alert("请填写0-999数字！");
        }
    }

    $scope.checktablePrefix = function (tablePrefix, index) {
        if (tablePrefix.indexOf("origin") > -1) {
            $scope['tablePrefix_' + index] = "";
            alert("表名前缀不允许含有【origin】关键字");
        }
    }


    $scope.deleteOneDetail = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/fileTemplate/removeFileTemplateDetails?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.detail($scope.item);

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }


    $scope.selectOneChange = function () {
        var msg = "您真的确定要修改吗？\n\n修改后字段绑定的清洗字段需要全部再次绑定！\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        $scope.reBound = true;
    }

    $scope.getPinyin = function (fieldKey) {
        var url = "/fileTemplate/getPinyin";
        if (myservice.isEmpty(fieldKey)) {
            fieldKey = ' ';
        }
        $http.post(url, fieldKey).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.fieldName = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }


    //清洗配置
    $scope.rinseBusiness = function (item) {
        $scope.fileTemplateId = item.id;
        $scope.fileTemplateName = item.templateName;
        $("#cnDivRinseBusiness").show();
        $scope.detailForRinseBusiness(item);
        $scope.getRepetBussDivDetailList();
        $scope.getNullBussDivDetailList();
        $scope.getReplaceBussDivDetailList();
        $scope.getSuffixBussDivDetailList();
        $scope.getPrefixBussDivDetailList();
        $scope.getIpBussDivDetailList();
        $scope.getPhoneBussDivDetailList();
        $scope.getIdCardNoBussDivDetailList();
    }
    $scope.closeCnDivRinseBusiness = function () {
        $("#cnDivRinseBusiness").hide();
        $("#cnRepetBussDivDetail").hide();
        $("#cnReplaceBussDivDetail").hide();
        $("#cnNullBussDivDetail").hide();
        $("#cnSuffixBussDivDetail").hide();
        $("#cnPrefixBussDivDetail").hide();
        $("#cnIpBussDivDetail").hide();
        $("#cnPhoneBussDivDetail").hide();
        $("#cnIdCardNoBussDivDetail").hide();
    }
    $scope.template = {};
    $scope.detailForRinseBusiness = function (item) {
        var url = "/fileTemplate/getFileTemplateDetails";
        var params = {
            templateId: item.id
        }
        $scope.availableCategories = [];
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            angular.forEach(temp.obj, function (e) {
                var cell = e.fieldKey + "::" + e.id;
                $scope.availableCategories.push(cell);
            })
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.addRepetBuss = function () {
        $("#cnRepetBussDivDetail").show();
        $scope.template.categoriesRepet = [];
    }

    $scope.addNullBuss = function () {
        $("#cnNullBussDivDetail").show();
        $scope.template.categoriesNull = [];
        $scope.value = "";
    }

    $scope.addReplaceBuss = function () {
        $("#cnReplaceBussDivDetail").show();
        $scope.template.categoriesReplace = [];
        $scope.value = "";
        $scope.key = "";
    }

    $scope.addSuffixBuss = function () {
        $("#cnSuffixBussDivDetail").show();
        $scope.template.categoriesSuffix = [];
        $scope.suffix = "";
    }

    $scope.addPrefixBuss = function () {
        $("#cnPrefixBussDivDetail").show();
        $scope.template.categoriesPrefix = [];
        $scope.prefix = "";
    }

    $scope.addIpBuss = function () {
        $("#cnIpBussDivDetail").show();
        $scope.template.categoriesIp = [];
    }

    $scope.addPhoneBuss = function () {
        $("#cnPhoneBussDivDetail").show();
        $scope.template.categoriesPhone = [];
    }

    $scope.addIdCardNoBuss = function () {
        $("#cnIdCardNoBussDivDetail").show();
        $scope.template.categoriesIdCardNo = [];
    }

    $scope.closeRepetBussDivDetail = function () {
        $("#cnRepetBussDivDetail").hide();
    }

    $scope.closeNullBussDivDetail = function () {
        $("#cnNullBussDivDetail").hide();
    }

    $scope.closeReplaceBussDivDetail = function () {
        $("#cnReplaceBussDivDetail").hide();
    }

    $scope.closeSuffixBussDivDetail = function () {
        $("#cnSuffixBussDivDetail").hide();
    }

    $scope.closePrefixBussDivDetail = function () {
        $("#cnPrefixBussDivDetail").hide();
    }

    $scope.closeIpBussDivDetail = function () {
        $("#cnIpBussDivDetail").hide();
    }

    $scope.closePhoneBussDivDetail = function () {
        $("#cnPhoneBussDivDetail").hide();
    }

    $scope.closeIdCardNoBussDivDetail = function () {
        $("#cnIdCardNoBussDivDetail").hide();
    }


    $scope.saveRepetBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesRepet)) {
            alert("请先选择字段！");
            return;
        }
        var fields = "";
        angular.forEach($scope.template.categoriesRepet, function (e) {
            fields += e.substring(e.lastIndexOf("::") + 2) + ',';
        });
        var url = "/fileTemplate/saveRepetBuss";
        var params = {
            fileTemplateId: $scope.fileTemplateId,
            fields: fields
        }

        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $("#cnRepetBussDivDetail").hide();
            $scope.getRepetBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.saveNullBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesNull)) {
            alert("请先选择字段！");
            return;
        }
        var url = "/fileTemplate/saveNullBuss";
        angular.forEach($scope.template.categoriesNull, function (e) {
            field = e.substring(e.lastIndexOf("::") + 2);

            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field,
                value: $scope.value
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getNullBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnNullBussDivDetail").hide();

    }

    $scope.saveReplaceBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesReplace)) {
            alert("请先选择字段！");
            return;
        }
        if (myservice.isEmpty($scope.value)) {
            alert("请填写要修改的值！");
            return;
        }
        if (myservice.isEmpty($scope.key)) {
            alert("请填写要修改的原值！");
            return;
        }
        var url = "/fileTemplate/saveReplaceBuss";
        angular.forEach($scope.template.categoriesReplace, function (e) {
            field = e.substring(e.lastIndexOf("::") + 2);

            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field,
                key: $scope.key,
                value: $scope.value
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getReplaceBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnReplaceBussDivDetail").hide();

    }

    $scope.saveSuffixBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesSuffix)) {
            alert("请先选择字段！");
            return;
        }
        if (myservice.isEmpty($scope.suffix)) {
            alert("请填写要修改去除的后缀！");
            return;
        }
        var url = "/fileTemplate/saveSuffixBuss";
        angular.forEach($scope.template.categoriesSuffix, function (e) {
            var field = e.substring(e.lastIndexOf("::") + 2);
            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field,
                suffix: $scope.suffix
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getSuffixBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnSuffixBussDivDetail").hide();
    }

    $scope.savePrefixBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesPrefix)) {
            alert("请先选择字段！");
            return;
        }
        if (myservice.isEmpty($scope.prefix)) {
            alert("请填写要修改去除的后缀！");
            return;
        }
        var url = "/fileTemplate/savePrefixBuss";
        angular.forEach($scope.template.categoriesPrefix, function (e) {
            var field = e.substring(e.lastIndexOf("::") + 2);
            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field,
                prefix: $scope.prefix
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getPrefixBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnPrefixBussDivDetail").hide();
    }

    $scope.saveIpBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesIp)) {
            alert("请先选择字段！");
            return;
        }
        var url = "/fileTemplate/saveIpBuss";
        angular.forEach($scope.template.categoriesIp, function (e) {
            var field = e.substring(e.lastIndexOf("::") + 2);
            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getIpBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnIpBussDivDetail").hide();
    }

    $scope.savePhoneBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesPhone)) {
            alert("请先选择字段！");
            return;
        }
        var url = "/fileTemplate/savePhoneBuss";
        angular.forEach($scope.template.categoriesPhone, function (e) {
            var field = e.substring(e.lastIndexOf("::") + 2);
            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getPhoneBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnPhoneBussDivDetail").hide();
    }

    $scope.saveIdCardNoBussDivDetail = function () {
        if (myservice.isEmpty($scope.template.categoriesIdCardNo)) {
            alert("请先选择字段！");
            return;
        }
        var url = "/fileTemplate/saveIdCardNoBuss";
        angular.forEach($scope.template.categoriesIdCardNo, function (e) {
            var field = e.substring(e.lastIndexOf("::") + 2);
            var params = {
                fileTemplateId: $scope.fileTemplateId,
                fileTemplateDetailId: field
            }

            $http.post(url, params).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                myservice.errors(temp);

                $scope.getIdCardNoBussDivDetailList();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        });
        $("#cnIdCardNoBussDivDetail").hide();
    }

    $scope.getRepetBussDivDetailList = function () {
        var url = "/fileTemplate/getRepetBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.repetBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.getNullBussDivDetailList = function () {
        var url = "/fileTemplate/getNullBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.nullBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getReplaceBussDivDetailList = function () {
        var url = "/fileTemplate/getReplaceBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.replaceBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.getSuffixBussDivDetailList = function () {
        var url = "/fileTemplate/getSuffixBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.suffixBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getPrefixBussDivDetailList = function () {
        var url = "/fileTemplate/getPrefixBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.prefixBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getIpBussDivDetailList = function () {
        var url = "/fileTemplate/getIpBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.ipBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getPhoneBussDivDetailList = function () {
        var url = "/fileTemplate/getPhoneBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.phoneBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.getIdCardNoBussDivDetailList = function () {
        var url = "/fileTemplate/getIdCardNoBussList?fileTemplateId=" + $scope.fileTemplateId;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.idCardNoBusinessList = myservice.setSerialNumber(temp.obj);
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteRepet = function (id) {
        var url = "/fileTemplate/deleteRepetById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getRepetBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteReplace = function (id) {
        var url = "/fileTemplate/deleteReplaceById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getReplaceBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteNull = function (id) {
        var url = "/fileTemplate/deleteNullById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getNullBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteSuffix = function (id) {
        var url = "/fileTemplate/deleteSuffixById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getSuffixBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deletePrefix = function (id) {
        var url = "/fileTemplate/deletePrefixById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getPrefixBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteIp = function (id) {
        var url = "/fileTemplate/deleteIpById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getIpBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deletePhone = function (id) {
        var url = "/fileTemplate/deletePhoneById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getPhoneBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteIdCardNo = function (id) {
        var url = "/fileTemplate/deleteIdCardNoById?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getIdCardNoBussDivDetailList();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

});


//导入文件管理
App.controller("fileManagerController", function ($http, $timeout, $scope,
                                                  myservice, $rootScope, $websocket) {
    $("#pleaseWait").hide();
    myservice.loginLockCheck();

    //已选择的文件
    $scope.selected = [];

    var isArr = [];

    $scope.test2 = function () {
        var url = "/file/test2";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    //label:要赋值的标签，dic_group：字典组名称
    loadDictionary1 = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "hasDecompress";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    loadDictionary2 = function () {
        var url = "/sysDictionary/getSysDictionaryByDicGroup?dicGroup=" + "hasImport";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites2 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    loadDictionary1();
    loadDictionary2();
    $scope.query = function () {
        $scope.selected = [];
        $scope.checked = false;
        $("#pleaseWait").show();
        var url = "/file/findFileList";
        var hasDecompress;
        var hasImport;
        if ($scope.selectedOptions1 == "0") {
            hasDecompress = false;
        } else if ($scope.selectedOptions1 == "1") {
            hasDecompress = true;
        }
        if ($scope.selectedOptions2 == "0") {
            hasImport = false;
        } else if ($scope.selectedOptions2 == "1") {
            hasImport = true;
        }
        var params = {
            caseId: $scope.caseId,
            fileType: $scope.fileType,
            fileName: $scope.fileName,
            hasDecompress: hasDecompress,
            hasImport: hasImport
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.obj = myservice.setSerialNumber(temp.obj);

            angular.forEach($scope.obj, function (e) {
                //console.log(e.id);
                isArr.push(e);
            })

            $("#pleaseWait").hide();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }


    //获取勾选数组
    $scope.selectOne = function (item) {
        if (item.checked) {
            $scope.selected.push(item.id);
        } else {
            $scope.selected.splice($scope.selected.indexOf(item.id), 1);
        }
    }

    //数据同步
    $scope.dataSyncOne = function (item) {
        item.doImport = true;
        item.hasImport = true;
        var url = "/file/dataSync?selected=" + item.id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.deleteOne = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/file/deleteFile?selected=" + id + ",";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.deleteSelected = function () {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/file/deleteFile?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.selectAll = function (checked) {
        $scope.selected = [];
        if (checked) {
            angular.forEach($scope.obj, function (e) {
                e.checked = true;
                $scope.selected.push(e.id);
            })
        } else {
            angular.forEach($scope.obj, function (e) {
                e.checked = false;
                $scope.selected.splice($scope.selected.indexOf(e.id), 1);
            })
        }
    }

    //获取所有的模板组
    $scope.templateGroups = [];
    $scope.getTemplateGroup = function () {
        var url = "/fileTemplateGroup/getFileTemplateGroupDtoListByThisUserId";
        var params = {}
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            angular.forEach(temp.obj, function (e) {
                $scope.templateGroups.push(e.groupName + "::" + e.groupId);
            })

        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }
    $scope.getTemplateGroup();

    //保存和修改配置的目模板组
    $scope.changeFlag = function (item, selectName) {
        console.log(selectName);
        $scope.selectedName = selectName;
        var arr = selectName.split("::");
        var url = "/file/updateFileAttachmentTemplateGroup";
        var params = {
            id: item.id,
            templateGroupId: arr[1],
            templateGroupName: arr[0]
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }


    $scope.dataSync = function (item) {
        if ($scope.selected.length == 0) {
            alert("请先勾选");
            return;
        }
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/file/dataSync?selected=" + $scope.selected;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.test = function () {

        var url = "/file/test";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.doWsClient = function () {
        //获取文件服务器ip地址
        var url = "/file/getFileServerIp";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            var ws = $websocket.$new('ws://' + temp.obj + "/schedule/" + $rootScope.user.id);
            var webSocketRequestDto = {
                userId: $rootScope.user.id
            }
            var jsonString = angular.toJson(webSocketRequestDto);
            ws.$on('$open', function () {
                ws.$emit("Hello", jsonString); // 进行注册
            }).$on('$message', function (message) { // it listents for 'incoming event'
                //console.log(message);
                var jsonObj = angular.fromJson(message);
                if (jsonObj["WS-00"]) {
                    console.log(jsonObj["WS-00"]);
                }
                if (jsonObj["WS-01"]) {

                    $scope.process = jsonObj["WS-01"];

                    angular.forEach(isArr, function (e) {
                        if (jsonObj["WS-01"][e.id]) {
                            // $scope['progress'+e.id] = "radial-bar radial-bar-info radial-bar-"+jsonObj["WS-01"][e.id]+" radial-bar-sm";
                            //$scope.progress[e.id] = jsonObj["WS-01"][e.id];
                        }
                    });
                }
            });
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.doWsClient();
    $scope.query();
});

App.controller("myFileUploadController", ['$scope', 'FileUploader', '$http', 'myservice', function ($scope, FileUploader, $http, myservice) {
    myservice.loginLockCheck();
    $("#fileUpload").hide();
    loadDictionary1 = function () {
        var url = "/fileCase/getCases";
        var params = {}
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.sites1 = temp.obj;
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    loadDictionary1();

    /*
    $scope.caseIdChanged =function(){
        if(myservice.isEmpty($scope.selectedOptions1)){
            $("#fileUpload").hide();
            return;
        }
        var url = "/file/setCaseId?caseId="+$scope.selectedOptions1;
        $http.post(url).success(function(data)
        {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if(temp.flag == "FAILED"){
                myservice.errors(temp);
            }else{
                $("#fileUpload").show();
            }


        }).error(function(data)
        {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }*/

    $scope.caseIdChanged = function () {
        if (myservice.isEmpty($scope.selectedOptions1)) {
            $("#fileUpload").hide();
            return;
        }
        $("#fileUpload").show();
    }

    var uploader = $scope.uploader = new FileUploader({
        url: '/file/uploadFile'
    });

    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function (item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 40;
        }
    });

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function (fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function (addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function (item) {
        item.url += "?caseId=" + $scope.selectedOptions1;
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function (fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function (progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function (fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function (fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function (fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function () {
        console.info('onCompleteAll');
    };

    //console.info('uploader', uploader);
}]);

//系统配置
App.controller("configController", function ($http, $timeout, $scope,
                                             myservice) {
    myservice.loginLockCheck();
    $scope.names = ["无效", "有效"];
    $("#pleaseWait").hide();
    // myservice.dragFunc("cnDiv");

    $("#cnDiv").hide();

    $scope.add = function () {
        $("#cnDiv").show();
        $scope.description = "";
        $scope.cfg_key = "";
        $scope.cfg_value = "";
        $scope.selectedName = "无效";
    }

    $scope.close = function () {
        $("#cnDiv").hide();
    }

    $scope.save = function () {
        var params = {
            description: $scope.description,
            cfg_key: $scope.cfg_key,
            cfg_value: $scope.cfg_value,
            flag: $scope.selectedName == "无效" ? 0 : 1,
            sortNo: $scope.sortNo
        }
        var url = "/config/saveConfig";
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getAllConfig();

        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDiv").hide();
    }

    $scope.getAllConfig = function () {
        $("#pleaseWait").show();
        var url = "/config/getAllConfigs";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            } else {
                var i = 1;
                angular.forEach(temp.obj, function (item) {
                    item.no = i++;
                });
                $scope.obj = temp.obj;
            }
            $("#pleaseWait").hide();
        }).error(function (data) {
            $("#pleaseWait").hide();
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.sortNoChange = function () {
        var reg = /^[0-9]{1,3}$/;
        if (!reg.test($scope.sortNo)) {
            $scope.sortNo = "";
            alert("请填写0-999数字！");
        }
    }

    $scope.getAllConfig();

    $scope.update = function (item) {
        var msg = "您真的确定要修改吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/config/updateConfig";
        var params = {
            id: item.id,
            description: item.description,
            cfg_key: item.cfg_key,
            cfg_value: item.cfg_value,
            flag: item.flag,
            sortNo: item.sortNo
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.getAllConfig();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.delete = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/config/deleteConfig?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.getAllConfig();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.changeFlag = function (item, selectedName) {
        if (selectedName == "有效") {
            item.flag = 1;
        } else if (selectedName == "无效") {
            item.flag = 0;
        }
    }
});

//系统配置
App.controller("dictionaryController", function ($http, $timeout, $scope,
                                                 myservice) {
    myservice.loginLockCheck();
    myservice.dragFunc("cnDivAdd");
    myservice.dragFunc("cnDivUpdate");
    $("#cnDivAdd").hide();
    $("#cnDivUpdate").hide();

    $scope.add = function () {
        $("#cnDivAdd").show();
        $scope.add_dicGroup = "";
        $scope.add_value = "";
        $scope.add_dicShow = "";
        $scope.add_comment = "";
    }

    $scope.close = function () {
        $("#cnDivAdd").hide();
        $("#cnDivUpdate").hide();
    }

    $scope.save = function () {
        if (myservice.isEmpty($scope.add_dicGroup)) {
            alert("请填写字典组！");
            return;
        }
        if (myservice.isEmpty($scope.add_value)) {
            alert("请填写字典值！");
            return;
        }
        if (myservice.isEmpty($scope.add_dicShow)) {
            alert("请填写需要显示的内容！");
            return;
        }
        var params = {
            dicGroup: $scope.add_dicGroup,
            value: $scope.add_value,
            dicShow: $scope.add_dicShow,
            comment: $scope.add_comment
        }
        var url = "/sysDictionary/saveSysDictionary";
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.getAll();
            $("#cnDivAdd").hide();
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
    }

    $scope.getAll = function () {
        $("#pleaseWait").show();
        if(!$scope.dicGroup){
            var url = "/sysDictionary/getAllSysDictionary";
            $http.post(url).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                if (temp.flag == "FAILED") {
                    myservice.errors(temp);
                } else {
                    var i = 1;
                    angular.forEach(temp.obj, function (item) {
                        item.no = i++;
                    });
                    $scope.obj = temp.obj;
                }
                $("#pleaseWait").hide();
            }).error(function (data) {
                $("#pleaseWait").hide();
                alert("会话已经断开或者检查网络是否正常！");
            });
        }else{
            var url = "/sysDictionary/getSysDictionaryByDicGroupLike?dicGroup="+$scope.dicGroup;
            $http.post(url).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                if (temp.flag == "FAILED") {
                    myservice.errors(temp);
                } else {
                    var i = 1;
                    angular.forEach(temp.obj, function (item) {
                        item.no = i++;
                    });
                    $scope.obj = temp.obj;
                }
                $("#pleaseWait").hide();
            }).error(function (data) {
                $("#pleaseWait").hide();
                alert("会话已经断开或者检查网络是否正常！");
            });
        }
    }

    $scope.getAll();

    $scope.updateBefore = function (item) {
        $("#cnDivUpdate").show();
        $scope.update_dicGroup = item.dicGroup;
        $scope.update_value = item.value;
        $scope.update_dicShow = item.dicShow;
        $scope.update_comment = item.comment;
        $scope.update_id = item.id;
    }


    $scope.update = function (item) {
        if (myservice.isEmpty($scope.update_dicGroup)) {
            alert("请填写字典组！");
            return;
        }
        if (myservice.isEmpty($scope.update_value)) {
            alert("请填写字典值！");
            return;
        }
        if (myservice.isEmpty($scope.update_dicShow)) {
            alert("请填写显示内容！");
            return;
        }
        var msg = "您真的确定要修改吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/sysDictionary/updateSysDictionary";
        var params = {
            id: $scope.update_id,
            dicGroup: $scope.update_dicGroup,
            value: $scope.update_value,
            dicShow: $scope.update_dicShow,
            comment: $scope.update_comment
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.getAll();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
        $("#cnDivUpdate").hide();
    }

    $scope.delete = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/sysDictionary/deleteSysDictionary?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.getAll();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
});

//访问白名单
App.controller("whiteListController", function ($http, $timeout, $scope,
                                                myservice) {
    $("#pleaseWait").hide();
    myservice.loginLockCheck();
    // myservice.dragFunc("cnDiv");
    $("#cnDiv").hide();

    $scope.add = function () {
        $("#cnDiv").show();
        $scope.s_Name = "";
        $scope.s_IdCard = "";
        $scope.s_PoliceNo = "";
        $scope.s_Ip = "";
        $scope.s_Mac = "";
        $scope.s_Comment = "";
    }

    $scope.close = function () {
        $("#cnDiv").hide();
    }

    $scope.save = function () {

        var params = {
            name: $scope.s_Name,
            idCard: $scope.s_IdCard,
            policeNo: $scope.s_PoliceNo,
            ip: $scope.s_Ip,
            mac: $scope.s_Mac,
            comment: $scope.s_Comment
        }
        var url = "/nginx/save/whiteIp";
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            $scope.query();

        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });
        $("#cnDiv").hide();
    }

    $scope.query = function () {
        $("#pleaseWait").show();
        var url = "/nginx/queryWhiteList";
        var params = {
            name: $scope.name,
            idCard: $scope.idCard,
            policeNo: $scope.policeNo,
            ip: $scope.ip,
            mac: $scope.mac
        }
        $http.post(url, params).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            } else {
                var i = 1;
                angular.forEach(temp.obj, function (item) {
                    item.no = i++;
                });
                $scope.obj = temp.obj;
            }
            $("#pleaseWait").hide();
        }).error(function (data) {
            $("#pleaseWait").hide();
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
    $scope.delete = function (id) {
        var msg = "您真的确定要删除吗？\n\n请确认！";
        if (confirm(msg) == false) {
            return;
        }
        var url = "/nginx/deleteWhiteIp?id=" + id;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.query();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.test = function () {
        var url = "/account/loginForCaTest?code=111111&sfzh=320683198810192058";
        $http.get(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

});

//访问白名单
App.controller("bussLogController", function ($http, $timeout, $scope,
                                              myservice) {
    $("#pleaseWait").hide();
    myservice.loginLockCheck();

    $scope.query = function () {
        var parm = {
            cc: "ccc",
            bb: "bbb",
            dd: false
        }
        console.log(parm.dd === undefined);
        console.log(parm.dd == null);
        if (!parm.dd) {
            console.log(true);
        }

        $("#pleaseWait").show();
        var url = "/bussLog/queryLogs";
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            } else {
                var i = 1;
                angular.forEach(temp.obj, function (item) {
                    item.no = i++;
                });
                $scope.obj = temp.obj;
            }
            $("#pleaseWait").hide();
        }).error(function (data) {
            $("#pleaseWait").hide();
            alert("会话已经断开或者检查网络是否正常！");
        });
    }
});

//用户审核
App.controller("checkUserController", function ($http, $timeout, $scope, myservice) {
    $("#pleaseWait").hide();
    myservice.loginLockCheck();
    $scope.names = ["不通过", "审核通过", "待审核", "全部"];
    var selectedStatuesIndex = 3;
    var keyWord = "";
    $scope.getUsers = function () {
        $("#pleaseWait").show();
        if (!myservice.isEmpty($scope.keyWord)) {
            keyWord = $scope.keyWord;
        } else {
            keyWord = "";
        }
        var url = "/api/account/getUsers?flag=" + selectedStatuesIndex + "&keyWord=" + keyWord;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            } else {
                var i = 1;
                angular.forEach(temp.obj, function (item) {
                    item.no = i++;
                });
                $scope.obj = temp.obj;
            }
            $("#pleaseWait").hide();
        }).error(function (data) {
            $("#pleaseWait").hide()
            alert("会话已经断开或者检查网络是否正常！");
        });

    }

    $scope.getUsers();

    $scope.delete = function (id) {
        var r = confirm("确定要删除吗？");
        if (r) {
            var url = "/api/account/deleteUser?id=" + id;
            $http.post(url).success(function (data) {
                var jsonString = angular.toJson(data);
                var temp = angular.fromJson(jsonString);
                if (temp.flag == "FAILED") {
                    myservice.errors(temp);
                } else {
                    var i = 1;
                    angular.forEach(temp.obj, function (item) {
                        item.no = i++;
                    });
                    $scope.obj = temp.obj;
                }
                $scope.getUsers();
            }).error(function (data) {
                alert("会话已经断开或者检查网络是否正常！");
            });
        }
        $scope.getUsers();
    }

    $scope.update = function (item) {
        var url = "/api/account/userCheck" + "?id=" + item.id + "&flag=" + item.flag;
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            if (temp.flag == "FAILED") {
                myservice.errors(temp);
            }
            $scope.getUsers();

        }).error(function (data) {
            alert("会话已经断开或者检查网络是否正常！");
        });
    }

    $scope.changeFlagTop = function (selectedName) {
        if (angular.equals(selectedName, "不通过")) {
            selectedStatuesIndex = 0;
        }
        if (angular.equals(selectedName, "审核通过")) {
            selectedStatuesIndex = 1;
        }
        if (angular.equals(selectedName, "待审核")) {
            selectedStatuesIndex = 2;
        }
        if (angular.equals(selectedName, "全部")) {
            selectedStatuesIndex = 3;
        }
    }

    $scope.changeFlag = function (item, selectedName) {
        if (angular.equals(selectedName, "不通过")) {
            item.flag = 0;
        }
        if (angular.equals(selectedName, "审核通过")) {
            item.flag = 1;
        }
        if (angular.equals(selectedName, "待审核")) {
            item.flag = 2;
        }
        if (angular.equals(selectedName, "全部")) {
            item.flag = 3;
        }
    }

});
/**=========================================================
 * Module: access-login.js
 * Demo for login api
 =========================================================*/
App.controller('LoginFormController', ['$scope', '$http', '$state', '$cookieStore', 'drawCodaService', 'myservice', '$rootScope', '$localStorage', function ($scope, $http, $state, $cookieStore, drawCodaService, myservice, $rootScope, $localStorage) {

    $scope.account = {};
    var code = "";
    var c = document.getElementById('drawCodaCanvas');
    $(function () {
        code = drawCodaService.drawCoda(c);
        $scope.account.userName = $cookieStore.get("uaseName");
        $scope.account.password = $cookieStore.get("password");
    });


    $scope.drawCoda = function () {
        code = drawCodaService.drawCoda(c);
    }

    $scope.login = function () {
        if (angular.isDefined($scope.account.remember) && $scope.account.remember) {
            $cookieStore.put("userName", $scope.account.userName);
            $cookieStore.put("password", $scope.account.password);
        }
        $scope.authMsg = '';
        md5Password = md5($scope.account.password);
        // if($scope.loginForm.$valid && code.toLowerCase() == $scope.verificationCode.toLowerCase()) {
        if ($scope.loginForm.$valid) {
            $http
                .post('api/account/login', {userName: $scope.account.userName, password: md5Password})
                .success(function (data) {
                    // assumes if ok, response is an object with some data, if not, a string with error
                    // customize according to your api
                    jsonString = angular.toJson(data);
                    response = angular.fromJson(jsonString);
                    if (response.flag != "SUCESS") {
                        $scope.authMsg = "";
                        angular.forEach(response.errorList, function (e) {
                            $scope.authMsg += e.errorMessage;
                            code = drawCodaService.drawCoda(c);
                        })
                    } else {
                        myservice.setCookie("irisEmail", $scope.account.email);
                        //$localStorage.userName=$scope.account.userName;
                        $localStorage.userName = response.obj.userName;
                        $localStorage.userId = response.obj.id;
                        //$rootScope.user.name = $scope.account.userName;
                        $rootScope.user.name = response.obj.userName;
                        $rootScope.user.id = response.obj.id;
                        $state.go('page.home');
                    }
                }, function (x) {
                    $scope.authMsg = 'Server Request Error';
                });
        } else {
            // set as dirty if the user click directly to login so we show the validation messages
            //$scope.loginForm.account_email.$dirty = true;
            $scope.loginForm.account_password.$dirty = true;
            $scope.authMsg = "验证码错误";
            code = drawCodaService.drawCoda(c);
        }

    };

    $scope.loginForCa = function () {
        var url = window.location.protocol + "//" + window.location.host + "/account/loginForCa";
        window.location.href = url;
    }
}]);
App.controller('HomeController', ['$scope','$http','$state','myservice','$sce', function ($scope,$http,$state,myservice,$sce) {

    //登录和锁定校验
    myservice.loginLockCheck();

    var url = '/actualCenter/getMcgcUrl';
    var sdm_url = '/actualCenter/getSdmUrl';
    var dataSrc = '';
    var sdmSrc = 'http://50.73.68.61:8666/sdm/index.jsp?userName=Sysadmin&password=11111111';
    $scope.skip = function () {
        $('.hide').removeClass('hide'); // 打开模态框
        $http.post(url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            dataSrc = data.obj;
            dataSrc = $sce.trustAsResourceUrl(dataSrc);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

        $http.post(sdm_url).success(function (data) {
            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            myservice.errors(temp);
            sdmSrc = data.obj;
            sdmSrc = $sce.trustAsResourceUrl(sdmSrc);
        }).error(function (data) {
            alert("请检查必填项是否填写！");
        });

    }
    $scope.skip();

    $('#logOut').click(function () {
        $http
            .post('api/account/logOut')
            .success(function (data) {
                // assumes if ok, response is an object with some data, if not, a string with error
                // customize according to your api
                $state.go('page.login');

            }, function (x) {
                alert('Server Request Error');
            });
    })
    $("#goPtgl").click(function(){
        $state.go('app.checkUser');
    })
    // $scope.logout=function () {
    //     $http
    //         .post('api/account/logOut')
    //         .success(function (data) {
    //             // assumes if ok, response is an object with some data, if not, a string with error
    //             // customize according to your api
    //             $state.go('page.login');
    //
    //         }, function (x) {
    //             alert('Server Request Error');
    //         });
    // };
    $(".home-body-item").on('click',function(e) {
        console.log(dataSrc);
        switch ($(this).attr('type')) {
            case 'item1':
                $state.go('app.fileUpload');
                break;
            case 'item2':
                //window.open('http://192.168.2.108:8080/sdm_web_war/index.jsp?userName=system&password=3c33c5db746c4c066b9faa398a895e77')
                window.open(dataSrc)
                break;
            case 'item3':
                window.open('http://50.73.68.61:8666/sdm/index.jsp?userName=Sysadmin&password=11111111')
                break;
            case 'item4':
                $state.go('app.dictionary');
                break;
        }
    })
}]);

App.controller('UnlockFormController', function ($scope, $http, $state, myservice) {
    $scope.lock = {};
    $scope.lock.password = "";
    $(lock = function () {
        $http
            .post('api/account/lock')
            .success(function (data) {
                jsonString = angular.toJson(data);
                response = angular.fromJson(jsonString);
                myservice.errors(response);
            }, function (x) {
                $scope.authMsg = 'Server Request Error';
            });

    });

    $scope.unlock = function () {
        if (!$scope.lock.password.trim() == "") {
            $scope.authMsg = '';
            md5Password = md5($scope.lock.password);
            $http
                .post('api/account/unlock?password=' + md5Password)
                .success(function (data) {
                    jsonString = angular.toJson(data);
                    response = angular.fromJson(jsonString);
                    if (response.flag != "SUCESS") {
                        myservice.errors(response);
                        if (response.obj == 1) {
                            $scope.alertessage = true;
                            return;
                        }
                    } else {
                        $state.go('app.dashboard');
                    }
                }, function (x) {
                    $scope.authMsg = 'Server Request Error';
                });
        } else {
            $scope.lockForm.lock_password.$dirty = true;
            $scope.lockForm.lock_password.$error.required = true;
        }
    }
});

/**=========================================================
 * Module: access-register.js
 * Demo for register account api
 =========================================================*/

App.controller('RegisterFormController', ['$scope', '$http', '$state', 'myservice', function ($scope, $http, $state, myservice) {

    // bind here all data from the form
    $scope.account = {};
    // place the message if something goes wrong
    $scope.authMsg = '';

    var level = 0;

    $scope.chkvalue = function () {
        $scope.check_password = false;
        if (myservice.isEmpty($scope.register.password) || $scope.register.password.length < 8) {
            $scope.check_password = true;
            level = 1;
        } else if (passwordLevel($scope.register.password) < 3) {
            $scope.check_password = true;
            level = 2;
        } else if (passwordLevel($scope.register.password) < 4) {
            $scope.check_password = false;
            level = 3;
        } else {
            $scope.check_password = false;
            level = 4;
        }

    }


    function passwordLevel(password) {
        var Modes = 0;
        for (i = 0; i < password.length; i++) {
            Modes |= CharMode(password.charCodeAt(i));
        }
        return bitTotal(Modes);

        //CharMode函数
        function CharMode(iN) {
            if (iN >= 48 && iN <= 57)//数字
                return 1;
            if (iN >= 65 && iN <= 90) //大写字母
                return 2;
            if ((iN >= 97 && iN <= 122) || (iN >= 65 && iN <= 90))
            //大小写
                return 4;
            else
                return 8; //特殊字符
        }

        //bitTotal函数
        function bitTotal(num) {
            modes = 0;
            for (i = 0; i < 4; i++) {
                if (num & 1) modes++;
                num >>>= 1;
            }
            return modes;
        }
    }

    $scope.register = function () {
        if (level <= 1) {
            return;
        }
        $scope.authMsg = '';
        if (!angular.isDefined($scope.register.account_password_confirm)) {
            $scope.authMsg = '请再一次输入密码';
            return;
        }
        md5Password = md5($scope.register.password);
        if ($scope.registerForm.$valid) {

            $http
                .post('api/account/register',
                    {
                        userName: $scope.account.userName,
                        idCard: $scope.account.idCard,
                        policeNo: $scope.account.policeNo,
                        password: md5Password
                    }
                )
                .success(function (data) {
                    // assumes if ok, response is an object with some data, if not, a string with error
                    // customize according to your api
                    jsonString = angular.toJson(data);
                    response = angular.fromJson(jsonString);
                    if (response.flag != "SUCESS") {
                        angular.forEach(response.errorList, function (e) {
                            $scope.authMsg += e.errorMessage;
                        });
                        $scope.authMsg += response.message;
                    } else {
                        $state.go('page.login');
                    }
                }, function (x) {
                    $scope.authMsg = 'Server Request Error';
                });
        } else {
            // set as dirty if the user click directly to login so we show the validation messages
            //$scope.registerForm.account_email.$dirty = true;
            $scope.registerForm.account_password.$dirty = true;
            $scope.registerForm.account_agreed.$dirty = true;
            $scope.check_password = false;

        }
    };

}]);

App.controller('recoverFormController', ['$scope', '$http', '$state', 'myservice', function ($scope, $http, $state, myservice) {

    // bind here all data from the form
    $scope.account = {};
    // place the message if something goes wrong
    $scope.authMsg = '';

    var level = 0;

    $scope.chkvalue = function () {
        $scope.check_password = false;
        if (myservice.isEmpty($scope.register.password) || $scope.register.password.length < 8) {
            $scope.check_password = true;
            level = 1;
        } else if (passwordLevel($scope.register.password) < 3) {
            $scope.check_password = true;
            level = 2;
        } else if (passwordLevel($scope.register.password) < 4) {
            $scope.check_password = false;
            level = 3;
        } else {
            $scope.check_password = false;
            level = 4;
        }

    }


    function passwordLevel(password) {
        var Modes = 0;
        for (i = 0; i < password.length; i++) {
            Modes |= CharMode(password.charCodeAt(i));
        }
        return bitTotal(Modes);

        //CharMode函数
        function CharMode(iN) {
            if (iN >= 48 && iN <= 57)//数字
                return 1;
            if (iN >= 65 && iN <= 90) //大写字母
                return 2;
            if ((iN >= 97 && iN <= 122) || (iN >= 65 && iN <= 90))
            //大小写
                return 4;
            else
                return 8; //特殊字符
        }

        //bitTotal函数
        function bitTotal(num) {
            modes = 0;
            for (i = 0; i < 4; i++) {
                if (num & 1) modes++;
                num >>>= 1;
            }
            return modes;
        }
    }

    $scope.recover = function () {
        alert(1111);
        if (level <= 1) {
            return;
        }
        $scope.authMsg = '';
        if (!angular.isDefined($scope.register.account_password_confirm)) {
            $scope.authMsg = '请再一次输入密码';
            return;
        }
        oldMd5Password = md5($scope.account.oldPassword);
        md5Password = md5($scope.register.password);
        if ($scope.recoverForm.$valid) {

            $http
                .post('api/account/restPassword',
                    {
                        userName: $scope.account.userName,
                        oldPassword: oldMd5Password,
                        password: md5Password
                    }
                )
                .success(function (data) {
                    // assumes if ok, response is an object with some data, if not, a string with error
                    // customize according to your api
                    jsonString = angular.toJson(data);
                    response = angular.fromJson(jsonString);
                    if (response.flag != "SUCESS") {
                        angular.forEach(response.errorList, function (e) {
                            $scope.authMsg += e.errorMessage;
                        });
                        $scope.authMsg += response.message;
                    } else {
                        $state.go('page.login');
                    }
                }, function (x) {
                    $scope.authMsg = 'Server Request Error';
                });
        } else {
            $scope.recoverForm.account_password.$dirty = true;
            $scope.recoverForm.account_agreed.$dirty = true;
            $scope.check_password = false;

        }
    };

}]);

/**=========================================================
 * Module: angular-grid.js
 * Example for Angular Grid
 =========================================================*/

App.controller('AngularGridController', ['$scope', '$http', function ($scope, $http) {
    'use strict';

    // Basic
    var columnDefs = [
        {displayName: 'Athlete', field: 'athlete', width: 150},
        {displayName: 'Age', field: 'age', width: 90},
        {displayName: 'Country', field: 'country', width: 120},
        {displayName: 'Year', field: 'year', width: 90},
        {displayName: 'Date', field: 'date', width: 110},
        {displayName: 'Sport', field: 'sport', width: 110},
        {displayName: 'Gold', field: 'gold', width: 100},
        {displayName: 'Silver', field: 'silver', width: 100},
        {displayName: 'Bronze', field: 'bronze', width: 100},
        {displayName: 'Total', field: 'total', width: 100}
    ];

    $scope.gridOptions = {
        columnDefs: columnDefs,
        rowData: null,
        ready: function (api) {
            api.sizeColumnsToFit();
        }
    };

    // Filter Example
    var irishAthletes = ['John Joe Nevin', 'Katie Taylor', 'Paddy Barnes', 'Kenny Egan', 'Darren Sutherland', 'Margaret Thatcher', 'Tony Blair', 'Ronald Regan', 'Barack Obama'];

    var columnDefsFilter = [
        {
            displayName: 'Athlete', field: 'athlete', width: 150, filter: 'set',
            filterParams: {cellHeight: 20, values: irishAthletes}
        },
        {displayName: 'Age', field: 'age', width: 90, filter: 'number'},
        {displayName: 'Country', field: 'country', width: 120},
        {displayName: 'Year', field: 'year', width: 90},
        {displayName: 'Date', field: 'date', width: 110},
        {displayName: 'Sport', field: 'sport', width: 110},
        {displayName: 'Gold', field: 'gold', width: 100, filter: 'number'},
        {displayName: 'Silver', field: 'silver', width: 100, filter: 'number'},
        {displayName: 'Bronze', field: 'bronze', width: 100, filter: 'number'},
        {displayName: 'Total', field: 'total', width: 100, filter: 'number'}
    ];

    $scope.gridOptions1 = {
        columnDefs: columnDefsFilter,
        rowData: null,
        enableFilter: true,
        ready: function (api) {
            api.sizeColumnsToFit();
        }

    };


    // Pinning Example

    $scope.gridOptions2 = {
        columnDefs: columnDefs,
        rowData: null,
        pinnedColumnCount: 2,
        ready: function (api) {
            api.sizeColumnsToFit();
        }
    };

    //-----------------------------
    // Get the data from SERVER
    //-----------------------------

    $http.get('server/ag-owinners.json')
        .then(function (res) {
            // basic
            $scope.gridOptions.rowData = res.data;
            $scope.gridOptions.api.onNewRows();
            // filter
            $scope.gridOptions1.rowData = res.data;
            $scope.gridOptions1.api.onNewRows();
            // pinning
            $scope.gridOptions2.rowData = res.data;
            $scope.gridOptions2.api.onNewRows();
        });

}]);
/**=========================================================
 * Module: article.js
 =========================================================*/

App.controller('ArticleController', ['$scope', function ($scope) {

    $scope.htmlContent = 'Article content...';

    $scope.postDemo = {};
    $scope.postDemo.tags = ['coding', 'less'];
    $scope.availableTags = ['coding', 'less', 'sass', 'angularjs', 'node', 'expressJS'];
    $scope.postDemo.categories = ['JAVASCRIPT', 'WEB'];
    $scope.availableCategories = ['JAVASCRIPT', 'WEB', 'BOOTSTRAP', 'SERVER', 'HTML5', 'CSS'];

    $scope.reviewers = [
        {name: 'Adam', email: 'adam@email.com', age: 10},
        {name: 'Amalie', email: 'amalie@email.com', age: 12},
        {name: 'Wladimir', email: 'wladimir@email.com', age: 30},
        {name: 'Samantha', email: 'samantha@email.com', age: 31},
        {name: 'Estefanía', email: 'estefanía@email.com', age: 16},
        {name: 'Natasha', email: 'natasha@email.com', age: 54},
        {name: 'Nicole', email: 'nicole@email.com', age: 43},
        {name: 'Adrian', email: 'adrian@email.com', age: 21}
    ];


    $scope.alerts = [
        {
            type: 'info',
            msg: 'There is an autosaved version of this article that is more recent than the version below. <a href="#" class="text-white">Restore</a>'
        }
    ];

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

}]);

/**
 * AngularJS default filter with the following expression:
 * "person in people | filter: {name: $select.search, age: $select.search}"
 * performs a AND between 'name: $select.search' and 'age: $select.search'.
 * We want to perform a OR.
 */
App.filter('propsFilter', function () {
    return function (items, props) {
        var out = [];

        if (angular.isArray(items)) {
            items.forEach(function (item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});
/**=========================================================
 * Module: calendar-ui.js
 * This script handle the calendar demo with draggable
 * events and events creations
 =========================================================*/

App.controller('CalendarController', ['$scope', function ($scope) {
    'use strict';

    if (!$.fn.fullCalendar) return;

    // global shared var to know what we are dragging
    var draggingEvent = null;


    /**
     * ExternalEvent object
     * @param jQuery Object elements Set of element as jQuery objects
     */
    var ExternalEvent = function (elements) {

        if (!elements) return;

        elements.each(function () {
            var $this = $(this);
            // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
            // it doesn't need to have a start or end
            var calendarEventObject = {
                title: $.trim($this.text()) // use the element's text as the event title
            };

            // store the Event Object in the DOM element so we can get to it later
            $this.data('calendarEventObject', calendarEventObject);

            // make the event draggable using jQuery UI
            $this.draggable({
                zIndex: 1070,
                revert: true, // will cause the event to go back to its
                revertDuration: 0  //  original position after the drag
            });

        });
    };

    /**
     * Invoke full calendar plugin and attach behavior
     * @param  jQuery [calElement] The calendar dom element wrapped into jQuery
     * @param  EventObject [events] An object with the event list to load when the calendar displays
     */
    function initCalendar(calElement, events) {

        // check to remove elements from the list
        var removeAfterDrop = $('#remove-after-drop');

        calElement.fullCalendar({
            isRTL: $scope.app.layout.isRTL,
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            buttonIcons: { // note the space at the beginning
                prev: ' fa fa-caret-left',
                next: ' fa fa-caret-right'
            },
            buttonText: {
                today: 'today',
                month: 'month',
                week: 'week',
                day: 'day'
            },
            editable: true,
            droppable: true, // this allows things to be dropped onto the calendar
            drop: function (date, allDay) { // this function is called when something is dropped

                var $this = $(this),
                    // retrieve the dropped element's stored Event Object
                    originalEventObject = $this.data('calendarEventObject');

                // if something went wrong, abort
                if (!originalEventObject) return;

                // clone the object to avoid multiple events with reference to the same object
                var clonedEventObject = $.extend({}, originalEventObject);

                // assign the reported date
                clonedEventObject.start = date;
                clonedEventObject.allDay = allDay;
                clonedEventObject.backgroundColor = $this.css('background-color');
                clonedEventObject.borderColor = $this.css('border-color');

                // render the event on the calendar
                // the last `true` argument determines if the event "sticks"
                // (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
                calElement.fullCalendar('renderEvent', clonedEventObject, true);

                // if necessary remove the element from the list
                if (removeAfterDrop.is(':checked')) {
                    $this.remove();
                }
            },
            eventDragStart: function (event, js, ui) {
                draggingEvent = event;
            },
            // This array is the events sources
            events: events
        });
    }

    /**
     * Inits the external events panel
     * @param  jQuery [calElement] The calendar dom element wrapped into jQuery
     */
    function initExternalEvents(calElement) {
        // Panel with the external events list
        var externalEvents = $('.external-events');

        // init the external events in the panel
        new ExternalEvent(externalEvents.children('div'));

        // External event color is danger-red by default
        var currColor = '#f6504d';
        // Color selector button
        var eventAddBtn = $('.external-event-add-btn');
        // New external event name input
        var eventNameInput = $('.external-event-name');
        // Color switchers
        var eventColorSelector = $('.external-event-color-selector .circle');

        // Trash events Droparea
        $('.external-events-trash').droppable({
            accept: '.fc-event',
            activeClass: 'active',
            hoverClass: 'hovered',
            tolerance: 'touch',
            drop: function (event, ui) {

                // You can use this function to send an ajax request
                // to remove the event from the repository

                if (draggingEvent) {
                    var eid = draggingEvent.id || draggingEvent._id;
                    // Remove the event
                    calElement.fullCalendar('removeEvents', eid);
                    // Remove the dom element
                    ui.draggable.remove();
                    // clear
                    draggingEvent = null;
                }
            }
        });

        eventColorSelector.click(function (e) {
            e.preventDefault();
            var $this = $(this);

            // Save color
            currColor = $this.css('background-color');
            // De-select all and select the current one
            eventColorSelector.removeClass('selected');
            $this.addClass('selected');
        });

        eventAddBtn.click(function (e) {
            e.preventDefault();

            // Get event name from input
            var val = eventNameInput.val();
            // Dont allow empty values
            if ($.trim(val) === '') return;

            // Create new event element
            var newEvent = $('<div/>').css({
                'background-color': currColor,
                'border-color': currColor,
                'color': '#fff'
            })
                .html(val);

            // Prepends to the external events list
            externalEvents.prepend(newEvent);
            // Initialize the new event element
            new ExternalEvent(newEvent);
            // Clear input
            eventNameInput.val('');
        });
    }

    /**
     * Creates an array of events to display in the first load of the calendar
     * Wrap into this function a request to a source to get via ajax the stored events
     * @return Array The array with the events
     */
    function createDemoEvents() {
        // Date for the calendar events (dummy data)
        var date = new Date();
        var d = date.getDate(),
            m = date.getMonth(),
            y = date.getFullYear();

        return [
            {
                title: 'All Day Event',
                start: new Date(y, m, 1),
                backgroundColor: '#f56954', //red
                borderColor: '#f56954' //red
            },
            {
                title: 'Long Event',
                start: new Date(y, m, d - 5),
                end: new Date(y, m, d - 2),
                backgroundColor: '#f39c12', //yellow
                borderColor: '#f39c12' //yellow
            },
            {
                title: 'Meeting',
                start: new Date(y, m, d, 10, 30),
                allDay: false,
                backgroundColor: '#0073b7', //Blue
                borderColor: '#0073b7' //Blue
            },
            {
                title: 'Lunch',
                start: new Date(y, m, d, 12, 0),
                end: new Date(y, m, d, 14, 0),
                allDay: false,
                backgroundColor: '#00c0ef', //Info (aqua)
                borderColor: '#00c0ef' //Info (aqua)
            },
            {
                title: 'Birthday Party',
                start: new Date(y, m, d + 1, 19, 0),
                end: new Date(y, m, d + 1, 22, 30),
                allDay: false,
                backgroundColor: '#00a65a', //Success (green)
                borderColor: '#00a65a' //Success (green)
            },
            {
                title: 'Open Google',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                url: '//google.com/',
                backgroundColor: '#3c8dbc', //Primary (light-blue)
                borderColor: '#3c8dbc' //Primary (light-blue)
            }
        ];
    }

    // When dom ready, init calendar and events
    $(function () {

        // The element that will display the calendar
        var calendar = $('#calendar');

        var demoEvents = createDemoEvents();

        initExternalEvents(calendar);

        initCalendar(calendar, demoEvents);

    });

}]);
App.controller('AngularCarouselController', ["$scope", function ($scope) {

    $scope.colors = ["#fc0003", "#f70008", "#f2000d", "#ed0012", "#e80017", "#e3001c", "#de0021", "#d90026", "#d4002b", "#cf0030", "#c90036", "#c4003b", "#bf0040", "#ba0045", "#b5004a", "#b0004f", "#ab0054", "#a60059", "#a1005e", "#9c0063", "#960069", "#91006e", "#8c0073", "#870078", "#82007d", "#7d0082", "#780087", "#73008c", "#6e0091", "#690096", "#63009c", "#5e00a1", "#5900a6", "#5400ab", "#4f00b0", "#4a00b5", "#4500ba", "#4000bf", "#3b00c4", "#3600c9", "#3000cf", "#2b00d4", "#2600d9", "#2100de", "#1c00e3", "#1700e8", "#1200ed", "#0d00f2", "#0800f7", "#0300fc"];

    function getSlide(target, style) {
        var i = target.length;
        return {
            id: (i + 1),
            label: 'slide #' + (i + 1),
            img: 'http://lorempixel.com/1200/500/' + style + '/' + ((i + 1) % 10),
            color: $scope.colors[(i * 10) % $scope.colors.length],
            odd: (i % 2 === 0)
        };
    }

    function addSlide(target, style) {
        target.push(getSlide(target, style));
    }

    $scope.carouselIndex = 3;
    $scope.carouselIndex2 = 0;
    $scope.carouselIndex2 = 1;
    $scope.carouselIndex3 = 5;
    $scope.carouselIndex4 = 5;

    function addSlides(target, style, qty) {
        for (var i = 0; i < qty; i++) {
            addSlide(target, style);
        }
    }

    // 1st ngRepeat demo
    $scope.slides = [];
    addSlides($scope.slides, 'sports', 50);

    // 2nd ngRepeat demo
    $scope.slides2 = [];
    addSlides($scope.slides2, 'sports', 10);

    // 3rd ngRepeat demo
    $scope.slides3 = [];
    addSlides($scope.slides3, 'people', 50);

    // 4th ngRepeat demo
    $scope.slides4 = [];
    addSlides($scope.slides4, 'city', 50);


    // 5th ngRepeat demo
    $scope.slides6 = [];
    $scope.carouselIndex6 = 0;
    addSlides($scope.slides6, 'sports', 10);
    $scope.addSlide = function (at) {
        if (at === 'head') {
            $scope.slides6.unshift(getSlide($scope.slides6, 'people'));
        } else {
            $scope.slides6.push(getSlide($scope.slides6, 'people'));
        }
    };

}]);

/**=========================================================
 * Module: carousel.js
 * Controller for ChartJs
 =========================================================*/

App.controller('ChartJSController', ["$scope", "colors", function ($scope, colors) {

    // random values for demo
    var rFactor = function () {
        return Math.round(Math.random() * 100);
    };


// Line chart
// -----------------------------------

    $scope.lineData = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                label: 'My First dataset',
                fillColor: 'rgba(114,102,186,0.2)',
                strokeColor: 'rgba(114,102,186,1)',
                pointColor: 'rgba(114,102,186,1)',
                pointStrokeColor: '#fff',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(114,102,186,1)',
                data: [rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor()]
            },
            {
                label: 'My Second dataset',
                fillColor: 'rgba(35,183,229,0.2)',
                strokeColor: 'rgba(35,183,229,1)',
                pointColor: 'rgba(35,183,229,1)',
                pointStrokeColor: '#fff',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(35,183,229,1)',
                data: [rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor()]
            }
        ]
    };


    $scope.lineOptions = {
        scaleShowGridLines: true,
        scaleGridLineColor: 'rgba(0,0,0,.05)',
        scaleGridLineWidth: 1,
        bezierCurve: true,
        bezierCurveTension: 0.4,
        pointDot: true,
        pointDotRadius: 4,
        pointDotStrokeWidth: 1,
        pointHitDetectionRadius: 20,
        datasetStroke: true,
        datasetStrokeWidth: 2,
        datasetFill: true,
    };


// Bar chart
// -----------------------------------

    $scope.barData = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                fillColor: colors.byName('info'),
                strokeColor: colors.byName('info'),
                highlightFill: colors.byName('info'),
                highlightStroke: colors.byName('info'),
                data: [rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor()]
            },
            {
                fillColor: colors.byName('primary'),
                strokeColor: colors.byName('primary'),
                highlightFill: colors.byName('primary'),
                highlightStroke: colors.byName('primary'),
                data: [rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor(), rFactor()]
            }
        ]
    };

    $scope.barOptions = {
        scaleBeginAtZero: true,
        scaleShowGridLines: true,
        scaleGridLineColor: 'rgba(0,0,0,.05)',
        scaleGridLineWidth: 1,
        barShowStroke: true,
        barStrokeWidth: 2,
        barValueSpacing: 5,
        barDatasetSpacing: 1,
    };


//  Doughnut chart
// -----------------------------------

    $scope.doughnutData = [
        {
            value: 300,
            color: colors.byName('purple'),
            highlight: colors.byName('purple'),
            label: 'Purple'
        },
        {
            value: 50,
            color: colors.byName('info'),
            highlight: colors.byName('info'),
            label: 'Info'
        },
        {
            value: 100,
            color: colors.byName('yellow'),
            highlight: colors.byName('yellow'),
            label: 'Yellow'
        }
    ];

    $scope.doughnutOptions = {
        segmentShowStroke: true,
        segmentStrokeColor: '#fff',
        segmentStrokeWidth: 2,
        percentageInnerCutout: 85,
        animationSteps: 100,
        animationEasing: 'easeOutBounce',
        animateRotate: true,
        animateScale: false
    };

// Pie chart
// -----------------------------------

    $scope.pieData = [
        {
            value: 300,
            color: colors.byName('purple'),
            highlight: colors.byName('purple'),
            label: 'Purple'
        },
        {
            value: 40,
            color: colors.byName('yellow'),
            highlight: colors.byName('yellow'),
            label: 'Yellow'
        },
        {
            value: 120,
            color: colors.byName('info'),
            highlight: colors.byName('info'),
            label: 'Info'
        }
    ];

    $scope.pieOptions = {
        segmentShowStroke: true,
        segmentStrokeColor: '#fff',
        segmentStrokeWidth: 2,
        percentageInnerCutout: 0, // Setting this to zero convert a doughnut into a Pie
        animationSteps: 100,
        animationEasing: 'easeOutBounce',
        animateRotate: true,
        animateScale: false
    };

// Polar chart
// -----------------------------------

    $scope.polarData = [
        {
            value: 300,
            color: colors.byName('pink'),
            highlight: colors.byName('pink'),
            label: 'Red'
        },
        {
            value: 50,
            color: colors.byName('purple'),
            highlight: colors.byName('purple'),
            label: 'Green'
        },
        {
            value: 100,
            color: colors.byName('pink'),
            highlight: colors.byName('pink'),
            label: 'Yellow'
        },
        {
            value: 140,
            color: colors.byName('purple'),
            highlight: colors.byName('purple'),
            label: 'Grey'
        },
    ];

    $scope.polarOptions = {
        scaleShowLabelBackdrop: true,
        scaleBackdropColor: 'rgba(255,255,255,0.75)',
        scaleBeginAtZero: true,
        scaleBackdropPaddingY: 1,
        scaleBackdropPaddingX: 1,
        scaleShowLine: true,
        segmentShowStroke: true,
        segmentStrokeColor: '#fff',
        segmentStrokeWidth: 2,
        animationSteps: 100,
        animationEasing: 'easeOutBounce',
        animateRotate: true,
        animateScale: false
    };


// Radar chart
// -----------------------------------

    $scope.radarData = {
        labels: ['Eating', 'Drinking', 'Sleeping', 'Designing', 'Coding', 'Cycling', 'Running'],
        datasets: [
            {
                label: 'My First dataset',
                fillColor: 'rgba(114,102,186,0.2)',
                strokeColor: 'rgba(114,102,186,1)',
                pointColor: 'rgba(114,102,186,1)',
                pointStrokeColor: '#fff',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(114,102,186,1)',
                data: [65, 59, 90, 81, 56, 55, 40]
            },
            {
                label: 'My Second dataset',
                fillColor: 'rgba(151,187,205,0.2)',
                strokeColor: 'rgba(151,187,205,1)',
                pointColor: 'rgba(151,187,205,1)',
                pointStrokeColor: '#fff',
                pointHighlightFill: '#fff',
                pointHighlightStroke: 'rgba(151,187,205,1)',
                data: [28, 48, 40, 19, 96, 27, 100]
            }
        ]
    };

    $scope.radarOptions = {
        scaleShowLine: true,
        angleShowLineOut: true,
        scaleShowLabels: false,
        scaleBeginAtZero: true,
        angleLineColor: 'rgba(0,0,0,.1)',
        angleLineWidth: 1,
        pointLabelFontFamily: "'Arial'",
        pointLabelFontStyle: 'bold',
        pointLabelFontSize: 10,
        pointLabelFontColor: '#565656',
        pointDot: true,
        pointDotRadius: 3,
        pointDotStrokeWidth: 1,
        pointHitDetectionRadius: 20,
        datasetStroke: true,
        datasetStrokeWidth: 2,
        datasetFill: true
    };


}]);

/**=========================================================
 * Module: chartist.js
 =========================================================*/

App.controller('ChartistController', ['$scope', function ($scope) {
    'use strict';

    // Line chart
    // -----------------------------------

    $scope.lineData = {
        labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
        series: [
            [12, 9, 7, 8, 5],
            [2, 1, 3.5, 7, 3],
            [1, 3, 4, 5, 6]
        ]
    };

    $scope.lineOptions = {
        fullWidth: true,
        height: 220,
        chartPadding: {
            right: 40
        }
    };

    // Bar bipolar
    // -----------------------------------

    $scope.barBipolarOptions = {
        high: 10,
        low: -10,
        height: 220,
        axisX: {
            labelInterpolationFnc: function (value, index) {
                return index % 2 === 0 ? value : null;
            }
        }
    };
    $scope.barBipolarData = {
        labels: ['W1', 'W2', 'W3', 'W4', 'W5', 'W6', 'W7', 'W8', 'W9', 'W10'],
        series: [
            [1, 2, 4, 8, 6, -2, -1, -4, -6, -2]
        ]
    };


    // Bar horizontal
    // -----------------------------------

    $scope.barHorizontalData = {
        labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
        series: [
            [5, 4, 3, 7, 5, 10, 3],
            [3, 2, 9, 5, 4, 6, 4]
        ]
    };

    $scope.barHorizontalOptions = {
        seriesBarDistance: 10,
        reverseData: true,
        horizontalBars: true,
        height: 220,
        axisY: {
            offset: 70
        }
    };

    // Smil Animations
    // -----------------------------------

    // Let's put a sequence number aside so we can use it in the event callbacks
    var seq = 0,
        delays = 80,
        durations = 500;

    $scope.smilData = {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        series: [
            [12, 9, 7, 8, 5, 4, 6, 2, 3, 3, 4, 6],
            [4, 5, 3, 7, 3, 5, 5, 3, 4, 4, 5, 5],
            [5, 3, 4, 5, 6, 3, 3, 4, 5, 6, 3, 4],
            [3, 4, 5, 6, 7, 6, 4, 5, 6, 7, 6, 3]
        ]
    };

    $scope.smilOptions = {
        low: 0,
        height: 260
    };

    $scope.smilEvents = {
        created: function () {
            seq = 0;
        },
        draw: function (data) {
            seq++;

            if (data.type === 'line') {
                // If the drawn element is a line we do a simple opacity fade in. This could also be achieved using CSS3 animations.
                data.element.animate({
                    opacity: {
                        // The delay when we like to start the animation
                        begin: seq * delays + 1000,
                        // Duration of the animation
                        dur: durations,
                        // The value where the animation should start
                        from: 0,
                        // The value where it should end
                        to: 1
                    }
                });
            } else if (data.type === 'label' && data.axis === 'x') {
                data.element.animate({
                    y: {
                        begin: seq * delays,
                        dur: durations,
                        from: data.y + 100,
                        to: data.y,
                        // We can specify an easing function from Chartist.Svg.Easing
                        easing: 'easeOutQuart'
                    }
                });
            } else if (data.type === 'label' && data.axis === 'y') {
                data.element.animate({
                    x: {
                        begin: seq * delays,
                        dur: durations,
                        from: data.x - 100,
                        to: data.x,
                        easing: 'easeOutQuart'
                    }
                });
            } else if (data.type === 'point') {
                data.element.animate({
                    x1: {
                        begin: seq * delays,
                        dur: durations,
                        from: data.x - 10,
                        to: data.x,
                        easing: 'easeOutQuart'
                    },
                    x2: {
                        begin: seq * delays,
                        dur: durations,
                        from: data.x - 10,
                        to: data.x,
                        easing: 'easeOutQuart'
                    },
                    opacity: {
                        begin: seq * delays,
                        dur: durations,
                        from: 0,
                        to: 1,
                        easing: 'easeOutQuart'
                    }
                });
            } else if (data.type === 'grid') {

                // Using data.axis we get x or y which we can use to construct our animation definition objects
                var pos1Animation = {
                    begin: seq * delays,
                    dur: durations,
                    from: data[data.axis + '1'] - 30,
                    to: data[data.axis + '1'],
                    easing: 'easeOutQuart'
                };

                var pos2Animation = {
                    begin: seq * delays,
                    dur: durations,
                    from: data[data.axis + '2'] - 100,
                    to: data[data.axis + '2'],
                    easing: 'easeOutQuart'
                };

                var animations = {};
                animations[data.axis + '1'] = pos1Animation;
                animations[data.axis + '2'] = pos2Animation;
                animations['opacity'] = {
                    begin: seq * delays,
                    dur: durations,
                    from: 0,
                    to: 1,
                    easing: 'easeOutQuart'
                };

                data.element.animate(animations);
            }
        }
    };


    // SVG PATH animation
    // -----------------------------------

    $scope.pathData = {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        series: [
            [1, 5, 2, 5, 4, 3],
            [2, 3, 4, 8, 1, 2],
            [5, 4, 3, 2, 1, 0.5]
        ]
    };

    $scope.pathOptions = {
        low: 0,
        showArea: true,
        showPoint: false,
        fullWidth: true,
        height: 260
    };

    $scope.pathEvents = {
        draw: function (data) {
            if (data.type === 'line' || data.type === 'area') {
                data.element.animate({
                    d: {
                        begin: 2000 * data.index,
                        dur: 2000,
                        from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
                        to: data.path.clone().stringify(),
                        easing: Chartist.Svg.Easing.easeOutQuint
                    }
                });
            }
        }
    };

}]);

/**=========================================================
 * Module: code-editor.js
 * Codemirror code editor controller
 =========================================================*/

App.controller('CodeEditorController', ['$scope', '$http', '$ocLazyLoad', function ($scope, $http, $ocLazyLoad) {

    $scope.editorThemes = ['3024-day', '3024-night', 'ambiance-mobile', 'ambiance', 'base16-dark', 'base16-light', 'blackboard', 'cobalt', 'eclipse', 'elegant', 'erlang-dark', 'lesser-dark', 'mbo', 'mdn-like', 'midnight', 'monokai', 'neat', 'neo', 'night', 'paraiso-dark', 'paraiso-light', 'pastel-on-dark', 'rubyblue', 'solarized', 'the-matrix', 'tomorrow-night-eighties', 'twilight', 'vibrant-ink', 'xq-dark', 'xq-light'];

    $scope.editorOpts = {
        mode: 'javascript',
        lineNumbers: true,
        matchBrackets: true,
        theme: 'mbo',
        viewportMargin: Infinity
    };

    $scope.refreshEditor = 0;

    // Load dinamically the stylesheet for the selected theme
    // You can use ozLazyLoad to load also the mode js based
    // on the file extension that is loaded (see handle_filetree)
    $scope.loadTheme = function () {
        var BASE = 'vendor/codemirror/theme/';
        $ocLazyLoad.load(BASE + $scope.editorOpts.theme + '.css');
        $scope.refreshEditor = !$scope.refreshEditor;
    };
    // load default theme
    $scope.loadTheme($scope.editorOpts.theme);
    // Add some initial text
    $scope.code = "// Open a file from the left menu \n" +
        "// It will be requested to the server and loaded into the editor\n" +
        "// Also try adding a New File from the toolbar\n";


    // Tree

    var selectedBranch;
    $scope.handle_filetree = function (branch) {

        selectedBranch = branch;

        var basePath = 'server/editor/';
        var isFolder = !!branch.children.length;

        console.log("You selected: " + branch.label + ' - isFolder? ' + isFolder);

        if (!isFolder) {

            $http
                .get(basePath + branch.path)
                .success(function (response) {

                    console.log('Loaded.. ' + branch.path);
                    // set the new code into the editor
                    $scope.code = response;

                    $scope.editorOpts.mode = detectMode(branch.path);
                    console.log('Mode is: ' + $scope.editorOpts.mode);

                });
        }
    };

    function detectMode(file) {
        var ext = file.split('.');
        ext = ext ? ext[ext.length - 1] : '';
        switch (ext) {
            case 'html':
                return 'htmlmixed';
            case 'css':
                return 'css';
            default:
                return 'javascript';
        }
    }

    var tree;
    tree = $scope.filetree = {};

    // Adds a new branch to the tree
    $scope.new_filetree = function () {
        var b;
        b = tree.get_selected_branch();

        // if we select a leaf -> select the parent folder
        if (b && b.children.length === 0) {
            b = tree.get_parent_branch(b);
        }

        return tree.add_branch(b, {
            "label": "another.html",
            "path": "source/another.html"
        });
    };

}]).service('LoadTreeService', ["$resource", function ($resource) {
    return $resource('server/editor/filetree.json');
}]);

/**=========================================================
 * Module: datatable,js
 * Angular Datatable controller
 =========================================================*/

App.controller('DataTableController', ['$scope', '$resource', 'DTOptionsBuilder', 'DTColumnDefBuilder',
    function ($scope, $resource, DTOptionsBuilder, DTColumnDefBuilder) {
        'use strict';

        // Ajax

        $resource('server/datatable.json').query().$promise.then(function (persons) {
            $scope.persons = persons;
        });

        // Changing data

        $scope.heroes = [{
            "id": 860,
            "firstName": "Superman",
            "lastName": "Yoda"
        }, {
            "id": 870,
            "firstName": "Ace",
            "lastName": "Ventura"
        }, {
            "id": 590,
            "firstName": "Flash",
            "lastName": "Gordon"
        }, {
            "id": 803,
            "firstName": "Luke",
            "lastName": "Skywalker"
        }
        ];

        $scope.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers');
        $scope.dtColumnDefs = [
            DTColumnDefBuilder.newColumnDef(0),
            DTColumnDefBuilder.newColumnDef(1),
            DTColumnDefBuilder.newColumnDef(2),
            DTColumnDefBuilder.newColumnDef(3).notSortable()
        ];
        $scope.person2Add = _buildPerson2Add(1);
        $scope.addPerson = addPerson;
        $scope.modifyPerson = modifyPerson;
        $scope.removePerson = removePerson;

        function _buildPerson2Add(id) {
            return {
                id: id,
                firstName: 'Foo' + id,
                lastName: 'Bar' + id
            };
        }

        function addPerson() {
            $scope.heroes.push(angular.copy($scope.person2Add));
            $scope.person2Add = _buildPerson2Add($scope.person2Add.id + 1);
        }

        function modifyPerson(index) {
            $scope.heroes.splice(index, 1, angular.copy($scope.person2Add));
            $scope.person2Add = _buildPerson2Add($scope.person2Add.id + 1);
        }

        function removePerson(index) {
            $scope.heroes.splice(index, 1);
        }

    }]);
/**=========================================================
 * Module: demo-alerts.js
 * Provides a simple demo for pagination
 =========================================================*/

App.controller('AlertDemoCtrl', ['$scope', function AlertDemoCtrl($scope) {

    $scope.alerts = [
        {type: 'danger', msg: 'Oh snap! Change a few things up and try submitting again.'},
        {type: 'warning', msg: 'Well done! You successfully read this important alert message.'}
    ];

    $scope.addAlert = function () {
        $scope.alerts.push({msg: 'Another alert!'});
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

}]);
/**=========================================================
 * Module: demo-buttons.js
 * Provides a simple demo for buttons actions
 =========================================================*/

App.controller('ButtonsCtrl', ['$scope', function ($scope) {

    $scope.singleModel = 1;

    $scope.radioModel = 'Middle';

    $scope.checkModel = {
        left: false,
        middle: true,
        right: false
    };

}]);
/**=========================================================
 * Module: demo-carousel.js
 * Provides a simple demo for bootstrap ui carousel
 =========================================================*/

App.controller('CarouselDemoCtrl', ['$scope', function ($scope) {
    $scope.myInterval = 5000;
    var slides = $scope.slides = [];
    $scope.addSlide = function () {
        var newWidth = 800 + slides.length;
        slides.push({
            image: '//placekitten.com/' + newWidth + '/300',
            text: ['More', 'Extra', 'Lots of', 'Surplus'][slides.length % 2] + ' ' +
                ['Cats', 'Kittys', 'Felines', 'Cutes'][slides.length % 2]
        });
    };
    for (var i = 0; i < 2; i++) {
        $scope.addSlide();
    }
}]);
/**=========================================================
 * Module: demo-datepicker.js
 * Provides a simple demo for bootstrap datepicker
 =========================================================*/

App.controller('DatepickerDemoCtrl', ['$scope', function ($scope) {
    $scope.today = function () {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function (date, mode) {
        return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
    };

    $scope.toggleMin = function () {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.initDate = new Date('2016-15-20');
    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

}]);

/**=========================================================
 * Module: demo-dialog.js
 * Demo for multiple ngDialog Usage
 * - ngDialogProvider for default values not supported
 *   using lazy loader. Include plugin in base.js instead.
 =========================================================*/

// Called from the route state. 'tpl' is resolved before
App.controller('DialogIntroCtrl', ['$scope', 'ngDialog', 'tpl', function ($scope, ngDialog, tpl) {
    'user strict';

    // share with other controllers
    $scope.tpl = tpl;
    // open dialog window
    ngDialog.open({
        template: tpl.path,
        // plain: true,
        className: 'ngdialog-theme-default'
    });

}]);

// Loads from view
App.controller('DialogMainCtrl', ["$scope", "$rootScope", "ngDialog", function ($scope, $rootScope, ngDialog) {
    'user strict';

    $rootScope.jsonData = '{"foo": "bar"}';
    $rootScope.theme = 'ngdialog-theme-default';

    $scope.directivePreCloseCallback = function (value) {
        if (confirm('Close it? MainCtrl.Directive. (Value = ' + value + ')')) {
            return true;
        }
        return false;
    };

    $scope.preCloseCallbackOnScope = function (value) {
        if (confirm('Close it? MainCtrl.OnScope (Value = ' + value + ')')) {
            return true;
        }
        return false;
    };

    $scope.open = function () {
        ngDialog.open({template: 'firstDialogId', controller: 'InsideCtrl', data: {foo: 'some data'}});
    };

    $scope.openDefault = function () {
        ngDialog.open({
            template: 'firstDialogId',
            controller: 'InsideCtrl',
            className: 'ngdialog-theme-default'
        });
    };

    $scope.openDefaultWithPreCloseCallbackInlined = function () {
        ngDialog.open({
            template: 'firstDialogId',
            controller: 'InsideCtrl',
            className: 'ngdialog-theme-default',
            preCloseCallback: function (value) {
                if (confirm('Close it?  (Value = ' + value + ')')) {
                    return true;
                }
                return false;
            }
        });
    };

    $scope.openConfirm = function () {
        ngDialog.openConfirm({
            template: 'modalDialogId',
            className: 'ngdialog-theme-default'
        }).then(function (value) {
            console.log('Modal promise resolved. Value: ', value);
        }, function (reason) {
            console.log('Modal promise rejected. Reason: ', reason);
        });
    };

    $scope.openConfirmWithPreCloseCallbackOnScope = function () {
        ngDialog.openConfirm({
            template: 'modalDialogId',
            className: 'ngdialog-theme-default',
            preCloseCallback: 'preCloseCallbackOnScope',
            scope: $scope
        }).then(function (value) {
            console.log('Modal promise resolved. Value: ', value);
        }, function (reason) {
            console.log('Modal promise rejected. Reason: ', reason);
        });
    };

    $scope.openConfirmWithPreCloseCallbackInlinedWithNestedConfirm = function () {
        ngDialog.openConfirm({
            template: 'dialogWithNestedConfirmDialogId',
            className: 'ngdialog-theme-default',
            preCloseCallback: function (value) {

                var nestedConfirmDialog = ngDialog.openConfirm({
                    template:
                        '<p>Are you sure you want to close the parent dialog?</p>' +
                        '<div>' +
                        '<button type="button" class="btn btn-default" ng-click="closeThisDialog(0)">No' +
                        '<button type="button" class="btn btn-primary" ng-click="confirm(1)">Yes' +
                        '</button></div>',
                    plain: true,
                    className: 'ngdialog-theme-default'
                });

                return nestedConfirmDialog;
            },
            scope: $scope
        })
            .then(function (value) {
                console.log('resolved:' + value);
                // Perform the save here
            }, function (value) {
                console.log('rejected:' + value);

            });
    };

    $scope.openInlineController = function () {
        $rootScope.theme = 'ngdialog-theme-default';

        ngDialog.open({
            template: 'withInlineController',
            controller: ['$scope', '$timeout', function ($scope, $timeout) {
                var counter = 0;
                var timeout;

                function count() {
                    $scope.exampleExternalData = 'Counter ' + (counter++);
                    timeout = $timeout(count, 450);
                }

                count();
                $scope.$on('$destroy', function () {
                    $timeout.cancel(timeout);
                });
            }],
            className: 'ngdialog-theme-default'
        });
    };

    $scope.openTemplate = function () {
        $scope.value = true;

        ngDialog.open({
            template: $scope.tpl.path,
            className: 'ngdialog-theme-default',
            scope: $scope
        });
    };

    $scope.openTemplateNoCache = function () {
        $scope.value = true;

        ngDialog.open({
            template: $scope.tpl.path,
            className: 'ngdialog-theme-default',
            scope: $scope,
            cache: false
        });
    };

    $scope.openTimed = function () {
        var dialog = ngDialog.open({
            template: '<p>Just passing through!</p>',
            plain: true,
            closeByDocument: false,
            closeByEscape: false
        });
        setTimeout(function () {
            dialog.close();
        }, 2000);
    };

    $scope.openNotify = function () {
        var dialog = ngDialog.open({
            template:
                '<p>You can do whatever you want when I close, however that happens.</p>' +
                '<div><button type="button" class="btn btn-primary" ng-click="closeThisDialog(1)">Close Me</button></div>',
            plain: true
        });
        dialog.closePromise.then(function (data) {
            console.log('ngDialog closed' + (data.value === 1 ? ' using the button' : '') + ' and notified by promise: ' + data.id);
        });
    };

    $scope.openWithoutOverlay = function () {
        ngDialog.open({
            template: '<h2>Notice that there is no overlay!</h2>',
            className: 'ngdialog-theme-default',
            plain: true,
            overlay: false
        });
    };

    $rootScope.$on('ngDialog.opened', function (e, $dialog) {
        console.log('ngDialog opened: ' + $dialog.attr('id'));
    });

    $rootScope.$on('ngDialog.closed', function (e, $dialog) {
        console.log('ngDialog closed: ' + $dialog.attr('id'));
    });

    $rootScope.$on('ngDialog.closing', function (e, $dialog) {
        console.log('ngDialog closing: ' + $dialog.attr('id'));
    });
}]);

App.controller('InsideCtrl', ["$scope", "ngDialog", function ($scope, ngDialog) {
    'user strict';
    $scope.dialogModel = {
        message: 'message from passed scope'
    };
    $scope.openSecond = function () {
        ngDialog.open({
            template: '<p class="lead m0"><a href="" ng-click="closeSecond()">Close all by click here!</a></h3>',
            plain: true,
            closeByEscape: false,
            controller: 'SecondModalCtrl'
        });
    };
}]);

App.controller('SecondModalCtrl', ["$scope", "ngDialog", function ($scope, ngDialog) {
    'user strict';
    $scope.closeSecond = function () {
        ngDialog.close();
    };
}]);

App.controller('FormDemoCtrl', ["$scope", "$resource", function ($scope, $resource) {
    'use strict';

    // the following allow to request array $resource instead of object (default)
    var actions = {'get': {method: 'GET', isArray: true}};

    // Tags inputs
    // -----------------------------------
    var Cities = $resource('server/cities.json', {}, actions);

    Cities.get(function (data) {

        $scope.cities = data;

    });
    // for non ajax form just fill the scope variable
    // $scope.cities = ['Amsterdam','Washington','Sydney','Beijing','Cairo'];

    // Slider demo values
    $scope.slider1 = 5;
    $scope.slider2 = 10;
    $scope.slider3 = 15;
    $scope.slider4 = 20;
    $scope.slider5 = 25;
    $scope.slider6 = 30;
    $scope.slider7 = 10;
    $scope.slider8 = [250, 750];

    // Chosen data
    // -----------------------------------

    var States = $resource('server/chosen-states.json', {}, {'query': {method: 'GET', isArray: true}});

    $scope.states = States.query();


    $scope.alertSubmit = function () {
        alert('Form submitted!');
        return false;
    };

    // Angular wysiwyg
    // -----------------------------------

    $scope.wysiwygContent = '<p> Write something here.. </p>';

    // Text Angular (wysiwyg)
    // -----------------------------------

    $scope.htmlContent = '<h2>Try me!</h2><p>textAngular is a super cool WYSIWYG Text Editor directive for AngularJS</p><p><b>Features:</b></p><ol><li>Automatic Seamless Two-Way-Binding</li><li style="color: blue;">Super Easy <b>Theming</b> Options</li><li>Simple Editor Instance Creation</li><li>Safely Parses Html for Custom Toolbar Icons</li><li>Doesn&apos;t Use an iFrame</li><li>Works with Firefox, Chrome, and IE8+</li></ol><p><a href="https://github.com/fraywing/textAngular">Source</a> </p>';


}]);
/**=========================================================
 * Module: demo-notify.js
 * Provides a simple demo for notify
 =========================================================*/

App.controller('NotifyDemoCtrl', ['$scope', 'Notify', '$timeout', function AlertDemoCtrl($scope, Notify, $timeout) {

    $scope.msgHtml = "<em class='fa fa-check'></em> Message with icon..";

    $scope.notifyMsg = "Some messages here..";
    $scope.notifyOpts = {
        status: 'danger',
        pos: 'bottom-center'
    };

    // Service usage example
    $timeout(function () {

        Notify.alert(
            'This is a custom message from notify..',
            {status: 'success'}
        );

    }, 500);


}]);
/**=========================================================
 * Module: demo-pagination.js
 * Provides a simple demo for pagination
 =========================================================*/

App.controller('PaginationDemoCtrl', ['$scope', function ($scope) {
    $scope.totalItems = 64;
    $scope.currentPage = 4;

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        console.log('Page changed to: ' + $scope.currentPage);
    };

    $scope.itemsPerPage = 20;
    $scope.maxSize = 10;
    $scope.bigTotalItems = 300;
    $scope.bigCurrentPage = 1;
}]);
/**=========================================================
 * Module: demo-panels.js
 * Provides a simple demo for panel actions
 =========================================================*/

App.controller('PanelsCtrl', ['$scope', '$timeout', function ($scope, $timeout) {

    // PANEL COLLAPSE EVENTS
    // -----------------------------------

    // We can use panel id name for the boolean flag to [un]collapse the panel
    $scope.$watch('panelDemo1', function (newVal) {

        console.log('panelDemo1 collapsed: ' + newVal);

    });


    // PANEL DISMISS EVENTS
    // -----------------------------------

    // Before remove panel
    $scope.$on('panel-remove', function (event, id, deferred) {

        console.log('Panel #' + id + ' removing');

        // Here is obligatory to call the resolve() if we pretend to remove the panel finally
        // Not calling resolve() will NOT remove the panel
        // It's up to your app to decide if panel should be removed or not
        deferred.resolve();

    });

    // Panel removed ( only if above was resolved() )
    $scope.$on('panel-removed', function (event, id) {

        console.log('Panel #' + id + ' removed');

    });


    // PANEL REFRESH EVENTS
    // -----------------------------------

    $scope.$on('panel-refresh', function (event, id) {
        var secs = 3;

        console.log('Refreshing during ' + secs + 's #' + id);

        $timeout(function () {
            // directive listen for to remove the spinner
            // after we end up to perform own operations
            $scope.$broadcast('removeSpinner', id);

            console.log('Refreshed #' + id);

        }, 3000);

    });

    // PANELS VIA NG-REPEAT
    // -----------------------------------

    $scope.panels = [
        {
            id: 'panelRepeat1',
            title: 'Panel Title 1',
            body: 'Nulla eget lorem leo, sit amet elementum lorem. '
        },
        {
            id: 'panelRepeat2',
            title: 'Panel Title 2',
            body: 'Nulla eget lorem leo, sit amet elementum lorem. '
        },
        {
            id: 'panelRepeat3',
            title: 'Panel Title 3',
            body: 'Nulla eget lorem leo, sit amet elementum lorem. '
        }
    ];

}]);
/**=========================================================
 * Module: demo-popover.js
 * Provides a simple demo for popovers
 =========================================================*/

App.controller('PopoverDemoCtrl', ['$scope', function ($scope) {

    $scope.dynamicPopover = 'Hello, World!';
    $scope.dynamicPopoverTitle = 'Title';

}]);
/**=========================================================
 * Module: demo-progress.js
 * Provides a simple demo to animate progress bar
 =========================================================*/

App.controller('ProgressDemoCtrl', ['$scope', function ($scope) {

    $scope.max = 200;

    $scope.random = function () {
        var value = Math.floor((Math.random() * 100) + 1);
        var type;

        if (value < 25) {
            type = 'success';
        } else if (value < 50) {
            type = 'info';
        } else if (value < 75) {
            type = 'warning';
        } else {
            type = 'danger';
        }

        $scope.showWarning = (type === 'danger' || type === 'warning');

        $scope.dynamic = value;
        $scope.type = type;
    };
    $scope.random();

    $scope.randomStacked = function () {
        $scope.stacked = [];
        var types = ['success', 'info', 'warning', 'danger'];

        for (var i = 0, n = Math.floor((Math.random() * 4) + 1); i < n; i++) {
            var index = Math.floor((Math.random() * 4));
            $scope.stacked.push({
                value: Math.floor((Math.random() * 30) + 1),
                type: types[index]
            });
        }
    };
    $scope.randomStacked();
}]);
/**=========================================================
 * Module: demo-rating.js
 * Provides a demo for ratings UI
 =========================================================*/

App.controller('RatingDemoCtrl', ['$scope', function ($scope) {

    $scope.rate = 7;
    $scope.max = 10;
    $scope.isReadonly = false;

    $scope.hoveringOver = function (value) {
        $scope.overStar = value;
        $scope.percent = 100 * (value / $scope.max);
    };

    $scope.ratingStates = [
        {stateOn: 'fa fa-check', stateOff: 'fa fa-check-circle'},
        {stateOn: 'fa fa-star', stateOff: 'fa fa-star-o'},
        {stateOn: 'fa fa-heart', stateOff: 'fa fa-ban'},
        {stateOn: 'fa fa-heart'},
        {stateOff: 'fa fa-power-off'}
    ];

}]);
/**=========================================================
 * Module: demo-timepicker.js
 * Provides a simple demo for bootstrap ui timepicker
 =========================================================*/

App.controller('TimepickerDemoCtrl', ['$scope', function ($scope) {
    $scope.mytime = new Date();

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
        hstep: [1, 2, 3],
        mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = true;
    $scope.toggleMode = function () {
        $scope.ismeridian = !$scope.ismeridian;
    };

    $scope.update = function () {
        var d = new Date();
        d.setHours(14);
        d.setMinutes(0);
        $scope.mytime = d;
    };

    $scope.changed = function () {
        console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function () {
        $scope.mytime = null;
    };
}]);

/**=========================================================
 * Module: demo-toaster.js
 * Demos for toaster notifications
 =========================================================*/

App.controller('ToasterDemoCtrl', ['$scope', 'toaster', function ($scope, toaster) {

    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };

    $scope.pop = function () {
        toaster.pop($scope.toaster.type, $scope.toaster.title, $scope.toaster.text);
    };

}]);
/**=========================================================
 * Module: demo-tooltip.js
 * Provides a simple demo for tooltip
 =========================================================*/
App.controller('TooltipDemoCtrl', ['$scope', function ($scope) {

    $scope.dynamicTooltip = 'Hello, World!';
    $scope.dynamicTooltipText = 'dynamic';
    $scope.htmlTooltip = 'I\'ve been made <b>bold</b>!';

}]);
/**=========================================================
 * Module: demo-typeahead.js
 * Provides a simple demo for typeahead
 =========================================================*/

App.controller('TypeaheadCtrl', ['$scope', '$http', function ($scope, $http) {

    $scope.selected = undefined;
    $scope.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Dakota', 'North Carolina', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];
    // Any function returning a promise object can be used to load values asynchronously
    $scope.getLocation = function (val) {
        return $http.get('//maps.googleapis.com/maps/api/geocode/json', {
            params: {
                address: val,
                sensor: false
            }
        }).then(function (res) {
            var addresses = [];
            angular.forEach(res.data.results, function (item) {
                addresses.push(item.formatted_address);
            });
            return addresses;
        });
    };

    $scope.statesWithFlags = [{
        'name': 'Alabama',
        'flag': '5/5c/Flag_of_Alabama.svg/45px-Flag_of_Alabama.svg.png'
    }, {'name': 'Alaska', 'flag': 'e/e6/Flag_of_Alaska.svg/43px-Flag_of_Alaska.svg.png'}, {
        'name': 'Arizona',
        'flag': '9/9d/Flag_of_Arizona.svg/45px-Flag_of_Arizona.svg.png'
    }, {'name': 'Arkansas', 'flag': '9/9d/Flag_of_Arkansas.svg/45px-Flag_of_Arkansas.svg.png'}, {
        'name': 'California',
        'flag': '0/01/Flag_of_California.svg/45px-Flag_of_California.svg.png'
    }, {'name': 'Colorado', 'flag': '4/46/Flag_of_Colorado.svg/45px-Flag_of_Colorado.svg.png'}, {
        'name': 'Connecticut',
        'flag': '9/96/Flag_of_Connecticut.svg/39px-Flag_of_Connecticut.svg.png'
    }, {'name': 'Delaware', 'flag': 'c/c6/Flag_of_Delaware.svg/45px-Flag_of_Delaware.svg.png'}, {
        'name': 'Florida',
        'flag': 'f/f7/Flag_of_Florida.svg/45px-Flag_of_Florida.svg.png'
    }, {
        'name': 'Georgia',
        'flag': '5/54/Flag_of_Georgia_%28U.S._state%29.svg/46px-Flag_of_Georgia_%28U.S._state%29.svg.png'
    }, {'name': 'Hawaii', 'flag': 'e/ef/Flag_of_Hawaii.svg/46px-Flag_of_Hawaii.svg.png'}, {
        'name': 'Idaho',
        'flag': 'a/a4/Flag_of_Idaho.svg/38px-Flag_of_Idaho.svg.png'
    }, {'name': 'Illinois', 'flag': '0/01/Flag_of_Illinois.svg/46px-Flag_of_Illinois.svg.png'}, {
        'name': 'Indiana',
        'flag': 'a/ac/Flag_of_Indiana.svg/45px-Flag_of_Indiana.svg.png'
    }, {'name': 'Iowa', 'flag': 'a/aa/Flag_of_Iowa.svg/44px-Flag_of_Iowa.svg.png'}, {
        'name': 'Kansas',
        'flag': 'd/da/Flag_of_Kansas.svg/46px-Flag_of_Kansas.svg.png'
    }, {'name': 'Kentucky', 'flag': '8/8d/Flag_of_Kentucky.svg/46px-Flag_of_Kentucky.svg.png'}, {
        'name': 'Louisiana',
        'flag': 'e/e0/Flag_of_Louisiana.svg/46px-Flag_of_Louisiana.svg.png'
    }, {'name': 'Maine', 'flag': '3/35/Flag_of_Maine.svg/45px-Flag_of_Maine.svg.png'}, {
        'name': 'Maryland',
        'flag': 'a/a0/Flag_of_Maryland.svg/45px-Flag_of_Maryland.svg.png'
    }, {
        'name': 'Massachusetts',
        'flag': 'f/f2/Flag_of_Massachusetts.svg/46px-Flag_of_Massachusetts.svg.png'
    }, {'name': 'Michigan', 'flag': 'b/b5/Flag_of_Michigan.svg/45px-Flag_of_Michigan.svg.png'}, {
        'name': 'Minnesota',
        'flag': 'b/b9/Flag_of_Minnesota.svg/46px-Flag_of_Minnesota.svg.png'
    }, {
        'name': 'Mississippi',
        'flag': '4/42/Flag_of_Mississippi.svg/45px-Flag_of_Mississippi.svg.png'
    }, {'name': 'Missouri', 'flag': '5/5a/Flag_of_Missouri.svg/46px-Flag_of_Missouri.svg.png'}, {
        'name': 'Montana',
        'flag': 'c/cb/Flag_of_Montana.svg/45px-Flag_of_Montana.svg.png'
    }, {'name': 'Nebraska', 'flag': '4/4d/Flag_of_Nebraska.svg/46px-Flag_of_Nebraska.svg.png'}, {
        'name': 'Nevada',
        'flag': 'f/f1/Flag_of_Nevada.svg/45px-Flag_of_Nevada.svg.png'
    }, {
        'name': 'New Hampshire',
        'flag': '2/28/Flag_of_New_Hampshire.svg/45px-Flag_of_New_Hampshire.svg.png'
    }, {
        'name': 'New Jersey',
        'flag': '9/92/Flag_of_New_Jersey.svg/45px-Flag_of_New_Jersey.svg.png'
    }, {
        'name': 'New Mexico',
        'flag': 'c/c3/Flag_of_New_Mexico.svg/45px-Flag_of_New_Mexico.svg.png'
    }, {
        'name': 'New York',
        'flag': '1/1a/Flag_of_New_York.svg/46px-Flag_of_New_York.svg.png'
    }, {
        'name': 'North Carolina',
        'flag': 'b/bb/Flag_of_North_Carolina.svg/45px-Flag_of_North_Carolina.svg.png'
    }, {
        'name': 'North Dakota',
        'flag': 'e/ee/Flag_of_North_Dakota.svg/38px-Flag_of_North_Dakota.svg.png'
    }, {'name': 'Ohio', 'flag': '4/4c/Flag_of_Ohio.svg/46px-Flag_of_Ohio.svg.png'}, {
        'name': 'Oklahoma',
        'flag': '6/6e/Flag_of_Oklahoma.svg/45px-Flag_of_Oklahoma.svg.png'
    }, {'name': 'Oregon', 'flag': 'b/b9/Flag_of_Oregon.svg/46px-Flag_of_Oregon.svg.png'}, {
        'name': 'Pennsylvania',
        'flag': 'f/f7/Flag_of_Pennsylvania.svg/45px-Flag_of_Pennsylvania.svg.png'
    }, {
        'name': 'Rhode Island',
        'flag': 'f/f3/Flag_of_Rhode_Island.svg/32px-Flag_of_Rhode_Island.svg.png'
    }, {
        'name': 'South Carolina',
        'flag': '6/69/Flag_of_South_Carolina.svg/45px-Flag_of_South_Carolina.svg.png'
    }, {
        'name': 'South Dakota',
        'flag': '1/1a/Flag_of_South_Dakota.svg/46px-Flag_of_South_Dakota.svg.png'
    }, {'name': 'Tennessee', 'flag': '9/9e/Flag_of_Tennessee.svg/46px-Flag_of_Tennessee.svg.png'}, {
        'name': 'Texas',
        'flag': 'f/f7/Flag_of_Texas.svg/45px-Flag_of_Texas.svg.png'
    }, {'name': 'Utah', 'flag': 'f/f6/Flag_of_Utah.svg/45px-Flag_of_Utah.svg.png'}, {
        'name': 'Vermont',
        'flag': '4/49/Flag_of_Vermont.svg/46px-Flag_of_Vermont.svg.png'
    }, {'name': 'Virginia', 'flag': '4/47/Flag_of_Virginia.svg/44px-Flag_of_Virginia.svg.png'}, {
        'name': 'Washington',
        'flag': '5/54/Flag_of_Washington.svg/46px-Flag_of_Washington.svg.png'
    }, {
        'name': 'West Virginia',
        'flag': '2/22/Flag_of_West_Virginia.svg/46px-Flag_of_West_Virginia.svg.png'
    }, {'name': 'Wisconsin', 'flag': '2/22/Flag_of_Wisconsin.svg/45px-Flag_of_Wisconsin.svg.png'}, {
        'name': 'Wyoming',
        'flag': 'b/bc/Flag_of_Wyoming.svg/43px-Flag_of_Wyoming.svg.png'
    }];

}]);
/**=========================================================
 * Module: flot-chart.js
 * Setup options and data for flot chart directive
 =========================================================*/

App.controller('FlotChartController', ['$scope', 'ChartData', '$timeout', function ($scope, ChartData, $timeout) {
    'use strict';

    // BAR
    // -----------------------------------
    $scope.barData = ChartData.load('server/chart/bar.json');
    $scope.barOptions = {
        series: {
            bars: {
                align: 'center',
                lineWidth: 0,
                show: true,
                barWidth: 0.6,
                fill: 0.9
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: function (label, x, y) {
                return x + ' : ' + y;
            }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // BAR STACKED
    // -----------------------------------
    $scope.barStackeData = ChartData.load('server/chart/barstacked.json');
    $scope.barStackedOptions = {
        series: {
            stack: true,
            bars: {
                align: 'center',
                lineWidth: 0,
                show: true,
                barWidth: 0.6,
                fill: 0.9
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: function (label, x, y) {
                return x + ' : ' + y;
            }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 200, // optional: use it for a clear represetation
            position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // SPLINE
    // -----------------------------------
    $scope.splineData = ChartData.load('server/chart/spline.json');
    $scope.splineOptions = {
        series: {
            lines: {
                show: false
            },
            points: {
                show: true,
                radius: 4
            },
            splines: {
                show: true,
                tension: 0.4,
                lineWidth: 1,
                fill: 0.5
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: function (label, x, y) {
                return x + ' : ' + y;
            }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 150, // optional: use it for a clear represetation
            tickColor: '#eee',
            position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: function (v) {
                return v/* + ' visitors'*/;
            }
        },
        shadowSize: 0
    };

    // AREA
    // -----------------------------------
    $scope.areaData = ChartData.load('server/chart/area.json');
    $scope.areaOptions = {
        series: {
            lines: {
                show: true,
                fill: 0.8
            },
            points: {
                show: true,
                radius: 4
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: function (label, x, y) {
                return x + ' : ' + y;
            }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            tickColor: '#eee',
            position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: function (v) {
                return v + ' visitors';
            }
        },
        shadowSize: 0
    };

    // LINE
    // -----------------------------------
    $scope.lineData = ChartData.load('server/chart/line.json');
    $scope.lineOptions = {
        series: {
            lines: {
                show: true,
                fill: 0.01
            },
            points: {
                show: true,
                radius: 4
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: function (label, x, y) {
                return x + ' : ' + y;
            }
        },
        xaxis: {
            tickColor: '#eee',
            mode: 'categories'
        },
        yaxis: {
            position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // PIE
    // -----------------------------------
    $scope.pieData = ChartData.load('server/chart/pie.json');
    $scope.pieOptions = {
        series: {
            pie: {
                show: true,
                innerRadius: 0,
                label: {
                    show: true,
                    radius: 0.8,
                    formatter: function (label, series) {
                        return '<div class="flot-pie-label">' +
                            //label + ' : ' +
                            Math.round(series.percent) +
                            '%</div>';
                    },
                    background: {
                        opacity: 0.8,
                        color: '#222'
                    }
                }
            }
        }
    };

    // DONUT
    // -----------------------------------
    $scope.donutData = ChartData.load('server/chart/donut.json');
    $scope.donutOptions = {
        series: {
            pie: {
                show: true,
                innerRadius: 0.5 // This makes the donut shape
            }
        }
    };


    // REALTIME
    // -----------------------------------
    $scope.realTimeOptions = {
        series: {
            lines: {show: true, fill: true, fillColor: {colors: ['#a0e0f3', '#23b7e5']}},
            shadowSize: 0 // Drawing is faster without shadows
        },
        grid: {
            show: false,
            borderWidth: 0,
            minBorderMargin: 20,
            labelMargin: 10
        },
        xaxis: {
            tickFormatter: function () {
                return "";
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        },
        colors: ["#23b7e5"]
    };

    // Generate random data for realtime demo
    var data = [], totalPoints = 300;

    update();

    function getRandomData() {
        if (data.length > 0)
            data = data.slice(1);
        // Do a random walk
        while (data.length < totalPoints) {
            var prev = data.length > 0 ? data[data.length - 1] : 50,
                y = prev + Math.random() * 10 - 5;
            if (y < 0) {
                y = 0;
            } else if (y > 100) {
                y = 100;
            }
            data.push(y);
        }
        // Zip the generated y values with the x values
        var res = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]]);
        }
        return [res];
    }

    function update() {
        $scope.realTimeData = getRandomData();
        $timeout(update, 30);
    }

    // end random data generation


    // PANEL REFRESH EVENTS
    // -----------------------------------

    $scope.$on('panel-refresh', function (event, id) {

        console.log('Simulating chart refresh during 3s on #' + id);

        // Instead of timeout you can request a chart data
        $timeout(function () {

            // directive listen for to remove the spinner
            // after we end up to perform own operations
            $scope.$broadcast('removeSpinner', id);

            console.log('Refreshed #' + id);

        }, 3000);

    });


    // PANEL DISMISS EVENTS
    // -----------------------------------

    // Before remove panel
    $scope.$on('panel-remove', function (event, id, deferred) {

        console.log('Panel #' + id + ' removing');

        // Here is obligatory to call the resolve() if we pretend to remove the panel finally
        // Not calling resolve() will NOT remove the panel
        // It's up to your app to decide if panel should be removed or not
        deferred.resolve();

    });

    // Panel removed ( only if above was resolved() )
    $scope.$on('panel-removed', function (event, id) {

        console.log('Panel #' + id + ' removed');

    });

}]).service('ChartData', ["$resource", function ($resource) {

    var opts = {
        get: {method: 'GET', isArray: true}
    };
    return {
        load: function (source) {
            return $resource(source, {}, opts).get();
        }
    };
}]);
/**=========================================================
 * Module: form-imgcrop.js
 * Image crop controller
 =========================================================*/
App.controller('ImageCropController', ["$scope", function ($scope) {

    $scope.reset = function () {
        $scope.myImage = '';
        $scope.myCroppedImage = '';
        $scope.imgcropType = "square";
    };

    $scope.reset();

    var handleFileSelect = function (evt) {
        var file = evt.currentTarget.files[0];
        var reader = new FileReader();
        reader.onload = function (evt) {
            $scope.$apply(function ($scope) {
                $scope.myImage = evt.target.result;
            });
        };
        if (file)
            reader.readAsDataURL(file);
    };

    angular.element(document.querySelector('#fileInput')).on('change', handleFileSelect);

}]);
/**=========================================================
 * Module: FormValidationController
 * Input validation with UI Validate
 =========================================================*/

App.controller('FormValidationController', ["$scope", function ($scope) {
    'use strict';

    $scope.notBlackListed = function (value) {
        var blacklist = ['some@mail.com', 'another@email.com'];
        return blacklist.indexOf(value) === -1;
    };

    $scope.words = function (value) {
        return value && value.split(' ').length;
    };

    $scope.submitted = false;
    $scope.validateInput = function (name, type) {
        var input = $scope.formValidate[name];
        return (input.$dirty || $scope.submitted) && input.$error[type];
    };

    // Submit form
    $scope.submitForm = function () {
        $scope.submitted = true;
        if ($scope.formValidate.$valid) {
            console.log('Submitted!!');
        } else {
            console.log('Not valid!!');
            return false;
        }
    };

}]);

/**=========================================================
 * Module: form-xeditable.js
 * Form xEditable controller
 =========================================================*/

App.controller('FormxEditableController', ['$scope', 'editableOptions', 'editableThemes', '$filter', '$http',
    function ($scope, editableOptions, editableThemes, $filter, $http) {

        editableOptions.theme = 'bs3';

        editableThemes.bs3.inputClass = 'input-sm';
        editableThemes.bs3.buttonsClass = 'btn-sm';
        editableThemes.bs3.submitTpl = '<button type="submit" class="btn btn-success"><span class="fa fa-check"></span></button>';
        editableThemes.bs3.cancelTpl = '<button type="button" class="btn btn-default" ng-click="$form.$cancel()">' +
            '<span class="fa fa-times text-muted"></span>' +
            '</button>';

        $scope.user = {
            email: 'email@example.com',
            tel: '123-45-67',
            number: 29,
            range: 10,
            url: 'http://example.com',
            search: 'blabla',
            color: '#6a4415',
            date: null,
            time: '12:30',
            datetime: null,
            month: null,
            week: null,
            desc: 'Sed pharetra euismod dolor, id feugiat ante volutpat eget. '
        };

        // Local select
        // -----------------------------------

        $scope.user2 = {
            status: 2
        };

        $scope.statuses = [
            {value: 1, text: 'status1'},
            {value: 2, text: 'status2'},
            {value: 3, text: 'status3'},
            {value: 4, text: 'status4'}
        ];

        $scope.showStatus = function () {
            var selected = $filter('filter')($scope.statuses, {value: $scope.user2.status});
            return ($scope.user2.status && selected.length) ? selected[0].text : 'Not set';
        };

        // select remote
        // -----------------------------------

        $scope.user3 = {
            id: 4,
            text: 'admin' // original value
        };

        $scope.groups = [];

        $scope.loadGroups = function () {
            return $scope.groups.length ? null : $http.get('server/xeditable-groups.json').success(function (data) {
                $scope.groups = data;
            });
        };

        $scope.$watch('user3.id', function (newVal, oldVal) {
            if (newVal !== oldVal) {
                var selected = $filter('filter')($scope.groups, {id: $scope.user3.id});
                $scope.user3.text = selected.length ? selected[0].text : null;
            }
        });

        // Typeahead
        // -----------------------------------

        $scope.user4 = {
            state: 'Arizona'
        };

        $scope.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Dakota', 'North Carolina', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

    }]);
/**=========================================================
 * Module: modals.js
 * Provides a simple way to implement bootstrap modals from templates
 =========================================================*/

App.controller('ModalGmapController', ['$scope', '$modal', '$timeout', function ($scope, $modal, $timeout) {

    $scope.open = function (size) {

        var modalInstance = $modal.open({
            templateUrl: '/myModalContent.html',
            controller: ModalInstanceCtrl,
            size: size
        });
    };

    // Please note that $modalInstance represents a modal window (instance) dependency.
    // It is not the same as the $modal service used above.

    var ModalInstanceCtrl = function ($scope, $modalInstance, $timeout) {

        $modalInstance.opened.then(function () {
            var position = new google.maps.LatLng(33.790807, -117.835734);

            $scope.mapOptionsModal = {
                zoom: 14,
                center: position,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            // we use timeout to wait maps to be ready before add a markers
            $timeout(function () {
                // 1. Add a marker at the position it was initialized
                new google.maps.Marker({
                    map: $scope.myMapModal,
                    position: position
                });
                // 2. Trigger a resize so the map is redrawed
                google.maps.event.trigger($scope.myMapModal, 'resize');
                // 3. Move to the center if it is misaligned
                $scope.myMapModal.panTo(position);
            });

        });

        $scope.ok = function () {
            $modalInstance.close('closed');
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    };
    ModalInstanceCtrl.$inject = ["$scope", "$modalInstance", "$timeout"];

}]);


App.controller('GMapController', ["$scope", "$timeout", function ($scope, $timeout) {

    var position = [
        new google.maps.LatLng(33.790807, -117.835734),
        new google.maps.LatLng(33.790807, -117.835734),
        new google.maps.LatLng(33.790807, -117.835734),
        new google.maps.LatLng(33.790807, -117.835734),
        new google.maps.LatLng(33.787453, -117.835858)
    ];

    $scope.addMarker = addMarker;
    // we use timeout to wait maps to be ready before add a markers
    $timeout(function () {
        addMarker($scope.myMap1, position[0]);
        addMarker($scope.myMap2, position[1]);
        addMarker($scope.myMap3, position[2]);
        addMarker($scope.myMap5, position[3]);
    });

    $scope.mapOptions1 = {
        zoom: 14,
        center: position[0],
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        scrollwheel: false
    };

    $scope.mapOptions2 = {
        zoom: 19,
        center: position[1],
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    $scope.mapOptions3 = {
        zoom: 14,
        center: position[2],
        mapTypeId: google.maps.MapTypeId.SATELLITE
    };

    $scope.mapOptions4 = {
        zoom: 14,
        center: position[3],
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    // for multiple markers
    $timeout(function () {
        addMarker($scope.myMap4, position[3]);
        addMarker($scope.myMap4, position[4]);
    });

    // custom map style
    var MapStyles = [{
        'featureType': 'water',
        'stylers': [{'visibility': 'on'}, {'color': '#bdd1f9'}]
    }, {
        'featureType': 'all',
        'elementType': 'labels.text.fill',
        'stylers': [{'color': '#334165'}]
    }, {featureType: 'landscape', stylers: [{color: '#e9ebf1'}]}, {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: '#c5c6c6'}]
    }, {featureType: 'road.arterial', elementType: 'geometry', stylers: [{color: '#fff'}]}, {
        featureType: 'road.local',
        elementType: 'geometry',
        stylers: [{color: '#fff'}]
    }, {featureType: 'transit', elementType: 'geometry', stylers: [{color: '#d8dbe0'}]}, {
        featureType: 'poi',
        elementType: 'geometry',
        stylers: [{color: '#cfd5e0'}]
    }, {featureType: 'administrative', stylers: [{visibility: 'on'}, {lightness: 33}]}, {
        featureType: 'poi.park',
        elementType: 'labels',
        stylers: [{visibility: 'on'}, {lightness: 20}]
    }, {featureType: 'road', stylers: [{color: '#d8dbe0', lightness: 20}]}];
    $scope.mapOptions5 = {
        zoom: 14,
        center: position[3],
        styles: MapStyles,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        scrollwheel: false
    };

    ///////////////

    function addMarker(map, position) {
        return new google.maps.Marker({
            map: map,
            position: position
        });
    }

}]);
/**=========================================================
 * Module: calendar-ui.js
 * This script handle the calendar demo with draggable
 * events and events creations
 =========================================================*/

App.controller('InfiniteScrollController', ["$scope", "$timeout", function ($scope, $timeout) {

    $scope.images = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

    $scope.loadMore = function () {
        var last = $scope.images[$scope.images.length - 1];
        for (var i = 1; i <= 10; i++) {
            $scope.images.push(last + i);
        }
    };

}]).factory('datasource', [
    '$log', '$timeout', function (console, $timeout) {
        'use strict';

        var get = function (index, count, success) {
            return $timeout(function () {
                var i, result, _i, _ref;
                result = [];
                for (i = _i = index, _ref = index + count - 1; index <= _ref ? _i <= _ref : _i >= _ref; i = index <= _ref ? ++_i : --_i) {
                    result.push('item #' + i);
                }
                return success(result);
            }, 100);
        };
        return {
            get: get
        };
    }]);
/**=========================================================
 * Module: locale.js
 * Demo for locale settings
 =========================================================*/

App.controller('LocalizationController', ["$rootScope", "tmhDynamicLocale", "$locale", function ($rootScope, tmhDynamicLocale, $locale) {
    $rootScope.availableLocales = {
        'en': 'English',
        'es': 'Spanish',
        'de': 'German',
        'fr': 'French',
        'ar': 'Arabic',
        'ja': 'Japanese',
        'ko': 'Korean',
        'zh': 'Chinese'
    };

    $rootScope.model = {selectedLocale: 'en'};

    $rootScope.$locale = $locale;

    $rootScope.changeLocale = tmhDynamicLocale.set;

}]);

/**=========================================================
 * Module: demo-pagination.js
 * Provides a simple demo for pagination
 =========================================================*/

App.controller('MailboxController', ["$scope", "colors", function ($scope, colors) {


    $scope.folders = [
        {name: 'Inbox', folder: 'inbox', alert: 42, icon: "fa-inbox"},
        {name: 'Starred', folder: 'starred', alert: 10, icon: "fa-star"},
        {name: 'Sent', folder: 'sent', alert: 0, icon: "fa-paper-plane-o"},
        {name: 'Draft', folder: 'draft', alert: 5, icon: "fa-edit"},
        {name: 'Trash', folder: 'trash', alert: 0, icon: "fa-trash"}
    ];

    $scope.labels = [
        {name: 'Red', color: 'danger'},
        {name: 'Pink', color: 'pink'},
        {name: 'Blue', color: 'info'},
        {name: 'Yellow', color: 'warning'}
    ];

    $scope.mail = {
        cc: false,
        bcc: false
    };
    // Mailbox editr initial content
    $scope.content = "<p>Type something..</p>";


}]);

App.controller('MailFolderController', ['$scope', 'mails', '$stateParams', function ($scope, mails, $stateParams) {
    // no filter for inbox
    $scope.folder = $stateParams.folder === 'inbox' ? '' : $stateParams.folder;
    mails.all().then(function (mails) {
        $scope.mails = mails;
    });
}]);

App.controller('MailViewController', ['$scope', 'mails', '$stateParams', function ($scope, mails, $stateParams) {
    mails.get($stateParams.mid).then(function (mail) {
        $scope.mail = mail;
    });
}]);

// A RESTful factory for retreiving mails from 'mails.json'
App.factory('mails', ['$http', function ($http) {
    var path = 'server/mails.json';
    var mails = $http.get(path).then(function (resp) {
        return resp.data.mails;
    });

    var factory = {};
    factory.all = function () {
        return mails;
    };
    factory.get = function (id) {
        return mails.then(function (mails) {
            for (var i = 0; i < mails.length; i++) {
                if (mails[i].id == id) return mails[i];
            }
            return null;
        });
    };
    return factory;
}]);
/**=========================================================
 * Module: main.js
 * Main Application Controller
 =========================================================*/

App.controller('AppController',
    ['$rootScope', '$scope', '$state', '$http', '$translate', '$window', '$localStorage', '$timeout', 'toggleStateService', 'colors', 'browser', 'cfpLoadingBar',
        function ($rootScope, $scope, $state, $http, $translate, $window, $localStorage, $timeout, toggle, colors, browser, cfpLoadingBar) {
            "use strict";

            // Setup the layout mode
            $rootScope.app.layout.horizontal = ($rootScope.$stateParams.layout == 'app-h');

            // Loading bar transition
            // -----------------------------------
            var thBar;
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                if ($('.wrapper > section').length) // check if bar container exists
                    thBar = $timeout(function () {
                        cfpLoadingBar.start();
                    }, 0); // sets a latency Threshold
            });
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                event.targetScope.$watch("$viewContentLoaded", function () {
                    $timeout.cancel(thBar);
                    cfpLoadingBar.complete();
                });
            });


            // Hook not found
            $rootScope.$on('$stateNotFound',
                function (event, unfoundState, fromState, fromParams) {
                    console.log(unfoundState.to); // "lazy.state"
                    console.log(unfoundState.toParams); // {a:1, b:2}
                    console.log(unfoundState.options); // {inherit:false} + default options
                });
            // Hook error
            $rootScope.$on('$stateChangeError',
                function (event, toState, toParams, fromState, fromParams, error) {
                    console.log(error);
                });
            // Hook success
            $rootScope.$on('$stateChangeSuccess',
                function (event, toState, toParams, fromState, fromParams) {
                    // display new view from top
                    $window.scrollTo(0, 0);
                    // Save the route title
                    $rootScope.currTitle = $state.current.title;
                });

            $rootScope.currTitle = $state.current.title;
            $rootScope.pageTitle = function () {
                var title = $rootScope.app.name + ' - ' + ($rootScope.currTitle || $rootScope.app.description);
                document.title = title;
                return title;
            };

            // iPad may presents ghost click issues
            // if( ! browser.ipad )
            // FastClick.attach(document.body);

            // Close submenu when sidebar change from collapsed to normal
            $rootScope.$watch('app.layout.isCollapsed', function (newValue, oldValue) {
                if (newValue === false)
                    $rootScope.$broadcast('closeSidebarMenu');
            });

            // Restore layout settings
            if (angular.isDefined($localStorage.layout))
                $scope.app.layout = $localStorage.layout;
            else
                $localStorage.layout = $scope.app.layout;

            $rootScope.$watch("app.layout", function () {
                $localStorage.layout = $scope.app.layout;
            }, true);


            // Allows to use branding color with interpolation
            // {{ colorByName('primary') }}
            $scope.colorByName = colors.byName;

            // Hides/show user avatar on sidebar
            $scope.toggleUserBlock = function () {
                $scope.$broadcast('toggleUserBlock');
            };

            $scope.logOut = function () {
                $http
                    .post('api/account/logOut')
                    .success(function (data) {
                        // assumes if ok, response is an object with some data, if not, a string with error
                        // customize according to your api
                        $state.go('page.login');

                    }, function (x) {
                        alert('Server Request Error');
                    });
            };
            $scope.goHome = function () {

                $state.go('page.home');

            };

            // Internationalization
            // ----------------------

            $scope.language = {
                // Handles language dropdown
                listIsOpen: false,
                // list of available languages
                available: {
                    'zh_cn': '中文',
                    'es_AR': 'Español',
                    'en': 'English'

                },
                // display always the current ui language
                init: function () {
                    var proposedLanguage = $translate.proposedLanguage() || $translate.use();
                    var preferredLanguage = $translate.preferredLanguage(); // we know we have set a preferred one in app.config
                    $scope.language.selected = $scope.language.available[(proposedLanguage || preferredLanguage)];
                },
                set: function (localeId, ev) {
                    // Set the new idiom
                    $translate.use(localeId);
                    // save a reference for the current language
                    $scope.language.selected = $scope.language.available[localeId];
                    // finally toggle dropdown
                    $scope.language.listIsOpen = !$scope.language.listIsOpen;
                }
            };

            $scope.language.init();

            // Restore application classes state
            toggle.restoreState($(document.body));

            // cancel click event easily
            $rootScope.cancel = function ($event) {
                $event.stopPropagation();
            };

            $scope.search = function () {
                console.log($rootScope.app.searchKey);
                $state.go('app.infoSearch', {}, {reload: true});
            }
        }]);

/**=========================================================
 * Module: masonry-deck.js
 * Demo for Angular Deck
 =========================================================*/

App.controller('MasonryDeckController', ['$scope', 'RouteHelpers', function ($scope, RouteHelpers) {
    'use strict';

    $scope.basepath = RouteHelpers.basepath;

    $scope.photos = [
        {id: 'photo-1', name: 'Awesome photo', src: 'http://lorempixel.com/400/300/abstract'},
        {id: 'photo-2', name: 'Great photo', src: 'http://lorempixel.com/450/400/city'},
        {id: 'photo-3', name: 'Strange photo', src: 'http://lorempixel.com/400/300/people'},
        {id: 'photo-4', name: 'A photo?', src: 'http://lorempixel.com/400/300/transport'},
        {id: 'photo-5', name: 'What a photo', src: 'http://lorempixel.com/450/300/fashion'},
        {id: 'photo-6', name: 'Silly photo', src: 'http://lorempixel.com/400/300/technics'},
        {id: 'photo-7', name: 'Weird photo', src: 'http://lorempixel.com/410/350/sports'},
        {id: 'photo-8', name: 'Modern photo', src: 'http://lorempixel.com/400/300/nightlife'},
        {id: 'photo-9', name: 'Classical photo', src: 'http://lorempixel.com/400/300/nature'},
        {id: 'photo-10', name: 'Dynamic photo', src: 'http://lorempixel.com/420/300/abstract'},
        {id: 'photo-11', name: 'Neat photo', src: 'http://lorempixel.com/400/300/sports'},
        {id: 'photo-12', name: 'Bumpy photo', src: 'http://lorempixel.com/400/300/nightlife'},
        {id: 'photo-13', name: 'Brilliant photo', src: 'http://lorempixel.com/400/380/nature'},
        {id: 'photo-14', name: 'Excellent photo', src: 'http://lorempixel.com/480/300/technics'},
        {id: 'photo-15', name: 'Gorgeous photo', src: 'http://lorempixel.com/400/300/sports'},
        {id: 'photo-16', name: 'Lovely photo', src: 'http://lorempixel.com/400/300/nightlife'},
        {id: 'photo-17', name: 'A "wow" photo', src: 'http://lorempixel.com/400/300/nature'},
        {id: 'photo-18', name: 'Bodacious photo', src: 'http://lorempixel.com/400/300/abstract'}
    ];

}]).directive('imageloaded', [
    // Copyright(c) 2013 André König <akoenig@posteo.de>
    // MIT Licensed
    function () {

        'use strict';

        return {
            restrict: 'A',

            link: function (scope, element, attrs) {
                var cssClass = attrs.loadedclass;

                element.bind('load', function (e) {
                    angular.element(element).addClass(cssClass);
                });
            }
        }
    }
]);


/**=========================================================
 * Module: modals.js
 * Provides a simple way to implement bootstrap modals from templates
 =========================================================*/

App.controller('ModalController', ['$scope', '$modal', function ($scope, $modal) {

    $scope.open = function (size) {

        var modalInstance = $modal.open({
            templateUrl: '/myModalContent.html',
            controller: ModalInstanceCtrl,
            size: size
        });

        var state = $('#modal-state');
        modalInstance.result.then(function () {
            state.text('Modal dismissed with OK status');
        }, function () {
            state.text('Modal dismissed with Cancel status');
        });
    };

    // Please note that $modalInstance represents a modal window (instance) dependency.
    // It is not the same as the $modal service used above.

    var ModalInstanceCtrl = function ($scope, $modalInstance) {


        $scope.ok = function () {
            $modalInstance.close('closed');
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };
    ModalInstanceCtrl.$inject = ["$scope", "$modalInstance"];

}]);

/**=========================================================
 * Module: morris.js
 =========================================================*/

App.controller('ChartMorrisController', ['$scope', '$timeout', 'colors', function ($scope, $timeout, colors) {

    $scope.chartdata = [
        {y: "2006", a: 100, b: 90},
        {y: "2007", a: 75, b: 65},
        {y: "2008", a: 50, b: 40},
        {y: "2009", a: 75, b: 65},
        {y: "2010", a: 50, b: 40},
        {y: "2011", a: 75, b: 65},
        {y: "2012", a: 100, b: 90}
    ];

    /* test data update
  $timeout(function(){
    $scope.chartdata[0].a = 50;
    $scope.chartdata[0].b = 50;
  }, 3000); */

    $scope.donutdata = [
        {label: "Download Sales", value: 12},
        {label: "In-Store Sales", value: 30},
        {label: "Mail-Order Sales", value: 20}
    ];

    $scope.donutOptions = {
        colors: [colors.byName('danger'), colors.byName('yellow'), colors.byName('warning')],
        resize: true
    };

    $scope.barOptions = {
        xkey: 'y',
        ykeys: ["a", "b"],
        labels: ["Series A", "Series B"],
        xLabelMargin: 2,
        barColors: [colors.byName('info'), colors.byName('danger')],
        resize: true
    };

    $scope.lineOptions = {
        xkey: 'y',
        ykeys: ["a", "b"],
        labels: ["Serie A", "Serie B"],
        lineColors: ["#31C0BE", "#7a92a3"],
        resize: true
    };

    $scope.areaOptions = {
        xkey: 'y',
        ykeys: ["a", "b"],
        labels: ["Serie A", "Serie B"],
        lineColors: [colors.byName('purple'), colors.byName('info')],
        resize: true
    };

}]);

/**=========================================================
 * Module: access-login.js
 * Demo for login api
 =========================================================*/

App.controller('AbnTestController', ['$scope', '$timeout', '$resource', function ($scope, $timeout, $resource) {

    $scope.my_tree_handler = function (branch) {

        $scope.output = "You selected: " + branch.label;

        if (branch.data && branch.data.description) {
            $scope.output += '(' + branch.data.description + ')';
            return $scope.output;
        }
    };

    // onSelect event handlers
    var apple_selected = function (branch) {
        $scope.output = "APPLE! : " + branch.label;
        return $scope.output;
    };

    var treedata_avm = [
        {
            label: 'Animal',
            children: [
                {
                    label: 'Dog',
                    data: {
                        description: "man's best friend"
                    }
                }, {
                    label: 'Cat',
                    data: {
                        description: "Felis catus"
                    }
                }, {
                    label: 'Hippopotamus',
                    data: {
                        description: "hungry, hungry"
                    }
                }, {
                    label: 'Chicken',
                    children: ['White Leghorn', 'Rhode Island Red', 'Jersey Giant']
                }
            ]
        }, {
            label: 'Vegetable',
            data: {
                definition: "A plant or part of a plant used as food, typically as accompaniment to meat or fish, such as a cabbage, potato, carrot, or bean.",
                data_can_contain_anything: true
            },
            onSelect: function (branch) {
                $scope.output = "Vegetable: " + branch.data.definition;
                return $scope.output;
            },
            children: [
                {
                    label: 'Oranges'
                }, {
                    label: 'Apples',
                    children: [
                        {
                            label: 'Granny Smith',
                            onSelect: apple_selected
                        }, {
                            label: 'Red Delicous',
                            onSelect: apple_selected
                        }, {
                            label: 'Fuji',
                            onSelect: apple_selected
                        }
                    ]
                }
            ]
        }, {
            label: 'Mineral',
            children: [
                {
                    label: 'Rock',
                    children: ['Igneous', 'Sedimentary', 'Metamorphic']
                }, {
                    label: 'Metal',
                    children: ['Aluminum', 'Steel', 'Copper']
                }, {
                    label: 'Plastic',
                    children: [
                        {
                            label: 'Thermoplastic',
                            children: ['polyethylene', 'polypropylene', 'polystyrene', ' polyvinyl chloride']
                        }, {
                            label: 'Thermosetting Polymer',
                            children: ['polyester', 'polyurethane', 'vulcanized rubber', 'bakelite', 'urea-formaldehyde']
                        }
                    ]
                }
            ]
        }
    ];

    var treedata_geography = [
        {
            label: 'North America',
            children: [
                {
                    label: 'Canada',
                    children: ['Toronto', 'Vancouver']
                }, {
                    label: 'USA',
                    children: ['New York', 'Los Angeles']
                }, {
                    label: 'Mexico',
                    children: ['Mexico City', 'Guadalajara']
                }
            ]
        }, {
            label: 'South America',
            children: [
                {
                    label: 'Venezuela',
                    children: ['Caracas', 'Maracaibo']
                }, {
                    label: 'Brazil',
                    children: ['Sao Paulo', 'Rio de Janeiro']
                }, {
                    label: 'Argentina',
                    children: ['Buenos Aires', 'Cordoba']
                }
            ]
        }
    ];

    $scope.my_data = treedata_avm;
    $scope.try_changing_the_tree_data = function () {
        if ($scope.my_data === treedata_avm) {
            $scope.my_data = treedata_geography;
        } else {
            $scope.my_data = treedata_avm;
        }
        return $scope.my_data;
    };

    var tree;
    // This is our API control variable
    $scope.my_tree = tree = {};
    $scope.try_async_load = function () {

        $scope.my_data = [];
        $scope.doing_async = true;

        // Request tree data via $resource
        var remoteTree = $resource('server/treedata.json');

        return remoteTree.get(function (res) {

            $scope.my_data = res.data;

            $scope.doing_async = false;

            return tree.expand_all();

            // we must return a promise so the plugin
            // can watch when it's resolved
        }).$promise;
    };

    // Adds a new branch to the tree
    $scope.try_adding_a_branch = function () {
        var b;
        b = tree.get_selected_branch();
        return tree.add_branch(b, {
            label: 'New Branch',
            data: {
                something: 42,
                "else": 43
            }
        });
    };

}]);

/**=========================================================
 * Module: nestable.js
 * Nestable controller
 =========================================================*/

App.controller('NestableController', ['$scope', function ($scope) {

    'use strict';

    $scope.items = [
        {
            item: {text: 'a'},
            children: []
        },
        {
            item: {text: 'b'},
            children: [
                {
                    item: {text: 'c'},
                    children: []
                },
                {
                    item: {text: 'd'},
                    children: []
                }
            ]
        },
        {
            item: {text: 'e'},
            children: []
        },
        {
            item: {text: 'f'},
            children: []
        }
    ];

    $scope.items2 = [
        {
            item: {text: '1'},
            children: []
        },
        {
            item: {text: '2'},
            children: [
                {
                    item: {text: '3'},
                    children: []
                },
                {
                    item: {text: '4'},
                    children: []
                }
            ]
        },
        {
            item: {text: '5'},
            children: []
        },
        {
            item: {text: '6'},
            children: []
        }
    ]


}]);

/**=========================================================
 * Module: ng-grid.js
 * ngGrid demo
 =========================================================*/

App.controller('NGGridController', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {

    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    };
    $scope.totalServerItems = 0;
    $scope.pagingOptions = {
        pageSizes: [250, 500, 1000],  // page size options
        pageSize: 250,              // default page size
        currentPage: 1                 // initial page
    };

    $scope.gridOptions = {
        data: 'myData',
        enablePaging: true,
        showFooter: true,
        rowHeight: 36,
        headerRowHeight: 38,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        filterOptions: $scope.filterOptions
    };

    $scope.setPagingData = function (data, page, pageSize) {
        // calc for pager
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        // Store data from server
        $scope.myData = pagedData;
        // Update server side data length
        $scope.totalServerItems = data.length;

        if (!$scope.$$phase) {
            $scope.$apply();
        }

    };

    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
        var ngGridResourcePath = 'server/ng-grid-data.json';

        $timeout(function () {

            if (searchText) {
                var ft = searchText.toLowerCase();
                $http.get(ngGridResourcePath).success(function (largeLoad) {
                    var data = largeLoad.filter(function (item) {
                        return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                    });
                    $scope.setPagingData(data, page, pageSize);
                });
            } else {
                $http.get(ngGridResourcePath).success(function (largeLoad) {
                    $scope.setPagingData(largeLoad, page, pageSize);
                });
            }
        }, 100);
    };


    $scope.$watch('pagingOptions', function (newVal, oldVal) {
        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }
    }, true);
    $scope.$watch('filterOptions', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }
    }, true);

    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

}]);

/**=========================================================
 * Module: NGTableCtrl.js
 * Controller for ngTables
 =========================================================*/

App.controller('NGTableCtrl', NGTableCtrl);

function NGTableCtrl($scope, $filter, ngTableParams, $resource, $timeout, ngTableDataService) {
    'use strict';
    // required for inner references
    var vm = this;


    var data = [
        {name: "Moroni", age: 50, money: -10},
        {name: "Tiancum", age: 43, money: 120},
        {name: "Jacob", age: 27, money: 5.5},
        {name: "Nephi", age: 29, money: -54},
        {name: "Enos", age: 34, money: 110},
        {name: "Tiancum", age: 43, money: 1000},
        {name: "Jacob", age: 27, money: -201},
        {name: "Nephi", age: 29, money: 100},
        {name: "Enos", age: 34, money: -52.5},
        {name: "Tiancum", age: 43, money: 52.1},
        {name: "Jacob", age: 27, money: 110},
        {name: "Nephi", age: 29, money: -55},
        {name: "Enos", age: 34, money: 551},
        {name: "Tiancum", age: 43, money: -1410},
        {name: "Jacob", age: 27, money: 410},
        {name: "Nephi", age: 29, money: 100},
        {name: "Enos", age: 34, money: -100}
    ];

    // SELECT ROWS
    // -----------------------------------

    vm.data = data;

    vm.tableParams3 = new ngTableParams({
        page: 1,            // show first page
        count: 10          // count per page
    }, {
        total: data.length, // length of data
        getData: function ($defer, params) {
            // use build-in angular filter
            var filteredData = params.filter() ?
                $filter('filter')(data, params.filter()) :
                data;
            var orderedData = params.sorting() ?
                $filter('orderBy')(filteredData, params.orderBy()) :
                data;

            params.total(orderedData.length); // set total for recalc pagination
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });

    vm.changeSelection = function (user) {
        // console.info(user);
    };

    // EXPORT CSV
    // -----------------------------------

    var data4 = [{name: "Moroni", age: 50},
        {name: "Tiancum", age: 43},
        {name: "Jacob", age: 27},
        {name: "Nephi", age: 29},
        {name: "Enos", age: 34},
        {name: "Tiancum", age: 43},
        {name: "Jacob", age: 27},
        {name: "Nephi", age: 29},
        {name: "Enos", age: 34},
        {name: "Tiancum", age: 43},
        {name: "Jacob", age: 27},
        {name: "Nephi", age: 29},
        {name: "Enos", age: 34},
        {name: "Tiancum", age: 43},
        {name: "Jacob", age: 27},
        {name: "Nephi", age: 29},
        {name: "Enos", age: 34}];

    vm.tableParams4 = new ngTableParams({
        page: 1,            // show first page
        count: 10           // count per page
    }, {
        total: data4.length, // length of data4
        getData: function ($defer, params) {
            $defer.resolve(data4.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });


    // SORTING
    // -----------------------------------


    vm.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
        sorting: {
            name: 'asc'     // initial sorting
        }
    }, {
        total: data.length, // length of data
        getData: function ($defer, params) {
            // use build-in angular filter
            var orderedData = params.sorting() ?
                $filter('orderBy')(data, params.orderBy()) :
                data;

            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });

    // FILTERS
    // -----------------------------------

    vm.tableParams2 = new ngTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {
            name: '',
            age: ''
            // name: 'M'       // initial filter
        }
    }, {
        total: data.length, // length of data
        getData: function ($defer, params) {
            // use build-in angular filter
            var orderedData = params.filter() ?
                $filter('filter')(data, params.filter()) :
                data;

            vm.users = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

            params.total(orderedData.length); // set total for recalc pagination
            $defer.resolve(vm.users);
        }
    });

    // AJAX

    var Api = $resource('server/table-data.json');

    vm.tableParams5 = new ngTableParams({
        page: 1,            // show first page
        count: 10           // count per page
    }, {
        total: 0,           // length of data
        counts: [],         // hide page counts control
        getData: function ($defer, params) {

            // Service using cache to avoid mutiple requests
            ngTableDataService.getData($defer, params, Api);

            /* direct ajax request to api (perform result pagination on the server)
          Api.get(params.url(), function(data) {
              $timeout(function() {
                  // update table params
                  params.total(data.total);
                  // set new data
                  $defer.resolve(data.result);
              }, 500);
          });
          */
        }
    });

}

NGTableCtrl.$inject = ["$scope", "$filter", "ngTableParams", "$resource", "$timeout", "ngTableDataService"];

// NOTE: We add the service definition here for quick reference
App.service('ngTableDataService', function () {

    var TableData = {
        cache: null,
        getData: function ($defer, params, api) {
            // if no cache, request data and filter
            if (!TableData.cache) {
                if (api) {
                    api.get(function (data) {
                        TableData.cache = data;
                        filterdata($defer, params);
                    });
                }
            } else {
                filterdata($defer, params);
            }

            function filterdata($defer, params) {
                var from = (params.page() - 1) * params.count();
                var to = params.page() * params.count();
                var filteredData = TableData.cache.result.slice(from, to);

                params.total(TableData.cache.total);
                $defer.resolve(filteredData);
            }

        }
    };

    return TableData;

});

/**=========================================================
 * Module: notifications.js
 * Initializes the notifications system
 =========================================================*/
App.controller('NotificationController', ['$scope', function ($scope) {

    $scope.autoplace = function (context, source) {
        //return (predictTooltipTop(source) < 0) ?  "bottom": "top";
        var pos = 'top';
        if (predictTooltipTop(source) < 0)
            pos = 'bottom';
        if (predictTooltipLeft(source) < 0)
            pos = 'right';
        return pos;
    };

    // Predicts tooltip top position
    // based on the trigger element
    function predictTooltipTop(el) {
        var top = el.offsetTop;
        var height = 40; // asumes ~40px tooltip height

        while (el.offsetParent) {
            el = el.offsetParent;
            top += el.offsetTop;
        }
        return (top - height) - (window.pageYOffset);
    }

    // Predicts tooltip top position
    // based on the trigger element
    function predictTooltipLeft(el) {
        var left = el.offsetLeft;
        var width = el.offsetWidth;

        while (el.offsetParent) {
            el = el.offsetParent;
            left += el.offsetLeft;
        }
        return (left - width) - (window.pageXOffset);
    }

}]);
/**=========================================================
 * Module: portlet.js
 * Drag and drop any panel to change its position
 * The Selector should could be applied to any object that contains
 * panel, so .col-* element are ideal.
 =========================================================*/
App.controller('portletsController', ['$scope', '$timeout', '$window', function ($scope, $timeout, $window) {
    'use strict';

    // Component is optional
    if (!$.fn.sortable) return;

    var Selector = '[portlet]',
        storageKeyName = 'portletState';

    angular.element(document).ready(function () {

        $timeout(function () {

            $(Selector).sortable({
                connectWith: Selector,
                items: 'div.panel',
                handle: '.portlet-handler',
                opacity: 0.7,
                placeholder: 'portlet box-placeholder',
                cancel: '.portlet-cancel',
                forcePlaceholderSize: true,
                iframeFix: false,
                tolerance: 'pointer',
                helper: 'original',
                revert: 200,
                forceHelperSize: true,
                start: saveListSize,
                update: savePortletOrder,
                create: loadPortletOrder
            })
            // optionally disables mouse selection
            //.disableSelection()
            ;
        }, 0);

    });

    function savePortletOrder(event, ui) {
        var self = event.target;
        var data = angular.fromJson($scope.$storage[storageKeyName]);

        if (!data) {
            data = {};
        }

        data[self.id] = $(self).sortable('toArray');

        $scope.$storage[storageKeyName] = angular.toJson(data);

        // save portlet size to avoid jumps
        saveListSize.apply(self);
    }

    function loadPortletOrder(event) {
        var self = event.target;
        var data = angular.fromJson($scope.$storage[storageKeyName]);

        if (data) {

            var porletId = self.id,
                panels = data[porletId];

            if (panels) {
                var portlet = $('#' + porletId);

                $.each(panels, function (index, value) {
                    $('#' + value).appendTo(portlet);
                });
            }

        }

        // save portlet size to avoid jumps
        saveListSize.apply(self);
    }

    // Keeps a consistent size in all portlet lists
    function saveListSize() {
        var $this = $(this);
        $this.css('min-height', $this.height());
    }

    /*function resetListSize() {
    $(this).css('min-height', "");
  }*/

}]);
/**=========================================================
 * Module: rickshaw.js
 =========================================================*/

App.controller('ChartRickshawController', ['$scope', function ($scope) {
    'use strict';

    $scope.renderers = [{
        id: 'area',
        name: 'Area'
    }, {
        id: 'line',
        name: 'Line'
    }, {
        id: 'bar',
        name: 'Bar'
    }, {
        id: 'scatterplot',
        name: 'Scatterplot'
    }];

    $scope.palettes = [
        'spectrum14',
        'spectrum2000',
        'spectrum2001',
        'colorwheel',
        'cool',
        'classic9',
        'munin'
    ];

    $scope.rendererChanged = function (id) {
        $scope['options' + id] = {
            renderer: $scope['renderer' + id].id
        };
    };

    $scope.paletteChanged = function (id) {
        $scope['features' + id] = {
            palette: $scope['palette' + id]
        };
    };

    $scope.changeSeriesData = function (id) {
        var seriesList = [];
        for (var i = 0; i < 3; i++) {
            var series = {
                name: 'Series ' + (i + 1),
                data: []
            };
            for (var j = 0; j < 10; j++) {
                series.data.push({x: j, y: Math.random() * 20});
            }
            seriesList.push(series);
            $scope['series' + id][i] = series;
        }
        //$scope['series' + id] = seriesList;
    };

    $scope.series0 = [];

    $scope.options0 = {
        renderer: 'area'
    };

    $scope.renderer0 = $scope.renderers[0];
    $scope.palette0 = $scope.palettes[0];

    $scope.rendererChanged(0);
    $scope.paletteChanged(0);
    $scope.changeSeriesData(0);

    // Graph 2

    var seriesData = [[], [], []];
    var random = new Rickshaw.Fixtures.RandomData(150);

    for (var i = 0; i < 150; i++) {
        random.addData(seriesData);
    }

    $scope.series2 = [
        {
            color: "#c05020",
            data: seriesData[0],
            name: 'New York'
        }, {
            color: "#30c020",
            data: seriesData[1],
            name: 'London'
        }, {
            color: "#6060c0",
            data: seriesData[2],
            name: 'Tokyo'
        }
    ];

    $scope.options2 = {
        renderer: 'area'
    };


}]);

/**=========================================================
 * Module: sidebar-menu.js
 * Handle sidebar collapsible elements
 =========================================================*/

App.controller('SidebarController', ['$rootScope', '$scope', '$state', '$http', '$timeout', 'Utils',
    function ($rootScope, $scope, $state, $http, $timeout, Utils) {

        var collapseList = [];

        // demo: when switch from collapse to hover, close all items
        $rootScope.$watch('app.layout.asideHover', function (oldVal, newVal) {
            if (newVal === false && oldVal === true) {
                closeAllBut(-1);
            }
        });

        // Check item and children active state
        var isActive = function (item) {

            if (!item) return;

            if (!item.sref || item.sref == '#') {
                var foundActive = false;
                angular.forEach(item.submenu, function (value, key) {
                    if (isActive(value)) foundActive = true;
                });
                return foundActive;
            } else
                return $state.is(item.sref) || $state.includes(item.sref);
        };

        // Load menu from json file
        // -----------------------------------

        $scope.getMenuItemPropClasses = function (item) {
            return (item.heading ? 'nav-heading' : '') +
                (isActive(item) ? ' active' : '');
        };

        $scope.loadSidebarMenu = function () {

            var menuURL = "/menu/getSiderBarMenu";
            $http.post(menuURL)
                .success(function (data) {
                    var jsonString = angular.toJson(data);
                    var temp = angular.fromJson(jsonString);
                    $scope.menuItems = temp.obj;
                })

                /*
              var menuJson = 'server/sidebar-menu.json',
                  menuURL  = menuJson + '?v=' + (new Date().getTime()); // jumps cache
              $http.get(menuURL)
                  .success(function(items) {
                      $scope.menuItems = items;
                  })
*/

                .error(function (data, status, headers, config) {
                    alert('Failure loading menu');
                });
        };

        $scope.loadSidebarMenu();

        // Handle sidebar collapse items
        // -----------------------------------

        $scope.addCollapse = function ($index, item) {
            collapseList[$index] = $rootScope.app.layout.asideHover ? true : !isActive(item);
        };

        $scope.isCollapse = function ($index) {
            return (collapseList[$index]);
        };

        $scope.toggleCollapse = function ($index, isParentItem) {


            // collapsed sidebar doesn't toggle drodopwn
            if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) return true;

            // make sure the item index exists
            if (angular.isDefined(collapseList[$index])) {
                if (!$scope.lastEventFromChild) {
                    collapseList[$index] = !collapseList[$index];
                    closeAllBut($index);
                }
            } else if (isParentItem) {
                closeAllBut(-1);
            }

            $scope.lastEventFromChild = isChild($index);

            return true;

        };

        function closeAllBut(index) {
            index += '';
            for (var i in collapseList) {
                if (index < 0 || index.indexOf(i) < 0)
                    collapseList[i] = true;
            }
        }

        function isChild($index) {
            return (typeof $index === 'string') && !($index.indexOf('-') < 0);
        }

    }]);

/**=========================================================
 * Module: sortable.js
 * Sortable controller
 =========================================================*/

App.controller('SortableController', ['$scope', function ($scope) {
    'use strict';

    // Single List
    $scope.data1 = [
        {id: 1, name: 'Donald Hoffman'},
        {id: 2, name: 'Wallace Barrett'},
        {id: 3, name: 'Marsha Hicks'},
        {id: 4, name: 'Roland Brown'}
    ];

    $scope.add = function () {
        $scope.data1.push({id: $scope.data1.length + 1, name: 'Earl Knight'});
    };

    $scope.sortableCallback = function (sourceModel, destModel, start, end) {
        console.log(start + ' -> ' + end);
    };

    $scope.sortableOptions = {
        placeholder: '<div class="box-placeholder p0 m0"><div></div></div>',
        forcePlaceholderSize: true
    };

}]);

/**=========================================================
 * Module: demo-buttons.js
 * Provides a simple demo for buttons actions
 =========================================================*/

App.controller('TablexEditableController', ['$scope', '$filter', '$http', 'editableOptions', 'editableThemes', '$q',
    function ($scope, $filter, $http, editableOptions, editableThemes, $q) {

        // editable row
        // -----------------------------------
        $scope.users = [
            {id: 1, name: 'awesome user1', status: 2, group: 4, groupName: 'admin'},
            {id: 2, name: 'awesome user2', status: undefined, group: 3, groupName: 'vip'},
            {id: 3, name: 'awesome user3', status: 2, group: null}
        ];

        $scope.statuses = [
            {value: 1, text: 'status1'},
            {value: 2, text: 'status2'},
            {value: 3, text: 'status3'},
            {value: 4, text: 'status4'}
        ];

        $scope.groups = [];
        $scope.loadGroups = function () {
            return $scope.groups.length ? null : $http.get('server/xeditable-groups.json').success(function (data) {
                $scope.groups = data;
            });
        };

        $scope.showGroup = function (user) {
            if (user.group && $scope.groups.length) {
                var selected = $filter('filter')($scope.groups, {id: user.group});
                return selected.length ? selected[0].text : 'Not set';
            } else {
                return user.groupName || 'Not set';
            }
        };

        $scope.showStatus = function (user) {
            var selected = [];
            if (user.status) {
                selected = $filter('filter')($scope.statuses, {value: user.status});
            }
            return selected.length ? selected[0].text : 'Not set';
        };

        $scope.checkName = function (data, id) {
            if (id === 2 && data !== 'awesome') {
                return "Username 2 should be `awesome`";
            }
        };

        $scope.saveUser = function (data, id) {
            //$scope.user not updated yet
            angular.extend(data, {id: id});
            console.log('Saving user: ' + id);
            // return $http.post('/saveUser', data);
        };

        // remove user
        $scope.removeUser = function (index) {
            $scope.users.splice(index, 1);
        };

        // add user
        $scope.addUser = function () {
            $scope.inserted = {
                id: $scope.users.length + 1,
                name: '',
                status: null,
                group: null,
                isNew: true
            };
            $scope.users.push($scope.inserted);
        };

        // editable column
        // -----------------------------------


        $scope.saveColumn = function (column) {
            var results = [];
            angular.forEach($scope.users, function (user) {
                // results.push($http.post('/saveColumn', {column: column, value: user[column], id: user.id}));
                console.log('Saving column: ' + column);
            });
            return $q.all(results);
        };

        // editable table
        // -----------------------------------

        // filter users to show
        $scope.filterUser = function (user) {
            return user.isDeleted !== true;
        };

        // mark user as deleted
        $scope.deleteUser = function (id) {
            var filtered = $filter('filter')($scope.users, {id: id});
            if (filtered.length) {
                filtered[0].isDeleted = true;
            }
        };

        // cancel all changes
        $scope.cancel = function () {
            for (var i = $scope.users.length; i--;) {
                var user = $scope.users[i];
                // undelete
                if (user.isDeleted) {
                    delete user.isDeleted;
                }
                // remove new
                if (user.isNew) {
                    $scope.users.splice(i, 1);
                }
            }
        };

        // save edits
        $scope.saveTable = function () {
            var results = [];
            for (var i = $scope.users.length; i--;) {
                var user = $scope.users[i];
                // actually delete user
                if (user.isDeleted) {
                    $scope.users.splice(i, 1);
                }
                // mark as not new
                if (user.isNew) {
                    user.isNew = false;
                }

                // send on server
                // results.push($http.post('/saveUser', user));
                console.log('Saving Table...');
            }

            return $q.all(results);
        };

    }]);

App.controller("TodoController", ['$scope', '$filter', function ($scope, $filter) {

    $scope.items = [
        {
            todo: {
                title: "Meeting with Mark at 7am.",
                description: "Pellentesque convallis mauris eu elit imperdiet quis eleifend quam aliquet. "
            },
            complete: true
        },
        {
            todo: {title: "Call Sonya. Talk about the new project.", description: ""},
            complete: false
        },
        {
            todo: {title: "Find a new place for vacations", description: ""},
            complete: false
        }
    ];

    $scope.editingTodo = false;
    $scope.todo = {};

    $scope.addTodo = function () {

        if ($scope.todo.title === "") return;
        if (!$scope.todo.description) $scope.todo.description = "";

        if ($scope.editingTodo) {
            $scope.todo = {};
            $scope.editingTodo = false;
        } else {
            $scope.items.push({todo: angular.copy($scope.todo), complete: false});
            $scope.todo.title = "";
            $scope.todo.description = "";
        }
    };

    $scope.editTodo = function (index, $event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.todo = $scope.items[index].todo;
        $scope.editingTodo = true;
    };

    $scope.removeTodo = function (index, $event) {
        $scope.items.splice(index, 1);
    };

    $scope.clearAll = function () {
        $scope.items = [];
    };

    $scope.totalCompleted = function () {
        return $filter("filter")($scope.items, function (item) {
            return item.complete;
        }).length;
    };

    $scope.totalPending = function () {
        return $filter("filter")($scope.items, function (item) {
            return !item.complete;
        }).length;
    };
}]);
/**=========================================================
 * Module: UIGridController
 =========================================================*/
App.controller('UIGridController', ['$scope', 'uiGridConstants', '$http', function ($scope, uiGridConstants, $http) {

    // Basic example
    // -----------------------------------

    $scope.gridOptions = {
        rowHeight: 34,
        data: [
            {
                "name": "Wilder Gonzales",
                "gender": "male",
                "company": "Geekko"
            },
            {
                "name": "Georgina Schultz",
                "gender": "female",
                "company": "Suretech"
            },
            {
                "name": "Carroll Buchanan",
                "gender": "male",
                "company": "Ecosys"
            },
            {
                "name": "Valarie Atkinson",
                "gender": "female",
                "company": "Hopeli"
            },
            {
                "name": "Schroeder Mathews",
                "gender": "male",
                "company": "Polarium"
            },
            {
                "name": "Ethel Price",
                "gender": "female",
                "company": "Enersol"
            },
            {
                "name": "Claudine Neal",
                "gender": "female",
                "company": "Sealoud"
            },
            {
                "name": "Beryl Rice",
                "gender": "female",
                "company": "Velity"
            },
            {
                "name": "Lynda Mendoza",
                "gender": "female",
                "company": "Dogspa"
            },
            {
                "name": "Sarah Massey",
                "gender": "female",
                "company": "Bisba"
            },
            {
                "name": "Robles Boyle",
                "gender": "male",
                "company": "Comtract"
            },
            {
                "name": "Evans Hickman",
                "gender": "male",
                "company": "Parleynet"
            },
            {
                "name": "Dawson Barber",
                "gender": "male",
                "company": "Dymi"
            },
            {
                "name": "Bruce Strong",
                "gender": "male",
                "company": "Xyqag"
            },
            {
                "name": "Nellie Whitfield",
                "gender": "female",
                "company": "Exospace"
            },
            {
                "name": "Jackson Macias",
                "gender": "male",
                "company": "Aquamate"
            },
            {
                "name": "Pena Pena",
                "gender": "male",
                "company": "Quarx"
            },
            {
                "name": "Lelia Gates",
                "gender": "female",
                "company": "Proxsoft"
            },
            {
                "name": "Letitia Vasquez",
                "gender": "female",
                "company": "Slumberia"
            },
            {
                "name": "Trevino Moreno",
                "gender": "male",
                "company": "Conjurica"
            }
        ]
    };

    // Complex example
    // -----------------------------------

    var data = [];

    $scope.gridOptionsComplex = {
        showGridFooter: true,
        showColumnFooter: true,
        enableFiltering: true,
        columnDefs: [
            {field: 'name', width: '13%'},
            {field: 'address.street', aggregationType: uiGridConstants.aggregationTypes.sum, width: '13%'},
            {
                field: 'age',
                aggregationType: uiGridConstants.aggregationTypes.avg,
                aggregationHideLabel: true,
                width: '13%'
            },
            {
                name: 'ageMin',
                field: 'age',
                aggregationType: uiGridConstants.aggregationTypes.min,
                width: '13%',
                displayName: 'Age for min'
            },
            {
                name: 'ageMax',
                field: 'age',
                aggregationType: uiGridConstants.aggregationTypes.max,
                width: '13%',
                displayName: 'Age for max'
            },
            {
                name: 'customCellTemplate',
                field: 'age',
                width: '14%',
                footerCellTemplate: '<div class="ui-grid-cell-contents bg-info text-center">Custom HTML</div>'
            },
            {
                name: 'registered',
                field: 'registered',
                width: '20%',
                cellFilter: 'date',
                footerCellFilter: 'date',
                aggregationType: uiGridConstants.aggregationTypes.max
            }
        ],
        data: data,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
        }
    }

    $http.get('server/uigrid-complex.json')
        .success(function (data) {
            data.forEach(function (row) {
                row.registered = Date.parse(row.registered);
            });
            $scope.gridOptionsComplex.data = data;
        });


    $scope.gridOptions1 = {
        paginationPageSizes: [25, 50, 75],
        paginationPageSize: 25,
        columnDefs: [
            {name: 'name'},
            {name: 'gender'},
            {name: 'company'}
        ]
    };

    $http.get('server/uigrid-100.json')
        .success(function (data) {
            $scope.gridOptions1.data = data;
        });

}]);

/**=========================================================
 * Module: uiselect.js
 * uiSelect controller
 =========================================================*/

App.controller('uiSelectController', ["$scope", "$http", function ($scope, $http) {
    $scope.disabled = undefined;

    $scope.enable = function () {
        $scope.disabled = false;
    };

    $scope.disable = function () {
        $scope.disabled = true;
    };

    $scope.clear = function () {
        $scope.person.selected = undefined;
        $scope.address.selected = undefined;
        $scope.country.selected = undefined;
    };

    $scope.person = {};
    $scope.people = [
        {name: 'Adam', email: 'adam@email.com', age: 10},
        {name: 'Amalie', email: 'amalie@email.com', age: 12},
        {name: 'Wladimir', email: 'wladimir@email.com', age: 30},
        {name: 'Samantha', email: 'samantha@email.com', age: 31},
        {name: 'Estefanía', email: 'estefanía@email.com', age: 16},
        {name: 'Natasha', email: 'natasha@email.com', age: 54},
        {name: 'Nicole', email: 'nicole@email.com', age: 43},
        {name: 'Adrian', email: 'adrian@email.com', age: 21}
    ];

    $scope.address = {};
    $scope.refreshAddresses = function (address) {
        var params = {address: address, sensor: false};
        return $http.get(
            'http://maps.googleapis.com/maps/api/geocode/json',
            {params: params}
        ).then(function (response) {
            $scope.addresses = response.data.results;
        });
    };

    $scope.country = {};
    $scope.countries = [ // Taken from https://gist.github.com/unceus/6501985
        {name: 'Afghanistan', code: 'AF'},
        {name: 'Åland Islands', code: 'AX'},
        {name: 'Albania', code: 'AL'},
        {name: 'Algeria', code: 'DZ'},
        {name: 'American Samoa', code: 'AS'},
        {name: 'Andorra', code: 'AD'},
        {name: 'Angola', code: 'AO'},
        {name: 'Anguilla', code: 'AI'},
        {name: 'Antarctica', code: 'AQ'},
        {name: 'Antigua and Barbuda', code: 'AG'},
        {name: 'Argentina', code: 'AR'},
        {name: 'Armenia', code: 'AM'},
        {name: 'Aruba', code: 'AW'},
        {name: 'Australia', code: 'AU'},
        {name: 'Austria', code: 'AT'},
        {name: 'Azerbaijan', code: 'AZ'},
        {name: 'Bahamas', code: 'BS'},
        {name: 'Bahrain', code: 'BH'},
        {name: 'Bangladesh', code: 'BD'},
        {name: 'Barbados', code: 'BB'},
        {name: 'Belarus', code: 'BY'},
        {name: 'Belgium', code: 'BE'},
        {name: 'Belize', code: 'BZ'},
        {name: 'Benin', code: 'BJ'},
        {name: 'Bermuda', code: 'BM'},
        {name: 'Bhutan', code: 'BT'},
        {name: 'Bolivia', code: 'BO'},
        {name: 'Bosnia and Herzegovina', code: 'BA'},
        {name: 'Botswana', code: 'BW'},
        {name: 'Bouvet Island', code: 'BV'},
        {name: 'Brazil', code: 'BR'},
        {name: 'British Indian Ocean Territory', code: 'IO'},
        {name: 'Brunei Darussalam', code: 'BN'},
        {name: 'Bulgaria', code: 'BG'},
        {name: 'Burkina Faso', code: 'BF'},
        {name: 'Burundi', code: 'BI'},
        {name: 'Cambodia', code: 'KH'},
        {name: 'Cameroon', code: 'CM'},
        {name: 'Canada', code: 'CA'},
        {name: 'Cape Verde', code: 'CV'},
        {name: 'Cayman Islands', code: 'KY'},
        {name: 'Central African Republic', code: 'CF'},
        {name: 'Chad', code: 'TD'},
        {name: 'Chile', code: 'CL'},
        {name: 'China', code: 'CN'},
        {name: 'Christmas Island', code: 'CX'},
        {name: 'Cocos (Keeling) Islands', code: 'CC'},
        {name: 'Colombia', code: 'CO'},
        {name: 'Comoros', code: 'KM'},
        {name: 'Congo', code: 'CG'},
        {name: 'Congo, The Democratic Republic of the', code: 'CD'},
        {name: 'Cook Islands', code: 'CK'},
        {name: 'Costa Rica', code: 'CR'},
        {name: 'Cote D\'Ivoire', code: 'CI'},
        {name: 'Croatia', code: 'HR'},
        {name: 'Cuba', code: 'CU'},
        {name: 'Cyprus', code: 'CY'},
        {name: 'Czech Republic', code: 'CZ'},
        {name: 'Denmark', code: 'DK'},
        {name: 'Djibouti', code: 'DJ'},
        {name: 'Dominica', code: 'DM'},
        {name: 'Dominican Republic', code: 'DO'},
        {name: 'Ecuador', code: 'EC'},
        {name: 'Egypt', code: 'EG'},
        {name: 'El Salvador', code: 'SV'},
        {name: 'Equatorial Guinea', code: 'GQ'},
        {name: 'Eritrea', code: 'ER'},
        {name: 'Estonia', code: 'EE'},
        {name: 'Ethiopia', code: 'ET'},
        {name: 'Falkland Islands (Malvinas)', code: 'FK'},
        {name: 'Faroe Islands', code: 'FO'},
        {name: 'Fiji', code: 'FJ'},
        {name: 'Finland', code: 'FI'},
        {name: 'France', code: 'FR'},
        {name: 'French Guiana', code: 'GF'},
        {name: 'French Polynesia', code: 'PF'},
        {name: 'French Southern Territories', code: 'TF'},
        {name: 'Gabon', code: 'GA'},
        {name: 'Gambia', code: 'GM'},
        {name: 'Georgia', code: 'GE'},
        {name: 'Germany', code: 'DE'},
        {name: 'Ghana', code: 'GH'},
        {name: 'Gibraltar', code: 'GI'},
        {name: 'Greece', code: 'GR'},
        {name: 'Greenland', code: 'GL'},
        {name: 'Grenada', code: 'GD'},
        {name: 'Guadeloupe', code: 'GP'},
        {name: 'Guam', code: 'GU'},
        {name: 'Guatemala', code: 'GT'},
        {name: 'Guernsey', code: 'GG'},
        {name: 'Guinea', code: 'GN'},
        {name: 'Guinea-Bissau', code: 'GW'},
        {name: 'Guyana', code: 'GY'},
        {name: 'Haiti', code: 'HT'},
        {name: 'Heard Island and Mcdonald Islands', code: 'HM'},
        {name: 'Holy See (Vatican City State)', code: 'VA'},
        {name: 'Honduras', code: 'HN'},
        {name: 'Hong Kong', code: 'HK'},
        {name: 'Hungary', code: 'HU'},
        {name: 'Iceland', code: 'IS'},
        {name: 'India', code: 'IN'},
        {name: 'Indonesia', code: 'ID'},
        {name: 'Iran, Islamic Republic Of', code: 'IR'},
        {name: 'Iraq', code: 'IQ'},
        {name: 'Ireland', code: 'IE'},
        {name: 'Isle of Man', code: 'IM'},
        {name: 'Israel', code: 'IL'},
        {name: 'Italy', code: 'IT'},
        {name: 'Jamaica', code: 'JM'},
        {name: 'Japan', code: 'JP'},
        {name: 'Jersey', code: 'JE'},
        {name: 'Jordan', code: 'JO'},
        {name: 'Kazakhstan', code: 'KZ'},
        {name: 'Kenya', code: 'KE'},
        {name: 'Kiribati', code: 'KI'},
        {name: 'Korea, Democratic People\'s Republic of', code: 'KP'},
        {name: 'Korea, Republic of', code: 'KR'},
        {name: 'Kuwait', code: 'KW'},
        {name: 'Kyrgyzstan', code: 'KG'},
        {name: 'Lao People\'s Democratic Republic', code: 'LA'},
        {name: 'Latvia', code: 'LV'},
        {name: 'Lebanon', code: 'LB'},
        {name: 'Lesotho', code: 'LS'},
        {name: 'Liberia', code: 'LR'},
        {name: 'Libyan Arab Jamahiriya', code: 'LY'},
        {name: 'Liechtenstein', code: 'LI'},
        {name: 'Lithuania', code: 'LT'},
        {name: 'Luxembourg', code: 'LU'},
        {name: 'Macao', code: 'MO'},
        {name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK'},
        {name: 'Madagascar', code: 'MG'},
        {name: 'Malawi', code: 'MW'},
        {name: 'Malaysia', code: 'MY'},
        {name: 'Maldives', code: 'MV'},
        {name: 'Mali', code: 'ML'},
        {name: 'Malta', code: 'MT'},
        {name: 'Marshall Islands', code: 'MH'},
        {name: 'Martinique', code: 'MQ'},
        {name: 'Mauritania', code: 'MR'},
        {name: 'Mauritius', code: 'MU'},
        {name: 'Mayotte', code: 'YT'},
        {name: 'Mexico', code: 'MX'},
        {name: 'Micronesia, Federated States of', code: 'FM'},
        {name: 'Moldova, Republic of', code: 'MD'},
        {name: 'Monaco', code: 'MC'},
        {name: 'Mongolia', code: 'MN'},
        {name: 'Montserrat', code: 'MS'},
        {name: 'Morocco', code: 'MA'},
        {name: 'Mozambique', code: 'MZ'},
        {name: 'Myanmar', code: 'MM'},
        {name: 'Namibia', code: 'NA'},
        {name: 'Nauru', code: 'NR'},
        {name: 'Nepal', code: 'NP'},
        {name: 'Netherlands', code: 'NL'},
        {name: 'Netherlands Antilles', code: 'AN'},
        {name: 'New Caledonia', code: 'NC'},
        {name: 'New Zealand', code: 'NZ'},
        {name: 'Nicaragua', code: 'NI'},
        {name: 'Niger', code: 'NE'},
        {name: 'Nigeria', code: 'NG'},
        {name: 'Niue', code: 'NU'},
        {name: 'Norfolk Island', code: 'NF'},
        {name: 'Northern Mariana Islands', code: 'MP'},
        {name: 'Norway', code: 'NO'},
        {name: 'Oman', code: 'OM'},
        {name: 'Pakistan', code: 'PK'},
        {name: 'Palau', code: 'PW'},
        {name: 'Palestinian Territory, Occupied', code: 'PS'},
        {name: 'Panama', code: 'PA'},
        {name: 'Papua New Guinea', code: 'PG'},
        {name: 'Paraguay', code: 'PY'},
        {name: 'Peru', code: 'PE'},
        {name: 'Philippines', code: 'PH'},
        {name: 'Pitcairn', code: 'PN'},
        {name: 'Poland', code: 'PL'},
        {name: 'Portugal', code: 'PT'},
        {name: 'Puerto Rico', code: 'PR'},
        {name: 'Qatar', code: 'QA'},
        {name: 'Reunion', code: 'RE'},
        {name: 'Romania', code: 'RO'},
        {name: 'Russian Federation', code: 'RU'},
        {name: 'Rwanda', code: 'RW'},
        {name: 'Saint Helena', code: 'SH'},
        {name: 'Saint Kitts and Nevis', code: 'KN'},
        {name: 'Saint Lucia', code: 'LC'},
        {name: 'Saint Pierre and Miquelon', code: 'PM'},
        {name: 'Saint Vincent and the Grenadines', code: 'VC'},
        {name: 'Samoa', code: 'WS'},
        {name: 'San Marino', code: 'SM'},
        {name: 'Sao Tome and Principe', code: 'ST'},
        {name: 'Saudi Arabia', code: 'SA'},
        {name: 'Senegal', code: 'SN'},
        {name: 'Serbia and Montenegro', code: 'CS'},
        {name: 'Seychelles', code: 'SC'},
        {name: 'Sierra Leone', code: 'SL'},
        {name: 'Singapore', code: 'SG'},
        {name: 'Slovakia', code: 'SK'},
        {name: 'Slovenia', code: 'SI'},
        {name: 'Solomon Islands', code: 'SB'},
        {name: 'Somalia', code: 'SO'},
        {name: 'South Africa', code: 'ZA'},
        {name: 'South Georgia and the South Sandwich Islands', code: 'GS'},
        {name: 'Spain', code: 'ES'},
        {name: 'Sri Lanka', code: 'LK'},
        {name: 'Sudan', code: 'SD'},
        {name: 'Suriname', code: 'SR'},
        {name: 'Svalbard and Jan Mayen', code: 'SJ'},
        {name: 'Swaziland', code: 'SZ'},
        {name: 'Sweden', code: 'SE'},
        {name: 'Switzerland', code: 'CH'},
        {name: 'Syrian Arab Republic', code: 'SY'},
        {name: 'Taiwan, Province of China', code: 'TW'},
        {name: 'Tajikistan', code: 'TJ'},
        {name: 'Tanzania, United Republic of', code: 'TZ'},
        {name: 'Thailand', code: 'TH'},
        {name: 'Timor-Leste', code: 'TL'},
        {name: 'Togo', code: 'TG'},
        {name: 'Tokelau', code: 'TK'},
        {name: 'Tonga', code: 'TO'},
        {name: 'Trinidad and Tobago', code: 'TT'},
        {name: 'Tunisia', code: 'TN'},
        {name: 'Turkey', code: 'TR'},
        {name: 'Turkmenistan', code: 'TM'},
        {name: 'Turks and Caicos Islands', code: 'TC'},
        {name: 'Tuvalu', code: 'TV'},
        {name: 'Uganda', code: 'UG'},
        {name: 'Ukraine', code: 'UA'},
        {name: 'United Arab Emirates', code: 'AE'},
        {name: 'United Kingdom', code: 'GB'},
        {name: 'United States', code: 'US'},
        {name: 'United States Minor Outlying Islands', code: 'UM'},
        {name: 'Uruguay', code: 'UY'},
        {name: 'Uzbekistan', code: 'UZ'},
        {name: 'Vanuatu', code: 'VU'},
        {name: 'Venezuela', code: 'VE'},
        {name: 'Vietnam', code: 'VN'},
        {name: 'Virgin Islands, British', code: 'VG'},
        {name: 'Virgin Islands, U.S.', code: 'VI'},
        {name: 'Wallis and Futuna', code: 'WF'},
        {name: 'Western Sahara', code: 'EH'},
        {name: 'Yemen', code: 'YE'},
        {name: 'Zambia', code: 'ZM'},
        {name: 'Zimbabwe', code: 'ZW'}
    ];


    // Multiple
    $scope.someGroupFn = function (item) {

        if (item.name[0] >= 'A' && item.name[0] <= 'M')
            return 'From A - M';

        if (item.name[0] >= 'N' && item.name[0] <= 'Z')
            return 'From N - Z';

    };

    $scope.counter = 0;
    $scope.someFunction = function (item, model) {
        $scope.counter++;
        $scope.eventResult = {item: item, model: model};
    };

    $scope.availableColors = ['Red', 'Green', 'Blue', 'Yellow', 'Magenta', 'Maroon', 'Umbra', 'Turquoise'];

    $scope.multipleDemo = {};
    $scope.multipleDemo.colors = ['Blue', 'Red'];
    $scope.multipleDemo.selectedPeople = [$scope.people[5], $scope.people[4]];
    $scope.multipleDemo.selectedPeopleWithGroupBy = [$scope.people[8], $scope.people[6]];
    $scope.multipleDemo.selectedPeopleSimple = ['samantha@email.com', 'wladimir@email.com'];

}]);


/**
 * AngularJS default filter with the following expression:
 * "person in people | filter: {name: $select.search, age: $select.search}"
 * performs a AND between 'name: $select.search' and 'age: $select.search'.
 * We want to perform a OR.
 */
App.filter('propsFilter', function () {
    return function (items, props) {
        var out = [];

        if (angular.isArray(items)) {
            items.forEach(function (item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});
/**=========================================================
 * Module: upload.js
 =========================================================*/

App.controller('FileUploadController', ['$scope', 'FileUploader', function ($scope, FileUploader) {

    var uploader = $scope.uploader = new FileUploader({
        url: 'server/upload.php'
    });

    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function (item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function (fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function (addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function (item) {
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function (fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function (progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function (fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function (fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function (fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function () {
        console.info('onCompleteAll');
    };

    console.info('uploader', uploader);
}]);
App.controller('UserBlockController', ['$scope', function ($scope) {

    $scope.userBlockVisible = true;

    $scope.$on('toggleUserBlock', function (event, args) {

        $scope.userBlockVisible = !$scope.userBlockVisible;

    });

}]);
/**=========================================================
 * Module: vmaps,js
 * jVector Maps support
 =========================================================*/

App.controller('VectorMapController', ['$scope', function ($scope) {
    'use strict';

    $scope.seriesData = {
        'CA': 11100,   // Canada
        'DE': 2510,    // Germany
        'FR': 3710,    // France
        'AU': 5710,    // Australia
        'GB': 8310,    // Great Britain
        'RU': 9310,    // Russia
        'BR': 6610,    // Brazil
        'IN': 7810,    // India
        'CN': 4310,    // China
        'US': 839,     // USA
        'SA': 410      // Saudi Arabia
    };

    $scope.markersData = [
        {latLng: [41.90, 12.45], name: 'Vatican City'},
        {latLng: [43.73, 7.41], name: 'Monaco'},
        {latLng: [-0.52, 166.93], name: 'Nauru'},
        {latLng: [-8.51, 179.21], name: 'Tuvalu'},
        {latLng: [7.11, 171.06], name: 'Marshall Islands'},
        {latLng: [17.3, -62.73], name: 'Saint Kitts and Nevis'},
        {latLng: [3.2, 73.22], name: 'Maldives'},
        {latLng: [35.88, 14.5], name: 'Malta'},
        {latLng: [41.0, -71.06], name: 'New England'},
        {latLng: [12.05, -61.75], name: 'Grenada'},
        {latLng: [13.16, -59.55], name: 'Barbados'},
        {latLng: [17.11, -61.85], name: 'Antigua and Barbuda'},
        {latLng: [-4.61, 55.45], name: 'Seychelles'},
        {latLng: [7.35, 134.46], name: 'Palau'},
        {latLng: [42.5, 1.51], name: 'Andorra'}
    ];

}]);

/**=========================================================
 * Module: word-cloud.js
 * Controller for jqCloud
 =========================================================*/

App.controller('WordCloudController', ['$scope', function ($scope) {

    $scope.words = [
        {
            text: 'Lorem',
            weight: 13
            //link: 'http://themicon.co'
        }, {
            text: 'Ipsum',
            weight: 10.5
        }, {
            text: 'Dolor',
            weight: 9.4
        }, {
            text: 'Sit',
            weight: 8
        }, {
            text: 'Amet',
            weight: 6.2
        }, {
            text: 'Consectetur',
            weight: 5
        }, {
            text: 'Adipiscing',
            weight: 5
        }, {
            text: 'Sit',
            weight: 8
        }, {
            text: 'Amet',
            weight: 6.2
        }, {
            text: 'Consectetur',
            weight: 5
        }, {
            text: 'Adipiscing',
            weight: 5
        }
    ];

}]);


App.directive('ngConfirmClick', ["$parse", "$interpolate", function ($parse, $interpolate) {
    return {
        restrict: "A",
        priority: -1,
        compile: function (ele, attr) {
            var fn = $parse(attr.ngConfirmClick, null, true);
            return function ngEventHandler(scope, ele) {
                ele.on('click', function (event) {
                    var callback = function () {
                        fn(scope, {$event: "confirm"});
                    };
                    var message = $interpolate(attr.ngConfirmMessage)(scope) || 'Are you sure?';
                    if (confirm(message)) {
                        if (scope.$root.$$phase) {
                            scope.$evalAsync(callback);
                        } else {
                            scope.$apply(callback);
                        }
                    }
                });
            }
        }
    }
}]);

/**=========================================================
 * Module: anchor.js
 * Disables null anchor behavior
 =========================================================*/

App.directive('href', function () {

    return {
        restrict: 'A',
        compile: function (element, attr) {
            return function (scope, element) {
                if (attr.ngClick || attr.href === '' || attr.href === '#') {
                    if (!element.hasClass('dropdown-toggle'))
                        element.on('click', function (e) {
                            e.preventDefault();
                            e.stopPropagation();
                        });
                }
            };
        }
    };
});

App.directive('pageDirective', ['$rootScope',
    function ($rootScope, helper) {
        var link = function (scope, elem, attr) {
            scope.pageConfig.pageIndex;  //当前页码
            scope.pageConfig.totalPage;  //总页数
            scope.pageConfig.totalCount; //总记录数
            scope.pageConfig.pageSize = scope.pageConfig.pageSize || 5;  //每页条数
            var prev = scope.pageConfig.prevPage || '<';  //上一页文字
            var next = scope.pageConfig.nextPage || '>';  //下一页文字
            var first = scope.pageConfig.firstPage || '<<';  //首页文字
            var last = scope.pageConfig.lastPage || '>>';  //末页文字
            var degeCount = scope.pageConfig.degeCount || 3;  //当前页码两边的页码个数（默认：3）
            var isShowEllipsis = scope.pageConfig.isShowEllipsis;   //是否显示省略号不可点击按钮
            var ellipsisBtn = isShowEllipsis ? '<li><span class="ellipsis">...</span></li>' : '';


            //监听父作用域列表数据获取成功后
            scope.$on("initPage", function (e, m) {
                initPage(scope.pageConfig.totalPage, scope.pageConfig.pageIndex, degeCount)
            });

            function initPage(totalPage, pageIndex, degeCount) {
                var pageHtml = '';
                var tmpHtmlPrev = '';
                var tmpHtmlNext = '';
                if (pageIndex - degeCount >= degeCount - 1 && totalPage - pageIndex >= degeCount + 1) {   //前后都需要
                    var count = degeCount;  //前后各自需要显示的页码个数
                    for (var i = 0; i < count; i++) {
                        if (pageIndex != 1) {
                            tmpHtmlPrev += '<li><a href="javascript:;" class="page-number">' + (pageIndex - (count - i)) + '</a></li>';
                        }
                        tmpHtmlNext += '<li><a href="javascript:;" class="page-number">' + ((pageIndex - 0) + i + 1) + '</a></li>';
                    }
                    pageHtml = '<li><a id="first_page" href="javascript:;">' + first + '</a></li>' +
                        '<li><a id="prev_page" href="javascript:;">' + prev + '</a></li>' + ((pageIndex > degeCount + 1) ? ellipsisBtn : '') +
                        tmpHtmlPrev +
                        '<li><a href="javascript:;" class="active">' + pageIndex + '</a></li>' +
                        tmpHtmlNext +
                        ellipsisBtn +
                        '<li><a id="next_page" href="javascript:;">' + next + '</a></li>' +
                        '<li><a id="last_page" href="javascript:;">' + last + '</a></li>';
                } else if (pageIndex - degeCount >= degeCount - 1 && totalPage - pageIndex < degeCount + 1) { //前需要，后不需要
                    var count = degeCount;  //前需要显示的页码个数
                    var countNext = totalPage - pageIndex;  //后需要显示的页码个数
                    if (pageIndex != 1) {
                        for (var i = 0; i < count; i++) {
                            tmpHtmlPrev += '<li><a href="javascript:;" class="page-number">' + (pageIndex - (count - i)) + '</a></li>';
                        }
                    }
                    for (var i = 0; i < countNext; i++) {
                        tmpHtmlNext += '<li><a href="javascript:;" class="page-number">' + ((pageIndex - 0) + i + 1) + '</a></li>';
                    }
                    pageHtml = '<li><a id="first_page" href="javascript:;">' + first + '</a></li>' +
                        '<li><a id="prev_page" href="javascript:;">' + prev + '</a></li>' + ((pageIndex > degeCount + 1) ? ellipsisBtn : '') +
                        tmpHtmlPrev +
                        '<li><a href="javascript:;" class="active">' + pageIndex + '</a></li>' +
                        tmpHtmlNext +
                        '<li><a id="next_page" href="javascript:;">' + next + '</a></li>' +
                        '<li><a id="last_page" href="javascript:;">' + last + '</a></li>';
                } else if (pageIndex - degeCount < degeCount - 1 && totalPage - pageIndex >= degeCount + 1) { //前不需要，后需要
                    var countPrev = pageIndex - 1;  //前需要显示的页码个数
                    var count = degeCount;  //后需要显示的页码个数
                    if (pageIndex != 1) {
                        for (var i = 0; i < countPrev; i++) {
                            tmpHtmlPrev += '<li><a href="javascript:;" class="page-number">' + (pageIndex - (countPrev - i)) + '</a></li>';
                        }
                    }
                    for (var i = 0; i < count; i++) {
                        tmpHtmlNext += '<li><a href="javascript:;" class="page-number">' + ((pageIndex - 0) + i + 1) + '</a></li>';
                    }
                    pageHtml = '<li><a id="first_page" href="javascript:;">' + first + '</a></li>' +
                        '<li><a id="prev_page" href="javascript:;">' + prev + '</a></li>' +
                        tmpHtmlPrev +
                        '<li><a href="javascript:;" class="active">' + pageIndex + '</a></li>' +
                        tmpHtmlNext +
                        ellipsisBtn +
                        '<li><a id="next_page" href="javascript:;">' + next + '</a></li>' +
                        '<li><a id="last_page" href="javascript:;">' + last + '</a></li>';
                } else if (pageIndex - degeCount < degeCount - 1 && totalPage - pageIndex < degeCount + 1) {   //前后都不需要
                    var countPrev = pageIndex - 1;  //前需要显示的页码个数
                    var countNext = totalPage - pageIndex;  //后需要显示的页码个数
                    if (pageIndex != 1) {
                        for (var i = 0; i < countPrev; i++) {
                            tmpHtmlPrev += '<li><a href="javascript:;" class="page-number">' + (pageIndex - (countPrev - i)) + '</a></li>';
                        }
                    }
                    for (var i = 0; i < countNext; i++) {
                        tmpHtmlNext += '<li><a href="javascript:;" class="page-number">' + ((pageIndex - 0) + i + 1) + '</a></li>';
                    }
                    pageHtml = '<li><a id="first_page" href="javascript:;">' + first + '</a></li>' +
                        '<li><a id="prev_page" href="javascript:;">' + prev + '</a></li>' +
                        tmpHtmlPrev +
                        '<li><a href="javascript:;" class="active">' + pageIndex + '</a></li>' +
                        tmpHtmlNext +
                        '<li><a id="next_page" href="javascript:;">' + next + '</a></li>' +
                        '<li><a id="last_page" href="javascript:;">' + last + '</a></li>';
                }
                document.getElementById('page_ul').innerHTML = pageHtml;
            }

            /*点击页码（首页、上一页、下一页、末页）*/
            document.getElementById('page_ul').addEventListener('click', function (e) {
                var _this = e.target;   //当前被点击的a标签
                var idAttr = _this.id;  //id属性
                var className = _this.className;    //class属性
                if (idAttr == 'first_page') { //如果是点击的首页
                    scope.pageConfig.pageIndex = 1;
                } else if (idAttr == 'prev_page') {    //如果点击的是上一页
                    scope.pageConfig.pageIndex = scope.pageConfig.pageIndex == 1 ? scope.pageConfig.pageIndex : scope.pageConfig.pageIndex - 1;
                } else if (idAttr == 'next_page') { //如果点击的是下一页
                    scope.pageConfig.pageIndex = scope.pageConfig.pageIndex == scope.pageConfig.totalPage ? scope.pageConfig.pageIndex : parseInt(scope.pageConfig.pageIndex) + 1;
                } else if (idAttr == 'last_page') { //如果点击的是末页
                    scope.pageConfig.pageIndex = scope.pageConfig.totalPage;
                } else if (className == 'page-number') {   //如果点击的是数字页码
                    scope.pageConfig.pageIndex = _this.innerText;
                }
                initPage(scope.pageConfig.totalPage, scope.pageConfig.pageIndex, degeCount);
                scope.$emit('clickPage');
            });
            /*改变每页条数*/
            document.getElementById('page_size').addEventListener('change', function () {
                var _this = this;
                scope.pageConfig.pageIndex = 1;
                scope.pageConfig.pageSize = _this.value - 0;

                scope.pageConfig.totalPage = Math.ceil(scope.pageConfig.totalCount / scope.pageConfig.pageSize);
                console.log("totalPage:"+scope.pageConfig.totalPage);
                initPage(scope.pageConfig.totalPage, scope.pageConfig.pageIndex, degeCount);
                scope.$emit('clickPage');
            })
        };

        return {
            restrict: 'EA',
            'scope': {
                'pageConfig': '=',
            },
            templateUrl: 'app/views/directive/pageConfig/page-directive.html',
            link: link
        };
    }
]);

App.directive('treeView2', [function () {
    return {
        restrict: 'E',
        templateUrl: 'treeView.html',
        scope: {
            treeData: '=',
            canChecked: '=',
            textField: '@',
            itemClicked: '&',
            itemCheckedChanged: '&',
            itemTemplateUrl: '@',
            deleteUrl: '@',
            addUrl: '@',
            paramOne: '@'
        },
        controller: ['$scope', '$http', 'myservice', function ($scope, $http, myservice) {
            $scope.itemExpended = function (item, $event) {
                item.$$isExpend = !item.$$isExpend;
                $event.stopPropagation();
            };

            $scope.getItemIcon = function (item) {
                var isLeaf = $scope.isLeaf(item);

                if (isLeaf) {
                    return 'fa fa-leaf';
                }

                return item.$$isExpend ? 'fa fa-minus' : 'fa fa-plus';
            };

            $scope.isLeaf = function (item) {
                return !item.submenu || !item.submenu.length;
            };

            $scope.chk = function (callback, item) {
                var itemId = item.id;

            };

            $scope.warpCallback = function (callback, item, $event) {

                ($scope[callback] || angular.noop)({
                    $item: item,
                    $event: $event
                });
            };

            function setAllExpanFlag(tree, flag) {
                angular.forEach(tree, function (e) {
                    e.$$isExpend = flag;
                    if (e.submenu) {
                        setAllExpanFlag(e.submenu, flag);
                    }
                });
            }

            $scope.expandAll = function (tree) {
                setAllExpanFlag(tree, true);
            }

            $scope.collapseAll = function (tree) {
                setAllExpanFlag(tree, false);
            }


            //递归获取所有的节点
            var allNode = [];

            function getAllNode(tree, allNode, father) {
                angular.forEach(tree, function (e) {
                    if (father) {
                        e.fatherId = father.id;
                    }
                    allNode.push(e);
                    if (e.submenu && e.submenu.length > 0) {
                        e.childSize = e.submenu.length;
                        getAllNode(e.submenu, allNode, e);
                    } else {
                        e.childSize = 0;
                    }
                });
            }

            //角色新增节点
            var newNodeAdd = [];

            //递归把已勾选的所有父节点进行勾选,并找出新增勾选
            function doCheck(item, allNode) {

                doCheckFather(item, allNode)
                doCheckChildren(item, allNode);

            }

            function doCheckFather(item, allNode) {
                //把父亲勾上
                angular.forEach(allNode, function (e) {
                    if (e.id && e.id== item.fatherId) {
                        e.isChecked = true;
                            doCheckFather(e, allNode);
                    }
                });
            }

            function doCheckChildren(item, allNode) {
                //把儿子也勾上
                angular.forEach(allNode, function (e) {
                    if (item.submenu) {
                        angular.forEach(item.submenu, function (h) {
                            if (e.id == h.id) {
                                //是他的儿子，儿子也打钩
                                if (h.isChecked == null || h.isChecked == undefined || h.isChecked == false) {
                                    newNodeAdd.push(h);
                                }
                                h.isChecked = true;
                            }
                            if (h.submenu && h.submenu.length > 0) {
                                doCheckChildren(h, allNode);
                            }
                        });
                    }

                });
            }

            var deleteNodes = [];

            function deleteNode(item, allNode) {
                //和item平级的全部去掉勾选则父节点也去掉勾选。
                //1.找出所有子节点去掉勾选
                angular.forEach(allNode, function (e) {
                    if (item.id == e.fatherId) {
                        if (!(e.isChecked == null || e.isChecked == undefined || e.isChecked == false)) {
                            deleteNodes.push(e);
                            e.isChecked = false;

                            deleteNode(e, allNode);
                        }
                    }
                })
            }

            function OwnerMenuDto(ownerId, menuId) {
                var o = new Object();
                o.ownerId = ownerId;
                o.menuId = menuId;
                return o;
            }


            $scope.itemChange = function (item, tree) {
                //组装父子结构
                getAllNode(tree, allNode, null);
                //如果变化的那个变成勾选状态则他的父级依次递归都勾选，并且记录改变的项
                if (item.isChecked) {
                    var msg = "您真的确定授权吗？\n\n请确认！";
                    if (confirm(msg) == false) {
                        item.isChecked = !item.isChecked;
                        return;
                    }

                    newNodeAdd = [];
                    if (item.fatherId) {
                        newNodeAdd.push(item);
                    }
                    doCheck(item, allNode);

                    var OwnerMenuDtoArr = []
                    angular.forEach(newNodeAdd, function (e) {
                        if (null != e.id) {
                            OwnerMenuDtoArr.push(OwnerMenuDto($scope.paramOne, e.id));
                        }
                    });
                    $http.post($scope.addUrl, OwnerMenuDtoArr).success(function (data) {
                        var jsonString = angular.toJson(data);
                        var temp = angular.fromJson(jsonString);
                        myservice.errors(temp);
                    }).error(function (data) {
                        alert("会话已经断开或者检查网络是否正常！");
                    });
                } else {
                    var msg = "您真的确定取消授权吗？\n\n请确认！";
                    if (confirm(msg) == false) {
                        item.isChecked = !item.isChecked;
                        return;
                    }
                    deleteNodes = [];
                    if (item.fatherId) {
                        deleteNodes.push(item);
                    }

                    deleteNode(item, allNode);

                    var OwnerMenuDtoArr = []
                    angular.forEach(deleteNodes, function (e) {

                        if (null != e.id) {
                            OwnerMenuDtoArr.push(OwnerMenuDto($scope.paramOne, e.id));
                        }

                    });
                    $http.post($scope.deleteUrl, OwnerMenuDtoArr).success(function (data) {
                        var jsonString = angular.toJson(data);
                        var temp = angular.fromJson(jsonString);
                        myservice.errors(temp);
                    }).error(function (data) {
                        alert("会话已经断开或者检查网络是否正常！");
                    });
                }
            };
        }]
    };
}]);


App.directive('treeView', [function () {
    return {
        restrict: 'E',
        templateUrl: 'treeView.html',
        scope: {
            treeData: '=',
            canChecked: '=',
            textField: '@',
            itemClicked: '&',
            itemCheckedChanged: '&',
            itemTemplateUrl: '@',
            deleteUrl: '@',
            addUrl: '@',
            paramOne: '@'
        },
        controller: ['$scope', '$http', 'myservice', function ($scope, $http, myservice) {
            $scope.itemExpended = function (item, $event) {
                item.$$isExpend = !item.$$isExpend;
                $event.stopPropagation();
            };

            $scope.getItemIcon = function (item) {
                var isLeaf = $scope.isLeaf(item);

                if (isLeaf) {
                    return 'fa fa-leaf';
                }

                return item.$$isExpend ? 'fa fa-minus' : 'fa fa-plus';
            };

            $scope.isLeaf = function (item) {
                return !item.submenu || !item.submenu.length;
            };

            $scope.chk = function (callback, item) {
                var itemId = item.id;

            };

            $scope.warpCallback = function (callback, item, $event) {

                ($scope[callback] || angular.noop)({
                    $item: item,
                    $event: $event
                });
            };

            function setAllExpanFlag(tree, flag) {
                angular.forEach(tree, function (e) {
                    e.$$isExpend = flag;
                    if (e.submenu) {
                        setAllExpanFlag(e.submenu, flag);
                    }
                });
            }

            $scope.expandAll = function (tree) {
                setAllExpanFlag(tree, true);
            }

            $scope.collapseAll = function (tree) {
                setAllExpanFlag(tree, false);
            }


            //递归获取所有的节点
            var allNode = [];

            function getAllNode(tree, allNode, father) {
                angular.forEach(tree, function (e) {
                    if (father) {
                        e.fatherId = father.id;
                    }
                    allNode.push(e);
                    if (e.submenu && e.submenu.length > 0) {
                        e.childSize = e.submenu.length;
                        getAllNode(e.submenu, allNode, e);
                    } else {
                        e.childSize = 0;
                    }
                });
            }

            //角色新增节点
            var newNodeAdd = [];

            function doCheck(item, allNode) {
                doCheckFather(item, allNode);
                doCheckChildren(item, allNode)
            }

            //递归把已勾选的所有父节点进行勾选,并找出新增勾选
            function doCheckFather(item, allNode) {
                //把父亲勾上
                angular.forEach(allNode, function (e) {
                    if (e.id && e.id == item.fatherId) {
                        e.isChecked = true;
                        doCheckFather(e, allNode);
                    }
                });
                //doCheckChildren(item, allNode);
            }

            function doCheckChildren(item, allNode) {
                //把儿子也勾上
                angular.forEach(allNode, function (e) {
                    if (item.submenu.length > 0) {
                        angular.forEach(item.submenu, function (h) {
                            if (e.id == h.id) {
                                //是他的儿子，儿子也打钩
                                if (h.isChecked == null || h.isChecked == undefined || h.isChecked == false) {
                                    newNodeAdd.push(h);
                                }
                                h.isChecked = true;
                            }
                            if (h.submenu && h.submenu.length > 0) {
                                doCheckChildren(h, allNode);
                            }
                        });
                    }
                });
            }

            var deleteNodes = [];

            function deleteNode(item, allNode) {
                //和item平级的全部去掉勾选则父节点也去掉勾选。
                //1.找出所有子节点去掉勾选
                angular.forEach(allNode, function (e) {
                    if (item.id == e.fatherId) {
                        if (!(e.isChecked == null || e.isChecked == undefined || e.isChecked == false)) {
                            deleteNodes.push(e);
                            e.isChecked = false;

                            deleteNode(e, allNode);
                        }
                    }
                })
            }

            function OwnerMenuDto(ownerId, menuId) {
                var o = new Object();
                o.ownerId = ownerId;
                o.menuId = menuId;
                return o;
            }


            $scope.itemChange = function (item, tree) {
                //如果变化的那个变成勾选状态则他的父级依次递归都勾选，并且记录改变的项
                if (item.isChecked) {
                    newNodeAdd = [];
                    newNodeAdd.push(item);
                    getAllNode(tree, allNode, null);
                    doCheck(item, allNode);

                    var OwnerMenuDtoArr = []
                    angular.forEach(newNodeAdd, function (e) {
                        if (null != e.id) {
                            console.log(e.id);
                            OwnerMenuDtoArr.push(OwnerMenuDto($scope.paramOne, e.id));
                        }
                    });
                    $http.post($scope.addUrl, OwnerMenuDtoArr).success(function (data) {
                        var jsonString = angular.toJson(data);
                        var temp = angular.fromJson(jsonString);
                        myservice.errors(temp);
                    }).error(function (data) {
                        alert("会话已经断开或者检查网络是否正常！");
                    });
                } else {
                    deleteNodes = [];
                    deleteNodes.push(item);
                    deleteNode(item, allNode);

                    var OwnerMenuDtoArr = []
                    angular.forEach(deleteNodes, function (e) {

                        if (null != e.id) {
                            OwnerMenuDtoArr.push(OwnerMenuDto($scope.paramOne, e.id));
                        }

                    });
                    $http.post($scope.deleteUrl, OwnerMenuDtoArr).success(function (data) {
                        var jsonString = angular.toJson(data);
                        var temp = angular.fromJson(jsonString);
                        myservice.errors(temp);
                    }).error(function (data) {
                        alert("会话已经断开或者检查网络是否正常！");
                    });
                }
            };
        }]
    };
}]);


/**=========================================================
 * Module: colors.js
 * Services to retrieve global colors
 =========================================================*/

App.factory('cytoscapeAttrFactory', function ($http, $q) {

    var colors = {
        colors_01: "#993284",
        colors_02: "#99576d",
        colors_03: "#217999",
        colors_04: "#706699",
        colors_05: "#992508",
        colors_06: "#993C8E",
        colors_07: "#786799",
        colors_08: "#6B8299",
        colors_09: "#189911",
        colors_10: "#6d7577"
    }

    return {
        colors: colors
    }

});

App.service('cytoscapeAttrService', function ($http, $q, myservice) {

    this.getElements = function () {

        var promise = $http
            .post('/neo4jRelation/getNeo4jRelations?relationship=sss&relationId=1');
        promise.then(function (data) {

            var jsonString = angular.toJson(data);
            var temp = angular.fromJson(jsonString);
            return temp.obj;
        })

    }
});


App.directive('cytoscape3', ['$rootScope', 'cytoscapeAttrService', function ($rootScope, cytoscapeAttrService) {

    return {
        restrict: 'E',
        template: '<div id="cy"></div>',
        replace: true,
        scope: {
            myData: '=',
            cyClick: '&'
        },
        link: function (scope, element, attrs, fn) {
            scope.doCy = function () {
                $('#cy').cytoscape({
                    layout: {
                        name: 'concentric',
                        fit: true,
                        padding: 30,
                        startAngle: 4 / 2 * Math.PI,
                        sweep: undefined,
                        clockwise: true,
                        equidistant: false,
                        minNodeSpacing: 100
                    },
                    style: cytoscape.stylesheet()
                        .selector('node')//节点样式
                        .css({
                            'content': 'data(name)',
                            'text-valign': 'center',
                            'color': 'white',
                            "height": 60,
                            "width": 60,

                            'text-outline-width': 2,
                            'text-outline-color': '#316383',//颜色设置
                            "background-color": "#316383",
                            "label": "data(label)"
                        })
                        .selector('edge')//边线样式
                        .css({
                            'curve-style': 'bezier',
                            "label": "data(label)",
                            'target-arrow-shape': 'triangle',
                            'target-arrow-color': 'black',
                            'line-color': '#ccc',
                            'width': 1
                        })
                        .selector(':selected')
                        .css({
                            'content': 'data(value)',
                            'background-color': 'red',
                            'line-color': 'red',
                            'target-arrow-color': 'red',
                            'source-arrow-color': 'red'
                        })
                        .selector('.background')
                        .css({
                            "text-background-opacity": 1,
                            "text-background-color": "#ccc",
                            "text-background-shape": "roundrectangle",
                            "text-border-color": "#000",
                            "text-border-width": 1,
                            "text-border-opacity": 1
                        })
                        .selector('node[label="main"]')//主节点样式设置
                        .css({
                            "background-color": '#d0413e',
                            'text-outline-width': 2,
                            'text-outline-color': '#d0413e',

                        })
                        .selector('.faded')
                        .css({
                            'opacity': 0.25,
                            'text-opacity': 0
                        }),

                    ready: function () {
                        var aa = this;

                        // giddy up...
                        aa.elements().unselectify();

                        // Event listeners
                        // with sample calling to the controller function as passed as an attribute
                        aa.on('tap', 'node', function (e) {

                            var evtTarget = e.cyTarget;
                            var nodeId = evtTarget.id();
                            scope.cyClick({value: nodeId});
                        });


                        aa.load(scope.myData);
                    }

                });
            };


            $rootScope.$on('showNeo4j', function () {
                scope.doCy();
            });


        }

    }
}])

App.directive('cytoscape2', ['$rootScope', 'cytoscapeAttrFactory', function ($rootScope, cytoscapeAttrFactory) {

    return {
        restrict: 'E',
        template: '<div id="cy"></div>',
        replace: true,
        scope: {
            neo4jStyle: '@',
            neo4jElement: '@'
        },
        link: function (scope, element, attrs, fn) {

            $scope.ele = cytoscapeAttrService.getElements();
            var cy = window.cy = cytoscape({
                container: document.getElementById('cy'),
                boxSelectionEnabled: true,
                style: [
                    {
                        selector: 'node',
                        style: {
                            'background-color': cytoscapeAttrFactory.colors.colors_01,
                            'text-valign': 'center',
                            'content': 'data(id)'
                        }
                    },
                    {
                        selector: 'node[id = "a"]',
                        style: {
                            'background-color': cytoscapeAttrFactory.colors.colors_08,
                            'text-valign': 'center',
                            'content': 'data(id)'
                        }
                    },


                    {
                        selector: 'edge',
                        style: {
                            'content': 'data(relationship)',
                            'curve-style': 'bezier',
                            'target-arrow-shape': 'triangle'
                        }
                    }
                ],
                elements: $scope.ele,
                /*
                elements: {
                    nodes: [
                        { data: { id: 'a' } },
                        { data: { id: 'a' } },
                        { data: { id: 'b' } },
                        { data: { id: 'c' } },
                        { data: { id: 'd' } }
                    ],
                    edges: [
                        { data: { id: 'aa', source: 'a', target: 'a' , relationship: 'bbb'} },
                        { data: { id: 'ab', source: 'a', target: 'b' , relationship: 'hhh'} },
                        { data: { id: 'ac', source: 'a', target: 'b' , relationship: 'xxx'} },
                        { data: { id: 'ba', source: 'b', target: 'a' , relationship: 'kkk'} },
                        { data: { id: 'ca', source: 'b', target: 'a' , relationship: 'ccc'} }
                    ]
                },*/
                layout: {
                    name: 'circle',
                    fit: true,
                    //name: 'grid',
                    padding: 5
                }
            })
        }
    }
}]);

App.directive('cytoscape', ['$rootScope', function ($rootScope) {
    // graph visualisation by - https://github.com/cytoscape/cytoscape.js
    return {
        restrict: 'E',
        template: '<div id="cy"></div>',
        replace: true,
        scope: {
            // data objects to be passed as an attributes - for nodes and edges
            cyData: '=',
            cyEdges: '=',
            // controller function to be triggered when clicking on a node
            cyClick: '&'
        },
        link: function (scope, element, attrs, fn) {
            // dictionary of colors by types. Just to show some design options
            scope.typeColors = {
                'ellipse': '#992222',
                'triangle': '#0000ff',
                'rectangle': '#661199',
                'roundrectangle': '#772244',
                'pentagon': '#808080',
                'hexagon': '#229988',
                'heptagon': '#118844',
                'octagon': '#335577',
                'star': '#113355'
            };

            // graph  build
            scope.doCy = function () { // will be triggered on an event broadcast
                // initialize data object
                scope.elements = {};
                scope.elements.nodes = [];
                scope.elements.edges = [];

                // parse edges
                // you can build a complete object in the controller and pass it without rebuilding it in the directive.
                // doing it like that allows you to add options, design or what needed to the objects
                // doing it like that is also good if your data object/s has a different structure
                for (i = 0; i < scope.cyEdges.length; i++) {
                    // get edge source
                    var eSource = scope.cyEdges[i].source;
                    // get edge target
                    var eTarget = scope.cyEdges[i].target;
                    // get edge id
                    var eId = scope.cyEdges[i].id;
                    // build the edge object
                    var edgeObj = {
                        data: {
                            id: eId,
                            source: eSource,
                            target: eTarget
                        }
                    };
                    // adding the edge object to the edges array
                    scope.elements.edges.push(edgeObj);
                }

                // parse data and create the Nodes array
                // object type - is the object's group
                for (i = 0; i < scope.cyData.length; i++) {
                    // get id, name and type  from the object
                    var dId = scope.cyData[i].id;
                    var dName = scope.cyData[i].name;
                    var dType = scope.cyData[i].type;
                    // get color from the object-color dictionary
                    var typeColor = scope.typeColors['rectangle'];
                    // build the object, add or change properties as you need - just have a name and id
                    var elementObj = {
                        group: dType, 'data': {
                            id: dId,
                            name: dName,
                            typeColor: typeColor,
                            typeShape: dType,
                            type: dType
                        }
                    };
                    // add new object to the Nodes array
                    scope.elements.nodes.push(elementObj);
                }
                // graph  initialization
                // use object's properties as properties using: data(propertyName)
                // check Cytoscapes site for much more data, options, designs etc
                // http://cytoscape.github.io/cytoscape.js/
                // here are just some basic options
                $('#cy').cytoscape({
                    layout: {
                        name: 'circle',
                        fit: true, // whether to fit the viewport to the graph
                        ready: undefined, // callback on layoutready
                        stop: undefined, // callback on layoutstop
                        mouseover: undefined,
                        padding: 5 // the padding on fit
                    },
                    style: cytoscape.stylesheet()
                        .selector('node')
                        .css({
                            'shape': 'data(typeShape)',
                            'width': '120',
                            'height': '90',
                            'background-color': 'data(typeColor)',
                            'content': 'data(name)',
                            'text-valign': 'center',
                            'color': 'white',
                            'text-outline-width': 2,
                            'text-outline-color': 'data(typeColor)'
                        })
                        .selector('edge')
                        .css({
                            'target-arrow-shape': 'triangle',
                            'source-arrow-shape': 'triangle'
                        })
                        .selector(':selected')
                        .css({
                            'background-color': 'black',
                            'line-color': 'black',
                            'target-arrow-color': 'black',
                            'source-arrow-color': 'black'
                        })
                        .selector('.faded')
                        .css({
                            'opacity': 0.65,
                            'text-opacity': 0.65
                        }),
                    ready: function () {
                        var cy = window.cy = this;

                        // giddy up...
                        cy.elements().unselectify();

                        // Event listeners
                        // with sample calling to the controller function as passed as an attribute
                        cy.on('tap', 'node', function (e) {

                            var evtTarget = e.cyTarget;
                            var nodeId = evtTarget.id();
                            scope.cyClick({value: nodeId});
                        });

                        // load the objects array
                        // use cy.add() / cy.remove() with passed data to add or remove nodes and edges without rebuilding the graph
                        // sample use can be adding a passed variable which will be broadcast on change
                        cy.load(scope.elements);
                    }
                });

            }; // end doCy()
            // When the app object changed = redraw the graph
            // you can use it to pass data to be added or removed from the object without redrawing it
            // using cy.remove() / cy.add()
            $rootScope.$on('appChanged', function () {
                scope.doCy();
            });
        }
    };
}]);
/**=========================================================
 * Module: animate-enabled.js
 * Enable or disables ngAnimate for element with directive
 =========================================================*/

App.directive("animateEnabled", ["$animate", function ($animate) {
    return {
        link: function (scope, element, attrs) {
            scope.$watch(function () {
                return scope.$eval(attrs.animateEnabled, scope);
            }, function (newValue) {
                $animate.enabled(!!newValue, element);
            });
        }
    };
}]);
/**=========================================================
 * Module: chart.js
 * Wrapper directive for chartJS.
 * Based on https://gist.github.com/AndreasHeiberg/9837868
 =========================================================*/

var ChartJS = function (type) {
    return {
        restrict: "A",
        scope: {
            data: "=",
            options: "=",
            id: "@",
            width: "=",
            height: "=",
            resize: "=",
            chart: "@",
            segments: "@",
            responsive: "=",
            tooltip: "=",
            legend: "="
        },
        link: function ($scope, $elem) {
            var ctx = $elem[0].getContext("2d");
            var autosize = false;

            $scope.size = function () {
                if ($scope.width <= 0) {
                    $elem.width($elem.parent().width());
                    ctx.canvas.width = $elem.width();
                } else {
                    ctx.canvas.width = $scope.width || ctx.canvas.width;
                    autosize = true;
                }

                if ($scope.height <= 0) {
                    $elem.height($elem.parent().height());
                    ctx.canvas.height = ctx.canvas.width / 2;
                } else {
                    ctx.canvas.height = $scope.height || ctx.canvas.height;
                    autosize = true;
                }
            };

            $scope.$watch("data", function (newVal, oldVal) {
                if (chartCreated)
                    chartCreated.destroy();

                // if data not defined, exit
                if (!newVal) {
                    return;
                }
                if ($scope.chart) {
                    type = $scope.chart;
                }

                if (autosize) {
                    $scope.size();
                    chart = new Chart(ctx);
                }

                if ($scope.responsive || $scope.resize)
                    $scope.options.responsive = true;

                if ($scope.responsive !== undefined)
                    $scope.options.responsive = $scope.responsive;

                chartCreated = chart[type]($scope.data, $scope.options);
                chartCreated.update();
                if ($scope.legend)
                    angular.element($elem[0]).parent().after(chartCreated.generateLegend());
            }, true);

            $scope.$watch("tooltip", function (newVal, oldVal) {
                if (chartCreated)
                    chartCreated.draw();
                if (newVal === undefined || !chartCreated.segments)
                    return;
                if (!isFinite(newVal) || newVal >= chartCreated.segments.length || newVal < 0)
                    return;
                var activeSegment = chartCreated.segments[newVal];
                activeSegment.save();
                activeSegment.fillColor = activeSegment.highlightColor;
                chartCreated.showTooltip([activeSegment]);
                activeSegment.restore();
            }, true);

            $scope.size();
            var chart = new Chart(ctx);
            var chartCreated;
        }
    };
};

/* Aliases for various chart types */
App.directive("chartjs", function () {
    return ChartJS();
});
App.directive("linechart", function () {
    return ChartJS("Line");
});
App.directive("barchart", function () {
    return ChartJS("Bar");
});
App.directive("radarchart", function () {
    return ChartJS("Radar");
});
App.directive("polarchart", function () {
    return ChartJS("PolarArea");
});
App.directive("piechart", function () {
    return ChartJS("Pie");
});
App.directive("doughnutchart", function () {
    return ChartJS("Doughnut");
});
App.directive("donutchart", function () {
    return ChartJS("Doughnut");
});

/**=========================================================
 * Module: classy-loader.js
 * Enable use of classyloader directly from data attributes
 =========================================================*/

App.directive('classyloader', ["$timeout", "Utils", function ($timeout, Utils) {
    'use strict';

    var $scroller = $(window),
        inViewFlagClass = 'js-is-in-view'; // a classname to detect when a chart has been triggered after scroll

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            // run after interpolation
            $timeout(function () {

                var $element = $(element),
                    options = $element.data();

                // At lease we need a data-percentage attribute
                if (options) {
                    if (options.triggerInView) {

                        $scroller.scroll(function () {
                            checkLoaderInVIew($element, options);
                        });
                        // if the element starts already in view
                        checkLoaderInVIew($element, options);
                    } else
                        startLoader($element, options);
                }

            }, 0);

            function checkLoaderInVIew(element, options) {
                var offset = -20;
                if (!element.hasClass(inViewFlagClass) &&
                    Utils.isInView(element, {topoffset: offset})) {
                    startLoader(element, options);
                }
            }

            function startLoader(element, options) {
                element.ClassyLoader(options).addClass(inViewFlagClass);
            }
        }
    };
}]);

App.directive('draggable', ['$document', function ($document) {
    return function (scope, element, attr) {
        var startX = 0, startY = 0, x = 0, y = 0;
        element = angular.element(document.getElementsByClassName("modal-dialog"));
        element.css({
            position: 'relative',
            cursor: 'move'
        });

        element.on('mousedown', function (event) {
            // Prevent default dragging of selected content
            event.preventDefault();
            startX = event.pageX - x;
            startY = event.pageY - y;
            $document.on('mousemove', mousemove);
            $document.on('mouseup', mouseup);
        });

        function mousemove(event) {
            y = event.pageY - startY;
            x = event.pageX - startX;
            element.css({
                top: y + 'px',
                left: x + 'px'
            });
        }

        function mouseup() {
            $document.off('mousemove', mousemove);
            $document.off('mouseup', mouseup);
        }
    };
}]);

/**=========================================================
 * Module: clear-storage.js
 * Removes a key from the browser storage via element click
 =========================================================*/

App.directive('resetKey', ['$state', '$rootScope', function ($state, $rootScope) {
    'use strict';

    return {
        restrict: 'A',
        scope: {
            resetKey: '='
        },
        link: function (scope, element, attrs) {

            scope.resetKey = attrs.resetKey;

        },
        controller: ["$scope", "$element", function ($scope, $element) {

            $element.on('click', function (e) {
                e.preventDefault();

                if ($scope.resetKey) {
                    delete $rootScope.$storage[$scope.resetKey];
                    $state.go($state.current, {}, {reload: true});
                } else {
                    $.error('No storage key specified for reset.');
                }
            });

        }]

    };

}]);
/**=========================================================
 * Module: filestyle.js
 * Initializes the fielstyle plugin
 =========================================================*/

App.directive('filestyle', function () {
    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {
            var options = $element.data();

            // old usage support
            options.classInput = $element.data('classinput') || options.classInput;

            $element.filestyle(options);
        }]
    };
});

/**=========================================================
 * Module: flatdoc.js
 * Creates the flatdoc markup and initializes the plugin
 =========================================================*/

App.directive('flatdoc', ['$location', function ($location) {
    return {
        restrict: "EA",
        template: "<div role='flatdoc'><div role='flatdoc-menu'></div><div role='flatdoc-content'></div></div>",
        link: function (scope, element, attrs) {

            Flatdoc.run({
                fetcher: Flatdoc.file(attrs.src)
            });

            var $root = $('html, body');
            $(document).on('flatdoc:ready', function () {
                var docMenu = $('[role="flatdoc-menu"]');
                docMenu.find('a').on('click', function (e) {
                    e.preventDefault();
                    e.stopPropagation();

                    var $this = $(this);

                    docMenu.find('a.active').removeClass('active');
                    $this.addClass('active');

                    $root.animate({
                        scrollTop: $(this.getAttribute('href')).offset().top - ($('.topnavbar').height() + 10)
                    }, 800);
                });

            });
        }
    };

}]);
/**=========================================================
 * Module: flot.js
 * Initializes the Flot chart plugin and handles data refresh
 =========================================================*/

App.directive('flot', ['$http', '$timeout', function ($http, $timeout) {
    'use strict';
    return {
        restrict: 'EA',
        template: '<div></div>',
        scope: {
            dataset: '=?',
            options: '=',
            series: '=',
            callback: '=',
            src: '='
        },
        link: linkFunction
    };

    function linkFunction(scope, element, attributes) {
        var height, plot, plotArea, width;
        var heightDefault = 220;

        plot = null;

        width = attributes.width || '100%';
        height = attributes.height || heightDefault;

        plotArea = $(element.children()[0]);
        plotArea.css({
            width: width,
            height: height
        });

        function init() {
            var plotObj;
            if (!scope.dataset || !scope.options) return;
            plotObj = $.plot(plotArea, scope.dataset, scope.options);
            scope.$emit('plotReady', plotObj);
            if (scope.callback) {
                scope.callback(plotObj, scope);
            }

            return plotObj;
        }

        function onDatasetChanged(dataset) {
            if (plot) {
                plot.setData(dataset);
                plot.setupGrid();
                return plot.draw();
            } else {
                plot = init();
                onSerieToggled(scope.series);
                return plot;
            }
        }

        scope.$watchCollection('dataset', onDatasetChanged, true);

        function onSerieToggled(series) {
            if (!plot || !series) return;
            var someData = plot.getData();
            for (var sName in series) {
                angular.forEach(series[sName], toggleFor(sName));
            }

            plot.setData(someData);
            plot.draw();

            function toggleFor(sName) {
                return function (s, i) {
                    if (someData[i] && someData[i][sName])
                        someData[i][sName].show = s;
                };
            }
        }

        scope.$watch('series', onSerieToggled, true);

        function onSrcChanged(src) {

            if (src) {

                $http.get(src)
                    .success(function (data) {

                        $timeout(function () {
                            scope.dataset = data;
                        });

                    }).error(function () {
                    $.error('Flot chart: Bad request.');
                });

            }
        }

        scope.$watch('src', onSrcChanged);
    }

}]);

/**=========================================================
 * Module: form-wizard.js
 * Handles form wizard plugin and validation
 =========================================================*/

App.directive('formWizard', ["$parse", function ($parse) {
    'use strict';

    return {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attribute) {
            var validate = $parse(attribute.validateSteps)(scope),
                wiz = new Wizard(attribute.steps, !!validate, element);
            scope.wizard = wiz.init();

        }
    };

    function Wizard(quantity, validate, element) {

        var self = this;
        self.quantity = parseInt(quantity, 10);
        self.validate = validate;
        self.element = element;

        self.init = function () {
            self.createsteps(self.quantity);
            self.go(1); // always start at fist step
            return self;
        };

        self.go = function (step) {

            if (angular.isDefined(self.steps[step])) {

                if (self.validate && step !== 1) {
                    var form = $(self.element),
                        group = form.children().children('div').get(step - 2);

                    if (false === form.parsley().validate(group.id)) {
                        return false;
                    }
                }

                self.cleanall();
                self.steps[step] = true;
            }
        };

        self.active = function (step) {
            return !!self.steps[step];
        };

        self.cleanall = function () {
            for (var i in self.steps) {
                self.steps[i] = false;
            }
        };

        self.createsteps = function (q) {
            self.steps = [];
            for (var i = 1; i <= q; i++) self.steps[i] = false;
        };

    }

}]);

/**=========================================================
 * Module: fullscreen.js
 * Toggle the fullscreen mode on/off
 =========================================================*/

App.directive('toggleFullscreen', function () {
    'use strict';

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {

            element.on('click', function (e) {
                e.preventDefault();

                if (screenfull.enabled) {

                    screenfull.toggle();

                    // Switch icon indicator
                    if (screenfull.isFullscreen)
                        $(this).children('em').removeClass('fa-expand').addClass('fa-compress');
                    else
                        $(this).children('em').removeClass('fa-compress').addClass('fa-expand');

                } else {
                    $.error('Fullscreen not enabled');
                }

            });
        }
    };

});


/**=========================================================
 * Module: load-css.js
 * Request and load into the current page a css file
 =========================================================*/

App.directive('loadCss', function () {
    'use strict';

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            element.on('click', function (e) {
                if (element.is('a')) e.preventDefault();
                var uri = attrs.loadCss,
                    link;

                if (uri) {
                    link = createLink(uri);
                    if (!link) {
                        $.error('Error creating stylesheet link element.');
                    }
                } else {
                    $.error('No stylesheet location defined.');
                }

            });

        }
    };

    function createLink(uri) {
        var linkId = 'autoloaded-stylesheet',
            oldLink = $('#' + linkId).attr('id', linkId + '-old');

        $('head').append($('<link/>').attr({
            'id': linkId,
            'rel': 'stylesheet',
            'href': uri
        }));

        if (oldLink.length) {
            oldLink.remove();
        }

        return $('#' + linkId);
    }


});
/**=========================================================
 * Module: masked,js
 * Initializes the masked inputs
 =========================================================*/

App.directive('masked', function () {
    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {
            var $elem = $($element);
            if ($.fn.inputmask)
                $elem.inputmask();
        }]
    };
});

/**=========================================================
 * Module: morris.js
 * AngularJS Directives for Morris Charts
 =========================================================*/

(function () {
    "use strict";

    App.directive('morrisBar', morrisChart('Bar'));
    App.directive('morrisDonut', morrisChart('Donut'));
    App.directive('morrisLine', morrisChart('Line'));
    App.directive('morrisArea', morrisChart('Area'));

    function morrisChart(type) {
        return function () {
            return {
                restrict: 'EA',
                scope: {
                    morrisData: '=',
                    morrisOptions: '='
                },
                link: function ($scope, elem, attrs) {
                    // start ready to watch for changes in data
                    $scope.$watch("morrisData", function (newVal, oldVal) {
                        if (newVal) {
                            $scope.morrisInstance.setData(newVal);
                            $scope.morrisInstance.redraw();
                        }
                    }, true);
                    // the element that contains the chart
                    $scope.morrisOptions.element = elem;
                    // If data defined copy to options
                    if ($scope.morrisData)
                        $scope.morrisOptions.data = $scope.morrisData;
                    // Init chart
                    $scope.morrisInstance = new Morris[type]($scope.morrisOptions);

                }
            }
        }
    }

})();

/**=========================================================
 * Module: navbar-search.js
 * Navbar search toggler * Auto dismiss on ESC key
 =========================================================*/

App.directive('searchOpen', ['navSearch', function (navSearch) {
    'use strict';

    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {
            $element
                .on('click', function (e) {
                    e.stopPropagation();
                })
                .on('click', navSearch.toggle);
        }]
    };

}]).directive('searchDismiss', ['navSearch', function (navSearch) {
    'use strict';

    var inputSelector = '.navbar-form input[type="text"]';

    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {

            $(inputSelector)
                .on('click', function (e) {
                    e.stopPropagation();
                })
                .on('keyup', function (e) {
                    if (e.keyCode == 27) // ESC
                        navSearch.dismiss();
                });

            // click anywhere closes the search
            $(document).on('click', navSearch.dismiss);
            // dismissable options
            $element
                .on('click', function (e) {
                    e.stopPropagation();
                })
                .on('click', navSearch.dismiss);
        }]
    };

}]);


/**=========================================================
 * Module: notify.js
 * Directive for notify plugin
 =========================================================*/

App.directive('notify', ["$window", "Notify", function ($window, Notify) {

    return {
        restrict: 'A',
        scope: {
            options: '=',
            message: '='
        },
        link: function (scope, element, attrs) {

            element.on('click', function (e) {
                e.preventDefault();
                Notify.alert(scope.message, scope.options);
            });

        }
    };

}]);


/**=========================================================
 * Module: now.js
 * Provides a simple way to display the current time formatted
 =========================================================*/

App.directive("now", ['dateFilter', '$interval', function (dateFilter, $interval) {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {

            var format = attrs.format;

            function updateTime() {
                var dt = dateFilter(new Date(), format);
                element.text(dt);
            }

            updateTime();
            $interval(updateTime, 1000);
        }
    };
}]);
/**=========================================================
 * Module panel-tools.js
 * Directive tools to control panels.
 * Allows collapse, refresh and dismiss (remove)
 * Saves panel state in browser storage
 =========================================================*/

App.directive('paneltool', ["$compile", "$timeout", function ($compile, $timeout) {
    var templates = {
        /* jshint multistr: true */
        collapse: "<a href='#' panel-collapse='' tooltip='Collapse Panel' ng-click='{{panelId}} = !{{panelId}}'> \
                <em ng-show='{{panelId}}' class='fa fa-plus'></em> \
                <em ng-show='!{{panelId}}' class='fa fa-minus'></em> \
              </a>",
        dismiss: "<a href='#' panel-dismiss='' tooltip='Close Panel'>\
               <em class='fa fa-times'></em>\
             </a>",
        refresh: "<a href='#' panel-refresh='' data-spinner='{{spinner}}' tooltip='Refresh Panel'>\
               <em class='fa fa-refresh'></em>\
             </a>"
    };

    function getTemplate(elem, attrs) {
        var temp = '';
        attrs = attrs || {};
        if (attrs.toolCollapse)
            temp += templates.collapse.replace(/{{panelId}}/g, (elem.parent().parent().attr('id')));
        if (attrs.toolDismiss)
            temp += templates.dismiss;
        if (attrs.toolRefresh)
            temp += templates.refresh.replace(/{{spinner}}/g, attrs.toolRefresh);
        return temp;
    }

    return {
        restrict: 'E',
        scope: false,
        link: function (scope, element, attrs) {

            var tools = scope.panelTools || attrs;

            $timeout(function () {
                element.html(getTemplate(element, tools)).show();
                $compile(element.contents())(scope);

                element.addClass('pull-right');
            });

        }
    };
}])
/**=========================================================
 * Dismiss panels * [panel-dismiss]
 =========================================================*/
    .directive('panelDismiss', ["$q", "Utils", function ($q, Utils) {
        'use strict';
        return {
            restrict: 'A',
            controller: ["$scope", "$element", function ($scope, $element) {
                var removeEvent = 'panel-remove',
                    removedEvent = 'panel-removed';

                $element.on('click', function () {

                    // find the first parent panel
                    var parent = $(this).closest('.panel');

                    removeElement();

                    function removeElement() {
                        var deferred = $q.defer();
                        var promise = deferred.promise;

                        // Communicate event destroying panel
                        $scope.$emit(removeEvent, parent.attr('id'), deferred);
                        promise.then(destroyMiddleware);
                    }

                    // Run the animation before destroy the panel
                    function destroyMiddleware() {
                        if (Utils.support.animation) {
                            parent.animo({animation: 'bounceOut'}, destroyPanel);
                        } else destroyPanel();
                    }

                    function destroyPanel() {

                        var col = parent.parent();
                        parent.remove();
                        // remove the parent if it is a row and is empty and not a sortable (portlet)
                        col
                            .filter(function () {
                                var el = $(this);
                                return (el.is('[class*="col-"]:not(.sortable)') && el.children('*').length === 0);
                            }).remove();

                        // Communicate event destroyed panel
                        $scope.$emit(removedEvent, parent.attr('id'));

                    }
                });
            }]
        };
    }])
    /**=========================================================
     * Collapse panels * [panel-collapse]
     =========================================================*/
    .directive('panelCollapse', ['$timeout', function ($timeout) {
        'use strict';

        var storageKeyName = 'panelState',
            storage;

        return {
            restrict: 'A',
            scope: false,
            controller: ["$scope", "$element", function ($scope, $element) {

                // Prepare the panel to be collapsible
                var $elem = $($element),
                    parent = $elem.closest('.panel'), // find the first parent panel
                    panelId = parent.attr('id');

                storage = $scope.$storage;

                // Load the saved state if exists
                var currentState = loadPanelState(panelId);
                if (typeof currentState !== 'undefined') {
                    $timeout(function () {
                            $scope[panelId] = currentState;
                        },
                        10);
                }

                // bind events to switch icons
                $element.bind('click', function () {

                    savePanelState(panelId, !$scope[panelId]);

                });
            }]
        };

        function savePanelState(id, state) {
            if (!id) return false;
            var data = angular.fromJson(storage[storageKeyName]);
            if (!data) {
                data = {};
            }
            data[id] = state;
            storage[storageKeyName] = angular.toJson(data);
        }

        function loadPanelState(id) {
            if (!id) return false;
            var data = angular.fromJson(storage[storageKeyName]);
            if (data) {
                return data[id];
            }
        }

    }])
    /**=========================================================
     * Refresh panels
     * [panel-refresh] * [data-spinner="standard"]
     =========================================================*/
    .directive('panelRefresh', ["$q", function ($q) {
        'use strict';

        return {
            restrict: 'A',
            scope: false,
            controller: ["$scope", "$element", function ($scope, $element) {

                var refreshEvent = 'panel-refresh',
                    whirlClass = 'whirl',
                    defaultSpinner = 'standard';


                // catch clicks to toggle panel refresh
                $element.on('click', function () {
                    var $this = $(this),
                        panel = $this.parents('.panel').eq(0),
                        spinner = $this.data('spinner') || defaultSpinner
                    ;

                    // start showing the spinner
                    panel.addClass(whirlClass + ' ' + spinner);

                    // Emit event when refresh clicked
                    $scope.$emit(refreshEvent, panel.attr('id'));

                });

                // listen to remove spinner
                $scope.$on('removeSpinner', removeSpinner);

                // method to clear the spinner when done
                function removeSpinner(ev, id) {
                    if (!id) return;
                    var newid = id.charAt(0) == '#' ? id : ('#' + id);
                    angular
                        .element(newid)
                        .removeClass(whirlClass);
                }
            }]
        };
    }]);

/**=========================================================
 * Module: play-animation.js
 * Provides a simple way to run animation with a trigger
 * Requires animo.js
 =========================================================*/

App.directive('animate', ["$window", "Utils", function ($window, Utils) {

    'use strict';

    var $scroller = $(window).add('body, .wrapper');

    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {

            // Parse animations params and attach trigger to scroll
            var $elem = $(elem),
                offset = $elem.data('offset'),
                delay = $elem.data('delay') || 100, // milliseconds
                animation = $elem.data('play') || 'bounce';

            if (typeof offset !== 'undefined') {

                // test if the element starts visible
                testAnimation($elem);
                // test on scroll
                $scroller.scroll(function () {
                    testAnimation($elem);
                });

            }

            // Test an element visibilty and trigger the given animation
            function testAnimation(element) {
                if (!element.hasClass('anim-running') &&
                    Utils.isInView(element, {topoffset: offset})) {
                    element
                        .addClass('anim-running');

                    setTimeout(function () {
                        element
                            .addClass('anim-done')
                            .animo({animation: animation, duration: 0.7});
                    }, delay);

                }
            }

            // Run click triggered animations
            $elem.on('click', function () {

                var $elem = $(this),
                    targetSel = $elem.data('target'),
                    animation = $elem.data('play') || 'bounce',
                    target = $(targetSel);

                if (target && target.length) {
                    target.animo({animation: animation});
                }

            });
        }
    };

}]);

/**=========================================================
 * Module: scroll.js
 * Make a content box scrollable
 =========================================================*/

App.directive('scrollable', function () {
    return {
        restrict: 'EA',
        link: function (scope, elem, attrs) {
            var defaultHeight = 250;
            elem.slimScroll({
                height: (attrs.height || defaultHeight)
            });
        }
    };
});
/**=========================================================
 * Module: sidebar.js
 * Wraps the sidebar and handles collapsed state
 =========================================================*/

App.directive('sidebar', ['$rootScope', '$window', 'Utils', function ($rootScope, $window, Utils) {

    var $win = $($window);
    var $body = $('body');
    var $scope;
    var $sidebar;
    var currentState = $rootScope.$state.current.name;

    return {
        restrict: 'EA',
        template: '<nav class="sidebar" ng-transclude></nav>',
        transclude: true,
        replace: true,
        link: function (scope, element, attrs) {

            $scope = scope;
            $sidebar = element;

            var eventName = Utils.isTouch() ? 'click' : 'mouseenter';
            var subNav = $();
            $sidebar.on(eventName, '.nav > li', function () {

                if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) {

                    subNav.trigger('mouseleave');
                    subNav = toggleMenuItem($(this));

                    // Used to detect click and touch events outside the sidebar
                    sidebarAddBackdrop();

                }

            });

            scope.$on('closeSidebarMenu', function () {
                removeFloatingNav();
            });

            // Normalize state when resize to mobile
            $win.on('resize', function () {
                if (!Utils.isMobile())
                    $body.removeClass('aside-toggled');
            });

            // Adjustment on route changes
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                currentState = toState.name;
                // Hide sidebar automatically on mobile
                $('body.aside-toggled').removeClass('aside-toggled');

                $rootScope.$broadcast('closeSidebarMenu');
            });

            // Allows to close
            if (angular.isDefined(attrs.sidebarAnyclickClose)) {

                $('.wrapper').on('click.sidebar', function (e) {
                    // don't check if sidebar not visible
                    if (!$body.hasClass('aside-toggled')) return;

                    // if not child of sidebar
                    if (!$(e.target).parents('.aside').length) {
                        $body.removeClass('aside-toggled');
                    }

                });
            }

        }
    };

    function sidebarAddBackdrop() {
        var $backdrop = $('<div/>', {'class': 'dropdown-backdrop'});
        $backdrop.insertAfter('.aside-inner').on("click mouseenter", function () {
            removeFloatingNav();
        });
    }

    // Open the collapse sidebar submenu items when on touch devices
    // - desktop only opens on hover
    function toggleTouchItem($element) {
        $element
            .siblings('li')
            .removeClass('open')
            .end()
            .toggleClass('open');
    }

    // Handles hover to open items under collapsed menu
    // -----------------------------------
    function toggleMenuItem($listItem) {

        removeFloatingNav();

        var ul = $listItem.children('ul');

        if (!ul.length) return $();
        if ($listItem.hasClass('open')) {
            toggleTouchItem($listItem);
            return $();
        }

        var $aside = $('.aside');
        var $asideInner = $('.aside-inner'); // for top offset calculation
        // float aside uses extra padding on aside
        var mar = parseInt($asideInner.css('padding-top'), 0) + parseInt($aside.css('padding-top'), 0);
        var subNav = ul.clone().appendTo($aside);

        toggleTouchItem($listItem);

        var itemTop = ($listItem.position().top + mar) - $sidebar.scrollTop();
        var vwHeight = $win.height();

        subNav
            .addClass('nav-floating')
            .css({
                position: $scope.app.layout.isFixed ? 'fixed' : 'absolute',
                top: itemTop,
                bottom: (subNav.outerHeight(true) + itemTop > vwHeight) ? 0 : 'auto'
            });

        subNav.on('mouseleave', function () {
            toggleTouchItem($listItem);
            subNav.remove();
        });

        return subNav;
    }

    function removeFloatingNav() {
        $('.dropdown-backdrop').remove();
        $('.sidebar-subnav.nav-floating').remove();
        $('.sidebar li.open').removeClass('open');
    }

}]);
/**=========================================================
 * Module: skycons.js
 * Include any animated weather icon from Skycons
 =========================================================*/

App.directive('skycon', function () {

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {

            var skycons = new Skycons({'color': (attrs.color || 'white')});

            element.html('<canvas width="' + attrs.width + '" height="' + attrs.height + '"></canvas>');

            skycons.add(element.children()[0], attrs.skycon);

            skycons.play();

        }
    };
});
/**=========================================================
 * Module: sparkline.js
 * SparkLines Mini Charts
 =========================================================*/

App.directive('sparkline', ['$timeout', '$window', function ($timeout, $window) {

    'use strict';

    return {
        restrict: 'EA',
        controller: ["$scope", "$element", function ($scope, $element) {
            var runSL = function () {
                initSparLine($element);
            };

            $timeout(runSL);
        }]
    };

    function initSparLine($element) {
        var options = $element.data();

        options.type = options.type || 'bar'; // default chart is bar
        options.disableHiddenCheck = true;

        $element.sparkline('html', options);

        if (options.resize) {
            $(window).resize(function () {
                $element.sparkline('html', options);
            });
        }
    }

}]);

/**=========================================================
 * Module: table-checkall.js
 * Tables check all checkbox
 =========================================================*/

App.directive('checkAll', function () {
    'use strict';

    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {

            $element.on('change', function () {
                var $this = $(this),
                    index = $this.index() + 1,
                    checkbox = $this.find('input[type="checkbox"]'),
                    table = $this.parents('table');
                // Make sure to affect only the correct checkbox column
                table.find('tbody > tr > td:nth-child(' + index + ') input[type="checkbox"]')
                    .prop('checked', checkbox[0].checked);

            });
        }]
    };

});
/**=========================================================
 * Module: tags-input.js
 * Initializes the tag inputs plugin
 =========================================================*/

App.directive('tagsinput', ["$timeout", function ($timeout) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ngModel) {

            element.on('itemAdded itemRemoved', function () {
                // check if view value is not empty and is a string
                // and update the view from string to an array of tags
                if (ngModel.$viewValue && ngModel.$viewValue.split) {
                    ngModel.$setViewValue(ngModel.$viewValue.split(','));
                    ngModel.$render();
                }
            });

            $timeout(function () {
                element.tagsinput();
            });

        }
    };
}]);

/**=========================================================
 * Module: toggle-state.js
 * Toggle a classname from the BODY Useful to change a state that
 * affects globally the entire layout or more than one item
 * Targeted elements must have [toggle-state="CLASS-NAME-TO-TOGGLE"]
 * User no-persist to avoid saving the sate in browser storage
 =========================================================*/

App.directive('toggleState', ['toggleStateService', function (toggle) {
    'use strict';

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {

            var $body = $('body');

            $(element)
                .on('click', function (e) {
                    e.preventDefault();
                    var classname = attrs.toggleState;

                    if (classname) {
                        if ($body.hasClass(classname)) {
                            $body.removeClass(classname);
                            if (!attrs.noPersist)
                                toggle.removeState(classname);
                        } else {
                            $body.addClass(classname);
                            if (!attrs.noPersist)
                                toggle.addState(classname);
                        }

                    }

                });
        }
    };

}]);

/**=========================================================
 * Module: trigger-resize.js
 * Triggers a window resize event from any element
 =========================================================*/

App.directive("triggerResize", ['$window', '$timeout', function ($window, $timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            element.on('click', function () {
                $timeout(function () {
                    $window.dispatchEvent(new Event('resize'))
                });
            });
        }
    };
}]);

/**=========================================================
 * Module: validate-form.js
 * Initializes the validation plugin Parsley
 =========================================================*/

App.directive('validateForm', function () {
    return {
        restrict: 'A',
        controller: ["$scope", "$element", function ($scope, $element) {
            var $elem = $($element);
            if ($.fn.parsley)
                $elem.parsley();
        }]
    };
});

/**=========================================================
 * Module: vector-map.js.js
 * Init jQuery Vector Map plugin
 =========================================================*/

App.directive('vectorMap', ['vectorMap', function (vectorMap) {
    'use strict';

    var defaultColors = {
        markerColor: '#23b7e5',      // the marker points
        bgColor: 'transparent',      // the background
        scaleColors: ['#878c9a'],    // the color of the region in the serie
        regionFill: '#bbbec6'       // the base region color
    };

    return {
        restrict: 'EA',
        link: function (scope, element, attrs) {

            var mapHeight = attrs.height || '300',
                options = {
                    markerColor: attrs.markerColor || defaultColors.markerColor,
                    bgColor: attrs.bgColor || defaultColors.bgColor,
                    scale: attrs.scale || 1,
                    scaleColors: attrs.scaleColors || defaultColors.scaleColors,
                    regionFill: attrs.regionFill || defaultColors.regionFill,
                    mapName: attrs.mapName || 'world_mill_en'
                };

            element.css('height', mapHeight);

            vectorMap.init(element, options, scope.seriesData, scope.markersData);

        }
    };

}]);
/**=========================================================
 * Module: browser.js
 * Browser detection
 =========================================================*/

App.service('browser', function () {
    "use strict";

    var matched, browser;

    var uaMatch = function (ua) {
        ua = ua.toLowerCase();

        var match = /(opr)[\/]([\w.]+)/.exec(ua) ||
            /(chrome)[ \/]([\w.]+)/.exec(ua) ||
            /(version)[ \/]([\w.]+).*(safari)[ \/]([\w.]+)/.exec(ua) ||
            /(webkit)[ \/]([\w.]+)/.exec(ua) ||
            /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
            /(msie) ([\w.]+)/.exec(ua) ||
            ua.indexOf("trident") >= 0 && /(rv)(?::| )([\w.]+)/.exec(ua) ||
            ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
            [];

        var platform_match = /(ipad)/.exec(ua) ||
            /(iphone)/.exec(ua) ||
            /(android)/.exec(ua) ||
            /(windows phone)/.exec(ua) ||
            /(win)/.exec(ua) ||
            /(mac)/.exec(ua) ||
            /(linux)/.exec(ua) ||
            /(cros)/i.exec(ua) ||
            [];

        return {
            browser: match[3] || match[1] || "",
            version: match[2] || "0",
            platform: platform_match[0] || ""
        };
    };

    matched = uaMatch(window.navigator.userAgent);
    browser = {};

    if (matched.browser) {
        browser[matched.browser] = true;
        browser.version = matched.version;
        browser.versionNumber = parseInt(matched.version);
    }

    if (matched.platform) {
        browser[matched.platform] = true;
    }

    // These are all considered mobile platforms, meaning they run a mobile browser
    if (browser.android || browser.ipad || browser.iphone || browser["windows phone"]) {
        browser.mobile = true;
    }

    // These are all considered desktop platforms, meaning they run a desktop browser
    if (browser.cros || browser.mac || browser.linux || browser.win) {
        browser.desktop = true;
    }

    // Chrome, Opera 15+ and Safari are webkit based browsers
    if (browser.chrome || browser.opr || browser.safari) {
        browser.webkit = true;
    }

    // IE11 has a new token so we will assign it msie to avoid breaking changes
    if (browser.rv) {
        var ie = "msie";

        matched.browser = ie;
        browser[ie] = true;
    }

    // Opera 15+ are identified as opr
    if (browser.opr) {
        var opera = "opera";

        matched.browser = opera;
        browser[opera] = true;
    }

    // Stock Android browsers are marked as Safari on Android.
    if (browser.safari && browser.android) {
        var android = "android";

        matched.browser = android;
        browser[android] = true;
    }

    // Assign the name and platform variable
    browser.name = matched.browser;
    browser.platform = matched.platform;


    return browser;

});
/**=========================================================
 * Module: colors.js
 * Services to retrieve global colors
 =========================================================*/

App.factory('colors', ['APP_COLORS', function (colors) {

    return {
        byName: function (name) {
            return (colors[name] || '#fff');
        }
    };

}]);


/**=========================================================
 * Module: nav-search.js
 * Services to share navbar search functions
 =========================================================*/

App.service('navSearch', function () {
    var navbarFormSelector = 'form.navbar-form';
    return {
        toggle: function () {

            var navbarForm = $(navbarFormSelector);

            navbarForm.toggleClass('open');

            var isOpen = navbarForm.hasClass('open');

            navbarForm.find('input')[isOpen ? 'focus' : 'blur']();

        },

        dismiss: function () {
            $(navbarFormSelector)
                .removeClass('open')      // Close control
                .find('input[type="text"]').blur() // remove focus
                .val('')                    // Empty input
            ;
        }
    };

});
/**=========================================================
 * Module: notify.js
 * Create a notifications that fade out automatically.
 * Based on Notify addon from UIKit (http://getuikit.com/docs/addons_notify.html)
 =========================================================*/

App.service('Notify', ["$timeout", function ($timeout) {
    this.alert = alert;

    ////////////////

    function alert(msg, opts) {
        if (msg) {
            $timeout(function () {
                $.notify(msg, opts || {});
            });
        }
    }

}]);


/**
 * Notify Addon definition as jQuery plugin
 * Adapted version to work with Bootstrap classes
 * More information http://getuikit.com/docs/addons_notify.html
 */

(function ($, window, document) {

    var containers = {},
        messages = {},

        notify = function (options) {

            if ($.type(options) == 'string') {
                options = {message: options};
            }

            if (arguments[1]) {
                options = $.extend(options, $.type(arguments[1]) == 'string' ? {status: arguments[1]} : arguments[1]);
            }

            return (new Message(options)).show();
        },
        closeAll = function (group, instantly) {
            if (group) {
                for (var id in messages) {
                    if (group === messages[id].group) messages[id].close(instantly);
                }
            } else {
                for (var id in messages) {
                    messages[id].close(instantly);
                }
            }
        };

    var Message = function (options) {

        var $this = this;

        this.options = $.extend({}, Message.defaults, options);

        this.uuid = "ID" + (new Date().getTime()) + "RAND" + (Math.ceil(Math.random() * 100000));
        this.element = $([
            // @geedmo: alert-dismissable enables bs close icon
            '<div class="uk-notify-message alert-dismissable">',
            '<a class="close">&times;</a>',
            '<div>' + this.options.message + '</div>',
            '</div>'

        ].join('')).data("notifyMessage", this);

        // status
        if (this.options.status) {
            this.element.addClass('alert alert-' + this.options.status);
            this.currentstatus = this.options.status;
        }

        this.group = this.options.group;

        messages[this.uuid] = this;

        if (!containers[this.options.pos]) {
            containers[this.options.pos] = $('<div class="uk-notify uk-notify-' + this.options.pos + '"></div>').appendTo('body').on("click", ".uk-notify-message", function () {
                $(this).data("notifyMessage").close();
            });
        }
    };


    $.extend(Message.prototype, {

        uuid: false,
        element: false,
        timout: false,
        currentstatus: "",
        group: false,

        show: function () {

            if (this.element.is(":visible")) return;

            var $this = this;

            containers[this.options.pos].show().prepend(this.element);

            var marginbottom = parseInt(this.element.css("margin-bottom"), 10);

            this.element.css({
                "opacity": 0,
                "margin-top": -1 * this.element.outerHeight(),
                "margin-bottom": 0
            }).animate({"opacity": 1, "margin-top": 0, "margin-bottom": marginbottom}, function () {

                if ($this.options.timeout) {

                    var closefn = function () {
                        $this.close();
                    };

                    $this.timeout = setTimeout(closefn, $this.options.timeout);

                    $this.element.hover(
                        function () {
                            clearTimeout($this.timeout);
                        },
                        function () {
                            $this.timeout = setTimeout(closefn, $this.options.timeout);
                        }
                    );
                }

            });

            return this;
        },

        close: function (instantly) {

            var $this = this,
                finalize = function () {
                    $this.element.remove();

                    if (!containers[$this.options.pos].children().length) {
                        containers[$this.options.pos].hide();
                    }

                    delete messages[$this.uuid];
                };

            if (this.timeout) clearTimeout(this.timeout);

            if (instantly) {
                finalize();
            } else {
                this.element.animate({
                    "opacity": 0,
                    "margin-top": -1 * this.element.outerHeight(),
                    "margin-bottom": 0
                }, function () {
                    finalize();
                });
            }
        },

        content: function (html) {

            var container = this.element.find(">div");

            if (!html) {
                return container.html();
            }

            container.html(html);

            return this;
        },

        status: function (status) {

            if (!status) {
                return this.currentstatus;
            }

            this.element.removeClass('alert alert-' + this.currentstatus).addClass('alert alert-' + status);

            this.currentstatus = status;

            return this;
        }
    });

    Message.defaults = {
        message: "",
        status: "normal",
        timeout: 5000,
        group: null,
        pos: 'top-center'
    };


    $["notify"] = notify;
    $["notify"].message = Message;
    $["notify"].closeAll = closeAll;

    return notify;

}(jQuery, window, document));

/**=========================================================
 * Module: helpers.js
 * Provides helper functions for routes definition
 =========================================================*/

App.provider('RouteHelpers', ['APP_REQUIRES', function (appRequires) {
    "use strict";

    // Set here the base of the relative path
    // for all app views
    this.basepath = function (uri) {
        return 'app/views/' + uri;
    };

    // Generates a resolve object by passing script names
    // previously configured in constant.APP_REQUIRES
    this.resolveFor = function () {
        var _args = arguments;
        return {
            deps: ['$ocLazyLoad', '$q', function ($ocLL, $q) {
                // Creates a promise chain for each argument
                var promise = $q.when(1); // empty promise
                for (var i = 0, len = _args.length; i < len; i++) {
                    promise = andThen(_args[i]);
                }
                return promise;

                // creates promise to chain dynamically
                function andThen(_arg) {
                    // also support a function that returns a promise
                    if (typeof _arg == 'function')
                        return promise.then(_arg);
                    else
                        return promise.then(function () {
                            // if is a module, pass the name. If not, pass the array
                            var whatToLoad = getRequired(_arg);
                            // simple error check
                            if (!whatToLoad) return $.error('Route resolve: Bad resource name [' + _arg + ']');
                            // finally, return a promise
                            return $ocLL.load(whatToLoad);
                        });
                }

                // check and returns required data
                // analyze module items with the form [name: '', files: []]
                // and also simple array of script files (for not angular js)
                function getRequired(name) {
                    if (appRequires.modules)
                        for (var m in appRequires.modules)
                            if (appRequires.modules[m].name && appRequires.modules[m].name === name)
                                return appRequires.modules[m];
                    return appRequires.scripts && appRequires.scripts[name];
                }

            }]
        };
    }; // resolveFor

    // not necessary, only used in config block for routes
    this.$get = function () {
        return {
            basepath: this.basepath
        }
    };

}]);


/**=========================================================
 * Module: toggle-state.js
 * Services to share toggle state functionality
 =========================================================*/

App.service('toggleStateService', ['$rootScope', function ($rootScope) {

    var storageKeyName = 'toggleState';

    // Helper object to check for words in a phrase //
    var WordChecker = {
        hasWord: function (phrase, word) {
            return new RegExp('(^|\\s)' + word + '(\\s|$)').test(phrase);
        },
        addWord: function (phrase, word) {
            if (!this.hasWord(phrase, word)) {
                return (phrase + (phrase ? ' ' : '') + word);
            }
        },
        removeWord: function (phrase, word) {
            if (this.hasWord(phrase, word)) {
                return phrase.replace(new RegExp('(^|\\s)*' + word + '(\\s|$)*', 'g'), '');
            }
        }
    };

    // Return service public methods
    return {
        // Add a state to the browser storage to be restored later
        addState: function (classname) {
            var data = angular.fromJson($rootScope.$storage[storageKeyName]);

            if (!data) {
                data = classname;
            } else {
                data = WordChecker.addWord(data, classname);
            }

            $rootScope.$storage[storageKeyName] = angular.toJson(data);
        },

        // Remove a state from the browser storage
        removeState: function (classname) {
            var data = $rootScope.$storage[storageKeyName];
            // nothing to remove
            if (!data) return;

            data = WordChecker.removeWord(data, classname);

            $rootScope.$storage[storageKeyName] = angular.toJson(data);
        },

        // Load the state string and restore the classlist
        restoreState: function ($elem) {
            var data = angular.fromJson($rootScope.$storage[storageKeyName]);

            // nothing to restore
            if (!data) return;
            $elem.addClass(data);
        }

    };

}]);
/**=========================================================
 * Module: utils.js
 * Utility library to use across the theme
 =========================================================*/

App.service('Utils', ["$window", "APP_MEDIAQUERY", function ($window, APP_MEDIAQUERY) {
    'use strict';

    var $html = angular.element("html"),
        $win = angular.element($window),
        $body = angular.element('body');

    return {
        // DETECTION
        support: {
            transition: (function () {
                var transitionEnd = (function () {

                    var element = document.body || document.documentElement,
                        transEndEventNames = {
                            WebkitTransition: 'webkitTransitionEnd',
                            MozTransition: 'transitionend',
                            OTransition: 'oTransitionEnd otransitionend',
                            transition: 'transitionend'
                        }, name;

                    for (name in transEndEventNames) {
                        if (element.style[name] !== undefined) return transEndEventNames[name];
                    }
                }());

                return transitionEnd && {end: transitionEnd};
            })(),
            animation: (function () {

                var animationEnd = (function () {

                    var element = document.body || document.documentElement,
                        animEndEventNames = {
                            WebkitAnimation: 'webkitAnimationEnd',
                            MozAnimation: 'animationend',
                            OAnimation: 'oAnimationEnd oanimationend',
                            animation: 'animationend'
                        }, name;

                    for (name in animEndEventNames) {
                        if (element.style[name] !== undefined) return animEndEventNames[name];
                    }
                }());

                return animationEnd && {end: animationEnd};
            })(),
            requestAnimationFrame: window.requestAnimationFrame ||
                window.webkitRequestAnimationFrame ||
                window.mozRequestAnimationFrame ||
                window.msRequestAnimationFrame ||
                window.oRequestAnimationFrame ||
                function (callback) {
                    window.setTimeout(callback, 1000 / 60);
                },
            touch: (
                ('ontouchstart' in window && navigator.userAgent.toLowerCase().match(/mobile|tablet/)) ||
                (window.DocumentTouch && document instanceof window.DocumentTouch) ||
                (window.navigator['msPointerEnabled'] && window.navigator['msMaxTouchPoints'] > 0) || //IE 10
                (window.navigator['pointerEnabled'] && window.navigator['maxTouchPoints'] > 0) || //IE >=11
                false
            ),
            mutationobserver: (window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver || null)
        },
        // UTILITIES
        isInView: function (element, options) {

            var $element = $(element);

            if (!$element.is(':visible')) {
                return false;
            }

            var window_left = $win.scrollLeft(),
                window_top = $win.scrollTop(),
                offset = $element.offset(),
                left = offset.left,
                top = offset.top;

            options = $.extend({topoffset: 0, leftoffset: 0}, options);

            if (top + $element.height() >= window_top && top - options.topoffset <= window_top + $win.height() &&
                left + $element.width() >= window_left && left - options.leftoffset <= window_left + $win.width()) {
                return true;
            } else {
                return false;
            }
        },
        langdirection: $html.attr("dir") == "rtl" ? "right" : "left",
        isTouch: function () {
            return $html.hasClass('touch');
        },
        isSidebarCollapsed: function () {
            return $body.hasClass('aside-collapsed');
        },
        isSidebarToggled: function () {
            return $body.hasClass('aside-toggled');
        },
        isMobile: function () {
            return $win.width() < APP_MEDIAQUERY.tablet;
        }
    };
}]);
/**=========================================================
 * Module: vector-map.js
 * Services to initialize vector map plugin
 =========================================================*/

App.service('vectorMap', function () {
    'use strict';
    return {
        init: function ($element, opts, series, markers) {
            $element.vectorMap({
                map: opts.mapName,
                backgroundColor: opts.bgColor,
                zoomMin: 1,
                zoomMax: 8,
                zoomOnScroll: false,
                regionStyle: {
                    initial: {
                        'fill': opts.regionFill,
                        'fill-opacity': 1,
                        'stroke': 'none',
                        'stroke-width': 1.5,
                        'stroke-opacity': 1
                    },
                    hover: {
                        'fill-opacity': 0.8
                    },
                    selected: {
                        fill: 'blue'
                    },
                    selectedHover: {}
                },
                focusOn: {x: 0.4, y: 0.6, scale: opts.scale},
                markerStyle: {
                    initial: {
                        fill: opts.markerColor,
                        stroke: opts.markerColor
                    }
                },
                onRegionLabelShow: function (e, el, code) {
                    if (series && series[code])
                        el.html(el.html() + ': ' + series[code] + ' visitors');
                },
                markers: markers,
                series: {
                    regions: [{
                        values: series,
                        scale: opts.scaleColors,
                        normalizeFunction: 'polynomial'
                    }]
                },
            });
        }
    };
});
// To run this code, edit file
// index.html or index.jade and change
// html data-ng-app attribute from
// angle to myAppName
// -----------------------------------

var myApp = angular.module('myAppName', ['angle']);

myApp.run(["$log", function ($log) {

    $log.log('I\'m a line from custom.js');

}]);

myApp.config(["RouteHelpersProvider", function (RouteHelpersProvider) {

    // Custom Route definition

}]);

myApp.controller('oneOfMyOwnController', ["$scope", function ($scope) {
    /* controller code */
}]);

myApp.directive('oneOfMyOwnDirectives', function () {
    /*directive code*/
});


myApp.config(["$stateProvider", function ($stateProvider /* ... */) {
    /* specific routes here (see file config.js) */
}]);