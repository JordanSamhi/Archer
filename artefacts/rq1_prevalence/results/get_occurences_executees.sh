echo "#==============#"
echo "|   GOODWARE   |"
echo "#==============#"

echo

total=$(cat empirical_results_methods_goodware.lst |grep "|2|"|awk -F"|" '{print $4}'|tr '&' '\n'|wc -l)
echo "Total: $total"

echo

echo Detail:
cat empirical_results_methods_goodware.lst |grep "|2|"|awk -F"|" '{print $4}'|tr '&' '\n'|sort|uniq -c|sort -nr|awk '{print $2": "$1}'

echo

echo "#=============#"
echo "|   MALWARE   |"
echo "#=============#"

echo

echo Total:
total=$(cat empirical_results_methods_malware.lst |grep "|2|"|awk -F"|" '{print $4}'|tr '&' '\n'|wc -l)
echo "Total: $total"

echo

echo Detail:
cat empirical_results_methods_malware.lst |grep "|2|"|awk -F"|" '{print $4}'|tr '&' '\n'|sort|uniq -c|sort -nr|awk '{print $2": "$1}'
