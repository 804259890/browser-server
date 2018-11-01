CREATE VIEW block_statistic_view AS;
SELECT bl.`chain_id`, bl.number AS height, bl.`timestamp` AS `time`, COUNT(0) AS `transaction`
FROM `block` bl LEFT JOIN `transaction` tr ON tr.`chain_id`=bl.`chain_id` AND tr.`block_number`=bl.`number`
GROUP BY bl.`chain_id`, bl.`number`,bl.`timestamp` ORDER BY bl.`timestamp` DESC LIMIT 3600;