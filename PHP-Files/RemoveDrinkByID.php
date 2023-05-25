<?php
	$con = mysqli_connect('localhost','root','root');
	if(!$con){
		echo json_encode("connection failed");
		die();
	}
	$db = mysqli_select_db($con,'CoffeeShop');
	
		
	$query = "delete from products where PID=".$_POST[PID];
	$result = mysqli_query($con, $query);
	if(!$result)
		echo json_encode("fail");
	else
		echo json_encode("success");
	
	
	?>

