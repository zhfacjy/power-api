# service层
- 用于实现业务逻辑，如计算功率、登录注册
- 类名与数据库表名对应，如`MeterRecordServiceImpl`里所有的操作主要和meter_record表的数据有关
- 需要操作数据时需要使用`repository`中的对象