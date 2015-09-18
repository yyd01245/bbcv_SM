#!/bin/bash
if [ ! -f "pid" ]
then
    node ../index.js &
    echo $! > pid
    echo "onewayvod start success"
fi
