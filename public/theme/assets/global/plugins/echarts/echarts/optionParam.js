/*************************************************************************************************/

/**
 * 系统状态参数1
 */
var systemStateOption1 = {
	tooltip : {
		formatter : "{a} <br/>{b} : {c}%"
	},
/*	toolbox : {
		show : true,
		feature : {
			mark : {
				show : true
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},*/
	series : [ {
		name : '主机系统状态',
		type : 'gauge',
		splitNumber : 5, // 分割段数，默认为5
		axisLine : { // 坐标轴线
			lineStyle : { // 属性lineStyle控制线条样式
				color : [ [ 0.2, '#2ec7c9' ], [ 0.8, '#5ab1ef' ],
						[ 1, '#d87a80' ] ],
				width : 8,
				type : 'solid'
			}
		},
		axisTick : { // 坐标轴小标记
			splitNumber : 10, // 每份split细分多少段
			length : 12, // 属性length控制线长
			lineStyle : { // 属性lineStyle控制线条样式
				color : 'auto',
				width : 1,
				type : 'solid'

			}
		},
		axisLabel : { // 坐标轴文本标签，详见axis.axisLabel
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto'
			}
		},
		splitLine : { // 分隔线
			show : true, // 默认显示，属性show控制显示与否
			length : 30, // 属性length控制线长
			lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
				color : 'auto'
			}
		},
		pointer : {
			width : 5
		},
		title : {
			show : true,
			text : '负载率',
			x : "center",
			offsetCenter : [ 0, '-40%' ], // x, y，单位px
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				fontWeight : 'bolder'
			}
		},
		detail : {
			formatter : '{value}%',
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto',
				fontWeight : 'bolder'
			}
		},
		data : [ {
			value : 0
			//name : '负载率'
		} ]
	} ]
};

/**
 * 系统状态参数2
 */
var systemStateOption2 = {
	tooltip : {
		formatter : "{a} <br/>{b} : {c}%"
	},
/*	toolbox : {
		show : true,
		feature : {
			mark : {
				show : true
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},*/
	series : [ {
		name : '主机系统状态',
		type : 'gauge',
		splitNumber : 5, // 分割段数，默认为5
		axisLine : { // 坐标轴线
			lineStyle : { // 属性lineStyle控制线条样式
				color : [ [ 0.2, '#2ec7c9' ], [ 0.8, '#5ab1ef' ],
						[ 1, '#d87a80' ] ],
				width : 8,
				type : 'solid'
			}
		},
		axisTick : { // 坐标轴小标记
			splitNumber : 10, // 每份split细分多少段
			length : 12, // 属性length控制线长
			lineStyle : { // 属性lineStyle控制线条样式
				color : 'auto',
				width : 1,
				type : 'solid'

			}
		},
		axisLabel : { // 坐标轴文本标签，详见axis.axisLabel
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto'
			}
		},
		splitLine : { // 分隔线
			show : true, // 默认显示，属性show控制显示与否
			length : 30, // 属性length控制线长
			lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
				color : 'auto'
			}
		},
		pointer : {
			width : 5
		},
		title : {
			show : true,
			text : '负载率',
			x : "center",
			offsetCenter : [ 0, '-40%' ], // x, y，单位px
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				fontWeight : 'bolder'
			}
		},
		detail : {
			formatter : '{value}%',
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto',
				fontWeight : 'bolder'
			}
		},
		data : [ {
			value : 0
			//name : '负载率'
		} ]
	} ]
};

/**
 * 系统状态参数3
 */
var systemStateOption3 = {
	tooltip : {
		formatter : "{a} <br/>{b} : {c}%"
	},
/*	toolbox : {
		show : true,
		feature : {
			mark : {
				show : true
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},*/
	series : [ {
		name : '主机系统状态',
		type : 'gauge',
		splitNumber : 5, // 分割段数，默认为5
		axisLine : { // 坐标轴线
			lineStyle : { // 属性lineStyle控制线条样式
				color : [ [ 0.2, '#2ec7c9' ], [ 0.8, '#5ab1ef' ],
						[ 1, '#d87a80' ] ],
				width : 8,
				type : 'solid'
			}
		},
		axisTick : { // 坐标轴小标记
			splitNumber : 10, // 每份split细分多少段
			length : 12, // 属性length控制线长
			lineStyle : { // 属性lineStyle控制线条样式
				color : 'auto',
				width : 1,
				type : 'solid'

			}
		},
		axisLabel : { // 坐标轴文本标签，详见axis.axisLabel
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto'
			}
		},
		splitLine : { // 分隔线
			show : true, // 默认显示，属性show控制显示与否
			length : 30, // 属性length控制线长
			lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
				color : 'auto'
			}
		},
		pointer : {
			width : 5
		},
		title : {
			show : true,
			text : '负载率',
			x : "center",
			offsetCenter : [ 0, '-40%' ], // x, y，单位px
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				fontWeight : 'bolder'
			}
		},
		detail : {
			formatter : '{value}%',
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto',
				fontWeight : 'bolder'
			}
		},
		data : [ {
			value : 0
			//name : '负载率'
		} ]
	} ]
};

