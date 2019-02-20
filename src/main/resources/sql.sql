SELECT
  DATE_FORMAT(mr.create_at, '%Y-%m-%d') days,
  MAX(mr.active_power)                  max_active_power,
  AVG(mr.active_power)                  avg_active_power,
  MIN(mr.active_power)                  min_active_power,
  count(mr.id)                          count
FROM meter_record mr
WHERE UNIX_TIMESTAMP(mr.create_at) >= UNIX_TIMESTAMP('2019-02-16 00:00:00')
      AND UNIX_TIMESTAMP(mr.create_at) <= UNIX_TIMESTAMP('2019-02-17 23:59:59')
GROUP BY days;


SELECT
  *,
  max(active_power)
FROM (SELECT *
      FROM meter_record mr1
      WHERE UNIX_TIMESTAMP(mr1.create_at) >= UNIX_TIMESTAMP('2019-02-17 00:00:00')
            AND UNIX_TIMESTAMP(mr1.create_at) <= UNIX_TIMESTAMP('2019-02-17 23:59:59')
      ORDER BY mr1.active_power DESC) tmr
GROUP BY meter;

SELECT
  g1.name,
  g1.publisher,
  g1.price,
  g2.price,
  g1.released_at
FROM games AS g1
  LEFT OUTER JOIN games AS g2
    ON g1.released_at = g2.released_at AND g1.price < g2.price
WHERE g2.price IS NULL;

SELECT
  m1.meter        AS                             meter,
  m1.active_power AS                             limitValue,
  DATE_FORMAT(m1.create_at, '%Y-%m-%d %H:%i:%S') createAt
FROM meter_record AS m1 INNER JOIN (
                                     SELECT
                                       meter,
                                       MAX(active_power) AS max_active_power
                                     FROM meter_record
                                     WHERE UNIX_TIMESTAMP(create_at) >= UNIX_TIMESTAMP('2019-02-16 00:00:00')
                                           AND UNIX_TIMESTAMP(create_at) < UNIX_TIMESTAMP('2019-02-17 00:00:00')
                                     GROUP BY meter
                                   ) AS m2 ON m1.meter = m2.meter AND m1.active_power = m2.max_active_power


SELECT
  m2.meter        AS                             meter,
  m2.active_power AS                             limitValue,
  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt
FROM
  (SELECT
     meter,
     MAX(active_power) AS max_active_power
   FROM meter_record
   WHERE UNIX_TIMESTAMP(create_at) >= UNIX_TIMESTAMP('2019-02-16 00:00:00')
         AND UNIX_TIMESTAMP(create_at) < UNIX_TIMESTAMP('2019-02-17 00:00:00')
   GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m2.active_power = m1.max_active_power AND m1.meter = m2.meter;

SELECT
  a.meter        AS                             meter,
  a.active_power AS                             limitValue,
  DATE_FORMAT(b.create_at, '%Y-%m-%d %H:%i:%S') createAt
FROM (
       SELECT
         max(active_power) AS active_power,
         meter
       FROM meter_record
       WHERE DATE_FORMAT(create_at, '%Y%m%d') = '20190216'
       GROUP BY meter) AS a
  LEFT JOIN meter_record b ON a.active_power = b.active_power
                              AND DATE_FORMAT(b.create_at, '%Y%m%d') = '20190216' AND b.meter = a.meter;

SELECT
  meter,
  AVG(active_power)                  AS limitValue,
  DATE_FORMAT(create_at, '%Y-%m-%d') AS createAt
FROM meter_record
WHERE DATE_FORMAT(create_at, '%Y-%m-%d') = '2019-02-16'
GROUP BY meter;

SELECT
  m1.meter,
  ((m1.ia * m1.va + m1.ib * m1.vb + m1.ic * m1.vc) * (-cos((pi() / 2) + DEGREES(
      acos(m1.active_power / (m1.ia * m1.va + m1.ib * m1.vb + m1.ic * m1.vc)))))) AS max_reactive_power
FROM meter_record AS m1 INNER JOIN (
                                     SELECT
                                       m2.meter,
                                       max((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES(
                                           acos(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb +
                                                                    m2.ic * m2.vc))))))) AS max_reactive_power
                                     FROM meter_record AS m2
                                     WHERE UNIX_TIMESTAMP(create_at) >= UNIX_TIMESTAMP('2019-02-16 00:00:00')
                                           AND UNIX_TIMESTAMP(create_at) < UNIX_TIMESTAMP('2019-02-17 00:00:00')
                                     GROUP BY meter) AS m2 ON m1.meter = m2.meter AND m2.max_reactive_power = (
  (m1.ia * m1.va + m1.ib * m1.vb + m1.ic * m1.vc) *
  (-cos((pi() / 2) + DEGREES(acos(m1.active_power / ((m1.ia * m1.va + m1.ib * m1.vb + m1.ic * m1.vc)))))));

SELECT
  m1.meter              AS                       meter,
  m1.max_reactive_power AS                       limitValue,
  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt
