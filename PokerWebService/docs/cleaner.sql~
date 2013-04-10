use ece493grp2;

DELETE FROM misc_data WHERE accountID In (select id from user_table where net_money_ALL = 0);
DELETE FROM game_actions WHERE accountID In (select id from user_table where net_money_ALL = 0);


CREATE TEMPORARY TABLE MyTemp
SELECT id FROM user_table
WHERE net_money_ALL = 0;

DELETE FROM user_table WHERE id In (SELECT id FROM MyTemp);

DROP TEMPORARY TABLE MyTemp;
