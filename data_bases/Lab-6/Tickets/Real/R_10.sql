select Customer_ID, Sales_Order_ID,
       sum(SubTotal) over (partition by Customer_ID order by Order_Date
rows between unbounded preceding and current row)
from Sales.Sales_Order_Header
order by Customer_ID, sales_order_id