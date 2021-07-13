<?php

$db_name="business_card_scanner";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if(!$con) {
	echo "Could not connect to database";
}

$name=$_POST["name"];
$phone=$_POST["phone"];
$address=$_POST["address"];
$city=$_POST["city"];
$email=$_POST["email"];

$sql_query="insert into cards (name,phone,address,city,email) values('$name','$phone','$address','$city','$email')";
if(mysqli_query($con,$sql_query)) {
	echo "Saved to database successfully";
}
else {
	echo "Failed to save";
}
?>