cat goodware_results.lst malware_results.lst|awk -F";" '{print $1"|"$12}'|tr '#' '\n'|sed 's/\%.*$//g'|awk -F"|" '{if(NF == 4){print $0}}'|shuf|head -100
