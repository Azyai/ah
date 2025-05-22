// api/activity.ts

export async function getActivities() {
    // 这里应该是实际的API调用
    return [
        {
            id: 1,
            name: '活动1',
            description: '这是活动1的描述这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1这是活动1',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        // 更多活动...
        {
            id: 2,
            name: '活动2',
            description: '这是活动2的活动1的描述这是活动1这是活动1这是活动1这活动1的描述这是活动1这是活动1这是活动1这活动1的描述这是活动1这是活动1这是活动1这活动1的描述这是活动1这是活动1这是活动1这活动1的描述这是活动1这是活动1这是活动1这活动1的描述这是活动1这是活动1这是活动1这描述',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        {
            id: 3,
            name: '活动3',
            description: '这是活动3活动3活动3活动3活动3活动3活动3活动3活动3活动3活动3描述',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        {
            id: 4,
            name: '活动4',
            description: '这是活动4活动4活动4活动4活动4活动4活动4活动4述',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        {
            id: 5,
            name: '活动5',
            description: '活动5活动5活动5活动5活动5活动5活动5活动5活动5活动5活动5活动5活动5描述',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        {
            id: 6,
            name: '活动6',
            description: '这是活动6活动6活动6活动6活动6活动6活动6活动6活动6活动6活动6活动6活动6述哇',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
        {
            id: 7,
            name: '活动2',
            description: '这是活动2的描述哇哇哇哇哇哇哇哇哇哇',
            prizes: [
                { id: 1, name: '奖品1', description: '这是奖品1的描述' },
                { id: 2, name: '奖品2', description: '这是奖品2的描述' }
            ],
            restrictions: [
                { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
                { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
            ]
        },
    ]
}

export async function getActivityDetail(activityId: number) {
    // 这里应该是实际的API调用
    return {
        id: activityId,
        name: `活动${activityId}`,
        description: `这是活动${activityId}的描述`,
        prizes: [
            { id: 1, name: '奖品1', description: '这是奖品1的描述' },
            { id: 2, name: '奖品2', description: '这是奖品2的描述' }
        ],
        restrictions: [
            { id: 1, limitType: 1, limitValue: 5, intervalSeconds: 60 },
            { id: 2, limitType: 2, limitValue: 3, intervalSeconds: 3600 }
        ]
    }
}
