use eipl_amcs_db;
DROP PROCEDURE IF EXISTS `rpt_bonus_register` ;
DROP PROCEDURE IF EXISTS `rpt_dairy_sale_register`;
DROP PROCEDURE IF EXISTS `rpt_dispatch_note` ;
DROP PROCEDURE IF EXISTS `rpt_local_milk_sale` ;
DROP PROCEDURE IF EXISTS `rpt_member_bill_head_report` ;
DROP PROCEDURE IF EXISTS `rpt_member_billing_bank_detail` ;
DROP PROCEDURE IF EXISTS `rpt_member_billing_head_wise` ;
DROP PROCEDURE IF EXISTS `rpt_member_billing_month_year_cosolidate`;
DROP PROCEDURE IF EXISTS `rpt_member_billing_other_head_addition` ;
DROP PROCEDURE IF EXISTS `rpt_member_billing_other_head_deduction` ;
DROP PROCEDURE IF EXISTS `rpt_member_billing_product_sale_deduction` ;
DROP PROCEDURE IF EXISTS `rpt_member_collection_summary` ;
DROP PROCEDURE IF EXISTS `rpt_member_milk_collection_slip` ;
DROP PROCEDURE IF EXISTS `rpt_member_milk_collection_slip_head_wise` ;
DROP PROCEDURE IF EXISTS `rpt_member_reg` ;
DROP PROCEDURE IF EXISTS `rpt_member_wise_head_pivoting` ;
DROP PROCEDURE IF EXISTS `rpt_member_wise_local_milk_sale` ;
DROP PROCEDURE IF EXISTS `rpt_member_wise_product_sale_detail` ;
DROP PROCEDURE IF EXISTS `rpt_milk_dispatch_challan` ;
DROP PROCEDURE IF EXISTS `rpt_payment_register`;
DROP PROCEDURE IF EXISTS `rpt_payment_register_bank` ;
DROP PROCEDURE IF EXISTS `rpt_payment_register_bank_2`;
DROP PROCEDURE IF EXISTS `rpt_payment_register_bank_excel`;
DROP PROCEDURE IF EXISTS `rpt_product_sale_detail_member_wise` ;
DROP PROCEDURE IF EXISTS `rpt_product_sale_invoice`;
DROP PROCEDURE IF EXISTS `rpt_shift_code_wise` ;
DROP PROCEDURE IF EXISTS `rpt_society_purchase` ;
DROP PROCEDURE IF EXISTS `rpt_society_purchase_member_wise` ;
DROP PROCEDURE IF EXISTS `rpt_stock_summary` ;
DROP PROCEDURE IF EXISTS `process_member_billing` ;
DROP PROCEDURE IF EXISTS `rpt_committee_register` ;
DROP PROCEDURE IF EXISTS `rpt_dispatch_note_two` ;
DROP PROCEDURE IF EXISTS `rpt_election_reg` ;
DROP PROCEDURE IF EXISTS `rpt_meeting_details` ;
DROP PROCEDURE IF EXISTS `rpt_member_collection_audit` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_consolidate` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_milk_type_wise_consolidate` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_consolidate_summary` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_summary` ;
DROP PROCEDURE IF EXISTS `rpt_billing_prouct_sale_deduction_consolidated` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_month_wise` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_quarter_wise` ;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_year_wise` ;
DROP PROCEDURE IF EXISTS `rpt_patrak_one` ;
DROP PROCEDURE IF EXISTS `rpt_shift_name_wise` ;
DROP PROCEDURE IF EXISTS `rpt_staff_Salary` ;
DROP PROCEDURE IF EXISTS `rpt_staff_Salary_addition` ;
DROP PROCEDURE IF EXISTS `rpt_staff_Salary_deduction` ;
DROP PROCEDURE IF EXISTS `rpt_subreport_product_sale_invoice` ;
DROP PROCEDURE IF EXISTS `rpt_payment_register_milk_type_wise` ;
DROP PROCEDURE IF EXISTS `rpt_billing_prouct_sale_deduction` ;
DROP PROCEDURE IF EXISTS `rpt_bonus_register_for_bank_cash`;
DROP PROCEDURE IF EXISTS `rpt_bonus_for_all`;
drop procedure if exists `rpt_bonus_excel`;
DROP PROCEDURE IF EXISTS `rpt_bonus_register_consolidated`;
DROP PROCEDURE IF EXISTS `rpt_bonus_register_for_bank_cash_consolidated`;
DROP PROCEDURE IF EXISTS `rpt_milk_local_sale_dispatch_month_wise`;
DROP PROCEDURE IF EXISTS `rpt_milk_collecton_dispatch_receipt_loss`;
DROP PROCEDURE IF EXISTS `final_amount`;
drop procedure if exists rpt_bonus_excel;
drop procedure if exists new_procedure;
drop procedure if exists rpt_dispatch_note_two0;
drop procedure if exists rpt_dispatch_note_twoo;
drop procedure if exists rpt_dispatch_note_two_4;
drop procedure if exists rpt_payment_registerr;
drop procedure if exists rpt_product_sale_details;
drop procedure if exists sp_product_purchase;
drop procedure if exists sp_product_sale;
drop procedure if exists sp_rpt_product_transaction;
DROP PROCEDURE IF EXISTS `rpt_product_sale_detail_member_wise1` ;
DROP PROCEDURE IF EXISTS `sp_current_stock_for_product_with_product` ;
DROP PROCEDURE IF EXISTS `rpt_code_wise_product_sale_detail` ;
DROP PROCEDURE IF EXISTS `GetBonusSummary`;
DROP PROCEDURE IF EXISTS `sp_bonus_member_wise`;
DROP PROCEDURE IF EXISTS `GetMilkCollectionData`;
DROP PROCEDURE IF EXISTS `sp_rpt_account_ledger_purchase_sale`;
DROP PROCEDURE IF EXISTS `sp_rpt_milk_sale_dispatch_profit_loss`;
DROP PROCEDURE IF EXISTS `rpt_member_collection_summary1`;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_consolidate_with_deduction`;
DROP PROCEDURE IF EXISTS `rpt_milk_collection_milk_type_wise_consolidate_with_deduction`;


-- CDA
DROP PROCEDURE IF EXISTS `rpt_cda_date_wise_with_milk_type`;
DROP PROCEDURE IF EXISTS `rpt_cda_date_without_with_milk_type`;
DROP PROCEDURE IF EXISTS `rpt_cda_shift_wise_with_milk_type`;
DROP PROCEDURE IF EXISTS `rpt_cda_shift_without_with_milk_type`;
DROP PROCEDURE IF EXISTS `sp_rpt_milk_sale_dispatch_profit_loss_with_out_milk_type`;
-- Share
DROP PROCEDURE IF EXISTS `rpt_share_cancel`;
DROP PROCEDURE IF EXISTS `rpt_share_dividend`;
DROP PROCEDURE IF EXISTS `rpt_share_issue`;
DROP PROCEDURE IF EXISTS `rpt_share_transfer`;
DROP PROCEDURE IF EXISTS `rpt_share_members`;
-- ACCOUNT
DROP PROCEDURE  IF EXISTS `sp_accounting_opening_balance`;
DROP PROCEDURE  IF EXISTS `sp_accounting_sub_ledger_opening_balance`;
DROP PROCEDURE  IF EXISTS `sp_accounting_subledger_ledger_opening_balance`;
DROP PROCEDURE  IF EXISTS `sp_current_stock_for_product`;
DROP PROCEDURE  IF EXISTS `sp_product_receipt_by_desc`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_ledger_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_ledger_sub_ledger_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_ledger_summary`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_ledger_summary_two`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_ledger_with_sub_ledger_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_sub_ledger_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_account_sub_ledger_book_summary`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_balance_sheet`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_cash_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_cash_day_book`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_opening_balance`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_profit_loss`;
DROP PROCEDURE  IF EXISTS `sp_rpt_accounting_trading`;
DROP PROCEDURE  IF EXISTS `sp_rpt_milk_sale_dispatch_profit_loss`;


DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE rpt_bonus_register(IN p_society_code varchar(12),IN p_bonus_summary_code varchar(20),IN p_locale VARCHAR(20))
BEGIN

SELECT 
        s.code AS society_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE IFNULL(s.short_name,s.name) END  AS society_name,
        u.code AS union_code,
        CASE WHEN p_locale ='en' THEN u.name ELSE IFNULL(u.name_local,u.name) END  AS union_name,
        DATE_FORMAT(bs.from_date,'%d/%m/%Y') as from_date,
        DATE_FORMAT(bs.to_date,'%d/%m/%Y') as to_date,
        CASE WHEN  (bs.bonus_criteria = 0 ) THEN  "%" else case when  bs.bonus_criteria = 1 then  "Rs/Ltr" else CASE WHEN bs.bonus_criteria = 2 theN "Rs" END END END  AS bonus_criteria,
        bs.bonus_criteria_value,
        RIGHT(b.member_code,4) AS member_code,
         CASE WHEN p_locale ='en' THEN
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
            IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        b.milk_amount,
         round(b.bonus_amount,2)   as bonus_amount,
        b.milk_qty as  milk_qty,
        CAST(case when b.x_col1 IS NULL THEN 0 ELSE b.x_col1 END as decimal(18,2)) as kapat,
        CAST(bs.x_col2 as decimal(18,2)) as total_kapat_bonus
    FROM bonus AS b
        INNER JOIN bonus_summary AS bs ON b.bonus_summary_code = bs.code
        LEFT JOIN society AS s ON b.society_code=s.code
        LEFT JOIN unions AS u ON b.union_code=u.code
        LEFT JOIN members AS m ON b.member_code=m.code
        LEFT JOIn member_details as md on md.member_code=m.code
       WHERE  b.society_code = p_society_code

        AND b.bonus_summary_code = p_bonus_summary_code  order by m.code
         ;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `final_amount`(IN p_member_code varchar(30), IN p_society_payment_cycle_code varchar(15),IN p_from_date datetime, IN p_to_date datetime)
BEGIN

select    (IFNULL(SUM(milk_amount),0) - IFNULL(SUM(product_amount),0) - IFNULL(SUM(local_amount),0)) AS amount
FROM (
SELECT     IFNULL(SUM(mc.amount),0) AS milk_amount, 0 AS product_amount, 0 AS local_amount
FROM     milk_collection mc 
where    mc.member_code = p_member_code and mc.society_payment_cycle_code=p_society_payment_cycle_code

UNION ALL

SELECT 0 AS milk_amount,IFNULL(SUM(psi.installment_amount),0) AS product_amount,0 AS local_amount
FROM product_sale_installment psi
WHERE psi.member_code = p_member_code and psi.society_payment_cycle_code=p_society_payment_cycle_code

UNION ALL

select 0 AS milk_amount,0 AS product_amount,IFNULL(SUM(ls.amount),0) AS local_amount
from local_milk_sale ls
where ls.consumer_code = p_member_code and ls.sale_date >= p_from_date AND ls.sale_date <= p_to_date
and ls.consumer_type!=5
) a;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `new_procedure`()
BEGIN

select	(IFNULL(SUM(milk_amount),0) - IFNULL(SUM(product_amount),0) - IFNULL(SUM(local_amount),0)) AS amount
FROM (
SELECT 	IFNULL(SUM(mc.amount),0) AS milk_amount, 0 AS product_amount, 0 AS local_amount
FROM 	milk_collection mc 
where	mc.member_code = '10411230002' and mc.society_payment_cycle_code='104112326'

UNION ALL

SELECT 0 AS milk_amount,IFNULL(SUM(psi.installment_amount),0) AS product_amount,0 AS local_amount
FROM product_sale_installment psi
WHERE psi.member_code = '10411230002' and psi.society_payment_cycle_code='104112326'

UNION ALL

select 0 AS milk_amount,0 AS product_amount,IFNULL(SUM(ls.amount),0) AS local_amount
from local_milk_sale ls
where ls.consumer_code = '10411230002' and ls.sale_date >= '2022-04-11 06:00:00' AND ls.sale_date <= '2022-04-20 18:00:00'
) a;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `process_member_billing`(IN p_society_payment_cycle_code varchar(15), IN p_prev_society_payment_cycle_code varchar(15), IN p_from_date datetime, IN p_to_date datetime, IN p_processed INT, IN p_society_code varchar(12), IN p_user_code varchar(14))
BEGIN
	IF p_processed = 1 THEN

		
		insert into member_bill_transaction_audit(audit_created_at, operation_type, code, member_bill_code, bill_head_code,
			`type`, amount, adjustment, prev_due, ref_no,formula,fraction,union_code,society_code, created_by, created_at, updated_by, 
			updated_at, x_col1, x_col2, x_col3, audit_created_by) 
		select now(), "DELETE", code, member_bill_code, bill_head_code,`type`, amount, adjustment, prev_due, ref_no,formula,
			fraction,union_code,society_code, created_by, created_at,
			updated_by, updated_at, x_col1, x_col2, x_col3, p_user_code
        from member_bill_transaction where member_bill_code in (select member_bill_code from member_bill 
        where society_payment_cycle_code = p_society_payment_cycle_code);
        delete from member_bill_transaction where member_bill_code in (select code from member_bill where society_payment_cycle_code = p_society_payment_cycle_code);
		
        
		insert into member_bill_audit(audit_created_at, operation_type, code, union_code, society_code,
			member_code, milk_qty, avg_fat, avg_snf, avg_clr, kg_fat, kg_snf, milk_amount,
			x_col1, x_col2, x_col3,status,payment_mode,bank_acno,ifsc,payment_ref,is_disbursed,
			disbursed_date,society_payment_cycle_code,created_at,created_by,updated_at,updated_by,
			product_sale_amount,local_sale_amount,loan_amount,other_add_amount,other_ded_amount,
			net_amount,voucher_no, audit_created_by) 
		select now(), "DELETE", code,union_code, society_code, member_code, milk_qty, avg_fat,
			avg_snf, avg_clr, kg_fat, kg_snf, milk_amount,
			x_col1, x_col2, x_col3,status,payment_mode,bank_acno,ifsc,payment_ref,is_disbursed,
			disbursed_date,society_payment_cycle_code,created_at,created_by,updated_at,updated_by,
			product_sale_amount,local_sale_amount,loan_amount,other_add_amount,other_ded_amount,
			net_amount,voucher_no,p_user_code 
        from member_bill where society_payment_cycle_code = p_society_payment_cycle_code;

        delete from member_bill where society_payment_cycle_code = p_society_payment_cycle_code;
    
		
        insert into member_bill_summary_audit(audit_created_at,operation_type,code,milk_qty,milk_amount,
			product_sale_amount,local_sale_amount,loan_amount,other_ded_amount,other_add_amount,
			net_amount,disbursed_amount,status,society_payment_cycle_code,created_at,created_by,
			updated_at,updated_by,x_col1,x_col2,x_col3,audit_created_by)
        select now(),"DELETE",code,milk_qty,milk_amount,
			product_sale_amount,local_sale_amount,loan_amount,other_ded_amount,other_add_amount,
			net_amount,disbursed_amount,status,society_payment_cycle_code,created_at,created_by,
			updated_at,updated_by,x_col1,x_col2,x_col3,p_user_code
        from member_bill_summary where society_payment_cycle_code = p_society_payment_cycle_code;
        
        delete from member_bill_summary where society_payment_cycle_code = p_society_payment_cycle_code;
        
		
		select m.code as member_code, concat(m.first_name, ' ', ifnull(m.middle_name, ''), ' ',m.last_name) as member_name, md.account_no as account_no, md.ifsc as ifsc,
			sum(tmp.quantity) as quantity, sum(tmp.avg_fat) as avg_fat, sum(tmp.avg_snf) as avg_snf,
			sum(tmp.amount) as amount, sum(tmp.ls_amount) as ls_amount, sum(tmp.ps_amount) as ps_amount, sum(tmp.loan_amount) as loan_amount,
			0 as is_disburse
		FROM
		(select mc.member_code as member_code, round(sum(mc.qty), 2) as quantity, round(SUM(mc.fat * mc.qty / 100) / SUM(mc.qty) * 100, 2) AS avg_fat,
				round(SUM(mc.snf * mc.qty / 100) / SUM(mc.qty) * 100, 2) AS avg_snf,
				round(sum(mc.amount), 2) as amount, 0 as ls_amount, 0 as ps_amount, 0 as loan_amount
			from milk_collection mc WHERE mc.society_payment_cycle_code = p_society_payment_cycle_code AND mc.society_code = p_society_code group by member_code
			union
			select ls.consumer_code as member_code, 0 as quantity, 0 AS avg_fat, 0 AS avg_snf, 0 as amount, ROUND(IFNULL(SUM(ls.credit), 0), 2) AS ls_amount, 0 as ps_amount, 0 as loan_amount
			FROM local_milk_sale ls
			WHERE ls.consumer_type < 3 AND ls.sale_date BETWEEN p_from_date AND p_to_date group by consumer_code
			union
			select bill.member_code as member_code, 0 as quantity, 0 AS avg_fat, 0 AS avg_snf, 0 as amount, 
				ROUND(IFNULL(SUM(due), 0), 2) AS ls_amount, 0 as ps_amount, 0 as loan_amount
			FROM member_bill_transaction txn INNER JOIN member_bill bill ON bill.code = txn.member_bill_code
			WHERE bill_head_code = '103' AND bill.society_payment_cycle_code = p_prev_society_payment_cycle_code group by member_code having ROUND(IFNULL(SUM(prev_due), 0), 2) > 0
			union
			select ps.member_code as member_code, 0 as quantity, 0 AS avg_fat, 0 AS avg_snf, 0 as amount, 0 as ls_amount,
				ROUND(IFNULL(SUM(installment_amount + previous_pending_amount), 0), 2)  AS ps_amount, 0 as loan_amount
			FROM product_sale_installment ps
			WHERE society_payment_cycle_code = p_society_payment_cycle_code AND type = 1 group by member_code
			union
			select ps.member_code as member_code, 0 as quantity, 0 AS avg_fat, 0 AS avg_snf, 0 as amount, 0 as ls_amount, 
				0 AS ps_amount, ROUND(IFNULL(SUM(IFNULL(installment_amount,0) +IFNULL(previous_pending_amount,0)), 0), 2) AS loan_amount
				FROM product_sale_installment ps
			WHERE society_payment_cycle_code = p_society_payment_cycle_code AND type = 3 group by member_code ) tmp
		INNER JOIN members m on m.code = tmp.member_code
        inner join member_details md on md.member_code = m.code
		group by m.code, concat(m.first_name, ifnull(m.middle_name, ''), m.last_name), md.account_no, md.ifsc;
	ELSE
		select m.code as member_code, concat(m.first_name, ' ', ifnull(m.middle_name, ''), ' ',m.last_name) as member_name, fb.bank_acno, fb.ifsc, milk_qty as quantity,
		avg_fat, avg_snf, milk_amount as amount, local_sale_amount as ls_amount, product_sale_amount as ps_amount, loan_amount as loan_amount, 
        case is_disbursed when true then 1 else 0 end as is_disburse, fb.code, fb.net_amount as net_payable
		from member_bill fb
		inner join members m on fb.member_code = m.code
		inner join member_details md on md.member_code = m.code
		where fb.society_payment_cycle_code = p_society_payment_cycle_code;
    END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE rpt_billing_prouct_sale_deduction(IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN

SELECT 
    CASE WHEN p_locale ='en' THEN pp.name ELSE  IFNULL(pp.name_local,pp.name)  END AS product_short_name,
    ROUND((IFNULL(SUM(pst.amount), 0)),
            2) AS netamount,1 sr
FROM
    product_sale_transaction pst 
        INNER JOIN
        product_sale ps on ps.invoice_no=pst.invoice_no left join
    products AS pp ON pp.code = pst.product_code
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code  AND ps.invoice_no=psi.invoice_no
          AND psi.society_payment_cycle_code=p_society_payment_cycle_code
 WHERE psi.society_payment_cycle_code=p_society_payment_cycle_code
GROUP BY   pp.name ,pp.name_local

UNION ALL

SELECT 
   'Total'    AS product_short_name,
    ROUND((IFNULL(SUM(pst.amount), 0)),
            2) AS netamount,2 sr
FROM
    product_sale_transaction pst 
        INNER JOIN
        product_sale ps on ps.invoice_no=pst.invoice_no left join
    products AS pp ON pp.code = pst.product_code
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code  AND ps.invoice_no=psi.invoice_no
          AND psi.society_payment_cycle_code=p_society_payment_cycle_code
 WHERE psi.society_payment_cycle_code=p_society_payment_cycle_code
 order by sr asc

 ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_excel`(IN p_society_code VARCHAR(12),IN p_from_date DATE,IN p_to_date DATE,IN p_member_code varchar(30),IN p_locale VARCHAR(2),IN p_payment_mode INT ,IN p_bank_code VARCHAR(20),IN p_bonus_type INT)
BEGIN 
DECLARE sql_qty  VARCHAR(5000) DEFAULT NULL;
DECLARE sql_qty_2  VARCHAR(5000) DEFAULT NULL;
DECLARE sql_qty_3  VARCHAR(5000) DEFAULT NULL;
DECLARE sql_qty_4  VARCHAR(5000) DEFAULT NULL;
SET @sql_qty = sql_qty;
SET @sql_qty_2 = sql_qty_2;
SET @sql_qty_3 = sql_qty_3;
SET @sql_qty_4 = sql_qty_4;
DROP TEMPORARY TABLE IF EXISTS  tbl_bonus_summary_bonus; CREATE TEMPORARY TABLE tbl_bonus_summary_bonus (SELECT  A.code ,A.from_date,A.to_date,A.type,A.member_code,  CASE WHEN p_locale ='en' THEN concat(m.first_name,' ',m.middle_name,' ',m.last_name) ELSE concat(m.first_name_local,' ',m.middle_name_local,' ',m.last_name_local) END   as member_name,CAST(A.base_on_amount as  DECIMAL(18,2)) as base_on_amount,A.base_on_type,order_types FROM  (SELECT b.code,b.from_date,b.to_date,b.type,bb.member_code,bb.milk_qty as base_on_amount ,concat( date_format(from_date,'%d-%m-%y'),'-to-',date_format(to_date,'%d-%m-%y'),'-1-Qty') as base_on_type ,1 as order_types FROM bonus_summary as b INNER JOIN bonus as bb on b.code=bb.bonus_summary_code WHERE from_date >= p_from_date and to_date <=p_to_date AND b.type=p_bonus_type AND bb.member_code=CASE WHEN p_member_code=0 THEN bb.member_code ELSE p_member_code END  AND b.society_code=p_society_code UNION ALL SELECT b.code,b.from_date,b.to_date,b.type,bb.member_code, bb.milk_amount as base_on_amount,concat(date_format(from_date,'%d-%m-%y') ,'-to-',date_format(to_date,'%d-%m-%y'),'-2-Amount') as base_on_type,2 as order_types FROM bonus_summary as b INNER JOIN bonus as bb on b.code=bb.bonus_summary_code WHERE from_date >= p_from_date and to_date <=p_to_date AND bb.member_code=CASE WHEN p_member_code=0 THEN bb.member_code ELSE p_member_code END  AND b.type=p_bonus_type AND b.society_code=p_society_code UNION ALL SELECT b.code,b.from_date,b.to_date,b.type,bb.member_code, bb.bonus_amount as base_on_amount ,concat(date_format(from_date,'%d-%m-%y'),'-to-',date_format(to_date,'%d-%m-%y') ,'-3-BonusAmount') as base_on_type,3 as order_types FROM bonus_summary as b INNER JOIN bonus as bb on b.code=bb.bonus_summary_code WHERE from_date >= p_from_date and to_date <=p_to_date AND bb.member_code=CASE WHEN p_member_code=0 THEN bb.member_code ELSE p_member_code END AND b.society_code=p_society_code AND b.type=p_bonus_type) as A INNER JOIN members as m on m.code=A.member_code ORDER BY member_code,order_types);

 
 
SELECT 
    GROUP_CONCAT(DISTINCT CONCAT(' SUM(CASE WHEN base_on_type = ',
                '\'',
                base_on_type,
                '\'',
                ' THEN CAST(base_on_amount as DECIMAL(18,2)) ELSE 0 END) AS "',
                base_on_type,
                '"')
        ORDER BY from_Date , CAST(order_types AS UNSIGNED) ASC)
INTO @sql_qty FROM
    tbl_bonus_summary_bonus AS bs
WHERE
    bs.order_types = 1;
    
    
SELECT 
    GROUP_CONCAT(DISTINCT CONCAT(' SUM(CASE WHEN base_on_type = ',
                '\'',
                base_on_type,
                '\'',
                ' THEN CAST(base_on_amount as DECIMAL(18,2)) ELSE 0 END) AS "',
                base_on_type,
                '"')
        ORDER BY from_Date , CAST(order_types AS UNSIGNED) ASC)
INTO @sql_qty_2 FROM
    tbl_bonus_summary_bonus AS bs
WHERE
    bs.order_types = 2;
    
    
SELECT 
    GROUP_CONCAT(DISTINCT CONCAT(' SUM(CASE WHEN base_on_type = ',
                '\'',
                base_on_type,
                '\'',
                ' THEN CAST(base_on_amount as DECIMAL(18,2)) ELSE 0 END) AS "',
                base_on_type,
                '"')
        ORDER BY from_Date , CAST(order_types AS UNSIGNED) ASC)
INTO @sql_qty_3 FROM
    tbl_bonus_summary_bonus AS bs
WHERE
    bs.order_types = 3;
    
 SET @sql_qty_4 =  CONCAT('SELECT RIGHT(bb.member_code,4) as member_code,bs.member_name ,',@sql_qty,',',@sql_qty_2,',',@sql_qty_3,',md.account_no, 
        CAST(SUM(CASE WHEN order_types=3 THEN base_on_amount ELSE 0 END )as DECIMAL(18,2))  as bonus_amount,
 CAST(case when bb.x_col1 IS NULL THEN 0 ELSE bb.x_col1 END as decimal(18,2)) as kapat,
 CAST(SUM(CASE WHEN order_types=3 THEN base_on_amount ELSE 0 END )as DECIMAL(18,2)) - CAST(case when bb.x_col1 IS NULL THEN 0 ELSE 
 bb.x_col1 END as decimal(18,2)) as total_amount  FROM tbl_bonus_summary_bonus  as bs 
 INNER JOIN bonus as bb on bs.code=bb.bonus_summary_code AND bb.member_code=bs.member_code
 INNER JOIN member_details as md on md.member_code=bb.member_code  
WHERE md.payment_mode = CASE WHEN ',p_payment_mode,'= 0 THEN md.payment_mode ELSE ',p_payment_mode,' 
END
 AND md.bank_code = CASE WHEN ', p_bank_code,' = 0 THEN md.bank_code ELSE ',p_bank_code,' END
 GROUP BY bb.member_code,bs.member_name ,
 md.account_no,bb.x_col1 ORDER BY member_code,order_types ASC' );
 
PREPARE stmt FROM @sql_qty_4 ;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 
    
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_for_all`(IN p_society_code VARCHAR(12),IN p_from_date DATE,IN p_to_date DATE,IN p_member_code varchar(30),IN p_locale VARCHAR(2),IN p_payment_mode INT ,IN p_bank_code VARCHAR(20),IN p_bonus_type INT,IN p_milk_type_code varchar(1))
BEGIN 
 
  SELECT 
         CASE WHEN bb.type = 0 THEN 'UNION Bonuse' ELSE CASE WHEN bb.type = 1 THEN 'Society Bonus' END END as bonus_type, 
    RIGHT(bb.member_code,4) AS member_code,
    CASE
        WHEN
            p_locale = 'en'
        THEN
            CONCAT(m.first_name,
                    ' ',
                    m.middle_name,
                    ' ',
                    m.last_name)
        ELSE CONCAT(m.first_name_local,
                ' ',
                m.middle_name_local,
                ' ',
                m.last_name_local)
    END AS member_name,md.account_no,
            SUM(CAST(bb.bonus_amount AS DECIMAL (18 , 2 ))) AS bonus_amount ,SUM(CAST(bb.X_col1 as decimal(18,2))) as kapat,
    SUm(CAST(bb.bonus_amount AS DECIMAL (18 , 2 )))-SUM(CAST(bb.X_col1 as decimal(18,2))) AS total_amount    
            
    FROM
        bonus_summary AS b
    INNER JOIN bonus AS bb ON b.code = bb.bonus_summary_code
   
        INNER JOIN
    members AS m ON m.code = bb.member_code 
    join member_Details md on m.code=md.code
    where
           md.payment_mode= CASE WHEN p_payment_mode = 0 THEN md.payment_mode ELSE p_payment_mode END  
     and
       --  from_Date >=p_from_date AND to_date<=p_to_date
        (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date)) ) 

            AND bb.member_code = CASE
            WHEN p_member_code = 0 THEN bb.member_code
            ELSE p_member_code
        END
            AND b.society_code = p_society_code
            AND b.type = p_bonus_type and
            -- AND md.bank_code=p_bank_code
            case when p_bank_code=0 then ifnull(md.bank_code,0)=ifnull(md.bank_code,0) else md.bank_code=p_bank_code END
            and case when p_milk_type_code=0 then b.x_col1=b.x_col1 else
            b.x_col1=p_milk_type_code END
    GROUP BY  bb.type,bb.member_code,m.first_name,m.middle_name,m.last_name,
    m.first_name_local,m.middle_name_local,m.last_name_local,md.account_no,m.code_ex
    ;

END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_register_consolidated`(IN p_society_code varchar(12),IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_locale VARCHAR(2),IN p_member_code varchar(30),IN p_bonus_type INT,IN p_milk_type_code varchar(1))
BEGIN
-- call rpt_bonus_register_consolidated('1030256','2021-04-01','2023-03-31','gu',0,1,0);
    
    SELECT 
        CONCAT(date_format(p_from_date,'%d-%m-%y'),'-To-',date_format(p_to_date,'%d-%m-%y')) as p_from_date , 
        s.code AS society_code,
        s.name AS society_name,
        u.code AS union_code,
        u.name AS union_name,
        RIGHT(b.member_code,4) AS member_code,
        CASE WHEN p_locale ='en' THEN
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
            IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        CAST(SUM(b.milk_qty)as decimal(18,2)) as milk_qty,
        CAST(SUM(b.milk_amount)as decimal(18,2))as milk_amount,
        CAST(SUM(b.bonus_amount)as decimal(18,2)) as bonus_amount,
       CASE WHEN sum(b.x_col1) IS NULL THEN 0 ELSE IFNULL(CAST(sum(b.x_col1) as decimal(18,2)),0) END  as kapat,
       IFNULL( CAST(sum(b.x_col3) as decimal(18,2)),0) as total_kapat_bonus,
             ifnull(mt.name,'ALL') as milk_type_name


    FROM bonus AS b
        INNER JOIN bonus_summary AS bs ON b.bonus_summary_code = bs.code
        LEFT JOIN society AS s ON b.society_code=s.code
        LEFT JOIN unions AS u ON b.union_code=u.code
        LEFT JOIN members AS m ON b.member_code=m.code
        LEFT JOIn milk_types mt on mt.code =p_milk_type_code

    WHERE b.type=p_bonus_type and  b.society_code = p_society_code AND b.member_code =CASE WHEN p_member_code=0 THEN b.member_code ELSE p_member_code END
        AND (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date)) )
        AND 
        case when p_milk_type_code=0 then bs.x_col1=bs.x_col1 else bs.x_col1=p_milk_type_code end
        GROUP BY m.middle_name_local,m.last_name_local, m.first_name_local,s.code,s.name  ,u.code ,u.name   ,b.member_code ,m.first_name,m.middle_name,m.last_name
        
    ORDER BY b.member_code;
END ;;
DELIMITER ;



DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_register_for_bank_cash`(IN p_society_code varchar(12),IN p_bonus_summary_code varchar(20),IN p_payment_type INT,IN p_bank_code VARCHAR(200),IN p_locale VARCHAR(20))
BEGIN

SELECT 
		s.code AS society_code,
		CASE WHEN p_locale ='en' THEN s.name ELSE IFNULL(s.short_name,s.name) END  AS society_name,
		u.code AS union_code,
		CASE WHEN p_locale ='en' THEN u.name ELSE IFNULL(u.name_local,u.name) END  AS union_name,
		DATE_FORMAT(bs.from_date,'%d/%m/%Y') as from_date,
		DATE_FORMAT(bs.to_date,'%d/%m/%Y') as to_date,
		CASE WHEN  (bs.bonus_criteria = 0 ) THEN  "%" else case when  bs.bonus_criteria = 1 then  "Rs/Ltr" else CASE WHEN bs.bonus_criteria = 2 theN "Rs" END END END  AS bonus_criteria,
		bs.bonus_criteria_value,
		RIGHT(b.member_code,4) AS member_code,
		 CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		b.milk_amount,
		 round(b.bonus_amount,2)   as bonus_amount,
        md.account_no,
        b.milk_qty as  milk_qty,
        CASE WHEN md.payment_mode= 1 THEN 'Bank' ELSE 'Cash' END  payment_mode,
        IFNULL(CASE WHEN md.payment_mode= 1 THEN CASE WHEN  p_locale ='en' THEN banks.name ELSE IFNULL(banks.name,banks.name_local) END  ELSE 'Cash' END,'N/A') bank_name,
		CAST(case when b.x_col1 IS NULL THEN 0 ELSE b.x_col1 END as decimal(18,2)) as kapat,
        CAST(bs.x_col2 as decimal(18,2)) as total_kapat_bonus
	FROM bonus AS b
		INNER JOIN bonus_summary AS bs ON b.bonus_summary_code = bs.code
		LEFT JOIN society AS s ON b.society_code=s.code
		LEFT JOIN unions AS u ON b.union_code=u.code
		LEFT JOIN members AS m ON b.member_code=m.code
        LEFT JOIn member_details as md on md.member_code=m.code
        LEFT JOIn banks as banks on banks.code=md.bank_code
       WHERE md.payment_mode= CASE WHEN p_payment_type = -1 THEN md.payment_mode ELSE p_payment_type END  	and b.society_code = p_society_code
       
       AND IFNULL(md.bank_code,0)=CASE WHEN (p_bank_code= 1  OR p_bank_code= 0  OR p_bank_code IS NULL OR p_bank_code = '') THEN   IFNULL(md.bank_code,0) ELSE p_bank_code  END 
		AND b.bonus_summary_code = p_bonus_summary_code  order by m.code
		 ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_register_for_bank_cash_consolidated`(IN p_society_code VARCHAR(12),IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_payment_type INT,IN p_bank_code VARCHAR(200),IN p_locale VARCHAR(20),IN p_member_code varchar(30),IN p_milk_type_code varchar(1))
BEGIN

SELECT  CONCAT(date_format(p_from_date,'%d-%m-%y'),'-To-',date_format(p_to_date,'%d-%m-%y')) as p_from_date , 
        s.code AS society_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE IFNULL(s.short_name,s.name) END  AS society_name,
        u.code AS union_code,
        CASE WHEN p_locale ='en' THEN u.name ELSE IFNULL(u.name_local,u.name) END  AS union_name,
           
        RIGHT(b.member_code,4) AS member_code,
         CASE WHEN p_locale ='en' THEN
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
            IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        SUM(b.milk_amount)as milk_amount,
        round(SUM(b.bonus_amount),2)   as bonus_amount,
         IFNULL(md.account_no,'N/A') as account_no,
        SUM(b.milk_qty) as  milk_qty,
        CASE WHEN md.payment_mode= 1 THEN 'Bank' ELSE 'Cash' END  payment_mode,
        IFNULL(CASE WHEN md.payment_mode= 1 THEN CASE WHEN  p_locale ='en' THEN banks.name ELSE IFNULL(banks.name,banks.name_local) END  ELSE 'Cash' END,'N/A') bank_name,
        SUm(CAST(case when b.x_col1 IS NULL THEN 0 ELSE b.x_col1 END as decimal(18,2))) as kapat,
      round(SUM(b.bonus_amount),2) - SUM( CAST(bs.x_col2 as decimal(18,2))) as total_kapat_bonus,
      ifnull(mt.name,'ALL') as milk_type_name
    FROM bonus AS b
        INNER JOIN bonus_summary AS bs ON b.bonus_summary_code = bs.code
        LEFT JOIN society AS s ON b.society_code=s.code
        LEFT JOIN unions AS u ON b.union_code=u.code
        LEFT JOIN members AS m ON b.member_code=m.code
        LEFT JOIn member_details as md on md.member_code=m.code
        LEFT JOIn banks as banks on banks.code=md.bank_code
        LEFT JOIn milk_types mt on mt.code =p_milk_type_code
       WHERE (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date)) ) AND
   --    md.payment_mode= CASE WHEN p_payment_type = 0 THEN md.payment_mode ELSE p_payment_type END  
       ifnull(md.payment_mode,0) = p_payment_type
       
       and b.society_code = p_society_code AND b.member_code =CASE WHEN p_member_code=0 THEN b.member_code ELSE p_member_code END
    --   AND IFNULL(md.bank_code,0)=  case when p_bank_code=0 then ifnull(md.bank_code,0)=ifnull(md.bank_code,0) else md.bank_code=p_bank_code END   
   --    AND IFNULL(md.bank_code,0)=  case when p_bank_code=0 then ifnull(md.bank_code,0)=p_bank_code else md.bank_code=p_bank_code END   
      AND case when p_bank_code = 0 then IFNULL(md.bank_code,0)=IFNULL(md.bank_code,0) else IFNULL(md.bank_code,0)=p_bank_code end
  AND 
        case when p_milk_type_code=0 then bs.x_col1=bs.x_col1 else bs.x_col1=p_milk_type_code end
         GROUP BY b.member_code,md.account_no, banks.name,banks.name_local,md.payment_mode,s.code,s.name ,s.short_name,u.code 
,u.name ,u.name_local,m.first_name,m.middle_name,m.last_name,m.first_name_local,m.middle_name_local,m.last_name_local 
         order by m.code
         ;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_cda_date_wise_with_milk_type`(IN p_society_code varchar(15), IN p_from_date datetime, IN p_to_date datetime, IN p_ltr_kg INT, IN p_milk_type INT, IN p_locale varchar(5))
BEGIN
-- call cda.rpt_cda_date_wise_with_milk_type('1010490', '2022-05-26 18:00:00', '2022-05-26 18:00:00', 0, 0, 'en');
 SELECT 
    A.society_code AS society_code,
    A.animal_type_code AS animal_type_code,
    CASE
        WHEN p_locale = 'en' THEN mt.name
        ELSE IFNULL(mt.name, mt.name_local)
    END AS animal_type_name,
    A.collection_date AS collection_date,
    IFNULL(ROUND(IFNULL(SUM(A.milk_amount), 0) + IFNULL(SUM(A.bmc_amount), 0),
                    2),
            0) AS amount,
    IFNULL(ROUND(IFNULL(SUM(A.milk_qty), 0) + IFNULL(SUM(A.bmc_qty), 0),
                    2),
            0) AS quantity,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)),
                    4),
            0) AS kgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)),
                    4),
            0) AS kgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgCLR,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)),
                    4),
            0) AS kgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_amount), 0), 2),
            0) AS dis_amount,
    IFNULL(ROUND(SUM(IFNULL(A.dis_qty, 0)), 2), 0) AS dis_quantity,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0), 4), 0) AS dis_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0), 4), 0) AS dis_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0), 4), 0) AS dis_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_qty) - SUM(A.milk_qty + A.bmc_qty),
                            0),
                    2),
            0) AS dif_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_amount) - SUM(A.milk_amount + A.bmc_amount),
                            0),
                    2),
            0) AS dif_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_amount), 0), 2),
            0) AS rec_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_qty), 0), 2), 0) AS rec_quantity,
    IFNULL(IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0),
            0) AS rec_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0), 4), 0) AS rec_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0), 4), 0) AS rec_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0), 4), 0) AS rec_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(rec_qty) - SUM(dis_qty), 0),
                    2),
            0) AS dif_rec_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgCLR,
    IFNULL(ROUND(SUM(rec_amount) - IFNULL(SUM(dis_amount), 0),
                    2),
            0) AS dif_rec_amount
FROM
    (SELECT 
        mc.society_code AS society_code,
            mc.milk_type_code AS animal_type_code,
            CAST(mc.collection_date AS DATE) AS collection_date,
            ROUND(SUM(amount), 2) AS milk_amount,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_qty), 2)
            END AS milk_qty,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)), 4)
            END AS milk_kgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)), 4)
            END AS milk_kgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgCLR,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)), 4)
            END AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_collection mc
    INNER JOIN society dcs ON mc.society_code = mc.society_code
    INNER JOIN milk_types AS animal_type ON mc.milk_type_code = animal_type.code
    WHERE
        mc.collection_date BETWEEN p_from_date AND p_to_date
            AND mc.society_code = p_society_code
            AND mc.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mc.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY mc.society_code , mc.milk_type_code , CAST(mc.collection_date AS DATE) , qty_mode UNION ALL SELECT 
        md.society_code AS society_code,
            mdt.milk_type_code AS animal_type_code,
            CAST(to_date AS DATE) AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            ROUND(SUM(amount), 2) AS dis_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS dis_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS dis_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS dis_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_dispatch md
    INNER JOIN milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
            AND mdt.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mdt.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY md.society_code , mdt.milk_type_code , CAST(to_date AS DATE) , quantity_mode UNION ALL SELECT 
        md.society_code AS society_code,
            mdt.milk_type_code AS animal_type_code,
            CAST(to_date AS DATE) AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            ROUND(SUM(amount), 2) AS rec_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS rec_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS rec_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS rec_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS rec_kgCLR
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
            AND mdt.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mdt.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY md.society_code , mdt.milk_type_code , CAST(to_date AS DATE) , quantity_mode) A
        INNER JOIN
    milk_types AS mt ON mt.code = A.animal_type_code
GROUP BY A.collection_date , A.animal_type_code , A.society_code , mt.name , mt.name_local
ORDER BY A.collection_date , mt.name;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_cda_date_without_with_milk_type`(IN p_society_code varchar(15), IN p_from_date datetime, IN p_to_date datetime, IN p_ltr_kg INT, IN p_locale VARCHAR(5))
BEGIN
-- call cda.rpt_cda_date_without_with_milk_type('1011619', '2022-01-01 06:00:00', '2022-01-01 06:00:00' , 0, '1');

 SELECT 
    A.society_code AS society_code,
    A.collection_date AS collection_date,
    IFNULL(ROUND(IFNULL(SUM(A.milk_amount), 0) + IFNULL(SUM(A.bmc_amount), 0),
                    2),
            0) AS amount,
    IFNULL(ROUND(IFNULL(SUM(A.milk_qty), 0) + IFNULL(SUM(A.bmc_qty), 0),
                    2),
            0) AS quantity,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)),
                    4),
            0) AS kgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)),
                    4),
            0) AS kgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgCLR,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)),
                    4),
            0) AS kgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_amount), 0), 2),
            0) AS dis_amount,
    IFNULL(ROUND(SUM(IFNULL(A.dis_qty, 0)), 2), 0) AS dis_quantity,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0), 4), 0) AS dis_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0), 4), 0) AS dis_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0), 4), 0) AS dis_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_qty) - SUM(A.milk_qty + A.bmc_qty),
                            0),
                    2),
            0) AS dif_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_amount) - SUM(A.milk_amount + A.bmc_amount),
                            0),
                    2),
            0) AS dif_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_amount), 0), 2),
            0) AS rec_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_qty), 0), 2), 0) AS rec_quantity,
    IFNULL(IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0),
            0) AS rec_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0), 4), 0) AS rec_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0), 4), 0) AS rec_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0), 4), 0) AS rec_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(rec_qty) - SUM(dis_qty), 0),
                    2),
            0) AS dif_rec_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgCLR,
    IFNULL(ROUND(SUM(rec_amount) - IFNULL(SUM(dis_amount), 0),
                    2),
            0) AS dif_rec_amount
FROM
    (SELECT 
        mc.society_code AS society_code,
            CAST(mc.collection_date AS DATE) AS collection_date,
            ROUND(SUM(amount), 2) AS milk_amount,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_qty), 2)
            END AS milk_qty,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)), 4)
            END AS milk_kgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)), 4)
            END AS milk_kgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgCLR,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)), 4)
            END AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_collection mc
    INNER JOIN society dcs ON mc.society_code = mc.society_code
    WHERE
        mc.collection_date BETWEEN p_from_date AND p_to_date
            AND mc.society_code = p_society_code
    GROUP BY mc.society_code , mc.collection_date , qty_mode UNION ALL SELECT 
        md.society_code AS society_code,
            CAST(to_date AS DATE) AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            ROUND(SUM(amount), 2) AS dis_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS dis_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS dis_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS dis_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_dispatch md
    INNER JOIN milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
    GROUP BY  md.society_code,quantity_mode,CAST(to_date AS DATE) UNION ALL SELECT 
        md.society_code AS society_code,
            CAST(to_date AS DATE) AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            ROUND(SUM(amount), 2) AS rec_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS rec_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS rec_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS rec_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS rec_kgCLR
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
    GROUP BY  md.society_code,CAST(to_date AS DATE),quantity_mode) A
GROUP BY A.collection_date , A.society_code
ORDER BY A.collection_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_cda_shift_wise_with_milk_type`(IN p_society_code varchar(15), IN p_from_date datetime, IN p_to_date datetime, IN p_ltr_kg INT, IN p_milk_type INT, IN p_locale varchar(22)  )
BEGIN
-- call cda.rpt_cda_shift_wise_with_milk_type('1011619', '2022-01-01 06:00:00', '2022-01-01 06:00:00' , 0, 0, '1');

 SELECT 
    A.society_code AS society_code,
    A.animal_type_code AS animal_type_code,
    CASE
        WHEN p_locale = 'en' THEN mt.name
        ELSE IFNULL(mt.name, mt.name_local)
    END AS animal_type_name,
    CONCAT(DATE_FORMAT(A.collection_date, '%d-%m-%y'),
            '-',
            CASE
                WHEN SUBSTRING(A.collection_date, 11, 16) = ' 06:00:00' THEN 'M'
                ELSE 'E'
            END) AS collection_date,
    IFNULL(ROUND(IFNULL(SUM(A.milk_amount), 0) + IFNULL(SUM(A.bmc_amount), 0),
                    2),
            0) AS amount,
    IFNULL(ROUND(IFNULL(SUM(A.milk_qty), 0) + IFNULL(SUM(A.bmc_qty), 0),
                    2),
            0) AS quantity,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)),
                    4),
            0) AS kgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)),
                    4),
            0) AS kgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgCLR,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)),
                    4),
            0) AS kgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_amount), 0), 2),
            0) AS dis_amount,
    IFNULL(ROUND(SUM(IFNULL(A.dis_qty, 0)), 2), 0) AS dis_quantity,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0), 4), 0) AS dis_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0), 4), 0) AS dis_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0), 4), 0) AS dis_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_qty) - SUM(A.milk_qty + A.bmc_qty),
                            0),
                    2),
            0) AS dif_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_amount) - SUM(A.milk_amount + A.bmc_amount),
                            0),
                    2),
            0) AS dif_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_amount), 0), 2),
            0) AS rec_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_qty), 0), 2), 0) AS rec_quantity,
    IFNULL(IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0),
            0) AS rec_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0), 4), 0) AS rec_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0), 4), 0) AS rec_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0), 4), 0) AS rec_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(rec_qty) - SUM(dis_qty), 0),
                    2),
            0) AS dif_rec_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgCLR,
    IFNULL(ROUND(SUM(rec_amount) - IFNULL(SUM(dis_amount), 0),
                    2),
            0) AS dif_rec_amount
FROM
    (SELECT 
        mc.society_code AS society_code,
            mc.milk_type_code AS animal_type_code,
            mc.collection_date AS collection_date,
            ROUND(SUM(amount), 2) AS milk_amount,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_qty), 2)
            END AS milk_qty,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)), 4)
            END AS milk_kgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)), 4)
            END AS milk_kgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgCLR,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)), 4)
            END AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_collection mc
    INNER JOIN society dcs ON mc.society_code = mc.society_code
    INNER JOIN milk_types AS animal_type ON mc.milk_type_code = animal_type.code
    WHERE
        mc.collection_date BETWEEN p_from_date AND p_to_date
            AND mc.society_code = p_society_code
            AND mc.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mc.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY mc.society_code , mc.milk_type_code , mc.collection_date , qty_mode UNION ALL SELECT 
        md.society_code AS society_code,
            mdt.milk_type_code AS animal_type_code,
            to_date   AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            ROUND(SUM(amount), 2) AS dis_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS dis_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS dis_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS dis_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_dispatch md
    INNER JOIN milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
            AND mdt.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mdt.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY md.society_code , mdt.milk_type_code ,to_date, quantity_mode UNION ALL SELECT 
        md.society_code AS society_code,
            mdt.milk_type_code AS animal_type_code,
            to_date   AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            ROUND(SUM(amount), 2) AS rec_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS rec_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS rec_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS rec_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS rec_kgCLR
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
            AND mdt.milk_type_code = CASE
            WHEN p_milk_type = 0 THEN mdt.milk_type_code
            ELSE p_milk_type
        END
    GROUP BY md.society_code , quantity_mode ,to_date , mdt.milk_type_code) A
        INNER JOIN
    milk_types AS mt ON mt.code = A.animal_type_code
GROUP BY A.collection_date , A.animal_type_code , A.society_code , mt.name , mt.name_local
ORDER BY A.collection_date , mt.name;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_cda_shift_without_with_milk_type`(IN p_society_code VARCHAR(15), IN p_from_date DATETIME, IN p_to_date DATETIME, IN p_ltr_kg INT, IN p_locale VARCHAR(20))
BEGIN
-- call cda.rpt_cda_shift_without_with_milk_type('1011619', '2022-01-01 06:00:00', '2022-01-01 06:00:00' , 0, '1');
 
 SELECT 
    A.society_code AS society_code,
    CONCAT(DATE_FORMAT(A.collection_date, '%d-%m-%y'),
            '-',
            CASE
                WHEN SUBSTRING(A.collection_date, 11, 16) = ' 06:00:00' THEN 'M'
                ELSE 'E'
            END) AS collection_date,
    IFNULL(ROUND(IFNULL(SUM(A.milk_amount), 0) + IFNULL(SUM(A.bmc_amount), 0),
                    2),
            0) AS amount,
    IFNULL(ROUND(IFNULL(SUM(A.milk_qty), 0) + IFNULL(SUM(A.bmc_qty), 0),
                    2),
            0) AS quantity,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)),
                    4),
            0) AS kgSNF,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)),
                    4),
            0) AS kgFAT,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                    2),
            0) AS avgCLR,
    IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)),
                    4),
            0) AS kgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_amount), 0), 2),
            0) AS dis_amount,
    IFNULL(ROUND(SUM(IFNULL(A.dis_qty, 0)), 2), 0) AS dis_quantity,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0), 4), 0) AS dis_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0), 4), 0) AS dis_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                    2),
            0) AS dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0), 4), 0) AS dis_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_qty) - SUM(A.milk_qty + A.bmc_qty),
                            0),
                    2),
            0) AS dif_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgSNF, 0) + IFNULL(A.bmc_kgSNF, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgFAT, 0) + IFNULL(A.bmc_kgFAT, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(SUM(IFNULL(A.milk_kgCLR, 0) + IFNULL(A.bmc_kgCLR, 0)) / SUM(IFNULL(A.milk_qty, 0) + IFNULL(A.bmc_qty, 0)) * 100,
                            2),
                    0)),
            0) AS dif_dis_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(dis_amount) - SUM(A.milk_amount + A.bmc_amount),
                            0),
                    2),
            0) AS dif_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_amount), 0), 2),
            0) AS rec_amount,
    IFNULL(ROUND(IFNULL(SUM(A.rec_qty), 0), 2), 0) AS rec_quantity,
    IFNULL(IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0),
            0) AS rec_avgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0), 4), 0) AS rec_kgSNF,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0), 4), 0) AS rec_kgFAT,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                    2),
            0) AS rec_avgCLR,
    IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0), 4), 0) AS rec_kgCLR,
    IFNULL(ROUND(IFNULL(SUM(rec_qty) - SUM(dis_qty), 0),
                    2),
            0) AS dif_rec_quantity,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgSNF), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgSNF), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgSNF,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgFAT), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgFAT), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgFAT,
    IFNULL((IFNULL(ROUND(IFNULL(SUM(A.rec_kgCLR), 0) / IFNULL(SUM(A.rec_qty), 0) * 100,
                            2),
                    0) - IFNULL(ROUND(IFNULL(SUM(A.dis_kgCLR), 0) / IFNULL(SUM(A.dis_qty), 0) * 100,
                            2),
                    0)),
            0) AS dif_rec_avgCLR,
    IFNULL(ROUND(SUM(rec_amount) - IFNULL(SUM(dis_amount), 0),
                    2),
            0) AS dif_rec_amount
FROM
    (SELECT 
        mc.society_code AS society_code,
            mc.collection_date AS collection_date,
            ROUND(SUM(amount), 2) AS milk_amount,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_qty), 2)
            END AS milk_qty,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 4)), 4)
            END AS milk_kgSNF,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 4)), 4)
            END AS milk_kgFAT,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)) / SUM(converted_qty) * 100, 2)
            END AS milk_avgCLR,
            CASE
                WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 4)), 4)
            END AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_collection mc
    INNER JOIN society dcs ON mc.society_code = mc.society_code
    WHERE
        mc.collection_date BETWEEN p_from_date AND p_to_date
            AND mc.society_code = p_society_code
    GROUP BY mc.collection_date , mc.society_code , qty_mode UNION ALL SELECT 
        md.society_code AS society_code,
             to_date  AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            ROUND(SUM(amount), 2) AS dis_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS dis_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS dis_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS dis_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS dis_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS dis_kgCLR,
            0 AS rec_amount,
            0 AS rec_qty,
            0 AS rec_avgSNF,
            0 AS rec_kgSNF,
            0 AS rec_avgFAT,
            0 AS rec_kgFAT,
            0 AS rec_avgCLR,
            0 AS rec_kgCLR
    FROM
        milk_dispatch md
    INNER JOIN milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
    GROUP BY md.society_code , to_date , quantity_mode UNION ALL SELECT 
        md.society_code AS society_code,
            to_date AS collection_date,
            0 AS milk_amount,
            0 AS milk_qty,
            0 AS milk_avgSNF,
            0 AS milk_kgSNF,
            0 AS milk_avgFAT,
            0 AS milk_kgFAT,
            0 AS milk_avgCLR,
            0 AS milk_kgCLR,
            0 AS bmc_amount,
            0 AS bmc_qty,
            0 AS bmc_avgSNF,
            0 AS bmc_kgSNF,
            0 AS bmc_avgFAT,
            0 AS bmc_kgFAT,
            0 AS bmc_avgCLR,
            0 AS bmc_kgCLR,
            0 AS dis_amount,
            0 AS dis_qty,
            0 AS dis_avgSNF,
            0 AS dis_kgSNF,
            0 AS dis_avgFAT,
            0 AS dis_kgFAT,
            0 AS dis_avgCLR,
            0 AS dis_kgCLR,
            ROUND(SUM(amount), 2) AS rec_amount,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(qty), 2)
                ELSE ROUND(SUM(converted_quantity), 2)
            END AS rec_qty,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_snf / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_snf / 100, 4)), 4)
            END AS rec_kgSNF,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_fat / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_fat / 100, 4)), 4)
            END AS rec_kgFAT,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)) / SUM(qty) * 100, 2)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)) / SUM(converted_quantity) * 100, 2)
            END AS rec_avgCLR,
            CASE
                WHEN p_ltr_kg = quantity_mode THEN ROUND(SUM(ROUND(qty * avg_clr / 100, 4)), 4)
                ELSE ROUND(SUM(ROUND(converted_quantity * avg_clr / 100, 4)), 4)
            END AS rec_kgCLR
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    INNER JOIN society dcs ON dcs.code = md.society_code
    WHERE
        md.from_date >= p_from_date
            AND md.to_date <= p_to_date
            AND md.society_code = p_society_code
    GROUP BY md.society_code , quantity_mode , to_date) A
GROUP BY A.collection_date , A.society_code
ORDER BY A.collection_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_committee_register`(IN p_date DATE,IN p_society_code VARCHAR(12),IN p_locale VARCHAR(10))
BEGIN
-- call rpt_committee_register ('2022-04-16','1011006','gu');

 
    SELECT 
        CASE WHEN p_locale='en' THEN  committee_members.member_name ELSE  IFNULL(mm.first_name_local,committee_members.member_name)  END as member_name , 
        p_date as p_date,
        CONCAT(CASE WHEN p_locale='en' THEN society.name ELSE IFNULL(society.name_local,society.name) END ,'-(',society.code_ex,')') as society_code,
 
        max(election_date) as max_election_date,
		DATE_FORMAT(election_date,'%d/%m/%y') as election_date,
        designation.name as designation_name,
        DATE_FORMAT(tenure_from_date,'%d/%m/%y') as tenure_from_date, 
		DATE_FORMAT(tenure_to_date,'%d/%m/%y') as tenure_to_date
  FROM committee_members as committee_members 
        INNER JOIN designation as designation ON committee_members.designation_code =designation.code 
        INNER JOIN society as society ON committee_members.society_code=society.code
        INNER JOIN members as  mm ON mm.code=committee_members.member_code
    where  committee_members.society_code=p_society_code 
           AND committee_members.election_date = (SELECT MAX(election_date) FROM committee_members  WHERE  election_date <= p_date AND committee_members.society_code=p_society_code) 
           group by society.name_local,committee_members.member_code,society.code_ex,society.name, committee_members.member_name, designation.name, tenure_from_date, tenure_to_date,election_date 
           ORDER BY election_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dairy_sale_register`(IN p_society_code VARCHAR(12) , IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_milk_type INT,IN p_locale varchar(50))
BEGIN

SELECT A.society_code,
A.from_date,
A.to_date,
A.milk_type_code,
CASE WHEN p_locale ='en' THEN m.name ELSE  IFNULL(m.name_local,m.name)  END AS animal_name,
CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
concat(SUBSTRING(DATE_FORMAT(from_date,'%d/%m/%Y'),1,5),CASE when SUBSTRING(from_date,12)='06:00:00'  then ' - M ' else ' - E ' END ,' - ' , 
        SUBSTRING(DATE_FORMAT(to_date,'%d/%m/%Y'),1,5),CASE when SUBSTRING(to_date,12)='06:00:00'  then ' - M ' else ' - E ' END)   AS period,
IFNULL(round(sum(A.mc_qty),3),0) AS mc_qty, 
IFNULL(round(sum(A.mc_fat),2),0) AS mc_fat, 
IFNULL(round(sum(A.mc_snf),2),0) AS mc_snf, 
IFNULL(round(sum(A.mc_amount),2),0) AS mc_amount,
IFNULL(round(sum(A.md_qty),3),0) AS md_qty, 
IFNULL(round(sum(A.md_fat),2),0) AS md_fat, 
IFNULL(round(sum(A.md_snf),2),0) AS md_snf, 
IFNULL(round(sum(A.md_amount),2),0) AS md_amount,
IFNULL(round(sum(A.md_amount),2),0) - IFNULL(round(sum(A.mc_amount),2),0) AS difference
FROM
(
SELECT 
	md.society_code,
	md.from_date,
    md.to_date,
    mt.milk_type_code,
    round(sum(mc.qty),3) AS mc_qty,
    Round(round(sum(mc.qty * mc.fat / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_fat,
    Round(round(sum(mc.qty * mc.snf / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_snf,
    round(SUM(mc.amount),2) AS mc_amount,
    0 AS md_qty,
    0 AS md_fat,
    0 AS md_snf,
    0 AS md_amount
FROM milk_dispatch md
	INNER JOIN milk_dispatch_transaction mt ON md.challan_no = mt.challan_no
	INNER JOIN milk_collection mc ON mc.collection_date BETWEEN md.from_date AND md.to_date AND md.society_code = mc.society_code AND mt.milk_type_code = mc.milk_type_code
WHERE md.from_date >= p_from_date AND md.to_date <= p_to_date AND md.society_code = p_society_code 
	AND mt.milk_type_code = CASE WHEN p_milk_type = 0 THEN mt.milk_type_code ELSE p_milk_type END
GROUP BY md.society_code,md.from_date,md.to_date,mt.milk_type_code


union

SELECT 
	md.society_code,
	md.from_date,
    md.to_date,
    mt.milk_type_code,
    0 AS mc_qty,
    0 AS mc_fat,
    0 AS mc_snf,
    0 AS mc_amount,
    mt.qty AS md_qty,
    mt.avg_fat AS md_fat,
    mt.avg_snf AS md_snf,
    round(SUM(mt.amount),2) AS md_amount
FROM milk_dispatch md
	INNER JOIN milk_dispatch_transaction mt ON md.challan_no = mt.challan_no
WHERE md.from_date >= p_from_date AND md.to_date <= p_to_date AND md.society_code = p_society_code
	AND mt.milk_type_code = CASE WHEN p_milk_type = 0 THEN mt.milk_type_code ELSE p_milk_type END
GROUP BY md.society_code,md.from_date,md.to_date,mt.milk_type_code,mt.qty,mt.avg_fat,mt.avg_snf
) A
INNER JOIN society s ON s.code=A.society_code
INNER JOIN milk_types m ON m.code=A.milk_type_code
GROUP BY society_code,from_date,to_date,milk_type_code,m.name,s.name;

END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dispatch_note`(IN p_society_code VARCHAR(12) ,IN p_invoice_no VARCHAR(255),IN p_locale VARCHAR(255))
BEGIN

SELECT A.society_code,
		CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
		
		A.challan_no,
        DATE_FORMAT(A.created_at,'%d/%m/%Y') AS challan_date,
        concat(DATE_FORMAT(A.from_date,'%d/%m/%Y'),CASE when SUBSTRING(A.from_date,12)='06:00:00'  then ' - M ' else ' - E ' END) AS from_date,
        concat(DATE_FORMAT(A.to_date,'%d/%m/%Y'),CASE when SUBSTRING(A.to_date,12)='06:00:00'  then ' - M ' else ' - E ' END) AS to_date,
        A.route_no,
        A.route_name,
        A.vehicle_no,
        A.vehicle_in_time,
        A.vehicle_out_time,
		concat(DATE_FORMAT(A.collection_date,'%d/%m/%Y'),CASE when SUBSTRING(A.collection_date,12)='06:00:00'  then ' - M ' else ' - E ' END,' M.Count-(',SUM(member_code),')')   AS collection_date,
		ROUND(SUM(milk_collection_cow_qty),3) AS milk_collection_cow_qty,
		ROUND(SUM(milk_collection_buffalo_qty),3) AS milk_collection_buffalo_qty,
		ROUND(SUM(milk_collection_mix_qty),3) AS milk_collection_mix_qty,
      --  SUM(member_code) as member_code,
		ROUND(SUM(local_sale_cow_qty),3) AS local_sale_cow_qty,
		ROUND(SUM(local_sale_buffalo_qty),3)  AS local_sale_buffalo_qty,
		ROUND(SUM(local_sale_mix_qty),3) AS local_sale_mix_qty,
		ROUND(SUM(milk_dispatch_cow_qty),3) AS milk_dispatch_cow_qty,
		ROUND(SUM(milk_dispatch_buffalo_qty),3) AS milk_dispatch_buffalo_qty,
		ROUND(SUM(milk_dispatch_mix_qty),3) AS milk_dispatch_mix_qty,
		ROUND(SUM(milk_dispatch_cow_fat),3) AS milk_dispatch_cow_fat,
		ROUND(SUM(milk_dispatch_buffalo_fat),3) AS milk_dispatch_buffalo_fat,
		ROUND(SUM(milk_dispatch_mix_fat),3) AS milk_dispatch_mix_fat
	FROM (
			SELECT 
				md.challan_no,
                md.created_at,
                md.from_date,
                md.to_date,
                md.route_no,
                CASE WHEN p_locale ='en' THEN r.name ELSE  IFNULL(r.name_local,r.name)  END AS route_name,
                
                md.vehicle_no,
                md.vehicle_in_time,
                md.vehicle_out_time,
				mc.society_code,
				mc.collection_date AS collection_date,
				round(sum(CASE WHEN mty.name="Cow" THEN mc.qty ELSE 0 END),3) AS milk_collection_cow_qty,
				round(sum(CASE WHEN mty.name="Buffalo" THEN mc.qty ELSE 0 END),3) AS milk_collection_buffalo_qty,
				round(sum(CASE WHEN mty.name="Mix" THEN mc.qty ELSE 0 END),3) AS milk_collection_mix_qty,
                 COUNT(DISTINCT  mc.member_code) as member_code,
				0 AS local_sale_cow_qty,
				0 AS local_sale_buffalo_qty,
				0 AS local_sale_mix_qty,
				0 AS milk_dispatch_cow_qty,
				0 AS milk_dispatch_buffalo_qty,
				0 AS milk_dispatch_mix_qty,
                0 AS milk_dispatch_cow_fat,
				0 AS milk_dispatch_buffalo_fat,
				0 AS milk_dispatch_mix_fat
			FROM milk_dispatch md
			
				INNER JOIN milk_collection mc ON mc.collection_date BETWEEN md.from_date AND md.to_date AND md.society_code = mc.society_code
				INNER JOIN milk_types mty ON mc.milk_type_code = mty.code
                INNER JOIN routes r ON md.route_no = r.code
			WHERE mc.society_code = p_society_code AND md.challan_no = p_invoice_no
			GROUP BY md.challan_no,md.created_at,md.from_date,md.to_date,md.route_no,r.name,r.name_local,md.vehicle_no,md.vehicle_in_time,md.vehicle_out_time,mc.society_code,mc.collection_date,mty.name

			UNION ALL

			SELECT 
				md.challan_no,
                md.created_at,
                md.from_date,
                md.to_date,
                md.route_no,
                CASE WHEN p_locale ='en' THEN r.name ELSE  IFNULL(r.name_local,r.name)  END AS route_name,
                
                md.vehicle_no,
                md.vehicle_in_time,
                md.vehicle_out_time,
				ls.society_code,
				ls.sale_date AS collection_date,
				0 AS milk_collection_cow_qty,
				0 AS milk_collection_buffalo_qty,
				0 AS milk_collection_mix_qty,
                0 as member_code,
				round(sum(CASE WHEN mty.name="Cow" THEN ls.quantity ELSE 0 END),3) AS local_sale_cow_qty,
				round(sum(CASE WHEN mty.name="Buffalo" THEN ls.quantity ELSE 0 END),3) AS local_sale_buffalo_qty,
				round(sum(CASE WHEN mty.name="Mix" THEN ls.quantity ELSE 0 END),3) AS local_sale_mix_qty,
				0 AS milk_dispatch_cow_qty,
				0 AS milk_dispatch_buffalo_qty,
				0 AS milk_dispatch_mix_qty,
                0 AS milk_dispatch_cow_fat,
				0 AS milk_dispatch_buffalo_fat,
				0 AS milk_dispatch_mix_fat
			FROM milk_dispatch md
			
				INNER JOIN local_milk_sale ls ON ls.sale_date BETWEEN md.from_date AND md.to_date AND md.society_code = ls.society_code
				INNER JOIN milk_types mty ON ls.milk_type_code = mty.code
				INNER JOIN routes r ON md.route_no = r.code
			WHERE ls.society_code = p_society_code AND md.challan_no = p_invoice_no
			GROUP BY md.challan_no,md.created_at,md.from_date,md.to_date,md.route_no,r.name,r.name_local,md.vehicle_no,md.vehicle_in_time,md.vehicle_out_time,ls.society_code,ls.sale_date,mty.name
            
            UNION ALL

			SELECT  
				challan_no, 
                a.created_at, 
                from_date, 
                to_date, 
                route_no, 
                route_name AS route_name,
                vehicle_no, 
				vehicle_in_time, 
                vehicle_out_time, 
                society_code, 
                collection_date,
				SUM( milk_collection_cow_qty) AS milk_collection_cow_qty,
				SUM(milk_collection_buffalo_qty) AS milk_collection_buffalo_qty,
                SUM( milk_collection_mix_qty) AS milk_collection_mix_qty, 
                0 as member_code,
                SUM(local_sale_cow_qty) AS local_sale_cow_qty, 
				SUM(local_sale_buffalo_qty) AS local_sale_buffalo_qty, 
                SUM(local_sale_mix_qty) AS local_sale_mix_qty, 
                SUM(milk_dispatch_cow_qty) AS milk_dispatch_cow_qty, 
				SUM(milk_dispatch_buffalo_qty) AS milk_dispatch_buffalo_qty, 
                SUM(milk_dispatch_mix_qty) AS milk_dispatch_mix_qty, 
                SUM(milk_dispatch_cow_fat) AS milk_dispatch_cow_fat,
				SUM(milk_dispatch_buffalo_fat) AS milk_dispatch_buffalo_fat, 
                SUM(milk_dispatch_mix_fat) AS milk_dispatch_mix_fat
  FROM 
				(SELECT 
				md.challan_no as challan_no,
                md.created_at as created_at,
                md.from_date as from_date,
                md.to_date as to_date,
                md.route_no as route_no,
                 CASE WHEN p_locale ='en' THEN r.name ELSE  IFNULL(r.name_local,r.name)  END AS route_name,
                md.vehicle_no as vehicle_no,
                md.vehicle_in_time as vehicle_in_time,
                md.vehicle_out_time as vehicle_out_time,
				md.society_code as society_code,
				md.to_date AS collection_date,
				0 AS milk_collection_cow_qty,
				0 AS milk_collection_buffalo_qty,
				0 AS milk_collection_mix_qty,
				0 AS local_sale_cow_qty,
				0 AS local_sale_buffalo_qty,
				0 AS local_sale_mix_qty,
				CASE WHEN mt.milk_type_code=1 THEN mt.qty ELSE 0 END AS milk_dispatch_cow_qty,
				CASE WHEN mt.milk_type_code=2 THEN mt.qty ELSE 0 END AS milk_dispatch_buffalo_qty,
				CASE WHEN  mt.milk_type_code=3 THEN mt.qty ELSE 0 END AS milk_dispatch_mix_qty,
                CASE WHEN  mt.milk_type_code=1 THEN mt.avg_fat ELSE 0 END AS milk_dispatch_cow_fat,
				CASE WHEN mt.milk_type_code=2 THEN mt.avg_fat ELSE 0 END AS milk_dispatch_buffalo_fat,
				CASE WHEN  mt.milk_type_code=3  THEN mt.avg_fat ELSE 0 END AS milk_dispatch_mix_fat
			FROM milk_dispatch md
				INNER JOIN milk_dispatch_transaction mt ON md.challan_no = mt.challan_no
				INNER JOIN milk_types mty ON mt.milk_type_code = mty.code
                LEFT JOIN routes r ON md.route_no = r.code
			WHERE md.society_code = p_society_code AND md.challan_no = p_invoice_no )as a 
 
 GROUP BY challan_no, a.created_at, from_date, to_date, route_no, route_name  ,vehicle_no, vehicle_in_time, vehicle_out_time, society_code, collection_date

		) A
	INNER JOIN society s ON s.code=A.society_code
	GROUP BY A.society_code,s.name,s.name_local,A.challan_no,A.created_at,from_date,to_date,A.route_no,A.route_name,A.vehicle_no,A.vehicle_in_time,A.vehicle_out_time,A.collection_date
    ORDER BY A.collection_date,society_code;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dispatch_note_two`(IN p_society_code VARCHAR(12),IN p_challan_no VARCHAR(200),IN p_locale VARCHAR(2))
BEGIN


select from_date,to_date,concat((CASE WHEN p_locale ='en' THEN 'Trucksheet From date ' ELSE ' ટ્રકશીટ તારીખથી ' END ),date_format(from_date,'%d-%m-%y'),' ',
case when from_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ,' To ',
date_format(to_date,'%d-%m-%y'),' ',case when to_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ) INTO @from_date ,@to_date,@p_param From milk_dispatch where challan_no=p_challan_no and society_code=p_society_code;
select  
case when p_locale='en' then
concat(name,' - ',code_ex) 
else concat(IFNULL(name_local,name),' - ',code_ex) 
 end
INTO @p_s_name From society where  code=p_society_code;

  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Milk Purchase Cow Liter' else CAST('સોસાયટી દૂધ ખરીદ ગાય લિટર' AS CHAR CHARACTER SET utf8) end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Milk Purchase Cow Liter' ELSE CAST('સોસાયટી દૂધ ખરીદ ગાય લિટર' AS CHAR CHARACTER SET utf8)   END AS milk_type_name,

       
            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else CAST('સોસાયટી દૂધ ખરીદ ભેંસ લીટર' AS CHAR CHARACTER SET utf8) end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else CAST('સોસાયટી દૂધ ખરીદ ભેંસ લીટર' AS CHAR CHARACTER SET utf8)  end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
        
  ) AS A
GROUP BY milk_type_name 

UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
       CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર' end AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર' end AS milk_type_name,            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
        
  ) AS B
GROUP BY milk_type_name 

 

 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,
    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,    0 AS qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,     0 qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 and collection_date BETWEEN @from_date AND @to_date) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,
            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end
    AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end AS milk_type_name,
    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity),2),0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર' end AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2   AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર' end AS milk_type_name,    0 AS qty_m,
    IFNULL(ROUND(SUM(quantity), 2), 0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 AND sale_date BETWEEN @from_date AND @to_date
) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,
    IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Milk Send To Dairy Liter' else 'દૂધ ડેરી લીટરમાં મોકલો' end AS milk_type_name,
    ROUND(SUM(qty)/1.03, 2) AS qty_m,
    0 qty_e,
     ROUND(SUM(qty)/1.03, 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
    WHERE md.challan_no=p_challan_no
UNION ALL  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    CASE WHEN SUM(qty_m) = 0 THEN 0 ELSE CAST((SUM(kg_fat_m)/SUM(qty_m))*100 as decimal(18,2)) END AS qty_m,
    CASE WHEN SUM(qty_e) = 0 THEN 0 ELSE CAST((SUM(kg_fat_e)/SUM(qty_e))*100 as decimal(18,2)) END  AS qty_e,
    CASE WHEN (SUM(qty_m)+SUM(qty_e)) = 0 THEN 0 ELSE CAST((SUM(kg_fat_m)+ SUM(kg_fat_e))/(SUM(qty_m)+SUM(qty_e))*100  as decimal(18,2)) END  AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Avg Fat Cow' else 'સોસાયટી દૂધ ખરીદ ગાય ફેટ' end
        AS milk_type_name,
             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Avg Fat Cow' ELSE 'સોસાયટી દૂધ ખરીદ ગાય ફેટ'   END AS milk_type_name,

       
           0 AS kg_fat_m  ,
            0  as qty_m,
            IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
            IFNULL(SUM(qty),0)  as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Avg Fat Buffalo' else 'સોસાયટી દૂધ ખરીદ ભેંસ ફેટ' end
        AS milk_type_name,
             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Avg Fat Buffalo' else 'સોસાયટી દૂધ ખરીદ ભેંસ ફેટ' end
        AS milk_type_name,  0 AS kg_fat_m  ,
            0  as qty_m,
          IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
           IFNULL(SUM(qty),0) as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date ) as A GROUP BY milk_type_name
        
  
UNION ALL

 SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    CASE WHEN SUM(qty_m) = 0 THEN 0 ELSE ROUND(SUM(kg_fat_m)/SUM(qty_m)*100,2) END AS qty_m,
    CASE WHEN SUM(qty_e) = 0 THEN 0 ELSE ROUND(SUM(kg_fat_e)/SUM(qty_e)*100,2) END  AS qty_e,
    CASE WHEN (SUM(qty_m)+SUM(qty_e)) = 0 THEN 0 ELSE ROUND((SUM(kg_fat_m)+ SUM(kg_fat_e))/(SUM(qty_m)+SUM(qty_e))*100,2) END  AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' THEN 'Society Avg Fat' ELSE 'સોસાયટી દૂધ ખરીદ  ફેટ'  END AS milk_type_name,

             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
           CASE WHEN p_locale='en' THEN 'Society Avg Fat' ELSE 'સોસાયટી દૂધ ખરીદ  ફેટ'  END AS milk_type_name,

       
           0 AS kg_fat_m  ,
            0  as qty_m,
            IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
            IFNULL(SUM(qty),0)  as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2     AND collection_date BETWEEN @from_date AND @to_date
     ) as A GROUP BY milk_type_name
        
   

UNION ALL

SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then 
    'Total Milk Send To Dairy Kilo' else 'કુલ દૂધ ડેરીમાં મોકલો કિલો' end AS milk_type_name,
    Round(SUM(qty),2) AS qty_m,
    0 qty_e,
    Round(SUM(qty),2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
     WHERE md.challan_no=p_challan_no;
     
    
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dispatch_note_two0`(IN p_society_code VARCHAR(12),IN p_challan_no VARCHAR(200),IN p_locale VARCHAR(2))
BEGIN

select from_date,to_date,concat((CASE WHEN p_locale ='en' THEN 'Trucksheet From date ' ELSE ' ટ્રકશીટ તારીખથી ' END ),date_format(from_date,'%d-%m-%y'),' ',
case when from_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ,' To ',
date_format(to_date,'%d-%m-%y'),' ',case when to_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ) INTO @from_date ,@to_date,@p_param From milk_dispatch where challan_no=p_challan_no and society_code=p_society_code;
select  
case when p_locale='en' then
concat(name,' - ',code_ex) 
else concat(name_local,' - ',code_ex) 
 end
INTO @p_s_name From society where  code=p_society_code;

  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Milk Purchase Cow Liter' else 'સોસાયટી દૂધ ખરીદ ગાય લિટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Milk Purchase Cow Liter' ELSE 'સોસાયટી દૂધ ખરીદ ગાય લિટર'   END AS milk_type_name,

       
            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
        
  ) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
       CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
        
  ) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,
    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,    0 AS qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,     0 qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 and collection_date BETWEEN @from_date AND @to_date) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,
            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end
    AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end AS milk_type_name,
    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity),2),0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર
' end AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2 
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર
' end AS milk_type_name,    0 AS qty_m,
    IFNULL(ROUND(SUM(quantity), 2), 0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 AND sale_date BETWEEN @from_date AND @to_date
) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,
    IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 2) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Milk Send To Dairy Liter' else 'દૂધ ડેરી લીટરમાં મોકલો
' end AS milk_type_name,
    ROUND(SUM(qty), 2) AS qty_m,
    0 qty_e,
     ROUND(SUM(qty), 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
    WHERE md.challan_no=p_challan_no
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en'
then
    'Society Avg Fat Cow' else 'સોસાયટી સરેરાશ ફેટ ગાય
' end AS milk_type_name,
    IFNULL(ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
                    2),
            0) AS qty_m,
    0 qty_e,
    IFNULL(ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
                    2),
            0) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
WHERE
    md.challan_no = p_challan_no
        AND mdt.milk_type_code = 1 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Society Avg Fat Buffalo' else 'સોસાયટી સરેરાશ ફેટ ભેંસ
' end AS milk_type_name,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS qty_m,
    0 qty_e,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
WHERE
    md.challan_no = p_challan_no
        AND mdt.milk_type_code = 2

UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Society Avg Fat' else 'સોસાયટી સરેરાશ ફેટ
' end AS milk_type_name,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS qty_m,
    0 qty_e,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
     WHERE md.challan_no=p_challan_no
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then 
    'Total Milk Send To Dairy Kilo' else 'કુલ દૂધ ડેરીમાં મોકલો કિલો
' end AS milk_type_name,
    ROUND(SUM(qty) * 1.03, 2) AS qty_m,
    0 qty_e,
    ROUND(SUM(qty) * 1.03, 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
     WHERE md.challan_no=p_challan_no;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dispatch_note_twoo`(IN p_society_code VARCHAR(12),IN p_challan_no VARCHAR(200),IN p_locale VARCHAR(2))
BEGIN

select from_date,to_date,concat((CASE WHEN p_locale ='en' THEN 'Trucksheet From date ' ELSE ' ટ્રકશીટ તારીખથી ' END ),date_format(from_date,'%d-%m-%y'),' ',
case when from_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ,' To ',
date_format(to_date,'%d-%m-%y'),' ',case when to_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ) INTO @from_date ,@to_date,@p_param From milk_dispatch where challan_no=p_challan_no and society_code=p_society_code;
select  
case when p_locale='en' then
concat(name,' - ',code_ex) 
else concat(name_local,' - ',code_ex) 
 end
INTO @p_s_name From society where  code=p_society_code;

  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Milk Purchase Cow Liter' else 'સોસાયટી દૂધ ખરીદ ગાય લિટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Milk Purchase Cow Liter' ELSE 'સોસાયટી દૂધ ખરીદ ગાય લિટર'   END AS milk_type_name,

       
            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
        
  ) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
       CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
        
  ) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,
    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,    0 AS qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,     0 qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 and collection_date BETWEEN @from_date AND @to_date) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,
            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end
    AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end AS milk_type_name,
    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity),2),0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર
' end AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2 
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર
' end AS milk_type_name,    0 AS qty_m,
    IFNULL(ROUND(SUM(quantity), 2), 0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 AND sale_date BETWEEN @from_date AND @to_date
) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,
    IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર ' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 2) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Milk Send To Dairy Liter' else 'દૂધ ડેરી લીટરમાં મોકલો
' end AS milk_type_name,
    ROUND(SUM(qty), 2) AS qty_m,
    0 qty_e,
     ROUND(SUM(qty), 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
    WHERE md.challan_no=p_challan_no
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en'
then
    'Society Avg Fat Cow' else 'સોસાયટી સરેરાશ ફેટ ગાય
' end AS milk_type_name,
    IFNULL(ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
                    2),
            0) AS qty_m,
    0 qty_e,
    IFNULL(ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
                    2),
            0) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
WHERE
    md.challan_no = p_challan_no
        AND mdt.milk_type_code = 1 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Society Avg Fat Buffalo' else 'સોસાયટી સરેરાશ ફેટ ભેંસ
' end AS milk_type_name,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS qty_m,
    0 qty_e,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
WHERE
    md.challan_no = p_challan_no
        AND mdt.milk_type_code = 2

UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Society Avg Fat' else 'સોસાયટી સરેરાશ ફેટ
' end AS milk_type_name,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS qty_m,
    0 qty_e,
    ROUND(SUM(avg_fat * qty / 100) / SUM(qty) * 100,
            2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
     WHERE md.challan_no=p_challan_no
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then 
    'Total Milk Send To Dairy Kilo' else 'કુલ દૂધ ડેરીમાં મોકલો કિલો
' end AS milk_type_name,
    ROUND(SUM(qty) * 1.03, 2) AS qty_m,
    0 qty_e,
    ROUND(SUM(qty) * 1.03, 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
     WHERE md.challan_no=p_challan_no;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_dispatch_note_two_4`(IN p_society_code VARCHAR(12),IN p_challan_no VARCHAR(200),IN p_locale VARCHAR(2))
BEGIN


select from_date,to_date,concat((CASE WHEN p_locale ='en' THEN 'Trucksheet From date ' ELSE ' ટ્રકશીટ તારીખથી ' END ),date_format(from_date,'%d-%m-%y'),' ',
case when from_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ,' To ',
date_format(to_date,'%d-%m-%y'),' ',case when to_shift_code=1 then 
case when p_locale = 'en' then'Morning' else 'સવાર' end 
ELSE 
case when p_locale = 'en' then'Evening' else 'સાંજ' end 
END ) INTO @from_date ,@to_date,@p_param From milk_dispatch where challan_no=p_challan_no and society_code=p_society_code;
select  
case when p_locale='en' then
concat(name,' - ',code_ex) 
else concat(name_local,' - ',code_ex) 
 end
INTO @p_s_name From society where  code=p_society_code;

  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Milk Purchase Cow Liter' else 'સોસાયટી દૂધ ખરીદ ગાય લિટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Milk Purchase Cow Liter' ELSE 'સોસાયટી દૂધ ખરીદ ગાય લિટર'   END AS milk_type_name,

       
            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date 
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Milk Purchase Buffalo Liter' else 'સોસાયટી દૂધ ખરીદ ભેંસ લીટર' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
        
  ) AS A
GROUP BY milk_type_name 

UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
       CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,
            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            ROUND(SUM(Qty), 2) qty_m,
            0 AS qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Total Milk Purchase litre' else 'સોસાયટી કુલ દૂધ ખરીદી લિટર
' end AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(Qty), 2) qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date
        
  ) AS B
GROUP BY milk_type_name 

 

 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,
    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Cow Amount' else 'સોસાયટીના દૂધની ખરીદી ગાયની રકમ' end
    AS milk_type_name,    0 AS qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,    ROUND(SUM(amount), 2) qty_m,
    0 AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2  and collection_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
 case when p_locale='en' then
    'Society Milk Purchase Buffalo Amount' else 'સોસાયટીના દૂધની ખરીદી ભેંસની રકમ' end
    AS milk_type_name,     0 qty_m,
    ROUND(SUM(amount), 2) AS qty_e
FROM
    milk_collection AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 and collection_date BETWEEN @from_date AND @to_date) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,
            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            ROUND(SUM(amount), 2) AS qty_m,
            0 AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 1 and milk_type_code=1 and collection_date BETWEEN @from_date AND @to_date UNION ALL SELECT 
  case when p_locale='en' then  
        'Society Total Milk Purchase Amount' 
        else 'સોસાયટીની કુલ દૂધ ખરીદીની રકમ' end
        AS milk_type_name,            0 AS qty_m,
            ROUND(SUM(amount), 2) AS qty_e,
            0 AS total
    FROM
        milk_Collection
    WHERE
        shift_code = 2  and milk_type_code=2 and collection_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end
    AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
    case when p_locale='en'
    then 
    'Local Sales Cow Liter' else 'સ્થાનિક વેચાણ ગાય લિટર' end AS milk_type_name,
    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity),2),0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર' end AS milk_type_name,
     IFNULL(ROUND(SUM(quantity),2),0) qty_m,
    0 AS qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 1
        AND mc.milk_type_code = 2   AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then 
    'Local Sales Buffalo Liter' else 'સ્થાનિક વેચાણ ભેંસ લિટર' end AS milk_type_name,    0 AS qty_m,
    IFNULL(ROUND(SUM(quantity), 2), 0) qty_e
FROM
    local_milk_sale AS mc
WHERE
    mc.shift_code = 2
        AND mc.milk_type_code = 2 AND sale_date BETWEEN @from_date AND @to_date
) AS A
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    SUM(qty_m) AS qty_m,
    SUM(qty_e) AS qty_e,
    SUM(qty_m) + SUM(qty_e) AS total
FROM
    (SELECT 
    case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,
    IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 1  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_m,
    0 AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 1 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date
UNION ALL SELECT 
case when p_locale='en' then
    'Total Local Sales Liter' else 
    'કુલ સ્થાનિક વેચાણ લિટર' end
    AS milk_type_name,    0 AS qty_m,
     IFNULL(ROUND(SUM(quantity), 2),0) AS qty_e,
    0 AS total
FROM
    local_milk_sale
WHERE
    shift_code = 2 AND milk_type_code = 2  AND sale_date BETWEEN @from_date AND @to_date) AS B
GROUP BY milk_type_name 
UNION ALL SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then
    'Milk Send To Dairy Liter' else 'દૂધ ડેરી લીટરમાં મોકલો' end AS milk_type_name,
    ROUND(SUM(qty)/1.03, 2) AS qty_m,
    0 qty_e,
     ROUND(SUM(qty)/1.03, 2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no 
    WHERE md.challan_no=p_challan_no
UNION ALL  SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    CASE WHEN SUM(qty_m) = 0 THEN 0 ELSE CAST(SUM(kg_fat_m)/SUM(qty_m)*100 as decimal(18,2)) END AS qty_m,
    CASE WHEN SUM(qty_e) = 0 THEN 0 ELSE CAST(SUM(kg_fat_e)/SUM(qty_e)*100 as decimal(18,2)) END  AS qty_e,
    CASE WHEN (SUM(qty_m)+SUM(qty_e)) = 0 THEN 0 ELSE CAST((SUM(kg_fat_m)+ SUM(kg_fat_e))/(SUM(qty_m)+SUM(qty_e))*100  as decimal(18,2)) END  AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' then 
        'Society Avg Fat Cow' else 'સોસાયટી દૂધ ખરીદ ગાય ફેટ' end
        AS milk_type_name,
             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
           CASE WHEN p_locale ='en' THEN 'Society Avg Fat Cow' ELSE 'સોસાયટી દૂધ ખરીદ ગાય ફેટ'   END AS milk_type_name,

       
           0 AS kg_fat_m  ,
            0  as qty_m,
            IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
            IFNULL(SUM(qty),0)  as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=1 AND collection_date BETWEEN @from_date AND @to_date
        
        UNION ALL
        SELECT 
        CASE WHEN p_locale='en' then
        'Society Avg Fat Buffalo' else 'સોસાયટી દૂધ ખરીદ ભેંસ ફેટ' end
        AS milk_type_name,
             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date 
  UNION ALL SELECT 
CASE WHEN p_locale='en' then
        'Society Avg Fat Buffalo' else 'સોસાયટી દૂધ ખરીદ ભેંસ ફેટ' end
        AS milk_type_name,  0 AS kg_fat_m  ,
            0  as qty_m,
          IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
           IFNULL(SUM(qty),0) as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2   and mc.milk_type_code=2 AND collection_date BETWEEN @from_date AND @to_date ) as A GROUP BY milk_type_name
        
  
UNION ALL

 SELECT  @p_s_name as s_name,@p_param as p_name,
    milk_type_name,
    CASE WHEN SUM(qty_m) = 0 THEN 0 ELSE ROUND(SUM(kg_fat_m)/SUM(qty_m)*100,2) END AS qty_m,
    CASE WHEN SUM(qty_e) = 0 THEN 0 ELSE ROUND(SUM(kg_fat_e)/SUM(qty_e)*100,2) END  AS qty_e,
    CASE WHEN (SUM(qty_m)+SUM(qty_e)) = 0 THEN 0 ELSE ROUND((SUM(kg_fat_m)+ SUM(kg_fat_e))/(SUM(qty_m)+SUM(qty_e))*100,2) END  AS total
FROM
    (SELECT 
    CASE WHEN p_locale='en' THEN 'Society Avg Fat' ELSE 'સોસાયટી દૂધ ખરીદ  ફેટ'  END AS milk_type_name,

             IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_m  ,
              IFNULL(SUM(qty),0)  as qty_m,
            0 AS kg_fat_e,
            0 as qty_e
    FROM
        milk_collection AS mc
 
    WHERE
        mc.shift_code = 1  AND collection_date BETWEEN @from_date AND @to_date
  UNION ALL SELECT 
           CASE WHEN p_locale='en' THEN 'Society Avg Fat' ELSE 'સોસાયટી દૂધ ખરીદ  ફેટ'  END AS milk_type_name,

       
           0 AS kg_fat_m  ,
            0  as qty_m,
            IFNULL(ROUND(SUM(fat * qty / 100)  ,2),0) AS kg_fat_e,
            IFNULL(SUM(qty),0)  as qty_e
    FROM
        milk_collection AS mc
    WHERE 
        mc.shift_code = 2     AND collection_date BETWEEN @from_date AND @to_date
     ) as A GROUP BY milk_type_name
        
   

UNION ALL

SELECT @p_s_name as s_name,@p_param as p_name,
case when p_locale='en' then 
    'Total Milk Send To Dairy Kilo' else 'કુલ દૂધ ડેરીમાં મોકલો કિલો' end AS milk_type_name,
    Round(SUM(qty),2) AS qty_m,
    0 qty_e,
    Round(SUM(qty),2) AS total
FROM
    milk_dispatch AS md
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
     WHERE md.challan_no=p_challan_no;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE rpt_election_reg(IN p_from_date DATETIME,IN p_to_date DATETIME,p_society_code VARCHAR(255),IN p_animal_type INT,IN p_limit INT,IN p_locale VARCHAR(50),IN p_qty_amount INT)
BEGIN

IF (p_animal_type=0) THEN
IF (p_limit=0)THEN
SELECT 
    DATE_FORMAT(p_from_date,'%d/%m/%Y') as p_from_date,
    DATE_FORMAT(p_to_date,'%d/%m/%Y') as p_to_date,
    s.code AS society_code,
    CASE WHEN p_locale ='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  AS society_name,
    u.code AS union_code,
    CASE WHEN p_locale ='en' THEN u.name  ELSE IFNULL(u.name_local,u.name) END  AS union_name,
    RIGHT(mc.member_code,4) AS member_code,
    CASE WHEN p_locale ='en' THEN CONCAT(IFNULL(m.first_name, ''),

        + ' ',
        IFNULL(m.middle_name, ''),
        + ' ',
        IFNULL(m.last_name, '')) ELSE IFNULL(concat(m.first_name_local," ",m.last_name_local),
        concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,''))) END  AS member_name,
SUM(qty) AS qty,
SUM(amount) AS amount,
'' as mt_name
FROM
    milk_collection AS mc
        LEFT JOIN
    society AS s ON mc.society_code = s.code
        LEFT JOIN
    unions AS u ON mc.union_code = u.code
        LEFT JOIN
    members AS m ON mc.member_code = m.code
WHERE mc.collection_date  BETWEEN p_from_date AND p_to_date 
AND mc.society_code=p_society_code
GROUP BY s.name_local,u.name_local,m.last_name_local,m.first_name_local,s.code , s.name , u.code , u.name , m.first_name , m.middle_name , m.last_name,mc.member_code
ORDER BY CASE WHEN p_qty_amount = 1 THEN SUM(qty) ELSE CASE WHEN  p_qty_amount = 2 THEN SUM(amount)  END END DESC
   ;
 ELSE 
 SELECT 
    DATE_FORMAT(p_from_date,'%d/%m/%Y') as p_from_date,
    DATE_FORMAT(p_to_date,'%d/%m/%Y') as p_to_date,
    s.code AS society_code,
    CASE WHEN p_locale ='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  AS society_name,
    u.code AS union_code,
    CASE WHEN p_locale ='en' THEN u.name  ELSE IFNULL(u.name_local,u.name) END  AS union_name,
    RIGHT(mc.member_code,4) AS member_code,
    CASE WHEN p_locale ='en' THEN CONCAT(IFNULL(m.first_name, ''),

        + ' ',
        IFNULL(m.middle_name, ''),
        + ' ',
        IFNULL(m.last_name, '')) ELSE IFNULL(concat(m.first_name_local," ",m.last_name_local),
        concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,''))) END  AS member_name,
SUM(qty) AS qty,
SUM(amount) AS amount,
'' as mt_name
FROM
    milk_collection AS mc
        LEFT JOIN
    society AS s ON mc.society_code = s.code
        LEFT JOIN
    unions AS u ON mc.union_code = u.code
        LEFT JOIN
    members AS m ON mc.member_code = m.code
WHERE mc.collection_date  BETWEEN p_from_date AND p_to_date 
AND mc.society_code=p_society_code
GROUP BY s.name_local,u.name_local,m.last_name_local,m.first_name_local,s.code , s.name , u.code , u.name , m.first_name , m.middle_name , m.last_name,mc.member_code
ORDER BY CASE WHEN p_qty_amount = 1 THEN SUM(qty) ELSE CASE WHEN  p_qty_amount = 2 THEN SUM(amount)  END END DESC
LIMIT p_limit  ;
END IF;
ELSE
IF (p_limit=0)THEN
SELECT 
    DATE_FORMAT(p_from_date,'%d/%m/%Y') as p_from_date,
    DATE_FORMAT(p_to_date,'%d/%m/%Y') as p_to_date,
    s.code AS society_code,
    CASE WHEN p_locale ='en' THEN  s.name ELSE IFNULL(s.name_local,s.name) END AS society_name,
    u.code AS union_code,
    CASE WHEN p_locale ='en' THEN  u.name ELSE IFNULL(u.name_local,u.name) END AS union_name,
    RIGHT(mc.member_code,4) AS member_code,
    CASE WHEN p_locale ='en' THEN CONCAT(IFNULL(m.first_name, ''),

        + ' ',
        IFNULL(m.middle_name, ''),
        + ' ',
        IFNULL(m.last_name, ''))ELSE IFNULL(concat(m.first_name_local," ",m.last_name_local),
        concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,''))) END AS member_name,
SUM(qty) AS qty,
SUM(amount) AS amount,
CASE WHEN p_locale ='en' THEN  mt.name ELSE IFNULL(mt.name_local,mt.name) END as mt_name
FROM
    milk_collection AS mc
        LEFT JOIN
    society AS s ON mc.society_code = s.code
        LEFT JOIN
    unions AS u ON mc.union_code = u.code
        LEFT JOIN
    members AS m ON mc.member_code = m.code
    LEFT JOIN 
    milk_types mt ON mt.code=mc.milk_type_code
    WHERE mc.collection_date  BETWEEN p_from_date AND p_to_date 
AND mc.society_code=p_society_code AND mc.milk_type_code=p_animal_type
GROUP BY mt.name_local,m.last_name_local,m.first_name_local,u.name_local,s.name_local,mt.name_local,s.code , s.name , u.code , u.name , m.first_name , m.middle_name , m.last_name,mc.member_code,  mt.name
ORDER BY CASE WHEN p_qty_amount = 1 THEN SUM(qty) ELSE CASE WHEN  p_qty_amount = 2 THEN SUM(amount)  END END DESC
   ;

ELSE 
SELECT 
    DATE_FORMAT(p_from_date,'%d/%m/%Y') as p_from_date,
    DATE_FORMAT(p_to_date,'%d/%m/%Y') as p_to_date,
    s.code AS society_code,
    CASE WHEN p_locale ='en' THEN  s.name ELSE IFNULL(s.name_local,s.name) END AS society_name,
    u.code AS union_code,
    CASE WHEN p_locale ='en' THEN  u.name ELSE IFNULL(u.name_local,u.name) END AS union_name,
    RIGHT(mc.member_code,4) AS member_code,
    CASE WHEN p_locale ='en' THEN CONCAT(IFNULL(m.first_name, ''),

        + ' ',
        IFNULL(m.middle_name, ''),
        + ' ',
        IFNULL(m.last_name, ''))ELSE IFNULL(concat(m.first_name_local," ",m.last_name_local),
        concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,''))) END AS member_name,
SUM(qty) AS qty,
SUM(amount) AS amount,
CASE WHEN p_locale ='en' THEN  mt.name ELSE IFNULL(mt.name_local,mt.name) END as mt_name
FROM
    milk_collection AS mc
        LEFT JOIN
    society AS s ON mc.society_code = s.code
        LEFT JOIN
    unions AS u ON mc.union_code = u.code
        LEFT JOIN
    members AS m ON mc.member_code = m.code
    LEFT JOIN 
    milk_types mt ON mt.code=mc.milk_type_code
    WHERE mc.collection_date  BETWEEN p_from_date AND p_to_date 
AND mc.society_code=p_society_code AND mc.milk_type_code=p_animal_type
GROUP BY mt.name_local,m.last_name_local,m.first_name_local,u.name_local,s.name_local,mt.name_local,s.code , s.name , u.code , u.name , m.first_name , m.middle_name , m.last_name,mc.member_code,  mt.name
ORDER BY CASE WHEN p_qty_amount = 1 THEN SUM(qty) ELSE CASE WHEN  p_qty_amount = 2 THEN SUM(amount)  END END DESC
LIMIT p_limit  ;
END IF;
END IF;
END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_local_milk_sale`(IN p_sale_date VARCHAR(100),IN p_consumer_type INT,IN p_milk_class INT,IN p_milk_type INT, IN p_society_code VARCHAR(12),IN p_member_code varchar(30))
BEGIN
	SELECT lms.society_code,s.name As society_name ,
    RIGHT(lms.consumer_code,4) AS consumer_code,
    mcl.name As milk_grade,
    CASE WHEN  (lms.consumer_type = 1 ) THEN  "Member" else "Customer" end AS consumer_type,
	lms.sale_date ,
           DATE_FORMAT(lms.sale_date, '%d-%m-%y') as date_formate,
		   round(lms.converted_quantity,2) AS quantity, 
           round(round(lms.amount,2)/round(lms.converted_quantity,2),2) AS rate, 
           round(lms.amount,2) AS amount, 
           mt.name AS milk_type, 
	       round(lms.cash,2) AS cash,sft.name AS shift, 
	       round(lms.credit,2) AS credit, 
	       round(lms.coupon,2) AS coupon,
    
			CASE WHEN  (lms.consumer_type = 2 ) THEN  cus.name 
            ELSE concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,''))  END  AS consumer_name
			FROM local_milk_sale AS lms 
			INNER JOIN milk_types AS mt ON lms.milk_type_code = mt.code         
			INNER JOIN shifts AS sft ON lms.shift_code = sft.code 
			INNER JOIN milk_classes AS mcl ON lms.milk_class_code=mcl.code
			LEFT JOIN members AS member ON lms.consumer_code = member.code 
			LEFT JOIN customers AS cus ON lms.consumer_code = cus.code      
			INNER JOIN society AS s ON lms.society_code =s.code
			
			 WHERE lms.sale_date = p_sale_date
			 AND lms.consumer_type = Case p_consumer_type when 0 then lms.consumer_type else p_consumer_type end
			 And lms.consumer_code = Case p_consumer_type when 0 then lms.consumer_code else 
										  Case when p_member_code = 1 then lms.consumer_code else p_member_code End End
			 AND lms.milk_class_code = Case  p_milk_class when 0 then lms.milk_class_code else p_milk_class END
			 AND lms.milk_type_code = Case  p_milk_type when 0 then  lms.milk_type_code else p_milk_type END 
	         AND lms.society_code=p_society_code
			 ORDER BY lms.sale_date,lms.consumer_code; 

END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_meeting_details`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_locale varchar(50))
BEGIN
-- call rpt_meeting_details('2022-01-01','2023-01-01','1011006','en')
select DATE_FORMAT(p_from_date,'%d/%m/%y') as p_from_date,DATE_FORMAT(p_to_date,'%d/%m/%y') as p_to_date,
	   CONCAT(CASE WHEN p_locale='en' THEN society.name ELSE IFNULL(society.name_local,society.name) END ,'-(',society.code_ex,')') as society_code,
       detailed_agenda as detailed_agenda,
       subject_line as subject_line,
       DATE_FORMAT( meeting_date,'%d/%m/%y') as meeting_date ,
       meeting_time as meeting_time ,
       CASE WHEN MOM.status=1 THEN 'Open' ELSE 'Close' END  as mom_status,
       MOM.mom as mom_mom,
       ifnull(mom_action.action_taken,'N/A') as action_taken, 
      case when mom_action.date is null then '' else DATE_FORMAT( mom_action.date,'%d/%m/%y') end as mom_action_date 
 from meeting_agenda
 INNER JOIN mom as MOM ON meeting_agenda.code =MOM.meeting_agenda_code
 INNER JOIN society ON meeting_agenda.society_code=society.code
 left JOIN mom_action ON MOM.code =mom_action.mom_code
 WHERE meeting_agenda.society_code=p_society_code
 AND CAST(meeting_date as date) BETWEEN p_from_date AND p_to_date
 order by  meeting_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_bank_detail`(IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN

SELECT  member.code_ex as   member_ex_code,
		CASE WHEN p_locale ='en' THEN
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,'')) ELSE  
			IFNULL(concat(member.first_name_local,+" ", member.middle_name_local," ",member.last_name_local),
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,''))) END as member_name, 
        
        farmer_bill.milk_qty as qty,
        farmer_bill.milk_amount as milk_amount,
        IFNULL(bank.name,'') as bank_name,IFNULL(md.ifsc,'') as ifsc,IFNULL(md.account_no,'') as account_no,
        dcs.code AS society_code,
        CASE WHEN p_locale ='en' THEN dcs.short_name ELSE  IFNULL(dcs.short_name_local,dcs.short_name)  END AS society_name,
        CONCAT(dcs.short_name,'-(',spc.code,')') as society_ex_code ,
        CONCAT(unions.name,'-(',unions.code,')') as unions_ex_code ,
        CONCAT(date_format(spc.from_date,'%d/%m/%Y'),' To ',date_format(spc.to_date,'%d/%m/%Y')) as pc_code,
        farmer_bill.net_amount
FROM  member_bill as farmer_bill
INNER JOIN members as member  ON farmer_bill.member_code =member.code
INNER JOIN  society as dcs ON farmer_bill.society_code=dcs.code
INNER JOIN member_details as md ON md.member_code=member.code
LEFT JOIN banks as bank ON bank.code=md.bank_code  
LEFT JOIN society_payment_cycles as spc ON spc.code=farmer_bill.society_payment_cycle_code
LEFT JOIN unions as unions ON unions.code= dcs.union_code
WHERE farmer_bill.society_payment_cycle_code=p_society_payment_cycle_code
AND farmer_bill.society_code=p_society_code
ORDER BY member.code_ex ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_head_wise`(IN p_society_code VARCHAR(50),IN p_member_code varchar(30),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN

SELECT member.code_ex as   member_ex_code,farmer_bill.member_code as member_code,
		CASE WHEN p_locale ='en' THEN
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,'')) ELSE  
			IFNULL(concat(member.first_name_local,+" ", member.middle_name_local," ",member.last_name_local),
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,''))) END as member_name, 
       
       farmer_bill.milk_qty as qty,
       farmer_bill.milk_amount as milk_amount,farmer_bill.net_amount,
       concat(date_format(from_date,'%d/%m/%Y'),' To ',date_format(to_date,'%d/%m/%Y')) as pc_date,
        CONCAT(
        CASE WHEN p_locale ='en' THEN U.name ELSE  IFNULL(U.name_local,U.name)  END,' - (',society.union_code,')')as s_code ,
        CONCAT(
        CASE WHEN p_locale ='en' THEN society.short_name ELSE  IFNULL(society.short_name_local,society.short_name)  END,' - (',farmer_bill.society_code,')') as p_code,
        farmer_bill.society_code as society_code,farmer_bill.society_payment_cycle_code as society_payment_cycle_code
FROM  member_bill as farmer_bill
INNER JOIN members as member  ON farmer_bill.member_code =member.code
INNER JOIN  society as dcs ON farmer_bill.society_code=dcs.code
LEFT JOIN society_payment_cycles as sc ON sc.code=farmer_bill.society_payment_cycle_code
LEFT JOIN society as society ON society.code=farmer_bill.society_code
LEFT JOIN unions as U ON u.code=society.union_code
WHERE farmer_bill.society_code =p_society_code
AND farmer_bill.society_payment_cycle_code=p_society_payment_cycle_code
AND farmer_bill.member_code=CASE WHEN p_member_code= 0  THEN farmer_bill.member_code ELSE p_member_code END
ORDER BY member_ex_code;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_month_year_cosolidate`(IN p_society_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_from_date DATETIME,IN p_to_date DATETIME,IN  p_report_type INT,IN p_locale varchar(50))
-- CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_month_year_cosolidate`(IN p_society_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN  p_report_type INT,IN p_locale varchar(50))
BEGIN

IF (p_report_type=1)THEN

 SELECT CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' TO ' ,DATE_FORMAT(p_to_date,'%d/%m/%Y')) as param_date,
		
		mb.society_code,
		RIGHT(mb.member_code,4) AS member_code,
		mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
		
        CASE WHEN p_locale ='en' THEN u.name ELSE  IFNULL(u.name_local,u.name)  END AS union_name,
		
	    0 as pc_date,
        
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
    
		
		SUM(mb.milk_qty) as milk_qty,
		SUM(mb.milk_amount) as milk_amount,
		SUM(mb.net_amount) as net_amount,
        SUM(mb.product_sale_amount) as product_sale_amount,
		SUM(mb.local_sale_amount) as local_sale_amount,
		SUM(mb.loan_amount) as loan_amount,
        SUM(IFNULL(mb.other_add_amount,0) )   AS other_add_amount,
        SUM(IFNULL(mb.other_ded_amount,0) ) AS other_ded_amount ,
		MONTH(spc.from_date) as months,Year(spc.from_date) as years,
		DATE_FORMAT(spc.from_date,'%M')as feb_name,
        CONCAT(s.short_name,'',mb.society_code) as society_code_name,
        CONCAT(u.name,'', mb.union_code) as union_code_name
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
       WHERE spc.from_date >=p_from_date AND spc.to_date <=p_to_date  AND mb.society_code= p_society_code
       AND mb.member_code =CASE WHEN p_member_code=0 THEN mb.member_code ELSE p_member_code END 
GROUP BY  mb.society_code,mb.member_code,mb.union_code,s.name  ,u.name , m.first_name ,m.middle_name,m.last_name , 
 MONTH(spc.from_date),Year(spc.from_date),DATE_FORMAT(spc.from_date,'%M')
ORDER BY mb.member_code;

ELSE IF (p_report_type=2) THEN

SELECT  CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' TO ' ,DATE_FORMAT(p_to_date,'%d/%m/%Y')) as param_date,
		mb.society_code,
		RIGHT(mb.member_code,4) AS member_code,
		mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
		
        CASE WHEN p_locale ='en' THEN u.name ELSE  IFNULL(u.name_local,u.name)  END AS union_name,
		
        CONCAT(DATE_FORMAT(spc.from_date,'%d/%m/%Y'),' TO ',DATE_FORMAT(spc.to_date,'%d/%m/%Y') ) as pc_date,
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		
		SUM(mb.milk_qty) as milk_qty,
		SUM(mb.milk_amount) as milk_amount,
		SUM(mb.net_amount) as net_amount,
        SUM(mb.product_sale_amount) as product_sale_amount,
		SUM(mb.local_sale_amount) as local_sale_amount,
		SUM(mb.loan_amount) as loan_amount,
        SUM(IFNULL(mb.other_add_amount,0) )   AS other_add_amount,
        SUM(IFNULL(mb.other_ded_amount,0) ) AS other_ded_amount ,
        0 as months,0 as years,'' as feb_name,
        CONCAT(s.name,'',mb.society_code) as society_code_name,
        CONCAT(u.name,'', mb.union_code) as union_code_name
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
   WHERE spc.from_date >=p_from_date AND spc.to_date <=p_to_date  AND mb.society_code= p_society_code
       AND mb.member_code =CASE WHEN p_member_code=0 THEN mb.member_code ELSE p_member_code END 
GROUP BY  mb.society_payment_cycle_code,mb.society_code,mb.member_code,mb.union_code,s.name  ,u.name , m.first_name ,m.middle_name,m.last_name ,spc.from_date,spc.to_date
ORDER BY mb.member_code,mb.society_payment_cycle_code;

ELSE 

SELECT  CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' TO ' ,DATE_FORMAT(p_to_date,'%d/%m/%Y')) as param_date,
		mb.society_code,
		RIGHT(mb.member_code,4) AS member_code,
		mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name) END AS society_name,
		
        CASE WHEN p_locale ='en' THEN u.name ELSE  IFNULL(u.name_local,u.name) END AS union_name,
		
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		
		SUM(mb.milk_qty) as milk_qty,
		SUM(mb.milk_amount) as milk_amount,
		SUM(mb.net_amount) as net_amount,
        SUM(mb.product_sale_amount) as product_sale_amount,
		SUM(mb.local_sale_amount) as local_sale_amount,
		SUM(mb.loan_amount) as loan_amount,
        SUM(IFNULL(mb.other_add_amount,0) )   AS other_add_amount,
        SUM(IFNULL(mb.other_ded_amount,0) ) AS other_ded_amount ,
        0 as months,0 as years, 0 as pc_date,'' as feb_name,
        CONCAT(s.name,'',mb.society_code) as society_code_name,
         CONCAT(u.name,'', mb.union_code) as union_code_name
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
    WHERE spc.from_date >=p_from_date AND spc.to_date <=p_to_date  AND mb.society_code= p_society_code
       AND mb.member_code =CASE WHEN p_member_code=0 THEN mb.member_code ELSE p_member_code END 
GROUP BY  mb.society_code,mb.member_code,mb.union_code,s.name  ,u.name ,m.first_name ,m.middle_name,m.last_name 
ORDER BY mb.member_code;
END IF;
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_other_head_addition`(IN p_member_code VARCHAR(50),IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN
SELECT mb.member_code as member_code,
		CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
       bh.name as  head_name,
       bi.adjustment as amount
FROM member_bill as mb
LEFT JOIN member_bill_transaction as bi ON bi.member_bill_code=mb.code
LEFT JOIN bill_head as bh ON bh.society_code=mb.society_code and bh.code=bi.bill_head_code
WHERE   bh.code NOT IN (101,105)   AND head_type=1
AND mb.society_code=p_society_code AND mb.society_payment_cycle_code=p_society_payment_cycle_code
AND mb.member_code=p_member_code;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_other_head_deduction`(IN p_member_code VARCHAR(50),IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN
SELECT mb.member_code as member_code, 
		CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
       
       bi.adjustment as amount 
FROM member_bill as mb
LEFT JOIN member_bill_transaction as bi ON bi.member_bill_code=mb.code
LEFT JOIN bill_head as bh ON  bh.code=bi.bill_head_code
WHERE bh.code NOT IN ('102')  and head_type=0
and mb.society_code=p_society_code AND mb.society_payment_cycle_code=p_society_payment_cycle_code
and mb.member_code=p_member_code;

END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_billing_product_sale_deduction`(IN p_member_code VARCHAR(50),IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_locale varchar(50))
BEGIN

SELECT 
    ps.consumer_code,
    CASE WHEN p_locale ='en' THEN pp.name ELSE  IFNULL(pp.name_local,pp.name)  END AS product_short_name,
    
    ROUND((IFNULL(SUM(pst.amount), 0) + IFNULL(SUM(pst.tax_amount), 0) - IFNULL(SUM(pst.discount), 0)),
            2) AS netamount
FROM
    product_sale ps
        INNER JOIN
    product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
        INNER JOIN
    products AS pp ON pp.code = pst.product_code
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code 
          AND psi.society_payment_cycle_code=p_society_payment_cycle_code
        
 WHERE ps.consumer_code = p_member_code  AND pst.society_code=p_society_code
GROUP BY ps.consumer_code , pp.name,pp.name_local  
 ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_bill_head_report`(IN p_society_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN p_head_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_report_type INT,IN p_locale varchar(50))
BEGIN

IF (p_report_type =1 )THEN
SELECT 
    CONCAT(date_format(p_from_date,'%d/%m/%Y'),' To ',date_format(p_to_date,'%d/%m/%Y')) as param_date,
    mb.member_code,
    m.code_ex AS member_ex_code,
    CASE WHEN p_locale ='en' THEN
		concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
		IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
		concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
    
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
    IFNULL(bi.adjustment, mb.net_amount) AS head_value,
    mb.milk_qty,
    mb.milk_amount,mb.society_payment_cycle_code as society_payment_cycle_code,
    CONCAT(DATE_FORMAT(spc.from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(spc.to_date,'%d/%m/%Y')) as pc_code,
    mb.society_code AS s_code,
    CASE WHEN p_locale ='en' THEN sc.name ELSE  IFNULL(sc.name_local,sc.name)  END AS society_name,
     
     CONCAT(u.name," - (",u.code,")") as u_name
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON   bh.code = bi.bill_head_code
        LEFT JOIN
    members AS m ON m.code = mb.member_code
        LEFT JOIN 
	society_payment_cycles as spc ON spc.code=mb.society_payment_cycle_code
            LEFT JOIN society as sc ON sc.code=mb.society_code 
        LEFT JOIN 
	 unions as u ON u.code=spc.union_code
WHERE bh.code NOT IN (101,105)   AND CAST(spc.from_date as DATE) >= p_from_date and CAST(spc.to_date as DATE) <=p_to_date
AND mb.society_code=p_society_code  AND bi.bill_head_code=CASE WHEN p_head_code ='0' THEN bi.bill_head_code   ELSE p_head_code END
  AND mb.member_code=CASE WHEN p_member_code ='0' THEN mb.member_code   ELSE p_member_code END ORDER BY mb.member_code
 ;
 ELSE
 
 SELECT 
    CONCAT(date_format(p_from_date,'%d/%m/%Y'),' To ',date_format(p_to_date,'%d/%m/%Y')) as param_date,
    mb.member_code,
    m.code_ex AS member_ex_code,
    CASE WHEN p_locale ='en' THEN
		concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
		IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
		concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
    
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
    SUM(IFNULL(bi.adjustment, mb.net_amount)) AS head_value,
    SUM(mb.milk_qty)as milk_qty,
    SUM(mb.milk_amount)  as milk_amount ,
    ''as  society_payment_cycle_code,
    ''as pc_code,
    mb.society_code AS s_code,
    CASE WHEN p_locale ='en' THEN sc.name ELSE  IFNULL(sc.name_local,sc.name)  END AS society_name,
	
    CONCAT(u.name," - (",u.code,")") as u_name
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON   bh.code = bi.bill_head_code
        LEFT JOIN
    members AS m ON m.code = mb.member_code
        LEFT JOIN 
	society_payment_cycles as spc ON spc.code=mb.society_payment_cycle_code
            LEFT JOIN society as sc ON sc.code=mb.society_code 
        LEFT JOIN 
	 unions as u ON u.code=spc.union_code
WHERE bh.code not in (101,105)  AND CAST(spc.from_date as DATE) >= p_from_date and CAST(spc.to_date as DATE) <=p_to_date
AND mb.society_code=p_society_code  AND bi.bill_head_code=CASE WHEN p_head_code ='0' THEN bi.bill_head_code   ELSE p_head_code END
  AND mb.member_code=CASE WHEN p_member_code ='0' THEN mb.member_code   ELSE p_member_code END
 GROUP BY bh.name,bh.name_local,mb.member_code,m.code_ex  ,m.first_name ,m.middle_name,m.last_name,bh.name ,sc.name,mb.society_code,u.name,u.code ORDER BY mb.member_code;
 END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_collection_audit`(IN p_society_code VARCHAR(50),IN p_from_date DATETIME, IN p_to_date DATETIME,IN p_locale varchar(50))
BEGIN
-- CALL rpt_member_collection_audit('1010987','2022-03-28 06:00:00','2022-03-28 06:00:00','en')
	
    SELECT 	DATE_FORMAT(p_from_date, '%d-%m-%Y') as p_from_date
			,DATE_FORMAT(p_to_date, '%d-%m-%Y') as p_to_date
			,CASE WHEN p_locale ='en' THEN CONCAT(u.name,' - (',mc.union_code,')') ELSE  CONCAT(IFNULL(u.name_local,u.name),' - (',mc.union_code,')') END AS unions_name
			,mc.society_code
			,CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name
            ,DATE_FORMAT(mc.collection_date, '%d/%m/%Y') AS collection_date
            ,CASE WHEN p_locale ='en' THEN sf.name ELSE  IFNULL(sf.name_local,sf.name)  END AS shift
            ,m.code_ex AS member_code
            , CASE WHEN p_locale ='en' THEN
				CONCAT(IFNULL(m.first_name+" ",''),IFNULL(m.middle_name+" ",''),IFNULL(m.last_name,'')) ELSE  
				IFNULL(CONCAT(m.first_name_local," ", m.middle_name_local," ",m.last_name_local),
				CONCAT(IFNULL(m.first_name+" ",''),IFNULL(m.middle_name+" ",''),IFNULL(m.last_name,''))) END AS member_name
            ,IFNULL(mca.qty,0) AS old_qty
            ,IFNULL(mca.fat,0) AS old_fat
            ,IFNULL(mca.snf,0) AS old_snf
            ,IFNULL(mca.amount,0) AS old_amount
            ,1 as old_user
            ,DATE_FORMAT(mca.created_at, '%d/%m/%Y %H:%i:%s') AS created_at
            ,IFNULL(mc.qty,0) AS new_qty
            ,IFNULL(mc.fat,0) AS new_fat
            ,IFNULL(mc.snf,0) AS new_snf
            ,IFNULL(mc.amount,0) AS new_amount
            ,1 as new_user
            ,DATE_FORMAT(mc.updated_at, '%d/%m/%Y %H:%i:%s') AS updated_at
    FROM	milk_collection mc
			INNER JOIN milk_collection_audit mca ON mc.code = mca.code
            INNER JOIN society s ON mc.society_code = s.code
            INNER JOIN shifts sf ON mc.shift_code = sf.code
            INNER JOIN unions u ON mc.union_code = u.code
            INNER JOIN members m ON mc.member_code = m.code
	WHERE 	mc.collection_date BETWEEN p_from_date AND p_to_date AND mc.society_code=p_society_code;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_collection_summary`(IN p_from_collection_date DATETIME,IN p_to_collection_date DATETIME,IN p_society_code VARCHAR(12),IN p_ltr_kg INT,IN p_locale varchar(50))
BEGIN

    SELECT
    SUBSTRING(DATE_FORMAT(p_from_collection_date,'%d/%m/%Y'),1,10) AS from_date,
    SUBSTRING(DATE_FORMAT(p_to_collection_date,'%d/%m/%Y'),1,10) AS to_date,
    
    
    mc.society_code AS society_code,
    CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
    mc.union_code AS union_code,
    CASE WHEN p_locale ='en' THEN u.name ELSE  IFNULL(s.name_local,u.name)  END AS union_name,
    s.village_code AS village_code,
    CASE WHEN p_locale ='en' THEN v.name ELSE  IFNULL(v.name_local,v.name)  END AS village_name,
    concat(SUBSTRING(DATE_FORMAT(mc.collection_date,'%d/%m/%YY'),1,10),CASE when SUBSTRING(mc.collection_date,12)='06:00:00'  then ' - M ' else ' - E ' END) AS collection_date,
    CASE WHEN  (mc.qty_mode = 0 ) THEN  "Ltr" else "Kg" end AS qty_mode,
    RIGHT(mc.member_code,4) AS member_code,
    CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
    CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS milk_type,

IFNULL(ded.installment_amount,0) as installment_amount ,
IFNULL(lms.credit,0) as local_sale_credit,
round(IFNULL(SUM(amount),0)-(IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0)),2) as netpayble,

round( IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0),2) as deduction,
case when p_ltr_kg =qty_mode then round(round(sum(amount),2)/round(sum(qty),2),2) else round(round(sum(amount),2)/round(sum(converted_qty),2),2) END  AS avg_rate,
round(sum(amount),2)AS amount,

case when p_ltr_kg =qty_mode then round(sum(qty),2) 
else round(sum(converted_qty),2) END AS qty,

case when p_ltr_kg =qty_mode then round(sum(Round(qty * snf/100,2))/Sum(qty) * 100, 2) 
else  round(sum(Round(converted_qty * snf/100,2))/Sum(converted_qty) * 100, 2) END  as avg_snf,
 
case when p_ltr_kg =qty_mode then sum(Round(qty * snf/100,2)) 
else sum(Round(converted_qty * snf/100,2)) END  as kg_snf,

case when p_ltr_kg =qty_mode then round(sum(Round(qty * fat/100,2))/Sum(qty) * 100, 1) 
else round(sum(Round(converted_qty * fat/100,2))/Sum(converted_qty) * 100, 1) END as avg_fat,
   
case when p_ltr_kg =qty_mode then sum(Round(qty * fat/100,2)) 
else sum(Round(converted_qty * fat/100,2))  END as kg_fat,
   
case when p_ltr_kg =qty_mode then round(sum(Round(qty * clr/100,2))/Sum(qty) * 100, 1) 
else round(sum(Round(converted_qty * clr/100,2))/Sum(converted_qty) * 100, 1) END  as avg_clr,
   
case when p_ltr_kg =qty_mode then sum(Round(qty * clr/100,2)) 
else sum(Round(converted_qty * clr/100,2)) END  as kg_clr

FROM milk_collection AS mc
    INNER JOIN society AS s ON mc.society_code = s.code
    INNER JOIN unions AS u ON mc.union_code = u.code
    INNER JOIN villages AS v ON s.village_code = v.code
    INNER JOIN members AS m ON mc.member_code = m.code
    INNER JOIN milk_types AS mt ON mc.milk_type_code = mt.code
    
    LEFT JOIN (select   member_code,sum(installment_amount) as installment_amount 
               FROM product_sale_installment as psi 
               LEFT JOIN product_sale as ps ON ps.invoice_no=psi.invoice_no
               where deduction_date BETWEEN p_from_collection_date AND p_to_collection_date AND ps.payment_mode=1 AND (ps.consumer_type=0 or ps.consumer_type=1)  
               group by  member_code
               ) as ded ON ded.member_code = mc.member_code 
    LEFT JOIN (select   consumer_code,sum(credit) as credit 
                from local_milk_sale   
                where(consumer_type=0 OR consumer_type=1) AND payment_mode =1 AND sale_date BETWEEN p_from_collection_date AND p_to_collection_date
                group by  consumer_code
                ) as lms ON lms.consumer_code = mc.member_code 
   
   
   WHERE collection_date BETWEEN p_from_collection_date AND p_to_collection_date
   AND mc.society_code=p_society_code
	
   group by society_code,society_name,union_code,union_name,village_code,village_name,mc.collection_date,
			mc.qty_mode,mc.member_code,member_name,installment_amount,local_sale_credit,
			amount,ded.installment_amount,lms.credit,qty,converted_qty,clr,snf,fat,mt.name,mt.name_local
	
   ORDER BY  mc.member_code,collection_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_milk_collection_slip`(IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_locale VARCHAR(50))
BEGIN
 
SELECT m.code_ex as  member_code_ex,
	   mc.member_code as member_original_code,
       CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
       
       CONCAT(DATE_FORMAT(CAST(mc.collection_date as DATE),'%d') ,' - ', CASE WHEN mc.shift_code=1 THEN 'M' ELSE 'E' END) as collection_date,
       ROUND( (qty),2)   as quantity,
       ROUND( (fat * qty / 100) /  (qty) * 100,2)  as fat,
       ROUND( (snf * qty / 100) /  (qty) * 100,2)   as snf,
       ROUND( (amount),2)  as amount,
       ROUND( (amount)/ (qty),2)  as rate,
       CASE WHEN p_locale ='en' THEN aty.name ELSE  IFNULL(aty.name_local,aty.name)  END AS animal_type_name,
       
       mc.society_code AS s_code,
       CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
       CONCAT(date_format(spc.from_date,'%d/%m/%Y'),' To ',date_format(spc.to_date,'%d/%m/%Y')) as pc_code,
       mc.society_payment_cycle_code as society_payment_cycle_code,mc.society_code as society_code
FROM milk_collection as mc 
INNER JOIN members as m ON m.code=mc.member_code
INNER JOIN milk_types as aty ON aty.code=mc.milk_type_code
INNER JOIn society as s ON s.code=mc.society_code
LEFT JOIN society_payment_cycles as spc ON spc.code=mc.society_payment_cycle_code
WHERE mc.society_code=p_society_code 
AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code  ELSE p_member_code END 
AND mc.society_payment_cycle_code=p_society_payment_cycle_code

 ORDER BY  mc.member_code,CAST(mc.collection_date as date),shift_code 
  ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=root@localhost PROCEDURE rpt_member_milk_collection_slip_head_wise(IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_locale VARCHAR(50))
BEGIN
SELECT member_code,head_name,amount FROM 
(SELECT 
    RIGHT(mb.member_code,4) as member_code, 
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,

    sum(bi.adjustment) as amount,
    1 as sr_no
FROM
    member_bill AS mb
       LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON  bh.code = bi.bill_head_code 
WHERE bh.code  NOT IN (102,105,101) AND bh.head_type=0 AND mb.member_code=p_member_code
AND mb.society_payment_cycle_code=p_society_payment_cycle_code
AND mb.society_code=p_society_code
GROUP BY mb.member_code,bh.name_local,bh.name
UNION ALL
 
SELECT 
    RIGHT(mb.member_code,4) as member_code, 
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,

    bi.adjustment as amount,
    2 as sr_no
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON  bh.code = bi.bill_head_code 
WHERE bh.code  NOT IN (102,105,101) AND bh.head_type=1 AND mb.member_code=p_member_code
AND mb.society_payment_cycle_code=p_society_payment_cycle_code
AND mb.society_code=p_society_code

UNION ALL 
 
SELECT 
    RIGHT(ps.consumer_code,4) as member_code,
    CASE WHEN p_locale ='en' THEN pp.name ELSE  IFNULL(pp.name_local,pp.name)  END AS head_name,
    ROUND(IFNULL((SUM(pst.net_amount)), 0),2) AS amount,
     3 as sr_no
FROM
    product_sale ps
          JOIN
    product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
          JOIN
    products AS pp ON pp.code = pst.product_code
         JOIN 
   product_sale_installment as psi ON psi.member_code=ps.consumer_code AND ps.invoice_no = psi.invoice_no
    AND psi.society_payment_cycle_code=p_society_payment_cycle_code
WHERE
    ps.consumer_code = p_member_code
AND pst.society_code=p_society_code
GROUP BY ps.consumer_code , pp.name ,pp.name_local 

UNION
 
SELECT 
    RIGHT(ps.consumer_code,4) as member_code,
    'Total-Product' AS head_name,
    ROUND(IFNULL((SUM(pst.net_amount)), 0),2) AS amount,
    4 as sr_no
FROM
    product_sale ps
          JOIN
    product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
          JOIN
    products AS pp ON pp.code = pst.product_code
         JOIN 
   product_sale_installment as psi ON psi.member_code=ps.consumer_code AND ps.invoice_no = psi.invoice_no
    AND psi.society_payment_cycle_code=p_society_payment_cycle_code
WHERE
    ps.consumer_code = p_member_code
AND pst.society_code=p_society_code AND pst.net_amount !=0 
 GROUP BY RIGHT(ps.consumer_code,4)

 UNION ALL

 SELECT 
    RIGHT( mb.member_code,4) as member_code , 
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,

    bi.amount as amount,
     5 as sr_no
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON  bh.code = bi.bill_head_code
WHERE mb.member_code=p_member_code AND  bh.code  IN (105)
AND mb.member_code=p_member_code
AND mb.society_payment_cycle_code=p_society_payment_cycle_code
AND mb.society_code=p_society_code ORDER BY sr_no ASC) as A;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_reg`(IN p_society_code Varchar(12),IN p_from_date DATE ,IN p_member_type INT,IN p_locale varchar(50))
BEGIN
SELECT 
  p_from_date,
  CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS member_type,
  IFNULL(SUM(md.number_of_cow+md.number_of_buffalo),'') AS total_animal,
  IFNULL((DATE_FORMAT(md.registration_date, '%d-%m-%Y')),'') AS registration_date,
  IFNULL((DATE_FORMAT(md.birth_date, '%d-%m-%Y')),'') AS birth_date,
  m.mobile_no AS mobile_no,
  IFNULL(g.name,'') AS gender,
  RIGHT(m.code,4) AS member_code,
  IFNULL (md.account_no,'') as account_no,
  s.code AS society_code ,
  CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
  CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
  md.union_code,
  IFNULL(s.village_code,'') AS village_code,
  CASE WHEN p_locale ='en' THEN u.name ELSE  IFNULL(u.name_local,u.name)  END AS union_name,
  CASE WHEN p_locale ='en' THEN IFNULL(v.name,'') ELSE  IFNULL(v.name_local,IFNULL(v.name,''))  END AS village_name
FROM members AS m
     LEFT JOIN member_details AS md ON  m.code=md.member_code
     LEFT JOIN genders AS g ON md.gender_code=g.code
     LEFT JOIN society AS s ON s.code=m.society_code
     LEFT JOIN unions AS u ON u.code=md.union_code
     LEFT JOIN villages AS v ON v.code=s.village_code
     LEFT JOIN member_types AS mt ON m.member_type_code=mt.code

 WHERE m.is_active=true
 AND m.society_code=p_society_code
 AND m.member_type_code = case p_member_type when 1 then 1 else  2 end
 AND (md.registration_date IS NULL OR md.registration_date <= p_from_date)

 group by  member_type,md.number_of_cow,md.number_of_buffalo,md.registration_date,md.birth_date,mobile_no,gender,m.code,society_code,society_name,member_name,md.union_code,
      md.village_code,union_name,village_name,md.account_no
 order by member_code,registration_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_wise_head_pivoting`(IN p_member_code VARCHAR(255),IN p_society_code VARCHAR(50),IN p_society_payment_cycle_code varchar(100),IN p_locale VARCHAR(50))
BEGIN

SELECT 
CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.last_name,''), +" - ",IFNULL(A.member_code,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.last_name_local," - ",A.member_code),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.last_name,''), +" - ",IFNULL(A.member_code,''))) END as member_code,

head_name,
amount,A.society_code,ord,
CASE WHEN p_locale ='en' THEN ss.short_name ELSE  IFNULL(ss.short_name_local,ss.short_name)  END AS society_name,

A.society_code as society_code_name,
CONCAT(us.name,' - (',ss.union_code,')') as union_code_name,
CONCAT(date_format(spc.from_date,'%d/%m/%Y'),' To ',date_format(spc.to_date,'%d/%m/%Y')) as p_date
FROM 
(SELECT 
    RIGHT(mb.member_code,4) as member_code,
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
    IFNULL(bi.adjustment,bi.amount) AS amount,
    mb.society_code as society_code,
    2 AS ord
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON bh.society_code = mb.society_code
        AND bh.code = bi.bill_head_code
WHERE mb.member_code=CASE WHEN p_member_code =0 THEN mb.member_code ELSE p_member_code END 
AND mb.society_payment_cycle_code=p_society_payment_cycle_code
 AND  bh.code  = 101 

UNION ALL 

SELECT 
    RIGHT(mb.member_code,4) as member_code,
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
   IFNULL(mb.net_amount,0) AS  amount,
   mb.society_code as society_code,
    5 AS ord
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON bh.society_code = mb.society_code
        AND bh.code = bi.bill_head_code
WHERE mb.member_code=CASE WHEN p_member_code =0 THEN mb.member_code ELSE p_member_code END  AND mb.society_payment_cycle_code=p_society_payment_cycle_code AND
    bh.code  = 105 
    
UNION ALL 
SELECT 
    RIGHT(mb.member_code,4) as member_code,
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
     IFNULL(bi.adjustment,bi.amount) AS amount,
     mb.society_code as society_code,
    4 AS ord
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON bh.society_code = mb.society_code
        AND bh.code = bi.bill_head_code
WHERE mb.member_code=CASE WHEN p_member_code =0 THEN mb.member_code ELSE p_member_code END  
AND mb.society_payment_cycle_code=p_society_payment_cycle_code AND
     bh.code  NOT IN (101,102, 105)  and bh.head_type=0
     
UNION ALL 
SELECT 
    RIGHT(mb.member_code,4) as member_code,
    CASE WHEN p_locale ='en' THEN bh.name ELSE  IFNULL(bh.name_local,bh.name)  END AS head_name,
    
     IFNULL(bi.adjustment,bi.amount) AS amount,
     mb.society_code as society_code,
    4 AS ord
FROM
    member_bill AS mb
        LEFT JOIN
    member_bill_transaction AS bi ON bi.member_bill_code = mb.code
        LEFT JOIN
    bill_head AS bh ON bh.society_code = mb.society_code
        AND bh.code = bi.bill_head_code
WHERE mb.member_code=CASE WHEN p_member_code =0 THEN mb.member_code ELSE p_member_code END  AND mb.society_payment_cycle_code=p_society_payment_cycle_code AND
     bh.code  NOT IN (101,102, 105)  and bh.head_type=1
     
 
UNION ALL
SELECT 
    RIGHT(ps.consumer_code,4) as member_code ,
    CASE WHEN p_locale ='en' THEN pp.name ELSE  IFNULL(pp.name_local,pp.name)  END AS head_name,
    
  ROUND((IFNULL(SUM(pst.amount), 0) + IFNULL(SUM(pst.tax_amount), 0) - IFNULL(SUM(pst.discount), 0)),
            2) AS amount,
            pst.society_code as society_code,
    3 AS ord
FROM
    product_sale ps
        LEFT JOIN
    product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
        
        LEFT JOIN
    products AS pp ON pp.code = pst.product_code
		LEFT JOIN  product_sale_installment as psi ON psi.society_payment_cycle_code=p_society_payment_cycle_code
        AND psi.member_code=p_member_code AND psi.member_code=ps.consumer_code
       WHERE ps.consumer_code=CASE WHEN p_member_code =0 THEN ps.consumer_code ELSE p_member_code END  
GROUP BY ps.consumer_code , pp.name ,pp.name_local,pst.society_code

UNION ALL SELECT 
    RIGHT(mb.member_code,4) as member_code,
    'Milk Qty' AS head_name,
    SUM(milk_qty) AS amount,
     mb.society_code as society_code,
    1 AS ord
FROM
    member_bill AS mb
    WHERE mb.member_code=CASE WHEN p_member_code =0 THEN mb.member_code ELSE p_member_code END 
    AND mb.society_payment_cycle_code=p_society_payment_cycle_code 
GROUP BY mb.member_code,mb.society_code
ORDER BY ord,member_code ) as A 

INNER JOIN society as ss ON ss.code=A.society_code
INNER JOIN UNIONs as us ON us.code=ss.union_code
INNER JOIN society_payment_cycles as spc ON spc.code=p_society_payment_cycle_code
LEFT JOIn members as m ON  RIGHT(m.code,4)=A.member_code;
  
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_wise_local_milk_sale`(IN p_society_code VARCHAR(50),IN p_from_date DATETIME, IN p_to_date DATETIME,IN p_report_type INT,IN p_locale varchar(50))
BEGIN

IF (p_report_type=1 )THEN
SELECT 
     
    DATE_FORMAT(lms.sale_date, '%d/%m/%Y') AS dates,
    DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
	DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
    CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS shift,
    
    SUM(lms.quantity) as quantity,
    CASE WHEN SUM(lms.quantity) = 0 THEN 0 ELSE ROUND(SUM(lms.amount)/SUM(lms.quantity),2) END as rate,
    SUM(lms.amount) as amount,
    CASE WHEN p_locale ='en' THEN mc.name ELSE  IFNULL(mc.name_local,mc.name)  END AS mc_name,
    
    CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS mt_name,
    
    CASE
        WHEN lms.payment_mode = 0 THEN 'Cash'
        WHEN lms.payment_mode = 1 THEN 'Credit'
        ELSE 'coupon'
    END AS payment_type,
    lms.society_code AS society_code,
    CASE WHEN p_locale ='en' THEN society.name ELSE  IFNULL(society.name_local,society.name)  END AS society_name,
    
     concat(unions.name,' - (',lms.union_code,')') as unions_name
FROM
    local_milk_sale lms
		LEFT JOIN
    milk_classes AS mc ON mc.code = lms.milk_class_code
        LEFT JOIN
    milk_types AS mt ON mt.code = lms.milk_type_code
        LEFT JOIN
    shifts AS s ON s.code = lms.shift_code
        LEFT JOIN society as society ON society.code=lms.society_code
        LEFT JOIN unions as unions ON unions.code=lms.union_code
        
    WHERE lms.sale_date BETWEEN p_from_date AND p_to_date
    AND lms.society_code=p_society_code
    GROUP BY   lms.sale_date ,s.name  ,mc.name  ,mt.name ,lms.payment_mode 
    ,society.short_name ,unions.name,lms.society_code,lms.union_code ,s.name_local,mc.name_local,mt.name_local
    ORDER BY  dates;

ELSE 

SELECT DATE_FORMAT(CAST(lms.sale_date as DATE), '%d/%m/%Y')  AS dates,
	   DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
	   DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
       '' AS shift,
       SUM(lms.quantity) as quantity,
       CASE WHEN SUM(lms.quantity) = 0 THEN 0 ELSE ROUND(SUM(lms.amount)/SUM(lms.quantity),2) END as rate,
       SUM(lms.amount) as amount,
       CASE WHEN p_locale ='en' THEN mc.name ELSE  IFNULL(mc.name_local,mc.name)  END AS mc_name,
		
		CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS mt_name,
       mt.name as mt_name,
    CASE
        WHEN lms.payment_mode = 0 THEN 'Cash'
        WHEN lms.payment_mode = 0 THEN 'Credit'
        ELSE 'coupon'
    END AS payment_type,
    lms.society_code AS society_code,
    CASE WHEN p_locale ='en' THEN society.name ELSE  IFNULL(society.name_local,society.name)  END AS society_name,
    
    concat(unions.name,' - (',lms.union_code,')') as unions_name
FROM
    local_milk_sale lms
        LEFT JOIN
    milk_classes AS mc ON mc.code = lms.milk_class_code
        LEFT JOIN
    milk_types AS mt ON mt.code = lms.milk_type_code
        LEFT JOIN
    shifts AS s ON s.code = lms.shift_code
        LEFT JOIN society as society ON society.code=lms.society_code
		LEFT JOIN unions as unions ON unions.code=lms.union_code
    WHERE lms.sale_date BETWEEN p_from_date AND p_to_date
    AND lms.society_code=p_society_code
    GROUP BY   DATE_FORMAT(CAST(lms.sale_date as DATE), '%d/%m/%Y'),society.name,lms.society_code,lms.payment_mode,  mc.name ,
       mt.name ,lms.payment_mode,society.name,unions.name,lms.society_code,lms.union_code,mc.name_local,mt.name_local
    ORDER BY  dates
;
END IF;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_wise_product_sale_detail`(IN p_society_code VARCHAR(50),IN p_member_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN p_locale VARCHAR(50))
BEGIN

SELECT 
    CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(p_to_date,'%d/%m/%Y')) as pc_date,
    RIGHT( ps.consumer_code,4) AS consumer_code,
    pst.amount AS amount,
    pst.discount AS discount,
    pst.tax_amount AS tax_amount,
    pst.net_amount AS net_amount,
    DATE_FORMAT(ps.invoice_date, '%d/%m/%Y') AS invoice_date,
    CASE WHEN p_locale ='en' THEN p.name ELSE  IFNULL(p.name_local,p.name)  END AS product_name,
    
    CASE
        WHEN ps.payment_mode = 1 THEN 'Credit'
        ELSE 'Cash'
    END AS payment_mode,
    CASE
        WHEN ps.consumer_type = 1 THEN 'Member'
        WHEN ps.consumer_type = 2 THEN 'nonmember'
        WHEN ps.consumer_type = 3 THEN 'vendor'
        WHEN ps.consumer_type = 4 THEN 'institute'
        WHEN ps.consumer_type = 5 THEN 'retailsale'
        WHEN ps.consumer_type = 6 THEN 'consumer'
        WHEN ps.consumer_type = 7 THEN 'other'
    END AS consumer_type,
    CASE
        WHEN
            (ps.consumer_type = 3
                OR ps.consumer_type = 4
                OR ps.consumer_type = 7
                OR ps.consumer_type = 6
                OR ps.consumer_type = 7)
        THEN
			CASE WHEN p_locale ='en' THEN c.name ELSE  IFNULL(p.name_local,c.name)  END
            
        ELSE 
        (CASE WHEN p_locale ='en' THEN
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,'')) ELSE  
			IFNULL(concat(member.first_name_local,+" ", member.middle_name_local," ",member.last_name_local),
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,''))) END)
        
    END AS consumer_name,
    pst.quantity AS quantity,
    pst.rate AS rate,
    CASE WHEN p_locale ='en' THEN u.short_name ELSE  IFNULL(u.name_local,u.short_name) END AS unit_short_name,
    
    pst.society_code AS society_name_code,
    CASE WHEN p_locale ='en' THEN ss.short_name ELSE  IFNULL(ss.short_name_local,ss.short_name) END AS society_name,
    
    concat(unions.name,' - (',pst.union_code,')') as unions_name_code
FROM
    product_sale AS ps
        INNER JOIN
    product_sale_transaction AS pst ON pst.invoice_no = ps.invoice_no
         
        LEFT JOIN
    members AS member ON ps.consumer_code = member.code
        LEFT JOIN
    customers AS c ON c.code = ps.consumer_code
        LEFT JOIN
    products AS p ON p.code = pst.product_code
        LEFT JOIN
    product_groups AS pss ON pss.code = p.product_group_code
        LEFT JOIN
    units AS u ON u.code = pss.base_unit
        LEFT JOIN
    society AS ss ON ss.code = pst.society_code
        LEFT JOIN
    unions AS unions ON unions.code = pst.union_code
    WHERE pst.society_code=p_society_code
    AND ps.consumer_code =CASE WHEN p_member_code = 0 THEN ps.consumer_code ELSE p_member_code END
    AND ps.invoice_date BETWEEN p_from_date AND p_to_date
    ORDER BY ps.consumer_code ;
    END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_consolidate`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_locale VARCHAR(20))
BEGIN

 SELECT  CONCAT(date_format(p_from_date,'%d/%m/%Y'),(CASE WHEN cast(p_from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END),' To ',date_format(p_to_date,'%d/%m/%Y'),(CASE WHEN cast(p_to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)) as dates,
    mc.society_code,
    CASE
        WHEN p_locale = 'en' THEN s.name
        ELSE IFNULL(s.name_local, s.name)
    END AS society_name,
    m.code_ex AS member_code,
    CASE
        WHEN
            p_locale = 'en'
        THEN
            CONCAT(IFNULL(m.first_name, ''),
                    + ' ',
                    IFNULL(m.middle_name, ''),
                    + ' ',
                    IFNULL(m.last_name, ''))
        ELSE IFNULL(CONCAT(m.first_name_local,
                        + ' ',
                        m.middle_name_local,
                        ' ',
                        m.last_name_local),
                CONCAT(IFNULL(m.first_name, ''),
                        + ' ',
                        IFNULL(m.middle_name, ''),
                        + ' ',
                        IFNULL(m.last_name, '')))
    END AS member_name,
    ROUND(SUM(mc.qty),2) as qty,
       CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(fat, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS fat,
      CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(snf, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS snf,
    ROUND(SUM(mc.amount)/SUM(mc.qty),2) as rate,
    ROUND(SUM(mc.amount),2) as amount,
    ROUND(SUM(qty*fat)/100,2) as kg_fat,
    ROUND(SUM(qty*snf)/100,2) as kg_snf
FROM
    milk_collection mc
        INNER JOIN
    society s ON mc.society_code = s.code
        INNER JOIN
    members m ON mc.member_code = m.code
        INNER JOIN
    shifts st ON mc.shift_code = st.code
 WHERE
     mc.collection_date   BETWEEN p_from_date and p_to_date
      AND mc.society_code = p_society_code 
       AND mc.member_code = CASE WHEN  p_member_code=0 THEN mc.member_code ELSE p_member_code END 
GROUP BY   mc.society_code,m.code_ex,s.name_local, s.name,m.first_name,m.middle_name,m.last_name,m.first_name_local, m.middle_name_local, m.last_name_local
ORDER BY  society_code , member_code ;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_milk_type_wise_consolidate`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_milk_type_code INT,IN p_locale VARCHAR(20))
BEGIN

 SELECT  CONCAT(date_format(p_from_date,'%d/%m/%Y'),(CASE WHEN cast(p_from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END),' To ',date_format(p_to_date,'%d/%m/%Y'),(CASE WHEN cast(p_to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)) as dates,
    mc.society_code,
    CASE
        WHEN p_locale = 'en' THEN s.name
        ELSE IFNULL(s.name_local, s.name)
    END AS society_name,
    m.code_ex AS member_code,
    CASE
        WHEN
            p_locale = 'en'
        THEN
            CONCAT(IFNULL(m.first_name, ''),
                    + ' ',
                    IFNULL(m.middle_name, ''),
                    + ' ',
                    IFNULL(m.last_name, ''))
        ELSE IFNULL(CONCAT(m.first_name_local,
                        + ' ',
                        m.middle_name_local,
                        ' ',
                        m.last_name_local),
                CONCAT(IFNULL(m.first_name, ''),
                        + ' ',
                        IFNULL(m.middle_name, ''),
                        + ' ',
                        IFNULL(m.last_name, '')))
    END AS member_name,
   CASE
        WHEN p_locale = 'en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name) END  as animal_type_name,
    ROUND(SUM(mc.qty),2) as qty,
       CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(fat, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS fat,
      CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(snf, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS snf,
    ROUND(SUM(mc.amount)/SUM(mc.qty),2) as rate,
    ROUND(SUM(mc.amount),2) as amount,
	ROUND(SUM(qty*fat)/100,2) as kg_fat,
    ROUND(SUM(qty*snf)/100,2) as kg_snf
FROM
    milk_collection mc
        INNER JOIN
    society s ON mc.society_code = s.code
        INNER JOIN
    members m ON mc.member_code = m.code
        INNER JOIN
    shifts st ON mc.shift_code = st.code
    INNER JOIN
    milk_types mt ON mc.milk_type_code = mt.code
 WHERE
  mc.collection_date  BETWEEN p_from_date AND p_to_date
        AND mc.society_code = p_society_code 
        AND mc.member_code = CASE WHEN  p_member_code=0 THEN mc.member_code ELSE p_member_code END 
GROUP BY mt.name_local,mt.name, mt.name,mc.society_code,s.name_local, s.name, m.code_ex,m.first_name,m.last_name,m.middle_name,m.first_name_local, m.middle_name_local,m.last_name_local
ORDER BY  society_code , member_code ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_consolidate_summary`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime )
BEGIN
-- call eipl_amcs_db.rpt_milk_collection_consolidate_summary('1041123', '0', '2021-04-25 06:00:00', '2022-12-28 18:00:00' );

SELECT   IFNULL(SUM(CASE WHEN mc.milk_type_code=1 THEN  (qty)  END),0)  as cow_qty,
		 IFNULL(SUM(CASE WHEN mc.milk_type_code=2 THEN  (qty)   END),0)  as buaff_qty,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=3 THEN  (qty)   END),0)  as mix_qty,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=4 THEN  (qty)   END),0)  as a2_cow_qty,
		 
		 IFNULL(SUM(CASE WHEN mc.milk_type_code=1 THEN ROUND((amount),2) ELSE 0 END),0) as cow_amount,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=2 THEN ROUND((amount),2)ELSE 0 END),0) as buff_amount,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=3 THEN ROUND((amount),2) ELSE 0 END),0) as mix_amount ,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=4 THEN ROUND((amount),2) ELSE 0 END),0) as a2_cow_amount ,
         
         IFNULL(COUNT(DISTINCT (CASE WHEN mc.milk_type_code=1 THEN mc.member_code ELSE 0 END))-1,0)    as cow_memnber_count,
		 IFNULL(COUNT(DISTINCT (CASE WHEN mc.milk_type_code=2 THEN mc.member_code ELSE 0 END))-1 ,0)as buaff_memnber_count,
		 IFNULL(COUNT(DISTINCT(CASE WHEN mc.milk_type_code=3 THEN mc.member_code ELSE 0 END))-1,0)  as mix_memnber_count,
		 IFNULL(COUNT(DISTINCT(CASE WHEN mc.milk_type_code=4 THEN mc.member_code ELSE 0 END))-1,0)  as a2_cow_memnber_count,
         IFNULL(c_fat,0) as c_fat,
		 IFNULL(b_fat,0) as b_fat,
		 IFNULL(m_fat,0) as m_fat,
		 IFNULL(a_fat,0) as a_fat,
         IFNULL(c_snf,0) as c_snf,
		 IFNULL(b_snf,0) as b_snf,
		 IFNULL(m_snf,0) as m_snf,
		 IFNULL(a_snf,0) as a_snf
         
         
FROM
    milk_collection mc
    INNER JOIN milk_types as mt on mt.code=mc.milk_type_code
    LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as c_fat, ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2) as c_snf FROM milk_collection mc
              where  milk_type_code=1  AND  mc.collection_date  
                BETWEEN p_from_date and p_to_date AND milk_type_code=1  
                 AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
               -- AND mc.society_code=p_society_code
                ) as B ON 1=1
    LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as b_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as b_snf FROM milk_collection mc  
                where  milk_type_code=2   AND  mc.collection_date  
                 BETWEEN p_from_date and p_to_date AND milk_type_code=2  
                 AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END  
               -- AND mc.society_code=p_society_code 
               ) as C  ON 1=1        
     LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as m_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as m_snf FROM milk_collection mc 
                 where  milk_type_code=3 AND  mc.collection_date  
                  BETWEEN p_from_date and p_to_date AND milk_type_code=3
                  AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
                -- AND mc.society_code=p_society_code 
                ) as D    ON 1=1 
	LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as a_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as a_snf FROM milk_collection mc 
                 where  milk_type_code=4 AND  mc.collection_date  
                  BETWEEN p_from_date and p_to_date AND milk_type_code=4
                  AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
                -- AND mc.society_code=p_society_code 
                ) as E    ON 1=1           
 WHERE
     mc.collection_date   BETWEEN p_from_date and p_to_date
     AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
   --   AND mc.society_code=p_society_code
    group by  c_fat,
         b_fat,
         m_fat,
         a_fat,
         c_snf,
         b_snf,
         m_snf,
         a_snf
   ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_month_wise`(IN p_member_type INT,IN p_society_code VARCHAR(255),IN p_member_code varchar(255),IN p_from_date date,IN p_to_date DATE ,IN p_locale VARCHAR(20))
BEGIN
-- call rpt_milk_collection_month_wise(2,'1011619','10116190035',0,0,'en');
 IF (p_member_type=1) THEN
SELECT CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period, CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,
    YEAR(collection_date) AS years,
    CONCAT(MONTHNAME(collection_date),
            '-',
            YEAR(collection_date)) AS month_name,
            '' as dates,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
	-- (DATEDIFF(p_to_date, p_from_date)+1)*2 AS shift,
    COUNT(DISTINCT collection_date) AS shift,
    COUNT(DISTINCT collection_date) AS pouring_member ,
    CONCAT((CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END),' - ',m.code_ex) AS member_name,
            MONTH(collection_date)  as collection_dates
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
   LEFT JOIN members AS m ON mc.member_code=m.code
   WHERE mc.society_code= p_society_code
   AND CAST(collection_date as date) BETWEEN p_from_date AND p_to_date
   AND   mc.member_code=CASE WHEN p_member_code =0 THEN mc.member_code ELSE p_member_code END 
GROUP BY YEAR(collection_date),MONTHNAME(collection_date),m.first_name_local, m.middle_name_local,m.last_name_local,s.name,s.code_ex,m.first_name,m.middle_name,m.last_name,m.code_ex,s.short_name_local,MONTH(collection_date), CONCAT(MONTHNAME(collection_date),
            '-',
            YEAR(collection_date))
ORDER BY Years,collection_dates;
 ELSE IF (p_member_type=2) THEn
 SELECT  CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period ,CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,
    YEAR(collection_date) AS years,
    CONCAT(MONTHNAME(collection_date),
            '-',
            YEAR(collection_date)) AS month_name,
            '' as dates,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
    COUNT(DISTINCT collection_date) AS shift,
    COUNT(DISTINCT collection_date) AS pouring_member  ,
    'ALL' as member_name,MONTH(collection_date) as collection_dates
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
   LEFT JOIN members AS m ON mc.member_code=m.code
   WHERE mc.society_code= p_society_code
  AND CAST(collection_date as date) BETWEEN p_from_date AND p_to_date
   AND   mc.member_code=CASE WHEN p_member_code =0 THEN mc.member_code ELSE p_member_code END 
GROUP BY   YEAR(collection_date),MONTHNAME(collection_date) ,s.name,s.code_ex ,s.short_name_local,MONTH(collection_date), CONCAT(MONTHNAME(collection_date),
            '-',
            YEAR(collection_date))
ORDER BY Years,collection_dates;
END IF;
 
END IF;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_quarter_wise`(IN p_member_type INT,IN p_society_code VARCHAR(255),IN p_member_code varchar(255),IN p_from_date date ,IN p_to_date date,IN p_locale VARCHAR(20))
BEGIN
IF(p_member_type=1) THEN 
SELECT  CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period,CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,YEAR(collection_date) AS years,
    1 AS month_name,
    CASE
        WHEN
            Month(collection_date) >= 1 and  Month(collection_date)  <= 4
        THEN
            CONCAT('01-01-',
                    YEAR(collection_date),
                    ' To ',
                    '30-04-',
                    YEAR(collection_date))
        WHEN
                Month(collection_date) >= 5 and  Month(collection_date)  <= 8
        THEN
            CONCAT('01-05-',
                    YEAR(collection_date),
                    ' To ',
                    '31-08-',
                    YEAR(collection_date))
        WHEN
                Month(collection_date) >= 9 and  Month(collection_date)  <= 12
        THEN
            CONCAT('01-09-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date))
        /* WHEN
            QUARTER(collection_date) = 4
        THEN
            CONCAT('01-10-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date)) */
    END AS dates,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
    COUNT(DISTINCT collection_date) AS shift,
     COUNT(DISTINCT collection_date) AS pouring_member,
        CONCAT((CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END),' - ',m.code_ex) AS member_name
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
    LEFT JOIN members AS m ON mc.member_code=m.code
   WHERE mc.society_code= p_society_code
   AND cast(collection_date as date) BETWEEN p_from_date and p_to_date
 --  AND   QUARTER(collection_date)=CASE WHEN p_quarter =0 THEN QUARTER(collection_date) ELSE p_quarter END 
  -- AND   YEAR(collection_date)=CASE WHEN p_year =0 THEN YEAR(collection_date) ELSE p_year END 
   AND   mc.member_code=CASE WHEN p_member_code =0 THEN mc.member_code ELSE p_member_code END 
GROUP BY 
--   Month(collection_date),
YEAR(collection_date),m.first_name_local, m.middle_name_local,m.last_name_local,s.name,s.code_ex,s.short_name_local,s.name,s.code_ex,m.first_name,m.middle_name,m.last_name,m.code_ex, 
CASE
        WHEN
              Month(collection_date) >= 1 and  Month(collection_date)  <= 4
        THEN
            CONCAT('01-01-',
                    YEAR(collection_date),
                    ' To ',
                    '30-04-',
                    YEAR(collection_date))
        WHEN
              Month(collection_date) >= 5 and  Month(collection_date)  <= 8
        THEN
            CONCAT('01-05-',
                    YEAR(collection_date),
                    ' To ',
                    '31-08-',
                    YEAR(collection_date))
        WHEN
               Month(collection_date) >= 9 and  Month(collection_date)  <= 12
        THEN
            CONCAT('01-09-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date))
        /* WHEN
            QUARTER(collection_date) = 4
        THEN
            CONCAT('01-10-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date)) */
    
    END
ORDER BY Years;
ELSE IF (p_member_type=2) THEN
SELECT  CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period,CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,YEAR(collection_date) AS years,
    1 AS month_name,
   CASE
        WHEN
               Month(collection_date) >= 1 and  Month(collection_date)  <= 4
        THEN
            CONCAT('01-01-',
                    YEAR(collection_date),
                    ' To ',
                    '30-04-',
                    YEAR(collection_date))
        WHEN
              Month(collection_date) >= 5 and  Month(collection_date)  <= 8
        THEN
            CONCAT('01-05-',
                    YEAR(collection_date),
                    ' To ',
                    '31-08-',
                    YEAR(collection_date))
        WHEN
               Month(collection_date) >= 9 and  Month(collection_date)  <= 12
        THEN
            CONCAT('01-09-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date))
        /* WHEN
            QUARTER(collection_date) = 4
        THEN
            CONCAT('01-10-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date)) */
    END AS dates,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
    COUNT(DISTINCT collection_date) AS shift,
	COUNT(DISTINCT collection_date) AS pouring_member,
     'ALL' as member_name
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
   WHERE mc.society_code= p_society_code
   AND cast(collection_date as date) BETWEEN p_from_date and p_to_date
  -- AND   QUARTER(collection_date)=CASE WHEN p_quarter =0 THEN QUARTER(collection_date) ELSE p_quarter END 
   -- AND   YEAR(collection_date)=CASE WHEN p_year =0 THEN YEAR(collection_date) ELSE p_year END 
GROUP BY   
-- Month(collection_date),
YEAR(collection_date),s.name,s.code_ex,s.short_name_local , CASE
        WHEN
              Month(collection_date) >= 1 and  Month(collection_date)  <= 4
        THEN
            CONCAT('01-01-',
                    YEAR(collection_date),
                    ' To ',
                    '30-04-',
                    YEAR(collection_date))
        WHEN
              Month(collection_date) >= 5 and  Month(collection_date)  <= 8
        THEN
            CONCAT('01-05-',
                    YEAR(collection_date),
                    ' To ',
                    '31-08-',
                    YEAR(collection_date))
        WHEN
               Month(collection_date) >= 9 and  Month(collection_date)  <= 12
        THEN
            CONCAT('01-09-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date))
        /* WHEN
            QUARTER(collection_date) = 4
        THEN
            CONCAT('01-10-',
                    YEAR(collection_date),
                    ' To ',
                    '31-12-',
                    YEAR(collection_date)) */
    END 
ORDER BY Years;
END IF; 
END IF; 
END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_product_sale_detail_member_wise1`(IN p_society_code VARCHAR(50),IN p_product_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN p_locale VARCHAR(50))
BEGIN

SELECT 
    CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(p_to_date,'%d/%m/%Y')) as pc_date,
    sum(pst.net_amount) AS net_amount,
    CASE WHEN p_locale ='en' THEN p.name ELSE  IFNULL(p.name_local,p.name)  END AS product_name,

    sum(pst.quantity) AS quantity,
    avg(pst.rate) AS rate,    
    pst.society_code AS society_name_code,
    CASE WHEN p_locale ='en' THEN ss.short_name ELSE  IFNULL(ss.short_name_local,ss.short_name) END AS society_name
    
FROM
    product_sale AS ps
        INNER JOIN
    product_sale_transaction AS pst ON pst.invoice_no = ps.invoice_no
         
        LEFT JOIN
    products AS p ON p.code = pst.product_code
        LEFT JOIN
    product_groups AS pss ON pss.code = p.product_group_code
        LEFT JOIN
    units AS u ON u.code = pss.base_unit
        LEFT JOIN
    society AS ss ON ss.code = pst.society_code
        LEFT JOIN
    unions AS unions ON unions.code = pst.union_code
    WHERE pst.society_code=p_society_code
    AND ps.invoice_date BETWEEN p_from_date AND p_to_date
    And case when p_product_code=0 then p.code=p.code else p.code=p_product_code end
    group by p.code;
    END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_year_wise`(IN p_member_type INT,IN p_society_code VARCHAR(255),IN p_member_code VARCHAR(255),IN p_from_date DATE,IN p_to_date DATE ,IN p_locale VARCHAR(20))
BEGIN
-- call rpt_milk_collection_year_wise(1,'1011619','10116190035',0,'en');
  IF (p_member_type=1) THEN
SELECT  CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period,CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,
    YEAR(collection_date) AS years,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
    COUNT(DISTINCT collection_date) AS shift,
    COUNT(DISTINCT collection_date) AS pouring_member,
     CONCAT((CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END),' - ',m.code_ex) AS member_name
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
    LEFT JOIN members AS m ON mc.member_code=m.code
   WHERE mc.society_code= p_society_code
   AND   mc.member_code=CASE WHEN p_member_code =0 THEN mc.member_code ELSE p_member_code END 
  -- AND   YEAR(collection_date)=CASE WHEN p_year =0 THEN YEAR(collection_date) ELSE p_year END 
  AND CAST(collection_date as DATE) BETWEEN p_from_date AND p_to_date
GROUP BY   YEAR(collection_date),m.first_name,m.middle_name,m.last_name,s.name,s.code_ex,s.short_name_local,m.first_name_local, m.middle_name_local,m.last_name_local,m.code_ex
ORDER BY Years;
ELSE IF (p_member_type=2) THEN
SELECT CONCAT(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y'))  as period,CONCAT((CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.short_name_local,s.name) END ),' - ',s.code_ex) as society_name,
    YEAR(collection_date) AS years,
    SUM(qty) AS qty,
    SUM(amount) AS amount,
    COUNT(DISTINCT collection_date) AS shift,
    COUNT(DISTINCT collection_date) AS pouring_member,
    'ALL' as member_name
FROM
    milk_collection as mc
    INNER JOIN society as s ON s.code=mc.society_code
   WHERE mc.society_code= p_society_code
     -- AND   YEAR(collection_date)=CASE WHEN p_year =0 THEN YEAR(collection_date) ELSE p_year END 
  AND CAST(collection_date as DATE) BETWEEN p_from_date AND p_to_date
GROUP BY   YEAR(collection_date),s.name,s.code_ex,s.short_name_local
ORDER BY Years;
 END IF;
 END IF; 
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collecton_dispatch_receipt_loss`(IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_milk_type INT,IN p_locale VARCHAR(20))
BEGIN
 -- call eipl_amcs_db.rpt_milk_collecton_dispatch_receipt_loss( '2022-11-22 18:00', '2022-11-23 06:00', 0, 'en');
	
   SELECT  
          CONCAT(DATE_FORMAT(p_from_date, '%d/%m/%y'),
            '-',
            CASE
                WHEN SUBSTRING(p_from_date, 12) = '06:00:00' THEN 'M'
                ELSE 'E'
            END,
            ' To ',
            CONCAT(DATE_FORMAT(p_to_date, '%d/%m/%y'),
                    '-',
                    CASE
                        WHEN SUBSTRING(p_to_date, 12) = '06:00:00' THEN 'M'
                        ELSE 'E'
                    END)) AS period, CONCAT(DATE_FORMAT(A.from_date, '%d/%m/%y'),
            '-',
            CASE
                WHEN SUBSTRING(A.from_date, 12) = '06:00:00' THEN 'M'
                ELSE 'E'
            END,
            ' To ',
            CONCAT(DATE_FORMAT(A.to_date, '%d/%m/%y'),
                    '-',
                    CASE
                        WHEN SUBSTRING(A.to_date, 12) = '06:00:00' THEN 'M'
                        ELSE 'E'
                    END)) AS to_date,  
    IFNULL(ROUND(SUM(mc_qty), 2),0) AS mc_qty,
    IFNULL(ROUND(SUM(mc_amount), 2),0) AS mc_amount,
    IFNULL(ROUND(SUM(d_qty),2),0) as d_qty,
    IFNULL(ROUND(SUM(d_amount),2),0)as d_amount,
	IFNULL(ROUND(SUM(r_qty),2),0) as r_qty,
    IFNULL(ROUND(SUM(r_amount),2),0) as r_amount,
	IFNULL(ROUND(SUM(r_converted_quantity),2),0) as r_converted_quantity ,
    mc.name as dispatch_milk_type,
	IFNULL(ROUND(IFNULL(SUM(r_qty), 0) - SUM(mc_qty), 2),0) AS diff_qty ,
	IFNULL(SUM(r_amount), 0) - 	IFNULL(SUM(mc_amount), 0) AS diff_amount,
    IFNULL(CAST(((IFNULL(SUM(r_amount), 0) - SUM(mc_amount)) / SUM(mc_amount)) * 100
        AS DECIMAL (18 , 2 )), 0) AS percentage,
    ROUND(IFNULL(SUM(r_converted_quantity), 0) - IFNULL(SUM(mc_qty), 0),
            2) AS diff_qty_kg,
     /* IFNULL(ROUND((ROUND(IFNULL(SUM(0), 0) - IFNULL(SUM(r_qty), 0),
                    2)) * 100 / SUM(mc_qty),
            2), 0)  
           
            AS liter,*/
            0 as liter,
           IFNULL(   CONCAT(ROUND(SUM(d_fat)/SUM(d_qty)*100, 2),
            ' - ',
            ROUND(IFNULL(SUM(r_fat)/SUM(r_qty)*100, 0), 2)),0) AS di_fat,
             ROUND(IFNULL(SUM(liter_amount), 0), 2) AS liter_amount,
    ROUND(IFNULL(SUM(liter_qty), 0), 2) AS liter_qty
	FROM 
(SELECT md.from_date,md.to_date,SUM(mc.qty) mc_qty,SUM(mc.amount) mc_amount,0 as d_qty,0 as liter_qty,0 as r_qty,0 AS d_amount,0 AS d_fat,0 as liter_amount,0 as r_converted_quantity,0 as r_amount,0 as r_fat, mc.milk_type_code FROM milk_dispatch as md
INNER JOIN milk_collection mc ON mc.collection_date between md.from_date and md.to_date
WHERE md.from_date >= p_from_date and md.to_date <=p_to_date
GROUP BY md.from_date,md.to_date ,mc.milk_type_code
UNION ALL
 SELECT md.from_date,md.to_date,0 mc_qty,SUM(mc.qty) as d_qty,0 mc_amount,0 as liter_qty,0 as r_qty,SUM(mc.amount) AS d_amount,IFNULL((SUM(avg_fat * qty / 100) ),0) AS d_fat,0 as liter_amount,0 as r_converted_quantity,0 as r_amount,0 as r_fat, mc.milk_type_code FROM milk_dispatch as md
INNER JOIN milk_dispatch_transaction mc ON mc.challan_no =md.challan_no
WHERE md.from_date >=p_from_date and md.to_date <=p_to_date
GROUP BY md.from_date,md.to_date ,mc.milk_type_code 
UNION ALL
 SELECT md.from_date,md.to_date,0 mc_qty,0 mc_amount,0 as d_qty,SUM(quantity) as liter_qty,0 as r_qty,0 AS d_amount,0 AS d_fat,SUM(amount) AS liter_amount ,0 as r_converted_quantity,0 as r_amount,0 as r_fat, mc.milk_type_code FROM milk_dispatch as md
INNER JOIN local_milk_sale mc ON mc.sale_date BETWEEN md.from_Date AND md.to_date
WHERE md.from_date >= p_from_date and md.to_date <=p_to_date
GROUP BY md.from_date,md.to_date ,mc.milk_type_code 
UNION ALL
 SELECT md.from_date,md.to_date,0 mc_qty,0 mc_amount,0 as d_qty,0 as liter_qty,SUM(qty) as r_qty,0 AS d_amount,0 AS d_fat,0 as liter_amount, SUM(converted_quantity) AS r_converted_quantity,
    SUM(amount) AS r_amount,
    IFNULL((SUM(avg_fat * qty / 100)),0) AS r_fat, mc.milk_type_code FROM milk_receipt as md
INNER JOIN milk_receipt_transaction mc ON mc.milk_receipt_code=md.code
WHERE md.from_date >= p_from_date and md.to_date <=p_to_date

GROUP BY md.from_date,md.to_date ,mc.milk_type_code 
) as A
INNER JOIN milk_types mc ON mc.code=A.milk_type_code
WHERE   A.milk_type_code = CASE WHEN p_milk_type=0 THEN A.milk_type_code ELSE p_milk_type END 
group by from_date,to_date,milk_type_code;

 
END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_dispatch_challan`(IN p_society_code varchar(225), IN p_challan_no varchar(225))
BEGIN
	SELECT 
		md.society_code,
		s.name AS society_name, 
		md.destination_code AS destination,
		md.challan_no,
		md.created_at AS challan_date,
		mqtyp.name AS milk_quality_type_name, 
		mt.name AS milk_type_name,
		md.dispatch_type,
		md.from_date, 
		fs.name AS from_shift, 
		md.to_date, 
        md.destination_type As destination_type,
		ts.name AS to_shift, 
		IFNULL((mdt.nos_of_can),0) AS nos_of_can,
		round(IFNULL((mdt.qty),0),2) AS dispatch_qty,
		IFNULL((mdt.avg_fat),0) AS avg_fat, 
		IFNULL((mdt.avg_snf),0) AS avg_snf, 
		IFNULL((mdt.avg_clr),0) AS avg_clr, 
		round(IFNULL(round(mdt.amount,2)/round(mdt.qty,2),0),2) AS rate, 
		IFNULL((mdt.amount),0) AS amount,
		IFNULL((mdt.lactose),0) AS lactose, 
		IFNULL((mdt.water),0) AS water, 
		IFNULL((mdt.protein),0) AS protein,
		IFNULL((mdt.temp),0) AS temp,
		IFNULL((mdt.freezing_point),0) AS freezing_point, 
		IFNULL((mdt.density),0) AS density, 
		IFNULL((mdt.acidity),0) AS acidity,
		md.route_no,
		r.name AS route_name,
		md.vehicle_no,
		md.vehicle_in_time,
		md.vehicle_out_time, 
		mdt.chamber_no
	FROM milk_dispatch md
		INNER JOIN milk_dispatch_transaction mdt ON md.challan_no = mdt.challan_no
		INNER JOIN society s ON md.society_code = s.code
		INNER JOIN routes r ON md.route_no = r.code
		INNER JOIN milk_quality_types mqtyp ON  mdt.milk_quality_type_code = mqtyp.code
		INNER JOIN milk_types mt ON mdt.milk_type_code = mt.code
		INNER JOIN shifts fs ON md.from_shift_code = fs.code
		INNER JOIN shifts ts ON md.to_shift_code = ts.code
	WHERE md.society_code = p_society_code AND md.challan_no = p_challan_no;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_local_sale_dispatch_month_wise`(IN p_society_code VARCHAR(20),IN p_from_date DATETIME,IN p_to_date DATETIME ,IN p_locale VARCHAR(2),IN p_animal_type_code INT)
BEGIN



SELECT CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%y'),'-',CASE WHEN substring(p_from_date,12) ='06:00:00' THEN 'M' ELSE 'E' END) as from_Date,
CONCAT(DATE_FORMAT(p_to_date,'%d/%m/%y'),'-',CASE WHEN substring(p_to_date,12) ='06:00:00' THEN 'M' ELSE 'E' END ) as to_date,
(SELECT  CONCAT(CASE WHEN p_locale='en' THEN name ELSE IFNULL(short_name_local,name) END ,'-(',code,')') FROM society where code=p_society_code) as dcs_name,
CASE WHEN p_animal_type_code=0 THEN 'ALL' ELSE (SELECT   (CASE WHEN p_locale='en' THEN name ELSE IFNULL(name_local,name) END ) FROM milk_types WHERE code =  p_animal_type_code  ) END as milk_type_name,
month_name,SUM(m_qty) as m_qty,SUM(m_amount) as m_amount,SUM(l_qty) as l_qty,SUM(l_amount) as l_amount,SUM(d_qty) as d_qty,SUM(d_amount) as d_amount,SUM(count_member) as count_member  FROM 
(SELECT 
    CONCAT(MONTHNAME(collection_date),
            '-',
            YEAR(collection_date)) AS month_name,
    SUM(qty) AS m_qty,
    SUM(amount) AS m_amount,
    COUNT(DISTINCT member_code) as count_member,
    0 AS l_qty,
    0 AS l_amount,
    0 AS d_qty,
    0 AS d_amount,
    MONTH(collection_date) AS c_date,
    YEAR(collection_date) y_date
FROM
    milk_collection AS mc
        INNER JOIN
    society AS s ON s.code = mc.society_code
   WHERE mc.milk_type_code = CASE WHEN  p_animal_type_code= 0 THEN mc.milk_type_code ELSE p_animal_type_code END 
   AND mc.collection_date BETWEEN p_from_date AND p_to_date
GROUP BY CONCAT(MONTHNAME(collection_date),
        '-',
        YEAR(collection_date)),mc.collection_date ,MONTH(collection_date),YEAR(collection_date)
UNION ALL SELECT 
    CONCAT(MONTHNAME(sale_date),
            '-',
            YEAR(sale_date)) AS month_name,
    0 AS m_qty,
    0 AS m_amount,
    0 as count_member,
   
    SUM(quantity) AS l_qty,
    SUM(amount) AS l_amount,
    0 AS d_qty,
    0 AS d_amount,
    MONTH(sale_date) AS c_date,
    YEAR(sale_date) y_date
FROM
    local_milk_sale AS mc
    WHERE mc.milk_type_code = CASE WHEN  p_animal_type_code= 0 THEN mc.milk_type_code ELSE p_animal_type_code END 
     AND mc.sale_date BETWEEN p_from_date AND p_to_date
GROUP BY CONCAT(MONTHNAME(sale_date),'-', YEAR(sale_date)),MONTH(sale_date),sale_date,YEAR(sale_date)
UNION ALL SELECT 
    CONCAT(MONTHNAME(from_date),
            '-',
            YEAR(from_date)) AS month_name,
    0 AS m_qty,
    0 AS m_amount,
    0 as count_member,
    0 AS l_qty,
    0 AS l_amount,
    SUM(qty) AS d_qty,
    SUM(amount) AS d_amount,
    MONTH(from_date) AS c_date,
    YEAR(from_date) y_date
FROM
    milk_dispatch AS mc
        INNER JOIN
    milk_dispatch_transaction AS mdt ON mdt.challan_no = mc.challan_no
     WHERE mdt.milk_type_code = CASE WHEN  p_animal_type_code= 0 THEN mdt.milk_type_code ELSE p_animal_type_code END 
       AND mc.from_date >= p_from_date AND mc.to_date <= p_to_date
GROUP BY CONCAT(MONTHNAME(from_date),'-', YEAR(from_date)),from_date,YEAR(from_date)
 
 ) as A 
 GROUP BY month_name,y_date,c_date
 ORDER BY y_date,c_date
 ;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_patrak_one`(IN p_society_code VARCHAR(12),IN p_from_date DATETIME ,IN p_to_date DATETIME,IN p_milk_type_code INT,IN p_locale varchar(20))
BEGIN
 
select
 CASE WHEN p_locale='en' THEN concat(name,'-(',code_ex,')')ELSE concat(IFNULL(name_local,name),'-(',code_ex,')') END  INTO @p_society_name from society where code=p_society_code ;

  SELECT  CASE WHEN p_milk_type_code=0 THEN 'ALL' ELSE (SELECT   (CASE WHEN p_locale='en' THEN name ELSE IFNULL(name_local,name) END ) FROM milk_types WHERE code =  p_milk_type_code  ) END as milk_type_name, 
@p_society_name  as society_name, CONCAT(date_format(p_from_date,'%d/%m/%y'), ' ' ,CASE WHEN substring(p_from_date,12) ='18:00:00' THEN 'Evening' ELSE 'Morning' END,' To ' ,date_format(p_to_date,'%d/%m/%y'),' ' ,CASE WHEN substring(p_to_date,12) ='18:00:00' THEN 'Evening' ELSE 'Morning' END)as display ,concat(date_format(p_from_date,'%d/%m/%y'),' To ',date_format(p_to_date,'%d/%m/%y')) dates,
    IFNULL(SUM(mc_qty),0) AS mc_qty,
     IFNULL(SUM(mc_amount),0) AS mc_amount,
     IFNULL(SUM(ls_qty),0) AS ls_qty,
     IFNULL(SUM(ls_amount),0) AS ls_amount,
    CASE WHEN SUM(mc_qty) = 0 THEN 0  ELSE ROUND((SUM(ls_qty) / SUM(mc_qty)) * 100,2) END  AS liter_per,
    SUM(good) AS good,
    SUM(Sour) AS Sour,
    SUM(Curd) AS Curd,
     CASE WHEN (SUM(Sour) = 0 OR SUM(Curd)=0)  THEN 0  ELSE ROUND((SUM(mc_qty)/SUM(Curd)/SUM(Sour)) /100,2) END  AS curd_milk_agains_purchase,
    SUM(dispatch_amount) AS receipt_amount,
    SUM(ls_amount) + SUM(dispatch_amount) AS total_local_disaptch,
    SUM(ls_amount) + SUM(dispatch_amount)- SUM(mc_amount) AS milk_sale_profit_milk_seles_price,
    ROUND(CASE WHEn SUM(mc_amount)=0 THEN 0 ELSE (SUM(ls_amount) + SUM(dispatch_amount)- SUM(mc_amount))/SUM(mc_amount)*100 END,2)  AS percentage_of_profit,
      -- SUM(dispatch_qty) - (SUM(good)+SUM(Sour) +SUM(Curd) ) as percentage_dispatch_qty_receipt,
      concat((SUM(good)+SUM(Sour) +SUM(Curd) - SUM(dispatch_qty) )," (",round(((((SUM(good)+SUM(Sour) +SUM(Curd) )*100)/sum(dispatch_qty))-100),2),"%)")  as percentage_dispatch_qty_receipt
         -- "-" + ((SUM(good)+SUM(Sour) +SUM(Curd) )*100)/sum(dispatch_qty)
FROM
    (SELECT 
        SUM(qty) AS mc_qty,
            SUM(amount) AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
            0 AS good,
            0 AS Sour,
            0 AS Curd,
            0 AS dispatch_amount,  0 as dispatch_qty
    FROM
        milk_collection WHERE  (collection_date) BETWEEN p_from_date AND p_to_date and milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END   UNION ALL SELECT 
        0 AS mc_qty,
            0 AS mc_amount,
            SUM(quantity) AS ls_qty,
            SUM(amount) AS ls_amount,
            0 AS good,
            0 AS Sour,
            0 AS Curd,
            0 AS dispatch_amount,  0 as dispatch_qty
    FROM
        local_milk_sale WHERE  (sale_date ) BETWEEN p_from_date AND p_to_date and 
        milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END UNION ALL  SELECT 0 AS mc_qty,0 AS mc_amount,0 AS ls_qty,0 AS ls_amount,SUM(good) as good,SUM(Sour) as Sour,SUM(Curd) as Curd,0 AS dispatch_amount,  0 as dispatch_qty  FROM 
 (SELECT 
            0 AS mc_qty,
            0 AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
			SUM(converted_quantity)  AS good,
            0 Sour,
            0 Curd,
            0 AS dispatch_amount,  0 as dispatch_qty
    FROM
        milk_receipt AS mr
    INNER JOIN milk_receipt_transaction AS mrt ON mrt.milk_receipt_code = mr.code
    WHERE mrt.milk_quality_type_code = 1  AND  mr.from_date  >=p_from_date AND  mr.to_date  <= p_to_date
    and   milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END 
 UNION ALL
 
  SELECT 
            0 AS mc_qty,
            0 AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
			0  AS good,
            SUM(converted_quantity) AS Sour,
            0 Curd,
            0 AS dispatch_amount,  0 as dispatch_qty
    FROM
        milk_receipt AS mr
    INNER JOIN milk_receipt_transaction AS mrt ON mrt.milk_receipt_code = mr.code
      WHERE mrt.milk_quality_type_code = 2 AND  mr.from_date   >=p_from_date AND  mr.to_date  <= p_to_date
     and   milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END 
    UNION ALL
    
     SELECT 
             0 AS mc_qty,
            0 AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
			0  AS good,
            0 Sour,
            SUM(converted_quantity) as Curd,
            0 AS dispatch_amount,  0 as dispatch_qty
    FROM
        milk_receipt AS mr
    INNER JOIN milk_receipt_transaction AS mrt ON mrt.milk_receipt_code = mr.code
    WHERE mrt.milk_quality_type_code = 3   and   milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END 
    AND  mr.from_date  >=p_from_date AND  mr.to_date  <= p_to_date ) as A UNION ALL SELECT 
            0 AS mc_qty,
            0 AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
            0 AS good,
            0 AS Sour,
            0 AS Curd,
		    SUM(amount) AS dispatch_amount,
            0 as dispatch_qty
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    WHERE    md.from_date  >= p_from_date  AND  md.to_date <=  p_to_date 
     and   milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END 
    UNION ALL
     SELECT 
            0 AS mc_qty,
            0 AS mc_amount,
            0 AS ls_qty,
            0 AS ls_amount,
            0 AS good,
            0 AS Sour,
            0 AS Curd,
		    0 AS dispatch_amount,
			SUM(converted_quantity) AS dispatch_qty
    FROM
        milk_dispatch AS md
    INNER JOIN milk_dispatch_transaction AS mdt ON mdt.challan_no = md.challan_no
      WHERE  md.from_date  >= p_from_date  AND  md.to_date  <= p_to_date 
       and   milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END 
    ) AS A;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_register`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_locale varchar(50))
BEGIN

	SELECT 
		mb.society_code,
		RIGHT(mb.member_code,4) AS member_code,
		mb.society_payment_cycle_code,
		mb.payment_mode,
		mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
		
		u.name AS union_name,
        concat(CONCAT(DATE_FORMAT(spc.from_date, '%d-%m-%Y'),(CASE WHEN cast(spc.from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)),' -To- ',
        CONCAT(DATE_FORMAT(spc.to_date, '%d-%m-%Y'),(CASE WHEN cast(spc.to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)))period,
		spc.from_date,
		spc.to_date,
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		
		mb.milk_qty,
		mb.milk_amount,
		mb.net_amount,
        case when mb.other_add_amount is null then 0 else mb.other_add_amount end AS other_add_amount,
        case when mb.other_ded_amount is null then 0 else mb.product_sale_amount+mb.local_sale_amount+mb.loan_amount +mb.other_ded_amount end AS other_ded_amount,
        case when b.name is null then "" else b.name end AS bank_name,
        case when br.name is null then "" else br.name end AS branch_name,
        case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno,
        case when mb.ifsc is null then "" else mb.ifsc end AS ifsc,
        case when mcc.cow_amount is null then 0 else mcc.cow_amount end AS cow_amount,
		case when mcc.a2_cow_amount is null then 0 else mcc.a2_cow_amount end AS a2_cow_amount,
		
        case when mcc.buffalo_amount is null then 0 else mcc.buffalo_amount end AS buffalo_amount,
		
        case when mcc.mix_amount is null then 0 else mcc.mix_amount end AS mix_amount,
		
        case when mcc.cow_qty is null then 0 else mcc.cow_qty end AS cow_qty,
        case when mcc.a2_cow_qty is null then 0 else mcc.a2_cow_qty end AS a2_cow_qty,
		
        case when mcc.buffalo_qty is null then 0 else mcc.buffalo_qty end AS buffalo_qty,
		
        case when mcc.mix_qty is null then 0 else mcc.mix_qty end AS mix_qty
		
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
		LEFT JOIN member_details AS md ON mb.member_code=md.code
		LEFT JOIN banks AS b ON md.bank_code=b.code
		LEFT JOIN branches br ON md.branch_code=br.code
        INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
						SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.amount ELSE 0 END) AS a2_cow_amount,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
						SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.qty ELSE 0 END) AS a2_cow_qty,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
						SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
					FROM society_payment_cycles pc
						INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
						INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
					WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
					GROUP BY pc.society_code, pc.code,mc.member_code
				) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
						AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
	WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
		AND mb.payment_mode = p_payment_mode
        order by m.code;		
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_registerr`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_locale varchar(50))
BEGIN

	SELECT 
		mb.society_code,
		RIGHT(mb.member_code,4) AS member_code,
		mb.society_payment_cycle_code,
		mb.payment_mode,
		mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
		
		u.name AS union_name,
		CONCAT(CONCAT(DATE_FORMAT(spc.from_date, '%d-%m-%Y'),(CASE WHEN cast(spc.from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)),'-To-',
        CONCAT(DATE_FORMAT(spc.to_date, '%d-%m-%Y'),(CASE WHEN cast(spc.to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)))period,
  --      concat(IFNULL((DATE_FORMAT(spc.from_date, '%d-%m-%Y')),''),+" To ",IFNULL((DATE_FORMAT(spc.to_date, '%d-%m-%Y')),''))  AS period,
		spc.from_date,
		spc.to_date,
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		
		mb.milk_qty,
		mb.milk_amount,
		mb.net_amount,
        case when mb.other_add_amount is null then 0 else mb.other_add_amount end AS other_add_amount,
        case when mb.other_ded_amount is null then 0 else mb.product_sale_amount+mb.local_sale_amount+mb.loan_amount +mb.other_ded_amount end AS other_ded_amount,
        case when b.name is null then "" else b.name end AS bank_name,
        case when br.name is null then "" else br.name end AS branch_name,
        case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno,
        case when mb.ifsc is null then "" else mb.ifsc end AS ifsc,
        case when mcc.cow_amount is null then 0 else mcc.cow_amount end AS cow_amount,
        case when mcc.a2_cow_amount is null then 0 else mcc.a2_cow_amount end AS a2_cow_amount,
		
        case when mcc.buffalo_amount is null then 0 else mcc.buffalo_amount end AS buffalo_amount,
		
        case when mcc.mix_amount is null then 0 else mcc.mix_amount end AS mix_amount,
		
        case when mcc.cow_qty is null then 0 else mcc.cow_qty end AS cow_qty,
        case when mcc.a2_cow_qty is null then 0 else mcc.a2_cow_qty end AS a2_cow_qty,
		
        case when mcc.buffalo_qty is null then 0 else mcc.buffalo_qty end AS buffalo_qty,
		
        case when mcc.mix_qty is null then 0 else mcc.mix_qty end AS mix_qty
		
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
		LEFT JOIN member_details AS md ON mb.member_code=md.code
		LEFT JOIN banks AS b ON md.bank_code=b.code
		LEFT JOIN branches br ON md.branch_code=br.code
        INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
						SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.amount ELSE 0 END) AS a2_cow_amount,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
						SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.qty ELSE 0 END) AS a2_cow_qty,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
						SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
					FROM society_payment_cycles pc
						INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
						INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
					WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
					GROUP BY pc.society_code, pc.code,mc.member_code
				) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
						AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
	WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
		AND mb.payment_mode = p_payment_mode
        order by m.code;		
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_register_bank`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_bank_code varchar(4),IN p_locale varchar(50))
BEGIN

    SELECT 
        mb.society_code,
        RIGHT(mb.member_code,4) AS member_code,
        mb.society_payment_cycle_code,
        mb.payment_mode,
        mb.union_code,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
        
        u.name AS union_name,
        CONCAT(CONCAT(DATE_FORMAT(spc.from_date, '%d-%m-%Y'),(CASE WHEN cast(spc.from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)),'-To-',
        CONCAT(DATE_FORMAT(spc.to_date, '%d-%m-%Y'),(CASE WHEN cast(spc.to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)))period,
  --      concat(IFNULL((DATE_FORMAT(spc.from_date, '%d-%m-%Y')),''),+" To ",IFNULL((DATE_FORMAT(spc.to_date, '%d-%m-%Y')),''))  AS period,
        spc.from_date,
        spc.to_date,
        CASE WHEN p_locale ='en' THEN
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
            IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        
        mb.milk_qty,
        mb.milk_amount,
        mb.net_amount,
        md.bank_code,
        case when mb.other_add_amount is null then 0 else mb.other_add_amount end AS other_add_amount,
        case when mb.other_ded_amount is null then 0 else mb.product_sale_amount+mb.local_sale_amount+mb.loan_amount +mb.other_ded_amount end AS other_ded_amount,
        CASE WHEN p_locale ='en' THEN IFNULL(b.name,'') ELSE  IFNULL(b.name_local,IFNULL(b.name,''))  END AS bank_name,
        
        CASE WHEN p_locale ='en' THEN IFNULL(br.name,'') ELSE  IFNULL(br.name_local,IFNULL(b.name,''))  END AS branch_name,
        
        case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno,
        case when mb.ifsc is null then "" else mb.ifsc end AS ifsc,
        case when mcc.cow_amount is null then 0 else mcc.cow_amount end AS cow_amount,
        case when mcc.a2_cow_amount is null then 0 else mcc.a2_cow_amount end AS a2_cow_amount,

        case when mcc.buffalo_amount is null then 0 else mcc.buffalo_amount end AS buffalo_amount,
        
        case when mcc.mix_amount is null then 0 else mcc.mix_amount end AS mix_amount,
        
        case when mcc.cow_qty is null then 0 else mcc.cow_qty end AS cow_qty,
        case when mcc.a2_cow_qty is null then 0 else mcc.a2_cow_qty end AS a2_cow_qty,

        case when mcc.buffalo_qty is null then 0 else mcc.buffalo_qty end AS buffalo_qty,
        
        case when mcc.mix_qty is null then 0 else mcc.mix_qty end AS mix_qty
        
    FROM member_bill AS mb
        LEFT JOIN society AS s ON mb.society_code=s.code
        LEFT JOIN unions AS u ON mb.union_code=u.code
        LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
        LEFT JOIN members AS m ON mb.member_code=m.code
        LEFT JOIN member_details AS md ON mb.member_code=md.code
        LEFT JOIN banks AS b ON md.bank_code=b.code
        LEFT JOIN branches br ON md.branch_code=br.code
        INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
                        SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
                        SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.amount ELSE 0 END) AS a2_cow_amount,
                        SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
                        SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
                        SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.qty ELSE 0 END) AS a2_cow_qty,
                        SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
                    FROM society_payment_cycles pc
                        INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
                        INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
                    WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
                    GROUP BY pc.society_code, pc.code,mc.member_code
                ) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
                        AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
    WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
          AND mb.payment_mode = p_payment_mode 
        AND md.bank_code=CASE WHEN p_bank_code = 0 THEN md.bank_code  ELSE p_bank_code END;        
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_register_bank_2`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_bank_code varchar(4),IN p_locale varchar(50))
BEGIN

SET @row_number = 0;
	SELECT 
	 (@row_number:=@row_number + 1)  AS sr_no,
		RIGHT(mb.member_code,4) AS member_code,
	 
	  
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		 
 	cast(mb.net_amount as decimal(18,2)) as net_amount,
 
         
        case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno 
       
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
		LEFT JOIN member_details AS md ON mb.member_code=md.code
		LEFT JOIN banks AS b ON md.bank_code=b.code
		LEFT JOIN branches br ON md.branch_code=br.code
        INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
						SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
					FROM society_payment_cycles pc
						INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
						INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
					WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
					GROUP BY pc.society_code, pc.code,mc.member_code
				) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
						AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
	WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
		AND mb.payment_mode = p_payment_mode 
        AND md.bank_code=CASE WHEN p_bank_code = 0 THEN md.bank_code  ELSE p_bank_code END;		
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_register_bank_excel`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_bank_code varchar(4),IN p_locale varchar(50))
BEGIN

SET @row_number = 0;
	SELECT 
	 (@row_number:=@row_number + 1)  AS sr_no,
		RIGHT(mb.member_code,4) AS member_code,
	 
	  
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
		 
 	mb.net_amount as net_amount,
 	mb.milk_amount as milk_amount,
 	mb.other_ded_amount as other_ded_amount,
 
         
        case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno, 
		case when mb.ifsc is null then "" else mb.ifsc end AS ifsc
	FROM member_bill AS mb
		LEFT JOIN society AS s ON mb.society_code=s.code
		LEFT JOIN unions AS u ON mb.union_code=u.code
		LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
		LEFT JOIN members AS m ON mb.member_code=m.code
		LEFT JOIN member_details AS md ON mb.member_code=md.code
		LEFT JOIN banks AS b ON md.bank_code=b.code
		LEFT JOIN branches br ON md.branch_code=br.code
        INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                        SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
						SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
						SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
						SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
					FROM society_payment_cycles pc
						INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
						INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
					WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
					GROUP BY pc.society_code, pc.code,mc.member_code
				) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
						AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
	WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
		AND mb.payment_mode = p_payment_mode 
        AND md.bank_code=CASE WHEN p_bank_code = 0 THEN md.bank_code  ELSE p_bank_code END;		
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_payment_register_milk_type_wise`(IN p_society_code varchar(12),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_locale varchar(50),IN p_milk_type INT)
BEGIN

SELECT 
    mb.society_code,
    RIGHT(mb.member_code,4) AS member_code,
    mb.society_payment_cycle_code,
    mb.payment_mode,
    mb.union_code,
    CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
    
    u.name AS union_name,
    concat(IFNULL((DATE_FORMAT(spc.from_date, '%d-%m-%Y')),''),+" To ",IFNULL((DATE_FORMAT(spc.to_date, '%d-%m-%Y')),''))  AS period,
    spc.from_date,
    spc.to_date,
    CASE WHEN p_locale ='en' THEN
        concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
        IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
        concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
    
    mb.milk_qty,
    mb.milk_amount,
    mb.net_amount,
    case when mb.other_add_amount is null then 0 else mb.other_add_amount end AS other_add_amount,
    case when mb.other_ded_amount is null then 0 else mb.product_sale_amount+mb.local_sale_amount+mb.loan_amount +mb.other_ded_amount end AS other_ded_amount,
    case when b.name is null then "" else b.name end AS bank_name,
    case when br.name is null then "" else br.name end AS branch_name,
    case when mb.bank_acno is null then "" else mb.bank_acno end AS bank_acno,
    case when mb.ifsc is null then "" else mb.ifsc end AS ifsc,
    case when mcc.cow_amount is null then 0 else mcc.cow_amount end AS cow_amount,
    case when mcc.a2_cow_amount is null then 0 else mcc.a2_cow_amount end AS a2_cow_amount,
    
    case when mcc.buffalo_amount is null then 0 else mcc.buffalo_amount end AS buffalo_amount,
    
    case when mcc.mix_amount is null then 0 else mcc.mix_amount end AS mix_amount,
    
    case when mcc.cow_qty is null then 0 else mcc.cow_qty end AS cow_qty,
    case when mcc.a2_cow_qty is null then 0 else mcc.a2_cow_qty end AS a2_cow_qty,
    
    case when mcc.buffalo_qty is null then 0 else mcc.buffalo_qty end AS buffalo_qty,
    
    case when mcc.mix_qty is null then 0 else mcc.mix_qty end AS mix_qty
    
FROM member_bill AS mb
    LEFT JOIN society AS s ON mb.society_code=s.code
    LEFT JOIN unions AS u ON mb.union_code=u.code
    LEFT JOIN society_payment_cycles AS spc ON mb.society_payment_cycle_code=spc.code
    LEFT JOIN members AS m ON mb.member_code=m.code
    LEFT JOIN member_details AS md ON mb.member_code=md.code
    LEFT JOIN banks AS b ON md.bank_code=b.code
    LEFT JOIN branches br ON md.branch_code=br.code
    INNER JOIN (SELECT pc.society_code, pc.code AS society_payment_cycle_code,mc.member_code,
                    SUM(CASE WHEN mt.name = 'Cow' THEN mc.amount ELSE 0 END) AS cow_amount,
                    SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.amount ELSE 0 END) AS a2_cow_amount,
                    SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.amount ELSE 0 END) AS buffalo_amount,
                    SUM(CASE WHEN mt.name = 'Mix' THEN mc.amount ELSE 0 END) AS mix_amount,
                    SUM(CASE WHEN mt.name = 'Cow' THEN mc.qty ELSE 0 END) AS cow_qty,
                    SUM(CASE WHEN mt.name = 'A2 Cow' THEN mc.qty ELSE 0 END) AS a2_cow_qty,
                    SUM(CASE WHEN mt.name = 'Buffalo' THEN mc.qty ELSE 0 END) AS buffalo_qty,
                    SUM(CASE WHEN mt.name = 'Mix' THEN mc.qty ELSE 0 END) AS mix_qty
                FROM society_payment_cycles pc
                    INNER JOIN milk_collection mc ON pc.society_code = mc.society_code AND mc.collection_date >= pc.from_date AND mc.collection_date <= pc.to_date 
                    INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
                WHERE pc.society_code = p_society_code AND pc.code = p_society_payment_cycle_code
                AND mc.milk_type_code=CASE WHEN p_milk_type=0 THEN mc.milk_type_code ELSE p_milk_type END
                GROUP BY pc.society_code, pc.code,mc.member_code
            ) mcc ON mb.society_payment_cycle_code = mcc.society_payment_cycle_code 
                    AND mb.society_code = mcc.society_code AND mb.member_code = mcc.member_code
WHERE mb.society_code = p_society_code AND mb.society_payment_cycle_code = p_society_payment_cycle_code
 
    order by m.code;        
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_product_sale_detail_member_wise`(IN p_society_code VARCHAR(50),IN p_product_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN p_locale VARCHAR(50))
BEGIN

SELECT 
    CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(p_to_date,'%d/%m/%Y')) as pc_date,
    RIGHT( ps.consumer_code,4) AS consumer_code,
    pst.amount AS amount,
    pst.discount AS discount,
    pst.tax_amount AS tax_amount,
    pst.net_amount AS net_amount,
    DATE_FORMAT(ps.invoice_date, '%d/%m/%Y') AS invoice_date,
    CASE WHEN p_locale ='en' THEN p.name ELSE  IFNULL(p.name_local,p.name)  END AS product_name,
    
    CASE
        WHEN ps.payment_mode = 1 THEN 'Credit'
        ELSE 'Cash'
    END AS payment_mode,
    CASE
        WHEN ps.consumer_type = 1 THEN 'Member'
        WHEN ps.consumer_type = 2 THEN 'nonmember'
        WHEN ps.consumer_type = 3 THEN 'vendor'
        WHEN ps.consumer_type = 4 THEN 'institute'
        WHEN ps.consumer_type = 5 THEN 'retailsale'
        WHEN ps.consumer_type = 6 THEN 'consumer'
        WHEN ps.consumer_type = 7 THEN 'other'
    END AS consumer_type,
    CASE
        WHEN
            (ps.consumer_type = 3
                OR ps.consumer_type = 4
                OR ps.consumer_type = 7
                OR ps.consumer_type = 6
                OR ps.consumer_type = 7)
        THEN
			CASE WHEN p_locale ='en' THEN c.name ELSE  IFNULL(p.name_local,c.name)  END
            
        ELSE 
        (CASE WHEN p_locale ='en' THEN
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,'')) ELSE  
			IFNULL(concat(member.first_name_local,+" ", member.middle_name_local," ",member.last_name_local),
			concat(IFNULL(member.first_name,''),+" ",IFNULL(member.middle_name,''), +" ",IFNULL(member.last_name,''))) END)
        
    END AS consumer_name,
    pst.quantity AS quantity,
    pst.rate AS rate,
    CASE WHEN p_locale ='en' THEN u.short_name ELSE  IFNULL(u.name_local,u.short_name) END AS unit_short_name,
    
    pst.society_code AS society_name_code,
    CASE WHEN p_locale ='en' THEN ss.short_name ELSE  IFNULL(ss.short_name_local,ss.short_name) END AS society_name,
    
    concat(unions.name,' - (',pst.union_code,')') as unions_name_code
FROM
    product_sale AS ps
        INNER JOIN
    product_sale_transaction AS pst ON pst.invoice_no = ps.invoice_no
         
        LEFT JOIN
    members AS member ON ps.consumer_code = member.code
        LEFT JOIN
    customers AS c ON c.code = ps.consumer_code
        LEFT JOIN
    products AS p ON p.code = pst.product_code
        LEFT JOIN
    product_groups AS pss ON pss.code = p.product_group_code
        LEFT JOIN
    units AS u ON u.code = pss.base_unit
        LEFT JOIN
    society AS ss ON ss.code = pst.society_code
        LEFT JOIN
    unions AS unions ON unions.code = pst.union_code
    WHERE pst.society_code=p_society_code
    AND pst.product_code =CASE WHEN p_product_code = 0 THEN pst.product_code ELSE p_product_code END
    AND ps.invoice_date BETWEEN p_from_date AND p_to_date
    ORDER BY ps.consumer_code ;
    END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_product_sale_invoice`(IN p_society_code varchar(12), IN p_invoice_no varchar(35))
BEGIN

	SELECT 
		ps.society_code,
		society.name As society_name,
		ps.union_code,
		u.name As union_name,
		ps.invoice_no,
		ps.invoice_date,
		p.reference_code,
		p.name As product_name,
		ps.consumer_code,
		ps.consumer_type,
		society.village_code,
		v.name As village_name,
        CASE WHEN  (ps.consumer_type = 3 OR ps.consumer_type = 4 OR ps.consumer_type = 5) THEN  c.name ELSE
        concat(IFNULL(mb.first_name,''),+" ",IFNULL(mb.middle_name,''), +" ",IFNULL(mb.last_name,'')) END  AS consumer_name,
		Round(IFNULL(pst.quantity,0),2) AS quantity,
		Round(IFNULL(pst.rate,0),2) AS rate,
		Round((IFNULL(pst.quantity,0) * IFNULL(pst.rate,0)),2) AS amount,
		Round(IFNULL(pst.tax_amount,0),2) AS tax_amount,
		Round(IFNULL(pst.discount,0),2) AS discount,
		Round((IFNULL(pst.amount,0) + IFNULL(pst.tax_amount,0) - IFNULL(pst.discount,0)),2) AS netamount
	FROM product_sale ps
		LEFT JOIN  product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
		LEFT JOIN society society ON ps.society_code = society.code
		LEFT JOIN products p ON pst.product_code = p.code
		LEFT JOIN members mb ON ps.consumer_code = mb.code
		LEFT JOIN customers c ON ps.consumer_code = c.code
		LEFT JOIN unions u ON ps.union_code = u.code
		LEFT JOIN villages v ON society.village_code = v.code
	WHERE ps.invoice_no = p_invoice_no AND ps.society_code = p_society_code
	GROUP BY ps.society_code,society.name,ps.invoice_no,ps.invoice_date,p.reference_code,p.name, ps.consumer_code,society.village_code,v.name,
             ps.consumer_type,mb.middle_name,mb.first_name,mb.last_name,c.code,u.code,quantity,rate,amount,tax_amount,discount,netamount,ps.union_code,u.name;

END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_share_transfer`(IN p_society_code Varchar(12),IN p_from_date DATE,IN p_to_date DATE ,IN p_locale varchar(50))
BEGIN
 SELECT
p_from_date as from_date,
p_to_date as to_date,
 s.share_code as new_member_code,
 s.transferred_from_code as old_member_code,
 so.name as society_name,
 so.code as society_code,
CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as new_member_name, 
 
CASE WHEN p_locale ='en' THEN
    concat(IFNULL(mm.first_name,''),+" ",IFNULL(mm.middle_name,''), +" ",IFNULL(mm.last_name,'')) ELSE
    IFNULL(concat(mm.first_name_local,+" ", mm.middle_name_local," ",mm.last_name_local),
    concat(IFNULL(mm.first_name,''),+" ",IFNULL(mm.middle_name,''), +" ",IFNULL(mm.last_name,''))) END as   old_member_name,

 s.no_of_share as no_of_share,s.share_amount as share_amount
 from share s join members m on s.member_code=m.code 
 join society so on s.society_code=so.code
join members mm on s.transferred_from_share_code = mm.code
 where s.society_code=p_society_code and
 s.transfer_date between p_from_date and p_to_date
 ;
 END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_shift_code_wise`(IN p_society_code varchar(12), IN p_collection_date datetime,IN p_dock_no varchar(15),IN p_locale VARCHAR(2))
BEGIN
 
  SELECT 
        1 AS sr,
        mc.society_code,
        mc.dock_no,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
        CONCAT(CONCAT(CASE WHEN mc.updated_at IS NOT NULL THEN '#' ELSE '' END),'', m.code_ex)   AS member_code,
        CASE WHEN p_locale ='en' THEN
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
            IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
            concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        CAST(mc.collection_date AS DATE) AS collection_date,
        CASE WHEN p_locale ='en' THEN st.name ELSE  IFNULL(st.name_local,st.name)  END AS shift_name,
        CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS milk_type_name,
        mc.sample_no,
		CASE WHEN (is_quality_auto = 0) THEN '*' ELSE '' END   AS  qtyauto,
		CASE WHEN (is_weight_auto = 0) THEN '*' ELSE '' END  AS  qltyauto,
		CASE WHEN (is_weight_auto = 0) THEN '*' ELSE '' END  AS  snfauto,
        mc.qty,
        mc.fat,
        mc.snf,
        mc.amount,
        0 AS cow_member_count,
        0 AS buffalo_member_count,
        0 AS mix_member_count,
        0 AS a2_cow_member_count,
        0 AS cow_qty,
        0 AS buffalo_qty,
        0 AS mix_qty,
        0 AS a2_cow_qty,
        0 AS cow_amount,
        0 AS buffalo_amount,
        0 AS mix_amount,
        0 AS a2_cow_amount,
        0 AS local_cow_qty,
        0 AS local_buffalo_qty,
        0 AS local_mix_qty,
        0 AS local_a2_cow_qty,
        0 AS local_cow_amount,
        0 AS local_buffalo_amount,
        0 AS local_mix_amount,
        0 AS local_a2_cow_amount,
        0 AS cow_avg_fat,
        0 AS buff_avg_fat,
        0 AS mix_avg_fat,
        0 AS a2_cow_avg_fat,
        IFNULL(edit,0) as edit,
         IFNULL(auto,0)   as auto
    FROM milk_collection mc
        INNER JOIN society s ON mc.society_code = s.code
        INNER JOIN members m ON mc.member_code = m.code
        INNER JOIN shifts st ON mc.shift_code = st.code
        INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
        LEFT JOIN (SELECT COUNT(*) as edit,collection_date,dock_no,society_code,member_code  FROM milk_collection mc  WHERE  mc.collection_date = p_collection_date AND mc.society_code = p_society_code AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end AND  updated_at IS NOT NULL GROUP BY collection_date,dock_no,society_code,member_code ) as A ON A.collection_date=mc.collection_date   AND A.member_code=mc.member_code
        LEFT JOIN (SELECT COUNT(*) as auto,collection_date,dock_no,society_code,member_code  FROM milk_collection mc  WHERE mc.collection_date = p_collection_date AND mc.society_code = p_society_code AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end AND  (is_quality_auto = 0 OR  is_weight_auto = 0) GROUP BY collection_date,dock_no,society_code,member_code ) as B ON B.collection_date=mc.collection_date AND B.society_code=mc.society_code AND B.dock_no=mc.dock_no    AND B.member_code=mc.member_code
    WHERE mc.collection_date = p_collection_date AND mc.society_code = p_society_code
        AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end

    UNION ALL
    SELECT
        2 AS sr,
        mc.society_code,
        mc.dock_no,
		s.name AS society_name,
        COUNT(DISTINCT member_code) AS member_code,
        '' as member_name,
        CAST(collection_date AS DATE) AS collection_date,
        st.name AS shift_name,
        '' AS milk_type_name,
        COUNT(DISTINCT sample_no) AS sample_no,
        0 as qtyauto,
        0 as qltyauto,
        0 as snfauto,
        IFNULL(SUM(qty),0) AS qty,
        CASE WHEN IFNULL(SUM(qty),0) = 0 THEN 0 ELSE ROUND(IFNULL(SUM(mc.fat*mc.qty/100)/SUM(qty)*100,0),1) END AS fat,
        CASE WHEN IFNULL(SUM(qty),0) = 0 THEN 0 ELSE ROUND(IFNULL(SUM(mc.snf*mc.qty/100)/SUM(qty)*100,0),1) END AS snf,
        IFNULL(SUM(amount),0) AS amount,
        COUNT(DISTINCT CASE WHEN mt.name = 'Cow' THEN sample_no END) AS cow_member_count,
        COUNT(DISTINCT CASE WHEN mt.name = 'Buffalo' THEN sample_no END) AS buffalo_member_count,
        COUNT(DISTINCT CASE WHEN mt.name = 'Mix' THEN sample_no END) AS mix_member_count,
        COUNT(DISTINCT CASE WHEN mt.name = 'A2 Cow' THEN sample_no END) AS a2_cow_member_count,
        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN qty ELSE 0 END),0) AS cow_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN qty ELSE 0 END),0) AS buffalo_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN qty ELSE 0 END),0) AS mix_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'A2 cow' THEN qty ELSE 0 END),0) AS a2_cow_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN amount ELSE 0 END),0)AS cow_amount,
        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN amount ELSE 0 END),0) AS buffalo_amount,
        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN amount ELSE 0 END),0) AS mix_amount,
        IFNULL(SUM(CASE WHEN mt.name = 'A2 Cow' THEN amount ELSE 0 END),0) AS a2_cow_amount,
        IFNULL(local_cow_qty,0) AS local_cow_qty,
        IFNULL(local_buffalo_qty,0) AS local_buffalo_qty,
        IFNULL(local_mix_qty,0) AS local_mix_qty,
        IFNULL(local_a2_cow_qty,0) AS local_a2_cow_qty,
        IFNULL(local_cow_amount,0)AS local_cow_amount,
        IFNULL(local_buffalo_amount,0) AS local_buffalo_amount,
        IFNULL(local_mix_amount,0) AS local_mix_amount,
        IFNULL(local_a2_cow_amount,0) AS local_a2_cow_amount,
		IFNULL(cow_avg_fat,0) AS cow_avg_fat,
        IFNULL(buff_avg_fat,0) AS buff_avg_fat,
        IFNULL(mix_avg_fat,0) AS mix_avg_fat,
        IFNULL(a2_cow_avg_fat,0) AS a2_cow_avg_fat,
        0 as edit,
        0 as auto
    FROM milk_collection mc
        INNER JOIN society s ON mc.society_code = s.code
        INNER JOIN shifts st ON mc.shift_code = st.code
        INNER JOIN members m ON mc.member_code = m.code
        INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
        LEFT JOIN (SELECT lms.society_code,lms.sale_date, lms.shift_code,
                        
                        
                        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN lms.quantity ELSE 0 END),0) AS local_cow_qty,
                        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN lms.quantity ELSE 0 END),0) AS local_buffalo_qty,
                        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN lms.quantity ELSE 0 END),0) AS local_mix_qty,
                        IFNULL(SUM(CASE WHEN mt.name = 'A2 Cow' THEN lms.quantity ELSE 0 END),0) AS local_a2_cow_qty,
                        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN lms.amount ELSE 0 END),0)AS local_cow_amount,
                        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN lms.amount ELSE 0 END),0) AS local_buffalo_amount,
                        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN lms.amount ELSE 0 END),0) AS local_mix_amount,
                        IFNULL(SUM(CASE WHEN mt.name = 'A2 Cow' THEN lms.amount ELSE 0 END),0) AS local_a2_cow_amount
                    FROM local_milk_sale lms
                        INNER JOIN milk_types mt ON lms.milk_type_code = mt.code
                    WHERE lms.sale_date = p_collection_date AND lms.society_code = p_society_code GROUP BY lms.society_code,lms.sale_date, lms.shift_code)
                    lms ON CAST(mc.collection_date AS DATE) = CAST(lms.sale_date AS DATE) AND mc.shift_code = lms.shift_code AND 
                    mc.society_code = lms.society_code
                    
				LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as cow_avg_fat ,mc.collection_date as collection_dates FROM milk_collection mc
              where  milk_type_code=1  AND  mc.collection_date  =  p_collection_date AND mc.society_code = p_society_code 
              group by mc.collection_date
               ) mcc ON mcc.collection_dates = p_collection_date                
			LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as buff_avg_fat ,mc.collection_date  as collection_dates FROM milk_collection mc
              where  milk_type_code=2  AND  mc.collection_date  =  p_collection_date   AND mc.society_code = p_society_code
              group by  mc.collection_date
               ) mcb ON mcb.collection_dates = p_collection_date                
			LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as mix_avg_fat ,mc.collection_date as collection_dates FROM milk_collection mc
              where  milk_type_code=3  AND  mc.collection_date  =  p_collection_date  AND mc.society_code = p_society_code 
              group by mc.collection_date
               ) mcm ON mcm.collection_dates = p_collection_date
		  LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as a2_cow_avg_fat ,mc.collection_date as collection_dates FROM milk_collection mc
              where  milk_type_code=4  AND  mc.collection_date  =  p_collection_date  AND mc.society_code = p_society_code 
              group by mc.collection_date
               ) mca2 ON mca2.collection_dates = p_collection_date
                    
                    
                    AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end
    WHERE mc.collection_date = p_collection_date AND mc.society_code = p_society_code
    AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end 
    GROUP BY mc.society_code,s.name,mc.dock_no,CAST(mc.collection_date AS DATE),st.name,local_cow_qty,local_buffalo_qty,local_mix_qty,local_a2_cow_qty,local_cow_amount,local_buffalo_amount,    
        local_mix_amount,local_a2_cow_amount,mcc.cow_avg_fat,mcb.buff_avg_fat,mcm.mix_avg_fat,mca2.a2_cow_avg_fat
       
    ORDER BY dock_no,sr,society_code,collection_date,milk_type_name;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_shift_name_wise`(IN p_society_code varchar(12), IN p_collection_date datetime,IN p_dock_no varchar(15),IN p_locale VARCHAR(2))
BEGIN
  
  SELECT 
		1 AS sr,
		mc.society_code,
        mc.dock_no,
        CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
        m.code_ex AS member_code,
        CASE WHEN p_locale ='en' THEN
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
			IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
			concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name,
        CAST(mc.collection_date AS DATE) AS collection_date,
        CASE WHEN p_locale ='en' THEN st.name ELSE  IFNULL(st.name_local,st.name)  END AS shift_name,
        CASE WHEN p_locale ='en' THEN mt.name ELSE  IFNULL(mt.name_local,mt.name)  END AS milk_type_name,
         mc.sample_no,
		CASE WHEN (is_quality_auto = 0) THEN '*' ELSE '' END   AS  qtyauto,
		CASE WHEN (is_weight_auto = 0) THEN '*' ELSE '' END  AS  qltyauto,
		CASE WHEN (is_weight_auto = 0) THEN '*' ELSE '' END  AS  snfauto,
        mc.qty,
        mc.fat,
        mc.snf,
        mc.amount,
		0 AS cow_member_count,
        0 AS buffalo_member_count,
        0 AS mix_member_count,
        0 AS cow_qty,
        0 AS buffalo_qty,
        0 AS mix_qty,
        0 AS cow_amount,
        0 AS buffalo_amount,
        0 AS mix_amount,
        0 AS local_cow_qty,
		0 AS local_buffalo_qty,
		0 AS local_mix_qty,
		0 AS local_cow_amount,
		0 AS local_buffalo_amount,
		0 AS local_mix_amount
	FROM milk_collection mc
		INNER JOIN society s ON mc.society_code = s.code
        INNER JOIN members m ON mc.member_code = m.code
        INNER JOIN shifts st ON mc.shift_code = st.code
        INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
	WHERE mc.collection_date = p_collection_date AND mc.society_code = p_society_code
		AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end 
    UNION ALL
    SELECT 
		2 AS sr,
		mc.society_code,
        mc.dock_no,
		s.name AS society_name,
        COUNT(DISTINCT member_code) AS member_code,
        'Parth' as member_name,
        CAST(collection_date AS DATE) AS collection_date,
        st.name AS shift_name,
        '' AS milk_type_name,
        COUNT(DISTINCT sample_no) AS sample_no,
		0 as qtyauto,
        0 as qltyauto,
        0 as snfauto,
        IFNULL(SUM(qty),0) AS qty,
        CASE WHEN IFNULL(SUM(qty),0) = 0 THEN 0 ELSE ROUND(IFNULL(SUM(mc.fat*mc.qty/100)/SUM(qty)*100,0),1) END AS fat,
        CASE WHEN IFNULL(SUM(qty),0) = 0 THEN 0 ELSE ROUND(IFNULL(SUM(mc.snf*mc.qty/100)/SUM(qty)*100,0),1) END AS snf,
        IFNULL(SUM(amount),0) AS amount,
        COUNT(DISTINCT CASE WHEN mt.name = 'Cow' THEN sample_no END) AS cow_member_count,
        COUNT(DISTINCT CASE WHEN mt.name = 'Buffalo' THEN sample_no END) AS buffalo_member_count,
        COUNT(DISTINCT CASE WHEN mt.name = 'Mix' THEN sample_no END) AS mix_member_count,
        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN qty ELSE 0 END),0) AS cow_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN qty ELSE 0 END),0) AS buffalo_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN qty ELSE 0 END),0) AS mix_qty,
        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN amount ELSE 0 END),0)AS cow_amount,
        IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN amount ELSE 0 END),0) AS buffalo_amount,
        IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN amount ELSE 0 END),0) AS mix_amount,
        IFNULL(local_cow_qty,0) AS local_cow_qty,
		IFNULL(local_buffalo_qty,0) AS local_buffalo_qty,
		IFNULL(local_mix_qty,0) AS local_mix_qty,
		IFNULL(local_cow_amount,0)AS local_cow_amount,
		IFNULL(local_buffalo_amount,0) AS local_buffalo_amount,
		IFNULL(local_mix_amount,0) AS local_mix_amount
	FROM milk_collection mc
		INNER JOIN society s ON mc.society_code = s.code
		INNER JOIN shifts st ON mc.shift_code = st.code
        INNER JOIN members m ON mc.member_code = m.code
        INNER JOIN milk_types mt ON mc.milk_type_code = mt.code
		LEFT JOIN (SELECT lms.society_code,lms.sale_date, lms.shift_code,
						
                        
                        IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN lms.quantity ELSE 0 END),0) AS local_cow_qty,
						IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN lms.quantity ELSE 0 END),0) AS local_buffalo_qty,
						IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN lms.quantity ELSE 0 END),0) AS local_mix_qty,
						IFNULL(SUM(CASE WHEN mt.name = 'Cow' THEN lms.amount ELSE 0 END),0)AS local_cow_amount,
						IFNULL(SUM(CASE WHEN mt.name = 'Buffalo' THEN lms.amount ELSE 0 END),0) AS local_buffalo_amount,
						IFNULL(SUM(CASE WHEN mt.name = 'Mix' THEN lms.amount ELSE 0 END),0) AS local_mix_amount
					FROM local_milk_sale lms
						INNER JOIN milk_types mt ON lms.milk_type_code = mt.code
					WHERE lms.sale_date = p_collection_date AND lms.society_code = p_society_code GROUP BY lms.society_code,lms.sale_date, lms.shift_code) lms ON CAST(mc.collection_date AS DATE) = CAST(lms.sale_date AS DATE) AND mc.shift_code = lms.shift_code AND mc.society_code = lms.society_code
                    AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end
    WHERE mc.collection_date = p_collection_date AND mc.society_code = p_society_code
    AND mc.dock_no = CASE WHEN p_dock_no = 0 then mc.dock_no ELSE p_dock_no end 
    GROUP BY mc.society_code,s.name,mc.dock_no,CAST(collection_date AS DATE),st.name,local_cow_qty,local_buffalo_qty,local_mix_qty,local_cow_amount,local_buffalo_amount,	
		  local_mix_amount 
    ORDER BY dock_no,sr,society_code,member_code,collection_date,milk_type_name;

END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_society_purchase`(IN p_from_collection_date DATETIME,IN p_to_collection_date DATETIME,IN p_society_code VARCHAR(12),IN p_locale varchar(50),IN p_milk_type_code int)
BEGIN
    SELECT A.society_code,
    A.milk_type_code,
    A.collection_date,
    CASE WHEN p_locale ='en' THEN m.name ELSE  IFNULL(m.name_local,m.name)  END AS animal_name,
    CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
concat(SUBSTRING(DATE_FORMAT(collection_date,'%d/%m/%Y'),1,5),CASE when SUBSTRING(collection_date,12)='06:00:00'  then ' - M ' else ' - E ' END)   AS period,
IFNULL(round(sum(A.mc_qty),3),0) AS mc_qty, 
IFNULL(round(sum(A.mc_fat),2),0) AS mc_fat, 
IFNULL(round(sum(A.mc_snf),2),0) AS mc_snf, 
IFNULL(round(sum(A.mc_amount),2),0) AS mc_amount,
IFNULL(round(sum(A.md_qty),3),0) AS ms_qty, 
IFNULL(round(sum(A.md_amount),2),0) AS ms_amount
FROM
(
SELECT 
    mc.society_code,
    mc.collection_date,
    mc.milk_type_code,
    round(sum(mc.qty),3) AS mc_qty,
    Round(round(sum(mc.qty * mc.fat / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_fat,
    Round(round(sum(mc.qty * mc.snf / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_snf,
    round(SUM(mc.amount),2) AS mc_amount,
    0 AS md_qty,
    0 AS md_amount
FROM  milk_collection mc 
WHERE mc.collection_date >= p_from_collection_date AND mc.collection_date <= p_to_collection_date AND mc.society_code = p_society_code and
  milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END
GROUP BY mc.society_code,mc.collection_date,mc.milk_type_code

UNION

SELECT 
    mc.society_code,
    mc.sale_date AS collection_date,
    mc.milk_type_code,
    0 AS mc_qty,
    0 AS mc_fat,
    0 AS mc_snf,
    0 AS mc_amount,
    round(sum(mc.quantity),3) AS md_qty,
    round(SUM(mc.amount),2) AS md_amount
FROM  local_milk_sale mc 
WHERE mc.sale_date >= p_from_collection_date AND mc.sale_date <= p_to_collection_date AND mc.society_code = p_society_code and
  milk_type_code=CASE WHEN p_milk_type_code=0 THEN milk_type_code ELSE p_milk_type_code END
GROUP BY mc.society_code,mc.sale_date,mc.milk_type_code
) A
INNER JOIN society s ON s.code=A.society_code
INNER JOIN milk_types m ON m.code=A.milk_type_code
GROUP BY A.society_code,A.milk_type_code,A.collection_date,m.name,s.name
ORDER BY A.collection_date,A.milk_type_code;

END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_society_purchase_member_wise`(IN p_from_collection_date DATETIME,IN p_to_collection_date DATETIME,IN p_society_code VARCHAR(12),IN p_locale varchar(50),IN p_milk_type_code int,IN p_member_code varchar(50))
BEGIN
    SELECT A.society_code,
    A.milk_type_code,
    A.collection_date,
    A.member_name,
    CASE WHEN p_locale ='en' THEN m.name ELSE  IFNULL(m.name_local,m.name)  END AS animal_name,
    CASE WHEN p_locale ='en' THEN s.name ELSE  IFNULL(s.name_local,s.name)  END AS society_name,
concat(SUBSTRING(DATE_FORMAT(collection_date,'%d/%m/%Y'),1,5),CASE when SUBSTRING(collection_date,12)='06:00:00'  then ' - M ' else ' - E ' END)   AS period,
IFNULL(round(sum(A.mc_qty),3),0) AS mc_qty, 
IFNULL(round(sum(A.mc_fat),2),0) AS mc_fat, 
IFNULL(round(sum(A.mc_snf),2),0) AS mc_snf, 
IFNULL(round(sum(A.mc_amount),2),0) AS mc_amount,
IFNULL(round(sum(A.md_qty),3),0) AS ms_qty, 
IFNULL(round(sum(A.md_amount),2),0) AS ms_amount
FROM
(
SELECT 
    mc.society_code,
    mc.member_code,
    mc.collection_date,
    mc.milk_type_code,
CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.last_name,''), +" - ",IFNULL(mc.member_code,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.last_name_local," - ",mc.member_code),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.last_name,''), +" - ",IFNULL(mc.member_code,''))) END as member_name,
    round(sum(mc.qty),3) AS mc_qty,
    Round(round(sum(mc.qty * mc.fat / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_fat,
    Round(round(sum(mc.qty * mc.snf / 100),4)/Round(Sum((mc.qty)),2) * 100,2) AS mc_snf,
    round(SUM(mc.amount),2) AS mc_amount,
    0 AS md_qty,
    0 AS md_amount
FROM  milk_collection mc JOIN members m ON m.code=mc.member_code
WHERE mc.collection_date >= p_from_collection_date AND mc.collection_date <= p_to_collection_date AND 
mc.society_code = p_society_code and mc.member_code=case when p_member_code=0 then  mc.member_code else p_member_code end
and mc.milk_type_code = case when p_milk_type_code=0 then mc.milk_type_code else p_milk_type_code end
GROUP BY mc.society_code,mc.collection_date,mc.milk_type_code,mc.member_code


UNION

SELECT 
    mc.society_code,
    mc.sale_date AS collection_date,
    mc.milk_type_code,
    mc.consumer_code,
    '' AS member_name,
    0 AS mc_qty,
    0 AS mc_fat,
    0 AS mc_snf,
    0 AS mc_amount,
    round(sum(mc.quantity),3) AS md_qty,
    round(SUM(mc.amount),2) AS md_amount
FROM  local_milk_sale mc
WHERE mc.sale_date >= p_from_collection_date AND mc.sale_date <= p_to_collection_date AND mc.society_code = p_society_code 
and   mc.consumer_code=case when p_member_code=0 then  mc.consumer_code else p_member_code end and
mc.milk_type_code = case when p_milk_type_code=0 then mc.milk_type_code else p_milk_type_code end
GROUP BY mc.society_code,mc.sale_date,mc.milk_type_code,mc.consumer_code
) A
INNER JOIN society s ON s.code=A.society_code
INNER JOIN milk_types m ON m.code=A.milk_type_code
GROUP BY A.society_code,A.milk_type_code,A.collection_date,m.name,s.name,A.member_name
ORDER BY A.collection_date,A.milk_type_code;

END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_staff_Salary`(IN p_member_code varchar(500),IN p_society_code VARCHAR(50),IN p_month VARCHAR(50),IN p_locale varchar(20))
BEGIN
-- call rpt_staff_Salary('0', '1011006', '4-2022', 'en');
SELECT dates,s_name,su_code,staff_code,staff_name,designation_name ,SUM(add_amount) as add_amount,SUM(deduction_amount) as deduction_amount,SUM(add_amount)-SUM(deduction_amount) as total,bank_account_no as bank_account_no FROM 
(SELECT  sm.code  as staff_code,
     SUBSTRING(sm.code,8)as    su_code,
    sm.name AS staff_name,
    d.name AS designation_name,
    sh.name AS head_name,
    SUM(stm.amount) as add_amount,
    0 as deduction_amount,
     
    CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)) as dates,
    concat(CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END ,s.code_ex) as s_name,
      sm.bank_account_no as bank_account_no
FROM
    staff_salary_head_mapping AS stm
        INNER JOIN
    salary_head AS sh ON sh.code = stm.salary_head_code
        INNER JOIN
    staff_member AS sm ON sm.code = stm.staff_member_code
        INNER JOIN
    designation AS d ON d.code = sm.designation_code
		INNER JOIN society as s ON s.code=stm.society_code
    WHERE  sh.type = 1 and stm.staff_member_code=CASE WHEN p_member_code= '0' THEN stm.staff_member_code ELSE p_member_code END 
     AND stm.society_code= p_society_code  AND  CONCAT(MONTH(stm.wef_date),'-',YEAR(stm.wef_date))=p_month
        GROUP BY sm.bank_account_no, sm.code,sm.name,d.name,sh.name,s.name_local,s.code_ex,CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date))  ,s.name
 UNION ALL
 SELECT sm.code  as staff_code,
  SUBSTRING(sm.code,8)as    su_code,
    sm.name AS staff_name,
    d.name AS designation_name,
    sh.name AS head_name,
    0 as add_amount,
    SUM(stm.amount) as deduction_amount,
     
    CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)) as dates,
  concat(CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END ,s.code_ex) as s_name,
  sm.bank_account_no as bank_account_no
FROM
   staff_salary_head_mapping AS stm
        INNER JOIN
    salary_head AS sh ON sh.code = stm.salary_head_code
        INNER JOIN
    staff_member AS sm ON sm.code = stm.staff_member_code
        INNER JOIN
    designation AS d ON d.code = sm.designation_code
    INNER JOIN society as s ON s.code=stm.society_code
    WHERE  sh.type = 0  and stm.staff_member_code=CASE WHEN p_member_code='0' THEN stm.staff_member_code ELSE p_member_code END 
     AND stm.society_code= p_society_code  AND  CONCAT(MONTH(stm.wef_date),'-',YEAR(stm.wef_date))=p_month
        GROUP BY  sm.code,sm.name,d.name,sh.name,s.name_local,s.code_ex,CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date))  ,s.name, sm.bank_account_no) AS A
GROUP BY bank_account_no,dates,s_name,su_code,staff_code,staff_name,designation_name ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_staff_Salary_addition`(IN p_member_code varchar(500),IN p_society_code VARCHAR(50),IN p_month VARCHAR(50),IN p_locale varchar(20))
BEGIN
--  call rpt_staff_Salary_addition('0', '1011006', '4-2022', 'en');
SELECT  sm.code as staff_code,sh.name as head_name,SUM(stm.amount) as add_amount,
		CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)) as dates
FROM
    staff_salary_head_mapping AS stm
        INNER JOIN
    salary_head AS sh ON sh.code = stm.salary_head_code
        INNER JOIN
    staff_member AS sm ON sm.code = stm.staff_member_code
    WHERE  sh.type = 1  and stm.staff_member_code=CASE WHEN p_member_code=0 THEN stm.staff_member_code ELSE p_member_code END 
     AND stm.society_code= p_society_code AND  CONCAT(MONTH(stm.wef_date),'-',YEAR(stm.wef_date))=p_month
        GROUP BY   sm.code,CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)),sh.name;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_staff_Salary_deduction`(IN p_member_code varchar(500),IN p_society_code VARCHAR(50),IN p_month VARCHAR(50),IN p_locale varchar(20))
BEGIN
--  call rpt_staff_Salary_deduction('0', '1011006', '4-2022', 'en');
SELECT  sm.code as staff_code,sh.name as head_name,SUM(stm.amount) as add_amount,
		CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)) as dates
FROM
    staff_salary_head_mapping AS stm
        INNER JOIN
    salary_head AS sh ON sh.code = stm.salary_head_code
        INNER JOIN
    staff_member AS sm ON sm.code = stm.staff_member_code
    WHERE  sh.type = 0  and stm.staff_member_code=CASE WHEN p_member_code=0 THEN stm.staff_member_code ELSE p_member_code END 
     AND stm.society_code= p_society_code AND  CONCAT(MONTH(stm.wef_date),'-',YEAR(stm.wef_date))=p_month
        GROUP BY   sm.code,CONCAT(MONTHNAME(stm.wef_date),' - ',YEar(stm.wef_date)),sh.name;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_stock_summary`(IN p_society_code varchar(225), IN p_as_on_date date)
BEGIN
	select
		pst.product_code,
		p.name AS product_name,
        p.reference_code AS reference_code,
		pst.union_code,
		u.name AS union_name,
		pst.society_code,
		s.name AS society_name,
		s.village_code,
		v.name AS village_name,
		ut.name AS unit_name,
		pst.final_value,
		pst.transaction_date
		
	FROM product_stock_transaction AS pst
		INNER JOIN products AS p ON pst.product_code = p.code  
		INNER JOIN unions AS u ON pst.union_code = u.code  
		INNER JOIN society AS s ON pst.society_code = s.code
		INNER JOIN villages AS v ON s.village_code = v.code
		INNER JOIN units AS ut ON p.primary_uom_code = ut.code
		
	
	WHERE CAST(pst.created_at AS DATE) <= CAST(p_as_on_date AS DATE)
		AND pst.code = (SELECT code FROM product_stock_transaction 
        WHERE CAST(created_at AS DATE) <= CAST(p_as_on_date AS DATE) and product_code = pst.product_code ORDER BY created_at desc, created_at desc limit 1)
        AND pst.society_code = p_society_code 
		ORDER BY pst.product_code,pst.transaction_date;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_subreport_product_sale_invoice`(IN p_society_code varchar(12), IN p_invoice_no varchar(35))
BEGIN

	SELECT 
		Round(sum(IFNULL(pstx.value,0)),2) AS TaxAmount,
		tax.name
	FROM product_sale ps
		INNER JOIN product_sale_transaction pst ON ps.invoice_no = pst.invoice_no
		INNER JOIN product_sale_tax pstx ON pst.invoice_txn_no = pstx.invoice_txn_no
		INNER JOIN tax tax ON pst.tax_code = tax.code
	WHERE ps.invoice_no = p_invoice_no AND ps.society_code = p_society_code
    GROUP BY pst.tax_code,tax.name;

END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_share_issue`(IN p_society_code Varchar(12),IN p_from_date DATE,IN p_to_date DATE ,IN p_locale varchar(50))
BEGIN
 SELECT 
 p_from_date as from_date,
 p_to_date as to_date, 
s.share_code as member_code,
case when p_locale='en' then
 g.name 
 else
  g.name_local 
end as gender
 ,
 so.name as society_name,
 so.code as society_code,
 CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
sum(s.no_of_share) as no_of_share,sum(s.share_amount) as share_amount
 from share s join members m on s.member_code=m.code 
 join member_details md on md.code=m.code
 join genders g on g.code=md.gender_code
 join society so on s.society_code=so.code
 where s.society_code=p_society_code and
 s.issue_date between p_from_date and p_to_date
 and s.is_cancelled=false and s.is_transferred=false
 group by s.share_code,g.name,g.name_local,m.first_name,m.first_name_local,m.middle_name,m.middle_name_local,m.last_name_local,m.last_name
 ;
 END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_share_dividend`(IN p_society_code Varchar(12),IN p_from_date DATE,IN p_to_date DATE ,IN p_locale varchar(50))
BEGIN
 SELECT 
 p_from_date as from_date,
 p_to_date as to_date,
 sd.share_code as member_code,
 s.name as society_name,
 s.code as society_code,
 CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
 sum(sd.no_of_share) as no_of_share,sum(sd.share_amount) as share_amount,sum(sd.dividend_amount) as dividend_amount,sd.dividend_value as dividend_value
 ,sd.dividend_value_type as dividend_type
 from share_dividend sd join members m on sd.member_code=m.code 
 join society s on sd.society_code=s.code
 where sd.society_code=p_society_code and
 sd.disbursement_date between p_from_date and p_to_date group by sd.share_code,m.first_name,m.first_name_local,m.middle_name,
 m.middle_name_local,m.last_name,m.last_name_local,sd.dividend_value,sd.dividend_value_type;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_share_cancel`(IN p_society_code Varchar(12),IN p_from_date DATE,IN p_to_date DATE ,IN p_locale varchar(50))
BEGIN
 SELECT
p_from_date as from_date,
p_to_date as to_date,
 s.share_code as member_code,
 so.name as society_name,
 so.code as society_code,
 CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE  
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name, 
  sum(s.no_of_share) as no_of_share,sum(s.share_amount) as share_amount
 from share s join members m on s.member_code=m.code 
 join society so on s.society_code=so.code
 where s.society_code=p_society_code and
 s.cancel_date between p_from_date and p_to_date
 and s.is_cancelled=true and s.is_transferred=false group by s.share_code;
 END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_share_members`(IN p_society_code Varchar(12),IN p_locale varchar(50))
BEGIN
 select 
 
 s.share_code as share_code,
 m.code_ex as member_code,
 so.name as society_name,
 so.code as society_code,
 CASE WHEN p_locale ='en' THEN
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,'')) ELSE
    IFNULL(concat(m.first_name_local,+" ", m.middle_name_local," ",m.last_name_local),
    concat(IFNULL(m.first_name,''),+" ",IFNULL(m.middle_name,''), +" ",IFNULL(m.last_name,''))) END as member_name
 from share s
 join members m on s.member_code=m.code
 join society so on m.society_code=so.code
 group by s.share_code order by s.share_code;
 
 END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_accounting_opening_balance`(IN p_society_code varchar(225), IN p_from_date DATE,IN p_to_date DATE,IN p_locale varchar(20))
BEGIN
-- call sp_accounting_opening_balance(1,'2022-04-02','2022-04-02',11)

	
SET @start_date=(SELECT MAX(start_date)   FROM ledger_opening_balance as lob 
				  INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code 
				  WHERE fy.start_date=p_from_date) ;
SET @financial_year=(SELECT  fy.code   FROM financial_years as fy WHERE fy.start_date = @start_date);  
   
 
 
 SELECT ledger_code,ledger_name,SUM(Balance) as Balance FROM
	(SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		round((IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))), 2) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				(ledger_type.balance_sheet = true  OR ledger_type.profit_loss=true)
				AND (lob.financial_years_code = @financial_year)
				AND (lob.society_code=p_society_code or lob.society_code IS NULL)
			GROUP BY  ledger.name_local,lob.ledgers_code, ledger.name ,lob.credit_debit)
            
            UNION ALL
            
            
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (vt.credit_debit=0)  then SUM(amount) END as DEBIT,
	            case when (vt.credit_debit=1)  then SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
				INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
			WHERE 
				(ledger_type.balance_sheet = true  OR ledger_type.profit_loss=true) AND ledger.is_active=true  AND voucher.cancelled =false
				AND (voucher.society_code=p_society_code or voucher.society_code is null)
				AND voucher.voucher_date between case when @start_date is null then p_from_date else @start_date end and p_to_date 
			GROUP BY  ledger.name_local,vt.ledger_code , ledger.name,vt.credit_debit )
	) as A group by A.ledger_code ,A.ledger_name 
     /*UNION  
    SELECT 
		ledger.code as ledger_code, CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name, 0 as Balance
	FROM ledgers as ledger 
		INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
		INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
	WHERE (ledger_type.balance_sheet = TRUE OR ledger_type.profit_loss = TRUE )*/ ) as A 
    GROUP BY ledger_code,ledger_name ORDER BY ledger_code;
    
    
 END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_accounting_sub_ledger_opening_balance`(IN p_society_code VARCHAR(255),IN p_from_date DATE,IN p_to_date DATE, IN p_locale varchar(20))
BEGIN
-- call sp_accounting_sub_ledger_opening_balance(0,'2022-04-01','2023-04-01','en')
declare start_date date;
declare financial_year VARCHAR(15);
	
	SELECT 
		max(start_date) into start_date 
    FROM sub_ledger_opening_balance as lop 
		INNER JOIN financial_years as fy ON fy.code=lop.financial_years_code 
	WHERE fy.start_date<=p_from_date;
    
	SELECT 
       fy.code into financial_year 
    FROM financial_years as fy         
	WHERE fy.start_date = start_date;       
 
 
 SELECT 
		A.sub_ledger_code as sub_ledger_code,
        A.sub_ledger_name as sub_ledger_name,
		round((IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))), 2) as Balance,
         A.ledger_code as ledger_code
	FROM(	
			
			(SELECT 
				lob.sub_ledger_code as sub_ledger_code,
				sl.name as sub_ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT, 
                lob.ledgers_code as ledger_code,
                0 as DEBIT,
                0 as CREDIT
			FROM sub_ledger_opening_balance as lob
                LEFT JOIN sub_ledgers as sl ON sl.code=lob.sub_ledger_code
				LEFT JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				LEFT JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				LEFT JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
				LEFT JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				(ledger_type.balance_sheet = true  or ledger_type.profit_loss = true )
				 AND(lob.financial_years_code = financial_year)
				 AND (lob.society_code=p_society_code)
			GROUP BY  lob.sub_ledger_code, sl.name ,lob.credit_debit
            ,lob.ledgers_code 
            ORDER BY fy.start_date desc
            )
            
            UNION
            
            
            (SELECT 
				vsl.sub_ledger_code as sub_ledger_code,
				sl.name as sub_ledger_name,
                
				case when (vsl.credit_debit=0)  then SUM(vsl.amount) END as DEBIT,
	            case when (vsl.credit_debit=1)  then SUM(vsl.amount) END as CREDIT,
                vt.ledger_code as ledger_code,
                0 as lob_DEBIT,
                0 as lob_CREDIT
                 
			FROM  voucher_sub_ledger as vsl 
				LEFT JOIN voucher as voucher ON voucher.code=vsl.voucher_code
				LEFT JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
			 	LEFT JOIN ledgers as ledger ON ledger.code=vt.ledger_code
			 	LEFT JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
			 	LEFT JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
                LEFT JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
			WHERE 
				(ledger_type.balance_sheet = true  or ledger_type.profit_loss = true ) AND voucher.cancelled =false
				AND (voucher.society_code=p_society_code)
				AND voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date 
			GROUP BY  vsl.sub_ledger_code,  vt.ledger_code,
            sl.name,vsl.credit_debit)
	) as A group by A.sub_ledger_code ,A.sub_ledger_name,A.ledger_code
    union
    SELECT 
		sl.code as sub_ledger_code, sl.name as sub_ledger_name, 0 as Balance,ledger.code as ledger_code
       
	FROM sub_ledgers as sl 
		LEFT JOIN ledgers as ledger ON sl.society_code=ledger.society_code
		LEFT JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
		LEFT JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
	WHERE (ledger_type.balance_sheet = true  or ledger_type.profit_loss = true ) AND (ledger.society_code = p_society_code) ;
    
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_accounting_subledger_ledger_opening_balance`(IN p_ledger_code VARCHAR(255),IN p_from_date DATE,IN p_to_date DATE, IN p_locale varchar(20))
BEGIN
-- call sp_accounting_subledger_ledger_opening_balance('0','2022-01-01','2022-01-01','en')
SELECT Opning.sub_ledger_code as sub_ledger_code, 
        Opning.sub_ledger_name as sub_ledger_name,
        round(IFNULL(SUM(CREDIT),0)-IFNULL(SUM(DEBIT),0),2) as Balance
  FROM (SELECT vsl.sub_ledger_code as sub_ledger_code,
                    sl.name as sub_ledger_name,
			   case when (vsl.credit_debit=0)  then SUM(vsl.amount) END as  DEBIT,
			   case when (vsl.credit_debit=1)  then SUM(vsl.amount) END as  CREDIT
		FROM voucher_sub_ledger as vsl
             LEFT JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
			 LEFT JOIN ledgers as ledger ON vt.ledger_code=ledger.code
             LEFT JOIN voucher as voucher ON voucher.code=vsl.voucher_code
			 LEFT JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
		WHERE voucher.cancelled =false
        AND vt.ledger_code=p_ledger_code
		AND voucher.voucher_date between p_from_date and p_to_date 
        GROUP BY  sl.name,vsl.sub_ledger_code, vsl.credit_debit) AS Opning group by 
        Opning.sub_ledger_code  , 
        Opning.sub_ledger_name 
        ORDER BY sub_ledger_code;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_current_stock_for_product`(IN p_as_on_date DATE ,IN p_society_code VARCHAR(12), IN p_locale VARCHAR(2))
BEGIN
-- CALL sp_current_stock_for_product('2022-05-01','1010226','en')

	SELECT DISTINCT(p.code) as product_code,CASE WHEN p_locale='en' THEN p.name ELSE IFNULL(p.name_local,p.name) END  as product_name, IFNULL(ps.stock, 0) stock, u.name as unit_name
        FROM
			products p 
            LEFT JOIN product_stock ps ON p.code = ps.product_code
            LEFT JOIN product_stock_transaction as pst ON pst.product_code=ps.product_code
            LEFT JOIN units u ON u.code = p.primary_uom_code
		WHERE 
			(p.society_code IS NULL OR ps.society_code = p_society_code) 
            AND pst.transaction_date <=p_as_on_date
            AND p.code NOT IN ('1', '2') ORDER BY p.code;
  
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_product_receipt_by_desc`(IN p_product_code VARCHAR(14),IN p_as_on_date DATE )
BEGIN
-- CALL sp_product_receipt_by_desc(1,'2022-05-01')
	SELECT 
		CAST(txn.rate as decimal(18,2)) rate, CAST(txn.quantity as decimal(18,2)) as quantity
	FROM
		product_receipt_transaction txn
		INNER JOIN product_receipt receipt ON txn.grn_no = receipt.grn_no
	WHERE 
		txn.product_code = p_product_code
        AND receipt.grn_date <= p_as_on_date  
	ORDER BY 
		receipt.grn_date  DESC;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_book`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_locale VARCHAR(20),IN p_ledger_code VARCHAR(255))
BEGIN

-- call sp_rpt_account_ledger_book('2022-05-01','2022-05-01',1,'en',0)

DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);

SELECT MAX(start_date) into start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code
		WHERE fy.start_date <= p_from_date;

SELECT fy.code into financial_year FROM financial_years as fy WHERE fy.start_date = start_date;
 

Select   DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
           DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, L.code as ledger_code,CASE WHEN p_locale='en' THEN L.name ELSE IFNULL(L.name_local,L.name) END  as ledger_name, B.voucher_no, B.voucher_date, B.narration,  (IFNULL(DEBIT,0)) as DEBIT,  (IFNULL(CREDIT,0)) as CREDIT
from
	(SELECT A.ledger_code, 'Opening Balance' as narration,
		case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) < 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as DEBIT,
        case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) >= 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as CREDIT,
		'' as voucher_no,  (p_from_date  - interval 1 day) as voucher_date
	FROM
		(SELECT lob.ledgers_code as ledger_code, 'Ledger Opening Balance' as narration,
			case when (lob.credit_debit=0) then SUM(balance) END as DEBIT,
			case when (lob.credit_debit=1) then SUM(balance) END as CREDIT
		FROM ledger_opening_balance as lob
		WHERE lob.financial_years_code= financial_year AND lob.ledgers_code=CASE WHEN p_ledger_code= 0 THEN lob.ledgers_code else p_ledger_code END
		AND lob.society_code=p_society_code
		GROUP BY  lob.ledgers_code, narration,credit_debit

        UNION

		SELECT vt.ledger_code as ledger_code, 'Ledger Opening Balance' as narration,
			case when (vt.credit_debit=0)  then SUM(amount) END as  DEBIT,
			case when (vt.credit_debit=1)  then SUM(amount) END as  CREDIT
		FROM voucher_transaction as vt
			INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code 
		WHERE (voucher.voucher_date >= start_date OR  voucher.voucher_date < p_from_date)    
        AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
	    AND voucher.society_code=p_society_code  AND voucher.cancelled =false
        GROUP BY  vt.ledger_code , narration,credit_debit) as A
	group by A.ledger_code, A.narration,voucher_date

	UNION
	
    SELECT vt.ledger_code as ledger_code, vt.narration as narration,
		case when (vt.credit_debit=0)  then IFNULL(ROUND((amount),2),0) END as  DEBIT,
		case when (vt.credit_debit=1)  then IFNULL(ROUND((amount),2),0) END as  CREDIT,
		SUBSTRING(vt.voucher_code,23,26) as voucher_no, voucher.voucher_date as voucher_date
	FROM voucher_transaction as vt
		INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
	WHERE voucher.voucher_date BETWEEN   p_from_date AND  p_to_date
		AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code ELSE p_ledger_code END
        AND voucher.society_code=p_society_code  AND voucher.cancelled =FALSE
	order by ledger_code ) B
Left join ledgers as L ON L.code=B.ledger_code
Order by B.ledger_code, voucher_date, B.voucher_no;

 
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_sub_ledger_book`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_locale varchar(20),IN p_ledger_code VARCHAR(255))
BEGIN

-- call sp_rpt_account_ledger_sub_ledger_book('2022-04-01','2022-09-01',1,1,0)
DECLARE start_date date;
DECLARE financial_year VARCHAR(15);

SELECT MAX(start_date) INTO start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code into financial_year FROM financial_years as fy WHERE fy.start_date = start_date;


SELECT  DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
        L.code as ledger_code,
        CASE WHEN L.name_local IS NULL THEN  L.name ELSE L.name_local END AS  ledger_name,
        D.narration,
        (IFNULL(CREDIT,0)) as CREDIT, 
        (IFNULL(DEBIT,0)) as DEBIT,
        D. voucher_code,
        D.voucher_date 
        FROM(
          (SELECT 0 as sr_no,  
                  L.code as ledger_code,
                  CASE WHEN L.name_local IS NULL THEN  L.name ELSE L.name_local END AS ledger_name,
                  B.narration,
                  (IFNULL(CREDIT,0)) as CREDIT,
                  (IFNULL(DEBIT,0)) as DEBIT,
                  B. voucher_code,
                  B.voucher_date      
			FROM
	            (SELECT    
						 A.ledger_code,
                       'Opening Balance' as narration,
		                CASE WHEN ROUND(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) >= 0 THEN ROUND(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) ELSE 0 END as CREDIT,
						CASE WHEN ROUND(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) < 0 THEN ROUND(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) ELSE 0 END as DEBIT,
						 '' as  voucher_code,
                         (p_from_date  - INTERVAL 1 DAY) as voucher_date
	                  FROM
		                  (SELECT   
                                  lob.ledgers_code as ledger_code, 'Ledger Opening Balance' as narration,
			                      CASE WHEN (lob.credit_debit=1) THEN SUM(balance) END as CREDIT,
								  CASE WHEN (lob.credit_debit=0) THEN SUM(balance) END as DEBIT
							 FROM ledger_opening_balance as lob
								  WHERE lob.financial_years_code= financial_year 
                              AND lob.ledgers_code=CASE WHEN p_ledger_code= 0 THEN lob.ledgers_code else p_ledger_code END
		                      AND lob.society_code=p_society_code 
		                      GROUP BY  lob.ledgers_code, narration,credit_debit

             UNION ALL
                SELECT ledger_code, narration ,SUM(CREDIT) as CREDIT ,SUM(DEBIT) as DEBIT FROM 
		            (  SELECT   
                               vt.ledger_code as ledger_code, 
                               'Ledger Opening Balance' as narration,
							   CASE WHEN (vt.credit_debit=1)  THEN SUM(amount) END as  CREDIT,
                               CASE WHEN (vt.credit_debit=0)  THEN SUM(amount) END as  DEBIT
				      FROM voucher_transaction as vt
			                  INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code 
							  -- WHERE  voucher.voucher_date BETWEEN CASE WHEN start_date IS NULL THEN p_from_date ELSE start_date END AND p_to_date
                              
                              WHERE   ( voucher.voucher_date >= start_date OR  voucher.voucher_date < p_from_date)  
                              AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code ELSE p_ledger_code END
	                          AND voucher.society_code=p_society_code  AND voucher.cancelled =FALSE
                              GROUP BY  vt.ledger_code, narration,credit_debit
                UNION ALL
                              SELECT  ledgers.code as ledger_code, 
                               'Ledger Opening Balance' as narration,
							   0 as  CREDIT,
                               0  as  DEBIT FROM ledgers 
                              WHERE    code=CASE WHEN p_ledger_code= 0 THEN code ELSE p_ledger_code END ) as F
                              GROUP BY  ledger_code, narration
                              
				) as A 
Left join ledgers as L ON L.code=A.ledger_code
  GROUP BY A.ledger_code, A.narration,voucher_date
	   UNION ALL
	
        SELECT  
               vt.ledger_code as ledger_code,
               vt.narration as narration,
               case when (vt.credit_debit=1)  then IFNULL(ROUND(SUM(amount),2),0) END as  CREDIT,
		       case when (vt.credit_debit=0)  then IFNULL(ROUND(SUM(amount),2),0) END as  DEBIT,
		       SUBSTRING(vt. voucher_code,23,26) as  voucher_code,
               voucher.voucher_date as voucher_date
	   FROM voucher_transaction as vt
		      INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
			  WHERE voucher.voucher_date between   p_from_date   and p_to_date
		      AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
              AND voucher.society_code=p_society_code   AND voucher.cancelled =false
              GROUP BY  ledger_code, narration,credit_debit, voucher_code,voucher.voucher_date 
		) B
          LEFT JOIN ledgers as L ON L.code=B.ledger_code
		  Order by  B.ledger_code, voucher_date, B.voucher_code)
Union

     (SELECT  1 as sr_no,
             vt.ledger_code as ledger_code,
             0 as ledger_name, 
             vsl.narration as narration,
             case when (vsl.credit_debit=1)  then IFNULL(ROUND((vsl.amount),2),0) END as  CREDIT,
		     case when (vsl.credit_debit=0)  then IFNULL(ROUND((vsl.amount),2),0) END as  DEBIT,
		     SUBSTRING(vsl. voucher_code,23,26) as voucher_no,
             voucher.voucher_date as voucher_date
	FROM voucher_sub_ledger as vsl
		INNER JOIN voucher_transaction as vt ON vt.code =vsl.voucher_code
		INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code
	WHERE voucher.voucher_date between   p_from_date   and p_to_date 
    AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
    AND voucher.society_code=p_society_code   AND voucher.cancelled =false
    Order by  ledger_code,voucher_date, vsl.voucher_code) 
) as  D
    Left join ledgers as L ON L.code=D.ledger_code
 
    Order by    D.ledger_code, voucher_date,D.voucher_code,sr_no ;
    
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_summary`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_locale varchar(20),IN p_ledger_code VARCHAR(255))
BEGIN
-- call sp_rpt_account_ledger_summary('2021-05-01','2024-05-01',1091661,0,'en')
 

DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);

SELECT MAX(start_date) into start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code INTO financial_year FROM financial_years AS fy WHERE fy.start_date = start_date;
 

SELECT  
		DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date,
	    C.ledger_code,
		C.ledger_name,
	    (round(SUM(opning_balance),2))as opning_balance, 
		IFNULL((round(SUM(credit),2)),0) as credit,
        IFNULL((round(SUM(debit),2)),0) as debit,
        C.society_name,
        C.ledger_group_name
	          
  FROM 
      (SELECT B.society_name,
           B.ledger_code ,
           B.ledger_name ,
           round(IFNULL(SUM(credit),0)-IFNULL(SUM(debit),0),2) as opning_balance,0 as credit,0 as debit,
           B.ledger_group_name
            
		    FROM 
                 (SELECT 
                 CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
                     lob.ledgers_code as ledger_code,
                     CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
					 CASE WHEN (lob.credit_debit=1) THEN (SUM(balance))   END as credit,
                     CASE WHEN (lob.credit_debit=0) THEN (SUM(balance))   END as debit,
                     CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
			     FROM ledger_opening_balance as lob
		            INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
                    INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
                   Inner JOIN society s on ledger.society_code=p_society_code
                    WHERE lob.financial_years_code = financial_year 
                    AND lob.ledgers_code=CASE WHEN p_ledger_code= 0 THEN lob.ledgers_code else p_ledger_code END
				 	AND lob.society_code=p_society_code
		            GROUP BY   lob.ledgers_code,ledger.name,credit_debit,lg.name,ledger.name_local,lg.name_local,
                    s.name,s.name_local
        UNION     
                SELECT 
                 CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
                     vt.ledger_code as ledger_code,
                    CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
			         case when (vt.credit_debit=1) then (SUM(amount))   END as credit,
                     case when (vt.credit_debit=0) then (SUM(amount))   END as debit,
				CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
		         FROM voucher_transaction as vt
                    INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
					INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
					INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
                    Inner JOIN society s on ledger.society_code=p_society_code
                    WHERE (voucher.voucher_date >= start_date OR  voucher.voucher_date <  p_from_date) 
					AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
	                AND voucher.society_code=p_society_code
                    AND voucher.cancelled =false
		            GROUP BY  vt.ledger_code,ledger.name,credit_debit, lg.name,ledger.name_local,lg.name_local
                    ,s.name,s.name_local
         ) as B GROUP BY  B.ledger_code ,B.ledger_name, B.ledger_group_name
         ,B.society_name
 UNION
	    SELECT 
        CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
			  vt.ledger_code as ledger_code,
             CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
			  0 as opning_balance,
              case when (vt.credit_debit=1) then (SUM(amount))   END as credit, 
			  case when (vt.credit_debit=0) then (SUM(amount))   END as debit,
			CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
			  
		 FROM voucher_transaction as vt
              INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
			  INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
              INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
             Inner JOIN society s on ledger.society_code=p_society_code
			  WHERE voucher.voucher_date between   p_from_date  and p_to_date
			  AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
			  AND voucher.society_code=p_society_code
              AND voucher.cancelled =false
		 GROUP BY  vt.ledger_code,ledger.name,ledger.name_local,credit_debit,lg.name_local,lg.name
          ,s.name,s.name_local
         )
         C  group  by C.ledger_code,C.ledger_name, C.ledger_group_name
          ,C.society_name
         ;
  
  
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_summary_two`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_locale varchar(20),IN p_ledger_code VARCHAR(255))
BEGIN
-- call sp_rpt_account_ledger_summary('2021-05-01','2024-05-01',1091661,0,'en')
 

DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);

SELECT MAX(start_date) into start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code INTO financial_year FROM financial_years AS fy WHERE fy.start_date = start_date;
 

SELECT  
		DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date,
	    C.ledger_code,
		C.ledger_name,
	    (round(SUM(opning_balance),2))as opning_balance, 
		IFNULL((round(SUM(credit),2)),0) as credit,
        IFNULL((round(SUM(debit),2)),0) as debit,
        C.society_name,
        C.ledger_group_name
	          
  FROM 
      (SELECT B.society_name,
           B.ledger_code ,
           B.ledger_name ,
           round(IFNULL(SUM(credit),0)-IFNULL(SUM(debit),0),2) as opning_balance,0 as credit,0 as debit,
           B.ledger_group_name
            
		    FROM 
                 (SELECT 
                 CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
                     lob.ledgers_code as ledger_code,
                     CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
					 CASE WHEN (lob.credit_debit=1) THEN (SUM(balance))   END as credit,
                     CASE WHEN (lob.credit_debit=0) THEN (SUM(balance))   END as debit,
                     CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
			     FROM ledger_opening_balance as lob
		            INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
                    INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
                   Inner JOIN society s on ledger.society_code=p_society_code
                    WHERE lob.financial_years_code = financial_year 
                    AND lob.ledgers_code=CASE WHEN p_ledger_code= 0 THEN lob.ledgers_code else p_ledger_code END
				 	AND lob.society_code=p_society_code
		            GROUP BY   lob.ledgers_code,ledger.name,credit_debit,lg.name,ledger.name_local,lg.name_local,
                    s.name,s.name_local
        UNION     
                SELECT 
                 CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
                     vt.ledger_code as ledger_code,
                    CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
			         case when (vt.credit_debit=1) then (SUM(amount))   END as credit,
                     case when (vt.credit_debit=0) then (SUM(amount))   END as debit,
				CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
		         FROM voucher_transaction as vt
                    INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
					INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
					INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
                    Inner JOIN society s on ledger.society_code=p_society_code
                    WHERE (voucher.voucher_date >= start_date OR  voucher.voucher_date <  p_from_date) 
					AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
	                AND voucher.society_code=p_society_code
                    AND voucher.cancelled =false
		            GROUP BY  vt.ledger_code,ledger.name,credit_debit, lg.name,ledger.name_local,lg.name_local
                    ,s.name,s.name_local
         ) as B GROUP BY  B.ledger_code ,B.ledger_name, B.ledger_group_name
         ,B.society_name
 UNION
	    SELECT 
        CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
			  vt.ledger_code as ledger_code,
             CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
			  0 as opning_balance,
              case when (vt.credit_debit=1) then (SUM(amount))   END as credit, 
			  case when (vt.credit_debit=0) then (SUM(amount))   END as debit,
			CASE WHEN p_locale ='en' THEN lg.name ELSE IFNULL(lg.name_local,lg.name) END as ledger_group_name
			  
		 FROM voucher_transaction as vt
              INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
			  INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
              INNER JOIN ledger_groups as lg ON lg.code=ledger.ledger_group_code
             Inner JOIN society s on ledger.society_code=p_society_code
			  WHERE voucher.voucher_date between   p_from_date  and p_to_date
			  AND vt.ledger_code=CASE WHEN p_ledger_code= 0 THEN vt.ledger_code else p_ledger_code END
			  AND voucher.society_code=p_society_code
              AND voucher.cancelled =false
		 GROUP BY  vt.ledger_code,ledger.name,ledger.name_local,credit_debit,lg.name_local,lg.name
          ,s.name,s.name_local
         )
         C  group  by C.ledger_code,C.ledger_name, C.ledger_group_name
          ,C.society_name
         ;
  
  
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_with_sub_ledger_book`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_sub_ledger_code VARCHAR(255),IN p_Type INT,IN p_locale varchar(20))
BEGIN
declare start_date date;
declare financial_year VARCHAR(15);

SELECT max(start_date) into start_date FROM sub_ledger_opening_balance as lop 
		INNER JOIN financial_years as fy ON fy.code=lop.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code into financial_year FROM financial_years as fy WHERE fy.start_date = start_date;

 
Select DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
	   DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
       L.code as sub_ledger_code , L.name as sub_ledger_name,
      
       B.ledger_name, abs(IFNULL(SUM(DEBIT),0)) as DEBIT, abs(IFNULL(SUM(CREDIT),0)) as CREDIT,B.type
from
	(SELECT A.sub_ledger_code, 
          
			case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) < 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as DEBIT,
        	case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) >= 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as CREDIT
		   , 0 as sub_ledger_name,'Opening Balance' as ledger_name, A.type 
	FROM
		(SELECT 
                 lob.sub_ledger_code as sub_ledger_code,
                
			    case when (lob.credit_debit=0) then SUM(balance) END as DEBIT,
			    case when (lob.credit_debit=1) then SUM(balance) END as CREDIT,
                0 as type
                FROM sub_ledger_opening_balance as lob
             INNER JOIN voucher_sub_ledger as vsl ON lob.sub_ledger_code=vsl.code  
             INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_code 
             INNER JOIN sub_ledgers as sl ON sl.code=lob.sub_ledger_code
		WHERE lob.financial_years_code = financial_year 
		AND lob.society_code=p_society_code
		AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END
        AND vsl.code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.code else p_sub_ledger_code END
		GROUP BY  lob.sub_ledger_code, 
        lob.credit_debit

        UNION

		SELECT vsl.sub_ledger_code as sub_ledger_code,
               
			   case when (vsl.credit_debit=0)  then SUM(vsl.amount) END as  DEBIT,
			   case when (vsl.credit_debit=1)  then SUM(vsl.amount) END as  CREDIT,
                0 as type
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
			INNER JOIN ledgers as ledger ON vt.ledger_code=ledger.code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code AND voucher.code=vt.voucher_code
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code 
		WHERE voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END  
        AND voucher.cancelled =false
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
	    AND voucher.society_code=p_society_code
        GROUP BY  vsl.sub_ledger_code,
        vsl.credit_debit) as A
	group by A.sub_ledger_code  ,A.type 

	UNION
	
   SELECT   vsl.sub_ledger_code as sub_ledger_code,
            
			case when (vsl.credit_debit=0)  then  SUM(vsl.amount) END as  DEBIT,
			case when (vsl.credit_debit=1)  then  SUM(vsl.amount) END as  CREDIT,
             sl.name as sub_ledger_name,
			 ledger.name as ledger_name,
			 1 as type
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code AND vt.voucher_code = voucher.code
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
			INNER JOIN ledgers as ledger ON ledger.code =vt.ledger_code
		WHERE voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END 
        AND voucher.cancelled =false
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
	    AND voucher.society_code=p_society_code
        group by   vsl.sub_ledger_code
        ,sl.name,ledger.name,vsl.credit_debit) B
Left join sub_ledgers as L ON L.code=B.sub_ledger_code
group by  L.code , L.name,
       
       B.ledger_name,B.type
 Order by L.code ,B.type ;
 
 
 
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_sub_ledger_book`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_sub_ledger_code VARCHAR(255),IN p_Type INT,IN p_locale varchar(20))
BEGIN

-- call sp_rpt_account_sub_ledger_book ('2022-03-02','2022-04-02',1,1,'1',1)
DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);

SELECT MAX(start_date) INTO start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code into financial_year FROM financial_years as fy WHERE fy.start_date = start_date;

 
Select DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
	   DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
       L.code as sub_ledger_code, L.name as sub_ledger_name,B.ledger_name,  B.voucher_date, B.narration,
       IFNULL(abs(IFNULL(DEBIT,0)),0) as DEBIT,
       IFNULL(abs(IFNULL(CREDIT,0)),0) as CREDIT
from
	(SELECT A.sub_ledger_code, 'Opening Balance' as narration,
			case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) < 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as DEBIT,
        	case when round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) >= 0 then round(IFNULL(SUM(A.CREDIT),0) - IFNULL(SUM(A.DEBIT),0),2) Else 0 End as CREDIT,
		  (p_from_date  - interval 1 day) as voucher_date,0 as sub_ledger_name,0 as ledger_name
	FROM
		(SELECT 
                 lob.sub_ledger_code as sub_ledger_code,
                'Sub Ledger Opening Balance' as narration,
			    case when (lob.credit_debit=0) then SUM(balance) END as DEBIT,
			    case when (lob.credit_debit=1) then SUM(balance) END as CREDIT
		FROM sub_ledger_opening_balance as lob
             INNER JOIN voucher_sub_ledger as vsl ON lob.sub_ledger_code=vsl.code  
             INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_code
             INNER JOIN sub_ledgers as sl ON sl.code=lob.sub_ledger_code
		WHERE lob.financial_years_code = financial_year 
		AND lob.society_code=p_society_code
		AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END
        AND vsl.code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.code else p_sub_ledger_code END
		GROUP BY  lob.sub_ledger_code, narration,lob.credit_debit

        UNION

		SELECT vsl.sub_ledger_code as sub_ledger_code,
               'Sub Ledger Opening Balance' as narration,
			   case when (vsl.credit_debit=0)  then SUM(vsl.amount) END as  DEBIT,
			   case when (vsl.credit_debit=1)  then SUM(vsl.amount) END as  CREDIT
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
			INNER JOIN ledgers as ledger ON vt.ledger_code=ledger.code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code AND voucher.code=vt.voucher_code
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code 
		WHERE (voucher.voucher_date >= start_date OR voucher.voucher_date < p_from_date)
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END  
        AND voucher.cancelled =false
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
	    AND voucher.society_code=p_society_code
        GROUP BY  vsl.sub_ledger_code, narration,vsl.credit_debit) as A
	group by A.sub_ledger_code, A.narration,voucher_date

	UNION
	
   SELECT   vsl.sub_ledger_code as sub_ledger_code,
            vsl.narration as narration,
			case when (vsl.credit_debit=0)  then  (vsl.amount) END as  DEBIT,
			case when (vsl.credit_debit=1)  then  (vsl.amount) END as  CREDIT,
			voucher.voucher_date as voucher_date,
            CASE WHEN p_locale='en' THEn sl.name ELSE IFNULL(sl.name_local,sl.name) END  as sub_ledger_name,
			CASE WHEN p_locale='en' THEn ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END as ledger_name
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code AND vt.voucher_code = voucher.code
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
			INNER JOIN ledgers as ledger ON ledger.code =vt.ledger_code
		WHERE voucher.voucher_date BETWEEN   p_from_date AND p_to_date
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type ELSE p_type END 
        AND voucher.cancelled =FALSE
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code ELSE p_sub_ledger_code END
	    AND voucher.society_code=p_society_code
       order by sub_ledger_code,voucher_date  ) B
Left join sub_ledgers as L ON L.code=B.sub_ledger_code
Order by B.sub_ledger_code, voucher_date ;

 END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_sub_ledger_book_summary`(IN p_from_date date,IN p_to_date date ,IN p_society_code varchar(255),IN p_sub_ledger_code VARCHAR(255),IN p_Type INT,IN p_locale varchar(255))
BEGIN
-- call sp_rpt_account_sub_ledger_book_summary('2022-03-02','2022-05-10',1,1,1,1)
DECLARE start_date date;
DECLARE financial_year VARCHAR(15);

SELECT max(start_date) into start_date FROM financial_years as fy   
			LEFT JOIN ledger_opening_balance as lob ON fy.code=lob.financial_years_code 
		WHERE fy.start_date <= p_from_date;

SELECT fy.code into financial_year FROM financial_years as fy WHERE fy.start_date = start_date;

 Select DATE_FORMAT(p_from_date, '%d/%m/%y') as p_from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') as p_to_date, 
		B.sub_ledger_code, B.sub_ledger_name,
        IFNULL((round(SUM(opning_balance),2)) ,0)as opning_balance, 
	    IFNULL((round(SUM(CREDIT),2)) ,0) as credit ,IFNULL((round(SUM(DEBIT),2)) ,0) as debit,B.type
 FROM
	(SELECT  A.sub_ledger_code,A.sub_ledger_name,
             round(IFNULL(SUM(CREDIT),0)-IFNULL(SUM(DEBIT),0),2) as opning_balance,0 as CREDIT,0 as DEBIT ,type 
		     
	FROM
		(SELECT 
                 vsl.sub_ledger_code as sub_ledger_code,
                 CASE WHEN sl.name_local IS NULL THEN  sl.name ELSE sl.name_local END as sub_ledger_name,
			     case when (lob.credit_debit=1) then (SUM(balance))   END as CREDIT,
				 case when (lob.credit_debit=0) then (SUM(balance))   END as DEBIT,
                  sl.type as type
		FROM sub_ledger_opening_balance as lob
             INNER JOIN voucher_transaction as vt ON  vt.ledger_code=lob.sub_ledger_code
             INNER JOIN voucher_sub_ledger as vsl ON vt.code=vsl.code AND vsl.voucher_code=vt.voucher_code
             INNER JOIN sub_ledgers as sl ON sl.code=lob.sub_ledger_code
		WHERE lob.financial_years_code = financial_year 
		AND lob.society_code=p_society_code
		AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
		GROUP BY  sub_ledger_code,lob.credit_debit,sub_ledger_name,sl.type,sl.name,sl.name_local

        UNION

		SELECT vsl.sub_ledger_code as sub_ledger_code,
               CASE WHEN sl.name_local IS NULL THEN  sl.name ELSE sl.name_local  END as sub_ledger_name,
			   case when (vsl.credit_debit=1)  then SUM(vsl.amount) END as  CREDIT,
			   case when (vsl.credit_debit=0)  then SUM(vsl.amount) END as DEBIT,
               sl.type as type
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
			INNER JOIN ledgers as ledger ON vt.ledger_code=ledger.code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
		WHERE (voucher.voucher_date >= start_date OR voucher.voucher_date <  p_from_date ) 
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
	    AND voucher.society_code=p_society_code
        AND voucher.cancelled =false
        GROUP BY  vsl.sub_ledger_code ,vsl.credit_debit,sl.name,sl.type,sl.name_local) as A
	group by A.sub_ledger_code,A.sub_ledger_name,A.type

	UNION
	
	SELECT vsl.sub_ledger_code as sub_ledger_code,
            CASE WHEN sl.name_local IS NULL THEN  sl.name ELSE sl.name_local END  as sub_ledger_name,
            0 as opning_balance,
			CASE WHEN (vsl.credit_debit=1)  THEN  SUM(vsl.amount) END AS  CREDIT,
			CASE WHEN (vsl.credit_debit=0)  THEN  SUM(vsl.amount) END AS  DEBIT,
            sl.type as type
		FROM voucher_sub_ledger as vsl
            INNER JOIN voucher_transaction as vt ON  vt.code=vsl.voucher_transaction_code
            INNER JOIN voucher as voucher ON voucher.code=vsl.voucher_code 
			INNER JOIN sub_ledgers as sl ON sl.code=vsl.sub_ledger_code
		WHERE voucher.voucher_date BETWEEN  p_from_date  AND p_to_date
        AND sl.type=CASE WHEN p_type= 0 THEN sl.type else p_type END
        AND vsl.sub_ledger_code=CASE WHEN p_sub_ledger_code= 0 THEN vsl.sub_ledger_code else p_sub_ledger_code END
	    AND voucher.society_code=p_society_code  
        AND voucher.cancelled =false
        GROUP BY vsl.sub_ledger_code,sl.name,vsl.credit_debit,sl.type,sl.name_local
        order by type,sub_ledger_code
        ) B GROUP BY B.sub_ledger_code, B.sub_ledger_name,B.type  Order by B.type,B.sub_ledger_code;
 
 
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_balance_sheet`(IN p_society_code varchar(225), IN p_from_date DATE,IN p_to_date DATE,IN p_liability_asset INT,IN p_locale varchar(20))
BEGIN
-- call sp_rpt_accounting_balance_sheet(1,'2022-05-01','2022-05-01',4,'hn')
SET @start_date=(SELECT MAX(start_date)   FROM ledger_opening_balance as lob 
				  INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code 
				  WHERE fy.start_date=p_from_date) ;
SET @financial_year=(SELECT  fy.code   FROM financial_years as fy WHERE fy.start_date = @start_date);   
IF p_liability_asset = 0 THEN
 SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		(IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END   as ledger_name,
				CASE WHEN (lob.credit_debit=0)  THEN SUM(balance) END as lob_DEBIT,
	            CASE WHEN (lob.credit_debit=1)  THEN SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				ledger_type.balance_sheet = true 
		 	 	   AND (lob.financial_years_code = @financial_year)
		  	 	AND (lob.society_code=p_society_code or lob.society_code is null)
			GROUP BY  ledger.name_local,lob.ledgers_code, ledger.name  ,lob.credit_debit ORDER BY fy.start_date desc)
            
            UNION ALL
            
            
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END   as ledger_name,
				CASE WHEN (vt.credit_debit=0)  THEN SUM(amount) END as DEBIT,
	            CASE WHEN (vt.credit_debit=1)  THEN SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
				INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
			WHERE 
				ledger_type.balance_sheet = true AND voucher.cancelled =false
			  	AND (voucher.society_code=p_society_code or voucher.society_code is null)
			  	AND voucher.voucher_date between case when @start_date is null then p_from_date else @start_date end and p_to_date 
			GROUP BY ledger.name_local, vt.ledger_code, ledger.name,vt.credit_debit )
	) as A group by A.ledger_code ,A.ledger_name HAVING (IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0)))  < 0;
ELSE    
	SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		(IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END   as ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				ledger_type.balance_sheet = true 
				  AND (lob.financial_years_code = @financial_year)
				  AND (lob.society_code=p_society_code or lob.society_code is null)
			GROUP BY  ledger.name_local,lob.ledgers_code, ledger.name ,lob.credit_debit  ORDER BY fy.start_date desc)
            
            UNION ALL
	
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END   as ledger_name,
				case when (vt.credit_debit=0)  then SUM(amount) END as DEBIT,
	            case when (vt.credit_debit=1)  then SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
					INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
			WHERE 
				ledger_type.balance_sheet = true  AND voucher.cancelled =false
			 	AND (voucher.society_code=p_society_code  or voucher.society_code is null)
			  	AND voucher.voucher_date between case when @start_date is null then p_from_date else @start_date end and p_to_date 
			GROUP BY ledger.name_local, vt.ledger_code, ledger.name,vt.credit_debit )
	) as A group by A.ledger_code ,A.ledger_name HAVING (IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0)))  >=0;
    END IF;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_cash_book`(IN p_society_code VARCHAR(20),IN p_from_date DATE ,IN p_to_date DATE,IN p_locale VARCHAR(20))
BEGIN
-- call sp_rpt_accounting_cash_book('1010226','2022-04-02','2022-04-10','en')
SELECT s.code_ex  as society_ex_code,
	   CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
	   DATE_format(p_from_date,'%d/%m/%y') as p_from_date,
	   DATE_format(p_to_date,'%d/%m/%y') as p_to_date,
       DATE_format(v.voucher_date,'%d/%m/%y') as voucher_date,
       v.code as voucher_code,
   CASE WHEN p_locale='en' THEN vt.name ELSE IFNULL(vt.name_local,vt.name) END  as voucher_type_name,
    CASE WHEN p_locale='en' THEN  l.name ELSE IFNULL(l.name_local,l.name) END  as ledger_name,
    CASE
        WHEN credit_debit = 1 THEN amount
        ELSE 0
    END credit,
    CASE
        WHEN credit_debit = 0 THEN amount
        ELSE 0
    END debit
FROM
    voucher v
        LEFT JOIN
    voucher_types AS vt ON vt.code = v.voucher_type_code
        LEFT JOIN
    voucher_transaction AS vtt ON vtt.voucher_code = v.code
        LEFT JOIN
    ledgers AS l ON l.code = vtt.ledger_code
        LEFT JOIN
	society as s ON s.code=v.society_code
WHERE
    v.cancelled = FALSE
    AND v.voucher_date BETWEEN p_from_date AND p_to_date
    AND v.society_code=p_society_code
    ORDER BY v.voucher_date;
      
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_cash_day_book`(IN p_society_code varchar(225), IN p_from_date DATE,IN p_locale VARCHAR(20))
BEGIN
 
SELECT 
      substring(voucher_code,25,27) as VNo, ledger_name as Ledger_Name,
      Case when (IFNULL(Sum(A.cr),0)-IFNULL(Sum(A.db),0)) > 0 then (IFNULL(Sum(A.cr),0)-IFNULL(Sum(A.db),0)) else 0 End as CashCr,
      Case when (IFNULL(Sum(A.trcr),0)-IFNULL(Sum(A.trdr),0)) > 0 then (IFNULL(Sum(A.trcr),0)-IFNULL(Sum(A.trdr),0)) else 0 End as trCr,
      Case when (IFNULL(Sum(A.cr),0)-IFNULL(Sum(A.db),0)) < 0 then (IFNULL(Sum(A.cr),0)-IFNULL(Sum(A.db),0)) else 0 End as CashDr,
      Case when (IFNULL(Sum(A.trcr),0)-IFNULL(Sum(A.trdr),0)) < 0 then (IFNULL(Sum(A.trcr),0)-IFNULL(Sum(A.trdr),0)) else 0 End as trDr ,
      type,
      A.society_code,
      A.society_name,
      Date_format(p_from_date,'%d/%m/%y') as p_from_date
FROM 
(SELECT 
        case when credit_debit = 1 then sum(amount) END as cr,
        case when credit_debit = 0 then sum(amount) END as db,
        '' as voucher_code,
        'opning_balance' as ledger_name,
        0 as trcr,
        0 as trdr,
        0 as type,
        voucher.society_code as society_code,
        d.name as society_name
FROM voucher as voucher
LEFT JOIN  voucher_transaction as  vt  ON vt.voucher_code=voucher.code
LEFT JOIN  society as d ON d.code=voucher.society_code
LEFT JOIN  ledgers as l ON l.code=vt.ledger_code
WHERE voucher.voucher_date < p_from_date AND d.code=p_society_code
AND l.ledger_group_code=1 AND cancelled=false   
group by credit_debit,  voucher.society_code,d.name

Union 



SELECT  
        case when credit_debit = 1 then sum(balance) END as cr,
        case when credit_debit = 0 then sum(balance) END as db,
        '' as voucher_code,
        'opning_balance' as ledger_name,
		0 as trcr,
        0 as trdr,
        1 as type,
        lop.society_code as society_code,
        d.name as society_name
From ledger_opening_balance as lop
LEFT JOIN ledgers as l on l.code=lop.ledgers_code
LEFT JOIN financial_years as  fy ON fy.code=lop.financial_years_code
LEFT JOIN  society as d ON d.code=lop.society_code
WHERE fy.start_date >=p_from_date and fy.end_date<=p_from_date
AND l.code = 1  AND lop.society_code=p_society_code
group by credit_debit,lop.society_code,d.name



Union 

SELECT  
       IFNULL(CASE WHEN credit_debit =1 THEN amount END,0) as cr,
       IFNULL(CASE WHEN credit_debit =0 THEN amount END ,0)as db,
	   voucher.code as voucher_code,
       l.name as ledger_name,
		0 as trcr,
        0 as trdr,
        2  as type,
		voucher.society_code as society_code,
        d.name as society_name
FROM voucher as  voucher
LEFT JOIN voucher_transaction as v ON v.voucher_code=voucher.code
LEFT JOIN ledgers as l ON l.code=v.ledger_code
LEFT JOIN  society as d ON d.code=voucher.society_code
WHERE voucher.voucher_date >= p_from_date AND voucher.voucher_date <= p_from_date
AND l.code<>1 AND voucher.society_code=p_society_code
and voucher.code in (SELECT distinct voucher.code
FROM voucher as  voucher
LEFT JOIN voucher_transaction as v ON v.voucher_code=voucher.code
LEFT JOIN ledgers as l ON l.code=v.ledger_code
WHERE voucher.voucher_date >= p_from_date AND voucher.voucher_date <= p_from_date
AND l.code=1 AND voucher.society_code=p_society_code)

Union

SELECT  0 as cr,
        0 as db,
	    voucher.code as voucher_code,
	   l.name as ledger_name,
       IFNULL(CASE WHEN credit_debit =0 THEN amount END ,0)as trCr,
       IFNULL(CASE WHEN credit_debit =1 THEN amount END,0) as trDr,
       3 as  type,
       voucher.society_code as society_code,
        d.name as society_name
FROM voucher as  voucher
LEFT JOIN voucher_types as vt ON vt.code=voucher.voucher_type_code
LEFT JOIN voucher_transaction as v ON v.voucher_code=voucher.code
LEFT JOIN ledgers as l ON l.code=v.ledger_code
LEFT JOIN  society as d ON d.code=voucher.society_code
WHERE voucher.voucher_date >= p_from_date AND voucher.voucher_date <= p_from_date AND voucher.society_code=p_society_code
and voucher.code in (SELECT distinct voucher.code
FROM voucher as  voucher
LEFT JOIN voucher_types as vt ON vt.code=voucher.voucher_type_code
LEFT JOIN voucher_transaction as v ON v.code=voucher.code
LEFT JOIN ledgers as l ON l.code=v.ledger_code
WHERE voucher.voucher_date >= p_from_date AND voucher.voucher_date <= p_from_date
AND l.code<>1 AND voucher.society_code=p_society_code)

union

 
SELECT  case when credit_debit = 1 then sum(amount) END as cr,
        case when credit_debit = 0 then sum(amount) END as db,
		'' as voucher_code,
	    'closing Balance' as ledger_name,
        0 as trCr,
        0 as trDr,
		4 as  type,
		voucher.society_code as society_code,
        d.name as society_name
FROM voucher as voucher
LEFT JOIN  voucher_transaction as  vt  ON vt.code=voucher.code
LEFT JOIN  ledgers as l ON l.code=vt.ledger_code
LEFT JOIN  society as d ON d.code=voucher.society_code
WHERE voucher.voucher_date < DATE_ADD(p_from_date ,INTERVAL 1 DAY)
AND l.code=1 AND cancelled=false   AND voucher.society_code=p_society_code
group by credit_debit,voucher.society_code,d.name)as A  group by voucher_code,ledger_name,A.society_code,A.society_name,type order by type ;
 
 
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_opening_balance`(IN p_society_code varchar(225), IN p_financial_year VARCHAR(255),IN p_locale varchar(20))
BEGIN
SELECT lop.code as ledger_code,CASE WHEN p_locale ='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
        lop.society_code,dcs.name as society_name,
        financial_years_code as financial_year_code,
        CASE WHEN credit_debit=1 THEN "CREDIT" ELSE "DEBIT" END as credit_debit,
        balance 
	FROM ledger_opening_balance as lop
    INNER JOIN ledgers as ledger ON ledger.code = lop.ledgers_code
    INNER JOIN society as dcs ON dcs.code =lop.society_code
    WHERE lop.society_code=p_society_code
	AND lop.financial_years_code=p_financial_year ORDER BY lop.ledgers_code;
 END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_profit_loss`(IN p_society_code varchar(225), IN p_from_date DATE,IN p_to_date DATE , IN p_income_expense INT,IN p_locale varchar(20))
BEGIN
-- call sp_rpt_accounting_profit_loss(1,'2022-05-01','2022-05-01',1,'en')
 
DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);
	
	SELECT 
		max(start_date) into start_date 
    FROM ledger_opening_balance as lop 
		INNER JOIN financial_years as fy ON fy.code=lop.financial_years_code 
	WHERE fy.start_date=p_from_date;
    
	SELECT 
       fy.code into financial_year 
    FROM financial_years as fy         
	WHERE fy.start_date = start_date;       
 
 
 IF p_income_expense = 0 THEN
	
	SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		(IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN  ledger_groups as ledger_groups ON ledger_groups.code=ledger.ledger_group_code
				INNER JOIN  ledger_types as ledger_types ON ledger_types.code=ledger_groups.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				ledger_types.profit_loss = true  
				AND (lob.financial_years_code = financial_year)
				AND (lob.society_code=p_society_code or lob.society_code is null)
			GROUP BY  ledger.name_local,lob.ledgers_code, ledger.name ,lob.credit_debit ORDER BY fy.start_date desc)
            
            UNION
            
            
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (vt.credit_debit=0)  then SUM(amount) END as DEBIT,
	            case when (vt.credit_debit=1)  then SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
				INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_groups ON ledger_groups.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_types ON ledger_types.code=ledger_groups.ledger_type_code
			WHERE 
				ledger_types.profit_loss = true  AND voucher.cancelled =false
				 AND (voucher.society_code=p_society_code or voucher.society_code is null)
				AND voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date 
			GROUP BY  ledger.name_local,vt.ledger_code, ledger.name,vt.credit_debit )
	) as A group by A.ledger_code ,A.ledger_name HAVING (IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0)))  < 0;
    
     
ELSE    

	
	SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		(IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN ledger_groups as ledger_groups ON ledger_groups.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_types ON ledger_types.code=ledger_groups.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				ledger_types.profit_loss = true
				AND (lob.financial_years_code = financial_year)
				AND (lob.society_code=p_society_code or lob.society_code is null)
			GROUP BY  ledger.name_local,ledger_code, ledger_name ,lob.credit_debit  ORDER BY fy.start_date desc)
            
            UNION
            
            
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (vt.credit_debit=0)  then SUM(amount) END as DEBIT,
	            case when (vt.credit_debit=1)  then SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
				INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_groups ON ledger_groups.code=ledger.ledger_group_code
				INNER JOIN ledger_types as ledger_types ON ledger_types.code=ledger_groups.ledger_type_code
			WHERE 
				ledger_types.profit_loss = true AND voucher.cancelled =false
				AND (voucher.society_code=p_society_code or voucher.society_code is null)
				AND voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date 
			GROUP BY  ledger.name_local,ledger_code, ledger.name,vt.credit_debit )
	) as A group by A.ledger_code ,A.ledger_name HAVING (IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0)))  >=0;
    
    END IF;
  
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_accounting_trading`(IN p_society_code varchar(225), IN p_from_date DATE,IN p_to_date DATE , IN p_locale VARCHAR(20))
BEGIN
-- call sp_rpt_accounting_trading('1010226','2022-04-02','2022-05-01','en')

DECLARE start_date DATE;
DECLARE financial_year VARCHAR(15);
SELECT 
		MAX(start_date) INTO start_date 
    FROM ledger_opening_balance AS lop 
		INNER JOIN financial_years AS fy ON fy.code=lop.financial_years_code 
	WHERE fy.start_date=p_from_date;
    
	SELECT 
       fy.code into financial_year 
    FROM financial_years as fy         
	WHERE fy.start_date = start_date;       
 
 
	
	SELECT 
		A.ledger_code as ledger_code,
        A.ledger_name as ledger_name,
		(IFNULL(round(SUM(CREDIT),2),0)+IFNULL(round(SUM(lob_CREDIT),2),0)) as CREDIT,
		(IFNULL(round(SUM(DEBIT),2),0)+IFNULL(round(SUM(lob_DEBIT),2),0)) as DEBIT,
		(IFNULL(ROUND(SUM(CREDIT),2),0)+IFNULL(ROUND(SUM(lob_CREDIT),2),0) - (IFNULL(ROUND(SUM(DEBIT),2),0)+IFNULL(ROUND(SUM(lob_DEBIT),2),0))) as Balance
	FROM(	
			
			(SELECT 
				lob.ledgers_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (lob.credit_debit=0)  then SUM(balance) END as lob_DEBIT,
	            case when (lob.credit_debit=1)  then SUM(balance) END as lob_CREDIT,
                0 as DEBIT,
                0 as CREDIT
			FROM ledger_opening_balance as lob
				INNER JOIN ledgers as ledger ON ledger.code=lob.ledgers_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
                INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
				INNER JOIN financial_years as fy ON fy.code=lob.financial_years_code
			WHERE 
				(ledger_type.code = 7 OR ledger_type.code=8) 
				AND (lob.financial_years_code = financial_year)
				AND (lob.society_code=p_society_code OR lob.society_code IS NULL)
			GROUP BY  lob.ledgers_code, ledger.name ,lob.credit_debit ORDER BY fy.start_date DESC)
            
            UNION
            
            
            (SELECT 
				vt.ledger_code as ledger_code,
				CASE WHEN p_locale='en' THEN ledger.name ELSE IFNULL(ledger.name_local,ledger.name) END  as ledger_name,
				case when (vt.credit_debit=0)  then SUM(amount) END as DEBIT,
	            case when (vt.credit_debit=1)  then SUM(amount) END as CREDIT,
                0 as lob_DEBIT,
                0 as lob_CREDIT
			FROM  voucher_transaction as vt 
				INNER JOIN voucher as voucher ON voucher.code=vt.voucher_code
				INNER JOIN ledgers as ledger ON ledger.code=vt.ledger_code
				INNER JOIN ledger_groups as ledger_group ON ledger_group.code=ledger.ledger_group_code
                INNER JOIN ledger_types as ledger_type ON ledger_type.code=ledger_group.ledger_type_code
			WHERE 
				(ledger_type.code = 7 OR ledger_type.code=8)  AND voucher.cancelled =false
				AND (voucher.society_code=p_society_code or voucher.society_code is null)
				AND voucher.voucher_date between case when start_date is null then p_from_date else start_date end and p_to_date 
			GROUP BY  vt.ledger_code, ledger.name,vt.credit_debit,ledger.name_local)
	) as A group by A.ledger_code ,A.ledger_name;
    
END ;;
DELIMITER ;

-- DELIMITER ;;
-- CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_milk_sale_dispatch_profit_loss`(IN p_society_code VARCHAR(20),IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_milk_type INT ,IN p_locale VARCHAR(2))
-- BEGIN
-- 
-- SELECT CONCAT(IFNULL(s.name,s.short_name),'-(',s.code_ex,')') as society_name,
--        CONCAT(IFNULL(u.name,u.name_local),'-(',u.code_ex,')') as union_name,
--        CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(p_from_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as p_from_date,
--        CONCAT(DATE_FORMAT(p_to_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(p_to_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as p_to_date,
--     CONCAT(DATE_FORMAT(collection_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(collection_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as collection_date,
--     ROUND(SUM(m_qty),2) AS milk_collection_qty,
--     ROUND(SUM(m_amount),2) AS milk_collection_amount,
--     ROUND(SUM(l_qty),2) AS local_sale_qty,
--     ROUND(SUM(l_amount),2) AS locak_sale_amount,
--     ROUND(SUM(m_qty) - SUM(l_qty),2) AS milk_collection_local_sale_qty,
--     ROUND(SUM(g_qty),2) AS good_qty,
--     ROUND(SUM(s_qty),2) AS Sour_qty,
--     ROUND(SUM(c_qty),2) AS Curd_qty,
--     ROUND(SUM(d_amount),2) AS dispatch_amount,
--     IFNULL(CAST(((IFNULL(SUM(d_amount), 0) - SUM(m_amount))) * 100/ SUM(m_amount)
--  
--     AS DECIMAL (18 , 2 )), 0) AS percentage,
--          0 as liter,
--     ROUND(SUM(g_qty) + SUM(s_qty) + SUM(c_qty),2) AS total_qty,
--      CASE
--         WHEN SUM(m_qty) = 0 THEN 0
--         ELSE ROUND(SUM(m_fat) / SUM(m_qty) * 100, 2)
--     END AS fat,
--      
--         sum(d_fat) as d_fat, 
-- 
--    CASE WHEN p_locale='en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name)  END   AS milk_type_name,
-- CASE WHEN p_milk_type=0 THEN 'ALL' ELSE  CASE WHEN p_locale='en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name)  END END AS milk_type_name_all,
--      ROUND((SUM(g_qty) + SUM(s_qty) + SUM(c_qty)) - (SUM(m_qty) - SUM(l_qty)),2) AS diff_qty,
--      ROUND((SUM(d_amount)) - (SUM(m_amount) - SUM(l_amount)),2) AS diff_amount,
--      ROUND((((SUM(d_amount) - SUM(m_amount) )* 100)/ SUM(m_amount)),2) AS amount_percentage
--     
-- FROM
--     (SELECT 
--         qty AS m_qty,
--             fat * qty / 100 AS m_fat,
--             amount AS m_amount,
--             0 AS l_qty,
--             0 AS l_fat,
--             0 AS l_amount,
--             collection_date,
--             0 AS g_qty,
--             0 AS s_qty,
--             0 AS c_qty,
--             0 AS d_fat,
--             0 AS d_amount,
--             milk_type_code
--     FROM
--         milk_collection WHERE  collection_date  BETWEEN p_from_date AND p_to_date 
--         AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
--         UNION ALL SELECT 
--         0 AS m_qty,
--             0 AS m_fat,
--             0 AS m_amount,
--             quantity AS l_qty,
--             0 AS l_fat,
--             amount AS l_amount,
--             sale_date,
--             0 AS g_qty,
--             0 AS s_qty,
--             0 AS c_qty,
--             0 AS d_fat,
--             0 AS d_amount,
--             milk_type_code
--     FROM
--         local_milk_sale WHERE  sale_date BETWEEN p_from_date AND p_to_date 
--         AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
--         UNION ALL SELECT 
--         0 AS m_qty,
--             0 AS m_fat,
--             0 AS m_amount,
--             0 AS l_qty,
--             0 AS l_fat,
--             0 AS l_amount,
--             md.from_date AS d_dates,
--             mdt.converted_quantity AS g_qty,
--             0 AS s_qty,
--             0 AS c_qty,
--             avg_fat d_fat,
--             mdt.amount AS d_amount,
--             milk_type_code
--     FROM
--          milk_receipt AS md
--     INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
-- 
-- WHERE
--     mdt.milk_quality_type_code = 1 AND   from_Date >= p_from_date AND  to_date   <= p_to_date
--     AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
--     UNION ALL SELECT 
--     0 AS m_qty,
--         0 AS m_fat,
--         0 AS m_amount,
--         0 AS l_qty,
--         0 AS l_fat,
--         0 AS l_amount,
--         md.from_date AS d_dates,
--         0 AS g_qty,
--         mdt.converted_quantity AS s_qty,
--         0 AS c_qty,
--         avg_fat d_fat,
--         mdt.amount,
--         milk_type_code
-- FROM
--      milk_receipt AS md
-- INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
-- WHERE
--     mdt.milk_quality_type_code = 2 and  from_Date >= p_from_date AND  to_date   <= p_to_date     UNION ALL SELECT 
--     0 AS m_qty,
--         0 AS m_fat,
--         0 AS m_amount,
--         0 AS l_qty,
--         0 AS l_fat,
--         0 AS l_amount,
--         md.from_date AS d_dates,
--         0 AS g_qty,
--         0 AS s_qty,
--         mdt.converted_quantity AS c_qty,
--         avg_fat d_fat,
--         mdt.amount,
--         milk_type_code
-- FROM
--     milk_receipt AS md
-- INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
-- WHERE
--     mdt.milk_quality_type_code = 3 AND   from_Date >= p_from_date AND  to_date   <= p_to_date
-- 
--     AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END   ) AS A
--     INNER JOIN
-- milk_types AS mt ON mt.code = A.milk_type_code
--   INNER JOIN society as s on s.code=p_society_code
--   INNER JOIN unions as u on u.code=s.union_code
--    where      milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END 
-- 
-- GROUP BY mt.name , collection_date,mt.name_local,s.name,s.short_name,u.name,u.name_local,s.code_ex,u.code_ex
-- ORDER BY milk_type_name Asc,collection_date  ;
-- 
-- END;;
-- DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_billing_prouct_sale_deduction_consolidated`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_locale varchar(20) )
BEGIN
-- call rpt_billing_prouct_sale_deduction_consolidated('1031700', '0', '2020-04-25 06:00:00', '2023-12-28 18:00:00','en' );
SELECT 
    CASE WHEN p_locale ='en' THEN pp.name ELSE  IFNULL(pp.name_local,pp.name)  END AS product_short_name,
    ROUND((IFNULL(SUM(pst.amount), 0)),
            2) AS netamount,1 sr
FROM
    product_sale_transaction pst 
        INNER JOIN
        product_sale ps on ps.invoice_no=pst.invoice_no INNER join
    products AS pp ON pp.code = pst.product_code
    
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code  AND ps.invoice_no=psi.invoice_no
         -- AND psi.society_payment_cycle_code=p_society_payment_cycle_code
          
INNER JOIn  society_payment_cycles as sco ON sco.code=psi.society_payment_cycle_code
WHERE sco.from_date >=p_from_date and sco.to_date <=p_to_date
 
GROUP BY   pp.name ,pp.name_local

UNION ALL

SELECT 
   'Product Deduction'    AS product_short_name,
    ROUND((IFNULL(SUM(pst.amount), 0)),
            2) AS netamount,2 sr
FROM
    product_sale_transaction pst 
        INNER JOIN
        product_sale ps on ps.invoice_no=pst.invoice_no left join
    products AS pp ON pp.code = pst.product_code
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code  AND ps.invoice_no=psi.invoice_no

INNER JOIn  society_payment_cycles as sco ON sco.code=psi.society_payment_cycle_code
WHERE sco.from_date >=p_from_date and sco.to_date <=p_to_date

UNION ALL 
select 'Net Payable'    AS product_short_name,SUM(IFNULL(netamount,0)-IFNULL(ps_amount,0)) as netamount, 3 as sr from 
(
select 
    ROUND((IFNULL(SUM(amount), 0)),
            2) AS netamount ,0 as ps_amount from milk_collection where collection_date BETWEEN p_from_date and p_to_date
   UNION ALL
   
            
SELECT 
    
   0 as netamount, ROUND((IFNULL(SUM(pst.amount), 0)),
            2) AS ps_amount
FROM
    product_sale_transaction pst 
        INNER JOIN
        product_sale ps on ps.invoice_no=pst.invoice_no left join
    products AS pp ON pp.code = pst.product_code
       INNER JOIN product_sale_installment as psi
          ON psi.member_code=ps.consumer_code  AND ps.invoice_no=psi.invoice_no

INNER JOIn  society_payment_cycles as sco ON sco.code=psi.society_payment_cycle_code
WHERE sco.from_date >=p_from_date and sco.to_date <=p_to_date) as R
  
 order by sr asc
 ;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_milk_sale_dispatch_profit_loss`(IN p_society_code VARCHAR(20),IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_milk_type INT ,IN p_locale VARCHAR(2))
BEGIN

SELECT CONCAT(IFNULL(s.name,s.short_name),'-(',s.code_ex,')') as society_name,
       CONCAT(IFNULL(u.name,u.name_local),'-(',u.code_ex,')') as union_name,
       CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(p_from_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as p_from_date,
       CONCAT(DATE_FORMAT(p_to_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(p_to_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as p_to_date,
    CONCAT(DATE_FORMAT(collection_date,'%d/%m/%y'),'-',CASE WHEN SUBSTRING(collection_date,12,8)='06:00:00' THEN 'M' ELSE 'E' END ) as collection_date,
    ROUND(SUM(m_qty),2) AS milk_collection_qty,
    ROUND(SUM(m_amount),2) AS milk_collection_amount,
    ROUND(SUM(l_qty),2) AS local_sale_qty,
    ROUND(SUM(l_amount),2) AS locak_sale_amount,
    ROUND(SUM(m_qty) - SUM(l_qty),2) AS milk_collection_local_sale_qty,
    ROUND(SUM(g_qty),2) AS good_qty,
    ROUND(SUM(s_qty),2) AS Sour_qty,
    ROUND(SUM(c_qty),2) AS Curd_qty,
    ROUND(SUM(d_amount),2) AS dispatch_amount,
    IFNULL(CAST(((IFNULL(SUM(d_amount), 0) - SUM(m_amount))) * 100/ SUM(m_amount)
 
    AS DECIMAL (18 , 2 )), 0) AS percentage,
         0 as liter,
    ROUND(SUM(g_qty) + SUM(s_qty) + SUM(c_qty),2) AS total_qty,
     CASE
        WHEN SUM(m_qty) = 0 THEN 0
        ELSE ROUND(SUM(m_fat) / SUM(m_qty) * 100, 2)
    END AS fat,
     
        sum(d_fat) as d_fat, 

   CASE WHEN p_locale='en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name)  END   AS milk_type_name,
CASE WHEN p_milk_type=0 THEN 'ALL' ELSE  CASE WHEN p_locale='en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name)  END END AS milk_type_name_all,
     ROUND((SUM(g_qty) + SUM(s_qty) + SUM(c_qty)) - (SUM(m_qty) - SUM(l_qty)),2) AS diff_qty,
     ROUND((SUM(d_amount)) - (SUM(m_amount) - SUM(l_amount)),2) AS diff_amount,
     ROUND(((((SUM(d_amount)) - (SUM(m_amount) - SUM(l_amount)))* 100)/ SUM(m_amount)),2) AS amount_percentage
    
FROM
    (SELECT 
        qty AS m_qty,
            fat * qty / 100 AS m_fat,
            amount AS m_amount,
            0 AS l_qty,
            0 AS l_fat,
            0 AS l_amount,
            collection_date,
            0 AS g_qty,
            0 AS s_qty,
            0 AS c_qty,
            0 AS d_fat,
            0 AS d_amount,
            milk_type_code
    FROM
        milk_collection WHERE  collection_date  BETWEEN p_from_date AND p_to_date 
        AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
        UNION ALL SELECT 
        0 AS m_qty,
            0 AS m_fat,
            0 AS m_amount,
            quantity AS l_qty,
            0 AS l_fat,
            amount AS l_amount,
            sale_date,
            0 AS g_qty,
            0 AS s_qty,
            0 AS c_qty,
            0 AS d_fat,
            0 AS d_amount,
            milk_type_code
    FROM
        local_milk_sale WHERE  sale_date BETWEEN p_from_date AND p_to_date 
        AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
        UNION ALL SELECT 
        0 AS m_qty,
            0 AS m_fat,
            0 AS m_amount,
            0 AS l_qty,
            0 AS l_fat,
            0 AS l_amount,
            md.from_date AS d_dates,
            mdt.converted_quantity AS g_qty,
            0 AS s_qty,
            0 AS c_qty,
            avg_fat d_fat,
            mdt.amount AS d_amount,
            milk_type_code
    FROM
         milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code

WHERE
    mdt.milk_quality_type_code = 1 AND   from_Date >= p_from_date AND  to_date   <= p_to_date
    AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END
    UNION ALL SELECT 
    0 AS m_qty,
        0 AS m_fat,
        0 AS m_amount,
        0 AS l_qty,
        0 AS l_fat,
        0 AS l_amount,
        md.from_date AS d_dates,
        0 AS g_qty,
        mdt.converted_quantity AS s_qty,
        0 AS c_qty,
        avg_fat d_fat,
        mdt.amount,
        milk_type_code
FROM
     milk_receipt AS md
INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
WHERE
    mdt.milk_quality_type_code = 2 and  from_Date >= p_from_date AND  to_date   <= p_to_date     UNION ALL SELECT 
    0 AS m_qty,
        0 AS m_fat,
        0 AS m_amount,
        0 AS l_qty,
        0 AS l_fat,
        0 AS l_amount,
        md.from_date AS d_dates,
        0 AS g_qty,
        0 AS s_qty,
        mdt.converted_quantity AS c_qty,
        avg_fat d_fat,
        mdt.amount,
        milk_type_code
FROM
    milk_receipt AS md
INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
WHERE
    mdt.milk_quality_type_code = 3 AND   from_Date >= p_from_date AND  to_date   <= p_to_date

    AND milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END   ) AS A
    INNER JOIN
milk_types AS mt ON mt.code = A.milk_type_code
  INNER JOIN society as s on s.code=p_society_code
  INNER JOIN unions as u on u.code=s.union_code
   where      milk_type_code= CASE WHEN p_milk_type=0 THEN milk_type_code ELSE p_milk_type END 

GROUP BY mt.name , collection_date,mt.name_local,s.name,s.short_name,u.name,u.name_local,s.code_ex,u.code_ex
ORDER BY milk_type_name Asc,collection_date  ;

END ;;
DELIMITER ;




DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_summary`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_locale varchar(20) )
BEGIN


SELECT   IFNULL(SUM(CASE WHEN mc.milk_type_code=1 THEN  (qty)  END),0)  as cow_qty,
		 IFNULL(SUM(CASE WHEN mc.milk_type_code=2 THEN  (qty)   END),0)  as buaff_qty,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=3 THEN  (qty)   END),0)  as mix_qty,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=4 THEN  (qty)   END),0)  as a2_cow_qty,
		 
		 IFNULL(SUM(CASE WHEN mc.milk_type_code=1 THEN ROUND((amount),2) ELSE 0 END),0) as cow_amount,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=2 THEN ROUND((amount),2)ELSE 0 END),0) as buff_amount,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=3 THEN ROUND((amount),2) ELSE 0 END),0) as mix_amount ,
         IFNULL(SUM(CASE WHEN mc.milk_type_code=4 THEN ROUND((amount),2) ELSE 0 END),0) as a2_cow_amount ,
         
         IFNULL(COUNT(DISTINCT (CASE WHEN mc.milk_type_code=1 THEN mc.member_code ELSE 0 END))-1,0)    as cow_memnber_count,
		 IFNULL(COUNT(DISTINCT (CASE WHEN mc.milk_type_code=2 THEN mc.member_code ELSE 0 END))-1 ,0)as buaff_memnber_count,
		 IFNULL(COUNT(DISTINCT(CASE WHEN mc.milk_type_code=3 THEN mc.member_code ELSE 0 END))-1,0)  as mix_memnber_count,
		 IFNULL(COUNT(DISTINCT(CASE WHEN mc.milk_type_code=4 THEN mc.member_code ELSE 0 END))-1,0)  as a2_cow_memnber_count,
         IFNULL(c_fat,0) as c_fat,
		 IFNULL(b_fat,0) as b_fat,
		 IFNULL(m_fat,0) as m_fat,
		 IFNULL(a_fat,0) as a_fat,
		  IFNULL(c_snf,0) as c_snf,
		 IFNULL(b_snf,0) as b_snf,
		 IFNULL(m_snf,0) as m_snf,
		 IFNULL(a_snf,0) as a_snf,
        concat(mc.society_code,'-', CASE WHEN p_locale = 'EN' THEN s.name ELSE IFNULL(s.name_local,s.name ) END ) as society_code ,
        p_from_date as from_date,
        p_to_date as to_date 
FROM
    milk_collection mc
    INNER JOIN milk_types as mt on mt.code=mc.milk_type_code
    Left join society as s on mc.society_code=s.code
     LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2) as c_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2) as c_snf  FROM milk_collection mc
              where  milk_type_code=1  AND  mc.collection_date  
                BETWEEN p_from_date and p_to_date AND milk_type_code=1  
                 AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
               
                ) as B ON 1=1
    LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as b_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as b_snf FROM milk_collection mc  
                where  milk_type_code=2   AND  mc.collection_date  
                 BETWEEN p_from_date and p_to_date AND milk_type_code=2  
                 AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END  
               
               ) as C  ON 1=1        
     LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as m_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as m_snf FROM milk_collection mc 
                 where  milk_type_code=3 AND  mc.collection_date  
                  BETWEEN p_from_date and p_to_date AND milk_type_code=3
                  AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
                
                ) as D    ON 1=1   
	LEFT JOIN (SELECT ROUND(SUM(qty*fat/100)/ SUM(qty)*100,2)as a_fat,ROUND(SUM(qty*snf/100)/ SUM(qty)*100,2)as a_snf FROM milk_collection mc 
                 where  milk_type_code=4 AND  mc.collection_date  
                  BETWEEN p_from_date and p_to_date AND milk_type_code=4
                  AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
                
                ) as E    ON 1=1    

 WHERE
     mc.collection_date   BETWEEN p_from_date and p_to_date
     AND mc.member_code=CASE WHEN p_member_code = 0 THEN mc.member_code ELSE p_member_code END 
   
    group by  c_fat, s.name,s.name_local,mc.society_code,
         b_fat,
         m_fat,
         a_fat, c_snf,b_snf,m_snf,a_snf
   ;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_product_sale_details`(IN p_society_code VARCHAR(50),IN p_product_code VARCHAR(50),IN p_from_date DATE,IN p_to_date DATE,IN p_locale VARCHAR(50))
BEGIN

SELECT 
    CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(p_to_date,'%d/%m/%Y')) as pc_date,
    sum(pst.net_amount) AS net_amount,
    CASE WHEN p_locale ='en' THEN p.name ELSE  IFNULL(p.name_local,p.name)  END AS product_name,

    sum(pst.quantity) AS quantity,
    avg(pst.rate) AS rate,    
    pst.society_code AS society_name_code,
    CASE WHEN p_locale ='en' THEN ss.short_name ELSE  IFNULL(ss.short_name_local,ss.short_name) END AS society_name
    
FROM
    product_sale AS ps
        INNER JOIN
    product_sale_transaction AS pst ON pst.invoice_no = ps.invoice_no
         
        LEFT JOIN
    products AS p ON p.code = pst.product_code
        LEFT JOIN
    product_groups AS pss ON pss.code = p.product_group_code
        LEFT JOIN
    units AS u ON u.code = pss.base_unit
        LEFT JOIN
    society AS ss ON ss.code = pst.society_code
        LEFT JOIN
    unions AS unions ON unions.code = pst.union_code
    WHERE pst.society_code=p_society_code
    AND ps.invoice_date BETWEEN p_from_date AND p_to_date
    And case when p_product_code=0 then p.code=p.code else p.code=p_product_code end
    group by p.code;
    END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_product_purchase`(IN p_from_date DATE,IN p_to_date DATE ,IN p_society_code VARCHAR(12), IN p_locale VARCHAR(2), IN p_product_code VARCHAR(30))
BEGIN

	select cast(product_code as char) as productCode,'' as productName,cast(sum(quantity) as decimal) as stock,cast(sum(prt.net_amount) as decimal) as valuation,'' as unit from product_receipt_transaction prt join product_receipt pr on prt.grn_no=pr.grn_no
    where pr.challan_date between p_from_date and p_to_date 
                and case when p_product_code=0 then prt.product_code=prt.product_code else prt.product_code=p_product_code end

    group by product_code order by prt.product_code;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_product_sale`(IN p_from_date DATE,IN p_to_date DATE ,IN p_society_code VARCHAR(12), IN p_locale VARCHAR(2), IN p_product_code VARCHAR(30))
BEGIN

	select cast(product_code as char) as productCode,'' as productName,cast(sum(quantity) as decimal) as stock,cast(sum(pst.net_amount) as decimal) as valuation,'' as unit from product_sale_transaction pst join product_sale ps on pst.invoice_no=ps.invoice_no
    where ps.invoice_date between p_from_date and p_to_date
                and case when p_product_code=0 then pst.product_code=pst.product_code else pst.product_code=p_product_code end

    group by product_code order by pst.product_code;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_product_transaction`(IN p_dcs_code varchar(225), IN p_from_date date, IN p_to_date date, IN p_language_code INT,IN p_product_code VARCHAR(255), IN p_sub_center_code VARCHAR(255))
BEGIN
IF p_language_code>11 THEN 

SELECT  DATE_FORMAT(p_from_date, '%d/%m/%y') AS from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') AS to_date,
		DATE_FORMAT(A.date , '%d/%m/%y')AS T_date,
        A.product_code,
        A.product_name,
        A.unit_name,
        SUM(C.Opning_balance) as Opning_balance, 
        SUM(A.purchase_qty) as Purchase_Qty,
        SUM(A.sale_qty) as Sale_Qty ,
        (SUM(C.Opning_balance)+SUM(A.purchase_qty)-SUM(A.sale_qty))as Closing_Qty
 FROM
(SELECT grn_date as date, 
        sum(Purchase_qty) as purchase_qty,
        0 as sale_qty, 
        product_code,
        product_name,
        unit_name 
FROM 
(SELECT prm.grn_date,
        sum(prt.received_quantity) AS purchase_qty,
        prt.product_code as product_code,
        p.product_name as product_name,
        U.unit_name as unit_name
FROM tbl_product_receipt_transaction prt 
INNER JOIN tbl_product_receipt_material prm ON prm.grn_no = prt.grn_no 
INNER JOIN tbl_product as p ON p.product_code=prt.product_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
WHERE prm.grn_date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND prt.product_code= case p_product_code when 0 then prt.product_code else p_product_code end
AND (prt.dcs_code = p_dcs_code or prt.dcs_code IS NULL) AND (prt.sub_center_code = p_sub_center_code or prt.sub_center_code IS NULL)
GROUP BY prt.product_code,prm.grn_date,p.product_name,U.unit_name
Union 
SELECT pst.transaction_date as grn_date,
		 sum(pst.new_value) as purchase_qty,
         pst.product_code as product_code,
         p.product_name as product_name,
         U.unit_name as unit_name
FROM tbl_product_stock_transaction as pst
INNER JOIN tbl_product as p ON p.product_code=pst.product_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
WHERE transaction_date >= p_from_date and transaction_date <= p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
AND pst.transaction_type in ('Converted Addition') GROUP BY pst.transaction_date, pst.product_code,p.product_name,U.unit_name
) A GROUP BY  grn_date, product_code,product_name,unit_name

UNION


select date,0 as purchase_qty,  sum(sale_qty) as sale_qty, product_code,product_name,unit_name from
(SELECT psm.date,
        sum(psmt.quantity) AS sale_qty,
        psmt.product_code as product_code,
        p.product_name as product_name,
        U.unit_name as unit_name
        FROM tbl_product_sale_to_member_transaction psmt 
INNER JOIN tbl_product_sale_to_member psm ON psm.invoice_no = psmt.invoice_no 
INNER JOIN tbl_product as p ON p.product_code=psmt.product_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
WHERE psm.date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND psmt.product_code= case p_product_code when 0 then psmt.product_code else p_product_code end
AND (psmt.dcs_code = p_dcs_code or psmt.dcs_code IS NULL) AND (psmt.sub_center_code = p_sub_center_code or psmt.sub_center_code IS NULL)
GROUP BY psmt.product_code,psm.date,p.product_name, U.unit_name
Union 
SELECT transaction_date as date, 
       sum(new_value) as sale_qty,
	   pst.product_code as product_code,
       p.product_name as product_name,
       U.unit_name as unit_name
       FROM tbl_product_stock_transaction as pst
INNER JOIN tbl_product as p ON p.product_code=pst.product_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
WHERE transaction_date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
AND transaction_type in ('Converted deduction') GROUP BY transaction_date, product_code,p.product_name,U.unit_name) A
GROUP BY date, product_code,product_name,unit_name) as A 
INNER JOIN


 (SELECT C.date,SUM(C.Opning_balance) as Opning_balance ,C.product_code,'' as product_name,'' as unit_name FROM(
      SELECT A.date,SUM(A.Opning_balance) as Opning_balance,A.product_code,'' as product_name,  '' as unit_name FROM
      (SELECT  d.str_date as date,
	           sum(prt.received_quantity)  AS Opning_balance,
			   prt.product_code  as product_code,
               '' as product_name,
               '' as unit_name
      FROM tbl_product_receipt_transaction prt 
	  INNER JOIN tbl_product_receipt_material prm ON prm.grn_no = prt.grn_no
      INNER JOIN tbl_product as p ON p.product_code=prt.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE prm.grn_date < d.str_date 
      AND p.product_code != 1 AND p.product_code != 2 
      AND  prt.product_code= case p_product_code when 0 then prt.product_code else p_product_code end
      AND (prt.dcs_code = p_dcs_code or prt.dcs_code IS NULL) AND (prt.sub_center_code = p_sub_center_code or prt.sub_center_code IS NULL)
	  GROUP BY prt.product_code,d.str_date
      
      Union 
      SELECT d.str_date as date,
      sum(pst.new_value) as Opning_balance,
      pst.product_code as product_code,
      '' as product_name,
      '' as unit_name
      FROM tbl_product_stock_transaction as pst
	  INNER JOIN tbl_product as p ON p.product_code=pst.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      where transaction_date < d.str_date
	  AND transaction_type in ('Converted Addition') 
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
      AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
      group by d.str_date,  pst.product_code
      ) A group by  A.date, A.product_code
 
      
      UNION
      SELECT B.date,SUM(B.Opning_balance) as Opning_balance ,B.product_code,'' as product_name,'' as unit_name 
      FROM
      (SELECT  d.str_date as date,
      (sum(psmt.quantity)*-1)  AS Opning_balance,
      psmt.product_code as product_code,
      '' as product_name,
      '' as unit_name
      FROM tbl_product_sale_to_member_transaction psmt 
	  INNER JOIN tbl_product_sale_to_member psm ON psm.invoice_no = psmt.invoice_no 
	  INNER JOIN tbl_product as p ON p.product_code=psmt.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE psm.date < d.str_date
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  psmt.product_code= case p_product_code when 0 then psmt.product_code else p_product_code end
      AND (psmt.dcs_code = p_dcs_code or psmt.dcs_code IS NULL) AND (psmt.sub_center_code = p_sub_center_code or psmt.sub_center_code IS NULL)
      GROUP BY psmt.product_code,d.str_date
      
      Union 
      
      SELECT d.str_date as date,
      (sum(new_value)*-1) as Opning_balance,
      pst.product_code,
	  '' as product_name,
      '' as unit_name
      FROM tbl_product_stock_transaction pst
      INNER JOIN tbl_product as  p ON p.product_code=pst.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE transaction_date < d.str_date
	  AND transaction_type in ('Converted Addition') 
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
      AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
      GROUP BY d.str_date, pst.product_code
      ) as B GROUP BY B.date,B.product_code
      ) as C GROUP BY C.date,C.product_code
      ) as C ON C.date=A.date 
		AND C.product_code=A.product_code GROUP BY A.date,A.product_code,A.product_name,A.unit_name 
        order by A.product_code ,A.date;
        
 ELSE       



SELECT  DATE_FORMAT(p_from_date, '%d/%m/%y') AS from_date,
		DATE_FORMAT(p_to_date, '%d/%m/%y') AS to_date,
		DATE_FORMAT(A.date , '%d/%m/%y')AS T_date,
        A.product_code,
        A.product_name,
        A.unit_name,
        SUM(C.Opning_balance) as Opning_balance, 
        SUM(A.purchase_qty) as Purchase_Qty,
        SUM(A.sale_qty) as Sale_Qty ,
        (SUM(C.Opning_balance)+SUM(A.purchase_qty)-SUM(A.sale_qty))as Closing_Qty
 FROM
(SELECT grn_date as date, 
        sum(Purchase_qty) as purchase_qty,
        0 as sale_qty, 
        product_code,
        product_name,
        unit_name 
FROM 
(SELECT prm.grn_date,
        sum(prt.received_quantity) AS purchase_qty,
        prt.product_code as product_code,
	    CASE WHEN pl.local_name IS NULL THEN p.product_name ELSE pl.local_name END  as product_name,
        CASE WHEN UL.local_name IS  NULL THEN  U.unit_name ELSE UL.local_name END as unit_name
FROM tbl_product_receipt_transaction prt 
INNER JOIN tbl_product_receipt_material prm ON prm.grn_no = prt.grn_no 
INNER JOIN tbl_product as p ON p.product_code=prt.product_code
LEFT OUTER JOIN tbl_product_local as pl ON pl.product_code=p.product_code AND pl.language_code=p_language_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
LEFT OUTER JOIN tbl_units_local as UL ON UL.unit_code=U.unit_code AND UL.language_code=p_language_code
WHERE prm.grn_date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND prt.product_code= case p_product_code when 0 then prt.product_code else p_product_code end
AND (prt.dcs_code = p_dcs_code or prt.dcs_code IS NULL) AND (prt.sub_center_code = p_sub_center_code or prt.sub_center_code IS NULL)
GROUP BY prt.product_code,prm.grn_date,p.product_name,U.unit_name,pl.local_name,UL.local_name
Union 
SELECT pst.transaction_date as grn_date,
		 sum(pst.new_value) as purchase_qty,
         pst.product_code as product_code,
         CASE WHEN pl.local_name IS NULL THEN p.product_name ELSE pl.local_name END  as product_name,
        CASE WHEN UL.local_name IS  NULL THEN  U.unit_name ELSE UL.local_name END as unit_name
FROM tbl_product_stock_transaction as pst
INNER JOIN tbl_product as p ON p.product_code=pst.product_code
LEFT OUTER JOIN tbl_product_local as pl ON pl.product_code=p.product_code AND pl.language_code=p_language_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
LEFT OUTER JOIN tbl_units_local as UL ON UL.unit_code=U.unit_code AND UL.language_code=p_language_code
WHERE transaction_date >= p_from_date and transaction_date <= p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
AND pst.transaction_type in ('Converted Addition') GROUP BY pst.transaction_date, pst.product_code,p.product_name,U.unit_name,pl.local_name,UL.local_name
) A GROUP BY  grn_date, product_code,product_name,unit_name

UNION


select date,0 as purchase_qty,  sum(sale_qty) as sale_qty, product_code,product_name,unit_name from
(SELECT psm.date,
        sum(psmt.quantity) AS sale_qty,
        psmt.product_code as product_code,
		CASE WHEN pl.local_name IS NULL THEN p.product_name ELSE pl.local_name END  as product_name,
        CASE WHEN UL.local_name IS  NULL THEN  U.unit_name ELSE UL.local_name END as unit_name
        FROM tbl_product_sale_to_member_transaction psmt 
INNER JOIN tbl_product_sale_to_member psm ON psm.invoice_no = psmt.invoice_no 
INNER JOIN tbl_product as p ON p.product_code=psmt.product_code
LEFT OUTER JOIN tbl_product_local as pl ON pl.product_code=p.product_code AND pl.language_code=p_language_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
LEFT OUTER JOIN tbl_units_local as UL ON UL.unit_code=U.unit_code AND UL.language_code=p_language_code
WHERE psm.date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND psmt.product_code= case p_product_code when 0 then psmt.product_code else p_product_code end
AND (psmt.dcs_code = p_dcs_code or psmt.dcs_code IS NULL) AND (psmt.sub_center_code = p_sub_center_code or psmt.sub_center_code IS NULL)
GROUP BY psmt.product_code,psm.date,p.product_name, U.unit_name,pl.local_name,UL.local_name
Union 
SELECT transaction_date as date, 
       sum(new_value) as sale_qty,
	   pst.product_code as product_code,
       CASE WHEN pl.local_name IS NULL THEN p.product_name ELSE pl.local_name END  as product_name,
	   CASE WHEN UL.local_name IS  NULL THEN  U.unit_name ELSE UL.local_name END as unit_name
       FROM tbl_product_stock_transaction as pst
INNER JOIN tbl_product as p ON p.product_code=pst.product_code
LEFT OUTER JOIN tbl_product_local as pl ON pl.product_code=p.product_code AND pl.language_code=p_language_code
INNER JOIN tbl_units as U ON U.unit_code=p.primary_uom
LEFT OUTER JOIN tbl_units_local as UL ON UL.unit_code=U.unit_code AND UL.language_code=p_language_code
WHERE transaction_date BETWEEN p_from_date AND p_to_date
AND p.product_code != 1 AND p.product_code != 2 
AND pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL) 
AND transaction_type in ('Converted deduction') GROUP BY transaction_date, product_code,p.product_name,U.unit_name,pl.local_name,UL.local_name) A
GROUP BY date, product_code,product_name,unit_name) as A 
INNER JOIN


 (SELECT C.date,SUM(C.Opning_balance) as Opning_balance ,C.product_code,'' as product_name,'' as unit_name FROM(
      SELECT A.date,SUM(A.Opning_balance) as Opning_balance,A.product_code,'' as product_name,  '' as unit_name FROM
      (SELECT  d.str_date as date,
	           sum(prt.received_quantity)  AS Opning_balance,
			   prt.product_code  as product_code,
               '' as product_name,
               '' as unit_name
      FROM tbl_product_receipt_transaction prt 
	  INNER JOIN tbl_product_receipt_material prm ON prm.grn_no = prt.grn_no
      INNER JOIN tbl_product as p ON p.product_code=prt.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE prm.grn_date < d.str_date 
      AND p.product_code != 1 AND p.product_code != 2 
      AND  prt.product_code= case p_product_code when 0 then prt.product_code else p_product_code end
      AND (prt.dcs_code = p_dcs_code or prt.dcs_code IS NULL) AND (prt.sub_center_code = p_sub_center_code or prt.sub_center_code IS NULL)
	  GROUP BY prt.product_code,d.str_date
      
      Union 
      SELECT d.str_date as date,
      sum(pst.new_value) as Opning_balance,
      pst.product_code as product_code,
      '' as product_name,
      '' as unit_name
      FROM tbl_product_stock_transaction as pst
	  INNER JOIN tbl_product as p ON p.product_code=pst.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      where transaction_date < d.str_date
	  AND transaction_type in ('Converted Addition') 
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
      AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
      group by d.str_date,  pst.product_code
      ) A group by  A.date, A.product_code
 
      
      UNION
      SELECT B.date,SUM(B.Opning_balance) as Opning_balance ,B.product_code,'' as product_name,'' as unit_name 
      FROM
      (SELECT  d.str_date as date,
      (sum(psmt.quantity)*-1)  AS Opning_balance,
      psmt.product_code as product_code,
      '' as product_name,
      '' as unit_name
      FROM tbl_product_sale_to_member_transaction psmt 
	  INNER JOIN tbl_product_sale_to_member psm ON psm.invoice_no = psmt.invoice_no 
	  INNER JOIN tbl_product as p ON p.product_code=psmt.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE psm.date < d.str_date
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  psmt.product_code= case p_product_code when 0 then psmt.product_code else p_product_code end
      AND (psmt.dcs_code = p_dcs_code or psmt.dcs_code IS NULL) AND (psmt.sub_center_code = p_sub_center_code or psmt.sub_center_code IS NULL)
      GROUP BY psmt.product_code,d.str_date
      
      Union 
      
      SELECT d.str_date as date,
      (sum(new_value)*-1) as Opning_balance,
      pst.product_code,
	  '' as product_name,
      '' as unit_name
      FROM tbl_product_stock_transaction pst
      INNER JOIN tbl_product as  p ON p.product_code=pst.product_code
      
      INNER JOIN day_intervals_new as d ON d.str_date=d.str_date
      WHERE transaction_date < d.str_date
	  AND transaction_type in ('Converted Addition') 
	  AND p.product_code != 1 AND p.product_code != 2 
      AND  pst.product_code= case p_product_code when 0 then pst.product_code else p_product_code end
      AND (pst.dcs_code = p_dcs_code or pst.dcs_code IS NULL) AND (pst.sub_center_code = p_sub_center_code or pst.sub_center_code IS NULL)
      GROUP BY d.str_date, pst.product_code
      ) as B GROUP BY B.date,B.product_code
      ) as C GROUP BY C.date,C.product_code
      ) as C ON C.date=A.date 
		AND C.product_code=A.product_code GROUP BY A.date,A.product_code,A.product_name,A.unit_name 
        order by A.product_code ,A.date;


END IF;

END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_current_stock_for_product_with_product`(IN p_as_on_date DATE ,IN p_society_code VARCHAR(12), IN p_locale VARCHAR(2), IN p_product_code VARCHAR(30))
BEGIN
-- CALL sp_current_stock_for_product('2022-05-01','1010226','en')

SELECT DISTINCT(p.code) as product_code,CASE WHEN p_locale='en' THEN p.name ELSE IFNULL(p.name_local,p.name) END  as product_name, 
sum(IFNULL(pst.new_value, 0)) as  stock, u.name as unit_name
    FROM
        products p 
        LEFT JOIN product_stock ps ON p.code = ps.product_code
        LEFT JOIN product_stock_transaction as pst ON pst.product_code=ps.product_code
        LEFT JOIN units u ON u.code = p.primary_uom_code
    WHERE 
        (p.society_code IS NULL OR ps.society_code = p_society_code) 
        AND pst.transaction_date <=p_as_on_date
        and case when p_product_code=0 then p.code=p.code else p.code=p_product_code end
        AND p.code NOT IN ('1', '2') group by p.code ORDER BY p.code

        ;
  
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_code_wise_product_sale_detail`(IN p_society_code VARCHAR(50),IN p_from_date VARCHAR(50),IN p_to_date VARCHAR(50),IN p_locale VARCHAR(50))
BEGIN

SELECT 
    CONCAT(DATE_FORMAT(p_from_date,'%d/%m/%Y'),' To ',DATE_FORMAT(p_to_date,'%d/%m/%Y')) as pc_date,
    RIGHT( ps.consumer_code,4) AS consumer_code,
       DATE_FORMAT(ps.invoice_date, '%d/%m/%Y') AS invoice_date,
       pst.net_amount,
       ss.code as society_code,
       ss.name as society_name
FROM
    product_sale AS ps
        INNER JOIN
    product_sale_transaction AS pst ON pst.invoice_no = ps.invoice_no

    LEFT JOIN
members AS member ON ps.consumer_code = member.code
    LEFT JOIN
society AS ss ON ss.code = pst.society_code
WHERE pst.society_code=p_society_code
AND ps.invoice_date BETWEEN p_from_date AND p_to_date
ORDER BY ps.consumer_code ;
END;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetBonusSummary`(
    IN p_from_date DATE,
    IN p_to_date DATE,IN p_milk_type_code varchar(1)
)
BEGIN
    SELECT
        concat(m.code_ex," - ",m.first_name) as NAME,
        
        concat(bs.from_date," / ",bs.to_date) as PERIOD,
        ROUND(SUM(b.milk_qty), 2) as QTY,
        ROUND(SUM(b.milk_amount), 2) AS AMOUNT ,
        concat(ROUND(SUM(b.bonus_amount), 2)," / ", ROUND((100 * SUM(b.bonus_amount)) / NULLIF(SUM(b.milk_amount), 0), 2),"%") AS 'BONUS / PERCENTAGE'
        
        
        

    FROM
        bonus b
    JOIN
        bonus_summary bs ON b.bonus_summary_code = bs.code
    JOIN
        members m ON b.member_code = m.code
    JOIN
        member_details mb ON mb.member_code = m.code
    WHERE
        bs.from_date >= p_from_date AND bs.to_date <= p_to_date     AND 
        case when p_milk_type_code=0 then bs.x_col1=bs.x_col1 else bs.x_col1=p_milk_type_code end
      
    GROUP BY
        m.code_ex, bs.from_date, bs.to_date, m.first_name, mb.account_no;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE sp_bonus_member_wise(IN fromDate DATE, IN toDate DATE)
BEGIN
    SET SESSION group_concat_max_len = 1000000;

    SET @sql = NULL;
    SELECT
      GROUP_CONCAT(
        CONCAT(
          'COALESCE(ROUND(MAX(CASE WHEN bs.from_date = ''',
          bs.from_date,
          ''' AND bs.to_date = ''',
          bs.to_date,
          ''' THEN b.milk_qty ELSE NULL END), 2), 0) AS ',
          CONCAT('milk_qty_', REPLACE(DATE_FORMAT(bs.from_date, '%Y-%m-%d'), '-', ''), '_', REPLACE(DATE_FORMAT(bs.to_date, '%Y-%m-%d'), '-', '')),
          ', COALESCE(ROUND(MAX(CASE WHEN bs.from_date = ''',
          bs.from_date,
          ''' AND bs.to_date = ''',
          bs.to_date,
          ''' THEN b.milk_amount ELSE NULL END), 2), 0) AS ',
          CONCAT('milk_amount_', REPLACE(DATE_FORMAT(bs.from_date, '%Y-%m-%d'), '-', ''), '_', REPLACE(DATE_FORMAT(bs.to_date, '%Y-%m-%d'), '-', '')),
          ', COALESCE(ROUND(MAX(CASE WHEN bs.from_date = ''',
          bs.from_date,
          ''' AND bs.to_date = ''',
          bs.to_date,
          ''' THEN b.bonus_amount ELSE NULL END), 2), 0) AS ',
          CONCAT('bonus_', REPLACE(DATE_FORMAT(bs.from_date, '%Y-%m-%d'), '-', ''), '_', REPLACE(DATE_FORMAT(bs.to_date, '%Y-%m-%d'), '-', '')),
          ', COALESCE(ROUND(MAX(CASE WHEN bs.from_date = ''',
          bs.from_date,
          ''' AND bs.to_date = ''',
          bs.to_date,
          ''' THEN (100 * b.bonus_amount / NULLIF(b.milk_amount, 0)) ELSE NULL END), 2), 0) AS ',
          CONCAT('percentage_', REPLACE(DATE_FORMAT(bs.from_date, '%Y-%m-%d'), '-', ''), '_', REPLACE(DATE_FORMAT(bs.to_date, '%Y-%m-%d'), '-', ''))
        )
      ) INTO @sql
    FROM
      bonus_summary bs
    WHERE
      bs.from_date >= fromDate AND bs.to_date <= toDate;

    SET @sql = CONCAT(
      'SELECT b.member_code, CONCAT(m.first_name, " ", COALESCE(m.middle_name, ""), " ", m.last_name) AS full_name, md.account_no, ', @sql,
      ', ROUND(SUM(COALESCE(b.milk_qty, 0)), 2) AS total_milk_qty, ROUND(SUM(COALESCE(b.milk_amount, 0)), 2) AS total_milk_amount, ROUND(SUM(COALESCE(b.bonus_amount, 0)), 2) AS total_bonus_amount',
      ', ROUND(AVG(COALESCE(100 * b.bonus_amount / NULLIF(b.milk_amount, 0), 0)), 2) AS avg_percentage',
      ' FROM bonus_summary bs ',
      ' INNER JOIN bonus b ON bs.code = b.bonus_summary_code ',
      ' INNER JOIN member_details md ON b.member_code = md.member_code ',
      ' INNER JOIN members m ON md.member_code = m.code ',
      ' WHERE bs.from_date >= ''', fromDate, ''' AND bs.to_date <= ''', toDate, ''' ',
      ' GROUP BY b.member_code,md.account_no'
    );

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetMilkCollectionData`(IN p_member_code varchar(30),IN p_locale VARCHAR(2),IN p_milk_type_code INT,IN p_from_date DATETIME,IN p_to_date DATETIME)
BEGIN
    SELECT m.society_code,
		   s.name,
        CASE WHEN p_locale = 'en' THEN 
            CONCAT(m.code, '-', m.first_name) 
        ELSE 
            CONCAT(m.code, '-', m.first_name_local) 
        END AS member_name,
       COALESCE(a.fat, 0) AS fat1,
        COALESCE(a.snf, 0) AS snf1,
        COALESCE(a.qty, 0) AS qty1,
        COALESCE(a.amount, 0) AS amount1,
        COALESCE(b.fat, 0) AS fat2,
        COALESCE(b.snf, 0) AS snf2,
        COALESCE(b.qty, 0) AS qty2,
        COALESCE(b.amount, 0) AS amount2
    FROM members m
    LEFT JOIN milk_collection a ON m.code = a.member_code AND a.collection_date = p_from_date
    LEFT JOIN milk_collection b ON m.code = b.member_code AND b.collection_date = p_to_date
	LEFT JOIN society s ON m.society_code = s.code
    WHERE 
        (a.milk_type_code = p_milk_type_code OR p_milk_type_code = 0)
        AND (m.code = p_member_code OR p_member_code = 0)
           HAVING 
        fat1 != 0 OR snf1 != 0 OR qty1 != 0 OR amount1 != 0 OR
        fat2 != 0 OR snf2 != 0 OR qty2 != 0 OR amount2 != 0;
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_account_ledger_purchase_sale`(IN p_from_date date,IN p_to_date date, IN p_society_code varchar(255),IN p_locale varchar(20))
BEGIN

SELECT purchase_amount, sale_amount, ledger_name, ledger_code,society_name
 FROM
(SELECT amount AS purchase_amount,
CASE WHEN p_locale='en' THEN l.name ELSE IFNULL(l.name_local,l.name) END  as ledger_name,
CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
l.code as ledger_code
, 0 AS sale_amount FROM voucher_transaction t 
JOIN ledgers l ON t.ledger_code = l.code 
JOIN voucher v ON t.voucher_code = v.code 
JOIN ledger_groups g ON l.ledger_group_code = g.code
JOIN society s on v.society_code=p_society_code
WHERE g.ledger_type_code = 7 AND v.voucher_date BETWEEN p_from_date AND p_to_date
UNION ALL 
SELECT amount AS sale_amount, 
CASE WHEN p_locale='en' THEN l.name ELSE IFNULL(l.name_local,l.name) END  as ledger_name,
CASE WHEN p_locale='en' THEN s.name ELSE IFNULL(s.name_local,s.name) END  as society_name,
l.code as ledger_code
, 0 AS purchase_amount FROM voucher_transaction t 
JOIN ledgers l ON t.ledger_code = l.code 
JOIN voucher v ON t.voucher_code = v.code 
JOIN ledger_groups g ON l.ledger_group_code = g.code
JOIN society s on v.society_code=p_society_code
WHERE g.ledger_type_code = 8 AND v.voucher_date BETWEEN p_from_date AND p_to_date) a;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_rpt_milk_sale_dispatch_profit_loss_with_out_milk_type`(IN p_society_code VARCHAR(20),IN p_from_date DATETIME,IN p_to_date DATETIME,IN p_locale VARCHAR(2))
BEGIN

  SELECT
    CONCAT(IFNULL(s.name, s.short_name),
            '-(',
            s.code_ex,
            ')') AS society_name,
    CONCAT(IFNULL(u.name, u.name_local),
            '-(',
            u.code_ex,
            ')') AS union_name,
    CONCAT(CONCAT(DATE_FORMAT(from_date, '%d/%m/%y'),
                    '-',
                    CASE
                        WHEN SUBSTRING(from_date, 12, 8) = '06:00:00' THEN 'M'
                        ELSE 'E'
                    END),
            '-To-',
            CONCAT(DATE_FORMAT(to_date, '%d/%m/%y'),
                    '-',
                    CASE
                        WHEN SUBSTRING(to_date, 12, 8) = '06:00:00' THEN 'M'
                        ELSE 'E'
                    END)) AS collection_date,
    CONCAT(DATE_FORMAT(p_from_date, '%d/%m/%y'),
            '-',
            CASE
                WHEN SUBSTRING(p_from_date, 12, 8) = '06:00:00' THEN 'M'
                ELSE 'E'
            END) AS p_from_date,
    CONCAT(DATE_FORMAT(p_to_date, '%d/%m/%y'),
            '-',
            CASE
                WHEN SUBSTRING(p_to_date, 12, 8) = '06:00:00' THEN 'M'
                ELSE 'E'
            END) AS p_to_date,
    ROUND(SUM(m_qty), 2) AS milk_collection_qty,
    ROUND(SUM(m_amount), 2) AS milk_collection_amount,
    ROUND(SUM(l_qty), 2) AS local_sale_qty,
    ROUND(SUM(l_amount), 2) AS locak_sale_amount,
    ROUND(SUM(m_qty) - SUM(l_qty), 2) AS milk_collection_local_sale_qty,
    ROUND(SUM(g_qty), 2) AS good_qty,
    ROUND(SUM(s_qty), 2) AS Sour_qty,
    ROUND(SUM(c_qty), 2) AS Curd_qty,
    ROUND(SUM(d_amount), 2) AS dispatch_amount,
    ROUND(SUM(g_qty) + SUM(s_qty) + SUM(c_qty), 2) AS total_qty,
    CASE
        WHEN SUM(m_qty) = 0 THEN 0
        ELSE ROUND(SUM(m_fat) / SUM(m_qty) * 100, 2)
    END AS fat,
    CASE
        WHEN SUM(IFNULL(g_qty, 0) + IFNULL(s_qty, 0) + IFNULL(c_qty, 0)) = 0 THEN 0
        ELSE ROUND(SUM(d_fat) / SUM(IFNULL(g_qty, 0) + IFNULL(s_qty, 0) + IFNULL(c_qty, 0)) * 100,
                2)
    END AS d_fat,
    ROUND((SUM(g_qty) + SUM(s_qty) + SUM(c_qty)) - (SUM(m_qty) - SUM(l_qty)),
            2) AS diff_qty,
    ROUND(((((SUM(d_amount)) + SUM(l_amount)) * 100) / SUM(m_amount)) - 100,
            2) AS diff_per,
    ROUND((SUM(d_amount)) - (SUM(m_amount) - SUM(l_amount)),
            2) AS diff_amount
FROM
    (SELECT
        IFNULL((SELECT
                    SUM(qty)
                FROM
                    milk_collection
                WHERE
                    collection_date BETWEEN from_date AND to_date), 0) AS m_qty,
            IFNULL((SELECT
                    SUM(amount)
                FROM
                    milk_collection
                WHERE
                    collection_date BETWEEN from_date AND to_date), 0) AS m_amount,
            IFNULL((SELECT
                    SUM(quantity)
                FROM
                    local_milk_sale
                WHERE
                    sale_date BETWEEN from_date AND to_date), 0) AS l_qty,
            IFNULL((SELECT
                    SUM(amount)
                FROM
                    local_milk_sale
                WHERE
                    sale_date BETWEEN from_date AND to_date), 0) AS l_amount,
            ROUND((g_qty), 2) AS g_qty,
            ROUND((s_qty), 2) AS s_qty,
            ROUND((c_qty), 2) AS c_qty,
            from_date,
            to_date,
            d_amount,
            d_fat,
            ROUND((g_qty) + (s_qty) + (c_qty), 2) AS total_qty,
            IFNULL((SELECT
                    ROUND(CASE
                            WHEN SUM(qty) = 0 THEN 0
                            ELSE SUM(qty * fat / 100)
                        END, 2)
                FROM
                    milk_collection
                WHERE
                    collection_date BETWEEN from_date AND to_date), 0) AS m_fat
    FROM
        (SELECT
        0 AS m_qty,
            0 AS m_fat,
            0 AS m_amount,
            0 AS l_qty,
            0 AS l_fat,
            0 AS l_amount,
            CASE
                WHEN milk_type_code = 3 THEN md.from_date
                ELSE md.from_date
            END AS from_date,
            md.to_date AS to_date,
            mdt.converted_quantity AS g_qty,
            0 AS s_qty,
            0 AS c_qty,
            mdt.avg_fat * mdt.qty / 100 AS d_fat,
            mdt.amount AS d_amount
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    WHERE
        mdt.milk_quality_type_code = 1
            AND from_date >= p_from_Date
            AND to_Date <= p_to_date UNION ALL SELECT
        0 AS m_qty,
            0 AS m_fat,
            0 AS m_amount,
            0 AS l_qty,
            0 AS l_fat,
            0 AS l_amount,
            CASE
                WHEN milk_type_code = 3 THEN md.from_date
                ELSE md.from_date
            END AS from_date,
            md.to_date AS to_date,
            0 AS g_qty,
            mdt.converted_quantity AS s_qty,
            0 AS c_qty,
            mdt.avg_fat * mdt.qty / 100 AS d_fat,
            mdt.amount
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    WHERE
        mdt.milk_quality_type_code = 2
            AND from_date >= p_from_Date
            AND to_Date <= p_to_date UNION ALL SELECT
        0 AS m_qty,
            0 AS m_fat,
            0 AS m_amount,
            0 AS l_qty,
            0 AS l_fat,
            0 AS l_amount,
            CASE
                WHEN milk_type_code = 3 THEN md.from_date
                ELSE md.from_date
            END AS from_date,
            md.to_date AS to_date,
            0 AS g_qty,
            0 AS s_qty,
            mdt.converted_quantity AS c_qty,
            mdt.avg_fat * mdt.qty / 100 AS d_fat,
            mdt.amount
    FROM
        milk_receipt AS md
    INNER JOIN milk_receipt_transaction AS mdt ON mdt.milk_receipt_code = md.code
    WHERE
        mdt.milk_quality_type_code = 3
            AND from_date >= p_from_Date
            AND to_Date <= p_to_date) AS A) AS B
        INNER JOIN
    society AS s ON s.code = p_society_code
        INNER JOIN
    unions AS u ON u.code = s.union_code
GROUP BY from_date , to_date , s.name , s.short_name , u.name , u.name_local , s.code_ex , u.code_ex;


END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_member_collection_summary1`(
    IN p_from_collection_date DATETIME,
    IN p_to_collection_date DATETIME,
    IN p_society_code VARCHAR(12),
    IN p_ltr_kg INT,
    IN p_locale VARCHAR(50),
    IN p_member_code VARCHAR(100)
)
BEGIN

    SELECT
        SUBSTRING(DATE_FORMAT(p_from_collection_date,'%d/%m/%Y'),1,10) AS from_date,
        SUBSTRING(DATE_FORMAT(p_to_collection_date,'%d/%m/%Y'),1,10) AS to_date,
        mc.society_code AS society_code,
        CASE WHEN p_locale = 'en' THEN s.name ELSE IFNULL(s.name_local, s.name) END AS society_name,
        mc.union_code AS union_code,
        CASE WHEN p_locale = 'en' THEN u.name ELSE IFNULL(s.name_local, u.name) END AS union_name,
        s.village_code AS village_code,
        CASE WHEN p_locale = 'en' THEN v.name ELSE IFNULL(v.name_local, v.name) END AS village_name,
        CONCAT(SUBSTRING(DATE_FORMAT(mc.collection_date, '%d/%m/%Y'), 1, 10), CASE WHEN SUBSTRING(mc.collection_date, 12) = '06:00:00' THEN ' - M ' ELSE ' - E ' END) AS collection_date,
        CASE WHEN (mc.qty_mode = 0) THEN 'Ltr' ELSE 'Kg' END AS qty_mode,
        RIGHT(mc.member_code, 4) AS member_code,
        CASE WHEN p_locale = 'en' THEN
            CONCAT(IFNULL(m.first_name, ''), ' ', IFNULL(m.middle_name, ''), ' ', IFNULL(m.last_name, ''))
        ELSE
            IFNULL(CONCAT(m.first_name_local, ' ', m.middle_name_local, ' ', m.last_name_local),
            CONCAT(IFNULL(m.first_name, ''), ' ', IFNULL(m.middle_name, ''), ' ', IFNULL(m.last_name, '')))
        END AS member_name,
        CASE WHEN p_locale = 'en' THEN mt.name ELSE IFNULL(mt.name_local, mt.name) END AS milk_type,
        IFNULL(ded.installment_amount, 0) AS installment_amount,
        IFNULL(lms.credit, 0) AS local_sale_credit,
        ROUND(IFNULL(SUM(amount), 0) - (IFNULL(ded.installment_amount, 0) + IFNULL(lms.credit, 0)), 2) AS netpayble,
        ROUND(IFNULL(ded.installment_amount, 0) + IFNULL(lms.credit, 0), 2) AS deduction,
        CASE WHEN p_ltr_kg = qty_mode THEN ROUND(ROUND(SUM(amount), 2) / ROUND(SUM(qty), 2), 2) ELSE ROUND(ROUND(SUM(amount), 2) / ROUND(SUM(converted_qty), 2), 2) END AS avg_rate,
        ROUND(SUM(amount), 2) AS amount,
        CASE WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(qty), 2) ELSE ROUND(SUM(converted_qty), 2) END AS qty,
        CASE WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * snf / 100, 2)) / SUM(qty) * 100, 2) ELSE ROUND(SUM(ROUND(converted_qty * snf / 100, 2)) / SUM(converted_qty) * 100, 2) END AS avg_snf,
        CASE WHEN p_ltr_kg = qty_mode THEN SUM(ROUND(qty * snf / 100, 2)) ELSE SUM(ROUND(converted_qty * snf / 100, 2)) END AS kg_snf,
        CASE WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * fat / 100, 2)) / SUM(qty) * 100, 1) ELSE ROUND(SUM(ROUND(converted_qty * fat / 100, 2)) / SUM(converted_qty) * 100, 1) END AS avg_fat,
        CASE WHEN p_ltr_kg = qty_mode THEN SUM(ROUND(qty * fat / 100, 2)) ELSE SUM(ROUND(converted_qty * fat / 100, 2)) END AS kg_fat,
        CASE WHEN p_ltr_kg = qty_mode THEN ROUND(SUM(ROUND(qty * clr / 100, 2)) / SUM(qty) * 100, 1) ELSE ROUND(SUM(ROUND(converted_qty * clr / 100, 2)) / SUM(converted_qty) * 100, 1) END AS avg_clr,
        CASE WHEN p_ltr_kg = qty_mode THEN SUM(ROUND(qty * clr / 100, 2)) ELSE SUM(ROUND(converted_qty * clr / 100, 2)) END AS kg_clr
    FROM
        milk_collection AS mc
    INNER JOIN
        society AS s ON mc.society_code = s.code
    INNER JOIN
        unions AS u ON mc.union_code = u.code
    INNER JOIN
        villages AS v ON s.village_code = v.code
    INNER JOIN
        members AS m ON mc.member_code = m.code
    INNER JOIN
        milk_types AS mt ON mc.milk_type_code = mt.code
    LEFT JOIN
        (SELECT member_code, SUM(installment_amount) AS installment_amount
         FROM product_sale_installment AS psi
         LEFT JOIN product_sale AS ps ON ps.invoice_no = psi.invoice_no
         WHERE deduction_date BETWEEN p_from_collection_date AND p_to_collection_date
           AND ps.payment_mode = 1 AND (ps.consumer_type = 0 OR ps.consumer_type = 1)
         GROUP BY member_code) AS ded ON ded.member_code = mc.member_code
    LEFT JOIN
        (SELECT consumer_code, SUM(credit) AS credit
         FROM local_milk_sale
         WHERE (consumer_type = 0 OR consumer_type = 1) AND payment_mode = 1 AND sale_date BETWEEN p_from_collection_date AND p_to_collection_date
         GROUP BY consumer_code) AS lms ON lms.consumer_code = mc.member_code
    WHERE
        mc.collection_date BETWEEN p_from_collection_date AND p_to_collection_date
        AND mc.society_code = p_society_code
        AND   mc.member_code=CASE WHEN p_member_code =0 THEN mc.member_code ELSE p_member_code END 
    GROUP BY
        society_code, society_name, union_code, union_name, village_code, village_name, mc.collection_date,
        mc.qty_mode, mc.member_code, member_name, installment_amount, local_sale_credit,
        amount, ded.installment_amount, lms.credit, qty, converted_qty, clr, snf, fat, mt.name, mt.name_local
    ORDER BY
        mc.member_code, collection_date;

END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_consolidate_with_deduction`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_locale VARCHAR(20))
BEGIN

 SELECT  CONCAT(date_format(p_from_date,'%d/%m/%Y'),(CASE WHEN cast(p_from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END),' To ',date_format(p_to_date,'%d/%m/%Y'),(CASE WHEN cast(p_to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)) as dates,
    mc.society_code,
    CASE
        WHEN p_locale = 'en' THEN s.name
        ELSE IFNULL(s.name_local, s.name)
    END AS society_name,
    m.code_ex AS member_code,
    CASE
        WHEN
            p_locale = 'en'
        THEN
            CONCAT(IFNULL(m.first_name, ''),
                    + ' ',
                    IFNULL(m.middle_name, ''),
                    + ' ',
                    IFNULL(m.last_name, ''))
        ELSE IFNULL(CONCAT(m.first_name_local,
                        + ' ',
                        m.middle_name_local,
                        ' ',
                        m.last_name_local),
                CONCAT(IFNULL(m.first_name, ''),
                        + ' ',
                        IFNULL(m.middle_name, ''),
                        + ' ',
                        IFNULL(m.last_name, '')))
    END AS member_name,
    ROUND(SUM(mc.qty),2) as qty,
       CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(fat, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS fat,
      CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(snf, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS snf,
    ROUND(SUM(mc.amount)/SUM(mc.qty),2) as rate,
    ROUND(SUM(mc.amount),2) as amount,
    ROUND(SUM(qty*fat)/100,2) as kg_fat,
    ROUND(SUM(qty*snf)/100,2) as kg_snf,
	IFNULL(ded.installment_amount,0) as installment_amount ,
	round(IFNULL(SUM(amount),0)-(IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0)),2) as netpayble,
	round( IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0),2) as deduction
FROM
    milk_collection mc
        INNER JOIN
    society s ON mc.society_code = s.code
        INNER JOIN
    members m ON mc.member_code = m.code
	LEFT JOIN (select   member_code,sum(installment_amount) as installment_amount 
               FROM product_sale_installment as psi 
               LEFT JOIN product_sale as ps ON ps.invoice_no=psi.invoice_no
               where deduction_date BETWEEN p_from_date AND p_to_date AND ps.payment_mode=1 AND (ps.consumer_type=0 or ps.consumer_type=1)  
               group by  member_code
               ) as ded ON ded.member_code = mc.member_code
	LEFT JOIN (select   consumer_code,sum(credit) as credit 
                from local_milk_sale   
                where(consumer_type=0 OR consumer_type=1) AND payment_mode =1 AND sale_date BETWEEN p_from_date AND p_to_date
                group by  consumer_code
                ) as lms ON lms.consumer_code = mc.member_code 
        INNER JOIN
    shifts st ON mc.shift_code = st.code
 WHERE
     mc.collection_date   BETWEEN p_from_date and p_to_date
      AND mc.society_code = p_society_code 
       AND mc.member_code = CASE WHEN  p_member_code=0 THEN mc.member_code ELSE p_member_code END 
GROUP BY   mc.society_code,m.code_ex,s.name_local, s.name,m.first_name,m.middle_name,m.last_name,m.first_name_local, m.middle_name_local,ded.installment_amount,lms.credit, m.last_name_local
ORDER BY  society_code , member_code ;
END;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_milk_collection_milk_type_wise_consolidate_with_deduction`(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date datetime,IN p_to_date datetime,IN p_milk_type_code INT,IN p_locale VARCHAR(20))
BEGIN

 SELECT  CONCAT(date_format(p_from_date,'%d/%m/%Y'),(CASE WHEN cast(p_from_date as time) ='06:00:00' THEN '-M' ELSE '-E' END),' To ',date_format(p_to_date,'%d/%m/%Y'),(CASE WHEN cast(p_to_date as time) ='06:00:00' THEN '-M' ELSE '-E' END)) as dates,
    mc.society_code,
    CASE
        WHEN p_locale = 'en' THEN s.name
        ELSE IFNULL(s.name_local, s.name)
    END AS society_name,
    m.code_ex AS member_code,
    CASE
        WHEN
            p_locale = 'en'
        THEN
            CONCAT(IFNULL(m.first_name, ''),
                    + ' ',
                    IFNULL(m.middle_name, ''),
                    + ' ',
                    IFNULL(m.last_name, ''))
        ELSE IFNULL(CONCAT(m.first_name_local,
                        + ' ',
                        m.middle_name_local,
                        ' ',
                        m.last_name_local),
                CONCAT(IFNULL(m.first_name, ''),
                        + ' ',
                        IFNULL(m.middle_name, ''),
                        + ' ',
                        IFNULL(m.last_name, '')))
    END AS member_name,
   CASE
        WHEN p_locale = 'en' THEN mt.name ELSE IFNULL(mt.name_local,mt.name) END  as animal_type_name,
    ROUND(SUM(mc.qty),2) as qty,
       CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(fat, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS fat,
      CASE
        WHEN IFNULL(SUM(qty), 0) = 0 THEN 0
        ELSE IFNULL(ROUND(SUM(ROUND(IFNULL(qty, 0) * IFNULL(snf, 0) / 100,4)) / IFNULL(SUM(qty), 0) * 100,2),0)
    END AS snf,
    ROUND(SUM(mc.amount)/SUM(mc.qty),2) as rate,
    ROUND(SUM(mc.amount),2) as amount,
	ROUND(SUM(qty*fat)/100,2) as kg_fat,
    ROUND(SUM(qty*snf)/100,2) as kg_snf,
	IFNULL(ded.installment_amount,0) as installment_amount ,
	round(IFNULL(SUM(amount),0)-(IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0)),2) as netpayble,
	round( IFNULL((ded.installment_amount),0)+IFNULL((lms.credit),0),2) as deduction
FROM
    milk_collection mc
        INNER JOIN
    society s ON mc.society_code = s.code
        INNER JOIN
    members m ON mc.member_code = m.code
	LEFT JOIN (select   member_code,sum(installment_amount) as installment_amount 
               FROM product_sale_installment as psi 
               LEFT JOIN product_sale as ps ON ps.invoice_no=psi.invoice_no
               where deduction_date BETWEEN p_from_date AND p_to_date AND ps.payment_mode=1 AND (ps.consumer_type=0 or ps.consumer_type=1)  
               group by  member_code
               ) as ded ON ded.member_code = mc.member_code
	LEFT JOIN (select   consumer_code,sum(credit) as credit 
                from local_milk_sale   
                where(consumer_type=0 OR consumer_type=1) AND payment_mode =1 AND sale_date BETWEEN p_from_date AND p_to_date
                group by  consumer_code
                ) as lms ON lms.consumer_code = mc.member_code 
        INNER JOIN
    shifts st ON mc.shift_code = st.code
    INNER JOIN
    milk_types mt ON mc.milk_type_code = mt.code
 WHERE
  mc.collection_date  BETWEEN p_from_date AND p_to_date
        AND mc.society_code = p_society_code 
        AND mc.member_code = CASE WHEN  p_member_code=0 THEN mc.member_code ELSE p_member_code END 
GROUP BY mt.name_local,mt.name, mt.name,mc.society_code,s.name_local, s.name,ded.installment_amount,lms.credit, m.code_ex,m.first_name,m.last_name,m.middle_name,m.first_name_local, m.middle_name_local,m.last_name_local
ORDER BY  society_code , member_code ;
END;;
DELIMITER ;


drop procedure if exists rpt_bonus_new;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `rpt_bonus_new`(IN p_society_code varchar(200),IN p_member_code varchar(200),IN p_from_date datetime,IN p_to_date datetime,IN p_milk_type_code varchar(1))
BEGIN
DROP TEMPORARY TABLE IF EXISTS bonus_new;
CREATE TEMPORARY TABLE IF NOT EXISTS bonus_new AS (
SELECT 
	c.society_code,
	c.society_name,
	c.member_code,
	c.periods,
	c.qty,
	c.amount,
	c.bonus_amount,
	c.total_amount,
    c.code
FROM (
	SELECT 
        s.code AS society_code,
        s.name AS society_name,
        CONCAT(RIGHT(m.code, 4), ' - ', m.first_name,m.middle_name,m.last_name) AS member_code,
        CONCAT(A.period, ' ', IFNULL(CONCAT(A.percentage, ' (', A.bonus_criteria_value, ')'), 0)) AS periods,
        A.qty as qty,
        A.amount as amount,
        A.bonus_amount as bonus_amount,
        0 AS total_amount,
        m.code
        FROM (
        SELECT 
            b.member_code,
            CONCAT(DATE_FORMAT(bs.from_date, '%Y-%m-%d'), ' ', DATE_FORMAT(bs.to_date, '%Y-%m-%d')) AS period,
            ROUND(SUM(b.milk_qty), 2) AS qty,
            ROUND(SUM(b.milk_amount), 2) AS amount,
           /* CASE 
                WHEN bs.bonus_criteria = 1 THEN SUM(bs.total_milk_qty)
                WHEN bs.bonus_criteria = 0 THEN SUM(bs.total_milk_amount)
                WHEN bs.bonus_criteria = 2 THEN SUM(bs.total_milk_amount)
            END*/ bonus_criteria_amount AS percentage,
            cast(bonus_criteria_amount*100/total_milk_amount as decimal(18,2))  as  bonus_criteria_value,
            CASE 
                WHEN bs.bonus_criteria = 1 THEN CAST(SUM(b.milk_qty) * bs.bonus_criteria_value AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 0 THEN CAST(ROUND(SUM(b.milk_amount), 2) * (bs.bonus_criteria_value / 100) AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 2 THEN CAST(CAST(ROUND(SUM(b.milk_amount), 2) * (SUM(bs.bonus_criteria_value * 100 / bs.total_milk_amount)) AS DECIMAL(18, 2)) / 100 AS DECIMAL(18, 2))
            END AS bonus_amount
        FROM
            bonus b
        JOIN
            bonus_summary bs ON b.bonus_summary_code = bs.code
            
         where bs.society_code=p_society_code
        AND b.member_code=CASE WHEN p_member_code=0  THEN b.member_code ELSE p_member_code END 
        AND (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) 
        /*OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date))*/ )
        AND bs.x_col1= (case when p_milk_type_code=0 then bs.x_col1 else  p_milk_type_code end)
        GROUP BY 
            bs.from_date, bs.to_date, b.member_code, bs.bonus_criteria_value, bs.bonus_criteria,bonus_criteria_amount,total_milk_amount
        ORDER BY 
            bs.from_date ASC
    ) AS A
   JOIN
        members m ON A.member_code = m.code
    JOIN
        society s ON m.society_code = s.code
    WHERE 
        A.period IS NOT NULL

    UNION ALL

    SELECT 
        s.code AS society_code,
        s.name AS society_name,
        CONCAT(RIGHT(m.code, 4), ' - ', m.first_name,m.middle_name,m.last_name) AS member_code,
        'Amount' AS periods,
        0 AS qty,
        0 AS amount,
        0 AS bonus_amount,
        SUM(A.bonus_amount) AS total_amount,
        m.code
    FROM (
        SELECT 
            b.member_code,
            CONCAT(DATE_FORMAT(bs.from_date, '%d-%m-%Y'), ' ', DATE_FORMAT(bs.to_date, '%d-%m-%Y')) AS period,
            ROUND(SUM(b.milk_qty), 2) AS qty,
            ROUND(SUM(b.milk_amount), 2) AS amount,
           /* CASE 
                WHEN bs.bonus_criteria = 1 THEN SUM(bs.total_milk_qty)
                WHEN bs.bonus_criteria = 0 THEN SUM(bs.total_milk_amount)
                WHEN bs.bonus_criteria = 2 THEN SUM(bs.total_milk_amount)
            END*/ bonus_criteria_amount AS percentage,
            bs.bonus_criteria_value,
            CASE 
                WHEN bs.bonus_criteria = 1 THEN CAST(SUM(b.milk_qty) * bs.bonus_criteria_value AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 0 THEN CAST(ROUND(SUM(b.milk_amount), 2) * (bs.bonus_criteria_value / 100) AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 2 THEN CAST(CAST(ROUND(SUM(b.milk_amount), 2) * (SUM(bs.bonus_criteria_value * 100 / bs.total_milk_amount)) AS DECIMAL(18, 2)) / 100 AS DECIMAL(18, 2))
            END AS bonus_amount
        FROM
            bonus b
        JOIN
            bonus_summary bs ON b.bonus_summary_code = bs.code
             
        where bs.society_code=p_society_code
        AND b.member_code=CASE WHEN p_member_code=0  THEN b.member_code ELSE p_member_code END 
        AND (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) 
        /*OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date))*/ )
        AND bs.x_col1= (case when p_milk_type_code=0 then bs.x_col1 else  p_milk_type_code end)
        GROUP BY 
            bs.from_date, bs.to_date, b.member_code, bs.bonus_criteria_value, bs.bonus_criteria,bonus_criteria_amount
        ORDER BY 
            bs.from_date ASC
    ) AS A
     JOIN
        members m ON A.member_code = m.code
    JOIN
        society s ON m.society_code = s.code
    WHERE 
        A.period IS NOT NULL
    GROUP BY 
        A.member_code

    UNION ALL

    SELECT 
        DISTINCT s.code as society_code,
                 s.name as society_name,
 --       CONCAT(RIGHT(m.code, 4), ' - ', m.first_name,m.middle_name,m.last_name) AS member_code,
		CONCAT(RIGHT(m.code, 4), ' - ', ifnull(m.first_name,''),ifnull(m.middle_name,''),ifnull(m.last_name,'')) AS member_code,
        'Sign' AS periods,
        '' AS qty,
        '' AS amount,
        '' AS bonus_amount,
        '' AS total_amount,
        m.code
    FROM (
        SELECT  
            b.member_code,
            CONCAT(DATE_FORMAT(bs.from_date, '%d-%m-%Y'), ' ', DATE_FORMAT(bs.to_date, '%d-%m-%Y')) AS period,
            ROUND(SUM(b.milk_qty), 2) AS qty,
            ROUND(SUM(b.milk_amount), 2) AS amount,
            /*CASE 
                WHEN bs.bonus_criteria = 1 THEN SUM(bs.total_milk_qty)
                WHEN bs.bonus_criteria = 0 THEN SUM(bs.total_milk_amount)
                WHEN bs.bonus_criteria = 2 THEN SUM(bs.total_milk_amount)
            END */ bonus_criteria_amount AS percentage,
            bs.bonus_criteria_value,
            CASE 
                WHEN bs.bonus_criteria = 1 THEN CAST(SUM(b.milk_qty) * bs.bonus_criteria_value AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 0 THEN CAST(ROUND(SUM(b.milk_amount), 2) * (bs.bonus_criteria_value / 100) AS DECIMAL(18, 2))
                WHEN bs.bonus_criteria = 2 THEN CAST(CAST(ROUND(SUM(b.milk_amount), 2) * (SUM(bs.bonus_criteria_value * 100 / bs.total_milk_amount)) AS DECIMAL(18, 2)) / 100 AS DECIMAL(18, 2))
            END AS bonus_amount
        FROM
            bonus b
        JOIN
            bonus_summary bs ON b.bonus_summary_code = bs.code
             
       where bs.society_code=p_society_code
        AND b.member_code=CASE WHEN p_member_code=0  THEN b.member_code ELSE p_member_code END 
        AND (((from_date BETWEEN p_from_date AND p_to_date) OR (to_date BETWEEN p_from_Date AND p_to_date)) 
        /*OR  ((p_from_date >= from_date and p_from_date <= p_to_date) Or (p_to_date >= from_date and p_to_date <= to_date))*/ )
        AND bs.x_col1= (case when p_milk_type_code=0 then bs.x_col1 else  p_milk_type_code end)
        GROUP BY 
            bs.from_date, bs.to_date, b.member_code, bs.bonus_criteria_value, bs.bonus_criteria ,bonus_criteria_amount
           
        ORDER BY 
            bs.from_date ASC
    ) AS A
    JOIN
        members m ON A.member_code = m.code
    JOIN
        society s ON m.society_code = s.code
    WHERE 
        A.period IS NOT NULL
	) c
    );
   
   DROP TEMPORARY TABLE IF EXISTS bonus_new1;
CREATE TEMPORARY TABLE IF NOT EXISTS bonus_new1 AS (
   select count(distinct code) as member_count from bonus_new);
   
select  society_code,
		society_name,
		member_code,
		periods,
		qty,
		amount,
		bonus_amount,
		total_amount ,
		member_count
   from bonus_new i
   LEFT JOIN bonus_new1 b ON 1=1
   ;

END;;
DELIMITER ;


UPDATE eipl_amcs_db.permissions SET module = 'view/operation/administration/Notification.fxml' WHERE (code = '165');
UPDATE  milk_collection mc INNER JOIN society_payment_cycles sp ON mc.collection_date >= sp.from_date AND mc.collection_date <= sp.to_date SET society_payment_cycle_code = sp.code;

ALTER TABLE `eipl_amcs_db`.`product_requisition` 
CHANGE COLUMN `requisition_date` `requisition_date` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `eipl_amcs_db`.`product_requisition_transaction` 
CHANGE COLUMN `requisition_date` `requisition_date` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `eipl_amcs_db`.`product_requisition_audit` 
CHANGE COLUMN `requisition_date` `requisition_date` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `eipl_amcs_db`.`product_requisition_transaction_audit` 
CHANGE COLUMN `requisition_date` `requisition_date` DATETIME NULL DEFAULT NULL ;

-- update milk_collection set society_code = (select code from society);
-- UPDATE milk_collection set union_code= '101';