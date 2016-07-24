#!/bin/bash

echo -n "快速测试?(Y:N)"
read t
# default configuration
fundCode=110001
sDate=2014-2-1
eDate=2016-1-1
shDate=2016-1-5
round=1
dtr=5
p=1
amount=500
stype=1
needFirst=2

if [ $t == N ]; then 
    echo -n "输入基金代码(六位): "
    read fundCode
    echo -n "开始定投日期(yyyy-m-d): "
    read sDate
    echo -n "结束定投日期(yyyy-m-d): "
    read eDate
    echo -n "赎回日期(yyyy-m-d): "
    read shDate
    echo -n "定投周期(月:1; 周:-7): "
    read round
    echo -n `[ $round == 1 ] && echo "每个月的第几天(1-28): " || echo "每一周的星期几(1-5): "`
    read dtr
    echo -n "申购费率(%): "
    read p
    echo -n "定投金额(元): "
    read amount
    echo "默认红利再投资，分红方式与实际相符..."
fi
response=`curl "http://fund.eastmoney.com/data/FundInvestCaculator_AIPDatas.aspx?fcode=${fundCode}&sdate=${sDate}&edate=${eDate}&shdate=${shDate}&round=${round}&dtr=${dtr}&p=${p}&je=${amount}&stype=${stype}&needfirst=${needFirst}&jsoncallback=FundDTSY.result"`

temp1=`echo $response | grep -oP '%\|(.+)\"}'` 
temp2=`echo ${temp1%\"*} | cut -c 3-`

# result=`echo $temp2 | sed 's/[[:space:]]//g'`

result=${temp2// /-}
arr=${result//_/ }


printf '%25s\t%12s\t%12s\t%12s\n' 定投日期 净值 购买金额 购买份额
sum=0
for i in $arr
do 
    # echo $i
    list=(${i//\~/ })
    printf '%25s\t%12s\t%12s\t%12s\n' ${list[@]}
    sum="${sum}+${list[3]//,/}"
done
sum=`echo $sum | bc`
echo "赎回时的总份额: ${sum}"

temp1=`curl "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=${fundCode}&page=1&per=20&sdate=${shDate}&edate=${shDate}" | grep -oP "([0-9]+\.[0-9]{4})"`
temp2=(${temp1})
shShare=${temp2[0]}
echo "赎回当天的净值: ${shShare}元"
echo `echo ${sum}*${shShare} | bc`
