import * as echarts from "echarts";

function defaultOption(name, dataX) {
    return {
        tooltip: {
            trigger: 'axis',
            position: function (pt) {
                return [pt[0], pt[1]];
            },
            confine: true,
            padding: 3,
            backgroundColor: '#FFFFFFE0',
            textStyle: {
                fontSize: 13
            }
        }, grid:{
            left: '10',
            right: '15',
            bottom: '0',
            top: '30',
            containLabel: true
        }, xAxis: {
            type: 'category',
            boundaryGap: false,
            data: dataX,
            animation: false,
            axisLabel: {
                formatter: function (value) {
                    value = new Date(value)
                    let time = value.toLocaleTimeString();
                    time = time.substring(0, time.length - 3)
                    const date = [value.getDate() + 1, value.getMonth() + 1].join('/')
                    return `${time}\n${date}`;
                }
            }
        }, yAxis: {
            type: 'value',
            name: name,
            boundaryGap: [0, '10%']
        }, dataZoom: [
            {
                type: 'inside',
                start: 95,
                end: 100,
                minValueSpan: 12
            }
        ]
    };
}

function singleSeries(option, name, dataY, colors) {
    option.series = [
        {
            name: name,
            type: 'line',
            sampling: 'lttb',
            showSymbol: false,
            itemStyle: {
                color: colors[0]
            },
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: colors[1]
                    }, {
                        offset: 1,
                        color: colors[2]
                    }
                ])
            },
            data: dataY
        }
    ]
}

function doubleSeries(option, name, dataY, colors) {
    option.series = [
        {
            name: name[0],
            type: 'line',
            sampling: 'lttb',
            showSymbol: false,
            itemStyle: {
                color: colors[0][0]
            },
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: colors[0][1]
                    }, {
                        offset: 1,
                        color: colors[0][2]
                    }
                ])
            },
            data: dataY[0]
        }, {
            name: name[1],
            type: 'line',
            sampling: 'lttb',
            showSymbol: false,
            itemStyle: {
                color: colors[1][0]
            },
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: colors[1][1]
                    }, {
                        offset: 1,
                        color: colors[1][2]
                    }
                ])
            },
            data: dataY[1]
        }
    ]
}

export { defaultOption, singleSeries, doubleSeries }
