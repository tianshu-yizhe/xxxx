document.addEventListener('DOMContentLoaded', function() {
    // 费用图表
    const feeCtx = document.getElementById('feeChart').getContext('2d');
    const feeChart = new Chart(feeCtx, {
        type: 'bar',
        data: {
            labels: ['物业费', '水费', '电费', '停车费', '其他'],
            datasets: [{
                label: '费用金额 (¥)',
                data: [1200, 300, 450, 200, 150],
                backgroundColor: [
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(153, 102, 255, 0.7)',
                    'rgba(255, 159, 64, 0.7)'
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // 维修图表
    const repairCtx = document.getElementById('repairChart').getContext('2d');
    const repairChart = new Chart(repairCtx, {
        type: 'doughnut',
        data: {
            labels: ['待处理', '处理中', '已完成'],
            datasets: [{
                data: [3, 2, 5],
                backgroundColor: [
                    'rgba(108, 117, 125, 0.7)',
                    'rgba(13, 110, 253, 0.7)',
                    'rgba(25, 135, 84, 0.7)'
                ],
                borderColor: [
                    'rgba(108, 117, 125, 1)',
                    'rgba(13, 110, 253, 1)',
                    'rgba(25, 135, 84, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'right',
                }
            }
        }
    });

    // 动态更新数据
    function updateCharts() {
        // 这里可以添加AJAX请求获取最新数据
        // 然后调用chart.update()方法更新图表
    }

    // 每30秒更新一次数据
    setInterval(updateCharts, 30000);
});