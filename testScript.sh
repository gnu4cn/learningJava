#! /bin/bash

for i in {1..20}
do
    echo ""
    echo "-----第 $i 遍运行 --------"
    echo ""
    java -jar ${PWD}/build/libs/com.xfoss.learningJava-0.0.1.jar
done
