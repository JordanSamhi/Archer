apk=$1
platforms=$2
redisServer=$3
redisPort=$4
redisPwd=$5
java -jar archer.jar -a $apk -p $platforms -s $redisServer -n $redisPort -w $redisPwd
rm $apk
