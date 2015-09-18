sh kill_progress.sh bvbcm
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh vncms
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh AudioRecord
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh bbrowser
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh chrome
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh bvbcs
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh PluKill
if [ $? -ne 0 ] ;then
	exit 1;
fi

sh kill_progress.sh mkdir
if [ $? -ne 0 ] ;then
	exit 1;
fi

# wait chrome relsease socket
sleep 1

rm -rf chrome*
rm -rf .BBCvnc*
