/*var newDataoption = {
    title : {
        text : '时间坐标折线图'
    },
   tooltip : {
        trigger: 'item',
        formatter : function (params) {
        	//alert(params);
        	alert(params.value[0]);
            var date = new Date(params.value[0]);
            data = date.getFullYear() + '-'
                   + (date.getMonth() + 1) + '-'
                   + date.getDate() + ' '
                   + date.getHours() + ':'
                   + date.getMinutes();
       
            return data + '<br/>'
                   + params.value[1] + ',' 
                   + params.value[2];
        }
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    dataZoom: {
        show: true,
        start : 10,
        showDetail : true,
        handleColor :'rgba(70,130,180,0.8)',
        handleSize : 8
    },
    legend : {
        data : ['series1']
    },
    grid: {
        y2: 80
    },
    xAxis : [
        {
            type : 'time',
            splitNumber:20,
            min:0
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name: 'series1',
            type: 'line',
            showAllSymbol: false,//是否显示提示框消息
            symbolSize: function (value){
                return Math.round(value[2]/10) + 2;
            },
            data: []
        }
    ]
};
*/

var newDataoption = {
    title : {
        text : '时间坐标折线图'
    },
   tooltip : {
        trigger: 'item'
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    dataZoom: {
        show: true,
        start : 10,
        showDetail : true,
        handleColor :'rgba(70,130,180,0.8)',
        handleSize : 8
    },
    legend : {
        data : ['series1']
    },
    grid: {
        y2: 80
    },
    xAxis : [
        {
            type : 'time',
            splitNumber:10
            //data:[]
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name: 'series1',
            type: 'line',
            showAllSymbol: false,//是否显示提示框消息
          /*  symbolSize: function (value){
                return Math.round(value[2]/10) + 2;
            },*/
            data: []
        }
    ]
};
                    