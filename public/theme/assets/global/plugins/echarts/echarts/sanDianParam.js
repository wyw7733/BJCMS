/****************************************散点图参数开始**********************************************************/ 
var sanDianOption = {
	    tooltip : {
	        trigger: 'axis',
	        showDelay : 0,
	        axisPointer:{
	            show: false,
	            type : 'cross',
	            lineStyle: {
	                type : 'dashed',
	                width : 1
	            }
	        },
	      formatter : function (params) {   
	        var value = params.value[0];
	       	var timess = (value.getYear()+1900)+"-"+(value.getMonth()+1)+"-"+value.getDate()+" "
	             + value.getHours()+":"+value.getMinutes()+":"+value.getSeconds();    	
	        var str =  "时间："+ timess +"   " + "值:"+params.value[1];	 
	        return str; 
	      }
	    },
	    legend: {
	        data:['cos']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataZoom : {show: true},
	            dataView : {show: true, readOnly: false},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    xAxis : [
	        {
	            type : 'time',
	            scale:true,
	         axisTick:false,
	        axisLabel:{
	        formatter:function(value){          
	          var timess = (value.getYear()+1900)+"-"+(value.getMonth()+1)+"-"+value.getDate()+" "
	             + value.getHours()+":"+value.getMinutes()+":"+value.getSeconds();    
	          return timess;
	            }
	          }
	        }
	    ],  
	    yAxis : [
	        {
	            type : 'value',
	            scale:true
	        }
	    ],
	    series : [
	        {
	            name:'cos',
	            type:'scatter',
	            large: true,
	            data:[[new Date(2015, 9, 1, 0,40000),50],
	                  [new Date(2015, 9, 1, 0,50000),150],
	            	  [new Date(2015, 9, 1, 0,60000),20],
	            	  [new Date(2015, 9, 1, 0,70000),45],
	            	  [new Date(2015, 9, 1, 0,80000),80],
	            	  [new Date(2015, 9, 1, 0,90000),35],
	            	  [new Date(2015, 9, 1, 0,8000),5]
	            	  ] 
	        }
	    ]
	};                  
/**************************************web 链接状态分布 结束******************************************/                