json-server generateDummyData.js &
sleep 15
mvn test
pkill -9 -f node