/**
 * 系统状态参数4
 */
var systemStateOption4 = {
	tooltip : {
		formatter : "{a} <br/>{b} : {c}%"
	},
/*	toolbox : {
		show : true,
		feature : {
			mark : {
				show : true
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},*/
	series : [ {
		name : '主机系统状态',
		type : 'gauge',
		splitNumber : 5, // 分割段数，默认为5
		axisLine : { // 坐标轴线
			lineStyle : { // 属性lineStyle控制线条样式
				color : [ [ 0.2, '#2ec7c9' ], [ 0.8, '#5ab1ef' ],
						[ 1, '#d87a80' ] ],
				width : 8,
				type : 'solid'
			}
		},
		axisTick : { // 坐标轴小标记
			splitNumber : 10, // 每份split细分多少段
			length : 12, // 属性length控制线长
			lineStyle : { // 属性lineStyle控制线条样式
				color : 'auto',
				width : 1,
				type : 'solid'

			}
		},
		axisLabel : { // 坐标轴文本标签，详见axis.axisLabel
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto'
			}
		},
		splitLine : { // 分隔线
			show : true, // 默认显示，属性show控制显示与否
			length : 30, // 属性length控制线长
			lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
				color : 'auto'
			}
		},
		pointer : {
			width : 5
		},
		title : {
			show : true,
			text : '负载率',
			x : "center",
			offsetCenter : [ 0, '-40%' ], // x, y，单位px
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				fontWeight : 'bolder'
			}
		},
		detail : {
			formatter : '{value}%',
			textStyle : { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
				color : 'auto',
				fontWeight : 'bolder'
			}
		},
		data : [ {
			value : 0
			//name : '负载率'
		} ]
	} ]
};
/****************************************************************************************************/

/**
 * 事件严重性分析
 */
var eventAyasyceOption = {
	// 图表标题
	title : {
		text : "近10天事件严重性分析", // 正标题
		//link : "http://www.stepday.com", // 正标题链接 点击可在新窗口中打开
		x : "center", // 标题水平方向位置
		//subtext : "From:http://www.stepday.com", // 副标题
		//sublink : "http://www.stepday.com", // 副标题链接
		// 正标题样式
		textStyle : {
			fontSize : 18
		},
		// 副标题样式
		subtextStyle : {
			fontSize : 12,
			color : "red"
		}
	},
	// 数据提示框配置
	tooltip : {
		trigger : 'axis' // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis' 其实就是是否共享提示框
	},
	// 图例配置
	legend : {
		data : [ '灾难性', '严重性', '一般严重性', '警告性' ], // 这里需要与series内的每一组数据的name值保持一致
		y : "bottom"
	},
	// 工具箱配置
/*	toolbox : {
		show : true, // 是否显示工具箱
		feature : {
			mark : false, // 辅助线标志，上图icon左数1/2/3，分别是启用，删除上一条，删除全部
			dataView : {
				readOnly : false
			}, // 数据视图，上图icon左数8，打开数据视图
			magicType : [ 'line', 'bar' ], // 图表类型切换，当前仅支持直角系下的折线图、柱状图转换，上图icon左数6/7，分别是切换折线图，切换柱形图
			restore : {
				show : true // 还原，复位原始图表，上图icon左数9，还原
			},
			saveAsImage : {
				show:true // 保存为图片，上图icon左数10，保存
			}
		}
	},*/
	calculable : true,
	// 轴配置
	xAxis : [
	{
		type : 'category',
		data : [],
		name : "日期"
	}
	],
	// Y轴配置
	yAxis : [
	{
		type : 'value',
		splitArea : {
			show : true
		},
		name : "数量"
	}
	],
	// 图表Series数据序列配置
	series : [
			{
				name : '灾难性',
				type : 'line',
				data : []
			},
			{
				name : '严重性',
				type : 'line',
				data : []
			},
			{
				name : '一般严重性',
				type : 'line',
				data : []
			},
			{
				name : '警告性',
				type : 'line',
				data : []
			}
	]
};

