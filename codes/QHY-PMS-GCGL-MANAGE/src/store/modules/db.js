/**
 * 持久化存储               
 * */
import util from '@/libs/util';
import router from '@/router';
import { cloneDeep } from 'lodash';

/**
 * @description 检查路径是否存在 不存在的话初始化
 * @param {Object} dbName {String} 数据库名称
 * @param {Object} path {String} 路径
 * @param {Object} user {Boolean} 区分用户
 * @param {Object} validator {Function} 数据校验钩子 返回 true 表示验证通过
 * @param {Object} defaultValue {*} 初始化默认值
 * @returns {String} 可以直接使用的路径
 */
function pathInit ({
    dbName = 'database',
    path = '',
    user = true,
    validator = () => true,
    defaultValue = ''
}) {
    const uuid = util.cookies.get('uuid') || 'ghost-uuid';
    // const currentPath = `${dbName}.${user ? `user.${uuid}` : 'public'}${path ? `.${path}` : ''}`;
    // const value = util.db.get(currentPath).value();
    const currentPath = path;
    const value = util.db.get(path).value();
    if (!(value !== undefined && validator(value))) {
        util.db.set(currentPath, defaultValue).write();
    }
    return currentPath;
}

export { pathInit };

export default {
    namespaced: true,
    actions: {
        /**
         * @description 将数据存储到指定位置 | 路径不存在会自动初始化
         * @description 效果类似于取值 dbName.path = value
         * @param context context
         * @param {Object} dbName {String} 数据库名称
         * @param {Object} path {String} 存储路径
         * @param {Object} value {*} 需要存储的值
         * @param {Object} user {Boolean} 是否区分用户
         */
        set (context, {
            dbName = 'database',
            path = '',
            value = '',
            user = false
        }) {
            util.db.set(pathInit({
                dbName,
                path,
                user
            }), value).write()
        },
        /**
         * @description 获取数据
         * @description 效果类似于取值 dbName.path || defaultValue
         * @param context context
         * @param {Object} dbName {String} 数据库名称
         * @param {Object} path {String} 存储路径
         * @param {Object} defaultValue {*} 取值失败的默认值
         * @param {Object} user {Boolean} 是否区分用户
         */
        get (context, {
            dbName = 'database',
            path = '',
            defaultValue = '',
            user = false
        }) {
            return new Promise(resolve => {
                resolve(cloneDeep(util.db.get(pathInit({
                    dbName,
                    path,
                    user,
                    defaultValue
                })).value()))
            })
        },
        /**
         * @description 获取存储数据库对象
         * @param {Object} context context
         * @param {Object} user {Boolean} 是否区分用户
         */
        database (context, {
            user = false
        } = {}) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: '',
                    user,
                    defaultValue: {}
                })))
            })
        },
        /**
         * @description 清空存储数据库对象
         * @param {Object} context context
         * @param {Object} user {Boolean} 是否区分用户
         */
        databaseClear (context, {
            user = false
        } = {}) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: '',
                    user,
                    validator: () => false,
                    defaultValue: {}
                })))
            })
        },
        /**
         * @description 获取存储数据库对象 [ 区分页面 ]
         * @param {Object} context context
         * @param {Object} basis {String} 页面区分依据 [ name | path | fullPath ]
         * @param {Object} user {Boolean} 是否区分用户
         */
        databasePage (context, {
            basis = 'fullPath',
            user = false
        } = {}) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: `$page.${router.app.$route[basis]}`,
                    user,
                    defaultValue: {}
                })))
            })
        },
        /**
         * @description 清空存储数据库对象 [ 区分页面 ]
         * @param {Object} context context
         * @param {Object} basis {String} 页面区分依据 [ name | path | fullPath ]
         * @param {Object} user {Boolean} 是否区分用户
         */
        databasePageClear (context, {
            basis = 'fullPath',
            user = false
        } = {}) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: `$page.${router.app.$route[basis]}`,
                    user,
                    validator: () => false,
                    defaultValue: {}
                })))
            })
        },
        /**
         * @description 快速将页面当前的数据 ( $data ) 持久化
         * @param {Object} context context
         * @param {Object} instance {Object} vue 实例
         * @param {Object} basis {String} 页面区分依据 [ name | path | fullPath ]
         * @param {Object} user {Boolean} 是否区分用户
         */
        pageSet (context, {
            instance,
            basis = 'fullPath',
            user = false
        }) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: `$page.${router.app.$route[basis]}.$data`,
                    user,
                    validator: () => false,
                    defaultValue: cloneDeep(instance.$data)
                })))
            })
        },
        /**
         * @description 快速获取页面快速持久化的数据
         * @param {Object} context context
         * @param {Object} instance {Object} vue 实例
         * @param {Object} basis {String} 页面区分依据 [ name | path | fullPath ]
         * @param {Object} user {Boolean} 是否区分用户
         */
        pageGet (context, {
            instance,
            basis = 'fullPath',
            user = false
        }) {
            return new Promise(resolve => {
                resolve(cloneDeep(util.db.get(pathInit({
                    dbName: 'database',
                    path: `$page.${router.app.$route[basis]}.$data`,
                    user,
                    defaultValue: cloneDeep(instance.$data)
                })).value()))
            })
        },
        /**
         * @description 清空页面快照
         * @param {Object} context context
         * @param {Object} basis {String} 页面区分依据 [ name | path | fullPath ]
         * @param {Object} user {Boolean} 是否区分用户
         */
        pageClear (context, {
            basis = 'fullPath',
            user = false
        }) {
            return new Promise(resolve => {
                resolve(util.db.get(pathInit({
                    dbName: 'database',
                    path: `$page.${router.app.$route[basis]}.$data`,
                    user,
                    validator: () => false,
                    defaultValue: {}
                })))
            })
        }
    }
};
