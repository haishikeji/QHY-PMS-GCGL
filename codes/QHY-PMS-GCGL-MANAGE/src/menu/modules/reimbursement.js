const pre = '/reimbursement/'

export default {
    path: '/reimbursement',
    title: '报销管理',
    header: 'reimbursement',
    icon: 'icon-reimbursement',
    children: [
        {
            title: '报销管理',
            children: [
                {
                    title: '费用费用',
                    path: `${pre}expenses`,
                }
            ]
        },
        {
            title: '报表',
            children: [
                {
                    title: '物资报表',
                    path: `${pre}report`,
                }
            ]
        }
    ]
}