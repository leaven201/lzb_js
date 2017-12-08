package enums;

public enum TrafficStatus {
	未分配				,
	工作已分配			,		
	工作和保护已分配		,
	工作和保护和重路由已分配,
	锁定					,		
	阻塞					,		//表示无可用资源
	规划业务阻塞			;		//表示路由计算失败
}
