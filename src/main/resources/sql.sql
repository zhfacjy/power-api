CREATE TABLE dict
(
  id        INT auto_increment
  PRIMARY KEY,
  label     VARCHAR(50) NULL comment '字典表的名',
  value     VARCHAR(50) NULL comment '字典表的值',
  parent_id INT         NOT NULL comment '父id',
  type      VARCHAR(30) NULL comment '类型',
  create_at TIMESTAMP   NULL comment '创建时间'
)
  comment '字典表';

INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('有功功率', 'active_power', 0, 'electric_category_single_day', '2019-02-15 03:21:57');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('A相', 'phase_a', 3, 'electric_category_single_day', '2019-02-15 03:26:21');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('B相', 'phase_b', 3, 'electric_category_single_day', '2019-02-15 03:26:45');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('C相', 'phase_c', 3, 'electric_category_single_day', '2019-02-15 03:27:13');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('总有功功率', 'total_active_power', 3, 'electric_category_single_day', '2019-02-15 03:29:22');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('电流', 'electric_current', 0, 'electric_category_single_day', '2019-02-15 03:38:30');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('相电压', 'phase_voltage', 0, 'electric_category_single_day', '2019-02-15 07:39:34');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('线电压', 'line_voltage', 0, 'electric_category_single_day', '2019-02-15 07:40:07');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('无功率功率', 'reactive_power', 0, 'electric_category_single_day', '2019-02-15 07:41:22');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('视在功率', 'apparent_power', 0, 'electric_category_single_day', '2019-02-15 07:41:56');
INSERT INTO dict (label, value, parent_id, type, create_at) VALUES ('功率因数', 'power_factor', 0, 'electric_category_single_day', '2019-02-16 16:00:16');

CREATE TABLE dict_line_counters_id__transformer_substation_id
(
  id                        INT auto_increment
  PRIMARY KEY,
  transformer_substation_id INT NULL comment '变电站id',
  dict_line_counters_id     INT NULL comment '进线柜id'
)
  comment '进线柜和变电站的关联';

CREATE TABLE dict_meter_value__dict_line_counters_id
(
  id                    INT auto_increment
  PRIMARY KEY,
  dict_meter_value      VARCHAR(2) NULL comment '电表号',
  dict_line_counters_id INT        NULL comment '进线柜id'
)
  comment '电表和进线柜的关联';

CREATE TABLE games
(
  game_id     BIGINT auto_increment
  PRIMARY KEY,
  name        VARCHAR(30)                         NULL,
  price       BIGINT                              NULL,
  released_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  publisher   VARCHAR(100)                        NULL,
  CONSTRAINT game_id
  UNIQUE (game_id)
);

CREATE TABLE meter_record
(
  id              INT auto_increment
  PRIMARY KEY,
  central_node    VARCHAR(2) NULL comment '中心节点',
  meter           VARCHAR(2) NULL comment '电表',
  command         VARCHAR(2) NULL comment '命令',
  early_warning   VARCHAR(2) NULL comment '预警',
  ua              FLOAT      NULL comment 'A相电压',
  ub              FLOAT      NULL comment 'B相电压',
  uc              FLOAT      NULL comment 'C相电压',
  ia              FLOAT      NULL comment 'A相电流',
  ib              FLOAT      NULL comment 'B相电流',
  ic              FLOAT      NULL comment 'C相电流',
  active_power    DOUBLE     NULL comment '功率',
  electric_energy DOUBLE     NULL comment '电能',
  temperature     INT        NULL comment '温度',
  current_limit   INT        NULL comment '电流上限',
  crc             VARCHAR(4) NULL comment 'CRC校验码',
  create_at       TIMESTAMP  NULL comment '记录时间',
  pfa             FLOAT      NULL comment 'A相功率因数',
  pfb             FLOAT      NULL comment 'B相功率因数',
  pfc             FLOAT      NULL comment 'C相功率因数',
  frequency       FLOAT      NULL comment '频率'
)
  comment '电表数据记录';

CREATE TABLE patrol_plan
(
  id                        INT auto_increment
  PRIMARY KEY,
  transformer_substation_id INT                                 NULL comment '变电站id',
  dict_patrol_group_id      INT                                 NULL comment '巡检组别id',
  dict_patrol_category_id   INT                                 NULL comment '巡检类别id',
  dict_patrol_nature_id     INT                                 NULL comment '巡检性质id',
  patrol_content            VARCHAR(100)                        NULL comment '巡检内容',
  dict_plan_status_id       INT                                 NULL comment '执行状态',
  review_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL comment '审核日期',
  review_by                 INT                                 NULL comment '审核人',
  finish_at                 TIMESTAMP                           NULL comment '完成日期',
  create_by                 INT                                 NULL comment '创建人',
  create_at                 TIMESTAMP                           NULL comment '起始日期'
)
  comment '巡检计划表';

CREATE TABLE transformer_substation
(
  id                         INT auto_increment
  PRIMARY KEY,
  label                      VARCHAR(2)   NULL comment '变电站名称',
  voltage_level              VARCHAR(10)  NULL comment '电压等级',
  installed_capacity         VARCHAR(10)  NULL comment '装机容量',
  declared_demand            VARCHAR(10)  NULL comment '申报需量',
  measurement_control_number VARCHAR(10)  NULL comment '测控装置',
  address                    VARCHAR(100) NULL comment '所在地址',
  create_at                  TIMESTAMP    NULL comment '创建时间'
)
  comment '变电站表';

CREATE TABLE user
(
  id        INT auto_increment
  PRIMARY KEY,
  username  VARCHAR(20)  NULL comment '用户名',
  password  VARCHAR(256) NULL comment '密码',
  mobile    VARCHAR(11)  NULL comment '手机号',
  create_at TIMESTAMP    NULL comment '创建时间'
)
  comment '用户表';

CREATE TABLE user_id__dict_patrol_group_id
(
  id                   INT auto_increment
  PRIMARY KEY,
  user_id              INT       NULL comment '用户id',
  dict_patrol_group_id INT       NULL comment '巡检组别id',
  create_at            TIMESTAMP NULL comment '创建时间'
)
  comment '用户和巡检组的关系';

CREATE TABLE user_id__patrol_plan_id
(
  id             INT auto_increment
  PRIMARY KEY,
  user_id        INT       NULL comment '用户id',
  patrol_plan_id INT       NULL comment '巡检计划id',
  create_at      TIMESTAMP NULL comment '创建时间'
)
  comment '计划的执行成员';

