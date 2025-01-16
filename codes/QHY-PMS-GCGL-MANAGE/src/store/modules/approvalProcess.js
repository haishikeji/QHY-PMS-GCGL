export default {
    namespaced: true,
    state: {
        nodeMap: new Map(),
        formItemMap: new Map(),
        isEdit: null,
        loginUser: JSON.parse(localStorage.getItem('loginUser') || '{}'),
        selectedNode: {},
        selectFormItem: null,
        design: {},
    },
    mutations: {
        selectedNode(state, val) {
            state.selectedNode = val
        },
        loadForm(state, val) {
            state.design = val
        },
        setIsEdit(state, val) {
            state.isEdit = val
        }
    },
}