FROM (
       SELECT
         m2.meter,
         MAX((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES(
             ACOS(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb +
                                      m2.ic * m2.vc))))))) AS max_reactive_power
       FROM meter_record AS m2
       WHERE DATE_FORMAT(create_at, '%Y-%m') = '2019-02'
       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter AND m1.max_reactive_power =
                                                                                  (m2.ia * m2.va + m2.ib * m2.vb +
                                                                                   m2.ic * m2.vc) *
                                                                                  (-cos((pi() / 2) + DEGREES(
                                                                                      ACOS(m2.active_power / ((
                                                                                        m2.ia * m2.va + m2.ib * m2.vb +
                                                                                        m2.ic * m2.vc))))));

SELECT
  m2.meter                                          AS meter,
  AVG((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES(
      ACOS(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb +
                               m2.ic * m2.vc))))))) AS limitValue,
  DATE_FORMAT(m2.create_at, '%Y-%m-%d')             AS createAt
FROM meter_record AS m2
WHERE DATE_FORMAT(create_at, '%Y-%m-%d') = '2019-02-18'
GROUP BY meter;

SELECT
  m1.meter AS                                    meter,
  m1.power AS                                    limitValue,
  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt
FROM (
       SELECT
         m2.meter,
         MAX(m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) AS power
       FROM meter_record AS m2
       WHERE DATE_FORMAT(create_at, '%Y-%m-18') = '2019-02-18'
       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter
                                                          AND m1.power = (m2.ia * m2.va
                                                                          + m2.ib * m2.vb
                                                                          + m2.ic * m2.vc);

SELECT
  m2.meter                                           AS meter,
  AVG(m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) AS limitValue,
  DATE_FORMAT(m2.create_at, '%Y-%m-%d')              AS createAt
FROM meter_record AS m2
WHERE DATE_FORMAT(create_at, '%Y-%m-%d') = '2019-02-18'
GROUP BY meter;

SELECT
  mr1.active_power,
  mr1.create_at
FROM meter_record mr1
WHERE UNIX_TIMESTAMP(mr1.create_at) >= UNIX_TIMESTAMP('2019-02-17 00:00:00')
      AND UNIX_TIMESTAMP(mr1.create_at) < UNIX_TIMESTAMP('2019-02-18 00:00:00')
ORDER BY mr1.active_power DESC;

SELECT
  DATE_FORMAT(mr.create_at, '%Y-%m-%d') AS createAt,
  MAX(mr.active_power)                  AS maxHolder,
  AVG(mr.active_power)                  AS avgHolder,
  MIN(mr.active_power)                  AS minHolder
FROM meter_record mr
WHERE UNIX_TIMESTAMP(mr.create_at) >= UNIX_TIMESTAMP('2019-02-16 00:00:00')
      AND UNIX_TIMESTAMP(mr.create_at) <= UNIX_TIMESTAMP('2019-02-17 23:59:59')
GROUP BY DATE_FORMAT(mr.create_at, '%Y-%m-%d');


SELECT (4 * HOUR(mr.create_at) + FLOOR(MINUTE(mr.create_at) / 1)) minutes
FROM meter_record mr
WHERE UNIX_TIMESTAMP(mr.create_at) >= UNIX_TIMESTAMP('2019-02-15 02:35:00')
      AND UNIX_TIMESTAMP(mr.create_at) <= UNIX_TIMESTAMP('2019-02-15 02:36:00')
GROUP BY minutes;

SELECT mr.create_at
FROM meter_record mr
WHERE UNIX_TIMESTAMP(mr.create_at) >= UNIX_TIMESTAMP('2019-02-17 20:00:00')
      AND UNIX_TIMESTAMP(mr.create_at) <= UNIX_TIMESTAMP('2019-02-17 20:36:00');

SELECT
  AVG(mr.va)           AS ua,
  AVG(mr.vb)           AS ub,
  AVG(mr.vc)           AS uc,
  AVG(mr.ia)           AS ia,
  AVG(mr.ib)           AS ib,
  AVG(mr.ic)           AS ic,
  AVG(mr.active_power) AS active_power,
  date_add('2019-02-17 00:00:00', INTERVAL 15 * floor(timestampdiff(MINUTE, '2019-02-17 00:00:00', mr.create_at) / 15)
  MINUTE)              AS create_at
FROM meter_record mr
WHERE UNIX_TIMESTAMP(mr.create_at) >= UNIX_TIMESTAMP('2019-02-17 00:00:00')
      AND UNIX_TIMESTAMP(mr.create_at) < UNIX_TIMESTAMP('2019-02-18 00:00:00')
GROUP BY floor(timestampdiff(MINUTE, '2019-02-17 00:00:00', mr.create_at) / 15);

SELECT floor(TIMESTAMPDIFF(MINUTE, '2019-02-17 00:00:00', '2019-02-17 00:01:01'));
SELECT TIMESTAMPDIFF(MINUTE, '2019-02-17 00:00:00', '2019-02-17 00:01:30');

SELECT @@sql_mode;

SET SQL_MODE = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';

SET GLOBAL sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));