/************************************************指针环形图*************************************************************/
var labelTop = {
	    normal : {
	        label : {
	            show : true,
	            position : 'center',
	            formatter : '{b}',
	            textStyle: {
	                baseline : 'bottom'
	            }
	        },
	        labelLine : {
	            show : false
	        }
	    }
	};
	var labelFromatter = {
	    normal : {
	        label : {
	            formatter : function (params){
	                return 100 - params.value + '%';
	            },
	            textStyle: {
	                baseline : 'top'
	            }
	        }
	    },
	};
	var labelBottom = {
	    normal : {
	        color: '#ccc',
	        label : {
	            show : true,
	            position : 'center'
	        },
	        labelLine : {
	            show : false
	        }
	    },
	    emphasis: {
	        color: 'rgba(0,0,0,0)'
	    }
	};
	
	var radius = [40, 30];
	/**
	 *指针参数
	 */
	var probeseHuanOption = {
	    legend: {
	        x : 'left',
	        y : 'left',
	        data:[
				'内存','cpu','综合打分',
				'内存','cpu','综合打分'
	        ]
	    },
	    title : {
	    /*	show : false,
	        text: 'The App World',
	        subtext: 'from global web index',
	        x: 'center'*/
	    },
	    /*toolbox: {
	        show : true,
	        feature : {
	            dataView : {show: true, readOnly: false},
	            magicType : {
	                show: true, 
	                type: ['pie', 'funnel'],
	                option: {
	                    funnel: {
	                        width: '20%',
	                        height: '25%',
	                        itemStyle : {
	                            normal : {
	                                label : {
	                                    formatter : function (params){
	                                        return 'other\n' + params.value + '%\n';
	                                    },
	                                    textStyle: {
	                                        baseline : 'middle'
	                                    }
	                                }
	                            },
	                        } 
	                    }
	                }
	            },
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },*/
	    series : [
	        {
	            type : 'pie',
	            center : ['8%', '20%'],
	            radius : radius,
	            x: '0%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'内存', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['22%', '20%'],
	            radius : radius,
	            x:'15%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'cpu', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['36%', '20%'],
	            radius : radius,
	            x:'30%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'综合打分', value:0,itemStyle : labelTop}
	            ]
	        },
	        //第二条数据
	        {
	            type : 'pie',
	            center : ['60%', '20%'],
	            radius : radius,
	            x: '55%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'内存', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['74%', '20%'],
	            radius : radius,
	            x:'70%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'cpu', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['88%', '20%'],
	            radius : radius,
	            x:'85%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'综合打分', value:0,itemStyle : labelTop}
	            ]
	        },
	        //第三条数据
	        {
	            type : 'pie',
	            center : ['8%', '67%'],
	            radius : radius,
	            y: '60%',   // for funnel
	            x: '0%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'内存', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['22%', '67%'],
	            radius : radius,
	            y: '55%',// for funnel
	            x:'15%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'cpu', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['36%', '67%'],
	            radius : radius,
	            y: '55%',   // for funnel
	            x:'36%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'综合打分', value:0,itemStyle : labelTop}
	            ]
	        },
	        //第四条数据
	        {
	            type : 'pie',
	            center : ['60%', '67%'],
	            radius : radius,
	            y: '55%',   // for funnel
	            x:'55%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'内存', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['74%', '67%'],
	            radius : radius,
	            y: '55%',   // for funnel
	            x:'70%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'cpu', value:0,itemStyle : labelTop}
	            ]
	        },
	        {
	            type : 'pie',
	            center : ['88%', '67%'],
	            radius : radius,
	            y: '55%',   // for funnel
	            x:'85%', // for funnel
	            itemStyle : labelFromatter,
	            data : [
	                {name:'other', value:100, itemStyle : labelBottom},
	                {name:'综合打分', value:0,itemStyle : labelTop}
	            ]
	        }
	    ]
	};
	
	/**************************************web 链接状态分布 开始******************************************/
var webOption = {
		    title : {
		        text: 'web响应状态来源分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient : 'vertical',
		        x : 'left',
		        data:[]
		    },
		    calculable : true,
		    series : [
		        {
		            name:'响应状态分布',
		            type:'pie',
		            radius : '78%',
		            center: ['50%', '55%'],
		            data:[
		                {value:'', name:''},
		            ]
		        }
		    ]
		};
	/**************************************web 链接状态分布 结束******************************************/                