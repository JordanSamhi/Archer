echo "#==============#"
echo "|   GOODWARE   |"
echo "#==============#"
echo 
total=$(cat empirical_results_methods_goodware.lst |grep "|1|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Executors: $total ($percent%)"
total=$(cat empirical_results_methods_goodware.lst |grep "|2|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Executees: $total ($percent%)"
total=$(cat empirical_results_methods_goodware.lst |grep "|3|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Helpers: $total ($percent%)"

echo

echo "#=============#"
echo "|   MALWARE   |"
echo "#=============#"

echo

total=$(cat empirical_results_methods_malware.lst |grep "|1|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Executors: $total ($percent%)"
total=$(cat empirical_results_methods_malware.lst |grep "|2|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Executees: $total ($percent%)"
total=$(cat empirical_results_methods_malware.lst |grep "|3|"|awk -F"|" '{print $1}'|sort|uniq|wc -l)
percent=$(echo $total | awk '{printf("%.0f",$1 * 100 / 3000)}')
echo "Number of Helpers: $total ($percent%)"
