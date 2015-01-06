LOG=/data/data/org.Ex3.AndLinux/files/log
$BUSYBOX tar xf $SRC -C $TGT &>> $LOG
cd $TGT
PROOT=$TGT/linuxroot source ./init.sh &>> $LOG
