<?php
	$con = mysqli_connect('localhost','root','root');
	if(!$con){
		echo json_encode("connection failed");
		die("");
	}
	$db = mysqli_select_db($con,'CoffeeShop');
	if(!$db)
		die("missing database");
	
	if(!isset($_GET['Drink_Name'])){
	    echo json_encode(array("error"=>"drink name is missing"));
		die("");
	}
	
	$query = "select * from products where name='".$_GET['Drink_Name']."'";
	$result = mysqli_query($con, $query);
	if(!$result)
		die("something went wrong");
	
	
	$row = mysqli_fetch_assoc($result);
	$drink = array("pid"=>$row['PID'], "name"=>$row['Name'], "price"=>$row['price'], "category"=>$row['category'], "description"=>$row['description']);
	
	echo json_encode($drink);

	?>

