<?php

$db_name="business_card_scanner";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if(!$con) {
	echo "Could not connect to database";
}

$sql_query=$_POST["query"];
$result = $con->query($sql_query);
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc())
		echo $row["name"] . "----" . $row["phone"] . "----" . $row["address"] . "----" . $row["city"] . "----" . $row["email"] . "----";
}
else {
	echo "Failed to get database";
}
?>